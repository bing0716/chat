package gsxy.db;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import gsxy.bean.Account;
import gsxy.utils.Cmd;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Vector;

public class DBOper {
    //注册用户
    public boolean addUser(Account acc){
        boolean bok =false;
        int num =0;
        Connection conn =new DBHelper().getConn(); //实例化一个类  进行一个接口的的引用
        // 或者是进行引用 返回的就是一个conn的一个值
        String sql ="insert into account(qqCode,nickname,pwd,ipAddress,port,age,sex,area,star," +
                "face,remark,selfsign,status) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        //定义的一个sql语句
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            int i=1; //定义列名，把每一列进行操作 操作到sql语句中
            pstmt.setInt(i++,acc.getQqCode());
            pstmt.setString(i++,acc.getNickName());
            pstmt.setString(i++,acc.getPwd());
            pstmt.setString(i++,acc.getIpAddr());
            pstmt.setInt(i++,acc.getPort());
            pstmt.setInt(i++,acc.getAge());
            pstmt.setString(i++,acc.getSex());
            pstmt.setString(i++,acc.getArea());
            pstmt.setString(i++,acc.getStar());
            pstmt.setString(i++,acc.getFace());
            pstmt.setString(i++,acc.getRemark());
            pstmt.setString(i++,acc.getSelfsign());
            pstmt.setInt(i++,acc.getStatus());

            num= pstmt.executeUpdate();//将sql语句执行的结果返回给数据库
            pstmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        boolean b = num ==0 ? (bok=false) :(bok=true);

        //三元运算符，执行布尔表达式 ，结果为真 则是结果1，结果为假，则执行结果2.
        // 结果b为 bok=false
        return bok;
    }

