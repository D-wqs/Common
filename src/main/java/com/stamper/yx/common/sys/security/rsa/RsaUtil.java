package com.stamper.yx.common.sys.security.rsa;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhf_10@163.com
 * @Description Rsa加解密工具类
 * @date 2019/1/23 0023 18:59
 */
public class RsaUtil {

	/**
	 * 算法名称
	 */
	private static final String ALGORITHM = "RSA";

	/**
	 * 密钥长度
	 */
	private static final int KEY_SIZE = 2048;

	public static final byte[] DEFAULT_SPLIT = "#PART#".getBytes();    // 当要加密的内容超过bufferSize，则采用partSplit进行分块加密
	public static final int DEFAULT_BUFFERSIZE = (KEY_SIZE / 8) - 11;// 当前秘钥支持加密的最大字节数

	/**
	 * 随机生成密钥对（包含公钥和私钥）
	 */
	public static KeyPair generateKeyPair() throws Exception {
		// 获取指定算法的密钥对生成器
		KeyPairGenerator gen = KeyPairGenerator.getInstance(ALGORITHM);
		// 初始化密钥对生成器（指定密钥长度, 使用默认的安全随机数源）
		gen.initialize(KEY_SIZE);
		// 随机生成一对密钥（包含公钥和私钥）
		return gen.generateKeyPair();
	}

	/**
	 * 将 公钥/私钥 编码后以 Base64 的格式保存到指定文件
	 */
	public static void saveKeyForEncodedBase64(Key key, File keyFile) throws IOException {
		// 获取密钥编码后的格式
		byte[] encBytes = key.getEncoded();
		// 转换为 Base64 文本
		String encBase64 = new BASE64Encoder().encode(encBytes);
		// 保存到文件
		IOUtils.writeFile(encBase64, keyFile);
	}

	/**
	 * 根据公钥的 Base64 文本创建公钥对象
	 */
	public static PublicKey getPublicKey(String pubKeyBase64) throws Exception {
		// 把 公钥的Base64文本 转换为已编码的 公钥bytes
		byte[] encPubKey = new BASE64Decoder().decodeBuffer(pubKeyBase64);
		// 创建 已编码的公钥规格
		X509EncodedKeySpec encPubKeySpec = new X509EncodedKeySpec(encPubKey);
		// 获取指定算法的密钥工厂, 根据 已编码的公钥规格, 生成公钥对象
		return java.security.KeyFactory.getInstance(ALGORITHM).generatePublic(encPubKeySpec);
	}

	/**
	 * 根据私钥的 Base64 文本创建私钥对象
	 */
	public static PrivateKey getPrivateKey(String priKeyBase64) throws Exception {
		// 把 私钥的Base64文本 转换为已编码的 私钥bytes
		byte[] encPriKey = new BASE64Decoder().decodeBuffer(priKeyBase64);
		// 创建 已编码的私钥规格
		PKCS8EncodedKeySpec encPriKeySpec = new PKCS8EncodedKeySpec(encPriKey);
		// 获取指定算法的密钥工厂, 根据 已编码的私钥规格, 生成私钥对象
		return java.security.KeyFactory.getInstance(ALGORITHM).generatePrivate(encPriKeySpec);
	}

	/**
	 * 公钥加密数据
	 */
	public static byte[] encrypt(byte[] plainData, PublicKey pubKey) throws Exception {
		// 获取指定算法的密码器
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		// 初始化密码器（公钥加密模型）
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		// 加密数据, 返回加密后的密文
		return cipher.doFinal(plainData);
	}

	/**
	 * 私钥解密数据
	 */
	public static byte[] decrypt(byte[] cipherData, PrivateKey priKey) throws Exception {
		// 获取指定算法的密码器
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		// 初始化密码器（私钥解密模型）
		cipher.init(Cipher.DECRYPT_MODE, priKey);
		// 解密数据, 返回解密后的明文
		return cipher.doFinal(cipherData);
	}

	/**
	 * 用公钥对字符串进行分段加密
	 */
	public static byte[] encryptByPublicKeyForSpilt(byte[] data, PublicKey publicKey) throws Exception {
		int dataLen = data.length;
		if (dataLen <= DEFAULT_BUFFERSIZE) {
			return encrypt(data, publicKey);
		}
		List<Byte> allBytes = new ArrayList<Byte>(2048);
		int bufIndex = 0;
		int subDataLoop = 0;
		byte[] buf = new byte[DEFAULT_BUFFERSIZE];
		for (int i = 0; i < dataLen; i++) {
			buf[bufIndex] = data[i];
			if (++bufIndex == DEFAULT_BUFFERSIZE || i == dataLen - 1) {
				subDataLoop++;
				if (subDataLoop != 1) {
					for (byte b : DEFAULT_SPLIT) {
						allBytes.add(b);
					}
				}
				byte[] encryptBytes = encrypt(buf, publicKey);
				for (byte b : encryptBytes) {
					allBytes.add(b);
				}
				bufIndex = 0;
				if (i == dataLen - 1) {
					buf = null;
				} else {
					buf = new byte[Math.min(DEFAULT_BUFFERSIZE, dataLen - i - 1)];
				}
			}
		}
		byte[] bytes = new byte[allBytes.size()];
		{
			int i = 0;
			for (Byte b : allBytes) {
				bytes[i++] = b.byteValue();
			}
		}
		return bytes;
	}

