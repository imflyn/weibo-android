package net.weibo.app.bean;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class PeopleList extends Entity
{
    private String            timestamp;
    private String            hasnext;
    private String            nextstartpos;
    private ArrayList<People> fans;

    public final String getTimestamp()
    {
        return timestamp;
    }

    public final void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public final String getHasnext()
    {
        return hasnext;
    }

    public final void setHasnext(String hasnext)
    {
        this.hasnext = hasnext;
    }

    public final String getNextstartpos()
    {
        return nextstartpos;
    }

    public final void setNextstartpos(String nextstartpos)
    {
        this.nextstartpos = nextstartpos;
    }

    public final ArrayList<People> getFans()
    {
        return fans;
    }

    public final void setFans(ArrayList<People> fans)
    {
        this.fans = fans;
    }

}
