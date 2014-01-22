package net.weibo.app.ui;

import java.util.ArrayList;

import net.weibo.app.R;
import net.weibo.app.adapter.HorizontalListViewApapter;
import net.weibo.app.asynctask.MyAsyncTask;
import net.weibo.common.UIUtils;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.widget.TextView;

public class SendLetterActivity extends BaseActivity implements View.OnClickListener
{
    private TextView tv_back;
    private TextView tv_title;
    private TextView tv_info;
    private String   name;
    private String   nick;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (null != savedInstanceState)
        {
            // people = (People) savedInstanceState.getSerializable("people");
            // newsList = (ArrayList<News>)
            // savedInstanceState.getSerializable("newsList");
            name = savedInstanceState.getString("name");
            nick = savedInstanceState.getString("nick");
        } else
        {
            Intent intent = getIntent();
            name = intent.getStringExtra("name");
            nick = intent.getStringExtra("nick");

        }
        setContentView(R.layout.activity_letter);
        initView();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        // outState.putSerializable("people", people);
        // outState.putSerializable("newsList", newsList);
        outState.putString("name", name);
        outState.putString("nick", nick);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        // people = (People) savedInstanceState.getSerializable("people");
        // newsList = (ArrayList<News>)
        // savedInstanceState.getSerializable("newsList");
        nick = savedInstanceState.getString("nick");
        name = savedInstanceState.getString("name");
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        // if (newsList.size() <= 0 && Utility.isTaskStopped(loadNewsTask))
        // {
        //
        // loadNewsTask = new LoadNewsTask();
        // loadNewsTask.execute();
        // }

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        // Utility.cancelTasks(loadInfoTask, loadNewsTask);
        // adapter.clear();
        // newsList.clear();
        // people = null;
        // name = "";

    }

    private class LoadInfoTask extends MyAsyncTask<String, String, Boolean>
    {
        private String                    name;
        private HorizontalListViewApapter horizontalAdapter;
        private ArrayList<Bitmap>         list = new ArrayList<Bitmap>();

        LoadInfoTask(String name)
        {
            this.name = name;
            horizontalAdapter = new HorizontalListViewApapter(SendLetterActivity.this);
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

            // InfoImpl infoImpl = new InfoImpl(appContext);
            // people = infoImpl.getUserInfo(name);
            //
            // if (null == people)
            // return false;
            // list.add(ImageUtils.getRoundedCornerBitmap(ImageDownloader.downloadBitmap(people.getHead()
            // + "/120"), 8));

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            super.onPostExecute(result);
            if (result)
            {
                // horizontalAdapter.setImages(list);
                // horizontalListView.setAdapter(horizontalAdapter);
                // horizontalAdapter.notifyDataSetChanged();
                //
                // tv_weibo_count.setText(people.getTweetnum());
                // tv_friends_count.setText(people.getIdolnum());
                // tv_fans_count.setText(people.getFansnum());
                // tv_topics_count.setText(people.getFavnum());
                //
                // if (people.getIsfans())
                // {
                // tv_listen.setText("取消收听");
                // LayoutParams layoutParams = tv_listen.getLayoutParams();
                //
                // layoutParams.width = Utility.dip2px(80);
                // tv_listen.setLayoutParams(layoutParams);
                // } else
                // {
                // tv_listen.setText("收听");
                // LayoutParams layoutParams = tv_listen.getLayoutParams();
                //
                // layoutParams.width = Utility.dip2px(60);
                // tv_listen.setLayoutParams(layoutParams);
                // }

            } else
            {
                UIUtils.ToastMessage(getApplicationContext(), "服务器不稳定请稍后再试!");
            }
        }

    }

    private void initView()
    {
        findView();
        registerListener();

        // if (null == adapter)
        // adapter = new NewsListViewAdapter(this, newsList,
        // pullToRefreshListView);
        // pullToRefreshListView.addFooterView(news_footer);
        // pullToRefreshListView.setAdapter(adapter);
        //
        // UIUtils.setScreenDisplayMetrics(appContext, horizontalListView, 20,
        // null);

        tv_title.setText(nick);
        tv_title.setSingleLine();
        tv_title.setEllipsize(TruncateAt.END);
        tv_back.setText(getString(R.string.back));
        tv_info.setText("资料");

    }

    private void findView()
    {
        tv_back = (TextView) findViewById(R.id.infoButton);
        tv_info = (TextView) findViewById(R.id.saveButton);
        tv_title = (TextView) findViewById(R.id.textView1);

    }

    private void registerListener()
    {
        tv_back.setOnClickListener(this);
        tv_info.setOnClickListener(this);

        // tv_weibo_count.setOnClickListener(this);
        // tv_friends_count.setOnClickListener(this);
        // tv_fans_count.setOnClickListener(this);
        // tv_topics_count.setOnClickListener(this);
        // news_footer.setOnClickListener(new OnClickListener()
        // {
        //
        // @Override
        // public void onClick(View v)
        // {
        // if (Utility.isTaskStopped(loadNewsTask) &&
        // !news_footer_more.getText().equals("数据已拉取完毕"))
        // {
        // News news = newsList.get(newsList.size() - 1);
        // loadNewsTask = new LoadNewsTask();
        // loadNewsTask.execute(String.valueOf(news.getTimestamp()),
        // String.valueOf(news.getId()), "1");
        // }
        //
        // }
        // });
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
                BrowserUserActivity.startActivity(this, name);
                break;
            default:
                break;
        }
    }

    // public void setFootView(String text, int tvVisivle, int pbVisivle, int
    // footVisible)
    // {
    // news_footer_more.setText(text);
    //
    // news_footer.setVisibility(footVisible);
    // news_footer_more.setVisibility(tvVisivle);
    // news_footer_progress.setVisibility(pbVisivle);
    //
    // }

    public static void startActivity(Activity activity, String myName, String nick)
    {

        Intent intent = new Intent(activity, SendLetterActivity.class);
        intent.putExtra("name", myName);
        intent.putExtra("nick", nick);
        activity.startActivity(intent);
    }
}
