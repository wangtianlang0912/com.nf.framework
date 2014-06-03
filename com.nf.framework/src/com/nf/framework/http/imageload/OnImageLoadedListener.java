/**   
 * @Title: OnImageLoadedListener.java 
 * @Package com.ApricotforestCommon.Util.ImageUtil 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author niufei
 * @date 2014-5-21 下午5:00:53 
 * @version V1.0   
*/
package com.nf.framework.http.imageload;

import android.widget.ImageView;

public interface OnImageLoadedListener {

	
	public void onImageLoaded(ImageView imageView,String imageUrl, boolean isLoadSuccess);
}
