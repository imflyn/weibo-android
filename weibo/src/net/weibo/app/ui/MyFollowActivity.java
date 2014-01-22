package net.weibo.app.ui;

import java.util.ArrayList;

import net.weibo.app.R;
import net.weibo.app.adapter.MyFollowerAdapter;
import net.weibo.app.bean.People;
import net.weibo.app.ui.fragment.LetterListFragment;
import net.weibo.app.widget.LoadingDialog;
import net.weibo.app.widget.SideBar;
import net.weibo.common.UIUtils;
import net.weibo.constant.BroadCastUrls;
import net.weibo.constant.Tids;
import net.weibo.dao.MyFollowersDBService;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 收听的人列表
 * 
 * @author V
 * 
 */
public class MyFollowActivity extends BaseActivity
{

    private View              view;
    private Button            button;
    private TextView          infoButton;
    private MyFollowerAdapter adapter;
    private ListView          lv_myFollow;
    private LoadingDialog     loading;
    private Thread            initThread;
    private SideBar           indexBar;
    private LinearLayout      linearLayout_list;
    private EditText          et_search;
    private TextView          mDialogText;
    private WindowManager     mWindowManager;
    private int               type    = -1;

    private ArrayList<People> list    = new ArrayList<People>();

    private Handler           handler = new Handler()
                                      {
                                          @Override
                                          public void handleMessage(android.os.Message msg)
                                          {
                                              switch (msg.what)
                                              {
                                                  case Tids.T_LOAD_FOLLOWERS:
                                                      if (null != loading && loading.isShowing())
                                                      {
                                                          loading.dismiss();
                                                      }
                                                      if ((Integer) msg.obj == Tids.T_FAIL)
                                                      {
                                                          UIUtils.ToastMessage(getApplicationContext(), "服务器异常,请稍后再试..");
                                                      } else
                                                      {
                                                          setListView();
                                                      }
                                                      initThread = null;
                                                      break;
                                                  default:
                                                      break;
                                              }
                                          };
                                      };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfollow);

        if (null != savedInstanceState)
        {
            type = savedInstanceState.getInt("type");
        } else
        {
            Intent intent = getIntent();
            type = intent.getIntExtra("type", -1);
        }
        initView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt("type", type);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null)
        {
            type = savedInstanceState.getInt("type");
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (null == list)
            list = new ArrayList<People>();
        if (checknet() && list.size() == 0)
            initViwData(true);
    }

    private OnItemClickListener itemClickListenet = new OnItemClickListener()
                                                  {

                                                      @Override
                                                      public void onItemClick(AdapterView<?> parent, View view, int id, long position)
                                                      {
                                                          Intent intent = new Intent();
                                                          switch (type)
                                                          {
                                                              case AbstractWriteActivity.AT_USER:
                                                                  intent.putExtra("name", "@" + list.get((int) position).getName() + " ");
                                                                  setResult(Activity.RESULT_OK, intent);
                                                                  finish();
                                                                  break;
                                                              case LetterListFragment.CHOOSE_PEOPLE:
                                                                  intent.putExtra("name", list.get((int) position).getName());
                                                                  intent.putExtra("nick", list.get((int) position).getNick());
                                                                  setResult(Activity.RESULT_OK, intent);
                                                                  finish();
                                                                  break;
                                                              default:
                                                                  BrowserUserActivity.startActivity(MyFollowActivity.this, ((People) (lv_myFollow.getAdapter().getItem((int) position))).getName());
                                                                  break;
                                                          }
                                                      }
                                                  };

    private boolean checknet()
    {
        if (!appContext.isNetworkConnected())
        {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            view = UIUtils.getNetBusyView(this);
            addContentView(view, params);
            UIUtils.ToastMessage(getApplicationContext(), getString(R.string.network_not_connected));
            netBusy();
            return false;
        }
        return true;
    }

    private void netBusy()
    {
        button = (Button) findViewById(R.id.btn_tryAgain);
        button.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!appContext.isNetworkConnected())
                {
                    UIUtils.ToastMessage(getApplicationContext(), MyFollowActivity.this.getString(R.string.network_not_connected));
                } else
                {
                    view.setVisibility(View.INVISIBLE);
                    view = null;
                    initViwData(true);
                }
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (null != indexBar)
        {
            if (mDialogText.getParent() != null)
                mWindowManager.removeView(mDialogText);
        }
        try
        {
            if (null != initThread)
            {
                if (initThread.isAlive())
                    initThread.interrupt();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            indexBar = null;
            mWindowManager = null;
            mDialogText = null;
            adapter.clear();
            adapter = null;
            lv_myFollow = null;
            initThread = null;
        }
        unregisterReceiver(receiver);

    }

    private void initView()
    {
        infoButton = (TextView) findViewById(R.id.infoButton);
        infoButton.setText(getString(R.string.back));

        lv_myFollow = (ListView) findViewById(R.id.lv_myFollow);
        et_search = (EditText) findViewById(R.id.et_search);
        indexBar = (SideBar) findViewById(R.id.sideBar);
        linearLayout_list = (LinearLayout) findViewById(R.id.linearLayout_list);
        linearLayout_list.setVisibility(View.INVISIBLE);
        loading = new LoadingDialog(this);

        mDialogText = (TextView) LayoutInflater.from(this).inflate(R.layout.list_position, null);
        mDialogText.setVisibility(View.INVISIBLE);
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        mWindowManager.addView(mDialogText, lp);

        adapter = new MyFollowerAdapter(this);
        adapter.setList(list);
        lv_myFollow.setAdapter(adapter);
        lv_myFollow.setOnItemClickListener(itemClickListenet);
        indexBar.setTextView(mDialogText);
        indexBar.setListView(lv_myFollow);

        registerListener();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver()
                                       {
                                           @Override
                                           public void onReceive(Context context, Intent intent)
                                           {
                                               initThread = null;
                                               initViwData(false);
                                           }
                                       };

    private void registerListener()
    {
        registerReceiver(receiver, new IntentFilter(BroadCastUrls.LOAD_MYFOLLOWERS));

        infoButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MyFollowActivity.this.finish();
            }
        });

        et_search.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                MyFollowersDBService db = new MyFollowersDBService(MyFollowActivity.this);
                if (String.valueOf(et_search.getText()).equals(""))
                {
                    list = db.query();
                } else
                {
                    list = db.search(String.valueOf(et_search.getText()));
                }
                adapter.setList(list);
                adapter.notifyDataSetChanged();
                db = null;
            }
        });
    }

    private void setListView()
    {
        if (null != adapter && null != list && null != indexBar)
        {

            adapter.setList(list);
            adapter.notifyDataSetChanged();

            linearLayout_list.setVisibility(View.VISIBLE);
        }
    }

    private void initViwData(boolean ifShow)
    {

        if (null == initThread)
        {
            if (loading != null && ifShow && list.size() == 0)
            {
                loading.show();
                loading.setCancelable(true);
            }
            initThread = new Thread()
            {
                @Override
                public void run()
                {
                    MyFollowersDBService db = new MyFollowersDBService(MyFollowActivity.this);
                    list = db.query();
                    if (list != null && list.size() > 0)
                        handler.obtainMessage(Tids.T_LOAD_FOLLOWERS, Tids.T_SUCCESS).sendToTarget();
                }
            };
            initThread.start();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
