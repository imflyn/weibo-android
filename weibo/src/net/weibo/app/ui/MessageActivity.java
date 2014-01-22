package net.weibo.app.ui;

import java.util.ArrayList;

import net.weibo.app.R;
import net.weibo.app.ui.fragment.LetterListFragment;
import net.weibo.app.ui.fragment.NewsListAtMeFragment;
import net.weibo.app.ui.fragment.NewsListVIPAtMeFragment;
import net.weibo.viewpagerindicator.IconPagerAdapter;
import net.weibo.viewpagerindicator.TabPageIndicator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;

/**
 * 消息Activity
 */
public class MessageActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initView();

    }

    private void initView()
    {

        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        MyFramgentAdapter adapter = new MyFramgentAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);
        pager.setOffscreenPageLimit(3);

        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);

    }

    class MyFramgentAdapter extends FragmentPagerAdapter implements IconPagerAdapter
    {
        private NewsListAtMeFragment    newsListAtMeFragment    = new NewsListAtMeFragment();
        private NewsListVIPAtMeFragment newsListVIPAtMeFragment = new NewsListVIPAtMeFragment();
        private LetterListFragment      letterFragment          = new LetterListFragment();
        private ArrayList<Fragment>     fragmentList;

        private int[]                   ICONS                   = new int[] { R.drawable.wb_icon_all_selector, R.drawable.wb_icon_fans_selector, R.drawable.wb_icon_letter_selector };
        private int[]                   Choose_Icons            = new int[] { R.drawable.wb_icon_all_d, R.drawable.wb_icon_fans_d, R.drawable.wb_icon_letter_d };

        public MyFramgentAdapter(FragmentManager fm)
        {
            super(fm);
            fragmentList = new ArrayList<Fragment>();
            fragmentList.add(newsListAtMeFragment);
            fragmentList.add(newsListVIPAtMeFragment);
            fragmentList.add(letterFragment);

        }

        @Override
        public int getIconResId(int index)
        {
            return ICONS[index];
        }

        @Override
        public int getChoosedIconResId(int index)
        {
            return Choose_Icons[index];
        }

        @Override
        public void destroyItem(View container, int position, Object object)
        {
            ((ViewPager) container).removeViewAt(position);
        }

        @Override
        public int getCount()
        {
            return fragmentList.size();
        }

        @Override
        public Fragment getItem(int position)
        {

            return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(position);

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            startActivity(intent);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
