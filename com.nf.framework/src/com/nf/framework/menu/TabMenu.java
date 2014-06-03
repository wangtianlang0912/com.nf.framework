/**
 * 功能： TODO 
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-9-12       下午05:36:37
 * Copyright (c) 2012, TNT All Rights Reserved.
*/

package com.nf.framework.menu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nf.framework.R;

public class TabMenu extends PopupWindow{
	private GridView gvBody, gvTitle;
	private LinearLayout mLayout;
	private MenuTitleAdapter titleAdapter;
	private OnBodyItemClickListener onBodyItemClickListener;
	/**
	 * 创建多栏目menu
	 * @param context
	 * @param titleClick
	 * @param bodyClick
	 * @param titleAdapter
	 * @param colorBgTabMenu
	 * @param aniTabMenu
	 */
	public TabMenu(Context context,OnItemClickListener titleClick,OnItemClickListener bodyClick,
			MenuTitleAdapter titleAdapter,int colorBgTabMenu,int aniTabMenu){
		super(context);
		
		mLayout = new LinearLayout(context);
		mLayout.setOrientation(LinearLayout.VERTICAL);
		//标题选项栏
		gvTitle = new GridView(context);
		gvTitle.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		gvTitle.setNumColumns(titleAdapter.getCount());
		gvTitle.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		gvTitle.setVerticalSpacing(1);
		gvTitle.setHorizontalSpacing(1);
		gvTitle.setGravity(Gravity.CENTER);
		gvTitle.setOnItemClickListener(titleClick);
		gvTitle.setAdapter(titleAdapter);
		gvTitle.setSelector(new ColorDrawable(Color.TRANSPARENT));//选中的时候为透明色
		this.titleAdapter=titleAdapter;
		//子选项栏
		gvBody = new GridView(context);
		gvBody.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		gvBody.setSelector(new ColorDrawable(Color.TRANSPARENT));//选中的时候为透明色
		gvBody.setNumColumns(4);
		gvBody.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		Display display =((Activity)context).getWindowManager().getDefaultDisplay();
		DisplayMetrics metric = new DisplayMetrics();
		display.getMetrics(metric);
		int paddingSize=(int)(10*metric.density);
		gvBody.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
		gvBody.setGravity(Gravity.CENTER);
		gvBody.setOnItemClickListener(bodyClick);
		mLayout.addView(gvTitle);
		mLayout.addView(gvBody);
		
		//设置默认项
		this.setContentView(mLayout);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setBackgroundDrawable(new ColorDrawable(colorBgTabMenu));// 设置TabMenu菜单背景
		this.setAnimationStyle(aniTabMenu);
		this.setFocusable(true);// menu菜单获得焦点 如果没有获得焦点menu菜单中的控件事件无法响应
	}
	/**
	 * 创建单一栏目menu
	 * @param context
	 * @param bodyClick
	 * @param colorBgTabMenu //TabMenu的背景颜色 
	 * @param aniTabMenu //出现与消失的动画  
	 */
	public TabMenu(Context context,int colorBgTabMenu,int aniTabMenu,int numItem){
		super(context);
		
		mLayout = new LinearLayout(context);
		mLayout.setOrientation(LinearLayout.VERTICAL);
		//子选项栏
		gvBody = new GridView(context);
		gvBody.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		if(numItem>3){
			gvBody.setNumColumns(2);	
		}else{
			gvBody.setNumColumns(numItem);
		}
		gvBody.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		gvBody.setGravity(Gravity.CENTER);
		gvBody.setSelector(new ColorDrawable(Color.TRANSPARENT));//选中的时候为透明色
		gvBody.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(onBodyItemClickListener!=null)
					onBodyItemClickListener.onItemClick(arg0,arg1,arg2,arg3);
					onBodyItemClickListener.closeMenu(TabMenu.this);
			}
		});
		mLayout.addView(gvBody);
		
		//设置默认项
		this.setContentView(mLayout);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setBackgroundDrawable(new ColorDrawable(colorBgTabMenu));// 设置TabMenu菜单背景
		this.setAnimationStyle(aniTabMenu);
		  /*设置触摸外面时消失*/  
		this.setOutsideTouchable(true);  
		this.setFocusable(true);// menu菜单获得焦点 如果没有获得焦点menu菜单中的控件事件无法响应
		this.setTouchable(true);
		//sub_view 是PopupWindow的子View
		gvBody.setFocusableInTouchMode(true);
		gvBody.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if(isShowing()){
					if ((keyCode == KeyEvent.KEYCODE_MENU)) {
							dismiss();// 这里写明模拟menu的PopupWindow退出就行
						return true;
					}
				}
				return false;
			}
		});
	}
	/**
	 * 设置显示栏目
	 * @param index
	 */
	public void SetTitleSelect(int index)
	{  if(this.gvTitle!=null)
		gvTitle.setSelection(index);
		if(this.titleAdapter!=null)
		this.titleAdapter.SetFocus(index);
	}
	
	public void SetBodySelect(int index,int colorSelBody)
	{
		int count=gvBody.getChildCount();
		for(int i=0;i<count;i++)
		{
			if(i!=index)
				((LinearLayout)gvBody.getChildAt(i)).setBackgroundColor(Color.TRANSPARENT);
		}
		((LinearLayout)gvBody.getChildAt(index)).setBackgroundColor(colorSelBody);
	}
	
	public void SetBodyAdapter(MenuBodyAdapter bodyAdapter)
	{
		gvBody.setAdapter(bodyAdapter);
	}
	/**
	 * 为指定某item 设置文本
	 * @param position
	 * @param text
	 */
	public void setItemText(MenuBodyAdapter adapter,int position,String text){
		LinearLayout lly=	(LinearLayout)(adapter.getItem(position));
		TextView tv=(TextView)(lly.getChildAt(1));
		tv.setText(text);
	}
	/**
	 * 自定义Adapter，TabMenu的每个分页的主体
	 * 
	 */
	static public class MenuBodyAdapter extends BaseAdapter {
		private Context mContext;
		private int fontSize;
		private String[] texts;
		private int[] imgResID;
		private Drawable[] draws;
		private int[] textsRes;
		private int imageLpWidth=32;
		private float menubody_density=1.0f;
		/**
		 * 设置TabMenu的分页主体
		 * @param context 调用方的上下文
		 * @param texts 按钮集合的字符串数组
		 * @param resID 按钮集合的图标资源数组
		 * @param fontSize 按钮字体大小
		 * @param color 按钮字体颜色
		 */
		public MenuBodyAdapter(Context context, String[] texts,int[] imgResID, int fontSize) 
		{
			this.mContext = context;
			this.texts = texts;
			this.fontSize=fontSize;
			this.imgResID=imgResID;
			InitDensity(context);
		}
		public MenuBodyAdapter(Context context, int[] textsRes,int[] imgResID, int fontSize) 
		{
			this.mContext = context;
			this.textsRes = textsRes;
			this.fontSize=fontSize;
			this.imgResID=imgResID;
			InitDensity(context);
		}
		public MenuBodyAdapter(Context context, String[] texts,Drawable[] draws, int fontSize) 
		{
			this.mContext = context;
			this.texts = texts;
			this.fontSize=fontSize;
			this.draws=draws;
			InitDensity(context);
		}
		public MenuBodyAdapter(Context context, int[] textsRes,Drawable[] draws, int fontSize) 
		{
			this.mContext = context;
			this.textsRes = textsRes;
			this.fontSize=fontSize;
			this.draws=draws;
			InitDensity(context);
		}
		private void InitDensity(Context context){
			Display display =((Activity)context).getWindowManager().getDefaultDisplay();
			DisplayMetrics metric = new DisplayMetrics();
			display.getMetrics(metric);
			menubody_density=metric.density;
		}
		public int getCount() {
			int textCount=0;
			if(texts!=null){
				textCount= texts.length;
			}else if(textsRes!=null){
				textCount= textsRes.length;
			}
			return textCount;
		}
		public Object getItem(int position) {
			
			return makeMenyBody(position);
		}
		public long getItemId(int position) {
			return position;
		}
		
		public String[] getTexts() {
			return texts;
		}
		public void setTexts(String[] texts) {
			this.texts = texts;
		}
		/**
		 * 为指定某item 设置文本
		 * @param position
		 * @param text
		 */
		public void setItemText(int position,String text){
			TextView tv=(TextView)(((LinearLayout)getItem(position)).getChildAt(1));
			tv.setText(text);
		}
		/**
		 * 为指定某item 设置文本
		 * @param position
		 * @param text
		 */
		public void setItemText(int position,int text){
			TextView tv=(TextView)(((LinearLayout)getItem(position)).getChildAt(1));
			tv.setText(text);
		}
		/**
		 * 设置图片的大小
		 */
		public void setMenuImageSize(int imageSize){
			if(imageSize!=0){
				return;
			}
			this.imageLpWidth=(int)menubody_density*imageSize;
		}
		private LinearLayout makeMenyBody(int position)
		{
			LinearLayout result=new LinearLayout(this.mContext);
			result.setOrientation(LinearLayout.VERTICAL);
			result.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);	
			result.setBackgroundResource(R.drawable.common_menu_item_bg);
			TextView text = new TextView(this.mContext);
			if(texts!=null&&texts.length!=0){
				text.setText(texts[position]);
			}else if(textsRes!=null&&textsRes.length!=0){
				text.setText(textsRes[position]);
			}
			text.setTextSize(fontSize);
			text.setTextColor(this.mContext.getResources().getColor(R.color.common_menu_txt_color));
			text.setGravity(Gravity.CENTER);
			text.setPadding(0,(int)menubody_density*5,0,0);
			ImageView img=new ImageView(this.mContext);
			if(imgResID!=null&&imgResID.length!=0){
				img.setImageResource(imgResID[position]);//SvgImageView
			}else if(draws!=null&&draws.length!=0){
				img.setImageDrawable(draws[position]);
			}
			int imagedpWidth=(int)(menubody_density*this.imageLpWidth);
			img.setLayoutParams(new LayoutParams(imagedpWidth,imagedpWidth));
			result.addView(img);
			result.addView(text);
			return result;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			
			return makeMenyBody(position);
		}
	}
	/**
	 * 自定义Adapter,TabMenu的分页标签部分
	 * 
	 */
	static public class MenuTitleAdapter extends BaseAdapter {
		private Context mContext;
        private int fontColor,unselcolor,selcolor;
        private TextView[] title;
    	private int textViewPaddingSize=10;
        /**
         * 设置TabMenu的title
         * @param context 调用方的上下文
         * @param titles 分页标签的字符串数组
         * @param fontSize 字体大小
         * @param fontcolor 字体颜色
         * @param unselcolor 未选中项的背景色
         * @param selcolor 选中项的背景色
         */
        public MenuTitleAdapter(Context context, String[] titles, int fontSize,
                int fontcolor,int unselcolor,int selcolor) {
            this.mContext = context;
            this.fontColor = fontcolor;
            this.unselcolor = unselcolor;
            this.selcolor=selcolor;
            this.title = new TextView[titles.length];
            textViewPaddingSize=(int)(getScreenDensity(context)*10);
            for (int i = 0; i < titles.length; i++) {
                title[i] = new TextView(mContext);
                title[i].setText(titles[i]);
                title[i].setTextSize(fontSize);
                title[i].setTextColor(fontColor);
                title[i].setGravity(Gravity.CENTER);
                title[i].setPadding(textViewPaddingSize,textViewPaddingSize,textViewPaddingSize,textViewPaddingSize);
            }
        }
        private float getScreenDensity(Context context){
			Display display =((Activity)context).getWindowManager().getDefaultDisplay();
			DisplayMetrics metric = new DisplayMetrics();
			display.getMetrics(metric);
			return metric.density;
		}
        public int getCount() {
            return title.length;
        }
        public Object getItem(int position) {
            return title[position];
        }
        public long getItemId(int position) {
            return title[position].getId();
        }
        /**
         * 设置选中的效果
         */
        private void SetFocus(int index)
        {
        	int length=title.length;
        	for(int i=0;i<length;i++)
        	{
        		if(i!=index)
        		{
        			title[i].setBackgroundDrawable(new ColorDrawable(unselcolor));//设置没选中的颜色
                    title[i].setTextColor(fontColor);//设置没选中项的字体颜色
        		}
        	}
        	title[index].setBackgroundColor(0x00);//设置选中项的颜色
            title[index].setTextColor(selcolor);//设置选中项的字体颜色
        }
        
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            if (convertView == null) {
                v = title[position];
            } else {
                v = convertView;
            }
            return v;
        }
	}
	/**
	 * menu选项事件监听
	 * @author win7
	 *
	 */
	public  interface OnBodyItemClickListener{
		/**
		 * 继承onItemClickListener事件
		 * @param arg0
		 * @param arg1
		 * @param arg2
		 * @param arg3
		 */
		public abstract void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3);
		/**
		 * 调用完毕后，关闭menu
		 */
		public abstract void closeMenu(TabMenu tabMenu);

	}
	public  void setOnBodyItemClickListener(OnBodyItemClickListener onBodyItemClickListener){
		this.onBodyItemClickListener=onBodyItemClickListener;
	}
}