	/**
	 * 使用私钥分段解密
	 */
	public static byte[] decryptByPrivateKeyForSpilt(byte[] encrypted, PrivateKey privateKey) throws Exception {
		int splitLen = DEFAULT_SPLIT.length;
		if (splitLen <= 0) {
			return decrypt(encrypted, privateKey);
		}
		int dataLen = encrypted.length;
		List<Byte> allBytes = new ArrayList<Byte>(1024);
		int latestStartIndex = 0;
		for (int i = 0; i < dataLen; i++) {
			byte bt = encrypted[i];
			boolean isMatchSplit = false;
			if (i == dataLen - 1) {
				// 到data的最后了
				byte[] part = new byte[dataLen - latestStartIndex];
				System.arraycopy(encrypted, latestStartIndex, part, 0, part.length);
				byte[] decryptPart = decrypt(part, privateKey);
				for (byte b : decryptPart) {
					allBytes.add(b);
				}
				latestStartIndex = i + splitLen;
				i = latestStartIndex - 1;
			} else if (bt == DEFAULT_SPLIT[0]) {
				// 这个是以split[0]开头
				if (splitLen > 1) {
					if (i + splitLen < dataLen) {
						// 没有超出data的范围
						for (int j = 1; j < splitLen; j++) {
							if (DEFAULT_SPLIT[j] != encrypted[i + j]) {
								break;
							}
							if (j == splitLen - 1) {
								// 验证到split的最后一位，都没有break，则表明已经确认是split段
								isMatchSplit = true;
							}
						}
					}
				} else {
					// split只有一位，则已经匹配了
					isMatchSplit = true;
				}
			}
			if (isMatchSplit) {
				byte[] part = new byte[i - latestStartIndex];
				System.arraycopy(encrypted, latestStartIndex, part, 0, part.length);
				byte[] decryptPart = decrypt(part, privateKey);
				for (byte b : decryptPart) {
					allBytes.add(b);
				}
				latestStartIndex = i + splitLen;
				i = latestStartIndex - 1;
			}
		}
		byte[] bytes = new byte[allBytes.size()];
		{
			int i = 0;
			for (Byte b : allBytes) {
				bytes[i++] = b.byteValue();
			}
		}
		return bytes;
	}

	/**
	 * 私钥加密数据
	 */
	public static byte[] encryptE(byte[] plainData, PrivateKey privateKey) throws Exception {
		// 获取指定算法的密码器
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		// 初始化密码器（公钥加密模型）
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		// 加密数据, 返回加密后的密文
		return cipher.doFinal(plainData);
	}

	/**
	 * 私钥解密数据
	 */
	public static byte[] decryptE(byte[] cipherData, PublicKey publicKey) throws Exception {
		// 获取指定算法的密码器
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		// 初始化密码器（私钥解密模型）
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		// 解密数据, 返回解密后的明文
		return cipher.doFinal(cipherData);
	}
	//***********************以下是以文件格式存储公私钥进行加解密******************************************

