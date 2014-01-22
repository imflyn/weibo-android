package net.weibo.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import net.weibo.app.AppConfig;
import net.weibo.app.AppContext;
import net.weibo.app.AppException;
import net.weibo.app.authorize.AuthorizeContrast;
import net.weibo.app.bean.URLs;
import net.weibo.http.AsyncHttpClient;
import net.weibo.http.AsyncHttpResponseHandler;
import net.weibo.http.RequestParams;
import net.weibo.http.SyncHttpClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

/**
 * API客户端接口：用于访问网络数据
 */
public class ApiClient
{
    private static AsyncHttpClient httpClient = new AsyncHttpClient();

    /**
     * 我的个人资料
     * 
     * @param appContext
     * @return
     * @throws AppException
     */
    public static void getMyInfo(AppContext appContext, AsyncHttpResponseHandler handler) throws AppException
    {

        RequestParams params = appContext.getRequestParams();

        httpClient.get(URLs.MY_INFO, params, handler);

    }

    /**
     * 获取用户资料
     * 
     * @param appContext
     * @return
     * @throws AppException
     */
    public static String getUserInfo(AppContext appContext, String name) throws AppException
    {
        SyncHttpClient clinet = new SyncHttpClient()
        {
            @Override
            public String onRequestFailed(Throwable error, String content)
            {

                return null;
            }
        };
        RequestParams params = appContext.getRequestParams();

        params.put("name", name);

        return clinet.get(URLs.USER_INFO, params);

    }

