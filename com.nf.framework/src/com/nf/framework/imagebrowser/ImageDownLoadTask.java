package com.nf.framework.imagebrowser;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.ApricotforestStatistic.Service.ApricotStatisticAgent;
import com.nf.framework.CheckInternet;
import com.nf.framework.ImageUtil;
import com.nf.framework.exception.XingshulinError;
import com.nf.framework.http.imageload.ImageLoader;
import com.nf.framework.widgets.zoomPhotoView.PhotoView;

public class ImageDownLoadTask extends AsyncTask<String, Integer, Bitmap> {
	private static final String TAG = "ImageDownLoadTask";
	private PhotoView pdView;
	private Context mcontext;
	private ImageBrowserVO imageVo;
	private ProgressBar progressBar;
	private String imageUrl;
	private int blantImageResource;
	private static final String LOG_TAG = "ImageGetForHttp";
	private static BitmapFactory.Options options = null;
	private ImageLoader loadImage;
	
	public ImageDownLoadTask(Context mcontext,ImageLoader loadImage, PhotoView pdView,
			ProgressBar progressBar, int blantImageResource,
			ImageBrowserVO imageVo) {
		this.mcontext = mcontext;
		this.pdView = pdView;
		this.blantImageResource = blantImageResource;
		this.imageVo = imageVo;
		this.progressBar = progressBar;
		this.loadImage=loadImage;
	}

	/** 下载准备工作。在UI线程中调用。 */
	protected void onPreExecute() {
		Log.i(TAG, "onPreExecute");
		imageUrl = imageVo.getPicUrl();
		if (imageVo != null && imageVo.getSmailPicUrlLocalPath() != null) {
			 DisplayMetrics dm = new DisplayMetrics();
			   ((Activity)mcontext).getWindowManager().getDefaultDisplay().getMetrics(dm);
			Bitmap smailBitmap=ImageUtil.decodeBitmapFromFile(imageVo
					.getSmailPicUrlLocalPath(),dm.widthPixels/2,dm.heightPixels/2);
			pdView.setImageBitmap(smailBitmap);
			if(smailBitmap != null && !smailBitmap.isRecycled()){  
				smailBitmap = null; 
			}
		}
		progressBar.setVisibility(View.VISIBLE);
	}

	/** 执行下载。在背景线程调用。 */
	protected Bitmap doInBackground(String... params) {
		Bitmap bitmap = null;
		try {
			if (!TextUtils.isEmpty(imageUrl)) {
				if(loadImage!=null){//内存中获取
					bitmap=	loadImage.getBitmapFromMemoryCache(imageUrl);
					if(bitmap!=null){
						return bitmap;
					}
				}
				bitmap=ImageBrowseUtil.getBitmapFromLocalFile(mcontext,imageUrl);
				if(bitmap!=null){
					if(loadImage!=null){
						loadImage.addBitmapToMemoryCache(imageUrl, bitmap);
					}
					return bitmap;
				}
				if (CheckInternet.getInstance(mcontext).checkInternet()) {
					HttpClient client = new DefaultHttpClient();// 发送请求
					HttpGet getRequest = new HttpGet(imageUrl);
					getRequest.getParams().setIntParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
					getRequest.getParams().setIntParameter(
							CoreConnectionPNames.SO_TIMEOUT, 20000);
					HttpEntity entity = null;
					try {
						HttpResponse response = client.execute(getRequest);
						final int statusCode = response.getStatusLine()
								.getStatusCode();
						if (statusCode == HttpStatus.SC_OK) {
							entity = response.getEntity();
						} else {
							ApricotStatisticAgent.onErrorRequestUrl(mcontext, imageUrl, statusCode, false);
						}
						if (entity != null) {
							InputStream inputStream = null;
							try {
								inputStream = entity.getContent();
								long contentLength = entity.getContentLength();
								ByteArrayOutputStream baos = new ByteArrayOutputStream();
								byte[] b = new byte[1024];
								int len = 0;
								int completeLength = 0;
								while ((len = inputStream.read(b, 0, 1024)) != -1) {
									baos.write(b, 0, len);
									baos.flush();
									completeLength += len;
									publishProgress((int)(completeLength/contentLength*100));
								}
								byte[] bytes = baos.toByteArray();
								inputStream.close();
								baos.close();
								baos = null;
								if (options == null) {
									options = getBitmapFactoryOptions();
								}
								bitmap = BitmapFactory.decodeByteArray(bytes,
										0, bytes.length, options);
								if(bitmap!=null){
									if(loadImage!=null){
										loadImage.addBitmapToMemoryCache(imageUrl, bitmap);
									}
									ImageBrowseUtil.getFileFromBytes(bytes,mcontext,imageUrl);
								}
								bytes = null;
								
							} finally {
								if (inputStream != null) {
									inputStream.close();
								}
								entity.consumeContent();
							}
						}
					} catch (Exception e) {
						getRequest.abort();
						throw new XingshulinError(mcontext,e);
					} finally {
						Log.w(LOG_TAG, "关闭连接");
						client.getConnectionManager().shutdown();
						getRequest = null;
						client = null;
					}
					return bitmap;
				}
			}
			return bitmap;
		} catch (Exception e) {
		}
		return null;
	}

	/** 更新下载进度。在UI线程调用。onProgressUpdate */
	protected void onProgressUpdate(Integer... progress) {
		// LogOut.out(this, "onProgressUpdate");
		progressBar.setProgress(progress[0]);
	}

	/** 通知下载任务完成。在UI线程调用。 */
	protected void onPostExecute(Bitmap bitmap) {
		Log.i(TAG, "onPostExecute");
		progressBar.setVisibility(View.GONE);
		if (pdView != null){
			if(bitmap != null) {
				pdView.setImageBitmap(bitmap);
			} else {
				if (blantImageResource != 0)
					pdView.setImageResource(blantImageResource);
			}
		}
		if(bitmap != null && !bitmap.isRecycled()){  
			bitmap = null; 
		}
	}

	protected void onCancelled() {
		Log.i(TAG, "DownloadImgTask cancel...");
		super.onCancelled();
	}

	private static Options getBitmapFactoryOptions() {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888; // 默认是Bitmap.Config.ARGB_8888
		/* 下面两个字段需要组合使用 */
		options.inPurgeable = true;
		options.inInputShareable = true;
		return options;
	}

	
}
