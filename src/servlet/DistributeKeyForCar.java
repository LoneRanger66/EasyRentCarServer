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
 * 为汽车分配虚拟钥匙
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
		System.out.println("汽车请求虚拟钥匙");
		// 以下三行设置编码，防止乱码
		request.setCharacterEncoding(Constants.CHARSET_NAME);
		response.setContentType("charset=" + Constants.CHARSET_NAME);
		response.setCharacterEncoding(Constants.CHARSET_NAME);
		JSONObject rcvJsonObj = ServletHelper.handleRcvMessage(request.getInputStream());
		if (rcvJsonObj == null) {
			System.out.println("接收到的数据不合法！");
			return;
		}
		String carId = rcvJsonObj.getString("carId");
		JSONObject sndJsonObj = new JSONObject();
		int carStatus = CarDAO.getCarStatus(carId);
		if (carStatus == Constants.CAR_RESERVED || carStatus == Constants.CAR_BEING_USED) {
			TransactionRecord tr = TransactionDAO.getTransactionByCarId(carId);
			sndJsonObj.put("status", true);
			sndJsonObj.put("key", tr.getKey());
			System.out.println("分配汽车密钥成功！");
		} else {
			System.out.println("分配汽车密钥失败！");
			sndJsonObj.put("status", false);
		}
		boolean flag = ServletHelper.handleSndMessage(sndJsonObj, rcvJsonObj.getString("AESKey"),
				response.getOutputStream());
		if (!flag) {
			System.out.println("响应失败！");
		}
	}
}
