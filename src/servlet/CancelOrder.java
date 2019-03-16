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
 * 用户请求取消订单
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
		System.out.println("用户请求取消订单");
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
		// 得到要取消预订的汽车编号
		String carId = rcvJsonObj.getString("carId");
		if (CarDAO.getCarStatus(carId) != Constants.CAR_RESERVED) {
			System.out.println("汽车未被预订！");
			return;
		}
		TransactionRecord tr = TransactionDAO.getTransactionByCarId(carId);
		/*
		 * 置car的status为Constants.CAR_CAN_USE，置tr的结束时间为当前时间，
		 * 并检查结束时间和下单时间的差值是否小于NO_CHARGE_TIME。
		 * （1）如果小于等于NO_CHARGE_TIME，则不用付款，返回status为true;
		 * 服务器置tr的status为Constants.TRANSACTION_ORDER_CANCELED，
		 * 并将tr存入数据库的transaction表 （2）否则，用户需要付款，返回status为true，rent为服务器计算的rent；
		 * 服务器计算的rent存入tr
		 */
		JSONObject sndJsonObj = new JSONObject();
		sndJsonObj.put("status", true);
		// 修改汽车的状态为可用
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
			System.out.println("响应失败！");
		}
	}
}
