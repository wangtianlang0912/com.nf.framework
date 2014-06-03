package com.nf.framework.http;

interface IHttpRequestService
 {	
 
	void startManage();
 
 	boolean isRunningTask(String url);
	
	boolean isWaitingTask(String url);
	
	void addTaskByGet(String url,int priority);
	
	void addTaskByPost(String url,int priority,in List list);
	
	void deleteTask(String url);
	
	void stopManage();
}
