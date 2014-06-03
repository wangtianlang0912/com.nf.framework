package com.nf.framework.actmanage;

import android.app.Activity;
import android.content.Context;

/**
 * 双击退出Activity的类。
 * 
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class ExitDoubleClick extends DoubleClick {

	private static ExitDoubleClick exitDoubleClick;

	private ExitDoubleClick(Context context) {
		super(context);
	}

	/**
	 * 返回一个双击退出的实例。
	 * 
	 * @param context
	 * @return ExitDoubleClick
	 */
	public static synchronized ExitDoubleClick getInstance(Context context) {
		if (exitDoubleClick == null) {
			exitDoubleClick = new ExitDoubleClick(context);
		}
		return exitDoubleClick;
	}

	/**
	 * 双击之后退出。
	 */
	@Override
	protected void afterDoubleClick() {
		((Activity) mContext).finish();
		destroy();
	}

	/**
	 * 双击退出Activity，如果msg为null，而默认显示的提示语为"再按一次退出"。
	 */
	@Override
	public void doDoubleClick(int delayTime, String msg) {
		if (msg == null || msg.equals("")) {
			msg = "再按一次退出";
		}
		super.doDoubleClick(delayTime, msg);
	}

	private static void destroy() {
		exitDoubleClick = null;
	}
}
