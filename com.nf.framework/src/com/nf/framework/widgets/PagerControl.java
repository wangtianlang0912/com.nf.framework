package com.nf.framework.widgets;

/*
 * Copyright (C) 2010 Deez Apps!
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.nf.framework.R;

/**
 * User: jeanguy@gmail.com
 * Date: Aug 11, 2010
 */
public class PagerControl extends View{

    private static final int DEFAULT_BAR_COLOR = 0xaa111111;
    private static final int DEFAULT_HIGHLIGHT_COLOR = 0xcc999999;
    private static final int DEFAULT_FADE_DELAY = 2000;
    private static final int DEFAULT_FADE_DURATION = 500;
    private int numPages,currentPage, position;
    private Paint barPaint, highlightPaint;
//    private  BitmapDrawable     highlightBitPaint;
//    private  Bitmap bmp;
    private int fadeDelay, fadeDuration;
    private float ovalRadius;
    private int pageSize=10;
    private Animation fadeOutAnimation;
    private float density=1.0f;
    private int circleRadius=3;//圆的半径 默认为3
    public PagerControl(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerControl(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PagerControl);
        int barColor = a.getColor(R.styleable.PagerControl_barColor, DEFAULT_BAR_COLOR);
        int highlightColor = a.getColor(R.styleable.PagerControl_highlightColor, DEFAULT_HIGHLIGHT_COLOR);
        fadeDelay = a.getInteger(R.styleable.PagerControl_fadeDelay, DEFAULT_FADE_DELAY);
        fadeDuration = a.getInteger(R.styleable.PagerControl_fadeDuration, DEFAULT_FADE_DURATION);
        ovalRadius = a.getDimension(R.styleable.PagerControl_roundRectRadius, 0f);
        a.recycle();

        barPaint = new Paint();
        barPaint.setColor(barColor);
        barPaint.setAntiAlias(true);   //主要用来消除边缘吧从效果上看 false的话有毛边   
        highlightPaint = new Paint();
        highlightPaint.setColor(highlightColor);
        highlightPaint.setAntiAlias(true);    //主要用来消除边缘吧从效果上看 false的话有毛边   
//        Resources r=getResources();
//        bmp=BitmapFactory.decodeResource(r, R.drawable.dot);
//
//        highlightBitPaint = new BitmapDrawable(bmp);
       
        fadeOutAnimation = new AlphaAnimation(1f, 0f);
   //     fadeOutAnimation.setDuration(fadeDuration);
//        fadeOutAnimation.setRepeatCount(0);
//        fadeOutAnimation.setInterpolator(new LinearInterpolator());
//        fadeOutAnimation.setFillEnabled(true);
//        fadeOutAnimation.setFillAfter(true);
        
        Display display =((Activity)context).getWindowManager().getDefaultDisplay();
		DisplayMetrics metric = new DisplayMetrics();
		display.getMetrics(metric);
		density=metric.density;
    }

    /**
     *
     * @return current number of pages
     */
    public int getNumPages() {
        return numPages;
    }

    /**
     *自定义页面的个数
     * @param numPages must be positive number
     */
    public void setNumPages(int numPages) {
        if (numPages <0) {
            throw new IllegalArgumentException("numPages must be positive");
        }
        this.numPages = numPages;
        invalidate();
        fadeOut();
    }

    private void fadeOut() {
//        if (fadeDuration > 0) {
//            clearAnimation();
//            fadeOutAnimation.setStartTime(AnimationUtils.currentAnimationTimeMillis() + fadeDelay);
//            setAnimation(fadeOutAnimation);
//        }
    }

    /**
     * 0 to numPages-1
     * @return
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     *
     * @param currentPage 0 to numPages-1
     */
    public void setCurrentPage(int currentPage) {
        if (currentPage < 0 || currentPage >= getNumPages()) {
            throw new IllegalArgumentException("currentPage parameter out of bounds");
            
        }
        if (this.currentPage != currentPage) {
            this.currentPage = currentPage;
            this.position = currentPage * getPageWidth();
            invalidate();
            fadeOut();
        }
    }

    /**
     * Equivalent to the width of the view divided by the current number of pages.
     * @return page width, in pixels
     */
    public int getPageWidth() {
    	
        return pageSize;
    }
    
    /**
     * 自定义两个圆心之间的距离
     * @param pageSize
     */
    public void setPageWidth(int pageSize) {
        this.pageSize= (int)(density*pageSize);
    }

    /**
     *
     * @param position can be -pageWidth to pageWidth*(numPages+1)
     */
    public void setPosition(int position) {
        if (this.position != position) {
            this.position = position;
            invalidate();
            fadeOut();
        }
    }
/**
 * 获取水平方向上两边需要空出的位置
 */
    private int getMarginHorizontal(){
    	if(getNumPages()>1)
    	return (getWidth()-(getPageWidth()*(getNumPages()-1)))/2;
    	else{
    		return 0;
    	}
    }
    /**
     * 获取竖直方向上的中心位置坐标
     * 
     */
    private int getMarginVertral(){
    		
    		return getHeight()/2;
    	
     }
    
    /**
     * 设置小圆的半径
     * @return
     */
    public void  setCircleRadius(int circleRadius){
    	this.circleRadius=circleRadius;
    }
    
    private int getCircleRadius() {
    	return (int)(density*circleRadius);
	}

	/**
     * 获取图片的半径大小
     * @return
     */
//    private int getBitMapRadius(){
//    	if(bmp.getWidth()%2==0){
//    		
//    		return bmp.getWidth()/2;
//    	}else{
//    		
//    		return (bmp.getWidth()+1)/2;
//    	}
//    	
//    }
    /**
     * 设置图片左偏移量
     * @return
     */
//    private int setMatrixHorizontalTranslate(){
//    	return (getMarginHorizontal()-getBitMapRadius()+position);
//    }
    /**
     * 设置圆左偏移量
     * @return
     */
    private int setCircleHorizontalTranslate(int i){
    	
    	return getMarginHorizontal()+i*getPageWidth();	
    }
    /**
     * 
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
    	if(getNumPages()>1){
	    	do{
	    		for(int i=0;i<getNumPages();i++){
	    	    	  canvas.drawCircle(setCircleHorizontalTranslate(i), getMarginVertral(),getCircleRadius(), barPaint);
	    	    }	
	    	}while(false);
	//      mMatrix.setTranslate(setMatrixHorizontalTranslate(), getMarginVertral()-getBitMapRadius());
	//      canvas.drawBitmap(bmp, mMatrix, highlightPaint);
           canvas.drawCircle(getMarginHorizontal()+position, getMarginVertral(),getCircleRadius(), highlightPaint);
    	}
    }
}
