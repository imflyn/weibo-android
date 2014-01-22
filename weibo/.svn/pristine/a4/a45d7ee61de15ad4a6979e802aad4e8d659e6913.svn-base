package net.weibo.app;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import net.weibo.app.authorize.AuthorizeContrast;
import net.weibo.common.StringUtils;
import net.weibo.constant.SmileyMap;
import net.weibo.http.RequestParams;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 */
public class AppContext extends Application
{
    public static final int         NETTYPE_WIFI         = 0x01;
    public static final int         NETTYPE_CMWAP        = 0x02;
    public static final int         NETTYPE_CMNET        = 0x03;

    public static final int         PAGE_SIZE            = 20;                                 // 默认分页大小

    private String                  saveImagePath;                                             // 保存图片路径
    private static RequestParams    params               = null;
    private static AppContext       appContext;

    private static boolean          DEVELOPER_MODE       = true;
    public static boolean           HARDWARE_ACCELERATED = false;

    private HashMap<String, Bitmap> emotionsPic          = new LinkedHashMap<String, Bitmap>();

    @Override
    public void onCreate()
    {
        // if
        // (DEVELOPER_MODE&&Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
        // {
        // StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
        // .detectDiskReads()
        // .detectDiskWrites()
        // .detectNetwork() // or .detectAll() for all detectable problems
        // .penaltyLog()
        // .build());
        // StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
        // .detectLeakedSqlLiteObjects()
        // .detectLeakedClosableObjects()
        // .penaltyLog()
        // .build());
        // }
        super.onCreate();
        appContext = this;
        // 注册App异常崩溃处理器
        // Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
        init();

    }

    public static AppContext getInstance()
    {
        return appContext;
    }

    public RequestParams getRequestParams()
    {
        if (null == params)
        {
            params = new RequestParams();
            AppConfig appConfig = AppConfig.getAppConfig(this);
            params.put("oauth_consumer_key", AuthorizeContrast.clientId);
            params.put("access_token", appConfig.get("accessToken"));
            params.put("openid", appConfig.get("openId"));
            params.put("clientip", getLocalIpAddress());
            params.put("oauth_version", "2.a");
            params.put("scope", "all");
            params.put("format", "json");
        }
        return params;
    }

    /**
     * 初始化
     */
    private void init()
    {
        // 设置保存图片的路径
        saveImagePath = getProperty(AppConfig.SAVE_IMAGE_PATH);
        if (StringUtils.isEmpty(saveImagePath))
        {
            setProperty(AppConfig.SAVE_IMAGE_PATH, AppConfig.DEFAULT_SAVE_IMAGE_PATH);
            saveImagePath = AppConfig.DEFAULT_SAVE_IMAGE_PATH;
        }
        // 硬件加速
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            HARDWARE_ACCELERATED = true;

    }

    public synchronized Map<String, Bitmap> getEmotionsPics()
    {
        if (emotionsPic != null && emotionsPic.size() > 0)
        {
            return emotionsPic;
        } else
        {
            getEmotionsTask();
            return emotionsPic;
        }
    }

    /**
     * 初始化表情
     */
    private void getEmotionsTask()
    {
        HashMap<String, String> emotions = SmileyMap.getInstance().get();
        List<String> index = new ArrayList<String>();
        index.addAll(emotions.keySet());
        for (String str : index)
        {
            String name = emotions.get(str);
            AssetManager assetManager = getApplicationContext().getAssets();
            InputStream inputStream;
            try
            {
                inputStream = assetManager.open(name);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (bitmap != null)
                {
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 30, 30, true);
                    if (bitmap != scaledBitmap)
                    {
                        bitmap.recycle();
                        bitmap = scaledBitmap;
                    }
                    emotionsPic.put(str, bitmap);
                }
            } catch (IOException ignored)
            {

            }
        }
    }

    /**
     * 检测网络是否可用
     * 
     * @return
     */
    public boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 获取App安装包信息
     * 
     * @return
     */
    public PackageInfo getPackageInfo()
    {
        PackageInfo info = null;

        try
        {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (Exception e)
        {
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 获取本机IP
     * 
     * @return
     */
    public String getLocalIpAddress()
    {
        try
        {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
            {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress())
                    {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex)
        {
            Log.e("WifiPreference IpAddress", ex.toString());
        }
        return null;
    }

    /**
     * 获取App唯一标识
     * 
     * @return
     */
    public String getAppId()
    {
        String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
        if (StringUtils.isEmpty(uniqueID))
        {
            uniqueID = UUID.randomUUID().toString();
            setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
        }
        return uniqueID;
    }

    /**
     * 获取内存中保存图片的路径
     * 
     * @return
     */
    public String getSaveImagePath()
    {
        return saveImagePath;
    }

    /**
     * 设置内存中保存图片的路径
     * 
     * @return
     */
    public void setSaveImagePath(String saveImagePath)
    {
        this.saveImagePath = saveImagePath;
    }

    /**
     * 清除保存的缓存
     */
    public void cleanCookie()
    {
        removeProperty(AppConfig.CONF_COOKIE);
    }

    public boolean containsProperty(String key)
    {
        Properties props = getProperties();
        return props.containsKey(key);
    }

    public Properties getProperties()
    {
        return AppConfig.getAppConfig(this).getProperties();
    }

    public void setProperty(String key, String value)
    {
        AppConfig.getAppConfig(this).set(key, value);
    }

    public String getProperty(String key)
    {
        return AppConfig.getAppConfig(this).get(key);
    }

    public void removeProperty(String... keys)
    {
        AppConfig.getAppConfig(this).remove(keys);
    }
}
