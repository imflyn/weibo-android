package net.weibo.app.adapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import net.weibo.app.R;
import net.weibo.app.widget.BadgeView;
import net.weibo.common.ImageUtils;
import net.weibo.common.UIUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class HorizontalListViewApapter extends BaseAdapter
{

    private Activity          activity;
    private ArrayList<Bitmap> lists;
    private static Boolean    flag = true;

    public HorizontalListViewApapter(Activity activity)
    {
        this.activity = activity;
    }

    public void setImages(ArrayList<Bitmap> pics)
    {
        lists = pics;
    }

    public void clearImages()
    {
        if (null != lists)
        {
            lists.clear();
            lists = null;
        }
    }

    public void setFlag(Boolean bol)
    {
        flag = bol;
    }

    @Override
    public int getCount()
    {
        return lists.size();
    }

    @Override
    public Object getItem(int position)
    {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ListItemView listItemView = null;
        if (convertView == null)
        {
            listItemView = new ListItemView();
            LayoutInflater listContainer = LayoutInflater.from(activity);
            convertView = listContainer.inflate(R.layout.lv_horizontal_item, null);
            listItemView.image = (ImageView) convertView.findViewById(R.id.image);

            if (listItemView.badge == null)
                listItemView.badge = new BadgeView(activity, listItemView.image);

            UIUtils.setBackGroundDrawable(listItemView.badge, activity, R.drawable.wb_avatar_pin);

            // listItemView.badge.setBackgroundResource(R.drawable.wb_avatar_pin);
            listItemView.badge.setBadgePosition(BadgeView.POSITION_TOP_LEFT);

            UIUtils.setScreenDisplayMetrics(activity, listItemView.image, 18, 30);

            listItemView.image.setScaleType(ImageView.ScaleType.FIT_XY);

            convertView.setTag(listItemView);
        } else
        {
            listItemView = (ListItemView) convertView.getTag();
        }

        if (position == 0)
        {
            listItemView.badge.show();
        } else
        {
            listItemView.badge.hide();
        }

        listItemView.image.setImageBitmap(lists.get(position));

        listItemView.image.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (position == lists.size() - 1 && !flag)
                {
                    CharSequence[] items = { activity.getString(R.string.img_from_album), activity.getString(R.string.img_from_camera), activity.getString(R.string.cancle) };

                    imageChooseItem(items);
                } else
                {
                }
            }
        });
        return convertView;
    }

    final class ListItemView
    {
        BadgeView badge;
        ImageView image;

    }

    /**
     * 操作选择
     * 
     * @param items
     */
    public void imageChooseItem(CharSequence[] items)
    {
        AlertDialog imageDialog = new AlertDialog.Builder(activity).setTitle("上传头像").setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item)
            {
                // 相册选图
                if (item == 0)
                {
                    startImagePick();
                }
                // 手机拍照
                else if (item == 1)
                {
                    startActionCamera();
                } else
                {
                    dialog.dismiss();
                }
            }
        }).create();
        imageDialog.show();
    }

    /**
     * 选择图片裁剪
     * 
     * @param output
     */
    private void startImagePick()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        activity.startActivityForResult(Intent.createChooser(intent, "选择图片"), ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
    }

    /**
     * 相机拍照
     * 
     * @param output
     */
    private void startActionCamera()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, this.getCameraTempFile());
        activity.startActivityForResult(intent, ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
    }

    // 拍照保存的绝对路径
    private Uri getCameraTempFile()
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
            UIUtils.ToastMessage(activity.getApplicationContext(), "无法保存上传的头像，请检查SD卡是否挂载");
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        // 照片命名
        String cropFileName = "QQWeibo_camera_" + timeStamp + ".jpg";
        // 裁剪头像的绝对路径
        ImageUtils.protraitPath = ImageUtils.PHOTO_SAVEPATH + cropFileName;
        ImageUtils.protraitFile = new File(ImageUtils.protraitPath);
        ImageUtils.cropUri = Uri.fromFile(ImageUtils.protraitFile);
        ImageUtils.origUri = ImageUtils.cropUri;
        return ImageUtils.cropUri;
    }

}