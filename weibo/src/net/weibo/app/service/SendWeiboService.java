package net.weibo.app.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.weibo.api.NewsImpl;
import net.weibo.app.AppContext;
import net.weibo.app.R;
import net.weibo.app.asynctask.MyAsyncTask;
import net.weibo.app.bean.Weibo;
import net.weibo.app.ui.WriteNewsActivity;
import net.weibo.common.ImageUtils;
import net.weibo.common.NotificationUtility;
import net.weibo.common.Utility;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;

/**
 * 发送微博service
 */
public class SendWeiboService extends Service
{

    private Map<WeiboSendTask, Boolean> tasksResult        = new HashMap<WeiboSendTask, Boolean>();
    private Map<WeiboSendTask, Integer> tasksNotifications = new HashMap<WeiboSendTask, Integer>();

    private Handler                     handler            = new Handler();

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        int lastNotificationId = intent.getIntExtra("lastNotificationId", -1);

        String picPath = intent.getStringExtra("picPath");
        String content = intent.getStringExtra("content");
        double longitude = intent.getDoubleExtra("longitude", 0.0);
        double latitude = intent.getDoubleExtra("latitude", 0.0);

        WeiboSendTask task = new WeiboSendTask(lastNotificationId, longitude, latitude, picPath, content);
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
        private double            longitude;
        private double            latitude;
        private BroadcastReceiver receiver;
        private PendingIntent     pendingIntent;

        private int               lastNotificationId;

        private String            picPath;
        private String            content;

        private Weibo             weibo;

        public WeiboSendTask(int lastNotificationId, double longitude, double latitude, String picPath, String content)
        {
            this.lastNotificationId = lastNotificationId;
            this.longitude = longitude;
            this.latitude = latitude;
            this.content = content;
            this.picPath = picPath;

            weibo = new Weibo();
            weibo.setContent(content);
            weibo.setLatitude(String.valueOf(latitude));
            weibo.setLongitude(String.valueOf(longitude));
            weibo.setPic_url(picPath);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            Notification.Builder builder = new Notification.Builder(SendWeiboService.this).setTicker(getString(R.string.sending)).setContentTitle(getString(R.string.sending)).setContentText(content)
                    .setOnlyAlertOnce(true).setOngoing(true).setSmallIcon(R.drawable.upload_white);

            // Notification notification = new Notification();
            // // 设置显示在手机最上边的状态栏的图标
            // notification.icon = R.drawable.upload_white;
            // // 当当前的notification被放到状态栏上的时候，提示内容
            // notification.tickerText =getString(R.string.sending);
            //
            // // 添加声音提示
            // notification.defaults=Notification.DEFAULT_SOUND;
            // // audioStreamType的值必须AudioManager中的值，代表着响铃的模式
            // notification.audioStreamType=
            // android.media.AudioManager.ADJUST_LOWER;

            if (!TextUtils.isEmpty(picPath))
            {
                builder.setProgress(0, 100, false);
            } else
            {
                builder.setProgress(0, 100, true);
            }

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

                IntentFilter intentFilter = new IntentFilter("net.weibo.app.stop" + String.valueOf(notificationId));

                registerReceiver(receiver, intentFilter);

                Intent broadcastIntent = new Intent("net.weibo.app.stop" + String.valueOf(notificationId));

                pendingIntent = PendingIntent.getBroadcast(SendWeiboService.this, 1, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
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
            result = newsImpl.writeNews(weibo);

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

                    Notification.Builder builder = new Notification.Builder(SendWeiboService.this).setTicker(getString(R.string.send_photo)).setContentTitle(getString(R.string.background_sending))
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
                    Notification.Builder builder = new Notification.Builder(SendWeiboService.this).setTicker(getString(R.string.send_photo)).setContentTitle(getString(R.string.wait_server_response))
                            .setContentText(content).setNumber(100).setProgress(100, 100, false).setOnlyAlertOnce(true).setOngoing(true).setSmallIcon(R.drawable.upload_white);

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
            Notification.Builder builder = new Notification.Builder(SendWeiboService.this).setTicker(getString(R.string.send_failed)).setContentTitle(getString(R.string.send_faile_click_to_open))
                    .setContentText(content).setOnlyAlertOnce(true).setAutoCancel(true).setSmallIcon(R.drawable.send_failed).setOngoing(false);

            Intent notifyIntent = WriteNewsActivity.startBecauseSendFailed(SendWeiboService.this, content, picPath, longitude, latitude);

            PendingIntent pendingIntent = PendingIntent.getActivity(SendWeiboService.this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(pendingIntent);

            Notification notification;
            if (Utility.isJB())
            {
                if (TextUtils.isEmpty(picPath))
                {
                    Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle(builder);
                    bigTextStyle.setBigContentTitle(getString(R.string.send_faile_click_to_open));
                    bigTextStyle.bigText(content);
                    bigTextStyle.setSummaryText(AppContext.getInstance().getProperty("Nick"));
                    builder.setStyle(bigTextStyle);
                } else
                {
                    Bitmap bitmap = ImageUtils.getBitmapByPath(picPath);
                    if (bitmap != null)
                    {
                        Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle(builder);
                        bigPictureStyle.setBigContentTitle(getString(R.string.send_faile_click_to_open));
                        bigPictureStyle.bigPicture(bitmap);
                        bigPictureStyle.setSummaryText(AppContext.getInstance().getProperty("Nick"));
                        builder.setStyle(bigPictureStyle);
                    } else
                    {
                        Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle(builder);
                        bigTextStyle.setBigContentTitle(getString(R.string.send_faile_click_to_open));
                        bigTextStyle.bigText(content);
                        bigTextStyle.setSummaryText(AppContext.getInstance().getProperty("Nick"));
                        builder.setStyle(bigTextStyle);
                    }
                }
                Intent intent = new Intent(SendWeiboService.this, WriteNewsActivity.class);
                intent.putExtra("picPath", picPath);
                intent.putExtra("content", content);
                intent.putExtra("longitude", task.longitude);
                intent.putExtra("latitude", task.latitude);

                intent.putExtra("lastNotificationId", tasksNotifications.get(task));

                PendingIntent retrySendIntent = PendingIntent.getService(SendWeiboService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
            Notification.Builder builder = new Notification.Builder(SendWeiboService.this).setTicker(getString(R.string.send_successfully)).setContentTitle(getString(R.string.send_successfully))
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
