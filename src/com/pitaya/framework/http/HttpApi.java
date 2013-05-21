package com.pitaya.framework.http;


import java.io.InputStream;
import java.lang.reflect.Type;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

public interface HttpApi {
	/**
	 * 返回对象请实现Result
	 * @param httpRequest
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	 abstract public Result doHttpRequest(HttpRequestBase httpRequest,
			 Class clazz) throws Exception;
	 
	    abstract public String doHttpPost(String url, NameValuePair... nameValuePairs)
	            throws Exception;

	    abstract public HttpGet createHttpGet(String url, NameValuePair... nameValuePairs);

	    abstract public HttpPost createHttpPost(String url, NameValuePair... nameValuePairs);
	    
}
