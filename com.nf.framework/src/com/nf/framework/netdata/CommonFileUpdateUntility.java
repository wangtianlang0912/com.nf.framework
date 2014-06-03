package com.nf.framework.netdata;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpStatus;
import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Xml;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nf.framework.CheckInternet;
import com.nf.framework.util.io.FileUtils;
import com.nf.framework.util.io.ZipUtil;

/***
 * 公共文件更新 处理类
 * 
 * @author niufei
 * 
 */

public class CommonFileUpdateUntility {

	private Context mcontext;
	private String sessionKey;
	private String configFilePath;
	private String descFileFolderPath;
	private String tempFilePath;
	private CommonFileUpdatedCallBack mCallBack;
	public CommonFileUpdateUntility(Context context, String sessionKey,
			String configFilePath,CommonFileUpdatedCallBack callBack) {
		this.mcontext = context;
		this.sessionKey = sessionKey;
		this.configFilePath=configFilePath;
		this.descFileFolderPath=new File(configFilePath).getParent();
		tempFilePath=descFileFolderPath+File.separator+"temp.zip";
		this.mCallBack=callBack;
	}

	public CommonFileUpdateUntility() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 检测是否有更新
	 * 
	 * @return
	 */
	public void CheckNewCommonFileDataAsyncTask() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				CommonFileVO localCommonFileVO=new CommonFileVO();
				File configFile=new File(configFilePath);
				if(configFile.exists()){
					localCommonFileVO=getCommonFileVO(configFile);
				}
				if(TextUtils.isEmpty(localCommonFileVO.getTypeInfo())){
					return;
				}
				// 查询服务端是否有更新消息
				String returnData = new CommonProHttpRequestService(mcontext)
						.GetFileUpdatePacketIsNewServcice(sessionKey,String.valueOf(localCommonFileVO.getFileVersion()),
								localCommonFileVO.getTypeInfo());
				if (!TextUtils.isEmpty(returnData)) {
					BaseObjectVO baseObject = NewReturnDataUtil.getBaseObjectResult(returnData);
					if (baseObject != null && baseObject.isResultObj()) {// 表示网络服务器端没有更新
						String json = baseObject.getObj();
						if (!TextUtils.isEmpty(json)&&descFileFolderPath!=null) {
//							CommonFileVO newCommonFileVO=getCommonFileVoFromJSON(json);
							CommonFileVO newCommonFileVO=getNewCommonFileVO(localCommonFileVO, json);
							if(newCommonFileVO.getFileVersion()>localCommonFileVO.getFileVersion()){
								UpdateData(String.valueOf(newCommonFileVO.getFileVersion()),newCommonFileVO.getTypeInfo(),tempFilePath, descFileFolderPath);
							}
						}
					}
				}
			}
		}).start();

	}

	/**
	 * 获取更新数据 并执行解压 
	 */
	private void UpdateData(String dataVer,String typeInfo,final String tempFilePath, final String descFileFolderPath) {
		// 联网状态下获取更新包
		if (CheckInternet.getInstance(mcontext).checkInternet()) {// 联网状态下

			String downloadUrl = new CommonProHttpRequestService(mcontext)
					.getDownloadFileUpdatePacketUrl(sessionKey, dataVer,typeInfo);
			DownLoadProgressTask downLoadProTask = new DownLoadProgressTask(
					mcontext, downloadUrl, tempFilePath,new DownLoadTaskExcuteFinishedCallBack() {
						
						@Override
						public void onDownLoadTaskExcuteFinished() {
							// TODO Auto-generated method stub
							// 下载完成后执行解压 安装操作
							// 解压全部文件到临时目录
							new Thread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									try {
										ZipUtil.unZipFiles(tempFilePath,descFileFolderPath);
										FileUtils.getInstance().deleteFile(tempFilePath);
										if(mCallBack!=null){
											mCallBack.onCommonFileUpdated();
										}
									
									} catch (IOException e) {
										// TODO Auto-generated catch block
									}
								}
							}).start();
						}
					});
			downLoadProTask.execute();
		}
	}
	/**
	 * 异步线程获取数据
	 */
	private class DownLoadProgressTask extends
			AsyncTask<String, Integer, String> {
		String urlPath, savePath;
		Context mcontext;
		private int cachePoolSize = 4096;// 缓冲池大小默认为4096
		private DownLoadTaskExcuteFinishedCallBack mCallBack;

		/**
		 * 普通下载
		 * 
		 * @param mcontext
		 * @param urlPath
		 * @param savePath
		 */
		public DownLoadProgressTask(Context mcontext, String urlPath,
				String savePath, DownLoadTaskExcuteFinishedCallBack callBack) {
			this.mcontext = mcontext;
			this.urlPath = urlPath;
			this.savePath = savePath;
			this.mCallBack = callBack;
		}

		@Override
		public String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String resultStr = null;
			int readCount = 0, readSum = 0;
			int StreamLen = 0;// 数据流总长度
			HttpURLConnection connection = null;
			InputStream inStream = null;
			try {
				URL url = new URL(urlPath);
				// 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
				// 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
				connection = (HttpURLConnection) url.openConnection();
				// 进行连接，但是实际上getrequest要在下一句的connection.getInputStream()函数中才会真正发到服务器
				connection.connect();
				connection.setConnectTimeout(20000);
				if (connection.getResponseCode() == HttpStatus.SC_OK) {
					String contentType = connection.getContentType();
					inStream = connection.getInputStream();
					if (contentType != null
							&& contentType.startsWith("application/")) {// application/zip
						StreamLen = connection.getContentLength();
						OutputStream output = new FileOutputStream(savePath);
						BufferedInputStream b = new BufferedInputStream(
								inStream);
						byte[] buffer = new byte[cachePoolSize];// 4096
						while ((readCount = b.read(buffer)) > 0) {
							output.write(buffer, 0, readCount);
							readSum = readSum + readCount;
						}
						if (readSum == StreamLen) {// 下载完成
						}
						output.flush();
						output.close();
					}
				}
				if (inStream != null)
					inStream.close();
			} catch (Exception e) {
			}
			if (connection != null)
				connection.disconnect();
			return resultStr;
		}

		@Override
		public void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(mCallBack!=null){
				mCallBack.onDownLoadTaskExcuteFinished();
			}
		}
	}

	private  interface DownLoadTaskExcuteFinishedCallBack {

		public void onDownLoadTaskExcuteFinished();
	}

	public static interface CommonFileUpdatedCallBack{
		
		public void onCommonFileUpdated();
	}
	
	public CommonFileVO getCommonFileVO(File configFile){
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(configFile);
			return getCommonFileVO(inputStream);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}finally{
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
		return null;
	}
	/***
	 * 解析本地存储的配置文件
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	public CommonFileVO getCommonFileVO(InputStream inputStream) throws Exception{
		CommonFileVO commonFileVO = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inputStream, "UTF-8");
		
		int event = parser.getEventType();//产生第一个事件
		while(event!=XmlPullParser.END_DOCUMENT){
			switch(event){
			case XmlPullParser.START_DOCUMENT://判断当前事件是否是文档开始事件
				commonFileVO = new CommonFileVO();//初始化commonFileVO集合
				break;
			case XmlPullParser.START_TAG://判断当前事件是否是标签元素开始事件
				if(commonFileVO!=null){
					if(CommonFileVO.FILEVERSION.equals(parser.getName())){//判断开始标签元素是否是fileVersion
						commonFileVO.setFileVersion(Integer.valueOf(parser.nextText()));
					}else if(CommonFileVO.FILENAME.equals(parser.getName())){//判断开始标签元素是否是fileName
						commonFileVO.setFileName(parser.nextText());
					}else if(CommonFileVO.CREATEDATE.equals(parser.getName())){//判断开始标签元素是否是createDate
						commonFileVO.setCreateDate(parser.nextText());
					}else if(CommonFileVO.TYPEINFO.equals(parser.getName())){//判断开始标签元素是否是typeInfo
						commonFileVO.setTypeInfo(parser.nextText());
					}
				}
				break;
			case XmlPullParser.END_TAG://判断当前事件是否是标签元素结束事件
				break;
			}
			event = parser.next();//进入下一个元素并触发相应事件
		}//end while
		return commonFileVO;
	}
	
	private CommonFileVO getNewCommonFileVO(CommonFileVO commonFile,String json){
		CommonFileVO newCommonFile=new CommonFileVO();
		newCommonFile.setFileVersion(Integer.valueOf(json));
		newCommonFile.setCreateDate(commonFile.getCreateDate());
		newCommonFile.setFileName(commonFile.getFileName());
		newCommonFile.setTypeInfo(commonFile.getTypeInfo());
		return newCommonFile;
	}
	/***
	 * 解析json
	 * @param json
	 * @return
	 */
	private CommonFileVO getCommonFileVoFromJSON(String json){
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		Type type = new TypeToken<CommonFileVO>(){}.getType();
		return gson.fromJson(json,type);
	}
	
	public static class CommonFileVO{
		
		public static final String FILENAME="fileName";
		public static final String CREATEDATE="createDate";
		public static final String TYPEINFO="typeInfo";
		public static final String FILEVERSION="fileVersion";
		String fileName;
		String createDate;
		String typeInfo;
		int fileVersion;
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		public String getCreateDate() {
			return createDate;
		}
		public void setCreateDate(String createDate) {
			this.createDate = createDate;
		}
		public String getTypeInfo() {
			return typeInfo;
		}
		public void setTypeInfo(String typeInfo) {
			this.typeInfo = typeInfo;
		}
		public int getFileVersion() {
			return fileVersion;
		}
		public void setFileVersion(int fileVersion) {
			this.fileVersion = fileVersion;
		}
		
	}
}

