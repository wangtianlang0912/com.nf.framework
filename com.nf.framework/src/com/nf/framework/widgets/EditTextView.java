/**
 * 功能： TODO 
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-9-3       下午04:13:29
 * Copyright (c) 2012, TNT All Rights Reserved.
 */

package com.nf.framework.widgets;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.RelativeLayout;

/**
 * 能够选择文字的View
 @author chroya
 */
public class EditTextView extends EditText{
	public final static int C_MENU_BEGIN_COPY= 0;//文本复制
	public final static int C_MENU_BEGIN_TRANSLATION =1;//文本翻译
    boolean bIsBeginSelecting = false;
//    private OnEditTextTouchListener onEditTouchListener=null;
    int line = 0;	// 光标所在行
    int off = 0;	// 光标所在列
    private Context mcontext;
    RelativeLayout attachLayout=null;
    private int selectItemId;
//    private class MenuHandler implements MenuItem.OnMenuItemClickListener {
//       
//    	public boolean onMenuItemClick(MenuItem item) {
//            return onContextMenuItem(item.getItemId());
//        }
//    }
//    
//    public boolean onContextMenuItem(int id) {
//    	setSelectItemId(id);
//    	bIsBeginSelecting = true;
//		setCursorVisible(true);
//		setKeyListener(null);
//    	return true;
//    }
    /**
     * 设置复制状态的方法
     * @param view
     * @param callBack
     */
//    public void setCopyState(View view){
//    	KeyEvent shiftPressEvent = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_SHIFT_RIGHT);
//		shiftPressEvent.dispatch(view);
//    }
    
    
    public EditTextView(Context context, AttributeSet attrs) {
    	super(context, attrs); //, 16842884
    	mcontext=context;
    	setUnEditable();
    	setInputHide();
    }
    
    public EditTextView(Context context) {   
        super(context); 
        mcontext=context;
        setUnEditable();
        setInputHide();
    }    
 
    public int getSelectItemId() {
		return selectItemId;
	}
	public void setSelectItemId(int selectItemId) {
		this.selectItemId = selectItemId;
	}
	/**
	 * 设置不可编辑状态
	 */
	public void setUnEditable(){
		setInputShow();
		setFilters(new InputFilter[] { new InputFilter() {
	      	 @Override
	      	 public CharSequence filter(CharSequence source, int start,
	      	  int end, Spanned dest, int dstart, int dend) {
	      	   return source.length() < 1 ? dest.subSequence(dstart, dend) : "";
	      	 }
	      	} });
	}
	/**
	 * 设置可编辑状态
	 */
	public void setEditable(){
		
		setFilters(new InputFilter[] { new InputFilter() {
	      	 @Override
	      	 public CharSequence filter(CharSequence source, int start,
	      	  int end, Spanned dest, int dstart, int dend) {
	      	   return source;
	      	 }
	      	} });
		setInputHide();
	}
	private void setInputHide(){
		setInputType(InputType.TYPE_NULL);
		setSingleLine(false);
	}	
	private void setInputShow(){
         setInputType(InputType.TYPE_CLASS_TEXT); 
         setSingleLine(false);
	}	
	// 长按屏幕弹出的上下文菜单
//    @Override  
//    protected void onCreateContextMenu(ContextMenu menu) {  
//    		MenuHandler handler = new MenuHandler();
//    		MenuItem copy=	menu.add(0, C_MENU_BEGIN_COPY, 0, "文本复制");
//    		copy.setOnMenuItemClickListener(handler);
//    		MenuItem translate=	menu.add(0, C_MENU_BEGIN_TRANSLATION, 0, "文本翻译");
//    		translate.setOnMenuItemClickListener(handler);
//    	
//    }   
	
//    @Override  
//    public boolean getDefaultEditable() {   
//        return false;   
//    }   
//    public void setOnTouchListener(EditTextView view,OnEditTextTouchListener onTouchListener){
//	   this.onEditTouchListener=onTouchListener;
//	   view.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// TODO Auto-generated method stub
//				if(onEditTouchListener!=null)
//					onEditTouchListener.onTouch((EditTextView)v, event, getSelectItemId());
//				return false;
//			}
//		});
//   	
//   }
//    	
    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
//    	if (bIsBeginSelecting) {
//    		// 文本选择模式下特殊处理
//            int action = event.getAction();   
//            Layout layout = getLayout();   
//            
//            switch(action) {   
//            case MotionEvent.ACTION_DOWN:   
//            	int y=getScrollY()+ (int)event.getY();
//            	int x=(int)event.getX();
//                line = layout.getLineForVertical(y);  
//                off = layout.getOffsetForHorizontal(line,x);   
//                Selection.setSelection(getEditableText(), off);  
//                break;   
//            case MotionEvent.ACTION_MOVE:   
//            case MotionEvent.ACTION_UP:   
//                line = layout.getLineForVertical(getScrollY()+(int)event.getY());    
//                int curOff = layout.getOffsetForHorizontal(line, (int)event.getX());
//                if (curOff > off)
//                	Selection.setSelection(getEditableText(), off, curOff);
//                else
//                	Selection.setSelection(getEditableText(), curOff, off);
//            }   
//            return true;  
//    	} else {
    		super.onTouchEvent(event);
    		return true;
//    	}
    }   
//   
//    // 清除选择内容
//    public void clearSelection() {
//    	Selection.removeSelection(getEditableText());
//		bIsBeginSelecting = false;
//		setCursorVisible(false);
//    }
//    
//    public boolean isInSelectMode() {
//    	return bIsBeginSelecting;
//    }
//    
//    public interface  OnEditTextTouchListener{
//    	
//    	public abstract  boolean onTouch(EditTextView v, MotionEvent event,int  MenuItemId);
//    }
}