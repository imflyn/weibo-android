package net.weibo.common;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import net.weibo.app.AppConfig;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.MediaColumns;
import android.util.DisplayMetrics;

/**
 * 图片操作工具包
 * 
 */
public class ImageUtils
{

    /** 请求相册 */
    public static final int    REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
    /** 请求相机 */
    public static final int    REQUEST_CODE_GETIMAGE_BYCAMERA = 1;
    /** 请求裁剪 */
    public static final int    REQUEST_CODE_GETIMAGE_BYCROP   = 2;

    public static Uri          origUri;
    public static Uri          cropUri;
    public static File         protraitFile;
    public static Bitmap       protraitBitmap;
    public static String       protraitPath;

    public static final String SDCARD_MNT                     = "/mnt/sdcard";
    public static final String SDCARD                         = "/sdcard";
    public static final String PHOTO_SAVEPATH                 = AppConfig.DEFAULT_SAVE_IMAGE_PATH + File.separator;
    public static final String SCREEN_PIC_SAVEPATH            = AppConfig.MIAN_FOLDER + File.separator + "screenshots";

    /**
     * 写图片文件 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
     * 
     * @throws IOException
     */
    public static void saveImage(Context context, String fileName, Bitmap bitmap) throws IOException
    {
        saveImage(context, fileName, bitmap, 100);
    }

    public static void saveImage(Context context, String fileName, Bitmap bitmap, int quality) throws IOException
    {
        if (bitmap == null || fileName == null || context == null)
            return;

        FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, quality, stream);
        byte[] bytes = stream.toByteArray();
        fos.write(bytes);
        fos.close();
    }

    /**
     * 获得圆角图片的方法
     * 
     * @param bitmap
     * @param roundPx
     *            一般设成14
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx)
    {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 将Drawable转化为Bitmap
     * 
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable)
    {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

    /**
     * 判断当前Url是否标准的content://样式，如果不是，则返回绝对路径
     * 
     * @param uri
     * @return
     */
    public static String getAbsolutePathFromNoStandardUri(Uri mUri)
    {
        String filePath = null;

        String mUriString = mUri.toString();
        mUriString = Uri.decode(mUriString);

        String pre1 = "file://" + SDCARD + File.separator;
        String pre2 = "file://" + SDCARD_MNT + File.separator;

        if (mUriString.startsWith(pre1))
        {
            filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + mUriString.substring(pre1.length());
        } else if (mUriString.startsWith(pre2))
        {
            filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + mUriString.substring(pre2.length());
        }
        return filePath;
    }

    /**
     * 通过uri获取文件的绝对路径
     * 
     * @param uri
     * @return
     */
    public static String getAbsoluteImagePath(Activity context, Uri uri)
    {
        String imagePath = "";
        String[] proj = { MediaColumns.DATA };
        Cursor cursor = context.managedQuery(uri, proj, // Which columns to
                                                        // return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)

        if (cursor != null)
        {
            int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
            if (cursor.getCount() > 0 && cursor.moveToFirst())
            {
                imagePath = cursor.getString(column_index);
            }
        }

        return imagePath;
    }

    public static Bitmap loadImgThumbnail(String filePath, int w, int h)
    {
        Bitmap bitmap = getBitmapByPath(filePath);
        return zoomBitmap(bitmap, w, h);
    }

    /**
     * 获取bitmap
     * 
     * @param filePath
     * @return
     */
    public static Bitmap getBitmapByPath(String filePath)
    {
        return getBitmapByPath(filePath, null);
    }

    public static Bitmap getBitmapByPath(String filePath, BitmapFactory.Options opts)
    {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try
        {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis, null, opts);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (OutOfMemoryError e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                fis.close();
            } catch (Exception e)
            {
            }
        }
        return bitmap;
    }

    /**
     * 放大缩小图片
     * 
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h)
    {
        Bitmap newbmp = null;
        if (bitmap != null)
        {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            float scaleWidht = ((float) w / width);
            float scaleHeight = ((float) h / height);
            matrix.postScale(scaleWidht, scaleHeight);
            newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        }
        return newbmp;
    }

    /**
     * 使用当前时间戳拼接一个唯一的文件名
     * 
     * @param format
     * @return
     */
    public static String getTempFileName()
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SS");
        String fileName = format.format(new Timestamp(System.currentTimeMillis()));
        return fileName;
    }

    /**
     * 写图片文件到SD卡
     * 
     * @throws IOException
     */
    public static void saveImageToSD(Context ctx, String filePath, Bitmap bitmap, int quality) throws IOException
    {
        if (bitmap != null)
        {
            File file = new File(filePath.substring(0, filePath.lastIndexOf(File.separator)));
            if (!file.exists())
            {
                file.mkdirs();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
            bitmap.compress(CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();
            if (ctx != null)
            {
                scanPhoto(ctx, filePath);
            }
        }
    }

    /**
     * 让Gallery上能马上看到该图片
     */
    private static void scanPhoto(Context ctx, String imgFileName)
    {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(imgFileName);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        ctx.sendBroadcast(mediaScanIntent);
    }

    /**
     * 获取bitmap
     * 
     * @param context
     * @param fileName
     * @return
     */
    public static Bitmap getBitmap(Context context, String fileName)
    {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try
        {
            fis = new FileInputStream(new File(fileName));
            // fis = context.openFileInput(fileName);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (OutOfMemoryError e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                fis.close();
            } catch (Exception e)
            {
            }
        }
        return bitmap;

    }

    /**
     * (缩放)重绘图片
     * 
     * @param context
     *            Activity
     * @param bitmap
     * @return
     */
    public static Bitmap reDrawBitMap(Activity context, Bitmap bitmap)
    {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int rHeight = dm.heightPixels;
        int rWidth = dm.widthPixels;
        // float rHeight=dm.heightPixels/dm.density+0.5f;
        // float rWidth=dm.widthPixels/dm.density+0.5f;
        // int height=bitmap.getScaledHeight(dm);
        // int width = bitmap.getScaledWidth(dm);
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        float zoomScale;
        /** 方式1 **/
        // if(rWidth/rHeight>width/height){//以高为准
        // zoomScale=((float) rHeight) / height;
        // }else{
        // //if(rWidth/rHeight<width/height)//以宽为准
        // zoomScale=((float) rWidth) / width;
        // }
        /** 方式2 **/
        // if(width*1.5 >= height) {//以宽为准
        // if(width >= rWidth)
        // zoomScale = ((float) rWidth) / width;
        // else
        // zoomScale = 1.0f;
        // }else {//以高为准
        // if(height >= rHeight)
        // zoomScale = ((float) rHeight) / height;
        // else
        // zoomScale = 1.0f;
        // }
        /** 方式3 **/
        if (width >= rWidth)
            zoomScale = ((float) rWidth) / width;
        else
            zoomScale = 1.0f;
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(zoomScale, zoomScale);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 获得bitmap大小
     * 
     * @param path
     * @return
     */
    public static int[] getBitmapSize(String path)
    {
        int[] result = { -1, -1 };
        File file = new File(path);

        if (!file.exists())
        {
            return result;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int width = options.outWidth;
        int height = options.outHeight;
        if (width > 0 && height > 0)
        {
            result[0] = width;
            result[1] = height;
        }

        return result;
    }
    

}
