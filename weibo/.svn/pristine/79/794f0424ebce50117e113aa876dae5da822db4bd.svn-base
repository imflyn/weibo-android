package net.weibo.api;

import java.util.ArrayList;

import net.weibo.app.AppContext;
import net.weibo.app.AppException;
import net.weibo.app.bean.Album;
import net.weibo.app.bean.AlbumList;
import net.weibo.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AlbumImpl
{
    /**
     * 获取微博相册 pageflag分页标识（0：第一页，1：向下翻页，2：向上翻页） pagetime
     * 本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间）
     */
    public void getAlbum(String pageflag, String pagetime, String name, AppContext appContext, AsyncHttpResponseHandler handler)
    {

        try
        {
            ApiClient.getAlbum(pageflag, pagetime, name, appContext, handler);
        } catch (AppException e)
        {
            e.printStackTrace();
        }

    }

    public AlbumList parse(String data) throws JSONException
    {
        AlbumList albumList = new AlbumList();
        Album album = null;
        ArrayList<Album> list = new ArrayList<Album>();
        JSONObject jsonObject = new JSONObject(data);
        JSONObject dataObject = jsonObject.getJSONObject("data");
        JSONArray infoArray = dataObject.getJSONArray("info");

        albumList.setHasNext(dataObject.getInt("hasnext") == 0 ? false : true);

        JSONObject infoObject = null;
        int size = infoArray.length();
        for (int i = 0; i < size; i++)
        {
            infoObject = infoArray.getJSONObject(i);
            album = new Album();

            album.setWbId(infoObject.getString("id"));
            album.setImage(infoObject.getString("image"));
            album.setPubtime(infoObject.getString("pubtime"));

            list.add(album);
            if (i == (size - 1))

                albumList.setPagetime(album.getPubtime());

        }
        albumList.setAlbums(list);
        return albumList;
    }

}
