package net.weibo.app.ui;

import net.weibo.app.AppConfig;
import net.weibo.app.AppManager;
import net.weibo.app.R;
import net.weibo.common.UIUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingActivity extends BaseActivity implements OnClickListener
{

    private TextView     tv_Info;
    private TextView     tv_InfoTitle;
    private TextView     tv_about;
    private TextView     tv_update;
    private TextView     tv_weibo;
    private TextView     tv_loginout;
    private TextView     tv_exit;
    private LinearLayout ly_functionSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView()
    {

        tv_Info = (TextView) findViewById(R.id.infoButton);
        tv_Info.setVisibility(View.GONE);

        tv_InfoTitle = (TextView) findViewById(R.id.textView1);
        tv_InfoTitle.setText(getString(R.string.setting));

        tv_about = (TextView) findViewById(R.id.tv_about);
        tv_about.setOnClickListener(this);

        tv_update = (TextView) findViewById(R.id.tv_update);
        tv_update.setOnClickListener(this);

        tv_weibo = (TextView) findViewById(R.id.tv_weibo);
        tv_weibo.setOnClickListener(this);

        tv_loginout = (TextView) findViewById(R.id.tv_loginout);
        tv_loginout.setOnClickListener(this);

        tv_exit = (TextView) findViewById(R.id.tv_exit);
        tv_exit.setOnClickListener(this);

        ly_functionSetting = (LinearLayout) findViewById(R.id.ly_functionSetting);
        ly_functionSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.infoButton:
                this.finish();
                break;
            case R.id.ly_functionSetting:
                Intent intent = new Intent(SettingActivity.this, SettingFuctionsActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_exit:
                AppManager.getAppManager().AppExit(getApplicationContext());
                break;
            case R.id.tv_loginout:
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
            default:
                break;
        }
    }

}
