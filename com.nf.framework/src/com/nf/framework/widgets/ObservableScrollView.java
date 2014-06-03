/**
 * d.java
 * com.Apricotforest.widgets
 * 工程：medicalJournals_for_android
 * 功能：scrollview 动态滚动 实现scollView 自动滚动时的事件监听 


 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-9-5       下午02:18:12
 * Copyright (c) 2012, TNT All Rights Reserved.
 */

package com.nf.framework.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class ObservableScrollView extends ScrollView {

	private ScrollViewListener scrollViewListener = null;

	public ObservableScrollView(Context context) {
		super(context);
	}

	public ObservableScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public ObservableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setScrollViewListener(ScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (scrollViewListener != null) {
			scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
		}
	}

	/**
	 * 滑动事件 降低滚动速度
	 */
	// @Override
	// public void fling(int velocityY) {
	// super.fling(velocityY / 10);
	// }

	public interface ScrollViewListener {

		void onScrollChanged(ObservableScrollView scrollView, int x, int y,
				int oldx, int oldy);

	}
}