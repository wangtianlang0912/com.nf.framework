package com.nf.framework;

/**
 * 系统常量数据
 * 
 * @author niufei
 * 
 */
public class CommonConstantData {

	private static final String ip = "http://services.xingshulin.com";
	
	private static final String UpdateIp = "http://update.xingshulin.com";
	/**
	 * 信息反馈接口
	 */
	public static final String URL = ip
			+ "/ApricotForestWirelessServiceForLiterature/LiteratureDataServlet?";
	/**
	 * 消息推送接口 日志统计接口
	 */
	public static final String PushURL = ip+ "/MessageService/MessagePushServlet?";// 正式
	
	/**
	 * 2014.1.15
	 * 公共文件更新接口
	 */
	public static final String FileUpdateURL = UpdateIp+ "/XSLFileUpdateServlet/FileUpdateServlet?";
	/**
	 * 免责声明 服务条款
	 */
	public static final String Privacy = "http://wireless.xingshulin.com/static/public/ad/privacy.html?";
	/**
	 * 联系客服
	 */
	public static final String Contact = "http://wireless.xingshulin.com/static/public/ad/contact.html?";
	/**
	 * 推荐应用
	 */
	public static final String Recommends = "http://wireless.xingshulin.com/static/public/ad/appList.html?";
	/**
	 * 关于杏树林
	 */
	public static final String AboutUs = "http://wireless.xingshulin.com/static/public/ad/aboutUs.html?";
	/**
	 * 反馈
	 */
	public static final String FeedBack = "http://wireless.xingshulin.com/errorCorrection.html?version=1.2";
	/**
	 * 联网错误地址
	 */
	public static final String ErrorURL = "file:///android_asset/error.html";
	/**
	 * 设备屏幕宽度 高度
	 */
	public static int screenWidth = 0;
	public static int screenHeight = 0;
	public static float density = 1;
	/**
	 * PicshowAcitivity-->DetailActivity
	 */
	public final static int PicShowTODetail = 11;
}
