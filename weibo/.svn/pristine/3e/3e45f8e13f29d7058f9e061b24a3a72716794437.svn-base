package net.weibo.app.adapter;

import java.util.ArrayList;
import java.util.List;

import net.weibo.app.R;
import net.weibo.app.bean.People;
import net.weibo.common.AsyncImageLoader;
import net.weibo.common.StringUtils;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class MyFollowerAdapter extends BaseAdapter implements SectionIndexer
{

    private Context           context;
    private SparseArray<View> viewMap;
    private AsyncImageLoader  loader;
    private List<People>      list;
    private People            people;
    private ImageGetter       imageGetter = new ImageGetter()
                                          {
                                              @Override
                                              public Drawable getDrawable(String source)
                                              {
                                                  int id = Integer.parseInt(source);
                                                  Drawable d = context.getResources().getDrawable(id);
                                                  d.setBounds(0, -5, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                                                  return d;
                                              }
                                          };

    public MyFollowerAdapter(Context context)
    {
        this.context = context;
        loader = new AsyncImageLoader(context);
        viewMap = new SparseArray<View>();

    }

    public void setList(ArrayList<People> list)
    {
        if (null != viewMap)
        {
            viewMap.clear();
        }
        if (null != list)
        {
            this.list = list;
        }
    }

    public void clear()
    {

        if (null != viewMap)
        {
            viewMap.clear();
        }
        if (null != loader)
        {
            loader.destroy();
        }

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
    public View getView(int position, View convertView, ViewGroup parent)
    {

        ViewHolder viewHolder = null;
        people = list.get(position);

        View rowView = this.viewMap.get(position);
        if (viewMap.size() > 30)// 这里设置缓存的Item数量
            viewMap.remove(0);// 删除第一项

        if (rowView == null)
        {
            viewHolder = new ViewHolder();

            if (people.getNick().length() == 1 && TextUtils.isEmpty(people.getName()))
            {
                rowView = LayoutInflater.from(context).inflate(R.layout.lv_py_index, null);
                viewHolder.nick = (TextView) rowView.findViewById(R.id.tv_index);

            } else
            {
                rowView = LayoutInflater.from(context).inflate(R.layout.lv_myfollowers_item, null);
                viewHolder.name = (TextView) rowView.findViewById(R.id.tv_name);
                viewHolder.nick = (TextView) rowView.findViewById(R.id.tv_nick);
                viewHolder.face = (ImageView) rowView.findViewById(R.id.iv_headImage);
                viewHolder.face.setAdjustViewBounds(true);

                if (!viewHolder.face.isDrawingCacheEnabled())
                {
                    // 头像
                    if (!StringUtils.isEmpty(people.getHead()))
                    {
                        loader.loadBitmap(viewHolder.face, people.getHead() + "/50", 4);
                    }
                    viewHolder.face.setDrawingCacheEnabled(true);
                }
                viewHolder.name.setText(people.getName());
            }
            rowView.setTag(viewHolder);
            viewMap.put(position, rowView);

        } else
        {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        viewHolder.nick.setText(people.getNick() + " ");

        if (people.getIsvip() != null)
        {
            if (people.getIsvip())
                viewHolder.nick.append(Html.fromHtml("<img src=\"" + R.drawable.icon_vip + "\"/>", imageGetter, null));
        }
        return rowView;
    }

    private final static class ViewHolder
    {
        TextView  nick;
        ImageView face;
        TextView  name;
    }

    @Override
    public Object[] getSections()
    {
        return null;
    }

    @Override
    public int getSectionForPosition(int position)
    {

        return 0;
    }

    @Override
    public int getPositionForSection(int section)
    {
        String nick;
        People p = new People();
        if (section == '#')
        {
            return 0;
        } else
        {
            int count = list.size();
            for (int i = 0; i < count; i++)
            {
                p = list.get(i);
                nick = p.getNick();
                char firstChar = nick.toUpperCase().charAt(0);
                if (firstChar == section)
                {
                    return i;
                }
            }
        }
        p = null;
        nick = null;
        return -1;
    }
}
