package gsxy.ui;

import gsxy.bean.Account;
import gsxy.utils.Cmd;
import gsxy.utils.Send;
import gsxy.utils.SendMsg;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatUI extends JFrame implements ActionListener,ItemListener{
    private Account selfAccount,friendAccount;
    JTextPane recePanel,sendPanel;
    JButton btnSend,btnShake,btnsendFile,btnColor,btnClose;
    JComboBox cbFont,cbSize,cbImg;
    JLabel lblboy,lblgirl,lblfriendInfo;

    public ChatUI(){};

    public ChatUI(Account self,Account friend){
        this.selfAccount=self;
        this.friendAccount=friend;
        ImageIcon ic =new ImageIcon(friend.getFace());
        setIconImage(ic.getImage()); //细节图片头像问题
        String str = null; //设置一个空的字符串
        if(friend.getRemark() != null && !friend.getRemark().equals("")){
            str =friend.getRemark()+"(";
        }else{
            str =friend.getNickName()+")";
        }
        str += friend.getQqCode()+")"+friend.getSelfsign();
        setTitle(str);
        lblfriendInfo= new JLabel(str,ic,JLabel.LEFT);
        add(lblfriendInfo, BorderLayout.NORTH);
        recePanel=new JTextPane();
        recePanel.setEnabled(false); //设置光标位置
        add(new JScrollPane(recePanel));
        btnShake =new JButton(new ImageIcon("chat/img/shake.png"));
        btnShake.setMargin(new Insets(0,0,0,0));//调整按钮大小为图片大小
        btnsendFile =new JButton("发送文件");
        btnColor =new JButton("字体颜色");
        String[] fonts = {"宋体", "斜体", "黑体", "隶书"};
        cbFont=new JComboBox(fonts);
        String[] fontsize = {"10", "11", "12", "13","20","25"};
        cbSize =new JComboBox(fontsize);
        cbImg =new JComboBox(getImg());

        JPanel btnPanel =new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.add(cbFont);
        btnPanel.add(cbSize);
        btnPanel.add(btnColor);
        btnPanel.add(btnShake);
        btnPanel.add(btnsendFile);
        btnPanel.add(cbImg);

        sendPanel =new JTextPane();
        JPanel southPanel =new JPanel(new BorderLayout());
        southPanel.add(btnPanel,BorderLayout.NORTH);
        southPanel.add(new JScrollPane(sendPanel));

        JPanel centerPanel =new JPanel(new GridLayout(2,1,0,0));
        centerPanel.add(new JScrollPane(recePanel));
        centerPanel.add(new JScrollPane(southPanel));
        add(centerPanel);
        btnSend=new JButton("发送（S）");
        btnSend.setMnemonic('S');
        btnClose =new JButton("关闭（X）");
        btnClose.setMnemonic('X');

        JPanel bottomPanel =new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(btnSend);
        bottomPanel.add(btnClose);
        add(bottomPanel,BorderLayout.SOUTH);
        lblboy =new JLabel(new ImageIcon("chat/img/qqshow1.jpg"));
        lblgirl =new JLabel(new ImageIcon("chat/img/qqshow2.jpg"));
        JPanel rightPanel =new JPanel(new GridLayout(2,1,5,0));
        rightPanel.add(lblboy);
        rightPanel.add(lblgirl);
        add(rightPanel,BorderLayout.EAST);
        btnShake.addActionListener(this);
        btnColor.addActionListener(this);
        btnSend.addActionListener(this);
        btnsendFile.addActionListener(this);
        btnClose.addActionListener(this);
        cbFont.addItemListener(this);
        cbSize.addItemListener(this);
        cbImg.addItemListener(this);

        setSize(600, 500);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        sendPanel.requestFocus();
    }
    public Icon[] getImg(){
        Icon[] icon =null;
        File file = new File("chat/img/bq");  //创建一个file实例对象 有什么用，还给了一个路径
        String sfile[]=file.list();

        icon = new ImageIcon[sfile.length];
        for (int i = 0; i <sfile.length ; i++) {
            icon[i] =new ImageIcon("chat/img/bq"+sfile[i]);
        }
        return icon;
    }
    //把发送框的内容提交到接收框，同时清除发送框的内容
    public void appendView(String name, StyledDocument xx){
        //获取接收框的文档（内容）
        StyledDocument vdoc =recePanel.getStyledDocument();
        //格式化时间
        Date date = new Date();//获取系统当前时间
        SimpleDateFormat sdf =new SimpleDateFormat("hh:mm:ss"); //设置显示时分秒
        String time =sdf.format(date); //获取时分秒
        //创建一个属性集合
        SimpleAttributeSet as =new SimpleAttributeSet();
        String s =name +" " +time +"\n";
        try {
            vdoc.insertString(vdoc.getLength(), s, as);
            int end =0;
            //处理显示的内容
            while(end <xx.getLength()){
                //获得一个元素
                Element e0 =xx.getCharacterElement(end);
                //获取对应的风格
                SimpleAttributeSet as1 =new SimpleAttributeSet();
                StyleConstants.setForeground(as1,StyleConstants.getForeground(e0.getAttributes()));
                StyleConstants.setFontSize(as1,StyleConstants.getFontSize(e0.getAttributes()));
                StyleConstants.setFontFamily(as1,StyleConstants.getFontFamily(e0.getAttributes()));

                //获取该元素的的内容
                s =e0.getDocument().getText(end,e0.getEndOffset() -end);
                //将元素添加到浏览窗中 防止漏写equals方法
                if("icon".equals(e0.getName())){
                    vdoc.insertString(vdoc.getLength(),s,e0.getAttributes());
                }else{
                    vdoc.insertString(vdoc.getLength(),s,as1);
                }
                end =e0.getEndOffset();
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

    }
    //动作监听器 还有鼠标监听器
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==btnSend){
            //动作监听事件中添加了视图
            appendView(selfAccount.getNickName(), sendPanel.getStyledDocument());
            //生成发送内容
            SendMsg msg =new SendMsg();  //utils工具类
            msg.cmd= Cmd.CMD_CHAT;  //这是什么意思
            msg.doc=sendPanel.getStyledDocument();//聊天内容
            msg.selfAccount =selfAccount;
            msg.friendAccount=friendAccount;
            //发送聊天信息
            new Send().send(msg);
            sendPanel.setText("");
        }else if(e.getSource()==btnShake){
            //生成发送类
            SendMsg msg = new SendMsg();
            msg.cmd =Cmd.CMD_SHAKE;
            msg.selfAccount=selfAccount;
            msg.friendAccount=friendAccount;
            //发送
            new Send().send(msg);
            shake();//抖动
        }else if (e.getSource() ==btnColor){
            JColorChooser cc =new JColorChooser(); //调用了一个工具类 用于选择颜色 都是用于判断
            Color c =cc.showDialog(this,"选择字体颜色",Color.BLACK);
            sendPanel.setForeground(c);//设置字体颜色
        }else if(e.getSource() == btnsendFile){
            String name=null;
            JFileChooser jFileChooser=new JFileChooser();
            int i=jFileChooser.showOpenDialog(null);
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int intRetVal=jFileChooser.showOpenDialog(null);
            if (intRetVal == JFileChooser.APPROVE_OPTION) {
//                TextFile.setText(jFileChooser.getSelectedFile().getAbsolutePath());
                name="jFileChooser.getSelectedFile().getAbsolutePath()";
            }

            //作业
        }else if(e.getSource() ==btnClose){
            dispose();//关闭窗口
        }

    }
    public void shake(){
        Point p=this.getLocationOnScreen();
        for (int i = 0; i <20 ; i++) {
            if(i%2 ==0)
            setLocation(p.x+5,p.y+5);
            else
                setLocation(p.x - 5, p.y - 5);//读懂时间 抖动的原理就是获取窗口当前坐标 上下移动
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getSource()==cbFont || e.getSource()==cbSize){
            String sfont =cbFont.getSelectedItem().toString(); //方法：放回当前所选项（Object） 为什么要转要转换为字符串
//            int size =Integer.parseInt(cbSize.getSelectedItem().toString());
            int size =Integer.parseInt(cbSize.getSelectedItem().toString());
            sendPanel.setFont(new Font(sfont,Font.PLAIN,size));
        }else if(e.getSource()==cbImg){
            Icon g = (Icon) cbImg.getSelectedItem();
            sendPanel.insertIcon(g);
        }
    }
}
