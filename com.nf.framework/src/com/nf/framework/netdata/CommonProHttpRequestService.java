/**
 * CommonProGetDataFromService.java
 * 工程：CommonPro
 * 功能：  从服务器上获取对应数据
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-11-29       下午05:06:01
 * Copyright (c) 2012, TNT All Rights Reserved.
 */

package com.nf.framework.netdata;

import java.util.ArrayList;

import org.apache.commons.httpclient.NameValuePair;

import android.content.Context;

import com.nf.framework.CommonConstantData;
import com.nf.framework.exception.XingshulinError;


public class CommonProHttpRequestService extends AbstractHttpService {

	

	public CommonProHttpRequestService(Context context) {
		super(context);
	}

	/**
	 * 获取推送消息
	 * 
	 * @param sessionKey
	 * @param appName
	 * @return
	 */
	public String getNotificationInfoFromService(String sessionKey,
			String appName) {

		String data = null;
		ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
		try {
			checkNullParams(sessionKey, appName);
			list.add(new NameValuePair("m", "GetMessage"));// 对应方法
			list.add(new NameValuePair("sessionKey", sessionKey));
			list.add(new NameValuePair("appName", appName));
			data = getDataByHttp(CommonConstantData.PushURL, list);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			new XingshulinError(e);
		}
		return data;
	}

	/**
	 * 错误日志接口
	 * 
	 * @param sessionKey
	 * @param errorMsg
	 * @param appName
	 * @return
	 */
	public String UpdateErrorLogServcice(String sessionKey, String errorMsg,
			String appName) {
		String data = null;
		ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
		try {
			checkNullParams(appName);
			list.add(new NameValuePair("m", "UpdateErrorLog"));// 对应方法
			list.add(new NameValuePair("errorMsg", errorMsg));
			list.add(new NameValuePair("os","android"));
			list.add(new NameValuePair("sessionKey", sessionKey));
			list.add(new NameValuePair("appName", appName));
			data = getDataByHttp(CommonConstantData.PushURL, list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			new XingshulinError(e);
		}
		return data;
	}

	/***
	 * 用户反馈接口
	 * 
	 * @param sessionKey
	 * @param fbcontent
	 * @param appName
	 * @return
	 */
	public String FeedBackInfoToServcice(String sessionKey, String fbcontent,
			String appName) {
		String data = null;
		ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
		try {
			checkNullParams(sessionKey, fbcontent, appName);
			list.add(new NameValuePair("m", "FeedBackInfo"));// 对应方法
			list.add(new NameValuePair("fbContent", fbcontent));
			list.add(new NameValuePair("AppName", appName));
			list.add(new NameValuePair("sessionKey", sessionKey));
			data = getDataByHttp(CommonConstantData.URL, list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			new XingshulinError(e);
		}
		return data;
	}

	/***
	 * APK版本更新接口 获取是否更新
	 * 
	 * @param sessionKey
	 * @param versionCode
	 * @param versionName
	 * @param appName
	 * @return
	 */
	public String ApkUpdateServcice(String sessionKey, int versionCode,
			String versionName, String appName) {
		String data = null;
		ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
		try {
			checkNullParams(sessionKey, versionName, appName);
			list.add(new NameValuePair("m", "getAPKUpdateInfo"));// 对应方法
			list.add(new NameValuePair("productName", appName));
			list.add(new NameValuePair("versionCode", String
					.valueOf(versionCode)));
			list.add(new NameValuePair("version", versionName));
			list.add(new NameValuePair("sessionKey", sessionKey));
			data = getDataByHttp(CommonConstantData.PushURL, list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			new XingshulinError(e);
		}
		return data;
	}
	
	/***
	 * 获取公共文件是否存在更新
	 * 2014.1.15
	 * @param sessionKey
	 * @param dataVer
	 * @param typeInfo
	 * @return
	 */
	public String GetFileUpdatePacketIsNewServcice(String sessionKey,String dataVer,String typeInfo) {
		String data = null;
		ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
		try {
			checkNullParams(sessionKey,dataVer,typeInfo);
			list.add(new NameValuePair("m", "GetFileUpdatePacketIsNew"));// 对应方法
			list.add(new NameValuePair("dataVer", dataVer));
			list.add(new NameValuePair("typeInfo",typeInfo));
			list.add(new NameValuePair("sessionKey", sessionKey));
			data = getDataByHttp(CommonConstantData.FileUpdateURL, list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			new XingshulinError(e);
		}
		return data;
	}
	
	/***
	 * 获取公共文件更新路径
	 * @param sessionKey
	 * @param dataVer
	 * @param typeInfo
	 * @return
	 */
	public   String getDownloadFileUpdatePacketUrl(String sessionKey,String dataVer,String typeInfo){
		
		String data=null;
		try {
			checkNullParams(sessionKey,dataVer,typeInfo);
			data=CommonConstantData.FileUpdateURL+"&m=DownloadFileUpdatePacket&sessionKey="
					+sessionKey+"&dataVer="+dataVer+"&typeInfo="+typeInfo;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
}
