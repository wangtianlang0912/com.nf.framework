//package com.ApricotforestCommon.widgets;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.drawable.Drawable;
//import android.graphics.drawable.StateListDrawable;
//import android.util.AttributeSet;
//import android.view.View;
//import android.widget.ImageView;
//
//import com.ApricotforestCommon.R;
//import com.libsvg.BaseSvgDrawable;
//
//
///**
// * 
// * @author Pavel.B.Chernov - some improvements
// * @author kushnarev - original idea
// *
// */
//
///**
// * Image View class with SVG format support
// */
//public class SvgImageView extends ImageView {
//    // Load native libraries
//	static {
//		//该包名不可修改 绝对路径
//		System.loadLibrary("svgandroid");
////		System.load("com/libsvg/lib/armeabi/libsvgandroid.so");
//	}    
//	/**
//	 * 当前显示的图片资源id
//	 */
//	private int CurrentImageResourceId=0;
//	private boolean mIsSvg = false;
//	
//    public SvgImageView(Context context) {
//        super(context);
//    }
//    
//    public SvgImageView(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }	
// 
//	public SvgImageView(Context context, AttributeSet attrs, int defStyle) {
//		// Let's try load supported by ImageView formats
//		super(context, attrs, defStyle);
//        
//        if (getDrawable() != null) {
//        	return;
//        }
//        //  Get defined attributes
//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SvgImageView, defStyle, 0);
//
//        // Getting a file name
//        CharSequence cs = a.getText(R.styleable.SvgImageView_android_src);
//        if(cs!=null){
//	        String file = cs.toString();
//	
//	        // Is it SVG file?
//	        if (! file.endsWith(".svg")) {
//	        	return;
//	        }
//        }
//        // Retrieve ID of the resource
//        int id = a.getResourceId(R.styleable.SvgImageView_android_src, -1);
//        if (id == -1 || id == 0) {
//        	return;
//        }
//
//        // Get the input stream for the raw resource
//        InputStream inStream = getResources().openRawResource(id);
//        if (inStream == null) {
//        	return;
//        }
//
//        BaseSvgDrawable svg = new BaseSvgDrawable(getResources(), inStream);
//        mIsSvg = true;
//		svg.setScaleType(this.getScaleType());
//        int vWidth = getWidth() - getPaddingLeft() - getPaddingRight();
//        int vHeight = getHeight() - getPaddingTop() - getPaddingBottom();
//		svg.adjustToParentSize(vWidth, vHeight);
//        
//        this.setImageDrawable(svg);
//	}   
//    // 以下这个方法也可以把你的图片数组传过来，以StateListDrawable来设置图片状态，来表现button的各中状态。未选
//    // 中，按下，选中效果。
//    public void setStateListDrawable(int imageId) {
//    	setCurrentImageResourceId(imageId);
//    	Integer[] mColorIds =new Integer[]{R.color._ffffff,R.color._999999,R.color._999999};
//    	setStateListDrawable(imageId,mColorIds);
//    }
//	 // 以下这个方法也可以把你的图片数组传过来，以StateListDrawable来设置图片状态，来表现button的各中状态。未选
//    // 中，按下，选中效果。
//    public void setStateListDrawable(int imageId,Integer[] mColorIds) {
//    	setCurrentImageResourceId(imageId);
//    	StateListDrawable bg=getStateListDrawable(imageId,mColorIds);
//        setImageDrawable(bg);
//    }
//    public StateListDrawable getStateListDrawable(int imageId){
//    	setCurrentImageResourceId(imageId);
//    	Integer[] mColorIds =new Integer[]{R.color._ffffff,R.color._999999,R.color._999999};
//    	StateListDrawable bg=	getStateListDrawable(imageId,mColorIds);
//		return bg; 
//   }
//    /**
//     * 返回当前显示的资源图片id
//     */
//    public int getCurrentImageResourceId() {
//		return CurrentImageResourceId;
//	}
//
//	private void setCurrentImageResourceId(int currentImageResourceId) {
//		CurrentImageResourceId = currentImageResourceId;
//	}
//
//	public void setStateListDrawable(int[] imageId){
//   	 StateListDrawable bg = new StateListDrawable();
//        Drawable normal =getDrawable(imageId[0]);
//        Drawable selected =getDrawable(imageId[1]);
//        Drawable pressed =getDrawable(imageId[2]);
//        bg.addState(View.PRESSED_ENABLED_STATE_SET, pressed);
//        bg.addState(View.ENABLED_FOCUSED_STATE_SET, selected);
//        bg.addState(View.ENABLED_STATE_SET, normal);
//        bg.addState(View.FOCUSED_STATE_SET, selected);
//        bg.addState(View.EMPTY_STATE_SET, normal);
//        setImageDrawable(bg);
//   }
//    public StateListDrawable getStateListDrawable(int imageId,Integer[] mColorIds){
//    	 StateListDrawable bg = new StateListDrawable();
//         Drawable normal =FileName2Drawable(imageId,mColorIds[0]);
//         Drawable selected =FileName2Drawable(imageId,mColorIds[1]);
//         Drawable pressed =FileName2Drawable(imageId,mColorIds[2]);
//         bg.addState(View.PRESSED_ENABLED_STATE_SET, pressed);
//         bg.addState(View.ENABLED_FOCUSED_STATE_SET, selected);
//         bg.addState(View.ENABLED_STATE_SET, normal);
//         bg.addState(View.FOCUSED_STATE_SET, selected);
//         bg.addState(View.EMPTY_STATE_SET, normal);
//		return bg; 
//    }
//	@Override
//	public void setScaleType (ImageView.ScaleType scaleType) {
//		super.setScaleType(scaleType);
//		if (! mIsSvg) {
//			return;
//		}
//		BaseSvgDrawable svg = (BaseSvgDrawable) getDrawable(); 
//		// FIXME
//		// We use dirty hack here, to avoid scalings of bitmaps
//		// Let SVG scale itself!
//		super.setImageDrawable(null);
//        svg.setScaleType(scaleType);
//        int vWidth = getWidth() - getPaddingLeft() - getPaddingRight();
//        int vHeight = getHeight() - getPaddingTop() - getPaddingBottom();
//        svg.adjustToParentSize(vWidth, vHeight);
//        super.setImageDrawable(svg);
//	}
//	public void setImageDrawable(int resourceId) {
//		setImageDrawable(getDrawable(resourceId));
//	}
//	/**
//	 * 显示指定颜色的svg图片
//	 * @param resourceId
//	 * @param colorId
//	 */
//	public void setImageDrawable(int resourceId,int colorId) {
//		BaseSvgDrawable svgDrawable= FileName2Drawable(resourceId,colorId);
//		if(svgDrawable!=null){
//			setImageDrawable(svgDrawable);
//		}
//	}
//	public BaseSvgDrawable getDrawable(int resourceId) {
//		InputStream inStream = getResources().openRawResource(resourceId);
//	       if (inStream == null) {
//	        	return null;
//	        }
//	    BaseSvgDrawable svg = new BaseSvgDrawable(getResources(), inStream);
//		return svg;
//	}
//	@Override
//	public void setImageDrawable(Drawable drawable) {
//		if (! (drawable instanceof BaseSvgDrawable)) {
//			super.setImageDrawable(drawable);
//			mIsSvg = false;
//			return;
//		}
//		if (getDrawable() == drawable) {
//			return;
//		}
//		BaseSvgDrawable svg = (BaseSvgDrawable) drawable;
//		svg.setScaleType(this.getScaleType());
//        int vWidth = getWidth() - getPaddingLeft() - getPaddingRight();
//        int vHeight = getHeight() - getPaddingTop() - getPaddingBottom();
//		svg.adjustToParentSize(vWidth, vHeight);
//		super.setImageDrawable(svg);
//		mIsSvg = true;
//	}
//
//	@Override
//	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//		if (! mIsSvg)
//			return;
//		BaseSvgDrawable svg = (BaseSvgDrawable) getDrawable(); 
//		// FIXME
//		// We use dirty hack here, to avoid scalings of bitmaps
//		// Let SVG scale itself!
//		super.setImageDrawable(null);
//        int vWidth = getWidth() - getPaddingLeft() - getPaddingRight();
//        int vHeight = getHeight() - getPaddingTop() - getPaddingBottom();
//        svg.adjustToParentSize(vWidth, vHeight);
//        super.setImageDrawable(svg);
//	}
//     /**
//  	 * svg图片转换成drawable
//  	 * @param FileName assets文件夹中的文件名称
//  	 * @param color 需要变换的颜色
//  	 * @return
//  	 */
//  	public  BaseSvgDrawable FileName2Drawable(int  resourceId,int colorId){
// 	 	 String color= getResources().getString(colorId);
//  	     return FileName2Drawable(resourceId,color);
//  	}
//     /**
// 	 * svg图片转换成drawable
// 	 * @param FileName assets文件夹中的文件名称
// 	 * @param color 需要变换的颜色
// 	 * @return
// 	 */
// 	public  BaseSvgDrawable FileName2Drawable(int  resourceId,String color){
// 		 BaseSvgDrawable drawable=null;
// 		  try {
//	 		 String str =getAssetFileContent(getContext(), resourceId); 
//	 	     String newStr=str.replaceAll("fill=\"([\\s\\S]*?)\"", "fill=\""+color+"\"");
//	 	     ByteArrayInputStream stream = new ByteArrayInputStream(newStr.getBytes());
//			 drawable= new BaseSvgDrawable(getResources(), stream);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
// 	     return drawable;
// 	}
// 	
// 	 /**
//     * 获取Asset文件夹下的文件内容
//     * @param mcontext
//     * @param fileName
//     * @return
//     */
//    private String getAssetFileContent(Context mcontext,int resourceId){
//    	  InputStream inputStream;
//		try {
//			inputStream= getResources().openRawResource(resourceId);
//		       if (inputStream == null) {
//		        	return null;
//		        }
//			return InputStream2String(inputStream);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		return null; 
//    	
//    }
//    
//    /**
//	 * InputStream2String
//	 * @param inputStream
//	 * @return
//	 */
//    private String InputStream2String(InputStream inputStream) { 
//		  ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); 
//		  byte buf[] = new byte[1024]; 
//		  int len; 
//		  try { 
//		   while ((len = inputStream.read(buf)) != -1) { 
//		    outputStream.write(buf, 0, len); 
//		   } 
//		   outputStream.close(); 
//		   inputStream.close(); 
//		  } catch (IOException e) { 
//
//		  } 
//		  return outputStream.toString(); 
//
//	}
//}
