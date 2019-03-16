package entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import util.Constants;

/**
 * �洢����id���û���id��������id���µ�ʱ�䣬ʹ����������ֹʱ�䣬�����ú�����Կ��
 * 
 * @author ZhaoYang
 *
 */
public class TransactionRecord {
	// ����id
	private String orderId;
	// �û�id
	private String username;
	// ����id
	private String carId;
	/*
	 * �¶���ʱ�䣬����û�ȡ������������orderTime�Ʒ� ���ȡ��ʱ������µ�ʱ�䲻��NO_CHARGE_TIME���򲻼Ʒ�
	 */
	private String orderTime;
	// ��ʼʱ��
	private String startTime;
	// ����ʱ��
	private String endTime;
	// ������,��λ��Ԫ/Сʱ
	private double rent;
	// ����״̬
	private int status;
	// key
	private String key;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public double getRent() {
		return rent;
	}

	public void setRent(double rent) {
		this.rent = rent;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * �����û�ʹ��������ʱ��
	 * 
	 * @return �û�ʹ��������ʱ��
	 */
	public long getDuration(int status) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long duration = 0;
		if (status == Constants.TRANSACTION_PAID) {
			try {
				duration = sdf.parse(getEndTime()).getTime() - sdf.parse(getStartTime()).getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if (status == Constants.TRANSACTION_ORDER_CANCELED) {
			try {
				duration = System.currentTimeMillis() - sdf.parse(getOrderTime()).getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return duration;
	}
}
