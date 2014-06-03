package com.nf.framework.updateapk;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nf.framework.JSONDataUtils;

public class AppInfoUtil {

	
	/**
	 *数据 json——>对象列表 VersionInfo
	 * @param jsonStr
	 * @return
	 */
	public  List<VersionInfo> getVersionInfoList(String jsonStr){
		JSONArray jsons = JSONDataUtils.StringToJSONArray(jsonStr);
		return	getVersionInfoList(jsons);
	}	
	/**
	 * 数据 json——>对象列表 VersionInfo
	 * @param jsons
	 * @return
	 */
	public  List<VersionInfo> getVersionInfoList(JSONArray jsons){
		List<VersionInfo> listdata = new ArrayList<VersionInfo>();
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		Type listType = new TypeToken<ArrayList<VersionInfo>>(){}.getType();
		listdata =	gson.fromJson(jsons.toString(),listType);
		return listdata;
	}
	
	/**
	 * 数据 json——>对象 VersionInfo
	 * @param jsons
	 * @return
	 */
	public VersionInfo getVersionInfo(String jsonStr){
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		Type listType = new TypeToken<VersionInfo>(){}.getType();
		VersionInfo versionInfo =	gson.fromJson(jsonStr,listType);
		return versionInfo;
	}
}
