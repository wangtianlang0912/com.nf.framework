package com.nf.framework.dialog;

/**
 * dialogActivity.java
 * 功能： TODO 
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-6-5       下午12:33:02
 * Copyright (c) 2012, TNT All Rights Reserved.
 */

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.nf.framework.R;

public class EditTextDialog extends AbsBaseDialog {
	

	private EditText contentEditTxt;
	

	public EditTextDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public EditTextDialog(Context context, int currentBtnStyle) {
		super(context, currentBtnStyle);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	protected void setContentLayoutView(LinearLayout contentLayout) {
		// TODO Auto-generated method stub
		View txtView =LayoutInflater.from(getContext()).inflate(R.layout.common_dialog_edittext_part,null);
		contentEditTxt=(EditText) txtView.findViewById(R.id.common_dialog_edittext_view);
		contentLayout.addView(txtView);
	}
	/**
	 *获取编辑框的文本内容
	 */
	public String  getEditText(){
		
		return contentEditTxt.getText().toString();
	}
	/**
	 *设置标题栏图片
	 * 
	 * @param image
	 */
	public void setTitleImg(int image) {

		iv_title.setImageResource(image);
	}

	/**
	 *设置标题栏标题
	 * 
	 * @param title
	 */
	public void setTitleText(String title) {

		tv_title.setText(title);
	}

	/**
	 * 设置消息显示内容
	 * 
	 * @param content
	 */
	public void setEditTextContent(String content) {

		contentEditTxt.setText(content);
		contentEditTxt.findFocus();
		contentEditTxt.setFocusable(true);
	}
}
