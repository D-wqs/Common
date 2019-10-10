package com.stamper.yx.common.sys.security.md5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 与Go项目中的加密方式统一:md5(md5($pass))
 */
public class MD5 {
	public static String toMD5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			md.reset();
			b = md.digest(b);//主要的一步，注释掉这一步就是正常的MD5加密
			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0){
					i += 256;
				}
				if (i < 16){
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			//32位加密
			return buf.toString();
			// 16位的加密
			//return buf.toString().substring(8, 24);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		String s = MD5.toMD5("123456");
		System.out.println(s);
	}
}
