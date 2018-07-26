package com.cms.edrm.filenet.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cms.edrm.filenet.constants.EDRMServiceConstants;
import com.cms.edrm.filenet.exception.EDRMException;
import com.filenet.api.admin.ChoiceList;
import com.filenet.api.admin.DocumentClassDefinition;
import com.filenet.api.admin.PropertyDefinition;
import com.filenet.api.admin.PropertyDefinitionBoolean;
import com.filenet.api.admin.PropertyDefinitionDateTime;
import com.filenet.api.admin.PropertyDefinitionInteger32;
import com.filenet.api.admin.PropertyDefinitionString;
import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.collection.AnnotationSet;
import com.filenet.api.collection.ClassDescriptionSet;
import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.collection.MarkingList;
import com.filenet.api.collection.MarkingSetSet;
import com.filenet.api.collection.PropertyDefinitionList;
import com.filenet.api.collection.PropertyDescriptionList;
import com.filenet.api.constants.AccessType;
import com.filenet.api.core.Annotation;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.meta.ClassDescription;
import com.filenet.api.property.Properties;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.security.AccessPermission;
import com.filenet.api.security.Marking;
import com.filenet.api.security.MarkingSet;
import com.filenet.apiimpl.core.ContentTransferImpl;
import com.filenet.apiimpl.core.SubListImpl;
import com.filenet.apiimpl.property.PropertyEngineObjectListImpl;
/**
 * 
 * Utility class for Search Services of Filenet Engine
 *
 */
