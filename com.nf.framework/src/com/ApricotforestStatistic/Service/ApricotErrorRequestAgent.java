package com.ApricotforestStatistic.Service;

import android.content.Context;

class ApricotErrorRequestAgent extends ApricotBaseAgent implements ApricotErrorRequestAgentInterface{


	/***
	 * 请求接口错误 提交
	 * @param mcontext
	 * @param requestUrl
	 * @param errorReponseCode
	 * @param isRequestByPost
	 */
	@Override
	public void onRequestUrl(Context mcontext,String requestUrl,int errorReponseCode,boolean isRequestByPost){
		if(mcontext==null||requestUrl==null){
			return;
		}
		if(isCloseStatic){
			return;
		}
		new Thread(new InsertErrorRequestThread(mcontext,requestUrl, String.valueOf(errorReponseCode), isRequestByPost)).start();
	}
	
}
