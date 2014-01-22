package net.weibo.app.ui;

import net.weibo.app.AppConfig;
import net.weibo.app.R;
import net.weibo.app.sp.SharedPreferencesConfig;
import net.weibo.app.widget.LoadingDialog;
import net.weibo.common.FileUtils;
import net.weibo.common.UIUtils;
import net.weibo.constant.Tids;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class SettingFuctionsActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener
{

    private TextView tv_back;
    private TextView tv_title;

    private TextView tv_soundAlarm;
    private TextView tv_rockAlarm;
    private TextView tv_auto_load;
    private TextView tv_allow_location;
    private TextView tv_printScreen;
    private TextView tv_watermark;
    private TextView tv_clearCache;

    private CheckBox cb_soundAlarm;
    private CheckBox cb_rockAlarm;
    private CheckBox cb_auto_load;
    private CheckBox cb_allow_location;
    private CheckBox cb_printScreen;
    private CheckBox cb_watermark;
    private Dialog   dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_functions);
        initView();
    }

    private Handler handler = new Handler()
                            {
                                @Override
                                public void handleMessage(android.os.Message msg)
                                {
                                    if (null != dialog)
                                    {
                                        dialog.dismiss();
                                        dialog = null;
                                    }
                                    switch (msg.what)
                                    {
                                        case Tids.T_SUCCESS:
                                            UIUtils.ToastMessage(getApplicationContext(), "清除缓存成功!");
                                            break;
                                        case Tids.T_FAIL:
                                            UIUtils.ToastMessage(getApplicationContext(), "清除缓存失败!");
                                            break;
                                        default:
                                            break;
                                    }
                                };
                            };

    @Override
    protected void onResume()
    {
        super.onResume();
        initData();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        handler = null;
        dialog = null;
    }

    private void initData()
    {
        if (SharedPreferencesConfig.getInstance(getApplicationContext()).getBoolean(SharedPreferencesConfig.SP_SOUNDALARM))
            cb_soundAlarm.setChecked(true);
        if (SharedPreferencesConfig.getInstance(getApplicationContext()).getBoolean(SharedPreferencesConfig.SP_ROCKALARM))
            cb_rockAlarm.setChecked(true);
        if (SharedPreferencesConfig.getInstance(getApplicationContext()).getBoolean(SharedPreferencesConfig.SP_AUTO_LOAD))
            cb_auto_load.setChecked(true);
        if (SharedPreferencesConfig.getInstance(getApplicationContext()).getBoolean(SharedPreferencesConfig.SP_ALLOW_LOCATION))
            cb_allow_location.setChecked(true);
        if (SharedPreferencesConfig.getInstance(getApplicationContext()).getBoolean(SharedPreferencesConfig.SP_PRINTSCREEN))
            cb_printScreen.setChecked(true);
        if (SharedPreferencesConfig.getInstance(getApplicationContext()).getBoolean(SharedPreferencesConfig.SP_WATERMARK))
            cb_watermark.setChecked(true);
        if (dialog == null)
        {
            dialog = new LoadingDialog(this);
        }
    }

    private void initView()
    {
        tv_back = (TextView) findViewById(R.id.infoButton);
        tv_back.setOnClickListener(this);
        tv_back.setText(getString(R.string.back));
        tv_title = (TextView) findViewById(R.id.textView1);
        tv_title.setText(getString(R.string.function_setting));
        tv_soundAlarm = (TextView) findViewById(R.id.tv_soundAlarm);
        tv_rockAlarm = (TextView) findViewById(R.id.tv_rockAlarm);
        tv_auto_load = (TextView) findViewById(R.id.tv_auto_load);
        tv_allow_location = (TextView) findViewById(R.id.tv_allow_location);
        tv_printScreen = (TextView) findViewById(R.id.tv_printScreen);
        tv_watermark = (TextView) findViewById(R.id.tv_watermark);
        tv_clearCache = (TextView) findViewById(R.id.tv_clearCache);

        tv_soundAlarm.setOnClickListener(this);
        tv_rockAlarm.setOnClickListener(this);
        tv_allow_location.setOnClickListener(this);
        tv_printScreen.setOnClickListener(this);
        tv_clearCache.setOnClickListener(this);
        tv_watermark.setOnClickListener(this);
        tv_auto_load.setOnClickListener(this);

        cb_soundAlarm = (CheckBox) findViewById(R.id.cb_soundAlarm);
        cb_rockAlarm = (CheckBox) findViewById(R.id.cb_rockAlarm);
        cb_auto_load = (CheckBox) findViewById(R.id.cb_auto_load);
        cb_allow_location = (CheckBox) findViewById(R.id.cb_allow_location);
        cb_printScreen = (CheckBox) findViewById(R.id.cb_printScreen);
        cb_watermark = (CheckBox) findViewById(R.id.cb_watermark);

        cb_soundAlarm.setOnCheckedChangeListener(this);
        cb_rockAlarm.setOnCheckedChangeListener(this);
        cb_auto_load.setOnCheckedChangeListener(this);
        cb_allow_location.setOnCheckedChangeListener(this);
        cb_printScreen.setOnCheckedChangeListener(this);
        cb_watermark.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.infoButton:
                this.finish();
                break;
            case R.id.tv_soundAlarm:
                if (!cb_soundAlarm.isChecked())
                {
                    cb_soundAlarm.setChecked(true);
                    cb_soundAlarm.setButtonDrawable(R.drawable.wb_checkbox_1);
                    SharedPreferencesConfig.getInstance(getApplicationContext()).setBoolean(SharedPreferencesConfig.SP_SOUNDALARM, true);
                } else
                {
                    cb_soundAlarm.setChecked(false);
                    cb_soundAlarm.setButtonDrawable(R.drawable.wb_checkbox_0);
                    SharedPreferencesConfig.getInstance(getApplicationContext()).setBoolean(SharedPreferencesConfig.SP_SOUNDALARM, false);
                }
                break;
            case R.id.tv_rockAlarm:
                if (!cb_rockAlarm.isChecked())
                {
                    cb_rockAlarm.setChecked(true);
                    cb_rockAlarm.setButtonDrawable(R.drawable.wb_checkbox_1);
                    SharedPreferencesConfig.getInstance(getApplicationContext()).setBoolean(SharedPreferencesConfig.SP_ROCKALARM, true);
                } else
                {
                    cb_rockAlarm.setChecked(false);
                    cb_rockAlarm.setButtonDrawable(R.drawable.wb_checkbox_0);
                    SharedPreferencesConfig.getInstance(getApplicationContext()).setBoolean(SharedPreferencesConfig.SP_ROCKALARM, false);
                }
                break;
            case R.id.tv_auto_load:
                if (!cb_auto_load.isChecked())
                {
                    cb_auto_load.setChecked(true);
                    cb_auto_load.setButtonDrawable(R.drawable.wb_checkbox_1);
                    SharedPreferencesConfig.getInstance(getApplicationContext()).setBoolean(SharedPreferencesConfig.SP_AUTO_LOAD, true);
                } else
                {
                    cb_auto_load.setChecked(false);
                    cb_auto_load.setButtonDrawable(R.drawable.wb_checkbox_0);
                    SharedPreferencesConfig.getInstance(getApplicationContext()).setBoolean(SharedPreferencesConfig.SP_AUTO_LOAD, false);
                }
                break;
            case R.id.tv_allow_location:
                if (!cb_allow_location.isChecked())
                {
                    cb_allow_location.setChecked(true);
                    cb_allow_location.setButtonDrawable(R.drawable.wb_checkbox_1);
                    SharedPreferencesConfig.getInstance(getApplicationContext()).setBoolean(SharedPreferencesConfig.SP_ALLOW_LOCATION, true);
                } else
                {
                    cb_allow_location.setChecked(false);
                    cb_allow_location.setButtonDrawable(R.drawable.wb_checkbox_0);
                    SharedPreferencesConfig.getInstance(getApplicationContext()).setBoolean(SharedPreferencesConfig.SP_ALLOW_LOCATION, false);
                }
                break;
            case R.id.tv_printScreen:
                if (!cb_printScreen.isChecked())
                {
                    cb_printScreen.setChecked(true);
                    cb_printScreen.setButtonDrawable(R.drawable.wb_checkbox_1);
                    SharedPreferencesConfig.getInstance(getApplicationContext()).setBoolean(SharedPreferencesConfig.SP_PRINTSCREEN, true);
                } else
                {
                    cb_printScreen.setChecked(false);
                    cb_printScreen.setButtonDrawable(R.drawable.wb_checkbox_0);
                    SharedPreferencesConfig.getInstance(getApplicationContext()).setBoolean(SharedPreferencesConfig.SP_PRINTSCREEN, false);
                }
                break;
            case R.id.tv_watermark:
                if (!cb_watermark.isChecked())
                {
                    cb_watermark.setChecked(true);
                    cb_watermark.setButtonDrawable(R.drawable.wb_checkbox_1);
                    SharedPreferencesConfig.getInstance(getApplicationContext()).setBoolean(SharedPreferencesConfig.SP_WATERMARK, true);
                } else
                {
                    cb_watermark.setChecked(false);
                    cb_watermark.setButtonDrawable(R.drawable.wb_checkbox_0);
                    SharedPreferencesConfig.getInstance(getApplicationContext()).setBoolean(SharedPreferencesConfig.SP_WATERMARK, false);
                }
                break;
            case R.id.tv_clearCache:
                if (UIUtils.isFastDoubleClick())
                    break;
                if (dialog != null)
                    dialog.show();
                tv_clearCache.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (FileUtils.deleteDirectory(AppConfig.DEFAULT_SAVE_IMAGE_PATH))
                            handler.obtainMessage(Tids.T_SUCCESS).sendToTarget();
                        else
                            handler.obtainMessage(Tids.T_FAIL).sendToTarget();
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        switch (buttonView.getId())
        {
            case R.id.cb_soundAlarm:
                if (isChecked)
                {
                    cb_soundAlarm.setChecked(true);
                    cb_soundAlarm.setButtonDrawable(R.drawable.wb_checkbox_1);
                    SharedPreferencesConfig.getInstance(getApplicationContext()).setBoolean(SharedPreferencesConfig.SP_SOUNDALARM, true);
                } else
                {
                    cb_soundAlarm.setChecked(false);
                    cb_soundAlarm.setButtonDrawable(R.drawable.wb_checkbox_0);
                    SharedPreferencesConfig.getInstance(getApplicationContext()).setBoolean(SharedPreferencesConfig.SP_SOUNDALARM, false);
                }
                break;
            case R.id.cb_rockAlarm:
                if (isChecked)
                {
                    cb_rockAlarm.setChecked(true);
                    cb_rockAlarm.setButtonDrawable(R.drawable.wb_checkbox_1);
                    SharedPreferencesConfig.getInstance(getApplicationContext()).setBoolean(SharedPreferencesConfig.SP_ROCKALARM, true);
                } else
                {
                    cb_rockAlarm.setChecked(false);
                    cb_rockAlarm.setButtonDrawable(R.drawable.wb_checkbox_0);
                    SharedPreferencesConfig.getInstance(getApplicationContext()).setBoolean(SharedPreferencesConfig.SP_ROCKALARM, false);
                }
                break;
            case R.id.cb_auto_load:
                if (isChecked)
                {
                    cb_auto_load.setChecked(true);
                    cb_auto_load.setButtonDrawable(R.drawable.wb_checkbox_1);
                    SharedPreferencesConfig.getInstance(getApplicationContext()).setBoolean(SharedPreferencesConfig.SP_AUTO_LOAD, true);
                } else
                {
                    cb_auto_load.setChecked(false);
                    cb_auto_load.setButtonDrawable(R.drawable.wb_checkbox_0);
                    SharedPreferencesConfig.getInstance(getApplicationContext()).setBoolean(SharedPreferencesConfig.SP_AUTO_LOAD, false);
                }
                break;
            case R.id.cb_allow_location:
                if (isChecked)
                {
                    cb_allow_location.setChecked(true);
                    cb_allow_location.setButtonDrawable(R.drawable.wb_checkbox_1);
                    SharedPreferencesConfig.getInstance(getApplicationContext()).setBoolean(SharedPreferencesConfig.SP_ALLOW_LOCATION, true);
                } else
                {
                    cb_allow_location.setChecked(false);
                    cb_allow_location.setButtonDrawable(R.drawable.wb_checkbox_0);
                    SharedPreferencesConfig.getInstance(getApplicationContext()).setBoolean(SharedPreferencesConfig.SP_ALLOW_LOCATION, false);
                }
                break;
            case R.id.cb_printScreen:
                if (isChecked)
                {
                    cb_printScreen.setChecked(true);
                    cb_printScreen.setButtonDrawable(R.drawable.wb_checkbox_1);
                    SharedPreferencesConfig.getInstance(getApplicationContext()).setBoolean(SharedPreferencesConfig.SP_PRINTSCREEN, true);
                } else
                {
                    cb_printScreen.setChecked(false);
                    cb_printScreen.setButtonDrawable(R.drawable.wb_checkbox_0);
                    SharedPreferencesConfig.getInstance(getApplicationContext()).setBoolean(SharedPreferencesConfig.SP_PRINTSCREEN, false);
                }
                break;
            case R.id.cb_watermark:
                if (isChecked)
                {
                    cb_watermark.setChecked(true);
                    cb_watermark.setButtonDrawable(R.drawable.wb_checkbox_1);
                    SharedPreferencesConfig.getInstance(getApplicationContext()).setBoolean(SharedPreferencesConfig.SP_WATERMARK, true);
                } else
                {
                    cb_watermark.setChecked(false);
                    cb_watermark.setButtonDrawable(R.drawable.wb_checkbox_0);
                    SharedPreferencesConfig.getInstance(getApplicationContext()).setBoolean(SharedPreferencesConfig.SP_WATERMARK, false);
                }
                break;
        }
    }

}
