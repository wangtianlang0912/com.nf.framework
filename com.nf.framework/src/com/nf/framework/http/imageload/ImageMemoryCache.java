package com.nf.framework.http.imageload;


import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
/****
 *内存中的缓存 
 ****/
public class ImageMemoryCache {
      private static final int HARD_CACHE_CAPACITY =6;
      private final static ConcurrentHashMap<String, SoftReference<Bitmap>> mSoftBitmapCache =
            new ConcurrentHashMap<String, SoftReference<Bitmap>>(HARD_CACHE_CAPACITY);
      //开辟8M硬缓存空间
      // 获取虚拟机可用内存（内存占用超过该值的时候，将报OOM异常导致程序崩溃）。最后除以1024是为了以kb为单位
      final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
      // 使用可用内存的1/8来作为Memory Cache
      final int cacheSize = maxMemory / 8;
//      private final int hardCachedSize =4*1024*1024;		
      private LruCache<String, Bitmap> mHardBitmapCache=null;
      public ImageMemoryCache() {
    	  mHardBitmapCache= new LruCache<String, Bitmap>(cacheSize){
    		  @Override
  			public int sizeOf(String key, Bitmap value){
  				return value.getRowBytes() * 1024;
  			}
  			@Override
  			protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue){
  				if(mSoftBitmapCache.size()>HARD_CACHE_CAPACITY){
  					Log.w("软引用缓存区超过"+HARD_CACHE_CAPACITY,"清空一次");
  					mSoftBitmapCache.clear();
  				}
  				Log.v("tag", "hard cache is full , push to soft cache");
  				//硬引用缓存区满，将一个最不经常使用的oldvalue推入到软引用缓存区
  				mSoftBitmapCache.put(key, new SoftReference<Bitmap>(oldValue));
  				oldValue=null;
  				newValue=null;
  			}
  		};           
      }
      
      /**
      * 从缓存中获取图片
      */
      public Bitmap getBitmapFromCache(String url) {
	      // 先从mHardBitmapCache缓存中获取
	      synchronized (mHardBitmapCache) {
	      final Bitmap bitmap =mHardBitmapCache.get(url);
	      if (bitmap != null) {
		      //如果找到的话，把元素移到linkedhashmap的最前面，从而保证在LRU算法中是最后被删除
		      mHardBitmapCache.remove(url);
		      mHardBitmapCache.put(url,bitmap);
		      return bitmap;
	      }
	      }
	      //如果mHardBitmapCache中找不到，到mSoftBitmapCache中找
	      SoftReference<Bitmap>bitmapReference = mSoftBitmapCache.get(url);
	      if (bitmapReference != null) {
	      final Bitmap bitmap =bitmapReference.get();
	      if (bitmap != null) {
	          //将图片移回硬缓存
	          mHardBitmapCache.put(url, bitmap);
	          Log.e("硬缓存硬缓存硬缓存",""+mHardBitmapCache.size());
	          Log.e("软缓存软缓存软缓存",""+mSoftBitmapCache.size());
	          mSoftBitmapCache.remove(url);
	      return bitmap;
	      } else {
	    	  mSoftBitmapCache.remove(url);
	      }
	      }
	      return null;
      } 
      /***添加图片到缓存***/
      public void addBitmapToCache(String url, Bitmap bitmap) {
            if (bitmap != null) {
                synchronized (mHardBitmapCache) {
                    mHardBitmapCache.put(url, bitmap);
                }
                bitmap=null;
            }
        }
}