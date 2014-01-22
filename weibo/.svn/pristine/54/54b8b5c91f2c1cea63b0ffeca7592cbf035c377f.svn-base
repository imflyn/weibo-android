package net.weibo.app.sp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 程序配置SharedPreferences工具类
 * 
 */
public class SharedPreferencesConfig
{

    private static final String             SHAREDPREFERENCES_NAME = "SYSTEM_CONFIG";
    public static final String              SP_SOUNDALARM          = "soundAlarm";
    public static final String              SP_ROCKALARM           = "rockAlarm";
    public static final String              SP_AUTO_LOAD           = "auto_load";
    public static final String              SP_ALLOW_LOCATION      = "allow_location";
    public static final String              SP_PRINTSCREEN         = "printScreen";
    public static final String              SP_WATERMARK           = "watermark";

    private static SharedPreferencesConfig  instance;
    private static SharedPreferences.Editor editor;
    private static SharedPreferences        sp;

    private void commit()
    {
        editor.commit();
    }

    private void clear()
    {
        editor.clear();
    }

    private void removeAll()
    {
        editor.clear();
        editor.commit();
    }

    private SharedPreferencesConfig(Context context)
    {
        sp = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_APPEND);
        editor = sp.edit();
    }

    public static synchronized SharedPreferencesConfig getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new SharedPreferencesConfig(context);
        }
        return instance;
    }

    public void setBoolean(String key, boolean bol)
    {
        editor.putBoolean(key, bol);
        commit();
    }

    public void setString(String key, String str)
    {
        editor.putString(key, str);
        commit();
    }

    public String getString(String key)
    {
        return sp.getString(key, "");
    }

    public boolean getBoolean(String key)
    {
        return sp.getBoolean(key, false);
    }

}
