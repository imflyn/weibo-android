package net.weibo.app.widget;

import net.weibo.app.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

/**
 * 加载对话框控件
 * 
 */
public class LoadingDialog extends Dialog
{

    private Context        mContext;
    private LayoutInflater inflater;
    private LayoutParams   lp;
    private TextView       loadtext;
    private View           layout;
    private Activity       activity;

    public LoadingDialog(Context context)
    {
        super(context, R.style.Dialog);

        this.mContext = context;

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.loadingdialog, null);
        loadtext = (TextView) layout.findViewById(R.id.loading_text);
        setContentView(layout);
        this.setCancelable(false);

        // 设置window属性
        lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0; // 去背景遮盖
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);

    }

    @Override
    public void dismiss()
    {
        super.dismiss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (null != mContext)
            {
                dismiss();
                activity = (Activity) mContext;
                activity.finish();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setLoadText(String content)
    {
        loadtext.setText(content);
    }
}