package com.nf.framework.dialog;
/**
 * 功能： TODO 
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-6-5       下午12:33:02
 * Copyright (c) 2012, TNT All Rights Reserved.
 */


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nf.framework.R;


public class PicReferDialog extends AbsBaseDialog {
	
	private TextView contextTxt;
	
	private ImageView iv_pic;
	
	public PicReferDialog(Context context, int currentBtnStyle) {
		super(context, currentBtnStyle);
		// TODO Auto-generated constructor stub
	}
	public PicReferDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		super.titleLayout.setVisibility(View.GONE);
	}
	@Override
	protected void setContentLayoutView(LinearLayout contentLayout) {
		// TODO Auto-generated method stub
		View picView =LayoutInflater.from(getContext()).inflate(R.layout.common_dialog_picrefer_part,null);
		contextTxt=(TextView) picView.findViewById(R.id.common_dialog_picrefer_txt_content);
		iv_pic=(ImageView)picView.findViewById(R.id.common_dialog_picrefer_pic_image);
		contentLayout.addView(picView);
		
	}
	/**
	 *设置图片显示
	 * @param image
	 */
	public void setImageId(int imageId) {

		iv_pic.setImageResource(imageId);
	}
	/**
	 *设置图片显示
	 * @param image
	 */
	public void setImageDrawable(Drawable drawable) {

		iv_pic.setImageDrawable(drawable);
	}
	/**
	 * 设置消息显示内容
	 * @param content
	 */
	public void setContent(SpannableString content) {

		contextTxt.setText(content);
	}
	/**
	 * 设置消息显示内容
	 * @param content
	 */
	public void setContent(String content) {

		contextTxt.setText(content);
	}
	/**
	 * 设置消息显示内容
	 * @param content
	 */
	public void setContent(int contentResource) {

		contextTxt.setText(contentResource);
	}
	public TextView getContextTxt() {
		return contextTxt;
	}
}
