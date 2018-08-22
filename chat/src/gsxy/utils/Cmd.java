package gsxy.utils;

public class Cmd {
    public static final int CMD_LOGIN = 1000;      //登录（在线）
    public static final int CMD_LOGOUT = 1001;      //下线（离线）
    public static final int CMD_BUSY = 1002;        //忙碌
    public static final int CMD_LEAVE = 1003;       //离开

    public static final int CMD_CHAT = 1004;        //聊天
    public static final int CMD_SHAKE = 1005;       //抖动
    public static final int CMD_ADDFRIEND = 1006;       //添加好友
    public static final int CMD_DELFRIEND = 1007;       //删除好友
    public static final int CMD_SENDFILE = 1008;        //发送文件
    public static final int CMD_ONLINE = 1009;      //上线通知
    public static final int CMD_OFFLINE = 1010;     //下线通知
    public static final int CMD_AGREEFRIEND = 1011;        //同意添加好友
    public static final int CMD_REJECTFRIEND = 1012;        //拒绝添加好友

    public static final String F_FRIEND = "好友";     //好友
    public static final String F_FAMILY = "家人";     //家人
    public static final String F_CLASSMATE = "同学";      //同学
    public static final String F_COLLEAGUE = "同事";      //同事 还有一个同事没有添加进去
    public static final String F_BLACKLIST = "黑名单";       //黑名单

    public static final int F_OFFLINE = 0;      //离线
    public static final int F_ONLINE = 1;       //在线
    public static final int F_BUSY = 2;     //忙碌
    public static final int F_LEAVE = 3;        //离开
}
