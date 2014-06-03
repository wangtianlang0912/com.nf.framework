package com.nf.framework;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class ImageUtil {
	/**
	 * 通过文件地址获取数据流
	 * 
	 * @param path
	 *            地址
	 * @return
	 * @throws Exception
	 */

	public static InputStream getRequest(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5000);
		if (conn.getResponseCode() == 200) {
			return conn.getInputStream();
		}
		return null;
	}

	/**
	 * 数据流转成byte
	 * 
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}

	/**
	 * 网上获取drawable图片
	 * 
	 * @param url
	 * @return
	 */
	public static Drawable loadImageFromUrl(String url) {
		URL m;
		InputStream i = null;
		try {
			m = new URL(url);
			i = (InputStream) m.getContent();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Drawable d = Drawable.createFromStream(i, "src");
		return d;
	}
  /***
    * 放大缩小bitmap操作
    * @param bitmap
    * @param density 如果density为0 则返回原图大小
    * @return
    */
	 public  static Bitmap ScaleBitmap(Bitmap bitmap,float density) {
		 if(bitmap==null){
			 return bitmap;
		 }
		 if(density==0){
			 density=1.0f;
		 }
		  Matrix matrix = new Matrix(); 
		  matrix.postScale(density,density); //长和宽放大缩小的比例
		  Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
		  return resizeBmp;
		 }
	/**
	 * 网上获取drawable图片
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static Drawable getDrawableFromUrl(String url) throws Exception {
		return Drawable.createFromStream(getRequest(url), null);
	}

	/**
	 * 将图片缩放成width*height 并且返回这张图片
	 * 
	 * @param bm
	 * @return
	 */
	public static Bitmap getCommonScaledBitmap(String url, int width, int height) {

		Bitmap bm = null;
		try {
			bm = getBitmapFromUrl(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Bitmap.createScaledBitmap(bm, width, height, true);
	}

	/**
	 * 将图片缩放成width*height 并且返回这张图片
	 * 
	 * @param bm
	 * @return
	 */
	public static Bitmap CommonScaledBitmap(Bitmap bm, int width, int height) {
		return Bitmap.createScaledBitmap(bm, width, height, true);
	}

	/**
	 * 网络获取bitmap图片
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static Bitmap getBitmapFromUrl(String url) throws Exception {
		byte[] bytes = getBytesFromUrl(url);
		return byteToBitmap(bytes);
	}
	/**
	 * 网络获取bitmap图片
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static Bitmap getBitmapFromUrl(Display display,String url) throws Exception {
		byte[] bytes = getBytesFromUrl(url);
		
		return byteToBitmap(display,bytes);
	}
	/**
	 * byte-->bitmap
	 * 
	 * @param byteArray
	 * @return
	 */ 
	public static Bitmap byteToBitmap(Display display,byte[] byteArray) {
		Bitmap bitmap=null;
		if (byteArray.length != 0) {
			 BitmapFactory.Options options = new BitmapFactory.Options();
			 options.inJustDecodeBounds = true;
			 BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length,options);
			 if(display!=null){
			 // Calculate inSampleSize
				 options.inSampleSize = calculateInSampleSize(options, display.getWidth(), display.getHeight()); /*图片长宽方向缩小倍数*/
			 }else{
				 options.inSampleSize=1; 
			 }
			 // Decode bitmap with inSampleSize set
			 options.inJustDecodeBounds = false;
			 options.inDither=false;    /*不进行图片抖动处理*/
			 options.inPreferredConfig=null;  /*设置让解码器以最佳方式解码*/
			 /* 下面两个字段需要组合使用 */  
			 options.inPurgeable = true;  
			 options.inInputShareable = true; 
			 bitmap= BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length,options);
			 byteArray=null;
		}
		return bitmap;
	}
	/**
	 * 网络获取圆角bitmap图片
	 * 
	 * @param url
	 * @param pixels
	 *            圆角度
	 * @return
	 * @throws Exception
	 */
	public static Bitmap getRoundBitmapFromUrl(String url, int pixels)
			throws Exception {
		byte[] bytes = getBytesFromUrl(url);
		Bitmap bitmap = byteToBitmap(bytes);
		return toRoundCorner(bitmap, pixels);
	}

	/**
	 * 通过url地址获取圆角drawable
	 * 
	 * @param url
	 * @param pixels
	 * @return
	 * @throws Exception
	 */
	public static Drawable geRoundDrawableFromUrl(String url, int pixels)
			throws Exception {
		byte[] bytes = getBytesFromUrl(url);
		BitmapDrawable bitmapDrawable = (BitmapDrawable) byteToDrawable(bytes);
		return toRoundCorner(bitmapDrawable, pixels);
	}

	public static byte[] getBytesFromUrl(String url) throws Exception {
		return readInputStream(getRequest(url));
	}

	/**
	 * byte-->bitmap
	 * 
	 * @param byteArray
	 * @return
	 */
	public static Bitmap byteToBitmap(byte[] byteArray) {
		if (byteArray.length != 0) {
			return BitmapFactory
					.decodeByteArray(byteArray, 0, byteArray.length);
		} else {
			return null;
		}
	}

	/**
	 * Bitmap---->InputStream
	 * 
	 * @return
	 */
	public static InputStream BitmapTOInputStream(Bitmap bitmap) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
		return inputStream;
	}

	/**
	 * byte-->drawable
	 * 
	 * @param byteArray
	 * @return
	 */
	public static Drawable byteToDrawable(byte[] byteArray) {
		ByteArrayInputStream ins = new ByteArrayInputStream(byteArray);
		return Drawable.createFromStream(ins, null);
	}

	/**
	 * bitmap-->byte
	 * 
	 * @param bm
	 * @return
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * Bitmap → Drawable
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Drawable convertBitmap2Drawable(Bitmap bitmap) {
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		// 因为BtimapDrawable是Drawable的子类，最终直接使用bd对象即可。
		return bd;
	}

	/**
	 * Drawable-->Bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {

		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
				.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * drawable-->RoundCornerBitmap
	 * 
	 * @param drawable
	 * @param pixels
	 * @return
	 */
	public static Bitmap drawableToRoundCornerBitmap(Drawable drawable,
			int pixels) {

		return toRoundCorner(drawableToBitmap(drawable), pixels);
	}

	/**
	 * 图片去色,返回灰度图片
	 * 
	 * @param bmpOriginal
	 *            传入的图片
	 * @return 去色后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	/**
	 * 去色同时加圆角
	 * 
	 * @param bmpOriginal
	 *            原图
	 * @param pixels
	 *            圆角弧度
	 * @return 修改后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
		return toRoundCorner(toGrayscale(bmpOriginal), pixels);
	}

	/**
	 * 把图片变成圆角
	 * 
	 * @param bitmap
	 *            需要修改的图片
	 * @param pixels
	 *            圆角的弧度
	 * @return 圆角图片
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
				.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * 使圆角功能支持BitampDrawable
	 * 
	 * @param bitmapDrawable
	 * @param pixels
	 * @return
	 */
	public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable,
			int pixels) {
		Bitmap bitmap = bitmapDrawable.getBitmap();
		bitmapDrawable = new BitmapDrawable(toRoundCorner(bitmap, pixels));
		return bitmapDrawable;
	}
	/**
	 * 文件中读取图片
	 * @param filePath
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeBitmapFromFile(String filePath,int reqWidth, int reqHeight){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath,options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false; // 设置了此属性一定要记得将值设置为false
		return	BitmapFactory.decodeFile(filePath,options);
	}
	/**
	 * 文件中读取图片
	 * @param filePath
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeBitmapFromFile(String filePath){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath,options);
		options.inJustDecodeBounds = false; // 设置了此属性一定要记得将值设置为false
		return	BitmapFactory.decodeFile(filePath,options);
	}
	/**
	 * 文件中读取图片 根据屏幕的精度大小来显示图片的宽高
	 * @param filePath
	 * @param density  
	 * @return
	 */
	public static Bitmap decodeBitmapFromFile(String filePath,float density){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath,options);
		
		if((int)density==0){
			density=1; 
			
		}
		options.inTargetDensity=(int)density;
		options.inJustDecodeBounds = false; // 设置了此属性一定要记得将值设置为false
		Bitmap bitmap=	BitmapFactory.decodeFile(filePath,options);
		return	bitmap;
	}
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {
	        if (width > height) {
	            inSampleSize = Math.round((float)height / (float)reqHeight);
	        } else {
	            inSampleSize = Math.round((float)width / (float)reqWidth);
	        }
	    }
	    return inSampleSize;
	}
	/**
	 * String url---》bitmap
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap PicURLToBitmap(String url) {

		Bitmap bitmap = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			// 生成一个http客户端对象
			HttpClient httpClient = new DefaultHttpClient();// 发送请求
			httpClient.getParams().setIntParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
			httpClient.getParams().setIntParameter(
					CoreConnectionPNames.SO_TIMEOUT, 20000);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity entity = httpResponse.getEntity();
			InputStream in = entity.getContent();

			if (in != null) {

				byte[] bt = getBytes(in);
				bitmap = BitmapFactory.decodeByteArray(bt, 0, bt.length);
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;

	}

	/**
	 * InputStream-->byte[]
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	private static byte[] getBytes(InputStream is) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int len = 0;

		while ((len = is.read(b, 0, 1024)) != -1) {
			baos.write(b, 0, len);
			baos.flush();
		}
		byte[] bytes = baos.toByteArray();
		return bytes;
	}

	/**
	 * Bytes---->Bimap
	 * 
	 * @param b
	 * @return
	 */
	private Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	/**
	 *设置背景图片平铺效果
	 */
	public static BitmapDrawable getBitmapDrawableRepeat(Context context,
			int ResourceId) {

		Bitmap bottom_bitmap = BitmapFactory.decodeResource(context
				.getResources(), ResourceId);
		BitmapDrawable bd = new BitmapDrawable(bottom_bitmap);
		bd.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
		bd.setDither(true);
		return bd;
	}

	/**
	 * 质量压缩方法
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
			int options = 100;
			while ( baos.toByteArray().length / 1024>100) {	//循环判断如果压缩后图片是否大于100kb,大于继续压缩		
				baos.reset();//重置baos即清空baos
				image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
				options -= 10;//每次都减少10
			}
			ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
			Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
			return bitmap;
		}
	 /**
	  * 图片按比例大小压缩方法（根据路径获取图片并压缩）：
	  * @param srcPath
	  * @return
	  */
	public static Bitmap getCompressImageByFilePath(Context context,String srcPath) {
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			//开始读入图片，此时把options.inJustDecodeBounds 设回true了
			newOpts.inJustDecodeBounds = true;
			Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空
			
			newOpts.inJustDecodeBounds = false;
			int w = newOpts.outWidth;
			int h = newOpts.outHeight;
			//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
//			float hh = 800f;//这里设置高度为800f
//			float ww = 480f;//这里设置宽度为480f
			WindowManager wm = (WindowManager) ((Activity)context).getSystemService(Context.WINDOW_SERVICE);
			int ww = wm.getDefaultDisplay().getWidth();//屏幕宽度
			int hh = wm.getDefaultDisplay().getHeight();//屏幕高度
			//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
			int be = 1;//be=1表示不缩放
			if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
				be =(int)Math.ceil(newOpts.outWidth/(float)ww);
			} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
				be =(int)Math.ceil(newOpts.outHeight/(float)hh);
			}
			if (be <= 0)
				be = 1;
			newOpts.inSampleSize = be;//设置缩放比例
			//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
			bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
			return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
		}
	 /**
	  * 图片按比例大小压缩方法（根据Bitmap图片压缩）：
	  * 循环判断如果压缩后图片是否大于100kb,大于继续压缩		
	  * @param image
	  * @return
	  */
	public static Bitmap getCompressImageByBitmap(Context context,Bitmap image) {
			if(context==null){
				return null;
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();		
			image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出	
				baos.reset();//重置baos即清空baos
				image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
			}
			ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			//开始读入图片，此时把options.inJustDecodeBounds 设回true了
			newOpts.inJustDecodeBounds = true;
			Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
			newOpts.inJustDecodeBounds = false;
			int w = newOpts.outWidth;
			int h = newOpts.outHeight;
			//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
//			float hh = 800f;//这里设置高度为800f
//			float ww = 480f;//这里设置宽度为480f
			WindowManager wm = (WindowManager) ((Activity)context).getSystemService(Context.WINDOW_SERVICE);
			int ww = wm.getDefaultDisplay().getWidth();//屏幕宽度
			int hh = wm.getDefaultDisplay().getHeight();//屏幕高度
			Log.i("ImageUtil",ww+";"+hh);
			//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
			int be = 1;//be=1表示不缩放
			if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
				be =(int)Math.ceil(newOpts.outWidth/(float)ww);
			} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
				be =(int)Math.ceil(newOpts.outHeight/(float)hh);
			}
			if (be <= 0)
				be = 1;
			newOpts.inSampleSize = be;//设置缩放比例
			//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
			isBm = new ByteArrayInputStream(baos.toByteArray());
			bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
			return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
		}
	/**
	 * 保存图像到指定路径
	 * @param bitName
	 * @param mBitmap
	 */
	public static void saveBitmap(String savePath,Bitmap mBitmap){
		  File f = new File(savePath);
		  try {
		   f.createNewFile();
		   FileOutputStream fOut = new FileOutputStream(f);
		   mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		   fOut.flush();
		   fOut.close();
		  } catch (Exception e) {
			   // TODO Auto-generated catch block
				  e.getStackTrace();
			  }
		 }
	/**
	 * 缩放 Drawable
	 * @param drawable
	 * @param w
	 * @param h
	 * @return
	 */
	public  static Drawable zoomDrawable(Drawable drawable, int w, int h)
    {
              int width = drawable.getIntrinsicWidth();
              int height= drawable.getIntrinsicHeight();
              Bitmap oldbmp = drawableToBitmap(drawable); // drawable转换成bitmap
              Matrix matrix = new Matrix();   // 创建操作图片用的Matrix对象
              float scaleWidth = ((float)w / width);   // 计算缩放比例
              float scaleHeight = ((float)h / height);
              matrix.postScale(scaleWidth, scaleHeight);         // 设置缩放比例
              Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);       // 建立新的bitmap，其内容是对原bitmap的缩放后的图
              return new BitmapDrawable(newbmp);       // 把bitmap转换成drawable并返回
    }
	
	/**
	 * 读取资源图片
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId){
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		//获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is,null,opt);
		}
	
	/****
	 * 根据resid 生成指定规格的butmap
	 * @param res
	 * @param resId
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	  public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,int reqWidth, int reqHeight) {

	        final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        options.inPurgeable = true;
	        BitmapFactory.decodeResource(res, resId, options);
	        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	        options.inJustDecodeBounds = false;
	        try {
	        	  return BitmapFactory.decodeResource(res, resId, options);
			} catch (OutOfMemoryError e) {
				 e.printStackTrace();
				 return null;
			}
	    }

	  /***
	   * 根据fileDescriptor 生成图片
	   * @param fileDescriptor
	   * @param reqWidth
	   * @param reqHeight
	   * @return
	   */
	    public static Bitmap decodeSampledBitmapFromDescriptor(FileDescriptor fileDescriptor, int reqWidth, int reqHeight) {

	        final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        options.inPurgeable = true;
	        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
	        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	        options.inJustDecodeBounds = false;
	        try {
	        	 return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
			} catch (OutOfMemoryError e) {
				 e.printStackTrace();
				 return null;
			}
	    }
	    
	    /***
	     * 根据byte数据生成图片
	     * @param data
	     * @param offset
	     * @param length
	     * @param reqWidth
	     * @param reqHeight
	     * @return
	     */
	    public static Bitmap decodeSampledBitmapFromByteArray(byte[] data,  int offset, int length, int reqWidth, int reqHeight) {
	        final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        options.inPurgeable = true;
	        BitmapFactory.decodeByteArray(data, offset, length, options);
	        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	        options.inJustDecodeBounds = false;
	        return BitmapFactory.decodeByteArray(data, offset, length, options);
	    }
}
