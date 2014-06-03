package com.nf.framework.widgets;

/**
 * 功能： TODO 
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-9-5       下午03:36:06
 * Copyright (c) 2012, TNT All Rights Reserved.
*/
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.FontMetrics;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.EditText;

public class FloatImageTextView extends EditText{
    private Bitmap mBitmap;
    private final Rect bitmapFrame = new Rect();
    private final Rect tmp = new Rect();
    private int mTargetDentity = DisplayMetrics.DENSITY_DEFAULT;
    
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private String mText;
    private ArrayList<TextLine> mTextLines;
    private final int[] textViewSize = new int[2];
    private int[] ImageMaxSize=new int[2];//图片最大的尺寸
    private int textColor =Color.BLACK;//设置字体颜色

    private final static int C_MENU_BEGIN_SELECTION = 0;
    boolean bIsBeginSelecting = false;
    int line = 0;	// 光标所在行
    int off = 0;	// 光标所在列
    
    public FloatImageTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    public FloatImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public FloatImageTextView(Context context) {
        super(context);
        init();
    }
    
    private void init() {
        mTargetDentity = getResources().getDisplayMetrics().densityDpi;
        mTextLines = new ArrayList<TextLine>();
        
        mPaint.setTextSize(getTextSize());
        mPaint.setColor(getTextColor());
        mPaint.setAntiAlias(true);
    }
	public int getTextColor() {
		return textColor;
	}
	/**
	 * 设置字体颜色
	 * @param textColor
	 */
	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = 0, h = 0;
        //图片大小
        w += bitmapFrame.width();
        h += bitmapFrame.height();
        
        //文本宽度
        if(null != mText && mText.length() > 0) {
            mTextLines.clear();
            int size = resolveSize(Integer.MAX_VALUE, widthMeasureSpec);
            measureAndSplitText(mPaint, mText, size);
            final int textWidth = textViewSize[0], textHeight = textViewSize[1];
            w += textWidth; //内容宽度
            if(h < textHeight) { //内容高度
                h = (int) textHeight;
            }
        }
        
        w = Math.max(w, getSuggestedMinimumWidth());
        h = Math.max(h, getSuggestedMinimumHeight());
        
        setMeasuredDimension(
                resolveSize(w, widthMeasureSpec), 
                resolveSize(h, heightMeasureSpec));
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        //绘制图片
        if(null != mBitmap) {
            canvas.drawBitmap(mBitmap, null, bitmapFrame, null);
        }
        
