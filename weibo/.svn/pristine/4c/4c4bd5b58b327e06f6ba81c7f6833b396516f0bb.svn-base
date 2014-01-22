package net.weibo.app.lib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.weibo.app.AppContext;
import net.weibo.app.bean.Message;
import net.weibo.app.bean.News;
import net.weibo.constant.WeiboPatterns;
import android.app.Activity;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;

/**
 * build emotions and clickable string in other threads except UI thread,
 * improve listview scroll performance
 */
public class ListViewTool
{

    private ListViewTool()
    {
    }

    /**
     * 添加高亮着色链接
     * 
     * @param news
     */
    public static void addJustHighLightLinks(Message msg, Activity activity)
    {
        msg.getSourceString();
        String text = stringFilter(msg.getText());
        text = ToDBC(text);
        msg.setListViewSpannableString(convertNormalStringToSpannableString(text, activity));
        if (msg instanceof News)
        {
            if (((News) msg).getSource() != null)
            {
                ((News) msg).getSource().getSourceString();
                ((News) msg).getSource().setListViewSpannableString(buildOriWeiboSpannalString(((News) msg).getSource(), activity));

            } else
            {
            }
        }
    }

    /**
     * 转发人姓名处理
     * 
     * @param news
     * @return
     */
    private static SpannableString buildOriWeiboSpannalString(Message msg, Activity activity)
    {
        String name = msg.getNick();
        String text = stringFilter(msg.getText());
        text = ToDBC(text);
        SpannableString value;

        if (!TextUtils.isEmpty(name))
        {
            value = convertNormalStringToSpannableString("@" + name + "：" + text, activity);
        } else
        {
            value = convertNormalStringToSpannableString(text, activity);
        }
        return value;
    }

    private static void addJustHighLightLinksOnlyReplyComment(Message msg, Activity activity)
    {
        String name = msg.getNick();
        SpannableString value;

        if (!TextUtils.isEmpty(name))
        {
            value = ListViewTool.convertNormalStringToSpannableString("@" + name + "：" + msg.getText(), activity);
        } else
        {
            value = ListViewTool.convertNormalStringToSpannableString(msg.getText(), activity);
        }

        msg.setListViewSpannableString(value);
    }

    public static SpannableString convertNormalStringToSpannableString(String txt, Activity activity)
    {
        // hack to fix android imagespan bug,see
        // http://stackoverflow.com/questions/3253148/imagespan-is-cut-off-incorrectly-aligned
        // if string only contains emotion tags,add a empty char to the end
        String hackTxt;
        if (txt.startsWith("[") && txt.endsWith("]"))
        {
            hackTxt = txt + " ";
        } else
        {
            hackTxt = txt;
        }
        SpannableString value = SpannableString.valueOf(hackTxt);

        Linkify.addLinks(value, WeiboPatterns.MENTION_URL, WeiboPatterns.MENTION_SCHEME);
        Linkify.addLinks(value, WeiboPatterns.WEB_URL, WeiboPatterns.WEB_SCHEME);
        Linkify.addLinks(value, WeiboPatterns.TOPIC_URL, WeiboPatterns.TOPIC_SCHEME);

        URLSpan[] urlSpans = value.getSpans(0, value.length(), URLSpan.class);
        MyURLSpan weiboSpan = null;
        for (URLSpan urlSpan : urlSpans)
        {
            weiboSpan = new MyURLSpan(urlSpan.getURL(), activity);
            int start = value.getSpanStart(urlSpan);
            int end = value.getSpanEnd(urlSpan);
            value.removeSpan(urlSpan);
            value.setSpan(weiboSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        ListViewTool.addEmotions(value, activity);
        return value;
    }

    public static void addEmotions(SpannableString value, Activity activity)
    {
        Matcher localMatcher = WeiboPatterns.EMOTION_URL.matcher(value);
        while (localMatcher.find())
        {
            String str2 = localMatcher.group(0);
            int k = localMatcher.start();
            int m = localMatcher.end();
            if (m - k < 8)
            {
                Bitmap bitmap = AppContext.getInstance().getEmotionsPics().get(str2);
                if (bitmap != null)
                {
                    ImageSpan localImageSpan = new ImageSpan(activity, bitmap, DynamicDrawableSpan.ALIGN_BASELINE);
                    value.setSpan(localImageSpan, k, m, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
    }

    /***
     * 半角转换为全角
     * 
     * @param input
     * @return
     */
    private static String ToDBC(String input)
    {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++)
        {
            if (c[i] == 12288)
            {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * * 去除特殊字符或将所有中文标号替换为英文标号
     * 
     * @param str
     * @return
     */
    private static String stringFilter(String str)
    {
        str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

}
