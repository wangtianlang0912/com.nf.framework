/**   
 * @Title: StaticEventInterface.java 
 * @Package com.ApricotforestStatistic 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author niufei
 * @date 2014-3-18 下午1:51:05 
 * @version V1.0   
*/
package com.ApricotforestStatistic.Service;

import android.content.Context;

interface ApricotEventAgentInterface {

	public void onEvent (Context mcontext,String eventName,int userId,String relatedParam);
	
}
