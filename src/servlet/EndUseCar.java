package servlet;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

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
 * �û�����ʹ������
 */
@WebServlet("/servlet/EndUseCar")
public class EndUseCar extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EndUseCar() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("�û�����ʹ������");
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
		String carId = rcvJsonObj.getString("carId");
		JSONObject sndJsonObj = new JSONObject();
		if (CarDAO.getCarStatus(carId) == Constants.CAR_BEING_USED) {
			CarDAO.modifyCarStatus(carId, Constants.CAR_CAN_USE);
			String endTime = rcvJsonObj.getString("endTime");
			TransactionRecord tr = TransactionDAO.getTransactionByCarId(carId);
			if (tr.getEndTime() == null) {
				TransactionDAO.setEndTime(tr.getOrderId(), endTime);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				endTime = sdf.format(Long.parseLong(endTime));
				tr.setEndTime(endTime);
				long duration = tr.getDuration(Constants.TRANSACTION_PAID);
				DecimalFormat df = new DecimalFormat("#.###");
				df.setRoundingMode(RoundingMode.FLOOR);
				double rent = Double.valueOf(df.format(CarDAO.getCarRent(carId) / 3600 / 1000 * duration));
				TransactionDAO.setRent(tr.getOrderId(), rent);
				sndJsonObj.put("status", true);
			} else {
				return;
			}
		} else {
			return;
		}
		boolean flag = ServletHelper.handleSndMessage(sndJsonObj, rcvJsonObj.getString("AESKey"),
				response.getOutputStream());
		if (!flag) {
			System.out.println("��Ӧʧ�ܣ�");
		}
	}
}
