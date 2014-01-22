package net.weibo.app.bean;

import java.io.Serializable;

public class Weibo implements Serializable
{
    private String longitude;
    private String latitude;
    private String pic_url;
    private String video_url;
    private String music_url;
    private String music_title;
    private String music_author;
    private String content;

    public final String getLongitude()
    {
        return longitude;
    }

    public final void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }

    public final String getLatitude()
    {
        return latitude;
    }

    public final void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }

    public final String getPic_url()
    {
        return pic_url;
    }

    public final void setPic_url(String pic_url)
    {
        this.pic_url = pic_url;
    }

    public final String getVideo_url()
    {
        return video_url;
    }

    public final void setVideo_url(String video_url)
    {
        this.video_url = video_url;
    }

    public final String getMusic_url()
    {
        return music_url;
    }

    public final void setMusic_url(String music_url)
    {
        this.music_url = music_url;
    }

    public final String getMusic_title()
    {
        return music_title;
    }

    public final void setMusic_title(String music_title)
    {
        this.music_title = music_title;
    }

    public final String getMusic_author()
    {
        return music_author;
    }

    public final void setMusic_author(String music_author)
    {
        this.music_author = music_author;
    }

    public final String getContent()
    {
        return content;
    }

    public final void setContent(String content)
    {
        this.content = content;
    }

}
