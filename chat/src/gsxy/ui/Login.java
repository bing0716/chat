package gsxy.ui;

import gsxy.bean.Account;
import gsxy.db.DBOper;
import gsxy.utils.Md5;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

//添加监听器 事件监听器 选项监听器 鼠标监听器
public class Login extends JFrame implements ActionListener,ItemListener,MouseListener {
    JComboBox cbqqCode;  //记住账号显示 下拉显示
    JPasswordField txtpwd;
    JLabel lblface,lblReg,lblfindpwd;
    JCheckBox cbmemory,cbautologin; //复选框的实现，复选框是一个可以
    // 被选定和取消选定的项，，它将其状态显示给用户。按照惯例，可以选定组中任意数量的复选框
    JButton btnLogin;

    public Login(){
//        this.setUndecorated(true);
//// this和super怎么不能放在一个方法里
        super("QQ2018");
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            //继承其中的方法来重新实现界面的不同风格
//            UIManager.setLookAndFeel("com.sun.java1.swing.plaf.motif.MotifLookAndFeel");
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
        //根据指定的路径创建一个文件，返回此图标的一个image
        Image icon = new ImageIcon("chat/img/face/9_online.png").getImage();
        this.setIconImage(icon);//设置要作为此窗口图标要显示的图像。

        JLabel lblhead =new JLabel(new ImageIcon("chat/img/loginbg.jpg"));
        add(lblhead);

        JLabel lblqqcode =new JLabel("账号",JLabel.RIGHT);
        String qqcodes[] = readQQCode();//读取曾经登陆的qq账号。 在本地文件中去访问以前的账号 这就是文件的IO流
        //这两个标签好像没有被实现。用一个返回值 和什么时候用空的方法 该去怎么调用，这有什么意义呢？

        cbqqCode =new JComboBox(qqcodes); //记住账号
        txtpwd =new JPasswordField();
        txtpwd.setBounds(100,160,150,25);
        lblfindpwd =new JLabel("找回密码");
        lblReg =new JLabel("注册用户");
        cbmemory =new JCheckBox("记住账号");
        cbautologin=new JCheckBox("自动登录");
        btnLogin =new JButton("登陆");

        cbqqCode.setBounds(100,120,150,25);
        cbqqCode.setEditable(true);
        lblReg.setBounds(250,120,80,25);
        lblReg.setForeground(Color.BLUE);//注册用户

        lblfindpwd.setBounds(250,160,80,25);
        lblfindpwd.setFont(new Font("宋体",Font.PLAIN,12));
        lblfindpwd.setForeground(Color.BLUE);

        cbmemory.setBounds(100,190,70,35);
        cbmemory.setForeground(Color.BLUE);
        cbmemory.setFont(new Font("宋体",Font.PLAIN,12));

        cbautologin.setBounds(180,190,70,25);
        cbautologin.setForeground(Color.BLUE);
        cbautologin.setFont(new Font("宋体",Font.PLAIN,12));

        btnLogin.setBounds(100,220,150,25);
        btnLogin.setForeground(Color.BLUE);
        btnLogin.setFont(new Font("宋体",Font.PLAIN,12));

        lblface=new JLabel(new ImageIcon("chat/img/face9.jpg"));
        lblface.setBounds(15,130,70,70);
        lblhead.add(cbqqCode);
        lblhead.add(lblReg);
        lblhead.add(txtpwd);
        lblhead.add(lblfindpwd);
        lblhead.add(cbmemory);
        lblhead.add(cbautologin);
        lblhead.add(btnLogin);
        lblhead.add(lblface);

        btnLogin.addActionListener(this); //按钮
        cbautologin.addItemListener(this);//自动登录
        cbmemory.addItemListener(this);//记住账号
        cbqqCode.addItemListener(this); //qq账号
        lblfindpwd.addMouseListener(this); //找回密码
        lblReg.addMouseListener(this);//注册用户

        setSize(420,340);
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Md5 md5 = new Md5();
        String qqcodes =cbqqCode.getSelectedItem().toString(); //转换成一个字符串
        //返回当前所选项的cbqqCode的QQ账号
        String pwds =md5.EncoderByMd5(txtpwd.getText().trim());

        Account acc =new Account();
        acc.setQqCode(Integer.parseInt(qqcodes));
        acc.setPwd(pwds);
        //先必须实例化一波，才能去调用里面的方法 get和set
        acc =new DBOper().login(acc);
        if(acc.getStatus() == 1){
            //登陆成功
            Icon ico =new ImageIcon(acc.getFace());
            lblface.setIcon(ico);
            //把登陆成功的的qq号码，保存到account.dat文件
            saveQQCode(acc.getQqCode()+"");
            JOptionPane.showMessageDialog(null,"登陆成功");
            new MainUI(acc); //显示主窗体
            this.dispose();
        }
      //  JOptionPane.showMessageDialog(null,e.getActionCommand());
    }

    @Override
    public void itemStateChanged(ItemEvent e) { //更改头像
        if(e.getSource() == cbqqCode){
            String qqcode = cbqqCode.getSelectedItem().toString();
            String face =new DBOper().getFace(Integer.parseInt(qqcode));
            lblface.setIcon(new ImageIcon(face));
        }
        //怎么给记住账号添加一个监听事件
//        if(e.getSource() == cbmemory){
//
// }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
            if(e.getSource() == lblReg){
                new RegUsers();
            }
            if(e.getSource()== lblfindpwd){
                new FindPassword();
            }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    //创建保存Qq账号的文件
     public void saveQQCode(String qqcode){
         File file = new File("account.dat");
         //把文件内容转换为数组
         String[] qqcodes =readQQCode(); //这个可以直接操作返回的就是String类型的
         Set set =new TreeSet();
         //是一个集合
         String str ="";
         if(qqcodes != null){
             for (int i = 0; i <qqcodes.length ; i++) {
                 set.add(qqcodes[i]);
             }
             set.add(qqcode);
             Iterator it = set.iterator();
             while(it.hasNext()){
                 str += it.next()+",";
             }
         }else{
             str =qqcode+"";
         }
         System.out.println(str);

         File f =new File("account.dat");
         try {
             BufferedWriter bw =new BufferedWriter(new FileWriter(f));
             bw.write(str);
             bw.close();
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

     //读取曾经登录过得的qq号码
    public String[] readQQCode(){
         String[] qqcodes={""}; //这里是一个数组qqcodes
         File file =new File("account.dat");
         //通过给定路径名字符串转换为抽象路径名来创建一个新的File实例
         if(file.exists()){
             try {
                 file.createNewFile();
                 BufferedReader br =new BufferedReader(new FileReader(file));
                 String str =br.readLine();//读取账号  返回的是一个String字符串 读取一个文本行
                 br.close();
                 //把文件内容转换为数组 这下面的是什么意思。
                 qqcodes = str.split(","); //split()方法用于把一个字符串分割成字符串数组。
             } catch (IOException e) {
                 e.printStackTrace();
             }
         } //这也是个return返回语句
         return qqcodes;
    }
    public static void main(String[] args) {
        new Login();
    }

}
