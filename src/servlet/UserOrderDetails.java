package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import dao.TransactionDAO;
import util.Constants;
import util.ServletHelper;

/**
 * 用户请求订单的详细信息
 */
@WebServlet("/servlet/UserOrderDetails")
public class UserOrderDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserOrderDetails() {
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
		System.out.println("用户请求订单的详细信息");
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
		String orderId = rcvJsonObj.getString("orderId");
		JSONObject sndJsonObj = TransactionDAO.getTransactionDetails(orderId);
		boolean flag = ServletHelper.handleSndMessage(sndJsonObj, rcvJsonObj.getString("AESKey"),
				response.getOutputStream());
		if (!flag) {
			System.out.println("响应失败！");
		}
	}
}
