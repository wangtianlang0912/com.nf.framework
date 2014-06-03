package com.nf.framework.http.downLoad;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.text.TextUtils;

import com.nf.framework.exception.XingshulinError;
import com.nf.framework.util.StringUtil;


/***
 * 下载管理类 启动线程
 * @author win7
 *
 */
public class DownLoadManager{
	
	private List<DownLoadThreadManageVO> downLoadThreadManageList=new ArrayList<DownLoadThreadManageVO>();
	public  String rootPath =null;//下载文件临时存储路径\
	private DownLoadCallback mDownLoadCallback;
	private static DownLoadManager downloadManager;
	// 最多运行线程数
	private	static int maxNumThread =3;
	private ExecutorService threadPool;
	public static DownLoadManager getDownloadManager()
	{

		if (downloadManager == null)
		{
			downloadManager = new DownLoadManager();
		}
		return downloadManager;
	}

	private DownLoadManager(){
		threadPool = Executors.newFixedThreadPool(maxNumThread);
	}
	/**
	 * 设置下载文件保存路径
	 * @param rootPath
	 */
	public void setDownLoadRootPath(String rootPath){
		
		if (!StringUtil.isEmpty(rootPath))
		{
			File rootFile = new File(rootPath);
			if (!rootFile.exists())
			{
				rootFile.mkdir();
			}
		}
		this.rootPath=rootPath;
	}
	
	/**
	 * 添加任务
	 * @param mcontext
	 * @param url
	 * @param downLoadFileName 下载文件的名称 ，可选
	 */
	public void addDownLoadTask(Context mcontext,String url,int downLoadItemId,int userId,String downLoadFileName)
	{
		if (TextUtils.isEmpty(url)){
			// 任务中存在这个任务,或者任务不满足要求
			return;
		}
		CreateDownLoadTask(mcontext, url,downLoadItemId,userId,downLoadFileName);
	}
	/**
	 * 创建一个新的下载任务 并将线程添加到线程池
	 */
	private void CreateDownLoadTask(Context context,String url,int downLoadItemId,int userId,String downLoadFileName){
		if(rootPath==null){
			
			throw new  XingshulinError("下载文件保存的根目录不能为空");
		}
		if(url!=null){
			///如果已经加入到下载队列中，则再次点击不生成新的线程
			if(downLoadThreadManageList.contains(new DownLoadThreadManageVO(url))){
				return;
			}
			DownLoadTask downloadThread=null;
			if(downLoadFileName==null){
				downloadThread=	new DownLoadTask(context,url,downLoadItemId,userId,rootPath, mDownLoadCallback);
			}else{
				downloadThread=	new DownLoadTask(context,url, rootPath,downLoadItemId,userId,mDownLoadCallback,downLoadFileName);
			}
			ExecuteDownLoadThread(context,url,downloadThread);
			
		}
	}
	
	/***
	 * 将下载任务添加到下载任务列表
	 */
	private void addDownLoadTaskTODownLoadList(String url,DownLoadTask downloadThread){
		///将正在安装的任务添加到安装任务列表中
		DownLoadThreadManageVO downLoadManage=new DownLoadThreadManageVO(url, downloadThread);
		downLoadThreadManageList.add(downLoadManage);
		if(downLoadThreadManageList.size()>maxNumThread){
			if (mDownLoadCallback != null){
				mDownLoadCallback.sendWaitingMessage(url);
			}
		}

	}
	/***
	 * 从下载管理任务列表中移除下载任务
	 * @param url
	 */
	public void removeDownLoadTaskFromDownLoadList(String url){
		
		downLoadThreadManageList.remove(new DownLoadThreadManageVO(url));
	}
	/****
	 * 移除全部下载任务列表
	 */
	public void removeAllDownLoadTasks(){
		if(downLoadThreadManageList!=null){
			downLoadThreadManageList.clear();
		}
	}
	/***
	 * 添加下载线程到线程池 等待下载
	 * @param thread
	 */
	private void ExecuteDownLoadThread(Context context,String downloadUrl,DownLoadTask thread){
		if(threadPool!=null&&!threadPool.isShutdown()){
			addDownLoadTaskTODownLoadList(downloadUrl,thread);
			threadPool.submit(thread);
		}
	}
	
	/**
	 * 暂停，或下载完成均需要调用此方法
	 * 停止指定线程 删除该任务 删除掉该线程信息
	 * @param url
	 */
	public synchronized void CancelThreadByUrl(String url){
		DownLoadThreadManageVO downLoadThreadManageVO=	new DownLoadThreadManageVO(url);
		if(downLoadThreadManageList.contains(downLoadThreadManageVO)){
			downLoadThreadManageVO=	downLoadThreadManageList.get(downLoadThreadManageList.indexOf(downLoadThreadManageVO));
			downLoadThreadManageVO.getDownLoadTask().pauseCurrentThread();
			removeDownLoadTaskFromDownLoadList(url);
		}
	}
	/**
	 * 关闭所有下载线程 
	 * @param url
	 */
	public  void CloseDownLoadThreadPool(){
		removeAllDownLoadTasks();
		for(DownLoadThreadManageVO downLoadThreadManageVO:downLoadThreadManageList){
			downLoadThreadManageVO.getDownLoadTask().pauseCurrentThread();
		}
//		if(threadPool!=null){
//			threadPool.shutdown();
//			threadPool=null;
//		}
	}
	
	/**
	 * 判断当前运行的任务中是否包含该任务
	 * @param url
	 * @return
	 */
	public boolean isContainDownLoadTaskByUrl(String url){
			return downLoadThreadManageList.contains(new DownLoadThreadManageVO(url));
	}
	/***
	 * 获取根目录路径
	 * @return
	 */
	private String getRootPath()
	{
		return rootPath;
	}
	/***
	 * 设置回调函数
	 * @param downLoadCallback
	 */
	public void setDownLoadCallback(DownLoadCallback downLoadCallback)
	{
		this.mDownLoadCallback = downLoadCallback;
	}

	private class DownLoadThreadManageVO{
		
		private DownLoadTask downLoadThread;
		private String url;
		
		public DownLoadThreadManageVO(String url,
				DownLoadTask downloadThread) {
			// TODO Auto-generated constructor stub
			this.downLoadThread=downloadThread;
			this.url=url;
		}
		
		
		public DownLoadThreadManageVO(String url) {
			super();
			this.url = url;
		}


		public DownLoadTask getDownLoadTask() {
			return downLoadThread;
		}
		public void setDownLoadTask(DownLoadTask downLoadThread) {
			this.downLoadThread = downLoadThread;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}


		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((url == null) ? 0 : url.hashCode());
			return result;
		}


		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DownLoadThreadManageVO other = (DownLoadThreadManageVO) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (url == null) {
				if (other.url != null)
					return false;
			} else if (!url.equals(other.url))
				return false;
			return true;
		}


		private DownLoadManager getOuterType() {
			return DownLoadManager.this;
		}
		
	}

}
