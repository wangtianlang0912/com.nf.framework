package com.nf.framework.browser;

/**
 * OriginalURLActivity.java
 * 功能：
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-5-18       下午02:34:58
 * Copyright (c) 2012, TNT All Rights Reserved.
 */


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nf.framework.BaseActivity;
import com.nf.framework.CloseActivityClass;
import com.nf.framework.IntentToCommonActUtil;
import com.nf.framework.LoadSysSoft;
import com.nf.framework.R;
import com.nf.framework.imagebrowser.ImageBrowserVO;
import com.umeng.analytics.MobclickAgent;

public class URLBrowerActivity extends BaseActivity{
	private Context mcontext;
	private WebView webview;
	private Intent homeIntent;
	private String urlAddress;
	/*-- Toolbar顶部菜单选项下标--*/
	private final int TOOLBAR_ITEM_GOBACK = 0;// 后退
	private final int TOOLBAR_ITEM_FORWARD = 1;// 前进
	private final int TOOLBAR_ITEM_REFRESH = 2;// 刷新
	private ImageView gobackBtn;
	private ImageView goforwardBtn;
	private ImageView refreshBtn;
	private ImageView browserBtn;
	private RelativeLayout refeshProgressbar;
	private String intentSource;
	public static final String INTENT_TITLE="param_title";
	public static final String INTENT_URL="url";
	public static final String INTENT_SOURCE="intentSource";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mcontext = this;
		CloseActivityClass.activityList.add(this);
		initView();
	}
	/**
	 * 初始化加载控件
	 */
	private void initView() {
		View view=LayoutInflater.from(mcontext).inflate(R.layout.common_web_browser_main,null);
		super.mainlayout.addView(view);
		super.leftButton.setVisibility(View.VISIBLE);
		super.leftButton.setImageResource(R.drawable.common_navigate_back_btn);
		refeshProgressbar=(RelativeLayout)this.findViewById(R.id.common_web_main_refesh_progressbar_layout);
		gobackBtn=(ImageView)this.findViewById(R.id.common_web_toolbar_goback_btn);
		goforwardBtn=(ImageView)this.findViewById(R.id.common_web_toolbar_goforward_btn);
		refreshBtn=(ImageView)this.findViewById(R.id.common_web_toolbar_refresh_btn);
		browserBtn=(ImageView)this.findViewById(R.id.common_web_toolbar_browser_btn);
		webview = (WebView) this.findViewById(R.id.common_web_main_web_context);
		// 设置全屏显示打网页
		webview.getSettings().setLoadWithOverviewMode(true);
		webview.getSettings().setUseWideViewPort(true);
		// 设置WebView属性，能够执行Javascript脚本
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setVerticalScrollBarEnabled(true);
		webview.getSettings().setBuiltInZoomControls(true);//设置支持缩放  
		webview.requestFocus();
		
		// 此方法可以处理webview 在加载时和加载完成时一些操作
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {

				if (newProgress == 100) { // 这里是设置activity的标题，
					// 也可以根据自己的需求做一些其他的操作
					refeshProgressbar.setVisibility(View.INVISIBLE);
					//刷新按钮
					setToolbarState(true,gobackBtn);
					//后退按钮
					setToolbarState(view.canGoBack(),gobackBtn);
					setToolbarState(view.canGoForward(),goforwardBtn);
				} else {
					refeshProgressbar.setVisibility(View.VISIBLE);
					setToolbarState(false,gobackBtn);
					setToolbarState(false,goforwardBtn);
				}
				
			}
		});
		// 如果页面中链接，如果希望点击链接继续在当前browser中响应，
		// 而不是新开Android的系统browser中响应该链接，必须覆盖webview的WebViewClient对象
		webview.setWebViewClient(new mWebViewClient(URLBrowerActivity.this));
		webview.addJavascriptInterface(new Object() {
			//发送播放器地址
			@SuppressWarnings("unused")
//			@JavascriptInterface
			public void  getVideoUrl(String videoUrl){
				if(videoUrl!=null){
					 new LoadSysSoft().OpenVideo(mcontext,videoUrl);
				}
			}
			// 获取大图地址
			@SuppressWarnings("unused")
			@JavascriptInterface
			public void getPicUrl(String url) {
				if (url != null) {
					List<ImageBrowserVO> imageList=new ArrayList<ImageBrowserVO>();
					ImageBrowserVO imageBrowser=new ImageBrowserVO();
					imageBrowser.setPicUrl(url);
					imageList.add(imageBrowser);
					IntentToCommonActUtil.IntentToImageBrowserAct(URLBrowerActivity.this, imageList,0);
				}
			}
			// 获取下载地址
			@SuppressWarnings("unused")
			@JavascriptInterface
			public void getDownLoadUrl(String url) {
				if (url != null) {
					new LoadSysSoft().OpenBrowser(mcontext,url);
				}
			}
			// 调用系统中的市场
			@SuppressWarnings("unused")
			@JavascriptInterface
			public void OpenMarketApp(String appPackageName) {
				if (appPackageName != null) {
					new LoadSysSoft().OpenMarketApp(mcontext,appPackageName);
				}
			}
		}, "detailJS");
		gobackBtn.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(mcontext, "网页界面按钮","后退");
				if (webview.canGoBack()) {
					webview.goBack();
				}
			}
		});
		goforwardBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(mcontext, "网页界面按钮","前进");
				if (webview.canGoForward()) {
					webview.goForward();
				}	
			}
		});
		super.leftButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				FinishActivity();	
			}
		});
		refreshBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MobclickAgent.onEvent(mcontext, "网页界面按钮","刷新");
				webview.loadUrl(urlAddress);
			}
		});
		browserBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MobclickAgent.onEvent(mcontext, "网页界面按钮","外部浏览器");
				String urlAddress=getIntent().getStringExtra(INTENT_URL);
				if(!TextUtils.isEmpty(urlAddress)){
					new LoadSysSoft().OpenBrowser(mcontext, urlAddress);
				}
			}
		});
	}
	protected void onResume() {
		super.onStaticResume(this,0,null);
		homeIntent = this.getIntent();
		String titleName = homeIntent.getStringExtra(INTENT_TITLE);
		if(!TextUtils.isEmpty(titleName)){
			super.top_textview.setText(titleName);
		}
		urlAddress = homeIntent.getStringExtra(INTENT_URL);
		webview.loadUrl(urlAddress);
	}
	/**
	 * 设置工具栏的显示状态
	 * @param clickable
	 * @param imageView
	 * @param imageRes
	 */
	public void setToolbarState(boolean clickable, ImageView imageView) {
		imageView.setClickable(clickable);
		if(imageView.equals(gobackBtn)){
			imageView.setImageResource(clickable?R.drawable.common_web_toolbar_goback_btn_clickable:R.drawable.common_web_toolbar_goback_btn_normal);
		}else if(imageView.equals(goforwardBtn)){
			imageView.setImageResource(clickable?R.drawable.common_web_toolbar_gofoward_btn_clickable:R.drawable.common_web_toolbar_goforward_btn_normal);
		}
	
	}

	protected void onPause() {
		super.onStaticPause(this,0,null);
	}
	/**
	 * 按下返回按钮
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			FinishActivity();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
		/**
		 * 关闭activity
		 */
		private void FinishActivity(){
			
			if(intentSource!=null){
				new LoadSysSoft().openAPP(mcontext,mcontext.getPackageName());
			}else{
				setResult(RESULT_OK, homeIntent);
			}
			finish();
			//上下交错效果
			overridePendingTransition(R.anim.common_slide_up_in, R.anim.common_slide_down_out);
		}
}
