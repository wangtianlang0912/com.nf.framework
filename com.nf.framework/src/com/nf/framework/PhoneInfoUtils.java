/**
 * 获取手机信息
 */
package com.nf.framework;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;

/**
 * 手机信息获取
 * 
 * @author Administrator 常用字段，数据获取
 */
public class PhoneInfoUtils {

	public static PhoneInfoUtils pdd = null;
	public Context context;

	public static PhoneInfoUtils getInstance(Context mcontext) {
		if (pdd == null) {

			pdd = new PhoneInfoUtils(mcontext);
		}
		return pdd;
	}

	public PhoneInfoUtils(Context mcontext) {
		context = mcontext;
	}

	/**
	 * 通过包名获取应用程序的名称。
	 * 
	 * @param context
	 *            Context对象。
	 * @param packageName
	 *            包名。
	 * @return 返回包名所对应的应用程序的名称。
	 */
	public static String getProgramNameByPackageName(Context context,
			String packageName) {
		PackageManager pm = context.getPackageManager();
		String name = null;
		try {
			name = pm.getApplicationLabel(
					pm.getApplicationInfo(packageName,
							PackageManager.GET_META_DATA)).toString();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * 获取当前应用的版本号
	 * 
	 * @param context
	 * @return
	 */
	public String getAppVersionNum() {

		String currentversion = null;
		try {
			currentversion = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 获取本地版本号

		return currentversion;
	}

	/**
	 * 获取应用的的代号
	 * 
	 * @param context
	 * @return
	 */
	public int getVersionCode() {

		int currentversionCode = 1;
		try {
			currentversionCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 获取本地版本号

		return currentversionCode;
	}

	/**
	 * 获取手机的电话号码
	 * 
	 * @return
	 */
	public String phoneNum() {
		// 获取手机号、手机串号信息
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		String phonenum = null;
		if (imei != null) {
			phonenum = tm.getLine1Number();
		}
		return phonenum;
	}

	/**
	 * 获取手机卡的序列号
	 * 
	 * @return
	 */
	public String getImsi() {

		// 获取手机号、手机串号信息
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = tm.getSimSerialNumber();

		return imsi;
	}

	/**
	 * 获取系统的版本号
	 * 
	 * @param 例如
	 *            ：2.2.2
	 * @return
	 */
	public String getOSVersionNum() {

		String currentversion = android.os.Build.VERSION.RELEASE;

		return currentversion;
	}

	/**
	 * 获取android操作系统版本
	 * 
	 * @return
	 */
	public int getAndroidSDKVersion() {
		int version = 1;
		try {
			version = android.os.Build.VERSION.SDK_INT;
		} catch (NumberFormatException e) {
			e.getStackTrace();
		}
		return version;
	}

	/**
	 * 手机系统的相关信息
	 */
	public void PhoneInfo() {

		String phoneInfo = "Product: " + android.os.Build.PRODUCT;
		phoneInfo += ", CPU_ABI: " + android.os.Build.CPU_ABI;
		phoneInfo += ", TAGS: " + android.os.Build.TAGS;
		phoneInfo += ", VERSION_CODES.BASE: "
				+ android.os.Build.VERSION_CODES.BASE;
		phoneInfo += ", MODEL: " + android.os.Build.MODEL;
		phoneInfo += ", SDK: " + android.os.Build.VERSION.SDK;
		phoneInfo += ", VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE;
		phoneInfo += ", DEVICE: " + android.os.Build.DEVICE;
		phoneInfo += ", DISPLAY: " + android.os.Build.DISPLAY;
		phoneInfo += ", BRAND: " + android.os.Build.BRAND;
		phoneInfo += ", BOARD: " + android.os.Build.BOARD;
		phoneInfo += ", FINGERPRINT: " + android.os.Build.FINGERPRINT;
		phoneInfo += ", ID: " + android.os.Build.ID;
		phoneInfo += ", MANUFACTURER: " + android.os.Build.MANUFACTURER;
		phoneInfo += ", USER: " + android.os.Build.USER;

		Log.e("手机信息：：", phoneInfo);

	}

	/**
	 * 获取手机串号
	 * 
	 * @return
	 */
	public String getDeviceId() {
		String deviceId = null;
		try {
			// 获取手机号、手机串号信息 当获取不到设备号时，系统会提供一个自动的deviceId
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			deviceId = tm.getDeviceId();
		} catch (Exception e) {
			e.getStackTrace();
			deviceId = "999999999999999";
		}
		return deviceId;
	}
	/***
	 * 获取mac地址，只有在开启wifi开关的情况下才能获取到
	 * @return
	 */
	public String getMac() {
		String macSerial = null;
		String str = "";
		try {
			Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);

			for (; null != str;) {
				str = input.readLine();
				if (str != null) {
					macSerial = str.trim();// 去空格
					break;
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return macSerial;
	}

	/***
	 * 获取cpu信息 例 ARMv7 Processor rev 0 (v7l)
	 * @return
	 */
	public String getCPU() {
		String cpuSerial = null;
		String str = "";
		try {
			Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);

			for (; null != str;) {
				str = input.readLine();
				if (str != null) {
					cpuSerial = str.trim();// 去空格
					break;
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return cpuSerial;
	}

	/***
	 * 获得系统当前时间 毫秒级
	 */
	public long getSysTimeByMillis() {
		long millis = System.currentTimeMillis();
		return millis;
	}

	/**
	 * 获得制定时间毫秒级
	 * 
	 * @param date
	 * @param time
	 * @return
	 */
	public long getMillisTime(String date, String time) {

		String[] dateStrs = date.split(":");
		String[] timeStrs = time.split(":");
		Calendar cal = new GregorianCalendar();
		cal.set(Integer.valueOf(dateStrs[0]), Integer.valueOf(dateStrs[1]) - 1,
				Integer.valueOf(dateStrs[2]), Integer.valueOf(timeStrs[0]),
				Integer.valueOf(timeStrs[1]), Integer.valueOf(timeStrs[2]));

		long CurrentMillis = 0;
		CurrentMillis = cal.getTime().getTime();

		return CurrentMillis;
	}

	/**
	 * 当前时间获取日期 time 例如： System.currentTimeMillis() 格式："yyyy:MM:dd kk:mm:ss"
	 * return 格式为24小时制 2012:03:23
	 */
	public String getDateByFormat() {
		long millis = getSysTimeByMillis();
		String datetime = DateFormat.format("yyyy:MM:dd", millis).toString();
		return datetime;
	}

	/**
	 * 当前时间获取时间 time 例如： System.currentTimeMillis() 格式："kk:mm" return 格式为24小时制
	 * 12:08
	 */
	public String getHourByFormat() {
		long millis = getSysTimeByMillis();
		String datetime = DateFormat.format("kk:mm", millis).toString();
		return datetime;
	}

	/**
	 * 当前时间获取日期 time 例如： System.currentTimeMillis() 格式："yyyy:MM:dd kk:mm:ss"
	 * return 格式为24小时制 2012:03:23
	 */
	public String getDateByFormat(long millTime) {
		String datetime = DateFormat.format("yyyy:MM:dd", millTime).toString();
		return datetime;
	}

	/**
	 * 判断是否存在快捷方式
	 * 
	 * @param context
	 * @return
	 */
	public boolean hasShortCut(String appName) {
		String url = "";
		if (getAndroidSDKVersion() < 8) {
			url = "content://com.android.launcher.settings/favorites?notify=true";
		} else {
			url = "content://com.android.launcher2.settings/favorites?notify=true";
		}
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(Uri.parse(url), new String[] { "title",
				"iconResource" }, "title=?", new String[] { appName }, null);
		if (cursor != null && cursor.moveToFirst()) {
			cursor.close();
			return true;
		}
		return false;
	}

	/**
	 * 判断桌面是否已添加快捷方式
	 * 
	 * @param cx
	 * @param titleName
	 *            快捷方式名称
	 * @return
	 */
	public static boolean hasShortcut(Context cx, String appName) {
		boolean result = false;
		// 获取当前应用名称
		String title = appName;
		// try {
		// final PackageManager pm = cx.getPackageManager();
		// title = pm.getApplicationLabel(
		// pm.getApplicationInfo(cx.getPackageName(),
		// PackageManager.GET_META_DATA)).toString();
		// } catch (Exception e) {
		// }
		final String uriStr;
		if (android.os.Build.VERSION.SDK_INT < 8) {
			uriStr = "content://com.android.launcher.settings/favorites?notify=true";
		} else {
			uriStr = "content://com.android.launcher2.settings/favorites?notify=true";
		}
		final Uri CONTENT_URI = Uri.parse(uriStr);
		final Cursor c = cx.getContentResolver().query(CONTENT_URI, null,
				"title=?", new String[] { title }, null);
		if (c != null && c.getCount() > 0) {
			result = true;
		}
		return result;
	}

	/* 提示用户是否创建快捷方式Dialog */
	public void showDialog(Context mcontext, final int icon,
			final String appName, final Class<?> cls) {
		// 定义一个AlertDialog.builder方法,用于退出程序提示
		AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
		builder.setMessage("是否创建桌面快捷方式");
		// 设置标题为提示
		builder.setTitle("提示");
		// 设置一个取消按钮，设置监听，监听事件为如果点击了，就将这个Dialog关闭
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		// 设置一个确认按钮，给其设置监听，点击就关闭这个Activity，Dialog也关闭
		builder.setPositiveButton("确认",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent addShortcut = new Intent(
								"com.android.launcher.action.INSTALL_SHORTCUT");
						Parcelable ShortcutIcon = Intent.ShortcutIconResource
								.fromContext(context, icon);
						addShortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
								appName);
						addShortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
								new Intent().setClass(context, cls));
						addShortcut.putExtra(
								Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
								ShortcutIcon);
						context.sendBroadcast(addShortcut);
					}
				});
		builder.create().show();
	}

	/**
	 * 添加快捷方式
	 * 
	 * @param icon
	 * @param appName
	 * @param cls
	 */
	public void addShortcut(int icon, String appName, Class<?> cls) {

		Intent intent = new Intent();
		intent.setClass(context, cls);
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");

		Intent addShortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		Parcelable ShortcutIcon = Intent.ShortcutIconResource.fromContext(
				context, icon);

		addShortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
		addShortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		addShortcut.putExtra("duplicate", false); // //不允许重复创建
		addShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, ShortcutIcon);
		context.sendBroadcast(addShortcut);
	}

	/****
	 * 删除程序的快捷方式
	 * 
	 * @param context
	 * @param appName
	 */
	public static void delShortcut(Context context, String appName) {
		Intent shortcut = new Intent(
				"com.android.launcher.action.UNINSTALL_SHORTCUT");
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
		// 指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer
		// 注意: ComponentName的第二个参数必须是完整的类名（包名+类名），否则无法删除快捷方式
		String appClass = context.getPackageName() + ".ActivityName";
		ComponentName comp = new ComponentName(context.getPackageName(),
				appClass);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(
				Intent.ACTION_MAIN).setComponent(comp));
		context.sendBroadcast(shortcut);
	}

	/**
	 * 判断一个应用是否被安装到本地
	 * 
	 * @param apkName
	 * @return
	 */
	public boolean isInstall(String apkName) {

		PackageInfo packageInfo;
		boolean isInstall = false;
		try {
			packageInfo = context.getPackageManager()
					.getPackageInfo(apkName, 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		if (packageInfo != null) {
			isInstall = true;
		}
		return isInstall;
	}

	/**
	 * 
	 * 获取手机CPU是单核还是多核 Gets the number of cores available in this device, across
	 * all processors. Requires: Ability to peruse the filesystem at
	 * "/sys/devices/system/cpu"
	 * 
	 * @return The number of cores, or 1 if failed to get result
	 */
	public int getNumCores() {
		// Private Class to display only CPU devices in the directory listing
		class CpuFilter implements FileFilter {
			@Override
			public boolean accept(File pathname) {
				// Check if filename is "cpu", followed by a single digit number
				if (Pattern.matches("cpu[0-9]", pathname.getName())) {
					return true;
				}
				return false;
			}
		}

		try {
			// Get directory containing CPU info
			File dir = new File("/sys/devices/system/cpu/");
			// Filter to only list the devices we care about
			File[] files = dir.listFiles(new CpuFilter());
			// Return the number of cores (virtual CPU devices)
			return files.length;
		} catch (Exception e) {
			// Default to return 1 core
			return 1;
		}
	}

	/**
	 * 当前时间获取时间 time 例如： System.currentTimeMillis() 格式："kk:mm" return 格式为24小时制
	 * 12:08:23
	 */
	public String getSecondsByFormat() {
		long millis = getSysTimeByMillis();
		String datetime = DateFormat.format("kk:mm:ss", millis).toString();
		return datetime;
	}
}
