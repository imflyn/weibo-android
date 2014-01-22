package net.weibo.app.ui;

import java.util.ArrayList;

import net.weibo.api.ListenersImpl;
import net.weibo.app.R;
import net.weibo.app.adapter.ListenersListViewApapter;
import net.weibo.app.bean.People;
import net.weibo.app.bean.PeopleList;
import net.weibo.app.bean.Result;
import net.weibo.app.widget.LoadingDialog;
import net.weibo.app.widget.PullDownListView;
import net.weibo.common.UIUtils;
import net.weibo.constant.Tids;
import net.weibo.http.AsyncHttpResponseHandler;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ListenersActivity extends BaseActivity implements PullDownListView.OnRefreshListioner
{

    private PullDownListView         mPullDownView;
    private ListView                 mListView;
    private TextView                 infoButton;
    private ArrayList<People>        list        = new ArrayList<People>();
    private ListenersListViewApapter adapter;
    private int                      currentPage = 0;
    private Thread                   initThead   = null;
    private PeopleList               listeners;
    private boolean                  hasNext     = false;
    private String                   timestamp   = "";
    private LoadingDialog            loading;
    private String                   openId;
    private Button                   button;
    private View                     view;

    private Handler                  handler     = new Handler()
                                                 {
                                                     @Override
                                                     public void handleMessage(android.os.Message msg)
                                                     {
                                                         switch (msg.what)
                                                         {
                                                             case Tids.T_LOAD_LISTENERS:
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
                                                                 initThead = null;
                                                                 break;
                                                         }
                                                     };
                                                 };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listeners);
        Intent intent = getIntent();
        openId = intent.getExtras().getString("openId");
        initView();

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (null == list)
            list = new ArrayList<People>();
        if (checknet() && list.size() == 0)
            initViwData(openId, currentPage + "", true);
    }

    private boolean checknet()
    {
        if (!appContext.isNetworkConnected())
        {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
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
                    mPullDownView.setVisibility(View.INVISIBLE);
                    UIUtils.ToastMessage(getApplicationContext(), ListenersActivity.this.getString(R.string.network_not_connected));
                } else
                {
                    view.setVisibility(View.INVISIBLE);
                    view = null;
                    initViwData(openId, currentPage + "", true);
                }
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        try
        {
            if (null != initThead)
            {
                if (initThead.isAlive())
                    initThead.interrupt();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            adapter.clear();
            adapter = null;
            handler = null;
            listeners = null;
            list.clear();
            list = null;
        }

    }

    private void initView()
    {
        mPullDownView = (PullDownListView) findViewById(R.id.sreach_list);
        infoButton = (TextView) findViewById(R.id.infoButton);
        mListView = mPullDownView.mListView;
        adapter = new ListenersListViewApapter(this);
        adapter.setList(list);

        mListView.setAdapter(adapter);

        loading = new LoadingDialog(this);
        registerListener();
    }

    private void registerListener()
    {
        mPullDownView.setRefreshListioner(this);
        mListView.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int id, long position)
            {
                BrowserUserActivity.startActivity(ListenersActivity.this, list.get((int) position).getName());
            }
        });

        infoButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ListenersActivity.this.finish();
            }
        });
    }

    private void setListView()
    {

        if (currentPage == 0)
        {
            mPullDownView.onRefreshComplete();// 这里表示刷新处理完成后把上面的加载刷新界面隐藏
        } else
        {
            mPullDownView.onLoadMoreComplete();// 这里表示加载更多处理完成后把下面的加载更多界面（隐藏或者设置字样更多）
        }
        if (hasNext)
        {
            mPullDownView.setMore(true);// 这里设置true表示还有更多加载，设置为false底部将不显示更多
        } else
        {
            mPullDownView.setMore(false);
        }
        adapter = new ListenersListViewApapter(this);
        adapter.setList(list);
        mListView.setAdapter(adapter);
        mListView.setSelection(currentPage);
        mPullDownView.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    private void initViwData(final String openId, final String page, boolean showLoading)
    {
        if (null == initThead)
        {
            if (loading != null && showLoading && list.size() == 0)
            {
                loading.show();
                loading.setCancelable(true);
            }
            initThead = new Thread()
            {
                @Override
                public void run()
                {

                    final ListenersImpl impl = new ListenersImpl(appContext);
                    impl.getMyListenersInfo(page, openId, new AsyncHttpResponseHandler()
                    {
                        @Override
                        public void onSuccess(String content)
                        {
                            try
                            {
                                Result result = Result.parse(content);
                                if (result.getRet() != 0)
                                {
                                    handler.obtainMessage(Tids.T_LOAD_LISTENERS, Tids.T_FAIL).sendToTarget();
                                } else
                                {
                                    listeners = impl.parse(content);

                                    if (!page.equals("0"))
                                    {
                                        list.addAll(listeners.getFans());
                                    } else
                                    {
                                        list = listeners.getFans();
                                    }
                                    if (listeners.getHasnext().equals("0"))// 0-表示还有数据，1-表示下页没有数据,
                                    {
                                        hasNext = true;
                                    } else
                                    {
                                        hasNext = false;
                                    }
                                    timestamp = listeners.getTimestamp();
                                    handler.obtainMessage(Tids.T_LOAD_LISTENERS, Tids.T_SUCCESS).sendToTarget();
                                }

                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            };
            initThead.start();
        }

    }

    @Override
    public void onRefresh()
    {
        if (checknet())
            currentPage = 0;
        initViwData(openId, "0", false);

    }

    @Override
    public void onLoadMore()
    {
        if (checknet())
            currentPage = currentPage + 30;
        initViwData(openId, currentPage + "", false);
    }

}
