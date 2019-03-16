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
 * 服务器启动和关闭时自动执行的监听器 服务器启动时自动从数据库中配置订单编号 服务器关闭时自动关闭数据库连接
 * 
 * @author ZhaoYang
 *
 */
public class ServletContextListenerImp implements ServletContextListener {

	/**
	 * 服务器启动时自动从数据库中配置订单编号
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("自动执行Tomcat初始化工作...");
		System.out.println("从数据库中配置订单编号");
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
		System.out.println("当前订单编号被配置为：" + arg0.getServletContext().getAttribute("orderId"));
	}

	/**
	 * 服务器关闭时自动关闭数据库连接
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("自动执行Tomcat释放资源工作...");
		System.out.println("关闭数据库连接...");
		DBHelper.closeConnection();
		// 取消注册的驱动
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