    //通
    public String getPwd(int qqcode){
//        Account acc =new Account();
        String getPwd=null; //设置为空，或者设置为“” 有什么区别
        Connection conn =new DBHelper().getConn();
        String sql ="select pwd from account where qqcode = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,qqcode);
            ResultSet rs =pstmt.executeQuery();
            if(rs.next()){
                getPwd =rs.getString("pwd").trim();
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getPwd;
    }


    //判断端口是否已经被别人占用
    public boolean isPort(int port){
        boolean bok =false;
        Connection conn =new DBHelper().getConn();
        String sql ="select * from account where port = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            int i=1;
            pstmt.setInt(i++,port);
            ResultSet rs=pstmt.executeQuery();
            if(rs.next()){
                bok =true;//已经被占用
            }
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bok;
    }
    public boolean isqqCode(int qqCode){
        boolean bok =false;
        Connection conn =new DBHelper().getConn();
        String sql ="select qqCode from account where qqCode = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,qqCode);
            ResultSet rs =pstmt.executeQuery();
            if(rs.next()){
                bok =true;
            }
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bok;
    }


    //登陆的界面 按照需求去返回不同的值。 我需要这一个类的返回 就需要整个数据库的信息。
    public Account login(Account acc){
        Connection conn =new DBHelper().getConn();
        String sql="select * from account where qqcode = ? and pwd =?";
//        System.out.println();

//        try {
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            byte[] input =acc.getPwd().getBytes();
//            byte[] output =md.digest(input);
//            String str = Base64.encode(output);
//
//        } catch (NoSuchAlgorithmException e1) {
//            e1.printStackTrace();
//        }
        try {
            PreparedStatement pstmt =conn.prepareStatement(sql);
            int i =1;
            pstmt.setInt(i++,acc.getQqCode());
            pstmt.setString(i++,acc.getPwd());
            ResultSet rs=pstmt.executeQuery(); //查询的结果返回到结果集。
            if(rs.next()){
                //修改状态为在线状态
                acc.setNickName(rs.getString("nickname").trim());
                acc.setIpAddr(rs.getString("ipaddress").trim());
                acc.setPort(rs.getInt("port"));
                acc.setAge(rs.getInt("age"));
                acc.setSex(rs.getString ("sex").trim());
                acc.setArea(rs.getString("area").trim());
                acc.setStar(rs.getString("star").trim());
                acc.setFace(rs.getString("face").trim());
                acc.setRemark(rs.getString("remark").trim());
                acc.setSelfsign(rs.getString("selfsign").trim());
                acc.setStatus(1);//在线，修改对象状态为在线
                modifyStatus(acc.getQqCode(),1);//修改状态为在线，修改数据库中的状态
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return acc;
    }

    //修改状态status =0,1,2,3;
    public boolean modifyStatus (int qqcode,int status){
        boolean bok =false;
        Connection conn =new DBHelper().getConn();
        String sql ="update account set status = ? where qqcode = ?";
        try {
            PreparedStatement pstmt =conn.prepareStatement(sql);
            int i=1;
            pstmt.setInt(i++,status);
            pstmt.setInt(i++,qqcode);
            if(pstmt.executeUpdate() >0){
                bok =true;
            }
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bok;
    }
    //返回头像
    public String getFace(int qqcode){
        String face ="";
        Connection conn =new DBHelper().getConn();
        String sql ="select face from account where qqcode = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            int i =1;
            pstmt.setInt(i++,qqcode);
            ResultSet rs =pstmt.executeQuery();
            if(rs.next()){
                face =rs.getString("face").trim();
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return face;
    }
    //返回所有好友、家人、朋友、黑名单等资料。
    public Vector getAllinfo(Account acc){
        Connection conn =new DBHelper().getConn();
        Vector<Account> allInfo =new Vector<Account>(); //sql语句里面在查询外连接 sql语句可能有问题
        String sql ="select a.*, f.groupname from account a right outer join friend f on a.qqCode = f.friendAccount where f.selfAccount = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,acc.getQqCode());
            ResultSet rs =pstmt.executeQuery();
            while(rs.next()){
                Account a=new Account();//一条记录创建一个Account对象 放到集合中 集合有哪些？
                a.setQqCode(rs.getInt("qqCode"));
                a.setNickName(rs.getString("nickname").trim());
                a.setIpAddr(rs.getString("ipaddress").trim());
                a.setPort(rs.getInt("port"));
                a.setAge(rs.getInt("age"));
                a.setSex(rs.getString("sex").trim());
                a.setStar(rs.getString("star").trim());
                a.setFace(rs.getString("face").trim());
                a.setRemark(rs.getString("remark").trim());
                a.setSelfsign(rs.getString("selfsign").trim());
                a.setStatus(rs.getInt("status"));
                a.setGroupname(rs.getString("groupname").trim());
                allInfo.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allInfo;
    }
    public Vector<Account> find(Account acc){
        Connection conn=new DBHelper().getConn();
        Vector allInfo =new Vector();
        String sql="select * from account where sex = '"+acc.getSex() + "'";
        if(acc.getQqCode() !=0){
            sql += " and qqCode = " +acc.getQqCode();
        }if(acc.getNickName() != null && !acc.getNickName().equals(""))
            sql += " and nickname like '%" + acc.getNickName()+"%'";
        if(acc.getAge() != 0)
            sql +=" and age = " + acc.getAge();
        try {
            Statement stmt = conn.prepareStatement(sql);
            ResultSet rs =stmt.executeQuery(sql);
            while(rs.next()){
                Vector rowData =new Vector();
                rowData.addElement(rs.getInt("qqCode"));
                rowData.addElement(rs.getString("nickname").trim());
                rowData.addElement(rs.getInt("age"));
                rowData.addElement(rs.getString("sex").trim());
                rowData.addElement(rs.getString("area").trim());
                rowData.addElement(rs.getString("star").trim());
                rowData.addElement(rs.getString("face").trim());
                rowData.addElement(rs.getString("remark").trim());
                allInfo.add(rowData);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allInfo;
    }

    //查找个人资料
    public Account findPersonInfo(int qqCode){
        Connection conn =new DBHelper().getConn();
        Account acc =new Account();
        String sql ="select * from account where qqCode = ? ";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,qqCode);
            ResultSet rs =pstmt.executeQuery(); //取完数据 数据还在不在数据库
            if(rs.next()){
                acc.setQqCode(Integer.parseInt(rs.getString("qqCode").trim()));
                acc.setNickName(rs.getString("nickname"));
                acc.setIpAddr(rs.getString("ipAddress").trim());
                acc.setPort(rs.getInt("port"));
                acc.setAge(rs.getInt("age"));
                acc.setSex(rs.getString("sex").trim());
                acc.setArea(rs.getString("area").trim());
                acc.setStar(rs.getString("star").trim());
                acc.setFace(rs.getString("face").trim());
                acc.setRemark(rs.getString("remark").trim());
                acc.setSelfsign(rs.getString("selfsign").trim());
                acc.setStatus(rs.getInt("status"));
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return acc;
    }
    //添加好友
    public boolean addFriend(Account account,int friendcode){
        Connection conn =new DBHelper().getConn();
        String sql ="insert into friend(selfAccount,friendAccount,groupname,"+"invalid) values(?,?,?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, account.getQqCode());
            pstmt.setInt(2, friendcode);
            pstmt.setString(3, Cmd.F_FRIEND);
            pstmt.setInt(4, 0);
            pstmt.executeUpdate();
            pstmt.close();
            pstmt=conn.prepareStatement(sql);
            pstmt.setInt(1, friendcode);
            pstmt.setInt(2, account.getQqCode());
            pstmt.setString(3, Cmd.F_FRIEND);
            pstmt.setInt(4, 0);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

}
