/**
 * ApricotforestStaticServicejava
 * com.Apricotforest.LocalSave.SharedPrefer
 * 工程：ApricotforestStatisticPro
 * 功能：  杏树林 统计模块 测试
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2013-3-27       下午5:27:35
 * Copyright (c) 2013, TNT All Rights Reserved.
*/

package com.ApricotforestStatistic.Service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
/****
 * 杏树林 统计模块 测试 保存timeTicket
 * @author niufei
 *
 */
public class ApricotStatisticSharePre {
	public static SharedPreferences sharedPreferences=null;
	public static ApricotStatisticSharePre ausss=null;
	public static ApricotStatisticSharePre getInstance(Context mcontext){
		if(ausss==null){
			ausss=new ApricotStatisticSharePre(mcontext);
		}
		return ausss;
	}
	private ApricotStatisticSharePre(Context mcontext){
		sharedPreferences = mcontext.getSharedPreferences("ApricotforestStatic_TimeTicket",Context.MODE_PRIVATE);
	}
	
	public long getTimeTicket(){
		return sharedPreferences.getLong("timeTicket",0);
	}
    public void setTimeTicket(long timeTicket){
		Editor editor=sharedPreferences.edit();
		editor.putLong("timeTicket",timeTicket);
		editor.commit();// 提交修改
    }
    /***
     * 设置当前db文件
     * @param currentDbFile
     */
    public void setCurrentDbFilePath(String currentDbFile){
    	
    		Editor editor=sharedPreferences.edit();
		editor.putString("currentDbFilePath",currentDbFile);
		editor.commit();// 提交修改
    }
    /***
     * 返回当前db文件路径
     * @return
     */
    public String getCurrentDbFilePath(){
    		return sharedPreferences.getString("currentDbFilePath",null);
    }
    /***
     * 存储UserId
     * @param userId
     */
//    public void setStaticUserId(int userId){
//  	 	Editor editor=sharedPreferences.edit();
//		editor.putInt("userId",userId);
//		editor.commit();// 提交修改
//    }
    /***
     * 返回当前用户id
     * @return
     */
//    public int getStaticUserId(){
//
//    		return sharedPreferences.getInt("userId",0);
//    }
    
    public void clear(){
	    	Editor editor= 	sharedPreferences.edit();
	    	editor.clear();
	    	editor.commit();
    }
    
}
