package net.weibo.app.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;

import net.weibo.api.InfoImpl;
import net.weibo.app.AppConfig;
import net.weibo.app.AppContext;
import net.weibo.app.AppException;
import net.weibo.app.R;
import net.weibo.app.adapter.HorizontalListViewApapter;
import net.weibo.app.bean.MyInfo;
import net.weibo.app.bean.Result;
import net.weibo.app.widget.HorizontalListView;
import net.weibo.app.widget.LoadingDialog;
import net.weibo.common.FileUtils;
import net.weibo.common.ImageUtils;
import net.weibo.common.StringUtils;
import net.weibo.common.UIUtils;
import net.weibo.constant.Tids;
import net.weibo.dao.LocalHeadPicDBService;
import net.weibo.http.AsyncHttpResponseHandler;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 我的资料Activity
 */
public class InfoActivity extends BaseActivity
{

    private final String              TAG         = "InfoActivity";
    private HorizontalListView        horizontalListView;
    private RelativeLayout            myNameRelativeLayout;
    private LinearLayout              myInfoLinearlayout;
    private LinearLayout              othersLinearLayout;
    private LinearLayout              myWeibosLinearLayout;
    private LinearLayout              myListenersLinearLayout;
    private LinearLayout              heedersLinearLayout;
    private LinearLayout              myAlbumLinearLayout;
    private TextView                  myNameTextView;
    private TextView                  tv_broadcastNums;
    private TextView                  tv_fanNums;
    private TextView                  tv_listenNums;
    private TextView                  tv_topicNum;
    private TextView                  tv_favsNum;
    private TextView                  tv_setting;
    private MyInfo                    myInfo;
    private Stack<Integer>            initArray   = new Stack<Integer>();
    private ArrayList<Bitmap>         headPicList = new ArrayList<Bitmap>();
    private HorizontalListViewApapter adapter;
    private InitInfoThread            initInfoThread;
    private LoadingDialog             loading;

    private final int                 CROP        = 200;

    @SuppressLint("HandlerLeak")
    private Handler                   handler     = new Handler()
                                                  {
                                                      @Override
                                                      public void handleMessage(android.os.Message msg)
                                                      {
                                                          super.handleMessage(msg);
                                                          switch (msg.what)
                                                          {
                                                              case Tids.T_SEARCH_MYINFO:
                                                                  setComponetData();
                                                                  break;
                                                              case Tids.T_SEARCH_HEADPIC:
                                                                  setListViewData();
                                                                  initInfoThread = null;
                                                                  break;
                                                              default:
                                                                  break;
                                                          }
                                                      };
                                                  };

    // 启动线程任务
    private void threadsManager(int id)
    {
        switch (id)
        {
            case Tids.T_INIT_MYINFO:
                if (null != initInfoThread)
                    break;
                initInfoThread = new InitInfoThread();
                initInfoThread.start();
                break;
            default:
                break;
        }
    }

    class InitInfoThread extends Thread
    {
        @Override
        public void run()
        {
            final InfoImpl myInfoImpl = new InfoImpl(appContext);
            if (appContext.isNetworkConnected())
            {
                myInfoImpl.getMyInfoFromNet(new AsyncHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(String content)
                    {
                        try
                        {
                            myInfo = myInfoImpl.parse(content);
                            handler.obtainMessage(Tids.T_SEARCH_MYINFO, null).sendToTarget();
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        headPicList = myInfoImpl.getHeadPic();
                        handler.obtainMessage(Tids.T_SEARCH_HEADPIC, null).sendToTarget();
                    }
                });
            }

        }
    }

    class InitTopicThread extends Thread
    {
        @Override
        public void run()
        {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        initView();
        initData();

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        updateData();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        initArray.clear();
        myInfo = null;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        initArray = null;
        headPicList = null;
        myInfo = null;
        adapter.clearImages();
        adapter = null;
        initInfoThread = null;
    }

    public void initData()
    {
        setComponetData();
        setListViewData();
    }

