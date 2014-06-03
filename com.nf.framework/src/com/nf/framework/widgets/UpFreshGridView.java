/**
 * TopFooterGridView.java
 * 功能： 实现顶部下拉刷新和底部单击加载数据  的gridview
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-4-17       下午05:48:06
 * Copyright (c) 2012, TNT All Rights Reserved.
 */

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nf.framework.R;

public class UpFreshGridView extends RelativeLayout implements OnScrollListener {

	public static final int COUNT_PER_PAGE = 20;
	private UnScrollGridView mGridView;
	private ScrollView footer_main_scrollview;
	private ImageView arrowImageView;
	private int topArrowImage = R.drawable.common_arrow_black;

	private TextView tipsTextview, lastUpdatedTextView;
	private ProgressBar topProgressBar;
	private ProgressBar bottomProgressBar;
	private RotateAnimation animation = null;
	private RotateAnimation reverseAnimation = null;
	private LinearLayout head_layout = null;
	private final static int RELEASE_To_REFRESH = 0;
	private final static int PULL_To_REFRESH = 1;
	private final static int REFRESHING = 2;
	private final static int DONE = 3;
	private final static int LOADING = 4;
	// 实际的padding的距离与界面上偏移距离的比例
	private final static int RATIO = 3;
	private OnFooterLoadMoreListener loadMoreListener = null;
	// 用于保证startY的值在一个完整的touch事件中只被记录一次
	private boolean isRecored;
	private int headContentWidth;
	private int headContentHeight;

	private int startY;
	private int firstItemIndex;

	private int state;

	private boolean isBack;
	private OnHeaderRefreshListener refreshListener;
	private OnGridViewItemLongClickListener onItemLongClicklistener;
	private boolean isRefreshable;
	private LinearLayout topfooter_main_layout;
	private View footView;
	private Button footer_btn;
	private boolean hasAddFooter = false;
	private ImageView blantView;

	private static final String TAG = "UpFreshGridView";

	public UpFreshGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public UpFreshGridView(Context context) {
		super(context);
		init(context);
	}

