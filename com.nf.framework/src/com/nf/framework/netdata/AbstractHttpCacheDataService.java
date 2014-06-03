package com.nf.framework.netdata;

import java.util.ArrayList;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.http.protocol.HTTP;
import android.content.Context;
import android.text.TextUtils;

public abstract class AbstractHttpCacheDataService extends AbstractHttpService{

	
	private Context mcontext;
	public AbstractHttpCacheDataService(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mcontext=context;
	}
	/****
	 * Http get 网络获取数据 
	 * @param url
	 * @param saveCache
	 * @param cacheFileFolder
	 * @param cacheFileName
	 * @return
	 * @throws Exception
	 */
	public String getDataFromNetByHttpGet(String url,boolean saveCache,String cacheFileFolder,String cacheFileName) throws Exception{
		
		String returnData=getDataFromNetByHttpGet(url);
		if(!TextUtils.isEmpty(returnData)&&saveCache){
			saveDataToCache(returnData,cacheFileFolder, cacheFileName);
		}
		return returnData;
	}
	/***
	 * http post 网络获取数据 
	 * @param url
	 * @param list
	 * @param saveCache
	 * @param cacheFileFolder
	 * @param cacheFileName
	 * @return
	 * @throws Exception
	 */
	public String getDataFromNetByHttpPost(String url, ArrayList<NameValuePair> list,boolean saveCache,String cacheFileFolder,String cacheFileName) throws Exception{
	
		String returnData=getDataFromNetByHttpPost(url, list);
		if(!TextUtils.isEmpty(returnData)&&saveCache){
			saveDataToCache(returnData,cacheFileFolder, cacheFileName);
		}
		return returnData;
	}
	/**
	 *get 获取网络数据
	 * @param url 网络请求地址
	 * @return
	 * @throws Exception
	 */
	public String getDataFromNetByHttpGet(String url) throws Exception{
		
		return getNet(url,HTTP.UTF_8);
	}
	/***
	 * post 请求网络地址
	 * @param url
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public String getDataFromNetByHttpPost(String url, ArrayList<NameValuePair> list) throws Exception{
		
		return getDataByHttp(url, list);
		
	}
	
	/***
	 * 获取文件缓存数据
	 * @param cacheFileFolder 缓存文件所在文件夹
	 * @param cacheFileName 缓存文件名称
	 * @return
	 */
	public String getDataFromCache(String cacheFileFolder,String cacheFileName){
		
		CacheDataMaster cacheDataMaster=new  CacheDataMaster(mcontext, cacheFileFolder);
		return cacheDataMaster.readCacheFile(cacheFileName);
	}
	
	/****
	 * 保存数据到缓存文件
	 * @param jsonData
	 * @param cacheFileFolder
	 * @param cacheFileName
	 */
	protected void saveDataToCache(String jsonData,String cacheFileFolder,String cacheFileName){
		
		CacheDataMaster cacheDataMaster=new  CacheDataMaster(mcontext, cacheFileFolder);
		cacheDataMaster.saveToCacheFile(jsonData,cacheFileName);
	}
}
