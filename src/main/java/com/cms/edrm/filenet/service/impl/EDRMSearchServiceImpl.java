package com.cms.edrm.filenet.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cms.edrm.filenet.constants.EDRMServiceConstants;
import com.cms.edrm.filenet.exception.EDRMException;
import com.cms.edrm.filenet.service.EDRMSearchService;
import com.cms.edrm.filenet.util.FolderServicesUtil;
import com.cms.edrm.filenet.util.SearchServicesUtil;
import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;
/**
 * 
 * Class which implements the search services
 *
 */
public class EDRMSearchServiceImpl implements EDRMSearchService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EDRMSearchServiceImpl.class);
	/**
	 * Method for search a document classes by last modified date in FileNet Engine
	 * @param documentClassDetails
	 * @return JSONObject
	 * @throws EDRMException 
	 * @throws ParseException 
	 * @throws JSONException 
	 * @throws EngineRuntimeException 
	 * @throws IOException 
	 */
	@Override
	public JSONObject documentClassSearchByDate(JSONObject documentClassDetails) throws EDRMException, EngineRuntimeException, JSONException, ParseException, IOException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start :: documentClassSearchByDate ::  EDRMSearchServiceImpl");
		}
		FnAuthDataProviderImpl connectionImpl = new FnAuthDataProviderImpl();
		ObjectStore objStore = connectionImpl.getObjectStore();
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonChildDocumentArray = new JSONArray();
		try{
			//requesting parameters information from JSON object
			JSONObject requestDetailsJsonObject = (JSONObject) documentClassDetails.get(EDRMServiceConstants.REQUEST_DETAILS);
			String lastSyncDate = (String) requestDetailsJsonObject.get(EDRMServiceConstants.LAST_SYNC_DATE);
			//searching a document classes based on last modified date
			jsonChildDocumentArray = new SearchServicesUtil().searchDocumentClassByDate(lastSyncDate, objStore, EDRMServiceConstants.CMS_CHILD_CLASS, jsonChildDocumentArray);
			jsonObject.put(EDRMServiceConstants.CORRESPONDENCE_TYPES, jsonChildDocumentArray);
		}
		catch (EngineRuntimeException engineRuntimeException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.ENGINE_RUNTIME_EXCEPTION,
					engineRuntimeException.getExceptionCode().getErrorId(), engineRuntimeException.getMessage());
		} catch (JSONException jsonException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.JSON_EXCEPTION, EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					jsonException.getMessage());
		} catch (Exception exception) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.GENERAL_EXCEPTION, EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					exception.getMessage());
		} finally {
			connectionImpl.doSignOff();
		}
		return jsonObject;
	}
	/**
	 * Method for search a document instances by last modified date in FileNet Engine
	 * @param documentObjectDetails
	 * @return JSONObject
	 * @throws EDRMException 
	 * @throws ParseException 
	 * @throws JSONException 
	 * @throws EngineRuntimeException 
	 * @throws IOException 
	 */
	@Override
	public JSONObject documentInstanceSearchByDate(JSONObject documentObjectDetails)
			throws EDRMException, EngineRuntimeException, JSONException, ParseException, IOException {
		if (LOGGER.isDebugEnabled()) 
		{
			LOGGER.debug("Start :: documentInstanceSearchByDate ::  EDRMSearchServiceImpl");
		}
		FnAuthDataProviderImpl connectionImpl = new FnAuthDataProviderImpl();
		ObjectStore objStore = connectionImpl.getObjectStore();
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonDocumentArray = new JSONArray();
		try{
			//requesting parameters information from JSON object
			JSONObject requestDetailsJsonObject = (JSONObject) documentObjectDetails.get(EDRMServiceConstants.REQUEST_DETAILS);
			String lastSyncDate = (String) requestDetailsJsonObject.get(EDRMServiceConstants.LAST_SYNC_DATE);
			ClassDefinition classDefinition = Factory.ClassDefinition.fetchInstance(objStore, EDRMServiceConstants.CMS_DEMO_CLASS, null);
			//searching a document instances by last modfied date
			jsonDocumentArray = new SearchServicesUtil().searchDocumentInstanceByDate(lastSyncDate, objStore, classDefinition.get_SymbolicName(), jsonDocumentArray);
			jsonObject.put(EDRMServiceConstants.CORRESPONDENCE, jsonDocumentArray);
		}
		catch (EngineRuntimeException engineRuntimeException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.ENGINE_RUNTIME_EXCEPTION,
					engineRuntimeException.getExceptionCode().getErrorId(), engineRuntimeException.getMessage());
		} catch (JSONException jsonException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.JSON_EXCEPTION, EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					jsonException.getMessage());
		} catch (Exception exception) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.GENERAL_EXCEPTION, EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					exception.getMessage());
		} finally {
			connectionImpl.doSignOff();
		}
		return jsonObject;
	}
	/**
	 * Method for search a marking sets by last modified date in FileNet Engine
	 * @param markingSetDetails
	 * @return JSONObject
	 * @throws EDRMException 
	 * @throws ParseException 
	 * @throws JSONException 
	 * @throws EngineRuntimeException 
	 * @throws IOException 
	 */
	@Override
	public JSONObject markingSetSearchByDate(JSONObject markingSetDetails)
			throws EDRMException, EngineRuntimeException, JSONException, ParseException, IOException {
		if (LOGGER.isDebugEnabled()) 
		{
			LOGGER.debug("Start :: markingSetSearchByDate ::  EDRMSearchServiceImpl");
		}
		FnAuthDataProviderImpl connectionImpl = new FnAuthDataProviderImpl();
		Domain doGetDomain = connectionImpl.doGetDomain();
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonMarkingSetArray = new JSONArray();
		try{
			//requesting parameters information from JSON object
			JSONObject requestDetailsJsonObject = (JSONObject) markingSetDetails.get(EDRMServiceConstants.REQUEST_DETAILS);
			String lastSyncDate = (String) requestDetailsJsonObject.get(EDRMServiceConstants.LAST_SYNC_DATE);
			//searching a marking sets based on last modified date
			jsonMarkingSetArray = new SearchServicesUtil().searchMarkingSetByDate(doGetDomain, lastSyncDate, jsonMarkingSetArray);
			jsonObject.put(EDRMServiceConstants.MARKING_SETS, jsonMarkingSetArray);
		}catch (EngineRuntimeException engineRuntimeException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.ENGINE_RUNTIME_EXCEPTION,
					engineRuntimeException.getExceptionCode().getErrorId(), engineRuntimeException.getMessage());
		} catch (JSONException jsonException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.JSON_EXCEPTION, EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					jsonException.getMessage());
		} catch (Exception exception) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.GENERAL_EXCEPTION, EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					exception.getMessage());
		} finally {
			connectionImpl.doSignOff();
		}
		return jsonObject;
	}
	/**
	 * Method for getting data audit using document id in FileNet Engine
	 * @param dataAuditDetails
	 * @return JSONObject
	 * @throws EDRMException 
	 * @throws ParseException 
	 * @throws JSONException 
	 * @throws EngineRuntimeException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	@Override
	public JSONObject dataAuditedByDate(JSONObject dataAuditDetails)
			throws EDRMException, EngineRuntimeException, JSONException, ParseException, IOException, ClassNotFoundException, SQLException {
		
		if (LOGGER.isDebugEnabled()) 
		{
			LOGGER.debug("Start :: dataAuditedByDate ::  EDRMSearchServiceImpl");
		}
		FnAuthDataProviderImpl connectionImpl = new FnAuthDataProviderImpl();
		Connection sqlServerConnection = connectionImpl.getSqlServerConnection();
		ObjectStore objectStore = connectionImpl.getObjectStore();
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonMarkingSetArray = new JSONArray();
		try {
			//requesting parameters information from JSON object
			JSONObject requestDetailsJsonObject = (JSONObject) dataAuditDetails.get(EDRMServiceConstants.REQUEST_DETAILS);
			String documentID = (String) requestDetailsJsonObject.get(EDRMServiceConstants.PROPERTY_ID);
			//getting Minute sheet audit as well as FileNet audit based on document id
			jsonMarkingSetArray = new SearchServicesUtil().getAuditedData(sqlServerConnection, jsonMarkingSetArray, documentID, objectStore);
			jsonObject.put(EDRMServiceConstants.AUDIT_DATA, jsonMarkingSetArray);
		}
		catch (EngineRuntimeException engineRuntimeException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.ENGINE_RUNTIME_EXCEPTION,
					engineRuntimeException.getExceptionCode().getErrorId(), engineRuntimeException.getMessage());
		} catch (JSONException jsonException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.JSON_EXCEPTION, EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					jsonException.getMessage());
		} catch (Exception exception) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.GENERAL_EXCEPTION, EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					exception.getMessage());
		} finally {
			connectionImpl.doSignOff();
		}
		return jsonObject;
	}
	/**
	 * Method for search a folders by last modified date in FileNet Engine
	 * @param folderSearchDetails
	 * @return JSONObject
	 * @throws EDRMException 
	 * @throws ParseException 
	 * @throws JSONException 
	 * @throws EngineRuntimeException 
	 * @throws IOException 
	 */
	@Override
	public JSONObject folderSearchByDate(JSONObject folderSearchDetails)
			throws EDRMException, EngineRuntimeException, JSONException, ParseException, IOException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start :: folderSearchByDate ::  EDRMSearchServiceImpl");
		}
		FnAuthDataProviderImpl connectionImpl = new FnAuthDataProviderImpl();
		ObjectStore objStore = connectionImpl.getObjectStore();
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonChildFolderArray = new JSONArray();
		try{
			//requesting parameters information from JSON object
			JSONObject requestDetailsJsonObject = (JSONObject) folderSearchDetails.get(EDRMServiceConstants.REQUEST_DETAILS);
			String lastSyncDate = (String) requestDetailsJsonObject.get(EDRMServiceConstants.LAST_SYNC_DATE);
			//searching a new folders/modified folders based on last modified date
			jsonChildFolderArray = new FolderServicesUtil().searchFoldersByDate(lastSyncDate, objStore, jsonChildFolderArray);
			jsonObject.put(EDRMServiceConstants.FOLDERS, jsonChildFolderArray);
		} catch (EngineRuntimeException engineRuntimeException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.ENGINE_RUNTIME_EXCEPTION,
					engineRuntimeException.getExceptionCode().getErrorId(), engineRuntimeException.getMessage());
		} catch (JSONException jsonException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.JSON_EXCEPTION, EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					jsonException.getMessage());
		} catch (Exception exception) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.GENERAL_EXCEPTION, EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					exception.getMessage());
		} finally {
			connectionImpl.doSignOff();
		}
		return jsonObject;
	}
	/**
	 * Method for search a annotations by last modified date in FileNet Engine
	 * @param annotationsSearchDetails
	 * @return JSONObject
	 * @throws EDRMException 
	 * @throws ParseException 
	 * @throws JSONException 
	 * @throws EngineRuntimeException 
	 * @throws IOException 
	 */
	@Override
	public JSONObject annotationsSearchByDate(JSONObject annotationsSearchDetails)
			throws EDRMException, EngineRuntimeException, JSONException, ParseException, IOException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start :: annotationsSearchByDate ::  EDRMSearchServiceImpl");
		}
		FnAuthDataProviderImpl connectionImpl = new FnAuthDataProviderImpl();
		ObjectStore objStore = connectionImpl.getObjectStore();
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonAnnotationsArray = new JSONArray();
		try{
			//requesting parameters information from JSON object
			JSONObject requestDetailsJsonObject = (JSONObject) annotationsSearchDetails.get(EDRMServiceConstants.REQUEST_DETAILS);
			String lastSyncDate = (String) requestDetailsJsonObject.get(EDRMServiceConstants.LAST_SYNC_DATE);
			//searching annotations based on last modified date
			jsonAnnotationsArray = new SearchServicesUtil().searchAnnotationsByDate(lastSyncDate, objStore, jsonAnnotationsArray);
			jsonObject.put(EDRMServiceConstants.ANNOTATIONS, jsonAnnotationsArray);
		} catch (EngineRuntimeException engineRuntimeException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.ENGINE_RUNTIME_EXCEPTION,
					engineRuntimeException.getExceptionCode().getErrorId(), engineRuntimeException.getMessage());
		} catch (JSONException jsonException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.JSON_EXCEPTION, EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					jsonException.getMessage());
		} catch (Exception exception) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.GENERAL_EXCEPTION, EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					exception.getMessage());
		} finally {
			connectionImpl.doSignOff();
		}
		return jsonObject;
	}
}
