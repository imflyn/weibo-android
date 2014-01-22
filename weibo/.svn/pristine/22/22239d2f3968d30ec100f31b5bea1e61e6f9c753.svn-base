package net.weibo.app.lib;

import net.weibo.common.Utility;
import android.app.Activity;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * 字符数限制
 */
public class TextNumLimitWatcher implements TextWatcher
{
    private TextView tv;
    private EditText et;
    private Activity activity;

    public TextNumLimitWatcher(TextView tv, EditText et, Activity activity)
    {
        this.tv = tv;
        this.et = et;
        this.activity = activity;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {

        int sum = Utility.length(et.getText().toString());

        int left = 140 - sum;

        if (left == 140)
        {
            tv.setText("140");
        } else
        {
            tv.setText(String.valueOf(left));
        }
        if (left < 0)
        {
            tv.setTextColor(activity.getResources().getColor(android.R.color.holo_red_dark));
        } else if (left >= 0 && left <= 140)
        {
            int[] attrs = new int[] { android.R.attr.actionMenuTextColor };
            TypedArray ta = activity.obtainStyledAttributes(attrs);
            int drawableFromTheme = ta.getColor(0, 430);
            tv.setTextColor(drawableFromTheme);
        }

    }

    @Override
    public void afterTextChanged(Editable s)
    {
    }
}
