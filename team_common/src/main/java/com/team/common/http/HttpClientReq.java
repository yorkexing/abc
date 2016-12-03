package com.team.common.http;

import java.util.HashMap;

/**
 * 
 * @author LWH
 * 
 */
public class HttpClientReq {

    private String reqUrl;
    private String bodyMessage;
    private HashMap<String,String> paraMap;
    private HashMap<String,String> headerMap;
	/**
	 * @return the reqUrl
	 */
	public String getReqUrl() {
		return reqUrl;
	}
	/**
	 * @param reqUrl the reqUrl to set
	 */
	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}
	/**
	 * @return the bodyMessage
	 */
	public String getBodyMessage() {
		return bodyMessage;
	}
	/**
	 * @param bodyMessage the bodyMessage to set
	 */
	public void setBodyMessage(String bodyMessage) {
		this.bodyMessage = bodyMessage;
	}
	/**
	 * @return the paraMap
	 */
	public HashMap<String, String> getParaMap() {
		return paraMap;
	}
	/**
	 * @param paraMap the paraMap to set
	 */
	public void setParaMap(HashMap<String, String> paraMap) {
		this.paraMap = paraMap;
	}
	/**
	 * @return the headerMap
	 */
	public HashMap<String, String> getHeaderMap() {
		return headerMap;
	}
	/**
	 * @param headerMap the headerMap to set
	 */
	public void setHeaderMap(HashMap<String, String> headerMap) {
		this.headerMap = headerMap;
	}

}
