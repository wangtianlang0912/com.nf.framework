package com.nf.framework.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
/**
 * 
 * @author niufei
 *
 */
public class HackyViewPager extends ViewPager {

	public HackyViewPager(Context context) {
		super(context);
	}
	 public HackyViewPager(Context context, AttributeSet attrs) {
		    super(context, attrs);
    }
/**
 * 重写该方法，否则会报java.lang.illegalargumentexception pointerindex out of range
 * 详见：http://blog.csdn.net/eoeandroida/article/details/7954398
 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			return super.onInterceptTouchEvent(ev);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return false;
		}
	}

}
