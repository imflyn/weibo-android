package net.weibo.app.ui;

import java.io.File;
import java.io.IOException;

import net.weibo.app.R;
import net.weibo.common.FileUtils;
import net.weibo.common.ImageDownloader;
import net.weibo.common.ImageUtils;
import net.weibo.common.StringUtils;
import net.weibo.common.UIUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

/**
 * 图片对话框
 * 
 */
public class ImageDialog extends BaseActivity
{

    private ViewSwitcher mViewSwitcher;
    private Button       btn_preview;
    private ImageView    mImage;

    private Thread       thread;
    private Handler      handler;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_dialog);

        this.initView();

        this.initData();
    }

    private View.OnTouchListener touchListener = new View.OnTouchListener()
                                               {
                                                   @Override
                                                   public boolean onTouch(View v, MotionEvent event)
                                                   {
                                                       thread.interrupt();
                                                       handler = null;
                                                       finish();
                                                       return true;
                                                   }
                                               };

    private void initView()
    {
        mViewSwitcher = (ViewSwitcher) findViewById(R.id.imagedialog_view_switcher);
        mViewSwitcher.setOnTouchListener(touchListener);

        btn_preview = (Button) findViewById(R.id.imagedialog_preview_button);
        btn_preview.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String imgURL = getIntent().getStringExtra("img_url");
                UIUtils.showImageZoomDialog(v.getContext(), imgURL);
                finish();
            }
        });

        mImage = (ImageView) findViewById(R.id.imagedialog_image);
        mImage.setOnTouchListener(touchListener);
    }

    private void initData()
    {
        final String imgURL = getIntent().getStringExtra("img_url");
        final String ErrMsg = getString(R.string.msg_load_image_fail);
        final String localImg = getIntent().getStringExtra("local_img");
        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                if (msg.what == 1 && msg.obj != null)
                {
                    mImage.setImageBitmap((Bitmap) msg.obj);
                    mViewSwitcher.showNext();
                } else
                {
                    UIUtils.ToastMessage(getApplicationContext(), ErrMsg);
                    finish();
                }
            }
        };
        thread = new Thread()
        {
            @Override
            public void run()
            {
                Message msg = new Message();
                Bitmap bmp = null;
                if (!StringUtils.isEmpty(localImg))
                {
                    bmp = BitmapFactory.decodeFile(localImg);
                    btn_preview.setVisibility(View.GONE);
                }
                String filename = FileUtils.getFileName(imgURL);
                // 读取本地图片
                if (imgURL.endsWith("portrait.gif") || StringUtils.isEmpty(imgURL))
                {
                    bmp = BitmapFactory.decodeStream(mImage.getResources().openRawResource(R.drawable.widget_dface));
                }
                if (bmp == null)
                {
                    // 是否有缓存图片
                    // Environment.getExternalStorageDirectory();返回/sdcard
                    String filepath = getFilesDir() + File.separator + filename;
                    File file = new File(filepath);
                    if (file.exists())
                    {
                        bmp = ImageUtils.getBitmap(mImage.getContext(), filename);
                        if (bmp != null)
                        {
                            // 缩放图片
                            bmp = ImageUtils.reDrawBitMap(ImageDialog.this, bmp);
                        }
                    }
                }
                if (bmp == null)
                {
                    bmp = ImageDownloader.downloadBitmap(imgURL);
                    if (bmp != null)
                    {
                        try
                        {
                            // 写图片缓存
                            ImageUtils.saveImage(mImage.getContext(), filename, bmp);
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        // 缩放图片
                        bmp = ImageUtils.reDrawBitMap(ImageDialog.this, bmp);
                    }
                }
                msg.what = 1;
                msg.obj = bmp;
                if (handler != null && !isInterrupted())
                    handler.sendMessage(msg);
            }
        };
        thread.start();
    }
}
