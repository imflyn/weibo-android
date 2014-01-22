package net.weibo.app;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.weibo.api.ApiClient;
import net.weibo.api.InfoImpl;
import net.weibo.app.authorize.AuthorizeContrast;
import net.weibo.app.authorize.AuthorizeWebView;
import net.weibo.app.service.InfoService;
import net.weibo.app.ui.MainActivity;
import net.weibo.common.StringUtils;
import net.weibo.common.UIUtils;
import net.weibo.constant.Task;
import net.weibo.constant.Tids;
import net.weibo.http.AsyncHttpResponseHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.oauthv2.OAuthV2Client;

/**
 * 应用程序启动类：显示欢迎界面并跳转到主界面
 * 
 */
public class AppStart extends Activity
{
    private final String TAG            = "WelcomeActivity";
    private OAuthV2      oAuth;
    private AppContext   appContext;
    private Dialog       dialog;
    private int          netFlag;
    private Thread       getAccessThread;
    private final int    redirectToMain = 0;

    private Handler      handler        = new Handler()
                                        {
                                            @Override
                                            public void handleMessage(Message msg)
                                            {
                                                getAccessThread = null;
                                                switch (msg.what)
                                                {
                                                    case AuthorizeContrast.AUTHORIZE_SUCCESS:
                                                        if (appContext.isNetworkConnected())
                                                            getMyInfo();
                                                        else
                                                            redirectToMain();
                                                    case redirectToMain:
                                                        redirectToMain();
                                                        break;
                                                    case AuthorizeContrast.AUTHORIZE_FAIL:
                                                        redirectToAuthorize();
                                                        break;
                                                    case AuthorizeContrast.AUTHORIZE_REFRESH:
                                                        accessRefresh();
                                                        break;
                                                    default:
                                                        break;
                                                }
                                            }
                                        };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.activity_welcome, null);
        setContentView(view);
        AppManager.getAppManager().addActivity(this);

        // 渐变展示启动屏
        AlphaAnimation alpha = new AlphaAnimation(1, 2);
        alpha.setDuration(3000);
        view.startAnimation(alpha);
        alpha.setAnimationListener(new AnimationListener()
        {

            @Override
            public void onAnimationStart(Animation animation)
            {
                getAccessInfo();
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        // 判断是否弹出过设置网络Dialog
        if (netFlag == 1)
        {
            if (appContext.isNetworkConnected())
            {
                closeSetNetworkUI();
                handler.obtainMessage(AuthorizeContrast.AUTHORIZE_FAIL).sendToTarget();
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 2)
        {
            if (resultCode == AuthorizeWebView.RESULT_CODE)
            {
                oAuth = (OAuthV2) data.getExtras().getSerializable("oauth");
                switch (oAuth.getStatus())
                {
                    case 0:
                        UIUtils.ToastMessage(getApplicationContext(), "授权成功");
                        findViewById(R.id.pb_load).setVisibility(View.VISIBLE);
                        AppConfig.getAppConfig(appContext).setAccessInfo(oAuth);
                        AppConfig.getAppConfig(appContext).clearDbData();
                        if (AppContext.getInstance().isNetworkConnected())
                        {
                            findViewById(R.id.main).setBackgroundColor(Color.WHITE);
                            getMyInfo();
                        } else
                            redirectToMain();
                        break;
                    case 1:
                        UIUtils.ToastMessage(getApplicationContext(), "请求发送失败");
                        AppManager.getAppManager().AppExit(getApplicationContext());
                        break;
                    case 2:
                        UIUtils.ToastMessage(getApplicationContext(), "获取验证码失败");
                        AppManager.getAppManager().AppExit(getApplicationContext());
                        break;
                    case 3:
                        UIUtils.ToastMessage(getApplicationContext(), "授权失败");
                        AppManager.getAppManager().AppExit(getApplicationContext());
                        break;
                    default:
                        break;
                }
            }
        } else if (requestCode == 1)
        {
            AppManager.getAppManager().AppExit(getApplicationContext());
        }
    }

    /**
     * 向service添加初始化任务
     */
    private void addTask(int id, Class name)
    {
        Intent intent = new Intent(AppStart.this, name);
        Task task = new Task();
        task.setId(id);
        intent.putExtra("task", task);
        startService(intent);
    }

    private void redirectToMain()
    {
        addTask(Tids.T_LOAD_FOLLOWERS, InfoService.class);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
        AppManager.getAppManager().finishActivity(this);
        finish();
    }

    private void redirectToAuthorize()
    {
        // 网络连接判断
        if (!appContext.isNetworkConnected())
        {
            showSetNetworkUI(this);
            return;
        }
        oAuth = new OAuthV2(AuthorizeContrast.redirectUri);
        oAuth.setClientId(AuthorizeContrast.clientId);
        oAuth.setClientSecret(AuthorizeContrast.clientSecret);
        // 关闭OAuthV2Client中的默认开启的QHttpClient。
        OAuthV2Client.getQHttpClient().shutdownConnection();
        Intent intent = new Intent(AppStart.this, AuthorizeWebView.class);// 创建Intent，使用WebView让用户授权
        intent.putExtra("oauth", oAuth);
        startActivityForResult(intent, 2);
        overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);

    }

    // 获取登录信息
    private void getAccessInfo()
    {
        if (null == getAccessThread)
        {
            getAccessThread = new Thread()
            {
                @Override
                public void run()
                {
                    appContext = (AppContext) (AppStart.this).getApplication();
                    switch (AppConfig.getAppConfig(appContext).getAccessInfo())
                    {
                        case AuthorizeContrast.AUTHORIZE_FAIL:
                            handler.obtainMessage(AuthorizeContrast.AUTHORIZE_FAIL).sendToTarget();
                            break;
                        case AuthorizeContrast.AUTHORIZE_REFRESH:
                            handler.obtainMessage(AuthorizeContrast.AUTHORIZE_REFRESH).sendToTarget();
                            break;
                        case AuthorizeContrast.AUTHORIZE_SUCCESS:
                            handler.obtainMessage(AuthorizeContrast.AUTHORIZE_SUCCESS).sendToTarget();
                            break;
                    }
                };
            };
            getAccessThread.start();
        }
    }

    // access_token时间到期失效,重新刷新
    private void accessRefresh()
    {
        new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    ApiClient.authorize_refresh(appContext, new AsyncHttpResponseHandler()
                    {
                        @Override
                        public void onSuccess(String content)
                        {
                            try
                            {
                                String[] result = content.split("&");

                                String expires_in = result[1];
                                String refresh_token = result[2];
                                Log.i(TAG, "expires_in:" + expires_in);
                                Log.i(TAG, "refresh_token:" + refresh_token);

                                AppConfig.getAppConfig(appContext).set("refresh_token", refresh_token);
                                AppConfig.getAppConfig(appContext).setExpiresIn(StringUtils.toLong(expires_in) + System.currentTimeMillis());

                                handler.obtainMessage(AuthorizeContrast.AUTHORIZE_SUCCESS).sendToTarget();
                            } catch (Exception e)
                            {
                                handler.obtainMessage(AuthorizeContrast.AUTHORIZE_FAIL).sendToTarget();
                            }
                        }

                        @Override
                        public void onFailure(Throwable error)
                        {
                            Log.i(TAG, error.toString());
                            handler.obtainMessage(AuthorizeContrast.AUTHORIZE_FAIL).sendToTarget();
                        }

                        @Override
                        public void onFinish()
                        {
                            super.onFinish();
                        }
                    });
                } catch (AppException e)
                {
                    e.printStackTrace();
                }
            };
        }.start();

    }

    private void getMyInfo()
    {
        final ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable()
        {
            @Override
            public void run()
            {
                final InfoImpl myInfoImpl = new InfoImpl(appContext);
                myInfoImpl.getMyInfoFromNet(new AsyncHttpResponseHandler()
                {

                    @Override
                    public void onSuccess(String content)
                    {
                        try
                        {
                            myInfoImpl.parse(content);

                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        } finally
                        {
                            handler.obtainMessage(redirectToMain).sendToTarget();
                            service.shutdown();
                        }
                    };

                });

            }
        });

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        handler = null;
        oAuth = null;
        appContext = null;
        dialog = null;
        getAccessThread = null;
    }

    // 弹出网络设置提示
    private void showSetNetworkUI(final Context context)
    {
        netFlag = 1;
        // 提示对话框
        AlertDialog.Builder builder = new Builder(context);
        dialog = builder.create();
        builder.setTitle("网络设置提示").setMessage("网络连接不可用,是否进行设置?").setPositiveButton("设置", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = null;
                // 判断手机系统的版本 即API大于10 就是3.0或以上版本
                if (android.os.Build.VERSION.SDK_INT > 10)
                {
                    intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                } else
                {
                    intent = new Intent();
                    ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                context.startActivity(intent);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                dialog.dismiss();
                AppManager.getAppManager().finishAllActivity();
            }
        }).show();
    }

    private void closeSetNetworkUI()
    {
        dialog.dismiss();
        dialog = null;
    }

}
