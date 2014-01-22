package net.weibo.app.ui;

import java.util.List;

import net.weibo.app.AppConfig;
import net.weibo.app.AppContext;
import net.weibo.app.AppManager;
import net.weibo.app.R;
import net.weibo.app.sp.SharedPreferencesConfig;
import net.weibo.common.MemoryCache;
import net.weibo.common.UIUtils;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

public abstract class BaseActivity extends FragmentActivity
{
    AppContext                  appContext;
    // 是否允许全屏
    private boolean             allowFullScreen = true;

    // 是否允许销毁
    private boolean             allowDestroy    = true;
    private SensorManager       mSensorManager;
    private Vibrator            vibrator;
    private View                view;
    private SensorEventListener sensorelistener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        allowFullScreen = true;
        appContext = (AppContext) getApplication();
        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
    }

    public boolean isAllowFullScreen()
    {
        return allowFullScreen;
    }

    /**
     * 设置是否可以全屏
     * 
     * @param allowFullScreen
     */
    public void setAllowFullScreen(boolean allowFullScreen)
    {
        this.allowFullScreen = allowFullScreen;
    }

    public void setAllowDestroy(boolean allowDestroy)
    {
        this.allowDestroy = allowDestroy;
    }

    public void setAllowDestroy(boolean allowDestroy, View view)
    {
        this.allowDestroy = allowDestroy;
        this.view = view;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        printSceen();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (null != mSensorManager)
            mSensorManager.unregisterListener(sensorelistener);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        // 将Activity从栈中移除
        AppManager.getAppManager().finishActivity(this);
        appContext = null;
        mSensorManager = null;
        vibrator = null;
        sensorelistener = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.wblogmenu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_account_manager:
                // 网络连接判断
                if (!appContext.isNetworkConnected())
                {
                    UIUtils.ToastMessage(getApplicationContext(), "网络连接失败,无法跳转腾讯微博授权页面...");
                    AppManager.getAppManager().AppExit(getApplicationContext());
                } else
                {
                    AppConfig.getAppConfig(getApplicationContext()).delete();
                    Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    AppManager.getAppManager().AppExit(getApplicationContext());
                }
                break;
            case R.id.menu_exit:
                AppManager.getAppManager().AppExit(getApplicationContext());
                break;
            case R.id.menu_preference:
                Intent intent = new Intent(BaseActivity.this, SettingActivity.class);
                intent.putExtra("type", true);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 摇一摇截屏
     */
    private void printSceen()
    {
        if (null == mSensorManager && SharedPreferencesConfig.getInstance(getApplicationContext()).getBoolean(SharedPreferencesConfig.SP_PRINTSCREEN))
        {
            // 1获得硬件信息
            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // 2 判断当前手机是否带加速度感应器，如果不带，直接结束，不启动服务
            List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
            if (sensors != null)
                if (sensors.size() == 0)
                    return;

            sensorelistener = new SensorEventListener()
            {
                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy)
                {

                }

                // 感应器发生改变
                @Override
                public void onSensorChanged(SensorEvent event)
                {
                    if (UIUtils.isFastDoubleClick())
                        return;

                    int sensorType = event.sensor.getType();

                    // 读取摇一摇敏感值
                    int shakeSenseValue = 17;
                    // values[0]:X轴，values[1]：Y轴，values[2]：Z轴
                    float[] values = event.values;

                    if (sensorType == Sensor.TYPE_ACCELEROMETER)
                    {
                        if ((Math.abs(values[0]) > shakeSenseValue || Math.abs(values[1]) > shakeSenseValue || Math.abs(values[2]) > shakeSenseValue))
                        {
                            // 触发事件，执行打开应用行为
                            vibrator.vibrate(800);
                            UIUtils.addScreenShot(BaseActivity.this);
                        }
                    }
                }
            };

        }
        // 4注册侦听事件
        if (null != mSensorManager)
            mSensorManager.registerListener(sensorelistener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && view != null)
        {
            view.onKeyDown(keyCode, event);
            if (!allowDestroy)
            {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        MemoryCache.getMemoryCache(appContext).clearCache();
    }
}
