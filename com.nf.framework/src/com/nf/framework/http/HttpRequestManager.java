package com.nf.framework.http;


import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.text.TextUtils;


/***
 * http请求管理类 启动线程
 * @author niufei
 *
 */
public class HttpRequestManager{
	
	private HttpReponseHander mHttpRequestHander;
	private static HttpRequestManager httpRequestManager;
	private ExecutorService threadPool;
	private static final
	
	/****
	 * 按照优先级排序，sessionkey 默认级别为0 其他请求按照由低到高排序
	 */
	Comparator<HttpRequestTaskVO> OrderIsdn =  new Comparator<HttpRequestTaskVO>(){
		public int compare(HttpRequestTaskVO o1, HttpRequestTaskVO o2) {
			// TODO Auto-generated method stub
			int numbera = o1.getPriority();
			int numberb = o2.getPriority();
			if(numberb > numbera)
			{
				return -1;
			}
			else if(numberb<numbera)
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
	};	
	/***
	 * 优先级队列
	 */
	private	Queue<HttpRequestTaskVO> priorityQueue =  new PriorityQueue<HttpRequestTaskVO>(3,OrderIsdn);
	
	public static HttpRequestManager getHttpRequestManager()
	{

		if (httpRequestManager == null)
		{
			httpRequestManager = new HttpRequestManager();
		}
		return httpRequestManager;
	}
	
	
	private HttpRequestManager(){
		threadPool = Executors.newSingleThreadExecutor();
	}
	
	/***
	 * 添加http get请求任务
	 * @param mcontext
	 * @param priority
	 * @param url
	 */
	public void addHttpRequestTaskByGet(int priority,String url)
	{
		if (TextUtils.isEmpty(url)){
			// 任务中存在这个任务,或者任务不满足要求
			return;
		}
		CreateHttpRequestTaskByGet(priority, url);
	}
	/***
	 * 添加http get请求任务
	 * @param mcontext
	 * @param priority
	 * @param url
	 */
	public void addHttpRequestTaskByPost(int priority,String url,List list)
	{
		if (TextUtils.isEmpty(url)){
			// 任务中存在这个任务,或者任务不满足要求
			return;
		}
		CreateHttpRequestTaskByPost(priority, url,list);
	}
	/**
	 * 创建一个新的get请求任务 并将任务存放到请求队列
	 */
	private void CreateHttpRequestTaskByGet(int priority,String url){
		if(url!=null){
			///如果已经加入到下载队列中，则再次点击不生成新的线程
			for(HttpRequestTaskVO httpRequestTask :priorityQueue){
				String httpUrl=httpRequestTask.getUrl();
				if(httpUrl.equals(url)){
					return;
				}
			}
			HttpRequestTask httpRequestTask=	new HttpRequestTask(url,mHttpRequestHander);
			addHttpRequestTaskTOQueue(url,priority,httpRequestTask);
			ExecuteHttpRequestTask();
			
		}
	}
	/**
	 * 创建一个新的post请求任务 并将任务存放到请求队列
	 */
	private void CreateHttpRequestTaskByPost(int priority,String url,List list){
		if(url!=null){
			///如果已经加入到下载队列中，则再次点击不生成新的线程
			for(HttpRequestTaskVO httpRequestTask :priorityQueue){
				String httpUrl=httpRequestTask.getUrl();
				if(httpUrl.equals(url)){
					return;
				}
			}
			HttpRequestTask httpRequestTask=	new HttpRequestTask(url,list,mHttpRequestHander);
			addHttpRequestTaskTOQueue(url,priority,httpRequestTask);
			ExecuteHttpRequestTask();
			
		}
	}
	/***
	 * 将任务添加到请求队列
	 */
	private void addHttpRequestTaskTOQueue(String url,int priority ,HttpRequestTask httpRequestTask){
		HttpRequestTaskVO httpRequestTaskVO=new HttpRequestTaskVO(priority,url,httpRequestTask);
		priorityQueue.add(httpRequestTaskVO);
	}
	/***
	 * 从队列中移除任务
	 * @param url
	 */
	public void removeTaskFromQueue(String url){
		HttpRequestTaskVO removeHttpTask=null;
		for(HttpRequestTaskVO httpRequest:priorityQueue){
			if(httpRequest.getUrl().equals(url)){
				removeHttpTask=httpRequest;
				break;
			}
		}
		if(priorityQueue!=null){
			priorityQueue.remove(removeHttpTask);
		}
	}
	/****
	 * 移除全部任务列表
	 */
	public void removeAllHttpTasks(){
		priorityQueue.clear();
	}
	/***
	 * 添加下载线程到线程池 等待下载
	 * @param thread
	 */
	private void ExecuteHttpRequestTask(){
		if(threadPool!=null&&priorityQueue!=null&&!priorityQueue.isEmpty()){
			HttpRequestTaskVO httpRequest=priorityQueue.poll();
			if(httpRequest!=null){
				threadPool.submit(httpRequest.getHttpRequestTask());
			}
		}
	}
	
	/**
	 * 暂停，或下载完成均需要调用此方法
	 * 停止指定线程 删除该任务 删除掉该线程信息
	 * @param url
	 */
	public synchronized void CancelTaskByUrl(String url){
		removeTaskFromQueue(url);
	}
	/**
	 * 关闭所有请求任务
	 * @param url
	 */
	public  void CloseHttpRequestThreadPool(){
		if(priorityQueue!=null){
			priorityQueue.clear();
		}
	}
	
	/***
	 * 设置回调函数
	 * @param downLoadCallback
	 */
	public void setDownLoadCallback(HttpReponseHander mHttpRequestHander)
	{
		this.mHttpRequestHander = mHttpRequestHander;
	}

	private class HttpRequestTaskVO{
		
		private int priority;
		private String url;
		private HttpRequestTask httpRequestTask;
		public int getPriority() {
			return priority;
		}
		public void setPriority(int priority) {
			this.priority = priority;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public HttpRequestTask getHttpRequestTask() {
			return httpRequestTask;
		}
		public void setHttpRequestTask(HttpRequestTask httpRequestTask) {
			this.httpRequestTask = httpRequestTask;
		}
		public HttpRequestTaskVO(int priority, String url,
				HttpRequestTask httpRequestTask) {
			super();
			this.priority = priority;
			this.url = url;
			this.httpRequestTask = httpRequestTask;
		}
		
	}


}
