package com.nf.framework.widgets;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nf.framework.R;


/**
 * 下拉刷新listview
 * @author win7
 *
 */

public class UpFreshListView extends ListView implements OnScrollListener {

	private static final String TAG = "listview";

	private final static int RELEASE_To_REFRESH = 0;
	private final static int PULL_To_REFRESH = 1;
	private final static int REFRESHING = 2;
	private final static int DONE = 3;
	private final static int LOADING = 4;
	// 实际的padding的距离与界面上偏移距离的比例
	private final static int RATIO = 3;

	private LayoutInflater inflater;

	private LinearLayout headView;
	private View footView;
	private View autoFootView;
	private boolean isLoadingMore;
	private TextView tipsTextview;
	private TextView lastUpdatedTextView;
	private ImageView arrowImageView;
	private ProgressBar progressBar;
	private ProgressBar bottomProgressBar;
	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;
	private int topArrowImage=R.drawable.common_arrow_black;
	// 用于保证startY的值在一个完整的touch事件中只被记录一次
	private boolean isRecored;

	private int headContentWidth;
	private int headContentHeight;

	private int startY;
	private int firstItemIndex;
	private Button footer_btn;
	private int state;
	private TextView footerLipView;
	private boolean isBack;

	private OnHeaderRefreshListener refreshListener;
	private OnFooterLoadMoreListener footerListener;
	private boolean isRefreshable;
	private OnScrollStopedListener onScrollStopedListener;
	private OnScrollLoadMoreListener onScrollLoadMoreListener;
	public UpFreshListView(Context context) {
		super(context);
		init(context);
	}

	public UpFreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		setCacheColorHint(context.getResources().getColor(R.color.transparent));
		inflater = LayoutInflater.from(context);

		headView = (LinearLayout) inflater.inflate(R.layout.common_header_refresh_layout, null);
		arrowImageView = (ImageView) headView.findViewById(R.id.common_header_refresh_layout_arrowImageView);
		arrowImageView.setMinimumWidth(70);
		arrowImageView.setMinimumHeight(50);
		
		progressBar = (ProgressBar) headView.findViewById(R.id.common_header_refresh_layout_progressBar);
		tipsTextview = (TextView) headView.findViewById(R.id.common_header_refresh_layout_tipsTextView);
		lastUpdatedTextView = (TextView) headView.findViewById(R.id.common_header_refresh_layout_lastUpdatedTextView);
		measureView(headView);
		headContentHeight = headView.getMeasuredHeight();
		headContentWidth = headView.getMeasuredWidth();

		headView.setPadding(0, -1 * headContentHeight, 0,0);
		headView.invalidate();

		Log.v("size", "width:" + headContentWidth + " height:"
				+ headContentHeight);
		addHeaderView(headView, null, false);
		
