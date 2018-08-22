package gsxy.ui;

import gsxy.bean.Account;
import gsxy.db.DBOper;
import gsxy.utils.Cmd;
import gsxy.utils.Send;
import gsxy.utils.SendMsg;
import gsxy.utils.Sound;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.AbstractList;
import java.util.Hashtable;
import java.util.Vector;

public class MainUI extends JFrame implements MouseListener, ActionListener, ItemListener, WindowListener {
    private Account account,friendAccount;
    private JTabbedPane tab;
    private JLabel lblhead;
    private JList lstFriend,lstFamily,lstclassmate,lstHmd; //这里有问题 黑名单是拼音缩写
    private JButton btnFind;//查找好友
    private Vector<Account> vFriend,vFamily,vClassmate,vHmd,vallDetail;
    private JPopupMenu pop;
    private JMenu menu;
    private JMenuItem miChat,miLookInfo,miMove,miDel,miFriend,miFamily,miClassmate,miHmd;
    private Hashtable<Integer, ChatUI> chatUsers = null;
    private JComboBox cbStatus;
    private TrayIcon trayIcon; //托盘
    private PopupMenu popicon; //托盘菜单
    private MenuItem miexit,miopen; //托盘跳出菜单项

    public MainUI() {
    }

    public MainUI(Account acc) {
        this.account = acc;
        setTitle(acc.getNickName());
        setIconImage(new ImageIcon(acc.getFace()).getImage());
        //获取昵称，备注，qq号码，个性签名
        String str = "";
        if (acc.getRemark() != null || !acc.getRemark().equals("")) {
            str = acc.getRemark() + "(";
        } else {
            str = acc.getNickName() + "("; //备注未显示名称
        }
        str += acc.getQqCode() + ")," + acc.getSelfsign();
        lblhead = new JLabel(str, new ImageIcon(acc.getFace()), JLabel.LEFT);
        add(lblhead, BorderLayout.NORTH);
        String[] status = {"下线", "上线", "忙碌", "离开"};
        cbStatus = new JComboBox(status);
        //有点像泛型的意思在里面
        vFriend = new Vector<Account>();
        vFamily = new Vector<Account>();
        vClassmate = new Vector<Account>();
        vHmd = new Vector<Account>();
        vallDetail = new Vector<Account>();

        lstFamily = new JList();
        lstFriend = new JList();
        lstclassmate = new JList();
        lstHmd = new JList();

        //鼠标事件
        lstFriend.addMouseListener(this);
        lstFamily.addMouseListener(this);
        lstclassmate.addMouseListener(this);
        lstHmd.addMouseListener(this);
        refresh();
        tab = new JTabbedPane();
        tab.addTab("好友", new JScrollPane(lstFriend));
        tab.addTab("家人", new JScrollPane(lstFamily));
        tab.addTab("同学", new JScrollPane(lstclassmate));
        tab.addTab("黑名单", new JScrollPane(lstHmd));
        add(tab);
        createMenu(); //创建菜单
        miLookInfo.addActionListener(this);
        miChat.addActionListener(this);
        miMove.addActionListener(this);
        miDel.addActionListener(this);
        miFriend.addActionListener(this);
        miFamily.addActionListener(this);
        miClassmate.addActionListener(this);
        miHmd.addActionListener(this);
        btnFind = new JButton("查找好友");
        JPanel southPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        southPanel.add(btnFind);
        southPanel.add(cbStatus);
        cbStatus.addItemListener(this);
        add(southPanel, BorderLayout.SOUTH);
        btnFind.addActionListener(this);
        addWindowListener(this); //添加关闭监听

        createPopupMenu(); //创建托盘弹出菜单
        setTrayIcon();//创建托盘图标
        trayIcon.addMouseListener(this);


        setSize(260, 680);
        setVisible(true);
        setResizable(false);
        //获取屏幕宽度减去300，定位
        int width = Toolkit.getDefaultToolkit().getScreenSize().width - 300;
        setLocation(width, 50);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        new ReceThread().start();

    }

