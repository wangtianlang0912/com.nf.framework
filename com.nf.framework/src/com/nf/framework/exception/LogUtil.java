package com.nf.framework.exception;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.nf.framework.util.io.FileUtils;

/**
 * 错误日志统计
 * @author niufei
 *
 */
public class LogUtil {

	public static boolean OpenDug=false;
	
	private static Context mcontext;
	/**
	 * 初始化log管理器 
	 * @param context
	 */
	public static void init(Context context){
		
		mcontext=context;
	}
	
	private static void writeExceptionLog(Throwable ex){
		if(mcontext==null){
			return;
		}
		writeExceptionLog(mcontext, ex);
	}
	
	/**
	 * 保存错误信息到文件中
	 * @param ex
	 * @return
	 */
	public static  void writeExceptionLog(Context context,Throwable ex) {
		Writer info = new StringWriter();
		PrintWriter printWriter = new PrintWriter(info);
		ex.printStackTrace(printWriter);

		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}

		String result = info.toString();
		printWriter.close();
		try {
			long timestamp = System.currentTimeMillis();
			String fileName = "ex-" + timestamp;
			String fileFolderPath=Environment.getExternalStorageDirectory()+File.separator+context.getPackageName()+File.separator+"log";
			if(!new File(fileFolderPath).exists()){
				new File(fileFolderPath).mkdirs();
			}
			String filePath=fileFolderPath+File.separator+fileName;
			FileUtils.getInstance().write(new File(filePath), result);
		} catch (Exception e) {
			Log.e("", "an error occured while writing report file...", e);
		}
	}
	
	
	
	public static void e(Context mcontext,String errorMsg){
		if(!OpenDug){
			return;
		}
		e(getTag(mcontext),errorMsg);	
	}
	
	public static void e(String tag,String errorMsg){
		if(!OpenDug){
			return;
		}
		Log.e(tag,errorMsg);	
	}
	
	public static void d(Context mcontext,String errorMsg){
		if(!OpenDug){
			return;
		}
		d(getTag(mcontext),errorMsg);	
	}
	public static void d(String tag,String errorMsg){
		if(!OpenDug){
			return;
		}
		Log.d(tag,errorMsg);	
	}
	
	public static void w(Context mcontext,String errorMsg){
		if(!OpenDug){
			return;
		}
		w(getTag(mcontext),errorMsg);	
	}
	public static void w(String tag,String errorMsg){
		if(!OpenDug){
			return;
		}
		Log.w(tag,errorMsg);	
	}
	public static void w(String tag ,String errorMsg,Throwable throwable){
		
		writeExceptionLog(throwable);
		if(!OpenDug){
			return;
		}
		w(tag,errorMsg);
	}
	
	public static void i(Context mcontext,String errorMsg){
		if(!OpenDug){
			return;
		}
		i(getTag(mcontext),errorMsg);	
	}
	public static void i(String tag,String errorMsg){
		if(!OpenDug){
			return;
		}
		Log.i(tag,errorMsg);	
	}
	public static void v(Context mcontext,String errorMsg){
		if(!OpenDug){
			return;
		}
		v(getTag(mcontext),errorMsg);	
	}
	public static void v(String tag,String errorMsg){
		if(!OpenDug){
			return;
		}
		Log.v(tag,errorMsg);	
	}
	private static String getTag(Context mcontext){
		
		return mcontext!=null?mcontext.getClass().getSimpleName():LogUtil.class.getSimpleName();
	}
	
	
	
	/**
	 * 
	 * @param urlStr
	 */
	private void SaveExceptionUrlToFile(String urlStr) {
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
}
