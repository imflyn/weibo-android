package net.weibo.app.ui;

import java.util.ArrayList;

import net.weibo.api.NewsImpl;
import net.weibo.app.AppContext;
import net.weibo.app.R;
import net.weibo.app.adapter.CommentsListViewApapter;
import net.weibo.app.asynctask.MyAsyncTask;
import net.weibo.app.bean.News;
import net.weibo.app.bean.NewsData;
import net.weibo.app.bean.NewsList;
import net.weibo.app.lib.LongClickableLinkMovementMethod;
import net.weibo.app.lib.MyURLSpan;
import net.weibo.app.widget.PullToRefreshListView;
import net.weibo.app.widget.TimeLineAvatarImageView;
import net.weibo.app.widget.WeiboDetailImageView;
import net.weibo.common.AsyncImageLoader;
import net.weibo.common.FileCache;
import net.weibo.common.ImageDownloader;
import net.weibo.common.StringUtils;
import net.weibo.common.TimeTool;
import net.weibo.common.Utility;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.text.Layout;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 */
public class BrowserNewsActivity extends BaseActivity implements OnClickListener
{

    private News                    news;
    private ArrayList<News>         comments = new ArrayList<News>();
    private TextView                username;
    private TextView                content;
    private TextView                recontent;
    private TextView                time;
    private TextView                source;

    private TimeLineAvatarImageView avatar;
    private WeiboDetailImageView    content_pic;
    private GridLayout              content_pic_multi;
    private WeiboDetailImageView    repost_pic;
    private GridLayout              repost_pic_multi;

    private LinearLayout            repost_layout;

    private TextView                comment_count;
    private TextView                repost_count;
    private LoadDataTask            loadDataTask;
    private LoadPicTask             loadPicTask;
    private LoadReCountTask         loadReCountTask;
    private LoadCommentsTask        loadCommentsTask;
    private PullToRefreshListView   pullToRefreshListView;
    private CommentsListViewApapter adapter;
    private View                    news_footer;
    private TextView                news_footer_more;
    private ProgressBar             news_footer_progress;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
        {
            news = (News) savedInstanceState.getSerializable("news");
            comments = (ArrayList<News>) savedInstanceState.getSerializable("comments");
        } else
        {
            Intent intent = getIntent();
            news = (News) intent.getSerializableExtra("news");
            comments.clear();
        }

        setContentView(R.layout.activity_news);
        initView();
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        news = (News) intent.getSerializableExtra("news");

