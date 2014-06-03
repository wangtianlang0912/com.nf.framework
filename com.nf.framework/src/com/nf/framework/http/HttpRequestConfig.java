package com.nf.framework.http;
/****
 * 网络请求权限
 * @author niufei
 *
 */
public class HttpRequestConfig {
	/***sessionkey请求 权限***/
	public static final int PRIORITY_SESSIONKEY=0;
	/****用户信息请求 ****/
	public static final int PRIORITY_USERINFO=1;
	/*****处理因业务逻辑造成的请求失败情况********/
	public static final int PRIORITY_PRECOMMON=2;
	/****普通数据请求****/
	public static final int PRIORITY_COMMON=3;
	
}
