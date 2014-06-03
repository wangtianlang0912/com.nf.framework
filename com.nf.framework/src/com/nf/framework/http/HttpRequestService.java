package com.nf.framework.http;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

import com.nf.framework.exception.LogUtil;
import com.nf.framework.exception.XingshulinError;

/**
 * 后台http请求服务
 * @author win7
 *
 */
public class HttpRequestService extends Service{
	private HttpRequestUICallback httpRequestCallBack;
	private HttpRequestManager httpRequestManager;
	public static HttpServiceImpl httpServiceImpl;
	/***当前执行的任务汇总**/
	private List<String> currentExcuteingTasks=new ArrayList<String>();
	/***服务中所有加入到流程中执行的任务列表***/
	private List<String> HttpTaskList=new ArrayList<String>();
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		return super.onStartCommand(intent, flags, startId);

	}
	@Override
	public void onCreate(){
		super.onCreate();
		if(httpServiceImpl==null){
			httpServiceImpl=new HttpServiceImpl();
		}
		httpRequestManager=HttpRequestManager.getHttpRequestManager();
		httpRequestManager.setDownLoadCallback(httpRequestHander);
	}

	@Override
	public void onStart(Intent intent, int startId)
	{
		super.onStart(intent, startId);
		LogUtil.i(this, "DownloadService.onStart()");
	}
	@Override 
	public boolean onUnbind(Intent intent){
		LogUtil.i(this, "DownloadService.onUnbind()");
		return super.onUnbind(intent);
	}
	
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if(httpRequestManager!=null)
			httpRequestManager.CloseHttpRequestThreadPool();
		LogUtil.i(this, "DownloadService.onDestroy()");
	}
	/***
	 * service层下载回调方法
	 */
	private HttpReponseHander httpRequestHander=new HttpReponseHander(){

		@Override
		public void onAddTaskOnService(String url) {
			// TODO Auto-generated method stub
			Toast.makeText(HttpRequestService.this,"已加入到队列",0).show();
			if(httpRequestCallBack!=null){
				httpRequestCallBack.sendAddMessage(url);
			}
		}
		@Override
		public void onProgressOnService(String url) {
			// TODO Auto-generated method stub
			if(httpRequestCallBack!=null){
				httpRequestCallBack.sendProgressMessage(url);
			}
		}
		@Override
		public void onFailureTaskOnService(String url, String strMsg) {
			// TODO Auto-generated method stub
			Toast.makeText(HttpRequestService.this,"请求失败",0).show();
			if(httpServiceImpl!=null){
				try {
					httpServiceImpl.addTaskByGet("www.xingshulin.com",HttpRequestConfig.PRIORITY_SESSIONKEY);
					httpServiceImpl.addTaskByGet("www.baidu.com",HttpRequestConfig.PRIORITY_PRECOMMON);
					
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					throw new XingshulinError(HttpRequestService.this, e);
				}
			}
			if(httpRequestCallBack!=null){
				httpRequestCallBack.sendFailureMessage(url, strMsg);
			}
		}
		@Override
		public void onSuccessTaskOnService(String url, String respondResult) {
			// TODO Auto-generated method stub
			Toast.makeText(HttpRequestService.this,respondResult,0).show();	
			if(httpRequestCallBack!=null){
				httpRequestCallBack.sendSuccessMessage(url, respondResult);
			}
		}
		@Override
		public void onFinishTaskOnService(String url) {
			// TODO Auto-generated method stub
			if(httpRequestCallBack!=null){
				httpRequestCallBack.sendFinishMessage(url);
			}
		}
    };
  
	
	public class HttpServiceImpl extends IHttpRequestService.Stub
	{
		/****
		 * 设置ui线程回调方法
		 * @param httpRequestCallBack
		 */
		public void setHttpRequestCallBack(HttpRequestUICallback httpRequestCallBack) {
			HttpRequestService.this.httpRequestCallBack = httpRequestCallBack;
		}
		@Override
		public void deleteTask(String url) throws RemoteException {
			// TODO Auto-generated method stub
			if(httpRequestManager!=null){
				httpRequestManager.CancelTaskByUrl(url);
			}
		}
		@Override
		public boolean isRunningTask(String url) throws RemoteException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isWaitingTask(String url) throws RemoteException {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public void startManage() throws RemoteException {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void addTaskByGet(String url, int priority)
				throws RemoteException {
			// TODO Auto-generated method stub
			if(httpRequestManager!=null){
				httpRequestManager.addHttpRequestTaskByGet(priority, url);
			}
		}
		@Override
		public void addTaskByPost(String url, int priority, List list)
				throws RemoteException {
			// TODO Auto-generated method stub
			if(httpRequestManager!=null){
				httpRequestManager.addHttpRequestTaskByPost(priority, url, list);
			}
		}
		@Override
		public void stopManage() throws RemoteException {
			// TODO Auto-generated method stub
			
		}
		
	}
}

