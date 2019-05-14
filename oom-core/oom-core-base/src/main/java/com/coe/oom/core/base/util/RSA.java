package com.coe.oom.core.base.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


/**
 * RSA加密，公私钥、加密串等都是16进制编码
 * 
 * @author mamingli
 */
public class RSA {


	private static Logger logger=LoggerFactory.getLogger(RSA.class);

	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	private static final int MAX_ENCRYPT_BLOCK = 117;

	/** */
	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;

	/**
	 * 得到私钥对象
	 * 
	 * @param key
	 *            密钥字符串（经过16进制编码）
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String key) throws Exception {
		try {
			byte[] keyBytes = StringUtil163.hexStrToBytes(key.trim());
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return keyFactory.generatePrivate(keySpec);
		} catch (Exception e) {
			String info = "getPrivateKey failed: " + key + " | " + e.getMessage();
			logger.error(info, e);
			throw new Exception(info, e);
		}
	}

	/**
	 * 得到公钥对象
	 * 
	 * @param key
	 *            密钥字符串（经过16进制编码）
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String key) throws Exception {
		try {
			byte[] keyBytes = StringUtil163.hexStrToBytes(key.trim());
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
			String info = "getPublicKey failed: " + key + " | " + e.getMessage();
			logger.error(info, e);
			throw new Exception(info, e);
		}
	}

	/**
	 * 本方法使用SHA1withRSA签名算法产生签名
	 * 
	 * @param privateKey
	 *            privateKey 签名时使用的私钥(16进制编码)
	 * @param src
	 *            src 签名的原字符串
	 * @return String 签名的返回结果(16进制编码)。当产生签名出错的时候，返回null。
	 * @throws PayException
	 */
	public static String sign(PrivateKey privateKey, String src, String encode) throws Exception {
		try {
			Signature sigEng = Signature.getInstance(SIGN_ALGORITHMS);
			sigEng.initSign(privateKey);
			sigEng.update(src.getBytes(encode));
			byte[] signature = sigEng.sign();
			return StringUtil163.bytesToHexStr(signature);
		} catch (Exception e) {
			String info = "sign failed: " + src + " | " + e.getMessage();
			logger.error(info, e);
			throw new Exception(info, e);
		}
	}

	/**
	 * 本方法使用SHA1withRSA签名算法验证签名
	 * 
	 * @param publicKey
	 *            pubKey 验证签名时使用的公钥(16进制编码)
	 * @param sign
	 *            sign 签名结果(16进制编码)
	 * @param src
	 *            src 签名的原字符串
	 * @throws PayException
	 *             验证失败时抛出异常
	 */
	public static void verify(PublicKey publicKey, String sign, String src, String encode) throws Exception {
		try {
			if (StringUtils.isBlank(sign) || StringUtils.isBlank(src)) {
				throw new Exception("sign or src isBlank");
			}
			Signature sigEng = Signature.getInstance("SHA1withRSA");
			sigEng.initVerify(publicKey);
			sigEng.update(src.getBytes(encode));
			byte[] sign1 = StringUtil163.hexStrToBytes(sign);
			if (!sigEng.verify(sign1)) {
				throw new Exception("验证签名失败");
			}
		} catch (Exception e) {
			String info = "verify failed: " + sign + " | " + src + " | " + e.getMessage();
			logger.error(info, e);
			throw new Exception(info, e);
		}
	}

