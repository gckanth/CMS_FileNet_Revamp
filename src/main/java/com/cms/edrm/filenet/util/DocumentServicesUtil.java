package com.cms.edrm.filenet.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
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
import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.admin.LocalizedString;
import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.collection.BooleanList;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.DateTimeList;
import com.filenet.api.collection.Float64List;
import com.filenet.api.collection.Integer32List;
import com.filenet.api.collection.StringList;
import com.filenet.api.collection.VersionableSet;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.core.IndependentlyPersistableObject;
import com.filenet.api.core.Link;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.core.VersionSeries;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.property.Properties;
import com.filenet.api.util.Id;
/**
 * 
 * Utility class for Document Services of Filenet Engine
 *
 */
public class DocumentServicesUtil {

	static Logger logger = LoggerFactory.getLogger(CommonServicesUtil.class);
	static String objectIdentity = null;
	/*
	 * This Method to create document instance
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
	public Document createDocumentInstance(ObjectStore objStore, JSONArray documentPropertiesJsonObject, String documentClassName, 
			String filepathJsonObject, String mimeTypeJsonObject, String documentCreator, String documentDateCreated, String folder_ID)throws EngineRuntimeException, JSONException, EDRMException, FileNotFoundException, ParseException{
		Document document = null;
		JSONObject jsonObject = null;
		CommonServicesUtil commonServicesUtil = new CommonServicesUtil();
		File file = new File(filepathJsonObject);
		document = Factory.Document.createInstance(objStore, documentClassName);
		document.set_Creator(documentCreator);
		document.set_DateCreated(commonServicesUtil.getDate(documentDateCreated));
		if(!filepathJsonObject.equalsIgnoreCase(EDRMServiceConstants.NO_DOCUMENT_CONTENT)) {
			//add document content
			document = addDocumentContent(objStore, document, filepathJsonObject, mimeTypeJsonObject);
			document.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
		}
		Properties properties = document.getProperties();
		//update document instance properties
		updateProperties(documentPropertiesJsonObject, file.getName(), properties);
		document.save(RefreshMode.REFRESH);
		IndependentlyPersistableObject independentObject = document;
		//searching for folder path, using folder unique identifier
		String folderName = new FolderServicesUtil().uniqueFolderSearch(objStore, folder_ID);
		//filing document into folder
		independentObject = fileToFolder(objStore, folderName, independentObject);
		Document documentObject = (Document) independentObject;
		jsonObject = new JSONObject();
		jsonObject.put(EDRMServiceConstants.DOCUMENT_ID, documentObject.get_Id().toString());
		jsonObject.put(EDRMServiceConstants.DOCUMENT_NAME, documentObject.get_Name());
		jsonObject.put(EDRMServiceConstants.DOCUMENT_STATUS_CODE, EDRMServiceConstants.SUCCESS_STATUS_CODE);
		logger.info("@Java-DocumentCommonMethods ,createDocumentInstance, @Message:Document has been successfully uploaded to Filenet");
		return document;
	}
	/*
	 * This method is to Add Document Content
	 * @param ObjectStore
	 * @param Document
	 * @param DocumentContentFilePath
	 * @param ContentMimeType
	 * @return Document
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throw EDRMException
	 */
	@SuppressWarnings("unchecked")
	public Document addDocumentContent(ObjectStore objStore, Document document, String filepathJsonString, String mimeTypeJsonObject) throws EngineRuntimeException, JSONException, EDRMException, FileNotFoundException
	{
		ContentElementList contentList = Factory.ContentElement.createList();
		ContentTransfer content = Factory.ContentTransfer.createInstance();
		File file = new File(filepathJsonString);
		FileInputStream fileStream = new FileInputStream(file);
		content.setCaptureSource(fileStream);
		contentList.add(content);
		
		if(mimeTypeJsonObject.length()==0)
			throw new EDRMException(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE, EDRMServiceConstants.DOCUMENT_NO_MIME_TYPE, EDRMServiceConstants.MIMETYPE_INVALID_CODE, EDRMServiceConstants.MIMETYPE_INVALID_MESSAGE);
		else
			document.set_MimeType(mimeTypeJsonObject.trim());
		
		document.set_ContentElements(contentList);
		return document;
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
	public JSONObject updateDocumentInstance(ObjectStore objStore, String documentID, JSONArray documentPropertiesJsonArray,
			String documentClassName, String filepathJsonObject, String mimeTypeJsonObject, String documentCreator, String documentDateCreated) throws EngineRuntimeException, JSONException, EDRMException, FileNotFoundException, ParseException {
		Document document;
		JSONObject jsonObject = new JSONObject();
		CommonServicesUtil commonServicesUtil = new CommonServicesUtil();
		// Specify internal and external files to be added as content.
		File internalFile = new File(filepathJsonObject);
		document = Factory.Document.fetchInstance(objStore, new Id(documentID), null);
		Properties properties = document.getProperties();
		//updating document properties
		updateProperties(documentPropertiesJsonArray, internalFile.getName(), properties);
		document.save(RefreshMode.REFRESH);
		jsonObject.put(EDRMServiceConstants.RESPONSE_CODE, EDRMServiceConstants.DOCUMENT_PROPERTIES_UPDATED);
		jsonObject.put(EDRMServiceConstants.RESPONSE_MESSAGE, EDRMServiceConstants.DOCUMENT_PROPERTIES_UPDATED);
		jsonObject.put(EDRMServiceConstants.DOCUMENT_ID, document.get_Id().toString());
		
		Document reservation;
		//checking content data
		if(!filepathJsonObject.equalsIgnoreCase(EDRMServiceConstants.NO_DOCUMENT_CONTENT)){
			//Getting Document Information
			Document newDocument=Factory.Document.getInstance(objStore, documentClassName.trim(), new Id(documentID));
			// Check out the Document object and save it.
			newDocument.checkout(com.filenet.api.constants.ReservationType.COLLABORATIVE, null, newDocument.getClassName(), newDocument.getProperties());
			newDocument.save(RefreshMode.REFRESH);
			// Get the Reservation object from the Document object.
			reservation = (Document) newDocument.get_Reservation();
			reservation.getProperties().removeFromCache(PropertyNames.LAST_MODIFIER);
			reservation.getProperties().removeFromCache(PropertyNames.DATE_LAST_MODIFIED);
			reservation.set_LastModifier(documentCreator);
			reservation.set_DateLastModified(commonServicesUtil.getDate(documentDateCreated));
			// Add content to the Reservation object.
			reservation = addDocumentContent(objStore, reservation, filepathJsonObject, mimeTypeJsonObject);
			reservation.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
			reservation.save(RefreshMode.REFRESH);
			jsonObject.put(EDRMServiceConstants.RESPONSE_CODE, EDRMServiceConstants.SUCCESS_STATUS_CODE);
			jsonObject.put(EDRMServiceConstants.RESPONSE_MESSAGE, EDRMServiceConstants.DOCUMENT_UPDATED_SUCCESS);
			jsonObject.put(EDRMServiceConstants.DOCUMENT_ID, reservation.get_Id().toString());
		}
		
		return jsonObject;
	}
	/*
	 * This method is to Checking Document Version
	 * @param ObjectStore
	 * @param documentID
	 * @return boolean
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throws EDRMException
	 */
	public boolean getCurrentVersion(ObjectStore objStore, String documentID) throws EngineRuntimeException, JSONException, EDRMException
	{
		Document document = Factory.Document.fetchInstance(objStore, new Id(documentID.trim()), null);
		return document.get_IsCurrentVersion();
	}
	/*
	 * This method is to Updating a Document Instance Properties
	 * @param documentPropertiesJsonArray
	 * @param fileName
	 * @param DocumentInstanceProperties
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throw EDRMException
	 */
	@SuppressWarnings("unchecked")
	public void updateProperties(JSONArray documentPropertiesJsonArray, String documentTitle, Properties properties) throws EngineRuntimeException, JSONException, EDRMException, ParseException
	{
		CommonServicesUtil commonServicesUtil = new CommonServicesUtil();
		for(int i=0;i<documentPropertiesJsonArray.length();i++) {
			JSONObject jsonArrayObject = (JSONObject) documentPropertiesJsonArray.get(i);
			String propertyName = jsonArrayObject.getString(EDRMServiceConstants.DOCUMENT_OBJECT_PROPERTY_TYPE);
			String dataType = jsonArrayObject.getString(EDRMServiceConstants.DOCUMENT_OBJECT_DATA_TYPE);
			String cardinality = jsonArrayObject.getString(EDRMServiceConstants.DOCUMENT_OBJECT_CARDINALITY);
			String propertyValue = null;
			JSONArray jsonArray = null;
			StringList stringList = Factory.StringList.createList();
			Integer32List intListValue = Factory.Integer32List.createList();
			Float64List float64List = Factory.Float64List.createList();
			DateTimeList dateListValue = Factory.DateTimeList.createList();
			BooleanList booleanList = Factory.BooleanList.createList();
			if(cardinality.trim().equalsIgnoreCase(EDRMServiceConstants.DOCUMENT_MULTI)){
				jsonArray = jsonArrayObject.getJSONArray(EDRMServiceConstants.OBJECT_VALUE);
			} else if(cardinality.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_SINGLE)){
				propertyValue = jsonArrayObject.getString(EDRMServiceConstants.OBJECT_VALUE);
			}
			if(null != documentTitle) {
				if(documentTitle.equalsIgnoreCase(EDRMServiceConstants.NO_DOCUMENT_CONTENT))
					properties.putValue(EDRMServiceConstants.DOCUMENT_TITLE, properties.getStringValue(EDRMServiceConstants.DOCUMENT_TITLE));
				else
					properties.putValue(EDRMServiceConstants.DOCUMENT_TITLE, documentTitle);
			}
			if(dataType.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_STRING) && cardinality.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_SINGLE)){
				properties.putValue(propertyName.trim(), propertyValue.trim());
			} else if(dataType.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_STRING) && cardinality.trim().equalsIgnoreCase(EDRMServiceConstants.DOCUMENT_MULTI)) {
				for(int j=0;j<jsonArray.length();j++) {
					propertyValue = (String) jsonArray.get(j);
					stringList.add(propertyValue.trim());
				}
				properties.putObjectValue(propertyName.trim(), stringList);
			} else if(dataType.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_FLOAT) && cardinality.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_SINGLE)){
				properties.putValue(propertyName.trim(), Float.valueOf(propertyValue.trim()));
			} else if(dataType.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_FLOAT) && cardinality.trim().equalsIgnoreCase(EDRMServiceConstants.DOCUMENT_MULTI)){
				for(int j=0;j<jsonArray.length();j++) {
					propertyValue = (String) jsonArray.get(j);
					float64List.add(Float.parseFloat(propertyValue.trim()));
				}
				properties.putObjectValue(propertyName.trim(), float64List);
			} else if(dataType.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_INTEGER) && cardinality.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_SINGLE)){
				properties.putValue(propertyName.trim(), Integer.valueOf(propertyValue.trim()));
			} else if(dataType.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_INTEGER) && cardinality.trim().equalsIgnoreCase(EDRMServiceConstants.DOCUMENT_MULTI)){
				for(int j=0;j<jsonArray.length();j++) {
					propertyValue = (String) jsonArray.get(j);
					int multiValue = Integer.parseInt(propertyValue);
					intListValue.add(multiValue);
				}
				properties.putObjectValue(propertyName.trim(), intListValue);
			} else if(dataType.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_BOOLEAN) && cardinality.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_SINGLE)){
				properties.putValue(propertyName.trim(), Boolean.valueOf(propertyValue.trim()));
			} else if(dataType.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_BOOLEAN) && cardinality.trim().equalsIgnoreCase(EDRMServiceConstants.DOCUMENT_MULTI)){
				for(int j=0;j<jsonArray.length();j++) {
					propertyValue = (String) jsonArray.get(j);
					booleanList.add(Boolean.parseBoolean(propertyValue));
				}
				properties.putObjectValue(propertyName.trim(), booleanList);
			} else if(dataType.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_DATE_TIME) && cardinality.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_SINGLE)){
				Date date = commonServicesUtil.getDate(propertyValue);
				properties.putValue(propertyName.trim(), date);
			} else if(dataType.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_DATE_TIME) && cardinality.trim().equalsIgnoreCase(EDRMServiceConstants.DOCUMENT_MULTI)){
				for(int j=0;j<jsonArray.length();j++) {
					propertyValue = (String) jsonArray.get(j);
					Date multiValue = commonServicesUtil.getDate(propertyValue.trim());
					dateListValue.add(multiValue);
				}
				properties.putObjectValue(propertyName.trim(), dateListValue);
			}
		}
	}
	/*
	 * This method is to Create Document Links
	 * @param ObjectStore
	 * @param ParentVersionSeriesID
	 * @param ChildDocumentInformation
	 * @return String
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throws EDRMException
	 */
	public JSONArray createDocumentLink(ObjectStore objectStore, String parentDocumentVSID, JSONArray childLinkDetails, String documentCreator, String documentDateCreated) throws EngineRuntimeException, JSONException, EDRMException, ParseException
	{
		String get_Id = null;
		JSONObject resultObject = null;
		JSONArray jsonArray = new JSONArray();
		for (int linkId = 0; linkId < childLinkDetails.length(); linkId++) 
		{
			JSONObject jsonArrayObject = (JSONObject) childLinkDetails.get(linkId);
			String linkName = jsonArrayObject.getString(EDRMServiceConstants.DOCUMENT_LINK_NAME);
			String description = jsonArrayObject.getString(EDRMServiceConstants.DOCUMENT_LINK_DESCRIPTION);
			String linkType = jsonArrayObject.getString(EDRMServiceConstants.DOCUMENT_LINK_TYPE);
			String childDocumentVSID = jsonArrayObject.getString(EDRMServiceConstants.CHILD_DOCUMENT_VSID);
			VersionSeries headVersionSeriesID = getVersionSeries(objectStore, parentDocumentVSID);
			VersionSeries tailVersionSeriesID = getVersionSeries(objectStore, childDocumentVSID);
			//create a document link object
			Link link = Factory.Link.createInstance(objectStore, EDRMServiceConstants.RELATED_ITEM_CLASS_NAME);
			com.filenet.api.property.Properties properties = link.getProperties();
			link.set_Creator(documentCreator);
			link.set_DateCreated(new CommonServicesUtil().getDate(documentDateCreated));
			properties.putValue(EDRMServiceConstants.TITLE, linkName.trim());
			properties.putValue(EDRMServiceConstants.DESCRIPTION, description.trim());
			properties.putValue(EDRMServiceConstants.DOCUMENT_LINK_NAME, linkType.trim());
			link.set_Head((IndependentObject) headVersionSeriesID);
			link.set_Tail((IndependentObject) tailVersionSeriesID);
			link.save(RefreshMode.REFRESH);
			get_Id = link.get_Id().toString();
			resultObject = new JSONObject();
			resultObject.put(EDRMServiceConstants.RESPONSE_CODE, EDRMServiceConstants.SUCCESS_STATUS_CODE);
			resultObject.put(EDRMServiceConstants.RESPONSE_MESSAGE, EDRMServiceConstants.DOCUMENT_LINK_CREATED);
			resultObject.put(EDRMServiceConstants.PROPERTY_ID, get_Id);
			jsonArray.put(resultObject);
		}
		return jsonArray;
	}
	/*
	 * This method is to Getting VersionSeries
	 * @path ObjectStore
	 * @path DocumentVersionSeriesID
	 * @return VersionSeries
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throws EDRMException
	 */
	public VersionSeries getVersionSeries(ObjectStore objectStore, String documentID) throws EngineRuntimeException, JSONException, EDRMException{
		VersionSeries versionSeriesID = Factory.VersionSeries.fetchInstance(objectStore, new Id(documentID.trim()), null);
		return versionSeriesID;
	}
	/*
	 * This method is to Filing a Document Instance into Folder
	 * @param ObjectStore
	 * @param FolderName
	 * @param filingObject
	 * @return Object(Document/Folder/CustomObject)
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throw EDRMException
	 */
	public IndependentlyPersistableObject fileToFolder(ObjectStore objStore, String folderName, IndependentlyPersistableObject document) throws EngineRuntimeException, JSONException, EDRMException{
		Folder folder = Factory.Folder.fetchInstance(objStore, folderName, null);
		ReferentialContainmentRelationship rel = folder.file(document, AutoUniqueName.AUTO_UNIQUE, null,
				DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
		rel.save(RefreshMode.NO_REFRESH);
		return document;
	}
	/*
	 * This method is to Parsing String to ContentEngineDateFormat
	 * @param dateString
	 * @return String
	 * @throw ParseException
	 */
	public String ceDateFormatConvertion(String dateDetails) throws ParseException{
		Date date = new Date();
		date = new SimpleDateFormat("MM/dd/yyyy, HH:mm a").parse(dateDetails);
		DateFormat df = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
		String s = df.format(date);
		return s;
	}
	/*
	 * This method is to get Document From Version Series ID
	 * @path ObjectStore
	 * @path VersionSeriesID
	 * @return JSONObject
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throws EDRMException
	 */
	@SuppressWarnings("rawtypes")
	public JSONObject getDocumentFromVersionSeriesID(ObjectStore objectStore, String versionSeriesID) throws EngineRuntimeException, JSONException, EDRMException{
		Document doc = null;
		JSONObject jsonObject = new JSONObject();
		VersionSeries versionSeries = Factory.VersionSeries.fetchInstance(objectStore, new Id(versionSeriesID), null);
		VersionableSet get_Versions = versionSeries.get_Versions();
		Iterator iterator = get_Versions.iterator();
		while(iterator.hasNext()) {
			doc = (Document)iterator.next();
			if(doc.get_IsCurrentVersion()){
				jsonObject.put(EDRMServiceConstants.DOCUMENT_ID, doc.get_Id().toString());
				jsonObject.put(EDRMServiceConstants.DOCUMENT_NAME, doc.get_Name());
			}
		}
		return jsonObject;
	}
	/*
	 * This method is to create Document Class
	 * @path ObjectStore
	 * @path symbolicName
	 * @path displayName
	 * @path documentParentClass
	 * @return JSONObject
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throws EDRMException
	 */
	@SuppressWarnings("unchecked")
	public JSONObject createDocumentClass(ObjectStore objectStore, String symbolicName, String displayName, String documentParentClass, String documentCreator, String documentDateCreated) throws EngineRuntimeException, JSONException, EDRMException, ParseException{
		ClassDefinition classDefinition = Factory.ClassDefinition.fetchInstance(objectStore, documentParentClass, null);
		ClassDefinition createSubclass = classDefinition.createSubclass();
		String get_LocaleName = "ar-om";
		// Set required properties to locale-specific string.
		LocalizedString locStr = getLocalizedString(displayName, get_LocaleName);
		// Create LocalizedString collection.
		createSubclass.set_DisplayNames (Factory.LocalizedString.createList());
		createSubclass.get_DisplayNames().add(locStr);
		createSubclass.set_SymbolicName(symbolicName);
		createSubclass.set_Creator(documentCreator);
		createSubclass.set_DateCreated(new CommonServicesUtil().getDate(documentDateCreated));
		createSubclass.save(RefreshMode.REFRESH);
		Id get_Id = createSubclass.get_Id();
		JSONObject resultObject = new JSONObject();
		resultObject.put(EDRMServiceConstants.RESPONSE_CODE, EDRMServiceConstants.SUCCESS_STATUS_CODE);
		resultObject.put(EDRMServiceConstants.RESPONSE_MESSAGE, EDRMServiceConstants.DOCUMENT_CLASS_CREATED);
		resultObject.put(EDRMServiceConstants.DOCUMENT_ID, get_Id.toString());

		return resultObject;
	}
	/*
	 * This method is to update Class Security
	 * @path ObjectStore
	 * @path symbolicName
	 * @path securityMask
	 * @path userName
	 * @path privilegeType
	 * @return JSONObject
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throws EDRMException
	 */
	public JSONObject updateClassSecurity(ObjectStore objectStore, String symbolicName, String securityMask, String userName,
			String privilegeType) throws EngineRuntimeException, JSONException, EDRMException{
		CommonServicesUtil commonServicesUtil = new CommonServicesUtil();
		ClassDefinition classDefinition = Factory.ClassDefinition.fetchInstance(objectStore, symbolicName.replace(" ", ""), null);
		//getting security mask value
		int securityMaskValue = commonServicesUtil.getSecurityMaskValue(securityMask);
		//setting document class permissions
		AccessPermissionList apl = commonServicesUtil.setPermissions(classDefinition.get_Permissions(), userName, securityMaskValue, privilegeType);
		classDefinition.set_Permissions(apl);
		classDefinition.save(RefreshMode.REFRESH);
		JSONObject resultObject = new JSONObject();
		resultObject.put(EDRMServiceConstants.RESPONSE_CODE, EDRMServiceConstants.SUCCESS_STATUS_CODE);
		resultObject.put(EDRMServiceConstants.RESPONSE_MESSAGE, EDRMServiceConstants.DOCUMENT_CLASS_CREATED_SECURITY_UPDATED);
		return resultObject;
	}
	/*
	 * This method is to get Localized String
	 * @param text
	 * @param locale
	 * @return LocalizedString
	 */
	private LocalizedString getLocalizedString(String text, String locale)
	{
		LocalizedString locStr = Factory.LocalizedString.createInstance();
		locStr.set_LocalizedText(text);
		locStr.set_LocaleName (locale);
		return locStr;
	}
	/*
	 * This method is to delete document instance
	 * @param ObjectStore
	 * @param documentID
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throws EDRMException
	 */
	public void deleteDocument(ObjectStore objectStore, String documentID) throws EngineRuntimeException, EDRMException{
		Document doc=Factory.Document.fetchInstance(objectStore, new Id(documentID), null);
		//document object deletion
		doc.delete();
		doc.save(RefreshMode.REFRESH);
	}
	/*
	 * This method is to delete document link instance
	 * @param ObjectStore
	 * @param linkID
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throws EDRMException
	 */
	public void deLinkDocument(ObjectStore objectStore, String linkID) throws EngineRuntimeException, EDRMException{
		Link link = Factory.Link.fetchInstance(objectStore, new Id(linkID), null);
		link.delete();
		link.save(RefreshMode.REFRESH);
	}
}