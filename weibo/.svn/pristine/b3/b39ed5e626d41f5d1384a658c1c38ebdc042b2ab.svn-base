package net.weibo.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView
{

    private GestureDetector  mGestureDetector;
    private static final int SWIPE_MIN_DISTANCE       = 120;
    private static final int SWIPE_MAX_OFF_PATH       = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private Context          context;

    public MyScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
        mGestureDetector = new GestureDetector(new YScrollDetecotr());
        setFadingEdgeLength(0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }

    class YScrollDetecotr extends SimpleOnGestureListener
    {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            return true;
        }
    }
}
