package net.weibo.app.bean;

import java.io.IOException;

import net.weibo.app.AppException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * http返回结果类
 * 
 * @author V
 * 
 */

public class Result
{
    private int        errcode;
    private String     msg;
    private int        ret;
    private int        seqid;

    private JSONObject data;

    public boolean OK()
    {
        return ret == 0;
    }

    public final int getErrcode()
    {
        return errcode;
    }

    public final void setErrcode(int errcode)
    {
        this.errcode = errcode;
    }

    public final String getMsg()
    {
        return msg;
    }

    public final void setMsg(String msg)
    {
        this.msg = msg;
    }

    public final int getRet()
    {
        return ret;
    }

    public final void setRet(int ret)
    {
        this.ret = ret;
    }

    public final int getSeqid()
    {
        return seqid;
    }

    public final void setSeqid(int seqid)
    {
        this.seqid = seqid;
    }

    public final JSONObject getData()
    {
        return data;
    }

    public final void setData(JSONObject data)
    {
        this.data = data;
    }

    /**
     * 解析http返回数据
     * 
     * @throws JSONException
     */
    public static Result parse(String data) throws IOException, AppException, JSONException
    {
        JSONObject jsonObject = new JSONObject(data);
        Result result = new Result();
        result.setData(jsonObject.getJSONObject("data"));
        result.setMsg(jsonObject.getString("msg"));
        result.setRet(jsonObject.getInt("ret"));
        result.setSeqid(jsonObject.getInt("seqid"));
        result.setErrcode(jsonObject.getInt("errcode"));
        return result;
    }

}
