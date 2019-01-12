package cn.jijiking51.goods.admin.Order.commons;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Driver;

public class JdbcUtil {
	private static String driver = "com.mysql.jdbc.Driver";
	private static String passname = "root";
	private static String password  = "123456";
	private static String url = "jdbc:mysql://127.0.0.1:3306/goods";
	private  Connection con;
	public  void init() throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		con = DriverManager.getConnection(url, passname, password);
	}


	public  Connection createConnection() throws ClassNotFoundException, SQLException {

		if(con == null) {
			init();
		}
		return con;

	}


	public static void closeConnection(Connection con) {
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
