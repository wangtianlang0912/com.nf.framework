package com.nf.framework.widgets;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 *
 * blog: http://blog.csdn.net/Yao_GUET
 * date: 2011-05-04
 */
public class ScrollLayout extends ViewGroup {

	private static final String TAG = "ScrollLayout";
	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;
	
	private int mCurScreen;
	private int mDefaultScreen = 0;

    private Set<OnScrollListener> mListeners = new HashSet<OnScrollListener>();
	
	private static final int TOUCH_STATE_REST = 0;
	private static final int TOUCH_STATE_SCROLLING = 1;
	
	private static final int SNAP_VELOCITY = 600;
	
	private int mTouchState = TOUCH_STATE_REST;
	private int mTouchSlop;
	private float mLastMotionX;
	private float mLastMotionY;

	public ScrollLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public ScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mScroller = new Scroller(context);
	
		mCurScreen = mDefaultScreen;
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onLayout");
		if (changed) {
			int childLeft = 0;
			final int childCount = getChildCount();
			
			for (int i=0; i<childCount; i++) {
				final View childView = getChildAt(i);
				if (childView.getVisibility() != View.GONE) {
					final int childWidth = childView.getMeasuredWidth();
					childView.layout(childLeft, 0, 
							childLeft+childWidth, childView.getMeasuredHeight());
					childLeft += childWidth;
				}
			}
		}
	}
	  protected void dispatchDraw(Canvas canvas) {

	        // ViewGroup.dispatchDraw() supports many features we don't need:
	        // clip to padding, layout animation, animation listener, disappearing
	        // children, etc. The following implementation attempts to fast-track
	        // the drawing dispatch by drawing only what we know needs to be drawn.

	        final long drawingTime = getDrawingTime();
	        // todo be smarter about which children need drawing
	        final int count = getChildCount();
	        for (int i = 0; i < count; i++) {
	            drawChild(canvas, getChildAt(i), drawingTime);
	        }
	        for (OnScrollListener mListener : mListeners) {
	            int adjustedScrollX = getScrollX() + pageWidthPadding();
	            mListener.onScroll(adjustedScrollX);
	            if (adjustedScrollX % getWidth() == 0) {
	                mListener.onViewScrollFinished(adjustedScrollX / getWidth());
	            }
	        }
	    }

	    int pageWidthPadding() {
	        return ((getMeasuredWidth() - getWidth()) / 2);
	    }

    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {   
    	Log.e(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);   
  
        final int width = MeasureSpec.getSize(widthMeasureSpec);   
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);   
  
        /*     if (widthMode != MeasureSpec.EXACTLY) {   
            throw new IllegalStateException("ScrollLayout only canmCurScreen run at EXACTLY mode!"); 
        }*/   
        final int height = MeasureSpec.getSize(heightMeasureSpec);   
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);   
      /*  if (heightMode != MeasureSpec.EXACTLY) {   
            throw new IllegalStateException("ScrollLayout only can run at EXACTLY mode!");
        } */  

        Log.v("&&&&&&&&&&&&&&&&&"+height,heightMode+"_**********************_"+MeasureSpec.EXACTLY);
        
        // The children are given the same width and height as the scrollLayout   
        final int count = getChildCount();   
        for (int i = 0; i < count; i++) {   
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);   
        }   
        // Log.e(TAG, "moving to screen "+mCurScreen);   
        scrollTo(mCurScreen * width, 0);         
    }  
    
    /**
     * According to the position of current layout
     * scroll to the destination page.
     */
    public void snapToDestination() {
    	final int screenWidth = getWidth();
    	final int destScreen = (getScrollX()+ screenWidth/2)/screenWidth;
    	snapToScreen(destScreen);
    }
    
    public void snapToScreen(int whichScreen) {
    	// get the valid layout page
    	whichScreen = Math.max(0, Math.min(whichScreen, getChildCount()-1));
    	if (getScrollX() != (whichScreen*getWidth())) {
    		
    		final int delta = whichScreen*getWidth()-getScrollX();
    		mScroller.startScroll(getScrollX(), 0, 
    				delta, 0, Math.abs(delta)*2);
    		mCurScreen = whichScreen;
    		invalidate();		// Redraw the layout
    	}
    }
    
    public void setToScreen(int whichScreen) {
    	whichScreen = Math.max(0, Math.min(whichScreen, getChildCount()-1));
    	mCurScreen = whichScreen;
    	scrollTo(whichScreen*getWidth(), 0);
    }
    
    public int getCurScreen() {
    	return mCurScreen;
    }
    public int getPageWidth() {
        return getWidth();
    }

	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		
		final int action = event.getAction();
		final float x = event.getX();
		final float y = event.getY();
		
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			Log.e(TAG, "event down!");
			if (!mScroller.isFinished()){
				mScroller.abortAnimation();
			}
			mLastMotionX = x;
			break;
			
		case MotionEvent.ACTION_MOVE:
			int deltaX = (int)(mLastMotionX - x);
			mLastMotionX = x;
			
            scrollBy(deltaX, 0);
			break;
			
		case MotionEvent.ACTION_UP:
			Log.e(TAG, "event : up");   
            // if (mTouchState == TOUCH_STATE_SCROLLING) {   
            final VelocityTracker velocityTracker = mVelocityTracker;   
            velocityTracker.computeCurrentVelocity(1000);   
            int velocityX = (int) velocityTracker.getXVelocity();   

            Log.e(TAG, "velocityX:"+velocityX); 
            
            if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {   
                // Fling enough to move left   
            	Log.e(TAG, "snap left");
                snapToScreen(mCurScreen - 1);   
            } else if (velocityX < -SNAP_VELOCITY   
                    && mCurScreen < getChildCount() - 1) {   
                // Fling enough to move right   
            	Log.e(TAG, "snap right");
                snapToScreen(mCurScreen + 1);   
            } else {   
                snapToDestination();   
            }   

            if (mVelocityTracker != null) {   
                mVelocityTracker.recycle();   
                mVelocityTracker = null;   
            }   
            // }   
            mTouchState = TOUCH_STATE_REST;   
			break;
		case MotionEvent.ACTION_CANCEL:
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		
		return true;
	}

    public void addOnScrollListener(OnScrollListener listener) {
        mListeners.add(listener);
    }

    public void removeOnScrollListener(OnScrollListener listener) {
        mListeners.remove(listener);
    }
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onInterceptTouchEvent-slop:"+mTouchSlop);
		
		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE) && 
				(mTouchState != TOUCH_STATE_REST)) {
			return true;
		}
		
		final float x = ev.getX();
		final float y = ev.getY();
		
		switch (action) {
		case MotionEvent.ACTION_MOVE:
			final int xDiff = (int)Math.abs(mLastMotionX-x);
			if (xDiff>mTouchSlop) {
				mTouchState = TOUCH_STATE_SCROLLING;
				
			}
			break;
			
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			mLastMotionY = y;
			mTouchState = mScroller.isFinished()? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
			break;
			
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		
		return mTouchState != TOUCH_STATE_REST;
	}
    /**
     * Implement to receive events on scroll position and page snaps.
     */
    public static interface OnScrollListener {
        /**
         * Receives the current scroll X value.  This value will be adjusted to assume the left edge of the first
         * page has a scroll position of 0.  Note that values less than 0 and greater than the right edge of the
         * last page are possible due to touch events scrolling beyond the edges.
         * @param scrollX Scroll X value
         */
        void onScroll(int scrollX);
        /**
         * Invoked when scrolling is finished (settled on a page, centered).
         * @param currentPage The current page
         */
        void onViewScrollFinished(int currentPage);
    }
}
