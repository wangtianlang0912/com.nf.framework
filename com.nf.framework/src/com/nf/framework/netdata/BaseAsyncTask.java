package com.nf.framework.netdata;

import android.content.Context;
import android.os.AsyncTask;

import com.nf.framework.CheckInternet;
import com.nf.framework.dialog.ProgressDialog;
import com.nf.framework.util.AndroidVersionCheckUtils;


public abstract class BaseAsyncTask<String, Integer,Result> extends
		AsyncTask<String, Integer,Result> {
	Context mcontext;
	boolean isNeedNet;
	ProgressDialog spd = null;
	
	private boolean isCanRunInBackground=true;

	public BaseAsyncTask(Context mcontext, boolean isNeedProgressBar,
			boolean isNeedNet) {
		this.mcontext = mcontext;
		this.isNeedNet = isNeedNet;
		if (isNeedProgressBar) {
			spd = new ProgressDialog(mcontext);
		}
	}
	
	@Override
	protected void onPreExecute() {
		showDialog();
		if (isNeedNet) {
			boolean isNet = CheckInternet.getInstance(mcontext).checkInternet();
			if (!isNet) {
				InternetReferUtil();
				setCanRunInBackground(false);
			}
		}
		onPreExecuteUtil();
		super.onPreExecute();
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
		dismissDialog();
		return;
	}

	@Override
	protected void onPostExecute(Result result) {
		super.onPostExecute(result);
		onPostExecuteUtil(result);
		dismissDialog();
	}

	/**
	 * // 更新进度
	 */
	@Override
	protected void onProgressUpdate(Integer... values) {

		super.onProgressUpdate(values);
	}
	/***
	 * 判断是否可以执行background中的方法
	 * @return
	 */
	public boolean isCanRunInBackground() {
		return isCanRunInBackground;
	}

	public void setCanRunInBackground(boolean isCanRunInBackground) {
		this.isCanRunInBackground = isCanRunInBackground;
	}
	/***
	 * 无网络连接时，网络提醒 默认为提示对话框
	 */
	public void InternetReferUtil(){
		CheckInternet.getInstance(mcontext).netDialog(mcontext);
	}
	/**
	 * onPreExecute执行方法
	 */
	protected abstract void onPreExecuteUtil();

	/**
	 * onCancelled 执行方法
	 */
	protected abstract void onCanceledUtil();

	/**
	 * onPostExecute执行方法
	 * 
	 * @param result
	 */
	protected abstract void onPostExecuteUtil(Result result);

	/**
	 * Show this dialog
	 */
	private void showDialog() {
		if (spd != null && !spd.isShowing())
			spd.show();
	}

	/**
	 * Dismiss this dialog
	 */
	private void dismissDialog() {
		if (spd != null && spd.isShowing())
			spd.dismiss();
	}

	/***
	 * Android从3.0开始，AsyncTask为顺序执行方式。这种顺序方式指的是，所有的使用AsyncTask的类均会在应用中排一个序，按顺序执行！
	 *	这就导致有时候发现AsyncTask迟迟得不到执行被莫名其妙得delay了，是因为其他的AsyncTask还没执行完！
	 * @param params
	 * @return
	 */
	public AsyncTask<String, Integer, Result> executeSelector(String...params){
		if(AndroidVersionCheckUtils.hasHoneycomb()){
			return super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params);
		}
		return super.execute(params);
	}
}