        //绘制文本
        TextLine line;
        final int size = mTextLines.size();
        for(int i = 0; i < size; i++) {
            line = mTextLines.get(i);
            canvas.drawText(line.text, line.x, line.y, mPaint);
        }
    }
    
    
    public void setImageBitmap(Bitmap bm) {
        setImageBitmap(bm, null);
    }
    
    public void setImageBitmap(Bitmap bm, int left, int top) {
    	setImageBitmap(bm, new Rect(left, top, 0, 0));
    }
       
    public int[] getImageMaxSize() {
		return ImageMaxSize;
	}


	public void setImageMaxSize(int maxWidth,int maxHight) {
		
		
		ImageMaxSize = new int[]{maxWidth,maxHight};
	}


	public void setImageBitmap(Bitmap bm, Rect bitmapFrame) {
        mBitmap = bm;
        computeBitmapSize(bitmapFrame);
        requestLayout();
        invalidate();
    }
    
    public void setText(String text) {
        mText = text;
        requestLayout();
        invalidate();
//        super.setText(text);
    }
    
    private void computeBitmapSize(Rect rect) {
        if(null != rect) {
            bitmapFrame.set(rect);
        }
        if(null != mBitmap) {
            if(rect.right == 0 && rect.bottom == 0) {
                final Rect r = bitmapFrame;
            
                int bitHeight=mBitmap.getScaledHeight(mTargetDentity);
                int bitWidth=mBitmap.getScaledWidth(mTargetDentity);
                if(ImageMaxSize[0]!=0&&ImageMaxSize[1]!=0){
                	ImageMaxSize= getImageMaxSize();
                	if(ImageMaxSize[0]>ImageMaxSize[1]){//如果宽度大于高度
                		ImageMaxSize[0]=(ImageMaxSize[1])*bitWidth/bitHeight;
                	}else if(ImageMaxSize[0]<ImageMaxSize[1]){
	                	ImageMaxSize[1]=ImageMaxSize[0]*bitHeight/bitWidth;
                	}else if(ImageMaxSize[0]==ImageMaxSize[1]){
                		if(bitHeight>=bitWidth){
                			ImageMaxSize[0]=(ImageMaxSize[1])*bitWidth/bitHeight;
                		}else{
                			ImageMaxSize[1]=ImageMaxSize[0]*bitHeight/bitWidth;
                		}
                	}
                	bitWidth=ImageMaxSize[0]>bitWidth?bitWidth:ImageMaxSize[0];
                	bitHeight=ImageMaxSize[1]>bitHeight?bitHeight:ImageMaxSize[1];
                }
                r.set(r.left, r.top,r.left +bitWidth ,r.top +bitHeight);
            }
        } else {
            bitmapFrame.setEmpty();
        }
    }
    
    private void measureAndSplitText(Paint p, String content, int maxWidth) {
        FontMetrics fm = mPaint.getFontMetrics();
        final int lineHeight = (int) (fm.bottom - fm.top);
        
        final Rect r = new Rect(bitmapFrame);
//        r.inset(-5, -5);
        
        final int length = content.length();
        int start = 0, end = 0, offsetX = 0, offsetY = 0;
        int availWidth = maxWidth;
        TextLine line;
        boolean onFirst = true;
        boolean newLine = true;
        while(start < length) {
            end++;
            if(end == length) { //剩余的不足一行的文本
                if(start <= length - 1) {
                    if(newLine) offsetY += lineHeight;
                    line = new TextLine();
                    line.text = content.substring(start, end - 1);
                    line.x = offsetX;
                    line.y = offsetY;
                    mTextLines.add(line);
                }
                break;
            }
            Log.d("gc", "offsetY--------- = " + r.bottom);
            p.getTextBounds(content, start, end, tmp);
            if(onFirst) { //确定每个字符串的坐标
                onFirst = false;
                final int height = lineHeight + offsetY;
                if(r.top >= height) { //顶部可以放下一行文字
                    offsetX = 0;
                    availWidth = maxWidth;
                    newLine = true;
                } else if(newLine && (r.bottom >= height && r.left >= tmp.width())) { //中部左边可以放文字
                    offsetX = 0;
                    availWidth = r.left;
                    newLine = false;
                } else if(r.bottom >= height && maxWidth - r.right >= tmp.width()) { //中部右边
                    offsetX = r.right;
                    availWidth = maxWidth - r.right;
                    newLine = true;
                }else if(r.bottom >= height && maxWidth - r.right < tmp.width()) { //右边写不下
                    offsetX = 0;
                    availWidth = r.left;
                    offsetY += lineHeight;
                    newLine = true;
                }else { //底部
                    offsetX = 0;
                    availWidth = maxWidth;
                    if(offsetY < r.bottom) offsetY = r.bottom;
                    newLine = true;
                }
            }
            Log.d("gc", "offsetY1 = " + offsetY);
            if(tmp.width() > availWidth) { //保存一行能放置的最大字符串
                onFirst = true;
                Spannable.Factory mSpannableFactory = Spannable.Factory.getInstance(); 
                Spannable spa=    mSpannableFactory.newSpannable(content);
                line = new TextLine();
                line.text = content.substring(start, end - 1);
                line.x = offsetX;
                mTextLines.add(line);
                if(newLine) {
                    offsetY += lineHeight;
                    line.y = offsetY;
                    Log.d("gc", "offsetY1 = " + offsetY + " ^^^^^^^^^^^^^lineHeight = " + lineHeight);
                } else {
                    line.y = offsetY + lineHeight;
                    Log.d("gc", "offsetY2 = " + offsetY + " ^^^^^^^^^^^^^lineHeight = " + lineHeight);
                }
                start = end - 1;
            }
        }
        textViewSize[1] = offsetY;
    }
    
    class TextLine {
        String text;
        int x;
        int y;
        
        @Override
        public String toString() {
            return "TextLine [text=" + text + ", x=" + x + ", y=" + y + "]";
        }
    }
    
    private class MenuHandler implements MenuItem.OnMenuItemClickListener {
        public boolean onMenuItemClick(MenuItem item) {
            return onContextMenuItem(item.getItemId());
        }
    }
    
    public boolean onContextMenuItem(int id) {
    	switch (id) {
    	case C_MENU_BEGIN_SELECTION:
    		bIsBeginSelecting = true;
    		setCursorVisible(true);
    		return true;
    	}
    	
		return false;
    }
    // 长按屏幕弹出的上下文菜单
    @Override  
    protected void onCreateContextMenu(ContextMenu menu) {   
    		MenuHandler handler = new MenuHandler();
    		menu.add(0, C_MENU_BEGIN_SELECTION, 0, "文本选择模式").
            setOnMenuItemClickListener(handler);
    }   
       
    @Override  
    public boolean getDefaultEditable() {   
        return false;   
    }   
       
    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
    	if (bIsBeginSelecting) {
    		// 文本选择模式下特殊处理
            int action = event.getAction();   
            Layout layout = getLayout();   

            switch(action) {   
            case MotionEvent.ACTION_DOWN:   
                line = layout.getLineForVertical(getScrollY()+ (int)event.getY());           
                off = layout.getOffsetForHorizontal(line, (int)event.getX());   
                Selection.setSelection(getEditableText(), off);   
                break;   
            case MotionEvent.ACTION_MOVE:   
            case MotionEvent.ACTION_UP:   
                line = layout.getLineForVertical(getScrollY()+(int)event.getY());    
                int curOff = layout.getOffsetForHorizontal(line, (int)event.getX());
                if (curOff > off)
                	Selection.setSelection(getEditableText(), off, curOff);
                else
                	Selection.setSelection(getEditableText(), curOff, off);
            }   
            return true;  
    	} else {
    		super.onTouchEvent(event);
    		return true;
    	}
    }   
    
    // 清除选择内容
    public void clearSelection() {
    	Selection.removeSelection(getEditableText());
		bIsBeginSelecting = false;
		setCursorVisible(false);
    }
    
    public boolean isInSelectMode() {
    	return bIsBeginSelecting;
    }
}
