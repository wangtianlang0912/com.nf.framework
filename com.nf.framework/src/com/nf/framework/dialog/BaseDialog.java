package com.nf.framework.dialog;
/**
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nf.framework.R;

public class BaseDialog extends AbsBaseDialog {
	private TextView txtContentView;

	public BaseDialog(Context context) {
	
		super(context,DIALOG_BUTTON_STYLE_TWO);
		// TODO Auto-generated constructor stub
	}
	public BaseDialog(Context context,int currentBtnStyle) {
		super(context,currentBtnStyle);
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
		View txtView =LayoutInflater.from(getContext()).inflate(R.layout.common_dialog_txt_part,null);
		txtContentView=(TextView) txtView.findViewById(R.id.common_dialog_txt_view);
		contentLayout.addView(txtView);
	}
	/**
	 * 设置消息显示内容
	 * @param content
	 */
	public void setContent(String content) {
		txtContentView.setText(content);
	}
	
}
