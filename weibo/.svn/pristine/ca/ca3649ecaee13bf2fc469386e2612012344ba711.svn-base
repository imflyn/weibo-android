package net.weibo.app.adapter;

import java.util.ArrayList;
import java.util.List;

import net.weibo.api.ListenersImpl;
import net.weibo.app.AppContext;
import net.weibo.app.R;
import net.weibo.app.bean.People;
import net.weibo.common.AsyncImageLoader;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ListenersListViewApapter extends BaseAdapter
{

    private List<People>      list;
    private Activity          activity;
    private People            fans        = null;
    private AsyncImageLoader  imageLoader = null;
    private AppContext        appContext;
    private SparseArray<View> viewMap     = new SparseArray<View>();

    private ImageGetter       imageGetter = new ImageGetter()
                                          {
                                              @Override
                                              public Drawable getDrawable(String source)
                                              {
                                                  int id = Integer.parseInt(source);
                                                  Drawable d = activity.getResources().getDrawable(id);
                                                  d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                                                  return d;
                                              }
                                          };

    public ListenersListViewApapter(Activity activity)
    {
        this.activity = activity;
        appContext = (AppContext) activity.getApplication();
        imageLoader = new AsyncImageLoader(activity);

    }

    public void setList(ArrayList<People> list)
    {
        if (null != this.list)
        {
            this.list.clear();
        }
        this.list = list;
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
    public View getView(final int position, View convertView, ViewGroup viewGroup)
    {
        ViewHolder viewHolder = null;
        fans = list.get(position);

        View rowView = this.viewMap.get(position);
        if (viewMap.size() > 48)// 这里设置缓存的Item数量
            viewMap.remove(0);// 删除第一项
        if (rowView == null)
        {
            viewHolder = new ViewHolder();
            rowView = LayoutInflater.from(activity).inflate(R.layout.lv_mylisteners_item, null);
            viewHolder.tv_nick = (TextView) rowView.findViewById(R.id.tv_nick);
            viewHolder.tv_name = (TextView) rowView.findViewById(R.id.tv_name);
            viewHolder.tv_listenernum = (TextView) rowView.findViewById(R.id.tv_listenernum);
            viewHolder.iv_headImage = (ImageView) rowView.findViewById(R.id.iv_headImage);
            viewHolder.ib_listent = (Button) rowView.findViewById(R.id.ib_listent);

            viewHolder.iv_headImage.setAdjustViewBounds(true);

            // 点击收听
            viewHolder.ib_listent.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    final ListenersImpl impl = new ListenersImpl(appContext);
                    impl.listenerOne(list.get(position).getName());
                    impl.insertPeopleToDb(list.get(position));
                    v.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.wb_square_button_hl));
                    ((TextView) v).setText("已收听");
                    v.setEnabled(false);
                }
            });

            if (!viewHolder.iv_headImage.isDrawingCacheEnabled())
            {
                // 头像
                if (!TextUtils.isEmpty(fans.getHead()))
                {
                    imageLoader.loadBitmap(viewHolder.iv_headImage, fans.getHead() + "/50", 4);
                }
                viewHolder.iv_headImage.setDrawingCacheEnabled(true);
            }

            if (!viewHolder.ib_listent.isDrawingCacheEnabled())
            {
                // 是否收听了
                if (fans.getIsfans())
                {
                    viewHolder.ib_listent.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.wb_square_button_hl));
                    viewHolder.ib_listent.setText("已收听");
                    viewHolder.ib_listent.setEnabled(false);
                } else
                {
                    viewHolder.ib_listent.setText("收听");
                    viewHolder.ib_listent.setEnabled(true);
                }
                viewHolder.ib_listent.setDrawingCacheEnabled(true);
            }

            rowView.setTag(viewHolder);
            viewMap.put(position, rowView);
        } else
        {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        // 昵称
        viewHolder.tv_nick.setText(fans.getNick() + " ");

        // 性别
        if (fans.getSex().equals("1"))
        {
            viewHolder.tv_nick.append(Html.fromHtml("<img src=\"" + R.drawable.wb_icon_male + "\"/>", imageGetter, null));
        } else if (fans.getSex().equals("2"))
        {
            viewHolder.tv_nick.append(Html.fromHtml("<img src=\"" + R.drawable.wb_icon_female + "\"/>", imageGetter, null));
        }

        // 帐号
        viewHolder.tv_name.setText(" " + fans.getName());

        // 粉丝数
        viewHolder.tv_listenernum.setText(fans.getFansnum());

        return rowView;
    }

    private final static class ViewHolder
    {
        ImageView iv_headImage;
        TextView  tv_nick;
        TextView  tv_name;
        TextView  tv_listenernum;
        Button    ib_listent;
    }
}