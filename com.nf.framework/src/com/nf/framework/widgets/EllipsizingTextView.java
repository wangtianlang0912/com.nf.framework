/**
 * 能够解决ellipsize only 2 lines are displayed.
 * I need to ellipsize a multi-line textview. My component is large enough to display at least 4 lines with
 * the ellipse, but only 2 lines are displayed. I tried to change the minimum and maximum number of rows of
 * the component but it changes nothing.
 * com.Apricotforest.widgets
 * 工程：medicalJournals_for_android
 * 功能： TODO 
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-10-11       下午03:33:15
 * Copyright (c) 2012, TNT All Rights Reserved.
 * 
 * http://stackoverflow.com/questions/2160619/android-ellipsize-multiline-textview
*/

package com.nf.framework.widgets;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextUtils.TruncateAt;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

public class EllipsizingTextView extends TextView {
   private  String ELLIPSIS = "...更多";

    public interface EllipsizeListener {
        void ellipsizeStateChanged(boolean ellipsized);
    }

    private final List<EllipsizeListener> ellipsizeListeners = new ArrayList<EllipsizeListener>();
    private boolean isEllipsized;
    private boolean isStale;
    private boolean programmaticChange;
    private String fullText;
    private int maxLines = -1;
    private float lineSpacingMultiplier = 1.0f;
    private float lineAdditionalVerticalPadding = 0.0f;
    public EllipsizingTextView(Context context) {
        super(context);
    }

    public EllipsizingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EllipsizingTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addEllipsizeListener(EllipsizeListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        ellipsizeListeners.add(listener);
    }

    public void removeEllipsizeListener(EllipsizeListener listener) {
        ellipsizeListeners.remove(listener);
    }
    public void removeAllEllipsizeListeners() {
    	if(ellipsizeListeners!=null)
        ellipsizeListeners.clear();
    }
    public boolean isEllipsized() {
        return isEllipsized;
    }

    public  String getELLIPSIS() {
		return ELLIPSIS;
	}
/**
 * 设置ellipsis显示文字
 * @param eLLIPSIS
 */
	public void setELLIPSIS(String eLLIPSIS) {
		ELLIPSIS = eLLIPSIS;
	}

	@Override
    public void setMaxLines(int maxLines) {
        super.setMaxLines(maxLines);
        this.maxLines = maxLines;
        isStale = true;
    }

    public int getMaxLines() {
        return maxLines;
    }

    @Override
    public void setLineSpacing(float add, float mult) {
        this.lineAdditionalVerticalPadding = add;
        this.lineSpacingMultiplier = mult;
        super.setLineSpacing(add, mult);
    }
   
    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        super.onTextChanged(text, start, before, after);
        if (!programmaticChange) {
            fullText = text.toString();
            isStale = true;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isStale) {
            super.setEllipsize(null);
            resetText();
        }
        super.onDraw(canvas);
    }

    private void resetText() {
        int maxLines = getMaxLines();
        String workingText = fullText;
        boolean ellipsized = false;
        if (maxLines != -1) {
            Layout layout = createWorkingLayout(workingText);
            if (layout.getLineCount() > maxLines) {
                workingText = fullText.substring(0, layout.getLineEnd(maxLines - 1)).trim();
                while (createWorkingLayout(workingText + getELLIPSIS()).getLineCount() > maxLines) {
                    int lastSpace = workingText.lastIndexOf(' ');
                    if (lastSpace == -1) {
                        break;
                    }
                    workingText = workingText.substring(0, lastSpace);
                }
                
                if(workingText.length()>getELLIPSIS().length()){
                	StringBuffer sb=new StringBuffer();
            		for(int i=workingText.length()-(getELLIPSIS().length()); i<workingText.length(); i++){
            			sb.append(workingText.charAt(i));
            		}
            		workingText=workingText.substring(0,workingText.lastIndexOf(sb.toString()));
                }
                workingText = workingText + getELLIPSIS();
                ellipsized = true;
            }
        }
        if (!workingText.equals(getText())) {
            programmaticChange = true;
            try {
            	if(workingText.endsWith(getELLIPSIS())){
            		SpannableString morecontent = new SpannableString(workingText);
            		morecontent.setSpan(new ForegroundColorSpan(Color.rgb(0,153,204)),workingText.length()-(getELLIPSIS().length()-3),workingText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//颜色
            		 setText(morecontent);
            	}else{
            		setText(workingText);
            	}
            } finally {
                programmaticChange = false;
            }
        }
        isStale = false;
        if (ellipsized != isEllipsized) {
            isEllipsized = ellipsized;
            for (EllipsizeListener listener : ellipsizeListeners) {
                listener.ellipsizeStateChanged(ellipsized);
            }
        }
    }

    private Layout createWorkingLayout(String workingText) {
        return new StaticLayout(workingText, getPaint(), getWidth() - getPaddingLeft() - getPaddingRight(),
                Alignment.ALIGN_NORMAL, lineSpacingMultiplier, lineAdditionalVerticalPadding, false);
    }

    @Override
    public void setEllipsize(TruncateAt where) {
        // Ellipsize settings are not respected
    }
}
