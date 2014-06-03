/**
 * 功能： 对mainActivity中的数据进行解析处理
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-3-7       下午05:39:59
 * Copyright (c) 2012, TNT All Rights Reserved.
*/

package com.nf.framework;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONDataUtils {

	
	/**
	 * 解析json数据
	 * @param str
	 * @param param
	 * @return 返回JSONObject类型
	 */
	public static JSONObject JSONDataToJSONObject(String str,String param){
		JSONObject json=null;
		if(json==null){
			return null;
		}
		try {
			JSONObject jsonStr=new JSONObject(str);
			if(jsonStr.has(param)){
			json=new JSONObject(String.valueOf(jsonStr.get(param)));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	/**
	 * 解析json数据
	 * @param str
	 * @param param
	 * @return 返回String类型
	 */
	public static String JSONDataToString(String str,String param){
		String json=null;
		if(str!=null&&str.length()!=0){
			try {
				JSONObject jsonStr=new JSONObject(str);
				if(jsonStr.has(param)){
					json=String.valueOf(jsonStr.get(param));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return json;
	}
/**
 * 将String转成JSONArray
 * @param json
 * @return
 */
	public static JSONArray StringToJSONArray(String json){
		JSONArray jsonArray=null;
		if(json==null){
			return null;
		}
		try {
			 jsonArray=new JSONArray(json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonArray;
	}
	
}
