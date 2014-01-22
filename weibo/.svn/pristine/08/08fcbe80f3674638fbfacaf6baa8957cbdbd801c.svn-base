package net.weibo.app.ui;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import net.weibo.app.AppContext;
import net.weibo.app.R;
import net.weibo.app.asynctask.MyAsyncTask;
import net.weibo.app.widget.CircleProgressView;
import net.weibo.common.ImageDownloader;
import net.weibo.common.ImageUtils;
import net.weibo.common.UIUtils;
import net.weibo.common.Utility;
import uk.co.senab.photoview.PhotoView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class GalleryActivity extends BaseActivity
{

    private TextView                                   tv_position;
    private TextView                                   tv_sum;
    private ViewPager                                  pager;
    private ImageButton                                btn_save;
    private HashSet<ViewGroup>                         unRecycledViews = new HashSet<ViewGroup>();
    private Bitmap                                     bitmap;
    private HashMap<String, PicSimpleBitmapWorkerTask> taskMap         = new HashMap<String, PicSimpleBitmapWorkerTask>();

    private static String[]                            urls;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.activity_gallery);
        Intent intent = getIntent();
        urls = intent.getExtras().getStringArray("images");
        initView();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        for (String url : urls)
        {
            MyAsyncTask task = taskMap.get(url);
            if (task != null)
                task.cancel(true);
        }
        Utility.recycleViewGroupAndChildViews(pager, true);
        for (ViewGroup viewGroup : unRecycledViews)
        {
            Utility.recycleViewGroupAndChildViews(viewGroup, true);
        }
        taskMap.clear();
        urls = null;
        bitmap = null;
        System.gc();

    }

    private void initView()
    {
        int position = getIntent().getIntExtra("position", 0);

        tv_position = (TextView) findViewById(R.id.tv_position);
        tv_position.setText(String.valueOf(position + 1));

        btn_save = (ImageButton) findViewById(R.id.btn_save);
        tv_sum = (TextView) findViewById(R.id.tv_sum);
        tv_sum.setText(String.valueOf(urls.length));

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new ImagePagerAdapter());
        pager.setCurrentItem(position);
        pager.setOffscreenPageLimit(1);

        registerListener();
    }

    private void registerListener()
    {
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                super.onPageSelected(position);
                tv_position.setText(String.valueOf(position + 1));
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                try
                {
                    if (null == bitmap)
                        return;
                    String saveImagePath = ((AppContext) getApplication()).getSaveImagePath();
                    String url = saveImagePath + File.separator + ImageUtils.getTempFileName();
                    ImageUtils.saveImageToSD(GalleryActivity.this, url + ".jpg", bitmap, 100);
                    UIUtils.ToastMessage(getApplicationContext(), "已将图片保存到" + url);
                } catch (IOException e)
                {
                    e.printStackTrace();
                    UIUtils.ToastMessage(getApplicationContext(), "保存失败");
                }
            }
        });

    }

    private class ImagePagerAdapter extends PagerAdapter
    {

        private LayoutInflater inflater;

        public ImagePagerAdapter()
        {
            inflater = getLayoutInflater();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            if (object instanceof ViewGroup)
            {
                ((ViewPager) container).removeView((View) object);
                unRecycledViews.remove(object);
                ViewGroup viewGroup = (ViewGroup) object;
                Utility.recycleViewGroupAndChildViews(viewGroup, true);
            } else
                ((ViewPager) container).removeView((View) object);
        }

        @Override
        public int getCount()
        {
            return urls.length;
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position)
        {
            View contentView = inflater.inflate(R.layout.activity_gallery_item, view, false);

            handlePage(position, contentView);

            ((ViewPager) view).addView(contentView, 0);
            unRecycledViews.add((ViewGroup) contentView);
            return contentView;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object)
        {
            super.setPrimaryItem(container, position, object);
            View contentView = (View) object;
            if (contentView == null)
                return;

            ImageView imageView = (ImageView) contentView.findViewById(R.id.image);

            if (imageView.getDrawable() != null)
                return;

            handlePage(position, contentView);
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view.equals(object);
        }

    }

    private void handlePage(int position, View contentView)
    {
        PhotoView imageView = (PhotoView) contentView.findViewById(R.id.image);
        imageView.setVisibility(View.VISIBLE);

        WebView gif = (WebView) contentView.findViewById(R.id.gif);
        gif.setBackgroundColor(getResources().getColor(R.color.transparent));
        gif.setVisibility(View.INVISIBLE);

        WebView large = (WebView) contentView.findViewById(R.id.large);
        large.setBackgroundColor(getResources().getColor(R.color.transparent));
        large.setVisibility(View.INVISIBLE);
        large.setOverScrollMode(View.OVER_SCROLL_NEVER);

        TextView wait = (TextView) contentView.findViewById(R.id.wait);

        TextView readError = (TextView) contentView.findViewById(R.id.error);

        String path = urls[position];

        final CircleProgressView spinner = (CircleProgressView) contentView.findViewById(R.id.loading);
        spinner.setVisibility(View.VISIBLE);

        if (taskMap.get(path) == null)
        {
            wait.setVisibility(View.VISIBLE);
            PicSimpleBitmapWorkerTask task = new PicSimpleBitmapWorkerTask(imageView, gif, large, spinner, wait, readError, path, taskMap);
            taskMap.put(path, task);
            task.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
        } else
        {
            PicSimpleBitmapWorkerTask task = taskMap.get(path);
            task.setWidget(imageView, gif, spinner, wait, readError);
        }

    }

    private class PicSimpleBitmapWorkerTask extends MyAsyncTask<String, Integer, Bitmap>
    {

        public void setWidget(ImageView iv, WebView gif, CircleProgressView spinner, TextView wait, TextView readError)
        {
            this.iv = iv;
            this.spinner = spinner;
            this.wait = wait;
            this.readError = readError;
            this.gif = gif;
        }

        private ImageView                                  iv;
        private WebView                                    gif;
        private WebView                                    large;
        private TextView                                   wait;
        private String                                     url;
        private CircleProgressView                         spinner;
        private TextView                                   readError;
        private HashMap<String, PicSimpleBitmapWorkerTask> taskMap;

        public PicSimpleBitmapWorkerTask(ImageView iv, WebView gif, WebView large, CircleProgressView spinner, TextView wait, TextView readError, String url,
                HashMap<String, PicSimpleBitmapWorkerTask> taskMap)
        {
            this.iv = iv;
            this.url = url;
            this.spinner = spinner;
            this.readError = readError;
            this.taskMap = taskMap;
            this.gif = gif;
            this.large = large;
            this.wait = wait;
            this.readError.setVisibility(View.INVISIBLE);
            this.spinner.setVisibility(View.VISIBLE);

        }

        @Override
        protected Bitmap doInBackground(String... dd)
        {
            if (AppContext.getInstance().isNetworkConnected())
            {
                bitmap = ImageDownloader.downloadBitmap(url + "/500");
                if (null != bitmap)
                    return bitmap;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);
            this.wait.setVisibility(View.INVISIBLE);
            int progress = values[0];
            int max = values[1];
            spinner.setMax(max);
            spinner.setProgress(progress);
        }

        @Override
        protected void onCancelled(Bitmap s)
        {
            super.onCancelled(s);
            taskMap.remove(url);
            this.spinner.setVisibility(View.INVISIBLE);
            this.wait.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(final Bitmap b)
        {
            if (isCancelled())
            {
                return;
            }
            this.spinner.setVisibility(View.INVISIBLE);
            this.wait.setVisibility(View.INVISIBLE);

            taskMap.remove(url);

            if (b == null || iv == null)
            {

                readError.setVisibility(View.VISIBLE);
                readError.setText(getString(R.string.picture_read_failed));
                return;
            } else
            {
                readError.setVisibility(View.INVISIBLE);
            }

            if (null != b)
                try
                {
                    AppContext.getInstance();
                    if (AppContext.HARDWARE_ACCELERATED)
                        iv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                    iv.setImageBitmap(bitmap);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

        }
    }

    private void readLarge(WebView large, String bitmapPath)
    {
        large.setVisibility(View.VISIBLE);

        if (large.getTag() != null)
            return;

        large.getSettings().setJavaScriptEnabled(true);
        large.getSettings().setUseWideViewPort(true);
        large.getSettings().setLoadWithOverviewMode(true);
        large.getSettings().setBuiltInZoomControls(true);
        large.getSettings().setDisplayZoomControls(false);

        large.setVerticalScrollBarEnabled(false);
        large.setHorizontalScrollBarEnabled(false);

        File file = new File(bitmapPath);

        String str1 = "file://" + file.getAbsolutePath().replace("/mnt/sdcard/", "/sdcard/");
        String str2 = "<html>\n<head>\n     <style>\n          html,body{background:transparent;margin:0;padding:0;}          *{-webkit-tap-highlight-color:rgba(0, 0, 0, 0);}\n     </style>\n     <script type=\"text/javascript\">\n     var imgUrl = \""
                + str1
                + "\";"
                + "     var objImage = new Image();\n"
                + "     var realWidth = 0;\n"
                + "     var realHeight = 0;\n"
                + "\n"
                + "     function onLoad() {\n"
                + "          objImage.onload = function() {\n"
                + "               realWidth = objImage.width;\n"
                + "               realHeight = objImage.height;\n"
                + "\n"
                + "               document.gagImg.src = imgUrl;\n"
                + "               onResize();\n"
                + "          }\n"
                + "          objImage.src = imgUrl;\n"
                + "     }\n"
                + "\n"
                + "     function onResize() {\n"
                + "          var scale = 1;\n"
                + "          var newWidth = document.gagImg.width;\n"
                + "          if (realWidth > newWidth) {\n"
                + "               scale = realWidth / newWidth;\n"
                + "          } else {\n"
                + "               scale = newWidth / realWidth;\n"
                + "          }\n"
                + "\n"
                + "          hiddenHeight = Math.ceil(30 * scale);\n"
                + "          document.getElementById('hiddenBar').style.height = hiddenHeight + \"px\";\n"
                + "          document.getElementById('hiddenBar').style.marginTop = -hiddenHeight + \"px\";\n"
                + "     }\n"
                + "     </script>\n"
                + "</head>\n"
                + "<body onload=\"onLoad()\" onresize=\"onResize()\" onclick=\"Android.toggleOverlayDisplay();\">\n"
                + "     <table style=\"width: 100%;height:100%;\">\n"
                + "          <tr style=\"width: 100%;\">\n"
                + "               <td valign=\"middle\" align=\"center\" style=\"width: 100%;\">\n"
                + "                    <div style=\"display:block\">\n"
                + "                         <img name=\"gagImg\" src=\"\" width=\"100%\" style=\"\" />\n"
                + "                    </div>\n"
                + "                    <div id=\"hiddenBar\" style=\"position:absolute; width: 100%; background: transparent;\"></div>\n"
                + "               </td>\n"
                + "          </tr>\n"
                + "     </table>\n" + "</body>\n" + "</html>";
        large.loadDataWithBaseURL("file:///android_asset/", str2, "text/html", "utf-8", null);
        large.setVisibility(View.VISIBLE);
        large.setTag(new Object());
    }

}
