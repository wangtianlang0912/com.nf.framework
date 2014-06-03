package com.nf.framework.skin;
/**
 * 功能： TODO 
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-8-22       下午04:31:02
 * Copyright (c) 2012, TNT All Rights Reserved.
*/
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
/**
* PadQzone皮肤管理器
* @author frankiewei
*
*/
public class SkinSettingManage {
	public final static String SKIN_PREF = "skinSetting";
	public SharedPreferences skinSettingPreference;
	private int[] skinResources = {};
	private Activity mActivity;
	
	
	public SkinSettingManage(Activity activity) {
		this.mActivity = activity;
		skinSettingPreference = mActivity.getSharedPreferences(SKIN_PREF, 3);
	}
	/**
	* 获取当前程序的皮肤序号
	*
	* @return
	*/
	public int getSkinType() {
		String key = "skin_type";
		return skinSettingPreference.getInt(key, 0);
	}
	/**
	* 把皮肤序号写到全局设置里去
	*
	* @param j
	*/
	public void setSkinType(int j) {
		SharedPreferences.Editor editor = skinSettingPreference.edit();
		String key = "skin_type";
		editor.putInt(key, j);
		editor.commit();
	}
	/**
	* 获取当前皮肤的背景图资源id
	*
	* @return
	*/
	public int getCurrentSkinRes() {
		int skinLen = skinResources.length;
		int getSkinLen = getSkinType();
		if(getSkinLen >= skinLen){
		getSkinLen = 0;
		}
		return skinResources[getSkinLen];
	}
	/**
	* 用于导航栏皮肤按钮切换皮肤
	*/
	public void toggleSkins(){
		int skinType = getSkinType();
		if(skinType == skinResources.length - 1){
			skinType = 0;
		}else{
			skinType ++;
		}
		setSkinType(skinType);
		mActivity.getWindow().setBackgroundDrawable(null);
		try {
			mActivity.getWindow().setBackgroundDrawableResource(getCurrentSkinRes());
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取安装上的全部皮肤
	 * @return
	 */
	public ArrayList<PackageInfo> getAllSkin() {
		ArrayList<PackageInfo> skinList = new ArrayList<PackageInfo>();
		List<PackageInfo> packs = mActivity.getPackageManager().getInstalledPackages(0);
		for (PackageInfo p : packs) {
			if (isSkinPackage(p.packageName)) {
				skinList.add(p);
			}
		}
		return skinList;
	}
/**
 * 判断是否为对应软件的皮肤
 * @param packageName
 * @return
 */
	private boolean isSkinPackage(String packageName) {
		String rex = "com.Apricotforest_epocket\\w";
		Pattern pattern = Pattern.compile(rex);
		Matcher matcher = pattern.matcher(packageName);
		return matcher.find();
	}
	
	/**
	* 用于初始化皮肤
	*/
	public void initSkins(){
		mActivity.getWindow().setBackgroundDrawableResource(getCurrentSkinRes());
	}
}