package net.weibo.app.widget;

import net.weibo.app.R;
import net.weibo.app.interfaces.WeiboDrawable;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 */
public class TimeLineAvatarImageView extends PerformanceImageView implements WeiboDrawable
{

    private Paint            paint             = new Paint();

    private boolean          showPressedState  = true;
    private boolean          pressed           = false;

    private static final int V_TYPE_NONE       = -1;
    private static final int V_TYPE_PERSONAL   = 0;
    private static final int V_TYPE_ENTERPRISE = 1;

    private int              vType             = V_TYPE_NONE;

    public TimeLineAvatarImageView(Context context)
    {
        this(context, null);
    }

    public TimeLineAvatarImageView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public TimeLineAvatarImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initLayout(context);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        Bitmap bitmap;
        switch (vType)
        {
            case V_TYPE_PERSONAL:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_vip);
                canvas.drawBitmap(bitmap, getWidth() - bitmap.getWidth(), getHeight() - bitmap.getHeight(), paint);
                break;
            case V_TYPE_ENTERPRISE:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_enterprise_vip);
                canvas.drawBitmap(bitmap, getWidth() - bitmap.getWidth(), getHeight() - bitmap.getHeight(), paint);
                break;
            default:
                break;
        }

        if (pressed)
        {
            canvas.drawColor(getResources().getColor(Color.TRANSPARENT));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        if (!showPressedState || !isClickable() || !isLongClickable())
            return super.onTouchEvent(event);

        switch (event.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:
                pressed = true;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                pressed = false;
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    protected void initLayout(Context context)
    {
        setPadding(5, 5, 5, 5);
    }

    @Override
    public void setImageDrawable(Drawable drawable)
    {
        super.setImageDrawable(drawable);
    }

    @Override
    public void setImageBitmap(Bitmap bm)
    {
        super.setImageBitmap(bm);
    }

    @Override
    public ImageView getImageView()
    {
        return this;
    }

    @Override
    public void setProgress(int value, int max)
    {

    }

    @Override
    public ProgressBar getProgressBar()
    {
        return null;
    }

    @Override
    public void setGifFlag(boolean value)
    {

    }

    @Override
    public void checkVerified(boolean isVip)
    {
        if (isVip)
        {
            if (!isVip)
            {
                verifiedPersonal();
            } else
            {
                verifiedEnterprise();
            }
        } else
        {
            reset();
        }
    }

    private void verifiedPersonal()
    {
        if (vType != V_TYPE_PERSONAL)
        {
            vType = V_TYPE_PERSONAL;
            invalidate();
        }

    }

    private void verifiedEnterprise()
    {
        if (vType != V_TYPE_ENTERPRISE)
        {
            vType = V_TYPE_ENTERPRISE;
            invalidate();
        }

    }

    private void reset()
    {
        if (vType != V_TYPE_NONE)
        {
            vType = V_TYPE_NONE;
            invalidate();
        }

    }

    @Override
    public void setPressesStateVisibility(boolean value)
    {
        if (showPressedState == value)
            return;
        showPressedState = value;
        invalidate();
    }
}
