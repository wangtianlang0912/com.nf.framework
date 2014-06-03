package com.nf.framework.http;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.nf.framework.exception.XingshulinError;

public class HttpRequestTask implements Runnable {

     private static final int INIT = 1;//定义三种下载的状态：初始化状态，正在下载状态，暂停状态
     private static final int DOWNLOADING = 2;
     private static final int PAUSE = 3;
     private int state = INIT;
     private HttpReponseHander mhandler;
     private String url;
     private List pamasList;
     private   final String DEBUG_TAG_POST="PostData";
 	private final String DEBUG_TAG_GET="GetData";

     /***
      * get请求
      * @param url
      * @param mhandler
      */
     public HttpRequestTask(String url,HttpReponseHander mhandler) {
    	 this.url=url;
         this.mhandler=mhandler;
     }
     /***
      * post请求
      * @param url
      * @param mhandler
      */
     public HttpRequestTask(String url,List list,HttpReponseHander mhandler) {
    	 this.url=url;
         this.mhandler=mhandler;
         this.pamasList=list;
     }
		@Override
		public void run() {
			// TODO Auto-generated method stub

			if(pamasList!=null){
				getDataByHttp(url, pamasList,mhandler);
			}else{
				getHttpRequestByGet(url,mhandler);
			}
		}
		
		/**
		 * post方法传值
		 * @param url
		 * @param pamasList
		 * @return
		 * @throws Exception
		 * 1.创建HttpGet或HttpPost对象，将要请求的URL通过构造方法传入HttpGet或HttpPost对象。
		   2.使用DefaultHttpClient类的execute方法发送HTTP GET或HTTP POST请求，并返回HttpResponse对象。
		   3.通过HttpResponse接口的getEntity方法返回响应信息，并进行相应的处理。
		 */
		public   void getDataByHttp(String url,List pamasList,HttpReponseHander mhandler){
			String result = null;
			HttpPost httpdata = new HttpPost(url);
			// 生成一个http客户端对象
			HttpClient httpClient = new DefaultHttpClient();// 发送请求
			try {
				// 设置连接超时时间(单位毫秒) 
				httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
				// 设置读数据超时时间(单位毫秒) 
				httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
				httpdata.setEntity(new UrlEncodedFormEntity(pamasList,HTTP.UTF_8));
				HttpResponse httpResponse = httpClient.execute(httpdata);// 接收响应
				// 判断请求是否成功处理  
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
			     // 解析返回的内容  
				  result= EntityUtils.toString(httpResponse.getEntity(),HTTP.UTF_8);  
				  mhandler.sendSuccessMessage(url, result);
				}else{
					 httpClient.getConnectionManager().shutdown();
					Log.e("AbstractGetDataFromService.getDataByHttp()",httpResponse.getStatusLine().getStatusCode()+"");
				 }
			  } catch (Exception e) {
				  Log.d(DEBUG_TAG_POST, "UnsupportedEncodingException");
				  mhandler.sendFailureMessage(url, e.getMessage());
				  throw new XingshulinError(e);
			  }finally{
					mhandler.sendFinishMessage(url);
					httpClient.getConnectionManager().shutdown();
				    httpdata=null;
					httpClient=null;
			  }
			 
		}
		

		/**
		 * get方法传值
		 * @param uripath
		 * @return
		 * @throws Exception
		 * 1.创建HttpGet或HttpPost对象，将要请求的URL通过构造方法传入HttpGet或HttpPost对象。
			2.使用DefaultHttpClient类的execute方法发送HTTP GET或HTTP POST请求，并返回HttpResponse对象。
			3.通过HttpResponse接口的getEntity方法返回响应信息，并进行相应的处理。
		 */
		  public void getHttpRequestByGet(String uripath,HttpReponseHander mhandler){
			  // 生成一个http客户端对象
			  HttpClient httpClient = new DefaultHttpClient();// 发送请求
			  String result = null;
			  try{
				mhandler.onProgressOnService(uripath);
				HttpGet httpGet = new HttpGet(uripath);
			
				httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
				httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
			    HttpResponse httpResponse =httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();// 取出响应
				// 判断请求是否成功处理  
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { 
					result= EntityUtils.toString(httpEntity);
					mhandler.sendSuccessMessage(url, result);
				}else{
					httpClient.getConnectionManager().shutdown();
				}
			  } catch (Exception e) {
		            Log.d(DEBUG_TAG_GET, "UnsupportedEncodingException");
		            httpClient.getConnectionManager().shutdown();
		            mhandler.sendFailureMessage(url, e.getMessage());
		            throw new XingshulinError(e);
			  }finally{
				  	mhandler.sendFinishMessage(url);
					httpClient.getConnectionManager().shutdown();
					httpClient=null;
			  }
			  
	   }
		
	
}