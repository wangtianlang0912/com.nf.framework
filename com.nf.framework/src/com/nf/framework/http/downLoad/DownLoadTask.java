package com.nf.framework.http.downLoad;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;

import android.content.Context;
import android.text.TextUtils;

import com.ApricotforestStatistic.Service.ApricotStatisticAgent;
import com.nf.framework.JSONDataUtils;
import com.nf.framework.exception.LogUtil;
import com.nf.framework.exception.XingshulinError;
import com.nf.framework.netdata.BaseObjectVO;
import com.nf.framework.netdata.NewReturnDataUtil;
import com.nf.framework.util.StringUtil;
import com.nf.framework.util.io.FileUtils;
import com.nf.framework.util.io.SdCardUtil;

public class DownLoadTask implements Runnable {

	private String tempFilePath;// 临时文件存储路径
	private File tempFile;// 临时下载文件
	private static final int INIT = 1;// 定义三种下载的状态：初始化状态，正在下载状态，暂停状态
	private static final int PAUSE = 3;
	private int state = INIT;
	private static final String SUFFIX = ".downLoad";
	private DownLoadCallback mhandler;
	private Context mcontext;
	private String downLoadUrl;
	private int downLoadItemId;
	private int userId;
	private String downLoadFileName = "";

	/***
	 * 
	 * @param mcontext
	 * @param downLoadUrl
	 *            含有下载文件名称的直接下载地址
	 * @param tempFilePath
	 *            临时文件存储目录
	 * @param mhandler
	 */
	public DownLoadTask(Context mcontext, String downLoadUrl,
			int downLoadItemId, int userId, String tempFilePath,
			DownLoadCallback mhandler) {
		this.mcontext = mcontext;
		this.downLoadUrl = downLoadUrl;
		this.tempFilePath = tempFilePath;
		this.mhandler = mhandler;
		this.downLoadFileName = getDownsLoadFileName(downLoadUrl);
		this.downLoadItemId = downLoadItemId;
		this.userId = userId;
		checkTempFileFolderPath(tempFilePath);
	}

	/***
	 * 
	 * @param mcontext
	 * @param downLoadUrl
	 *            不含文件名称的间接下载地址
	 * @param tempFilePath
	 *            临时文件存储目录
	 * @param mhandler
	 * @param downLoadFileName
	 *            自定义文件名称
	 */
	public DownLoadTask(Context mcontext, String downLoadUrl,
			String tempFilePath, int downLoadItemId, int userId,
			DownLoadCallback mhandler, String downLoadFileName) {
		this.mcontext = mcontext;
		this.downLoadUrl = downLoadUrl;
		this.tempFilePath = tempFilePath;
		this.mhandler = mhandler;
		this.downLoadFileName = downLoadFileName;
		this.downLoadItemId = downLoadItemId;
		this.userId = userId;
		checkTempFileFolderPath(tempFilePath);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		reset();
		HttpURLConnection connection=null;
		try {
			mhandler.sendStartMessage(downLoadUrl);
			
			if(StringUtil.isEmpty(downLoadUrl)){
				LogUtil.e(mcontext, "请求地址获取失败");
				return;
			}
			// 先判断当前是否存在该下载文件 如果存在则直接返回下载完成
			boolean fileExist = isExistDownLoadFile(downLoadUrl);
			if (fileExist) {
				mhandler.sendSuccessMessage(downLoadUrl,getDownLoadFile(downLoadUrl).getPath());
				return;
			}
			// 如果不存在该下载文件 则判断是否正在下载该文件，数据库是否已保存数据
			DownLoadInfoVO downLoadInfo = getDownLoadTempFileData(mcontext,downLoadItemId, userId);
			// 下载文件
			if (downLoadInfo == null) {// 尚未下载过该数据
				connection = getHttpURLConnectionByUrl(downLoadUrl,0);
			}else{
				connection = getHttpURLConnectionByUrl(downLoadUrl, downLoadInfo.getStartPos() + downLoadInfo.getCompeleteSize());
			}
			if (connection.getResponseCode() == HttpStatus.SC_OK) {
				SuccessRequestResultUtil(connection, downLoadInfo);
				
			}else{// http error code 
				//  send error message to ui
				mhandler.sendFailureMessage(downLoadUrl,DownLoadCallback.ERROR_NETFAILURE_REFER);
				//  static current urlPath info
				ApricotStatisticAgent.onErrorRequestUrl(mcontext, downLoadUrl, connection.getResponseCode(), true);
			}
		} catch (Exception e) {
			mhandler.sendFailureMessage(downLoadUrl,
					DownLoadCallback.ERROR_NETFAILURE_REFER);
			throw  new XingshulinError(mcontext,e);
		}finally{
			if(connection!=null)
			connection.disconnect();
			connection=null;
			
			mhandler.sendFinishMessage(downLoadUrl);
		}
	}

