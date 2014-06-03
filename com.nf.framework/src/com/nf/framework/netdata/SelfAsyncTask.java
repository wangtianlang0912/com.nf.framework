///**
// * SelfAsyncTask.java
// * com.Apricotforest.main
// * ���̣�medicalJournals_for_android
// * ���ܣ� TODO 
// * author      date          time      
// * ������������������������������������������������������������������������������������������
// * niufei     2012-7-26       ����09:56:30
// * Copyright (c) 2012, TNT All Rights Reserved.
//*/
//
//package com.nf.framework.netdata;
//
//import android.content.Context;
//import android.os.AsyncTask;
//import android.widget.Toast;
//
//import com.nf.framework.CheckInternet;
//import com.nf.framework.dialog.ProgressDialog;
//
//
//public class SelfAsyncTask extends AsyncTask <String, Integer, BaseObjectVO> {
//	
//	private static  Context mcontext=null;
//	private static  SelfAsyncTask self=null;
//	public	AsyncTaskInterface ati=null;
//	private static ProgressDialog spd=null;
//	private boolean isCanRunInBackground=true;
//	/**
//	 * �����첽�߳�����  
//	 * @param _mcontext activity context
//	 * @param dialogVisible �Ƿ�ʹ��  ����dialog 
//	 * @return
//	 */
//	public static SelfAsyncTask CreateInstance(Context _mcontext,boolean dialogVisible){
//		
//			self= new SelfAsyncTask();
//			if(dialogVisible){//ʹ��
//				if(mcontext==null||!mcontext.equals(_mcontext)){
//				  spd=new ProgressDialog(_mcontext);
//				  mcontext= _mcontext;
//				}
//			}else{
//				spd=null;
//			}
//		 return self;
//	}
//	protected void onPreExecute() {
//		super.onPreExecute();
//		boolean isNet = CheckInternet.getInstance(mcontext).checkInternet();
//		if (!isNet) {
//			InternetReferUtil();
//			setCanRunInBackground(false);
//			return;
//		}
//		showDialog();
//	}
//	protected BaseObjectVO doInBackground(String... params) {
//		// TODO Auto-generated method stub
//		if(isCanRunInBackground()){
//			return doInBackgroundDeal(params);
//		}else{
//			return null;
//		}
//		
//	}
//	protected void onPostExecute(BaseObjectVO result) {
//		super.onPostExecute(result);
//		if(isCanRunInBackground()){
//			if(result==null&&mcontext!=null){
//				Toast.makeText(mcontext,"�����쳣�����Ժ�����",Toast.LENGTH_LONG).show();
//			}else{
//				onPostExecuteDeal(result);
//			}
//		}
//		dismissDialog();
//		if (ati != null) {
//			ati.onFinishedExecute();
//		}
//	}
//	/***
//	 * �ж��Ƿ����ִ��background�еķ���
//	 * @return
//	 */
//	public boolean isCanRunInBackground() {
//		return isCanRunInBackground;
//	}
//
//	public void setCanRunInBackground(boolean isCanRunInBackground) {
//		this.isCanRunInBackground = isCanRunInBackground;
//	}
//	/***
//	 * ����������ʱ���������� Ĭ��Ϊ��ʾ�Ի���
//	 */
//	public void InternetReferUtil(){
//		CheckInternet.getInstance(mcontext).netDialog(mcontext);
//	}
//	public interface AsyncTaskInterface{
//		public BaseObjectVO doInBackgroundDeal(String... params);
//		public void onPostExecuteDeal(BaseObjectVO result);
//		public void onFinishedExecute();
//	}
//	
//	/**
//	 * 
//	 * @param asyncTaskPostExecuteInter
//	 */
//	public void setAsyncTaskDeal(AsyncTaskInterface asyncTaskInter) {
//		this.ati = asyncTaskInter;
//	}
//	/**
//	 * doInBackground�е���ݴ���
//	 * @return
//	 */
//	public BaseObjectVO doInBackgroundDeal(String... params){
//		BaseObjectVO object=null;
//		if (ati != null) {
//			object=	ati.doInBackgroundDeal(params);
//		}
//		return object;
//	}
//	/**
//	 * onPostExecute�е���ݴ���
//	 * @return
//	 */
//	public void onPostExecuteDeal(BaseObjectVO result){
//		
//		if (ati != null) {
//			ati.onPostExecuteDeal(result);
//		}
//	}
//	/**
//	 * Show this dialog
//	 */
//	public void showDialog(){
//		if(spd!=null&&!spd.isShowing())
//		spd.show(); 
//	}
//	/**
//	 * Dismiss this dialog
//	 */
//	public void dismissDialog(){
//		if(spd!=null&&spd.isShowing())
//		spd.dismiss(); 
//	}
//}
//
