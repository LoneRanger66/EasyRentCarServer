package servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.Constants;

/**
 * 用户请求汽车图片
 */
@WebServlet("/servlet/RequestCarPicture")
public class RequestCarPicture extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RequestCarPicture() {
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
		System.out.println("用户请求车辆图片");
		String charsetName = "UTF-8";
		// 以下三行设置编码，防止乱码
		request.setCharacterEncoding(charsetName);
		response.setContentType("charset=UTF-8");
		response.setCharacterEncoding(charsetName);

		InputStream in = request.getInputStream();
		int len;
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		while ((len = in.read(buffer)) != -1) {
			bout.write(buffer, 0, len);
		}
		byte[] rcvByte = bout.toByteArray();
		bout.close();
		String fileName = new String(rcvByte, charsetName);
		String prefix = null;
		if ("\\".equals(File.separator)) {
			prefix = Constants.prefixWindows;
		} else if ("/".equals(File.separator)) {
			prefix = Constants.prefixLinux;
		}
		File file = new File(prefix + fileName);
		FileInputStream fileInputStream = new FileInputStream(file);
		OutputStream out = response.getOutputStream();
		while ((len = fileInputStream.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
		fileInputStream.close();
		out.flush();
		out.close();
	}

}