	/***
	 * 返回下载连接对象
	 * @param urlPath
	 * @param startRange
	 * @return
	 * @throws IOException
	 */
	private HttpURLConnection getHttpURLConnectionByUrl(String urlPath,int startRange) throws IOException{
		if(StringUtil.isEmpty(urlPath)){
			return null;
		}
		// 下载文件
		HttpURLConnection connection = null;
		URL url = new URL(urlPath);
		connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(20000);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Accept-Encoding", "musixmatch");
		connection.setRequestProperty("Accept-Language", "zh-CN");
		connection.setRequestProperty("Referer", url.toString());
		connection.setRequestProperty("Charset", "UTF-8");
		connection.setRequestProperty("Connection", "Keep-Alive");
		String rangeStr = "bytes=" + startRange + "-";
		// 设置范围，格式为Range：bytes x-y;
		connection.setRequestProperty("Range", rangeStr);
		return connection;
	}
	
	/**
	 * 返回请求文件的md5值
	 * @param connection
	 * @return
	 */
	private String getFileMD5(HttpURLConnection connection){
		if(connection!=null){
			return connection.getHeaderField("Content-MD5");
		}
		return null;
	}
	
	 private  void printResponseHeader(HttpURLConnection http) throws UnsupportedEncodingException {
	        Map<String, String> header = getHttpResponseHeader(http);
	        for (Map.Entry<String, String> entry : header.entrySet()) {
	            String key = entry.getKey() != null ? entry.getKey() + ":" : "";
	            LogUtil.i(mcontext, key + entry.getValue());
	        }
	    }
	     