	/**
	 * 本方法用于产生1024位RSA公私钥对。
	 * 
	 * @return 私钥、公钥
	 */
	private static String[] genRSAKeyPair() {
		KeyPairGenerator rsaKeyGen = null;
		KeyPair rsaKeyPair = null;
		try {
			logger.error("Generating a pair of RSA key ... ");
			rsaKeyGen = KeyPairGenerator.getInstance("RSA");
			SecureRandom random = new SecureRandom();
			random.setSeed(("" + System.currentTimeMillis() * Math.random() * Math.random()).getBytes(Charset.forName("UTF-8")));
			rsaKeyGen.initialize(1024, random);
			rsaKeyPair = rsaKeyGen.genKeyPair();
			PublicKey rsaPublic = rsaKeyPair.getPublic();
			PrivateKey rsaPrivate = rsaKeyPair.getPrivate();

			String privateAndPublic[] = new String[2];
			privateAndPublic[0] = StringUtil163.bytesToHexStr(rsaPrivate.getEncoded());
			privateAndPublic[1] = StringUtil163.bytesToHexStr(rsaPublic.getEncoded());
			logger.error("私钥:" + privateAndPublic[0]);
			logger.error("公钥:" + privateAndPublic[1]);
			logger.error("1024-bit RSA key GENERATED.");

			return privateAndPublic;
		} catch (Exception e) {
			logger.error("genRSAKeyPair error：" + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 私钥解密
	 * 
	 * @param privateKey
	 *            私钥
	 * @param data
	 *            密文
	 * @param encode
	 *            编码方式
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(PrivateKey privateKey, String data, String encode) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] encryptedData = Base64.decodeBase64(data);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return new String(decryptedData, encode);
	}

	public static void main(String[] args) throws Exception {
		genRSAKeyPair();

		/*String privateKey = "30820276020100300d06092a864886f70d0101010500048202603082025c02010002818100b14d2a2e7b8e2d4c47cf9a4b090cb12393ee7ddc0a296f18c91bbfbf6105c13d1d763c1793c4e913ba4bd564b78d14cbe488055d8eed3751803e90d6c3e5218231b0f0a225ab364b021d6b4fff5125f05e9aeffef7afd0497e74c3e5f2e25a402d1b81c588e0e1cfc873a343722b27015cf078a3c50ab6d0407fcb594cf093b702030100010281803ec9edfb42244aa46df5f65546622945493937773a5e0c0b020582b55abdb7fe55b5ae78470b7fe34da15773f090d4e194101b9e53646ca7bf3be8fd35a8aa61a18bc36b157a0aba1881469627a5d8d1bd452c07800dd02af96663a7165e489b777dfebb8e5b222ff166dfe5bbd4646d9725914ed729d72859b9589315cb8029024100f655bab3e050b342f32250335ea0130e9f3abe43feaebb71efb150a76fcf0b588ba2849ec0b07b08aad010fd7d80bf4ef6570143f0329e39d5e27f59c78809ab024100b8420a465ecc81ba7831a3629f6f29c35617ebca9cd2444bda97951cd8fc4f7dfe4f09dcfd2ba9dcd6ba4ff6c8afd43ecc435c09788388f33ca7a9769c338a25024100e699d4a0e521c6deaacaf9774d62ded4365d8096188a2c7179a86d4f814cba5b56c47a103f64369923868a113376de20a65d4692f9fdf8bf3da9bdfc26dd03e7024026e218d4fcd05f580061493f58b1d6b85804b8478b8c7bc91e19fae7998ab523bb74e679f21b32bf5db8f782f69ad44964459ab88cec050c7f724a3ab37fd4d902406845feecb4a8a7e10d36fe940bbc9da8b977963b054f6c6ea945f7446967a2eaca8a8367d4954088198b35623ae5f812931e65c4973e1630744d91e955a4f187";
		String publicKey = "30819f300d06092a864886f70d010101050003818d0030818902818100b14d2a2e7b8e2d4c47cf9a4b090cb12393ee7ddc0a296f18c91bbfbf6105c13d1d763c1793c4e913ba4bd564b78d14cbe488055d8eed3751803e90d6c3e5218231b0f0a225ab364b021d6b4fff5125f05e9aeffef7afd0497e74c3e5f2e25a402d1b81c588e0e1cfc873a343722b27015cf078a3c50ab6d0407fcb594cf093b70203010001";
		// 签名
		String sign = sign(getPrivateKey(privateKey), "123ABC", "UTF-8");
		
		// 验证签名
		verify(getPublicKey(publicKey), sign, "123ABC", "UTF-8");

		String d = decrypt(getPrivateKey(privateKey), "KVO5xckumvlJ/MWrAd91D+6QdcqFWu3avO7SAVqDkDvRNNCOyhvThlyHnAD0ihMNPOvdPw7VjuuwDMaxX79Wf1HoS+lLNY7IoLGKMkvJNNqaKGHSK9bb+5jA8X5BVWpB3N/9ouQQlTimfcSijd4xyX1PTh/akN/zJjEARQo4MxQ=", "UTF-8");
		System.out.println(d);*/

	}

}
