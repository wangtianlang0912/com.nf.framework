package com.nf.framework.http.imageload;


import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;
/**
 * //ClientConnectionManager（关闭所有无效超时的连接）、closeIdleConnection（关闭空闲的连接）
 * 、releaseConnection（释放一个连接）
 * 、requestConnection（请求一个新的连接）
 * 、shutdown（关闭管理器并且释放资源）
 * @author win7
 *
 */
public class ImageGetForHttp {
    private static final String LOG_TAG="ImageGetForHttp";
    private static  BitmapFactory.Options options=null;
     public static Bitmap downloadBitmap(String url,float density) {
            //final int IO_BUFFER_SIZE = 4 * 1024;
    	 		Bitmap bitmap = null;
            HttpClient client = new DefaultHttpClient();// 发送请求
            HttpGet getRequest = new HttpGet(url);
            getRequest.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
            getRequest.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
            HttpEntity entity=null;
            try {
                HttpResponse response = client.execute(getRequest);
                final int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    entity = response.getEntity();
    			}else{
    				Log.d(LOG_TAG, "ImageGetForHttp.downloadBitmap()"+statusCode);
    			}
                if (entity != null) {
                    InputStream inputStream = null;
                    try {
                        inputStream = entity.getContent();
                        // return BitmapFactory.decodeStream(inputStream);
                        // Bug on slow connections, fixed in future release.
                        byte[] bytes=  getBytes(inputStream);
	                      if(options==null){
	                        options=   getBitmapFactoryOptions();
	                      }
	                    Log.d(LOG_TAG, "ImageGetForHttp.downloadBitmap()"+options.inDensity);
                        bitmap=BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);
//                        bitmap = ImageUtil.ScaleBitmap(bitmap, density);
                        bytes=null;
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        entity.consumeContent();
                    }
                }
            } catch (IOException e) {
                getRequest.abort();
                Log.w(LOG_TAG, "I/O error while retrieving bitmap from " + url, e);
            } catch (IllegalStateException e) {
                getRequest.abort();
                Log.w(LOG_TAG, "Incorrect URL: " + url);
            } catch (Exception e) {
                getRequest.abort();
                Log.w(LOG_TAG, "Error while retrieving bitmap from " + url, e);
            } finally {
                Log.w(LOG_TAG, "关闭连接");
                client.getConnectionManager().shutdown();
                getRequest=null;
                client=null; 
            }
            return bitmap;
        }
     private static  Options getBitmapFactoryOptions(){
    	 
    	 	 BitmapFactory.Options options = new BitmapFactory.Options();
         options.inPreferredConfig =Bitmap.Config.ARGB_8888;    // 默认是Bitmap.Config.ARGB_8888  
         /* 下面两个字段需要组合使用 */  
         options.inPurgeable = true;  
         options.inInputShareable = true;
		return options; 
     }
     /*
         * An InputStream that skips the exact number of bytes provided, unless it reaches EOF.
         */
        static class FlushedInputStream extends FilterInputStream {
            public FlushedInputStream(InputStream inputStream) {
                super(inputStream);
            }

            @Override
            public long skip(long n) throws IOException {
                long totalBytesSkipped = 0L;
                while (totalBytesSkipped < n) {
                    long bytesSkipped = in.skip(n - totalBytesSkipped);
                    if (bytesSkipped == 0L) {
                        int b = read();
                        if (b < 0) {
                            break;  // we reached EOF
                        } else {
                            bytesSkipped = 1; // we read one byte
                        }
                    }
                    totalBytesSkipped += bytesSkipped;
                }
                return totalBytesSkipped;
            }
        }

        private static byte[] getBytes(InputStream is) throws IOException {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int len = 0;

            while ((len = is.read(b, 0, 1024)) != -1) 
            {
             baos.write(b, 0, len);
             baos.flush();
            }
            byte[] bytes = baos.toByteArray();
            is.close();
            baos.close();
            baos =null;
            return bytes;
         }
        
        
        
}