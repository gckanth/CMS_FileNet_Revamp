package com.cms.edrm.filenet.exception;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.cms.edrm.filenet.constants.EDRMServiceConstants;
import com.cms.edrm.filenet.service.impl.EDRMDocumentServiceImpl;

/**
 * 
 * Handling the Custom Exception in the EDRM Service
 *
 */
@SuppressWarnings("unused")
public class EDRMException  extends Exception
{

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(EDRMDocumentServiceImpl.class);
		
	private String message = null;
	private String exceptionCode = null;
	private String exceptionMessage = null;
	private String responseCode = null;
	private String responseMessage = null;
	
	/**
	 * default Constructor
	 */
    public EDRMException() {
        super();
    }
    /**
     * Contructor with String as arguments to throw the exception message
     * @param message --exceptionmessage
     */
    public EDRMException(String message) {
        super(message);
        this.message = message;
    }
    /**
     * Contructor with Throwable as arguments to throw the exception cause
     * @param message --exceptionmessage
     */
    public EDRMException(String message,Throwable cause) {
        super(cause);
        this.message = message;
    }
    /*
     * Contructor with arguments to throw the exception code, exception message, response code and response message
     * @param responseCode
     * @param responseMessage
     * @param exceptionCode
     * @param exceptionMessage
     */
   public EDRMException(String responseCode, String responseMessage, String exceptionCode, String exceptionMessage) 
   {
	   this.exceptionCode = exceptionCode;
	   this.exceptionMessage = exceptionMessage;
	   this.responseCode = responseCode;
	   this.responseMessage = responseMessage;
	}
   /*
    * Contructor with arguments to throw the exception code, exception message, response code and response message
     * @param responseCode
     * @param responseMessage
    */
   public EDRMException(String responseCode, String responseMessage) 
   {
	   this.responseCode = responseCode;
	   this.responseMessage = responseMessage;
	}
   /*
    * Method with arguments to throw the exception code, exception message, response code and response message
    * @param responseCode
    * @param responseMessage
    * @param exceptionCode
    * @param exceptionMessage
    * @return JSONObject
    */
   public JSONObject generateExceptionDetails(String responseCode, String responseMessage, String exceptionCode, String exceptionMessage) throws EDRMException{
		
		JSONObject responseJsonObject = new JSONObject();
		JSONObject exceptionJsonObject = new JSONObject();
		JSONObject resultJsonObject = null;
		try
		{
		resultJsonObject = new JSONObject();
		
		if(responseCode.contains(EDRMServiceConstants.FLOWER_BRACE_OPEN_LITERAL))
		{
			responseJsonObject.put(EDRMServiceConstants.DOCUMENT_VS_ID, responseCode);
			responseJsonObject.put(EDRMServiceConstants.RESPONSE_CODE, EDRMServiceConstants.STATUS_CODE_NO_CONTENT);
			responseJsonObject.put(EDRMServiceConstants.RESPONSE_MESSAGE, responseMessage);
		}
		else
		{
			responseJsonObject.put(EDRMServiceConstants.RESPONSE_CODE, responseCode);
			responseJsonObject.put(EDRMServiceConstants.RESPONSE_MESSAGE, responseMessage);
		}
		exceptionJsonObject.put(EDRMServiceConstants.EXCEPTION_CODE, exceptionCode);
		exceptionJsonObject.put(EDRMServiceConstants.EXCEPTION_MESSAGE, exceptionMessage);
		
		resultJsonObject.put(EDRMServiceConstants.RESPONSE, responseJsonObject);
		resultJsonObject.put(EDRMServiceConstants.EXCEPTION, exceptionJsonObject);
		}
		catch(JSONException edrmException)
		{
			LOGGER.error("edrmException : "+edrmException);
		}
		return resultJsonObject;
	}
   
	/**
     * Overide toString method
     */
    @Override
    public String toString() {
    	return "";
    }
}
