/**
 * AppNetAuthority.java
 * 功能： TODO 网络权限
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-11-12       下午05:36:55
 * Copyright (c) 2012, TNT All Rights Reserved.
*/

package com.nf.framework.authority;

import android.content.Context;

import com.nf.framework.CheckInternet;

public class AppNetAuthority {

	public static AppNetAuthority ana=null;
	public static AppNetAuthority getInstance(){
		if(ana==null){
			ana=new AppNetAuthority();
		}
		return ana;
	}
 /**
  * 当前网络状态枚举
  * 
  * */
  public enum NetAuthority {
	  unNetConnect,//无网连接
	  WifiConnect,//wifi网络
	  Net2GComnect,//2G网络
	  Net3GComnect; //3G网络
  }
  
  public void getCurrentNetConnect(Context mcontext){
	  
       NetAuthority netState= CheckInternet.getInstance(mcontext).JudgeCurrentNetState();
	   switch(netState){
		   case unNetConnect:
			 
			   break;
		   case WifiConnect:
			 
			   break;
		   case Net2GComnect:
			 
			   break;
		   case Net3GComnect:
				 
			   break;
	   }
	
  }
  
}
