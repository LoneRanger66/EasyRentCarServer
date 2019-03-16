package util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * RSA解密模块
 * 
 * @author ZhaoYang
 *
 */
public class RSACoder {
	// 服务器的RSA私钥
	private static final byte[] PRIVATE_KEY = { 48, -126, 1, 85, 2, 1, 0, 48, 13, 6, 9, 42, -122, 72, -122, -9, 13, 1,
			1, 1, 5, 0, 4, -126, 1, 63, 48, -126, 1, 59, 2, 1, 0, 2, 65, 0, -111, -79, 67, -85, 108, 3, -32, -93, 61,
			-66, -28, -13, -119, -44, 16, -40, -33, 39, -35, -38, -103, -26, -94, 122, -83, 87, -2, 14, 99, 102, -6, 55,
			114, -12, -82, 50, 48, -45, -113, -75, -68, 121, 12, 81, -108, -128, -109, 11, 25, 67, 124, -112, 16, -128,
			111, 60, 118, -32, 108, -19, 4, -77, 79, 115, 2, 3, 1, 0, 1, 2, 64, 62, -50, -39, -86, 100, 5, -125, -23,
			59, -20, 56, -51, -104, -28, -104, 98, -74, 73, 124, -122, 10, 4, -8, -108, -60, 120, 3, -87, 118, 70, -27,
			-91, 43, -6, -87, -125, 31, -111, 58, -22, 27, -109, 67, -82, 112, 91, -114, -31, 14, -73, 34, -87, 44, 110,
			97, 66, -97, 26, -39, 59, -104, -78, 98, -31, 2, 33, 0, -37, -42, -89, 126, 76, -38, 54, -10, -98, -54, -85,
			-96, 121, -67, -81, -93, -121, 108, 52, 86, -85, -62, -68, 119, 18, -125, 97, -51, -112, 26, -69, -29, 2,
			33, 0, -87, -88, 86, 112, -7, 116, 83, -71, 68, 101, 55, -116, 111, 51, 92, 51, 53, -66, 76, 46, 88, -21,
			-93, -21, -117, 103, 41, 88, -90, 38, -109, 49, 2, 33, 0, -102, -53, 0, 80, -4, 72, 66, -97, 80, -100, 32,
			80, -12, -89, -51, -66, 117, -94, -119, 15, -26, 13, 78, -85, 1, 12, 37, -105, -102, -70, 88, 69, 2, 33, 0,
			-114, -31, 55, 83, 83, -78, 117, 36, 45, 6, -75, 30, 111, 52, 40, -20, -13, -48, -4, -27, 119, 123, -108, 4,
			53, -43, -66, -117, 106, -6, 71, -79, 2, 32, 37, 18, 72, 102, -14, 46, 16, 93, 95, 38, -46, 26, 110, -86,
			121, 83, 64, 114, 54, -9, 107, -24, -110, 98, -16, 41, -126, -94, 9, -109, -128, 35 };
	// 512位密钥最大解密 512/8=64位数据
	private static final int MAX_DECRYPT_BLOCK = 64;

	/**
	 * 用私钥解密数据
	 * 
	 * @param input
	 *            需要解密的字节数组
	 * @return 解密后的字节数组
	 */
	public static byte[] decryptByPrivateKey(byte[] input) {
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(PRIVATE_KEY);
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		PrivateKey privateKey = null;
		try {
			privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		try {
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		// 处理数据多于最大数据块的情况
		int length = input.length;
		int offset = 0;
		byte[] buffer = null;
		int i = 0;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		// 有数据需要处理作为循环判断的条件
		while (length - offset > 0) {
			// 剩余未处理数据大于最大数据块，从前往后处理最大数据块个字节
			if (length - offset > MAX_DECRYPT_BLOCK) {
				try {
					buffer = cipher.doFinal(input, offset, MAX_DECRYPT_BLOCK);
				} catch (IllegalBlockSizeException e) {
					e.printStackTrace();
				} catch (BadPaddingException e) {
					e.printStackTrace();
				}
			} else {
				// 剩余未处理数据小于最大数据块，处理剩余字节
				try {
					buffer = cipher.doFinal(input, offset, length - offset);
				} catch (IllegalBlockSizeException e) {
					e.printStackTrace();
				} catch (BadPaddingException e) {
					e.printStackTrace();
				}
			}
			try {
				byteArrayOutputStream.write(buffer);
			} catch (IOException e) {
				e.printStackTrace();
			}
			i++;
			offset = MAX_DECRYPT_BLOCK * i;
		}
		byte[] result = byteArrayOutputStream.toByteArray();
		try {
			byteArrayOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
