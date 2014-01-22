package net.weibo.app.ui.fragment;

import java.util.ArrayList;

import net.weibo.api.NewsImpl;
import net.weibo.app.R;
import net.weibo.app.adapter.NewsListViewAdapter;
import net.weibo.app.asynctask.MyAsyncTask;
import net.weibo.app.bean.News;
import net.weibo.app.bean.NewsData;
import net.weibo.app.bean.NewsList;
import net.weibo.app.widget.PullToRefreshListView;
import net.weibo.common.TimeTool;
import net.weibo.common.UIUtils;
import net.weibo.common.Utility;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NewsListAtMeFragment extends AbstractAppFragment
{
    private PullToRefreshListView pullToRefreshListView;
    private NewsListViewAdapter   adapter;
    private ArrayList<News>       newsList = new ArrayList<News>();
    private TextView              news_footer_more;
    private View                  news_footer;
    private ProgressBar           news_footer_progress;
    private LoadNewsTask          loadNewsTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        if (null != savedInstanceState)
        {
            newsList = (ArrayList<News>) savedInstanceState.getSerializable("newsList");
        } else
        {

        }
        initView();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (newsList.size() <= 0 && Utility.isTaskStopped(loadNewsTask))
        {

            loadNewsTask = new LoadNewsTask();
            loadNewsTask.execute();
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Utility.cancelTasks(loadNewsTask);
        adapter.clear();
        newsList.clear();

    }

    private void initView()
    {

        pullToRefreshListView = (PullToRefreshListView) getActivity().findViewById(R.id.atme_pullToRefreshListView);

        news_footer = getActivity().getLayoutInflater().inflate(R.layout.listview_footer, null);
        news_footer_more = (TextView) news_footer.findViewById(R.id.listview_foot_more);
        news_footer_progress = (ProgressBar) news_footer.findViewById(R.id.listview_foot_progress);

        if (null == adapter)
            adapter = new NewsListViewAdapter(getActivity(), newsList, pullToRefreshListView);

        pullToRefreshListView.addFooterView(news_footer);
        pullToRefreshListView.setAdapter(adapter);

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

        pullToRefreshListView.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                pullToRefreshListView.onScrollStateChanged(view, scrollState);

                // 数据为空--不用继续下面代码了
                if (newsList.isEmpty())
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
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {

                pullToRefreshListView.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        });
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                Utility.cancelTasks(loadNewsTask);
                loadNewsTask = new LoadNewsTask();
                loadNewsTask.execute();

            }
        });
    }

    private class LoadNewsTask extends MyAsyncTask<String, String, NewsList>
    {
        private String pageflag    = "0";
        private String pagetime;
        private String reqnum      = "10";
        private String lastid;
        private String type        = "0x20";
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
            if (null != params && params.length > 1)
            {
                pagetime = params[0];
                lastid = params[1];
                pageflag = params[2];
            }

            NewsImpl newsImpl = new NewsImpl();

            NewsData newsData = newsImpl.getAtMe(pageflag, pagetime, reqnum, lastid, type, contenttype);
            if (null != newsData)
            {
                NewsList newsList = newsData.getData();
                if (newsList == null)
                    return null;
                else
                    return newsList;
            } else
                return null;

        }

        @Override
        protected void onPostExecute(NewsList result)
        {
            super.onPostExecute(result);
            if (null == result)
            {
                UIUtils.ToastMessage(getActivity().getApplicationContext(), "服务器不稳定,暂时无法加载最新评论!");
            } else
            {
                if (result.getHasnext() == 0)
                {
                    setFootView("加载更多", View.VISIBLE, View.GONE, View.VISIBLE);
                } else
                {
                    setFootView("数据已拉取完毕", View.VISIBLE, View.GONE, View.VISIBLE);
                }

                if (!pageflag.equals("1"))
                {
                    newsList.clear();
                }
                newsList.addAll(result.getInfo());
            }
            adapter.setList(newsList);
            adapter.notifyDataSetChanged();
            pullToRefreshListView.onRefreshComplete("上次更新:" + TimeTool.getListTime(System.currentTimeMillis()));
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putSerializable("newsList", newsList);
    }

    public void setFootView(String text, int tvVisivle, int pbVisivle, int footVisible)
    {
        news_footer_more.setText(text);

        news_footer.setVisibility(footVisible);
        news_footer_more.setVisibility(tvVisivle);
        news_footer_progress.setVisibility(pbVisivle);

    }

}
