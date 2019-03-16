package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ���ݿ⸨�������࣬�����������ݿ�ͶϿ����ݿ�����
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
			System.out.println("û���ҵ���Ӧ������");
		}
	}

	/**
	 * ����ģʽ��ֻ����һ������
	 * 
	 * @return ���ݿ����Ӷ���
	 */
	public static Connection getConnection() {
		if (conn == null) {
			try {
				conn = DriverManager.getConnection(url, user, password);
			} catch (SQLException e) {
				System.out.println("�õ����ݿ�����ʧ�ܣ�");
			}
		}
		return conn;
	}

	/**
	 * �ر����ݿ�����
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
