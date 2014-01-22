package net.weibo.common;

import net.weibo.app.AppContext;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

/**
 */
public class NotificationUtility
{

    private NotificationUtility()
    {
        // Forbidden being instantiated.
    }

    public static void show(Notification notification, int id)
    {
        NotificationManager notificationManager = (NotificationManager) AppContext.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
    }

    public static void cancel(int id)
    {
        NotificationManager notificationManager = (NotificationManager) AppContext.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }

}
