/**
 * UnScrollGridView.java
 * com.Apricotforest.widgets
 * 工程：medicalJournals_for_android
 * 功能:不会自动滚动，显示gridview中全部内容
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-9-4       下午04:09:51
 * Copyright (c) 2012, TNT All Rights Reserved.
*/

package com.nf.framework.widgets;

import android.view.MotionEvent;
import android.widget.GridView;

public class UnScrollGridView extends GridView{

	public UnScrollGridView(android.content.Context context){
		super(context);
	}
		public UnScrollGridView(android.content.Context context,
				android.util.AttributeSet attrs)
		{
			super(context, attrs);
		}
		
	       //通过重新dispatchTouchEvent方法来禁止滑动
		@Override
		public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			return true;// 禁止Gridview进行滑动
		}
		return super.dispatchTouchEvent(ev);
		}
		/**
		 * 设置不滚动
		 */
		@Override
		public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
		{
			int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
					MeasureSpec.AT_MOST);
			super.onMeasure(widthMeasureSpec, expandSpec);

		}

		public void  requestLayoutIfNecessary(){
			super.requestLayout();
			super.invalidate();
		}
}
