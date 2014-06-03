package com.nf.framework.http.downLoad;


import android.os.Handler;
import android.os.Message;

public abstract class DownLoadCallback extends Handler
{

	protected static final int START_MESSAGE = 0;//开始启动
	protected static final int ADD_MESSAGE = 1;///添加新的任务
	protected static final int PROGRESS_MESSAGE = 2;//正在下载
	protected static final int SUCCESS_MESSAGE = 3;//下载成功
	protected static final int FAILURE_MESSAGE = 4;//下载失败
	protected static final int WAITING_MESSAGE=5;//等待中
	protected static final int PAUSE_MESSAGE = 6;//暂停下载
	protected static final int FINISH_MESSAGE = 7;//完成单个下载
	protected static final int STOP_MESSAGE = 8;//停止下载
	
	/**********版本不适用更新提示*************/
	public static final String ERROR_APPUPDATE_REFER="appupdate";
	/************网络连接异常****************************/
	public static final String ERROR_NETFAILURE_REFER="netfailure";
	/************无足够空间错误提示***********/
	public static final String ERROR_NOENOUGHSTORGE_ERROR="noenoughstorge";
	
	/************账户信息不合法，请重新登录***********/
	public static final String ERROR_USERINFO_ERROR="badUserInfo";

	/***MD5校验错误***/
	public static final String ERROR_MD5_ERROR="MD5Error";
	/***文件长度与返回文件长度不等***/
	public static final String ERROR_ContentLength_ERROR="ContentLengthError";
	/**
	 * 
	 * @param url
	 */
	public abstract void onStartOnUIThread(String url);
	/**
	 * 
	 * @param url
	 * @param isInterrupt
	 */
	public abstract void onAddTaskOnUIThread(String url,Boolean isInterrupt);
	/**
	 * 
	 * @param url
	 * @param speed
	 * @param progress
	 */
	public abstract void onLoadingTaskOnUIThread( String url,String speed,int progress);
	/***
	 * 
	 * @param url
	 */
	public abstract void onWaitingOnUIThread(String url);
	/**
	 * 
	 * @param url
	 */
	public abstract void onSuccessTaskOnUIThread(String url,String filePath);
	/**
	 * 
	 * @param url
	 */
	public abstract void onPauseTaskOnUIThread(String url);
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
	public abstract void onFinishTaskOnUIThread(String url);
	/***
	 * 
	 */
	public abstract void onStopTasksOnUIThread();
	
	public Message mMessage;
	@Override
	public void handleMessage(Message msg)
	{
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		this.mMessage=msg;
		Object[] response;
		switch (mMessage.what)
		{
			case START_MESSAGE:
				response= (Object[]) mMessage.obj;
				onStartOnUIThread((String) response[0]);
			break;
			case ADD_MESSAGE:
				response = (Object[]) mMessage.obj;
				onAddTaskOnUIThread((String) response[0], (Boolean) response[1]);
				break;
			case PROGRESS_MESSAGE:
				response = (Object[]) mMessage.obj;
				onLoadingTaskOnUIThread((String) response[0], (String) response[1],
						(Integer) response[2]);
				break;
			case WAITING_MESSAGE:
				response= (Object[]) mMessage.obj;
				onWaitingOnUIThread((String) response[0]);
			break;	
				
			case SUCCESS_MESSAGE:
				response = (Object[]) mMessage.obj;
				onSuccessTaskOnUIThread((String) response[0],(String) response[1]);
				break;
			case FAILURE_MESSAGE:
				response = (Object[]) mMessage.obj;
				onFailureTaskOnUIThread((String) response[0], (String) response[1]);
				break;
			case PAUSE_MESSAGE:
				response = (Object[]) mMessage.obj;
				onPauseTaskOnUIThread((String) response[0]);
				break;
			case FINISH_MESSAGE:
				response = (Object[]) mMessage.obj;
				onFinishTaskOnUIThread((String) response[0]);
				break;
			case STOP_MESSAGE:
				onStopTasksOnUIThread();
				break;
		}
	
	}

	protected void sendSuccessMessage(String url,String filePath)
	{
		sendMessage(obtainMessage(SUCCESS_MESSAGE, new Object[]
		{ url,filePath}));
	}

	protected void sendLoadMessage(String url, String speed, String progress)
	{
		sendMessage(obtainMessage(PROGRESS_MESSAGE, new Object[]
		{ url, speed, progress }));
	}

	protected void sendWaitingMessage(String url)
	{
		sendMessage(obtainMessage(WAITING_MESSAGE, new Object[]
		{ url}));
	}
	protected void sendAddMessage(String url, Boolean isInterrupt)
	{
		sendMessage(obtainMessage(ADD_MESSAGE, new Object[]
		{ url, isInterrupt }));
	}
	protected void sendPauseMessage(String url)
	{
		sendMessage(obtainMessage(PAUSE_MESSAGE, new Object[]
		{ url }));
	}
	protected void sendFailureMessage(String url, String strMsg)
	{
		sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[]
		{ url, strMsg }));
	}

	protected void sendStartMessage(String url)
	{
		sendMessage(obtainMessage(START_MESSAGE, new Object[]{url}));
	}
	protected void sendStopMessage()
	{
		sendMessage(obtainMessage(STOP_MESSAGE,null));
	}
	/***
	 * 下载任务结束，发送完成信息
	 * @param url
	 */
	protected void sendFinishMessage(String url)
	{
		sendMessage(obtainMessage(FINISH_MESSAGE, new Object[]{url}));
	}

}
