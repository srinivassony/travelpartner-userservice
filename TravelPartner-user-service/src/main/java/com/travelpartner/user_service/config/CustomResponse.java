package com.travelpartner.user_service.config;

import java.util.HashMap;
import java.util.Map;

public class CustomResponse<T> {

	private Map<String, Object> responseBody;
	private String statusCode;
	private int statusCodeValue;

	public CustomResponse(T body, String statusCode, int statusCodeValue) {
		this.responseBody = new HashMap<>();
		this.statusCode = statusCode;
		this.statusCodeValue = statusCodeValue;

		if (body instanceof String) {
			responseBody.put("message", body);
		} else {
			responseBody.put("data", body);
		}
	}

	public Map<String, Object> getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(T body) {
		if (body instanceof String) {
			this.responseBody.put("message", body);
		} else {
			this.responseBody.put("data", body);
		}
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatusCodeValue() {
		return statusCodeValue;
	}

	public void setStatusCodeValue(int statusCodeValue) {
		this.statusCodeValue = statusCodeValue;
	}

}
