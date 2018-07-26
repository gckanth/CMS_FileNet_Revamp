package com.cms.edrm.filenet.service.impl;

import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cms.edrm.filenet.constants.EDRMServiceConstants;
import com.cms.edrm.filenet.exception.EDRMException;
import com.cms.edrm.filenet.service.EDRMFolderService;
import com.cms.edrm.filenet.util.CommonServicesUtil;
import com.cms.edrm.filenet.util.FolderServicesUtil;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.property.Properties;
import com.filenet.api.util.Id;
/**
 * 
 * Class which implements the add and update folder services
 *
 */
public class EDRMFolderServiceImpl implements EDRMFolderService {
	static Logger LOGGER = LoggerFactory.getLogger(EDRMFolderServiceImpl.class);
	/**
	 * Method is to create folder
	 * @param Json Folder propertiesList
	 * @return Json response object
	 * @throws EDRMException
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public JSONObject createFolder(JSONObject propertiesList) throws EDRMException {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Start :: createFolder ::  EDRMFolderServiceImpl");
		}
		JSONObject responseObject = new JSONObject();
		FnAuthDataProviderImpl connectionImpl = null;
		try {
			connectionImpl = new FnAuthDataProviderImpl();
			ObjectStore objectStore = connectionImpl.getObjectStore();
			//requesting parameters information from JSON object
			JSONObject requestDetailsJsonObject = (JSONObject) propertiesList.get(EDRMServiceConstants.REQUEST_DETAILS);
			JSONObject folderDetailsJsonObject = (JSONObject) requestDetailsJsonObject.get(EDRMServiceConstants.FOLDER_DETAILS);
			String folderName = (String) folderDetailsJsonObject.get(EDRMServiceConstants.FOLDER_NAME);
			String folder_ID = (String) folderDetailsJsonObject.get(EDRMServiceConstants.FOLDER_ID);
			//searching for folder path using folder unique identifier
			String folderClassification = new FolderServicesUtil().uniqueFolderSearch(objectStore, folder_ID);
			JSONArray folderPropertiesJsonObject = (JSONArray) folderDetailsJsonObject.get(EDRMServiceConstants.FOLDER_PROPERTIES);
			String documentCreator = (String) folderDetailsJsonObject.get(EDRMServiceConstants.CREATOR);
			String activeDirectory = EDRMServiceConstants.ACTIVE_DIRECTORY;
			String documentDateCreated = (String) folderDetailsJsonObject.get(EDRMServiceConstants.DATE_CREATED);
			//folder creation in FileNet
			responseObject = new FolderServicesUtil().createFolderInFileNet(objectStore, folderName,
					folderClassification, folderPropertiesJsonObject, documentCreator+activeDirectory, documentDateCreated);
			String folderId = responseObject.getString(EDRMServiceConstants.PROPERTY_ID);
			Set keySet = folderDetailsJsonObject.keySet();
			Iterator iterator = keySet.iterator();
			while (iterator.hasNext()) {
				String keys = (String) iterator.next();
				if (keys.equals(EDRMServiceConstants.SECURITY)) {
					JSONArray securityJsonArray = (JSONArray) folderDetailsJsonObject.get(EDRMServiceConstants.SECURITY);
					for(int securityKey=0;securityKey<securityJsonArray.length();securityKey++) {
						JSONObject securityJsonObject = (JSONObject) securityJsonArray.get(securityKey);
						String userName = (String) securityJsonObject.get(EDRMServiceConstants.OBJECT_USERNAME);
						String privilege = (String) securityJsonObject.get(EDRMServiceConstants.OBJECT_PRIVILEGE);
						String privilegeType = (String) securityJsonObject.get(EDRMServiceConstants.OBJECT_PRIVILEGE_TYPE);
						Folder folder = Factory.Folder.fetchInstance(objectStore, new Id(folderId), null);
						//updating folder security
						folder = (Folder) new CommonServicesUtil().updateSecurity(folder, privilege.trim(), 
								userName+activeDirectory, EDRMServiceConstants.FOLDER, privilegeType.trim());
					}
				}
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("End :: createFolder ::  EDRMFolderServiceImpl");
			}
			//success response from FileNet
			return new CommonServicesUtil().generateSuccessResponse(
					responseObject.getString(EDRMServiceConstants.RESPONSE_CODE),
					responseObject.getString(EDRMServiceConstants.RESPONSE_MESSAGE), null, null, folderId);
		} catch (EngineRuntimeException engineRuntimeException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.FOLDER_CREATED_FAIL, engineRuntimeException.getExceptionCode().getErrorId(),
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
	/**
	 * Method is to update folder
	 * @param Json Folder propertiesList
	 * @return Json response object
	 * @throws EDRMException
	 */
	@Override
	public JSONObject updateFolder(JSONObject propertiesList) throws EDRMException {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Start :: updateFolder ::  EDRMFolderServiceImpl");
		}
		FnAuthDataProviderImpl connectionImpl = null;
		try{
			connectionImpl = new FnAuthDataProviderImpl();
			ObjectStore objectStore = connectionImpl.getObjectStore();
			//requesting parameters information from JSON object
			JSONObject requestDetailsJsonObject = (JSONObject) propertiesList.get(EDRMServiceConstants.REQUEST_DETAILS);
			JSONObject folderDetailsJsonObject = (JSONObject) requestDetailsJsonObject.get(EDRMServiceConstants.FOLDER_DETAILS);
			String folder_ID = (String) folderDetailsJsonObject.get(EDRMServiceConstants.FOLDER_ID);
			//searching for folder path using folder unique identifier
			String folderClassification = new FolderServicesUtil().uniqueFolderSearch(objectStore, folder_ID);
			JSONArray folderPropertiesJsonObject = (JSONArray) folderDetailsJsonObject.get(EDRMServiceConstants.FOLDER_PROPERTIES);

			Folder folder = Factory.Folder.fetchInstance(objectStore, folderClassification, null);
			Properties folderProperties = folder.getProperties();
			//updating folder properties
			folderProperties = new FolderServicesUtil().updateFolderProperties(folderPropertiesJsonObject, folderProperties);
			folder.save(RefreshMode.REFRESH);
			//success response from FileNet
			return new CommonServicesUtil().generateSuccessResponse(EDRMServiceConstants.SUCCESS_STATUS_CODE,
					EDRMServiceConstants.FOLDER_UPDATED_SUCCESS, null, null, 
					folder.get_Id().toString());

		} catch (EngineRuntimeException engineRuntimeException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.FOLDER_UPDATE_FAILED, engineRuntimeException.getExceptionCode().getErrorId(),
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
	/**
	 * Method is to delete folder
	 * @param Json Folder propertiesList
	 * @return Json response object
	 * @throws EDRMException
	 */
	@Override
	public JSONObject deleteFolder(JSONObject propertiesList) throws EDRMException {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Start :: deleteFolder ::  EDRMFolderServiceImpl");
		}
		FnAuthDataProviderImpl connectionImpl = null;
		try{
			connectionImpl = new FnAuthDataProviderImpl();
			ObjectStore objectStore = connectionImpl.getObjectStore();
			//requesting parameters information from JSON object
			JSONObject requestDetailsJsonObject = (JSONObject) propertiesList.get(EDRMServiceConstants.REQUEST_DETAILS);
			JSONObject folderDetailsJsonObject = (JSONObject) requestDetailsJsonObject.get(EDRMServiceConstants.FOLDER_DETAILS);
			String folderIDJsonObject = (String) folderDetailsJsonObject.get(EDRMServiceConstants.FOLDERID);
			//deletion of folder instance
			new FolderServicesUtil().deleteFolder(objectStore, folderIDJsonObject);
			//success response from FileNet
			return new CommonServicesUtil().generateSuccessResponse(EDRMServiceConstants.SUCCESS_STATUS_CODE,
					EDRMServiceConstants.FOLDER_DELETED_SUCCESS, null, null, null);
		} catch (EngineRuntimeException engineRuntimeException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.FOLDER_DELETE_FAILED, engineRuntimeException.getExceptionCode().getErrorId(),
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
	/**
	 * Method is to move/relocation folder
	 * @param Json Folder propertiesList
	 * @return Json response object
	 * @throws EDRMException
	 */
	@Override
	public JSONObject moveOrRelocationFolder(JSONObject propertiesList) throws EDRMException {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Start :: moveOrRelocationFolder ::  EDRMFolderServiceImpl");
		}
		FnAuthDataProviderImpl connectionImpl = null;
		try{
			connectionImpl = new FnAuthDataProviderImpl();
			ObjectStore objectStore = connectionImpl.getObjectStore();
			//requesting parameters information from JSON object
			JSONObject requestDetailsJsonObject = (JSONObject) propertiesList.get(EDRMServiceConstants.REQUEST_DETAILS);
			String fromFolderID = (String) requestDetailsJsonObject.get(EDRMServiceConstants.FROM_FOLDER);
			String toFolderID = (String) requestDetailsJsonObject.get(EDRMServiceConstants.TO_FOLDER);
			//moving (or) relocation of folder instance
			new FolderServicesUtil().moveFolder(objectStore, fromFolderID, toFolderID);
			//success response from FileNet
			return new CommonServicesUtil().generateSuccessResponse(EDRMServiceConstants.SUCCESS_STATUS_CODE,
					EDRMServiceConstants.FOLDER_MOVED_SUCCESS, null, null, null);
		} catch (EngineRuntimeException engineRuntimeException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.FOLDER_MOVED_FAILED, engineRuntimeException.getExceptionCode().getErrorId(),
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
