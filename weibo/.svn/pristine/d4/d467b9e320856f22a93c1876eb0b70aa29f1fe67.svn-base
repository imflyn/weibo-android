package net.weibo.app.ui;

import net.weibo.app.lib.ListViewTool;
import net.weibo.app.service.RepostWeiboService;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;

public class ReposeNewsActivity extends AbstractWriteActivity
{

    public static final String ACTION_DRAFT       = "DRAFT";
    public static final String ACTION_SEND_FAILED = "SEND_FAILED";

    private String             reId;
    private String             text;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initView();
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (!TextUtils.isEmpty(action))
        {
            if (action.equals(Intent.ACTION_SEND) && !TextUtils.isEmpty(type))
            {

            } else if (action.equals(ACTION_DRAFT))
            {

            } else if (action.equals(ACTION_SEND_FAILED))
            {
                initView();
                handleFailedOperation(intent);
            }
        } else
        {
            initView();
            setTitle("转发");
            reId = intent.getStringExtra("reId");
            text = intent.getStringExtra("text");

            content.setText(text);
        }

    }

    private void handleFailedOperation(Intent intent)
    {
        SpannableString span = ListViewTool.convertNormalStringToSpannableString(intent.getStringExtra("text"), this);

        content.setText(span);

    }

    @Override
    protected void onStart()
    {
        super.onStart();

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

    }

    private void initView()
    {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString("reId", reId);
        outState.putString("text", content.getEditableText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null)
        {
            reId = savedInstanceState.getString("reId");
            text = savedInstanceState.getString("text");
        }
    }

    public static Intent startBecauseSendFailed(Context context, String text, String reId)
    {
        Intent intent = new Intent(context, ReposeNewsActivity.class);
        intent.setAction(ReposeNewsActivity.ACTION_SEND_FAILED);
        intent.putExtra("text", text);
        intent.putExtra("reId", reId);
        return intent;
    }

    @Override
    protected boolean canShowSaveDraftDialog()
    {
        return true;
    }

    @Override
    protected void sendNews()
    {

        if (!canSend())
            return;

        String value = content.getText().toString();

        Intent intent = new Intent(this, RepostWeiboService.class);
        intent.putExtra("content", value);
        intent.putExtra("reId", reId);
        intent.putExtra("type", RepostWeiboService.repose);
        startService(intent);
        finish();
    }

    @Override
    public void saveToDraft()
    {

    }

    public static void turnToReposeNews(Activity activity, String reId, String text)
    {
        Intent intent = new Intent(activity, ReposeNewsActivity.class);
        intent.putExtra("text", text);
        intent.putExtra("reId", reId);
        activity.startActivity(intent);

    }

}
