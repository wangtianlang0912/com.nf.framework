package com.nf.framework;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;

import com.nf.framework.authority.AppNetAuthority.NetAuthority;
import com.nf.framework.dialog.AbsBaseDialog.DialogRightBtnOnClickListener;
import com.nf.framework.dialog.BaseDialog;

/**
 * @author niufei
 * 
 * 
 *判断联网
 *
 */
public class CheckInternet {
	
	public static CheckInternet cit=null;
	
	public 	Context mcontext;
	/**
	 * @param context
	 */
	public CheckInternet(Context context) {
		// TODO Auto-generated constructor stub
		mcontext=context;
	}
	public static CheckInternet getInstance(Context context){
	
		if(cit==null){
			
			cit=new CheckInternet(context);
			
		}
		return cit;
	}
	 /**
	  * 是否飞行模式
	  * @param context
	  * @return
	  */
	public boolean isAirplaneModeOn() {
		  return Settings.System.getInt(mcontext.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;
	}
	/**
	 * 检测是否已经连接网络。
	 * 
	 * @param context
	 * @return 当且仅当连上网络时返回true,否则返回false。
	 */
	public boolean  checkInternet(){
		ConnectivityManager connectivityManager = (ConnectivityManager) mcontext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager == null) {
			return false;
		}
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		return (info != null) && info.isAvailable();
	}
	/**
	 * @return 
	 * 判断是否跳转到设置对话框
	 * 
	 */
public static  void netDialog(final Context context){
	
	// 初始化一个自定义的Dialog
	BaseDialog dialog = new BaseDialog(context);
	dialog.show();
	dialog.setTitle(R.drawable.common_dialog_warming_logo,"网络提示");
	dialog.setContent("你的终端未设置网络连接，现在设置吗？");
	dialog.setBtnName(null,"取消", "设置");
	dialog.setDialogRightBtnOnClickListener(new DialogRightBtnOnClickListener() {
		@Override
		public void onButtonClick(View rightBtn) {
			// TODO Auto-generated method stub
			Intent intent = new Intent("android.settings.SETTINGS"); 	
			context.startActivity(intent);
		}
	});

}
/**
 * 判断当前网络状态
 */
public NetAuthority JudgeCurrentNetState() {
	ConnectivityManager connectivityManager = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo info = connectivityManager.getActiveNetworkInfo();
	if (info != null && info.isAvailable()) {
	TelephonyManager mTelephony = (TelephonyManager) mcontext.getSystemService(Context.TELEPHONY_SERVICE);
	int netType = info.getType();
	int netSubtype = info.getSubtype();
	if (netType == ConnectivityManager.TYPE_WIFI) {// WIFI
		return NetAuthority.WifiConnect;
	} else if (netType == ConnectivityManager.TYPE_MOBILE
			&& netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
			&& !mTelephony.isNetworkRoaming()) {// 3G
			return NetAuthority.Net3GComnect;
		} else {
			return NetAuthority.Net2GComnect;
		}
	}
	return NetAuthority.unNetConnect;
}
/**
 * 判断是否为2G网络或者未联网
 */
//public String JudgeMobileNetState() {
//	String flag = null;
//	ConnectivityManager connectivityManager = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
//	NetworkInfo info = connectivityManager.getActiveNetworkInfo();
//	if (info != null && info.isAvailable()) {
//	TelephonyManager mTelephony = (TelephonyManager) mcontext.getSystemService(Context.TELEPHONY_SERVICE);
//	int netType = info.getType();
//	int netSubtype = info.getSubtype();
//	if (netType == ConnectivityManager.TYPE_WIFI) {// WIFI
//		flag = "WELL";
//	} else if (netType == ConnectivityManager.TYPE_MOBILE
//			&& netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
//			&& !mTelephony.isNetworkRoaming()) {// 3G
//
//		flag = "WELL";
//	} else {
//		flag = "2G";
//	}
//	}
//	return flag;
//}

}
