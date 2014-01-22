package net.weibo.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDbService extends SQLiteOpenHelper
{
    private static final String DATEBASE_NAME    = "weibo.db ";
    private static final int    DATEBASE_VERSION = 1;

    public BaseDbService(Context context)
    {
        super(context, DATEBASE_NAME, null, DATEBASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.beginTransaction();
        String sql = ("  CREATE TABLE IF NOT EXISTS  " + "  myFollowers                 (" + "  id INTEGER PRIMARY KEY ,   " + "  nick varchar(25) null ,  " + "  isVip varchar(1) null ,  "
                + "  headUrl varchar(50) null ,  " + "  pyCode  varchar(20) null ,  " + "  name varchar(50)  null  )");
        db.execSQL(sql);
        sql = (" CREATE TABLE IF NOT EXISTS  " + " headpic                 (" + "  id INTEGER PRIMARY KEY ,   " + "  openId varchar(50) null ,  " + "  url varchar(50)  null  )");
        db.execSQL(sql);
        db.setTransactionSuccessful();
        db.endTransaction();
        // db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

}
