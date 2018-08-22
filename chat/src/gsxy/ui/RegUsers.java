package gsxy.ui;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import gsxy.bean.Account;
import gsxy.db.DBHelper;
import gsxy.db.DBOper;
import gsxy.utils.Md5;
import jdk.nashorn.internal.scripts.JO;
import sun.misc.BASE64Encoder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegUsers extends JFrame implements ActionListener{
    private Account account;
    private JLabel lblbg;   //背景图片
    private JComboBox cbface,area,star; //组合框或者下拉框 可以进行选择
    private JTextField qqCode,nickName,port,age,remark,ipAddr;
    private JRadioButton sex1,sex2; //单选框 进行性别的选择
    private JTextArea selfsign; //个性签名用了一个文本域 更加丰富
    private JPasswordField pwd,cfgpwd;      //验证密码和确认密码 JPasswordField是一个轻量级组件，允许编辑单行文本 不显示原始内容。
    private JButton btnSave,btnClose;       //两个按钮
    private String areas[] = {"北京","上海","安徽","浙江","广州","深圳"};
    private String stars[] = {"水瓶座","狮子座","处女座","白羊座","双鱼座","巨蟹座","射手座","天蝎座","金牛座"};
    //下面用的都是绝对路径 ，所有的项目当中都应该是相对路径
    private String[] faces = { //利用绝对路径从chat/img/login_bgimg.jpg
            "F:\\Mysql书籍\\img&sound\\img\\face\\0_online.png",
            "F:\\Mysql书籍\\img&sound\\img\\face\\1_online.png",
            "F:\\Mysql书籍\\img&sound\\img\\face\\2_online.png",
            "F:\\Mysql书籍\\img&sound\\img\\face\\3_online.png",
            "F:\\Mysql书籍\\img&sound\\img\\face\\4_online.png",
            "F:\\Mysql书籍\\img&sound\\img\\face\\5_online.png",
            "F:\\Mysql书籍\\img&sound\\img\\face\\6_online.png",
            "F:\\Mysql书籍\\img&sound\\img\\face\\7_online.png",
            "F:\\Mysql书籍\\img&sound\\img\\face\\8_online.png",
            "F:\\Mysql书籍\\img&sound\\img\\face\\9_online.png",
            "F:\\Mysql书籍\\img&sound\\img\\face\\10_online.png"
    };

    public RegUsers(){
        init();
    }  //为什么构造方法里直接用方法名去调用

    public RegUsers(Account account){
        this.account = account;
        init();
    }

    //初始化界面
    public void init(){
        try {
            //使用当前线程的上下文类加载器加载给定类名名称所指定的LookAndFeel，并将它传给setLookAndFeel
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        //根据一个指定的文件去创建ImageIcon
        Icon bgimg = new ImageIcon("F:\\Mysql书籍\\img&sound\\img\\login_bgimg.jpg");
        lblbg = new JLabel(bgimg);
        this.add(lblbg);  //为什么用this前缀 或者不用

        JLabel lblTitle = new JLabel("个人资料");   //指定文本内容，默认边对齐
        lblTitle.setFont(new Font("宋体", Font.BOLD,26)); //设置字体的通用格式 String name，style，size；
        lblTitle.setBounds(150,30,150,40);
        lblbg.add(lblTitle); //在JLabel标签里添加内容啦

        lblbg.setBorder(BorderFactory.createBevelBorder(2));
        int i = 0;
        Icon[] face = {
                new ImageIcon(faces[i++]),//根据一个给定的文件创建一个ImageIcon。
                new ImageIcon(faces[i++]),
                new ImageIcon(faces[i++]),
                new ImageIcon(faces[i++]),
                new ImageIcon(faces[i++]),
                new ImageIcon(faces[i++]),
                new ImageIcon(faces[i++]),
                new ImageIcon(faces[i++]),
                new ImageIcon(faces[i++]),
                new ImageIcon(faces[i++]),
                new ImageIcon(faces[i++]),
        };

        cbface = new JComboBox(face); //组合框或者下拉框 要进行一个数组的选用
        cbface.setMaximumRowCount(3); //设置JComboBox显示的最大行数
        JLabel lblface = new JLabel("头像：",JLabel.RIGHT); //创建具有指定文本和水平对其方法的JLable实例
        lblface.setBounds(270,70,60,25);
        cbface.setBounds(330,70,80,60);     //后面两位设置图片宽高
        lblbg.add(lblface);
        lblbg.add(cbface); //显示头像文字和头像图片显示的具体位置添加到背景图片中


        JLabel lblqqCode = new JLabel("QQ号码：",JLabel.RIGHT);
        JLabel lblnickName = new JLabel("昵称：",JLabel.RIGHT);
        JLabel lblpwd = new JLabel("登录密码：",JLabel.RIGHT);
        JLabel lblcfgpwd = new JLabel("确认密码：",JLabel.RIGHT);
        JLabel lblipAddr = new JLabel("本机地址：",JLabel.RIGHT);
        JLabel lblport = new JLabel("连接端口：",JLabel.RIGHT);
        JLabel lblage = new JLabel("年龄：",JLabel.RIGHT);
        JLabel lblsex = new JLabel("性别：",JLabel.RIGHT);
        JLabel lblnation = new JLabel("地区：",JLabel.RIGHT);
        JLabel lblstar = new JLabel("星座：",JLabel.RIGHT);
        JLabel lblremark = new JLabel("备注：",JLabel.RIGHT);
        JLabel lblselfsign = new JLabel("个性签名：",JLabel.RIGHT);

        qqCode = new JTextField(10);
        lblqqCode.setBounds(30,70,80,25);
        qqCode.setBounds(110,70,100,25);
        lblbg.add(lblqqCode);
        lblbg.add(qqCode);

        nickName = new JTextField(15);
        lblnickName.setBounds(30,110,80,25);
        nickName.setBounds(110,110,100,25);
        lblbg.add(lblnickName);
        lblbg.add(nickName);

        pwd = new JPasswordField(15);
        cfgpwd = new JPasswordField(15);
        lblpwd.setBounds(20,150,90,25);
        pwd.setBounds(110,150,100,25);
        lblcfgpwd.setBounds(240,150,90,25);
        cfgpwd.setBounds(330,150,100,25);
        lblbg.add(lblpwd);
        lblbg.add(pwd);
        lblbg.add(lblcfgpwd);
        lblbg.add(cfgpwd);


        //获取本机IP地址 也是一种固定的写法：显示getLocalHost方法 然后.getHostAddress方法
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        ipAddr = new JTextField(addr.getHostAddress());
        ipAddr.setEditable(false);      //只读，设置为不可编辑的模式
        lblipAddr.setBounds(20,190,90,25);
        ipAddr.setBounds(110,190,125,25);
        lblbg.add(lblipAddr);
        lblbg.add(ipAddr);

        port = new JTextField();
        lblport.setBounds(240,190,90,25);
        port.setBounds(330,190,100,25);
        lblbg.add(lblport);
        lblbg.add(port);

        age = new JTextField("0");      //默认年龄
        lblage.setBounds(50,230,60,25);
        age.setBounds(110,230,60,25);
        lblbg.add(lblage);
        lblbg.add(age);

        sex1 = new JRadioButton("男"); //JRadioButton实现一个单选按钮，此按钮可别选择或取消，可为用户显示其状态
        sex2 = new JRadioButton("女");
        sex1.setSelected(true);
        lblsex.setBounds(270,230,60,25);
        sex1.setBounds(330,230,60,25);
        sex2.setBounds(390,230,60,25);
        ButtonGroup bg = new ButtonGroup();//JRadioButton与ButtonGroup对象配合使用可创建一组按钮，一次只能选择其中的一个按钮
        bg.add(sex1);
        bg.add(sex2);
        lblbg.add(lblsex);
        lblbg.add(sex1);
        lblbg.add(sex2);

        area = new JComboBox(areas);
        lblnation.setBounds(50,270,60,25);
        area.setBounds(110,270,100,25);
        lblbg.add(lblnation);
        lblbg.add(area);

        star = new JComboBox(stars);
        lblstar.setBounds(270,270,60,25);
        star.setBounds(330,270,100,25);
        lblbg.add(lblstar);
        lblbg.add(star);

        remark = new JTextField(); //没有设置文本框列的长度
        lblremark.setBounds(50,310,60,25);
        remark.setBounds(110,310,200,25);
        lblbg.add(lblremark);
        lblbg.add(remark);

        selfsign = new JTextArea();
        lblselfsign.setBounds(20,350,90,25);
        selfsign.setBounds(110,350,320,80);
        JScrollPane spanel = new JScrollPane(selfsign);
        spanel.setBounds(110,350,320,80);
        lblbg.add(lblselfsign);
        lblbg.add(spanel);

        btnSave = new JButton("保存");
        btnClose = new JButton("关闭");
        btnSave.setBounds(150,485,80,30);
        btnClose.setBounds(280,485,80,30);
        lblbg.add(btnSave);
        lblbg.add(btnClose);

        //加入监听按钮
        btnSave.addActionListener(this);
        btnClose.addActionListener(this);
       /* btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });*/



        setResizable(true);        //不能更改窗口大小
        setSize(500,550);
        setVisible(true);
        setLocationRelativeTo(null);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     //退出系统 加this是指带继承JFrame类
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//关闭当前窗口
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==btnClose){ //e.getSource是获得事件源 也就是触发事件
        dispose();

        }
        else if(e.getSource()==btnSave){
            Account account =new Account(); //new出来一个实例对象 进行删除报错：空指针异常
            try {
                account.setQqCode(Integer.parseInt(qqCode.getText().trim()));
            } catch (NumberFormatException e1) {
               JOptionPane.showMessageDialog(null,"QQ号码必须为合法的整型数据。");
               return;
            }


           /* *//**利用MD5进行加密*//*
            public String EncoderByMd5(String pwd.getText()) throws NoSuchAlgorithmException, UnsupportedEncodingException{
                //确定计算方法
                MessageDigest md5= null;
                try {
                    md5 = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException e1) {
                    e1.printStackTrace();
                }
                BASE64Encoder base64en = new BASE64Encoder();
                //加密后的字符串
            try {
                String newstr=base64en.encode(md5.digest(pwd..getBytes("utf-8")));
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            return pwd.getText();
            }*/



            //验证密码和确认密码是否一致
            if(pwd.getText().trim() ==null){
                JOptionPane.showMessageDialog(null,"请输入登陆密码");
                return;
            }
            if(!pwd.getText().equals(cfgpwd.getText())){
                JOptionPane.showMessageDialog(null,"登录密码和确认密码不一致");
                return;
            }
            if(nickName.getText() ==null || nickName.getText().equals("")){
                JOptionPane.showMessageDialog(null,"昵称不能为空");
                return;
            }
            int  nport=0;
            try {
                nport= Integer.parseInt(port.getText().trim());
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(null, "端口含有非法字符");
                return;
            }
            if(nport <1024 || nport >65535){
                JOptionPane.showMessageDialog(null,"端口必须在1024——65535之间");
                return;
            }
            int nage=0;
            try {
                nage =Integer.parseInt(age.getText().trim());
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(null, "年龄中含有非法字符");
                return;
            }
            if(nage<1 ||nage>150){
                JOptionPane.showMessageDialog(null,"年龄在1~150岁之间");
                return;
            }
            //直接把信息通过sql语句写进去 可以用实体类进行写入
            //把值设置到account对象中
            //Integer.parseInt的作用是什么？
            account.setQqCode(Integer.parseInt(qqCode.getText().trim()));  //还在保存按钮中判断条件，在本类中去获得注册用户的信息反馈给Account表
            account.setNickName(nickName.getText().trim()); //要将密码进行加密
            Md5 md5 = new Md5();
//            String md5 =account.getMD5(pwd.getText().trim());
            account.setPwd(md5.EncoderByMd5(pwd.getText().trim()));
            account.setIpAddr(ipAddr.getText().trim());
            account.setPort(Integer.parseInt(port.getText().trim()));
            account.setFace(faces[cbface.getSelectedIndex()]);
            account.setAge(Integer.parseInt(age.getText().trim()));
            if(sex1.isSelected()){
                account.setSex("男");
            }else{
                account.setSex("女");
            }
            account.setArea(areas[area.getSelectedIndex()]);
            account.setStar(stars[star.getSelectedIndex()]);
            account.setRemark(remark.getText().trim());
            account.setSelfsign(selfsign.getText().trim());
            account.setStatus(0);

            DBOper db =new DBOper();//


            //判断端口是否被占用 返回是一个int型的端口号给
            boolean b =db.isPort(account.getPort());
            if(b){
                JOptionPane.showMessageDialog(null,"端口已经被占用，请输入其他端口");
                port.requestFocus();//获取焦点
                return;
            }
            boolean b1 =db.isqqCode(account.getQqCode());
            if(b1){
                JOptionPane.showMessageDialog(null,"此qq已被注册，请输入其他账号");
                qqCode.requestFocus();
                return;
            }
            b= db.addUser(account);
            if(b){
                JOptionPane.showMessageDialog(null,"恭喜你，注册成功，请记住你的QQ号");
                new Login();
            }else{
                JOptionPane.showMessageDialog(null,"注册失败");
            }
        }  //基本上所有的内容都在保存按钮上进行判断条件语句

    }

    public static void main(String[] args) {
        new RegUsers();
    }
}
