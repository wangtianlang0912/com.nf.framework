/**
 * 功能： TODO 
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-11-26       下午02:09:39
 * Copyright (c) 2012, TNT All Rights Reserved.
*/

package com.nf.framework.widgets.counterButton;

import android.content.Context;


public	class  VerificationInfo {
	/**
	 * 验证码方法
	 * @author win7
	 */
	  public enum VerificationType {
		  CommonVerification,//发送普通验证码（不需要手机排重验证），用于解绑等
		  UnRepeatVerification; //发送需要验证手机号码排重的验证码方法 用于注册和重新绑定新手机号
	  }
	
		Context mcontext;String telphone;VerificationType type;

		public VerificationInfo(Context mcontext,String telphone,VerificationType type){
			this.mcontext=mcontext;
			this.telphone=telphone;
			this.type=type;
		}
		public Context getMcontext() {
			return mcontext;
		}

		public void setMcontext(Context mcontext) {
			this.mcontext = mcontext;
		}

		public String getTelphone() {
			return telphone;
		}

		public void setTelphone(String telphone) {
			this.telphone = telphone;
		}

		public VerificationType getType() {
			return type;
		}

		public void setType(VerificationType type) {
			this.type = type;
		}
		
	}
	