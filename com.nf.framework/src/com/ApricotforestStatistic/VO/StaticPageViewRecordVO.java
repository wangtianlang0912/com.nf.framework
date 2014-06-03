package com.ApricotforestStatistic.VO;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/****
 * 访问页面
 * @author niufei
 *
 */
@DatabaseTable(tableName = "staticPageView")
public class StaticPageViewRecordVO implements Serializable{

	private static final long serialVersionUID = -6193060437119263143L;

	public static final String  Column_ID="id";
	
	public static final String  Column_CurrentViewCode="currentViewCode";
	
	
	public static final String  Column_StartTime="startTime";
	
	public static final String  Column_EndTime="endTime";
	
	public static final String  Column_StateTime="stateTime";
	
	public static final String  Column_Count="count";
	
	public static final String  Column_AverageTime="averageTime";
	
	public static final String Column_UserId="userId";
	@DatabaseField(generatedId = true) 
	int id; 
	@DatabaseField 
	String currentViewCode;//视图编号
	@DatabaseField 
	String startTime;//当次操作的开始时间
	@DatabaseField
	String endTime;//当次操作的结束时间
	@DatabaseField
	String stateTime;//当前操作的停留时间
	@DatabaseField
	int count;
	@DatabaseField
	String averageTime;//页面访问的平均停留时间
	@DatabaseField 
	String userId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCurrentViewCode() {
		return currentViewCode;
	}
	public void setCurrentViewCode(String currentViewCode) {
		this.currentViewCode = currentViewCode;
	}
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
	public String getStateTime() {
		return stateTime;
	}
	public void setStateTime(String stateTime) {
		this.stateTime = stateTime;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getAverageTime() {
		return averageTime;
	}
	public void setAverageTime(String averageTime) {
		this.averageTime = averageTime;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((currentViewCode == null) ? 0 : currentViewCode.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StaticPageViewRecordVO other = (StaticPageViewRecordVO) obj;
		if (currentViewCode == null) {
			if (other.currentViewCode != null)
				return false;
		} else if (!currentViewCode.equals(other.currentViewCode))
			return false;
		return true;
	}
	/***
	 * 最终返回到服务端的数据
	 * @param preViewCode
	 * @param currentViewCode
	 * @param count
	 * @param averageTime
	 */
	public StaticPageViewRecordVO(String preViewCode, String currentViewCode,
			int count, String averageTime) {
		super();
		this.currentViewCode = currentViewCode;
		this.count = count;
		this.averageTime = averageTime;
	}
	public StaticPageViewRecordVO(){
		
	}
	
	
}
