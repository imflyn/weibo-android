package net.weibo.app.bean;

import java.net.URLEncoder;

/**
 * 接口URL实体类
 */
public class URLs
{
    public final static String  HOST                 = "open.t.qq.com/api";

    public final static String  HTTP                 = "http://";
    public final static String  HTTPS                = "https://";

    public final static String  URL_SPLITTER         = "/";
    public final static String  URL_UNDERLINE        = "_";

    private final static String URL_API_HOST         = HTTPS + HOST + URL_SPLITTER;

    public final static String  AUTHORIZE_REFRESH    = "https://open.t.qq.com/cgi-bin/oauth2/access_token";
    public final static String  MY_INFO              = URL_API_HOST + "user/info";
    public final static String  USER_INFO            = URL_API_HOST + "user/other_info";
    public final static String  ADD_PIC              = URL_API_HOST + "t/add_pic";
    public final static String  UPDATE_HEAD_PIC      = URL_API_HOST + "user/update_head";
    public final static String  UPDATE_INFO          = URL_API_HOST + "user/update";
    public final static String  MY_LISTENTERS_INFO   = URL_API_HOST + "friends/fanslist_s";
    public final static String  USER_LISTENTERS_INFO = URL_API_HOST + "friends/user_fanslist";
    public final static String  LISTENTER_ONE        = URL_API_HOST + "friends/add";
    public final static String  MY_FOLLOWERS_LIST    = URL_API_HOST + "friends/idollist_s";
    public final static String  MY_TOPIC             = URL_API_HOST + "ht/recent_used";
    public final static String  GET_ALBUM            = URL_API_HOST + "statuses/get_micro_album";
    public final static String  GET_MYWEIBO          = URL_API_HOST + "statuses/home_timeline";
    public final static String  WRITE_NEWS           = URL_API_HOST + "t/add";
    public final static String  RE_WRITE_NEWS        = URL_API_HOST + "t/re_add";
    public final static String  SAVE_NEWS            = URL_API_HOST + "fav/addt";
    public final static String  REPOST_NEWS_LIST     = URL_API_HOST + "t/re_list";
    public final static String  GET_EMOTIONS         = URL_API_HOST + "other/get_emotions";
    public final static String  GET_NEWS_INFO        = URL_API_HOST + "t/list";
    public final static String  GET_NEWS_RECOUNT     = URL_API_HOST + "t/re_count";
    public final static String  GET_LETTERS          = URL_API_HOST + "private/home_timeline";
    public final static String  GET_LETTER           = URL_API_HOST + "private/user_timeline";
    public final static String  GET_AT_ME            = URL_API_HOST + "statuses/mentions_timeline";
    public final static String  GET_LOCATION         = URL_API_HOST + "lbs/get_poi";
    // public final static String WRITE_COMMENT = URL_API_HOST + "t/comment";
    public final static String  WRITE_COMMENT        = URL_API_HOST + "t/reply";
    public final static String  GET_OTHERS_WEIBO     = URL_API_HOST + "statuses/user_timeline";
    public final static String  DEL_LISTENTER_ONE    = URL_API_HOST + "friends/del";

    /**
     * 对URL进行格式处理
     * 
     * @param path
     * @return
     */
    public final static String formatURL(String path)
    {
        if (path.startsWith("http://") || path.startsWith("https://"))
            return path;
        return "http://" + URLEncoder.encode(path);
    }

    public final static String turnToHttp(String path)
    {
        if (path.startsWith("https://"))
            return HTTP + path.substring(8, path.length() - 1);
        else
            return path;

    }

    public final static boolean checkRealUrl(String url)
    {
        if (url.endsWith("/100") || url.endsWith("/50") || url.endsWith("/full") || url.endsWith("/120") || url.endsWith("/60") || url.endsWith("/80") || url.endsWith("/small")
                || url.endsWith("/320") || url.endsWith("/220") || url.endsWith("/500"))
            return false;
        return true;
    }

}
