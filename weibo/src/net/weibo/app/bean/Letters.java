package net.weibo.app.bean;

public class Letters extends Message
{

    private int    totalcnt;    // 与toname的信息总数,
    private int    unreadcnt;   // 与toname的未读信息总数,

    private String head;        // 当前用户头像url
    private int    isvip;       // 当前用户是否是微博认证用户
    private int    isrealname;  // 当前用户是否实名认证
    private String toname;      // 会话人的微博账号
    private String toopenid;    // 会话人的openid
    private String tonick;      // 会话人的昵称
    private String tohead;      // 会话人的头像url
    private int    toisvip;     // 会话人是否是微博认证用户
    private int    toisrealname; // 会话人是否实名认证
    private int    readflag;    // 信息的已读标志，1：已读，0：未读
    private int    msgbox;      // 表示是在收件箱还是发件箱(1：收件箱；2.发件箱；3：群聊)
    private String pubtime;     // 发表时间
    private long   tweetid;     // 消息id

    public final int getTotalcnt()
    {
        return totalcnt;
    }

    public final void setTotalcnt(int totalcnt)
    {
        this.totalcnt = totalcnt;
    }

    public final int getUnreadcnt()
    {
        return unreadcnt;
    }

    public final void setUnreadcnt(int unreadcnt)
    {
        this.unreadcnt = unreadcnt;
    }

    public final String getHead()
    {
        return head;
    }

    public final void setHead(String head)
    {
        this.head = head;
    }

    public final int getIsvip()
    {
        return isvip;
    }

    public final void setIsvip(int isvip)
    {
        this.isvip = isvip;
    }

    public final int getIsrealname()
    {
        return isrealname;
    }

    public final void setIsrealname(int isrealname)
    {
        this.isrealname = isrealname;
    }

    public final String getToname()
    {
        return toname;
    }

    public final void setToname(String toname)
    {
        this.toname = toname;
    }

    public final String getToopenid()
    {
        return toopenid;
    }

    public final void setToopenid(String toopenid)
    {
        this.toopenid = toopenid;
    }

    public final String getTonick()
    {
        return tonick;
    }

    public final void setTonick(String tonick)
    {
        this.tonick = tonick;
    }

    public final String getTohead()
    {
        return tohead;
    }

    public final void setTohead(String tohead)
    {
        this.tohead = tohead;
    }

    public final int getToisvip()
    {
        return toisvip;
    }

    public final void setToisvip(int toisvip)
    {
        this.toisvip = toisvip;
    }

    public final int getToisrealname()
    {
        return toisrealname;
    }

    public final void setToisrealname(int toisrealname)
    {
        this.toisrealname = toisrealname;
    }

    public final int getReadflag()
    {
        return readflag;
    }

    public final void setReadflag(int readflag)
    {
        this.readflag = readflag;
    }

    public final int getMsgbox()
    {
        return msgbox;
    }

    public final void setMsgbox(int msgbox)
    {
        this.msgbox = msgbox;
    }

    public final String getPubtime()
    {
        return pubtime;
    }

    public final void setPubtime(String pubtime)
    {
        this.pubtime = pubtime;
    }

    public final long getTweetid()
    {
        return tweetid;
    }

    public final void setTweetid(long tweetid)
    {
        this.tweetid = tweetid;
    }

}
