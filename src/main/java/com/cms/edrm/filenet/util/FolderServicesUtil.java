package com.cms.edrm.filenet.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cms.edrm.filenet.constants.EDRMServiceConstants;
import com.cms.edrm.filenet.exception.EDRMException;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.collection.Integer32List;
import com.filenet.api.collection.PropertyDescriptionList;
import com.filenet.api.collection.RepositoryRowSet;
import com.filenet.api.collection.StringList;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.CustomObject;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.meta.PropertyDescription;
import com.filenet.api.property.Properties;
import com.filenet.api.property.Property;
import com.filenet.api.query.RepositoryRow;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;
/**
 * 
 * Utility class for Folder Services of Filenet Engine
 *
 */
public class FolderServicesUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(FolderServicesUtil.class);
	/**
	 * Method for create folder in FileNet Engine
	 * @param objectStore
	 * @param folderName
	 * @param path
	 * @param properties
	 * @return JSONObject
	 * @throws EDRMException 
	 * @throws ParseException 
	 * @throws JSONException 
	 * @throws EngineRuntimeException 
	 */
	public JSONObject createFolderInFileNet(ObjectStore objectStore, String folderName, String path, JSONArray properties, String documentCreator, String documentDateCreated) throws EngineRuntimeException, JSONException, EDRMException, ParseException{
		JSONObject jsonObject = new JSONObject();
		Folder parentFolder = Factory.Folder.fetchInstance(objectStore, path, null);
		Folder childFolder = parentFolder.createSubFolder(folderName);
		childFolder.set_Creator(documentCreator);
		childFolder.set_DateCreated(new CommonServicesUtil().getDate(documentDateCreated));
		childFolder.save(RefreshMode.NO_REFRESH);	
		Folder folder = Factory.Folder.fetchInstance(objectStore, parentFolder.get_PathName()+"/"+folderName, null);
		Properties folderProperties = folder.getProperties();
		//updating folder properties
		folderProperties = updateFolderProperties(properties, folderProperties);
		folder.save(RefreshMode.REFRESH);
		jsonObject.put(EDRMServiceConstants.RESPONSE_CODE, EDRMServiceConstants.SUCCESS_STATUS_CODE); 
		jsonObject.put(EDRMServiceConstants.RESPONSE_MESSAGE, EDRMServiceConstants.FOLDER_CREATED_SUCCESS);
		jsonObject.put(EDRMServiceConstants.PROPERTY_ID, folder.get_Id().toString());
		return jsonObject;
	}
	/**
	 * Method for update folder properties in FileNet Engine
	 * @param customPropertiesArray
	 * @param folderProperties
	 * @return Properties
	 * @throws EDRMException 
	 * @throws ParseException 
	 * @throws JSONException 
	 * @throws EngineRuntimeException 
	 */
	public Properties updateFolderProperties(JSONArray customPropertiesArray, Properties folderProperties) throws EngineRuntimeException, JSONException, EDRMException, ParseException{
		for(int i=0;i<customPropertiesArray.length();i++)
		{
			JSONObject jsonArrayObject = (JSONObject) customPropertiesArray.get(i);
			String propertyName = jsonArrayObject.getString(EDRMServiceConstants.DOCUMENT_OBJECT_PROPERTY_TYPE);
			String propertyValue = jsonArrayObject.getString(EDRMServiceConstants.OBJECT_VALUE);
			String dataType = jsonArrayObject.getString(EDRMServiceConstants.DOCUMENT_OBJECT_DATA_TYPE);
			String cardinality = jsonArrayObject.getString(EDRMServiceConstants.DOCUMENT_OBJECT_CARDINALITY);
			if(dataType.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_STRING) && cardinality.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_SINGLE)){
				folderProperties.putValue(propertyName.trim(), propertyValue.trim());
			}
			else if(dataType.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_INTEGER) && cardinality.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_SINGLE)){
				folderProperties.putValue(propertyName.trim(), Integer.valueOf(propertyValue.trim()));
			} else if(dataType.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_BOOLEAN) && cardinality.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_SINGLE)){
				folderProperties.putValue(propertyName.trim(), Boolean.valueOf(propertyValue.trim()));
			}else if(dataType.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_DATE_TIME) && cardinality.trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_SINGLE)){
				Date date = new CommonServicesUtil().getDate(propertyValue);
				folderProperties.putValue(propertyName.trim(), date);
			} 
		}
		return folderProperties;
	}
	/*
	 * This method is to delete folder instance
	 * @param ObjectStore
	 * @param folderID
	 * @return JSONObject
	 * @throws EngineRuntimeException
	 * @throws EDRMException
	 */
	public void deleteFolder(ObjectStore objectStore, String folderID) throws EngineRuntimeException, EDRMException{
		Folder folder=Factory.Folder.fetchInstance(objectStore, new Id(folderID), null);
		//folder object deletion
		folder.delete();
		folder.save(RefreshMode.REFRESH);
	}
	/**
	 * Method for search folders by last modified date in FileNet Engine
	 * @param date
	 * @param objectStore
	 * @param jsonChildFolderArray
	 * @return JSONArray
	 * @throws EDRMException 
	 * @throws ParseException 
	 * @throws JSONException 
	 * @throws EngineRuntimeException 
	 */
	@SuppressWarnings("rawtypes")
	public JSONArray searchFoldersByDate(String date, ObjectStore objStore, JSONArray jsonChildFolderArray) throws EngineRuntimeException, JSONException, EDRMException, ParseException {
		JSONObject jsonObject = null;
		ArrayList al = null;
		JSONObject propertiesJsonObject = null;
		StringBuffer whereClause = new StringBuffer();
		//build query dynamically
		whereClause = new CommonServicesUtil().buildDateTypeQuery(EDRMServiceConstants.DATE_LAST_MODIFIED, date, whereClause);
		SearchSQL searchSql = new SearchSQL();
		searchSql.setFromClauseInitialValue(EDRMServiceConstants.FOLDER, null, true);
		if (whereClause.toString().trim().length() > 0) {
			searchSql.setWhereClause(whereClause.toString());
		}
		SearchScope searchScope = new SearchScope(objStore);
		//fetch folder objects from query
		FolderSet repositoryRowSet = (FolderSet) searchScope.fetchObjects(searchSql, null, null, true);
		Iterator iterator = repositoryRowSet.iterator();
		while (iterator.hasNext()) {
			jsonObject = new JSONObject();
			propertiesJsonObject = new JSONObject();
			Folder folder = (Folder)iterator.next();
			PropertyDescriptionList get_PropertyDescriptions = folder.get_ClassDescription().get_PropertyDescriptions();
			com.filenet.api.property.Properties prop = folder.getProperties();
			//frame folder properties as a JSON
			propertiesJsonObject = framePropertiesJSON(propertiesJsonObject, get_PropertyDescriptions, jsonObject, al, prop);
			propertiesJsonObject.put(EDRMServiceConstants.CREATOR, folder.get_Creator());
			propertiesJsonObject.put(EDRMServiceConstants.DATE_CREATED, folder.get_DateCreated());
			propertiesJsonObject.put(EDRMServiceConstants.DATE_LAST_MODIFIED, folder.get_DateLastModified());
			propertiesJsonObject.put(EDRMServiceConstants.EDRM_NAME, folder.get_Name());
			propertiesJsonObject.put(EDRMServiceConstants.PROPERTY_ID, folder.get_Id().toString());
			jsonChildFolderArray.put(propertiesJsonObject);
		}
		return jsonChildFolderArray;
	}
	/**
	 * Method for getting the user designation from custom database
	 * @param sqlServerConnection
	 * @param userID
	 * @return JSONArray
	 * @throws SQLException 
	 */
	public JSONArray getUserDesignation(Connection sqlServerConnection, String userID) throws SQLException{
		String sql = EDRMServiceConstants.USER_SEARCH_CRITERIA_SQL+userID+"'";
		Statement statement = sqlServerConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		JSONObject jsonObject = null;
		JSONArray jsonArray = new JSONArray();
		while(resultSet.next()) {
			jsonObject = new JSONObject();
			jsonObject.put(EDRMServiceConstants.USER_ID, resultSet.getString(EDRMServiceConstants.USER_ID));
			jsonObject.put(EDRMServiceConstants.NEW_DESIGNATION, resultSet.getString(EDRMServiceConstants.NEW_DESIGNATION));
			jsonArray.put(jsonObject);
		}
		return jsonArray;
	}
	/**
	 * This is common reusable Method for frame properties as a JSON Object in FileNet Engine
	 * @param propertiesJsonObject
	 * @param get_PropertyDescriptions
	 * @param framedJsonObject
	 * @param arrayListOfObject
	 * @param properties
	 * @return JSONArray
	 * @throws EDRMException 
	 * @throws ParseException 
	 * @throws JSONException 
	 * @throws EngineRuntimeException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JSONObject framePropertiesJSON(JSONObject propertiesJsonObject, PropertyDescriptionList get_PropertyDescriptions, JSONObject jsonObject, ArrayList arrayListOfObject, com.filenet.api.property.Properties properties){
		JSONArray propertiesJsonArray = new JSONArray();  
		Iterator itr = get_PropertyDescriptions.iterator();
		while(itr.hasNext()) {
			PropertyDescription props = (PropertyDescription) itr.next();
			if (!props.get_IsSystemGenerated() && !props.get_IsSystemOwned()
					&& !props.get_IsHidden() && !props.get_Name().equalsIgnoreCase(EDRMServiceConstants.PUBLICATION_SOURCE)) {
				if(EDRMServiceConstants.DOCUMENT_DISPLAY_TITLE.equalsIgnoreCase(props.get_DisplayName())) {
					jsonObject = new JSONObject();
					jsonObject.put(EDRMServiceConstants.PROPERTY_DATA_TYPE, props.get_DataType().toString());
					jsonObject.put(EDRMServiceConstants.PROPERTY_NAME, props.get_SymbolicName());
					jsonObject.put(EDRMServiceConstants.PROPERTY_VALUE, properties.getStringValue(props.get_SymbolicName()));
					propertiesJsonArray.put(jsonObject);
				}
				else {
					String get_SymbolicName = props.get_SymbolicName();
					if(props.get_DataType().toString().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_DATE)) {
						jsonObject = new JSONObject();
						jsonObject.put(EDRMServiceConstants.PROPERTY_DATA_TYPE, props.get_DataType().toString());
						jsonObject.put(EDRMServiceConstants.PROPERTY_NAME, props.get_SymbolicName());
						jsonObject.put(EDRMServiceConstants.PROPERTY_VALUE, properties.getDateTimeValue(get_SymbolicName));
						propertiesJsonArray.put(jsonObject);
					}
					else if(props.get_DataType().toString().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_BOOLEAN)) {
						jsonObject = new JSONObject();
						jsonObject.put(EDRMServiceConstants.PROPERTY_DATA_TYPE, props.get_DataType().toString());
						jsonObject.put(EDRMServiceConstants.PROPERTY_NAME, props.get_SymbolicName());
						jsonObject.put(EDRMServiceConstants.PROPERTY_VALUE, properties.getBooleanValue(get_SymbolicName));
						propertiesJsonArray.put(jsonObject);
					}
					else if(props.get_DataType().toString().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_LONG)){
						if(props.get_Cardinality().toString().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_SINGLE)) {
							jsonObject = new JSONObject();
							jsonObject.put(EDRMServiceConstants.PROPERTY_DATA_TYPE, props.get_DataType().toString());
							jsonObject.put(EDRMServiceConstants.PROPERTY_NAME, props.get_SymbolicName());
							jsonObject.put(EDRMServiceConstants.PROPERTY_VALUE, properties.getInteger32Value(get_SymbolicName));
							propertiesJsonArray.put(jsonObject);
						}
						else if(props.get_Cardinality().toString().equalsIgnoreCase(EDRMServiceConstants.EDRM_LIST)) {
							jsonObject = new JSONObject();
							jsonObject.put(EDRMServiceConstants.PROPERTY_DATA_TYPE, props.get_DataType().toString());
							jsonObject.put(EDRMServiceConstants.PROPERTY_NAME, props.get_SymbolicName());
							Integer32List integer32ListValue = properties.getInteger32ListValue(get_SymbolicName);
							Iterator integer32ListIterator = integer32ListValue.iterator();
							arrayListOfObject = new ArrayList();
							while(integer32ListIterator.hasNext()) {
								arrayListOfObject.add(integer32ListIterator.next());
							}
							jsonObject.put(EDRMServiceConstants.PROPERTY_VALUE, arrayListOfObject);
							propertiesJsonArray.put(jsonObject);
						}
					}
					else if(props.get_DataType().toString().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_STRING)) {
						if(props.get_Cardinality().toString().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_SINGLE)) {
							jsonObject = new JSONObject();
							jsonObject.put(EDRMServiceConstants.PROPERTY_DATA_TYPE, props.get_DataType().toString());
							jsonObject.put(EDRMServiceConstants.PROPERTY_NAME, props.get_SymbolicName());
							jsonObject.put(EDRMServiceConstants.PROPERTY_VALUE, properties.getStringValue(props.get_SymbolicName()));
							propertiesJsonArray.put(jsonObject);
						}
						else if(props.get_Cardinality().toString().equalsIgnoreCase(EDRMServiceConstants.EDRM_LIST)) {
							jsonObject = new JSONObject();
							jsonObject.put(EDRMServiceConstants.PROPERTY_DATA_TYPE, props.get_DataType().toString());
							jsonObject.put(EDRMServiceConstants.PROPERTY_NAME, props.get_SymbolicName());
							StringList stringListValue = properties.getStringListValue(get_SymbolicName);
							Iterator stringListIterator = stringListValue.iterator();
							arrayListOfObject = new ArrayList();
							while(stringListIterator.hasNext()) {
								arrayListOfObject.add(stringListIterator.next());
							}
							jsonObject.put(EDRMServiceConstants.PROPERTY_VALUE, arrayListOfObject);
							propertiesJsonArray.put(jsonObject);
						}
					}
				}
			}
		}
		propertiesJsonObject.put(EDRMServiceConstants.PROPERTIES, propertiesJsonArray);
		return propertiesJsonObject;
	}
	/*
	 * This method is to convert string to byte array
	 * @param hex
	 * @return byte[]
	 */
	public byte[] hexStringToByteArray(String hex) {
		int l = hex.length();
		byte[] data = new byte[l/2];
		for (int i = 0; i < l; i += 2) {
			data[i/2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
					+ Character.digit(hex.charAt(i+1), 16));
		}
		return data;
	}

	/*
	 * This method is to delete custom object instance
	 * @param ObjectStore
	 * @param taskID
	 * @return JSONObject
	 * @throws EngineRuntimeException
	 * @throws EDRMException
	 */
	public void deleteTask(ObjectStore objectStore, String taskID) throws EngineRuntimeException, EDRMException{
		CustomObject customObject=Factory.CustomObject.fetchInstance(objectStore, new Id(taskID), null);
		//custom Object deletion
		customObject.delete();
		customObject.save(RefreshMode.REFRESH);
	}
	/*
	 * This method is to move/relocation folder object instance
	 * @param ObjectStore
	 * @param fromFolderID
	 * @param toFolderID
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throws EDRMException
	 */
	public void moveFolder(ObjectStore objectStore, String fromFolderID, String toFolderID) throws EngineRuntimeException, JSONException, EDRMException {

		String fromFolderClassification = uniqueFolderSearch(objectStore, fromFolderID);
		String toFolderClassification = uniqueFolderSearch(objectStore, toFolderID);

		Folder fromFolder = Factory.Folder.fetchInstance(objectStore, fromFolderClassification, null);
		fromFolder.save(RefreshMode.REFRESH);

		Folder toFolder = Factory.Folder.fetchInstance(objectStore, toFolderClassification, null);
		toFolder.save(RefreshMode.REFRESH);

		fromFolder.move(toFolder);
		fromFolder.save(RefreshMode.REFRESH);
		LOGGER.info("Sucessfully Moved Folder from "+fromFolderClassification+" location to "+toFolderClassification+" location");
	}
	/*
	 * This method is to folderSearch Method
	 * @param ObjectStore
	 * @param folder_ID
	 * @return String
	 * @throws JSONException
	 * @throws EngineRuntimeException
	 * @throws EDRMException
	 */
	@SuppressWarnings("rawtypes")
	public String uniqueFolderSearch(ObjectStore objectStore, String folder_ID) throws EngineRuntimeException, JSONException, EDRMException{
		RepositoryRow repRow = null;
		String resultString = null;

		String mySQLString = EDRMServiceConstants.UNIQUE_FOLDER_SEARCH_SQL+"'"+ folder_ID +"'"+EDRMServiceConstants.SEARCH_CRITERIA_SQL_OPTIONS;
		SearchSQL searchSql = new SearchSQL();
		searchSql.setQueryString(mySQLString);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("  folderSearchCriteria method : searchSql : " + searchSql.toString());
		}
		SearchScope searchScope = new SearchScope(objectStore);
		RepositoryRowSet rowSet = searchScope.fetchRows(searchSql, null, null, new Boolean(true));
		Iterator<?> iterator = rowSet.iterator();
		while (iterator.hasNext()) 
		{
			repRow = (RepositoryRow) iterator.next();
			Properties properties = repRow.getProperties();
			Iterator iterator2 = properties.iterator();
			while(iterator2.hasNext())
			{
				Property impl = (Property)iterator2.next();
				if(impl.getPropertyName().equals(EDRMServiceConstants.FOLDER_PATH_NAME)){
					resultString = impl.getStringValue();
				}
			}
		}
		return resultString;
	}
}
