package net.weibo.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * 
 */
public class NoZoomWebView extends WebView
{
    public NoZoomWebView(Context context)
    {
        super(context);
    }

    public NoZoomWebView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public NoZoomWebView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public NoZoomWebView(Context context, AttributeSet attrs, int defStyle, boolean privateBrowsing)
    {
        super(context, attrs, defStyle, privateBrowsing);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return false;
    }

    @Override
    public boolean canScrollHorizontally(int direction)
    {
        return false;
    }
}
