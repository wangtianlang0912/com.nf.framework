/**   
 * @Title: StaticActInterface.java 
 * @Package com.ApricotforestStatistic 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author niufei
 * @date 2014-3-18 下午1:50:46 
 * @version V1.0   
*/
package com.ApricotforestStatistic.Service;

import android.content.Context;

interface ApricotPageViewAgentInterface {

	public  void onResume(Context context,int userId,String currentPageCode);

	public  void onPause(Context context,int userId,String currentPageCode);
}
