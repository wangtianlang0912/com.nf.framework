package com.nf.framework.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.ProgressBar;

public class TextProgressBar extends ProgressBar {

	private String text;
	private Paint mPaint;
	private Object buttonStateTag;
	private static int TEXT_SIZE=16;
	private static int TEXT_COLOR=Color.BLACK;
	public TextProgressBar(Context context) {
		super(context);
		initText();
	}

	public TextProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initText();
	}

	public TextProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initText();
	}

	@Override
	public synchronized void setProgress(int progress) {
		setText(progress);
		super.setProgress(progress);
		if(progress==100){
			 postInvalidate();
		}
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
			if(this.text.contains("-")){
				this.text="读取中";
			}
			Rect rect = new Rect();
			this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
			setPaintTextStyle();
			int x = (getWidth() / 2) - rect.centerX();
			int y = (getHeight() / 2) - rect.centerY();
			canvas.drawText(this.text, x, y, this.mPaint);
	}

	// 初始化，画笔

	private void initText() {
		this.mPaint = new Paint();
		this.mPaint.setAntiAlias(true);
		setPaintTextStyle();
	}

	private void setPaintTextStyle(){
		this.mPaint.setColor(TEXT_COLOR);
	    Display display =((Activity)getContext()).getWindowManager().getDefaultDisplay();
		DisplayMetrics metric = new DisplayMetrics();
		display.getMetrics(metric);
		this.mPaint.setTextSize(TEXT_SIZE*metric.density);
	}
	@SuppressWarnings("unused")
	private void setText() {
		setText(this.getProgress());
	}

	// 设置文字内容 个位起
	private void setText(int progress) {
//		int i = (progress * 100) / this.getMax();
		this.text = String.valueOf(progress) + "%";
	}
	/**
	 * 设置显示状态
	 * @param text
	 */
	public void setTextState(String text){
		this.text =text;
		 postInvalidate();
	}
	/**
	 * 设置初始进度状态
	 */
	public void setInitViewState(){
		setProgress(0);
	}
	/**
	 * 设置最大值进度状态
	 */
	public void setMaxViewState(){
		setProgress(getMax());
	}
	/**
	 * 获取当前显示状态
	 * @return
	 */
	public String getText() {
		return text;
	}

	public Object getButtonStateTag() {
		return buttonStateTag;
	}

	public void setButtonStateTag(Object buttonStateTag) {
		this.buttonStateTag = buttonStateTag;
	}

	/**
	 * 设置文字颜色
	 */
	public void setTextColor(int textColor){
		this.TEXT_COLOR=textColor;
	}
	/**
	 * 设置显示文字大小 
	 * @param textSize
	 */
	public void setTextSize(int textSize){
		this.TEXT_SIZE=textSize;
	}
	/**
	 * 设置初始显示状态
	 * @param text
	 */
	public void setInitTextState(String text){
		setTextState(text);
	}
	public void setProgressDrawable(int resId){
		
		setProgressDrawable(getResources().getDrawable(resId));
	}
}