	    private  Map<String, String> getHttpResponseHeader(
	            HttpURLConnection http) throws UnsupportedEncodingException {
	        Map<String, String> header = new LinkedHashMap<String, String>();
	        for (int i = 0;; i++) {
	        	
	            String mine = http.getHeaderField(i);
	            if (mine == null)
	                break;
	            header.put(http.getHeaderFieldKey(i), mine);
	        }
	        return header;
	    }
	/***
	 *  
	 * @param connection
	 * @param downLoadInfo
	 * @throws Exception
	 */
	private void SuccessRequestResultUtil(HttpURLConnection connection,DownLoadInfoVO downLoadInfo) throws Exception{
		
		String contentType = connection.getContentType();
		///content type is text
		if (contentType != null && (contentType.startsWith("text/"))) {
			InputStream inStream = connection.getInputStream();
			String returnData=inputStream2String(inStream);
			contentRequestTypeIsText(returnData);
			inStream.close();
			inStream=null;
			// content type application ,download package
		} else if (contentType != null && contentType.startsWith("application/")){
			
			contentRequestTypeIsApplication(connection,downLoadInfo);
		}
	}
	/**
	 *  when return content type is text 
	 * @param returnData
	 * @throws IOException
	 */
	private void contentRequestTypeIsText(String returnData) throws IOException{
		if(returnData!=null){
			BaseObjectVO baseObject=NewReturnDataUtil.getBaseObjectResult(returnData);
			String obj=baseObject.getObj();
			String errorCode=JSONDataUtils.JSONDataToString(obj, "errorCode");
			if(errorCode.equals("20130313")){// 表示有新的版本需要升级
				mhandler.sendFailureMessage(	downLoadUrl,
						DownLoadCallback.ERROR_APPUPDATE_REFER);
			}else{
				String reason=JSONDataUtils.JSONDataToString(obj, "reason");
				mhandler.sendFailureMessage(downLoadUrl,reason);
			}
		}
	}
	/**
	 * when content type is application  ,excute this method
	 * 
	 *  if local save file size is not equal currectionContentLength ,delete local info and reload downLoadUrl
	 * @param connection
	 * @param downLoadInfo
	 * @throws IOException
	 */
	private void contentRequestTypeIsApplication(HttpURLConnection connection,DownLoadInfoVO downLoadInfo) throws IOException{
		int fileContentLength=connection.getContentLength();
		// if last request file contentLength !=0 && this.length error equal to last request 
		if(downLoadInfo!=null&&downLoadInfo.getTotalFileSize()!=0&&fileContentLength!=downLoadInfo.getTotalFileSize()){
			LogUtil.e(mcontext, "请求地址获取文件内容与上次不一致");
			//delete downloadTempFile 
			new File(getDownLoadTempFilePath(downLoadFileName)).delete();
			//delete current download records
			DownLoadFileProcressDAO.getInstance(mcontext).deleteByItemId(downLoadItemId, userId);
			// reload current urlPath
			connection=getHttpURLConnectionByUrl(downLoadUrl,0);
			fileContentLength=connection.getContentLength();
			downLoadInfo=null;
		}
		// if fileContentLength  size > the rest of sdcard
		// 如果当前sd卡的剩余空间比当前文件大
		SdCardUtil sdcardUtil = new SdCardUtil();
		if (sdcardUtil.isAvailableStorage(fileContentLength)) {
			if(downLoadInfo==null){
				downLoadInfo = new DownLoadInfoVO(0, 0, fileContentLength,
						0, downLoadUrl, downLoadItemId, userId);
				createTempFile(downLoadFileName);
				// 保存记录
				DownLoadFileProcressDAO.getInstance(mcontext).saveInfo(downLoadInfo);
			}
		} else {
			// return unAvailable Storage
			mhandler.sendFailureMessage(
					downLoadUrl,
					DownLoadCallback.ERROR_NOENOUGHSTORGE_ERROR);
			return;
		}
		
		ProgressReportingRandomAccessFile	randomAccessFile = new ProgressReportingRandomAccessFile(tempFile,
				"rwd", downLoadInfo.getCompeleteSize(),downLoadInfo.getTotalFileSize());
		randomAccessFile.seek(downLoadInfo.getStartPos()+ downLoadInfo.getCompeleteSize());
		// 将要下载的文件写到保存在保存路径下的文件中
		InputStream	is = connection.getInputStream();
		byte[] buffer = new byte[4096];
		int length = -1;
		int compeleteSize = downLoadInfo.getCompeleteSize();
		
		while ((length = is.read(buffer)) != -1) {
			randomAccessFile.write(buffer, 0, length);
			compeleteSize += length;
			// 更新数据库中的下载信息
			DownLoadFileProcressDAO.getInstance(mcontext).updataInfos(0,compeleteSize, downLoadInfo.getUrl());
			if (state == PAUSE) {
				break;
			}
			if (compeleteSize == downLoadInfo.getTotalFileSize()) {// 文件下载完毕，断开循环
				break;
			}
		}
		is.close();
		is=null;
		if (state == PAUSE) {
			mhandler.sendPauseMessage(downLoadUrl);
			return;
		}
		///normal 正常情况可视为已下载完成
		
		deleteCurrentTask(downLoadInfo.getUrl(),	downLoadInfo.getUserId());
		// /判断下载文件是否完成，完成回到成功方法
		if (tempFile.length()!= downLoadInfo.getTotalFileSize()) {
			
			mhandler.sendFailureMessage(downLoadInfo.getUrl(),DownLoadCallback.ERROR_ContentLength_ERROR);	
			return;
		}
		// Md5 校验
		String md5=getFileMD5(connection);
		if(!TextUtils.isEmpty(md5)){
			if(!MD5Util.getFileMD5String(tempFile).equals(md5)){
				
				mhandler.sendFailureMessage(downLoadInfo.getUrl(),DownLoadCallback.ERROR_MD5_ERROR);	
				return;
			}
		}
		//经过校验可认定为下载成功
		File downLoadFile = getDownLoadFile(downLoadInfo.getUrl());
		tempFile.renameTo(downLoadFile);
		mhandler.sendSuccessMessage(downLoadInfo.getUrl(),downLoadFile.getPath());
	}
	
	/**
	 * 判断是否存在当前的下载文件
	 * 
	 * @return
	 */
	private boolean isExistDownLoadFile(String url) {

		File downLoadFile = getDownLoadFile(url);
		return downLoadFile.exists();
	}

