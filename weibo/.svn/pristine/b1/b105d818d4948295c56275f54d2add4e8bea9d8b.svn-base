package net.weibo.app.ui;

import java.util.ArrayList;

import net.weibo.api.InfoImpl;
import net.weibo.api.ListenersImpl;
import net.weibo.api.NewsImpl;
import net.weibo.app.AppContext;
import net.weibo.app.R;
import net.weibo.app.adapter.HorizontalListViewApapter;
import net.weibo.app.adapter.NewsListViewAdapter;
import net.weibo.app.asynctask.MyAsyncTask;
import net.weibo.app.bean.News;
import net.weibo.app.bean.NewsList;
import net.weibo.app.bean.People;
import net.weibo.app.widget.HorizontalListView;
import net.weibo.app.widget.PullToRefreshListView;
import net.weibo.common.ImageDownloader;
import net.weibo.common.ImageUtils;
import net.weibo.common.UIUtils;
import net.weibo.common.Utility;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BrowserUserActivity extends BaseActivity implements OnClickListener
{

    private TextView              tv_back;
    private TextView              tv_tilte;
    private TextView              tv_listen;
    private TextView              tv_weibo_count;
    private TextView              tv_friends_count;
    private TextView              tv_fans_count;
    private TextView              tv_topics_count;

    private TextView              news_footer_more;
    private View                  news_footer;
    private ProgressBar           news_footer_progress;

    private PullToRefreshListView pullToRefreshListView;
    private HorizontalListView    horizontalListView;
    private NewsListViewAdapter   adapter;
    private ArrayList<News>       newsList = new ArrayList<News>();
    private People                people;
    private String                name;
    private LoadInfoTask          loadInfoTask;
    private LoadNewsTask          loadNewsTask;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        initView();

        if (null != savedInstanceState)
        {
            people = (People) savedInstanceState.getSerializable("people");
            newsList = (ArrayList<News>) savedInstanceState.getSerializable("newsList");
            name = savedInstanceState.getString("name");
        } else
        {
            Intent intent = getIntent();
            name = intent.getStringExtra("name");

        }
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setContentView(R.layout.activity_userinfo);
        initView();

        name = intent.getStringExtra("name");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putSerializable("people", people);
        outState.putSerializable("newsList", newsList);
        outState.putString("name", name);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        people = (People) savedInstanceState.getSerializable("people");
        newsList = (ArrayList<News>) savedInstanceState.getSerializable("newsList");
        name = savedInstanceState.getString("name");
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if (newsList.size() <= 0 && Utility.isTaskStopped(loadNewsTask))
        {

            loadNewsTask = new LoadNewsTask();
            loadNewsTask.execute();
        }
        if (people == null && Utility.isTaskStopped(loadInfoTask))
        {
            loadInfoTask = new LoadInfoTask(name);
            loadInfoTask.execute();
        }

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Utility.cancelTasks(loadInfoTask, loadNewsTask);
        adapter.clear();
        newsList.clear();
        people = null;
        name = "";

    }

    private class LoadInfoTask extends MyAsyncTask<String, String, Boolean>
    {
        private String                    name;
        private HorizontalListViewApapter horizontalAdapter;
        private ArrayList<Bitmap>         list = new ArrayList<Bitmap>();

        LoadInfoTask(String name)
        {
            this.name = name;
            horizontalAdapter = new HorizontalListViewApapter(BrowserUserActivity.this);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params)
        {

            if (isCancelled())
                return false;

            InfoImpl infoImpl = new InfoImpl(appContext);
            people = infoImpl.getUserInfo(name);

            if (null == people)
                return false;
            list.add(ImageUtils.getRoundedCornerBitmap(ImageDownloader.downloadBitmap(people.getHead() + "/120"), 8));

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            super.onPostExecute(result);
            if (result)
            {
                horizontalAdapter.setImages(list);
                horizontalListView.setAdapter(horizontalAdapter);
                horizontalAdapter.notifyDataSetChanged();

                tv_weibo_count.setText(people.getTweetnum());
                tv_friends_count.setText(people.getIdolnum());
                tv_fans_count.setText(people.getFansnum());
                tv_topics_count.setText(people.getFavnum());

                if (people.getIsfans())
                {
                    tv_listen.setText("取消收听");
                    LayoutParams layoutParams = tv_listen.getLayoutParams();

                    layoutParams.width = Utility.dip2px(80);
                    tv_listen.setLayoutParams(layoutParams);
                } else
                {
                    tv_listen.setText("收听");
                    LayoutParams layoutParams = tv_listen.getLayoutParams();

                    layoutParams.width = Utility.dip2px(60);
                    tv_listen.setLayoutParams(layoutParams);
                }

            } else
            {
                UIUtils.ToastMessage(getApplicationContext(), "服务器不稳定请稍后再试!");
            }
        }

    }

    private class LoadNewsTask extends MyAsyncTask<String, String, NewsList>
    {
        private String pageflag    = "0";
        private String pagetime;
        private String reqnum      = "10";
        private String lastid;
        private String type        = "3";
        private String contenttype = "0";

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            setFootView("正在加载", View.VISIBLE, View.VISIBLE, View.VISIBLE);
        }

        @Override
        protected NewsList doInBackground(String... params)
        {
            if (isCancelled())
                return null;
            if (null != params && params.length > 0)
            {
                pagetime = params[0];
                lastid = params[1];
                pageflag = params[2];
            }
            NewsImpl newsImpl = new NewsImpl();
            return newsImpl.getOthersWeibo(pageflag, pagetime, reqnum, lastid, name, type, contenttype).getData();

        }

        @Override
        protected void onPostExecute(NewsList result)
        {
            super.onPostExecute(result);
            if (null == result)
            {
                UIUtils.ToastMessage(getApplicationContext(), "服务器不稳定,暂时无法加载最新评论!");
            } else
            {
                if (result.getHasnext() == 0)
                {
                    setFootView("加载更多", View.VISIBLE, View.GONE, View.VISIBLE);
                } else
                {
                    setFootView("数据已拉取完毕", View.VISIBLE, View.GONE, View.VISIBLE);
                }
                newsList.addAll(result.getInfo());
            }
            adapter.setList(newsList);
            adapter.notifyDataSetChanged();

        }

    }

    private void initView()
    {
        findView();
        registerListener();

        if (null == adapter)
            adapter = new NewsListViewAdapter(this, newsList, pullToRefreshListView);
        pullToRefreshListView.addFooterView(news_footer);
        pullToRefreshListView.setAdapter(adapter);

        UIUtils.setScreenDisplayMetrics(appContext, horizontalListView, 20, null);

        tv_tilte.setText("资料");
        tv_back.setText(getString(R.string.back));
        tv_listen.setText("收听");

    }

    private void findView()
    {
        tv_back = (TextView) findViewById(R.id.infoButton);
        tv_listen = (TextView) findViewById(R.id.saveButton);
        tv_tilte = (TextView) findViewById(R.id.textView1);
        tv_weibo_count = (TextView) findViewById(R.id.weibo_count);
        tv_friends_count = (TextView) findViewById(R.id.friends_count);
        tv_fans_count = (TextView) findViewById(R.id.fans_count);
        tv_topics_count = (TextView) findViewById(R.id.topics_count);
        horizontalListView = (HorizontalListView) findViewById(R.id.horizontalListView);
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView);
        pullToRefreshListView.setSure(true);

        news_footer = getLayoutInflater().inflate(R.layout.listview_footer, null);
        news_footer_more = (TextView) news_footer.findViewById(R.id.listview_foot_more);
        news_footer_progress = (ProgressBar) news_footer.findViewById(R.id.listview_foot_progress);

    }

    private void registerListener()
    {
        tv_back.setOnClickListener(this);
        tv_listen.setOnClickListener(this);
        tv_weibo_count.setOnClickListener(this);
        tv_friends_count.setOnClickListener(this);
        tv_fans_count.setOnClickListener(this);
        tv_topics_count.setOnClickListener(this);
        news_footer.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (Utility.isTaskStopped(loadNewsTask) && !news_footer_more.getText().equals("数据已拉取完毕"))
                {
                    News news = newsList.get(newsList.size() - 1);
                    loadNewsTask = new LoadNewsTask();
                    loadNewsTask.execute(String.valueOf(news.getTimestamp()), String.valueOf(news.getId()), "1");
                }

            }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.infoButton:
                finish();
                break;
            case R.id.saveButton:
                if (!UIUtils.isFastDoubleClick())
                {
                    if (AppContext.getInstance().isNetworkConnected())
                    {
                        final ListenersImpl impl = new ListenersImpl(appContext);
                        LayoutParams layoutParams = tv_listen.getLayoutParams();

                        if (people.getIsfans())
                        {

                            new Thread()
                            {
                                @Override
                                public void run()
                                {
                                    impl.delListenerOne(name);
                                    impl.delListenerFromDb(name);

                                };
                            }.start();
                            people.setIsfans(false);
                            tv_listen.setText("收听");

                            layoutParams.width = Utility.dip2px(60);
                            tv_listen.setLayoutParams(layoutParams);

                        } else
                        {

                            new Thread()
                            {
                                @Override
                                public void run()
                                {
                                    impl.listenerOne(name);
                                    impl.insertPeopleToDb(people);

                                };
                            }.start();
                            people.setIsfans(true);
                            tv_listen.setText("取消收听");

                            layoutParams.width = Utility.dip2px(80);
                            tv_listen.setLayoutParams(layoutParams);
                        }
                    } else
                    {
                        UIUtils.ToastMessage(getApplicationContext(), getString(R.string.network_not_connected));
                    }
                }

                break;
            case R.id.weibo_count:
                break;
            case R.id.friends_count:
                break;
            case R.id.fans_count:
                break;
            case R.id.topics_count:
                break;
            default:
                break;
        }
    }

    public void setFootView(String text, int tvVisivle, int pbVisivle, int footVisible)
    {
        news_footer_more.setText(text);

        news_footer.setVisibility(footVisible);
        news_footer_more.setVisibility(tvVisivle);
        news_footer_progress.setVisibility(pbVisivle);

    }

    public static void startActivity(Activity activity, String myName)
    {
        if (!AppContext.getInstance().isNetworkConnected())
        {
            UIUtils.ToastMessage(AppContext.getInstance(), "网络不稳定,请稍后再试!");
            return;
        }
        Intent intent = new Intent(activity, BrowserUserActivity.class);
        intent.putExtra("name", myName);
        activity.startActivity(intent);
    }
}
