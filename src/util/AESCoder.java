package util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES���ܺͽ���ģ��
 * 
 * @author ZhaoYang
 *
 */
public class AESCoder {
	// �ӽ����㷨
	private static final String KEY_ALGORITHM = "AES";
	// �ӽ��ܷ�ʽ
	private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

	/**
	 * ����AES��Կ
	 * 
	 * @return AES��Կ
	 */
	public static byte[] initKey() {
		KeyGenerator kg = null;
		try {
			kg = KeyGenerator.getInstance(KEY_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// ��ԿΪ128λ
		kg.init(128);
		SecretKey secretKey = kg.generateKey();
		return secretKey.getEncoded();
	}

	/**
	 * ����AES��Կ����Constants.CHARSET_NAME_AESKEY�ַ������ɵ��ַ�����ʾ
	 * 
	 * @return AES��Կ���ַ�����ʾ
	 */
	public static String initKeyStr() {
		String key = null;
		try {
			key = new String(initKey(), Constants.CHARSET_NAME_AESKEY);
		} catch (UnsupportedEncodingException e) {
			// �����ܷ���
			e.printStackTrace();
		}
		return key;
	}

	/**
	 * AES����
	 * 
	 * @param input
	 *            ��Ҫ���ܵ��ֽ�����
	 * @param key
	 *            AES��Կ
	 * @return ���ܺ���ֽ�����
	 */
	public static byte[] decrypt(byte[] input, byte[] key) {
		SecretKeySpec k = new SecretKeySpec(key, KEY_ALGORITHM);
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		try {
			cipher.init(Cipher.DECRYPT_MODE, k);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		try {
			return cipher.doFinal(input);
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * AES����
	 * 
	 * @param input
	 *            ��Ҫ���ܵ��ֽ�����
	 * @param key
	 *            AES��Կ
	 * @return ���ܺ���ֽ�����
	 */
	public static byte[] encrypt(byte[] input, byte[] key) {
		SecretKeySpec k = new SecretKeySpec(key, KEY_ALGORITHM);
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		try {
			cipher.init(Cipher.ENCRYPT_MODE, k);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		try {
			return cipher.doFinal(input);
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