    /**
     * 晒头像
     * 
     * @param map
     * @throws AppException
     */
    public static void showHeadPic(HashMap<String, String> map, AppContext appContext, AsyncHttpResponseHandler handler) throws AppException
    {

        RequestParams params = appContext.getRequestParams();

        params.put("content", map.get("content"));
        try
        {
            params.put("pic", new File(map.get("pic")));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        httpClient.post(URLs.ADD_PIC, params, handler);
    }

    /**
     * 更新头像
     * 
     */
    public static void updateHeadPic(String url, AppContext appContext, AsyncHttpResponseHandler handler) throws AppException
    {
        RequestParams params = appContext.getRequestParams();
        try
        {
            params.put("pic", new File(url));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        httpClient.post(URLs.UPDATE_HEAD_PIC, params, handler);
    }

    /**
     * 更新个人资料
     * 
     */
    public static void updateInfo(String nick, String introduction, AppContext appContext, AsyncHttpResponseHandler handler) throws AppException
    {
        RequestParams params = appContext.getRequestParams();
        params.put("nick", nick);
        params.put("introduction", introduction);

        httpClient.post(URLs.UPDATE_INFO, params, handler);
    }

    /**
     * 获取用户听众列表
     * 
     * @param appContext
     * @param handler
     * @throws AppException
     */
    public static void getMyListenersInfo(String startindex, String fopenid, AppContext appContext, AsyncHttpResponseHandler handler) throws AppException
    {
        RequestParams params = appContext.getRequestParams();
        params.put("reqnum", "30");
        params.put("startindex", startindex);
        params.put("fopenid", fopenid);
        params.put("mode", "0");
        params.put("name", "");
        httpClient.get(URLs.USER_LISTENTERS_INFO, params, handler);
    }

    /**
     * 收听某人
     * 
     * @param name
     * @param appContext
     * @param handler
     * @throws AppException
     */
    public static void listenerOne(String name, AppContext appContext) throws AppException
    {
        RequestParams params = appContext.getRequestParams();
        params.put("name", name);
        httpClient.post(URLs.LISTENTER_ONE, params, null);
    }

    /**
     * 获取我的偶像列表
     * 
     * @param startindex
     * @param appContext
     * @throws AppException
     */
    public static String getMyFollowers(String startindex, AppContext appContext) throws AppException
    {
        RequestParams params = appContext.getRequestParams();
        params.put("startindex", startindex);
        params.put("reqnum", "200");
        params.put("mode", "0");

        StringBuffer sb = new StringBuffer();
        String url = AsyncHttpClient.getUrlWithQueryString(URLs.MY_FOLLOWERS_LIST, params);

        HttpClient client = httpClient.getHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = null;
        HttpEntity entity = null;

        try
        {
            response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200)
            {
                entity = response.getEntity();
                sb.append(EntityUtils.toString(entity));
            }
        } catch (ClientProtocolException e)
        {
            e.printStackTrace();
            return null;
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        } finally
        {
            entity = null;
            client = null;
        }
        return sb.toString();
    }

    /**
     * 
     * 刷新授权使用期限
     */
    public static void authorize_refresh(AppContext appContext, AsyncHttpResponseHandler handler) throws AppException
    {
        RequestParams params = appContext.getRequestParams();

        params.put("client_id", AuthorizeContrast.clientId);
        params.put("grant_type", "refresh_token");
        params.put("refresh_token", AppConfig.getAppConfig(appContext).get("refresh_token"));

        httpClient.post(URLs.AUTHORIZE_REFRESH, params, handler);

    }

    /**
     * 获取微博图片 pageflag分页标识（0：第一页，1：向下翻页，2：向上翻页） pagetime
     * 本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间）
     * 
     * @param appContext
     * @param handler
     * @throws AppException
     * 
     * 
     */
    public static void getAlbum(String pageflag, String pagetime, String name, AppContext appContext, AsyncHttpResponseHandler handler) throws AppException
    {
        RequestParams params = appContext.getRequestParams();
        params.put("reqnum", "24");
        params.put("pageflag", pageflag);
        params.put("pagetime", pagetime);
        params.put("name", name);

        httpClient.get(appContext, URLs.GET_ALBUM, params, handler);
    }

    /**
     * 获取最近使用的话题
     * 
     * @param appContext
     * @param handler
     * @throws AppException
     */
    public static void getMyTopic(AppContext appContext, AsyncHttpResponseHandler handler) throws AppException
    {
        RequestParams params = appContext.getRequestParams();

        params.put("client_id", AuthorizeContrast.clientId);
        params.put("grant_type", "refresh_token");
        params.put("refresh_token", AppConfig.getAppConfig(appContext).get("refresh_token"));

        httpClient.get(URLs.MY_TOPIC, params, handler);
    }

    /**
     * 
     * 获取当前登录用户已经关注的人的微博
     * 
     * pageflag 是 分页标识（0：第一页，1：向下翻页，2：向上翻页 pagetime
     * 本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间） reqnum
     * 每次请求记录的条数（1-70条） type 是 拉取类型（需填写十进制数字） 0x1 原创发表 0x2 转载
     * 如需拉取多个类型请使用|，如(0x1|0x2)得到3，则type=3即可，填零表示拉取所有类型 contenttype
     * 内容过滤。0-表示所有类型，
     * 1-带文本，2-带链接，4-带图片，8-带视频，0x10-带音频建议不使用contenttype为1的类型，如果要拉取只有文本的微博
     * ，建议使用0x80
     * 
     */
    public static void getMyWeibo(String reqnum, String type, String contenttype, String pageflag, String pagetime, AppContext appContext, AsyncHttpResponseHandler handler) throws AppException
    {
        RequestParams params = appContext.getRequestParams();

        params.put("reqnum", reqnum);
        params.put("type", type);
        params.put("contenttype", contenttype);
        params.put("pageflag", pageflag);
        params.put("pagetime", pagetime);

        httpClient.get(URLs.GET_MYWEIBO, params, handler);
    }

    /**
     * 获取他人微博
     * 
     * @param pageflag
     * @param pagetime
     * @param reqnum
     * @param lastid
     * @param name
     * @param type
     * @param contenttype
     * @param appContext
     * @return
     * @throws AppException
     */
    public static String getOthersWeibo(String pageflag, String pagetime, String reqnum, String lastid, String name, String type, String contenttype, AppContext appContext) throws AppException
    {
        SyncHttpClient clinet = new SyncHttpClient()
        {

            @Override
            public String onRequestFailed(Throwable error, String content)
            {

                return null;
            }
        };

        RequestParams params = appContext.getRequestParams();

        params.put("pageflag", pageflag);
        params.put("pagetime", pagetime);
        params.put("lastid", lastid);
        params.put("name", name);
        params.put("type", type);
        params.put("reqnum", reqnum);
        params.put("contenttype", contenttype);

        return clinet.get(URLs.GET_OTHERS_WEIBO, params);
    }

    /**
     * 发送微博
     * 
     */
    public static String writeNews(String longitude, String latitude, String pic, String content, String syncflag, String compatibleflag, AppContext appContext) throws AppException
    {
        String result = null;
        SyncHttpClient clinet = new SyncHttpClient()
        {

            @Override
            public String onRequestFailed(Throwable error, String content)
            {

                return null;
            }
        };
        RequestParams params = appContext.getRequestParams();

        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("content", content);
        params.put("syncflag", syncflag);
        params.put("compatibleflag", compatibleflag);

        try
        {
            params.put("pic", new File(pic));
            result = clinet.post(URLs.ADD_PIC, params);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            result = clinet.post(URLs.WRITE_NEWS, params);
        }

        return result;
    }

    /**
     * 转发微博
     * 
     * @param content
     * @param reid
     * @param appContext
     * @return
     * @throws AppException
     */
    public static String reWriteNews(String content, String reid, AppContext appContext) throws AppException
    {

        SyncHttpClient clinet = new SyncHttpClient()
        {

            @Override
            public String onRequestFailed(Throwable error, String content)
            {

                return null;
            }
        };
        RequestParams params = appContext.getRequestParams();

        params.put("content", content);
        params.put("reid", reid);

        String result = clinet.post(URLs.RE_WRITE_NEWS, params);

        return result;
    }

    /**
     * 收藏微博
     * 
     */
    public static String saveNews(String reid, AppContext appContext) throws AppException
    {

        SyncHttpClient clinet = new SyncHttpClient()
        {

            @Override
            public String onRequestFailed(Throwable error, String content)
            {

                return null;
            }
        };
        RequestParams params = appContext.getRequestParams();

        params.put("reid", reid);

        String result = clinet.post(URLs.SAVE_NEWS, params);

        return result;
    }

    /**
     * 获取转发列表
     * 
     * @param flag
     * @param rootid
     * @param pageflag
     * @param pagetime
     * @param reqnum
     * @param twitterid
     * @param appContext
     * @return
     * @throws AppException
     */
    public static String getRepostList(String flag, String rootid, String pageflag, String pagetime, String reqnum, String twitterid, AppContext appContext) throws AppException
    {

        SyncHttpClient clinet = new SyncHttpClient()
        {

            @Override
            public String onRequestFailed(Throwable error, String content)
            {

                return null;
            }
        };
        RequestParams params = appContext.getRequestParams();

        params.put("flag", flag);
        params.put("rootid", rootid);
        params.put("pageflag", pageflag);
        params.put("pagetime", pagetime);
        params.put("reqnum", reqnum);
        params.put("twitterid", twitterid);

        return clinet.get(URLs.REPOST_NEWS_LIST, params);
    }

    /**
     * 获取单条微博信息
     * 
     * @param ids
     * @param appContext
     * @return
     * @throws AppException
     */
    public static String getNewsInfo(String ids, AppContext appContext) throws AppException
    {
        SyncHttpClient clinet = new SyncHttpClient()
        {

            @Override
            public String onRequestFailed(Throwable error, String content)
            {

                return null;
            }
        };
        RequestParams params = appContext.getRequestParams();

        params.put("ids", ids);

        return clinet.get(URLs.GET_NEWS_INFO, params);
    }

    /**
     * 获取转发或点评数量 flag 类型标识，0－获取转播计数，1－获取点评计数 2－两者都获取
     */
    public static String getReCount(String ids, String flag, AppContext appContext) throws AppException
    {
        SyncHttpClient clinet = new SyncHttpClient()
        {

            @Override
            public String onRequestFailed(Throwable error, String content)
            {

                return null;
            }
        };
        RequestParams params = appContext.getRequestParams();

        params.put("ids", ids);
        params.put("flag", flag);

        return clinet.get(URLs.GET_NEWS_RECOUNT, params);
    }

    /**
     * 获取私信
     * 
     * @param pageflag
     *            分页标识（0：第一页，1：向下翻页，2：向上翻页）
     * @param pagetime
     *            本页起始时间，用于翻页，和pageflag、lastid配合使用（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，
     *            向下翻页：填上一次请求返回的最后一条记录时间）
     * @param reqnum
     *            每次请求记录的条数（最多30条，默认30条）
     * @param lastid
     *            用于翻页，和pageflag、pagetime配合使用（第一页：填0，向上翻页：填上一次请求返回的第一条记录id，向下翻页：
     *            填上一次请求返回的最后一条记录id）
     * @param listtype
     *            私信类型，0-只拉1对1会话列表，1-只拉群聊列表，3-拉1对1会话及群聊列表 (yyj备注:这里填0)
     * @param appContext
     * @return
     * @throws AppException
     */
    public static String getLetters(String pageflag, String pagetime, String reqnum, String lastid, String listtype, AppContext appContext) throws AppException
    {
        SyncHttpClient clinet = new SyncHttpClient()
        {
            @Override
            public String onRequestFailed(Throwable error, String content)
            {

                return null;
            }
        };
        RequestParams params = appContext.getRequestParams();

        params.put("pageflag", pageflag);
        params.put("pagetime", pagetime);
        params.put("reqnum", reqnum);
        params.put("lastid", lastid);
        params.put("listtype", listtype);

        return clinet.get(URLs.GET_LETTERS, params);
    }

    /**
     * 获取@我的微博
     * 
     * @param pageflag
     * @param pagetime
     * @param reqnum
     * @param lastid
     * @param type
     * @param contenttype
     * @param appContext
     * @return
     * @throws AppException
     */
    public static String getAtMe(String pageflag, String pagetime, String reqnum, String lastid, String type, String contenttype, AppContext appContext) throws AppException
    {
        SyncHttpClient clinet = new SyncHttpClient()
        {
            @Override
            public String onRequestFailed(Throwable error, String content)
            {

                return null;
            }
        };
        RequestParams params = appContext.getRequestParams();

        params.put("pageflag", pageflag);
        params.put("pagetime", pagetime);
        params.put("reqnum", reqnum);
        params.put("lastid", lastid);
        params.put("type", type);
        params.put("contenttype", contenttype);

        return clinet.get(URLs.GET_AT_ME, params);
    }

    /**
     * 获取地理位置
     * 
     * @param longitude
     * @param latitude
     * @param appContext
     * @return
     * @throws AppException
     */
    public static String getLocation(String longitude, String latitude, String reqnum, String radius, String position, AppContext appContext) throws AppException
    {
        SyncHttpClient clinet = new SyncHttpClient()
        {
            @Override
            public String onRequestFailed(Throwable error, String content)
            {

                return null;
            }
        };
        RequestParams params = appContext.getRequestParams();

        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("reqnum", reqnum);
        params.put("radius", radius);
        params.put("position", position);

        return clinet.post(URLs.GET_LOCATION, params);
    }

    /**
     * 评论微博
     * 
     * @param content
     * @param reid
     * @param appContext
     * @return
     * @throws AppException
     */
    public static String writeComment(String content, String reid, AppContext appContext) throws AppException
    {

        SyncHttpClient clinet = new SyncHttpClient()
        {

            @Override
            public String onRequestFailed(Throwable error, String content)
            {

                return null;
            }
        };
        RequestParams params = appContext.getRequestParams();

        params.put("content", content);
        params.put("reid", reid);

        String result = clinet.post(URLs.WRITE_COMMENT, params);

        return result;
    }

    /**
     * 取消收听
     * 
     * @param name
     * @param appContext
     * @return
     * @throws AppException
     */
    public static String cancleListen(String name, AppContext appContext) throws AppException
    {

        SyncHttpClient clinet = new SyncHttpClient()
        {

            @Override
            public String onRequestFailed(Throwable error, String content)
            {

                return null;
            }
        };
        RequestParams params = appContext.getRequestParams();

        params.put("name", name);

        String result = clinet.post(URLs.DEL_LISTENTER_ONE, params);

        return result;
    }

    /**
     * 获取与某人的私信列表
     * 
     * @param pageflag
     * @param pagetime
     * @param reqnum
     * @param lastid
     * @param name
     * @param appContext
     * @return
     * @throws AppException
     */
    public static String getLetterByOne(String pageflag, String pagetime, String reqnum, String lastid, String name, AppContext appContext) throws AppException
    {

        SyncHttpClient clinet = new SyncHttpClient()
        {

            @Override
            public String onRequestFailed(Throwable error, String content)
            {

                return null;
            }
        };
        RequestParams params = appContext.getRequestParams();

        params.put("pageflag", pageflag);
        params.put("pagetime", pagetime);
        params.put("reqnum", reqnum);
        params.put("lastid", lastid);
        params.put("name", name);

        String result = clinet.get(URLs.GET_LETTER, params);

        return result;

    }

}
