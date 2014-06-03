package com.nf.framework.updateapk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

import com.nf.framework.R;
import com.nf.framework.exception.XingshulinError;


/***
 * 更新版本
 * 
 * @author zhangjia
 * 
 */
public class UpdateAppService extends Service {
	/**
	 * 更新app的传参值
	 */
	public static final String INTENTSERIAL_VERSIONINFO = "VersionInfo";

	private static final int TIMEOUT = 10 * 1000;// 超时
	private static final int DOWN_OK = 1;
	private static final int DOWN_ERROR = 0;
	private String down_url = null;
	private String app_name;
	private String downLoadDir;
	private static final String APK_SUFFIX = ".apk";
	private File updateFile;// 需要下载的文件
	private NotificationManager notificationManager;
	private Notification notification;

	private Intent updateIntent;
	private PendingIntent pendingIntent;

	private int notification_id = 0;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent!=null){
			VersionInfo updateApp = (VersionInfo) intent
					.getSerializableExtra(INTENTSERIAL_VERSIONINFO);
			if(updateApp!=null){
				app_name = updateApp.getProductname();
				down_url = updateApp.getApk_path();
				downLoadDir = updateApp.getDownLoadFileDir();
				// 创建文件
				createFile(app_name);
		
				createNotification();
		
				createThread();
			}
		}
		return super.onStartCommand(intent, flags, startId);

	}

	/***
	 * 开线程下载
	 */
	public void createThread() {
		/***
		 * 更新UI
		 */
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case DOWN_OK:
					// 下载完成，点击安装
					Uri uri = Uri.fromFile(updateFile);
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(uri,
							"application/vnd.android.package-archive");

					pendingIntent = PendingIntent.getActivity(
							UpdateAppService.this, 0, intent, 0);

					notification.setLatestEventInfo(UpdateAppService.this,
							app_name, "下载成功，点击安装", pendingIntent);

					notificationManager.notify(notification_id, notification);

					stopService(updateIntent);
					break;
				default:
					notification.setLatestEventInfo(UpdateAppService.this,
							app_name, "下载失败", pendingIntent);
					notificationManager.notify(notification_id, notification);
					stopService(updateIntent);
					break;
				}

			}

		};

		final Message message = new Message();

		new Thread(new Runnable() {
			@Override
			public void run() {

				boolean isSuccess= downloadUpdateFile(down_url,updateFile.toString());
				if (isSuccess) {
					// 下载成功
					message.what = DOWN_OK;
					handler.sendMessage(message);
				} else {
					message.what = DOWN_ERROR;
					handler.sendMessage(message);
				}
			}
		}).start();
	}

	/***
	 * 创建通知栏
	 */
	RemoteViews contentView;

	public void createNotification() {
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notification = new Notification();
		notification.icon = android.R.drawable.stat_sys_download;
		// 这个参数是通知提示闪出来的值.
		notification.tickerText = "开始下载";

		updateIntent = new Intent();
		pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);

		// 这里面的参数是通知栏view显示的内容
		notification.setLatestEventInfo(this, app_name, "下载：0%", pendingIntent);

		notificationManager.notify(notification_id, notification);

		/***
		 * 在这里我们用自定的view来显示Notification
		 */
		contentView = new RemoteViews(getPackageName(),
				R.layout.common_notification_item);
		contentView.setTextViewText(R.id.common_notification_title, "正在下载"
				+ app_name);
		contentView.setTextViewText(R.id.common_notification_txt_percent, "0%");
		contentView.setProgressBar(R.id.common_notification_progress, 100, 0,
				false);

		notification.contentView = contentView;

		updateIntent = new Intent();
		updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);

		notification.contentIntent = pendingIntent;

		notificationManager.notify(notification_id, notification);

	}

	/***
	 * 下载文件
	 * 
	 * @return
	 * @throws MalformedURLException
	 */
	public boolean downloadUpdateFile(String down_url, String file) {
		int down_step = 5;// 提示step
		int totalSize;// 文件总大小
		int downloadCount = 0;// 已经下载好的大小
		int updateCount = 0;//  
		InputStream inputStream;
		OutputStream outputStream;
		try {
			URL url = new URL(down_url);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			httpURLConnection.setConnectTimeout(TIMEOUT);
			httpURLConnection.setReadTimeout(TIMEOUT);
			// 获取下载文件的size
			totalSize = httpURLConnection.getContentLength();
			if (httpURLConnection.getResponseCode() != 200) {
				return false;
			}
			inputStream = httpURLConnection.getInputStream();
			outputStream = new FileOutputStream(file, false);// 文件存在则覆盖掉
			byte buffer[] = new byte[1024];
			int readsize = 0;
			while ((readsize = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, readsize);
				downloadCount += readsize;// 时时获取下载到的大小
				//每次增张5%
				if (updateCount == 0
						|| (downloadCount * 100 / totalSize - down_step) >= updateCount) {
					updateCount += down_step;
					contentView.setTextViewText(
							R.id.common_notification_txt_percent, updateCount
									+ "%");
					contentView.setProgressBar(
							R.id.common_notification_progress, 100,
							updateCount, false);
					// show_view
					notificationManager.notify(notification_id, notification);
				}
			}
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
			inputStream.close();
			outputStream.close();
			return totalSize==downloadCount&&totalSize!=-1&&totalSize!=0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/***
	 * 创建文件
	 */
	public void createFile(String name) {
		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
				.getExternalStorageState())) {
			File updateDir = null;
			if (downLoadDir != null) {
				updateDir = new File(Environment.getExternalStorageDirectory()
						+ File.separator + downLoadDir);
			} else {
				updateDir = new File(Environment.getExternalStorageDirectory()
						.getPath());
			}
			updateFile = new File(updateDir + File.separator + name
					+ APK_SUFFIX);

			if (!updateDir.exists()) {
				updateDir.mkdirs();
			}
			if (!updateFile.exists()) {
				try {
					updateFile.createNewFile();
				} catch (IOException e) {
					throw new XingshulinError(e);
				}
			}
		}
	}
}
