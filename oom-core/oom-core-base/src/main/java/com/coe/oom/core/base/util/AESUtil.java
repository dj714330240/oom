package com.coe.oom.core.base.util;

import org.apache.xmlbeans.impl.util.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class AESUtil {
	private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

	private static MessageDigest md5 = null;
	static {
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/*
	 * 加密 1.构造密钥生成器 2.根据ecnodeRules规则初始化密钥生成器 3.产生密钥 4.创建和初始化密码器 5.内容加密 6.返回字符串
	 */
	public static String encode(String content, String encodeRules) {
		try {
			// SecretKey key=new SecretKeySpec(raw, "AES");
			SecretKeySpec key = new SecretKeySpec(Arrays.copyOf(encodeRules.getBytes("utf-8"), 16), "AES");

			// 6.根据指定算法AES自成密码器
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			// 7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
			cipher.init(Cipher.ENCRYPT_MODE, key);
			// 8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
			byte[] byte_encode = content.getBytes("utf-8");
			// 9.根据密码器的初始化方式--加密：将数据加密
			byte[] byte_AES = cipher.doFinal(byte_encode);
			// 10.将加密后的数据转换为字符串
			// 这里用Base64Encoder中会找不到包
			// 解决办法：
			// 在项目的Build path中先移除JRE System Library，再添加库JRE System
			// Library，重新编译后就一切正常了。
			String AES_encode = new String(new BASE64Encoder().encode(byte_AES));
			// 11.将字符串返回
			return AES_encode;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 如果有错就返加nulll
		return null;
	}

	/*
	 * 解密 解密过程： 1.同加密1-4步 2.将加密后的字符串反纺成byte[]数组 3.将加密内容解密
	 */
	public static String decode(String content, String encodeRules) {
		try {
			// SecretKey key=new SecretKeySpec(raw, "AES");
			SecretKeySpec key = new SecretKeySpec(Arrays.copyOf(encodeRules.getBytes("utf-8"), 16), "AES");
			// 6.根据指定算法AES自成密码器
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			// 7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
			cipher.init(Cipher.DECRYPT_MODE, key);
			// 8.将加密并编码后的内容解码成字节数组
			byte[] byte_content = new BASE64Decoder().decodeBuffer(content);
			/*
			 * 解密
			 */
			byte[] byte_decode = cipher.doFinal(byte_content);
			String AES_decode = new String(byte_decode, "utf-8");
			return AES_decode;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 如果有错就返加nulll
		return null;
	}

	/**
	 * 用于获取一个String的md5值
	 * 
	 * @param string
	 * @return
	 */
	public static String getMd5(String str) {
		byte[] bs = md5.digest(str.getBytes());
		StringBuilder sb = new StringBuilder(40);
		for (byte x : bs) {
			if ((x & 0xff) >> 4 == 0) {
				sb.append("0").append(Integer.toHexString(x & 0xff));
			} else {
				sb.append(Integer.toHexString(x & 0xff));
			}
		}
		return sb.toString();
	}

	public static String getMd5Uppercase(String str) {
		byte[] bs = md5.digest(str.getBytes());
		StringBuilder sb = new StringBuilder(40);
		for (byte x : bs) {
			if ((x & 0xff) >> 4 == 0) {
				sb.append("0").append(Integer.toHexString(x & 0xff));
			} else {
				sb.append(Integer.toHexString(x & 0xff));
			}
		}
		return sb.toString().toUpperCase();
	}

	public static void main(String[] args) {
		// String result=encode("asdfasdf", "123456");
		System.out.println(StringUtil.sort("{'asddf':'assd','saddff':{'ddsv':'ppo'}}"));
	}

	public static String encrypt(String sSrc, String vector, String secretkey) throws Exception {
		if (secretkey == null) {
			return null;
		}

		Cipher cipher = Cipher.getInstance(ALGORITHM);
		int blockSize = cipher.getBlockSize();

		byte[] dataBytes = sSrc.getBytes();
		int plaintextLength = dataBytes.length;
		if (plaintextLength % blockSize != 0) {
			plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
		}

		byte[] plaintext = new byte[plaintextLength];
		System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

		SecretKeySpec keyspec = new SecretKeySpec(secretkey.getBytes(), "AES");
		IvParameterSpec ivspec = new IvParameterSpec(vector.getBytes());

		cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
		byte[] encrypted = cipher.doFinal(plaintext);

		return new String(Base64.encode(encrypted));// Use BASE64 to transcode
	}

	/**
	 * Aes decryption
	 *
	 * @param sSrc
	 *            String to be decrypted
	 * @param vector
	 *            hexadecimal
	 * @param secretkey
	 *            hexadecimal
	 * @return Decrypted string
	 * @throws Exception
	 */
	public static String decrypt(String sSrc, String vector, String secretkey) throws Exception {
		try {
			// Check secretkey
			if (secretkey == null) {
				return null;
			}

			try {
				byte[] encrypted1 = Base64.decode(sSrc.getBytes());
				Cipher cipher = Cipher.getInstance(ALGORITHM);
				SecretKeySpec keyspec = new SecretKeySpec(secretkey.getBytes(), "AES");
				IvParameterSpec ivspec = new IvParameterSpec(vector.getBytes());

				cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

				byte[] original = cipher.doFinal(encrypted1);
				String originalString = new String(original);
				return originalString;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Transform string into hexadecimal
	 * 
	 * @param s
	 * @return
	 */
	public static String toHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = Integer.valueOf(s.charAt(i)).intValue();
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		return str.toUpperCase();
	}

	public static String md5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}

			return buf.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
}
