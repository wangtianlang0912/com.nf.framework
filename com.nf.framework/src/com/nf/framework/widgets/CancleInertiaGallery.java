/**
 * CancleInertiaGallery.java
 * 功能：取消惯性的gallery，去掉惯性滚动 以及 短距离翻页的实现
 * 实现自动滚屏，当手滑动后会实现短暂停留，然后继续滚动
 * 
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-7-12       下午04:57:54
 * Copyright (c) 2012, TNT All Rights Reserved.
 */

package com.nf.framework.widgets;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

public class CancleInertiaGallery extends Gallery {
	private static int TICK_WHAT = 5;
	private static boolean waitHander = false;
	private long millionseconds = 5000;// 自动滚屏间隔时间
	private int contentItemCount = 0;

	public CancleInertiaGallery(Context context, AttributeSet attrSet) {
		super(context, attrSet);
		// TODO Auto-generated constructor stub
	}

	public CancleInertiaGallery(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
	}

	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
		return e2.getX() > e1.getX();
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		// return super.onFling(e1, e2, 0, velocityY);//方法一：只去除翻页惯性
		// return false;//方法二：只去除翻页惯性 注：没有被注释掉的代码实现了开始说的2种效果。
		int kEvent;
		if (isScrollingLeft(e1, e2)) {
			// Check if scrolling left
			kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
		} else {
			// Otherwise scrolling right
			kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
		}
		setWaitHander(true);
		onKeyDown(kEvent, null);
		return true;
	}
	public boolean isWaitHander(){
		return waitHander;
	}
	/**
	 * 
	 * @param waithander
	 */
	public void setWaitHander(boolean waithander){
		waitHander=waithander;
	}
	/**
	 * 开始自动滚动方法
	 * 
	 * @param ms
	 */
	public void startAutoScroll(long ms) {
		mHandler.removeMessages(TICK_WHAT);
		mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), ms);
		this.millionseconds = ms;
		contentItemCount = getAdapter().getCount();// 获取显示内容的个数
	}
	public void stopScroll(){
		setWaitHander(true);
		mHandler.removeMessages(TICK_WHAT);
	}
	/**
	 * 开始自动滚动方法
	 * 
	 * @param ms
	 */
	public void startAutoScroll() {
		mHandler.removeMessages(TICK_WHAT);
		mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT),
				millionseconds);
		contentItemCount = getAdapter().getCount();// 获取显示内容的个数
	}

	/**
	 * 如果当前显示的图片并不是自动滚动后显示的图片id（被手动滚动过），那该信息发送失效
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(Message m) {

			if (!isWaitHander()) {
				int nextItem = 0;
				nextItem = getSelectedItemPosition();
				if (nextItem < contentItemCount - 1) {
					nextItem++;
				} else {
					nextItem = 0;
				}
				setSelection(nextItem, false);
			} else {
				setWaitHander(false);
			}
			sendMessageDelayed(Message.obtain(this, TICK_WHAT), millionseconds);
		}
	};

}