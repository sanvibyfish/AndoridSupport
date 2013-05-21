package com.pitaya.framework.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;


import android.util.Log;


public abstract class AbstractHttpApi implements HttpApi {
	
	private static final String TAG = "AbstractHttpApi";
	
    private static final String DEFAULT_CLIENT_VERSION = "com.fg114.maimaijia";
    private static final String CLIENT_VERSION_HEADER = "User-Agent";
    private static final int TIMEOUT = 30;
    private static final boolean DEBUG = true;
    
    
    private final DefaultHttpClient mHttpClient;
    private final String mClientVersion;    
    
    public AbstractHttpApi(DefaultHttpClient httpClient, String clientVersion) {
        mHttpClient = httpClient;
        if (clientVersion != null) {
            mClientVersion = clientVersion;
        } else {
            mClientVersion = DEFAULT_CLIENT_VERSION;
        }
    }
    
    public InputStream executeHttpRequestSuccess(HttpRequestBase httpRequest) throws Exception{
    	 HttpResponse response = executeHttpRequest(httpRequest);
    	 int statusCode = response.getStatusLine().getStatusCode();
         switch (statusCode) {
         case 200:
            return response.getEntity().getContent();             
         case 400:
         	if(DEBUG)Log.d(TAG, "HTTP Code: 400");
             throw new Exception(
                     EntityUtils.toString(response.getEntity()));

         case 401:
             response.getEntity().consumeContent();
             if(DEBUG)Log.d(TAG, "HTTP Code: 401");
             throw new Exception(response.getStatusLine().toString());

         case 404:
             response.getEntity().consumeContent();
             if(DEBUG)Log.d(TAG, "HTTP Code: 404");
             throw new Exception(response.getStatusLine().toString());

         case 500:
             response.getEntity().consumeContent();
             if(DEBUG)Log.d(TAG, "HTTP Code: 500");
             throw new Exception("Consuetude is down. Try again later.");

         default:
         	 if(DEBUG)Log.d(TAG, "Default case for status code reached: "
                     + response.getStatusLine().toString());
             response.getEntity().consumeContent();
             throw new Exception("Error connecting to server: " + statusCode + ". Try again later.");
     }
    }
    
    public String doHttpPost(String url, NameValuePair... nameValuePairs)
            throws Exception,
            IOException {
        HttpPost httpPost = createHttpPost(url, nameValuePairs);

        HttpResponse response = executeHttpRequest(httpPost);

        switch (response.getStatusLine().getStatusCode()) {
            case 200:
                try {
                    return EntityUtils.toString(response.getEntity());
                } catch (ParseException e) {
                    throw new Exception(e.getMessage());
                }

            case 401:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());

            case 404:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());

            default:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());
        }
    }

    /**
     * execute() an httpRequest catching exceptions and returning null instead.
     *
     * @param httpRequest
     * @return
     * @throws IOException
     */
    public HttpResponse executeHttpRequest(HttpRequestBase httpRequest) throws IOException {
        try {
            mHttpClient.getConnectionManager().closeExpiredConnections();
            return mHttpClient.execute(httpRequest);
        } catch (IOException e) {
            httpRequest.abort();
            throw e;
        } 
    }

    public HttpGet createHttpGet(String url, NameValuePair... nameValuePairs) {
        String query = URLEncodedUtils.format(stripNulls(nameValuePairs), HTTP.UTF_8);
        HttpGet httpGet = new HttpGet(url + "?" + query);
        httpGet.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
        return httpGet;
    }

    /**
     * 上传图片请传入参数为image
     */
    public HttpPost createHttpPost(String url, NameValuePair... nameValuePairs) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
        try {
        	
	        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
	        for(int index=0; index < nameValuePairs.length; index++) {
	            if(nameValuePairs[index].getName().equals("image")) {
	                // If the key equals to "image", we use FileBody to transfer the data
						entity.addPart(nameValuePairs[index].getName(), new FileBody(new File (nameValuePairs[index].getValue())));
	            } else {
	                // Normal string data
	                entity.addPart(nameValuePairs[index].getName(), new StringBody(nameValuePairs[index].getValue(),Charset.forName("UTF-8")));
	            }
	        }

	        httpPost.setEntity(entity);
        } catch (UnsupportedEncodingException e1) {
            throw new IllegalArgumentException("Unable to encode http parameters.");
        }
        return httpPost;
    }

    private List<NameValuePair> stripNulls(NameValuePair... nameValuePairs) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (int i = 0; i < nameValuePairs.length; i++) {
            NameValuePair param = nameValuePairs[i];
            if (param.getValue() != null) {
                params.add(param);
            }
        }
        return params;
    }

    /**
     * Create a thread-safe client. This client does not do redirecting, to allow us to capture
     * correct "error" codes.
     *
     * @return HttpClient
     */
    public static final DefaultHttpClient createHttpClient() {
        // Sets up the http part of the service.
        final SchemeRegistry supportedSchemes = new SchemeRegistry();

        // Register the "http" protocol scheme, it is required
        // by the default operator to look up socket factories.
        final SocketFactory sf = PlainSocketFactory.getSocketFactory();
        supportedSchemes.register(new Scheme("http", sf, 80));

        // Set some client http client parameter defaults.
        final HttpParams httpParams = createHttpParams();
        HttpClientParams.setRedirecting(httpParams, false);

        final ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParams,
                supportedSchemes);
        return new DefaultHttpClient(ccm, httpParams);
    }

    /**
     * Create the default HTTP protocol parameters.
     */
    private static final HttpParams createHttpParams() {
        final HttpParams params = new BasicHttpParams();

        // Turn off stale checking. Our connections break all the time anyway,
        // and it's not worth it to pay the penalty of checking every time.
        HttpConnectionParams.setStaleCheckingEnabled(params, false);

        HttpConnectionParams.setConnectionTimeout(params, TIMEOUT * 1000);
        HttpConnectionParams.setSoTimeout(params, TIMEOUT * 1000);
        HttpConnectionParams.setSocketBufferSize(params, 8192);

        return params;
    }

}
