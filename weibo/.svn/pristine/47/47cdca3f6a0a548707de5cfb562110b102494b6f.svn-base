package net.weibo.api;

import java.util.ArrayList;
import java.util.List;

import net.weibo.app.AppContext;
import net.weibo.app.AppException;
import net.weibo.app.bean.People;
import net.weibo.app.bean.PeopleList;
import net.weibo.common.Cn2Spell;
import net.weibo.dao.MyFollowersDBService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class FollowersImpl
{
    private AppContext        appContext;
    private PeopleList        followers;
    private ArrayList<People> list       = new ArrayList<People>();
    private int               retryCount = 0;

    public FollowersImpl(AppContext appContext)
    {
        this.appContext = appContext;
    }

    private PeopleList parse(String data) throws JSONException
    {

        followers = new PeopleList();
        People fans = null;
        ArrayList<People> list = new ArrayList<People>();
        JSONObject jsonObject = new JSONObject(data);
        JSONObject dataObject = jsonObject.getJSONObject("data");
        JSONArray infoArray = dataObject.getJSONArray("info");

        followers.setHasnext(dataObject.getInt("hasnext") + "");
        // followers.setNextstartpos(dataObject.getString("curnum"));

        JSONObject infoObject = null;
        int size = infoArray.length();
        for (int i = 0; i < size; i++)
        {
            infoObject = infoArray.getJSONObject(i);
            fans = new People();
            fans.setName(infoObject.getString("name"));
            // fans.setOpenid(infoObject.getString("openid"));
            fans.setNick(infoObject.getString("nick"));
            fans.setHead(infoObject.getString("head"));
            fans.setIsvip(infoObject.getInt("isvip") == 1 ? true : false);
            list.add(fans);
        }
        followers.setFans(list);
        return followers;
    }

    private List<People> getMyFollowersInfo(String startIndex)
    {
        int num = Integer.parseInt(startIndex);
        followers = null;
        try
        {
            String data = ApiClient.getMyFollowers(startIndex, appContext);
            if (data == null)
                return null;
            followers = parse(data);
            if (null != followers.getFans() && followers.getFans().size() > 0)
                list.addAll(followers.getFans());
            if (followers.getHasnext().equals("0"))// 0-表示还有数据，1-表示下页没有数据,
            {
                getMyFollowersInfo(num + 200 + "");
            }
        } catch (AppException e)
        {
            e.printStackTrace();
            return null;
        } catch (JSONException e)
        {
            retryCount++;
            if (retryCount < 3)
                getMyFollowersInfo(startIndex);
            return null;
        }
        sortList();
        return list;
    }

    private void insertPeoplesToDb()
    {
        MyFollowersDBService db = new MyFollowersDBService(appContext);
        db.delete();
        db.insert(list);
    }

    public List<People> initPeoples(String startIndex)
    {
        if (!appContext.isNetworkConnected())
            return null;
        getMyFollowersInfo(startIndex);
        sortList();
        insertPeoplesToDb();
        return list;
    }

    private List<People> sortList()
    {
        String s1 = null;
        int size = list.size();
        String indexString = "";
        Cn2Spell cn2Spell = new Cn2Spell();
        for (int i = 0; i < size; i++)
        {
            if (!TextUtils.isEmpty(list.get(i).getPyCode()))
                continue;
            s1 = cn2Spell.converterToSpell(list.get(i).getNick().toUpperCase());
            list.get(i).setPyCode(s1);
            for (char index = 'A'; index <= 'Z'; index++)
            {
                if (indexString.contains(String.valueOf(index)))
                    continue;
                if (s1.charAt(0) == index)
                {
                    indexString = indexString + String.valueOf(index);
                    People p = new People();
                    p.setNick(String.valueOf(index));
                    p.setPyCode(String.valueOf(index));
                    list.add(p);
                }
            }
        }
        return list;
    }
    /**
     * 拼音排序
     * 
     * @author V
     * 
     */
    // class ComparatorList implements Comparator<People>
    // {
    // public int compare(People p1, People p2)
    // {
    // String s1 = Cn2Spell.converterToFirstSpell(p1.getNick());
    //
    // String s2 = Cn2Spell.converterToFirstSpell(p2.getNick());
    // return Collator.getInstance(Locale.ENGLISH).compare(s1, s2);
    // }
    //
    // }
}
