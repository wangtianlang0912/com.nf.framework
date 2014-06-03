package com.nf.framework.netdata;

/**
 * $id$
 * Copyright 2012 Inc. All rights reserved.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.ApricotforestStatistic.Service.ApricotStatisticAgent;
import com.nf.framework.exception.XingshulinError;
import com.nf.framework.exception.XingshulinException;
import com.nf.framework.util.io.FileUtils;

/**
 * 
 * 接口请求参数的抽象类
 * 
 * @author niufei
 * 
 */
public abstract class AbstractHttpService {

	public static final String UTF_8 = "UTF-8";
	public static final String DESC = "descend";
	public static final String ASC = "ascend";

	private final static int TIMEOUT_CONNECTION = 15000;
	private final static int TIMEOUT_SOCKET = 20000;
	private final static int RETRY_TIME = 2;
	private Context context;
	public AbstractHttpService(Context context) {
		this.context = context;
	}
	
	public void checkNullParams(String... params) throws XingshulinException {

		for (String param : params) {
			if (TextUtils.isEmpty(param)) {
				String errorMsg = "请求参数不能为空";
				throw new XingshulinException(
						XingshulinError.ERROR_CODE_NULL_PARAMETER, errorMsg,
						errorMsg);
			}
		}
	}

	/**
	 * post方法传值
	 * 
	 * @param url
	 * @param list
	 * @return
	 * @throws Exception
	 *             1.创建HttpGet或HttpPost对象，将要请求的URL通过构造方法传入HttpGet或HttpPost对象。
	 *             2.使用DefaultHttpClient类的execute方法发送HTTP GET或HTTP
	 *             POST请求，并返回HttpResponse对象。
	 *             3.通过HttpResponse接口的getEntity方法返回响应信息，并进行相应的处理。
	 */
	public String getDataByHttp(String url, ArrayList<NameValuePair> list) {
		String responseBody = "";
		InputStream inputStream = null;
		HttpClient httpClient = null;
		PostMethod httpPost = null;
		int time = 0;
		do {
			try {
				// 生成一个http客户端对象
				httpClient = getHttpClient();// 发送请求
				httpPost = getHttpPost(url, null, null);
				if (list != null) {
					NameValuePair[] nameValues = new NameValuePair[] {};
					httpPost.addParameters(list.toArray(nameValues));
				}
//				String saveUrl = getALLInterfaceUrl(url, list);
//				SaveInterfaceUrlToFile(saveUrl);
				int statusCode = httpClient.executeMethod(httpPost);
				if (statusCode == HttpStatus.SC_OK) {
					inputStream = httpPost.getResponseBodyAsStream();
					responseBody = inputStream2String(inputStream);
					// System.out.println("XMLDATA=====>"+responseBody);
					break;
				} else {
					 //将post地址转换为get地址，并且保存到存储卡中的列表中
					String requestUrl = getALLInterfaceUrl(url, list);
					ApricotStatisticAgent.onErrorRequestUrl(context, requestUrl,statusCode, true);
//					throw new XingshulinError(DEBUG_TAG_POST + ":::"+ statusCode);
				}
		
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
					}
				}
				// 释放连接
				httpPost.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);
		return responseBody;
	}

	/**
	 * get方法传值
	 * 
	 * @param uripath
	 * @return
	 * @throws Exception
	 *             1.创建HttpGet或HttpPost对象，将要请求的URL通过构造方法传入HttpGet或HttpPost对象。
	 *             2.使用DefaultHttpClient类的execute方法发送HTTP GET或HTTP
	 *             POST请求，并返回HttpResponse对象。
	 *             3.通过HttpResponse接口的getEntity方法返回响应信息，并进行相应的处理。
	 */
	public String getNet(String uripath, String enconding) throws Exception {
		InputStream inputStream = null;
		// 生成一个http客户端对象
		HttpClient httpClient = getHttpClient();// 发送请求
		String responseBody = null;
		GetMethod httpGet = null;
		int time = 0;
		do {
			try {
				httpGet = getHttpGet(uripath, null, null);
				int statusCode = httpClient.executeMethod(httpGet);
				// 判断请求是否成功处理
				if (statusCode == HttpStatus.SC_OK) {
					inputStream = httpGet.getResponseBodyAsStream();
					responseBody = inputStream2String(inputStream);
					break;
				} else {
					ApricotStatisticAgent.onErrorRequestUrl(context, uripath,statusCode, false);
//					throw new XingshulinError(DEBUG_TAG_GET + ":::"
//							+ statusCode);
				}
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
					}
				}
				// 释放连接
				httpGet.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);
		return responseBody;
	}

	private static HttpClient getHttpClient() {
		HttpClient httpClient = new HttpClient();
		// 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
		httpClient.getParams().setCookiePolicy(
				CookiePolicy.BROWSER_COMPATIBILITY);
		// 设置 默认的超时重试处理策略
		httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		// 设置 连接超时时间
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(TIMEOUT_CONNECTION);
		// 设置 读数据超时时间
		httpClient.getHttpConnectionManager().getParams()
				.setSoTimeout(TIMEOUT_SOCKET);
		// 设置 字符集
		httpClient.getParams().setContentCharset(UTF_8);
		return httpClient;
	}

	private static GetMethod getHttpGet(String url, String cookie,
			String userAgent) {
		GetMethod httpGet = new GetMethod(url);
		// 设置 请求超时时间
		httpGet.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpGet.setRequestHeader("Connection", "Keep-Alive");
		httpGet.setRequestHeader("Cookie", cookie);
		httpGet.setRequestHeader("User-Agent", userAgent);
		return httpGet;
	}

	private static PostMethod getHttpPost(String url, String cookie,
			String userAgent) {
		PostMethod httpPost = new PostMethod(url);
		// 设置 请求超时时间
		httpPost.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpPost.setRequestHeader("Connection", "Keep-Alive");
		httpPost.setRequestHeader("Cookie", cookie);
		httpPost.setRequestHeader("User-Agent", userAgent);
		return httpPost;
	}

	protected String getALLInterfaceUrl(String url, List<NameValuePair> list) {
		StringBuffer sb = new StringBuffer();
		if (url == null) {
			throw new XingshulinError("接口地址路径不能为空");
		}
		if (list == null) {
			throw new XingshulinError("接口地址参数列表对象不能为空");
		}
		sb.append(url);
		for (NameValuePair nameValue : list) {
			sb.append(nameValue.getName());
			sb.append("=");
			sb.append(nameValue.getValue());
			sb.append("&");
		}
		String urlStr = sb.toString();
		urlStr = urlStr.substring(0, urlStr.length() - 1);
//		System.out.println(urlStr);
		return urlStr;
	}

	/**
	 * post 文件上传接口
	 * 
	 * @param url
	 * @param nameValuePairs
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public String PostFileByHttp(String url, List<NameValuePair> nameValuePairs) throws ClientProtocolException, IOException {
		String result = null;
	
		org.apache.http.client.HttpClient httpClient = new DefaultHttpClient();
		// 设置通信协议版本
		httpClient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		HttpPost httpPost = new HttpPost(url);
		httpClient.getParams().setIntParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT_CONNECTION);
		// 设置读数据超时时间(单位毫秒)
		httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT,
				TIMEOUT_SOCKET);
				
		MultipartEntity entity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		for (int index = 0; index < nameValuePairs.size(); index++) {
			if (nameValuePairs.get(index).getName().equalsIgnoreCase("image")) {
				// If the key equals to "image", we use FileBody to transfer
				// the data
				if (nameValuePairs.get(index).getValue() != null) {
					entity.addPart(nameValuePairs.get(index).getName(),
							new FileBody(new File(nameValuePairs.get(index)
									.getValue())));
				}
			} else {
				// Normal string data
				entity.addPart(nameValuePairs.get(index).getName(),
						new StringBody(nameValuePairs.get(index).getValue()));
			}
		}
		httpPost.setEntity(entity);
		HttpResponse response = httpClient.execute(httpPost);// 接收响应
		// 判断请求是否成功处理
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			// 解析返回的内容
			result = EntityUtils.toString(response.getEntity(), "utf-8");
		} else {
			// //将post地址转换为get地址，并且保存到存储卡中的列表中
			String requestUrl = getALLInterfaceUrl(url, nameValuePairs);
			int errorReponseCode = response.getStatusLine().getStatusCode();
			ApricotStatisticAgent.onErrorRequestUrl(context, requestUrl,errorReponseCode, true);
			httpClient.getConnectionManager().shutdown();
		}
		return result;
	}
	
	/**
	 * 
	 * @param urlStr
	 */
	private void SaveInterfaceUrlToFile(String urlStr) {
		FileUtils fileHelper = FileUtils.getInstance();
		try {
			File file = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/url.txt");
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

	public String inputStream2String(InputStream in) throws IOException {
		if (in != null) {
			BufferedReader bf = new BufferedReader(new InputStreamReader(in,
					"UTF-8"));
			// 最好在将字节流转换为字符流的时候 进行转码
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = bf.readLine()) != null) {
				buffer.append(line);
			}
			bf.close();
			return buffer.toString();
		}
		return "";
	}
}
