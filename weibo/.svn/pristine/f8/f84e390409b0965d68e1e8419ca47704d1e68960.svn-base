package net.weibo.app.widget;

import net.weibo.app.R;
import net.weibo.app.sp.SharedPreferencesConfig;
import net.weibo.common.SoundTool;
import net.weibo.common.Utility;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Toast提示控件(带音乐播放)
 * 
 */
public class MyToast extends Toast
{

    private boolean play;

    public MyToast(Context context)
    {
        super(context);

        this.play = SharedPreferencesConfig.getInstance(context).getBoolean(SharedPreferencesConfig.SP_SOUNDALARM);

    }

    @Override
    public void show()
    {
        super.show();

        if (play)
        {
            SoundTool.getInstance().play(SoundTool.NEWS_REFRESH);
        }
    }

    /**
     * 获取控件实例
     * 
     * @param context
     * @param text
     *            提示消息
     * @param isSound
     *            是否播放声音
     * @return
     */
    public static void show(Context context, CharSequence text)
    {
        MyToast toast = new MyToast(context);

        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();

        View v = inflate.inflate(R.layout.toast_load, null);
        v.setMinimumWidth(dm.widthPixels);// 设置控件最小宽度为手机屏幕宽度

        TextView tv = (TextView) v.findViewById(R.id.new_data_toast_message);
        if (!TextUtils.isEmpty(text))
            tv.setText(text);

        toast.setView(v);
        toast.setDuration(2000);
        toast.setGravity(Gravity.TOP, 0, Utility.dip2px(45));
        toast.show();
    }

}
