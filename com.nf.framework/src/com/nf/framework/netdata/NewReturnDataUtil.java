/**
 * 功能： TODO 
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-3-27       下午12:57:06
 * Copyright (c) 2012, TNT All Rights Reserved.
 */

package com.nf.framework.netdata;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.nf.framework.JSONDataUtils;

/**
 * 新的接口处理方法
 * 
 * @author win7
 *         {"jsonType":"object","numRows":0,"obj":{},"result":{"errorCode":""
 *         ,"reason":"无效方法名！","result":false}}
 */
public class NewReturnDataUtil {
	/**
	 * java对象反射HashMap
	 * 
	 * @param obj
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static HashMap<String, Object> testReflect(Object obj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		Class c = obj.getClass();
		Method m[] = c.getDeclaredMethods();
		for (int i = 0; i < m.length; i++) {
			if (m[i].getName().indexOf("get") == 0) {
				hashMap.put(m[i].getName(), m[i].invoke(obj, new Object[0]));
			}
		}
		return hashMap;
	}

	/**
	 * 判定获取的返回数据是否符合规范
	 * 
	 * @param returnData
	 * @return
	 */
	public static boolean JudgeReturnData(String returnData) {

		boolean result = false;
		String resultObj = JSONDataUtils.JSONDataToString(returnData, "result");
		resultObj = JSONDataUtils.JSONDataToString(resultObj, "result");
		result = Boolean.valueOf(resultObj);
		return result;
	}

	/**
	 * 将json转化为对象
	 * 
	 * @param jsonData
	 * @return
	 */
	public static BaseObjectVO getBaseObjectResult(String jsonData) {
		BaseObjectVO baseObject = new BaseObjectVO();
		// 统计所有的条数
		String numRows = JSONDataUtils.JSONDataToString(jsonData, "numRows");
		String objdata = JSONDataUtils.JSONDataToString(jsonData, "obj");
		String jsonType = JSONDataUtils.JSONDataToString(jsonData, "jsonType");
		String data = JSONDataUtils.JSONDataToString(jsonData, "data");
		boolean resultObj = JudgeReturnData(jsonData);
		String reason = resultObj ? null : errorReason(jsonData);
		if (numRows != null && !"".equals(numRows) && !"null".equals(numRows)) {
			baseObject.setNumRows(Integer.valueOf(numRows));
		}
		baseObject.setObj(objdata);
		baseObject.setJsonType(jsonType);
		baseObject.setResultObj(resultObj);
		baseObject.setReason(reason);
		baseObject.setData((data == null || "".equals(data)) ? 0 : Integer
				.valueOf(data));
		return baseObject;
	}

	/**
	 * 获取失败原因
	 * 
	 * @param returnData
	 * @return Str
	 */
	public static String errorReason(String returnData) {
		String resultObj = JSONDataUtils.JSONDataToString(returnData, "result");
		resultObj = JSONDataUtils.JSONDataToString(resultObj, "reason");
		return resultObj;
	}

	/**
	 * 判定获取的返回数据是否符合规范
	 * 
	 * @param returnData
	 * @return Str
	 */
	public static String JudgeReturnDataToStr(String returnData) {
		String resultObj = JSONDataUtils.JSONDataToString(returnData,
				"resultObj");
		resultObj = JSONDataUtils.JSONDataToString(resultObj, "result");
		return resultObj;
	}
}
