package net.weibo.app.adapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import net.weibo.api.InfoImpl;
import net.weibo.app.AppConfig;
import net.weibo.app.AppContext;
import net.weibo.app.R;
import net.weibo.app.bean.Result;
import net.weibo.app.ui.InfoEditActivity;
import net.weibo.app.widget.BadgeView;
import net.weibo.app.widget.LoadingDialog;
import net.weibo.common.ImageUtils;
import net.weibo.common.UIUtils;
import net.weibo.constant.Tids;
import net.weibo.dao.LocalHeadPicDBService;
import net.weibo.http.AsyncHttpResponseHandler;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class PhotoGridViewApapter extends BaseAdapter
{

    private Activity                           activity;
    private ArrayList<HashMap<String, Object>> list;
    private static Boolean                     flag    = true;
    private LoadingDialog                      dialog;

    private Handler                            handler = new Handler()
                                                       {
                                                           @Override
                                                           public void handleMessage(android.os.Message msg)
                                                           {
                                                               switch (msg.what)
                                                               {
                                                                   case Tids.T_DELETE_HEADPIC:
                                                                       if (null != dialog)
                                                                       {
                                                                           dialog.dismiss();
                                                                       }
                                                                       if ((Integer) msg.obj == 1)
                                                                       {
                                                                           UIUtils.ToastMessage(activity.getApplicationContext(), "删除成功!");
                                                                       } else
                                                                       {
                                                                           UIUtils.ToastMessage(activity.getApplicationContext(), "删除失败!");
                                                                       }
                                                                       PhotoGridViewApapter.this.notifyDataSetChanged();
                                                                       break;
                                                                   case Tids.T_UPDATE_HEADPIC:
                                                                       if (null != dialog)
                                                                       {
                                                                           dialog.dismiss();
                                                                       }
                                                                       if ((Integer) msg.obj == 1)
                                                                       {
                                                                           UIUtils.ToastMessage(activity.getApplicationContext(), "设置成功!");
                                                                       } else
                                                                       {
                                                                           UIUtils.ToastMessage(activity.getApplicationContext(), "设置失败!");
                                                                       }
                                                                       PhotoGridViewApapter.this.notifyDataSetChanged();
                                                                       break;
                                                               }
                                                           };
                                                       };

    public PhotoGridViewApapter(Activity activity)
    {
        this.activity = activity;
        dialog = new LoadingDialog(activity);
    }

    public void setImages(ArrayList<HashMap<String, Object>> pics)
    {
        list = pics;
    }

    public void clearImages()
    {
        if (null != list)
        {
            list.clear();
            list = null;
        }
    }

    public void setFlag(Boolean bol)
    {
        flag = bol;
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public Object getItem(int position)
    {
        return list.get(position).get("pic");
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

            // listItemView.badge.setBackgroundResource(R.drawable.wb_avatar_pin);
            UIUtils.setBackGroundDrawable(listItemView.badge, activity, R.drawable.wb_avatar_pin);
            listItemView.badge.setBadgePosition(BadgeView.POSITION_TOP_LEFT);

            UIUtils.setScreenDisplayMetrics(activity, listItemView.image, 14, 25);

            listItemView.image.setScaleType(ImageView.ScaleType.FIT_XY);
            listItemView.image.setPadding(2, 3, 2, 3);
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

        listItemView.image.setImageBitmap((Bitmap) list.get(position).get("pic"));

        listItemView.image.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (position == list.size() - 1 && !flag)
                {
                    CharSequence[] items = { activity.getString(R.string.img_from_album), activity.getString(R.string.img_from_camera), activity.getString(R.string.cancle) };

                    imageChooseItem(items);
                } else if (position > 0)
                {
                    CharSequence[] items = { activity.getString(R.string.see_big_pic), activity.getString(R.string.set_head_pic), activity.getString(R.string.delete),
                            activity.getString(R.string.cancle) };
                    ChooseItem(items, position);
                } else if (position == 0)
                {
                    CharSequence[] items = { activity.getString(R.string.img_from_album), activity.getString(R.string.img_from_camera), activity.getString(R.string.see_big_pic),
                            activity.getString(R.string.cancle) };
                    ChooseItem2(items, position);
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
    public void ChooseItem2(CharSequence[] items, final int position)
    {
        AlertDialog imageDialog = new AlertDialog.Builder(activity).setTitle("操作").setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item)
            {

                // 相册
                if (item == 0)
                {
                    startImagePick(true);
                }
                // 拍照
                else if (item == 1)
                {
                    startActionCamera(true);
                }
                // 查看大图
                else if (item == 2)
                {
                    final String url = (String) list.get(position).get("url");
                    if (null != url)
                        UIUtils.showImageZoomDialog(activity, url);
                } else
                {
                    dialog.dismiss();
                }
            }
        }).create();
        imageDialog.show();
    }

    /**
     * 操作选择
     * 
     * @param items
     */
    public void ChooseItem(CharSequence[] items, final int position)
    {
        AlertDialog imageDialog = new AlertDialog.Builder(activity).setTitle("操作").setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item)
            {
                // 查看大图
                if (item == 0)
                {
                    final String url = (String) list.get(position).get("url");
                    if (null != url)
                        UIUtils.showImageZoomDialog(activity, url);
                }
                // 设为头像
                else if (item == 1)
                {
                    updateHeadPic(position);
                }
                // 删除
                else if (item == 2)
                {
                    if (PhotoGridViewApapter.this.dialog != null)
                    {
                        PhotoGridViewApapter.this.dialog.setLoadText("正在删除...");
                        PhotoGridViewApapter.this.dialog.show();
                    }
                    LocalHeadPicDBService db = new LocalHeadPicDBService(activity);
                    if (null != list.get(position).get("url"))
                    {
                        db.delete((String) list.get(position).get("url"));
                        list.remove(position);
                        if (list.size() < 6 && position == 5)
                        {
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("pic", ImageUtils.getRoundedCornerBitmap(BitmapFactory.decodeStream(activity.getResources().openRawResource(R.drawable.wb_add_avatar_140)), 8));
                            map.put("url", null);
                            list.add(map);
                            flag = false;
                        }
                        handler.obtainMessage(Tids.T_DELETE_HEADPIC, 1).sendToTarget();
                    } else
                    {
                        handler.obtainMessage(Tids.T_DELETE_HEADPIC, -1).sendToTarget();
                    }
                } else
                {
                    dialog.dismiss();
                }
            }
        }).create();
        imageDialog.show();
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
                    startImagePick(false);
                }
                // 手机拍照
                else if (item == 1)
                {
                    startActionCamera(false);
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
    private void startImagePick(boolean ifUpload)
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        activity.startActivityForResult(Intent.createChooser(intent, "选择图片"), ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
        InfoEditActivity.ifUpload = ifUpload;
    }

    /**
     * 相机拍照
     * 
     * @param output
     */
    private void startActionCamera(boolean ifUpload)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, this.getCameraTempFile());
        activity.startActivityForResult(intent, ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
        InfoEditActivity.ifUpload = ifUpload;
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

    private void updateHeadPic(final int position)
    {
        if (PhotoGridViewApapter.this.dialog != null)
        {
            PhotoGridViewApapter.this.dialog.setLoadText("设置头像中...");
            PhotoGridViewApapter.this.dialog.show();
        }
        final AppContext appContext = (AppContext) activity.getApplication();
        new Thread()
        {
            @Override
            public void run()
            {

                final InfoImpl infoImpl = new InfoImpl(appContext);
                final String url = (String) list.get(position).get("url");
                infoImpl.updateHeadPic(url, appContext, new AsyncHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(String content)
                    {
                        Message msg = new Message();
                        msg.what = Tids.T_UPDATE_HEADPIC;
                        Result result = null;
                        try
                        {
                            result = Result.parse(content);
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                            msg.obj = -1;
                            msg.what = Tids.T_UPDATE_HEADPIC;
                            handler.sendMessage(msg);
                            return;
                        }
                        if (null == result || result.getRet() != 0)
                        {
                            msg.obj = -1;
                            msg.what = Tids.T_UPDATE_HEADPIC;
                        } else
                        {
                            LocalHeadPicDBService db = new LocalHeadPicDBService(activity);
                            String headUrl = AppConfig.getAppConfig(appContext).get("HeadUrl");
                            db.insert(headUrl.substring(headUrl.lastIndexOf("/") + 1, headUrl.length()), AppConfig.getAppConfig(appContext).get("openId"));
                            db.delete(url);
                            try
                            {
                                infoImpl.parse(content);
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                            Collections.swap(list, position, 0);
                            msg.obj = 1;
                        }
                        handler.sendMessage(msg);
                    }
                });
            };
        }.start();
    }

}