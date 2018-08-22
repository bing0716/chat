package gsxy.bean;

public class Message {
    private int id;
    private int selfAccount;        //自己的账号
    private int friendAccount;      //朋友的账号
    private int cmd;        //消息类型（命令字）
    private String msgcontent;      //消息内容

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSelfAccount() {
        return selfAccount;
    }

    public void setSelfAccount(int selfAccount) {
        this.selfAccount = selfAccount;
    }

    public int getFriendAccount() {
        return friendAccount;
    }

    public void setFriendAccount(int friendAccount) {
        this.friendAccount = friendAccount;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getMsgcontent() {
        return msgcontent;
    }

    public void setMsgcontent(String msgcontent) {
        this.msgcontent = msgcontent;
    }
}
