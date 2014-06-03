package com.nf.framework;
//package com.Apricotforest;
//
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.graphics.drawable.StateListDrawable;
//import android.view.View;
//
//import com.larvalabs.svgandroid.SVG;
//import com.larvalabs.svgandroid.SVGParser;
///**
// * 设置svg图片的样式切换
// * @author win7
// *
// */
//public class StyleChangeView extends View {
// 
//        public StyleChangeView(Context context) {
//            super(context);
//        }
// 
//        // 以下这个方法也可以把你的图片数组传过来，以StateListDrawable来设置图片状态，来表现button的各中状态。未选
//    // 中，按下，选中效果。
//    public StateListDrawable setbg(Integer[] mImageIds) {
//        StateListDrawable bg = new StateListDrawable();
//        Drawable normal = this.getResources().getDrawable(mImageIds[0]);
//        Drawable selected = this.getResources().getDrawable(mImageIds[1]);
//        Drawable pressed = this.getResources().getDrawable(mImageIds[2]);
//        bg.addState(View.PRESSED_ENABLED_STATE_SET, pressed);
//        bg.addState(View.ENABLED_FOCUSED_STATE_SET, selected);
//        bg.addState(View.ENABLED_STATE_SET, normal);
//        bg.addState(View.FOCUSED_STATE_SET, selected);
//        bg.addState(View.EMPTY_STATE_SET, normal); 
//        return bg;
//    }
//  
//    // 以下这个方法也可以把你的图片数组传过来，以StateListDrawable来设置图片状态，来表现button的各中状态。未选
//    // 中，按下，选中效果。
//    public StateListDrawable setbg(Drawable[] mDrawableIds) {
//        StateListDrawable bg = new StateListDrawable();
//        Drawable normal =mDrawableIds[0];
//        Drawable selected =mDrawableIds[1];
//        Drawable pressed =mDrawableIds[2];
//        bg.addState(View.PRESSED_ENABLED_STATE_SET, pressed);
//        bg.addState(View.ENABLED_FOCUSED_STATE_SET, selected);
//        bg.addState(View.ENABLED_STATE_SET, normal);
//        bg.addState(View.FOCUSED_STATE_SET, selected);
//        bg.addState(View.EMPTY_STATE_SET, normal);
//        return bg;
//    }
//   /**
//    *  以下这个方法也可以把你的图片数组传过来，以StateListDrawable来设置图片状态，来表现button的各中状态。未选
//    * @param fileName
//    * @param mColorIds
//    * @return
//    */
//    // 中，按下，选中效果。
//    public StateListDrawable setbg(String fileName,String[] mColorIds) {
//        StateListDrawable bg = new StateListDrawable();
//        Drawable normal =FileName2Drawable(fileName,mColorIds[0]);
//        Drawable selected =FileName2Drawable(fileName,mColorIds[1]);
//        Drawable pressed =FileName2Drawable(fileName,mColorIds[2]);
//        bg.addState(View.PRESSED_ENABLED_STATE_SET, pressed);
//        bg.addState(View.ENABLED_FOCUSED_STATE_SET, selected);
//        bg.addState(View.ENABLED_STATE_SET, normal);
//        bg.addState(View.FOCUSED_STATE_SET, selected);
//        bg.addState(View.EMPTY_STATE_SET, normal);
//        return bg;
//    }
//    /**
//	 * svg图片转换成drawable
//	 * @param FileName assets文件夹中的文件名称
//	 * @param color 需要变换的颜色
//	 * @return
//	 */
//	public  Drawable FileName2Drawable(String fileName,String color){
//		 SVG svg = null;
//		 Drawable drawable=null;
//	     String str = FileHelper.getInstance().getAssetFileContent(getContext(), fileName); 
//	     String newStr=str.replaceAll("fill=\"([\\s\\S]*?)\"", "fill=\""+color+"\"");
//	     svg=SVGParser.getSVGFromString(newStr);
//	     drawable=svg.createPictureDrawable();
//	    return drawable;
//	}
//	  /**
//	 * svg图片转换成drawable
//	 * @param FileName assets文件夹中的文件名称
//	 * @param colorId 需要变换的颜色
//	 * @return
//	 */
//	public  Drawable FileName2Drawable(String fileName,int colorId){
//		 Drawable drawable=null;
//		 String color=getContext().getString(colorId);
//		 drawable=	FileName2Drawable(fileName,color);
//	
//		 return drawable;
//	}
//    /**
//	 * svg图片转换成drawable
//	 * @param FileName assets文件夹中的文件名称
//	 * @param color 需要变换的颜色
//	 * @return
//	 */
////	public  Bitmap FileName2Bitmap(String fileName,String color){
////		 SVG svg = null;
////		 Bitmap bm=null;
////		 Drawable drawable=null;
////		InputStream inputStream = null; 
////	    try { 
////		  inputStream =  this.getResources().getAssets().open(fileName); 
////		  String str = InputStream2String(inputStream); 
////		  String newStr=str.replaceAll("fill=\"([\\s\\S]*?)\"", "fill=\""+color+"\"");
////		  svg=SVGParser.getSVGFromString(newStr);
////		  drawable=svg.createPictureDrawable();
////		  bm=ImageUtil.drawableToBitmap(drawable);
////	    } catch (IOException e) { 
////			   Log.e("SVGPicUtil", e.getMessage()); 
////			} 
////	return bm;
////	}
//	  /**
//	 * svg图片转换成drawable
//	 * @param FileName assets文件夹中的文件名称
//	 * @return
//	 */
//	public  Drawable FileName2Drawable(String fileName){
//		 SVG svg = null;
//		 Drawable drawable=null;
//	     String str = FileHelper.getInstance().getAssetFileContent(getContext(), fileName); 
//		 svg=SVGParser.getSVGFromString(str);
//		 drawable=svg.createPictureDrawable();
//		 return drawable;
//	}
//
//}