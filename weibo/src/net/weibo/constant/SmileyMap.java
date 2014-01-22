package net.weibo.constant;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class SmileyMap
{
    private static SmileyMap        instance = new SmileyMap();
    private HashMap<String, String> map      = new LinkedHashMap<String, String>();

    private SmileyMap()
    {
        map.put("/微笑", "h001.png");
        map.put("/撇嘴", "h002.png");
        map.put("/色", "h003.png");
        map.put("/发呆", "h004.png");
        map.put("/得意", "h005.png");
        map.put("/流泪", "h006.png");
        map.put("/害羞", "h007.png");
        map.put("/闭嘴", "h008.png");
        map.put("/睡", "h009.png");
        map.put("/大哭", "h010.png");
        map.put("/尴尬", "h011.png");
        map.put("/发怒", "h012.png");
        map.put("/调皮", "h013.png");
        map.put("/呲牙", "h014.png");
        map.put("/惊讶", "h015.png");
        map.put("/难过", "h016.png");
        map.put("/酷", "h017.png");
        map.put("/非典", "h018.png");
        map.put("/抓狂", "h019.png");
        map.put("/吐", "h020.png");
        map.put("/偷笑", "h021.png");
        map.put("/可爱", "h022.png");
        map.put("/白眼", "h023.png");
        map.put("/傲慢", "h024.png");
        map.put("/饥饿", "h025.png");
        map.put("/困", "h026.png");
        map.put("/惊恐", "h027.png");
        map.put("/流汗", "h028.png");
        map.put("/憨笑", "h029.png");
        map.put("/大兵", "h030.png");
        map.put("/奋斗", "h031.png");
        map.put("/咒骂", "h032.png");
        map.put("/疑问", "h033.png");
        map.put("/嘘", "h034.png");
        map.put("/晕", "h035.png");
        map.put("/折磨", "h036.png");
        map.put("/衰", "h037.png");
        map.put("/骷髅", "h038.png");
        map.put("/敲打", "h039.png");
        map.put("/再见", "h040.png");
        map.put("/擦汗", "h041.png");
        map.put("/抠鼻", "h042.png");
        map.put("/鼓掌", "h043.png");
        map.put("/糗大了", "h044.png");
        map.put("/坏笑", "h045.png");
        map.put("/左哼哼", "h046.png");
        map.put("/右哼哼", "h047.png");
        map.put("/哈欠", "h048.png");
        map.put("/鄙视", "h049.png");
        map.put("/委屈", "h050.png");
        map.put("/快哭了", "h051.png");
        map.put("/阴险", "h052.png");
        map.put("/亲亲", "h053.png");
        map.put("/吓", "h054.png");
        map.put("/可怜", "h055.png");
        map.put("/菜刀", "h056.png");
        map.put("/西瓜", "h057.png");
        map.put("/啤酒", "h058.png");
        map.put("/篮球", "h059.png");
        map.put("/乒乓", "h060.png");
        map.put("/咖啡", "h061.png");
        map.put("/饭", "h062.png");
        map.put("/猪头", "h063.png");
        map.put("/玫瑰", "h064.png");
        map.put("/凋谢", "h065.png");
        map.put("/男", "h066.png");
        map.put("/爱心", "h067.png");
        map.put("/心碎", "h068.png");
        map.put("/蛋糕", "h069.png");
        map.put("/闪电", "h070.png");
        map.put("/炸弹", "h071.png");
        map.put("/刀", "h072.png");
        map.put("/足球", "h073.png");
        map.put("/瓢虫", "h074.png");
        map.put("/便便", "h075.png");
        map.put("/月亮", "h076.png");
        map.put("/太阳", "h077.png");
        map.put("/礼物", "h078.png");
        map.put("/拥抱", "h079.png");
        map.put("/强", "h080.png");
        map.put("/弱", "h081.png");
        map.put("/握手", "h082.png");
        map.put("/胜利", "h083.png");
        map.put("/抱拳", "h084.png");
        map.put("/勾引", "h085.png");
        map.put("/拳头", "h086.png");
        map.put("/差劲", "h087.png");
        map.put("/爱你", "h088.png");
        map.put("/NO", "h089.png");
        map.put("/OK", "h090.png");
        map.put("/爱情", "h091.png");
        map.put("/飞吻", "h092.png");
        map.put("/跳跳", "h093.png");
        map.put("/发抖", "h094.png");
        map.put("/怄火", "h095.png");
        map.put("/转圈", "h096.png");
        map.put("/磕头", "h097.png");
        map.put("/回头", "h098.png");
        map.put("/跳绳", "h099.png");
        map.put("/挥手", "h0100.png");
        map.put("/激动", "h0101.png");
        map.put("/街舞", "h0102.png");
        map.put("/献吻", "h0103.png");
        map.put("/左太极", "h0104.png");
        map.put("/右太极", "h0105.png");

    }

    public static SmileyMap getInstance()
    {
        return instance;
    }

    public HashMap<String, String> get()
    {
        return map;
    }
}
