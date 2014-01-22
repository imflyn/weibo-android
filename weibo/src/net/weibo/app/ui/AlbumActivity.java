package net.weibo.app.ui;

import java.util.ArrayList;

import net.weibo.api.AlbumImpl;
import net.weibo.app.R;
import net.weibo.app.adapter.AlbumGridViewApapter;
import net.weibo.app.bean.Album;
import net.weibo.app.bean.AlbumList;
import net.weibo.app.bean.Result;
import net.weibo.app.widget.LoadingDialog;
import net.weibo.app.widget.PullToRefreshView;
import net.weibo.app.widget.PullToRefreshView.OnFooterRefreshListener;
import net.weibo.common.UIUtils;
import net.weibo.constant.Tids;
import net.weibo.http.AsyncHttpResponseHandler;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;

public class AlbumActivity extends BaseActivity implements OnFooterRefreshListener
{

    private ImageButton          btn_back;
    private ImageButton          btn_share;
    private Thread               initThread;
    private ArrayList<Album>     list     = new ArrayList<Album>();
    private AlbumGridViewApapter adapter;
    private GridView             gridView;
    private PullToRefreshView    mPullToRefreshView;
    private LoadingDialog        loading;
    private String               pageflag = "0";
    private String               pagetime;
    private String               Name;
    private boolean              hasNext  = false;
    private View                 view;
    private boolean              needRefresh;

    private Handler              handler  = new Handler()
                                          {
                                              @Override
                                              public void handleMessage(android.os.Message msg)
                                              {
                                                  initThread = null;
                                                  if (null != loading)
                                                      loading.dismiss();
                                                  switch (msg.what)
                                                  {
                                                      case Tids.T_LOAD_ALBUM:
                                                          if (msg.arg1 == Tids.T_SUCCESS)
                                                              setAlbum();
                                                          else
                                                              UIUtils.ToastMessage(getApplicationContext(), "加载失败,服务器不稳定请稍后再试!");
                                                          break;
                                                      default:
                                                          break;
                                                  }
                                              };
                                          };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        Intent intent = getIntent();
        Name = intent.getStringExtra("Name");
        initView();
    }

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
        Button button = (Button) findViewById(R.id.btn_tryAgain);
        button.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!appContext.isNetworkConnected())
                {
                    UIUtils.ToastMessage(getApplicationContext(), AlbumActivity.this.getString(R.string.network_not_connected));
                } else
                {
                    view.setVisibility(View.INVISIBLE);
                    view = null;
                    loadAlbum();
                }
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (null == list)
            list = new ArrayList<Album>();
        if (checknet() && list.size() == 0)
            loadAlbum();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
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
            initThread = null;
            handler = null;
            adapter.clear();
            list.clear();
            list = null;
        }

    }

    private void initView()
    {
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_share = (ImageButton) findViewById(R.id.btn_share);

        btn_back.setOnClickListener(clickListener);
        btn_share.setOnClickListener(clickListener);

        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        mPullToRefreshView.setIfUpArrow(true);
        mPullToRefreshView.setIfDownArrow(false);
        mPullToRefreshView.setOnFooterRefreshListener(this);

        gridView = (GridView) findViewById(R.id.gv_album);

        adapter = new AlbumGridViewApapter(this);
        adapter.setList(list);
        gridView.setAdapter(adapter);
        gridView.setClickable(false);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

        gridView.setOnScrollListener(new OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                if (scrollState == 0)
                {
                    needRefresh = true;
                } else
                {
                    needRefresh = false;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                if (!needRefresh)
                    return;
                if (firstVisibleItem == 0)
                {
                }
                if (visibleItemCount + firstVisibleItem == totalItemCount && hasNext)
                {
                    AlbumActivity.this.onFooterRefresh(mPullToRefreshView);
                }
                needRefresh = false;
            }
        });

        loading = new LoadingDialog(this);

    }

    private void setAlbum()
    {
        adapter.setList(list);
        adapter.notifyDataSetChanged();
        mPullToRefreshView.onFooterRefreshComplete();
    }

    private void loadAlbum()
    {
        if (null == initThread)
        {
            if (null != loading)
            {
                if (!loading.isShowing() && list.size() == 0)
                    loading.show();
            }
            final AlbumImpl impl = new AlbumImpl();
            impl.getAlbum(pageflag, pagetime, Name, appContext, new AsyncHttpResponseHandler()
            {
                @Override
                public void onSuccess(String content)
                {
                    try
                    {
                        final Result result = Result.parse(content);
                        if (result.getRet() == 0)
                        {
                            AlbumList albumList = impl.parse(content);
                            hasNext = albumList.isHasNext();
                            if (pageflag.equals("0"))
                            {
                                list = albumList.getAlbums();
                                pageflag = "1";
                            } else
                                list.addAll(albumList.getAlbums());

                            pagetime = albumList.getPagetime();

                            handler.obtainMessage(Tids.T_LOAD_ALBUM, Tids.T_SUCCESS, 0, null).sendToTarget();
                        } else
                        {
                            handler.obtainMessage(Tids.T_LOAD_ALBUM, Tids.T_FAIL, 0, null).sendToTarget();
                        }
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                };

                @Override
                public void onFailure(Throwable error)
                {
                    handler.obtainMessage(Tids.T_LOAD_ALBUM, Tids.T_FAIL, 0, null).sendToTarget();
                };
            });

        }
    }

    private OnClickListener clickListener = new OnClickListener()
                                          {

                                              @Override
                                              public void onClick(View v)
                                              {
                                                  switch (v.getId())
                                                  {
                                                      case R.id.btn_back:
                                                          AlbumActivity.this.finish();
                                                          break;
                                                      default:
                                                          break;
                                                  }
                                              }
                                          };

    @Override
    public void onFooterRefresh(PullToRefreshView view)
    {
        if (UIUtils.isFastDoubleClick())
            return;
        if (!hasNext)
        {
            UIUtils.ToastMessage(getApplicationContext(), "没有更多内容!");
            return;
        }
        if (appContext.isNetworkConnected())
            loadAlbum();
        else
            UIUtils.ToastMessage(getApplicationContext(), getString(R.string.net_busy));
    }

}
