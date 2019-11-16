package com.stamper.yx.common.sys;

import net.sf.ehcache.util.PropertyUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 配置文件工具类
 */
public class PropertiesUtil {
	private Properties props;

	//指定加载
	public PropertiesUtil(String fileName) {
		readProperties(fileName);
	}

	/**
	 * 加载配置文件
	 * @param fileName
	 */
	private void readProperties(String fileName) {
		if (props == null) {
			props = new Properties();
		}
		try {
			InputStream inputStream = PropertyUtil.class.getClassLoader().getResourceAsStream(fileName);
			props.load(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据数组key名,获取指定字符串数组
	 * @param arrNames
	 * @return
	 */
	public String[] getArr(String arrNames) {
		Map<?, ?> all = getAll();
		int length = 0;
		List<String> temp = new LinkedList<>();
		for (Map.Entry<?, ?> en : all.entrySet()) {
			String key = en.getKey().toString();
			if (key.startsWith(arrNames)) {
				temp.add(en.getValue().toString());
				length++;
			}
		}
		return temp.toArray(new String[length]);
	}

	/**
	 * 根据key读取对应的value
	 * @param key
	 * @return
	 */
	public String getStr(String key) {
		if(StringUtils.isBlank(key)){
			return null;
		}
		String property = props.getProperty(key);
		if(StringUtils.isBlank(property)){
			return null;
		}
		String msg = null;
		try {
			msg = new String(property.getBytes("ISO8859-1"), "UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * 根据key读取对应的value
	 *
	 * @param key
	 * @return
	 */
	public Integer getInt(String key) {
		String property = props.getProperty(key);
		int i = Integer.parseInt(property);
		return i;
	}

	public static Map<String, String> getList(String fileName) {
		if (StringUtils.isBlank(fileName)) {
			return null;
		}
		try {
			Map<String, String> result = new HashMap<>();
			InputStream inputStream = PropertyUtil.class.getClassLoader().getResourceAsStream(fileName);
			Properties pp = new Properties();
			pp.load(inputStream);
			Enumeration<?> enumeration = pp.propertyNames();
			while (enumeration.hasMoreElements()) {
				Object name = enumeration.nextElement();
				Object value = pp.get(name);
				result.put(name.toString(), value.toString());
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 得到所有的配置信息
	 *
	 * @return
	 */
	public LinkedHashMap<String, String> getAll() {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		Enumeration<?> enu = props.propertyNames();
		while (enu.hasMoreElements()) {
			String key = (String)enu.nextElement();
			String value = props.getProperty(key);
			try {
				key = new String(key.getBytes("ISO-8859-1"),"utf8");
			} catch (UnsupportedEncodingException e) {
			}
			map.put(key, value);
		}
		return map;
	}

}
