package com.cms.edrm.filenet.util;

import java.text.ParseException;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cms.edrm.filenet.constants.EDRMServiceConstants;
import com.cms.edrm.filenet.exception.EDRMException;
import com.filenet.api.admin.Choice;
import com.filenet.api.admin.ChoiceList;
import com.filenet.api.admin.LocalizedString;
import com.filenet.api.admin.PropertyDefinitionBoolean;
import com.filenet.api.admin.PropertyDefinitionDateTime;
import com.filenet.api.admin.PropertyDefinitionFloat64;
import com.filenet.api.admin.PropertyDefinitionInteger32;
import com.filenet.api.admin.PropertyDefinitionString;
import com.filenet.api.admin.PropertyTemplateBoolean;
import com.filenet.api.admin.PropertyTemplateDateTime;
import com.filenet.api.admin.PropertyTemplateFloat64;
import com.filenet.api.admin.PropertyTemplateInteger32;
import com.filenet.api.admin.PropertyTemplateString;
import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.collection.MarkingList;
import com.filenet.api.collection.MarkingSetSet;
import com.filenet.api.collection.PropertyDefinitionList;
import com.filenet.api.collection.RepositoryRowSet;
import com.filenet.api.constants.Cardinality;
import com.filenet.api.constants.ChoiceType;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.constants.TypeID;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.query.RepositoryRow;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.security.Marking;
import com.filenet.api.security.MarkingSet;
import com.filenet.api.util.Id;
/**
 * 
 * Utility class for Property Services of Filenet Engine
 *
 */
