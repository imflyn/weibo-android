package net.weibo.app.widget;

import net.weibo.app.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TabHost;

public class AnimationTabHostView extends TabHost
{
    private static Animation goEnter;
    private static Animation goExit;

    private static int       mTabCount;

    public AnimationTabHostView(Context context)
    {
        super(context);
        initAnimation(context);
    }

    public AnimationTabHostView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initAnimation(context);
    }

    /**
     * 初始化动画
     * 
     * @param context
     */
    private void initAnimation(Context context)
    {
        goEnter = AnimationUtils.loadAnimation(context, R.anim.activity_enter_go);
        goExit = AnimationUtils.loadAnimation(context, R.anim.activity_exit_go);
    }

    /**
     * 
     * @return 返回当前标签页的总数
     */

    public int getTabCount()
    {
        return mTabCount;
    }

    @Override
    public void addTab(TabSpec tabSpec)
    {
        mTabCount++;
        super.addTab(tabSpec);
    }

    // 重写setCurrentTab(int index) 方法，这里加入动画！关键点就在这。
    @Override
    public void setCurrentTab(int index)
    {
        // 切换前所在页的页面
        int mCurrentTabID = getCurrentTab();
        if (null != getCurrentView())
        {
            // 第一次设置 Tab 时，该值为 null。

            // 离开的页面
            // 循环时，末页到第一页(边界处理)
            if (mCurrentTabID == (mTabCount - 1) && index == 0)
            {
                getCurrentView().startAnimation(goExit);
            }
            // 循环时，首页到末页
            else if (mCurrentTabID == 0 && index == (mTabCount - 1))
            {
                getCurrentView().startAnimation(goExit);
            }
            // 切换到右边的界面，从左边离开
            else
            {
                getCurrentView().startAnimation(goExit);
            }
        }
        // 设置当前页
        super.setCurrentTab(index);
        // 当前页进来是动画
        // 循环时，末页到第一页
        if (getCurrentView() == null)
            return;
        if (mCurrentTabID == (mTabCount - 1) && index == 0)
        {
            if (getCurrentView() != null)
                getCurrentView().startAnimation(goEnter);
        }
        // 循环时，首页到末页(边界处理)
        else if (mCurrentTabID == 0 && index == (mTabCount - 1))
        {
            getCurrentView().startAnimation(goEnter);
        }
        // 切换到右边的界面，从右边进来
        else
        {
            getCurrentView().startAnimation(goEnter);
        }

    }

}
