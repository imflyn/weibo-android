package net.weibo.api;

import java.util.ArrayList;

import net.weibo.app.AppContext;
import net.weibo.app.AppException;
import net.weibo.app.bean.PeopleList;
import net.weibo.app.bean.People;
import net.weibo.dao.MyFollowersDBService;
import net.weibo.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListenersImpl
{
    private AppContext appContext;
    private PeopleList listeners;

    public ListenersImpl(AppContext appContext)
    {
        this.appContext = appContext;
    }

    public PeopleList parse(String data) throws JSONException
    {

        listeners = new PeopleList();
        People fans = null;
        ArrayList<People> list = new ArrayList<People>();
        JSONObject jsonObject = new JSONObject(data);
        JSONObject dataObject = jsonObject.getJSONObject("data");
        JSONArray infoArray = dataObject.getJSONArray("info");

        listeners.setTimestamp(dataObject.getString("timestamp"));
        listeners.setHasnext(dataObject.getInt("hasnext") + "");
        listeners.setNextstartpos(dataObject.getString("curnum"));

        JSONObject infoObject = null;
        int size = infoArray.length();
        for (int i = 0; i < size; i++)
        {
            infoObject = infoArray.getJSONObject(i);
            fans = new People();
            fans.setName(infoObject.getString("name"));
            fans.setOpenid(infoObject.getString("openid"));
            fans.setNick(infoObject.getString("nick"));
            fans.setHead(infoObject.getString("head"));
            fans.setSex(infoObject.getInt("sex") + "");
            fans.setFansnum(infoObject.getInt("fansnum") + "");
            fans.setIsfans(infoObject.getBoolean("isfans"));
            list.add(fans);
        }
        listeners.setFans(list);

        return listeners;
    }

    public void insertPeopleToDb(People p)
    {
        MyFollowersDBService db = new MyFollowersDBService(appContext);
        db.insertPeople(p);
    }

    public void getMyListenersInfo(String startIndex, String openId, AsyncHttpResponseHandler handler)
    {
        try
        {
            ApiClient.getMyListenersInfo(startIndex, openId, appContext, handler);
        } catch (AppException e)
        {
            e.printStackTrace();
        }
    }

    public void listenerOne(String name)
    {
        try
        {
            ApiClient.listenerOne(name, appContext);
        } catch (AppException e)
        {
            e.printStackTrace();
        }
    }

    public void delListenerOne(String name)
    {
        try
        {
            ApiClient.cancleListen(name, appContext);
        } catch (AppException e)
        {
            e.printStackTrace();
        }
    }

    public void delListenerFromDb(String name)
    {
        MyFollowersDBService db = new MyFollowersDBService(appContext);
        db.deletePeole(name);
    }

}
