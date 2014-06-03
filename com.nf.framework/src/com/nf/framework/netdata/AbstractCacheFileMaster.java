package com.nf.framework.netdata;

import java.io.File;
import java.util.List;

import android.content.Context;

import com.nf.framework.util.io.FileUtils;

/***
 * 本地文件保存类抽象方法
 * @author tingting
 *
 */
public abstract class AbstractCacheFileMaster {
	
	protected abstract void saveToCacheFile(String jsonData,String fileName);
	
	protected abstract String readCacheFile(String fileName);
	
	protected abstract boolean deleteCacheFileByName(String fileName);
	
	protected abstract boolean deleteAllCacheFiles();
	
	protected abstract List<String> queryAllCacheFileNameList();
	
	/**
	 * 保存到系统文件中 /data/data/...
	 * @param context
	 * @param jsonData
	 * @param fileFolderName
	 * @param fileName
	 * @throws Exception
	 */
	protected void SaveToSysFile(Context context,String jsonData,String fileFolderName,String fileName) throws Exception{
		
		if(jsonData==null){
			return;
		}
		String fileFolderPath=getSysSaveFilePath(context, fileFolderName);
		FileUtils fileHelper=FileUtils.getInstance();
		if(!fileHelper.isAbsolutePackageExist(fileFolderPath)){
			fileHelper.createAbsoluteDir(fileFolderPath);
		}
		String CacheFilePath=fileFolderPath+File.separator+fileName;
		saveDataToFile(CacheFilePath, jsonData);
		
	}
	/***
	 * 保存数据到文件
	 * @param CacheFilePath
	 * @param jsonData
	 * @throws Exception 
	 */
	private void saveDataToFile(String CacheFilePath,String jsonData) throws Exception{
		
		//create log file
		File CacheFile=new File(CacheFilePath);		
		if(!CacheFile.exists()){
			CacheFile.createNewFile();
		}
		FileUtils fileHelper=FileUtils.getInstance();
		fileHelper.write(CacheFile, jsonData);
	}
	/***
	 * 读取本地文件内容
	 * @param context
	 * @param fileName
	 * @param fileFolderName
	 * @param isSdCard
	 * @return
	 * @throws Exception 
	 */
	protected String readCacheFile(Context context,String fileName,String fileFolderName) throws Exception {
		// TODO Auto-generated method stub
		if(fileName==null){
			return null;
		}
		String	fileFolderPath=getSysSaveFilePath(context, fileFolderName);
		if(new File(fileFolderPath).exists()){
			String CacheFilePath=fileFolderPath+File.separator+fileName;
			File CacheFile=new File(CacheFilePath);
			if(CacheFile.exists()){
				return FileUtils.getInstance().read(CacheFile);
			}
		}
		return null;
	}
	/***
	 * 删除指定文件
	 * @param context
	 * @param fileName
	 * @param fileFolderName
	 * @param isSdCard
	 * @return
	 */
	protected boolean deleteCacheFileByName(Context context,String fileName,String fileFolderName) {
		// TODO Auto-generated method stub
		if(fileName==null){
			return false;
		}
		String	fileFolderPath=getSysSaveFilePath(context, fileFolderName);
		if(new File(fileFolderPath).exists()){
			String CacheFilePath=fileFolderPath+File.separator+fileName;
			File CacheFile=new File(CacheFilePath);
			if(CacheFile.exists()){
				return FileUtils.getInstance().deleteFile(CacheFilePath);
			}
		}
		return false;
	}
	/***
	 * 清空所有的本地文件
	 * @param context
	 * @param isSdCard  判断是否sd卡中操作
	 * @param fileFolderName
	 * @return
	 */
	public boolean deleteAllCacheFile(Context context,String fileFolderName){
		
		String	fileFolderPath=getSysSaveFilePath(context, fileFolderName);
		if(new File(fileFolderPath).exists()){
			FileUtils.getInstance().deleteDirectory(fileFolderPath);
		}
		return true;
	}
	/***
	 * 返回指定文件夹中的全部文件名称
	 * @param context
	 * @param fileFolderName
	 * @param isSdCard
	 * @return
	 */
	public List<String> queryAllCacheFile(Context context,String fileFolderName){
		
		String	fileFolderPath=getSysSaveFilePath(context, fileFolderName);
		if(new File(fileFolderPath).exists()){
			return 	FileUtils.getInstance().queryFileNameList(fileFolderPath);
		}
		return null;
	}
	
	/**
	 *
	 * @return
	 */
	protected String getSysSaveFilePath(Context context,String fileFolderName){
		
		return context.getCacheDir().getPath()+File.separator+fileFolderName;
	}
}
