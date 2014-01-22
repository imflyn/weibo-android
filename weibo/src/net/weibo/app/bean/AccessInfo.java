package net.weibo.app.bean;

public class AccessInfo
{
    private static AccessInfo accessInfo;
    private String            openid;
    private String            openkey;
    private String            access_token;
    private String            accessSecret;
    private long              expires_in;
    private String            refresh_token;

    private void AccessInfo()
    {

    }

    public synchronized static AccessInfo getAccessInstance()
    {
        if (null == accessInfo)
        {
            accessInfo = new AccessInfo();
        }
        return accessInfo;
    }

    public final String getOpenid()
    {
        return openid;
    }

    public final void setOpenid(String openid)
    {
        this.openid = openid;
    }

    public final String getOpenkey()
    {
        return openkey;
    }

    public final void setOpenkey(String openkey)
    {
        this.openkey = openkey;
    }

    public final String getAccess_token()
    {
        return access_token;
    }

    public final void setAccess_token(String access_token)
    {
        this.access_token = access_token;
    }

    public final String getAccessSecret()
    {
        return accessSecret;
    }

    public final void setAccessSecret(String accessSecret)
    {
        this.accessSecret = accessSecret;
    }

    public final long getExpires_in()
    {
        return expires_in;
    }

    public final void setExpires_in(long expires_in)
    {
        this.expires_in = expires_in;
    }

    public final String getRefresh_token()
    {
        return refresh_token;
    }

    public final void setRefresh_token(String refresh_token)
    {
        this.refresh_token = refresh_token;
    }

}
