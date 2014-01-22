package net.weibo.app.ui;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.weibo.api.NewsImpl;
import net.weibo.app.AppConfig;
import net.weibo.app.AppContext;
import net.weibo.app.R;
import net.weibo.app.adapter.NewsListViewAdapter;
import net.weibo.app.bean.News;
import net.weibo.app.bean.NewsList;
import net.weibo.app.bean.Result;
import net.weibo.app.sp.SharedPreferencesConfig;
import net.weibo.app.widget.MyToast;
import net.weibo.app.widget.PullToRefreshListView;
import net.weibo.common.SoundTool;
import net.weibo.common.TimeTool;
import net.weibo.common.UIUtils;
import net.weibo.common.Utility;
import net.weibo.constant.Tids;
import net.weibo.http.AsyncHttpResponseHandler;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 首页Activity
 */
public class HomeActivity extends BaseActivity
{
    private TextView                   tv_title;
    private ImageButton                tv_writeWeibo;
    private ImageButton                btn_toTop;
    private PullToRefreshListView      pullToRefreshListView;
    private static NewsList            newsList;
    private static ArrayList<News>     list = new ArrayList<News>();
    private ExecutorService            executors;
    private Loader                     loader;
    private static NewsListViewAdapter adapter;
    private TextView                   news_footer_more;
    private View                       news_footer;
    private ProgressBar                news_footer_progress;
    private ProgressBar                pb_load;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if (list.size() <= 0)
            initData();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        tv_title.setText(AppConfig.getAppConfig(appContext).get("Nick"));
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if (null != newsList)
            outState.putSerializable("newsList", newsList);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (data == null)
            return;
        final News news = (News) data.getSerializableExtra("news");
        if (news != null)
        {
            for (int i = 0; i < list.size(); i++)
            {
                if (news.getId().equals(list.get(i).getId()))
                {
                    News ori = list.get(i);
                    if (ori.getCount() != news.getCount() || ori.getMcount() != news.getMcount())
                    {
                        ori.setCount(news.getCount());
                        ori.setMcount(news.getMcount());
                        list.remove(i);
                        list.add(i, ori);
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                }
            }

        }
    }

    private void initView()
    {

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(AppConfig.getAppConfig(appContext).get("Nick"));
        tv_writeWeibo = (ImageButton) findViewById(R.id.ib_write);
        btn_toTop = (ImageButton) findViewById(R.id.btn_toTop);

        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView);
        pullToRefreshListView.setDivider(null);
        pullToRefreshListView.setScrollingCacheEnabled(false);

        news_footer = getLayoutInflater().inflate(R.layout.listview_footer, null);
        news_footer_more = (TextView) news_footer.findViewById(R.id.listview_foot_more);
        news_footer_progress = (ProgressBar) news_footer.findViewById(R.id.listview_foot_progress);
        pb_load = (ProgressBar) findViewById(R.id.pb_load);