    private void setComponetData()
    {

        if (myInfo == null)
        {
            InfoImpl infoImpl = new InfoImpl(appContext);
            myInfo = infoImpl.getMyInfoFromLocal();
            if (null != myInfo.getNick())
                if (myInfo.getNick().length() > 10)
                    myInfo.setNick(myInfo.getNick().substring(0, 10) + "...");
            myNameTextView.setText(myInfo.getNick() + " ");
            myNameTextView.append(Html.fromHtml("<font color=\"#7B7B7B\" >(" + myInfo.getName() + ")</font>"));
            tv_broadcastNums.setText("0");
            tv_fanNums.setText("0");
            tv_listenNums.setText("0");
            tv_topicNum.setText("0");
            tv_favsNum.setText("0");
        } else
        {
            if (myInfo.getNick().length() > 10)
                myInfo.setNick(myInfo.getNick().substring(0, 10) + "...");
            myNameTextView.setText(myInfo.getNick() + "   ");
            myNameTextView.append(Html.fromHtml("<font color=\"#7B7B7B\" >(" + myInfo.getName() + ")</font>"));
            tv_broadcastNums.setText(myInfo.getTweetnum() + "");
            tv_fanNums.setText(myInfo.getFansnum());
            tv_listenNums.setText(myInfo.getIdolnum());
            tv_topicNum.setText("0");
            tv_favsNum.setText(myInfo.getFavnum());
        }
    }

