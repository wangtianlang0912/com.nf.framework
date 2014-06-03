package com.ApricotforestStatistic.VO;

import java.io.Serializable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/****
 * 统计事件对象
 * @author niufei
 *
 */
@DatabaseTable(tableName = "staticEvent")
public class StaticEventVO implements Serializable { 
	
	public static final String Colum_UserId="userId";
	public static final String Colum_NetType="netType";
	public static final String Colum_NetWorking="netWorking";
	public static final String Colum_EventName="eventName";
	public static final String Colum_EventRelatedId="eventRelatedId";
	public static final String Colum_Count="count";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@DatabaseField(generatedId = true) 
	int id; 
	@DatabaseField 
	String userId;
	@DatabaseField 
	String netType;
	@DatabaseField 
	String netWorking;//网络类型，网络运营商
	@DatabaseField 
	String eventName;//事件名称
	@DatabaseField 
	String eventRelatedId;//事件关联id
	@DatabaseField
	int count;
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
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getEventRelatedId() {
		return eventRelatedId;
	}
	public void setEventRelatedId(String eventRelatedId) {
		this.eventRelatedId = eventRelatedId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
