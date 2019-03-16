package servlet;

import java.io.IOException;
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
 * Ϊ������������Կ��
 */
@WebServlet("/servlet/DistributeKeyForCar")
public class DistributeKeyForCar extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DistributeKeyForCar() {
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
		System.out.println("������������Կ��");
		// �����������ñ��룬��ֹ����
		request.setCharacterEncoding(Constants.CHARSET_NAME);
		response.setContentType("charset=" + Constants.CHARSET_NAME);
		response.setCharacterEncoding(Constants.CHARSET_NAME);
		JSONObject rcvJsonObj = ServletHelper.handleRcvMessage(request.getInputStream());
		if (rcvJsonObj == null) {
			System.out.println("���յ������ݲ��Ϸ���");
			return;
		}
		String carId = rcvJsonObj.getString("carId");
		JSONObject sndJsonObj = new JSONObject();
		int carStatus = CarDAO.getCarStatus(carId);
		if (carStatus == Constants.CAR_RESERVED || carStatus == Constants.CAR_BEING_USED) {
			TransactionRecord tr = TransactionDAO.getTransactionByCarId(carId);
			sndJsonObj.put("status", true);
			sndJsonObj.put("key", tr.getKey());
			System.out.println("����������Կ�ɹ���");
		} else {
			System.out.println("����������Կʧ�ܣ�");
			sndJsonObj.put("status", false);
		}
		boolean flag = ServletHelper.handleSndMessage(sndJsonObj, rcvJsonObj.getString("AESKey"),
				response.getOutputStream());
		if (!flag) {
			System.out.println("��Ӧʧ�ܣ�");
		}
	}
}
