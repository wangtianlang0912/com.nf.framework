/**
 * BaseClass.java
 * com.Apricotforest.main
 * ���̣�medicalJournals_for_android
 * ���ܣ� TODO 
 * author      date          time      
 * ������������������������������������������������������������������������������������������
 * niufei     2012-9-21       ����02:35:25
 * Copyright (c) 2012, TNT All Rights Reserved.
*/

package com.nf.framework.netdata;

import java.io.Serializable;

public class BaseObjectVO implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1778185471990583625L;
	String obj;
	boolean resultObj;
	String reason;
	String jsonType;
	String errorCode;
	int numRows;
	int data;
	public String getObj() {
		return obj;
	}
	public void setObj(String obj) {
		this.obj = obj;
	}
	
	public boolean isResultObj() {
		return resultObj;
	}
	public void setResultObj(boolean resultObj) {
		this.resultObj = resultObj;
	}
	public int getNumRows() {
		return numRows;
	}
	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getJsonType() {
		return jsonType;
	}
	public void setJsonType(String jsonType) {
		this.jsonType = jsonType;
	}
	public int getData() {
		return data;
	}
	public void setData(int data) {
		this.data = data;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
}
