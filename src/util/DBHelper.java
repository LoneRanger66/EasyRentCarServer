package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库辅助连接类，用来连接数据库和断开数据库连接
 * 
 * @author ZhaoYang
 *
 */
public class DBHelper {
	private static final String url = "jdbc:mysql://127.0.0.1:3306/easy_rent_car?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false";
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String user = "root";
	private static final String password = "mysql";
	private static Connection conn = null;

	static {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			System.out.println("没有找到相应驱动！");
		}
	}

	/**
	 * 单例模式，只保留一个连接
	 * 
	 * @return 数据库连接对象
	 */
	public static Connection getConnection() {
		if (conn == null) {
			try {
				conn = DriverManager.getConnection(url, user, password);
			} catch (SQLException e) {
				System.out.println("得到数据库连接失败！");
			}
		}
		return conn;
	}

	/**
	 * 关闭数据库连接
	 */
	public static void closeConnection() {
		if (conn != null) {
			try {
				if (!conn.isClosed()) {
					conn.close();
				}
				conn = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
