package net.weibo.common;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.microedition.khronos.opengles.GL10;

import net.weibo.app.AppContext;
import net.weibo.app.BuildConfig;
import net.weibo.app.R;
import net.weibo.app.asynctask.MyAsyncTask;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.opengl.GLES10;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Utility
{

    private Utility()
    {
        // Forbidden being instantiated.
    }

    public static String encodeUrl(Map<String, String> param)
    {
        if (param == null)
        {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        Set<String> keys = param.keySet();
        boolean first = true;

        for (String key : keys)
        {
            String value = param.get(key);
            // pain...EditMyProfileDao params' values can be empty
            if (!TextUtils.isEmpty(value) || key.equals("description") || key.equals("url"))
            {
                if (first)
                    first = false;
                else
                    sb.append("&");
                try
                {
                    sb.append(URLEncoder.encode(key, "UTF-8")).append("=").append(URLEncoder.encode(param.get(key), "UTF-8"));
                } catch (UnsupportedEncodingException e)
                {

                }
            }

        }

        return sb.toString();
    }

    public static boolean isJB()
    {
        return android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * 获取栈顶activity
     * 
     * @param context
     * @return
     */
    public static String getTopActivityName(Context context)
    {
        String topActivityName = null;
        ActivityManager activityManager = (ActivityManager) (context.getSystemService(android.content.Context.ACTIVITY_SERVICE));
        List<RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null)
        {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            String topActivityClassName = f.getClassName();
            String temp[] = topActivityClassName.split("\\.");
            topActivityName = temp[temp.length - 1];
            System.out.println("topActivityName=" + topActivityName);
        }
        return topActivityName;
    }

    public static boolean isTaskStopped(MyAsyncTask task)
    {
        return task == null || task.getStatus() == MyAsyncTask.Status.FINISHED;
    }

    public static Bundle decodeUrl(String s)
    {
        Bundle params = new Bundle();
        if (s != null)
        {
            String array[] = s.split("&");
            for (String parameter : array)
            {
                String v[] = parameter.split("=");
                try
                {
                    params.putString(URLDecoder.decode(v[0], "UTF-8"), URLDecoder.decode(v[1], "UTF-8"));
                } catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();

                }
            }
        }
        return params;
    }

    public static void closeSilently(Closeable closeable)
    {
        if (closeable != null)
            try
            {
                closeable.close();
            } catch (IOException ignored)
            {

            }
    }

    /**
     * Parse a URL query and fragment parameters into a key-value bundle.
     */
    public static Bundle parseUrl(String url)
    {
        // hack to prevent MalformedURLException
        url = url.replace("https", "http");
        try
        {
            URL u = new URL(url);
            Bundle b = decodeUrl(u.getQuery());
            b.putAll(decodeUrl(u.getRef()));
            return b;
        } catch (MalformedURLException e)
        {
            return new Bundle();
        }
    }

    public static void cancelTasks(MyAsyncTask... tasks)
    {
        for (MyAsyncTask task : tasks)
        {
            if (task != null)
                task.cancel(true);
        }
    }

    public static int length(String paramString)
    {
        int i = 0;
        for (int j = 0; j < paramString.length(); j++)
        {
            if (paramString.substring(j, j + 1).matches("[Α-￥]"))
                i += 2;
            else
                i++;
        }

        if (i % 2 > 0)
        {
            i = 1 + i / 2;
        } else
        {
            i = i / 2;
        }

        return i;
    }

    public static int getScreenWidth(Activity activity)
    {
        if (activity != null)
        {
            Display display = activity.getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            return metrics.widthPixels;
        }

        return 480;
    }

    public static boolean isConnected(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean isWifi(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
            {
                return true;
            }
        }
        return false;
    }

    public static int getNetType(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            return networkInfo.getType();
        }
        return -1;
    }

    public static boolean isGprs(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            if (networkInfo.getType() != ConnectivityManager.TYPE_WIFI)
            {
                return true;
            }
        }
        return false;
    }

    public static String getPicPathFromUri(Uri uri, Activity activity)
    {
        String value = uri.getPath();

        if (value.startsWith("/external"))
        {
            String[] proj = { MediaColumns.DATA };
            Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
        {
            return value;
        }
    }

    public static boolean isAllNotNull(Object... obs)
    {
        for (int i = 0; i < obs.length; i++)
        {
            if (obs[i] == null)
            {
                return false;
            }
        }
        return true;
    }

    // public static boolean isGPSLocationCorrect(GeoBean geoBean)
    // {
    // double latitude = geoBean.getLat();
    // double longitude = geoBean.getLon();
    // if (latitude < -90.0 || latitude > 90.0)
    // {
    // return false;
    // }
    // if (longitude < -180.0 || longitude > 180.0)
    // {
    // return false;
    // }
    // return true;
    // }

    public static boolean isIntentSafe(Activity activity, Uri uri)
    {
        Intent mapCall = new Intent(Intent.ACTION_VIEW, uri);
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(mapCall, 0);
        return activities.size() > 0;
    }

    public static boolean isIntentSafe(Activity activity, Intent intent)
    {
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        return activities.size() > 0;
    }

    public static boolean isGooglePlaySafe(Activity activity)
    {
        Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.gms");
        Intent mapCall = new Intent(Intent.ACTION_VIEW, uri);
        mapCall.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        mapCall.setPackage("com.android.vending");
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(mapCall, 0);
        return activities.size() > 0;
    }

    public static String buildTabText(int number)
    {

        if (number == 0)
        {
            return null;
        }

        String num;
        if (number < 99)
        {
            num = "(" + number + ")";
        } else
        {
            num = "(99+)";
        }
        return num;

    }

    public static Rect locateView(View v)
    {
        int[] location = new int[2];
        if (v == null)
            return null;
        try
        {
            v.getLocationOnScreen(location);
        } catch (NullPointerException npe)
        {
            // Happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect locationRect = new Rect();
        locationRect.left = location[0];
        locationRect.top = location[1];
        locationRect.right = locationRect.left + v.getWidth();
        locationRect.bottom = locationRect.top + v.getHeight();
        return locationRect;
    }

    public static int countWord(String content, String word, int preCount)
    {
        int count = preCount;
        int index = content.indexOf(word);
        if (index == -1)
        {
            return count;
        } else
        {
            count++;
            return countWord(content.substring(index + word.length()), word, count);
        }
    }

    public static void buildTabCount(ActionBar.Tab tab, String tabStrRes, int count)
    {
        if (tab == null)
            return;
        String content = tab.getText().toString();
        int value = 0;
        int start = content.indexOf("(");
        int end = content.lastIndexOf(")");
        if (start > 0)
        {
            String result = content.substring(start + 1, end);
            value = Integer.valueOf(result);
        }
        if (value <= count)
        {
            tab.setText(tabStrRes + "(" + count + ")");
        }
    }

    public static void buildTabCount(TextView tab, String tabStrRes, int count)
    {
        if (tab == null)
            return;
        // String content = tab.getText().toString();
        // int value = 0;
        // int start = content.indexOf("(");
        // int end = content.lastIndexOf(")");
        // if (start > 0) {
        // String result = content.substring(start + 1, end);
        // value = Integer.valueOf(result);
        // }
        // if (value <= count) {
        tab.setText(" " + count + " " + tabStrRes);
        // }
    }

    public static String getIdFromWeiboAccountLink(String url)
    {
        String id = url.substring(19);
        id = id.replace("/", "");
        return id;
    }

    public static String getDomainFromWeiboAccountLink(String url)
    {
        String domain = url.substring(17);
        domain = domain.replace("/", "");
        return domain;
    }

    public static boolean isWeiboAccountIdLink(String url)
    {
        return !TextUtils.isEmpty(url) && url.startsWith("http://weibo.com/u/");
    }

    public static boolean isWeiboAccountDomainLink(String url)
    {
        if (TextUtils.isEmpty(url))
        {
            return false;
        } else
        {
            boolean a = url.startsWith("http://weibo.com/");
            boolean b = !url.contains("?");
            return a && b;
        }
    }

    public static void vibrate(Context context, View view)
    {
        // Vibrator vibrator = (Vibrator)
        // context.getSystemService(Context.VIBRATOR_SERVICE);
        // vibrator.vibrate(30);
        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
    }

    public static void playClickSound(View view)
    {
        view.playSoundEffect(SoundEffectConstants.CLICK);
    }

    public static View getListViewItemViewFromPosition(ListView listView, int position)
    {
        return listView.getChildAt(position - listView.getFirstVisiblePosition());
    }

    public static String getMotionEventStringName(MotionEvent event)
    {
        switch (event.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:
                return "MotionEvent.ACTION_DOWN";
            case MotionEvent.ACTION_UP:
                return "MotionEvent.ACTION_UP";
            case MotionEvent.ACTION_CANCEL:
                return "MotionEvent.ACTION_CANCEL";
            case MotionEvent.ACTION_MOVE:
                return "MotionEvent.ACTION_MOVE";
            default:
                return "Other";
        }
    }

    public static void printStackTrace(Exception e)
    {
        if (BuildConfig.DEBUG)
            e.printStackTrace();
    }

    public static String convertStateNumberToString(Context context, String numberStr)
    {
        int thousandInt = 1000;
        int tenThousandInt = thousandInt * 10;
        int number = Integer.valueOf(numberStr);
        if (number == tenThousandInt)
        {
            return String.valueOf((number / tenThousandInt) + context.getString(R.string.ten_thousand));
        }
        if (number > tenThousandInt)
        {
            String result = String.valueOf((number / tenThousandInt) + context.getString(R.string.ten_thousand));
            if (number > tenThousandInt * 10)
            {
                return result;
            }
            String thousand = String.valueOf(numberStr.charAt(numberStr.length() - 4));
            if (Integer.valueOf(thousand) != 0)
                result += thousand;
            return result;
        }
        if (number > thousandInt)
        {
            NumberFormat nf = NumberFormat.getNumberInstance();
            return nf.format(Long.valueOf(number));
        }
        return String.valueOf(number);
    }

    public static int getMaxLeftWidthOrHeightImageViewCanRead(int heightOrWidth)
    {
        // 1pixel==4bytes
        // http://stackoverflow.com/questions/13536042/android-bitmap-allocating-16-bytes-per-pixel
        // http://stackoverflow.com/questions/15313807/android-maximum-allowed-width-height-of-bitmap
        int[] maxSizeArray = new int[1];
        GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxSizeArray, 0);

        if (maxSizeArray[0] == 0)
        {
            GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxSizeArray, 0);
        }
        int maxHeight = maxSizeArray[0];
        int maxWidth = maxSizeArray[0];

        return (maxHeight * maxWidth) / heightOrWidth;
    }

    // sometime can get value, sometime can't, so I define it is 2048x2048
    public static int getBitmapMaxWidthAndMaxHeight()
    {
        int[] maxSizeArray = new int[1];
        GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxSizeArray, 0);

        if (maxSizeArray[0] == 0)
        {
            GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxSizeArray, 0);
        }
        // return maxSizeArray[0];
        return 2048;
    }

    public static void recycleViewGroupAndChildViews(ViewGroup viewGroup, boolean recycleBitmap)
    {
        for (int i = 0; i < viewGroup.getChildCount(); i++)
        {

            View child = viewGroup.getChildAt(i);

            if (child instanceof WebView)
            {
                WebView webView = (WebView) child;
                webView.loadUrl("about:blank");
                webView.stopLoading();
                continue;
            }

            if (child instanceof ViewGroup)
            {
                recycleViewGroupAndChildViews((ViewGroup) child, true);
                continue;
            }

            if (child instanceof ImageView)
            {
                ImageView iv = (ImageView) child;
                Drawable drawable = iv.getDrawable();
                if (drawable instanceof BitmapDrawable)
                {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    if (recycleBitmap && bitmap != null)
                    {
                        bitmap.recycle();
                    }
                }
                iv.setImageBitmap(null);
                iv.setBackgroundDrawable(null);
                continue;
            }

            child.setBackgroundDrawable(null);

        }

        viewGroup.setBackgroundDrawable(null);
    }

    public static int dip2px(int dipValue)
    {
        float reSize = AppContext.getInstance().getResources().getDisplayMetrics().density;
        return (int) ((dipValue * reSize) + 0.5);
    }

    public static int px2dip(int pxValue)
    {
        float reSize = AppContext.getInstance().getResources().getDisplayMetrics().density;
        return (int) ((pxValue / reSize) + 0.5);
    }

    public static float sp2px(int spValue)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, AppContext.getInstance().getResources().getDisplayMetrics());
    }
}
