package com.nf.framework;
/**
 * propertyUtil.java
 * 处理property文件 
 * 功能： TODO 
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-10-23       下午02:50:19
 * Copyright (c) 2012, TNT All Rights Reserved.
 */

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class PropertyUtil {
	
	private static PropertyUtil pu=null;
	public static PropertyUtil getInstantce(){
		if(pu==null){
			pu=new PropertyUtil();
		}
		return pu;
	}

	/**
	 * 根据key读取value
	 * 
	 * @param filePath
	 * @param key
	 * @return
	 */
	public static String readValue(String filePath, String key) {
		Properties props = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(
					filePath));
			props.loadFromXML(in);
			String value = props.getProperty(key);
			System.out.println(""+value);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据value读取key
	 * 
	 * @param in
	 * @param value
	 * @return
	 */
	public static String readKey(InputStream in, String value) {
		Properties props = new Properties();
		String key = null;
		try {
			props.loadFromXML(in);
			Enumeration en = props.propertyNames();
			while (en.hasMoreElements()) {
				key = (String) en.nextElement();
				String Property = props.getProperty(key);
				if (Property.equals(value)) {
					break;
				}else{
					key=null;
				}
			}
			return key;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 读取properties的全部value信息
	 * @param filePath
	 */
	public Object[] readPropertiesValues(InputStream fileInputStream) {
		Properties props = new Properties();
		List<String> list=new ArrayList<String>();
		HashMap<String,String> map=new HashMap<String,String>();
		try {	
			props.loadFromXML(fileInputStream);
			Enumeration en = props.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				String value = props.getProperty(key);
				map.put(key, value);
			}
			List<Map.Entry<String, String>> infoIds =new ArrayList<Map.Entry<String, String>>(map.entrySet());
			infoIds=SortByKey(infoIds);
			 //排序后
			 for (int i = 0; i < infoIds.size(); i++) {
				 list.add(infoIds.get(i).getValue());
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list.toArray();
	}
	/**
	 * 按照Key值排序
	 * @param list
	 * @return
	 */
	public List<Map.Entry<String, String>> SortByKey(List<Map.Entry<String, String>> list){
		//排序
		Collections.sort(list, new Comparator<Map.Entry<String, String>>() {   
		    public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {      
		        return (o1.getKey()).toString().compareTo(o2.getKey());
		    }
		});
		return list; 
		
	}
	/**
	 * 按照Value值排序
	 * @param list
	 * @return
	 */
	public List<Map.Entry<String, Integer>> SortByValue(List<Map.Entry<String, Integer>> list){
		//排序
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {   
		    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {      
		        return (o2.getValue() - o1.getValue()); 
		    }
		});
		return list; 
		
	}
	/**
	 * 写入properties信息
	 * 
	 * @param filePath
	 * @param parameterName
	 * @param parameterValue
	 */
	public void writeProperties(String filePath, int parameterName,
			String parameterValue) {
		Properties prop = new Properties();
		try {
			InputStream fis = new FileInputStream(filePath);
			// 从输入流中读取属性列表（键和元素对）
			prop.load(fis);
			// 调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。
			// 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
			OutputStream fos = new FileOutputStream(filePath);
			prop.setProperty(String.valueOf(parameterName), parameterValue);
			// 以适合使用 load 方法加载到 Properties 表中的格式，
			// 将此 Properties 表中的属性列表（键和元素对）写入输出流
			prop.storeToXML(fos, String.valueOf(parameterName), "utf-8");

		} catch (IOException e) {
			e.getStackTrace();
		}
	}
}
