package gsxy.utils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class SendmailUtil {
    public static void main(String[] args) throws MessagingException {
        Properties properties =new Properties(); //Properties类表示了一个持久的属性集。Properties可保存在流
        // 中或从流中加载。属性列表中每个键及其对应值都是一个字符串。
        String str[]=new String[9];

        try {
            FileReader  fr = new FileReader("D:\\test\\qq.txt");
            BufferedReader br = new BufferedReader(fr);
            for (int i = 0; i <9; i++) {
                String r = br.readLine();
                String[] s = r.split("=");
                String className = s[1];
                str[i] = className;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        properties.put("mail.transport.protocol",str[0]);//连接协议 每个相当于key value模式 相当于map集合
        properties.put("mail.smtp.host",str[1]);//发送邮件服务器stmp.qq.com 。接收邮件服务器是pop.qq.com。
        properties.put("mail.smtp.port",str[2]);//端口号
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.ssl.enable","false");//设置使用ssl安全连接 否
        properties.put("mail.debug","true");
        //得到回话对象
        Session session =Session.getInstance(properties);
        //获取邮件对象
        Message message =new MimeMessage(session);
        //设置发件人邮箱地址
        message.setFrom(new InternetAddress(str[3]));
        //设置收件人地址
        message.setRecipients(MimeMessage.RecipientType.TO,new InternetAddress[] {new InternetAddress(str[4])});
        //标题
        message.setSubject(str[5]);
        //设置邮件内容
        String resultcode =achieveCode();
        message.setText(resultcode);
        Transport transport =session.getTransport();
        //连接自己的邮箱账户
        transport.connect(str[7],str[8]);
        transport.sendMessage(message,message.getAllRecipients());
    }
    public static String achieveCode(){
        String[] beforeShuffle =new String[]{"k","j","i","h","g","f","e",
                "d","c","b","a","9","8","7","6","5","4","3","2","1","0"};
        List list = Arrays.asList(beforeShuffle);
        //返回一个受指定数组支持的固定大小的列表
        Collections.shuffle(list);//使用默认随机源对指定列表进行替换
        StringBuilder sb = new StringBuilder();
        //构造一个字符串生成器 集合说的长度是.size 数组是.length;
        for (int i = 0; i <list.size() ; i++) {
            sb.append(list.get(i));
        }
        String afterShuffle =sb.toString();
        String result = afterShuffle.substring(3, 9);//取中间6个数
        System.out.print(result);
        return  result;
    }
}
