/**   
 * @Title: ApricotErrorRequestAgentInterface.java 
 * @Package com.ApricotforestStatistic.Service 
 * @author niufei
 * @date 2014-3-18 ����2:43:08 
 * @version V1.0   
 */
package com.ApricotforestStatistic.Service;

import android.content.Context;

interface ApricotErrorRequestAgentInterface {

	public void onRequestUrl(Context mcontext, String requestUrl,
			int errorReponseCode, boolean isRequestByPost);
}
