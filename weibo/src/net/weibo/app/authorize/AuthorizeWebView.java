package net.weibo.app.authorize;

import net.weibo.app.AppManager;
import net.weibo.app.bean.URLs;
import net.weibo.common.UIUtils;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tencent.weibo.oauthv2.OAuthV2;

/**
 * 使用Webview显示OAuth Version 2.a ImplicitGrant方式授权的页面<br>
 * (移动终端不建议使用Authorize code grant方式授权）<br>
 * <p>
 * 本类使用方法：
 * </p>
 * <li>需要调用本类的地方请添加如下代码
 * 
 * <pre>
 * // 请将OAuthV2Activity改为所在类的类名
 * Intent intent = new Intent(OAuthV2Activity.this, OAuthV2AuthorizeWebView.class);
 * intent.putExtra(&quot;oauth&quot;, oAuth); // oAuth为OAuthV2类的实例，存放授权相关信息
 * startActivityForResult(intent, myRrequestCode); // 请设置合适的requsetCode
 * </pre> <li>重写接收回调信息的方法
 * 
 * <pre>
 * if (requestCode==myRrequestCode) {  //对应之前设置的的myRequsetCode
 *     if (resultCode==OAuthV2AuthorizeWebView.RESULT_CODE) {
 *         //取得返回的OAuthV2类实例oAuth
 *         oAuth=(OAuthV2) data.getExtras().getSerializable("oauth");
 *     }
 * }
 * 
 * <pre>
 * @see android.app.Activity#onActivityResult(int requestCode, int resultCode,  Intent data)
 */
public class AuthorizeWebView extends Activity
{
    public final static int     RESULT_CODE = 2;
    public final static int     BACK_CODE   = 1;
    private static final String TAG         = "OAuthV2AuthorizeWebView";
    private OAuthV2             oAuth;
    private long                firstTime   = 0;
    private WebView             webView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(this);
        webView = new WebView(this);
        linearLayout.addView(webView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setContentView(linearLayout);
        Intent intent = this.getIntent();
        oAuth = (OAuthV2) intent.getExtras().getSerializable("oauth");
        String urlStr = OAuthV2Client.generateImplicitGrantUrl(oAuth);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webView.requestFocus();
        System.out.println("urlStr:" + URLs.turnToHttp(urlStr));
        webView.loadUrl(URLs.turnToHttp(urlStr));
        UIUtils.ToastMessage(getApplicationContext(), "正在加载授权网页");
        WebViewClient client = new WebViewClient()
        {
            /**
             * 回调方法，当页面开始加载时执行
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {

                if (url.indexOf("access_token=") != -1)
                {
                    int start = url.indexOf("access_token=");
                    String responseData = url.substring(start);
                    OAuthV2Client.parseAccessTokenAndOpenId(responseData, oAuth);
                    Intent intent = new Intent();
                    intent.putExtra("oauth", oAuth);
                    switch (oAuth.getStatus())
                    {
                        case 0:
                            UIUtils.ToastMessage(getApplicationContext(), "授权成功");
                            setResult(RESULT_CODE, intent);
                            view.destroyDrawingCache();
                            view.destroy();
                            finish();
                            break;
                        case 1:
                            UIUtils.ToastMessage(getApplicationContext(), "请求发送失败");
                            break;
                        case 2:
                            UIUtils.ToastMessage(getApplicationContext(), "获取验证码失败");
                            break;
                        case 3:
                            UIUtils.ToastMessage(getApplicationContext(), "授权失败");
                            break;
                        default:
                            break;
                    }

                }
                super.onPageStarted(view, url, favicon);
            }

            /*
             * Android2.2及以上版本才能使用该方法
             * 目前https://open.t.qq.com中存在http资源会引起sslerror，待网站修正后可去掉该方法
             */
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
            {
                if ((null != view.getUrl()) && (view.getUrl().startsWith("https://open.t.qq.com")))
                {
                    handler.proceed();// 接受证书
                } else
                {

                    handler.cancel(); // 默认的处理方式，WebView变成空白页
                }
                // handleMessage(Message msg); 其他处理
            }
        };
        webView.setWebViewClient(client);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (webView != null)
        {
            webView.removeAllViews();
            webView.destroy();
            // 清除cookie
            CookieSyncManager.createInstance(AuthorizeWebView.this);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
        }

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 800)
            {// 如果两次按键时间间隔大于800毫秒，则不退出
                Toast.makeText(AuthorizeWebView.this, "再按一次退出程序！", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;// 更新firstTime
                return true;
            } else
            {

                AppManager.getAppManager().AppExit(getApplicationContext());
                this.finish();
            }
        }
        return super.onKeyUp(keyCode, event);
    }

}
