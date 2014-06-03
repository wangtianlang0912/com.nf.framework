package com.nf.framework;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
 
public class CloseActivityClass{
	
	
	 public static List<Activity> activityList = new ArrayList<Activity>();
	/* 
     * @param context 上下文
     */
    public static void exitClient(Context context)
    {
        Log.d("CloseActivityClass", "----- exitClient -----");
        AsyncImageBitmapLoader.imageCache.clear();
        AsyncImageLoader.imageCache.clear();
        // 关闭所有Activity
        int len=activityList.size();
        for (int i = 0; i <len; i++)
        {
            if (null != activityList.get(i))
            {
                activityList.get(i).finish();
            }
        }
        ActivityManager activityMgr= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE );
        activityMgr.restartPackage(context.getPackageName());

//        System.exit(0);
    }
/**
 * 关闭单个activity
 */
    public static void closeActivityByContent(Context mcontext){
    	
    	int index=activityList.indexOf(mcontext);
    	if(index!=-1){
    		activityList.get(index).finish();
    	}
    }
    /**
     * 20130520 新增
     * 关闭单个activity
     */
    public static void closeActivityBySimpleName(String simpleActiName){
    	
    	  for(Activity activity:CloseActivityClass.activityList){
			  if(activity.getClass().getSimpleName().equals(simpleActiName))
			   activity.finish();
		}
     }
}