package net.weibo.app.ui;

import net.weibo.app.AppContext;
import net.weibo.app.R;
import net.weibo.app.lib.CheatSheet;
import net.weibo.app.lib.ListViewTool;
import net.weibo.app.lib.SmileyPickerUtility;
import net.weibo.app.lib.TextNumLimitWatcher;
import net.weibo.app.widget.SaveDraftDialog;
import net.weibo.app.widget.SaveDraftDialog.IDraft;
import net.weibo.app.widget.SmileyPicker;
import net.weibo.common.AsyncImageLoader;
import net.weibo.common.UIUtils;
import net.weibo.common.Utility;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public abstract class AbstractWriteActivity extends BaseActivity implements IDraft
{
    public static final int  CAMERA_RESULT = 0;
    public static final int  PIC_RESULT    = 1;
    public static final int  AT_USER       = 3;
    public static final int  BROWSER_PIC   = 4;

    TextView                 tv_title;
    private TextView         tv_back;
    private TextView         tv_send;

    TextView                 tv_location;
    AutoCompleteTextView     content;

    ImageView                iv_head;
    ImageButton              menu_pic;
    ImageButton              menu_geo;
    ImageButton              menu_at;
    ImageButton              menu_emoticon;
    ImageButton              menu_topic;

    SmileyPicker             smiley        = null;
    private RelativeLayout   container     = null;

    private AsyncImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abstract_write);

        initView();
        registerListener();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        imageLoader.destroy();

        iv_head.setImageBitmap(null);
        menu_pic.setImageBitmap(null);
        menu_geo.setImageBitmap(null);
        menu_at.setImageBitmap(null);
        menu_emoticon.setImageBitmap(null);
        menu_topic.setImageBitmap(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case AT_USER:
                    String name = intent.getStringExtra("name");
                    String ori = content.getText().toString();
                    int index = content.getSelectionStart();
                    StringBuilder stringBuilder = new StringBuilder(ori);
                    stringBuilder.insert(index, name);

                    SpannableString span = new SpannableString(stringBuilder.toString());
                    ListViewTool.addEmotions(span, this);
                    content.setText(span);

                    content.setSelection(index + name.length());
                    break;
            }

        }

    }

    private void initView()
    {
        tv_title = (TextView) findViewById(R.id.textView1);
        tv_title.setText(getString(R.string.boradcast));
        tv_send = (TextView) findViewById(R.id.saveButton);
        setTitle(getString(R.string.send));
        tv_back = (TextView) findViewById(R.id.infoButton);
        tv_back.setText(getString(R.string.close));

        tv_location = (TextView) findViewById(R.id.tv_location);

        iv_head = (ImageView) findViewById(R.id.iv_head);

        menu_pic = (ImageButton) findViewById(R.id.menu_pic);
        menu_geo = (ImageButton) findViewById(R.id.menu_geo);
        menu_at = (ImageButton) findViewById(R.id.menu_at);
        menu_emoticon = (ImageButton) findViewById(R.id.menu_emoticon);
        menu_topic = (ImageButton) findViewById(R.id.menu_topic);

        CheatSheet.setup(AbstractWriteActivity.this, menu_pic, "添加图片");
        CheatSheet.setup(AbstractWriteActivity.this, menu_geo, "地理位置");
        CheatSheet.setup(AbstractWriteActivity.this, menu_at, "@好友");
        CheatSheet.setup(AbstractWriteActivity.this, menu_emoticon, "添加表情");
        CheatSheet.setup(AbstractWriteActivity.this, menu_topic, "添加话题");

        content = ((AutoCompleteTextView) findViewById(R.id.status_new_content));
        content.addTextChangedListener(new TextNumLimitWatcher((TextView) findViewById(R.id.tv_length), content, this));

        smiley = (SmileyPicker) findViewById(R.id.smiley_picker);
        smiley.setEditText(AbstractWriteActivity.this, ((LinearLayout) findViewById(R.id.root_layout)), content);
        container = (RelativeLayout) findViewById(R.id.container);

        if (imageLoader == null)
            imageLoader = new AsyncImageLoader(AppContext.getInstance());
        imageLoader.loadBitmap(iv_head, AppContext.getInstance().getProperty("HeadUrl") + "/50", 2);

    }

    private OnClickListener clickListener = new OnClickListener()
                                          {

                                              @Override
                                              public void onClick(View v)
                                              {
                                                  switch (v.getId())
                                                  {
                                                      case R.id.infoButton:
                                                          if (smiley.isShown())
                                                              hideSmileyPicker(false);
                                                          else if (!TextUtils.isEmpty(content.getText().toString()) && canShowSaveDraftDialog())
                                                          {
                                                              SaveDraftDialog dialog = new SaveDraftDialog();
                                                              dialog.show(getSupportFragmentManager(), "");
                                                          } else
                                                              finish();
                                                          break;
                                                      case R.id.saveButton:
                                                          sendNews();
                                                          break;
                                                      case R.id.menu_emoticon:
                                                          if (smiley.isShown())
                                                              hideSmileyPicker(false);
                                                          else
                                                              showSmileyPicker(SmileyPickerUtility.isKeyBoardShow(AbstractWriteActivity.this));
                                                          break;
                                                      case R.id.status_new_content:
                                                          hideSmileyPicker(true);
                                                          break;
                                                      case R.id.menu_topic:
                                                          insertTopic();
                                                          break;
                                                      case R.id.menu_at:
                                                          Intent intent = new Intent(AbstractWriteActivity.this, MyFollowActivity.class);
                                                          intent.putExtra("type", AT_USER);
                                                          startActivityForResult(intent, AT_USER);
                                                          break;
                                                      default:
                                                          break;
                                                  }
                                              }
                                          };

    private void registerListener()
    {
        tv_back.setOnClickListener(clickListener);
        tv_send.setOnClickListener(clickListener);

        menu_emoticon.setOnClickListener(clickListener);
        menu_topic.setOnClickListener(clickListener);
        menu_at.setOnClickListener(clickListener);

        content.setOnClickListener(clickListener);

    }

    private void showSmileyPicker(boolean showAnimation)
    {
        this.smiley.show(AbstractWriteActivity.this, showAnimation);
        lockContainerHeight(SmileyPickerUtility.getAppContentHeight(AbstractWriteActivity.this));

    }

    public void hideSmileyPicker(boolean showKeyBoard)
    {
        if (this.smiley.isShown())
        {
            if (showKeyBoard)
            {
                LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams) this.container.getLayoutParams();
                localLayoutParams.height = smiley.getTop();
                localLayoutParams.weight = 0;
                this.smiley.hide(AbstractWriteActivity.this);

                SmileyPickerUtility.showKeyBoard(content);
                content.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        unlockContainerHeightDelayed();
                    }
                }, 100);
            } else
            {
                this.smiley.hide(AbstractWriteActivity.this);
                unlockContainerHeightDelayed();
            }
        }

    }

    private void lockContainerHeight(int paramInt)
    {
        LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams) this.container.getLayoutParams();
        localLayoutParams.height = paramInt;
        localLayoutParams.weight = 0;
    }

    public void unlockContainerHeightDelayed()
    {
        ((LinearLayout.LayoutParams) AbstractWriteActivity.this.container.getLayoutParams()).weight = 1;
    }

    boolean canSend()
    {
        if (UIUtils.isFastDoubleClick())
            return false;

        boolean haveContent = !TextUtils.isEmpty(content.getText().toString());

        int sum = Utility.length(content.getText().toString());
        int num = 140 - sum;

        boolean contentNumBelow140 = (num >= 0);

        if (haveContent && contentNumBelow140)
        {

            if (!AppContext.getInstance().isNetworkConnected())
            {
                UIUtils.ToastMessage(getApplicationContext(), "网络不稳定,请稍候再试!");
                return false;
            }
            return true;
        } else
        {
            if (!haveContent)
            {
                Toast.makeText(this, getString(R.string.content_cant_be_empty), Toast.LENGTH_SHORT).show();
            }
            if (!contentNumBelow140)
            {
                Toast.makeText(this, getString(R.string.content_words_number_too_many), Toast.LENGTH_SHORT).show();
            }
        }

        return false;
    }

    protected void insertTopic()
    {
        String ori = content.getText().toString();
        String topicTag = "##";

        SpannableString span = new SpannableString(ori + topicTag);
        ListViewTool.addEmotions(span, this);

        content.setText(span);
        content.setSelection(content.getText().toString().length() - 1);

    }

    protected void sendNews()
    {
    };

    @Override
    public void onBackPressed()
    {
        if (smiley.isShown())
        {
            hideSmileyPicker(false);
        } else if (!TextUtils.isEmpty(content.getText().toString()) && canShowSaveDraftDialog())
        {
            SaveDraftDialog dialog = new SaveDraftDialog();
            dialog.show(getSupportFragmentManager(), "");
        } else
        {
            super.onBackPressed();
        }
    }

    protected abstract boolean canShowSaveDraftDialog();

    /**
     * 设置标题
     * 
     * @param title
     */
    protected void setTitle(String title)
    {
        tv_send.setText(title);
    }
}
