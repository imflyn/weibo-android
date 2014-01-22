package net.weibo.app.adapter;

import java.util.ArrayList;

import net.weibo.app.R;
import net.weibo.app.bean.Album;
import net.weibo.common.AsyncImageLoader;
import net.weibo.common.StringUtils;
import net.weibo.common.UIUtils;
import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class AlbumGridViewApapter extends BaseAdapter
{

    private Activity          activity;
    private ArrayList<Album>  list;
    private AsyncImageLoader  loader;
    private Album             album;
    private SparseArray<View> map = new SparseArray<View>();

    public AlbumGridViewApapter(Activity activity)
    {
        this.activity = activity;
        if (null == loader)
        {
            loader = new AsyncImageLoader(activity);
        }
    }

    public void setList(ArrayList<Album> list)
    {
        this.list = list;
    }

    public void clear()
    {
        if (null != list)
        {
            list.clear();
            list = null;
        }
        if (null != map)
        {
            map.clear();
            map = null;
        }
        if (null == loader)
            loader.destroy();
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public Object getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        Holder holder = null;
        album = list.get(position);

        View rowView = this.map.get(position);
        if (map.size() > 24)// 这里设置缓存的Item数量
            map.remove(0);// 删除第一项

        if (rowView == null)
        {
            holder = new Holder();

            rowView = LayoutInflater.from(activity).inflate(R.layout.gv_album_item, null);

            holder.imageView = (ImageView) rowView.findViewById(R.id.iv_album);

            UIUtils.setScreenDisplayMetrics(activity, holder.imageView, 19, 31);
            holder.imageView.setAdjustViewBounds(true);

            if (!holder.imageView.isDrawingCacheEnabled())
            {
                String url = album.getImage() + "/full";

                if (!StringUtils.isEmpty(url))
                {
                    holder.imageView.setScaleType(ScaleType.FIT_XY);
                    loader.loadBitmap(holder.imageView, url, 0);
                    holder.imageView.setDrawingCacheEnabled(true);
                }
            }

            rowView.setTag(holder);
            map.put(position, rowView);
        } else
        {
            holder = (Holder) rowView.getTag();
        }

        return rowView;

    }

    final class Holder
    {
        ImageView imageView;
    }

}