package net.weibo.app.bean;

import java.io.Serializable;

public class Letter implements Serializable
{
    private int    readflag; // 私信内容,
    private int    msgbox;  // 表示是在收件箱还是发件箱(1：收件箱；2：发件箱；3：群聊),
    private String pubtime; // 发表时间,
    private String tweetid; // 消息id,
    private String text;    // 私信内容,
    private String origtext; // 原始内容,
    private Video  video;   // 视频信息
    private Music  music;   // 音频信息
    private String image;   // 图片url列表

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

    public final String getTweetid()
    {
        return tweetid;
    }

    public final void setTweetid(String tweetid)
    {
        this.tweetid = tweetid;
    }

    public final String getText()
    {
        return text;
    }

    public final void setText(String text)
    {
        this.text = text;
    }

    public final String getOrigtext()
    {
        return origtext;
    }

    public final void setOrigtext(String origtext)
    {
        this.origtext = origtext;
    }

    public final Video getVideo()
    {
        return video;
    }

    public final void setVideo(Video video)
    {
        this.video = video;
    }

    public final Music getMusic()
    {
        return music;
    }

    public final void setMusic(Music music)
    {
        this.music = music;
    }

    public final String getImage()
    {
        return image;
    }

    public final void setImage(String image)
    {
        this.image = image;
    }

}
