package com.ApricotforestStatistic.Service;

import android.content.Context;

/***
 * 杏树林 页面统计类
 * @author niufei
 *
 */
class ApricotPageViewAgent extends ApricotBaseAgent implements ApricotPageViewAgentInterface{


	/**
	 * 
	 * @param context
	 * @param pageCode 页面代码
	 */
	@Override
	public void onResume(Context context,int userId,String currentPageCode){
		if(context==null){
			return;
		}
		if(isCloseStatic){
			return;
		}
		if(currentPageCode==null){
			currentPageCode=context.getClass().getName();
		}
		new Thread(new InsertPageViewThread(context,currentPageCode,userId)).start();
	}
	/****
	 * 
	 * @param context
	 * @param pageCode 页面代码
	 */
	@Override
	public  void onPause(Context context,int userId,String currentPageCode){
		if(context==null){
			return;
		}
		if(isCloseStatic){
			return;
		}
		if(currentPageCode==null){
			currentPageCode=context.getClass().getName();
		}
		new Thread(new UpdatePageViewThread(context,currentPageCode,userId)).start();
	}
}
