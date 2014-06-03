package com.ApricotforestStatistic.Service;

import android.content.Context;

import com.ApricotforestStatistic.sqlite.StatisticExtandDataCallBack;
import com.nf.framework.exception.XingshulinError;
import com.umeng.analytics.MobclickAgent;

/***
 * 页面访问方法类
 * 
 * @author niufei
 * 
 */
public class ApricotStatisticAgent {

	private static ApricotPageViewAgent apricotPageViewAgent = new ApricotPageViewAgent();
	private static ApricotEventAgent apricotEventAgent = new ApricotEventAgent();
	private static ApricotErrorRequestAgent apricotErrorAgent = new ApricotErrorRequestAgent();

	/**
	 * 用于软件开启时，上传上一次使用的文件数据
	 * 判断是否存在需要上传的文件数据库
	 * 读取数据库表信息，获取上传json数据
	 * 异步上传
	 * @param mcontext
	 */
	public static void onInitStaticData(Context mcontext){
		onInitStaticData(mcontext,null);
	}
	/**
	 * 用于软件开启时，上传上一次使用的文件数据
	 * 判断是否存在需要上传的文件数据库
	 * 读取数据库表信息，获取上传json数据
	 * 异步上传
	 * @param mcontext
	 */
	public static void onInitStaticData(Context mcontext,StatisticExtandDataCallBack callBack){
		if(apricotPageViewAgent!=null)
		apricotPageViewAgent.onInitStatisticData(mcontext,callBack);
	}
	/**
	 *  开启统计功能
	 * 
	 * @param niufei
	 * @param 2014-3-18 下午2:57:29
	 * @return void
	 * @throws
	 */
	public static void 	openAllStaticEvent(){
		if(apricotPageViewAgent!=null)
		apricotPageViewAgent.openAllStaticEvent();
	}
	/**
	 * 关闭统计功能
	 * 
	 * @param niufei
	 * @param 2014-3-18 下午2:58:01
	 * @return void
	 * @throws
	 */
	public static void closeAllStaticEvent(){
		if(apricotPageViewAgent!=null)
		apricotPageViewAgent.closeAllStaticEvent();
	}
	
	public static void onCreateWithUMeng(Context context) {
		if (context == null) {
			throw new XingshulinError("上下文不能为空");
		}
		MobclickAgent.onError(context);
	}

	/**
	 * onResume方法中使用 包含友盟的统计事件
	 * 
	 * @param context
	 * @param pageCode
	 */
	public static void onResumeWithUMeng(Context context, int userId,
			String pageCode) {
		if (context == null) {
			throw new XingshulinError("上下文不能为空");
		}
		MobclickAgent.onResume(context);
		onResume(context, userId, pageCode);
	}

	/**
	 * 
	 * @param context
	 * @param userId
	 * @param pageCode
	 * @param niufei
	 * @param 2014-3-18 下午2:37:49
	 * @return void
	 * @throws
	 */
	public static void onResume(Context context, int userId, String pageCode) {
		if (context == null) {
			throw new XingshulinError("上下文不能为空");
		}
		if (apricotPageViewAgent != null)
			apricotPageViewAgent.onResume(context, userId, pageCode);
	}

	/**
	 * onPause方法中使用 包含友盟的统计事件
	 * 
	 * @param context
	 * @param pageCode
	 */
	public static void onPauseWithUMeng(Context context, int userId,
			String pageCode) {
		if (context == null) {
			throw new XingshulinError("上下文不能为空");
		}
		MobclickAgent.onPause(context);
		onPause(context, userId, pageCode);
	}

	/**
	 * 
	 * @param context
	 * @param userId
	 * @param pageCode
	 * @param niufei
	 * @param 2014-3-18 下午2:37:11
	 * @return void
	 * @throws
	 */
	public static void onPause(Context context, int userId, String pageCode) {
		if (context == null) {
			throw new XingshulinError("上下文不能为空");
		}
		if (apricotPageViewAgent != null)
			apricotPageViewAgent.onPause(context, userId, pageCode);
	}

	/***
	 * 
	 * @param context
	 * @param userId
	 * @param eventName
	 * @param niufei
	 * @param 2014-3-18 下午2:35:47
	 * @return void
	 * @throws
	 */
	public static void onEventWithUMeng(Context context, int userId,
			String eventName) {

		onEventWithUMeng(context, userId, eventName, null);
	}

	/***
	 * 
	 * @param context
	 * @param userId
	 * @param eventName
	 * @param niufei
	 * @param 2014-3-18 下午2:35:55
	 * @return void
	 * @throws
	 */
	public static void onEvent(Context context, int userId, String eventName) {

		onEvent(context, userId, eventName, null);
	}

	/***
	 * 
	 * @param context
	 * @param userId
	 * @param eventName
	 * @param relatedParam
	 * @param niufei
	 * @param 2014-3-18 下午2:36:06
	 * @return void
	 * @throws
	 */
	public static void onEventWithUMeng(Context context, int userId,
			String eventName, String relatedParam) {

		MobclickAgent.onEvent(context, eventName, relatedParam);
		onEvent(context, userId, eventName, relatedParam);
	}

	/**
	 * 
	 * @param context
	 * @param userId
	 * @param eventName
	 * @param relatedParam
	 * @param niufei
	 * @param 2014-3-18 下午2:36:11
	 * @return void
	 * @throws
	 */
	public static void onEvent(Context context, int userId, String eventName,
			String relatedParam) {

		if (apricotEventAgent != null)
			apricotEventAgent.onEvent(context, eventName, userId, relatedParam);
	}

	/**
	 * 
	 * @param mcontext
	 * @param requestUrl
	 * @param errorReponseCode
	 * @param isRequestByPost
	 * @param niufei
	 * @param 2014-3-18 下午2:46:02
	 * @return void
	 * @throws
	 */
	public static void onErrorRequestUrl(Context mcontext, String requestUrl,
			int errorReponseCode, boolean isRequestByPost) {
		if (apricotErrorAgent != null) {
			apricotErrorAgent.onRequestUrl(mcontext, requestUrl,
					errorReponseCode, isRequestByPost);
		}
	}
}
