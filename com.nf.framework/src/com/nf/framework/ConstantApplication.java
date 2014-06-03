package com.nf.framework;

import android.app.Application;
import android.util.Log;

/**
 * 用来控制所有application中的activity
 * @author niufei
 *
 */
public class ConstantApplication extends Application { 
	
	@Override
	public void onCreate() {
		super.onCreate();
//		CrashHandler crashHandler = CrashHandler.getInstance();
//		//注册crashHandler
//		crashHandler.init(getApplicationContext());
//		//发送以前没发送的报告(可选)
//		crashHandler.sendPreviousReportsToServer();
	}
	@Override  
    public void onTerminate(){  
        super.onTerminate();  
    }  
  
    public void onConfigurationChanged(){  
        Log.e("ConstantApplication","onConfigurationChanged");  
    }  
}
