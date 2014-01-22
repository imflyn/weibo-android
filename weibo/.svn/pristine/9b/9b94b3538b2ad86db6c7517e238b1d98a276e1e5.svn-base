package net.weibo.api;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import net.weibo.app.AppConfig;
import net.weibo.app.AppContext;
import net.weibo.app.AppException;
import net.weibo.app.bean.MyInfo;
import net.weibo.app.bean.People;
import net.weibo.app.bean.Result;
import net.weibo.common.FileCache;
import net.weibo.common.ImageDownloader;
import net.weibo.common.MemoryCache;
import net.weibo.common.ImageUtils;
import net.weibo.dao.LocalHeadPicDBService;
import net.weibo.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

public class InfoImpl
{
    private AppContext mContext;
    private AppConfig  appConfig;
    private MyInfo     myInfo;

    public InfoImpl(AppContext mContext)
    {
        this.mContext = mContext;
        appConfig = AppConfig.getAppConfig(mContext);
        myInfo = MyInfo.getMyinfo();
    }

    /**
     * 从本地获取个人信息
     * 
     * @return
     */
    public MyInfo getMyInfoFromLocal()
    {

        // myInfo.setBirth_day(get("Birth_day"));
        // myInfo.setBirth_month(get("Birth_month"));
        // myInfo.setBirth_year(get("Birth_year"));
        // myInfo.setExp(get("Exp"));
        // myInfo.setFansnum(get("Fansnum"));
        // myInfo.setFavnum(get("Favnum"));
        // myInfo.setHeadUrl(get("HeadUrl"));
        // myInfo.setHomepage(get("Homepage"));
        myInfo.setIntroduction(get("Introduction"));
        // myInfo.setIsrealname(get("Isrealname"));
        // myInfo.setIsvip(get("Isvip"));
        // myInfo.setLevel(get("Level"));
        // myInfo.setMutual_fans_num(get("Mutual_fans_num"));
        myInfo.setName(get("Name"));
        myInfo.setNick(get("Nick"));
        myInfo.setOpenid(get("openId"));
        // myInfo.setSend_private_flag(get("Send_private_flag"));
        // myInfo.setSex(get("Sex"));
        // myInfo.setTweetnum(get("Tweetnum"));
        // myInfo.setVerifyinfo(get("Verifyinfo"));
        // myInfo.setIdolnum(get("Idolnum"));
        return myInfo;
    }

    private String get(String key)
    {
        return appConfig.get(key);
    }

