package com.nusiss.neighbourlysg.exception;

import java.util.Date;

public class ErrorResponse {
	
	private String errorCode;
	private String errorDetails;
	private Date timestamp;
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorDetails() {
		return errorDetails;
	}
	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public ErrorResponse(String errorCode, String errorDetails, Date timestamp) {
		super();
		this.errorCode = errorCode;
		this.errorDetails = errorDetails;
		this.timestamp = timestamp;
	}
	
	
	

}
