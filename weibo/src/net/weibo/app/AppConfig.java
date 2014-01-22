package net.weibo.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import net.weibo.app.authorize.AuthorizeContrast;
import net.weibo.common.StringUtils;
import net.weibo.dao.MyFollowersDBService;
import android.content.Context;
import android.os.Environment;

import com.tencent.weibo.oauthv2.OAuthV2;

/**
 * 应用程序配置类：用于保存用户相关信息及设置
 * 
 */
public class AppConfig
{
    private final static String TAG                     = "AppConfig";
    private final static String APP_CONFIG              = "config";
    public final static String  SAVE_IMAGE_PATH         = "save_image_path";
    public final static String  CONF_ACCESSTOKEN        = "accessToken";
    public final static String  CONF_OPENKEY            = "openkey";
    public final static String  CONF_EXPIRESIN          = "expiresIn";
    public final static String  CONF_OPENID             = "openId";
    public final static String  CONF_COOKIE             = "cookie";

    public final static String  CONF_LOAD_IMAGE         = "perf_loadimage";
    public final static String  CONF_SCROLL             = "perf_scroll";
    public final static String  CONF_HTTPS_LOGIN        = "perf_httpslogin";
    public final static String  CONF_VOICE              = "perf_voice";
    public final static String  CONF_CHECKUP            = "perf_checkup";
    public final static String  CONF_APP_UNIQUEID       = "APP_UNIQUEID";

    public final static String  MIAN_FOLDER             = "QQWeibo";
    public final static String  DEFAULT_SAVE_IMAGE_PATH = Environment.getExternalStorageDirectory() + File.separator + MIAN_FOLDER + File.separator + "imgCache";

    private static AppConfig    appConfig;
    private Context             mContext;

    public static AppConfig getAppConfig(Context context)
    {
        if (null == appConfig)
        {
            appConfig = new AppConfig();
            appConfig.mContext = context;
        }
        return appConfig;
    }

    private AppConfig()
    {

    }

    public void set(String key, String value)
    {
        Properties props = getProperties();
        props.setProperty(key, value);
        setProps(props);
    }

    public String get(String key)
    {
        Properties props = getProperties();
        return (null != props) ? props.getProperty(key) : null;
    }

    public void remove(String... key)
    {
        Properties props = getProperties();
        for (String k : key)
        {
            props.remove(k);
        }
        setProps(props);
    }

    public Properties getProperties()
    {
        FileInputStream fis = null;
        Properties props = new Properties();
        // 读取app_config目录下的config
        try
        {
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            fis = new FileInputStream(dirConf.getPath() + File.separator + APP_CONFIG);
            props.load(fis);
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                fis.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return props;
    }

    private void setProps(Properties props)
    {
        FileOutputStream fos = null;

        try
        {
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            File Conf = new File(dirConf, APP_CONFIG);
            fos = new FileOutputStream(Conf);
            props.store(fos, null);
            fos.flush();
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                fos.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }

    public String getAccountName()
    {
        String accoutName = "";
        return accoutName;
    }

    /**
     * 获取本地授权信息
     * 
     * @return
     */
    public int getAccessInfo()
    {

        // accessInfo = AccessInfo.getAccessInstance();
        // accessInfo.setAccess_token(getAccessToken());
        // accessInfo.setAccessSecret(getOpenKey());
        // accessInfo.setExpires_in(getExpiresIn());
        // accessInfo.setOpenid(getOpenId());
        // accessInfo.setOpenkey(getOpenKey());
        if (StringUtils.isEmpty(getAccessToken()))
        {
            return AuthorizeContrast.AUTHORIZE_FAIL;
        }
        if (StringUtils.isEmpty(getOpenId()))
        {
            return AuthorizeContrast.AUTHORIZE_FAIL;
        }
        if (getExpiresIn() < System.currentTimeMillis())
        {
            return AuthorizeContrast.AUTHORIZE_REFRESH;
        }
        return AuthorizeContrast.AUTHORIZE_SUCCESS;
    }

    /**
     * 设置本地授权信息
     * 
     * @return
     */
    public void setAccessInfo(OAuthV2 oAuth)
    {
        setOpenId(oAuth.getOpenid());
        setOpenKey(oAuth.getOpenkey());
        setExpiresIn(StringUtils.toLong(oAuth.getExpiresIn() + "000") + System.currentTimeMillis());
        setAccessToken(oAuth.getAccessToken());
        set("refresh_token", oAuth.getRefreshToken());

        // accessInfo = AccessInfo.getAccessInstance();
        // accessInfo.setAccess_token(getAccessToken());
        // accessInfo.setAccessSecret(getOpenKey());
        // accessInfo.setExpires_in(getExpiresIn());
        // accessInfo.setOpenid(getOpenId());
        // accessInfo.setOpenkey(getOpenKey());
    }

    public void clearDbData()
    {
        MyFollowersDBService db = new MyFollowersDBService(mContext);
        db.delete();
    }

    /**
     * 删除config文件
     */
    public void delete()
    {
        File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
        File Conf = new File(dirConf, APP_CONFIG);
        Conf.delete();
    }

    /**
     * 检查是否是第一次登录
     */
    public Boolean isFirstLogin()
    {
        return null == get("isFirstLogin") ? true : false;
    }

    public void setAccessToken(String accessToken)
    {
        set(CONF_ACCESSTOKEN, accessToken);
    }

    public String getAccessToken()
    {
        return get(CONF_ACCESSTOKEN);
    }

    public void setOpenId(String openId)
    {
        set(CONF_OPENID, openId);
    }

    public String getOpenId()
    {
        return get(CONF_OPENID);
    }

    public void setOpenKey(String openKey)
    {
        set(CONF_OPENKEY, openKey);
    }

    public String getOpenKey()
    {
        return get(CONF_OPENKEY);
    }

    public void setExpiresIn(long expiresIn)
    {
        set(CONF_EXPIRESIN, String.valueOf(expiresIn));
    }

    public long getExpiresIn()
    {
        return StringUtils.toLong(get(CONF_EXPIRESIN));
    }
}
