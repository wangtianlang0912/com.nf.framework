package com.nf.framework.http.imageload;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.nf.framework.ImageUtil;

public class ImageLoader {
    private ExecutorService executorService; // 固定五个线程来
    private ImageMemoryCache memoryCache;// 内存缓存
    private ImageFileCache fileCache;// 文件缓存
    private Map<String, WeakReference<ImageView>> taskMap;// 存放任务
    private String cachDir;//缓存路径
    private boolean isNetLimit;///是否开启网络限制
    private int defaultImageResource;//默认图片显示
    private static ImageLoader loadImage=null;
    private boolean isScaleDensity=false;
    private static float density;
    private 	WeakReference<ImageView>   imageViewReference;
    
    
    private static OnImageLoadedListener mOnImageLoadedListener;
    /****
     * 
     * @param activity 默认存储文件路径为 包名
     * @return
     */
    public static ImageLoader getInstance(Activity activity){
    	
    	    DisplayMetrics metric = new DisplayMetrics();
    	    activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
    	    density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
    		mOnImageLoadedListener=null;
    	    if(loadImage==null){
	    		loadImage=new ImageLoader(activity.getPackageName());
	    	}
		return loadImage;
    }
    /**
     * 单例
     * @param cachDir
     * @param isNetLimit
     * @return
     */
    public static ImageLoader getInstance(String cachDir){
    	
	    	if(loadImage==null){
	    		loadImage=new ImageLoader(cachDir);
	    	}
	    	mOnImageLoadedListener=null;
		return loadImage;
    }
    /***
     * 设置缓存路径
     * @param cachDir
     */
    private ImageLoader(String cachDir) {
        executorService = Executors.newFixedThreadPool(10);
        memoryCache = new ImageMemoryCache();
        fileCache = new ImageFileCache(cachDir);
        this.cachDir=cachDir;
        taskMap = new HashMap<String, WeakReference<ImageView>>();
    }
    public void addTask(String url, ImageView imageView) {
    		imageView.setContentDescription(url);
        Bitmap bitmap=getBitmapFromMemoryCache(url);
        if(bitmap!=null)
        {
       		if(((String)imageView.getContentDescription()).equals(url)){
       			imageView.setImageBitmap(bitmap);
       		}
       		recycleBitmap(bitmap);
        }else
        {
        synchronized (taskMap) {
	        	imageViewReference=new  	WeakReference<ImageView>(imageView);
	        taskMap.put(Integer.toString(imageView.hashCode()), imageViewReference);
        }
        }
    }
    
    /***
     * 判断当前view任务是否已经存在
     * @param imageView
     * @return
     */
    private boolean checkImageTaskExist(ImageView imageView){

    		Collection<	WeakReference<ImageView>> imageViewRefrences= 	taskMap.values();
        for (WeakReference<ImageView> imageViewReference : imageViewRefrences) {
            if (imageViewReference!= null&&imageView.equals(imageViewReference.get())) {
            	return true;
            }
        }
        return false;
    }
    public boolean isScaleDensity() {
		return isScaleDensity;
	}

	public void setScaleDensity(boolean isScaleDensity) {
		this.isScaleDensity = isScaleDensity;
	}
    
    public boolean isNetLimit() {
		return isNetLimit;
	}
    /**
     * 是否开启网络限制
     * @param isNetLimit
     */
	public void setNetLimit(boolean isNetLimit) {
		this.isNetLimit = isNetLimit;
	}
	public void doTask() {
        synchronized (taskMap) {
        		Collection<	WeakReference<ImageView>> imageViewRefrences= 	taskMap.values();
            for (WeakReference<ImageView> imageViewReference : imageViewRefrences) {
                if (imageViewReference!= null) {
                		ImageView imageView=   imageViewReference.get();
                    if (imageView!=null&&imageView.getContentDescription() != null) {
                        loadImage((String)imageView.getContentDescription(), imageView);
                        taskMap.remove(Integer.toString(imageView.hashCode()));
                    }
                }else{
                		taskMap.remove(null);
                }
            }
        }
    }

