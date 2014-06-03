package com.nf.framework.http;


import android.os.Handler;
import android.os.Message;
/***
 * 
 * @author niufei
 * 网络层任务处理
 *
 */
public abstract class HttpReponseHander extends Handler
{

	protected static final int ADD_MESSAGE = 1;///添加新的任务
	protected static final int PROGRESS_MESSAGE=2;//加载中
	protected static final int SUCCESS_MESSAGE =3;//请求响应成功
	protected static final int FAILURE_MESSAGE =4;//请求响应失败
	protected static final int CANCEL_MESSAGE = 5;//取消请求
	protected static final int FINISH_MESSAGE = 6;//完成任务

	/**
	 * 
	 * @param url
	 * @param isInterrupt
	 */
	public abstract void onAddTaskOnService(String url);
	/***
	 * 
	 * @param url
	 */
	public abstract void onProgressOnService(String url);
	/**
	 * 
	 * @param url
	 */
	public abstract void onSuccessTaskOnService(String url,String respondResult);
	/***
	 * 
	 * @param url
	 * @param strMsg
	 */
	public abstract void onFailureTaskOnService(String url,String strMsg);
	/**
	 * 
	 * @param url
	 */
	public abstract void onFinishTaskOnService(String url);
	
	@Override
	public void handleMessage(Message msg)
	{
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		Object[] response;
		switch (msg.what)
		{
			case ADD_MESSAGE:
				response = (Object[]) msg.obj;
				onAddTaskOnService((String) response[0]);
				break;
			case PROGRESS_MESSAGE:
				response = (Object[]) msg.obj;
				onProgressOnService((String) response[0]);
				break;
			case SUCCESS_MESSAGE:
				response = (Object[]) msg.obj;
				onSuccessTaskOnService((String) response[0],(String) response[1]);
				break;
			case FAILURE_MESSAGE:
				response = (Object[]) msg.obj;
				onFailureTaskOnService((String) response[0], (String) response[1]);
				break;
			case CANCEL_MESSAGE:
				response = (Object[]) msg.obj;
				onFailureTaskOnService((String) response[0], (String) response[1]);
				break;
			case FINISH_MESSAGE:
				response = (Object[]) msg.obj;
				onFinishTaskOnService((String) response[0]);
				break;
		}
	}

	protected void sendSuccessMessage(String url,String respondResult)
	{
		sendMessage(obtainMessage(SUCCESS_MESSAGE, new Object[]
		{ url,respondResult}));
	}

	protected void sendAddMessage(String url)
	{
		sendMessage(obtainMessage(ADD_MESSAGE, new Object[]
		{ url}));
	}
	protected void sendProgressMessage(String url)
	{
		sendMessage(obtainMessage(PROGRESS_MESSAGE, new Object[]
		{ url}));
	}
	protected void sendFailureMessage(String url, String strMsg)
	{
		sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[]
		{ url, strMsg }));
	}

	protected void sendCancelMessage(String url)
	{
		sendMessage(obtainMessage(CANCEL_MESSAGE, new Object[]{url}));
	}
	protected void sendFinishMessage(String url)
	{
		sendMessage(obtainMessage(FINISH_MESSAGE, new Object[]{url}));
	}
}
