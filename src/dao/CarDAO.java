package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import util.Constants;
import util.DBHelper;

/**
 * 本类包括增删查改汽车信息等的静态方法
 * 
 * @author ZhaoYang
 *
 */
public class CarDAO {

	/**
	 * 增加汽车信息
	 * 
	 * @param carId
	 *            汽车编号
	 * @param carName
	 *            汽车名字
	 * @param carPicture
	 *            汽车图片
	 * @param carRent
	 *            汽车租金（单位 元/小时）
	 * @param carStatus
	 *            汽车状态 1代表可用，2代表已被预订，3代表正在被使用，4代表故障
	 * @param carBluetoothAddress
	 *            汽车蓝牙地址
	 */
	public static void addCarInformation(String carId, String carName, String carPicture, double carRent, int carStatus,
			String carBluetoothAddress) {
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		String sql = "insert into car values(?,?,?,?,?,?,?)";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, carId);
			preparedStatement.setString(2, carName);
			preparedStatement.setString(3, carPicture);
			preparedStatement.setDouble(4, carRent);
			preparedStatement.setInt(5, carStatus);
			preparedStatement.setString(6, carBluetoothAddress);
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
	 * 根据汽车编号移除相应汽车信息
	 * 
	 * @param carId
	 *            汽车编号
	 */
	public static void removeCarInformation(String carId) {
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		String sql = "delete from car where id=?";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, carId);
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
	 * 根据汽车编号得到相应汽车的信息
	 * 
	 * @param carId
	 *            汽车编号
	 * @return 该汽车信息的JSON文件
	 */
	public static JSONObject getCarInformation(String carId) {
		JSONObject jsonObject = null;
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String sql = "select * from car where id=?";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, carId);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("carId", rs.getString("id"));
				jsonObject.put("carName", rs.getString("name"));
				jsonObject.put("carPicture", rs.getString("picture"));
				jsonObject.put("carRent", rs.getDouble("rent"));
				jsonObject.put("carStatus", rs.getInt("status"));
				jsonObject.put("carBluetoothAddress", rs.getString("bluetoothAddress"));
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
	 * 根据汽车的变化修改汽车信息
	 * 
	 * @param carId
	 *            汽车编号
	 * @param carName
	 *            汽车名字
	 * @param carPicture
	 *            汽车图片
	 * @param carRent
	 *            汽车租金（单位 元/小时）
	 * @param carStatus
	 *            汽车状态 1代表可用，2代表已被预订，3代表正在被使用，4代表故障
	 * @param carBluetoothAddress
	 *            汽车蓝牙地址
	 */
	public static void modifyCarInformation(String carId, String carName, String carPicture, double carRent,
			int carStatus, String carBluetoothAddress) {
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		String sql = "update car set name=?,picture=?,rent=?,status=?,blueToothAddress=? where id=?";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, carName);
			preparedStatement.setString(2, carPicture);
			preparedStatement.setDouble(3, carRent);
			preparedStatement.setInt(4, carStatus);
			preparedStatement.setString(5, carBluetoothAddress);
			preparedStatement.setString(6, carId);
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
	 * 得到当前可用的汽车信息，包括汽车的编号、名字、图片和租金
	 * 
	 * @return 包含上述信息的JSON对象
	 */
	public static JSONObject getAvailableCar() {
		JSONObject carJSONObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject tmp;
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String sql = "select id,name,picture,rent from car where status=" + Constants.CAR_CAN_USE;
		try {
			preparedStatement = conn.prepareStatement(sql);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				tmp = new JSONObject();
				tmp.put("carId", rs.getString("id"));
				tmp.put("carName", rs.getString("name"));
				tmp.put("carPicture", rs.getString("picture"));
				tmp.put("carRent", rs.getDouble("rent"));
				jsonArray.put(tmp);
			}
			carJSONObject.put("availableCar", jsonArray);
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
		return carJSONObject;
	}

	/**
	 * 得到汽车的状态
	 * 
	 * @param carId
	 *            汽车编号
	 * @return 汽车的状态 1表示可用，2表示已被预订，3表示正在被使用，4表示故障
	 */
	public static int getCarStatus(String carId) {
		int status = 0;
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String sql = "select status from car where id=?";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, carId);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				status = rs.getInt("status");
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
	 * 修改相应汽车的状态
	 * 
	 * @param carId
	 *            汽车编号
	 * @param status
	 *            汽车的状态 1表示可用，2表示已被预订，3表示正在被使用，4表示故障
	 */
	public static void modifyCarStatus(String carId, int status) {
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		String sql = "update car set status=? where id=?";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, status);
			preparedStatement.setString(2, carId);
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
	 * 得到相应汽车的租金（单位：元/小时）
	 * 
	 * @param carId
	 * @return 汽车的租金（单位：元/小时）
	 */
	public static double getCarRent(String carId) {
		double rent = 0;
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String sql = "select rent from car where id=?";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, carId);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				rent = rs.getDouble("rent");
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
		return rent;
	}

	/**
	 * 得到相应汽车的蓝牙地址
	 * 
	 * @param carId
	 *            汽车编号
	 * @return 汽车的蓝牙地址
	 */
	public static String getCarBluetoothAddress(String carId) {
		String bluetoothAddress = null;
		Connection conn = DBHelper.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String sql = "select bluetoothAddress from car where id=?";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, carId);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				bluetoothAddress = rs.getString("bluetoothAddress");
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
		return bluetoothAddress;
	}
}
