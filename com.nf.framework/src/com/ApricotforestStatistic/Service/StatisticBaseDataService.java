/**
Ω * LiteratureListDataUtil.java
 * com.Apricotforest.main
 * 工程：tabVertical
 * 功能： TODO 
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-3-27       下午12:57:06
 * Copyright (c) 2012, TNT All Rights Reserved.
*/

package com.ApricotforestStatistic.Service;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.ApricotforestStatistic.StaticConfig;
import com.ApricotforestStatistic.VO.StaticBaseVO;
import com.ApricotforestStatistic.VO.StaticErrorRequestVO;
import com.ApricotforestStatistic.VO.StaticEventVO;
import com.ApricotforestStatistic.VO.StaticPageViewRecordVO;
import com.ApricotforestStatistic.VO.StaticPageViewSkipVO;
import com.ApricotforestStatistic.sqlite.StaticErrorRequestDAO;
import com.ApricotforestStatistic.sqlite.StaticEventDAO;
import com.ApricotforestStatistic.sqlite.StaticPageViewDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nf.framework.base64.Md5Encrypt;


public class StatisticBaseDataService {
	
	/**
	 * staticBase-->json
	 * @param staticBase
	 * @return
	 */
	public static String StaticBaseVOToJson(StaticBaseVO staticBase){
	
		if(staticBase!=null){
			GsonBuilder gsonBuilder=new GsonBuilder();
			Gson gson=gsonBuilder.create();
			return gson.toJson(staticBase);
		}
		return null;
	}
	
	/***
	 * 返回封装好的static对象
	 * 
	 * 如果 无访问记录和事件记录则返回为null
	 * @param mcontext
	 * @param dbfilePath
	 * @param userId
	 * @return
	 * @throws SQLException 
	 */
	public static StaticBaseVO  getStaticBaseVO(Context mcontext,String dbfilePath) throws SQLException{
		if(mcontext==null||dbfilePath==null){
			return null;
		}
		List<StaticEventVO> staticEventList=StaticEventDAO.getInstance().getStaticEventListFromSql(mcontext,dbfilePath);
		List<StaticPageViewRecordVO> staticPageViewList=StaticPageViewDAO.getInstance().getUpLoadPageViewList(mcontext,dbfilePath);
		List<StaticPageViewSkipVO> staticPageViewSkipList=StaticPageViewDAO.getInstance().getUpLoadPageViewSkipList(mcontext,dbfilePath);
		List<StaticErrorRequestVO> staticErrorRequestList=StaticErrorRequestDAO.getInstance().getStaticErrorRequestListFromSql(mcontext,dbfilePath);
		if(isListEmpty(staticEventList)&&isListEmpty(staticPageViewList)&&isListEmpty(staticErrorRequestList)){
			return null;
		}
		File dbfile=new File(dbfilePath);
		String dbFileName= dbfile.getName();
		String startTime=null;
		if(dbFileName.contains(StaticConfig.proUpLoadPrefix)){//&&dbFileName.endsWith(ApricotAgent.dbSuffix)){
			startTime=dbFileName.substring(dbFileName.indexOf(StaticConfig.proUpLoadPrefix)+StaticConfig.proUpLoadPrefix.length());// dbFileName.lastIndexOf(ApricotEventAgent.dbSuffix));
		}
		String endTime =String.valueOf(dbfile.lastModified());//获取最后修改的时间
		return 	BuildStaticBaseVO(mcontext, startTime, endTime, staticEventList,staticPageViewList,staticPageViewSkipList,staticErrorRequestList);
	}
	
