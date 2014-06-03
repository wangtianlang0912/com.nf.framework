package com.nf.framework.browser;

import com.nf.framework.CommonConstantData;
import com.nf.framework.LoadSysSoft;

import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class mWebViewClient extends WebViewClient{

	private Context context;
	/**
	 * 
	 */
	public mWebViewClient(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
	}
	 // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		if(url!=null&&(url.endsWith(".mp3")||url.endsWith(".mp4"))){
			new LoadSysSoft().OpenVideo(context, url);
		}else{
			view.loadUrl(url);
		}
		return true;
	}
	@Override
	public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) {
		view.loadUrl(CommonConstantData.ErrorURL);
	}
}
