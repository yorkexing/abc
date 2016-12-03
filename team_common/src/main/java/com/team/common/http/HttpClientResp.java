package com.team.common.http;

import java.util.HashMap;

/**
 * 
 * @author lwh
 * 
 */
public class HttpClientResp {

    private int httpcode;
    private String bodymessage;
    private HashMap<String,String> headerMap;

    /**
     * @return the httpcode
     */
    public int getHttpcode() {
        return httpcode;
    }
    /**
     * @param httpcode the httpcode to set
     */
    public void setHttpcode(int httpcode) {
        this.httpcode = httpcode;
    }
    /**
     * @return the bodymessage
     */
    public String getBodymessage() {
        return bodymessage;
    }
    /**
     * @param bodymessage the bodymessage to set
     */
    public void setBodymessage(String bodymessage) {
        this.bodymessage = bodymessage;
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
