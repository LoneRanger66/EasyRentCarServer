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
 * ���������ɾ���������Ϣ�ȵľ�̬����
 * 
 * @author ZhaoYang
 *
 */
public class CarDAO {

	/**
	 * ����������Ϣ
	 * 
	 * @param carId
	 *            �������
	 * @param carName
	 *            ��������
	 * @param carPicture
	 *            ����ͼƬ
	 * @param carRent
	 *            ������𣨵�λ Ԫ/Сʱ��
	 * @param carStatus
	 *            ����״̬ 1������ã�2�����ѱ�Ԥ����3�������ڱ�ʹ�ã�4�������
	 * @param carBluetoothAddress
	 *            ����������ַ
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
	 * ������������Ƴ���Ӧ������Ϣ
	 * 
	 * @param carId
	 *            �������
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
	 * ����������ŵõ���Ӧ��������Ϣ
	 * 
	 * @param carId
	 *            �������
	 * @return ��������Ϣ��JSON�ļ�
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
	 * ���������ı仯�޸�������Ϣ
	 * 
	 * @param carId
	 *            �������
	 * @param carName
	 *            ��������
	 * @param carPicture
	 *            ����ͼƬ
	 * @param carRent
	 *            ������𣨵�λ Ԫ/Сʱ��
	 * @param carStatus
	 *            ����״̬ 1������ã�2�����ѱ�Ԥ����3�������ڱ�ʹ�ã�4�������
	 * @param carBluetoothAddress
	 *            ����������ַ
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
	 * �õ���ǰ���õ�������Ϣ�����������ı�š����֡�ͼƬ�����
	 * 
	 * @return ����������Ϣ��JSON����
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
	 * �õ�������״̬
	 * 
	 * @param carId
	 *            �������
	 * @return ������״̬ 1��ʾ���ã�2��ʾ�ѱ�Ԥ����3��ʾ���ڱ�ʹ�ã�4��ʾ����
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
	 * �޸���Ӧ������״̬
	 * 
	 * @param carId
	 *            �������
	 * @param status
	 *            ������״̬ 1��ʾ���ã�2��ʾ�ѱ�Ԥ����3��ʾ���ڱ�ʹ�ã�4��ʾ����
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
	 * �õ���Ӧ��������𣨵�λ��Ԫ/Сʱ��
	 * 
	 * @param carId
	 * @return ��������𣨵�λ��Ԫ/Сʱ��
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
	 * �õ���Ӧ������������ַ
	 * 
	 * @param carId
	 *            �������
	 * @return ������������ַ
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
