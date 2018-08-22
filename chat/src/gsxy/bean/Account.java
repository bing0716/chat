package gsxy.bean;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;

public class Account implements Serializable{
    //对应Account表，一个字段对应一个成员变量
    private int qqCode;     //qq账号
    private String nickName;        //昵称
    private String name;        //真实姓名
    private String pwd;     //密码
    private String ipAddr;      //ip地址
    private int port;       //端口
    private int age;        //年龄
    private String sex;     //性别
    private String area;        //地区
    private String star;        //星座
    private String face;        //头像
    private String remark;      //备注
    private String selfsign;      //个性签名
    private int status;     //状态，0=离线，1=在线，2=隐身，3=忙碌
    private String groupname;    //分组

//    @Override
//    public String toString() {
//        return "Account{" +
//                "qqCode=" + qqCode +
//                '}';
//    }   //打印输出的时候也会自动重写了toString方法，只不过我原本不知道。

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public int getQqCode() {
        return qqCode;
    }

    public void setQqCode(int qqCode) {
        this.qqCode = qqCode;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSelfsign() {
        return selfsign;
    }

    public void setSelfsign(String selfsign) {
        this.selfsign = selfsign;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    /*public  String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/


}
