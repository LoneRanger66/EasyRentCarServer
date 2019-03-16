package util;

/**
 * 常量类，不允许被继承
 * 
 * @author ZhaoYang
 *
 */
public final class Constants {
	/*
	 * 汽车的4个状态
	 */
	// 汽车可以被使用
	public static final int CAR_CAN_USE = 1;
	// 汽车已经被预订
	public static final int CAR_RESERVED = 2;
	// 汽车正在被使用
	public static final int CAR_BEING_USED = 3;
	// 汽车故障，无法使用
	public static final int CAR_BREAK_DOWN = 4;

	/*
	 * 用户的3个状态
	 */
	// 用户存在
	public static final int USER_ACCOUNT_EXIST = 1;
	// 用户不存在
	public static final int USER_ACCOUNT_NOT_EXIST = 2;
	// 用户密码错误
	public static final int USER_PASSWORD_WRONG = 3;

	/*
	 * 订单的3个状态
	 */
	// 交易正在进行
	public static final int TRANSACTION_IN_PROGRESS = 0;
	// 交易完成，用户已付款
	public static final int TRANSACTION_PAID = 1;
	// 交易取消
	public static final int TRANSACTION_ORDER_CANCELED = 2;

	/**
	 * 其它常量
	 */
	// windows下使用
	public static final String prefixWindows = "E:/EasyRentCarData/picture/";
	// linux下使用
	public static final String prefixLinux = "/var/lib/mysql/easy_rent_car_data/";
	// 时戳允许误差时间（单位：毫秒）
	public static final long TIME_OUT = 100 * 1000;
	// 字符编码
	public static final String CHARSET_NAME = "UTF-8";
	// AES key用字符串保存时的编码
	public static final String CHARSET_NAME_AESKEY = "ISO-8859-1";
	// 从下单到取消订单的免费时间（单位：毫秒）
	public static final long NO_CHARGE_TIME = 10 * 1000;

}
