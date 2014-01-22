package net.weibo.app.adapter;

import java.util.ArrayList;

import net.weibo.app.R;
import net.weibo.app.bean.Letters;
import net.weibo.common.AsyncImageLoader;
import net.weibo.common.TimeTool;
import android.app.Activity;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LettersListViewApapter extends BaseAdapter
{

    private ArrayList<Letters> list;
    private Activity           activity;
    private Letters            letter      = null;
    private AsyncImageLoader   imageLoader = null;
    private SparseArray<View>  viewMap     = new SparseArray<View>();

    public LettersListViewApapter(Activity activity, ArrayList<Letters> list)
    {
        this.activity = activity;
        this.list = list;
        imageLoader = new AsyncImageLoader(activity);

    }

    public void setList(ArrayList<Letters> letterList)
    {
        if (null != this.list)
        {
            this.list = letterList;
        }

    }

    public void clear()
    {
        if (null != this.list)
        {
            this.list.clear();
        }
        this.list = null;
        if (null != viewMap)
        {
            viewMap.clear();
        }
        if (null != imageLoader)
        {
            imageLoader.destroy();
        }

    }

    @Override
    public int getCount()
    {
        if (list != null)
        {
            return list.size();
        } else
        {
            return 0;
        }
    }

    @Override
    public Object getItem(int position)
    {
        if (position >= 0 && list != null && list.size() > 0 && position < list.size())
            return list.get(position);
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup)
    {
        ViewHolder viewHolder = null;
        letter = list.get(position);
        View rowView = this.viewMap.get(position);

        if (viewMap.size() > 10)// 这里设置缓存的Item数量
            viewMap.remove(0);// 删除第一项

        if (rowView == null)
        {
            viewHolder = new ViewHolder();
            rowView = LayoutInflater.from(activity).inflate(R.layout.lv_letters_item, null);
            viewHolder.tv_nick = (TextView) rowView.findViewById(R.id.tv_nick);
            viewHolder.tv_text = (TextView) rowView.findViewById(R.id.tv_text);
            viewHolder.tv_time = (TextView) rowView.findViewById(R.id.tv_time);
            viewHolder.iv_headImage = (ImageView) rowView.findViewById(R.id.iv_headImage);

            viewHolder.iv_headImage.setAdjustViewBounds(true);

            viewHolder.tv_text.setText(letter.getListViewSpannableString(activity));
            viewHolder.tv_time.setText(TimeTool.getListTime(Long.valueOf(letter.getPubtime() + "000")));

            if (letter.getMsgbox() == 1)// 收件箱
            {
                viewHolder.tv_nick.setText(letter.getTonick());

                if (!viewHolder.iv_headImage.isDrawingCacheEnabled())
                {
                    // 头像
                    if (!TextUtils.isEmpty(letter.getHead()))
                    {
                        imageLoader.loadBitmap(viewHolder.iv_headImage, letter.getTohead() + "/50", 0);
                    }
                    viewHolder.iv_headImage.setDrawingCacheEnabled(true);
                }
                viewHolder.tv_text.setCompoundDrawablesWithIntrinsicBounds(activity.getResources().getDrawable(R.drawable.wb_msg_in), null, null, null);
            } else if (letter.getMsgbox() == 2)// 发件箱
            {
                viewHolder.tv_nick.setText(letter.getNick());

                if (!viewHolder.iv_headImage.isDrawingCacheEnabled())
                {
                    // 头像
                    if (!TextUtils.isEmpty(letter.getHead()))
                    {
                        imageLoader.loadBitmap(viewHolder.iv_headImage, letter.getHead() + "/50", 0);
                    }
                    viewHolder.iv_headImage.setDrawingCacheEnabled(true);
                }
                viewHolder.tv_text.setCompoundDrawablesWithIntrinsicBounds(activity.getResources().getDrawable(R.drawable.wb_msg_out), null, null, null);
            }

            if (letter.getToisvip() == 1)
            {
                viewHolder.tv_nick.setCompoundDrawablesWithIntrinsicBounds(null, null, activity.getResources().getDrawable(R.drawable.icon_vip), null);
            }

            rowView.setTag(viewHolder);
            viewMap.put(position, rowView);
        } else
        {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        return rowView;
    }

    private final static class ViewHolder
    {
        ImageView iv_headImage;
        TextView  tv_nick;
        TextView  tv_text;
        TextView  tv_time;
    }
}