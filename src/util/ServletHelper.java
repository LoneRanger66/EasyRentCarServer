package util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import dao.UserDAO;

/**
 * servlet帮助类 本类实现了servlet共有的方法。<br/>
 * 
 * @author ZhaoYang
 *
 */
public class ServletHelper {
	/**
	 * 从输入流接收字节数组，然后把接收到的字节数组用Base64解码、RSA解密后，验证SHA1值、时戳、用户名和密码<br/>
	 * 
	 * @param in
	 *            输入流
	 * @return 验证成功则返回接收到的JSON对象，验证失败则返回NULL
	 */
	public static JSONObject handleRcvMessage(InputStream in) {
		int len;
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] input = null;
		try {
			while ((len = in.read(buffer)) != -1) {
				bout.write(buffer, 0, len);
			}
			input = bout.toByteArray();
			in.close();
			bout.close();
		} catch (IOException e) {
			System.out.println("IO失败！");
			return null;
		}
		byte[] decodeByte = Base64.getDecoder().decode(input);
		byte[] decryptByte = RSACoder.decryptByPrivateKey(decodeByte);
		// 截取解密后字节数组除后二十个字节以外的字节，即JSON对象的byte数组表示
		byte[] jsonByte = Arrays.copyOfRange(decryptByte, 0, decryptByte.length - 20);
		// 截取解密后字节数组的后二十个字节，即SHA1值
		byte[] rcvSHA1 = Arrays.copyOfRange(decryptByte, decryptByte.length - 20, decryptByte.length);
		// 对jsonByte进行SHA1值计算
		byte[] calculatedSHA1 = SHA1Coder.encodeBySHA1(jsonByte);
		// 比较接收的和计算的SHA1值是否相等
		if (!Arrays.equals(rcvSHA1, calculatedSHA1)) {
			System.out.println("SHA1验证未通过！");
			return null;
		}
		JSONObject rcvJsonObj = null;
		try {
			rcvJsonObj = new JSONObject(new String(jsonByte, Constants.CHARSET_NAME));
		} catch (JSONException | UnsupportedEncodingException e) {
			// 不可能发生字符集不支持的情况
			System.out.println("接收的JSON格式错误！");
			return null;
		}
		// 验证时戳
		String timestamp = rcvJsonObj.getString("timestamp");
		if (Math.abs(System.currentTimeMillis() - Long.parseLong(timestamp)) > Constants.TIME_OUT) {
			System.out.println("时戳验证未通过!");
			return null;
		}
		return rcvJsonObj;
	}

	/**
	 * 检查用户名和密码是否正确
	 * 
	 * @param rcvJsonObj
	 *            接收到的JSON对象
	 * @return 正确则返回true，错误则返回false
	 */
	public static boolean verifyUsernameAndPassword(JSONObject rcvJsonObj) {
		// 验证用户名
		String username = rcvJsonObj.getString("username");
		if (!UserDAO.verifyUserExist(username)) {
			System.out.println("用户不存在！");
			return false;
		}
		// 验证密码
		String password = rcvJsonObj.getString("password");
		if (!password.equals(UserDAO.getPassword(username))) {
			System.out.println("密码错误！");
			return false;
		}
		return true;
	}

	/**
	 * 把时戳和SHA1值添加到将要发送的字节数组中，然后进行AES加密和Base64编码，最后将数据发送到输出流<br/>
	 * 
	 * @param sndJsonObj
	 *            要发送的JSON对象
	 * @param AESKeyStr
	 *            接收到的AES密钥
	 * @param out
	 *            输出流
	 * @return 如果发送成功，返回true；如果发送失败，返回false
	 */
	public static boolean handleSndMessage(JSONObject sndJsonObj, String AESKeyStr, OutputStream out) {
		// 添加时戳
		sndJsonObj.put("timestamp", String.valueOf(System.currentTimeMillis()));
		byte[] jsonByte = null;
		try {
			jsonByte = sndJsonObj.toString().getBytes(Constants.CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			// 永远不会发生
			e.printStackTrace();
		}
		byte[] SHA1 = SHA1Coder.encodeBySHA1(jsonByte);
		byte[] sndByte = new byte[jsonByte.length + SHA1.length];
		// 合并jsonByte和SHA1两个字节数组到sndByte中
		System.arraycopy(jsonByte, 0, sndByte, 0, jsonByte.length);
		System.arraycopy(SHA1, 0, sndByte, jsonByte.length, SHA1.length);
		// 使用AES加密数据
		byte[] encryptByte = null;
		try {
			encryptByte = AESCoder.encrypt(sndByte, AESKeyStr.getBytes(Constants.CHARSET_NAME_AESKEY));
		} catch (UnsupportedEncodingException e) {
			// 永远不会发生
			e.printStackTrace();
		}
		// 使用Base64编码数据
		byte[] encodeByte = Base64.getEncoder().encode(encryptByte);
		// 发送数据
		try {
			out.write(encodeByte);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.println("向输出流写失败！");
			return false;
		}
		return true;
	}
}