public class SearchServicesUtil {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchServicesUtil.class);
	/**
	 * Method for search a document classes by last modified date in FileNet Engine
	 * @param Date
	 * @param objectStore
	 * @param documentClassName
	 * @param childDocumentArray
	 * @return JSONArray
	 * @throws EDRMException 
	 * @throws ParseException 
	 * @throws JSONException 
	 * @throws EngineRuntimeException 
	 */
	@SuppressWarnings({ "rawtypes" })
	public JSONArray searchDocumentClassByDate(String date, ObjectStore objectStore, String documentClassName, JSONArray jsonChildDocumentArray) throws EngineRuntimeException, JSONException, EDRMException, ParseException {
		JSONObject jsonObject = null;
		JSONObject propertiesJsonObject = null;
		int searchCount = 0;
		StringBuffer whereClause = new StringBuffer();
		//build query dynamically
		whereClause = new CommonServicesUtil().buildDateTypeQuery(EDRMServiceConstants.DATE_LAST_MODIFIED, date, whereClause);
		if( whereClause.toString().trim().length() > 0) {
			 whereClause.append(" AND ");
		 }
		//build query dynamically
		whereClause = new CommonServicesUtil().buildStringTypeQuery(EDRMServiceConstants.DOCUMENT_PROPERTY_SYMBOLIC_NAME, documentClassName, whereClause);
		SearchSQL searchSql = new SearchSQL();
		searchSql.setFromClauseInitialValue(EDRMServiceConstants.DOCUMENT_CLASS_DEFINITION, null, true);
		if (whereClause.toString().trim().length() > 0) {
			searchSql.setWhereClause(whereClause.toString());
		}
		ClassDescription classDes = null;
		SearchScope searchScope = new SearchScope(objectStore);
		IndependentObjectSet repositoryRowSet = (IndependentObjectSet) searchScope.fetchObjects(searchSql, null, null, true);
		Iterator iterator = repositoryRowSet.iterator();
		while (iterator.hasNext()) {
			searchCount++;
			DocumentClassDefinition repRowProp = (DocumentClassDefinition) iterator.next();
			jsonObject = new JSONObject();
			JSONArray propArray = new JSONArray();
			PropertyDefinitionList get_PropertyDefinitions = repRowProp.get_PropertyDefinitions();
			Iterator itr = get_PropertyDefinitions.iterator();
			while(itr.hasNext()) {
				PropertyDefinition pdf = (PropertyDefinition)itr.next();
				propArray = propertyDefinition(pdf, propertiesJsonObject, propArray);
			}
			jsonObject.put(EDRMServiceConstants.PROPERTIES, propArray);
			jsonObject.put(EDRMServiceConstants.DOCUMENT_PARENT_CLASS, repRowProp.get_SuperclassDefinition().get_DisplayName());
			jsonObject.put(EDRMServiceConstants.DATE_CREATED, repRowProp.get_DateCreated());
			jsonObject.put(EDRMServiceConstants.DATE_LAST_MODIFIED, repRowProp.get_DateLastModified());
			jsonObject.put(EDRMServiceConstants.CREATOR, repRowProp.get_Creator());
			jsonObject.put(EDRMServiceConstants.CLASS_SYMBOLIC_NAME, repRowProp.get_SymbolicName());
			jsonObject.put(EDRMServiceConstants.CLASS_NAME, repRowProp.get_Name());
			jsonObject.put(EDRMServiceConstants.PROPERTY_ID, repRowProp.get_Id().toString());
			ClassDescription classDescription = Factory.ClassDescription.fetchInstance(objectStore, documentClassName, null);
			ClassDescriptionSet get_ImmediateSubclassDescriptions = classDescription.get_ImmediateSubclassDescriptions();
			Iterator it = get_ImmediateSubclassDescriptions.iterator();
			while(it.hasNext()){
				classDes = (ClassDescription) it.next();
				jsonChildDocumentArray = searchDocumentClassByDate(date, objectStore, classDes.get_SymbolicName(), jsonChildDocumentArray);
			}
			jsonChildDocumentArray.put(jsonObject);
		}
		if(searchCount==0) {
			ClassDescription classDescription = Factory.ClassDescription.fetchInstance(objectStore, documentClassName, null);
			ClassDescriptionSet get_ImmediateSubclassDescriptions = classDescription.get_ImmediateSubclassDescriptions();
			Iterator it = get_ImmediateSubclassDescriptions.iterator();
			while(it.hasNext()) {
				classDes = (ClassDescription) it.next();
				jsonChildDocumentArray = searchDocumentClassByDate(date, objectStore, classDes.get_SymbolicName(), jsonChildDocumentArray);
			}
		}
		return jsonChildDocumentArray;
	}
	/**
	 * Method for getting propertyDefinition information in FileNet Engine
	 * @param pdf
	 * @param propertiesJsonObject
	 * @param propArray
	 * @return JSONArray
	 * @throws EDRMException 
	 * @throws JSONException 
	 * @throws EngineRuntimeException 
	 */
	public JSONArray propertyDefinition(PropertyDefinition pdf,JSONObject propertiesJsonObject, JSONArray propArray) throws EngineRuntimeException, JSONException, EDRMException{
		if(pdf.getClass().toString().contains(EDRMServiceConstants.PROPERTY_DEFINITION_STRING_IMPL)){
			propertiesJsonObject = new JSONObject();
			PropertyDefinitionString templateString = (PropertyDefinitionString)pdf;
			if(!templateString.get_IsSystemOwned() && !templateString.get_IsHidden() && !templateString.get_IsNameProperty()&&templateString.get_CopyToReservation()&& !templateString.get_Name().equals("Publication Source")&&!templateString.get_Name().equals("PropertyTemplateCreation")){
			propertiesJsonObject.put(EDRMServiceConstants.NAME, templateString.get_Name());
			propertiesJsonObject.put(EDRMServiceConstants.DOCUMENT_PROPERTY_SYMBOLIC_NAME, templateString.get_SymbolicName());
			propertiesJsonObject.put(EDRMServiceConstants.PROPERTY_DATA_TYPE, templateString.get_DataType());
			propertiesJsonObject.put(EDRMServiceConstants.PROPERTY_CARDINALITY, templateString.get_Cardinality());
			propertiesJsonObject.put(EDRMServiceConstants.PROPERTY_MAX_LENGTH, templateString.get_MaximumLengthString());
			Boolean get_IsValueRequired = templateString.get_IsValueRequired();
			propertiesJsonObject.put(EDRMServiceConstants.PROPERTY_IS_REQUIRED, get_IsValueRequired.toString());
			ChoiceList get_ChoiceList = templateString.get_ChoiceList();
			if(get_ChoiceList!=null)
			propertiesJsonObject.put(EDRMServiceConstants.EDRM_LISTS_NAME, get_ChoiceList.get_DisplayName());
			MarkingSet get_MarkingSet = templateString.get_MarkingSet();
			if(get_MarkingSet!=null)
			propertiesJsonObject.put(EDRMServiceConstants.EDRM_LISTS_NAME, get_MarkingSet.get_DisplayName());
			propArray.put(propertiesJsonObject);
			}
		}
		if(pdf.getClass().toString().contains(EDRMServiceConstants.PROPERTY_DEFINITION_INTEGER_IMPL)){
			propertiesJsonObject = new JSONObject();
			PropertyDefinitionInteger32 templateString = (PropertyDefinitionInteger32)pdf;
			if(!templateString.get_IsSystemOwned() && !templateString.get_IsHidden() && !templateString.get_IsNameProperty()&&templateString.get_CopyToReservation()&& !templateString.get_Name().equals("Publication Source")&&!templateString.get_Name().equals("PropertyTemplateCreation")){
			propertiesJsonObject.put(EDRMServiceConstants.NAME, templateString.get_Name());
			propertiesJsonObject.put(EDRMServiceConstants.DOCUMENT_PROPERTY_SYMBOLIC_NAME, templateString.get_SymbolicName());
			propertiesJsonObject.put(EDRMServiceConstants.PROPERTY_DATA_TYPE, templateString.get_DataType());
			propertiesJsonObject.put(EDRMServiceConstants.PROPERTY_CARDINALITY, templateString.get_Cardinality());
			Boolean get_IsValueRequired = templateString.get_IsValueRequired();
			propertiesJsonObject.put(EDRMServiceConstants.PROPERTY_IS_REQUIRED, get_IsValueRequired.toString());
			propArray.put(propertiesJsonObject);
			}
		}
		if(pdf.getClass().toString().contains(EDRMServiceConstants.PROPERTY_DEFINITION_BOOLEAN_IMPL)){
			propertiesJsonObject = new JSONObject();
			PropertyDefinitionBoolean templateString = (PropertyDefinitionBoolean)pdf;
			if(!templateString.get_IsSystemOwned() && !templateString.get_IsHidden() && !templateString.get_IsNameProperty()&&templateString.get_CopyToReservation()&& !templateString.get_Name().equals("Publication Source")&&!templateString.get_Name().equals("PropertyTemplateCreation")){
			propertiesJsonObject.put(EDRMServiceConstants.NAME, templateString.get_Name());
			propertiesJsonObject.put(EDRMServiceConstants.DOCUMENT_PROPERTY_SYMBOLIC_NAME, templateString.get_SymbolicName());
			propertiesJsonObject.put(EDRMServiceConstants.PROPERTY_DATA_TYPE, templateString.get_DataType());
			propertiesJsonObject.put(EDRMServiceConstants.PROPERTY_CARDINALITY, templateString.get_Cardinality());
			Boolean get_IsValueRequired = templateString.get_IsValueRequired();
			propertiesJsonObject.put(EDRMServiceConstants.PROPERTY_IS_REQUIRED, get_IsValueRequired.toString());
			propArray.put(propertiesJsonObject);
			}
		}
		if(pdf.getClass().toString().contains(EDRMServiceConstants.PROPERTY_DEFINITION_DATE_TIME_IMPL)){
			propertiesJsonObject = new JSONObject();
			PropertyDefinitionDateTime templateString = (PropertyDefinitionDateTime)pdf;
			if(!templateString.get_IsSystemOwned() && !templateString.get_IsHidden() && !templateString.get_IsNameProperty()&&templateString.get_CopyToReservation()&& !templateString.get_Name().equals("Publication Source")&&!templateString.get_Name().equals("PropertyTemplateCreation")){
			propertiesJsonObject.put(EDRMServiceConstants.NAME, templateString.get_Name());
			propertiesJsonObject.put(EDRMServiceConstants.DOCUMENT_PROPERTY_SYMBOLIC_NAME, templateString.get_SymbolicName());
			propertiesJsonObject.put(EDRMServiceConstants.PROPERTY_DATA_TYPE, templateString.get_DataType());
			propertiesJsonObject.put(EDRMServiceConstants.PROPERTY_CARDINALITY, templateString.get_Cardinality());
			Boolean get_IsValueRequired = templateString.get_IsValueRequired();
			propertiesJsonObject.put(EDRMServiceConstants.PROPERTY_IS_REQUIRED, get_IsValueRequired.toString());
			propArray.put(propertiesJsonObject);
			}
		}
		return propArray;
	}
	/**
	 * Method for search a document instances by last modified date in FileNet Engine
	 * @param Date
	 * @param objectStore
	 * @param documentClassName
	 * @param childDocumentArray
	 * @return JSONArray
	 * @throws EDRMException 
	 * @throws ParseException 
	 * @throws JSONException 
	 * @throws EngineRuntimeException 
	 */
	@SuppressWarnings("rawtypes")
	public JSONArray searchDocumentInstanceByDate(String date, ObjectStore objStore, String cmsClass, JSONArray jsonChildDocumentArray) throws EngineRuntimeException, JSONException, EDRMException, ParseException {
		JSONObject jsonObject = null;
		ArrayList al = null;
		JSONObject propertiesJsonObject = null;
		StringBuffer whereClause = new StringBuffer();
		//build query dynamically
		whereClause = new CommonServicesUtil().buildDateTypeQuery(EDRMServiceConstants.DATE_LAST_MODIFIED, date, whereClause);
		SearchSQL searchSql = new SearchSQL();
		searchSql.setFromClauseInitialValue(cmsClass, null, true);
		if (whereClause.toString().trim().length() > 0) {
			searchSql.setWhereClause(whereClause.toString());
		}
		SearchScope searchScope = new SearchScope(objStore);
		//getting from document objects 
		DocumentSet repositoryRowSet = (DocumentSet) searchScope.fetchObjects(searchSql, null, null, true);
		Iterator iterator = repositoryRowSet.iterator();
		while (iterator.hasNext()) {
			jsonObject = new JSONObject();
			propertiesJsonObject = new JSONObject();
			Document document = (Document)iterator.next();
				PropertyDescriptionList get_PropertyDescriptions = document.get_ClassDescription().get_PropertyDescriptions();
				com.filenet.api.property.Properties prop = document.getProperties();
				//getting properties as a JSON
				propertiesJsonObject = new FolderServicesUtil().framePropertiesJSON(propertiesJsonObject, get_PropertyDescriptions, jsonObject, al, prop);
				  al = new ArrayList();
				  AccessPermissionList get_Permissions = document.get_Permissions();
				  al = getSecurityUserList(al, get_Permissions);
				  propertiesJsonObject.put(EDRMServiceConstants.CREATOR, document.get_Creator());
				  propertiesJsonObject.put(EDRMServiceConstants.DATE_CREATED, document.get_DateCreated());
				  propertiesJsonObject.put(EDRMServiceConstants.DATE_LAST_MODIFIED, document.get_DateLastModified());
				  propertiesJsonObject.put(EDRMServiceConstants.SECURITY, al);
				  propertiesJsonObject.put(EDRMServiceConstants.PROPERTY_ID, document.get_Id().toString());
				  jsonChildDocumentArray.put(propertiesJsonObject);
		}	
		return jsonChildDocumentArray;
	}
	/**
	 * This is common reusable Method for Security User List as a ArrayList Object in FileNet Engine 
	 * @param get_Permissions 
	 * @param arrayListOfObject 
	 * @return ArrayList 
	 * @throws EDRMException 
	 * @throws JSONException 
	 * @throws EngineRuntimeException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getSecurityUserList(ArrayList arrayListOfObject, AccessPermissionList get_Permissions) throws JSONException, EDRMException, EngineRuntimeException{
		  Iterator permissionIterator = get_Permissions.iterator();
		  JSONObject jsonObject = null;
		  while(permissionIterator.hasNext()) {
			  AccessPermission permission = (AccessPermission) permissionIterator.next();
			  jsonObject = new JSONObject();
			  jsonObject.put(EDRMServiceConstants.USER, permission.get_GranteeName());
			  jsonObject.put(EDRMServiceConstants.ACCESS_MASK, new CommonServicesUtil().getSecurityMaskString(permission.get_AccessMask()));
			  jsonObject.put(EDRMServiceConstants.ACCESS_TYPE, permission.get_AccessType());
			  arrayListOfObject.add(jsonObject);
		  }
		return arrayListOfObject;
	}
	/**
	 * Method for search a marking set by last modified date in FileNet Engine
	 * @param Domain
	 * @param date
	 * @param jsonMarkingSetArray
	 * @return JSONArray
	 * @throws EDRMException 
	 * @throws ParseException 
	 * @throws JSONException 
	 * @throws EngineRuntimeException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JSONArray searchMarkingSetByDate(Domain domain, String date, JSONArray jsonMarkingSetArray) throws EngineRuntimeException, JSONException, EDRMException, ParseException {
		// Getting Marking Sets
		MarkingSetSet get_MarkingSets = domain.get_MarkingSets();
		Iterator iterator = get_MarkingSets.iterator();
		JSONObject jsonObject = null;
		JSONObject jsonMarkingsObject = null;
		JSONObject jsonMarkings = null;
		Date d = new CommonServicesUtil().getDate(date);
		while(iterator.hasNext()) {
			MarkingSet ms = (MarkingSet)iterator.next();
			Date get_DateLastModified = ms.get_DateLastModified();
			//comparing marking set with date property
			if(d.compareTo(get_DateLastModified)<=0) {
			jsonObject = new JSONObject();
			jsonObject.put(EDRMServiceConstants.MARKING_SET_NAME, ms.get_DisplayName());
			MarkingList get_Markings = ms.get_Markings();
			Iterator markingIterator = get_Markings.iterator();
			ArrayList markingArrayList = new ArrayList();
			while(markingIterator.hasNext()) {
				jsonMarkingsObject = new JSONObject();
				Marking marking = (Marking)markingIterator.next();
				String get_MarkingValue = marking.get_MarkingValue();
				AccessPermissionList get_Permissions = marking.get_Permissions();
				Iterator get_PermissionsIterator = get_Permissions.iterator();
				ArrayList permissionArrayList = new ArrayList();
				while(get_PermissionsIterator.hasNext()) {
					jsonMarkings = new JSONObject();
					AccessPermission accessPermission = (AccessPermission)get_PermissionsIterator.next();
					String get_GranteeName = accessPermission.get_GranteeName();
					AccessType get_AccessType = accessPermission.get_AccessType();
					jsonMarkings.put(EDRMServiceConstants.MARKING_SET_USERNAME, get_GranteeName);
					permissionArrayList.add(jsonMarkings);
					jsonMarkingsObject.put(EDRMServiceConstants.MARKING_SET_PRIVILEGE, new CommonServicesUtil().getSecurityMaskString(marking.get_ConstraintMask()));
					jsonMarkingsObject.put(EDRMServiceConstants.MARKING_SET_PRIVILEGE_TYPE, get_AccessType);
				}
				jsonMarkingsObject.put(EDRMServiceConstants.MARKING_SET_USERS, permissionArrayList);
				jsonMarkingsObject.put(EDRMServiceConstants.OBJECT_VALUE, get_MarkingValue);
				markingArrayList.add(jsonMarkingsObject);
			}
			jsonObject.put(EDRMServiceConstants.CREATOR, ms.get_Creator());
			jsonObject.put(EDRMServiceConstants.DATE_CREATED, ms.get_DateCreated());
			jsonObject.put(EDRMServiceConstants.PROPERTY_ID, ms.get_Id().toString());
			jsonObject.put(EDRMServiceConstants.MARKING_SET_MARKING_VALUES, markingArrayList);
			jsonMarkingSetArray.put(jsonObject);
			}
		}
		return jsonMarkingSetArray;
	}
	/**
	 * Method for search audited data by last modified date from FileNet Engine and Custom MinuteSheet table
	 * @param sqlServerConnection
	 * @param jsonAuditSetArray
	 * @param date
	 * @return JSONArray
	 * @throws EDRMException 
	 * @throws ParseException 
	 * @throws JSONException 
	 * @throws EngineRuntimeException 
	 */
	public JSONArray getAuditedData(Connection sqlServerConnection, JSONArray jsonAuditSetArray, String guid, ObjectStore objectStore) throws SQLException, ParseException, JSONException, EngineRuntimeException, EDRMException {
		String sql = EDRMServiceConstants.SQL_MINUTESHEET_QUERY;
		JSONObject jsonAuditObject = null;
		Statement statement = sqlServerConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		JSONObject jsonObject = null;
		JSONArray jsonArray = new JSONArray();
		jsonAuditObject = new JSONObject();
		while(resultSet.next()) {
			jsonObject = new JSONObject();
			String id = resultSet.getString(EDRMServiceConstants.RECORD_GUID);
			if(id.equals(guid)) {
				jsonObject.put(EDRMServiceConstants.RECORD_SERIAL_NO, resultSet.getString(EDRMServiceConstants.RECORD_SERIAL_NO));
				jsonObject.put(EDRMServiceConstants.ACTION_TAKEN_BY, resultSet.getString(EDRMServiceConstants.ACTION_TAKEN_BY));
				jsonObject.put(EDRMServiceConstants.ACTION_DATE, resultSet.getString(EDRMServiceConstants.ACTION_DATE));
				jsonObject.put(EDRMServiceConstants.ACTION_TYPE, resultSet.getString(EDRMServiceConstants.ACTION_TYPE));
				jsonObject.put(EDRMServiceConstants.REMARKS, resultSet.getString(EDRMServiceConstants.REMARKS));
				jsonObject.put(EDRMServiceConstants.WOB_PROCESS_ID, resultSet.getString(EDRMServiceConstants.WOB_PROCESS_ID));
				jsonObject.put(EDRMServiceConstants.DESIGNATION, new FolderServicesUtil().getUserDesignation(sqlServerConnection, resultSet.getString(EDRMServiceConstants.ACTION_TAKEN_BY)));
				jsonObject.put(EDRMServiceConstants.RECORD_GUID, id);
				jsonArray.put(jsonObject);
			}
		}
		jsonObject = new JSONObject();
		jsonObject.put(EDRMServiceConstants.MINUTE_SHEET, jsonArray);
		jsonAuditObject.put(EDRMServiceConstants.MINUTE_SHEET_AUDIT, jsonObject);
		jsonAuditObject.put(EDRMServiceConstants.RM_AUDIT, new CommonServicesUtil().getDocumentRMAudit(objectStore, guid));
		jsonAuditSetArray.put(jsonAuditObject);
		return jsonAuditSetArray;
	}
	/**
	 * Method for search annotations by last modified date in FileNet Engine
	 * @param date
	 * @param objectStore
	 * @param jsonAnnotationsArray
	 * @return JSONArray
	 * @throws EDRMException 
	 * @throws ParseException 
	 * @throws JSONException 
	 * @throws EngineRuntimeException 
	 * @throws IOException 
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	public JSONArray searchAnnotationsByDate(String date, ObjectStore objStore, JSONArray jsonAnnotationsArray) throws EngineRuntimeException, JSONException, EDRMException, ParseException, IOException {
		JSONObject jsonObject = null;
		ArrayList al = null;
		JSONObject propertiesJsonObject = null;
		StringBuffer whereClause = new StringBuffer();
		//build query dynamically
		whereClause = new CommonServicesUtil().buildDateTypeQuery(EDRMServiceConstants.DATE_LAST_MODIFIED, date, whereClause);
		SearchSQL searchSql = new SearchSQL();
		searchSql.setFromClauseInitialValue(EDRMServiceConstants.ANNOTATION, null, true);
		if (whereClause.toString().trim().length() > 0) {
			searchSql.setWhereClause(whereClause.toString());
		}
		SearchScope searchScope = new SearchScope(objStore);
		//getting Annotation objects
		AnnotationSet repositoryRowSet = (AnnotationSet) searchScope.fetchObjects(searchSql, null, null, true);
		Iterator iterator = repositoryRowSet.iterator();
		while (iterator.hasNext()) {
			jsonObject = new JSONObject();
			propertiesJsonObject = new JSONObject();
			Annotation annotation = (Annotation)iterator.next();
			Document getAnnotatedDocument = (Document) annotation.get_AnnotatedObject();
			String annotatedDocumentID = getAnnotatedDocument.get_Id().toString();
			Properties properties = annotation.getProperties();
			PropertyEngineObjectListImpl impl = (PropertyEngineObjectListImpl) properties.get(EDRMServiceConstants.CONTENT_ELEMENTS);
			SubListImpl subListImpl = (SubListImpl) impl.getObjectValue();
			Iterator itr = subListImpl.iterator();
			while(itr.hasNext()) {
				ContentTransferImpl property = (ContentTransferImpl)itr.next();
				InputStream stream = property.accessContentStream();
				String readStr = "";
				int docLen = 1024;
				byte[] buf = new byte[docLen];
				int n = 1;
				while (n > 0) {
					n = stream.read(buf, 0, docLen);
					readStr = readStr + new String(buf);
					buf = new byte[docLen];
				}
				stream.close();
				String stickyNoteString = null;
				if(readStr.contains(EDRMServiceConstants.STICKY_NOTE)){
					StringTokenizer tokenizer = new StringTokenizer(readStr, " ");
					while (tokenizer.hasMoreTokens()) {
						String stickyNoteContent = tokenizer.nextToken();
						if(stickyNoteContent.contains(EDRMServiceConstants.STICKY_ENCODING)){
							stickyNoteContent = stickyNoteContent.replace(EDRMServiceConstants.STICKY_ENCODING, "");
							stickyNoteContent = stickyNoteContent.replace(EDRMServiceConstants.STICKY_ENCODING, "");
							stickyNoteContent = stickyNoteContent.replace(EDRMServiceConstants.STICKY_UNICODE, "");
							stickyNoteContent = stickyNoteContent.replace(EDRMServiceConstants.STICKY_F_TEXT, "");
							stickyNoteContent = stickyNoteContent.replace(EDRMServiceConstants.STICKY_PROP_DESC, "");
							stickyNoteContent = stickyNoteContent.replace(EDRMServiceConstants.STICKY_F_ANNO, "");
							
							byte[] bytes = new FolderServicesUtil().hexStringToByteArray(stickyNoteContent.trim());
							stickyNoteString = new String(bytes, StandardCharsets.UTF_8);
							
							stickyNoteString = stickyNoteString.replace(EDRMServiceConstants.STICKY_REPLACER, "");
							
							jsonObject.put(EDRMServiceConstants.CREATOR, annotation.get_Creator());
							jsonObject.put(EDRMServiceConstants.PROPERTY_ID, annotation.get_Id().toString());
							jsonObject.put(EDRMServiceConstants.DATE_CREATED, annotation.get_DateCreated());
							jsonObject.put(EDRMServiceConstants.DATE_LAST_MODIFIED, annotation.get_DateLastModified());
							jsonObject.put(EDRMServiceConstants.EDRM_NAME, annotation.get_Name());
							jsonObject.put(EDRMServiceConstants.PROPERTY_ID, annotatedDocumentID);
							jsonObject.put(EDRMServiceConstants.STICKY_NOTE, stickyNoteString);
							propertiesJsonObject.put(EDRMServiceConstants.PROPERTIES, jsonObject);
							jsonAnnotationsArray.put(propertiesJsonObject);
						}
					}
				}
			}
		}
		return jsonAnnotationsArray;
	}
}
