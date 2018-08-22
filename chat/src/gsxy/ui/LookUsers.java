package gsxy.ui;

import gsxy.bean.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LookUsers extends JFrame implements ActionListener {
    private Account account;
    private JLabel lblbg;//背景图片
    //初始化界面
    public void init(){
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        Icon bgimg =new ImageIcon("chat/img/login_bgimg.jpg");

        lblbg =new JLabel(bgimg);
        add(lblbg);

        JLabel lblTitle = new JLabel("好友资料");
        lblTitle.setFont(new Font("宋体", Font.BOLD, 26));
        lblTitle.setBounds(150, 30, 150, 40);
        lblbg.add(lblTitle);
        lblbg.setBorder(BorderFactory.createBevelBorder(2));
        int i =0;
        JLabel lblface = new JLabel("好友头像", new ImageIcon(account.getFace()), JLabel.RIGHT);
        lblface.setBounds(270, 70, 160, 80);
        lblbg.add(lblface);
        JLabel lblqqcode =new JLabel("QQ号码"+account.getQqCode(),JLabel.RIGHT);
        JLabel lblnickName =new JLabel("昵称"+account.getNickName(),JLabel.RIGHT);
        JLabel lblipAddr =new JLabel("IP地址"+account.getIpAddr(),JLabel.RIGHT);
        JLabel lblport =new JLabel("端口"+account.getPort(),JLabel.RIGHT);
        JLabel lblage =new JLabel("年龄"+account.getAge(),JLabel.RIGHT);
        JLabel lblsex =new JLabel("性别"+account.getSex(),JLabel.RIGHT);
        JLabel lblnation =new JLabel("地区"+account.getArea(),JLabel.RIGHT);
        JLabel lblstar =new JLabel("星座"+account.getStar(),JLabel.RIGHT);
        JLabel lblremark =new JLabel("备注"+account.getRemark(),JLabel.RIGHT);
        JLabel lblselfsign = new JLabel("个性签名" + account.getSelfsign(), JLabel.RIGHT);

        lblqqcode.setBounds(50,70,160,25);
        lblbg.add(lblqqcode);
        lblnickName.setBounds(50,110,160,25);
        lblbg.add(lblnickName);

        lblipAddr.setBounds(50, 150, 160, 25);
        lblbg.add(lblipAddr);
        lblport.setBounds(270, 150, 160, 25);
        lblbg.add(lblport);

        lblage.setBounds(50,190,160,25);
        lblbg.add(lblage);
        lblsex.setBounds(270,190,160,25);
        ButtonGroup bg =new ButtonGroup();
        lblbg.add(lblsex);

        lblnation.setBounds(50,230,160,25);
        lblbg.add(lblnation);
        lblstar.setBounds(270, 230, 160, 25);
        lblbg.add(lblstar);

        lblremark.setBounds(50, 270, 160, 25);
        lblbg.add(lblremark);
        lblselfsign.setBounds(270,270,160,25);
        lblbg.add(lblselfsign);

        setResizable(false);
        setSize(500, 600);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    public LookUsers(){
        init();
    }
    public LookUsers(Account account){
        this.account=account;
        init();
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
