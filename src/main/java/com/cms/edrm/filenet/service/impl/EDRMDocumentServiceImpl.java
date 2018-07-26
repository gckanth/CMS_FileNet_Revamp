package com.cms.edrm.filenet.service.impl;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cms.edrm.filenet.client.CMSDeclareRecordClient;
import com.cms.edrm.filenet.constants.EDRMServiceConstants;
import com.cms.edrm.filenet.exception.EDRMException;
import com.cms.edrm.filenet.service.EDRMDocumentService;
import com.cms.edrm.filenet.util.CommonServicesUtil;
import com.cms.edrm.filenet.util.DocumentServicesUtil;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.util.Id;

/**
 * 
 * Class which implements the add and update document services
 *
 */
public class EDRMDocumentServiceImpl implements EDRMDocumentService {
	private static final Logger LOGGER = LoggerFactory.getLogger(EDRMDocumentServiceImpl.class);
	/**
	 * Method which provides Add document service
	 * @param Json Document propertiesList
	 * @return Json response object
	 * @throws EDRMException
	 * @throws FileNotFoundException
	 * @throws ParseException
	 * @throws JSONException
	 */
	@SuppressWarnings({ "unused", "rawtypes" })
	@Override
	public JSONObject addDocument(JSONObject jsonObject) throws EDRMException {
		if (LOGGER.isDebugEnabled()) 
		{
			LOGGER.debug("Start :: addDocument ::  EDRMDocumentServiceImpl");
		}
		JSONObject recordResultObject = null;
		FnAuthDataProviderImpl connectionImpl = null;
		Document document = null;
		JSONObject declareRecordClientMethod = null;
		CommonServicesUtil commonServicesUtil = new CommonServicesUtil();
		DocumentServicesUtil documentServicesUtil = new DocumentServicesUtil();
		try {
			connectionImpl = new FnAuthDataProviderImpl();
			ObjectStore objStore = connectionImpl.getObjectStore();
			//requesting parameters information from JSON object
			JSONObject requestDetailsJsonObject = (JSONObject) jsonObject.get(EDRMServiceConstants.REQUEST_DETAILS);
			JSONObject documentDetailsJsonObject = (JSONObject) requestDetailsJsonObject.get(EDRMServiceConstants.DOCUMENT_MESG);
			
			String isRecord = (String) requestDetailsJsonObject.get(EDRMServiceConstants.DOCUMENT_IS_RECORD);
			String isVital = (String) requestDetailsJsonObject.get(EDRMServiceConstants.IS_VITAL);
			String documentClassName = (String) documentDetailsJsonObject.get(EDRMServiceConstants.DOCUMENT_CLASS);
			JSONArray documentPropertiesJsonObject = (JSONArray) documentDetailsJsonObject.get(EDRMServiceConstants.DOCUMENT_PROPERTIES);
			String filepathJsonObject = (String) documentDetailsJsonObject.get(EDRMServiceConstants.DOCUMENT_FILEPATH);
			String mimeTypeJsonObject = (String) documentDetailsJsonObject.get(EDRMServiceConstants.DOCUMENT_OBJECT_MIME_TYPE);
			String documentCreator = (String) documentDetailsJsonObject.get(EDRMServiceConstants.CREATOR);
			String documentDateCreated = (String) documentDetailsJsonObject.get(EDRMServiceConstants.DATE_CREATED);
			String folderId = (String) documentDetailsJsonObject.get(EDRMServiceConstants.FOLDER_ID);
			
			recordResultObject = new JSONObject();
			//creating document instance
			document = documentServicesUtil.createDocumentInstance(objStore, documentPropertiesJsonObject,
					documentClassName, filepathJsonObject, mimeTypeJsonObject, documentCreator, documentDateCreated, folderId);
			
			Set keySet = documentDetailsJsonObject.keySet();
			Iterator iterator = keySet.iterator();
			while(iterator.hasNext())
			{
				String keys = (String) iterator.next();
				if(keys.equals(EDRMServiceConstants.SECURITY))
				{
					String activeDirectory = EDRMServiceConstants.ACTIVE_DIRECTORY;
					JSONArray securityJsonArray = (JSONArray) documentDetailsJsonObject.get(EDRMServiceConstants.SECURITY);
					for(int securityKey=0;securityKey<securityJsonArray.length();securityKey++) {
					JSONObject securityJsonObject = (JSONObject) securityJsonArray.get(securityKey);
					String userName = (String) securityJsonObject.get(EDRMServiceConstants.OBJECT_USERNAME);
					String privilege = (String) securityJsonObject.get(EDRMServiceConstants.OBJECT_PRIVILEGE);
					String privilegeType = (String) securityJsonObject.get(EDRMServiceConstants.OBJECT_PRIVILEGE_TYPE);
					//updating document security
					document = (Document) commonServicesUtil.updateSecurity(document, privilege.trim(),
							userName+activeDirectory, EDRMServiceConstants.DOCUMENT_MESG, privilegeType.trim());
					}
				}
			}
			if(isRecord.equals(EDRMServiceConstants.PROPERTY_BOOLEAN_TRUE))
			{
				JSONObject jObject = new JSONObject();
				jObject.put(EDRMServiceConstants.REST_DOCUMENT_ID, document.get_Id().toString());
				jObject.put(EDRMServiceConstants.FOLDER_UNIQUE_IDENTIFIER, folderId);
				jObject.put(EDRMServiceConstants.IS_VITAL, isVital);
				jObject.put(EDRMServiceConstants.USER, documentCreator);
				String requestString = jObject.toString();
				CMSDeclareRecordClient cmsDeclareRecordClient = new CMSDeclareRecordClient();
				//declaring document as a Record
				declareRecordClientMethod = cmsDeclareRecordClient.declareRecordClientMethod(requestString);
			}
			//success response from FileNet
			return new CommonServicesUtil().generateSuccessResponse(EDRMServiceConstants.SUCCESS_STATUS_CODE, EDRMServiceConstants.DOCUMENT_COMMIT_SUCCESS,
						document.get_VersionSeries().get_Id().toString(), declareRecordClientMethod, document.get_Id().toString());
				
		} catch (EngineRuntimeException engineRuntimeException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.ENGINE_RUNTIME_EXCEPTION,
					engineRuntimeException.getExceptionCode().getErrorId(), engineRuntimeException.getMessage());
		} catch (JSONException jsonException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.JSON_EXCEPTION, EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					jsonException.getMessage());
		} catch (FileNotFoundException fileNotFoundException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.FILE_NOT_FOUND_EXCEPTION, EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					fileNotFoundException.getMessage());
		} catch (Exception exception) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.GENERAL_EXCEPTION, EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					exception.getMessage());
		} finally {
			connectionImpl.doSignOff();
		}
	}
	/**
	 * Method is to update the meta-data of a document
	 * @param Json Document propertiesList
	 * @return Json response object
	 * @throws EDRMException
	 * @throws fileNotFoundException
	 * @throws ParseException
	 * @throws JSONException
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public JSONObject updateDocument(JSONObject jsonObject) throws EDRMException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start :: updateDocument ::  EDRMDocumentServiceImpl");
		}
		JSONObject responseObject = new JSONObject();
		FnAuthDataProviderImpl connectionImpl = null;
		Document document = null;
		CommonServicesUtil commonServicesUtil = new CommonServicesUtil();
		DocumentServicesUtil documentServicesUtil = new DocumentServicesUtil();
		int count = 0;
		try {
			connectionImpl = new FnAuthDataProviderImpl();
			ObjectStore objStore = connectionImpl.getObjectStore();
			//requesting parameters information from JSON object
			JSONObject requestDetailsJsonObject = (JSONObject) jsonObject.get(EDRMServiceConstants.REQUEST_DETAILS);
			JSONObject documentDetailsJsonObject = (JSONObject) requestDetailsJsonObject
					.get(EDRMServiceConstants.DOCUMENT_MESG);
			String documentClassName = (String) documentDetailsJsonObject.get(EDRMServiceConstants.DOCUMENT_CLASS);
			JSONArray documentCriteriaJsonArray = (JSONArray) documentDetailsJsonObject
					.get(EDRMServiceConstants.DOCUMENT_SEARCH_CRITERIA);
			JSONArray documentPropertiesJsonArray = (JSONArray) documentDetailsJsonObject
					.get(EDRMServiceConstants.DOCUMENT_PROPERTIES);
			String filepathJsonObject = (String) documentDetailsJsonObject.get(EDRMServiceConstants.DOCUMENT_FILEPATH);
			String mimeTypeJsonObject = (String) documentDetailsJsonObject
					.get(EDRMServiceConstants.DOCUMENT_OBJECT_MIME_TYPE);
			String documentCreator = (String) documentDetailsJsonObject.get(EDRMServiceConstants.CREATOR);
			String documentDateCreated = (String) documentDetailsJsonObject
					.get(EDRMServiceConstants.DATE_CREATED);
			//searching a document instance criteria to update the document instance
			JSONArray resultCriteriaDocument = commonServicesUtil.searchCriteria(objStore, documentCriteriaJsonArray,
					documentClassName.trim());

			for (int i = 0; i < resultCriteriaDocument.length(); i++) {
				JSONObject jsonArrayObject = (JSONObject) resultCriteriaDocument.get(i);
				String documentID = jsonArrayObject.getString(EDRMServiceConstants.DOCUMENT_ID);
				//getting document version information
				boolean currentVersionStatus = documentServicesUtil.getCurrentVersion(objStore, documentID.trim());
				if (currentVersionStatus) {
					count++;
					//updating document instance
					responseObject = documentServicesUtil.updateDocumentInstance(objStore, documentID.trim(),
							documentPropertiesJsonArray, documentClassName.trim(), filepathJsonObject.trim(),
							mimeTypeJsonObject.trim(), documentCreator, documentDateCreated);
					
					Set keySet = documentDetailsJsonObject.keySet();
					Iterator iterator = keySet.iterator();
					while(iterator.hasNext())
					{
						String keys = (String) iterator.next();
						if(keys.equals(EDRMServiceConstants.SECURITY))
						{
							String activeDirectory = EDRMServiceConstants.ACTIVE_DIRECTORY;
							JSONArray securityJsonArray = (JSONArray) documentDetailsJsonObject.get(EDRMServiceConstants.SECURITY);
							for(int securityKey=0;securityKey<securityJsonArray.length();securityKey++){
								
							JSONObject securityJsonObject = (JSONObject) securityJsonArray.get(securityKey);
							String userName = (String) securityJsonObject.get(EDRMServiceConstants.OBJECT_USERNAME);
							String privilege = (String) securityJsonObject.get(EDRMServiceConstants.OBJECT_PRIVILEGE);
							String privilegeType = (String) securityJsonObject.get(EDRMServiceConstants.OBJECT_PRIVILEGE_TYPE);
							
							document = Factory.Document.fetchInstance(objStore, new Id(responseObject.getString(EDRMServiceConstants.DOCUMENT_ID)), null);
							//updating document security
							document = (Document) commonServicesUtil.updateSecurity(document, privilege.trim(),
									userName+activeDirectory, EDRMServiceConstants.DOCUMENT_MESG, privilegeType.trim());
							}
							//success response from FileNet
							return new CommonServicesUtil().generateSuccessResponse(EDRMServiceConstants.SUCCESS_STATUS_CODE, EDRMServiceConstants.DOCUMENT_PROPERTIES_UPDATED,
									null, null, document.get_Id().toString());
						}
					}
				}
			}
			if (count == 0) {
				return new EDRMException().generateExceptionDetails(
						EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
						EDRMServiceConstants.NO_SEARCH_RESULTS_FOUND, EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
						EDRMServiceConstants.NO_SEARCH_RESULTS_EXCEPTION_MESSAGE);
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("End :: updateDocument ::  EDRMDocumentServiceImpl");
			}
			//success response from FileNet
			return new CommonServicesUtil().generateSuccessResponse(EDRMServiceConstants.SUCCESS_STATUS_CODE, EDRMServiceConstants.DOCUMENT_PROPERTIES_UPDATED,
					null, null, responseObject.getString(EDRMServiceConstants.DOCUMENT_ID));
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
		} catch (ParseException parseException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					EDRMServiceConstants.PARSE_EXCEPTION, EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					parseException.getMessage());
		} finally {
			connectionImpl.doSignOff();
		}
	}
	/**
	 * Method is to document linking
	 * @param Json Document Link propertiesList
	 * @return Json response object
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throws Exception
	 */
	@Override
	public JSONObject documentLinking(JSONObject jsonObject) throws EDRMException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start :: documentLinking ::  EDRMDocumentServiceImpl");
		}
		FnAuthDataProviderImpl connectionImpl = new FnAuthDataProviderImpl();
		ObjectStore objStore = connectionImpl.getObjectStore();
		DocumentServicesUtil documentServicesUtil = new DocumentServicesUtil();
		JSONObject jObject = null;
		try {
			//requesting parameters information from JSON object
			JSONObject requestDetailsJsonObject = (JSONObject) jsonObject.get(EDRMServiceConstants.REQUEST_DETAILS);
			String parentDocumentVSID = (String) requestDetailsJsonObject
					.get(EDRMServiceConstants.PARENT_DOCUMENT_VSID);
			JSONArray childDocumentLinkDetailsArray = (JSONArray) requestDetailsJsonObject
					.get(EDRMServiceConstants.CHILD_DOCUMENT_LINK_DETAILS);
			String documentCreator = (String) requestDetailsJsonObject.get(EDRMServiceConstants.CREATOR);
			String activeDirectory = EDRMServiceConstants.ACTIVE_DIRECTORY;
			String documentDateCreated = (String) requestDetailsJsonObject.get(EDRMServiceConstants.DATE_CREATED);
			//creating of document linking
			JSONArray createLinkJson = documentServicesUtil.createDocumentLink(objStore, parentDocumentVSID,
					childDocumentLinkDetailsArray, documentCreator+activeDirectory, documentDateCreated);
			jObject = new JSONObject();
			jObject.put(EDRMServiceConstants.DOCUMENT_LINKS, createLinkJson);
			return jObject;
		} catch (EngineRuntimeException engineRuntimeException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.ENGINE_RUNTIME_EXCEPTION,
					engineRuntimeException.getExceptionCode().getErrorId(), engineRuntimeException.getMessage());
		} catch (JSONException jsonException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					EDRMServiceConstants.JSON_EXCEPTION, EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					jsonException.getMessage());
		} catch (Exception jsonException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					EDRMServiceConstants.GENERAL_EXCEPTION, EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					jsonException.getMessage());
		} finally {
			connectionImpl.doSignOff();
		}
	}
	/**
	 * Method is to add Document Class
	 * @param Json Document propertiesList
	 * @return Json response object
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throws Exception
	 */
	@Override
	public JSONObject createDocumentClass(JSONObject jsonObject) throws EDRMException {
		FnAuthDataProviderImpl connectionImpl = new FnAuthDataProviderImpl();
		ObjectStore objStore = connectionImpl.getObjectStore();
		DocumentServicesUtil documentServicesUtil = new DocumentServicesUtil();
		JSONObject resultObject = null;
		String responseCode = null;
		String responseMessage = null;
		try {
			//requesting parameters information from JSON object
			JSONObject requestDetailsJsonObject = (JSONObject) jsonObject.get(EDRMServiceConstants.REQUEST_DETAILS);
			JSONObject documentClassJsonObject = (JSONObject) requestDetailsJsonObject
					.get(EDRMServiceConstants.DOCUMENT_CLASS);
			String documentDisplayName = (String) documentClassJsonObject
					.get(EDRMServiceConstants.DOCUMENT_DISPLAY_NAME);
			String documentSymbolicName = (String) documentClassJsonObject
					.get(EDRMServiceConstants.DOCUMENT_SYMBOLIC_NAME);
			String documentParentClass = (String) documentClassJsonObject
					.get(EDRMServiceConstants.DOCUMENT_PARENT_CLASS);
			String documentCreator = (String) documentClassJsonObject.get(EDRMServiceConstants.CREATOR);
			String documentDateCreated = (String) documentClassJsonObject
					.get(EDRMServiceConstants.DATE_CREATED);
			String activeDirectory = EDRMServiceConstants.ACTIVE_DIRECTORY;
			//creating of document classes
			resultObject = documentServicesUtil.createDocumentClass(objStore, documentSymbolicName, documentDisplayName,
					documentParentClass, documentCreator+activeDirectory, documentDateCreated);
			responseCode = resultObject.getString(EDRMServiceConstants.RESPONSE_CODE);
			responseMessage = resultObject.getString(EDRMServiceConstants.RESPONSE_MESSAGE);
			//success response from FileNet
			return new CommonServicesUtil().generateSuccessResponse(responseCode, responseMessage, null, null, 
					resultObject.getString(EDRMServiceConstants.DOCUMENT_ID));
		} catch (EngineRuntimeException engineRuntimeException) {
			if (resultObject != null)
				return new EDRMException().generateExceptionDetails(responseCode,
						EDRMServiceConstants.DOCUMENT_CLASS_CREATED_SECURITY_FAILED,
						engineRuntimeException.getExceptionCode().getErrorId(), engineRuntimeException.getMessage());
			else
				return new EDRMException().generateExceptionDetails(
						EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
						EDRMServiceConstants.ENGINE_RUNTIME_EXCEPTION,
						engineRuntimeException.getExceptionCode().getErrorId(), engineRuntimeException.getMessage());
		} catch (JSONException jsonException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					EDRMServiceConstants.JSON_EXCEPTION, EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					jsonException.getMessage());
		} catch (Exception jsonException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					EDRMServiceConstants.GENERAL_EXCEPTION, EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					jsonException.getMessage());
		} finally {
			connectionImpl.doSignOff();
		}
	}
	/**
	 * Method is to delete document
	 * @param Json Document propertiesList
	 * @return Json response object
	 * @throws EDRMException
	 */
	@Override
	public JSONObject deleteDocument(JSONObject propertiesList) throws EDRMException {
		FnAuthDataProviderImpl connectionImpl = new FnAuthDataProviderImpl();
		ObjectStore objStore = connectionImpl.getObjectStore();
		DocumentServicesUtil documentServicesUtil = new DocumentServicesUtil();
		try {
			//requesting parameters information from JSON object
			JSONObject requestDetailsJsonObject = (JSONObject) propertiesList.get(EDRMServiceConstants.REQUEST_DETAILS);
			String documentIDJsonObject = (String) requestDetailsJsonObject.get(EDRMServiceConstants.DOCUMENT_ID);
			//deletion of document instance
			documentServicesUtil.deleteDocument(objStore, documentIDJsonObject);
			//success response from FileNet
			return new CommonServicesUtil().generateSuccessResponse(EDRMServiceConstants.SUCCESS_STATUS_CODE, EDRMServiceConstants.DOCUMENT_SUCCESSFULLY_DELETED, null, null, null);
		}
		catch (EngineRuntimeException engineRuntimeException) {
			return new EDRMException().generateExceptionDetails(
					EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.DOCUMENT_DELETE_FAILED,
					engineRuntimeException.getExceptionCode().getErrorId(), engineRuntimeException.getMessage());
		} catch (JSONException jsonException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					EDRMServiceConstants.JSON_EXCEPTION, EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					jsonException.getMessage());
		} catch (Exception jsonException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					EDRMServiceConstants.GENERAL_EXCEPTION, EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					jsonException.getMessage());
		} finally {
			connectionImpl.doSignOff();
		}
	}
	/**
	 * Method is to de linking of document
	 * @param Json Document Link propertiesList
	 * @return Json response object
	 * @throws EDRMException
	 */
	@Override
	public JSONObject deLinkDocument(JSONObject propertiesList) throws EDRMException {
		FnAuthDataProviderImpl connectionImpl = new FnAuthDataProviderImpl();
		ObjectStore objStore = connectionImpl.getObjectStore();
		DocumentServicesUtil documentServicesUtil = new DocumentServicesUtil();
		try {
			//requesting parameters information from JSON object
			JSONObject requestDetailsJsonObject = (JSONObject) propertiesList.get(EDRMServiceConstants.REQUEST_DETAILS);
			String linkIDJsonObject = (String) requestDetailsJsonObject.get(EDRMServiceConstants.LINK_ID);
			//de link of document 
			documentServicesUtil.deLinkDocument(objStore, linkIDJsonObject);
			//success response from FileNet
			return new CommonServicesUtil().generateSuccessResponse(EDRMServiceConstants.SUCCESS_STATUS_CODE, EDRMServiceConstants.DOCUMENT_DELINK_SUCCESS, null, null, null);
		}
		catch (EngineRuntimeException engineRuntimeException) {
			return new EDRMException().generateExceptionDetails(
					EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.DOCUMENT_DELINK_FAILED,
					engineRuntimeException.getExceptionCode().getErrorId(), engineRuntimeException.getMessage());
		} catch (JSONException jsonException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					EDRMServiceConstants.JSON_EXCEPTION, EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					jsonException.getMessage());
		} catch (Exception jsonException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					EDRMServiceConstants.GENERAL_EXCEPTION, EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					jsonException.getMessage());
		} finally {
			connectionImpl.doSignOff();
		}
	}
}
