package net.weibo.dao;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LocalHeadPicDBService extends BaseDbService
{

    public LocalHeadPicDBService(Context context)
    {
        super(context);
    }

    private static final String TABLE_NAME = "headpic";

    public void insert(String url, String openId)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String sql = "insert into " + TABLE_NAME + " (url,openId) values('" + url + "','" + openId + "')";
        db.execSQL(sql);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();

    }

    public ArrayList<String> query(String openId)
    {
        if (null == openId)
            return null;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> urls = new ArrayList<String>();
        Cursor cursor = db.rawQuery(" select  url from " + TABLE_NAME + " where openId=? order by id desc limit 5 ", new String[] { openId });
        if (cursor.moveToLast())
        {
            do
            {
                urls.add(cursor.getString(cursor.getColumnIndex("url")));
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        db.close();
        return urls;
    }

    public Cursor cursor()
    {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { "url" }, null, null, null, null, "id");
        db.close();
        return cursor;
    }

    public void deleteDb()
    {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    public int delete(String url)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int id = db.delete(TABLE_NAME, "url" + "=?", new String[] { url });
        db.close();
        return id;
    }

    public void delete()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(" delete from " + TABLE_NAME);
        db.close();
    }

}
