package com.ApricotforestStatistic.VO;

import java.io.Serializable;
import java.util.List;

/**
 * 统计对象 
 * @author niufei
 *
 */
public class StaticBaseVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	String startTime,endTime;//app开启时间，app关闭时间
	String deviceId,deviceBrand,deviceVersion;//设备信息
	List<StaticPageViewRecordVO> staticPageViewList;//统计视图访问计数列表
	List<StaticPageViewSkipVO> staticPageViewSkipList;//统计视图访问跳转权重记录列表
	List<StaticEventVO> staticEventList;//统计事件列表
	List<StaticErrorRequestVO> staticErrorRequestList;//统计请求异常列表
	List extandList;//业务相关扩展数据
	String systName,systVersion;//系统信息
	String appName,appVersion;//应用信息
	String ticket;//票据 确定与服务器标示
	String channelName;
	String netType;
	String netWorking;//网络类型，网络运营商
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceBrand() {
		return deviceBrand;
	}
	public void setDeviceBrand(String deviceBrand) {
		this.deviceBrand = deviceBrand;
	}
	public String getDeviceVersion() {
		return deviceVersion;
	}
	public void setDeviceVersion(String deviceVersion) {
		this.deviceVersion = deviceVersion;
	}
	public List<StaticEventVO> getStaticEventList() {
		return staticEventList;
	}
	public void setStaticEventList(List<StaticEventVO> staticEventList) {
		this.staticEventList = staticEventList;
	}
	public String getSystName() {
		return systName;
	}
	public void setSystName(String systName) {
		this.systName = systName;
	}
	public String getSystVersion() {
		return systVersion;
	}
	public void setSystVersion(String systVersion) {
		this.systVersion = systVersion;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public List<StaticPageViewRecordVO> getStaticPageViewList() {
		return staticPageViewList;
	}
	public void setStaticPageViewList(List<StaticPageViewRecordVO> staticPageViewList) {
		this.staticPageViewList = staticPageViewList;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public List<StaticErrorRequestVO> getStaticErrorRequestList() {
		return staticErrorRequestList;
	}
	public void setStaticErrorRequestList(
			List<StaticErrorRequestVO> staticErrorRequestList) {
		this.staticErrorRequestList = staticErrorRequestList;
	}
	
	
	public List<StaticPageViewSkipVO> getStaticPageViewSkipList() {
		return staticPageViewSkipList;
	}
	public void setStaticPageViewSkipList(
			List<StaticPageViewSkipVO> staticPageViewSkipList) {
		this.staticPageViewSkipList = staticPageViewSkipList;
	}
	public String getNetType() {
		return netType;
	}
	public void setNetType(String netType) {
		this.netType = netType;
	}
	public String getNetWorking() {
		return netWorking;
	}
	public void setNetWorking(String netWorking) {
		this.netWorking = netWorking;
	}
	public List getExtandList() {
		return extandList;
	}
	public void setExtandList(List extandList) {
		this.extandList = extandList;
	}

}
