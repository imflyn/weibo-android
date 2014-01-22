package net.weibo.common;

import java.io.File;
import java.io.IOException;

import net.weibo.app.AppManager;
import net.weibo.app.R;
import net.weibo.app.ui.ImageDialog;
import net.weibo.app.ui.ImageZoomDialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Toast;

public class UIUtils
{
    private static int  SCREEN_WIDTH  = 0;
    private static int  SCREEN_HEIGHT = 0;
    private static long lastClickTime;

    /**
     * 弹出Toast消息
     * 
     * @param msg
     */
    public static void ToastMessage(Context cont, String msg)
    {
        Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
    }

    public static void ToastMessage(Context cont, int msg)
    {
        Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
    }

    public static void ToastMessage(Context cont, String msg, int time)
    {
        Toast.makeText(cont, msg, time).show();
    }

    /**
     * 发送App异常崩溃报告
     * 
     * @param cont
     * @param crashReport
     */
    public static void sendAppCrashReport(final Context cont, final String crashReport)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle(R.string.app_error);
        builder.setMessage(R.string.app_error_message);
        builder.setPositiveButton(R.string.submit_report, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                // 发送异常报告
                Intent i = new Intent(Intent.ACTION_SEND);
                // i.setType("text/plain"); //模拟器
                i.setType("message/rfc822"); // 真机
                i.putExtra(Intent.EXTRA_EMAIL, new String[] { "imflyn@163.com" });
                i.putExtra(Intent.EXTRA_SUBJECT, "腾讯微博客户端 - 错误报告");
                i.putExtra(Intent.EXTRA_TEXT, crashReport);
                cont.startActivity(Intent.createChooser(i, "发送错误报告"));
                // 退出
                AppManager.getAppManager().AppExit(cont);
            }
        });
        builder.setNegativeButton(R.string.sure, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                // 退出
                AppManager.getAppManager().AppExit(cont);
            }
        });
        builder.show();
    }

    /**
     * 获得屏幕宽度和高度
     * 
     * @param context
     * @return
     */
    public static int[] getScreenDisplayMetrics(Context context)
    {
        int[] screenDisplayMetrics = new int[] { SCREEN_WIDTH, SCREEN_HEIGHT };
        if (SCREEN_WIDTH == 0 || SCREEN_HEIGHT == 0)
        {

            DisplayMetrics dm = new DisplayMetrics();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(dm);
            SCREEN_WIDTH = dm.widthPixels;
            SCREEN_HEIGHT = dm.heightPixels;
            screenDisplayMetrics[0] = SCREEN_WIDTH;
            screenDisplayMetrics[1] = SCREEN_HEIGHT;
        }

        return screenDisplayMetrics;

    }

    /**
     * 设置屏幕比例
     * 
     * @param context
     * @param view
     * @param toHeight
     * @param toWidth
     */
    public static void setScreenDisplayMetrics(Context context, View view, Integer toHeight, Integer toWidth)
    {
        if (SCREEN_WIDTH == 0 || SCREEN_HEIGHT == 0)
        {
            getScreenDisplayMetrics(context);
        }

        LayoutParams layoutParams = view.getLayoutParams();

        if (null != toHeight)
        {
            layoutParams.height = Math.round((SCREEN_HEIGHT * toHeight) / 100);
        }
        if (null != toWidth)
        {
            layoutParams.width = Math.round((SCREEN_WIDTH * toWidth) / 100);
        }
        view.setLayoutParams(layoutParams);
    }

    /**
     * 退出对话框
     * 
     * @param context
     */
    public static void exitDialog(final Context context)
    {
        AlertDialog.Builder builder = new Builder(context);
        builder.setMessage("确定要退出微博客户端吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                AppManager.getAppManager().AppExit(context);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 网络设置提示对话框
     * 
     * @param context
     */
    public static void showSetNetworkUI(final Context context)
    {
        // 提示对话框
        AlertDialog.Builder builder = new Builder(context);
        builder.setTitle("网络设置提示").setMessage("网络连接不可用,是否进行设置?").setPositiveButton("设置", new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = null;
                // 判断手机系统的版本 即API大于10 就是3.0或以上版本
                if (android.os.Build.VERSION.SDK_INT > 10)
                {
                    intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                } else
                {
                    intent = new Intent();
                    ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                context.startActivity(intent);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                dialog.dismiss();
            }
        }).show();
    }

    /**
     * 显示图片对话框
     * 
     * @param context
     * @param imgUrl
     */
    public static void showImageDialog(Context context, String imgUrl)
    {
        Intent intent = new Intent(context, ImageDialog.class);
        intent.putExtra("img_url", imgUrl);
        context.startActivity(intent);
    }

    /**
     * 显示图片对话框
     * 
     * @param context
     * @param imgUrl
     */
    public static void showImageZoomDialog(Context context, String imgUrl)
    {
        Intent intent = new Intent(context, ImageZoomDialog.class);
        intent.putExtra("img_url", imgUrl);
        context.startActivity(intent);
    }

    /**
     * 网络繁忙视图
     */
    public static View getNetBusyView(Activity activity)
    {
        View view = LayoutInflater.from(activity).inflate(R.layout.view_netbusy, null);
        return view;
    }

    /**
     * 检测是否为连续点击一个按钮
     */
    public static boolean isFastDoubleClick()
    {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1000)
        {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 添加截屏功能
     */
    public static void addScreenShot(final Activity context)
    {
        // View是你需要截图的View
        View view = null;
        view = context.getParent().getWindow().getDecorView();

        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        int[] screenDisplayMetrics = getScreenDisplayMetrics(context.getApplicationContext());
        int width = screenDisplayMetrics[0];
        int height = screenDisplayMetrics[1];

        // 获取状态栏高度
        Rect frame = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        Bitmap bmp = Bitmap.createBitmap(b1, 0, 0, width, height);
        view.destroyDrawingCache();

        final String name = ImageUtils.getTempFileName();
        try
        {
            ImageUtils.saveImageToSD(context.getApplicationContext(), ImageUtils.SDCARD_MNT + File.separator + ImageUtils.SCREEN_PIC_SAVEPATH + File.separator + name + ".jpg", bmp, 100);
            ToastMessage(context.getApplicationContext(), "图片已保存在 " + ImageUtils.SDCARD_MNT + File.separator + ImageUtils.SCREEN_PIC_SAVEPATH + File.separator + name + ".jpg");
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 控件设置背景图片
     * 
     * @param view
     * @param context
     * @param resourceId
     */
    public static void setBackGroundDrawable(View view, Context context, int resourceId)
    {
        view.setBackgroundDrawable(context.getResources().getDrawable(resourceId));
    }

}