		setOnScrollListener(this);
		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		state = DONE;
		isRefreshable = false;
	}
	/**
	 * 按钮触发
	 * @param mcontext
	 * @return
	 */
	private View bottomLoadMoreBtnView(Context mcontext){
		
		View footView= LayoutInflater.from(mcontext).inflate(
				R.layout.common_footer_loadmore_layout, null);
		footer_btn =(Button)footView.findViewById(R.id.common_footer_loadmore_btn);
		bottomProgressBar =(ProgressBar)footView.findViewById(R.id.common_footer_loadmore_progressbar);
		return footView;
	}
	
	/**
	 * 按钮触发
	 * @param mcontext
	 * @return
	 */
	private View bottomLoadMoreAutoView(Context mcontext){
		
		View footView= LayoutInflater.from(mcontext).inflate(
				R.layout.common_footer_loadmore_layout2, null);
		footerLipView =(TextView)footView.findViewById(R.id.common_footer_loadmore2_text);
//		bottomProgressBar =(ProgressBar)footView.findViewById(R.id.common_footer_loadmore2_progressbar);
		return footView;
	}
	@Override
	public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2,int arg3) {
		firstItemIndex = firstVisiableItem;
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
			case AbsListView.OnScrollListener.SCROLL_STATE_FLING://开始滚动
				break;
			case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://停止滚动
				if(onScrollStopedListener!=null){
					onScrollStopedListener.onScrollStoped(view);
				}
                System.out.println(view.getLastVisiblePosition()+"==="+view.getCount());
                //判断滚动到底部 
                if(view.getLastVisiblePosition()==(view.getCount()-1)){
	                	//然后 经行一些业务操作 确定底部加载布局
                		if(onScrollLoadMoreListener!=null&&getFooterViewsCount()!=0
                				&&autoFootView!=null&&!isLoadingMore){
                			isLoadingMore=true;
                			onScrollLoadMoreListener.OnScrollLoadMore(view);
                		}
                }
				break;
			case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://正在滚动
				break;
			default:
				break;
		}
	}
	public boolean onTouchEvent(MotionEvent event) {

		if (isRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (firstItemIndex == 0 && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
					Log.v(TAG, "在down时候记录当前位置‘");
				}
				break;

			case MotionEvent.ACTION_UP:

				if (state != REFRESHING && state != LOADING) {
					if (state == DONE) {
						// 什么都不做
					}
					if (state == PULL_To_REFRESH) {
						state = DONE;
						changeHeaderViewByState(state);

						Log.v(TAG, "由下拉刷新状态，到done状态");
					}
					if (state == RELEASE_To_REFRESH) {
						state = REFRESHING;
						changeHeaderViewByState(state);
						onRefresh();

						Log.v(TAG, "由松开刷新状态，到done状态");
					}
				}

				isRecored = false;
				isBack = false;

				break;

			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();

				if (!isRecored && firstItemIndex == 0) {
					Log.v(TAG, "在move时候记录下位置");
					isRecored = true;
					startY = tempY;
				}
				if (state != REFRESHING && isRecored && state != LOADING) {
					// 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动
					// 可以松手去刷新了
					if (state == RELEASE_To_REFRESH) {
						setSelection(0);
						// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
						if (((tempY - startY) / RATIO < headContentHeight)
								&& (tempY - startY) > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState(state);

							Log.v(TAG, "由松开刷新状态转变到下拉刷新状态");
						}
						// 一下子推到顶了
						else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState(state);

							Log.v(TAG, "由松开刷新状态转变到done状态");
						}
						// 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
						else {
							// 不用进行特别的操作，只用更新paddingTop的值就行了
						}
					}
					// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
					if (state == PULL_To_REFRESH) {

						setSelection(0);

						// 下拉到可以进入RELEASE_TO_REFRESH的状态
						if ((tempY - startY) / RATIO >= headContentHeight) {
							state = RELEASE_To_REFRESH;
							isBack = true;
							changeHeaderViewByState(state);

							Log.v(TAG, "由done或者下拉刷新状态转变到松开刷新");
						}
						// 上推到顶了
						else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState(state);

							Log.v(TAG, "由DOne或者下拉刷新状态转变到done状态");
						}
					}
					// done状态下
					if (state == DONE) {
						if (tempY - startY > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState(state);
						}
					}

					// 更新headView的size
					if (state == PULL_To_REFRESH) {
						headView.setPadding(0, -1 * headContentHeight
								+ (tempY - startY) / RATIO, 0, 0);

					}

					// 更新headView的paddingTop
					if (state == RELEASE_To_REFRESH) {
						headView.setPadding(0, (tempY - startY) / RATIO
								- headContentHeight, 0, 0);
					}

				}

				break;
			}
		}

		return super.onTouchEvent(event);
	}

	public void setTopArrowImage(int topArrowImage) {
		this.topArrowImage=topArrowImage;
	}

	public int getTopArrowImage() {
		return topArrowImage;
	}

	// 当状态改变时候，调用该方法，以更新界面
	private void changeHeaderViewByState(int state) {
		switch (state) {
		case RELEASE_To_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);

			tipsTextview.setText("松开刷新");

			Log.v(TAG, "当前状态，松开刷新");
			break;
		case PULL_To_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			// 是由RELEASE_To_REFRESH状态转变来的
			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(reverseAnimation);

				tipsTextview.setText("下拉刷新");
			} else {
				tipsTextview.setText("下拉刷新");
			}
			Log.v(TAG, "当前状态，下拉刷新");
			break;

		case REFRESHING:

			headView.setPadding(0, 0, 0, 0);
			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setText("正在刷新...");
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			Log.v(TAG, "当前状态,正在刷新...");
			break;
		case DONE:
			headView.setPadding(0, -1 * headContentHeight, 0, 0);

			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setImageResource(getTopArrowImage());
			tipsTextview.setText("下拉刷新");
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			Log.v(TAG, "当前状态，done");
			break;
		}
	}

	public void setOnHeaderRefreshListener(OnHeaderRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}
	public void onRefreshComplete() {
		state = DONE;
		lastUpdatedTextView.setText("上次更新时间：" + getDateFarmat().format(new Date()));
		changeHeaderViewByState(state);
		if(footer_btn!=null)
			footer_btn.setVisibility(View.VISIBLE);
		if(bottomProgressBar!=null)
			bottomProgressBar.setVisibility(View.GONE);
		
		isLoadingMore=false;
	}
	
	private SimpleDateFormat getDateFarmat(){
	 
		return new SimpleDateFormat("HH:mm:ss"); 
	}
	/*****
	 * 刷新前操作
	 */
	public void onPreRefreshView(){
		if(state!=REFRESHING)
		changeHeaderViewByState(REFRESHING);
	}
	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();
			removeFooterView();
		}
	}

	// 此方法直接照搬自网络上的一个下拉刷新的demo，此处是“估计”headView的width以及height
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public void setAdapter(BaseAdapter adapter) {
		
		lastUpdatedTextView.setText("上次更新时间：" +getDateFarmat().format(new Date()));
		super.setAdapter(adapter);
	}
	/**
	 * addFooterView
	 * @param visibility
	 */
	public void addFooterView(){
		//底部分页按钮实现
		
		if(footView==null){
			footView=bottomLoadMoreBtnView(getContext());
		}
		if(getFooterViewsCount() == 0){
			addFooterView(footView);
			footer_btn.setVisibility(View.VISIBLE);
			bottomProgressBar.setVisibility(View.GONE);
			footer_btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					footer_btn.setVisibility(View.GONE);
					bottomProgressBar.setVisibility(View.VISIBLE);
					if(footerListener!=null)
						footerListener.OnFooterClick(v);
				}
			});
		}
	}
	/**
	 * autoFootView
	 * @param visibility
	 */
	public void addAutoFooterView(){
		if(autoFootView==null){
			autoFootView=bottomLoadMoreAutoView(getContext());
		}
		if(getFooterViewsCount() == 0){
			addFooterView(autoFootView);
			isLoadingMore=false;
		}
	}
	/**
	 * removeFooterView
	 * @param visibility
	 */
	public void removeFooterView(){
		
		if(footView!=null&&getFooterViewsCount()!= 0)
			removeFooterView(footView);
	}
	public void removeAutoFooterView(){
		
		if(autoFootView!=null&&getFooterViewsCount()!= 0){
			removeFooterView(autoFootView);
			isLoadingMore=false;
		}
	}
	/**
	 * 设置底部分页按钮点击事件
	 */
	public void setFooterOnClickListener(OnFooterLoadMoreListener listener) {
		this.footerListener = listener;
		
	}
	/***
	 * 设置滚动停止回调
	 * @param onScrollStopedListener
	 */
	public void setOnScrollStopedListener(OnScrollStopedListener onScrollStopedListener){
		this.onScrollStopedListener=onScrollStopedListener;
	}
	
	/***
	 * 滚动加载更多
	 * @param onScrollLoadMoreListener
	 */
	public void setOnScrollLoadMoreListener(OnScrollLoadMoreListener onScrollLoadMoreListener){
		
		this.onScrollLoadMoreListener=onScrollLoadMoreListener;
	}
}