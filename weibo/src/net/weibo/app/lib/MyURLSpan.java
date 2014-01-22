package net.weibo.app.lib;

import net.weibo.app.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.provider.Browser;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 */
public class MyURLSpan extends ClickableSpan implements ParcelableSpan
{

    private final String   mURL;
    private final Activity activity;

    public MyURLSpan(String url, Activity activity)
    {
        mURL = url;
        this.activity = activity;
    }

    @Override
    public int getSpanTypeId()
    {
        return 11;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(mURL);
    }

    public String getURL()
    {
        return mURL;
    }

    @Override
    public void onClick(View widget)
    {

        Uri uri = Uri.parse(getURL());
        Context context = widget.getContext();
        if (null != uri && uri.getScheme().startsWith("http"))
        {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
            context.startActivity(intent);
        }
    }

    public void onLongClick(View widget)
    {

    }

    @Override
    public void updateDrawState(TextPaint tp)
    {
        tp.setColor(activity.getResources().getColor(R.color.link));
        tp.setUnderlineText(false); // 去掉下划线

        // int[] attrs = new int[] { android.R.color.black };
        // TypedArray ta = activity.obtainStyledAttributes(attrs);
        // int drawableFromTheme = ta.getColor(0, 430);
        // tp.setColor(drawableFromTheme);
    }
}
