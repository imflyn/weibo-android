package net.weibo.app.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.weibo.api.NewsImpl;
import net.weibo.app.AppContext;
import net.weibo.app.R;
import net.weibo.app.asynctask.MyAsyncTask;
import net.weibo.app.ui.CommentNewsActivity;
import net.weibo.app.ui.ReposeNewsActivity;
import net.weibo.common.NotificationUtility;
import net.weibo.common.Utility;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;

/**
 * 转发微博service
 */
public class RepostWeiboService extends Service
{

    private Map<WeiboSendTask, Boolean> tasksResult        = new HashMap<WeiboSendTask, Boolean>();
    private Map<WeiboSendTask, Integer> tasksNotifications = new HashMap<WeiboSendTask, Integer>();

    private Handler                     handler            = new Handler();

    public static final int             repose             = 10;
    public static final int             comment            = 11;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        int lastNotificationId = intent.getIntExtra("lastNotificationId", -1);

        String reId = intent.getStringExtra("reId");
        String content = intent.getStringExtra("content");
        int type = intent.getIntExtra("type", 0);

        WeiboSendTask task = new WeiboSendTask(lastNotificationId, reId, content, type);
        task.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);

        tasksResult.put(task, false);

        return START_REDELIVER_INTENT;
    }

    public void stopServiceIfTasksAreEnd(WeiboSendTask currentTask)
    {

        tasksResult.put(currentTask, true);

        boolean isAllTaskEnd = true;
        Set<WeiboSendTask> taskSet = tasksResult.keySet();
        for (WeiboSendTask task : taskSet)
        {
            if (!tasksResult.get(task))
            {
                isAllTaskEnd = false;
                break;
            }
        }
        if (isAllTaskEnd)
        {
            stopForeground(true);
            stopSelf();
        }
    }

    private class WeiboSendTask extends MyAsyncTask<Void, Long, Void>
    {

        private Notification      notification;
        private long              size;
        private BroadcastReceiver receiver;
        private PendingIntent     pendingIntent;

        private int               lastNotificationId;

        private String            reId;
        private String            content;

        private int               type;

        public WeiboSendTask(int lastNotificationId, String reId, String content, int type)
        {
            this.lastNotificationId = lastNotificationId;
            this.content = content;
            this.reId = reId;
            this.type = type;

        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            Notification.Builder builder = new Notification.Builder(RepostWeiboService.this).setTicker(getString(R.string.sending)).setContentTitle(getString(R.string.sending))
                    .setContentText(content).setOnlyAlertOnce(true).setOngoing(true).setSmallIcon(R.drawable.upload_white);

            int notificationId = (lastNotificationId != -1) ? lastNotificationId : new Random().nextInt(Integer.MAX_VALUE);

            if (Utility.isJB())
            {
                receiver = new BroadcastReceiver()
                {
                    @Override
                    public void onReceive(Context context, Intent intent)
                    {
                        WeiboSendTask.this.cancel(true);
                    }
                };

                IntentFilter intentFilter = new IntentFilter("net.weibo.app.reposestop" + String.valueOf(notificationId));

                registerReceiver(receiver, intentFilter);

                Intent broadcastIntent = new Intent("net.weibo.app.reposestop" + String.valueOf(notificationId));

                pendingIntent = PendingIntent.getBroadcast(RepostWeiboService.this, 1, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.addAction(R.drawable.send_failed, getString(R.string.cancle), pendingIntent);

                notification = builder.build();

            } else
            {
                notification = builder.getNotification();
            }

            NotificationUtility.show(notification, notificationId);
            tasksNotifications.put(WeiboSendTask.this, notificationId);

        }

        @Override
        protected Void doInBackground(Void... params)
        {
            boolean result = false;

            NewsImpl newsImpl = new NewsImpl();

            if (type == comment)
            {
                result = newsImpl.writeComments(content, reId);
            } else
            {
                result = newsImpl.reWriteNews(content, reId);
            }

            if (!result)
                cancel(true);
            return null;
        }

        private double lastStatus = -1d;
        private long   lastMillis = -1L;

        @Override
        protected void onProgressUpdate(Long... values)
        {

            if (values.length > 0)
            {

                long data = values[0];
                if (data != -1)
                {
                    double r = data / (double) size;

                    if (Math.abs(r - lastStatus) < 0.01d)
                    {
                        return;
                    }

                    if (System.currentTimeMillis() - lastMillis < 200L)
                    {
                        return;
                    }

                    lastStatus = r;

                    lastMillis = System.currentTimeMillis();

                    Notification.Builder builder = new Notification.Builder(RepostWeiboService.this).setTicker(getString(R.string.send_photo)).setContentTitle(getString(R.string.background_sending))
                            .setNumber((int) (r * 100)).setContentText(content).setProgress((int) size, (int) data, false).setOnlyAlertOnce(true).setOngoing(true)
                            .setSmallIcon(R.drawable.upload_white);

                    if (Utility.isJB())
                    {
                        builder.addAction(R.drawable.send_failed, getString(R.string.cancle), pendingIntent);
                        notification = builder.build();
                    } else
                    {
                        notification = builder.getNotification();
                    }
                } else
                {
                    Notification.Builder builder = new Notification.Builder(RepostWeiboService.this).setTicker(getString(R.string.send_photo))
                            .setContentTitle(getString(R.string.wait_server_response)).setContentText(content).setNumber(100).setProgress(100, 100, false).setOnlyAlertOnce(true).setOngoing(true)
                            .setSmallIcon(R.drawable.upload_white);

                    notification = builder.getNotification();
                }

                NotificationUtility.show(notification, tasksNotifications.get(WeiboSendTask.this));

            }
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            showSuccessfulNotification(WeiboSendTask.this);

            if (receiver != null)
            {
                unregisterReceiver(receiver);
            }
        }

        @Override
        protected void onCancelled(Void aVoid)
        {
            super.onCancelled(aVoid);

            showFailedNotification(WeiboSendTask.this);

            if (receiver != null)
            {
                unregisterReceiver(receiver);
            }
        }

        private void showFailedNotification(final WeiboSendTask task)
        {
            Notification.Builder builder = new Notification.Builder(RepostWeiboService.this).setTicker(getString(R.string.send_failed)).setContentTitle(getString(R.string.send_faile_click_to_open))
                    .setContentText(content).setOnlyAlertOnce(true).setAutoCancel(true).setSmallIcon(R.drawable.send_failed).setOngoing(false);

            Intent notifyIntent = ReposeNewsActivity.startBecauseSendFailed(RepostWeiboService.this, content, reId);

            PendingIntent pendingIntent = PendingIntent.getActivity(RepostWeiboService.this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(pendingIntent);

            Notification notification;
            if (Utility.isJB())
            {
                Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle(builder);
                bigTextStyle.setBigContentTitle(getString(R.string.send_faile_click_to_open));
                bigTextStyle.bigText(content);
                bigTextStyle.setSummaryText(AppContext.getInstance().getProperty("Nick"));
                builder.setStyle(bigTextStyle);

                Intent intent = null;
                if (type == comment)
                {
                    intent = new Intent(RepostWeiboService.this, CommentNewsActivity.class);
                } else
                {
                    intent = new Intent(RepostWeiboService.this, ReposeNewsActivity.class);
                }

                intent.putExtra("content", content);
                intent.putExtra("reId", reId);

                intent.putExtra("lastNotificationId", tasksNotifications.get(task));

                PendingIntent retrySendIntent = PendingIntent.getService(RepostWeiboService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.addAction(R.drawable.send_light, getString(R.string.retry_send), retrySendIntent);
                notification = builder.build();
            } else
            {
                notification = builder.getNotification();
            }

            final int id = tasksNotifications.get(task);
            NotificationUtility.show(notification, id);

            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    stopServiceIfTasksAreEnd(task);
                }
            }, 3000);
        }

        private void showSuccessfulNotification(final WeiboSendTask task)
        {
            Notification.Builder builder = new Notification.Builder(RepostWeiboService.this).setTicker(getString(R.string.send_successfully)).setContentTitle(getString(R.string.send_successfully))
                    .setOnlyAlertOnce(true).setAutoCancel(true).setSmallIcon(R.drawable.send_successfully).setOngoing(false);
            Notification notification = builder.getNotification();
            final int id = tasksNotifications.get(task);
            NotificationUtility.show(notification, id);
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    NotificationUtility.cancel(id);
                    stopServiceIfTasksAreEnd(task);
                }
            }, 3000);
        }

    }

}
