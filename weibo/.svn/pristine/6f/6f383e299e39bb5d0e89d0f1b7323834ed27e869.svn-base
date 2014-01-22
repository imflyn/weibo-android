package net.weibo.api;

import net.weibo.app.AppContext;
import net.weibo.app.AppException;
import net.weibo.app.bean.NewsData;
import net.weibo.app.bean.Result;
import net.weibo.app.bean.Weibo;
import net.weibo.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

public class NewsImpl
{

    public void getMyWeibo(String reqnum, String type, String contenttype, String pageflag, String pagetime, AppContext appContext, AsyncHttpResponseHandler handler)
    {
        try
        {
            ApiClient.getMyWeibo(reqnum, type, contenttype, pageflag, pagetime, appContext, handler);
        } catch (AppException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 解析微博
     * 
     * @param data
     * @return
     */
    public NewsData parse(String data)
    {
        NewsData info = null;

        Gson gson = new Gson();
        // new TypeToken<NewsList>()
        // {
        // }.getType()

        try
        {
            info = gson.fromJson(data, NewsData.class);

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return info;
    }

    /**
     * 写微博
     * 
     * @param weibo
     * @return
     */
    public boolean writeNews(Weibo weibo)
    {
        String result = "";
        try
        {
            result = ApiClient.writeNews(weibo.getLongitude(), weibo.getLatitude(), weibo.getPic_url(), weibo.getContent(), "1", "0", AppContext.getInstance());
        } catch (AppException e)
        {
            e.printStackTrace();
        }
        int a = 0;
        try
        {
            a = Result.parse(result).getRet();
        } catch (Exception e)
        {
            a = -1;
            e.printStackTrace();
        }
        if (a == 0)
            return true;

        return false;
    }

    /**
     * 转发微博
     * 
     * @param content
     * @param reId
     * @return
     */
    public boolean reWriteNews(String content, String reId)
    {
        String result = "";
        try
        {
            result = ApiClient.reWriteNews(content, reId, AppContext.getInstance());
        } catch (AppException e)
        {
            e.printStackTrace();
        }
        int a = 0;
        try
        {
            a = Result.parse(result).getRet();
        } catch (Exception e)
        {
            a = -1;
            e.printStackTrace();
        }
        if (a == 0)
            return true;

        return false;
    }

    /**
     * 评论微博
     * 
     * @param content
     * @param reId
     * @return
     */
    public boolean writeComments(String content, String reId)
    {
        String result = "";
        try
        {
            result = ApiClient.writeComment(content, reId, AppContext.getInstance());
        } catch (AppException e)
        {
            e.printStackTrace();
        }
        int a = 0;
        try
        {
            a = Result.parse(result).getRet();
        } catch (Exception e)
        {
            a = -1;
            e.printStackTrace();
        }
        if (a == 0)
            return true;

        return false;
    }

    /**
     * 收藏微博
     * 
     * @param wbId
     * @return
     */
    public boolean saveNews(String wbId)
    {
        String result = "";
        try
        {
            result = ApiClient.saveNews(wbId, AppContext.getInstance());
        } catch (AppException e)
        {
            e.printStackTrace();
        }
        int a = 0;
        try
        {
            a = Result.parse(result).getRet();
        } catch (Exception e)
        {
            a = -1;
            e.printStackTrace();
        }
        if (a == 0)
            return true;

        return false;
    }

    /**
     * 获取转发列表
     * 
     * @param flag
     *            类型标识。0－转播列表，1－点评列表，2－点评与转播列表
     * @param rootid
     *            转发或回复的微博根结点id（源微博id）
     * @param pageflag
     *            分页标识，用于翻页（0：第一页，1：向下翻页，2：向上翻页）
     * @param pagetime
     *            本页起始时间，与pageflag、twitterid共同使用，实现翻页功能（第一页：填0，向上翻页：
     *            填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间）
     * @param reqnum
     *            每次请求记录的条数（1-100条）,默认为20条
     * @param twitterid
     *            微博id，与pageflag、pagetime共同使用，实现翻页功能（第1页填0，继续向下翻页，
     *            填上一次请求返回的最后一条记录id）
     * @param appContext
     * @param handler
     */
    public NewsData getRepostList(String flag, String rootid, String pageflag, String pagetime, String reqnum, String twitterid)
    {
        String result = "";
        try
        {

            result = ApiClient.getRepostList(flag, rootid, pageflag, pagetime, reqnum, twitterid, AppContext.getInstance());
            int ret = Result.parse(result).getRet();
            if (ret == 0)
            {
                return parse(result);
            } else
                return null;

        } catch (Exception e)
        {
            e.printStackTrace();

        }
        return null;
    }

    public NewsData getOthersWeibo(String pageflag, String pagetime, String reqnum, String lastid, String name, String type, String contenttype)
    {
        String result = "";
        try
        {
            result = ApiClient.getOthersWeibo(pageflag, pagetime, reqnum, lastid, name, type, contenttype, AppContext.getInstance());
            int ret = Result.parse(result).getRet();
            if (ret == 0)
            {
                return parse(result);
            } else
                return null;

        } catch (Exception e)
        {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * 获取单条微博信息
     * 
     * @param ids
     * @return
     */
    public NewsData getNewsInfo(String ids)
    {
        String result = "";
        try
        {
            result = ApiClient.getNewsInfo(ids, AppContext.getInstance());
            int ret = Result.parse(result).getRet();
            if (ret == 0)
            {
                return parse(result);
            } else
                return null;

        } catch (Exception e)
        {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * 获取微博转发和评论次数
     * 
     * @param ids
     *            flag 类型标识，0－获取转播计数，1－获取点评计数 2－两者都获取
     * @param flag
     * @return 0是转发 1是评论
     */
    public int[] getReCount(String ids, String flag)
    {
        int[] count = new int[2];
        String result;
        try
        {
            result = ApiClient.getReCount(ids, flag, AppContext.getInstance());

            int ret = Result.parse(result).getRet();
            if (ret == 0)
            {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject dataObject = jsonObject.getJSONObject("data");
                JSONObject infoObject = dataObject.getJSONObject(ids);

                count[0] = infoObject.getInt("count");
                count[1] = infoObject.getInt("mcount");

                return count;
            } else
                return null;

        } catch (Exception e)
        {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * 获取@我的微博
     * 
     * @param pageflag
     *            分页标识（0：第一页，1：向下翻页，2：向上翻页）
     * @param pagetime
     *            本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间）
     * @param reqnum
     *            每次请求记录的条数（1-70条）(yyj备注:填20)
     * @param lastid
     *            用于翻页，和pagetime配合使用（第一页：填0，向上翻页：填上一次请求返回的第一条记录id，向下翻页：
     *            填上一次请求返回的最后一条记录id）
     * @param type
     *            (yyj备注:填3)拉取类型（需填写十进制数字）
     *            0x1：原创发表，0x2：转载，0x8：回复，0x10：空回，0x20：提及，0x40：评论。
     *            如需拉取多个类型请使用|，如(0x1|0x2)得到3，则type=3即可，填零表示拉取所有类型
     * @param contenttype
     *            (yyj备注:填0)内容过滤。0-表示所有类型，1-带文本，2-带链接，4-带图片，8-带视频，0x10-带音频
     *            建议不使用contenttype为1的类型，如果要拉取只有文本的微博，建议使用0x80
     * @return
     */
    public NewsData getAtMe(String pageflag, String pagetime, String reqnum, String lastid, String type, String contenttype)
    {

        String result = "";
        try
        {
            result = ApiClient.getAtMe(pageflag, pagetime, reqnum, lastid, type, contenttype, AppContext.getInstance());

            int ret = Result.parse(result).getRet();
            if (ret == 0)
            {
                return parse(result);
            } else
                return null;
        } catch (AppException e)
        {

        } catch (Exception e)
        {
            e.printStackTrace();

        }
        return null;

    }

    /**
     * 获取位置
     * 
     * @param longitude
     * @param latitude
     * @param reqnum
     * @param radius
     * @param position
     * @return
     */
    public String getLocation(String longitude, String latitude, String reqnum, String radius, String position)
    {
        String result = "";

        try
        {
            result = ApiClient.getLocation(longitude, latitude, reqnum, radius, position, AppContext.getInstance());
            int ret = Result.parse(result).getRet();
            if (ret == 0)
            {

                JSONObject jsonObject = new JSONObject(result);
                JSONObject dataObject = jsonObject.getJSONObject("data");
                JSONArray infoArray = dataObject.getJSONArray("poiinfo");

                jsonObject = (JSONObject) infoArray.get(0);
                String locationName = jsonObject.getString("poiname");

                return locationName;
            } else
                return null;
        } catch (AppException e)
        {

        } catch (Exception e)
        {
            e.printStackTrace();

        }
        return null;
    }
}
