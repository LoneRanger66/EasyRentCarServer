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
 * servlet������ ����ʵ����servlet���еķ�����<br/>
 * 
 * @author ZhaoYang
 *
 */
public class ServletHelper {
	/**
	 * �������������ֽ����飬Ȼ��ѽ��յ����ֽ�������Base64���롢RSA���ܺ���֤SHA1ֵ��ʱ�����û���������<br/>
	 * 
	 * @param in
	 *            ������
	 * @return ��֤�ɹ��򷵻ؽ��յ���JSON������֤ʧ���򷵻�NULL
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
			System.out.println("IOʧ�ܣ�");
			return null;
		}
		byte[] decodeByte = Base64.getDecoder().decode(input);
		byte[] decryptByte = RSACoder.decryptByPrivateKey(decodeByte);
		// ��ȡ���ܺ��ֽ���������ʮ���ֽ�������ֽڣ���JSON�����byte�����ʾ
		byte[] jsonByte = Arrays.copyOfRange(decryptByte, 0, decryptByte.length - 20);
		// ��ȡ���ܺ��ֽ�����ĺ��ʮ���ֽڣ���SHA1ֵ
		byte[] rcvSHA1 = Arrays.copyOfRange(decryptByte, decryptByte.length - 20, decryptByte.length);
		// ��jsonByte����SHA1ֵ����
		byte[] calculatedSHA1 = SHA1Coder.encodeBySHA1(jsonByte);
		// �ȽϽ��յĺͼ����SHA1ֵ�Ƿ����
		if (!Arrays.equals(rcvSHA1, calculatedSHA1)) {
			System.out.println("SHA1��֤δͨ����");
			return null;
		}
		JSONObject rcvJsonObj = null;
		try {
			rcvJsonObj = new JSONObject(new String(jsonByte, Constants.CHARSET_NAME));
		} catch (JSONException | UnsupportedEncodingException e) {
			// �����ܷ����ַ�����֧�ֵ����
			System.out.println("���յ�JSON��ʽ����");
			return null;
		}
		// ��֤ʱ��
		String timestamp = rcvJsonObj.getString("timestamp");
		if (Math.abs(System.currentTimeMillis() - Long.parseLong(timestamp)) > Constants.TIME_OUT) {
			System.out.println("ʱ����֤δͨ��!");
			return null;
		}
		return rcvJsonObj;
	}

	/**
	 * ����û����������Ƿ���ȷ
	 * 
	 * @param rcvJsonObj
	 *            ���յ���JSON����
	 * @return ��ȷ�򷵻�true�������򷵻�false
	 */
	public static boolean verifyUsernameAndPassword(JSONObject rcvJsonObj) {
		// ��֤�û���
		String username = rcvJsonObj.getString("username");
		if (!UserDAO.verifyUserExist(username)) {
			System.out.println("�û������ڣ�");
			return false;
		}
		// ��֤����
		String password = rcvJsonObj.getString("password");
		if (!password.equals(UserDAO.getPassword(username))) {
			System.out.println("�������");
			return false;
		}
		return true;
	}

	/**
	 * ��ʱ����SHA1ֵ��ӵ���Ҫ���͵��ֽ������У�Ȼ�����AES���ܺ�Base64���룬������ݷ��͵������<br/>
	 * 
	 * @param sndJsonObj
	 *            Ҫ���͵�JSON����
	 * @param AESKeyStr
	 *            ���յ���AES��Կ
	 * @param out
	 *            �����
	 * @return ������ͳɹ�������true���������ʧ�ܣ�����false
	 */
	public static boolean handleSndMessage(JSONObject sndJsonObj, String AESKeyStr, OutputStream out) {
		// ���ʱ��
		sndJsonObj.put("timestamp", String.valueOf(System.currentTimeMillis()));
		byte[] jsonByte = null;
		try {
			jsonByte = sndJsonObj.toString().getBytes(Constants.CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			// ��Զ���ᷢ��
			e.printStackTrace();
		}
		byte[] SHA1 = SHA1Coder.encodeBySHA1(jsonByte);
		byte[] sndByte = new byte[jsonByte.length + SHA1.length];
		// �ϲ�jsonByte��SHA1�����ֽ����鵽sndByte��
		System.arraycopy(jsonByte, 0, sndByte, 0, jsonByte.length);
		System.arraycopy(SHA1, 0, sndByte, jsonByte.length, SHA1.length);
		// ʹ��AES��������
		byte[] encryptByte = null;
		try {
			encryptByte = AESCoder.encrypt(sndByte, AESKeyStr.getBytes(Constants.CHARSET_NAME_AESKEY));
		} catch (UnsupportedEncodingException e) {
			// ��Զ���ᷢ��
			e.printStackTrace();
		}
		// ʹ��Base64��������
		byte[] encodeByte = Base64.getEncoder().encode(encryptByte);
		// ��������
		try {
			out.write(encodeByte);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.println("�������дʧ�ܣ�");
			return false;
		}
		return true;
	}
}
