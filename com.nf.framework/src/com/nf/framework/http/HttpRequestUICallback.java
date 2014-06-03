package com.nf.framework.http;


import android.app.Activity;
import android.os.Handler;
import android.os.Message;
/*****
 * 
 * @author niufei
 *	Http请求 视图控件回调类
 */
public abstract class HttpRequestUICallback extends Handler
{

	protected static final int ADD_HTTP_MESSAGE = 0;///添加新的任务
	protected static final int SUCCESS_HTTP_MESSAGE = 1;//请求成功
	protected static final int FAILURE_HTTP_MESSAGE = 2;//请求失败
	protected static final int PROGRESS_HTTP_MESSAGE=3;//加载中
	protected static final int FINISH_HTTP_MESSAGE=4;//完成任务

	protected Activity mactivity;
	public HttpRequestUICallback(Activity activity){
		
		this.mactivity=activity;
	}
	/**
	 * 
	 * @param url
	 * @param isInterrupt
	 */
	public abstract void onAddTaskOnUIThread(String url);
	/***
	 * 
	 * @param url
	 */
	public abstract void onProgressOnUIThread(String url);
	/**
	 * 
	 * @param url
	 */
	public abstract void onSuccessTaskOnUIThread(String url,String respondResult);
	/***
	 * 
	 * @param url
	 * @param strMsg
	 */
	public abstract void onFailureTaskOnUIThread(String url,String strMsg);
	/**
	 * 
	 * @param url
	 */
	public abstract void onFinishTaskOnUITread(String url);
	
	public Message mMessage;
	@Override
	public void handleMessage(Message msg)
	{
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		if(mactivity==null||msg==null){
			return;
		}
		this.mMessage=msg;
		mactivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Object[] response;
				switch (mMessage.what)
				{
					case ADD_HTTP_MESSAGE:
						response = (Object[]) mMessage.obj;
						onAddTaskOnUIThread((String) response[0]);
						break;
					case PROGRESS_HTTP_MESSAGE:
						response = (Object[]) mMessage.obj;
						onProgressOnUIThread((String) response[0]);
						break;
						
					case SUCCESS_HTTP_MESSAGE:
						response = (Object[]) mMessage.obj;
						onSuccessTaskOnUIThread((String) response[0],(String) response[1]);
						break;
					case FAILURE_HTTP_MESSAGE:
						response = (Object[]) mMessage.obj;
						onFailureTaskOnUIThread((String) response[0], (String) response[1]);
						break;
					case FINISH_HTTP_MESSAGE:
						response = (Object[]) mMessage.obj;
						onFinishTaskOnUITread((String) response[0]);
						break;
				}
			}
		});
	
	}

	protected void sendSuccessMessage(String url,String respondResult)
	{
		sendMessage(obtainMessage(SUCCESS_HTTP_MESSAGE, new Object[]
		{ url,respondResult}));
	}

	protected void sendAddMessage(String url)
	{
		sendMessage(obtainMessage(ADD_HTTP_MESSAGE, new Object[]
		{ url}));
	}
	protected void sendProgressMessage(String url)
	{
		sendMessage(obtainMessage(PROGRESS_HTTP_MESSAGE, new Object[]
		{ url}));
	}
	protected void sendFailureMessage(String url, String strMsg)
	{
		sendMessage(obtainMessage(FAILURE_HTTP_MESSAGE, new Object[]
		{ url, strMsg }));
	}
	protected void sendFinishMessage(String url)
	{
		sendMessage(obtainMessage(FINISH_HTTP_MESSAGE, new Object[]
		{ url}));
	}
}
