package net.weibo.app.service;

import java.util.ArrayList;
import java.util.HashMap;

import net.weibo.app.AppContext;
import net.weibo.constant.Task;
import net.weibo.constant.Tids;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

/**
 * 服务类
 * */
public class MainService extends Service implements Runnable
{
    /** 保存当前Activity对象 */
    private static final String              TAG          = "InfoService";
    private static ArrayList<Task>           infotaskList = new ArrayList<Task>();
    private static HashMap<String, Activity> actList      = new HashMap<String, Activity>();
    private static ArrayList<Activity>       actArrayList = new ArrayList<Activity>();
    private AppContext                       appContext   = null;

    public MainService()
    {
        appContext = (AppContext) getApplication();
    }

    /** 请求成功后更新Ui数据 */
    private Handler handler = new Handler()
                            {
                                @Override
                                public void handleMessage(Message msg)
                                {
                                    super.handleMessage(msg);
                                    switch (msg.what)
                                    {
                                        case Tids.T_SEARCH_HEADPIC:
                                            break;
                                        default:
                                            break;
                                    }

                                };

                            };

    /** 请求数据 */
    private void doTask(Task task)
    {
        switch (task.getId())
        {
            case Tids.T_SEARCH_MYINFO:
                break;
            default:
                break;
        }
        infotaskList.remove(task); // 执行任务结束，移出任务
    };

    /** 执行任务 */
    private void exeTask(Task task)
    {
        infotaskList.add(task);
        if (infotaskList.size() > 0)
        {
            doTask(task);
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.i(TAG, "stop");
        infotaskList = null;
        handler = null;
        appContext = null;
        actList = null;
        actArrayList = null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        super.onStart(intent, startId);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    /** 删除Activity对象 */
    public static void destoryActivity(String name)
    {
        actArrayList.remove(getActivityByName(name));
        actList.remove(name);
    }

    /** 添加Activity对象 */
    public static void addActivity(String className, Activity act)
    {
        actList.put(className, act);
        actArrayList.remove(act); // 先移除再添加，确保在最后
        actArrayList.add(act);
    }

    public static Activity getCurActivity()
    {
        int index = actArrayList.size();
        Activity activity = actArrayList.get(index - 1);
        return activity;
    }

    /** 获取Activity对象 */
    public static Activity getActivityByName(String name)
    {
        if (actList.get(name) != null)
        {
            return actList.get(name);
        }
        return null;
    }

    /** 获取act集合 */
    public static HashMap<String, Activity> getActList()
    {
        return actList;
    }

    @Override
    public void run()
    {

    }
}
