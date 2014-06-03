package com.nf.framework.widgets;


import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * 右边带有字母查询的ListView
 * @author Davee
 */
public class AlphabetListView extends FrameLayout {
    private Context mContext;

    private UpFreshListView mListView;
    private LinearLayout alphabetLayout;
    private TextView mTextView;
    private AlphabetPositionListener positionListener;
    private AlphabetOnItemClickListener onItemClickListener;
    private OnHeaderRefreshListener refreshListener;
	private OnFooterLoadMoreListener footerListener;
    private float screenDensity;
    
    private Handler mHandler;
    
    private HideIndicator mHideIndicator = new HideIndicator();
    
    private int indicatorDuration = 1000;
    
    public void setIndicatorDuration(int duration) {
        this.indicatorDuration = duration;
    }
    
    private final class HideIndicator implements Runnable {
        @Override
        public void run() {
            mTextView.setVisibility(View.INVISIBLE);
        }
    }

    public AlphabetListView(Context context) {
        super(context);
        init(context);
    }
    
    public AlphabetListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    
    /**
     * @param context
     */
    private void init(Context context) {
        mContext = context;

        screenDensity = context.getResources().getDisplayMetrics().density;
        
        mHandler = new Handler();

        mListView = new UpFreshListView(mContext);
        ListViewEventListener();
        initAlphabetLayout(mContext);

        mTextView = new TextView(mContext);
        mTextView.setTextSize(convertDIP2PX(50));
        mTextView.setTextColor(Color.argb(150, 255, 255, 255));
        mTextView.setBackgroundColor(Color.argb(200, 0, 0, 0));
        mTextView.setMinWidth(convertDIP2PX(70));
        mTextView.setMinHeight(convertDIP2PX(70));
        int pixels = convertDIP2PX(10);
        mTextView.setPadding(pixels, pixels, pixels, pixels);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setVisibility(View.INVISIBLE);
        FrameLayout.LayoutParams textLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        textLayoutParams.gravity = Gravity.CENTER;
        mTextView.setLayoutParams(textLayoutParams);
    }
/**
 *列表事件监听
 * @param onItemClickListener
 */
    public void setOnItemClickListener(AlphabetOnItemClickListener onItemClickListener){
    	this.onItemClickListener=onItemClickListener;
    }
    
	public void ListViewEventListener(){
		if(mListView!=null){
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					if(onItemClickListener!=null)
					onItemClickListener.OnItemClickListener(arg0, arg1, arg2, arg3);
				}
			});
//			mListView.setonRefreshListener(new OnRefreshListener() {
//				
//				@Override
//				public void onRefresh() {
//					// TODO Auto-generated method stub
//					if(refreshListener!=null){
//						refreshListener.AlphabetOnRefresh();
//					}
//				}
//			});
			mListView.setFooterOnClickListener(new OnFooterLoadMoreListener() {
				
				@Override
				public void OnFooterClick(View view) {
					// TODO Auto-generated method stub
					if(footerListener!=null){
						footerListener.OnFooterClick(view);
					}
				}
			});
		}
	}
	
    public UpFreshListView getmListView() {
    	return mListView;
	}

	/**
     * 添加Adapter
     * @param adapter
     * @param positionListener
     */
    public void setAdapter(ListAdapter adapter, AlphabetPositionListener positionListener) {
        if (positionListener == null) 
            throw new IllegalArgumentException("AlphabetPositionListener is required");

        mListView.setAdapter(adapter);
        this.positionListener = positionListener;

        this.addView(mListView);
        this.addView(alphabetLayout);
        this.addView(mTextView);
    }
    private void initAlphabetLayout(Context context) {
        alphabetLayout = new LinearLayout(context);
        alphabetLayout.setOrientation(LinearLayout.VERTICAL);
        FrameLayout.LayoutParams alphabetLayoutParams = new FrameLayout.LayoutParams(40,FrameLayout.LayoutParams.FILL_PARENT);
        alphabetLayoutParams.gravity = Gravity.RIGHT;
        alphabetLayout.setLayoutParams(alphabetLayoutParams);
        final String[] alphabet = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        params.gravity = Gravity.CENTER_HORIZONTAL;
        for (int i=0;i<alphabet.length;i++) {
            TextView textView = new TextView(context);
            textView.setTextColor(Color.argb(255, 150, 150, 150));
            textView.setBackgroundColor(Color.argb(0, 255, 255, 0));
            textView.setTextSize(15);
            textView.setText(alphabet[i]);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(params);
            textView.setTag(i);
            alphabetLayout.addView(textView);
        }
        alphabetLayout.setOnTouchListener(new OnTouchListener() {
        	int unit=0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    	TextView tv=(TextView)alphabetLayout.findViewWithTag(0);
                    	unit=tv.getHeight();
                    	alphabetLayout.setBackgroundColor(Color.argb(32, 0, 153, 204));
                        int l = (int)(event.getY()/unit);
                        if(l>=alphabet.length){
                        		l=alphabet.length-1;
                        }
                        int pos = positionListener.getPosition(alphabet[l]);
                        if (pos != -1) {
                            mTextView.setText(alphabet[l]);
                            mTextView.setVisibility(View.VISIBLE);
                            mHandler.removeCallbacks(mHideIndicator);
                            mHandler.postDelayed(mHideIndicator, indicatorDuration);
                            mListView.setSelection(pos);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                    	l=(int)(event.getY()/unit);
                    	if(l>=alphabet.length){
                    		l=alphabet.length-1;
                    	}
                        pos = positionListener.getPosition(alphabet[l]);
                        if (pos != -1) {
                            mTextView.setText(alphabet[l]);
                            mTextView.setVisibility(View.VISIBLE);
                            mHandler.removeCallbacks(mHideIndicator);
                            mHandler.postDelayed(mHideIndicator, indicatorDuration);
                            mListView.setSelection(pos);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        alphabetLayout.setBackgroundResource(0);
                        break;
                }
                return true;
            }
        });
    }

    public int convertDIP2PX(float dip) {
        return (int)(dip*screenDensity + 0.5f*(dip>=0?1:-1));
    }
	public void setonRefreshListener(OnHeaderRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
	}
	/**
	 * 设置底部分页按钮点击事件
	 */
	public void setFooterOnClickListener(OnFooterLoadMoreListener listener) {
		this.footerListener = listener;
		
	}
	/***
	 * 滚动加载更多
	 * @param onScrollLoadMoreListener
	 */
	public void setOnScrollLoadMoreListener(OnScrollLoadMoreListener onScrollLoadMoreListener){
		if(mListView!=null){
			mListView.setOnScrollLoadMoreListener(onScrollLoadMoreListener);
		}
	}
	/**
	 * addFooterView
	 * @param visibility
	 */
	public void addFooterView(){
		if(mListView!=null){
			mListView.addFooterView();
		}
	}
	/**
	 * removeFooterView
	 * @param visibility
	 */
	public void removeFooterView(){
		if(mListView!=null){
			mListView.removeFooterView();
		}
	}
}