        setContentView(R.layout.activity_news);
        initView();
        initData();
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
        Utility.cancelTasks(loadDataTask, loadPicTask, loadCommentsTask, loadReCountTask);
        adapter.clear();
        avatar.setImageBitmap(null);
        content_pic.setImageBitmap(null);
        repost_pic.setImageBitmap(null);
        Utility.recycleViewGroupAndChildViews(content_pic_multi, true);
        Utility.recycleViewGroupAndChildViews(repost_pic_multi, true);

    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putSerializable("news", news);
        outState.putSerializable("comments", comments);
    }

    private void initView()
    {
        ((TextView) findViewById(R.id.infoButton)).setText(getString(R.string.back));
        ((TextView) findViewById(R.id.infoButton)).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                BrowserNewsActivity.this.finish();
            }
        });

        ((TextView) findViewById(R.id.textView1)).setText(getString(R.string.detail));

        username = (TextView) findViewById(R.id.username);
        content = (TextView) findViewById(R.id.content);
        content.setOnTouchListener(onTouchListener);
        recontent = (TextView) findViewById(R.id.repost_content);
        recontent.setOnTouchListener(onTouchListener);

        time = (TextView) findViewById(R.id.time);
        source = (TextView) findViewById(R.id.source);

        comment_count = (TextView) findViewById(R.id.comment_count);
        repost_count = (TextView) findViewById(R.id.repost_count);

        avatar = (TimeLineAvatarImageView) findViewById(R.id.avatar);
        content_pic = (WeiboDetailImageView) findViewById(R.id.content_pic);
        content_pic_multi = (GridLayout) findViewById(R.id.content_pic_multi);
        content_pic_multi.setColumnCount(3);
        content_pic_multi.setRowCount(3);

        repost_pic = (WeiboDetailImageView) findViewById(R.id.repost_content_pic);
        repost_pic_multi = (GridLayout) findViewById(R.id.repost_content_pic_multi);
        repost_pic_multi.setColumnCount(3);
        repost_pic_multi.setRowCount(3);

        repost_layout = (LinearLayout) findViewById(R.id.repost_layout);

        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView);
        pullToRefreshListView.setIfDownArrow(false);

        news_footer = getLayoutInflater().inflate(R.layout.listview_footer, null);
        news_footer_more = (TextView) news_footer.findViewById(R.id.listview_foot_more);
        news_footer_progress = (ProgressBar) news_footer.findViewById(R.id.listview_foot_progress);

        if (null == adapter)
        {
            adapter = new CommentsListViewApapter(this, comments, pullToRefreshListView);
            pullToRefreshListView.addFooterView(news_footer);
        }

        pullToRefreshListView.setAdapter(adapter);

        news_footer.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Utility.isTaskStopped(loadCommentsTask) && news_footer_more.getText().equals("加载更多"))
                {
                    setFootView("正在加载", View.VISIBLE, View.VISIBLE, View.VISIBLE);
                    News comment = comments.get(comments.size() - 1);
                    loadCommentsTask = new LoadCommentsTask();
                    loadCommentsTask.execute(news.getId(), "1", String.valueOf(comment.getTimestamp()), comment.getId());
                }
            }
        });

        findViewById(R.id.btn_repost).setOnClickListener(this);
        findViewById(R.id.btn_comment).setOnClickListener(this);
        findViewById(R.id.btn_more).setOnClickListener(this);

        avatar.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                BrowserUserActivity.startActivity(BrowserNewsActivity.this, news.getName());
            }
        });

        username.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                BrowserUserActivity.startActivity(BrowserNewsActivity.this, news.getName());
            }
        });

    }

    private void initData()
    {
        if (Utility.isTaskStopped(loadDataTask))
        {
            loadDataTask = new LoadDataTask();
            loadDataTask.execute();
        }
        if (Utility.isTaskStopped(loadReCountTask))
        {
            loadReCountTask = new LoadReCountTask();
            loadReCountTask.execute();
        }
        if (Utility.isTaskStopped(loadPicTask))
        {
            loadPicTask = new LoadPicTask();
            loadPicTask.execute(news);
        }
        if (Utility.isTaskStopped(loadCommentsTask))
        {
            loadCommentsTask = new LoadCommentsTask();
            loadCommentsTask.execute(news.getId(), "0", "", "");
        }
    }

    /**
     * 加载数据任务
     * 
     * @author V
     */
    private class LoadDataTask extends MyAsyncTask<News, Integer, Boolean>
    {
        private AsyncImageLoader loader;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            loader = new AsyncImageLoader(AppContext.getInstance());
        }

        @Override
        protected Boolean doInBackground(News... params)
        {
            if (null != news)
                return true;
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            super.onPostExecute(result);

            if (isCancelled())
            {
                return;
            }

            if (!result)
                return;
            loader.loadBitmap(avatar, news.getHead() + "/50", 2);
            username.setText(news.getNick());
            content.setText(news.getListViewSpannableString(BrowserNewsActivity.this));
            if (StringUtils.isEmpty(news.getText()))
                content.setText("转发微博");

            time.setText(TimeTool.getListTime(Long.parseLong(news.getTimestamp() + "000")));
            source.setText(news.getFrom());
            comment_count.setText(String.valueOf(news.getMcount()));
            repost_count.setText(String.valueOf(news.getCount()));

            if (news.getSource() != null)
            {
                repost_layout.setVisibility(View.VISIBLE);
                recontent.setText(news.getSource().getListViewSpannableString(BrowserNewsActivity.this));
            } else
                repost_layout.setVisibility(View.GONE);
        }

        @Override
        protected void onCancelled()
        {
            super.onCancelled();
            loader.destroy();
        }
    }

    /**
     * 加载转发数量任务
     * 
     * @author V
     */
    private class LoadReCountTask extends MyAsyncTask<Void, String, int[]>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected int[] doInBackground(Void... params)
        {
            if (null != news)
            {
                if (AppContext.getInstance().isNetworkConnected())
                {
                    NewsImpl newsImpl = new NewsImpl();
                    int[] counts = newsImpl.getReCount(news.getId(), "2");
                    if (null != counts)
                    {
                        news.setCount(counts[0]);
                        news.setMcount(counts[1]);
                        Intent intent = new Intent();
                        intent.putExtra("news", news);
                        setResult(0, intent);
                    }
                    return counts;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(int[] result)
        {
            super.onPostExecute(result);

            if (isCancelled())
            {
                return;
            }

            if (null == result)
                return;
            comment_count.setText(String.valueOf(result[1]));
            repost_count.setText(String.valueOf(result[0]));

        }

    }

    /**
     * 加载评论任务
     * 
     * @author V
     */
    private class LoadCommentsTask extends MyAsyncTask<String, Object, NewsList>
    {
        private String rootid    = "";
        private String flag      = "2";
        private String pageflag  = "0";
        private String pagetime  = "";
        private String reqnum    = "10";
        private String twitterid = "";

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            setFootView("正在加载...", View.VISIBLE, View.VISIBLE, View.VISIBLE);
        }

        @Override
        protected NewsList doInBackground(String... params)
        {
            if (AppContext.getInstance().isNetworkConnected())
            {
                rootid = params[0];
                pageflag = params[1];
                pagetime = params[2];
                twitterid = params[3];
                NewsImpl newsImpl = new NewsImpl();
                NewsData newsData = newsImpl.getRepostList(flag, rootid, pageflag, pagetime, reqnum, twitterid);
                if (null == newsData)
                    return null;
                return newsData.getData();
            } else
                return null;
        }

        @Override
        protected void onPostExecute(NewsList newsList)
        {
            super.onPostExecute(newsList);

            if (isCancelled())
                return;

            if (null == newsList)
            {
                setFootView("消息已拉取完毕", View.VISIBLE, View.GONE, View.VISIBLE);
                adapter.notifyDataSetChanged();
                return;
            }
            if (newsList.getHasnext() == 0)
            {
                setFootView("加载更多", View.VISIBLE, View.GONE, View.VISIBLE);
            } else
            {
                setFootView("消息已拉取完毕", View.VISIBLE, View.GONE, View.VISIBLE);
            }
            if (newsList.getTotalnum() <= 0)
            {
                comments.clear();
            } else
            {
                comments.addAll(newsList.getInfo());
                adapter.setList(comments);
            }
            pullToRefreshListView.setSure(true);
            adapter.notifyDataSetChanged();
        }

    }

    /**
     * 加载图片任务
     * 
     * @author V
     * 
     */
    private class LoadPicTask extends MyAsyncTask<News, SparseArray<Bitmap>, Boolean>
    {
        private String[]            images;
        private String[]            ogiImages;
        private SparseArray<Bitmap> bitmaps    = new SparseArray<Bitmap>();
        private SparseArray<Bitmap> ogiBitmaps = new SparseArray<Bitmap>();

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            if (news.getImage() != null)
                content_pic.setVisibility(View.VISIBLE);
            if (news.getSource() != null)
            {
                if (news.getSource().getImage() != null)
                    repost_pic.setVisibility(View.VISIBLE);
            }
            repost_pic.getProgressBar().setVisibility(View.VISIBLE);
            repost_pic.getRetryButton().setVisibility(View.GONE);
            content_pic.getProgressBar().setVisibility(View.VISIBLE);
            content_pic.getRetryButton().setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(News... params)
        {
            boolean hasNet = AppContext.getInstance().isNetworkConnected();
            FileCache fileCache = null;
            if (!hasNet)
                fileCache = new FileCache();

            if (null != params[0])
            {
                news = params[0];
                images = news.getImage();
                if (null != news.getSource())
                    ogiImages = news.getSource().getImage();
            }
            if (images != null)
            {
                int length = images.length;
                for (int i = 0; i < length; i++)
                {
                    if (i == 0)
                    {
                        if (hasNet)
                            bitmaps.put(0, ImageDownloader.downloadBitmap(images[i] + "/500"));
                        else
                            bitmaps.put(0, fileCache.getImage(images[0]));
                    } else
                    {
                        if (hasNet)
                            bitmaps.put(i, ImageDownloader.downloadBitmap(images[i] + "/full"));
                        else
                            bitmaps.put(0, fileCache.getImage(images[i]));

                    }
                }

            }
            if (ogiImages != null)
            {
                int length = ogiImages.length;
                for (int i = 0; i < length; i++)
                {
                    if (i == 0)
                    {
                        if (hasNet)
                            ogiBitmaps.put(0, ImageDownloader.downloadBitmap(ogiImages[i] + "/500"));
                        else
                            ogiBitmaps.put(0, fileCache.getImage(images[0]));
                    } else
                    {
                        if (hasNet)
                            ogiBitmaps.put(i, ImageDownloader.downloadBitmap(ogiImages[i] + "/full"));
                        else
                            ogiBitmaps.put(0, fileCache.getImage(images[i]));

                    }
                }
            }
            publishProgress(bitmaps, ogiBitmaps);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            super.onPostExecute(result);
            if (isCancelled())
                return;

            if (result)
            {
                repost_pic.getProgressBar().setVisibility(View.GONE);
                content_pic.getProgressBar().setVisibility(View.GONE);
            }

        }

        @Override
        protected void onProgressUpdate(SparseArray<Bitmap>... values)
        {
            super.onProgressUpdate(values);
            if (null == values[0] && images != null)
                content_pic.getRetryButton().setVisibility(View.VISIBLE);
            else
            {
                buildPicture();
            }
            if (null == values[1] && ogiImages != null)
                repost_pic.getRetryButton().setVisibility(View.VISIBLE);
            else
            {
                buildRepostPic();
            }

        }

        private void buildPicture()
        {
            if (null != bitmaps)
            {
                int count = bitmaps.size();

                if (count < 2)
                {
                    content_pic.setVisibility(View.VISIBLE);
                    content_pic.setImageBitmap(bitmaps.get(0));
                    content_pic.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            showBigPicture(images, 0);
                        }
                    });
                } else
                {
                    ImageView pic;
                    content_pic_multi.setVisibility(View.VISIBLE);
                    for (int i = 0; i < count; i++)
                    {
                        final int finalI = i;
                        Bitmap bitmap = bitmaps.get(i);
                        pic = (ImageView) content_pic_multi.getChildAt(i);
                        pic.setImageBitmap(bitmap);
                        content_pic_multi.getChildAt(i).setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                showBigPicture(images, finalI);
                            }
                        });

                    }

                }

                if (count < 9)
                {
                    ImageView pic;
                    switch (count)
                    {
                        case 8:
                            pic = (ImageView) content_pic_multi.getChildAt(8);
                            pic.setVisibility(View.INVISIBLE);
                            break;
                        case 7:
                            for (int i = 8; i > 6; i--)
                            {
                                pic = (ImageView) content_pic_multi.getChildAt(i);
                                pic.setVisibility(View.INVISIBLE);
                            }
                            break;
                        case 6:
                            for (int i = 8; i > 5; i--)
                            {
                                pic = (ImageView) content_pic_multi.getChildAt(i);
                                pic.setVisibility(View.GONE);
                            }

                            break;
                        case 5:
                            for (int i = 8; i > 5; i--)
                            {
                                pic = (ImageView) content_pic_multi.getChildAt(i);
                                pic.setVisibility(View.GONE);
                            }
                            pic = (ImageView) content_pic_multi.getChildAt(5);
                            pic.setVisibility(View.INVISIBLE);
                            break;
                        case 4:
                            for (int i = 8; i > 5; i--)
                            {
                                pic = (ImageView) content_pic_multi.getChildAt(i);
                                pic.setVisibility(View.GONE);
                            }
                            pic = (ImageView) content_pic_multi.getChildAt(5);
                            pic.setVisibility(View.INVISIBLE);
                            pic = (ImageView) content_pic_multi.getChildAt(4);
                            pic.setVisibility(View.INVISIBLE);
                            break;
                        case 3:
                            for (int i = 8; i > 2; i--)
                            {
                                pic = (ImageView) content_pic_multi.getChildAt(i);
                                pic.setVisibility(View.GONE);
                            }
                            break;
                        case 2:
                            for (int i = 8; i > 2; i--)
                            {
                                pic = (ImageView) content_pic_multi.getChildAt(i);
                                pic.setVisibility(View.GONE);
                            }
                            pic = (ImageView) content_pic_multi.getChildAt(2);
                            pic.setVisibility(View.INVISIBLE);
                            break;
                        default:
                            content_pic_multi.setVisibility(View.GONE);
                            break;

                    }

                }

            } else
            {
                content_pic_multi.setVisibility(View.GONE);
            }
        }

        private void buildRepostPic()
        {
            if (null != ogiImages)
            {
                int count = ogiBitmaps.size();

                if (count < 2)
                {

                    repost_pic.setVisibility(View.VISIBLE);
                    repost_pic.setImageBitmap(ogiBitmaps.get(0));
                    repost_pic.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            showBigPicture(ogiImages, 0);
                        }
                    });
                } else
                {
                    ImageView pic;
                    repost_pic_multi.setVisibility(View.VISIBLE);
                    for (int i = 0; i < count; i++)
                    {
                        final int finalI = i;
                        pic = (ImageView) repost_pic_multi.getChildAt(i);
                        pic.setImageBitmap(ogiBitmaps.get(i));
                        repost_pic_multi.getChildAt(i).setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                showBigPicture(ogiImages, finalI);
                            }
                        });

                    }

                }

                if (count < 9)
                {
                    ImageView pic;
                    switch (count)
                    {
                        case 8:
                            pic = (ImageView) repost_pic_multi.getChildAt(8);
                            pic.setVisibility(View.INVISIBLE);
                            break;
                        case 7:
                            for (int i = 8; i > 6; i--)
                            {
                                pic = (ImageView) repost_pic_multi.getChildAt(i);
                                pic.setVisibility(View.INVISIBLE);
                            }
                            break;
                        case 6:
                            for (int i = 8; i > 5; i--)
                            {
                                pic = (ImageView) repost_pic_multi.getChildAt(i);
                                pic.setVisibility(View.GONE);
                            }

                            break;
                        case 5:
                            for (int i = 8; i > 5; i--)
                            {
                                pic = (ImageView) repost_pic_multi.getChildAt(i);
                                pic.setVisibility(View.GONE);
                            }
                            pic = (ImageView) repost_pic_multi.getChildAt(5);
                            pic.setVisibility(View.INVISIBLE);
                            break;
                        case 4:
                            for (int i = 8; i > 5; i--)
                            {
                                pic = (ImageView) repost_pic_multi.getChildAt(i);
                                pic.setVisibility(View.GONE);
                            }
                            pic = (ImageView) repost_pic_multi.getChildAt(5);
                            pic.setVisibility(View.INVISIBLE);
                            pic = (ImageView) repost_pic_multi.getChildAt(4);
                            pic.setVisibility(View.INVISIBLE);
                            break;
                        case 3:
                            for (int i = 8; i > 2; i--)
                            {
                                pic = (ImageView) repost_pic_multi.getChildAt(i);
                                pic.setVisibility(View.GONE);
                            }
                            break;
                        case 2:
                            for (int i = 8; i > 2; i--)
                            {
                                pic = (ImageView) repost_pic_multi.getChildAt(i);
                                pic.setVisibility(View.GONE);
                            }
                            pic = (ImageView) repost_pic_multi.getChildAt(2);
                            pic.setVisibility(View.INVISIBLE);
                            break;
                        default:
                            repost_pic_multi.setVisibility(View.GONE);
                            break;
                    }
                }
            } else
            {
                repost_pic_multi.setVisibility(View.GONE);
            }
        }
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener()
                                                 {
                                                     @Override
                                                     public boolean onTouch(View v, MotionEvent event)
                                                     {

                                                         Layout layout = ((TextView) v).getLayout();

                                                         int x = (int) event.getX();
                                                         int y = (int) event.getY();
                                                         int offset = 0;
                                                         if (layout != null)
                                                         {

                                                             int line = layout.getLineForVertical(y);
                                                             offset = layout.getOffsetForHorizontal(line, x);
                                                         }

                                                         TextView tv = (TextView) v;
                                                         SpannableString value = SpannableString.valueOf(tv.getText());
                                                         MyURLSpan[] urlSpans = value.getSpans(0, value.length(), MyURLSpan.class);
                                                         boolean result = false;
                                                         int start = 0;
                                                         int end = 0;
                                                         for (MyURLSpan urlSpan : urlSpans)
                                                         {
                                                             start = value.getSpanStart(urlSpan);
                                                             end = value.getSpanEnd(urlSpan);
                                                             if (start <= offset && offset <= end)
                                                             {
                                                                 result = true;
                                                                 break;
                                                             }
                                                         }

                                                         if (result)
                                                         {
                                                             return LongClickableLinkMovementMethod.getInstance().onTouchEvent(tv, value, event);
                                                         } else
                                                         {
                                                             return false;
                                                         }

                                                     }
                                                 };

    private void showBigPicture(String[] images, int position)
    {
        Intent intent = new Intent(this, GalleryActivity.class);
        intent.putExtra("images", images);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    public void setFootView(String text, int tvVisivle, int pbVisivle, int footVisible)
    {
        news_footer_more.setText(text);
        news_footer.setVisibility(footVisible);
        news_footer_more.setVisibility(tvVisivle);
        news_footer_progress.setVisibility(pbVisivle);
    }

    @Override
    public void onClick(View v)
    {
        String text = "";
        String reId = "";
        switch (v.getId())
        {
            case R.id.btn_repost:
                text = "||@" + news.getName() + ":" + news.getText();
                reId = news.getId();
                ReposeNewsActivity.turnToReposeNews(this, reId, text);
                break;
            case R.id.btn_comment:
                if (TextUtils.isEmpty(news.getText()))
                {
                    if (news.getSource() != null)
                    {
                        text = news.getSource().getText();
                    }
                } else
                {
                    text = news.getText();
                }
                reId = news.getId();
                CommentNewsActivity.turnToReposeNews(this, reId, text);

                break;
        }
    }

}