	/**
	 * 判断是否数据库中存在下载的记录，并且本地已含有该临时文件
	 * 
	 * 如果临时文件存在，下载记录存在，则进入断点续传状态
	 * 
	 * 如果有任何一项不存在则返回null ，视为未下载过状态
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	private DownLoadInfoVO getDownLoadTempFileData(Context context,
			int downloadItemId, int userId) throws Exception {
			File tempFile = new File(getDownLoadTempFilePath(downLoadFileName));
			if (!tempFile.exists()) {
				DownLoadFileProcressDAO.getInstance(context).deleteByItemId(downLoadItemId, userId);
				return null;
			}
		// /获取当前下载项的本地下载信息
			boolean isHas = DownLoadFileProcressDAO.getInstance(context)
					.isHasInfors(downloadItemId, userId);
			if (!isHas) {
				tempFile.delete();
				return null;
			}
			List<DownLoadInfoVO> downloadInfoList = DownLoadFileProcressDAO
					.getInstance(context).getInfos(downloadItemId, userId);
			// /下载数据为空表示未记录下载数据
			if (downloadInfoList.isEmpty()) {
				tempFile.delete();
				return null;
			}
			long tempFileSize =FileUtils.getInstance().getFileSizes(tempFile);;
			if (tempFileSize != 0) {
				DownLoadInfoVO downLoadInfo = downloadInfoList.get(0);
				if (downLoadInfo.getTotalFileSize() == 0
						|| downLoadInfo.getCompeleteSize() >= downLoadInfo
								.getTotalFileSize()
						|| downLoadInfo.getCompeleteSize() != tempFileSize) {
					DownLoadFileProcressDAO.getInstance(context)
							.deleteByItemId(downLoadItemId, userId);
					tempFile.delete();
					return null;
				} else{
					this.tempFile=tempFile;
					return downLoadInfo;
				}
			}
			return null;
	}

	/***
	 * 删除数据库中urlstr对应的下载器信息
	 * 
	 * @param urlstr
	 */
	private void deleteCurrentTask(String urlstr, int userId) {
		DownLoadFileProcressDAO.getInstance(mcontext).delete(urlstr, userId);
	}

	/****
	 * 设置暂停
	 */
	public void pauseCurrentThread() {
		state = PAUSE;
	}

	// 重置下载状态
	private void reset() {
		state = INIT;
	}

	/**
	 * 判断是否存在临时文件存储路径，不存在则创建
	 * 
	 * @param tempFilePath
	 */
	private void checkTempFileFolderPath(String tempFilePath) {
		if (tempFilePath != null) {
			File tempFileFolder = new File(tempFilePath);
			if (!tempFileFolder.exists()) {
				tempFileFolder.mkdirs();
			}
		}
	}

	/***
	 * 创建临时下载文件
	 * 
	 * @param url
	 * @throws IOException 
	 */
	private void createTempFile(String downLoadFileName) throws IOException {
		if (downLoadFileName == null) {
			throw new XingshulinError("下载文件的文件名称不能为空");
		}
		this.tempFile = new File(getDownLoadTempFilePath(downLoadFileName));
		if (!tempFile.exists()) {
			tempFile.createNewFile();
		}
	}

	/***
	 * 获取临时下载文件
	 * 
	 * @param url
	 * @param fileSize
	 * @throws Exception
	 */
//	private long getDownLoadTempFileByFileName(String downLoadFileName)
//			throws Exception {
//		if (downLoadFileName == null) {
//			throw new XingshulinError("下载文件的文件名称不能为空");
//		}
//		this.tempFile = new File(getDownLoadTempFilePath(downLoadFileName));
//		if (tempFile.exists()) {
//			return FileHelper.getInstance().getFileSizes(tempFile);
//		}
//		return 0;
//	}

	/**
	 * 获取临时下载文件的路径
	 * 
	 * @param downLoadFileName
	 * @return
	 */
	private String getDownLoadTempFilePath(String downLoadFileName) {
		if (downLoadFileName == null) {
			throw new XingshulinError("下载文件的文件名称不能为空");
		}
		String tempFileName = downLoadFileName + SUFFIX;
		return tempFilePath + File.separator + tempFileName;

	}