	/**
	 * 客户端加密, 返回加密后的数据
	 *
	 * @param plainData 加密的字符串字节数组
	 * @param pubFile   公钥文件位置
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] plainData, File pubFile) throws Exception {
		// 读取公钥文件, 创建公钥对象
		PublicKey pubKey = RsaUtil.getPublicKey(IOUtils.readFile(pubFile));
		// 用公钥加密数据
		byte[] cipher = RsaUtil.encrypt(plainData, pubKey);
		return cipher;
	}

	/**
	 * 服务端解密, 返回解密后的数据
	 *
	 * @param cipherData 密文字节数组
	 * @param priFile    私钥文件位置
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] cipherData, File priFile) throws Exception {
		// 读取私钥文件, 创建私钥对象
		PrivateKey priKey = RsaUtil.getPrivateKey(IOUtils.readFile(priFile));
		// 用私钥解密数据
		byte[] plainData = RsaUtil.decrypt(cipherData, priKey);
		return plainData;
	}

	public static String str_pubK = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgUJisBVFRUHIaHG7swKN0elX/HuFNiYbs5dL2ziQjbgF8LoQ3mCgJ6Q78c9ucIi2avQ7cBvZVfLSEEIXI1elOt7q1hHa4RmqjCbGjocnDlDDqGjb9UI3Ur/Ok0i0hzTbc2lihFgfWppJRp5CnjXlRft1b0nnRqpxoD8cxypKf9NYjVHMWf/jiu3ujhVANQVD4CyijkB3JJYmkSOBGFz3TTEsHkU8C9NUw+4L76X6Y9RtFGUIwBO6pKMR10QFe4ck/sRvMktU6/ywCDV/hZbWv26Umn6JEP4S/DI+j0EUfeC458RHvZTPGHWiq/UWx3sw9pwSVFfKOsYKsChbZ6WhNwIDAQAB";
	public static String str_priK = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCBQmKwFUVFQchocbuzAo3R6Vf8e4U2Jhuzl0vbOJCNuAXwuhDeYKAnpDvxz25wiLZq9DtwG9lV8tIQQhcjV6U63urWEdrhGaqMJsaOhycOUMOoaNv1QjdSv86TSLSHNNtzaWKEWB9amklGnkKeNeVF+3VvSedGqnGgPxzHKkp/01iNUcxZ/+OK7e6OFUA1BUPgLKKOQHckliaRI4EYXPdNMSweRTwL01TD7gvvpfpj1G0UZQjAE7qkoxHXRAV7hyT+xG8yS1Tr/LAINX+Flta/bpSafokQ/hL8Mj6PQRR94LjnxEe9lM8YdaKr9RbHezD2nBJUV8o6xgqwKFtnpaE3AgMBAAECggEBAIDWVZ2jJe95OTN5oZg1BHzlM/ESgV3OsC/arx5sDBFmCm2+WE//ScMZfTJyCmearRCALbp517BGnsDbz0pH8wZx6OrE00EpHwghIiowZmprcAotsoiMnq4ZRuMhRee6dL5dnXfCikX5oO4Fkus1VzjhAlWR+TdDboxGO/38llM/PWUJ5eUYj2QEztzKy/IsGguZ3WkeNS7lFj9XyZ/j+7SicWv99VxBUqraA3v9tsQkX5nFysuuUxslTPgDTKVHRtougXrHfYw1ASkk1gvGtK52DIfC4ViruqvNegHCxdnnGXyyQv0C2FJw8a5Gk3nTkcyt0WSAbp2lZRpM31akvXECgYEAzDnhISl+iHW+DWrOVHH68834V42pahNRFV4p72RlN3HJT4pCJketUBuGz3sLHwT12PGHH7ImUfzPMNZ8NaqUh8N79M3TpU8tVRQtgNbn9e/ueABJmSmLjNdEzxWEJ9d1A2/CMiGdyshn3cIyTKgGQS5BJ+CjYU6vnAIP4yjRFU8CgYEAogc39RH7gTpYmSLE1sqfkws5k2LVLHRILaU2lYBzRwpDs29YMjqd53v0krdfNfxohClt+lrZSiD1Qajjah/CQ995u7aH/tsLlbLCsNRLa5nPtdblBU2zhuMDcfqVWRv8k50LJne4BpGlE68UtYfrzw/hz54O0whL5GlbTTaCi5kCgYBuRQoz163EjJ6TrAnAOtLfdWUUER9acReky/Uklza3my6xTdutw6Hm0RYXTT6R/yGng7IMASsDtddBbW4fo/0S2RBC/Ce86GV3vK9dE6yndGd0T+NtWatJ3qn+joWO5Zz+wAdA/jmu1kqOyF5UWZ0W8JyppXdSASR8vfhKFS3frQKBgHA6c8XqKLy//zJC5Pip7JHbSowN/v4FpSEIuKAhErf4IiCdVQellk4Ki8M8BFTOek5gq+6nEq7H2VkbdDnDublth1JAAj7C1mlgIn587aigJ0EakhN0WI9rmq1OFjhcrDxKoLiKYAscwqy5rqx2cx0/MPev0TDXJEoXt8fpo86xAoGAVIVqC6Tc1S1uuLg7XSbyoATzKRC0ibdEH+Cr4KF6Fu7qSIQKfhH9/Px5GA1dsZ8YcLGuqkIl+om4NazBj+RgytyAYXlZs/aEQkWZbz7qQpxjG9dCf0s/81jcSjnwYBxsWsbl85rmW6S7qjnAf3SNIN77zWX46qTrjyy3SCAo6gA=";
	public static void main(String[] args) throws Exception {
		String message = "测试数据测试数据测试数据测试数据测试数据测试数据测试数据测试数据测试数据测试数据测试数据测试数据测试数据测试数据";
//		/**
//		 * 1.签名:私钥加密,公钥解密
//		 */
//
//		//私钥加密
//		byte[] encrypt = encryptE(message.getBytes(), com.yunxi.stamper.commons.jwt.RSA.KeyFactory.getPrivateKey());
//		System.out.println("私钥加密密文:" + new String(encrypt));
//
//		//公钥解密
//		byte[] bytes = decryptE(encrypt, com.yunxi.stamper.commons.jwt.RSA.KeyFactory.getPubKey());
//		System.out.println("公钥加密明文:" + new String(bytes));

		/**
		 * 2.加密:公钥加密,私钥解密
		 */
		long l = System.currentTimeMillis();
		System.out.println("明文长度:" + message.length());
		//公钥加密
		byte[] encryptE = encryptByPublicKeyForSpilt(message.getBytes(),getPublicKey(str_pubK));
		System.out.println("公钥加密密文:" + new String(encryptE));

		//私钥解密
		byte[] decryptE = decryptByPrivateKeyForSpilt(encryptE, getPrivateKey(str_priK));
		System.out.println("私钥解密明文:" + new String(decryptE));

		System.out.println("运行耗时:" + (System.currentTimeMillis() - l) / 1000.0);
	}
}