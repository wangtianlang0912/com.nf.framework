package com.ApricotforestStatistic;

import com.ApricotforestStatistic.Service.ApricotStatisticAgent;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
/***
 * 页面访问统计
 * @author niufei
 *
 */
public abstract class AbsStaticFragmentActivity  extends FragmentActivity{
	
	protected void onStaticCreate(Bundle savedInstanceState,Context context) {
		super.onCreate(savedInstanceState);
		ApricotStatisticAgent.onCreateWithUMeng(context);
	}
	
	protected void onStaticResume(Context context,int userId,String pageCode){
		super.onResume();
		ApricotStatisticAgent.onResumeWithUMeng(context,userId, pageCode);
	}
	protected void onStaticPause(Context context,int userId,String pageCode){
		super.onPause();
		ApricotStatisticAgent.onPauseWithUMeng(context,userId, pageCode);
	}
}
