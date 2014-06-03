
package com.nf.framework;

import android.content.Context;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.Toast;

/***
 * 
 *webChromeClient是一个比较神奇的东西，其里面提供了一系列的方法，
 * 
 *分别作用于我们的javascript代码调用特定方法时执行，我们一般在其内部
 * 
 *将javascript形式的展示切换为android的形式。
 * 
 * 例如：我们重写了onJsAlert方法，那么当页面中需要弹出alert窗口时，便
 * 
 * 会执行我们的代码，按照我们的Toast的形式提示用户。
 */

public class MyWebChromeClient extends WebChromeClient {

	private Context mcontext;

	public MyWebChromeClient() {

	}
	public MyWebChromeClient(Context context) {

		mcontext = context;
	}

	@Override
	public boolean onJsAlert(WebView view, String url, String message,

	JsResult result) {
		Toast.makeText(mcontext, message, Toast.LENGTH_LONG).show();
		return true;

	}
	public void onProgressChanged(WebView view, int newProgress,RelativeLayout progressbar_layout) {
	  if(newProgress==100){   // 这里是设置activity的标题， 也可以根据自己的需求做一些其他的操作
		  
			progressbar_layout.setVisibility(View.GONE);
		
	   }else{
	    	progressbar_layout.setVisibility(View.VISIBLE);  
	   }
	 }
}
