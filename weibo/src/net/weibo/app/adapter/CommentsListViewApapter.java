package net.weibo.app.adapter;

import java.util.ArrayList;

import net.weibo.app.R;
import net.weibo.app.bean.News;
import net.weibo.app.lib.LongClickableLinkMovementMethod;
import net.weibo.app.lib.MyURLSpan;
import net.weibo.app.widget.TimeLineAvatarImageView;
import net.weibo.common.AsyncImageLoader;
import net.weibo.common.StringUtils;
import net.weibo.common.TimeTool;
import android.app.Activity;
import android.text.Layout;
import android.text.SpannableString;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CommentsListViewApapter extends BaseAdapter
{

    private Activity          context;
    private SparseArray<View> viewMap;
    private AsyncImageLoader  loader;
    private ArrayList<News>   list;
    private News              comment;
    private ListView          listView;

    public CommentsListViewApapter(Activity context)
    {
        this.context = context;
    }

    public CommentsListViewApapter(Activity context, ArrayList<News> list, ListView listView)
    {
        this(context);
        loader = new AsyncImageLoader(context);
        viewMap = new SparseArray<View>();
        this.list = list;
        this.listView = listView;

    }

    public void setList(ArrayList<News> list)
    {
        if (null != viewMap)
        {
            this.viewMap.clear();
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
        if (list != null && list.get(position) != null && list.size() > 0 && position < list.size())
            return Long.valueOf(list.get(position).getId());
        else
            return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        ViewHolder viewHolder = null;

        comment = list.get(position);

        View rowView = this.viewMap.get(position);
        recyleView();
        if (rowView == null)
        {

            rowView = LayoutInflater.from(context).inflate(R.layout.lv_comments_item, null);
            viewHolder = buildHolder(rowView);
            buildData(viewHolder, comment);
            rowView.setTag(viewHolder);
            viewMap.put(position, rowView);

        } else
        {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        return rowView;
    }

    private void recyleView()
    {
        if (viewMap.size() > 12)// 这里设置缓存的Item数量
        {
            viewMap.remove(0);// 删除第一项
        }
    }

    private ViewHolder buildHolder(View convertView)
    {
        ViewHolder holder = new ViewHolder();
        holder.avatar = (TimeLineAvatarImageView) convertView.findViewById(R.id.avatar);
        holder.time = (TextView) convertView.findViewById(R.id.time);
        holder.username = (TextView) convertView.findViewById(R.id.username);
        holder.content = (TextView) convertView.findViewById(R.id.content);

        holder.content.setOnTouchListener(onTouchListener);
        return holder;
    }

    private void buildData(ViewHolder holder, News comment)
    {
        loader.loadBitmap(holder.avatar, comment.getHead() + "/50", 2);
        holder.time.setText(TimeTool.getListTime(Long.valueOf(comment.getTimestamp() + "000")));
        holder.username.setText(comment.getNick());
        if (StringUtils.isEmail(comment.getText()))
            holder.content.setText("转发微博");
        else
            holder.content.setText(comment.getListViewSpannableString(context));
    }

    private final static class ViewHolder
    {
        TimeLineAvatarImageView avatar;
        TextView                time;
        TextView                username;
        TextView                content;

    }

    // when view is recycled by listview, need to catch exception

    private ViewHolder getViewHolderByView(View view)
    {
        try
        {
            final int position = listView.getPositionForView(view);
            if (position == AdapterView.INVALID_POSITION)
            {
                return null;
            }
            return getViewHolderByView(position);
        } catch (NullPointerException e)
        {

        }
        return null;
    }

    private ViewHolder getViewHolderByView(int position)
    {

        int wantedPosition = position - listView.getHeaderViewsCount();
        int firstPosition = listView.getFirstVisiblePosition() - listView.getHeaderViewsCount();
        int wantedChild = wantedPosition - firstPosition;

        if (wantedChild < 0 || wantedChild >= listView.getChildCount())
        {
            return null;
        }

        View wantedView = listView.getChildAt(wantedChild);
        ViewHolder holder = (ViewHolder) wantedView.getTag();
        return holder;

    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener()
                                                 {
                                                     @Override
                                                     public boolean onTouch(View v, MotionEvent event)
                                                     {

                                                         ViewHolder holder = getViewHolderByView(v);

                                                         if (holder == null)
                                                         {
                                                             return false;
                                                         }

                                                         Layout layout = ((TextView) v).getLayout();

                                                         int x = (int) event.getX();
                                                         int y = (int) event.getY();
                                                         int offset = 0;
                                                         if (layout != null)
                                                         {

                                                             int line = layout.getLineForVertical(y);
                                                             offset = layout.getOffsetForHorizontal(line, x);
                                                         }

                                                         TextView tv = (TextView) v;
                                                         SpannableString value = SpannableString.valueOf(tv.getText());
                                                         MyURLSpan[] urlSpans = value.getSpans(0, value.length(), MyURLSpan.class);
                                                         boolean result = false;
                                                         int start = 0;
                                                         int end = 0;
                                                         for (MyURLSpan urlSpan : urlSpans)
                                                         {
                                                             start = value.getSpanStart(urlSpan);
                                                             end = value.getSpanEnd(urlSpan);
                                                             if (start <= offset && offset <= end)
                                                             {
                                                                 result = true;
                                                                 break;
                                                             }
                                                         }

                                                         if (result)
                                                         {
                                                             return LongClickableLinkMovementMethod.getInstance().onTouchEvent(tv, value, event);
                                                         } else
                                                         {
                                                             return false;
                                                         }

                                                     }
                                                 };

}
