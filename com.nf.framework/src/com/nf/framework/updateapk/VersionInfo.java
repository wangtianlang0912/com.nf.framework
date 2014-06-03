package com.nf.framework.updateapk;

import java.io.Serializable;
import java.util.Date;

/**
 * 软件版本更新对象
 * 
 * @author Royal
 * 
 */
public class VersionInfo  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5598490655789324049L;
	//":{"apk_path":"http://www.xingshulin.com/Pub/doc/medicalJournals_for_android.apk",
	//"id":2,"productname":"epocket","status":1,
	//"update_date":,"version_update_description":"xxxxxxx","versioncode":1,"versionname":"1.0"},"
	private int id;
	// 版本名称
	private String versionname;
	//下载地址
	private String apk_path;
	// 版本更新内容
	private String version_update_description;
	// 版本号
	private int versioncode;
	// 应用名称
	private String productname;
	//当前状态
	private int status;
	private String downLoadFileDir;//文件保存路径名称
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getVersionname() {
		return versionname;
	}
	public void setVersionname(String versionname) {
		this.versionname = versionname;
	}
	public String getVersion_update_description() {
		return version_update_description;
	}
	public void setVersion_update_description(String versionUpdateDescription) {
		version_update_description = versionUpdateDescription;
	}
	public int getVersioncode() {
		return versioncode;
	}
	public void setVersioncode(int versioncode) {
		this.versioncode = versioncode;
	}
	public String getProductname() {
		return productname;
	}
	public void setProductname(String productname) {
		this.productname = productname;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getApk_path() {
		return apk_path;
	}
	public void setApk_path(String apkPath) {
		apk_path = apkPath;
	}
	public String getDownLoadFileDir() {
		return downLoadFileDir;
	}
	public void setDownLoadFileDir(String downLoadFileDir) {
		this.downLoadFileDir = downLoadFileDir;
	}

}
