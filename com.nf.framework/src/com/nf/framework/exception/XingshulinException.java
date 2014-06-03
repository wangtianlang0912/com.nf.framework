/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.nf.framework.exception;


/**
 * 
 * 接口调用异常，需要开发者进行处理
 * 
 * @author niufei
 *
 */
public class XingshulinException extends Exception {

	
	
    public XingshulinException() {
		super();
	}

	private static final long serialVersionUID = 1L;

    /**
     * 服务器返回的错误代码，详细信息见：
     */
    private int errorCode;

    private String orgResponse;

    public XingshulinException(String errorMessage) {
        super(errorMessage);
    }

    public XingshulinException(int errorCode, String errorMessage, String orgResponse) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.orgResponse = orgResponse;
    }

    public XingshulinException(XingshulinError error) {
        super (error.getMessage());
        this.errorCode = error.getErrorCode();
        this.orgResponse = error.getOrgResponse();
    }

    public String getOrgResponse() {
        return orgResponse;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return "errorCode:" + this.errorCode + "\nerrorMessage:" + this.getMessage()
                + "\norgResponse:" + this.orgResponse;
    }

}
