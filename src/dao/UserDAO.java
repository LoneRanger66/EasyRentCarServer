package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

import util.DBHelper;

/**
 * ���������ɾ����û���Ϣ�ȵľ�̬����
 * 
 * @author ZhaoYang
 *
 */
public class UserDAO {
	/**
	 * �����û���Ϣ
	 * 
	 * @param username
	 *            �û���
	 * @param password
	 *            ����
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
	 * �����û���ɾ���û���Ϣ
	 * 
	 * @param username
	 *            �û���
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
	 * �����û����õ��û���������Ϣ
	 * 
	 * @param username
	 *            �û���
	 * @return �����û�������Ϣ��JSON����
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
	 * �����û����õ��û���Ǯ�����
	 * 
	 * @param username
	 *            �û���
	 * @return �����û�Ǯ������JSON����
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
	 * �����û�����ֵ�û�Ǯ�����
	 * 
	 * @param username
	 *            �û���
	 * @param chargeMoney
	 *            ��ֵ�Ľ��
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
	 * �����û��������û�Ǯ�����
	 * 
	 * @param username
	 *            �û���
	 * @param consumeMoney
	 *            ���ѽ��
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
	 * �����û����޸��û���Ϣ
	 * 
	 * @param username
	 *            �û���
	 * @param password
	 *            ����
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
	 * �����û����Ƿ����
	 * 
	 * @param username
	 *            �û���
	 * @return ������ڣ�����true�����򷵻�false
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
	 * �õ����û�������
	 * 
	 * @param username
	 *            �û���
	 * @return ���û�������
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
