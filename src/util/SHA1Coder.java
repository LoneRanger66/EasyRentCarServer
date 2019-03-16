package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * ��Ϣ��������֤ģ�飨����SHA1ֵ��
 * 
 * @author ZhaoYang
 *
 */
public class SHA1Coder {
	/**
	 * �������ݵ�SHA1ֵ
	 * 
	 * @param input
	 *            ��Ҫ����SHA1ֵ���ֽ�����
	 * @return SHA1ֵ�����ֽ������ʾ
	 */
	public static byte[] encodeBySHA1(byte[] input) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			// ��Զ���ᷢ��
			e.printStackTrace();
		}
		return messageDigest.digest(input);
	}
}