    /**
     * 从网络获取个人信息
     * 
     * @return
     */
    public void getMyInfoFromNet(AsyncHttpResponseHandler handler)
    {

        try
        {
            ApiClient.getMyInfo(mContext, handler);
        } catch (AppException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获得个人头像
     * 
     * @return
     */
    public ArrayList<Bitmap> getHeadPic()
    {
        MemoryCache memoryCache = MemoryCache.getMemoryCache(mContext);
        FileCache fileCache = new FileCache();
        String url = get("HeadUrl") + "/100";
        Bitmap result = null;

        result = memoryCache.getBitmapFromCache(url);
        if (result == null)
        {
            // 文件缓存中获取
            result = fileCache.getImage(url);
            if (result != null)
            {
                // 添加到内存缓存
                memoryCache.addBitmapToCache(url, result);
            } else
            {
                if (mContext.isNetworkConnected())
                {
                    // 从网络获取
                    result = ImageUtils.zoomBitmap(ImageDownloader.downloadBitmap(url), 120, 120);

                    if (result != null)
                    {
                        memoryCache.addBitmapToCache(url, result);
                        fileCache.saveBitmap(result, url);
                    }
                }
            }
        }

        ArrayList<Bitmap> pics = new ArrayList<Bitmap>();
        ArrayList<String> urls = new ArrayList<String>();
        if (null != result)
        {
            pics.add(ImageUtils.getRoundedCornerBitmap(result, 8));
        }
        ;

        LocalHeadPicDBService db = new LocalHeadPicDBService(mContext);
        urls = db.query(appConfig.get("openId"));
        if (null != urls)
        {
            for (String picUrl : urls)
            {
                result = memoryCache.getBitmapFromCache(picUrl);
                if (result == null)
                {
                    // 文件缓存中获取
                    result = fileCache.getImage(ImageUtils.PHOTO_SAVEPATH + picUrl);
                    if (result != null)
                    {
                        // 添加到内存缓存
                        memoryCache.addBitmapToCache(picUrl, result);
                    }
                }
                if (result != null)
                    pics.add(ImageUtils.getRoundedCornerBitmap(result, 14));
            }
        }
        urls = null;
        return pics;
    }

    /**
     * 获得个人头像2
     * 
     * @return
     */
    public ArrayList<HashMap<String, Object>> getHeadPic2()
    {
        ArrayList<HashMap<String, Object>> pics = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = null;
        MemoryCache memoryCache = MemoryCache.getMemoryCache(mContext);
        FileCache fileCache = new FileCache();
        String url = get("HeadUrl") + "/100";
        Bitmap result = null;
        result = memoryCache.getBitmapFromCache(url);
        if (result == null)
        {
            // 文件缓存中获取
            result = fileCache.getImage(url);
            if (result != null)
            {
                // 添加到内存缓存
                memoryCache.addBitmapToCache(url, result);
            } else
            {
                if (mContext.isNetworkConnected())
                {
                    // 从网络获取
                    result = ImageUtils.zoomBitmap(ImageDownloader.downloadBitmap(url), 120, 120);

                    if (result != null)
                    {
                        memoryCache.addBitmapToCache(url, result);
                        fileCache.saveBitmap(result, url);
                    }
                }
            }
        }

        ArrayList<String> urls = new ArrayList<String>();
        if (null != result)
        {
            map = new HashMap<String, Object>();
            map.put("pic", ImageUtils.getRoundedCornerBitmap(result, 8));
            map.put("url", url);
            pics.add(map);

        }
        ;

        LocalHeadPicDBService db = new LocalHeadPicDBService(mContext);
        urls = db.query(appConfig.get("openId"));
        if (null != urls)
        {
            for (String picUrl : urls)
            {
                result = memoryCache.getBitmapFromCache(picUrl);
                if (result == null)
                {
                    // 文件缓存中获取
                    result = fileCache.getImage(picUrl);
                    if (result != null)
                    {
                        // 添加到内存缓存
                        memoryCache.addBitmapToCache(picUrl, result);
                    }
                }
                if (result != null)
                {
                    map = new HashMap<String, Object>();
                    map.put("pic", ImageUtils.getRoundedCornerBitmap(result, 14));
                    map.put("url", picUrl);
                    pics.add(map);
                }

            }
        }
        return pics;
    }

    /**
     * 解析我的资料数据
     * 
     * @throws JSONException
     */
    public MyInfo parse(String data) throws JSONException
    {
        JSONObject jsonObject = new JSONObject(data);
        JSONObject jsonObject2 = jsonObject.getJSONObject("data");
        // myInfo.setBirth_day(jsonObject2.getInt("birth_day") + "");
        // myInfo.setBirth_month(jsonObject2.getInt("birth_month") + "");
        // myInfo.setBirth_year(jsonObject2.getInt("birth_year") + "");
        // myInfo.setExp(jsonObject2.getInt("exp") + "");
        myInfo.setFansnum(jsonObject2.getInt("fansnum") + "");
        myInfo.setFavnum(jsonObject2.getInt("favnum") + "");
        myInfo.setHeadUrl(jsonObject2.getString("head"));
        // myInfo.setHomepage(jsonObject2.getString("homepage"));
        myInfo.setIntroduction(jsonObject2.getString("introduction") + "");
        // myInfo.setIsrealname(jsonObject2.getInt(("isrealname")) + "");
        // myInfo.setIsvip(jsonObject2.getInt(("isvip")) + "");
        // myInfo.setLevel(jsonObject2.getInt(("level")) + "");
        // myInfo.setMutual_fans_num(jsonObject2.getInt("mutual_fans_num") +
        // "");
        myInfo.setName(jsonObject2.getString("name"));
        myInfo.setNick(jsonObject2.getString("nick"));
        myInfo.setOpenid(jsonObject2.getString("openid"));
        // myInfo.setSend_private_flag(jsonObject2.getInt(("send_private_flag"))
        // + "");
        // myInfo.setSex(jsonObject2.getInt("sex") + "");
        myInfo.setTweetnum(jsonObject2.getInt("tweetnum") + "");
        myInfo.setVerifyinfo(jsonObject2.getString("verifyinfo"));
        myInfo.setIdolnum(jsonObject2.getInt("idolnum") + "");

        // appConfig.set("Birth_day", myInfo.getBirth_day());
        // appConfig.set("Birth_month", myInfo.getBirth_month());
        // appConfig.set("Birth_year", myInfo.getBirth_year());
        // appConfig.set("Exp", myInfo.getExp());
        // appConfig.set("Fansnum", myInfo.getFansnum());
        // appConfig.set("Favnum", myInfo.getFavnum());
        appConfig.set("HeadUrl", myInfo.getHeadUrl());
        // appConfig.set("Homepage", myInfo.getHomepage());
        appConfig.set("Introduction", myInfo.getIntroduction());
        // appConfig.set("Isrealname", myInfo.getIsrealname());
        // appConfig.set("Isvip", myInfo.getIsvip());
        // appConfig.set("Level", myInfo.getLevel());
        // appConfig.set("Mutual_fans_num", myInfo.getMutual_fans_num());
        appConfig.set("Name", myInfo.getName());
        appConfig.set("Nick", myInfo.getNick());
        appConfig.set("openId", myInfo.getOpenid());
        // appConfig.set("Send_private_flag", myInfo.getIsrealname());
        // appConfig.set("Sex", myInfo.getSex());
        // appConfig.set("Tweetnum", myInfo.getTweetnum());
        // appConfig.set("Verifyinfo", myInfo.getVerifyinfo());
        // appConfig.set("Idolnum", myInfo.getIdolnum());
        return myInfo;
    }

    /**
     * 晒头像
     */
    public void showHeadPic(String url, AppContext appContex, AsyncHttpResponseHandler handler)
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("content", "#晒头像#我刚更新的新头像，是不是很赞啊。最新的腾讯微博Android版可以传多张头像了，大家一起show起来吧！手机下载：http://url.cn/HoexXL");
        map.put("pic", url);
        try
        {
            ApiClient.showHeadPic(map, mContext, handler);
        } catch (AppException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * 更新个人信息
     */
    public void updateInfo(String nick, String introduction, AppContext appContex, AsyncHttpResponseHandler handler)
    {
        try
        {
            ApiClient.updateInfo(nick, introduction, mContext, handler);
        } catch (AppException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * 更新个人头像
     */
    public void updateHeadPic(String url, AppContext appContext, AsyncHttpResponseHandler handler)
    {
        url = "/mnt/sdcard/QQWeibo/imgCache" + File.separator + url + ".jpg";
        try
        {
            ApiClient.updateHeadPic(url, appContext, handler);
        } catch (AppException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * 获取用户资料
     * 
     * @param name
     * @return
     */
    public People getUserInfo(String name)
    {
        String result = "";
        try
        {
            result = ApiClient.getUserInfo(AppContext.getInstance(), name);
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
        {

            try
            {
                return parsePeople(result);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 解析用户资料数据
     * 
     * @throws JSONException
     */
    public People parsePeople(String data) throws JSONException
    {
        People peole = new People();

        JSONObject jsonObject = new JSONObject(data);
        JSONObject jsonObject2 = jsonObject.getJSONObject("data");
        peole.setFansnum(String.valueOf(jsonObject2.getInt("fansnum")));
        peole.setHead(jsonObject2.getString("head"));
        peole.setName(jsonObject2.getString("name"));
        peole.setNick(jsonObject2.getString("nick"));
        peole.setOpenid(jsonObject2.getString("openid"));
        peole.setIdolnum(String.valueOf(jsonObject2.getInt("idolnum")));
        peole.setFavnum(String.valueOf(jsonObject2.getInt("favnum")));
        peole.setTweetnum(String.valueOf(jsonObject2.getInt("tweetnum")));
        peole.setIsfans(jsonObject2.getInt("ismyidol") == 1);
        return peole;
    }

}
