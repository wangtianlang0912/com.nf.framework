package com.nf.framework.imagebrowser;

import java.io.Serializable;

public class ImageBrowserVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6207223882841827824L;
	String description;
	boolean isNew,isSubscribe;
	String itemName;
	String picUrl;
	String itemtype;
	String smailPicUrlLocalPath;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isNew() {
		return isNew;
	}
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	public boolean isSubscribe() {
		return isSubscribe;
	}
	public void setSubscribe(boolean isSubscribe) {
		this.isSubscribe = isSubscribe;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getItemtype() {
		return itemtype;
	}
	public void setItemtype(String itemtype) {
		this.itemtype = itemtype;
	}
	public String getSmailPicUrlLocalPath() {
		return smailPicUrlLocalPath;
	}
	public void setSmailPicUrlLocalPath(String smailPicUrlLocalPath) {
		this.smailPicUrlLocalPath = smailPicUrlLocalPath;
	}
	
}
