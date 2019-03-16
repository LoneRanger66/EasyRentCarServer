package servlet;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import dao.CarDAO;
import dao.TransactionDAO;
import entity.TransactionRecord;
import util.Constants;
import util.ServletHelper;

/**
 * �û�����ȡ������
 */
@WebServlet("/servlet/CancelOrder")
public class CancelOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CancelOrder() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("�û�����ȡ������");
		// �����������ñ��룬��ֹ����
		request.setCharacterEncoding(Constants.CHARSET_NAME);
		response.setContentType("charset=" + Constants.CHARSET_NAME);
		response.setCharacterEncoding(Constants.CHARSET_NAME);
		// ���������õ�JSON����
		JSONObject rcvJsonObj = ServletHelper.handleRcvMessage(request.getInputStream());
		if (rcvJsonObj == null) {
			System.out.println("���յ������ݲ��Ϸ���");
			return;
		}
		if (!ServletHelper.verifyUsernameAndPassword(rcvJsonObj)) {
			System.out.println("�û������������");
			return;
		}
		// �õ�Ҫȡ��Ԥ�����������
		String carId = rcvJsonObj.getString("carId");
		if (CarDAO.getCarStatus(carId) != Constants.CAR_RESERVED) {
			System.out.println("����δ��Ԥ����");
			return;
		}
		TransactionRecord tr = TransactionDAO.getTransactionByCarId(carId);
		/*
		 * ��car��statusΪConstants.CAR_CAN_USE����tr�Ľ���ʱ��Ϊ��ǰʱ�䣬
		 * ��������ʱ����µ�ʱ��Ĳ�ֵ�Ƿ�С��NO_CHARGE_TIME��
		 * ��1�����С�ڵ���NO_CHARGE_TIME�����ø������statusΪtrue;
		 * ��������tr��statusΪConstants.TRANSACTION_ORDER_CANCELED��
		 * ����tr�������ݿ��transaction�� ��2�������û���Ҫ�������statusΪtrue��rentΪ�����������rent��
		 * �����������rent����tr
		 */
		JSONObject sndJsonObj = new JSONObject();
		sndJsonObj.put("status", true);
		// �޸�������״̬Ϊ����
		CarDAO.modifyCarStatus(carId, Constants.CAR_CAN_USE);
		String orderId = tr.getOrderId();
		String endTime = String.valueOf(System.currentTimeMillis());
		TransactionDAO.setEndTime(orderId, endTime);
		long duration = tr.getDuration(Constants.TRANSACTION_ORDER_CANCELED);
		if (duration <= Constants.NO_CHARGE_TIME) {
			TransactionDAO.setStatus(orderId, Constants.TRANSACTION_ORDER_CANCELED);
		} else {
			DecimalFormat df = new DecimalFormat("#.###");
			df.setRoundingMode(RoundingMode.FLOOR);
			double rent = Double.valueOf(df.format(CarDAO.getCarRent(carId) / 3600 / 1000 * duration));
			sndJsonObj.put("rent", rent);
			tr.setRent(rent);
			TransactionDAO.setRent(orderId, rent);
		}
		boolean flag = ServletHelper.handleSndMessage(sndJsonObj, rcvJsonObj.getString("AESKey"),
				response.getOutputStream());
		if (!flag) {
			System.out.println("��Ӧʧ�ܣ�");
		}
	}
}
