package com.ApricotforestStatistic.VO;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/**
 * 错误请求统计
 * @author niufei
 *
 */
@DatabaseTable(tableName = "staticErrorRequest")
public class StaticErrorRequestVO implements Serializable { 
	
	public static final String Colum_Id="id";
	public static final String Colum_RequestUrl="requestUrl";
	public static final String Colum_ErrorReponseCode="errorReponseCode";
	public static final String Colum_OccurTime="occurTime";
	public static final String Colum_RequestType="requestType";
	/**
	 * 
	 */
	private static final long serialVersionUID = -4505213949952942598L;
		@DatabaseField(generatedId = true) 
		int id; 
		@DatabaseField 
		String requestUrl;//错误地址
		@DatabaseField 
		String errorReponseCode;//返回错误码
		@DatabaseField 
		String occurTime; //发生时间
		@DatabaseField 
		String requestType;//请求类型 get or post
		@DatabaseField 
		String netType;
		@DatabaseField 
		String netWorking;//网络类型，网络运营商

		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getRequestUrl() {
			return requestUrl;
		}
		public void setRequestUrl(String requestUrl) {
			this.requestUrl = requestUrl;
		}
		public String getErrorReponseCode() {
			return errorReponseCode;
		}
		public void setErrorReponseCode(String errorReponseCode) {
			this.errorReponseCode = errorReponseCode;
		}
		public String getOccurTime() {
			return occurTime;
		}
		public void setOccurTime(String occurTime) {
			this.occurTime = occurTime;
		}
		public String getRequestType() {
			return requestType;
		}
		public void setRequestType(String requestType) {
			this.requestType = requestType;
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
		
}
