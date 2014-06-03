package com.nf.framework;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.nf.framework.util.io.FileUtils;
import com.nf.framework.util.io.SdCardUtil;


/**
 * 异步图片加载
 * @author niufei
 *
 */
public class AsyncImageBitmapLoader {
//	private static ImageCacheManager imageCacheManager;
	 //SoftReference是软引用，是为了更好的为了系统回收变量
    public static HashMap<String, SoftReference<Bitmap>> imageCache;
    public static AsyncImageBitmapLoader aibl=null;
    static {
    	imageCache = new HashMap<String, SoftReference<Bitmap>>();
    }
    public AsyncImageBitmapLoader() {
    }
 public static AsyncImageBitmapLoader getInstance() {
        if(aibl==null){
        	aibl=new AsyncImageBitmapLoader();
        	 /* FMU算法，在存储器中固定必然大小的存储空间，跨越固定空间后将缓存中占用最大尺寸的图片删除*/
//        	imageCacheManager = ImageCacheManager.getImageCacheService(mcontext,
//        			ImageCacheManager.MODE_FIXED_MEMORY_USED, "memory");
//        	imageCacheManager.setMax_Memory(1024 * 1024);

        	/*FTU算法，固定每张图片的缓存时限，以最后一次应用算起，跨越时限后删除 */
//        	 imageCacheManager = ImageCacheManager.getImageCacheService(mcontext,
//        	 ImageCacheManager.MODE_FIXED_TIMED_USED, "time");
//        	 imageCacheManager.setDelay_millisecond(3 * 60 * 1000);
        	 /* LRU算法，固定缓存图片数量（max_num），当图片数量超出max_num时，将缓存中比来用的起码的图片删除*/
//        	 imageCacheManager = ImageCacheManager.getImageCacheService(mcontext,
//        	 ImageCacheManager.MODE_LEAST_RECENTLY_USED, "num");
//        	 imageCacheManager.setMax_num(50);
        	 /*不适用缓存管理*/
//        	 imageCacheManager = ImageCacheManager.getImageCacheService(mcontext,
//        	 ImageCacheManager.MODE_NO_CACHE_USED, "nocache");
        }
        return aibl;
    }
    public Bitmap loadBitmap(final String imageStorePath,final String imageUrl,final ImageView imageView,final ImageCallback imageCallback){
        if (imageCache.containsKey(imageUrl)) {
            //从缓存中获取
            SoftReference<Bitmap> softReference = imageCache.get(imageUrl);
            Bitmap bitmap = softReference.get();
            if (bitmap != null) {
                return bitmap;
            }
        }
        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
                imageCallback.imageLoaded(imageStorePath,(Bitmap) message.obj, imageView,imageUrl);
            }
        };
        //建立新一个新的线程下载图片
        new Thread() {
            @Override
            public void run() {
                Bitmap bitmap = null;
				try {
					String imageName=imageUrl.substring(imageUrl.lastIndexOf("/")+1,imageUrl.length());//获取图片名称
					String absolutepicPath=imageStorePath+File.separator+imageName;
					boolean isPicExist=FileUtils.getInstance().isAbsolutePackageExist(absolutepicPath);//判断是否存在该图片
					if(isPicExist){//存在
						bitmap=FileUtils.getInstance().buildBitmapByAbsolutePath(absolutepicPath);	
					}else{
						bitmap = ImageUtil.getBitmapFromUrl(imageUrl);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
                imageCache.put(imageUrl, new SoftReference<Bitmap>(bitmap));
                Message message = handler.obtainMessage(0, bitmap);
                bitmap=null;
                handler.sendMessage(message);
            }
        }.start();
        return null;
    }
    //回调接口
    public interface ImageCallback {
        public void imageLoaded(String imageStorePath,Bitmap imageBitmap,ImageView imageView, String imageUrl);
    }
    public Bitmap loadBitmap(final String imageUrl,final BitmapImageCallback imageCallback){
        if (imageCache.containsKey(imageUrl)) {
            //从缓存中获取
            SoftReference<Bitmap> softReference = imageCache.get(imageUrl);
            Bitmap bitmap = softReference.get();
            if (bitmap != null) {
                return bitmap;
            }
        }
        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
                imageCallback.imageLoaded((Bitmap) message.obj,imageUrl);
            }
        };
        //建立新一个新的线程下载图片
        new Thread() {
            @Override
            public void run() {
                Bitmap bitmap = null;
				try {
					bitmap = ImageUtil.getBitmapFromUrl(imageUrl);
				} catch (Exception e) {
					e.printStackTrace();
				}
                imageCache.put(imageUrl, new SoftReference<Bitmap>(bitmap));
                Message message = handler.obtainMessage(0, bitmap);
                bitmap=null;
                handler.sendMessage(message);
            }
        }.start();
        return null;
    }
    //回调接口
    public interface BitmapImageCallback {
        public void imageLoaded(Bitmap imageBitmap,String imageUrl);
    }
    /**
     * 仅从sd卡中获取图片
     * @param imageStorePath
     * @param imageUrl
     * @param imageView
     * @param imageCallback
     * @return
     */
    public Bitmap loadLocalBitmap(final String imageStorePath,final String imageName,final ImageView imageView,final float density,final ImageCallback imageCallback){
        if (imageCache.containsKey(imageName)) {
            //从缓存中获取
            SoftReference<Bitmap> softReference = imageCache.get(imageName);
            Bitmap bitmap = softReference.get();
            if (bitmap != null) {
                return bitmap;
            }
        }
        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
                imageCallback.imageLoaded(imageStorePath,(Bitmap) message.obj, imageView,imageName);
            }
        };
        //建立新一个新的线程下载图片
        new Thread() {
            @Override
            public void run() {
                Bitmap bitmap = null;
				try {
					String imageFilePath= getTempBitmapFilePath(imageStorePath, imageName);//判断是否存在该图片
					boolean isPicExist=new File(imageFilePath).exists();
					if(isPicExist){//存在
						bitmap=FileUtils.getInstance().buildBitmap(imageFilePath,density);	
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
                imageCache.put(imageName, new SoftReference<Bitmap>(bitmap));
                Message message = handler.obtainMessage(0, bitmap);
                bitmap=null;
                handler.sendMessage(message);
            }
        }.start();
        return null;
    }
    public static String getTempBitmapFilePath(String imageFolder,String fileName){
		return new SdCardUtil().getSDCardPath()  +File.separator+ imageFolder +File.separator + fileName;
   }
}
