package com.cms.edrm.filenet.util;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cms.edrm.filenet.constants.EDRMServiceConstants;
import com.cms.edrm.filenet.exception.EDRMException;
import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.EventSet;
import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.CustomObject;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.IndependentlyPersistableObject;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.events.Event;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.property.Properties;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.security.AccessPermission;
import com.filenet.api.util.Id;
/**
 * 
 * Utility class for Common Services of Filenet Engine
 *
 */
public class CommonServicesUtil {
	static Logger logger = LoggerFactory.getLogger(CommonServicesUtil.class);
	static String objectIdentity = null;
	/*
	 * Method which provides security service
	 * @param InstanceObject (Document/CustomObject/Folder)
	 * @param Document ID
	 * @param Privilege Integer Value
	 * @param Username
	 * @param Type of Document 
	 * @param Type of Privilege 
	 * @return Document
	 * @throws EDRMException
	 */
	public IndependentlyPersistableObject updateSecurity(IndependentlyPersistableObject independentlyPersistableObject, String securityMask, String userName, String typeOfDocument, 
			String privilegeType) throws EngineRuntimeException, JSONException, EDRMException{
		JSONObject jsonObject = null;
		int securityEnableMask = getSecurityMaskValue(securityMask);
		if(typeOfDocument.endsWith(EDRMServiceConstants.DOCUMENT_MESG))
		{
			Document document = (Document) independentlyPersistableObject;
			//setting document permissions
			AccessPermissionList apl = setPermissions(document.get_Permissions(), userName, securityEnableMask, privilegeType);
			document.set_Permissions(apl);
			document.save(RefreshMode.REFRESH);
		}
		else if(typeOfDocument.endsWith(EDRMServiceConstants.DOCUMENT_OBJECT_CUSTOM_OBJECT))
		{
			CustomObject customObject = (CustomObject) independentlyPersistableObject;
			//setting custom object permissions
			AccessPermissionList apl = setPermissions(customObject.get_Permissions(), userName, securityEnableMask, privilegeType);
			customObject.set_Permissions(apl);
			customObject.save(RefreshMode.REFRESH);
		}
		else if(typeOfDocument.endsWith(EDRMServiceConstants.FOLDER))
		{
			Folder folder = (Folder) independentlyPersistableObject;
			//setting folder permissions
			AccessPermissionList apl = setPermissions(folder.get_Permissions(), userName, securityEnableMask, privilegeType);
			folder.set_Permissions(apl);
			folder.save(RefreshMode.REFRESH);
		}
		jsonObject = new JSONObject();
		jsonObject.put(EDRMServiceConstants.RESPONSE_CODE, EDRMServiceConstants.SUCCESS_STATUS_CODE);
		jsonObject.put(EDRMServiceConstants.RESPONSE_MESSAGE, EDRMServiceConstants.DOCUMENT_ACL_SUCCESS);
		return independentlyPersistableObject;
	}
	/*
	 * This method is to Setting permissions
	 * @param folderPermissions
	 * @param username
	 * @param securityMask
	 * @param privilegeType
	 * @return AccessPermissionList
	 * @throw EDRMException
	 * @throw JSONException
	 * @throw EngineRuntimeException
	 */
	@SuppressWarnings("unchecked")
	public AccessPermissionList setPermissions(AccessPermissionList acl, String userName, Integer securityMask, 
			String privilegeType) throws EngineRuntimeException, JSONException, EDRMException {
		AccessPermission permission = Factory.AccessPermission.createInstance();
		permission.set_GranteeName(userName);
		if(privilegeType.equalsIgnoreCase(EDRMServiceConstants.DOCUMENT_OBJECT_ALLOW)){
			permission.set_AccessType(AccessType.ALLOW);
		}else if(privilegeType.equalsIgnoreCase(EDRMServiceConstants.DOCUMENT_OBJECT_DENY)){
			permission.set_AccessType(AccessType.DENY);
		}
		permission.set_AccessMask(securityMask);
		acl.add(permission);

		return acl;
	}
	/*
	 * This method is to searchCriteria Method
	 * @param ObjectStore
	 * @param JSONArray(search criteria)
	 * @param document class
	 * @return JSONArray
	 */
	@SuppressWarnings("rawtypes")
	public JSONArray searchCriteria(ObjectStore objectStore, JSONArray searchCriteria, String documentClass) throws EngineRuntimeException, JSONException, EDRMException, ParseException{
		JSONObject jsonObject = null;
		JSONArray jsonArray = new JSONArray();
		StringBuffer whereClause = new StringBuffer();
		//build query dynamically
		whereClause = buildSearchCriteria(whereClause, searchCriteria);
		SearchSQL searchSql = new SearchSQL();
		searchSql.setFromClauseInitialValue(documentClass.trim(), null, false);
		if (whereClause.toString().trim().length() > 0) {
			searchSql.setWhereClause(whereClause.toString());
		}
		if (logger.isInfoEnabled()) {
			logger.info("  SearchDocument method : searchSql : " + searchSql.toString());
		}
		SearchScope searchScope = new SearchScope(objectStore);
		//fetch document objects
		DocumentSet fetchObjects = (DocumentSet) searchScope.fetchObjects(searchSql, null, null, false);
		Iterator iterator = fetchObjects.iterator();
		while (iterator.hasNext()) {
			Document documentInfo = (Document) iterator.next();
			jsonObject = new JSONObject();
			jsonObject.put(EDRMServiceConstants.DOCUMENT_ID, documentInfo.get_Id().toString());
			jsonArray.put(jsonObject);
		}
		return jsonArray;
	}
	/*
	 * This method is to Build Search Criteria 
	 * @param StringBuilder Object
	 * @param SearchCriteria
	 * @return StringBuffer
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throws ParseException
	 * @throw EDRMException
	 */
	public StringBuffer buildSearchCriteria(StringBuffer whereClause, JSONArray searchCriteria) throws EngineRuntimeException, JSONException, EDRMException, ParseException{
		for(int i=0;i<searchCriteria.length();i++)
		{
			JSONObject jsonArrayObject = (JSONObject) searchCriteria.get(i);
			String propertyName = jsonArrayObject.getString(EDRMServiceConstants.DOCUMENT_OBJECT_PROPERTY_TYPE);
			String propertyValue = jsonArrayObject.getString(EDRMServiceConstants.OBJECT_VALUE);
			String dataType = jsonArrayObject.getString(EDRMServiceConstants.DOCUMENT_OBJECT_DATA_TYPE);
			if( whereClause.toString().trim().length() > 0) {
				whereClause.append(" AND ");
			}
			//build query dynamically
			if(dataType.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_STRING)) {
				whereClause = buildStringTypeQuery(propertyName.trim(), propertyValue.trim(), whereClause);
			}
			else if(dataType.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_INTEGER) || dataType.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_FLOAT)){
				whereClause = buildIntegerFloatTypeQuery(propertyName.trim(), propertyValue.trim(), whereClause, dataType.trim());
			}
			else if(dataType.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_BOOLEAN)) {
				whereClause = buildBooleanTypeQuery(propertyName.trim(), propertyValue.trim(), whereClause);
			}
			else if(dataType.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_DATE_TIME)) {
				whereClause = buildDateTypeQuery(propertyName.trim(), propertyValue.trim(), whereClause);
			}
			else if(dataType.equalsIgnoreCase(EDRMServiceConstants.PROPERTY_ID) && propertyValue.startsWith(EDRMServiceConstants.FLOWER_BRACE_OPEN_LITERAL) && propertyValue.endsWith(EDRMServiceConstants.FLOWER_BRACE_CLOSE_LITERAL)){
				whereClause = buildQuery(propertyName.trim(), propertyValue.trim(), whereClause); 
			}
		}
		return whereClause;
	}
	/*
	 * This method is to Build String Type Query
	 * @param propName
	 * @param propValue
	 * @param whereClause
	 * @return StringBuffer
	 */
	public StringBuffer buildStringTypeQuery(String name, String propValue, StringBuffer whereClause){

		if(logger.isDebugEnabled()){
			logger.debug(" Entered : buildStringTypeSearchCriteria ");
		}
		if(propValue.length()==0 || propValue==null || name.length()==0 || name==null)
		{
			whereClause = whereClause.append(EDRMServiceConstants.DOCUMENT_NO_METADATA);
		}
		else {
			if(propValue.contains("'")) {
				propValue= propValue.replace("'", "''");
			}
			whereClause.append(name);
			whereClause.append(" =");
			whereClause.append(" '").append(propValue).append("'");
		}
		return whereClause;
	}
	/*
	 * This method is to Build Integer Float Type Query
	 * @param propName
	 * @param propValue
	 * @param whereClause
	 * @return StringBuffer
	 */
	public StringBuffer buildIntegerFloatTypeQuery(String name, String propValue, StringBuffer whereClause, String dataType) {
		if(logger.isDebugEnabled()){
			logger.debug(" Entered : buildIntegerFloatDoubleTypeSearchCriteria ");
		}
		if(propValue.length()==0 || propValue==null || name.length()==0 || name==null)
		{
			whereClause = whereClause.append(EDRMServiceConstants.DOCUMENT_NO_METADATA);
		}
		else
		{
			whereClause.append(name);
			whereClause.append(" =");
			whereClause.append(" ").append(propValue);
		}
		return whereClause ;
	}
	/*
	 * This method is to Build Boolean Type Query
	 * @param propName
	 * @param propValue
	 * @param whereClause
	 * @return StringBuffer
	 */
	public StringBuffer buildBooleanTypeQuery(String name, String propValue, StringBuffer whereClause)
	{ 
		if(logger.isDebugEnabled()){
			logger.debug(" Entered : buildBooleanTypeSearchCriteria ");
		}
		whereClause.append(name);
		whereClause.append(EDRMServiceConstants.EQUAL_EXPRESSION);
		whereClause.append(" ").append(propValue);
		return whereClause;
	}
	/*
	 * This method is to Build Date Type Query
	 * @param propName
	 * @param propValue
	 * @param whereClause
	 * @return StringBuffer
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throws EDRMException
	 * @throws ParseException
	 */
	public StringBuffer buildDateTypeQuery(String propName, String propValue, StringBuffer whereClause) throws EngineRuntimeException, JSONException, EDRMException, ParseException 
	{ 
		if(logger.isDebugEnabled()){
			logger.debug(" Entered : buildDateTypeSearchCriteria ");
		}
		DocumentServicesUtil documentServicesUtil = new DocumentServicesUtil();
		if(propValue.length()==0 || propValue==null || propName.length()==0 || propName==null)
		{
			whereClause = whereClause.append(EDRMServiceConstants.DOCUMENT_NO_METADATA);
		}
		else
		{
			//converting normal date to ContentEngine date
			propValue = documentServicesUtil.ceDateFormatConvertion(propValue);
			whereClause.append(propName);
			whereClause.append(EDRMServiceConstants.GREATER_THAN_EXPRESSION);
			whereClause.append(" ").append(propValue);
		}
		return whereClause;
	}
	/*
	 * This method is to Parsing String to Date
	 * @param dateString
	 * @return Date
	 * @throw ParseException
	 */
	public Date getDate(String date) throws ParseException{
		return new SimpleDateFormat("MM/dd/yyyy, HH:mm a").parse(date);
	}
	/*
	 * This Method to create Custom Object instance
	 * @param ObjectStore objectStore
	 * @param Document properties
	 * @param Document Class Name
	 * @param Document Content Path
	 * @param String MimeType
	 * @param String documentCreator
	 * @param String documentDateCreated
	 * @return jsonObject/exceptionJsonObject 
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throws EDRMException
	 */
	public CustomObject createCustomObject(ObjectStore objStore, JSONArray customObjectProperties, String customObjectName, 
			String folderName,String documentCreator,String documentDateCreated)throws EngineRuntimeException, JSONException, EDRMException, FileNotFoundException, ParseException{
		CustomObject customObject = null;
		JSONObject jsonObject = null;
		customObject = Factory.CustomObject.createInstance(objStore, customObjectName);
		customObject.set_Creator(documentCreator);
		customObject.set_DateCreated(new CommonServicesUtil().getDate(documentDateCreated));
		Properties properties = customObject.getProperties();
		DocumentServicesUtil servicesUtil = new DocumentServicesUtil();
		//updating properties
		servicesUtil.updateProperties(customObjectProperties,null, properties);
		customObject.save(RefreshMode.REFRESH);
		IndependentlyPersistableObject independentObject = customObject;
		//filing custom object to folder
		independentObject = servicesUtil.fileToFolder(objStore, folderName, independentObject);
		CustomObject customObj = (CustomObject) independentObject;
		jsonObject = new JSONObject();
		jsonObject.put(EDRMServiceConstants.OBJECT_ID, customObj.get_Id().toString());
		jsonObject.put(EDRMServiceConstants.STATUS_CODE, EDRMServiceConstants.SUCCESS_STATUS_CODE);

		return customObj;
	}
	/* This method is to provides update document properties
	 * @param Document ID
	 * @param Document properties
	 * @param Document Class Name
	 * @param Document Content Path
	 * @param Document Last Modifier
	 * @param Last Date Modified
	 * @return JSONArray
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throws EDRMException
	 */
	public JSONObject updateCustomObject(ObjectStore objStore, JSONArray customObjectProperties, String customObjectId, 
			String folderName) throws EngineRuntimeException, JSONException, EDRMException, FileNotFoundException, ParseException {
		CustomObject customObject = null;
		JSONObject jsonObject = new JSONObject();
		customObject = Factory.CustomObject.fetchInstance(objStore, new Id(customObjectId), null);
		Properties properties = customObject.getProperties();
		DocumentServicesUtil servicesUtil = new DocumentServicesUtil();
		//updating custom object properties
		servicesUtil.updateProperties(customObjectProperties,null, properties);
		customObject.save(RefreshMode.REFRESH);
		jsonObject.put(EDRMServiceConstants.RESPONSE_CODE, EDRMServiceConstants.SUCCESS_STATUS_CODE);
		jsonObject.put(EDRMServiceConstants.RESPONSE_MESSAGE, EDRMServiceConstants.CUSTOMOBJECT_UPDATED_SUCCESS);
		return jsonObject;
	}
	/*
	 * This method is to Build Other Type Query
	 * @param propName
	 * @param propValue
	 * @param whereClause
	 * @return StringBuffer
	 * @throws EDRMException
	 */
	public StringBuffer buildQuery(String name, String propValue, StringBuffer whereClause) throws EDRMException{ 
		if(logger.isDebugEnabled()){
			logger.debug(" Entered : buildOtherTypesSearchCriteria : Name: "+name+" propValue : "+propValue);
		}
		whereClause.append(name);
		whereClause.append(" =");
		whereClause.append(" (").append(propValue).append(")");
		return whereClause;
	}
	/*
	 * Method with arguments to throw the response code, response message, documentID and recordID
	 * @param responseCode
	 * @param responseMessage
	 * @param exceptionCode
	 * @param exceptionMessage
	 * @return JSONObject
	 */
	public JSONObject generateSuccessResponse(String respCode,String respMesg,String docVsId,JSONObject recId,String docId) throws JSONException,EDRMException
	{
		JSONObject respJson = new JSONObject();
		respJson.put(EDRMServiceConstants.RESPONSE_CODE, respCode);
		respJson.put(EDRMServiceConstants.RESPONSE_MESSAGE, respMesg);
		if(null != docVsId) {
			respJson.put(EDRMServiceConstants.DOCUMENT_VS_ID, docVsId);
		}
		if(null != recId) {
			respJson.put(EDRMServiceConstants.RECORD_ID, recId);
		}
		if(null != docId) {
			respJson.put(EDRMServiceConstants.PROPERTY_ID, docId);
		}
		return respJson;
	}
	/*
	 * Method is used for getting FileNet document audit
	 * @param objectStore
	 * @param recordId
	 * @return JSONObject
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throws EDRMException
	 * @throws ParseException
	 */
	@SuppressWarnings({ "unchecked" })
	public JSONObject getDocumentRMAudit(ObjectStore objectStore, String recordId) throws EngineRuntimeException, JSONException, EDRMException, ParseException{
		JSONObject auditDetails = new JSONObject();
		Document document=Factory.Document.fetchInstance(objectStore, new Id(recordId),null);
		//getting FileNet audit events
		EventSet eventSet = document.get_AuditedEvents();
		if (!eventSet.isEmpty()) {
			Iterator<Event> itEvent = eventSet.iterator();
			JSONArray allEvents = new JSONArray();
			while(itEvent.hasNext()){
				JSONObject eventDetails = new JSONObject();
				Event event = itEvent.next();
				eventDetails.put(EDRMServiceConstants.EVENT_NAME, event.getClassName());
				if(event.get_EventStatus() == 0)
					eventDetails.put(EDRMServiceConstants.EVENT_STATUS,EDRMServiceConstants.SUCCESS);
				else
					eventDetails.put(EDRMServiceConstants.EVENT_STATUS,EDRMServiceConstants.FAIL);

				eventDetails.put(EDRMServiceConstants.INITIATING_USER,event.get_InitiatingUser());
				eventDetails.put(EDRMServiceConstants.EVENT_DATE,event.get_DateCreated().toString());
				eventDetails.put(EDRMServiceConstants.LAST_MODIFIER,event.getProperties().getStringValue(EDRMServiceConstants.LAST_MODIFIER));
				allEvents.put(eventDetails);
			}
			auditDetails.put(EDRMServiceConstants.EVENTS, allEvents);
		}
		return auditDetails;
	}
	/*
	 * This method is to get security mask equivalent string value from property file
	 * @param securityMask
	 * @return String
	 * @throws EDRMException
	 */
	public String getSecurityMaskString(int securityMask)throws EDRMException {
		String securityEnableMask = null;
		if(securityMask == Integer.parseInt(EDRMServiceConstants.SECURITY_FULL_CONTROL_MASK)) {
			securityEnableMask = EDRMServiceConstants.SECURITY_FULL_CONTROL;
		} 
		else if(securityMask == Integer.parseInt(EDRMServiceConstants.SECURITY_CREATE_INSTANCE_MASK)) {
			securityEnableMask = EDRMServiceConstants.SECURITY_CREATE_INSTANCE;
		}
		else if(securityMask == Integer.parseInt(EDRMServiceConstants.SECURITY_DELETE_MASK)) {
			securityEnableMask = EDRMServiceConstants.SECURITY_DELETE;
		}
		else if(securityMask == Integer.parseInt(EDRMServiceConstants.SECURITY_LINK_DOCUMENT_ANNOTATE_MASK)) {
			securityEnableMask = EDRMServiceConstants.SECURITY_LINK_DOCUMENT_ANNOTATE;
		}
		else if(securityMask == Integer.parseInt(EDRMServiceConstants.SECURITY_MODIFY_ALL_PROPERTIES_MASK)) {
			securityEnableMask = EDRMServiceConstants.SECURITY_MODIFY_ALL_PROPERTIES;
		}
		else if(securityMask == Integer.parseInt(EDRMServiceConstants.SECURITY_VIEW_CONTENT_MASK)) {
			securityEnableMask = EDRMServiceConstants.SECURITY_VIEW_CONTENT;
		}
		else if(securityMask == Integer.parseInt(EDRMServiceConstants.SECURITY_VIEW_PROPERTIES_MASK)) {
			securityEnableMask = EDRMServiceConstants.SECURITY_VIEW_PROPERTIES;
		}
		return securityEnableMask;
	}
	/*
	 * This method is to get security mask value from property file
	 * @param securityMask
	 * @return Integer
	 * @throws EDRMException
	 */
	public Integer getSecurityMaskValue(String securityMask)throws EDRMException {
		int securityEnableMask = 0;
		if(securityMask.equalsIgnoreCase(EDRMServiceConstants.SECURITY_FULL_CONTROL)) {
			securityEnableMask = Integer.parseInt(EDRMServiceConstants.SECURITY_FULL_CONTROL_MASK);
		} 
		else if(securityMask.equalsIgnoreCase(EDRMServiceConstants.SECURITY_CREATE_INSTANCE)) {
			securityEnableMask = Integer.parseInt(EDRMServiceConstants.SECURITY_CREATE_INSTANCE_MASK);
		}
		else if(securityMask.equalsIgnoreCase(EDRMServiceConstants.SECURITY_DELETE)) {
			securityEnableMask = Integer.parseInt(EDRMServiceConstants.SECURITY_DELETE_MASK);
		}
		else if(securityMask.equalsIgnoreCase(EDRMServiceConstants.SECURITY_LINK_DOCUMENT_ANNOTATE)) {
			securityEnableMask = Integer.parseInt(EDRMServiceConstants.SECURITY_LINK_DOCUMENT_ANNOTATE_MASK);
		}
		else if(securityMask.equalsIgnoreCase(EDRMServiceConstants.SECURITY_MODIFY_ALL_PROPERTIES)) {
			securityEnableMask = Integer.parseInt(EDRMServiceConstants.SECURITY_MODIFY_ALL_PROPERTIES_MASK);
		}
		else if(securityMask.equalsIgnoreCase(EDRMServiceConstants.SECURITY_VIEW_CONTENT)) {
			securityEnableMask = Integer.parseInt(EDRMServiceConstants.SECURITY_VIEW_CONTENT_MASK);
		}
		else if(securityMask.equalsIgnoreCase(EDRMServiceConstants.SECURITY_VIEW_PROPERTIES)) {
			securityEnableMask = Integer.parseInt(EDRMServiceConstants.SECURITY_VIEW_PROPERTIES_MASK);
		}
		return securityEnableMask;
	}
}
