package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import dao.TransactionDAO;
import dao.UserDAO;
import entity.TransactionRecord;
import util.Constants;
import util.ServletHelper;

/**
 * 用户付款
 */
@WebServlet("/servlet/Pay")
public class Pay extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Pay() {
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
		System.out.println("用户请求付款");
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
		String username = rcvJsonObj.getString("username");
		String carId = rcvJsonObj.getString("carId");
		double rcvRent = rcvJsonObj.getDouble("rent");
		JSONObject sndJsonObj = new JSONObject();
		TransactionRecord tr = TransactionDAO.getTransactionByCarId(carId);
		if (tr == null) {
			System.out.println("该汽车不存在未付款的订单！");
			return;
		}
		if (rcvRent != tr.getRent()) {
			System.out.println("应付金额错误！");
			return;
		}
		double money = UserDAO.getUserMoney(username);
		if (rcvRent <= money) {
			synchronized (this) {
				String orderId = tr.getOrderId();
				TransactionDAO.setStatus(orderId, Constants.TRANSACTION_PAID);
				UserDAO.consumeUserMoney(username, rcvRent);
			}
			sndJsonObj.put("status", true);
		} else {
			sndJsonObj.put("status", false);
		}
		boolean flag = ServletHelper.handleSndMessage(sndJsonObj, rcvJsonObj.getString("AESKey"),
				response.getOutputStream());
		if (!flag) {
			System.out.println("响应失败！");
		}
	}
}