	private static boolean isListEmpty(List list){
		return (list==null||list.isEmpty())?true:false;
	}
	/***
	 * 构建buildStaticVO
	 * @param mcontext
	 * @param startTime
	 * @param endTime
	 * @param ticket
	 * @param staticEventList
	 * @param userId
	 * @return
	 */
	private static StaticBaseVO BuildStaticBaseVO(Context mcontext,String startTime,String endTime,List<StaticEventVO> staticEventList,List<StaticPageViewRecordVO> staticPageViewList,List<StaticPageViewSkipVO> staticPageViewSkipList,List<StaticErrorRequestVO> staticErrorRequestList){
		if(mcontext==null){
			return null;
		}
		StaticBaseVO staticBase=new StaticBaseVO();
		if(staticEventList!=null){
			staticBase.setStaticEventList(staticEventList);
		}
		if(staticPageViewList!=null){
			staticBase.setStaticPageViewList(staticPageViewList);
		}
		if(staticPageViewSkipList!=null){
			staticBase.setStaticPageViewSkipList(staticPageViewSkipList);
		}
		if(staticErrorRequestList!=null){
			staticBase.setStaticErrorRequestList(staticErrorRequestList);
		}
		try {
			PackageInfo info =mcontext.getPackageManager().getPackageInfo(mcontext.getApplicationInfo().packageName, PackageManager.GET_ACTIVITIES);
			if(info != null){   
				ApplicationInfo appInfo = info.applicationInfo;   
				String appName =mcontext.getPackageManager().getApplicationLabel(appInfo).toString();   
				String appVersion=info.versionName; //得到版本信息  
				staticBase.setAppName(appName);
				staticBase.setAppVersion(appVersion);
			}
			staticBase.setDeviceBrand(android.os.Build.BRAND);//设备厂商
			staticBase.setDeviceId(getDeviceId(mcontext));
			staticBase.setDeviceVersion(android.os.Build.MODEL);//设备名称
			staticBase.setEndTime(endTime);
			staticBase.setStartTime(startTime);
			staticBase.setSystName("android");
			staticBase.setSystVersion(android.os.Build.VERSION.RELEASE);//2.3.5
			String ticket=Md5Encrypt.md5(staticBase.getSystName()+","+staticBase.getSystVersion()+","+StaticConfig.passWord);
			staticBase.setTicket(ticket);
			String netType=StatisticBaseDataService.getCurrentNetState(mcontext);
			staticBase.setNetType(netType!=null?netType:"");
			staticBase.setNetWorking(StatisticBaseDataService.getSimOperator(mcontext));
			try {
				  ApplicationInfo appInfo = mcontext.getPackageManager().getApplicationInfo(mcontext.getPackageName(),PackageManager.GET_META_DATA);
				  String   channelName=appInfo.metaData.getString("XINGSHULIN_CHANNEL");
				  staticBase.setChannelName(channelName);
			 } catch (Exception e) {
				// TODO Auto-generated catch block
			 }    
			return staticBase;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
		}
		return null;
	}
	
	/****
	 * 将本次的文件重命名为等待上传数据的文件
	 * @param currentDbFile
	 * @param upLoadDbFile
	 */
	public static void reNameFileTOUpLoadFileName(File currentDbFile,File upLoadDbFile){
		if(currentDbFile==null||upLoadDbFile==null){
			return;
		}
		currentDbFile.renameTo(upLoadDbFile);
		
	}
	
	
	/**
	 * 获取手机串号 
	 * 
	 * @return
	 */
	private static String getDeviceId(Context mcontext){
		String deviceId =null;
	    try { 
			// 获取手机号、手机串号信息  	当获取不到设备号时，系统会提供一个自动的deviceId
			TelephonyManager tm = (TelephonyManager) mcontext.getSystemService(Context.TELEPHONY_SERVICE);
			deviceId = tm.getDeviceId();
	    }catch(Exception e){     
			  deviceId="999999999999999";
		  }
		return deviceId;
	}
	
	/**
	 * 判断当前网络状态
	 */
	public static String getCurrentNetState(Context mcontext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
		TelephonyManager mTelephony = (TelephonyManager) mcontext.getSystemService(Context.TELEPHONY_SERVICE);
		int netType = info.getType();
		int netSubtype = info.getSubtype();
		if (netType == ConnectivityManager.TYPE_WIFI) {// WIFI
			return "WIFI";
		} else if (netType == ConnectivityManager.TYPE_MOBILE
				&& netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
				&& !mTelephony.isNetworkRoaming()) {// 3G
				return "3G";
			} else {
				return "2G";
			}
		}
		return null;
	}
	
	/**
	 * 获取手机卡运营商
	 * 
	 * @return
	 */
	public static String getSimOperator(Context context) {

		TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); String operator = telManager.getSimOperator();

		if(operator!=null){ 
			if(operator.equals("46000") || operator.equals("46002")|| operator.equals("46007")){
			//中国移动
				return "China Mobile";
			}else if(operator.equals("46001")){
	
			//中国联通
				return "China Union";
			}else if(operator.equals("46003")){
			//中国电信
				return "China Telecom";
			} 
		}
		return null;
	}
}
