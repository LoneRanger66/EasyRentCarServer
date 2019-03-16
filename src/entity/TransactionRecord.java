package entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import util.Constants;

/**
 * 存储订单id，用户的id，汽车的id，下单时间，使用汽车的起止时间，租金费用和虚拟钥匙
 * 
 * @author ZhaoYang
 *
 */
public class TransactionRecord {
	// 订单id
	private String orderId;
	// 用户id
	private String username;
	// 汽车id
	private String carId;
	/*
	 * 下订单时间，如果用户取消订单，则用orderTime计费 如果取消时间距离下单时间不到NO_CHARGE_TIME，则不计费
	 */
	private String orderTime;
	// 开始时间
	private String startTime;
	// 结束时间
	private String endTime;
	// 租金费用,单位：元/小时
	private double rent;
	// 交易状态
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
	 * 计算用户使用汽车的时间
	 * 
	 * @return 用户使用汽车的时间
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
