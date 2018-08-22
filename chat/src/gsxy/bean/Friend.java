package gsxy.bean;

import java.io.Serializable;

public class Friend implements Serializable{
    private int id;
    private int selfAccount;        //自己的账号
    private int friendAccount;      //朋友账号
    private String groupname;       //所在分组（好友，家人，同学，同事，黑名单）
    private int invalid;        //是否黑名单（0：不是黑名单，1：是黑名单）

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

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public int getInvalid() {
        return invalid;
    }

    public void setInvalid(int invalid) {
        this.invalid = invalid;
    }
}
