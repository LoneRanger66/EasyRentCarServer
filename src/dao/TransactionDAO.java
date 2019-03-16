package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.TransactionRecord;
import util.DBHelper;

/**
 * ���������ɾ��Ľ�����Ϣ�ȵľ�̬����
 * 
 * @author ZhaoYang
 *
 */
public class TransactionDAO {
	/**
	 * ���ӽ�����Ϣ
	 * 
	 * @param orderId
	 *            �������
	 * @param username
	 *            �û���
	 * @param carId
	 *            �������
	 * @param orderTime
	 *            �µ�ʱ��
	 * @param startTime
	 *            ��ʼʹ������ʱ��
	 * @param endTime
	 *            ����ʹ������ʱ��
	 * @param rent
	 *            ���
	 * @param status
	 *            ����״̬ 0��ʾ�������ڽ��� 1��ʾ�Ѹ��������� 2��ʾ����ȡ��
	 * @param AESKey
	 *            ����Կ��
	 */

	public static void addTransaction(String orderId, String username, String carId, String orderTime, String startTime,
			String endTime, double rent, int status, String AESKey) {
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		String sql = "insert into transaction(id,username,carId,orderTime,startTime,endTime,rent,status,AESKey) values(?,?,?,?,?,?,?,?,?)";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (orderTime != null) {
			orderTime = sdf.format(Long.parseLong(orderTime));
		}
		if (startTime != null) {
			startTime = sdf.format(Long.parseLong(startTime));
		}
		if (endTime != null) {
			endTime = sdf.format(Long.parseLong(endTime));
		}
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, orderId);
			preparedStatement.setString(2, username);
			preparedStatement.setString(3, carId);
			preparedStatement.setString(4, orderTime);
			preparedStatement.setString(5, startTime);
			preparedStatement.setString(6, endTime);
			preparedStatement.setDouble(7, rent);
			preparedStatement.setInt(8, status);
			preparedStatement.setString(9, AESKey);
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
	 * ���ݶ������ɾ��������Ϣ
	 * 
	 * @param orderId
	 *            �������
	 */
	public static void removeTransaction(String orderId) {
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		String sql = "delete from transaction where id=?";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, orderId);
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
	 * �����������ã������û��������Ĳ�����Ϣ
	 * 
	 * @param orderId
	 *            ������
	 * @return ���û���������������Ϣ
	 */
	public static TransactionRecord getTransaction(String orderId) {
		TransactionRecord tr = new TransactionRecord();
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String sql = "select * from transaction where id=?";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, orderId);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				tr.setOrderId(rs.getString("id"));
				tr.setUsername(rs.getString("username"));
				tr.setCarId(rs.getString("carId"));
				String orderTime = rs.getString("orderTime");
				String startTime = rs.getString("startTime");
				String endTime = rs.getString("endTime");
				tr.setOrderTime(orderTime == null ? null : orderTime.substring(0, 19));
				tr.setStartTime(startTime == null ? null : startTime.substring(0, 19));
				tr.setEndTime(endTime == null ? null : endTime.substring(0, 19));
				tr.setRent(rs.getDouble("rent"));
				tr.setStatus(rs.getInt("status"));
				tr.setKey(rs.getString("AESKey"));
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
		return tr;
	}

	/**
	 * �����������ã������û��������Ĳ�����Ϣ
	 * 
	 * @param carId
	 *            �������
	 * @return ���û���������������Ϣ
	 */
	public static TransactionRecord getTransactionByCarId(String carId) {
		TransactionRecord tr = new TransactionRecord();
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String sql = "select * from transaction where carId=? and status=0";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, carId);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				tr.setOrderId(rs.getString("id"));
				tr.setUsername(rs.getString("username"));
				tr.setCarId(rs.getString("carId"));
				String orderTime = rs.getString("orderTime");
				String startTime = rs.getString("startTime");
				String endTime = rs.getString("endTime");
				tr.setOrderTime(orderTime == null ? null : orderTime.substring(0, 19));
				tr.setStartTime(startTime == null ? null : startTime.substring(0, 19));
				tr.setEndTime(endTime == null ? null : endTime.substring(0, 19));
				tr.setRent(rs.getDouble("rent"));
				tr.setStatus(rs.getInt("status"));
				tr.setKey(rs.getString("AESKey"));
			} else {
				return null;
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
		return tr;
	}

	/**
	 * �����û�����Ķ�����ŷ�����Ӧ�Ľ�����Ϣ
	 * 
	 * @param orderId
	 *            �������
	 * @return ����������Ϣ��JSON����
	 */
	public static JSONObject getTransactionDetails(String orderId) {
		JSONObject jsonObject = null;
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String sql = "select transaction.id AS tid,car.id AS cid,car.name,transaction.orderTime,transaction.startTime,transaction.endTime,transaction.rent,transaction.status from car,transaction where transaction.id=? and transaction.carId=car.id;";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, orderId);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("orderId", rs.getString("tid"));
				jsonObject.put("carId", rs.getString("cid"));
				jsonObject.put("carName", rs.getString("name"));
				String orderTime = rs.getString("orderTime");
				String startTime = rs.getString("startTime");
				String endTime = rs.getString("endTime");
				jsonObject.put("orderTime", orderTime == null ? null : orderTime.substring(0, 19));
				jsonObject.put("startTime", startTime == null ? null : startTime.substring(0, 19));
				jsonObject.put("endTime", endTime == null ? null : endTime.substring(0, 19));
				jsonObject.put("rent", rs.getDouble("rent"));
				jsonObject.put("orderStatus", rs.getInt("status"));
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
	 * ���ݶ�������޸Ľ�����Ϣ
	 * 
	 * @param orderId
	 *            �������
	 * @param username
	 *            �û���
	 * @param carId
	 *            �������
	 * @param orderTime
	 *            �µ�ʱ��
	 * @param startTime
	 *            ��ʼʹ������ʱ��
	 * @param endTime
	 *            ����ʹ������ʱ��
	 * @param rent
	 *            ���
	 * @param status
	 *            ����״̬ 1��ʾ�Ѹ��������� 2��ʾ����ȡ��
	 */
	public static void modifyTransaction(String orderId, String username, String carId, String orderTime,
			String startTime, String endTime, double rent, int status) {
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		String sql = "update transaction set username=?,carId=?,orderTime=?,startTime=?,endTime=?,rent=?,status=? where id=?";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, carId);
			preparedStatement.setString(3, orderTime);
			preparedStatement.setString(4, startTime);
			preparedStatement.setString(5, endTime);
			preparedStatement.setDouble(6, rent);
			preparedStatement.setInt(7, status);
			preparedStatement.setString(8, orderId);
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
	 * ����û��Ƿ����������˵�����������ˣ�����true�������δ��ɵĶ���������false
	 * 
	 * @param username
	 *            �û���
	 * @return ����û������������˵�������true�������δ��ɵĶ���������false
	 */
	public static boolean isUserChecked(String username) {
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		boolean status = false;
		String sql = "select count(*) from transaction where username=? and status=0";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, username);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				if (rs.getInt(1) == 0) {
					status = true;
				}
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
		return status;
	}

	/**
	 * �õ��û����еĽ�����Ϣ������������š��������֡��µ�ʱ�䡢���Ͷ���״̬
	 * 
	 * @param username
	 *            �û���
	 * @return ����������Ϣ��JSON����
	 */
	public static JSONObject getUserAllTransaction(String username) {
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject tmp;
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String sql = "select transaction.id,car.name,transaction.orderTime,transaction.rent,transaction.status from car,transaction where transaction.username=? and transaction.carId=car.id";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, username);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				tmp = new JSONObject();
				tmp.put("orderId", rs.getString("id"));
				tmp.put("carName", rs.getString("name"));
				String orderTime = rs.getString("orderTime");
				tmp.put("orderTime", orderTime == null ? null : orderTime.substring(0, 19));
				tmp.put("rent", rs.getDouble("rent"));
				tmp.put("orderStatus", rs.getInt("status"));
				jsonArray.put(tmp);
			}
			jsonObject.put("order", jsonArray);
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
	 * ���ݶ��������ý�����Ϣ�Ŀ�ʼʱ��
	 * 
	 * @param orderId
	 *            ������
	 * @param startTime
	 *            ��ʼʱ��
	 */
	public static void setStartTime(String orderId, String startTime) {
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		String sql = "update transaction set startTime=? where id=?";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		startTime = sdf.format(Long.parseLong(startTime));
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, startTime);
			preparedStatement.setString(2, orderId);
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
	 * ���ݶ��������ý�����Ϣ�Ľ���ʱ��
	 * 
	 * @param orderId
	 *            ������
	 * @param endTime
	 *            ����ʱ��
	 */
	public static void setEndTime(String orderId, String endTime) {
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		String sql = "update transaction set endTime=? where id=?";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		endTime = sdf.format(Long.parseLong(endTime));
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, endTime);
			preparedStatement.setString(2, orderId);
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
	 * ���ݶ��������ý�����Ϣ�ķ���
	 * 
	 * @param orderId
	 *            ������
	 * @param rent
	 *            ����
	 */
	public static void setRent(String orderId, double rent) {
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		String sql = "update transaction set rent=? where id=?";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setDouble(1, rent);
			preparedStatement.setString(2, orderId);
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
	 * ���ݶ��������ý���״̬
	 * 
	 * @param orderId
	 *            ������
	 * @param status
	 *            ����״̬
	 */
	public static void setStatus(String orderId, int status) {
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		String sql = "update transaction set status=? where id=?";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, status);
			preparedStatement.setString(2, orderId);
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
	 * ���ݶ�������������Կ��
	 * 
	 * @param orderId
	 *            ������
	 * @param key
	 *            ����Կ��
	 */
	public static void setKey(String orderId, String key) {
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		String sql = "update transaction set AESKey=? where id=?";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, key);
			preparedStatement.setString(2, orderId);
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

}
