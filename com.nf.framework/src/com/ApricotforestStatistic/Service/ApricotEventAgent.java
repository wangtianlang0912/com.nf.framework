package com.ApricotforestStatistic.Service;

import android.content.Context;
/***
 * 杏树林事件统计类
 * @author niufei
 *
 */
class ApricotEventAgent extends ApricotBaseAgent implements ApricotEventAgentInterface{

	/****
	 * 添加事件统计计数
	 * 异步实现
	 * @param mcontext
	 * @param eventName
	 * @param userId
	 * @param relatedParam
	 */
	@Override
	public  void onEvent (Context mcontext,String eventName,int userId,String relatedParam){
		if(mcontext==null||eventName==null){
			return;
		}
		if(isCloseStatic){
			return;
		}
		new Thread(new InsertOrUpdateEventThread(mcontext,eventName,userId,relatedParam)).start();
	}
	
}
