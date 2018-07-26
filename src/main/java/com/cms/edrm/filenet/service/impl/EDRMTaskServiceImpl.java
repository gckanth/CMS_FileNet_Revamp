package com.cms.edrm.filenet.service.impl;

import java.io.FileNotFoundException;
import java.text.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cms.edrm.filenet.constants.EDRMServiceConstants;
import com.cms.edrm.filenet.exception.EDRMException;
import com.cms.edrm.filenet.service.EDRMTaskService;
import com.cms.edrm.filenet.util.CommonServicesUtil;
import com.cms.edrm.filenet.util.DocumentServicesUtil;
import com.cms.edrm.filenet.util.FolderServicesUtil;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.CustomObject;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.property.Properties;

/**
 * 
 * Class which implements the add and update custom objects(task) services
 *
 */
public class EDRMTaskServiceImpl implements EDRMTaskService {
	private static final Logger LOGGER = LoggerFactory.getLogger(EDRMTaskServiceImpl.class);
	/**
	 * Method for adding a new custom object into FileNet Engine
	 * @param Json custom object properties details
	 * @return Json response object
	 * @throws EDRMException 
	 */
	@Override
	public JSONObject createTasks(JSONObject taskDetails) throws EDRMException {
		if (LOGGER.isDebugEnabled()) 
		{
			LOGGER.debug("Start :: addDocument ::  EDRMDocumentServiceImpl");
		}
		FnAuthDataProviderImpl connectionImpl = null;
		CustomObject customObject = null;
		CommonServicesUtil commonServicesUtil = new CommonServicesUtil();
		try {
			connectionImpl = new FnAuthDataProviderImpl();
			ObjectStore objStore = connectionImpl.getObjectStore();
			//requesting parameters information from JSON object
			JSONObject requestDetailsJsonObject = (JSONObject) taskDetails.get(EDRMServiceConstants.REQUEST_DETAILS);
			JSONObject taskJsonObject = (JSONObject) requestDetailsJsonObject.get(EDRMServiceConstants.EDRM_TASK);
			String documentCreator = (String) taskJsonObject.get(EDRMServiceConstants.CREATOR);
			String activeDirectory = EDRMServiceConstants.ACTIVE_DIRECTORY;
			String documentDateCreated = (String) taskJsonObject.get(EDRMServiceConstants.DATE_CREATED);
			String customObjClassName = (String) taskJsonObject.get(EDRMServiceConstants.EDRM_TASK_NAME);
			String folderName = (String) taskJsonObject.get(EDRMServiceConstants.DOCUMENT_FILEPATH);
			JSONArray taskDetailsJson = (JSONArray) taskJsonObject.get(EDRMServiceConstants.EDRM_TASK_DETAILS);
			
			//creating a custom objects
			customObject = commonServicesUtil.createCustomObject(objStore, taskDetailsJson, customObjClassName, 
					folderName, documentCreator+activeDirectory, documentDateCreated);

			String documentId = customObject.get_Id().toString();
			String statusCode = EDRMServiceConstants.SUCCESS_STATUS_CODE;
			String commitStatus = EDRMServiceConstants.CUSTOMOBJECT_COMMIT_SUCCESS;

		//success response from FileNet	
		return new CommonServicesUtil().generateSuccessResponse(statusCode, commitStatus,
						null, null, documentId);
			
		} catch (EngineRuntimeException engineRuntimeException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.ENGINE_RUNTIME_EXCEPTION,
					engineRuntimeException.getExceptionCode().getErrorId(), engineRuntimeException.getMessage());
		} catch (JSONException jsonException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					EDRMServiceConstants.JSON_EXCEPTION, EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					jsonException.getMessage());
		} catch (FileNotFoundException fileNotFoundException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					EDRMServiceConstants.FILE_NOT_FOUND_EXCEPTION, EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					fileNotFoundException.getMessage());
		} catch (Exception exception) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					EDRMServiceConstants.GENERAL_EXCEPTION, EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					exception.getMessage());
		} finally {
			connectionImpl.doSignOff();
		}
	}
	/**
	 * Method is to update the meta data of an existing custom object
	 * @param Json custom object properties details
	 * @return Json response object
	 * @throws EDRMException 
	 */
	@Override
	public JSONObject updateTasks(JSONObject taskDetails) throws EDRMException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start :: updateTasks ::  EDRMTaskServiceImpl");
		}
		FnAuthDataProviderImpl connectionImpl = null;
		
		try {
			connectionImpl = new FnAuthDataProviderImpl();
			ObjectStore objStore = connectionImpl.getObjectStore();
			//requesting parameters information from JSON object
			JSONObject requestDetailsJsonObject = (JSONObject) taskDetails.get(EDRMServiceConstants.REQUEST_DETAILS);
			JSONObject documentDetailsJsonObject = (JSONObject) requestDetailsJsonObject
					.get(EDRMServiceConstants.EDRM_TASK);
			String objectId = (String)documentDetailsJsonObject.get(EDRMServiceConstants.PROPERTY_ID);
			JSONArray documentPropertiesJsonArray = (JSONArray) documentDetailsJsonObject
					.get(EDRMServiceConstants.EDRM_TASK_DETAILS);
			CustomObject myObject= Factory.CustomObject.fetchInstance(objStore, objectId, null);
			Properties properties = myObject.getProperties();
			//updating a custom object properties
			new DocumentServicesUtil().updateProperties(documentPropertiesJsonArray, null, properties);
			myObject.save(RefreshMode.NO_REFRESH);
		//success response from FileNet
		return new CommonServicesUtil().generateSuccessResponse(EDRMServiceConstants.SUCCESS_STATUS_CODE, EDRMServiceConstants.CUSTOMOBJECT_UPDATED_SUCCESS,
						null, null, myObject.get_Id().toString());
		} catch (EngineRuntimeException engineRuntimeException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.ENGINE_RUNTIME_EXCEPTION,
					engineRuntimeException.getExceptionCode().getErrorId(), engineRuntimeException.getMessage());
		} catch (JSONException jsonException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					EDRMServiceConstants.JSON_EXCEPTION, EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					jsonException.getMessage());
		} catch (ParseException parseException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					EDRMServiceConstants.PARSE_EXCEPTION, EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					parseException.getMessage());
		} catch (Exception jsonException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.GENERAL_EXCEPTION, EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					jsonException.getMessage());
		} finally {
			connectionImpl.doSignOff();
		}
	}
	/**
	 * Method is to delete Tasks
	 * @param Json Custom Object propertiesList
	 * @return Json response object
	 * @throws EDRMException
	 */
	@Override
	public JSONObject deleteTask(JSONObject propertiesList) throws EDRMException {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Start :: deleteTask ::  EDRMFolderServiceImpl");
		}
		FnAuthDataProviderImpl connectionImpl = null;
		try{
			connectionImpl = new FnAuthDataProviderImpl();
			ObjectStore objectStore = connectionImpl.getObjectStore();
			//requesting parameters information from JSON object
			JSONObject requestDetailsJsonObject = (JSONObject) propertiesList.get(EDRMServiceConstants.REQUEST_DETAILS);
			JSONObject taskJsonObject = (JSONObject) requestDetailsJsonObject.get(EDRMServiceConstants.EDRM_TASK);
			String customObjectID = (String) taskJsonObject.get(EDRMServiceConstants.TASKID);
			//deletion of custom object instance
			new FolderServicesUtil().deleteTask(objectStore, customObjectID);
			//success response from FileNet
			return new CommonServicesUtil().generateSuccessResponse(EDRMServiceConstants.SUCCESS_STATUS_CODE,
					EDRMServiceConstants.TASK_DELETED_SUCCESS, null, null, null);
		} catch (EngineRuntimeException engineRuntimeException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.TASK_DELETE_FAILED, engineRuntimeException.getExceptionCode().getErrorId(),
					engineRuntimeException.getMessage());
		} catch (JSONException jsonException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.DOCUMENT_JSON_FAILED, EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					jsonException.getMessage());
		} catch (Exception jsonException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.GENERAL_EXCEPTION, EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					jsonException.getMessage());
		}
	}
}
