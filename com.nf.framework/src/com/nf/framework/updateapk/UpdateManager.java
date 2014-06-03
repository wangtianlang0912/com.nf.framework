package com.nf.framework.updateapk;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.View;
import android.widget.Toast;

import com.nf.framework.R;
import com.nf.framework.dialog.AbsBaseDialog.DialogRightBtnOnClickListener;
import com.nf.framework.dialog.BaseDialog;


/**
 * APK更新管理类
 * 
 * @author Royal
 * 
 */
public class UpdateManager {

	// 上下文对象
	private Context mContext;
	/**
	 * 参数为Context(上下文activity)的构造函数
	 * 2013.3.28新增
	 * @param context
	 */
	public UpdateManager(Context context) {
		this.mContext = context;
	}
	/**
	 * 检查版本更新
	 */
	public void checkUpdate(VersionInfo	info) {
		if (info != null) {
			if (checkUpdateVersion(info)) {
				// 如果当前版本号小于服务端版本号,则弹出提示更新对话框
				apkUpdateInfoDialog(info);
			}else{
				Toast.makeText(mContext,"当前版本为最新版本",0).show();
			}
		}
	}
	
	/***
	 * 检查当前版本与获取版本的大小
	 * @param info
	 * 2013.3.28新增
	 * @return
	 */
	public boolean checkUpdateVersion(VersionInfo	info){
		if(info==null){
			return false;
		}
		// 获取当前软件包信息
		PackageInfo pi = null;
		try {
			pi = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), PackageManager.GET_CONFIGURATIONS);
			// 当前软件版本号
			int versionCode = pi.versionCode;
			return versionCode < info.getVersioncode();
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.getStackTrace();
		}
		return false;
	}	
	/**
	 * 提示更新对话框
	 * 
	 * @param info
	 *            版本信息对象
	 */
	private  void apkUpdateInfoDialog(final VersionInfo versionInfo){

		// 初始化一个自定义的Dialog
		BaseDialog dialog = new BaseDialog(mContext);
		dialog.show();
		dialog.setTitle(R.drawable.common_dialog_login_logo,"版本更新提示");
		dialog.setBtnName(null,"以后再说","现在更新");
		dialog.setContent(versionInfo.getVersion_update_description());
		dialog.setDialogRightBtnOnClickListener(new DialogRightBtnOnClickListener() {
			
			@Override
			public void onButtonClick(View rightBtn) {
				// TODO Auto-generated method stub
					// 弹出下载框
//					showDownloadUpdateDialog(versionInfo);
					// 开启更新服务UpdateService
					// 这里为了把update更好模块化，可以传一些updateService依赖的值
					// 如布局ID，资源ID，动态获取的标题,这里以app_name为例
					Intent updateIntent = new Intent(mContext,UpdateAppService.class);
					updateIntent.putExtra(UpdateAppService.INTENTSERIAL_VERSIONINFO, versionInfo);
					mContext.startService(updateIntent);
			}
		});
	} 
}