    //查找聊天窗口是否存在，存在，如果不存在则创建，存在就直接显示
    public ChatUI findWin(Integer qqcode, SendMsg msg) {
        ChatUI chat = null;
        //查找窗口是否存在 如果不存在则返回空值
        chat = chatUsers.get(qqcode);  //返回指定键所映射到的值，如果此映射不包含此键的映射，则返回null。
        if (chat == null) { //不存在，则创建窗口
            if (msg == null) //双击或者右键打开窗口
                chat = new ChatUI(account, friendAccount); //account和friendAccount的值从account类传进来的
            else //想成打开窗口
                chat = new ChatUI(msg.friendAccount, msg.selfAccount);
            //把窗口保存在到chatUsers变量中
            chatUsers.put(qqcode, chat);
        }
        if (!chat.isVisible()) {
            chat.show();
        }
        return chat;
    }
    //添加动作事件监听
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == miChat) { //聊天
//                new ChatUI(account,friendAccount);
            findWin(friendAccount.getQqCode(), null);
        } else if (e.getSource() == btnFind) { //查找好友
            new FindUI(account);
        } else if(e.getSource()==miexit){
            System.exit(0); //正常退出
        } else if(e.getSource()==miopen){
            //获取系统托盘
            SystemTray tray =SystemTray.getSystemTray();
            //删除托盘图标
            tray.remove(trayIcon);
            //显示托盘图标
            MainUI.this.setVisible(true);
            //窗口恢复到正常大小
            MainUI.this.setState(JFrame.NORMAL);
        } else if(e.getSource()==miLookInfo){//查看好友
            if(friendAccount !=null)
                new LookUsers(friendAccount);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        int status = cbStatus.getSelectedIndex();
        new DBOper().modifyStatus(account.getQqCode(), status);
        //更改自己的头像
        String filename = account.getFace();
        String file = "";
        //发送更改状态通知
        switch (status) {
            case Cmd.F_OFFLINE:
                file = filename.substring(0, filename.indexOf('_')) + "_offline.png";
                call(Cmd.CMD_OFFLINE);
                break;
            case Cmd.F_ONLINE:
                file =filename;
                call(Cmd.CMD_ONLINE);
                break;
            case Cmd.F_BUSY:
                file=filename.substring(0,filename.indexOf('_'))+"_busy.png";
                call(Cmd.CMD_BUSY);
                break;
            case Cmd.F_LEAVE:
                file =filename.substring(0,filename.indexOf("_"))+"_quiet.png";
                call(Cmd.CMD_LEAVE);
                break;
        }
        lblhead.setIcon(new ImageIcon(file));
    }

    //上线 离线通知
    public void call(int cmd){
        if(vallDetail ==null) return;
        for (int i = 0; i <vallDetail.size() ; i++) {
            Account a=vallDetail.get(i);
            //不发给自己，并且朋友必须自己在线（在线 忙碌 隐身）
            if(a.getQqCode() != account.getQqCode() && a.getStatus() != Cmd.F_OFFLINE){
                //生成发送类
                SendMsg msg =new SendMsg();
                msg.cmd=cmd;
                msg.selfAccount =account;
                msg.friendAccount=a;
                //发送
                new Send().send(msg);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == lstFriend) {
            friendAccount = vFriend.get(lstFriend.getSelectedIndex());
            //双击
            if (e.getClickCount() == 2) {
                JOptionPane.showMessageDialog(null, "您双击了好友列表");
//                          findWin(friendAccount.getOqCode(),null)
            } //右键
            if (e.getButton() == 3) {
                //选中了资料才能弹出右键菜单
                if (lstFriend.getSelectedIndex() >= 0)
                    pop.show(lstFriend, e.getX(), e.getY()); //显示信息
            }
        } else if (e.getSource() == lstFamily) {
            friendAccount = vFamily.get(lstFamily.getSelectedIndex()); //
            if (e.getClickCount() == 2) {
                //双击
//                             findWin(friendAccount.getQqCode(),null);
            }
            if (e.getButton() == 3) {//右键
                if (lstFamily.getSelectedIndex() >= 0)
                    pop.show(lstFamily, e.getX(), e.getY());
            }
        } else if (e.getSource() == lstclassmate) {
            friendAccount = vClassmate.get(lstclassmate.getSelectedIndex());
            if (e.getClickCount() == 2) {
//                                findWin(friendAccount.getQqCode(),null);
            }
            if (e.getButton() == 3) {
                if (lstclassmate.getSelectedIndex() >= 0) {
                    pop.show(lstclassmate, e.getX(), e.getY());
                }
            }
        } else if (e.getSource() == lstHmd) {
            friendAccount = vHmd.get(lstclassmate.getSelectedIndex());
            if (e.getClickCount() == 2) {
//                                findWin(friendAccount.getQqCode(),null);
            }
            if (e.getButton() == 3) {
                if (lstHmd.getSelectedIndex() >= 0) {
                    pop.show(lstHmd, e.getX(), e.getY());
                }
            }
        }
    }

//    public static void main(String[] args) {
//        new MainUI();
//    }
    //给上面四个选项好友 家人 同学 黑名单 加监听事件 也就是鼠标点击事件

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

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        //更改用户为离线
        new DBOper().modifyStatus(account.getQqCode(),Cmd.F_OFFLINE);
        //发送离线通知
        call(Cmd.CMD_OFFLINE);



    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {
        //获取系统托盘
        SystemTray tray =SystemTray.getSystemTray();
        //创建一个托盘图标
        if(trayIcon !=null){//如果托盘图标不等于空
            tray.remove(trayIcon);
        }
        try {
            tray.add(trayIcon);
            setVisible(false); //是否隐藏正常任务栏图标
        } catch (AWTException e1) {
            System.out.println("托盘处理失败"+e1.getMessage());
        }
    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    public void refresh() {
        vallDetail = new DBOper().getAllinfo(account);
        //清除以前的所有记录
        vFriend.clear();
        vFamily.clear();
        vClassmate.clear();
        vHmd.clear();
        //把数据库中的最新数据分到放到对相应的Vector；
        for (int i = 0; i < vallDetail.size(); i++) {
            Account a = vallDetail.get(i);
            if (a.getGroupname().equals(Cmd.F_FRIEND))
                vFriend.add(a);
            else if (a.getGroupname().equals(Cmd.F_FAMILY))
                vFamily.add(a);
            else if (a.getGroupname().equals(Cmd.F_CLASSMATE))
                vClassmate.add(a);
            else if (a.getGroupname().equals(Cmd.F_BLACKLIST))
                vHmd.add(a);
        }
        //把向量放到list控件中，显示到界面
        //好友资料
        lstFriend.setModel(new listmode(vFriend));//显示资料
        lstFriend.setCellRenderer(new myfriend(vFriend)); //显示头像

        //家人资料
        lstFamily.setModel(new listmode(vFamily));
        lstFamily.setCellRenderer(new myfriend(vFamily));

        //同学资料
        lstclassmate.setModel(new listmode(vClassmate));
        lstclassmate.setCellRenderer(new myfriend(vClassmate));

        //黑名单
        lstHmd.setModel(new listmode(vHmd));
        lstHmd.setCellRenderer(new myfriend(vHmd));
    }    //refresh()方法

    //创建菜单
    public void createMenu() {
        pop = new JPopupMenu();
        miChat = new JMenuItem("聊天");
        miLookInfo = new JMenuItem("查看资料");
        miFriend = new JMenuItem("移动到好友");
        miFamily = new JMenuItem("移动到家人");
        miClassmate = new JMenuItem("移动到同学");
        miHmd = new JMenuItem("移动到黑名单");
        miMove = new JMenuItem("移动到好友");
        miDel = new JMenuItem("删除好友");

        pop.add(miChat);
        pop.add(miLookInfo);
        pop.add(miMove);
        pop.add(miDel);
        pop.add(miFriend);
        pop.add(miFamily);
        pop.add(miClassmate);
        pop.add(miHmd);
    }

    class listmode extends AbstractListModel {
        Vector dats; // Vetor类可以实现可增长的对象数组。与数组一样，它包含可以使用整数索引进行访问

        // 组件。但是Vetor的大小根据需要增大或缩小，以适应创建Vetor后进行添加或移除项的操作。
        public listmode(Vector dats) {
            this.dats = dats;
        }

        //获取长度
        @Override
        public int getSize() {
            return dats.size(); //返回此向量中的组件数
        }

        //获取行数
        @Override
        public Object getElementAt(int index) {
            Account user = (Account) dats.get(index); //get方法 返回此向量中指定位置的元素
            return user.getNickName() + "【" + user.getQqCode() + "】";
        }
    }


    //获取好友对象 其中实现了功能好友上线 以及下线 离开 忙碌的状态头像 以及获取好友的的昵称nickname 账号
    class myfriend extends DefaultListCellRenderer {
        Vector datas;

        public myfriend(Vector datas) {
            this.datas = datas;
        } //用构造方法来实现调用传参数。

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (index >= 0 && index < datas.size()) {
                Account user = (Account) datas.get(index);
                //给列表中的好友状态设置头像
                String filename = user.getFace();
                String file = "";
                switch (user.getStatus()) {
                    case Cmd.F_ONLINE:
                        file = filename;
                        break;
                    case Cmd.F_OFFLINE:
                        file = filename.substring(0, filename.indexOf('_')) + "_offline.png";
                        break;
                    case Cmd.F_BUSY:
                        file = filename.substring(0, filename.indexOf('_')) + "_busy.png";
                        break;
                    case Cmd.F_LEAVE:
                        file = filename.substring(0, filename.indexOf('_')) + "_quiet.png";
                        break;
                }
                setIcon(new ImageIcon(file));
                setText(user.getNickName().trim() + "(" + user.getQqCode() + ")");
            }
            //可能代码有两个错误。
            //设置字体颜色
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setOpaque(true);
            return this;
        }
    }

    //接收消息的多线程
    class ReceThread extends Thread {
        public ReceThread(){
            //启动线程时创建保存窗口的变量chatUsers
            chatUsers = new Hashtable<Integer, ChatUI>();
        }
        @Override
        public void run() {
            //zai自己的窗口接收数据
            try {
                DatagramSocket serverSocket = new DatagramSocket(account.getPort());
                while (true) {
                    byte[] b = new byte[1024 * 70];
                    DatagramPacket pack = new DatagramPacket(b, b.length);
                    serverSocket.receive(pack);
                    ByteArrayInputStream bis = new ByteArrayInputStream(b, 0, pack.getLength());
                    ObjectInputStream ois = new ObjectInputStream(bis);
                    SendMsg msg = (SendMsg) ois.readObject();
                    ChatUI chat = null;
                    //处理各种不同的信息
                    switch (msg.cmd) {
                        case Cmd.CMD_CHAT: //接收聊天
                            chat = findWin(msg.selfAccount.getQqCode(), msg);
                            //获取聊天信息，并显示聊天窗口
                            chat.appendView(msg.selfAccount.getNickName(), msg.doc);
                            break;
                        case Cmd.CMD_SHAKE: //接受抖动消息
                            chat = findWin(msg.selfAccount.getQqCode(), msg);
                            chat.shake();
                            break;
                        case Cmd.CMD_ONLINE: //接收上线通知
                            refresh(); //更新界面
                            new Sound();
                            //显示上线提示窗口
                            new TipUI(msg.selfAccount);
                            break;
                            case Cmd.CMD_OFFLINE://接受离线通知
                            case Cmd.CMD_LEAVE:  //离开
                            case Cmd.CMD_BUSY: //忙碌
                                refresh(); //更新界面
                                break;
                        case Cmd.CMD_SENDFILE:
                            String str1 = msg.selfAccount.getNickName() + "向你发送文件";
                            SendMsg m1 =new SendMsg();
                            m1.friendAccount =msg.selfAccount;
                            m1.selfAccount=msg.friendAccount;

                        case Cmd.CMD_ADDFRIEND://添加好友
                            String str = msg.selfAccount.getNickName() + "添加您为好友";
                            SendMsg m =new SendMsg();
                            m.friendAccount=msg.selfAccount;
                            m.selfAccount=msg.friendAccount;

                            if(JOptionPane.showConfirmDialog(null,str,"添加好友",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
                                        new DBOper().addFriend(msg.friendAccount,msg.selfAccount.getQqCode());
                                        refresh();
                                        m.cmd=Cmd.CMD_AGREEFRIEND;
                            }else{
                                        m.cmd=Cmd.CMD_REJECTFRIEND;
                            }
                                    new Send().send(m);
                                    break;
                        case Cmd.CMD_AGREEFRIEND:// 同意添加           :
                            refresh();
                            break;
                        case Cmd.CMD_REJECTFRIEND://拒绝添加
                            JOptionPane.showMessageDialog(null,msg.selfAccount.getQqCode()+"对方拒绝了你");
                            break;

                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    } //ReceThread在这个类里面。



    //创建托盘坐标的方法
    public void setTrayIcon(){
        Image image = new ImageIcon(account.getFace()).getImage();
        //创建托盘
        trayIcon =new TrayIcon(image,"QQ2018",popicon);
        trayIcon.setImageAutoSize(true); //系统自动调整图标大小
    }
    //c创建托盘菜单
    public void createPopupMenu(){
        //图标菜单
        popicon =new PopupMenu();
        miexit = new MenuItem("退出系统");
        miopen =new MenuItem("打开主界面");
        popicon.add(miopen);
        popicon.add(miexit);
        miexit.addActionListener(this);
        miopen.addActionListener(this);
    }
}
