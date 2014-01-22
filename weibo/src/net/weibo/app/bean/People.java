package net.weibo.app.bean;

/**
 * 用户实体类
 * 
 * @author V
 * 
 */
public class People extends Entity
{
    private String  name;
    private String  openid;
    private String  nick;
    private String  head;
    private String  sex;
    private String  fansnum;
    private String  idolnum;
    private boolean isfans;
    private Boolean isvip;
    private String  pyCode;
    private String  tweetnum;
    private String  favnum;

    public final String getName()
    {
        return name;
    }

    public final void setName(String name)
    {
        this.name = name;
    }

    public final String getOpenid()
    {
        return openid;
    }

    public final void setOpenid(String openid)
    {
        this.openid = openid;
    }

    public final String getNick()
    {
        return nick;
    }

    public final void setNick(String nick)
    {
        this.nick = nick;
    }

    public final String getHead()
    {
        return head;
    }

    public final void setHead(String head)
    {
        this.head = head;
    }

    public final String getSex()
    {
        return sex;
    }

    public final void setSex(String sex)
    {
        this.sex = sex;
    }

    public final String getFansnum()
    {
        return fansnum;
    }

    public final void setFansnum(String fansnum)
    {
        this.fansnum = fansnum;
    }

    public final String getIdolnum()
    {
        return idolnum;
    }

    public final void setIdolnum(String idolnum)
    {
        this.idolnum = idolnum;
    }

    public final boolean getIsfans()
    {
        return isfans;
    }

    public final void setIsfans(boolean isfans)
    {
        this.isfans = isfans;
    }

    public final Boolean getIsvip()
    {
        return isvip;
    }

    public final void setIsvip(Boolean isvip)
    {
        this.isvip = isvip;
    }

    public final String getPyCode()
    {
        return pyCode;
    }

    public final void setPyCode(String pyCode)
    {
        this.pyCode = pyCode;
    }

    public final String getTweetnum()
    {
        return tweetnum;
    }

    public final void setTweetnum(String tweetnum)
    {
        this.tweetnum = tweetnum;
    }

    public final String getFavnum()
    {
        return favnum;
    }

    public final void setFavnum(String favnum)
    {
        this.favnum = favnum;
    }

}