public class PropertyServicesUtil {
	static Logger logger = LoggerFactory.getLogger(PropertyServicesUtil.class);
	static String objectIdentity = null;
	/*
	 * This method is to create String Property
	 * @path ObjectStore
	 * @path propertyDetails
	 * @path localizedStrring
	 * @return PropertyDefinitionString
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throws EDRMException
	 */
	@SuppressWarnings("unchecked")
	public PropertyDefinitionString createStringProperty(JSONObject propDetails , ObjectStore objObjectStore , LocalizedString locStr, String symbolicName, Domain domain, String documentCreator, String documentDateCreated)throws EngineRuntimeException , JSONException,EDRMException, ParseException {
		PropertyDefinitionString objPropDef = null;
		if(	propDetails.get(EDRMServiceConstants.PROPERTY_DATA_TYPE).toString().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_STRING)) {
			PropertyTemplateString objPropTemplate = Factory.PropertyTemplateString.createInstance(objObjectStore);
			// Set cardinality of properties that will be created from the property template
			if(propDetails.getString(EDRMServiceConstants.PROPERTY_CARDINALITY).trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_SINGLE)) {
					objPropTemplate.set_Cardinality (Cardinality.SINGLE);
			} else if(propDetails.getString(EDRMServiceConstants.PROPERTY_CARDINALITY).trim().equalsIgnoreCase(EDRMServiceConstants.DOCUMENT_MULTI)) {
					objPropTemplate.set_Cardinality (Cardinality.LIST);
			}
			if(null != locStr ) {
				String listName = propDetails.getString(EDRMServiceConstants.EDRM_SEARCH_CHOICELIST_NAME);
				// Create LocalizedString collection
				objPropTemplate.set_DisplayNames (Factory.LocalizedString.createList());
				objPropTemplate.get_DisplayNames().add(locStr);
				objPropTemplate.set_SymbolicName(symbolicName);
				objPropTemplate.set_Creator(documentCreator);
				objPropTemplate.set_DateCreated(new CommonServicesUtil().getDate(documentDateCreated));
				objPropTemplate.set_MaximumLengthString(Integer.parseInt(propDetails.get(EDRMServiceConstants.PROPERTY_MAX_LENGTH).toString()));
				objPropTemplate.set_IsValueRequired(Boolean.parseBoolean(propDetails.get(EDRMServiceConstants.PROPERTY_IS_REQUIRED).toString()));
				if(listName.equalsIgnoreCase(EDRMServiceConstants.MARKING_SET_SECURITY_LEVELS)) {
					MarkingSet markingSet = getMarkingSet(listName, domain);
					objPropTemplate.set_MarkingSet(markingSet);
					// Save new property template to the server
					objPropTemplate.save(RefreshMode.REFRESH);
					objPropDef = (PropertyDefinitionString)objPropTemplate.createClassProperty();
				} else {
					// Save new property template to the server
					objPropTemplate.save(RefreshMode.REFRESH);
					objPropDef = (PropertyDefinitionString)objPropTemplate.createClassProperty();
					com.filenet.api.admin.ChoiceList list = getChoiceList(listName, objObjectStore);
					objPropDef.set_ChoiceList(list);
				}
			}
		}
		return objPropDef;
	}
	/*
	 * This method is to create Integer Property
	 * @path ObjectStore
	 * @path propertyDetails
	 * @path localizedStrring
	 * @return PropertyDefinitionInteger32
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throws EDRMException
	 */
	@SuppressWarnings("unchecked")
	public PropertyDefinitionInteger32 createIntegerProperty(JSONObject propDetails , ObjectStore objObjectStore , LocalizedString locStr, String symbolicName, String documentCreator, String documentDateCreated)throws EngineRuntimeException , JSONException,EDRMException, ParseException {
		PropertyDefinitionInteger32 objPropDef = null;
		PropertyTemplateInteger32 objPropTemplate = Factory.PropertyTemplateInteger32.createInstance(objObjectStore);
		// Set cardinality of properties that will be created from the property template
				if(propDetails.getString(EDRMServiceConstants.PROPERTY_CARDINALITY).trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_SINGLE)) {
					objPropTemplate.set_Cardinality (Cardinality.SINGLE);
				} else if(propDetails.getString(EDRMServiceConstants.PROPERTY_CARDINALITY).trim().equalsIgnoreCase(EDRMServiceConstants.DOCUMENT_MULTI)) {
					objPropTemplate.set_Cardinality (Cardinality.LIST);
				}
		if(null != locStr ) {
			// Create LocalizedString collection
			objPropTemplate.set_DisplayNames (Factory.LocalizedString.createList());
			objPropTemplate.get_DisplayNames().add(locStr);
			objPropTemplate.set_SymbolicName(symbolicName);
			objPropTemplate.set_Creator(documentCreator);
			objPropTemplate.set_DateCreated(new CommonServicesUtil().getDate(documentDateCreated));
			objPropTemplate.set_IsValueRequired(Boolean.parseBoolean(propDetails.get(EDRMServiceConstants.PROPERTY_IS_REQUIRED).toString()));
			// Save new property template to the server
			objPropTemplate.save(RefreshMode.REFRESH);
			objPropDef = (PropertyDefinitionInteger32)objPropTemplate.createClassProperty();
				String listName = propDetails.getString(EDRMServiceConstants.EDRM_SEARCH_CHOICELIST_NAME);
				if(null != listName && !listName.trim().isEmpty()) {
				    com.filenet.api.admin.ChoiceList list = getChoiceList(listName, objObjectStore);
				    objPropDef.set_ChoiceList(list);
				}
      	} 
		return objPropDef;
	}
	/*
	 * This method is to create DateTime Property
	 * @path ObjectStore
	 * @path propertyDetails
	 * @path localizedStrring
	 * @return PropertyDefinitionDateTime
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throws EDRMException
	 */
	@SuppressWarnings("unchecked")
	public PropertyDefinitionDateTime createDateTimeProperty(JSONObject propDetails , ObjectStore objObjectStore , LocalizedString locStr, String symbolicName, String documentCreator, String documentDateCreated)throws EngineRuntimeException , JSONException,EDRMException, ParseException {
		PropertyDefinitionDateTime objPropDef = null;
		PropertyTemplateDateTime objPropTemplate = Factory.PropertyTemplateDateTime.createInstance(objObjectStore);
		// Set cardinality of properties that will be created from the property template
				if(propDetails.getString(EDRMServiceConstants.PROPERTY_CARDINALITY).trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_SINGLE)) {
					objPropTemplate.set_Cardinality (Cardinality.SINGLE);
				} else if(propDetails.getString(EDRMServiceConstants.PROPERTY_CARDINALITY).trim().equalsIgnoreCase(EDRMServiceConstants.DOCUMENT_MULTI)) {
					objPropTemplate.set_Cardinality (Cardinality.LIST);
				}
		if(null != locStr ) {
			// Create LocalizedString collection
			objPropTemplate.set_DisplayNames (Factory.LocalizedString.createList());
			objPropTemplate.get_DisplayNames().add(locStr);
			objPropTemplate.set_SymbolicName(symbolicName);
			objPropTemplate.set_Creator(documentCreator);
			objPropTemplate.set_DateCreated(new CommonServicesUtil().getDate(documentDateCreated));
			objPropTemplate.set_IsValueRequired(Boolean.parseBoolean(propDetails.get(EDRMServiceConstants.PROPERTY_IS_REQUIRED).toString()));
			// Save new property template to the server
			objPropTemplate.save(RefreshMode.REFRESH);
			objPropDef = (PropertyDefinitionDateTime)objPropTemplate.createClassProperty();
		} 
		return objPropDef;
	}
	/*
	 * This method is to create Float Property
	 * @path ObjectStore
	 * @path propertyDetails
	 * @path localizedStrring
	 * @return PropertyDefinitionFloat64
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throws EDRMException
	 */
	@SuppressWarnings("unchecked")
	public PropertyDefinitionFloat64 createFloatProperty(JSONObject propDetails , ObjectStore objObjectStore , LocalizedString locStr, String symbolicName, String documentCreator, String documentDateCreated)throws EngineRuntimeException , JSONException,EDRMException, ParseException {
		PropertyDefinitionFloat64 objPropDef = null;
		PropertyTemplateFloat64 objPropTemplate = Factory.PropertyTemplateFloat64.createInstance(objObjectStore);
		// Set cardinality of properties that will be created from the property template
				if(propDetails.getString(EDRMServiceConstants.PROPERTY_CARDINALITY).trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_SINGLE)) {
					objPropTemplate.set_Cardinality (Cardinality.SINGLE);
				} else if(propDetails.getString(EDRMServiceConstants.PROPERTY_CARDINALITY).trim().equalsIgnoreCase(EDRMServiceConstants.DOCUMENT_MULTI)) {
					objPropTemplate.set_Cardinality (Cardinality.LIST);
				}
		if(null != locStr ) {
			// Create LocalizedString collection
			objPropTemplate.set_DisplayNames (Factory.LocalizedString.createList());
			objPropTemplate.get_DisplayNames().add(locStr);
			objPropTemplate.set_SymbolicName(symbolicName);
			objPropTemplate.set_Creator(documentCreator);
			objPropTemplate.set_DateCreated(new CommonServicesUtil().getDate(documentDateCreated));
			objPropTemplate.set_IsValueRequired(Boolean.parseBoolean(propDetails.get(EDRMServiceConstants.PROPERTY_IS_REQUIRED).toString()));
			// Save new property template to the server
			objPropTemplate.save(RefreshMode.REFRESH);
			objPropDef = (PropertyDefinitionFloat64)objPropTemplate.createClassProperty();
				String listName = propDetails.getString(EDRMServiceConstants.EDRM_SEARCH_CHOICELIST_NAME);
				if(null != listName && !listName.trim().isEmpty()) {
				    com.filenet.api.admin.ChoiceList list = getChoiceList(listName, objObjectStore);
				    objPropDef.set_ChoiceList(list);
				}
		} 
		return objPropDef;
	}
	/*
	 * This method is to create Boolean Property
	 * @path ObjectStore
	 * @path propertyDetails
	 * @path localizedStrring
	 * @return PropertyDefinitionBoolean
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throws EDRMException
	 */
	@SuppressWarnings("unchecked")
	public PropertyDefinitionBoolean createBooleanProperty(JSONObject propDetails , ObjectStore objObjectStore , LocalizedString locStr, String symbolicName, String documentCreator, String documentDateCreated)throws EngineRuntimeException , JSONException,EDRMException, ParseException {
		PropertyDefinitionBoolean objPropDef =null;
		PropertyTemplateBoolean objPropTemplate = Factory.PropertyTemplateBoolean.createInstance(objObjectStore);
		// Set cardinality of properties that will be created from the property template
				if(propDetails.getString(EDRMServiceConstants.PROPERTY_CARDINALITY).trim().equalsIgnoreCase(EDRMServiceConstants.PROPERTY_SINGLE)) {
					objPropTemplate.set_Cardinality (Cardinality.SINGLE);
				} else if(propDetails.getString(EDRMServiceConstants.PROPERTY_CARDINALITY).trim().equalsIgnoreCase(EDRMServiceConstants.DOCUMENT_MULTI)) {
					objPropTemplate.set_Cardinality (Cardinality.LIST);
				}
		if(null != locStr ) {
			// Create LocalizedString collection
			objPropTemplate.set_DisplayNames (Factory.LocalizedString.createList());
			objPropTemplate.get_DisplayNames().add(locStr);
			objPropTemplate.set_SymbolicName(symbolicName);
			objPropTemplate.set_Creator(documentCreator);
			objPropTemplate.set_DateCreated(new CommonServicesUtil().getDate(documentDateCreated));
			objPropTemplate.set_IsValueRequired(Boolean.parseBoolean(propDetails.get(EDRMServiceConstants.PROPERTY_IS_REQUIRED).toString()));
			// Save new property template to the server
			objPropTemplate.save(RefreshMode.REFRESH);
			objPropDef = (PropertyDefinitionBoolean)objPropTemplate.createClassProperty();
       	} 
		return objPropDef;
	}
	/*
	 * This method is to get Choice List
	 * @path ObjectStore
	 * @path listName
	 * @return ChoiceList
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throws EDRMException
	 */
	@SuppressWarnings("rawtypes")
	public ChoiceList getChoiceList(String listName,ObjectStore objObjectStore) throws EngineRuntimeException ,EDRMException {
		ChoiceList choiceList = null;
		String searchString = EDRMServiceConstants.EDRM_SEARCH_CHOICELIST+"'"+listName+"'";
	    SearchSQL searchSQL = new SearchSQL(searchString);
	    SearchScope searchScope = new SearchScope(objObjectStore);
	    RepositoryRowSet rowSet = searchScope.fetchRows(searchSQL, null, null, new Boolean(true));
	    Iterator iter = rowSet.iterator();
	    Id listId = null;
	    RepositoryRow row;
	    while (iter.hasNext()) {
	        row = (RepositoryRow) iter.next();
	        listId = row.getProperties().get("Id").getIdValue();
	        String displayName = row.getProperties().getStringValue("DisplayName");
	        if(logger.isDebugEnabled()){
	        	logger.debug(" Id=" + listId.toString());
	        	logger.debug(" DisplayName=" + displayName);
	        }
	    }
	    choiceList = Factory.ChoiceList.fetchInstance(objObjectStore,listId, null);
		return choiceList;
	}
	/*
	 * This method is to create Choice List Items
	 * @path listDetailsObject
	 * @path objChoiceList
	 * @param createOrUpdateFlag
	 * @return ChoiceList
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throws EDRMException
	 */
	@SuppressWarnings("unchecked")
	public ChoiceList choiceListItems(JSONObject listDetObject , ChoiceList objChoiceList, String createOrUpdateFlag) throws EngineRuntimeException ,JSONException,EDRMException{
	if(listDetObject.getString(EDRMServiceConstants.EDRM_LIST_TYPE).equalsIgnoreCase(EDRMServiceConstants.PROPERTY_STRING)) {
	JSONArray jsonArray = listDetObject.getJSONArray(EDRMServiceConstants.EDRM_LIST_VALUES);
	if(null != jsonArray && jsonArray.length()>0 ) {
		if(createOrUpdateFlag==EDRMServiceConstants.EDRM_CHOICELIST_CREATE) {
		objChoiceList.set_DataType(TypeID.STRING);
		// Add choice items to choice list and save it
		objChoiceList.set_ChoiceValues(Factory.Choice.createList());
		}
			for(int value=0;value<jsonArray.length();value++) {
				JSONObject keySets = (JSONObject) jsonArray.get(value);
				Choice choice = Factory.Choice.createInstance();
				choice.set_ChoiceType(ChoiceType.STRING);
				choice.set_DisplayName(keySets.get(EDRMServiceConstants.DISPLAY_NAME).toString());
				choice.set_ChoiceStringValue(keySets.get(EDRMServiceConstants.OBJECT_VALUE).toString());
				objChoiceList.get_ChoiceValues().add(choice);
			}
	}
		objChoiceList.save(RefreshMode.REFRESH);
	}
	else if(listDetObject.getString(EDRMServiceConstants.EDRM_LIST_TYPE).equalsIgnoreCase(EDRMServiceConstants.PROPERTY_INTEGER)) {
			JSONArray jsonArray = listDetObject.getJSONArray(EDRMServiceConstants.EDRM_LIST_VALUES);
			if(null != jsonArray && jsonArray.length()>0 ) {
				if(createOrUpdateFlag==EDRMServiceConstants.EDRM_CHOICELIST_CREATE)
					objChoiceList.set_DataType(TypeID.LONG);
					// Add choice items to choice list and save it
					objChoiceList.set_ChoiceValues(Factory.Choice.createList());
					for(int value=0;value<jsonArray.length();value++) {
						JSONObject keySets = (JSONObject) jsonArray.get(value);
						Choice choice = Factory.Choice.createInstance();
						choice.set_ChoiceType(ChoiceType.INTEGER);
						choice.set_DisplayName(keySets.get(EDRMServiceConstants.DISPLAY_NAME).toString());
						choice.set_ChoiceIntegerValue(Integer.valueOf(keySets.get(EDRMServiceConstants.OBJECT_VALUE).toString()));
						objChoiceList.get_ChoiceValues().add(choice);
					}
			}
			objChoiceList.save(RefreshMode.REFRESH);
		 }
		
	return objChoiceList;
	}
	/*
	 * This method is to create marking sets
	 * @param markingSetName
	 * @param domain
	 * @param markingValues
	 * @return MarkingSet
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throws EDRMException
	 */
	public MarkingSet createMarkingSet(Domain domain, String markingSetName, JSONArray markingValues) throws EngineRuntimeException, JSONException, EDRMException{
		MarkingSet markingSet = Factory.MarkingSet.createInstance(domain);
		markingSet.set_DisplayName(markingSetName);
		MarkingList markingList = createMarkingList(markingValues);
		markingSet.set_Markings(markingList);
		markingSet.save(RefreshMode.REFRESH);
		return markingSet;
	}
	/*
	 * This method is get marking sets
	 * @param markingSetName
	 * @param Domain
	 * @return MarkingSet
	 * @throws EDRMException
	 * @throws EngineRuntimeException
	 */
	@SuppressWarnings("rawtypes")
	public MarkingSet getMarkingSet(String markingSetName, Domain domain) throws EDRMException, EngineRuntimeException {
		MarkingSetSet getMarkingSets = domain.get_MarkingSets();
		Iterator iterator = getMarkingSets.iterator();
		Id id = null;
		while(iterator.hasNext()) {
			MarkingSet ms = (MarkingSet)iterator.next();
			if(markingSetName.equalsIgnoreCase(ms.get_DisplayName())) {
				id = ms.get_Id();
			}
		}
		MarkingSet markingSet = Factory.MarkingSet.fetchInstance(domain, id, null);
		return markingSet;
	}
	/*
	 * This method is create marking lists
	 * @param markingValues
	 * @return MarkingSet
	 * @throws EDRMException
	 * @throws JSONException
	 * @throws EngineRuntimeException
	 */
	@SuppressWarnings("unchecked")
	public MarkingList createMarkingList(JSONArray markingValues) throws JSONException, EngineRuntimeException, EDRMException{
		MarkingList markingList = Factory.Marking.createList();
		for (int i = 0; i < markingValues.length(); i++) {
			JSONObject jsonArrayObject = (JSONObject) markingValues.get(i);
			String markingValue = jsonArrayObject.getString(EDRMServiceConstants.MARKING_SET_MARKING_VALUE);
			JSONArray users = jsonArrayObject.getJSONArray(EDRMServiceConstants.MARKING_SET_USERS);
			String privilegeType = jsonArrayObject.getString(EDRMServiceConstants.MARKING_SET_PRIVILEGE_TYPE);
			int propertySecurityMask = new CommonServicesUtil().getSecurityMaskValue(jsonArrayObject.getString(EDRMServiceConstants.MARKING_SET_PRIVILEGE));
			AccessPermissionList permissionList = Factory.AccessPermission.createList();
			for(int j=0;j<users.length();j++) {
				JSONObject jsonObject = (JSONObject) users.get(j);
				//setting marking set permissions
				permissionList = new CommonServicesUtil().setPermissions(permissionList, jsonObject.getString(EDRMServiceConstants.MARKING_SET_USERNAME), Integer.parseInt(EDRMServiceConstants.MARKING_SET_SECURITY_MASK), privilegeType);
			}
			Marking createInstance = Factory.Marking.createInstance();
			createInstance.set_MarkingValue(markingValue);
			createInstance.set_ConstraintMask(propertySecurityMask);
			createInstance.set_Permissions(permissionList);
			markingList.add(createInstance);
		}
		return markingList;
	}
	/*
	 * This method is update marking set
	 * @param domain
	 * @param markingSetName
	 * @param markingValues
	 * @return MarkingSet
	 * @throws EDRMException
	 * @throws JSONException
	 * @throws EngineRuntimeException
	 */
	public MarkingSet updateMarkingSet(Domain domain, String markingSetName, JSONArray markingValues) throws EngineRuntimeException, JSONException, EDRMException{
		MarkingSet markingSet = getMarkingSet(markingSetName, domain);
		MarkingList markingList = createMarkingList(markingValues);
		markingSet.set_Markings(markingList);
		markingSet.save(RefreshMode.REFRESH);
		return markingSet;
	}
	/*
	 * This method is fetching propertyDefinitionList
	 * @param objPropDefs
	 * @param locStr
	 * @param propDetails
	 * @param objObjectStore
	 * @param domain
	 * @param creator
	 * @param documentDateCreated
	 * @return PropertyDefinitionList
	 * @throws EDRMException
	 * @throws JSONException
	 * @throws ParseException
	 * @throws EngineRuntimeException
	 */
	@SuppressWarnings("unchecked")
	public PropertyDefinitionList propertyDefinitionList(PropertyDefinitionList objPropDefs, LocalizedString locStr,
			JSONObject propDetails, ObjectStore objObjectStore, Domain domain, String creator,
			String documentDateCreated) throws EngineRuntimeException, JSONException, EDRMException, ParseException {
		if (propDetails.getString(EDRMServiceConstants.PROPERTY_DATA_TYPE)
				.equalsIgnoreCase(EDRMServiceConstants.PROPERTY_STRING)) {
			//creating a string type property
			PropertyDefinitionString objPropDef = createStringProperty(propDetails,
					objObjectStore, locStr,
					propDetails.getString(EDRMServiceConstants.PROPERTY_SYMBOLIC_NAME).trim(), domain, creator, documentDateCreated);
			// Add new property definition to class definition
			objPropDefs.add(objPropDef);
		} else if (propDetails.getString(EDRMServiceConstants.PROPERTY_DATA_TYPE)
				.equalsIgnoreCase(EDRMServiceConstants.PROPERTY_INTEGER)) {
			//creating a integer type property
			PropertyDefinitionInteger32 objPropDef = createIntegerProperty(propDetails,
					objObjectStore, locStr,
					propDetails.getString(EDRMServiceConstants.PROPERTY_SYMBOLIC_NAME).trim(), creator, documentDateCreated);
			// Add new property definition to class definition
			objPropDefs.add(objPropDef);
		} else if (propDetails.getString(EDRMServiceConstants.PROPERTY_DATA_TYPE)
				.equalsIgnoreCase(EDRMServiceConstants.PROPERTY_DATE_TIME)) {
			//creating date time property
			PropertyDefinitionDateTime objPropDef = createDateTimeProperty(propDetails,
					objObjectStore, locStr,
					propDetails.getString(EDRMServiceConstants.PROPERTY_SYMBOLIC_NAME).trim(), creator, documentDateCreated);
			// Add new property definition to class definition
			objPropDefs.add(objPropDef);
		} else if (propDetails.getString(EDRMServiceConstants.PROPERTY_DATA_TYPE)
				.equalsIgnoreCase(EDRMServiceConstants.PROPERTY_BOOLEAN)) {
			//creating boolean type property
			PropertyDefinitionBoolean objPropDef = createBooleanProperty(propDetails,
					objObjectStore, locStr,
					propDetails.getString(EDRMServiceConstants.PROPERTY_SYMBOLIC_NAME).trim(), creator, documentDateCreated);
			// Add new property definition to class definition
			objPropDefs.add(objPropDef);
		} else if (propDetails.getString(EDRMServiceConstants.PROPERTY_DATA_TYPE)
				.equalsIgnoreCase(EDRMServiceConstants.PROPERTY_FLOAT)) {
			//creating a float type property
			PropertyDefinitionFloat64 objPropDef = createFloatProperty(propDetails,
					objObjectStore, locStr,
					propDetails.getString(EDRMServiceConstants.PROPERTY_SYMBOLIC_NAME).trim(), creator, documentDateCreated);
			// Add new property definition to class definition
			objPropDefs.add(objPropDef);
		}
		return objPropDefs;
	}
}
