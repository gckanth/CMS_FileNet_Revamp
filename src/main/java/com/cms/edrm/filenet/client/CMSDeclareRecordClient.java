package com.cms.edrm.filenet.client;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.cms.edrm.filenet.constants.EDRMServiceConstants;
/*
 * This class is for rest client of CMS application
 */
public class CMSDeclareRecordClient {

	CloseableHttpClient httpclient = null;
	/*
	 * This method is declare record client 
	 * @param RequestJsonString
	 * @return ResponseJsonObject
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public JSONObject declareRecordClientMethod(String requestJson) throws ClientProtocolException, IOException{
		//creating client builder
		httpclient = HttpClientBuilder.create().build();
		JSONObject responseJsonObject = new JSONObject();
		StringEntity input = new StringEntity(requestJson);
		input.setContentType(EDRMServiceConstants.REST_JSON_FORM);
		//post service invoking
		HttpPost httppost = new HttpPost(EDRMServiceConstants.RECORD_DECLARE_REST_API);
		httppost.setEntity(input);
		
		HttpResponse response = httpclient.execute(httppost);
		//response from post service
		HttpEntity resEntity = response.getEntity();
		
		if (resEntity != null) 
		{
			responseJsonObject.put("Response", EntityUtils.toString(resEntity).toString());
		}
		return responseJsonObject;
	}	
}
