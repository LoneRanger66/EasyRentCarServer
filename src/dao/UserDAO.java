package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

import util.DBHelper;

/**
 * 本类包括增删查改用户信息等的静态方法
 * 
 * @author ZhaoYang
 *
 */
public class UserDAO {
	/**
	 * 增加用户信息
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 */
	public static void addUserInformation(String username, String password) {
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		String sql = "insert into user(username,password) values(?,?)";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (!preparedStatement.isClosed()) {
					preparedStatement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据用户名删除用户信息
	 * 
	 * @param username
	 *            用户名
	 */
	public static void removeUserInformation(String username) {
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		String sql = "delete from user where username=?";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, username);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (!preparedStatement.isClosed()) {
					preparedStatement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据用户名得到用户的所有信息
	 * 
	 * @param username
	 *            用户名
	 * @return 包含用户所有信息的JSON对象
	 */
	public static JSONObject getUserInformation(String username) {
		JSONObject jsonObject = null;
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String sql = "select * from user where username=?";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, username);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("username", rs.getString("username"));
				jsonObject.put("registerTime", rs.getString("registerTime").substring(0, 19));
				jsonObject.put("money", rs.getDouble("money"));
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
		return jsonObject;
	}

	/**
	 * 根据用户名得到用户的钱包金额
	 * 
	 * @param username
	 *            用户名
	 * @return 包含用户钱包金额的JSON对象
	 */
	public static double getUserMoney(String username) {
		double money = 0;
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String sql = "select money from user where username=?";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, username);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				money = rs.getDouble("money");
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
		return money;
	}

	/**
	 * 根据用户名充值用户钱包金额
	 * 
	 * @param username
	 *            用户名
	 * @param chargeMoney
	 *            充值的金额
	 */
	public static void chargeUserMoney(String username, double chargeMoney) {
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		String sql = "update user set money=money+? where username=?";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setDouble(1, chargeMoney);
			preparedStatement.setString(2, username);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (!preparedStatement.isClosed()) {
					preparedStatement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据用户名消费用户钱包金额
	 * 
	 * @param username
	 *            用户名
	 * @param consumeMoney
	 *            消费金额
	 */
	public static void consumeUserMoney(String username, double consumeMoney) {
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		String sql = "update user set money=money-? where username=?";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setDouble(1, consumeMoney);
			preparedStatement.setString(2, username);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (!preparedStatement.isClosed()) {
					preparedStatement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据用户名修改用户信息
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 */
	public static void modifyUserInformation(String username, String password) {
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		String sql = "update user set password=? where username=?";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, password);
			preparedStatement.setString(2, username);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (!preparedStatement.isClosed()) {
					preparedStatement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 检查该用户名是否存在
	 * 
	 * @param username
	 *            用户名
	 * @return 如果存在，返回true，否则返回false
	 */
	public static boolean verifyUserExist(String username) {
		int flag = 0;
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String sql = "select count(*) AS flag from user where username=?";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, username);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				flag = rs.getInt("flag");
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
		return (flag == 1) ? true : false;
	}

	/**
	 * 得到该用户的密码
	 * 
	 * @param username
	 *            用户名
	 * @return 该用户的密码
	 */
	public static String getPassword(String username) {
		String password = null;
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String sql = "select password from user where username=?";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, username);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				password = rs.getString("password");
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
		return password;
	}

}
