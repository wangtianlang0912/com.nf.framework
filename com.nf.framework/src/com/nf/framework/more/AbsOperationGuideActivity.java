package com.nf.framework.more;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.nf.framework.CloseActivityClass;
import com.nf.framework.R;
import com.nf.framework.exception.XingshulinError;

public abstract class AbsOperationGuideActivity extends Activity {

	private Context mcontext;
	private Intent intent = null;
	private int interMethod;
	private List<View> viewList = new ArrayList<View>();
	
	public static final String INTNET_TYPE_PARAM="param";
	/**
	 * 仅作展示，
	 */
	public static final int INTENT_TO_SHOW=0;
	/**
	 * 最后一个view包含有点击事件
	 */
	public static final int INTENT_TO_ONCLICK=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mcontext = this;
		CloseActivityClass.activityList.add(this);
		initView();
	}

	protected void onResume() {
		super.onResume();
		intent = this.getIntent();
		interMethod = intent.getIntExtra(INTNET_TYPE_PARAM, 2);// 进入方法，0代表moreActivity进入，1代表logoActivity
	}

	/**
	 * 初始化加载控件
	 */
	private void initView() {

		ViewPager viewPager = new ViewPager(mcontext);
		setContentView(viewPager);
		ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(mcontext,viewList);

		try {
			XmlResourceParser xmlResourceParser = getResources().getXml(
					operateViewConfigXML());
			// 判断是否到了文件的结尾
			while (xmlResourceParser.getEventType() != XmlResourceParser.END_DOCUMENT) {
				// 文件的内容的起始标签开始，注意这里的起始标签是test.xml文件里面<resources>标签下面的第一个标签

				if (xmlResourceParser.getEventType() == XmlResourceParser.START_TAG) {
					String tagname = xmlResourceParser.getName();
					if (tagname.equals("operate_view_item")) {
						String viewItemName = xmlResourceParser
								.getAttributeValue(0);
						int viewLayoutId = getResources().getIdentifier(
								viewItemName, "layout", getPackageName());
						if (viewLayoutId != 0) {
							View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
									.inflate(viewLayoutId, null);
							if (	viewItemName.equals("operate_item_end")&&interMethod==INTENT_TO_ONCLICK) {
								updateEndOnclickView(view);
							}
							viewList.add(view);
						}
					}
				} else if (xmlResourceParser.getEventType() == XmlResourceParser.END_TAG) {
				} else if (xmlResourceParser.getEventType() == XmlResourceParser.TEXT) {
				}
				xmlResourceParser.next();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			finish();
			throw new XingshulinError(AbsOperationGuideActivity.this, e);
		}
		viewPager.setAdapter(viewPagerAdapter);

	}

//	/**
//	 * 返回方法
//	 */
//	private void returnMethod() {
//		switch (interMethod) {
//		case 1:// 1代表logoActivity
//			// intent.setClass(OperationGuideActivity.this,TabManagerActivity.class);
//			// startActivity(intent);
//			break;
//		default:// 0代表moreActivity进入
//			setResult(RESULT_OK, intent);
//			break;
//		}
//		overridePendingTransition(R.anim.common_push_left_in,
//				R.anim.common_push_left_out);
//		finish();
//	}
	/**
	 * xml中点击事件对应方法
	 * @param view
	 */
	public void operate_main_onclick(View view){
		
		returnMethod(getIntent());
	}
	
	protected abstract int operateViewConfigXML();
	/**
	 * 仅当参数类型为INTENT_TO_ONCLICK 时，最后一个View的点击事件需要调用此方法，其他位置无效
	 * @param endLayout
	 */
	protected abstract void updateEndOnclickView(View endLayout);
	/***
	 * 当参数类型为intent_to_onclick 点击事件处理
	 * @param intent
	 */
	protected abstract void onClickTypeIntentCallBack(Intent intent);
	
	protected void returnMethod(Intent intent){
		if(interMethod==INTENT_TO_ONCLICK){// 1代表logoActivity
			onClickTypeIntentCallBack(intent);
		}else{
			setResult(RESULT_OK, intent);
		}
		overridePendingTransition(R.anim.common_push_left_in,R.anim.common_push_left_out);
		finish();
	}
	
	protected void onPause() {
		super.onPause();
	}

	/**
	 * 按下返回按钮
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		Log.e("ParentActivity","调用了返回按钮");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			returnMethod(getIntent());
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private class ViewPagerAdapter extends PagerAdapter {

		Context mcontext;
		List<View> viewList;

		public ViewPagerAdapter(Context mcontext, List<View> viewList) {
			// TODO Auto-generated constructor stub
			this.mcontext = mcontext;
			this.viewList = viewList;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return viewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0.equals(arg1);
		}

		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).removeView(viewList.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).addView(viewList.get(arg1));
			return viewList.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}
	}
}