	/**
	 * 下载文件的名称 适用于url地址后为文件名称的下载路径
	 * 
	 * @param url
	 * @return
	 */
	private String getDownsLoadFileName(String url) {
		if (url == null || !url.contains(".")) {
			throw new XingshulinError("该方法仅支持url地址为后为文件名称的下载路径");
		}
		return url.substring(url.lastIndexOf("/"));
	}

	/**
	 * 真正下载的文件 用于最终临时下载文件转换成原名
	 * 
	 * @param url
	 */
	private File getDownLoadFile(String url) {
		return getDownLoadFile(url, downLoadFileName);
	}

	/***
	 * 
	 * @param url
	 * @param downloadFileName
	 * @return
	 */
	private File getDownLoadFile(String url, String downloadFileName) {
		String downLoadFilePath = tempFilePath + File.separator
				+ downloadFileName;
		return new File(downLoadFilePath);
	}

	/***
	 * RandomAccessFile是用来访问那些保存数据记录的文件的，你就可以用seek(
	 * )方法来访问记录，并进行读写了。这些记录的大小不必相同；但是其大小和位置必须是可知的。但是该类仅限于操作文件。
	 * RandomAccessFile不属于InputStream和OutputStream类系的
	 * 。实际上，除了实现DataInput和DataOutput接口之外
	 * (DataInputStream和DataOutputStream也实现了这两个接口
	 * )，它和这两个类系毫不相干，甚至不使用InputStream和OutputStream类中已经存在的任何功能
	 * ；它是一个完全独立的类，所有方法(绝大多数都只属于它自己
	 * )都是从零开始写的。这可能是因为RandomAccessFile能在文件里面前后移动，所以它的行为与其它的I
	 * /O类有些根本性的不同。总而言之，它是一个直接继承Object的，独立的类。
	 * 基本上，RandomAccessFile的工作方式是，把DataInputStream和DataOutputStream结合起来
	 * ，再加上它自己的一些方法，比如定位用的getFilePointer( )，在文件里移动用的seek( )，以及判断文件大小的length(
	 * )、skipBytes()跳过多少字节数。此外，它的构造函数还要一个表示以只读方式("r")，还是以读写方式("rw")打开文件的参数
	 * (和C的fopen( )一模一样)。它不支持只写文件。
	 * 只有RandomAccessFile才有seek搜寻方法，而这个方法也只适用于文件。BufferedInputStream有一个mark(
	 * )方法，你可以用它来设定标记(把结果保存在一个内部变量里)，然后再调用reset( )返回这个位置，但是它的功能太弱了，而且也不怎么实用。
	 * 
	 * @author win7
	 * 
	 */
	private class ProgressReportingRandomAccessFile extends RandomAccessFile {
		private int progress = 0;
		private long previousFileSize;
		private long totalSize;
		private int downloadPercent;
		private double networkSpeed;
		private long previousTime;
		private long totalTime;
		private int lastDownloadPercent = 0;

		public ProgressReportingRandomAccessFile(File file, String mode,
				long previousFileSize, long totalSize)
				throws FileNotFoundException {
			super(file, mode);
			this.previousFileSize = previousFileSize;
			this.totalSize = totalSize;
		}

		@Override
		public void write(byte[] buffer, int offset, int count)
				throws IOException {

			super.write(buffer, offset, count);
			progress += count;

			totalTime = System.currentTimeMillis() - previousTime;
			previousTime = System.currentTimeMillis();
			downloadPercent = (int) ((progress + previousFileSize) * 100 / totalSize);
			networkSpeed = (double) count / (double) totalTime;
			if (lastDownloadPercent < downloadPercent) {
				lastDownloadPercent = downloadPercent;
				mhandler.onLoadingTaskOnUIThread(downLoadUrl,
						networkSpeed + "", downloadPercent);
			}
		}
	}
	/**
	 * 将inputStream转为String
	 * @param is
	 * @return
	 * @throws Exception
	 */
	private	String inputStream2String(InputStream is) throws Exception{
	    BufferedReader in = new BufferedReader(new InputStreamReader(is));
	    StringBuffer buffer = new StringBuffer();
	    String line = "";
	    while ((line = in.readLine()) != null){
	      buffer.append(line);
	    }
	    return buffer.toString();
	}
}