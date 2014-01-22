package net.weibo.api;

import net.weibo.app.AppContext;
import net.weibo.app.AppException;
import net.weibo.app.bean.LetterData;
import net.weibo.app.bean.LettersData;
import net.weibo.app.bean.Result;

import com.google.gson.Gson;

public class LettersImpl
{

    /**
     * 解析私信
     * 
     * @param data
     * @return
     */
    public LettersData parseLetters(String data)
    {
        LettersData info = null;

        Gson gson = new Gson();

        try
        {
            info = gson.fromJson(data, LettersData.class);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return info;
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
     */
    public LettersData getLetters(String pageflag, String pagetime, String reqnum, String lastid, String listtype)
    {

        String result = "";
        try
        {

            result = ApiClient.getLetters(pageflag, pagetime, reqnum, lastid, listtype, AppContext.getInstance());
            int ret = Result.parse(result).getRet();
            if (ret == 0)
            {
                return parseLetters(result);
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
     * 解析私信
     * 
     * @param data
     * @return
     */
    public LetterData parseLetter(String data)
    {
        LetterData info = null;

        Gson gson = new Gson();

        try
        {
            info = gson.fromJson(data, LetterData.class);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return info;
    }

    /**
     * 获取与某人的私信
     * 
     * @param pageflag
     * @param pagetime
     * @param reqnum
     * @param lastid
     * @param listtype
     * @return
     */
    public LetterData getLetterByOne(String pageflag, String pagetime, String reqnum, String lastid, String name)
    {
        String result = "";
        try
        {

            result = ApiClient.getLetterByOne(pageflag, pagetime, reqnum, lastid, name, AppContext.getInstance());
            int ret = Result.parse(result).getRet();
            if (ret == 0)
            {
                return parseLetter(result);
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
