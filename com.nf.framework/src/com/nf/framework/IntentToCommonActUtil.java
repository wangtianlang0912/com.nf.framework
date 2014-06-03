package com.nf.framework;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.nf.framework.browser.URLBrowerActivity;
import com.nf.framework.imagebrowser.ImageBrowserActivity;
import com.nf.framework.imagebrowser.ImageBrowserVO;


public class IntentToCommonActUtil {

	
	/****
	 * 跳转到内部网页界面
	 * @param activity
	 * @param paramTitle
	 * @param url
	 * @param userId
	 * @param requestCode
	 */
	public static void IntentToURLBrowerAct(Activity activity,String paramTitle,String url,String userId,int requestCode) {
		Intent intent = new Intent();
		if(paramTitle!=null){
		intent.putExtra(URLBrowerActivity.INTENT_TITLE,paramTitle);
		}
		if(userId!=null){
			url=url+"#userId="+userId;
		}
		intent.putExtra(URLBrowerActivity.INTENT_URL, url);
		intent.setClass(activity, URLBrowerActivity.class);
		activity.startActivityForResult(intent, requestCode);
		// 下往上推出效果
		activity.overridePendingTransition(R.anim.common_push_up_in,R.anim.common_push_up_out);
	}
	/****
	 *广播 服务 跳转到内部网页界面
	 * @param context
	 * @param paramTitle
	 * @param url
	 * @param userId
	 */
	public static void IntentToURLBrowerAct(Context context,String paramTitle,String url,String userId) {
		Intent intent = new Intent();
		if(paramTitle!=null){
		intent.putExtra(URLBrowerActivity.INTENT_TITLE,paramTitle);
		}
		if(userId!=null){
			url=url+"#userId="+userId;
		}
		intent.putExtra(URLBrowerActivity.INTENT_URL, url);
		intent.setClass(context, URLBrowerActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	/***
	 * 跳转到图片查看器 common library
	 * @param activity
	 * @param imageList
	 * @param currentPos
	 */
	public static void IntentToImageBrowserAct(Activity activity,List<ImageBrowserVO> imageList,int currentPos){
		
		Intent intent = new Intent();
		intent.setClass(activity,ImageBrowserActivity.class);
		intent.putExtra(ImageBrowserActivity.ImageBrowser_Intent_List, (Serializable)imageList);
		intent.putExtra(ImageBrowserActivity.ImageBrowser_Intent_Position, currentPos);
		activity.startActivity(intent);
	}
}