	/**
	 * 初始化视图
	 * 
	 * @param mcontext
	 */
	private void init(Context mcontext) {
		footer_main_scrollview = new ScrollView(mcontext);
		footer_main_scrollview.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		footer_main_scrollview.setOnTouchListener(scrollviewTouchListener);
		footer_main_scrollview.setScrollbarFadingEnabled(true);

		topfooter_main_layout = new LinearLayout(mcontext);
		topfooter_main_layout.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		topfooter_main_layout.setOrientation(LinearLayout.VERTICAL);
		// 顶部head
		head_layout = HeadUpdateView(mcontext);
		// 中部gridView
		mGridView = new UnScrollGridView(mcontext);
		mGridView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		mGridView.setScrollBarStyle(0);// 不显示滚动条
		mGridView.setFadingEdgeLength(0);// 拖动到中间位置两边会出现半透明阴影
		mGridView.setOnTouchListener(scrollviewTouchListener);
		mGridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (onItemLongClicklistener != null) {
					return onItemLongClicklistener.onItemLongClick(arg0, arg1,
							arg2, arg3);
				}
				return false;
			}
		});
		blantView = new ImageView(mcontext);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 30, 0, 0);// .addRule(RelativeLayout.ALIGN_PARENT_TOP,30);
		blantView.setLayoutParams(lp);
		blantView.setAdjustViewBounds(true);
		blantView.setPadding(0, 30, 0, 0);
		blantView.setScaleType(ScaleType.FIT_START);
		blantView.setScaleType(ScaleType.CENTER_CROP);
		blantView.setVisibility(View.GONE);
		// 底部分页按钮实现
		footView = bottomLoadMoreView(mcontext);

		topfooter_main_layout.addView(head_layout);
		topfooter_main_layout.addView(mGridView);
		topfooter_main_layout.addView(blantView);

		setFadingEdgeLength(0);// 取消模糊边距

		footer_main_scrollview.addView(topfooter_main_layout);
		this.addView(footer_main_scrollview);

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

		measureView(head_layout);
		headContentHeight = head_layout.getMeasuredHeight();
		headContentWidth = head_layout.getMeasuredWidth();

		head_layout.setPadding(0, -1 * headContentHeight, 0, 0);
		head_layout.invalidate();

		Log.v("size", "width:" + headContentWidth + " height:"
				+ headContentHeight);
		state = DONE;
		isRefreshable = false;
	}

	/**
	 * 顶部更新视图
	 * 
	 * @return
	 */
	private LinearLayout HeadUpdateView(Context mcontext) {
		// 头部刷新

		head_layout = (LinearLayout) LayoutInflater.from(mcontext).inflate(
				R.layout.common_header_refresh_layout, null);
		arrowImageView = (ImageView) head_layout
				.findViewById(R.id.common_header_refresh_layout_arrowImageView);
		arrowImageView.setMinimumWidth(70);
		arrowImageView.setMinimumHeight(50);
		topProgressBar = (ProgressBar) head_layout
				.findViewById(R.id.common_header_refresh_layout_progressBar);
		tipsTextview = (TextView) head_layout
				.findViewById(R.id.common_header_refresh_layout_tipsTextView);
		lastUpdatedTextView = (TextView) head_layout
				.findViewById(R.id.common_header_refresh_layout_lastUpdatedTextView);
		return head_layout;
	}

	private View bottomLoadMoreView(Context mcontext) {

		View footView = LayoutInflater.from(mcontext).inflate(
				R.layout.common_footer_loadmore_layout, null);
		footer_btn = (Button) footView.findViewById(R.id.common_footer_loadmore_btn);
		bottomProgressBar = (ProgressBar) footView
				.findViewById(R.id.common_footer_loadmore_progressbar);
		return footView;
	}

	public void setTopArrowImage(int topArrowImage) {
		this.topArrowImage = topArrowImage;
	}

	public int getTopArrowImage() {
		return topArrowImage;
	}

	/**
	 * 设置topview上标题的颜色
	 * 
	 * @param color
	 */
	public void setTopTipsTextColor(int color) {
		if (tipsTextview != null) {
			tipsTextview.setTextColor(color);
		}
	}

	/**
	 * 设置空制表位图片
	 * 
	 * @param resId
	 */
	public void setBlantViewImageResoure(int resId) {
		if (blantView != null && blantView.getTag() == null) {
			blantView.setImageResource(resId);
			blantView.setTag(resId);
		}
	}

	/**
	 * 添加footerView
	 * 
	 * @param footerview
	 */
	public void addFooterView() {
		if (!hasAddFooter) {
			topfooter_main_layout.addView(footView);
			footer_btn.setVisibility(View.VISIBLE);
			bottomProgressBar.setVisibility(View.GONE);
			hasAddFooter = true;
			// 下部点击加载更多
			footer_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					footer_btn.setVisibility(View.GONE);
					bottomProgressBar.setVisibility(View.VISIBLE);
					loadMoreListener.OnFooterClick(v);
				}
			});
		}

	}

	/**
	 * 设置制表位的显隐
	 * 
	 * @param visibility
	 */
	public void setBlantViewShow(int visibility) {
		if (blantView != null && topfooter_main_layout != null) {
			blantView.setVisibility(visibility);
		}
	}

	/**
	 * 删除footerView
	 * 
	 * @param footerview
	 */
	public void removeFooterView() {
		if (hasAddFooter) {
			topfooter_main_layout.removeView(footView);
			hasAddFooter = false;
		}
	}

	public void setAdapter(ListAdapter adapter) {
		mGridView.setAdapter(adapter);
	}

	public void setOnItemClickListener(OnItemClickListener itemclick) {
		mGridView.setOnItemClickListener(itemclick);
	}

	public void setNumColumns(int num) {
		mGridView.setNumColumns(num);
	}
	
	public int getNumColumns() {
		return mGridView.getNumColumns();
	}
	public GridView getGridView() {
		return mGridView;
	}

	/**
	 * 更新显示控件
	 */
	public void notifyViewSetChanged() {
		if (mGridView != null) {
			ListAdapter adapter = mGridView.getAdapter();
			if (adapter.getCount() == 0) {
				mGridView.setVisibility(View.GONE);
				setBlantViewShow(View.VISIBLE);

			} else {
				mGridView.setVisibility(View.VISIBLE);
				setBlantViewShow(View.GONE);
				mGridView.requestLayoutIfNecessary();
			}
		}
	}

	private OnTouchListener scrollviewTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (footer_main_scrollview.getScrollY() == 0) {
				isRefreshable = true;
			} else {
				isRefreshable = false;
			}
			onTouchEvent(event);
			return false;
		}
	};

	/**
	 * 设置底部分页按钮点击事件
	 * 
	 * @param listener
	 */
	public void setLoadMoreClickListener(OnFooterLoadMoreListener listener) {
		this.loadMoreListener = listener;
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

						// setSelection(0);

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
						// setSelection(0);

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
						head_layout.setPadding(0, -1 * headContentHeight
								+ (tempY - startY) / RATIO, 0, 0);

					}

					// 更新headView的paddingTop
					if (state == RELEASE_To_REFRESH) {
						head_layout.setPadding(0, (tempY - startY) / RATIO
								- headContentHeight, 0, 0);
					}

				}

				break;
			}
		}

		return super.onTouchEvent(event);
	}

	// 当状态改变时候，调用该方法，以更新界面
	private void changeHeaderViewByState(int state) {
		switch (state) {
		case RELEASE_To_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			topProgressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);

			tipsTextview.setText("松开刷新");

			Log.v(TAG, "当前状态，松开刷新");
			break;
		case PULL_To_REFRESH:
			topProgressBar.setVisibility(View.GONE);
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

			head_layout.setPadding(0, 0, 0, 0);

			topProgressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setText("正在刷新...");
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			Log.v(TAG, "当前状态,正在刷新...");
			break;
		case DONE:
			head_layout.setPadding(0, -1 * headContentHeight, 0, 0);

			topProgressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setImageResource(getTopArrowImage());
			tipsTextview.setText("下拉刷新");
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			Log.v(TAG, "当前状态，done");
			break;
		}
	}

	/*****
	 * 刷新前操作
	 */
	public void onPreRefreshView() {
		if (state != REFRESHING)
			changeHeaderViewByState(REFRESHING);
	}

	public void setOnHeaderRefreshListener(
			OnHeaderRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}

	public void onRefreshComplete() {
		state = DONE;
		SimpleDateFormat time=new SimpleDateFormat("HH:mm:ss"); 
		lastUpdatedTextView.setText("上次更新时间：" + time.format(new Date()));
		changeHeaderViewByState(state);
	}

	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}

	// 此方法直接照搬自网络上的一个下拉刷新的demo，此处是“估计”headView的width以及height
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
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

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		Log.v(TAG, "滚动滚动滚动滚动滚动滚动滚动滚动滚动滚动滚动滚动滚动滚动滚动滚动滚动滚动滚动滚动滚动滚动滚动滚动滚动滚动滚动滚动滚动");
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		Log.v(TAG, "滚动停止");
	}

	/**
	 * GridView 长按item事件监听
	 * 
	 * @param clickListener
	 */
	public void setOnGridViewItemLongClickListener(
			OnGridViewItemLongClickListener clickListener) {

		this.onItemLongClicklistener = clickListener;
	}

}
