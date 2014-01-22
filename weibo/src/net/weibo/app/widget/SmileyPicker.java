package net.weibo.app.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.weibo.app.AppContext;
import net.weibo.app.R;
import net.weibo.app.lib.ListViewTool;
import net.weibo.app.lib.SmileyPickerUtility;
import android.app.Activity;
import android.content.Context;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 */
public class SmileyPicker extends LinearLayout
{

    private int            mPickerHeight;
    private EditText       mEditText;
    private LayoutInflater mInflater;
    private List<String>   keys;
    private Activity       activity;

    public SmileyPicker(Context paramContext)
    {
        super(paramContext);
    }

    public SmileyPicker(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
        this.mInflater = LayoutInflater.from(paramContext);
        GridView gridView = (GridView) this.mInflater.inflate(R.layout.writeweiboactivity_smileypicker, null);
        addView(gridView);
        gridView.setAdapter(new SmileyAdapter(paramContext));
    }

    public void setEditText(Activity activity, ViewGroup rootLayout, EditText paramEditText)
    {
        this.mEditText = paramEditText;
        this.activity = activity;

    }

    public void show(Activity paramActivity, boolean showAnimation)
    {
        this.mPickerHeight = SmileyPickerUtility.getKeyboardHeight(paramActivity);
        SmileyPickerUtility.hideSoftInput(this.mEditText);
        getLayoutParams().height = this.mPickerHeight + 10;
        getLayoutParams().height = 400;
        setVisibility(View.VISIBLE);
        // open smilepicker, press home, press app switcher to return to write
        // weibo interface,
        // softkeyboard will be opened by android system when smilepicker is
        // showing,
        // this method is used to fix this issue
        paramActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    public void hide(Activity paramActivity)
    {
        setVisibility(View.GONE);
        paramActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }

    private final class SmileyAdapter extends BaseAdapter
    {

        private LayoutInflater mInflater;

        public SmileyAdapter(Context context)
        {

            this.mInflater = LayoutInflater.from(context);
            Set<String> keySet = AppContext.getInstance().getEmotionsPics().keySet();
            keys = new ArrayList<String>();
            keys.addAll(keySet);
        }

        private void bindView(final int position, View paramView)
        {
            ImageView imageView = ((ImageView) paramView.findViewById(R.id.smiley_item));
            imageView.setImageBitmap(AppContext.getInstance().getEmotionsPics().get(keys.get(position)));
            paramView.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    String ori = mEditText.getText().toString();
                    int index = mEditText.getSelectionStart();
                    StringBuilder stringBuilder = new StringBuilder(ori);
                    stringBuilder.insert(index, keys.get(position));

                    SpannableString span = new SpannableString(stringBuilder.toString());
                    ListViewTool.addEmotions(span, activity);

                    mEditText.setText(span);

                    mEditText.setSelection(index + keys.get(position).length());
                }
            });
        }

        @Override
        public int getCount()
        {
            return AppContext.getInstance().getEmotionsPics().size();
        }

        @Override
        public Object getItem(int paramInt)
        {
            return null;
        }

        @Override
        public long getItemId(int paramInt)
        {
            return 0L;
        }

        @Override
        public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
        {
            if (paramView == null)
                paramView = this.mInflater.inflate(R.layout.writeweiboactivity_smileypicker_item, null);
            bindView(paramInt, paramView);
            return paramView;
        }
    }

    // private void setupAnimations(LayoutTransition transition)
    // {
    //
    // ObjectAnimator animIn = ObjectAnimator.ofFloat(null, "translationY",
    // SmileyPickerUtility.getScreenHeight(this.activity),
    // mPickerHeight).setDuration(
    // transition.getDuration(LayoutTransition.APPEARING));
    // transition.setAnimator(LayoutTransition.APPEARING, animIn);
    //
    // ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "translationY",
    // mPickerHeight,
    // SmileyPickerUtility.getScreenHeight(this.activity)).setDuration(
    // transition.getDuration(LayoutTransition.DISAPPEARING));
    // transition.setAnimator(LayoutTransition.DISAPPEARING, animOut);
    //
    // }
}