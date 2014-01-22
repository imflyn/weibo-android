package net.weibo.dao;

import java.util.ArrayList;

import net.weibo.app.bean.People;
import net.weibo.common.Cn2Spell;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyFollowersDBService extends BaseDbService
{
    public MyFollowersDBService(Context context)
    {
        super(context);
    }

    private static final String TABLE_NAME = "myFollowers";

    public void insert(ArrayList<People> peoples)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        int size = peoples.size();
        String nick = "";
        String name = "";
        String head = "";
        String isVip = "";
        String pyCode = "";
        String sql = "";
        try
        {
            db.beginTransaction();
            for (int i = 0; i < size; i++)
            {
                nick = peoples.get(i).getNick();
                if (null == peoples.get(i).getIsvip())
                {
                    isVip = "0";
                } else if (peoples.get(i).getIsvip())
                {
                    isVip = "1";
                } else
                {
                    isVip = "0";
                }
                head = peoples.get(i).getHead();
                nick = peoples.get(i).getNick();
                name = peoples.get(i).getName();
                pyCode = peoples.get(i).getPyCode();
                sql = "insert into " + TABLE_NAME + " (nick,isVip,headUrl,pyCode,name) values(?,?,?,?,?)";
                db.execSQL(sql, new Object[] { nick, isVip, head, pyCode, name });
            }
            db.setTransactionSuccessful();
        } catch (Exception e)
        {

        } finally
        {
            db.endTransaction();
            db.close();
        }

    }

    public void insertPeople(People people)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        String nick = "";
        String name = "";
        String head = "";
        String isVip = "";
        String pyCode = "";
        String sql = "";
        db.beginTransaction();
        nick = people.getNick();
        if (null == people.getIsvip())
        {
            isVip = "0";
        } else if (people.getIsvip())
        {
            isVip = "1";
        } else
        {
            isVip = "0";
        }
        head = people.getHead();
        nick = people.getNick();
        name = people.getName();
        pyCode = people.getPyCode();
        Cn2Spell cn2Spell = new Cn2Spell();
        pyCode = String.valueOf(cn2Spell.converterToFirstSpell(nick).charAt(0));
        sql = " select id from " + TABLE_NAME + " where nick=? ";
        Cursor cursor = db.rawQuery(sql, new String[] { pyCode });
        if (!(cursor.getCount() > 0))
        {
            sql = "insert into " + TABLE_NAME + " (nick,isVip,headUrl,pyCode,name) values(?,?,?,?,?)";
            db.execSQL(sql, new Object[] { pyCode, "", null, pyCode, null });
        }
        cursor.close();
        sql = "insert into " + TABLE_NAME + " (nick,isVip,headUrl,pyCode,name) values(?,?,?,?,?)";
        db.execSQL(sql, new Object[] { nick, isVip, head, pyCode, name });
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();

    }

    public ArrayList<People> query()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<People> list = new ArrayList<People>();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("  select nick,isVip,headUrl,pyCode,name from " + TABLE_NAME + " order by  pyCode", null);
        while (cursor.moveToNext())
        {
            People people = new People();
            people.setHead(cursor.getString(cursor.getColumnIndex("headUrl")));
            people.setNick(cursor.getString(cursor.getColumnIndex("nick")));
            people.setName(cursor.getString(cursor.getColumnIndex("name")));
            people.setPyCode(cursor.getString(cursor.getColumnIndex("pyCode")));
            people.setIsvip(cursor.getString(cursor.getColumnIndex("isVip")).equals("1"));
            list.add(people);
        }
        db.setTransactionSuccessful();
        cursor.close();
        db.endTransaction();
        db.close();
        return list;
    }

    public ArrayList<People> search(String text)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        ArrayList<People> list = new ArrayList<People>();
        Cursor cursor = db.rawQuery("  select nick,isVip,headUrl,pyCode,name from " + TABLE_NAME + " where ( nick like '%" + text + "%' or pyCode like '%" + text
                + "%') and ( name<>'' or name is not null ) order by  pyCode", null);
        while (cursor.moveToNext())
        {
            People people = new People();
            people.setHead(cursor.getString(cursor.getColumnIndex("headUrl")));
            people.setNick(cursor.getString(cursor.getColumnIndex("nick")));
            people.setName(cursor.getString(cursor.getColumnIndex("name")));
            people.setPyCode(cursor.getString(cursor.getColumnIndex("pyCode")));
            people.setIsvip(cursor.getString(cursor.getColumnIndex("isVip")).equals("1"));
            list.add(people);
        }
        db.setTransactionSuccessful();
        cursor.close();
        db.endTransaction();
        db.close();
        return list;
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

    public void deletePeole(String name)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.execSQL(" delete from " + TABLE_NAME + " where name=?", new String[] { name });
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void delete()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.execSQL(" delete from " + TABLE_NAME);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

}
