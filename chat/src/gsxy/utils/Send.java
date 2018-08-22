package gsxy.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Send {
    //发送消息的方法
    public void send(SendMsg msg){
        try {
            //字节数组输出流
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(msg);
            byte[] b = bos.toByteArray();       //把要发送的信息，全部转换为字节数
            DatagramSocket socket = new DatagramSocket();
            InetAddress add = InetAddress.getByName(msg.friendAccount.getIpAddr());
            int port = msg.friendAccount.getPort();
            DatagramPacket p = new DatagramPacket(b,0,b.length,add,port);
            //发送
            socket.send(p);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
