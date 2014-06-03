package com.nf.framework;

import android.app.Activity;
import android.os.Handler;

/****
 * 
 * 
 * 定时相应线程 可自定义主线程定时时间，如子线程运行小于定时时间，则延时至定时时间
 * 
 * 如子线程运行时间大于定时时间则等待子线程完成再进行主线程操作
 * 
 * @author niufei
 * 
 */

public class TimerWaitingThread extends Thread {
	long milliseconds = 0;
	private long preExecuteTime;
	private ITimerWaitingThreadInter iTimerThreadInter;
	private Activity mActivity;
	Handler handler = new Handler();
	private String progressRefer;
	private long progress;
	private Runnable timerRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					iTimerThreadInter.onUIPostExecute();
					handler.removeCallbacks(timerRunnable);
				}
			});
		}
	};
	private Runnable progressingRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					iTimerThreadInter.onUIProgressParam(progressRefer, progress);
					handler.removeCallbacks(progressingRunnable);
				}
			});
		}
	};
	/***
	 * 
	 * @param activity
	 * @param milliseconds
	 *            毫秒
	 * @param iTimerThreadInter
	 */
	public TimerWaitingThread(Activity activity, long milliseconds,
			ITimerWaitingThreadInter iTimerThreadInter) {

		this.milliseconds = milliseconds;
		this.iTimerThreadInter = iTimerThreadInter;
		this.mActivity = activity;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		long currentTime = System.currentTimeMillis();
		iTimerThreadInter.onAysncPreExecute();
		preExecuteTime = System.currentTimeMillis() - currentTime;
		System.out.println("TimerWaitingThread.run()" + preExecuteTime);
		if (preExecuteTime < milliseconds) {
			handler.postDelayed(timerRunnable, milliseconds - preExecuteTime);
		} else {
			handler.post(timerRunnable);
		}
	}
	public void onProgressParam(String progressRefer,long progress){
		this.progressRefer=progressRefer;
		this.progress=progress;
		handler.post(progressingRunnable);
	}
	public interface ITimerWaitingThreadInter {

		public boolean onAysncPreExecute();

		public void onUIProgressParam(String info,long progress); 
		
		public void onUIPostExecute();
	}

}
