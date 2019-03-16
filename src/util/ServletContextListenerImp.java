package util;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * �����������͹ر�ʱ�Զ�ִ�еļ����� ����������ʱ�Զ������ݿ������ö������ �������ر�ʱ�Զ��ر����ݿ�����
 * 
 * @author ZhaoYang
 *
 */
public class ServletContextListenerImp implements ServletContextListener {

	/**
	 * ����������ʱ�Զ������ݿ������ö������
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("�Զ�ִ��Tomcat��ʼ������...");
		System.out.println("�����ݿ������ö������");
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String sql = "select max(cast(id AS unsigned)) AS orderId from transaction;";
		try {
			preparedStatement = conn.prepareStatement(sql);
			rs = preparedStatement.executeQuery();
			ServletContext application = arg0.getServletContext();
			if (rs.next()) {
				application.setAttribute("orderId", String.valueOf(rs.getInt("orderId") + 1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (!rs.isClosed()) {
					rs.close();
				}
				if (!preparedStatement.isClosed()) {
					preparedStatement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out.println("��ǰ������ű�����Ϊ��" + arg0.getServletContext().getAttribute("orderId"));
	}

	/**
	 * �������ر�ʱ�Զ��ر����ݿ�����
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("�Զ�ִ��Tomcat�ͷ���Դ����...");
		System.out.println("�ر����ݿ�����...");
		DBHelper.closeConnection();
		// ȡ��ע�������
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			try {
				DriverManager.deregisterDriver(drivers.nextElement());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
