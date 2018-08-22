package gsxy.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBHelper {

    public static final String url = "jdbc:mysql://127.0.0.1:3306/qq";
    public static final String driver = "com.mysql.jdbc.Driver";
    public static final String user = "root";
    public static final String password = "123456";

    public Connection conn = null;
    public PreparedStatement pst = null;

    //静态代码块
    static{
        //加载驱动 静态代码块的好处：可以直接在类进行实例化运行时，
        // 就可以自动加载到内存中 进行执行
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public DBHelper(){}
    public DBHelper(String sql){
        try {
            conn = DriverManager.getConnection(url,user,password);      //获取连接
            pst = conn.prepareStatement(sql);  //进行sql语句的执行
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        try {
            conn = DriverManager.getConnection(url,user,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
     //关闭资源操作
    public void close(){
        try {
            this.pst.close();
            this.conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
