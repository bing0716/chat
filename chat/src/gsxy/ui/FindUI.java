package gsxy.ui;

import gsxy.bean.Account;
import gsxy.db.DBOper;
import gsxy.utils.Cmd;
import gsxy.utils.Send;
import gsxy.utils.SendMsg;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class FindUI extends JFrame implements ActionListener{
    private JTextField txtQQcode,txtNickname,txtage;
    private JComboBox cbSex;
    private JTable dataTable;
    private JButton btnFind,btnClose,btnAdd;
    private Vector vhead,vdata; //这是个集合
    private Account account;
    myTable mytable;

    public FindUI(){}

    public FindUI(Account account){
        super("查找好友"); //图标名称
        this.account=account;
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        JPanel topPanel =new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblqqcode =new JLabel("QQ号码：");
        JLabel lblnickname =new JLabel("昵称：");
        JLabel lblage =new JLabel("年龄：");
        JLabel lblsex = new JLabel("性别：");
        txtQQcode =new JTextField(10);
        txtNickname =new JTextField(10);
        txtage = new JTextField(10);
        cbSex =new JComboBox(new String[]{"男","女"}); //一种新的写法 直接数组加在这里
        btnFind=new JButton("查找（F）");
        btnFind.setMnemonic('F'); //相当于快捷键 alt+F实现发送
        topPanel.add(lblqqcode);
        topPanel.add(txtQQcode);
        topPanel.add(lblnickname);
        topPanel.add(txtNickname);
        topPanel.add(lblage);
        topPanel.add(txtage);
        topPanel.add(lblsex);
        topPanel.add(cbSex);
        topPanel.add(btnFind);
        btnFind.addActionListener(this); //添加监听事件
        add(topPanel,BorderLayout.NORTH); //加载最上面

        vhead=new Vector();
        vhead.add("QQ号码");
        vhead.add("昵称");
        vhead.add("年龄");
        vhead.add("性别");
        vhead.add("地区");
        vhead.add("星座");
        vhead.add("头像");
        vhead.add("备注");


        vdata=new Vector();
        mytable =new myTable(vdata,vhead);
        dataTable=new JTable(mytable);
        add(new JScrollPane(dataTable));
        btnClose=new JButton("关闭（X）");
        btnClose.setMnemonic('X');
        btnAdd=new JButton("添加（A）");
        btnAdd.setMnemonic('A');
        JPanel bottomPanel =new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(btnAdd);
        bottomPanel.add(btnClose);
        add(bottomPanel,BorderLayout.SOUTH);
        btnAdd.addActionListener(this);
        btnClose.addActionListener(this);

        setSize(750,400);
        setVisible(true);
        setLocationRelativeTo(null);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void find(Account acc){
        vdata=new DBOper().find(acc);
        mytable.datas=vdata;
        dataTable.updateUI();//刷新界面
    }


    @Override
    public void actionPerformed(ActionEvent e) {
            if(e.getSource()== btnFind){
                Account acc =new Account();
                if (!txtQQcode.getText().equals(""))
                    acc.setQqCode(Integer.parseInt(txtQQcode.getText().trim()));
                if(!txtNickname.getText().equals(""))
                    acc.setNickName(txtNickname.getText().trim());
                if(!txtage.getText().equals(""))
                    acc.setAge(Integer.parseInt(txtage.getText().trim()));
                acc.setSex(cbSex.getSelectedItem().toString());
                 find(acc);
            }else if(e.getSource()==btnAdd){
                int row,col;
                row=dataTable.getSelectedRow();
                Vector rowData = (Vector) vdata.get(row);
                //获取第一列QQcode
                int qqcode = (int) rowData.get(0);
                Account acc =new DBOper().findPersonInfo(qqcode);



                SendMsg msg =new SendMsg();
                msg.cmd = Cmd.CMD_ADDFRIEND;
                msg.friendAccount =acc;
                msg.selfAccount=account;
                new Send().send(msg);
            }else if(e.getSource()==btnClose){
                dispose();
            }

    }

    //设置JTable的控件的值
    class myTable extends AbstractTableModel{
        Vector datas =new Vector();
        Vector columns =new Vector();
        //构造方法必须把表头和数据传进来
        public myTable(Vector datas,Vector columns){
            this.datas=datas;
            this.columns=columns;
        }
        //获取列名
        public String getColumnName(int columnIndex){
            return columns.get(columnIndex).toString();

        }
        //获取行数
        @Override
        public int getRowCount(){
            return datas.size();
        }
        //获取列数
        @Override
        public int getColumnCount(){
           return columns.size();
        }
        //获取每一列要显示的数据类型
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Vector v = (Vector) datas.get(rowIndex);
            if(columnIndex==6) //头像列返回图片
                return new ImageIcon(v.get(columnIndex).toString());
            else
                return v.get(columnIndex);
        }
    }
}
