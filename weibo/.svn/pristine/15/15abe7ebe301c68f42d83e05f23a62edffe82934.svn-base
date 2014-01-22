package net.weibo.app.adapter;

import java.util.ArrayList;

import net.weibo.app.AppContext;
import net.weibo.app.R;
import net.weibo.app.bean.News;
import net.weibo.app.lib.LongClickableLinkMovementMethod;
import net.weibo.app.lib.MyURLSpan;
import net.weibo.app.ui.BrowserNewsActivity;
import net.weibo.app.ui.CommentNewsActivity;
import net.weibo.app.ui.GalleryActivity;
import net.weibo.app.ui.ReposeNewsActivity;
import net.weibo.app.widget.TimeLineAvatarImageView;
import net.weibo.app.widget.TimeLineImageView;
import net.weibo.app.widget.TimeTextView;
import net.weibo.common.AsyncImageLoader;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.GridLayout;
import android.text.Layout;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NewsListViewAdapter extends BaseAdapter
{

    private Activity          context;
    private SparseArray<View> viewMap;
    private AsyncImageLoader  loader;
    private ArrayList<News>   list;
    private News              news;
    private ListView          listView;

    public NewsListViewAdapter(Activity context)
    {
        this.context = context;
    }

    public NewsListViewAdapter(final Activity context, ArrayList<News> list, ListView listView)
    {
        this(context);
        loader = new AsyncImageLoader(AppContext.getInstance());
        viewMap = new SparseArray<View>();
        this.list = list;
        this.listView = listView;
        listView.setOnItemClickListener(listViewOnItemClickListener);

        listView.setOnItemLongClickListener(new OnItemLongClickListener()
        {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                chooseItem(parent, new CharSequence[] { context.getString(R.string.rewrite), context.getString(R.string.comment), context.getString(R.string.cancle) }, position);
                return false;
            }
        });

    }

    /**
     * 操作选择
     * 
     * @param items
     */
    public void chooseItem(final AdapterView parent, CharSequence[] items, final int position)
    {
        AlertDialog dialog = new AlertDialog.Builder(context).setTitle("选择操作").setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item)
            {
                if (item == 0)
                {
                    News news = (News) parent.getAdapter().getItem(position);
                    String text = "";
                    if (news.getSelf() == 1)
                    {
                        text = "||@" + news.getName();
                    } else
                    {
                        text = "||@" + news.getName() + ":" + news.getText();
                    }
                    String reId = news.getId();
                    ReposeNewsActivity.turnToReposeNews(context, reId, text);
                } else if (item == 1)
                {
                    News news = (News) parent.getAdapter().getItem(position);
                    String text = "";
                    if (TextUtils.isEmpty(news.getText()))
                    {
                        if (news.getSource() != null)
                        {
                            text = news.getSource().getText();
                        }
                    } else
                    {
                        text = news.getText();
                    }
                    String reId = news.getId();
                    CommentNewsActivity.turnToReposeNews(context, reId, text);

                } else
                {
                    dialog.dismiss();
                }
            }
        }).create();
        dialog.show();
    }

    private AdapterView.OnItemClickListener listViewOnItemClickListener = new AdapterView.OnItemClickListener()
                                                                        {
                                                                            @Override
                                                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                                                                            {

                                                                                int headerViewsCount = listView.getHeaderViewsCount();
                                                                                if (isPositionBetweenHeaderViewAndFooterView(position))
                                                                                {
                                                                                    int indexInDataSource = position - headerViewsCount;
                                                                                    News msg = list.get(indexInDataSource);
                                                                                    if (!isNullFlag(msg))
                                                                                    {
                                                                                        listViewItemClick(msg);
                                                                                    }
                                                                                }
                                                                            }

                                                                            boolean isPositionBetweenHeaderViewAndFooterView(int position)
                                                                            {
                                                                                return position - listView.getHeaderViewsCount() < list.size() && position - listView.getHeaderViewsCount() >= 0;
                                                                            }

                                                                            boolean isNullFlag(News msg)
                                                                            {
                                                                                return msg == null;
                                                                            }

                                                                        };

    /**
     * 浏览微博
     */
    private void listViewItemClick(News news)
    {
        Intent intent = new Intent(context, BrowserNewsActivity.class);
        intent.putExtra("news", news);
        context.startActivityForResult(intent, 0);
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

        news = list.get(position);

        View rowView = this.viewMap.get(position);
        recyleView();
        if (rowView == null)
        {

            rowView = LayoutInflater.from(context).inflate(R.layout.lv_news_item, null);
            viewHolder = buildHolder(rowView);
            buildHead(viewHolder, news);
            buildData(viewHolder, news);
            buildPicture(viewHolder, news);
            if (null != news.getSource())
                buildRepostData(viewHolder, news.getSource());
            configLayerType(viewHolder);

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

    private void buildHead(ViewHolder holder, News news)
    {
        holder.head.setAdjustViewBounds(true);
        loader.loadBitmap(holder.head, news.getHead() + "/50", 0);
        holder.head.checkVerified(news.getIsvip() == 1); // 1是vip
    }

    private void buildData(ViewHolder holder, News news)
    {
        holder.username.setText(news.getNick());

        holder.time.setTime(Long.valueOf(news.getTimestamp() + "000"));
        holder.from.setText(news.getFrom());

        if (news.getCount() > 0 || news.getMcount() > 0 || TextUtils.isEmpty(news.getLocation()))
        {
            holder.count_layout.setVisibility(View.VISIBLE);
        }
        if (news.getCount() > 0)
            holder.repost_count.setText(String.valueOf(news.getCount()));
        if (news.getMcount() > 0)
            holder.comment_count.setText(String.valueOf(news.getMcount()));
        if (TextUtils.isEmpty(news.getLocation()))
            holder.gps.setText(news.getLocation());

        if (null == news.getSource())
            holder.repost_flag.setVisibility(View.GONE);

        holder.content.setText(news.getListViewSpannableString(context));
        if (TextUtils.isEmpty(holder.content.getText()))
            holder.content.setText("转发微博");

        holder.content.requestLayout();

    }

    private void buildPicture(ViewHolder holder, News news)
    {
        final String[] images = news.getImage();
        if (null != images)
        {

            int count = images.length;

            if (count < 2)
            {
                String url = images[0];
                holder.content_pic.setVisibility(View.VISIBLE);
                loader.loadBitmap(holder.content_pic, url + "/full", 0);
                holder.content_pic.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        showBigPicture(images, 0);
                    }
                });
            } else
            {
                holder.content_pic_multi.setVisibility(View.VISIBLE);
                for (int i = 0; i < count; i++)
                {
                    final int finalI = i;
                    String url = images[i];

                    loader.loadBitmap(holder.content_pic_multi.getChildAt(i), url + "/full", 0);

                    holder.content_pic_multi.getChildAt(i).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            showBigPicture(images, finalI);
                        }
                    });

                }

            }

            if (count < 9)
            {
                ImageView pic;
                switch (count)
                {
                    case 8:
                        pic = (ImageView) holder.content_pic_multi.getChildAt(8);
                        pic.setVisibility(View.INVISIBLE);
                        break;
                    case 7:
                        for (int i = 8; i > 6; i--)
                        {
                            pic = (ImageView) holder.content_pic_multi.getChildAt(i);
                            pic.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case 6:
                        for (int i = 8; i > 5; i--)
                        {
                            pic = (ImageView) holder.content_pic_multi.getChildAt(i);
                            pic.setVisibility(View.GONE);
                        }

                        break;
                    case 5:
                        for (int i = 8; i > 5; i--)
                        {
                            pic = (ImageView) holder.content_pic_multi.getChildAt(i);
                            pic.setVisibility(View.GONE);
                        }
                        pic = (ImageView) holder.content_pic_multi.getChildAt(5);
                        pic.setVisibility(View.INVISIBLE);
                        break;
                    case 4:
                        for (int i = 8; i > 5; i--)
                        {
                            pic = (ImageView) holder.content_pic_multi.getChildAt(i);
                            pic.setVisibility(View.GONE);
                        }
                        pic = (ImageView) holder.content_pic_multi.getChildAt(5);
                        pic.setVisibility(View.INVISIBLE);
                        pic = (ImageView) holder.content_pic_multi.getChildAt(4);
                        pic.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        for (int i = 8; i > 2; i--)
                        {
                            pic = (ImageView) holder.content_pic_multi.getChildAt(i);
                            pic.setVisibility(View.GONE);
                        }
                        break;
                    case 2:
                        for (int i = 8; i > 2; i--)
                        {
                            pic = (ImageView) holder.content_pic_multi.getChildAt(i);
                            pic.setVisibility(View.GONE);
                        }
                        pic = (ImageView) holder.content_pic_multi.getChildAt(2);
                        pic.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        holder.content_pic_multi.setVisibility(View.GONE);
                        break;

                }

            }

        } else
        {
            holder.content_pic_multi.setVisibility(View.GONE);
        }
    }

    private void buildRepostData(ViewHolder holder, News news)
    {
        holder.repost_content.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(news.getText()))
            holder.repost_content.setText("原微博已删除!");
        else
            holder.repost_content.setText(news.getListViewSpannableString(context));
        holder.repost_content.requestLayout();

        final String[] images = news.getImage();

        if (null != images)
        {
            int count = images.length;

            if (count < 2)
            {
                String url = images[0];
                holder.repost_content_pic.setVisibility(View.VISIBLE);
                loader.loadBitmap(holder.repost_content_pic, url + "/full", 0);
                holder.repost_content_pic.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        showBigPicture(images, 0);
                    }
                });
            } else
            {
                holder.repost_content_pic_multi.setVisibility(View.VISIBLE);
                for (int i = 0; i < count; i++)
                {
                    final int finalI = i;
                    String url = images[i];

                    loader.loadBitmap(holder.repost_content_pic_multi.getChildAt(i), url + "/full", 0);

                    holder.repost_content_pic_multi.getChildAt(i).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            showBigPicture(images, finalI);
                        }
                    });

                }

            }

            if (count < 9)
            {
                ImageView pic;
                switch (count)
                {
                    case 8:
                        pic = (ImageView) holder.repost_content_pic_multi.getChildAt(8);
                        pic.setVisibility(View.INVISIBLE);
                        break;
                    case 7:
                        for (int i = 8; i > 6; i--)
                        {
                            pic = (ImageView) holder.repost_content_pic_multi.getChildAt(i);
                            pic.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case 6:
                        for (int i = 8; i > 5; i--)
                        {
                            pic = (ImageView) holder.repost_content_pic_multi.getChildAt(i);
                            pic.setVisibility(View.GONE);
                        }

                        break;
                    case 5:
                        for (int i = 8; i > 5; i--)
                        {
                            pic = (ImageView) holder.repost_content_pic_multi.getChildAt(i);
                            pic.setVisibility(View.GONE);
                        }
                        pic = (ImageView) holder.repost_content_pic_multi.getChildAt(5);
                        pic.setVisibility(View.INVISIBLE);
                        break;
                    case 4:
                        for (int i = 8; i > 5; i--)
                        {
                            pic = (ImageView) holder.repost_content_pic_multi.getChildAt(i);
                            pic.setVisibility(View.GONE);
                        }
                        pic = (ImageView) holder.repost_content_pic_multi.getChildAt(5);
                        pic.setVisibility(View.INVISIBLE);
                        pic = (ImageView) holder.repost_content_pic_multi.getChildAt(4);
                        pic.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        for (int i = 8; i > 2; i--)
                        {
                            pic = (ImageView) holder.repost_content_pic_multi.getChildAt(i);
                            pic.setVisibility(View.GONE);
                        }
                        break;
                    case 2:
                        for (int i = 8; i > 2; i--)
                        {
                            pic = (ImageView) holder.repost_content_pic_multi.getChildAt(i);
                            pic.setVisibility(View.GONE);
                        }
                        pic = (ImageView) holder.repost_content_pic_multi.getChildAt(2);
                        pic.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        holder.repost_content_pic_multi.setVisibility(View.GONE);
                        break;

                }

            }

        } else
        {
            holder.repost_content_pic_multi.setVisibility(View.GONE);
        }

    }

    private void configLayerType(ViewHolder holder)
    {

        if (!AppContext.HARDWARE_ACCELERATED)
            return;

        int currentWidgetLayerType = holder.username.getLayerType();

        if (View.LAYER_TYPE_SOFTWARE != currentWidgetLayerType)
        {
            holder.username.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            if (holder.content != null)
                holder.content.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            if (holder.repost_content != null)
                holder.repost_content.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            if (holder.time != null)
                holder.time.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            if (holder.repost_count != null)
                holder.repost_count.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            if (holder.comment_count != null)
                holder.comment_count.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

    }

    private ViewHolder buildHolder(View convertView)
    {
        ViewHolder holder = new ViewHolder();
        holder.head = (TimeLineAvatarImageView) convertView.findViewById(R.id.iv_head);
        holder.gps = (TextView) convertView.findViewById(R.id.iv_gps);
        holder.repost_count = (TextView) convertView.findViewById(R.id.tv_repost_count);
        holder.comment_count = (TextView) convertView.findViewById(R.id.tv_comment_count);
        holder.username = (TextView) convertView.findViewById(R.id.username);
        TextPaint tp = holder.username.getPaint();
        tp.setFakeBoldText(true);
        holder.time = (TimeTextView) convertView.findViewById(R.id.time);
        holder.from = (TextView) convertView.findViewById(R.id.tv_from);
        holder.content = (TextView) convertView.findViewById(R.id.content);
        holder.content_pic = (TimeLineImageView) convertView.findViewById(R.id.content_pic);
        // UIUtils.setScreenDisplayMetrics(context, holder.content_pic, null,
        // 50);

        holder.content_pic_multi = (GridLayout) convertView.findViewById(R.id.content_pic_multi);
        holder.content_pic_multi.setRowCount(3);
        holder.content_pic_multi.setColumnCount(3);

        holder.repost_flag = convertView.findViewById(R.id.repost_flag);
        holder.repost_content = (TextView) convertView.findViewById(R.id.repost_content);
        holder.repost_content_pic = (TimeLineImageView) convertView.findViewById(R.id.repost_content_pic);
        // UIUtils.setScreenDisplayMetrics(context, holder.repost_content_pic,
        // null, 45);

        holder.repost_content_pic_multi = (GridLayout) convertView.findViewById(R.id.repost_content__pic_multi);
        holder.repost_content_pic_multi.setRowCount(3);
        holder.repost_content_pic_multi.setColumnCount(3);

        holder.listview_root = (RelativeLayout) convertView.findViewById(R.id.listview_root);
        holder.count_layout = (LinearLayout) convertView.findViewById(R.id.count_layout);

        if (holder.content != null)
            holder.content.setOnTouchListener(onTouchListener);
        if (holder.repost_content != null)
            holder.repost_content.setOnTouchListener(onTouchListener);

        return holder;
    }

    private final class ViewHolder
    {
        TimeLineAvatarImageView head;
        TextView                gps;
        TextView                repost_count;
        TextView                comment_count;
        TextView                username;
        TimeTextView            time;
        TextView                from;
        TextView                content;
        TimeLineImageView       content_pic;
        GridLayout              content_pic_multi;
        View                    repost_flag;
        TextView                repost_content;
        TimeLineImageView       repost_content_pic;
        GridLayout              repost_content_pic_multi;

        RelativeLayout          listview_root;
        LinearLayout            count_layout;

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

    private void showBigPicture(String[] images, int position)
    {
        Intent intent = new Intent(context, GalleryActivity.class);
        intent.putExtra("images", images);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

}
