package net.weibo.app.bean;

/**
 * 我的个人信息实体类
 */
public class MyInfo extends Entity
{
    private static MyInfo myinfo;
    private String        birth_day;
    private String        birth_month;
    private String        birth_year;
    private String        fansnum;
    private String        favnum;
    private String        headUrl;
    private String        homepage;
    private String        introduction;
    private String        isrealname;
    private String        isvip;
    private String        mutual_fans_num;
    private String        name;
    private String        nick;
    private String        openid;
    private String        send_private_flag;
    private String        sex;
    private String        tweetnum;
    private String        verifyinfo;
    private String        exp;
    private String        level;
    private String        idolnum;

    private MyInfo()
    {

    }

    public static MyInfo getMyinfo()
    {
        if (null == myinfo)
        {
            myinfo = new MyInfo();
        }
        return myinfo;
    }

    public static void setMyinfo(MyInfo myinfo)
    {
        MyInfo.myinfo = myinfo;
    }

    public final String getBirth_day()
    {
        return birth_day;
    }

    public final void setBirth_day(String birth_day)
    {
        this.birth_day = birth_day;
    }

    public final String getBirth_month()
    {
        return birth_month;
    }

    public final void setBirth_month(String birth_month)
    {
        this.birth_month = birth_month;
    }

    public final String getBirth_year()
    {
        return birth_year;
    }

    public final void setBirth_year(String birth_year)
    {
        this.birth_year = birth_year;
    }

    public final String getFansnum()
    {
        return fansnum;
    }

    public final void setFansnum(String fansnum)
    {
        this.fansnum = fansnum;
    }

    public final String getFavnum()
    {
        return favnum;
    }

    public final void setFavnum(String favnum)
    {
        this.favnum = favnum;
    }

    public final String getHeadUrl()
    {
        return headUrl;
    }

    public final void setHeadUrl(String headUrl)
    {
        this.headUrl = headUrl;
    }

    public final String getHomepage()
    {
        return homepage;
    }

    public final void setHomepage(String homepage)
    {
        this.homepage = homepage;
    }

    public final String getIntroduction()
    {
        return introduction;
    }

    public final void setIntroduction(String introduction)
    {
        this.introduction = introduction;
    }

    public final String getIsrealname()
    {
        return isrealname;
    }

    public final void setIsrealname(String isrealname)
    {
        this.isrealname = isrealname;
    }

    public final String getIsvip()
    {
        return isvip;
    }

    public final void setIsvip(String isvip)
    {
        this.isvip = isvip;
    }

    public final String getMutual_fans_num()
    {
        return mutual_fans_num;
    }

    public final void setMutual_fans_num(String mutual_fans_num)
    {
        this.mutual_fans_num = mutual_fans_num;
    }

    public final String getName()
    {
        return name;
    }

    public final void setName(String name)
    {
        this.name = name;
    }

    public final String getNick()
    {
        return nick;
    }

    public final void setNick(String nick)
    {
        this.nick = nick;
    }

    public final String getOpenid()
    {
        return openid;
    }

    public final void setOpenid(String openid)
    {
        this.openid = openid;
    }

    public final String getSend_private_flag()
    {
        return send_private_flag;
    }

    public final void setSend_private_flag(String send_private_flag)
    {
        this.send_private_flag = send_private_flag;
    }

    public final String getSex()
    {
        return sex;
    }

    public final void setSex(String sex)
    {
        this.sex = sex;
    }

    public final String getTweetnum()
    {
        return tweetnum;
    }

    public final void setTweetnum(String tweetnum)
    {
        this.tweetnum = tweetnum;
    }

    public final String getVerifyinfo()
    {
        return verifyinfo;
    }

    public final void setVerifyinfo(String verifyinfo)
    {
        this.verifyinfo = verifyinfo;
    }

    public final String getExp()
    {
        return exp;
    }

    public final void setExp(String exp)
    {
        this.exp = exp;
    }

    public final String getLevel()
    {
        return level;
    }

    public final void setLevel(String level)
    {
        this.level = level;
    }

    public final String getIdolnum()
    {
        return idolnum;
    }

    public final void setIdolnum(String idolnum)
    {
        this.idolnum = idolnum;
    }

}
