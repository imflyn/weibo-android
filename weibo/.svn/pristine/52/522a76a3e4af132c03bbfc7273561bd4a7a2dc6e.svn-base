package net.weibo.app.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import net.weibo.api.InfoImpl;
import net.weibo.app.AppConfig;
import net.weibo.app.AppException;
import net.weibo.app.R;
import net.weibo.app.adapter.PhotoGridViewApapter;
import net.weibo.app.bean.Result;
import net.weibo.app.widget.LoadingDialog;
import net.weibo.common.FileUtils;
import net.weibo.common.ImageUtils;
import net.weibo.common.StringUtils;
import net.weibo.common.UIUtils;
import net.weibo.constant.Tids;
import net.weibo.dao.LocalHeadPicDBService;
import net.weibo.http.AsyncHttpResponseHandler;

import org.json.JSONException;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

/**
 * 修改我的资料Activity
 */
public class InfoEditActivity extends BaseActivity implements OnClickListener
{

    private final String                       TAG         = "InfoEditActivity";
    private GridView                           headpicgrid;
    // private LinearLayout gridLinearLayout;
    // private LinearLayout infoLinearLayout;
    private TextView                           saveButton;
    private TextView                           infoButton;
    // private TextView textView1;
    // private TextView textView2;
    private EditText                           editText1;
    private EditText                           editText2;
    private ArrayList<HashMap<String, Object>> headPicList = new ArrayList<HashMap<String, Object>>();
    private PhotoGridViewApapter               adapter;
    private Thread                             t;
    private LoadingDialog                      loading;
    private final int                          CROP        = 200;
    public static boolean                      ifUpload    = false;

    private Handler                            handler     = new Handler()
                                                           {
                                                               @Override
                                                               public void handleMessage(android.os.Message msg)
                                                               {
                                                                   switch (msg.what)
                                                                   {
                                                                       case Tids.T_SEARCH_HEADPIC:
                                                                           setListViewData();
                                                                           t = null;
                                                                           break;
                                                                   }
                                                               };
                                                           };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo_eidt);
        initView();
        initViewData();
    }

    private void initView()
    {
        // gridLinearLayout = (LinearLayout)
        // findViewById(R.id.gridLinearLayout);
        // infoLinearLayout = (LinearLayout)
        // findViewById(R.id.infoLinearLayout);
        headpicgrid = (GridView) findViewById(R.id.headpicgrid);
        saveButton = (TextView) findViewById(R.id.saveButton);
        infoButton = (TextView) findViewById(R.id.infoButton);
        // textView2 = (TextView) findViewById(R.id.textView2);
        // textView1 = (TextView) findViewById(R.id.textView1);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);

        infoButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        loading = new LoadingDialog(this);
        adapter = new PhotoGridViewApapter(this);
        adapter.setImages(headPicList);
        headpicgrid.setAdapter(adapter);
        headpicgrid.setEnabled(false);
    }

    private void initViewData()
    {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        editText1.setText(bundle.getString("nick"));
        editText2.setText(bundle.getString("introduction"));
        setListViewData();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        updateData();
    }

    private void updateData()
    {
        if (null == t)
        {
            t = new Thread()
            {
                @Override
                public void run()
                {
                    final InfoImpl myInfoImpl = new InfoImpl(appContext);
                    headPicList = myInfoImpl.getHeadPic2();
                    handler.obtainMessage(Tids.T_SEARCH_HEADPIC, headPicList).sendToTarget();

                }
            };
            t.start();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        headPicList.clear();
        adapter.clearImages();
        headPicList = null;
        adapter = null;
        // if(!ImageUtils.protraitBitmap.isRecycled())
        // {
        // ImageUtils.protraitBitmap.recycle();
        // }
    }

    private void setListViewData()
    {
        HashMap<String, Object> map = null;
        if (headPicList == null || adapter == null)
        {
            return;
        }
        if (headPicList.size() <= 0)
        {
            map = new HashMap<String, Object>();
            map.put("pic", BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.wb_head_default)));
            map.put("url", null);
            headPicList.add(map);
        }
        if (headPicList.size() < 6)
        {
            map = new HashMap<String, Object>();
            map.put("pic", ImageUtils.getRoundedCornerBitmap(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.wb_add_avatar_140)), 8));
            map.put("url", null);
            headPicList.add(map);
            adapter.setFlag(false);
        } else
        {
            adapter.setFlag(true);
        }
        adapter.setImages(headPicList);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.infoButton:
                this.finish();
                break;
            case R.id.saveButton:
                if (UIUtils.isFastDoubleClick())
                    return;
                if (!appContext.isNetworkConnected())
                {
                    UIUtils.ToastMessage(getApplicationContext(), getString(R.string.network_not_connected));
                    break;
                }
                final Handler handler = new Handler()
                {
                    @Override
                    public void handleMessage(Message msg)
                    {
                        if (msg.what == 1)
                        {
                            if (loading != null)
                                loading.dismiss();
                            UIUtils.ToastMessage(getApplicationContext(), "更新信息成功!");
                        } else if (msg.what == -1 && msg.obj != null)
                        {
                            ((AppException) msg.obj).makeToast(InfoEditActivity.this);
                        } else
                        {
                            UIUtils.ToastMessage(getApplicationContext(), "更新信息失败!");
                            if (loading != null)
                                loading.dismiss();
                        }
                    }
                };
                if (loading != null)
                {
                    loading.setLoadText("正在更新信息···");
                    loading.show();
                }
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        final InfoImpl infoImpl = new InfoImpl(appContext);
                        infoImpl.updateInfo(editText1.getText() + "", editText2.getText() + "", appContext, new AsyncHttpResponseHandler()
                        {
                            @Override
                            public void onSuccess(String content)
                            {
                                Message msg = new Message();
                                msg.what = 1;
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
                                } else
                                {
                                    try
                                    {
                                        infoImpl.parse(content);
                                    } catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                                handler.sendMessage(msg);
                            }
                        });
                    };
                }.start();
                break;
        }
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
            thePath = ImageUtils.getAbsoluteImagePath(InfoEditActivity.this, uri);
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
                    ((AppException) msg.obj).makeToast(InfoEditActivity.this);
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
                    String[] path = ImageUtils.protraitPath.split("/");
                    String url = path[path.length - 1].substring(0, path[path.length - 1].length() - 4);
                    if (ifUpload)
                    {
                        final InfoImpl infoImpl = new InfoImpl(appContext);
                        infoImpl.updateHeadPic(url, appContext, new AsyncHttpResponseHandler()
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
                                } else
                                {
                                    try
                                    {
                                        infoImpl.parse(content);
                                        headPicList.remove(0);
                                        HashMap<String, Object> map = new HashMap<String, Object>();
                                        map.put("url", ImageUtils.protraitPath);
                                        map.put("pic", ImageUtils.protraitBitmap);
                                        headPicList.add(0, map);
                                    } catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                                handler.sendMessage(msg);
                            };
                        });
                    } else
                    {
                        LocalHeadPicDBService db = new LocalHeadPicDBService(InfoEditActivity.this);
                        db.insert(url, AppConfig.getAppConfig(InfoEditActivity.this).get("openId"));
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
                    }
                    ifUpload = false;
                } else
                {
                    loading.setLoadText("图像不存在，上传失败·");
                    loading.dismiss();
                }
            };
        }.start();
    }
}
