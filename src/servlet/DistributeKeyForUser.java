package servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import dao.CarDAO;
import dao.TransactionDAO;
import util.AESCoder;
import util.Constants;

import util.ServletHelper;

/**
 * 为用户分配虚拟钥匙
 */
@WebServlet("/servlet/DistributeKeyForUser")
public class DistributeKeyForUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DistributeKeyForUser() {
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
		System.out.println("用户请求虚拟钥匙");
		// 以下三行设置编码，防止乱码
		request.setCharacterEncoding(Constants.CHARSET_NAME);
		response.setContentType("charset=" + Constants.CHARSET_NAME);
		response.setCharacterEncoding(Constants.CHARSET_NAME);
		// 从输入流得到JSON对象
		JSONObject rcvJsonObj = ServletHelper.handleRcvMessage(request.getInputStream());
		if (rcvJsonObj == null) {
			System.out.println("接收到的数据不合法！");
			return;
		}
		if (!ServletHelper.verifyUsernameAndPassword(rcvJsonObj)) {
			System.out.println("用户名或密码错误！");
			return;
		}
		String carId = rcvJsonObj.getString("carId");
		JSONObject sndJsonObj = new JSONObject();
		if (TransactionDAO.isUserChecked(rcvJsonObj.getString("username"))) {
			boolean canUse;
			String orderId = null;
			synchronized (this) {
				canUse = CarDAO.getCarStatus(carId) == Constants.CAR_CAN_USE;
				if (canUse) {
					CarDAO.modifyCarStatus(carId, Constants.CAR_RESERVED);
					ServletContext application = this.getServletContext();
					orderId = (String) application.getAttribute("orderId");
					application.setAttribute("orderId", String.valueOf(Integer.parseInt(orderId) + 1));
				}
			}
			if (canUse) {
				String key = AESCoder.initKeyStr();
				TransactionDAO.addTransaction(orderId, rcvJsonObj.getString("username"), carId,
						String.valueOf(System.currentTimeMillis()), null, null, 0, Constants.TRANSACTION_IN_PROGRESS,
						key);
				sndJsonObj.put("status", 1);
				sndJsonObj.put("carBluetoothAddress", CarDAO.getCarBluetoothAddress(carId));
				sndJsonObj.put("key", key);
			} else {
				sndJsonObj.put("status", 3);
			}
		} else {
			sndJsonObj.put("status", 2);
		}
		boolean flag = ServletHelper.handleSndMessage(sndJsonObj, rcvJsonObj.getString("AESKey"),
				response.getOutputStream());
		if (!flag) {
			System.out.println("响应失败！");
		}
	}
}