    public void setOnImageLoadedListener(
			OnImageLoadedListener mOnImageLoadedListener) {
		this.mOnImageLoadedListener = mOnImageLoadedListener;
	}
	private void loadImage(String url, ImageView img) {
        /*** 加入新的任务 ***/
    		TaskWithResult taskWithResult=new TaskWithResult(new TaskHandler(url, img),url);
    		executorService.submit(taskWithResult);
    }

    /*** 获得一个图片,从三个地方获取,首先是内存缓存,然后是文件缓存,最后从网络获取 ***/
    private Bitmap getBitmap(String url,String cachdir) {
        // 从内存缓存中获取图片
        Bitmap result;
        result =getBitmapFromMemoryCache(url);
        result = ImageUtil.ScaleBitmap(result, isScaleDensity()?density:0);
        if (result == null) {
            // 文件缓存中获取
    			result = fileCache.getImage(isScaleDensity()?density:0,url,cachdir);
            if (result == null) {
            	if(!isNetLimit){//如果开启网络限制，则不使用网络加载 默认不限制
	                // 从网络获取
	                result = ImageGetForHttp.downloadBitmap(url,isScaleDensity()?density:0);
	                if (result != null) {
	                		addBitmapToMemoryCache(url, result);
	                    fileCache.saveBmpToSd(result, url,cachdir);                    
	                }
            	}
            } else {
             
            }
        }
        return result;
    }

    /***
     * 将图片添加到内存
     * @param picUrl
     * @param bitmap
     */
    public void addBitmapToMemoryCache(String picUrl, Bitmap bitmap){
    	
    	   // 添加到内存缓存
    		if(memoryCache!=null)
        memoryCache.addBitmapToCache(picUrl, bitmap);
    }
    /***
     *	内存中返回bitmap
     * @param picUrl
     * @return
     */
    public Bitmap getBitmapFromMemoryCache(String picUrl){
    		if(memoryCache!=null)
    		return  memoryCache.getBitmapFromCache(picUrl);
		return null;
    }
    /*** 完成消息 ***/
    private class TaskHandler extends Handler {
        String url;
        ImageView img;
        public TaskHandler(String url, ImageView img) {
            this.url = url;
            this.img = img;
        }
        @Override
        public void handleMessage(Message msg) {
            /*** 查看imageview需要显示的图片是否被改变 ***/
            if (img!=null&&img.getContentDescription().equals(url)) {
            		boolean isLoadSuccess;
            		if (msg.obj != null) {
                    Bitmap bitmap = (Bitmap) msg.obj;
//                    System.out.println("LoadImage.TaskHandler.handleMessage()"+bitmap.getByteCount());
//                    if(isScaleDensity()){
//                    		bitmap=ImageUtil.ScaleBitmap(bitmap, density);
//                    }
//                    System.out.println("LoadImage.TaskHandler.handleMessage()"+bitmap.getByteCount());
                    isLoadSuccess=(bitmap!=null);
                    img.setImageBitmap(bitmap);
                    recycleBitmap(bitmap);
                    
                }else{
	                	if(defaultImageResource!=0){
	                		img.setImageResource(defaultImageResource);
	                	}
	                	isLoadSuccess=false;
                }
                if(mOnImageLoadedListener!=null){
                		mOnImageLoadedListener.onImageLoaded(img,url,isLoadSuccess);
                }
                
            }
        }
    }

    /*** 子线程任务 ***/
    private class TaskWithResult implements Callable<String> {
        private String url;
        private Handler handler;

        public TaskWithResult(Handler handler, String url) {
            this.url = url;
            this.handler = handler;
        }

        @Override
        public String call() throws Exception {
            Message msg = new Message();
            msg.obj = getBitmap(url,cachDir);
            handler.sendMessage(msg);
            return url;
        }

    }
    /**
     * 设置无法获取图片时的默认图片
     * @param defaultImageResource
     */
	public void setDefaultImageResource(int defaultImageResource) {
		this.defaultImageResource = defaultImageResource;
	}
 private void recycleBitmap(Bitmap bitmap){
	 
	  if(bitmap!=null&&bitmap.isRecycled()){
      	bitmap=null;
      }
 }   
}