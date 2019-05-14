package com.coe.oom.core.base.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

/**
 * @author gyq 13-10-10下午4:35
 */
public final class StringUtil163 {

	private static final char[] HEX_LOOKUP_STRING = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private StringUtil163() {
	}

	/**
	 * 将字节数组转换为16进制字符串的形式.
	 */
	public static String bytesToHexStr(byte[] bcd) {
		StringBuffer s = new StringBuffer(bcd.length * 2);

		for (byte aBcd : bcd) {
			s.append(HEX_LOOKUP_STRING[(aBcd >>> 4) & 0x0f]);
			s.append(HEX_LOOKUP_STRING[aBcd & 0x0f]);
		}

		return s.toString();
	}

	/**
	 * 将16进制字符串还原为字节数组.
	 */
	public static byte[] hexStrToBytes(String s) {
		byte[] bytes;

		bytes = new byte[s.length() / 2];

		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
		}

		return bytes;
	}

	/**
	 * 
	 * 私钥解密
	 * 
	 * @param privateKey
	 *            私钥
	 * @param data
	 *            密文
	 * @param encode
	 *            编码方式
	 * @return
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws Exception
	 */
	public static String decrypt(PrivateKey privateKey, String data, String encode) throws Exception {
		Cipher cipher = null;
		cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] output = cipher.doFinal(Base64.decodeBase64(data));
		return new String(output, encode);
	}

}
