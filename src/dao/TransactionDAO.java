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
 * 本类包括增删查改交易信息等的静态方法
 * 
 * @author ZhaoYang
 *
 */
public class TransactionDAO {
	/**
	 * 增加交易信息
	 * 
	 * @param orderId
	 *            订单编号
	 * @param username
	 *            用户名
	 * @param carId
	 *            汽车编号
	 * @param orderTime
	 *            下单时间
	 * @param startTime
	 *            开始使用汽车时间
	 * @param endTime
	 *            结束使用汽车时间
	 * @param rent
	 *            租金
	 * @param status
	 *            交易状态 0表示交易正在进行 1表示已付款，交易完成 2表示订单取消
	 * @param AESKey
	 *            虚拟钥匙
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
	 * 根据订单编号删除交易信息
	 * 
	 * @param orderId
	 *            订单编号
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
	 * 供服务器调用，包含用户和汽车的部分信息
	 * 
	 * @param orderId
	 *            订单号
	 * @return 供用户和汽车交互的信息
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
	 * 供服务器调用，包含用户和汽车的部分信息
	 * 
	 * @param carId
	 *            汽车编号
	 * @return 供用户和汽车交互的信息
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
	 * 根据用户请求的订单编号返回相应的交易信息
	 * 
	 * @param orderId
	 *            订单编号
	 * @return 包含交易信息的JSON对象
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
	 * 根据订单编号修改交易信息
	 * 
	 * @param orderId
	 *            订单编号
	 * @param username
	 *            用户名
	 * @param carId
	 *            汽车编号
	 * @param orderTime
	 *            下单时间
	 * @param startTime
	 *            开始使用汽车时间
	 * @param endTime
	 *            结束使用汽车时间
	 * @param rent
	 *            租金
	 * @param status
	 *            交易状态 1表示已付款，交易完成 2表示订单取消
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
	 * 检查用户是否付清了所有账单。如果付清了，返回true；如果有未完成的订单，返回false
	 * 
	 * @param username
	 *            用户名
	 * @return 如果用户付清了所有账单，返回true；如果有未完成的订单，返回false
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
	 * 得到用户所有的交易信息，包括订单编号、汽车名字、下单时间、租金和订单状态
	 * 
	 * @param username
	 *            用户名
	 * @return 包含上述信息的JSON对象
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
	 * 根据订单号设置交易信息的开始时间
	 * 
	 * @param orderId
	 *            订单号
	 * @param startTime
	 *            开始时间
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
	 * 根据订单号设置交易信息的结束时间
	 * 
	 * @param orderId
	 *            订单号
	 * @param endTime
	 *            结束时间
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
	 * 根据订单号设置交易信息的费用
	 * 
	 * @param orderId
	 *            订单号
	 * @param rent
	 *            费用
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
	 * 根据订单号设置交易状态
	 * 
	 * @param orderId
	 *            订单号
	 * @param status
	 *            交易状态
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
	 * 根据订单号设置虚拟钥匙
	 * 
	 * @param orderId
	 *            订单号
	 * @param key
	 *            虚拟钥匙
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
