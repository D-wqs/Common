package com.stamper.yx.common.sys.security.AES;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author zhf_10@163.com
 * @Description
 * @date 2019/8/23 0023 15:53
 */
public class AesUtil {
	/**
	 * 算法名称
	 */
	private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
	private static final String SECRETKEY = "AES";


	/**
	 * content: 加密内容
	 * slatKey: 加密的盐，16位字符串
	 * vectorKey: 加密的向量，16位字符串
	 */
	public static String encrypt(String content, String slatKey) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM);//CBC(有向量模式)和ECB(无向量模式),传入“AES/CBC/NoPadding”可进行AES加密，传入"DESede/CBC/NoPadding"可进行DES3加密
		SecretKey secretKey = new SecretKeySpec(slatKey.getBytes(), SECRETKEY);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] encrypted = cipher.doFinal(content.getBytes("UTF-8"));
		return Base64.encodeBase64String(encrypted);
	}

	/**
	 * content: 解密内容(base64编码格式)
	 * slatKey: 加密时使用的盐，16位字符串
	 * vectorKey: 加密时使用的向量，16位字符串
	 */
	public static String decrypt(String base64Content, String slatKey) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM);//CBC(有向量模式)和ECB(无向量模式),传入“AES/CBC/NoPadding”可进行AES加密，传入"DESede/CBC/NoPadding"可进行DES3加密
		SecretKey secretKey = new SecretKeySpec(slatKey.getBytes(), SECRETKEY);
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] content = Base64.decodeBase64(base64Content);
		byte[] encrypted = cipher.doFinal(content);
		return new String(encrypted,"UTF-8");
	}

	public static void main(String[] args) throws Exception {
//
//		String slatKey = RandomUtil.getRandomABC(16);
//		String message = "你还是啥时候发件客户电视剧按季度将 就的撒回答  实打实";
//		long l = System.currentTimeMillis();
//		String encrypt = encrypt(message, "5975947220534742");
//		System.out.println("密文"+encrypt);
		String encrypt="dWC7KUh0WfscMg59hUcvIH0qUvNaFUONqvdHCoAI9cGfCyg0K/haHG21tNNWKm2IfUYeTAmbpH7aVXiv80XZvO99omfOsC8CfovY9XbiyvaU7Z7qdXaPMK5JC746/c5ngzlDZ4xBAV/O6BmnME6EvDXguV7nBETx7BjxeCX/y30=";
		System.out.println("长度"+encrypt.length());

		//String a1="I+SjF79+Ngx6JQpl9KUjZSIEPWq71TQeyOqsiOeo52Dh0/uXxWS2u1Yd0/zH7V6TdCMaHfpWebZlnuZkOH/67Ci+00hKcX3KW1dNElGjmZ6TgxJqj+ivZCAO/UkgyPXgQkGhhVzH/S1Ot24iRxhqmBx5FTqOSPXwGsxJKOmoLc445/yFHRArQ3Lg7gw04akB96hm5l0UG3MlEsX2enP2R0ykI3/QNJvgk3Zumi8B5XtRmp/2RMFbhwQCY/Ntyk4sbASIl/KMYiYQjMBTUEPnVFSjB+G85xFy+ws3S5peHb9wsa3C4qaqYxS73ZdulYCD/2NsASjyWNVzU4GfYkrv8jLM8a1lgZyiFUbjlu1aedLfpQoQcbceElQ0D1MD0Ir1oQMEaQCIBu603daFR5hSSHKx0Tv2fjvF9b0+bJER0BtD3oySVhNjpuvFzcmja5wOxfs85v5WEdArUX3aWVJ3iDdKKOezJ8iIlO9+EIGCYyXXVnk81jXSI6pMl7xDP383nRLIqohDEC5sI3jN5CHonfQN9Gxr7a17CGfWOJNqtOHj3NTHiIXKP/rjVIh6o/p/vc/EMQY73TPaIgH27qwfqdk9AdaJ7wfE2jSpDRXzgp0Qr9xvPV3kVWF+Hue367dfT3usTW/0L3LPdp9de1T3dazyVNUQD/uQLb5XzIQfoRNvF2OctEPZ6DXWMo7NBHqmbauUmwq9jx6D2xvw4Juum/Rj/COBt4ptub1wm6RoLWbfdLxJ8O5pDcWncouDSwfJjVm+O37EU5UGrvSQifbDPn2t64/MK54RqEgw94IOfi519rlyez0CYIiDdH6Wh5rirhp7anKQIqBtfZMBiWOd4+1Prt/h5+/TP9EWd5/xWSS4zQfhKzrHWSqXmLmxCr7WAsNCzajrEWPOzdlTp6zxu4WgmQhqyFS0Sh0qnNFF+lJ0kpz0PGcIASC3YIq/PRePFU+qpBhgwTz1f/P168O+I55bj5GtDQ0uS5aEyCqz/GPo2h8il56n5yTjcnYxXWn5Rin7b/HQiZA4j6h4G5HsqgOgxOxNqLNIh79Z/E8HxgGWKnjrdDT168VFqanJkvBkL+U3h7FFBswqpQ/rbJa5tZG3C1S+/zQ/jegn8fzOsZ+8+5jkhja0d3LnUPx25dvG85m/5XExHxLdq5hqqWj+n0oOtXxrtE39VHE0KzTrIK/jSICWz1pTFJY/ydK5ATli3XUJb/g7mq81t7YJHbBinxnTsZL7sTt0CLqnsqVLMtQdQwL7Gbea98oA2CQSfX/giC/ps6gzyc61BVLxj2v/DKnEx66i3ktFP2aqBcoA/xKnure2b27VFq5zMpnqba/PJ55zY/oA8R/muktaWG0aCOHIEoUCwffMRcBGQHMAMeFvUKoa4pXKIbehzbbr1HV5BGfmlqA6KRWN1hpG4sxy7A==";
		//String a2="I+SjF79+Ngx6JQpl9KUjZSIEPWq71TQeyOqsiOeo52Dh0/uXxWS2u1Yd0/zH7V6TdCMaHfpWebZlnuZkOH/67Ci+00hKcX3KW1dNElGjmZ6TgxJqj+ivZCAO/UkgyPXgQkGhhVzH/S1Ot24iRxhqmBx5FTqOSPXwGsxJKOmoLc445/yFHRArQ3Lg7gw04akB96hm5l0UG3MlEsX2enP2R0ykI3/QNJvgk3Zumi8B5XtRmp/2RMFbhwQCY/Ntyk4sbASIl/KMYiYQjMBTUEPnVFSjB+G85xFy+ws3S5peHb9wsa3C4qaqYxS73ZdulYCD/2NsASjyWNVzU4GfYkrv8jLM8a1lgZyiFUbjlu1aedLfpQoQcbceElQ0D1MD0Ir1oQMEaQCIBu603daFR5hSSHKx0Tv2fjvF9b0+bJER0BtD3oySVhNjpuvFzcmja5wOxfs85v5WEdArUX3aWVJ3iDdKKOezJ8iIlO9+EIGCYyXXVnk81jXSI6pMl7xDP383nRLIqohDEC5sI3jN5CHonfQN9Gxr7a17CGfWOJNqtOEM2YncISUNClideQqfliQY6NKHKOkvOp0N74T8bX9xvpClJWFFY6TJ5mjsWlBMV/xO3GE8qIry9WE18vOCNB8kLlKv6FHw7j1gY6FlIQvhTKzyVNUQD/uQLb5XzIQfoRNvF2OctEPZ6DXWMo7NBHqmbauUmwq9jx6D2xvw4Juum/Rj/COBt4ptub1wm6RoLWbfdLxJ8O5pDcWncouDSwfJjVm+O37EU5UGrvSQifbDPn2t64/MK54RqEgw94IOfi519rlyez0CYIiDdH6Wh5rirhp7anKQIqBtfZMBiWOd4+1Prt/h5+/TP9EWd5/xWSS4zQfhKzrHWSqXmLmxCr7WAsNCzajrEWPOzdlTp6zxu4WgmQhqyFS0Sh0qnNFF+lJ0kpz0PGcIASC3YIq/PRePFU+qpBhgwTz1f/P168O+I55bj5GtDQ0uS5aEyCqz/GPo2h8il56n5yTjcnYxXWn5Rin7b/HQiZA4j6h4G5HsqgOgxOxNqLNIh79Z/E8HxgEHPYIUHwRyUPxMwozbubeNykNZa57PXCe5T2Q6R5LYVx2CqyxInpS/dFlOxNXLu6bB/4Pxtn/gezEOLR1J8jcEpdLJMxVH4OJltfvsbkQVtEoOtXxrtE39VHE0KzTrIK/jSICWz1pTFJY/ydK5ATli3XUJb/g7mq81t7YJHbBinxnTsZL7sTt0CLqnsqVLMtQdQwL7Gbea98oA2CQSfX/giC/ps6gzyc61BVLxj2v/DKnEx66i3ktFP2aqBcoA/xKnure2b27VFq5zMpnqba/PJ55zY/oA8R/muktaWG0aCOHIEoUCwffMRcBGQHMAMeFvUKoa4pXKIbehzbbr1HV5BGfmlqA6KRWN1hpG4sxy7A==";

		String decrypt = decrypt(encrypt, "8142609499067095");
		System.out.println("明文："+decrypt);
		byte[] bytes1 = new BASE64Decoder().decodeBuffer(decrypt);
		System.out.println("编码："+new String(bytes1));
		byte[] bytes = new BASE64Decoder().decodeBuffer(decrypt);
//		System.out.println("运行耗时:" + (System.currentTimeMillis() - l) / 1000.0);

	}
}
