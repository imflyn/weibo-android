package net.weibo.app.ui;

import net.weibo.app.R;
import net.weibo.app.widget.AnimationTabHostView;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends TabActivity implements OnCheckedChangeListener
{
    private final String                TAG             = "MainActivity";
    private RadioGroup                  mainTab;
    private RadioButton                 mt_radio_button0;
    private RadioButton                 mt_radio_button1;
    private RadioButton                 mt_radio_button2;
    private RadioButton                 mt_radio_button3;
    private static AnimationTabHostView mTabHost;
    // 内容Intent
    private static Intent               mHomeIntent;
    private static Intent               mMssageeIntent;
    private static Intent               mSettingIntent;
    private static Intent               mInfoIntent;

    private final static String         TAB_TAG_HOME    = "tab_tag_home";
    private final static String         TAB_TAG_MESSAGE = "tab_tag_message";
    private final static String         TAB_TAG_SQUARE  = "tab_tag_square";
    private final static String         TAB_TAG_INFO    = "tab_tag_info";

    private static int                  currentIndex    = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        prepare();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        currentIndex = mTabHost.getCurrentTab();

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int index)
    {
        radioButtonChange(index);
        switch (index)
        {
            case R.id.mt_radio_button0:
                break;
            case R.id.mt_radio_button1:
                break;
            case R.id.mt_radio_button2:
                break;
            case R.id.mt_radio_button3:
                break;
            default:
                break;
        }
    }

    /**
     * 构建TabHost的Tab页
     * 
     * @param tag
     *            标记
     * @param resLabel
     *            标签
     * @param resIcon
     *            图标
     * @param content
     *            该tab展示的内容
     * @return 一个tab
     */
    private void buildTabSpec(TabHost tabHost, String tag, int resLabel, final Intent content)
    {

        TabSpec tabSpec = tabHost.newTabSpec(tag);
        tabSpec.setIndicator(getString(resLabel), null).setContent(content);
        tabHost.addTab(tabSpec);
    }

    private void initView()
    {

        mainTab = (RadioGroup) findViewById(R.id.main_tab);
        mainTab.setOnCheckedChangeListener(this);
        mt_radio_button0 = (RadioButton) mainTab.findViewById(R.id.mt_radio_button0);
        mt_radio_button1 = (RadioButton) mainTab.findViewById(R.id.mt_radio_button1);
        mt_radio_button2 = (RadioButton) mainTab.findViewById(R.id.mt_radio_button2);
        mt_radio_button3 = (RadioButton) mainTab.findViewById(R.id.mt_radio_button3);

    }

    private void prepare()
    {
        mHomeIntent = new Intent(this, HomeActivity.class);
        mMssageeIntent = new Intent(this, MessageActivity.class);
        mSettingIntent = new Intent(this, SettingActivity.class);
        mInfoIntent = new Intent(this, InfoActivity.class);

        mTabHost = (AnimationTabHostView) getTabHost();
        TabHost localTabHost = mTabHost;

        buildTabSpec(localTabHost, TAB_TAG_HOME, R.string.main_home, mHomeIntent);
        buildTabSpec(localTabHost, TAB_TAG_MESSAGE, R.string.main_message, mMssageeIntent);
        buildTabSpec(localTabHost, TAB_TAG_SQUARE, R.string.setting, mInfoIntent);
        buildTabSpec(localTabHost, TAB_TAG_INFO, R.string.main_info, mSettingIntent);

        try
        {
            mTabHost.setCurrentTab(currentIndex);
        } catch (Exception e)
        {

        }
    }

    private void radioButtonChange(int index)
    {
        switch (index)
        {
            case R.id.mt_radio_button0:
                MainActivity.mTabHost.setCurrentTabByTag(TAB_TAG_HOME);
                mt_radio_button0.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.wb_icon_tabbar_main_hl), null, null);
                mt_radio_button1.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.wb_icon_tabbar_at_nor), null, null);
                mt_radio_button2.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.wb_icon_tabbar_data_nor), null, null);
                mt_radio_button3.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.wb_icon_tabbar_search_nor), null, null);
                break;
            case R.id.mt_radio_button1:
                MainActivity.mTabHost.setCurrentTabByTag(TAB_TAG_MESSAGE);
                mt_radio_button0.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.wb_icon_tabbar_main_nor), null, null);
                mt_radio_button1.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.wb_icon_tabbar_at_hl), null, null);
                mt_radio_button2.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.wb_icon_tabbar_data_nor), null, null);
                mt_radio_button3.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.wb_icon_tabbar_search_nor), null, null);
                break;
            case R.id.mt_radio_button2:
                MainActivity.mTabHost.setCurrentTabByTag(TAB_TAG_SQUARE);
                mt_radio_button0.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.wb_icon_tabbar_main_nor), null, null);
                mt_radio_button1.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.wb_icon_tabbar_at_nor), null, null);
                mt_radio_button2.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.wb_icon_tabbar_data_hl), null, null);
                mt_radio_button3.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.wb_icon_tabbar_search_nor), null, null);

                break;
            case R.id.mt_radio_button3:
                MainActivity.mTabHost.setCurrentTabByTag(TAB_TAG_INFO);
                mt_radio_button0.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.wb_icon_tabbar_main_nor), null, null);
                mt_radio_button1.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.wb_icon_tabbar_at_nor), null, null);
                mt_radio_button2.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.wb_icon_tabbar_data_nor), null, null);
                mt_radio_button3.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.wb_icon_tabbar_search_hl), null, null);
                break;
            default:
                break;
        }
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

}
