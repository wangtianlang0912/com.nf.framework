/**
 * CountDownTimerButton.java
 * 功能： 倒计时Button按钮 
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-10-26       下午04:38:26
 * Copyright (c) 2012, TNT All Rights Reserved.
 */

package com.nf.framework.widgets.counterButton;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CounterButton extends Button implements OnClickListener {

	private OnTimerCountClickListener onTimerCountListener = null;
	private TimeCount timeCount = null;

	public CounterButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setOnClickListener(this);
	}

	public CounterButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setOnClickListener(this);
	}

	public CounterButton(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		setOnClickListener(this);
	}

	/**
	 * 定制倒计时总时间
	 * 
	 * @param millisInFuture
	 *            总时间
	 * @param countDownInterval
	 *            间隔时间
	 */
	public void setTimeCount(long millisInFuture, long countDownInterval) {
		timeCount = new TimeCount(millisInFuture, countDownInterval);
	}

	/**
	 * 停止定时器
	 */
	public void stopTimerCount(String showText) {
		setText(showText);
		if (timeCount != null)
			timeCount.cancel();
		setClickable(true);
	}

	/**
	 * 停止定时器
	 */
	public void stopTimerCount() {
		stopTimerCount("发送验证码");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		VerificationInfo verficationInfo = onTimerCountListener.onClick(v);
		if (verficationInfo != null) {
			// new SendVerificationAsyncTask(verficationInfo).execute();
		}
	}

	public void setOnTimerCountClickListener(
			OnTimerCountClickListener onTimerCountListener) {
		this.onTimerCountListener = onTimerCountListener;
	}

	/**
	 * 倒计时点击按钮事件
	 * 
	 * @author win7
	 * 
	 */
	public interface OnTimerCountClickListener {

		public VerificationInfo onClick(View v);
	}

	/* 定义一个倒计时的内部类 */
	class TimeCount extends CountDownTimer {

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			setText("发送验证码");
			setClickable(true);
			if (timeCount != null)
				timeCount.cancel();
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			setClickable(false);
			setText("剩余" + millisUntilFinished / 1000 + "秒");
		}
	}
}
