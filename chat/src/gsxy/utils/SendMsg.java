package gsxy.utils;

import gsxy.bean.Account;

import javax.swing.text.StyledDocument;
import java.io.Serializable;

public class SendMsg implements Serializable{
    public int cmd;         //命令字
    public Account selfAccount;     //自己的信息
    public Account friendAccount;       //对方的信息
    public StyledDocument doc;      //发送内容
    public String sFileName;        //发送文件名称
    public Object[] b;      //文件内容

}
