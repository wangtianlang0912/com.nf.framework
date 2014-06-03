package com.ApricotforestStatistic.Service;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.ApricotforestStatistic.StaticConfig;
import com.ApricotforestStatistic.VO.StaticBaseVO;
import com.ApricotforestStatistic.VO.StatisticExtandVO;
import com.ApricotforestStatistic.sqlite.SqlFileModifedCallBack;
import com.ApricotforestStatistic.sqlite.StaticErrorRequestDAO;
import com.ApricotforestStatistic.sqlite.StaticEventDAO;
import com.ApricotforestStatistic.sqlite.StaticPageViewDAO;
import com.ApricotforestStatistic.sqlite.StatisticExtandDataCallBack;
import com.nf.framework.CheckInternet;
import com.nf.framework.exception.LogUtil;
import com.nf.framework.util.io.FileUtils;

class ApricotBaseAgent {

	/***
	 * 是否关闭统计功能
	 */
	protected static boolean isCloseStatic = false;

	public static final String relationFile = "staticBaseRelation";

	/**
	 * 用于软件开启时，上传上一次使用的文件数据 判断是否存在需要上传的文件数据库 读取数据库表信息，获取上传json数据 异步上传
	 * @param mcontext
	 * @param niufei
	 * @param 2014-3-26 下午3:13:05
	 * @return void
	 * @throws
	 */
	public void onInitStatisticData(Context mcontext){
		onInitStatisticData(mcontext,null);
	}
	/**
	 * 用于软件开启时，上传上一次使用的文件数据 判断是否存在需要上传的文件数据库 读取数据库表信息，获取上传json数据 异步上传
	 * mcallBack 业务模块数据回调
	 * @param mcontext
	 */
	public void onInitStatisticData(Context mcontext,StatisticExtandDataCallBack mcallBack) {
		if (mcontext == null) {
			return;
		}
		openAllStaticEvent();
		try {
			// 删除旧数据
			new Thread(new deleteFileFolderThread(mcontext)).run();
			// /将上次或其他（超时）保存的信息修改为upload状态，
			reNameAllPreUpLoadingStaticFile(mcontext);
			if (CheckInternet.getInstance(mcontext).checkInternet()) {
				new Thread(new UploadStaticDataEventTask(mcontext,mcallBack)).start();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}

	/***
	 * 重命名所有需要上传的文件名称
	 * 
	 * @param context
	 */
	protected void reNameAllPreUpLoadingStaticFile(Context context) {
		if (isCloseStatic) {
			return;
		}
		// 查询所有等待上传的文件路径
		List<String> preUpLoadlist = queryPreUpLoadFiles(context);
		for (String preUpLoadFilePath : preUpLoadlist)
			reNamePreUpLoadingStaticFile(context, preUpLoadFilePath);
	}

	/**
	 * 重命名 需要上传的文件名称 并且删除临时文件
	 */
	protected void reNamePreUpLoadingStaticFile(Context context,
			String currentDbFile) {
		if (context == null || currentDbFile == null) {
			return;
		}
		try {
			StaticPageViewDAO.getInstance().UpdateAllPageViewRecord(context,
					currentDbFile);
			File currentFile = currentDbFile != null ? new File(currentDbFile)
					: null;
			if (currentFile != null) {
				String currentFileName = currentDbFile.substring(currentDbFile
						.lastIndexOf(File.separator) + 1);
				String upLoadFileName = getUpLoadDbFileName(currentFileName);
				String upLoadFilePath = currentDbFile.substring(0,
						currentDbFile.lastIndexOf(File.separator) + 1)
						+ upLoadFileName;
				File upLoadFile = upLoadFilePath != null ? new File(
						upLoadFilePath) : null;
				StatisticBaseDataService.reNameFileTOUpLoadFileName(
						currentFile, upLoadFile);
			}
		} catch (Exception e) {

		}
	}

	/**
	 * 关闭统计功能
	 */
	public void closeAllStaticEvent() {
		isCloseStatic = true;
	}

	/**
	 * 开启统计功能
	 */
	public void openAllStaticEvent() {
		isCloseStatic = false;
	}

	/****
	 * 检索 上传需要上传的数据
	 * 
	 * @author niufei
	 * 
	 */
	class UploadStaticDataEventTask implements Runnable {
		Context context;
		StatisticExtandDataCallBack mcallBack;
		UploadStaticDataEventTask(Context context,StatisticExtandDataCallBack mcallBack) {
			this.context = context;
			this.mcallBack=mcallBack;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 判断是否存在需要上传的文件数据库
			List<String> upLoadDbFilePaths = getAllUpLoadFiles(context);
			for (String upLoadDbFilePath : upLoadDbFilePaths) {
				try {
					// 存在 获取数据库文件数据
					StaticBaseVO staticBaseVO = StatisticBaseDataService
							.getStaticBaseVO(context, upLoadDbFilePath);
					if (staticBaseVO == null) {
						LogUtil.e(context, "空数据库____________"
								+ upLoadDbFilePath);
						new File(upLoadDbFilePath).delete();
						continue;
					}
					StatisticExtandVO statisticExtand=getExtandData(mcallBack, upLoadDbFilePath);
					
					staticBaseVO.setExtandList(statisticExtand!=null?statisticExtand.getExtandList():null);

					String staticData = StatisticBaseDataService
							.StaticBaseVOToJson(staticBaseVO);
					LogUtil.w(context, staticData);
					// 上传数据
					if (!TextUtils.isEmpty(staticData)) {
						ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
						list.add(new BasicNameValuePair("m", "record"));
						list.add(new BasicNameValuePair("data", staticData));
						int responseCode = getDataByHttp(
								StaticConfig.staticUrl, list);
						if (responseCode == 200) {
							// 路径为文件且不为空则进行删除
							File upLoadFile = new File(upLoadDbFilePath);
							if (upLoadFile.isFile() && upLoadFile.exists()) {
								LogUtil.d(context, "upload________________"
										+ upLoadDbFilePath);
								upLoadFile.delete();
							}
							if(mcallBack!=null){
								mcallBack.afterStatisticDataCompleted
								(statisticExtand!=null?statisticExtand.getExtandFilePath():null);
							}
						} else {
							LogUtil.d(context, "ApricotBase___upload error___"+responseCode);
							break;
						}
					}
				} catch (Exception e) {
					
					LogUtil.d(context,e.getStackTrace().toString());
					// 路径为文件且不为空则进行删除
					File upLoadFile = new File(upLoadDbFilePath);
					if (upLoadFile.isFile() && upLoadFile.exists()) {
						upLoadFile.delete();
					}
				} finally {
					// /判断文件夹删除
					File upLoadFileFolder = new File(upLoadDbFilePath)
							.getParentFile();
					if (upLoadFileFolder.isDirectory()) {
						File[] files = upLoadFileFolder.listFiles();
						if (files == null || files.length == 0) {
							upLoadFileFolder.delete();
						}
					}
				}
			}
		}

		private StatisticExtandVO getExtandData(StatisticExtandDataCallBack mcallBack ,String statisticDbFilePath) {
			if (mcallBack != null) {
				return mcallBack.extandDataCallBack(statisticDbFilePath);
			}
			return null;
		}

		/**
		 * 
		 * @param urlStr
		 */
		private void SaveInterfaceUrlToFile(String urlStr) {
			FileUtils fileHelper = FileUtils.getInstance();
			try {
				File file = new File(Environment.getExternalStorageDirectory()
						.getPath() + "/test.txt");
				if (!file.exists()) {
					file.createNewFile();
				}
				String fileStr = fileHelper.read(file);
				StringBuffer sb2 = new StringBuffer(fileStr);
				sb2.append("\r\n");
				sb2.append(urlStr);
				fileHelper.write(file, sb2.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/**
		 * post方法传值
		 * 
		 * @param url
		 * @param list
		 * @return
		 * @throws Exception
		 *             1.创建HttpGet或HttpPost对象，将要请求的URL通过构造方法传入HttpGet或HttpPost对象
		 *             。 2.使用DefaultHttpClient类的execute方法发送HTTP GET或HTTP
		 *             POST请求，并返回HttpResponse对象。
		 *             3.通过HttpResponse接口的getEntity方法返回响应信息，并进行相应的处理。
		 */
		public int getDataByHttp(String url, ArrayList<NameValuePair> list) {
			int statusCode = 0;
			String result = null;
			HttpPost httpdata = new HttpPost(url);
			// 生成一个http客户端对象
			HttpClient httpClient = new DefaultHttpClient();// 发送请求
			try {
				// 设置连接超时时间(单位毫秒)
				httpClient.getParams().setIntParameter(
						CoreConnectionPNames.CONNECTION_TIMEOUT, 15000);
				// 设置读数据超时时间(单位毫秒)
				httpClient.getParams().setIntParameter(
						CoreConnectionPNames.SO_TIMEOUT, 20000);
				httpdata.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));
				HttpResponse httpResponse = httpClient.execute(httpdata);// 接收响应
				statusCode = httpResponse.getStatusLine().getStatusCode();
				// 判断请求是否成功处理
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					// 解析返回的内容
					result = EntityUtils.toString(httpResponse.getEntity(),
							HTTP.UTF_8);
				} else {
					httpClient.getConnectionManager().shutdown();
					Log.e("AbstractGetDataFromService.getDataByHttp()",
							httpResponse.getStatusLine().getStatusCode() + "");
				}
			} catch (Exception e) {
			}
			httpClient.getConnectionManager().shutdown();
			httpdata = null;
			httpClient = null;
			return statusCode;
		}
	}
	/**
	 * 将统计事件数据保存到本地数据库中
	 * 
	 * 通过实现Runnable接口创建一个线程
	 * 
	 * @author DreamSea
	 */
	class InsertOrUpdateEventThread implements Runnable {
		Context context;
		String eventName;
		String relatedParam;
		int userId;

		InsertOrUpdateEventThread(Context mcontext, String eventName,
				int userId, String relatedParam) {
			this.context = mcontext;
			this.eventName = eventName;
			this.relatedParam = relatedParam;
			this.userId = userId;
		}

		public void run() {

			String dbFileName = getCurrentDbFile(context);
			StaticEventDAO.getInstance().insertOrUpdateEvent(context,
					dbFileName, eventName, String.valueOf(userId),
					relatedParam, sqlFileCallBack);
		}
	}

	/**
	 * 将页面的统计数据保存到本地数据库中
	 * 
	 * 通过实现Runnable接口创建一个线程
	 * 
	 * @author DreamSea
	 */
	class InsertPageViewThread implements Runnable {
		Context context;
		String currentViewCode;
		int userId;

		InsertPageViewThread(Context mcontext, String currentViewCode,
				int userId) {
			this.context = mcontext;
			this.currentViewCode = currentViewCode;
			this.userId = userId;
		}

		public void run() {

			String dbFileName = getCurrentDbFile(context);
			try {
				StaticPageViewDAO.getInstance().insertPageView(context,
						dbFileName, currentViewCode, userId, sqlFileCallBack);
			} catch (SQLException e) {
				// TODO Auto-generated catch block

			}
		}
	}

	/***
	 * 请求异常时错误统计 通过实现Runnable接口创建一个线程
	 * 
	 * @author niufei
	 * 
	 */
	class InsertErrorRequestThread implements Runnable {
		Context context;
		String requestUrl, errorReponseCode;
		boolean isRequestByPost;

		InsertErrorRequestThread(Context mcontext, String requestUrl,
				String errorReponseCode, boolean isRequestByPost) {
			this.context = mcontext;
			this.requestUrl = requestUrl;
			this.errorReponseCode = errorReponseCode;
			this.isRequestByPost = isRequestByPost;
		}

		public void run() {

			String dbFileName = getCurrentDbFile(context);
			String occurTime = String.valueOf(System.currentTimeMillis());
			StaticErrorRequestDAO.getInstance().insertErrorRequest(context,
					dbFileName, requestUrl, errorReponseCode, occurTime,
					isRequestByPost, sqlFileCallBack);
		}
	}

	/**
	 * 修改页面的统计数据保存到本地数据库中
	 * 
	 * 通过实现Runnable接口创建一个线程
	 * 
	 * @author DreamSea
	 */
	class UpdatePageViewThread implements Runnable {
		Context context;
		String currentViewCode;
		int userId;

		UpdatePageViewThread(Context mcontext, String currentViewCode,
				int userId) {
			this.context = mcontext;
			this.currentViewCode = currentViewCode;
			this.userId = userId;
		}

		public void run() {

			String dbFileName = getCurrentDbFile(context);
			try {
				StaticPageViewDAO.getInstance().UpdatePageView(context,
						dbFileName, currentViewCode, sqlFileCallBack);
			} catch (SQLException e) {
				// TODO Auto-generated catch block

			}
		}
	}

	public SqlFileModifedCallBack sqlFileCallBack = new SqlFileModifedCallBack() {

		@Override
		public void AfterSqlFileModifed() {
		}
	};

	/***
	 * 获取当前使用的db文件路径 如果当前timeTiket有效 并且当前的db文件存在，则直接返回该路径
	 * timeTiket无效，将该文件改为等待上传文件，不存在重新创建该文件
	 * 
	 * @return
	 */
	protected String getCurrentDbFile(Context context) {

		String currentDbFile = ApricotStatisticSharePre.getInstance(context)
				.getCurrentDbFilePath();
		if (currentDbFile == null || !new File(currentDbFile).exists()) {
			return buildNewDbFile(context);
		}
		if (!checkTimeTicket(context)
				|| currentDbFile.contains(StaticConfig.proUpLoadPrefix)) {
			return buildNewDbFile(context);
		}
		return currentDbFile;
	}

	/**
	 * 重新创建新的数据库文件
	 * 
	 * @param context
	 * @param userId
	 * @return
	 */
	private String buildNewDbFile(Context context) {
		ApricotStatisticSharePre.getInstance(context).setTimeTicket(
				System.currentTimeMillis());
		String staticFolder = getStaticDbFolder(context);
		String currentDbFile = null;
		if (staticFolder != null) {
			currentDbFile = staticFolder + File.separator + getDbFileName();
		}
		ApricotStatisticSharePre.getInstance(context).setCurrentDbFilePath(
				currentDbFile);
		return currentDbFile;
	}

	/****
	 * 检查本地timeTicket是否有效， 有效则返回ture 无效 重新赋值并返回false
	 * 
	 * @return
	 */
	private boolean checkTimeTicket(Context context) {
		// 判断是否有timeTicket 并且上次保存的时间是否在时间范围内
		ApricotStatisticSharePre staticShare = ApricotStatisticSharePre
				.getInstance(context);
		long timeTicket = staticShare.getTimeTicket();
		long currentTime = System.currentTimeMillis();
		staticShare.setTimeTicket(currentTime);
		return checkTimeTicket(timeTicket, currentTime);
	}

	/**
	 * 判断当前时间票据是否有效 false无效 true 有效
	 * 
	 * @param timeTicket
	 * @param currentTime
	 * @return
	 */
	private boolean checkTimeTicket(long timeTicket, long currentTime) {
		if (timeTicket == 0 || (currentTime - timeTicket) > 10 * 60 * 1000) {// 10*60*1000时间为0或者已经超过十分钟
																				// 重新建库
			return false;
		}
		return true;
	}

	/**
	 * 本地的统计文件夹 2014.02.17修改统计文件保存目录为应用cache目录下
	 * 
	 * @return
	 */
	private String getStaticDbFolder(Context context) {
		String staticFolder = context.getCacheDir().getPath() + File.separator
				+ StaticConfig.StaticFolderName;
		if (!new File(staticFolder).exists()) {
			new File(staticFolder).mkdirs();
		}
		return staticFolder;
	}

	/**
	 * 当前使用的db文件名称
	 * 
	 * @return
	 */
	private String getDbFileName() {
		return System.currentTimeMillis() + "";// + dbSuffix;
	}

	/**
	 * 需要上传的db文件名称
	 * 
	 * @return
	 */
	private String getUpLoadDbFileName(String currentDbFileName) {
		return StaticConfig.proUpLoadPrefix + currentDbFileName;
	}

	/**
	 * 获取所有需要上传的文件路径
	 * 
	 * @return
	 */
	private List<String> getAllUpLoadFiles(Context context) {
		List<String> upLoadFile = new ArrayList<String>();
		String upLoadDbFileFolder = getStaticDbFolder(context);
		if (upLoadDbFileFolder == null) {
			return upLoadFile;
		}
		List<String> filePaths = queryFilePath(upLoadDbFileFolder);
		for (String filePath : filePaths) {
			try {
				String fileName = filePath.substring(filePath
						.lastIndexOf(File.separator) + 1);
				if (!fileName.contains("-journal")) {
					if (fileName.startsWith(StaticConfig.proUpLoadPrefix)
							&& !fileName.contains("-journal")) {
						upLoadFile.add(filePath);
					} else {
						long timeTicket = Long.valueOf(fileName);
						if (!checkTimeTicket(timeTicket,
								System.currentTimeMillis())) {
							String upLoadFilePath = filePath.substring(0,
									filePath.lastIndexOf(File.separator) + 1)
									+ getUpLoadDbFileName(fileName);
							System.out
									.println("ApricotAgent.getAllUpLoadFiles()"
											+ upLoadFilePath);
							StatisticBaseDataService
									.reNameFileTOUpLoadFileName(new File(
											filePath), new File(upLoadFilePath));
							upLoadFile.add(upLoadFilePath);
						}
					}
				}
			} catch (Exception e) {
				
				LogUtil.e(context, e.getStackTrace().toString());
			}
		}
		return upLoadFile;
	}

	/***
	 * 查询所有等待上传的文件路径
	 * 
	 * @param context
	 * @return
	 */
	private List<String> queryPreUpLoadFiles(Context context) {
		List<String> preUpLoadFileList = new ArrayList<String>();
		String upLoadDbFileFolder = getStaticDbFolder(context);
		if (upLoadDbFileFolder == null) {
			return preUpLoadFileList;
		}
		List<String> filePaths = queryFilePath(upLoadDbFileFolder);
		for (String filePath : filePaths) {
			String fileName = filePath.substring(filePath
					.lastIndexOf(File.separator) + 1);
			if (!fileName.startsWith(StaticConfig.proUpLoadPrefix)
					&& !fileName.contains("-journal")) {
				preUpLoadFileList.add(filePath);
			} else if (fileName.contains("-journal")) {
				// 删除临时文件
				deleteTempFile(context, filePath);
			}
		}
		return preUpLoadFileList;
	}

	/**
	 * 遍历db临时产生的journal文件 删除指定文件名称的临时文件
	 */
	private void deleteTempFile(Context context, String filePath) {
		if (filePath.endsWith("-journal")) {
			new File(filePath).delete();
		}
	}

	/**
	 * 遍历指定文件夹 返回所有文件路径
	 * 
	 * @param strPath
	 * @param
	 * @return
	 */
	private List<String> queryFilePath(String strPath) {
		List<String> filelist = new ArrayList<String>();
		File dir = new File(strPath);
		File[] files = dir.listFiles();
		if (files == null)
			return filelist;
		for (int i = 0; i < files.length; i++) {
			if (!files[i].isDirectory()) {
				filelist.add(files[i].getAbsolutePath());
			}
		}
		return filelist;
	}

	/****
	 * 异步删除文件夹，主要处理由于原数据结构产生的数据问题
	 * 
	 * @author niufei
	 * 
	 */
	class deleteFileFolderThread implements Runnable {
		Context context;

		public deleteFileFolderThread(Context context) {
			this.context = context;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String fileFolder = getStaticDbFolder(context);
			if (fileFolder == null) {
				return;
			}
			File dir = new File(fileFolder);
			File[] files = dir.listFiles();
			if (files == null) {
				return;
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					delFolder(files[i].getAbsolutePath());
				}
			}

		}

	}

	/***
	 * 删除文件夹
	 * 
	 * @param folderPath
	 *            文件夹完整绝对路径
	 */
	public void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除指定文件夹下所有文件 param path 文件夹完整绝对路径
	 */
	public boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}
	// /**
	// * 返回统计基础关联文件路径
	// * @param context
	// * @return
	// */
	// public static String getCurrentStaticBaseRelationFilePath(Context
	// context){
	//
	// String staticFolder= getStaticDbFolder(context);
	// if(staticFolder!=null){
	// return staticFolder+File.separator+relationFile;
	// }
	// return null;
	//
	// }
	// /**
	// * 返回统计基础关联文件
	// * @param context
	// * @return
	// */
	// public static File getCurrentStaticBaseRelationFile(Context context){
	// String
	// staticRelationFilePath=getCurrentStaticBaseRelationFilePath(context);
	// if(staticRelationFilePath!=null){
	// File staticRelationFile=new File(staticRelationFilePath);
	// if(!staticRelationFile.exists()){
	// try {
	// staticRelationFile.createNewFile();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// }
	// }
	// return staticRelationFile;
	// }
	// return null;
	// }
	// /**
	// * 软件统计init时使用该方法， 保存上次使用统计记录
	// * 先于writeToRelationProperties方法
	// * 将上次的文件，重命名
	// */
	// public static void reNameLastRelationProperty(Context context){
	// String currentFilePath= getCurrentStaticBaseRelationFilePath(context);
	// if(currentFilePath!=null){
	// //将文件重命名
	// File currentFile=new File(currentFilePath);
	// if(currentFile.exists()){
	// String staticFolder= getStaticDbFolder(context);
	// if(staticFolder!=null){
	// String
	// upLoadFilePath=staticFolder+File.separator+proUpLoadPrefix+relationFile+"_"+(int)(Math.random()*100);
	// File upLoadFile=new File(upLoadFilePath);
	// StaticBaseDataService.reNameFileTOUpLoadFileName(currentFile,
	// upLoadFile);
	// }
	// }
	// }
	// }
	// /**
	// * 当软件开启时，创建关联文件数据并将数据写入文件
	// * @param context
	// * @param currentTimeTicket
	// * @param deviceId
	// * @throws Exception
	// */
	// public static void writeToRelationProperties(Context context,long
	// currentTimeTicket) throws Exception{
	// List<StaticBaseRelateVO> staticBaseRelateList=new
	// ArrayList<StaticBaseRelateVO>();
	// StaticBaseRelateVO staticBaseRelate=new StaticBaseRelateVO();
	// staticBaseRelate.setStartTime(String.valueOf(currentTimeTicket));
	// staticBaseRelateList.add(staticBaseRelate);
	// String
	// staticBaseRelateData=TranslateUserInfoTOString(staticBaseRelateList);
	// File file=getCurrentStaticBaseRelationFile(context);
	// if(file!=null){
	// FileHelper.getInstance().write(file, staticBaseRelateData);
	// }
	// }
	// /***
	// * 遍历统计文件夹中所有的需要上传的文件
	// * @param context
	// */
	// public static List<String> queryUpLoadFiles(Context context){
	// String staticFolder= getStaticDbFolder(context);
	// if(staticFolder!=null){
	// List<String> list=queryFilePath(staticFolder, true);
	// if(list!=null&&!list.isEmpty()){
	// return list;
	// }
	// }
	// return null;
	// }
	// /***
	// * 遍历统计文件夹中所有的准备需要上传的文件 需重命名
	// * @param context
	// */
	// public static List<String> queryUpLoadFiles(Context context){
	// String staticFolder= getStaticDbFolder(context);
	// if(staticFolder!=null){
	// List<String> list=queryFilePath(staticFolder, true);
	// if(list!=null&&!list.isEmpty()){
	// return list;
	// }
	// }
	// return null;
	// }
	// /***
	// * 从文件中读取返回list列表
	// * @param context
	// * @return
	// * @throws Exception
	// */
	// public static List<StaticBaseRelateVO>
	// readFromRelationPrelateVOList(Context context) throws Exception{
	// File file=getCurrentStaticBaseRelationFile(context);
	// String staticBaseRelateData=FileHelper.getInstance().read(file);
	// return TranslateStringTOUserInfo(staticBaseRelateData);
	// }
	//
	// /***
	// * 更新统计基础关联对象 并将更新数据重新保存
	// * 更新原则，根据保存对象中的创建时间最晚更新
	// * @param context
	// * @param newDbFilePath
	// * @throws Exception
	// */
	// @SuppressWarnings("unchecked")
	// public static void updateStaticBaseRelateVO(Context context,String
	// newDbFilePath) throws Exception{
	// if(newDbFilePath==null){
	// return;
	// }
	// List<StaticBaseRelateVO>
	// staticBaseRelateList=readFromRelationPrelateVOList(context);
	// if(staticBaseRelateList!=null&&!staticBaseRelateList.isEmpty()){
	// ComparatorStaticBaseRelate comparator=new ComparatorStaticBaseRelate();
	// Collections.sort(staticBaseRelateList, comparator);
	// StaticBaseRelateVO
	// staticBaseRelate=staticBaseRelateList.get(staticBaseRelateList.size()-1);
	// List<String> dbFilelist=staticBaseRelate.getDbFilePath();
	// dbFilelist.add(newDbFilePath);
	// staticBaseRelate.setDbFilePath(dbFilelist);
	// String
	// staticBaseRelateData=TranslateUserInfoTOString(staticBaseRelateList);
	// File file=getCurrentStaticBaseRelationFile(context);
	// if(file!=null){
	// FileHelper.getInstance().write(file, staticBaseRelateData);
	// }
	// }
	// }
	//
	//
	// /***
	// * List<StaticBaseRelateVO> 对象，转换为String
	// *
	// * @param staticBaseRelateList
	// * @return
	// * @throws IOException
	// */
	// public static String TranslateUserInfoTOString(List<StaticBaseRelateVO>
	// staticBaseRelateList)
	// throws IOException {
	// ByteArrayOutputStream baos = new ByteArrayOutputStream();
	// ObjectOutputStream oos = new ObjectOutputStream(baos);
	// oos.writeObject(staticBaseRelateList);
	// String userBase64 = new String(new
	// BASE64Encoder().encode(baos.toByteArray()));
	// return userBase64;
	//
	// }
	//
	// /**
	// * String 转换为 List<StaticBaseRelateVO>
	// *
	// * @param staticBaseRelateData
	// * @return
	// * @throws IOException
	// * @throws ClassNotFoundException
	// */
	// @SuppressWarnings("unchecked")
	// public static List<StaticBaseRelateVO> TranslateStringTOUserInfo(String
	// staticBaseRelateData)
	// throws IOException, ClassNotFoundException {
	// if (staticBaseRelateData == null)
	// return null;
	// byte[] base64Bytes = new
	// BASE64Decoder().decodeBuffer(staticBaseRelateData);
	// ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
	// ObjectInputStream ois = new ObjectInputStream(bais);
	// return (List<StaticBaseRelateVO>) ois.readObject();
	// }
	//
	// }
	// class ComparatorStaticBaseRelate implements Comparator{
	//
	// public int compare(Object arg0, Object arg1) {
	// StaticBaseRelateVO staticBaseRelate0=(StaticBaseRelateVO)arg0;
	// StaticBaseRelateVO staticBaseRelate1=(StaticBaseRelateVO)arg1;
	//
	// //首先比较时间
	// return
	// (staticBaseRelate0.getStartTime()).compareTo(staticBaseRelate1.getStartTime());
	// }
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ApricotforestStatistic.StaticActInterface#onResume(android.content
	 * .Context, int, java.lang.String)
	 */

}