    private void setListViewData()
    {
        if (headPicList.size() <= 0)
        {
            headPicList.add(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.wb_head_default)));
        }
        if (headPicList.size() < 6)
        {
            adapter.setFlag(false);
            headPicList.add(ImageUtils.getRoundedCornerBitmap(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.wb_add_avatar_140)), 8));
        } else
        {
            adapter.setFlag(true);
        }
        adapter.setImages(headPicList);
        adapter.notifyDataSetChanged();
    }

    private void updateData()
    {
        addInitList();
        for (int i = 0; i < initArray.size(); i++)
        {
            threadsManager(initArray.get(i));
        }
    }

    private void addInitList()
    {
        initArray.push(Tids.T_INIT_MYINFO);
    }

    private void initView()
    {
        myNameRelativeLayout = (RelativeLayout) findViewById(R.id.myNameRelativeLayout);
        othersLinearLayout = (LinearLayout) findViewById(R.id.othersLinearLayout);
        myInfoLinearlayout = (LinearLayout) findViewById(R.id.myInfoLinearlayout);
        myAlbumLinearLayout = (LinearLayout) findViewById(R.id.myAlbumLinearLayout);
        myNameTextView = (TextView) findViewById(R.id.myNameTextView);
        tv_broadcastNums = (TextView) findViewById(R.id.tv_broadcastNums);
        tv_listenNums = (TextView) findViewById(R.id.tv_listenNums);
        tv_topicNum = (TextView) findViewById(R.id.tv_topicNum);
        tv_fanNums = (TextView) findViewById(R.id.tv_fanNums);
        tv_favsNum = (TextView) findViewById(R.id.tv_favsNum);
        tv_setting = (TextView) findViewById(R.id.imageButton1);
        tv_setting.setVisibility(View.GONE);
        horizontalListView = (HorizontalListView) findViewById(R.id.horizontalListView);

        myWeibosLinearLayout = (LinearLayout) findViewById(R.id.myWeibosLinearLayout);
        myListenersLinearLayout = (LinearLayout) findViewById(R.id.myListenersLinearLayout);
        heedersLinearLayout = (LinearLayout) findViewById(R.id.heedersLinearLayout);

        adapter = new HorizontalListViewApapter(this);
        horizontalListView.setAdapter(adapter);
        loading = new LoadingDialog(this);
        initComponeSize();
        registerListener();
    }

    private void initComponeSize()
    {
        UIUtils.setScreenDisplayMetrics(appContext, horizontalListView, 20, null);
        UIUtils.setScreenDisplayMetrics(appContext, myNameRelativeLayout, 8, null);
        UIUtils.setScreenDisplayMetrics(appContext, myInfoLinearlayout, 20, null);
        UIUtils.setScreenDisplayMetrics(appContext, othersLinearLayout, 30, null);
    }

    private void registerListener()
    {
        // 个人信息
        myNameRelativeLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(InfoActivity.this, InfoEditActivity.class);
                intent.putExtra("nick", AppConfig.getAppConfig(appContext).get("Nick"));
                intent.putExtra("introduction", AppConfig.getAppConfig(appContext).get("Introduction"));

                startActivity(intent);
            }
        });
        // 我的听众
        myListenersLinearLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(InfoActivity.this, ListenersActivity.class);
                intent.putExtra("openId", AppConfig.getAppConfig(appContext).get("openId"));
                startActivity(intent);
            }
        });
        // 微相册
        myAlbumLinearLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(InfoActivity.this, AlbumActivity.class);
                intent.putExtra("Name", AppConfig.getAppConfig(appContext).get("Name"));
                startActivity(intent);
            }
        });
        // 我的微博
        myWeibosLinearLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                BrowserUserActivity.startActivity(InfoActivity.this, AppContext.getInstance().getProperty("Name"));
            }
        });
        // 我收听的人
        heedersLinearLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(InfoActivity.this, MyFollowActivity.class);
                startActivity(intent);
            }
        });
        // 设置按钮
        tv_setting.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                Intent intent = new Intent(InfoActivity.this, SettingActivity.class);
                intent.putExtra("type", false);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
    {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode)
        {
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA:
                startActionCrop(ImageUtils.origUri);// 拍照后裁剪
                break;
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
                startActionCrop(data.getData());// 选图后裁剪
                break;
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD:
                uploadNewPhoto();
                break;
            default:
                break;
        }
    }

    /**
     * 拍照后裁剪
     * 
     * @param data
     *            原始图片
     * @param output
     *            裁剪后图片
     */
    private void startActionCrop(Uri data)
    {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        intent.putExtra("output", this.getUploadTempFile(data));
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", CROP);// 输出图片大小
        intent.putExtra("outputY", CROP);
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        startActivityForResult(intent, ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
    }

    // 裁剪头像的绝对路径
    private Uri getUploadTempFile(Uri uri)
    {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED))
        {
            File savedir = new File(ImageUtils.PHOTO_SAVEPATH);
            if (!savedir.exists())
            {
                savedir.mkdirs();
            }
        } else
        {
            UIUtils.ToastMessage(getApplicationContext(), "无法保存上传的头像，请检查SD卡是否挂载");
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);

        // 如果是标准Uri
        if (StringUtils.isEmpty(thePath))
        {
            thePath = ImageUtils.getAbsoluteImagePath(InfoActivity.this, uri);
        }
        String ext = FileUtils.getFileFormat(thePath);
        // ext = StringUtils.isEmpty(ext) ? "jpg" : ext;
        // 照片命名
        String cropFileName = "QQWeibo_crop_" + timeStamp + ".jpg";
        // 裁剪头像的绝对路径
        ImageUtils.protraitPath = ImageUtils.PHOTO_SAVEPATH + cropFileName;
        ImageUtils.protraitFile = new File(ImageUtils.protraitPath);

        ImageUtils.cropUri = Uri.fromFile(ImageUtils.protraitFile);
        return ImageUtils.cropUri;
    }

    private void uploadNewPhoto()
    {
        final Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                if (msg.what == 1 && msg.obj != null)
                {
                    updateData();
                    UIUtils.ToastMessage(getApplicationContext(), "上传照片成功!");
                    if (loading != null)
                        loading.dismiss();

                } else if (msg.what == -1 && msg.obj != null)
                {
                    ((AppException) msg.obj).makeToast(InfoActivity.this);
                } else
                {
                    UIUtils.ToastMessage(getApplicationContext(), "上传照片失败!");
                }
            }
        };

        if (loading != null)
        {
            loading.setLoadText("正在上传头像···");
            loading.show();
        }
        new Thread()
        {
            @Override
            public void run()
            {
                // 获取头像缩略图
                if (!StringUtils.isEmpty(ImageUtils.protraitPath) && ImageUtils.protraitFile.exists())
                {
                    ImageUtils.protraitBitmap = ImageUtils.loadImgThumbnail(ImageUtils.protraitPath, 200, 200);
                } else
                {
                    loading.setLoadText("图像不存在，上传失败·");
                    loading.dismiss();
                }
                if (ImageUtils.protraitBitmap != null)
                {
                    LocalHeadPicDBService db = new LocalHeadPicDBService(appContext);
                    String[] path = ImageUtils.protraitPath.split("/");
                    db.insert(path[path.length - 1].substring(0, path[path.length - 1].length() - 4), AppConfig.getAppConfig(appContext).get("openId"));
                    InfoImpl infoImpl = new InfoImpl(appContext);
                    infoImpl.showHeadPic(ImageUtils.protraitPath, appContext, new AsyncHttpResponseHandler()
                    {
                        @Override
                        public void onSuccess(String content)
                        {
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = ImageUtils.protraitBitmap;
                            Result result = null;
                            try
                            {
                                result = Result.parse(content);
                            } catch (Exception e)
                            {
                                e.printStackTrace();
                                msg.obj = e;
                                msg.what = -1;
                            }
                            if (null == result || result.getRet() != 0)
                            {
                                msg.what = 0;
                            }
                            handler.sendMessage(msg);

                        }
                    });
                } else
                {
                    loading.setLoadText("图像不存在，上传失败·");
                    loading.dismiss();
                }
            };
        }.start();
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
