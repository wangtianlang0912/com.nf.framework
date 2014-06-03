/**
 * AES_ForAndroid.java
 * com.Apricotforest
 * 工程：tabVertical
 * 功能： TODO 
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-3-6       下午02:55:24
 * Copyright (c) 2012, TNT All Rights Reserved.
*/

package com.nf.framework.netdata;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.nf.framework.base64.BASE64Decoder;
import com.nf.framework.base64.BASE64Encoder;

public class AES_ForAndroid {
	static int AES_PasswordLength=32;

	/**
	 * 加密
	 * 
	 * @param content
	 *            需要加密的内容
	 * @param password
	 *            加密密码
	 * @return
	 */
	public static byte[] encrypt(byte[] byteContent, String password)
			 {
		byte[] crypted = null;
		byte[] passwordBytes= new byte[AES_PasswordLength];
		
		try {
		  
			byte[] passwordSizeAbleBytes=password.getBytes("utf-8");
		//	int size= (passwordSizeAbleBytes.length>32)?32:passwordSizeAbleBytes.length;
			System.arraycopy(passwordSizeAbleBytes, 0, passwordBytes, 0, AES_PasswordLength);
			SecretKeySpec skey = new SecretKeySpec(passwordBytes,
					"AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skey);
			crypted = cipher.doFinal(byteContent);

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return crypted;
	}

	// 2.2 解密
	//
	// 代码有详细注释，不多废话
	//
	// 注意：解密的时候要传入byte数组

	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @param password
	 *            解密密钥
	 * @return
	 */
	
	//kCCOptionPKCS7Padding|kCCOptionECBMode 
	//之间 
	//AES/ECB/PKCS5Padding
	public static byte[] decrypt(byte[] byteContent, String password) {
		byte[] crypted = null;
		byte[] passwordBytes= new byte[AES_PasswordLength];
		
		try {
			 
			byte[] passwordSizeAbleBytes= password.getBytes("utf-8");
			//int size= (passwordSizeAbleBytes.length>32)?32:passwordSizeAbleBytes.length;
			System.arraycopy(passwordSizeAbleBytes, 0, passwordBytes, 0, AES_PasswordLength);
			SecretKeySpec skey = new SecretKeySpec(passwordBytes,
					"AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skey);
			crypted = cipher.doFinal(byteContent);

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return crypted;
	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * 
	 * @return
	 */

	public static String parseByte2HexStr(byte buf[]) {

		StringBuffer sb = new StringBuffer();
		int len=buf.length;
		for (int i = 0; i < len; i++) {

			String hex = Integer.toHexString(buf[i] & 0xFF);

			if (hex.length() == 1) {

				hex = '0' + hex;

			}

			sb.append(hex.toUpperCase());

		}

		return sb.toString();

	}
	/**
	 * 将byte[]转换为string
	 * @param buff
	 * @return
	 */
		public static String parseByte2Base64Str(byte[] buff) {
			String base64 = "";
			BASE64Encoder encode = new BASE64Encoder();
			// 
			base64 = encode.encode(buff);
			return base64;

		}
	/**
	 * 将base64转换为byte[]
	 * @param base64
	 * @return
	 */
		public static byte[] parseBase64Str2Byte(String base64) {
			byte[] buff = new byte[0];
			BASE64Decoder decode = new BASE64Decoder();
			// 将base64转换为byte[]
			try {
				buff = decode.decodeBuffer(base64);

			} catch (IOException e) {
				e.printStackTrace();
			}

			return buff;

		}
	
/**
 *Base64 编码   buff——》String
 * @param buff  
 * @return
 */
//	public static String Base64parseByte2Base64Str(byte[] buff) {
//		String base64 = "";
//		// 将base64转换为byte[]
//		  base64 = Base64.encode(buff, 0, buff.length);
//		return base64;
//
//	}
	/** 
	 * 将String转换为byte[]
	 * @param base64
	 * @return
	 */
//	public static byte[] Base64parseBase64Str2Byte(String base64) {
//		byte[] buff = new byte[0];
//	//	base64=	 URLEncoder.encode(base64);// 编码
//		try {
//			buff = Base64.decode(base64);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return buff;
//
//	}

	// 2.4.2 将16进制转换为二进制

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * 
	 * @return
	 */

	public static byte[] parseHexStr2Byte(String hexStr) {

		if (hexStr.length() < 1)

			return null;

		byte[] result = new byte[hexStr.length() / 2];

		for (int i = 0; i < hexStr.length() / 2; i++) {

			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);

			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);

			result[i] = (byte) (high * 16 + low);

		}

		return result;

	}

}
