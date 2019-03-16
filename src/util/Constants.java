package util;

/**
 * �����࣬�������̳�
 * 
 * @author ZhaoYang
 *
 */
public final class Constants {
	/*
	 * ������4��״̬
	 */
	// �������Ա�ʹ��
	public static final int CAR_CAN_USE = 1;
	// �����Ѿ���Ԥ��
	public static final int CAR_RESERVED = 2;
	// �������ڱ�ʹ��
	public static final int CAR_BEING_USED = 3;
	// �������ϣ��޷�ʹ��
	public static final int CAR_BREAK_DOWN = 4;

	/*
	 * �û���3��״̬
	 */
	// �û�����
	public static final int USER_ACCOUNT_EXIST = 1;
	// �û�������
	public static final int USER_ACCOUNT_NOT_EXIST = 2;
	// �û��������
	public static final int USER_PASSWORD_WRONG = 3;

	/*
	 * ������3��״̬
	 */
	// �������ڽ���
	public static final int TRANSACTION_IN_PROGRESS = 0;
	// ������ɣ��û��Ѹ���
	public static final int TRANSACTION_PAID = 1;
	// ����ȡ��
	public static final int TRANSACTION_ORDER_CANCELED = 2;

	/**
	 * ��������
	 */
	// windows��ʹ��
	public static final String prefixWindows = "E:/EasyRentCarData/picture/";
	// linux��ʹ��
	public static final String prefixLinux = "/var/lib/mysql/easy_rent_car_data/";
	// ʱ���������ʱ�䣨��λ�����룩
	public static final long TIME_OUT = 100 * 1000;
	// �ַ�����
	public static final String CHARSET_NAME = "UTF-8";
	// AES key���ַ�������ʱ�ı���
	public static final String CHARSET_NAME_AESKEY = "ISO-8859-1";
	// ���µ���ȡ�����������ʱ�䣨��λ�����룩
	public static final long NO_CHARGE_TIME = 10 * 1000;

}
