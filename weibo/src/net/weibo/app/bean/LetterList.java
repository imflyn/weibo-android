package net.weibo.app.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class LetterList implements Serializable
{
    private int               hasnext;     // 结束标志，1：拉取完毕，0：未完
    private int               totalsesscnt; // 总的会话个数
    private long              timestamp;   // 时间戳

    private ArrayList<Letter> info;

    public final int getHasnext()
    {
        return hasnext;
    }

    public final void setHasnext(int hasnext)
    {
        this.hasnext = hasnext;
    }

    public final int getTotalsesscnt()
    {
        return totalsesscnt;
    }

    public final void setTotalsesscnt(int totalsesscnt)
    {
        this.totalsesscnt = totalsesscnt;
    }

    public final long getTimestamp()
    {
        return timestamp;
    }

    public final void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    public final ArrayList<Letter> getInfo()
    {
        return info;
    }

    public final void setInfo(ArrayList<Letter> info)
    {
        this.info = info;
    }

}
