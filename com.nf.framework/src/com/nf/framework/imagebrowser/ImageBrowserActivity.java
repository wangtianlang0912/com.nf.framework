package com.nf.framework.imagebrowser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;

import com.nf.framework.BaseActivity;
import com.nf.framework.CloseActivityClass;
import com.nf.framework.R;
import com.nf.framework.http.imageload.ImageLoader;
import com.nf.framework.widgets.HackyViewPager;
import com.nf.framework.widgets.zoomPhotoView.PhotoView;

public class ImageBrowserActivity extends BaseActivity {

	private ViewPager mViewPager;

	public static final String ImageBrowser_Intent_List = "imageList";

	public static final String ImageBrowser_Intent_Position = "currentPosition";

	private List<ImageBrowserVO> list = new ArrayList<ImageBrowserVO>();
	private WallPaperBrowseAdapter wallPaperAdapter = null;
	private Context mcontext;
	private int currentPosition=0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mcontext = this;
		CloseActivityClass.activityList.add(this);
		initView();
		Intent intent = getIntent();
		list = (List<ImageBrowserVO>) intent
				.getSerializableExtra(ImageBrowser_Intent_List);
		currentPosition = intent.getIntExtra(ImageBrowser_Intent_Position, 0);

		wallPaperAdapter = new WallPaperBrowseAdapter(this, list);
		mViewPager.setAdapter(wallPaperAdapter);
		mViewPager.setCurrentItem(currentPosition, true);
		top_textview.setText((currentPosition+1) + "/" + list.size());
	}

	/***
	 * 初始化视图控件
	 */
	private void initView() {
		mViewPager = new HackyViewPager(this);
		mViewPager.setBackgroundColor(Color.BLACK);
		super.mainlayout.addView(mViewPager);
		super.leftButton.setVisibility(View.VISIBLE);
		super.leftButton.setImageResource(R.drawable.common_navigate_back_btn);
		super.leftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		super.rightButton.setVisibility(View.INVISIBLE);
//		super.rightButton.setImageResource(R.drawable.share_btn);
		super.rightButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openOptionsMenu();
			}
		});
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				currentPosition=arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				String title=(arg0 + 1) + File.separator+ mViewPager.getAdapter().getCount();
				top_textview.setText(title);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});

	}

	protected void onResume() {
		super.onResume();
	}

	protected void onPause() {
		super.onPause();
	}
	/**
	 * 按下返回按钮
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	 class WallPaperBrowseAdapter extends PagerAdapter {

		List<ImageBrowserVO> mlist = new ArrayList<ImageBrowserVO>();
		private ViewGroup mcontainer;
		private LayoutInflater inflater;
		private ImageLoader loadImage;
		public WallPaperBrowseAdapter(Context context, List<ImageBrowserVO> list) {
			this.mlist = list;
			inflater =((Activity) context).getLayoutInflater();
			if(loadImage==null){
				loadImage=ImageLoader.getInstance(ImageBrowserActivity.this);
			}
		}

		@Override
		public int getCount() {
			return mlist.size();
		}
		@Override
		public View instantiateItem(ViewGroup container, int position) {
			// Now just add PhotoView to ViewPager and return it
			View imageLayout = inflater.inflate(R.layout.common_imagebrowser_layout, container, false);
			PhotoView imageView = (PhotoView) imageLayout.findViewById(R.id.common_imagebrowser_layout_image);
			final ProgressBar progressBar = (ProgressBar) imageLayout.findViewById(R.id.common_imagebrowser_image_layout_loading);
			ImageBrowserVO wallPaper = mlist.get(position);
			new ImageDownLoadTask(mcontext,loadImage,imageView, progressBar,0,wallPaper).execute();

			container.addView(imageLayout, LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
			mcontainer = container;
			return imageLayout;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
			
			mcontainer = container;
		}

		@Override
		public void startUpdate(ViewGroup container) {
			// TODO Auto-generated method stub
			super.startUpdate(container);
		}

		@Override
		public void finishUpdate(ViewGroup container) {
			// TODO Auto-generated method stub
			super.finishUpdate(container);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		public ViewGroup getMcontainer() {
			return mcontainer;
		}

		public void setMcontainer(ViewGroup mcontainer) {
			this.mcontainer = mcontainer;
		}

	}

	/**
	 * 创建MENU
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");// 必须创建一项

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void openOptionsMenu() {
		// TODO Auto-generated method stub
		super.openOptionsMenu();

	}

//	/**
//	 * 拦截MENU
//	 */
//	@Override
//	public boolean onMenuOpened(int featureId, Menu menu) {
//		int[] textRes = null;
//			textRes = new int[] { R.string.save_to_album,R.string.pic_socal_share};
//		TabMenu.MenuBodyAdapter bodyAdapter = new TabMenu.MenuBodyAdapter(
//				this,
//				textRes,
//				new int[] { R.drawable.menu_download_btn,R.drawable.menu_imageshare_btn},
//				15, 0xFF333333);
//		bodyAdapter.setMenuImageSize(32);
//		TabMenu tabMenu = new TabMenu(this, 0xffdddddd, R.style.PopupAnimation,
//				bodyAdapter.getCount());
//		tabMenu.update();
//		tabMenu.SetBodyAdapter(bodyAdapter);
//		if (tabMenu != null) {
//			if (tabMenu.isShowing()) {
//				tabMenu.dismiss();
//				tabMenu = null;
//			} else {
//				tabMenu.showAtLocation(getCurrentFocus(), Gravity.BOTTOM,
//						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
//				tabMenu.setOnBodyItemClickListener(new OnBodyItemClickListener() {
//					@Override
//					public void onItemClick(AdapterView<?> arg0, View arg1,
//							int arg2, long arg3) {
//						// TODO Auto-generated method stub
//						onMenuItemClick(arg0, arg1, arg2, arg3);
//					}
//
//					@Override
//					public void closeMenu(TabMenu tabMenu) {
//						// TODO Auto-generated method stub
//						if (tabMenu.isShowing()) {
//							tabMenu.dismiss();
//							tabMenu = null;
//						}
//					}
//				});
//			}
//		}
//		return false;// 返回为true 则显示系统menu
//	}
//
//	public void onMenuItemClick(AdapterView<?> arg0, View arg1, int arg2,
//			long arg3) {
//		// TODO Auto-generated method stub
////		ImageBrowserVO wallPaper=list.get(currentPosition);
////		String bitmapFileName=ImageBrowseUtil.getTempBitmapFileName(mcontext,wallPaper.getPicUrl());
////		String localImageUrl=ImageBrowseUtil.getTempBitmapFilePath(mcontext, bitmapFileName);
////		switch (arg2) {
////		case 0://下载到相册
////			boolean userRole=MJUserRoleAuthority.getInstance().JudgeUserRole(mcontext);
////			if(userRole){//如果用户权限为0 
////				new BaiduSocialShareUtil(ImageBrowseActivity.this).SavePicAlbumAsyncTask(localImageUrl);
////			}
////			break;
////		case 1://社会化分享
////			new BaiduSocialShareUtil(ImageBrowseActivity.this).ShareContent("分享自杏树林医学文献","http://www.xingshulin.com/products/mobileDownload.html",localImageUrl,wallPaper.getPicUrl());
////			break;
////		}
//	}

}