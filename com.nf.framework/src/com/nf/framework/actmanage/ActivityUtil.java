package com.nf.framework.actmanage;

/*
 * @(#)ActivityUtil.java		       Project:com.sinaapp.msdxblog.androidkit
 * Date:2012-9-17
 */
import android.content.Context;
import android.content.res.Configuration;

public class ActivityUtil {

	/**
	 * 返回当前屏幕是否为竖屏。
	 * 
	 * @param context
	 * @return 当且仅当当前屏幕为竖屏时返回true,否则返回false。
	 */
	public static boolean isScreenOriatationPortrait(Context context) {
		return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

	}
}
