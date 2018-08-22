package gsxy.ui;

import com.sun.awt.AWTUtilities;
import gsxy.bean.Account;

import javax.swing.*;
import java.awt.*;

public class TipUI extends JFrame{
    JLabel lblTip; //上线弹出框
    public TipUI(){}
    public TipUI(Account self){
        //去掉窗口标题栏
        setUndecorated(true);
        setSize(300,150);
        //获取屏幕的宽度
        int width =getToolkit().getScreenSize().width;
        int height =getToolkit().getScreenSize().height;
        width =width-this.getWidth();
        Container con =getContentPane(); //返回
        String s=self.getNickName()+"【" +self.getQqCode()+"】上线啦！";
        lblTip =new JLabel(s,new ImageIcon(self.getFace()),JLabel.CENTER);
        lblTip.setBackground(Color.PINK);
        lblTip.setOpaque(true);
        //设置文本 图标 文职 背景颜色 还有

        con.add(lblTip);
        setVisible(true);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //改变窗口坐标
        for (int i = 0; i <100 ; i++) {
            setLocation(width, (height - (int) (i * 1.5)));
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //改变窗口透明度
        for (int i = 0; i >=1; i++) {
            //设置透明度，1~0之间
            AWTUtilities.setWindowOpacity(this, 0.01f * i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } //关闭窗口
        dispose();
    }
}
