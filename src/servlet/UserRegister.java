package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import dao.UserDAO;
import util.Constants;
import util.ServletHelper;

/**
 * �û�ע��
 */
@WebServlet("/servlet/UserRegister")
public class UserRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserRegister() {
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
		System.out.println("�û�ע��");
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
		boolean status;
		String username = rcvJsonObj.getString("username");
		// ��֤�û���
		if (UserDAO.verifyUserExist(username)) {
			System.out.println("�û��Ѵ��ڣ�");
			status = false;
		} else {
			String password = rcvJsonObj.getString("password");
			UserDAO.addUserInformation(username, password);
			status = true;
		}
		JSONObject sndJsonObj = new JSONObject();
		sndJsonObj.put("status", status);
		boolean flag = ServletHelper.handleSndMessage(sndJsonObj, rcvJsonObj.getString("AESKey"),
				response.getOutputStream());
		if (!flag) {
			System.out.println("��Ӧʧ�ܣ�");
		}
	}
}
