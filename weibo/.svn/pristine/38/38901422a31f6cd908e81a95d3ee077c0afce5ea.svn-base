package net.weibo.app.bean;

import java.io.Serializable;

import net.weibo.app.lib.ListViewTool;
import android.app.Activity;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;

public class Message implements Serializable
{

    String                    name;                   // 当前用户微博账号,
    String                    openid;                 // 当前用户openid,
    String                    nick;                   // 当前用户昵称

    String                    text;                   // 私信内容
    String                    origtext;               // 原始内容
    transient SpannableString listViewSpannableString;
    transient String          sourceString;

    Video                     video;                  // 视频
    Music                     music;                  // 音乐
    String[]                  image;                  // 图片url列表

    /**
     * 添加url表情处理
     * 
     * @param activity
     * @return
     */
    public SpannableString getListViewSpannableString(Activity activity)
    {
        if (!TextUtils.isEmpty(listViewSpannableString))
        {
            return listViewSpannableString;
        } else
        {
            setMusicInfo();
            setVideoInfo();
            ListViewTool.addJustHighLightLinks(this, activity);
            return listViewSpannableString;
        }
    }

    public void setListViewSpannableString(SpannableString listViewSpannableString)
    {
        this.listViewSpannableString = listViewSpannableString;
    }

    /**
     * 转化html格式
     * 
     * @return
     */
    public String getSourceString()
    {
        if (!TextUtils.isEmpty(sourceString))
        {
            return sourceString;

        } else
        {

            if (!TextUtils.isEmpty(text))
                this.text = Html.fromHtml(text).toString();
            sourceString = this.text;
            return text;
        }
    }

    /**
     * 设置音乐
     */
    public void setMusicInfo()
    {
        if (null != music)
        {
            StringBuilder sb = new StringBuilder(text);
            sb.append("  分享音乐(");
            if (null != music.getTitle())
                sb.append(music.getTitle() + ":");
            sb.append(music.getUrl() + ")");

            text = sb.toString();
        }
    }

    /**
     * 设置视频
     */
    public void setVideoInfo()
    {
        if (null != video)
        {
            StringBuilder sb = new StringBuilder(text);
            sb.append("  分享视频(");
            if (null != video.getTitle())
                sb.append(video.getTitle() + ":");
            sb.append(video.getShorturl() + ")");
            if (null != video.getPicurl())
                image = new String[] { video.getPicurl() };

            text = sb.toString();
        }
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

    public final String[] getImage()
    {
        return image;
    }

    public final void setImage(String[] image)
    {
        this.image = image;
    }

    public final SpannableString getListViewSpannableString()
    {
        return listViewSpannableString;
    }

    public final void setSourceString(String sourceString)
    {
        this.sourceString = sourceString;
    }

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

}