        pb_load.setVisibility(View.VISIBLE);
        registerListener();

    }

    private void registerListener()
    {
        tv_writeWeibo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(HomeActivity.this, WriteNewsActivity.class);
                startActivity(intent);
            }
        });
        btn_toTop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pullToRefreshListView.setSelection(1);
            }
        });

        pullToRefreshListView.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                pullToRefreshListView.onScrollStateChanged(view, scrollState);

                // 数据为空--不用继续下面代码了
                if (list.isEmpty())
                    return;

                // 判断是否滚动到底部
                boolean scrollEnd = false;
                try
                {
                    if (view.getPositionForView(news_footer) == view.getLastVisiblePosition())
                        scrollEnd = true;
                } catch (Exception e)
                {
                    scrollEnd = false;
                }
                if (SharedPreferencesConfig.getInstance(appContext).getBoolean(SharedPreferencesConfig.SP_AUTO_LOAD) && scrollEnd && loader.pageflag.equals("1"))
                {
                    news_footer_more.setText(R.string.loading);
                    news_footer_progress.setVisibility(View.VISIBLE);
                    initData();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {

                pullToRefreshListView.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                if (firstVisibleItem >= AppContext.PAGE_SIZE && totalItemCount > visibleItemCount && btn_toTop.getVisibility() != View.VISIBLE)
                    btn_toTop.setVisibility(View.VISIBLE);
                else if (firstVisibleItem < AppContext.PAGE_SIZE && btn_toTop.getVisibility() == View.VISIBLE)
                    btn_toTop.setVisibility(View.GONE);
            }
        });
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                if (null == loader)
                    loader = new Loader();
                loader.pageflag = "0";
                loader.pagetime = "";
                initData();
                if (SharedPreferencesConfig.getInstance(appContext).getBoolean(SharedPreferencesConfig.SP_SOUNDALARM))
                    SoundTool.getInstance().play(SoundTool.PULLDOWN);
            }
        });

        news_footer.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                news_footer_more.setText(R.string.loading);
                news_footer_progress.setVisibility(View.VISIBLE);
                initData();
            }
        });

    }

    public ListView getListView()
    {
        if (null == pullToRefreshListView)
            pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView);
        return pullToRefreshListView;
    }

    private void initData()
    {
        if (null == executors)
            executors = Executors.newSingleThreadExecutor();
        if (null == loader)
            loader = new Loader();
        if (null != list)
            executors.submit(loader);
    }

    private Handler handler = new Handler()
                            {
                                @Override
                                public void handleMessage(android.os.Message msg)
                                {
                                    switch (msg.what)
                                    {
                                        case Tids.T_LOAD_NEWS:
                                            pb_load.setVisibility(View.GONE);
                                            if (msg.arg1 == Tids.T_SUCCESS)
                                            {
                                                setList();
                                            } else
                                            {
                                                list.clear();
                                                setList();
                                                UIUtils.ToastMessage(getApplicationContext(), "网络不稳定请稍候再试!");
                                                news_footer_progress.setVisibility(View.GONE);
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                };
                            };

    private void setList()
    {
        if (null == adapter)
        {
            pullToRefreshListView.addFooterView(news_footer);// 添加底部视图
                                                             // 必须在setAdapter前
            adapter = new NewsListViewAdapter(this, list, pullToRefreshListView);
            pullToRefreshListView.setAdapter(adapter);
            if (Utility.getTopActivityName(appContext).equals("MainActivity"))
            {
                if (list.size() > 0)
                    MyToast.show(this, "刚刚更新了" + String.valueOf(list.size()) + "条数据");
                else
                    MyToast.show(this, null);
            }
        }

        news_footer_more.setText(R.string.load_more);
        news_footer_progress.setVisibility(View.GONE);
        adapter.setList(list);
        adapter.notifyDataSetChanged();
        pullToRefreshListView.onRefreshComplete("上次更新:" + TimeTool.getListTime(System.currentTimeMillis()));

    }

    private class Loader implements Runnable
    {
        private NewsImpl     newsImpl    = new NewsImpl();
        private final String type        = "0";                                 // 0x1
                                                                                 // 原创发表
                                                                                 // 0x2
                                                                                 // 转载如需拉取多个类型请使用|，如(0x1|0x2)得到3，则type=3即可，填零表示拉取所有类型
        private final String requirenum  = String.valueOf(AppContext.PAGE_SIZE);
        private final String contenttype = "0";                                 // 0-表示所有类型，1-带文本，2-带链接，4-带图片，8-带视频，0x10-带音频
                                                                                 // 建议不使用contenttype为1的类型，如果要拉取只有文本的微博，建议使用0x80
        private String       pageflag    = "0";
        private String       pagetime;

        @Override
        public void run()
        {
            newsImpl.getMyWeibo(requirenum, type, contenttype, pageflag, pagetime, appContext, new AsyncHttpResponseHandler()
            {
                @Override
                public void onSuccess(String content)
                {
                    try
                    {
                        if (Result.parse(content).getRet() == 0)
                        {

                            newsList = newsImpl.parse(content).getData();
                            ArrayList<News> tempList = newsList.getInfo();
                            if (!pageflag.equals("0"))
                            {
                                // 去除重复微博
                                for (int i = 0; i < list.size(); i++)
                                {
                                    for (int j = 0; j < tempList.size(); j++)
                                    {
                                        if (list.get(i).getId().equals(tempList.get(j).getId()))
                                        {
                                            tempList.remove(j);
                                        }
                                    }
                                }
                            } else
                                list.clear();
                            list.addAll(tempList);
                            if (newsList.getHasnext() == 0)// 0为还有下页
                            {
                                pageflag = "1";
                                pagetime = String.valueOf(list.get(list.size() - 1).getTimestamp());
                            } else
                            {
                                news_footer.setVisibility(View.GONE);
                            }
                            handler.obtainMessage(Tids.T_LOAD_NEWS, Tids.T_SUCCESS, 0, null).sendToTarget();
                        } else
                            handler.obtainMessage(Tids.T_LOAD_NEWS, Tids.T_FAIL, 0, null).sendToTarget();
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        handler.obtainMessage(Tids.T_LOAD_NEWS, Tids.T_FAIL, 0, null).sendToTarget();
                    }

                };

                @Override
                public void onFailure(Throwable error, String content)
                {
                    error.printStackTrace();
                    handler.obtainMessage(Tids.T_LOAD_NEWS, Tids.T_FAIL, 0, null).sendToTarget();
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            startActivity(intent);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
