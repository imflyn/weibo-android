package net.weibo.constant;

/**
 * 任务类id常量
 */
public class Tids
{

    public static final int T_SUCCESS        = 0x100; // 成功
    public static final int T_FAIL           = 0x200; // 失败
    public static final int T_CHECK_VERSION  = 0;    // 检测版本
    public static final int T_SEARCH_MYINFO  = 1;    // 查询个人信息
    public static final int T_INIT_HEADPIC   = 2;    // 初始加载个人头像
    public static final int T_SEARCH_HEADPIC = 3;    // 加载个人头像
    public static final int T_INIT_MYINFO    = 4;    // 初始查询个人信息
    public static final int T_UPDATE_MYINFO  = 5;    // 更新保存个人信息
    public static final int T_UPDATE_HEADPIC = 6;    // 更新头像
    public static final int T_DELETE_HEADPIC = 7;    // 删除头像
    public static final int T_LOAD_LISTENERS = 8;    // 加载用户粉丝列表
    public static final int T_LOAD_FOLLOWERS = 9;    // 加载偶像列表
    public static final int T_LOAD_ALBUM     = 10;   // 加载相册
    public static final int T_LOAD_NEWS      = 11;   // 加载个人微博消息

}
