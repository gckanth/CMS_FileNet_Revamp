package com.cms.edrm.filenet.service.impl;

import java.text.ParseException;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cms.edrm.filenet.constants.EDRMServiceConstants;
import com.cms.edrm.filenet.exception.EDRMException;
import com.cms.edrm.filenet.pojo.EDRMResponseDTO;
import com.cms.edrm.filenet.service.EDRMPropertiesService;
import com.cms.edrm.filenet.service.FnAuthDataProvider;
import com.cms.edrm.filenet.util.CommonServicesUtil;
import com.cms.edrm.filenet.util.PropertyServicesUtil;
import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.admin.LocalizedString;
import com.filenet.api.admin.PropertyDefinition;
import com.filenet.api.collection.PropertyDefinitionList;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.security.MarkingSet;
import com.filenet.api.util.Id;
import com.filenet.apiimpl.core.ChoiceImpl;
/**
 * 
 * Class which implements the add and update properties services
 *
 */
public class EDRMPropertiesServiceImpl implements EDRMPropertiesService {
	private static final Logger LOGGER = LoggerFactory.getLogger(EDRMPropertiesServiceImpl.class);
	/**
	 * Method for creating a new property definition in class definition FileNet Engine
	 * @param Json Document propertyDefDetails
	 * @return Json response object
	 * @throws EDRMException 
	 */
	@Override
	public JSONObject createPropertyDefinition(JSONObject propertyDefDetails) throws EDRMException    {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Start :: createProperty ::  EDRMPropertiesServiceImpl");
		}
		JSONObject resObject = null;
		try {
			// requesting parameters information from JSON object
			JSONObject reqDetails = (JSONObject) propertyDefDetails.get(EDRMServiceConstants.REQUEST_DETAILS);
			JSONObject propJson = (JSONObject) reqDetails.get(EDRMServiceConstants.PROPERTY);
			String docClassName = (String) propJson.get(EDRMServiceConstants.DOCUMENT_CLASS);
			JSONObject propDetails = (JSONObject) propJson.get(EDRMServiceConstants.PROPERTY_DETAILS);
			String documentCreator = (String) propJson.get(EDRMServiceConstants.CREATOR);
			String activeDirectory = EDRMServiceConstants.ACTIVE_DIRECTORY;
			String documentDateCreated = (String) propJson.get(EDRMServiceConstants.DATE_CREATED);
			FnAuthDataProvider authDataProvider = new FnAuthDataProviderImpl();
			ObjectStore objObjectStore = authDataProvider.getObjectStore();
			Domain domain = authDataProvider.doGetDomain();
			ClassDefinition objClassDef = null;
			PropertyDefinitionList objPropDefs = null;
			objClassDef = Factory.ClassDefinition.fetchInstance(objObjectStore, docClassName, null);
			// Get PropertyDefinitions property from the property cache
			objPropDefs = objClassDef.get_PropertyDefinitions();
			LocalizedString locStr = null;
			String propName = null;
			propName = propDetails.getString(EDRMServiceConstants.PROPERTY_NAME).trim();
			String getLocaleName = objObjectStore.get_LocaleName();
			//Set up locale
			locStr = Factory.LocalizedString.createInstance();
			locStr.set_LocalizedText(propName);
			locStr.set_LocaleName(getLocaleName);
			objPropDefs = new PropertyServicesUtil().propertyDefinitionList(objPropDefs, locStr, propDetails, 
					objObjectStore, domain, documentCreator+activeDirectory, documentDateCreated);
			objClassDef.save(RefreshMode.NO_REFRESH);
			Id getId = objClassDef.get_Id();
			resObject = new JSONObject();
			//Success response from FileNet
			resObject = new CommonServicesUtil().generateSuccessResponse(EDRMServiceConstants.SUCCESS_STATUS_CODE,
					EDRMServiceConstants.EDRM_CREATEPROPERTY_SUCCESS_MESG, null, null, getId.toString());
			return resObject;
		} catch (EngineRuntimeException engineRuntimeException) {
			LOGGER.error(" Exception in :: createPropertyDefinition ::  EDRMPropertiesServiceImpl :: "
					+ engineRuntimeException.getMessage());
			resObject = new JSONObject();
			resObject = new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.EDRM_CREATEPROPERTY_FAILURE_MESG,
					engineRuntimeException.getExceptionCode().getErrorId(), engineRuntimeException.getMessage());
			return resObject;
		} catch (JSONException jsonException) {
			resObject = new JSONObject();
			resObject = new EDRMException().generateExceptionDetails(EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					EDRMServiceConstants.EDRM_CREATEPROPERTY_FAILURE_MESG,
					EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE, jsonException.getMessage());
			return resObject;
		} catch (Exception exception) {
			resObject = new JSONObject();
			resObject = new EDRMException().generateExceptionDetails(EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					EDRMServiceConstants.EDRM_CREATEPROPERTY_FAILURE_MESG,
					EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE, exception.getMessage());
			return resObject;
		}
	}
	/**
	 * Method to delete a  property definition from class definition in FileNet Engine
	 * @param Json Document propertyDefDetails
	 * @return Json response object
	 * @throws EDRMException 
	 * @throws ParseException 
	 * @throws JSONException 
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public JSONObject deletePropertyDefinition(JSONObject propertyDefDetails) throws EDRMException {

		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Start :: deleteProperty ::  EDRMDocumentServiceImpl");
		}
		JSONObject resObject =null;
		try {	
			//requesting parameters information from JSON object
			JSONObject reqDetails = (JSONObject) propertyDefDetails.get(EDRMServiceConstants.REQUEST_DETAILS);
			JSONObject propJson = (JSONObject) reqDetails.get(EDRMServiceConstants.PROPERTY);
			String docClassName = (String) propJson.get(EDRMServiceConstants.DOCUMENT_CLASS);
			JSONObject propDetails = (JSONObject) propJson.get(EDRMServiceConstants.PROPERTY_DETAILS);
			resObject = new JSONObject();

			FnAuthDataProvider authDataProvider = new FnAuthDataProviderImpl();
			ObjectStore objObjectStore = authDataProvider.getObjectStore();
			ClassDefinition objClassDef = null;
			PropertyDefinitionList objPropDefs = null;
			objClassDef = Factory.ClassDefinition.fetchInstance(objObjectStore, docClassName, null);
			// Get PropertyDefinitions property from the property cache
			objPropDefs = objClassDef.get_PropertyDefinitions();
			Iterator iterator = objPropDefs.iterator();
			while (iterator.hasNext()) {
				PropertyDefinition objPropDef = (PropertyDefinition) iterator.next();

				String propName = objPropDef.get_SymbolicName();
				if (propDetails.getString(EDRMServiceConstants.PROPERTY_NAME).trim().equalsIgnoreCase(propName)) {
					objPropDefs.remove(objPropDef);
					objClassDef.save(RefreshMode.REFRESH);
					break;
				}
			}
			resObject = new JSONObject();
			//success response from FileNet
			resObject = new CommonServicesUtil().generateSuccessResponse(EDRMServiceConstants.SUCCESS_STATUS_CODE,
					EDRMServiceConstants.EDRM_DELETEPROPERTY_SUCCESS_MESG, null, null, null);
		} catch (EngineRuntimeException engineRuntimeException) {
			LOGGER.error(" Exception in :: deletePropertyDefinition ::  EDRMPropertiesServiceImpl :: "
					+ engineRuntimeException.getMessage());
			resObject = new JSONObject();
			resObject = new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.EDRM_DELETEPROPERTY_FAILURE_MESG,
					engineRuntimeException.getExceptionCode().getErrorId(), engineRuntimeException.getMessage());
			return resObject;
		} catch (JSONException jsonException) {
			resObject = new JSONObject();
			resObject = new EDRMException().generateExceptionDetails(EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					EDRMServiceConstants.EDRM_DELETEPROPERTY_FAILURE_MESG,
					EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE, jsonException.getMessage());
			return resObject;
		} catch (Exception exception) {
			resObject = new JSONObject();
			resObject = new EDRMException().generateExceptionDetails(EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					EDRMServiceConstants.EDRM_CREATEPROPERTY_FAILURE_MESG,
					EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE, exception.getMessage());
			return resObject;
		}
		return resObject;
	}
	/**
	 * Method for creating a new choice list in FileNet Engine
	 * @param Json propDetails choicelist details 
	 * @return Json response object
	 * @throws EDRMException 
	 * @throws ParseException 
	 * @throws JSONException 
	 */
	@Override
	public JSONObject createChoiceList(JSONObject propDetails) throws EDRMException {
		JSONObject resObject = new JSONObject();
		PropertyServicesUtil propertyServicesUtil = new PropertyServicesUtil();
		try {
			//requesting parameters information from JSON object
			JSONObject reqDetails = (JSONObject) propDetails.get(EDRMServiceConstants.REQUEST_DETAILS);
			JSONObject listDetObject = (JSONObject) reqDetails.get(EDRMServiceConstants.EDRM_LIST_DETAILS);
			String listName = listDetObject.getString(EDRMServiceConstants.EDRM_NAME);
			String documentCreator = (String) listDetObject.get(EDRMServiceConstants.CREATOR);
			String activeDirectory = EDRMServiceConstants.ACTIVE_DIRECTORY;
			String documentDateCreated = (String) listDetObject.get(EDRMServiceConstants.DATE_CREATED);
			FnAuthDataProvider authDataProvider = new FnAuthDataProviderImpl();
			ObjectStore objObjectStore = authDataProvider.getObjectStore();
			com.filenet.api.admin.ChoiceList objChoiceList = Factory.ChoiceList.createInstance(objObjectStore);
			objChoiceList.set_DisplayName(listName);
			objChoiceList.set_Creator(documentCreator+activeDirectory);
			objChoiceList.set_DateCreated(new CommonServicesUtil().getDate(documentDateCreated));
			// creating a choice list
			objChoiceList = propertyServicesUtil.choiceListItems(listDetObject, objChoiceList,
					EDRMServiceConstants.EDRM_CHOICELIST_CREATE);
			objChoiceList.save(RefreshMode.REFRESH);
			Id get_Id = objChoiceList.get_Id();
			resObject = new JSONObject();
			//success response from FileNet
			resObject = new CommonServicesUtil().generateSuccessResponse(
					EDRMServiceConstants.SUCCESS_STATUS_CODE,
					EDRMServiceConstants.EDRM_CREATECHOICELIST_SUCCESS_MESG, null, null, get_Id.toString());
			return resObject;
		} catch (EngineRuntimeException engineRuntimeException) {
			LOGGER.error(" Exception in :: createChoiceList ::  EDRMPropertiesServiceImpl :: "
					+ engineRuntimeException.getMessage());
			resObject = new JSONObject();
			resObject = new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.EDRM_CREATECHOICELIST_FAILURE_MESG,
					engineRuntimeException.getExceptionCode().getErrorId(), engineRuntimeException.getMessage());
			return resObject;
		} catch (JSONException jsonException) {
			resObject = new JSONObject();
			resObject = new EDRMException().generateExceptionDetails(EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					EDRMServiceConstants.EDRM_CREATECHOICELIST_FAILURE_MESG,
					EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE, jsonException.getMessage());
			return resObject;
		} catch (Exception exception) {
			resObject = new JSONObject();
			resObject = new EDRMException().generateExceptionDetails(EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					EDRMServiceConstants.EDRM_CREATEPROPERTY_FAILURE_MESG,
					EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE, exception.getMessage());
			return resObject;
		}
	}
	/**
	 * Method for updating a choice list in FileNet Engine
	 * @param Json propDetails choicelist details 
	 * @return Json response object
	 * @throws EDRMException 
	 * @throws ParseException 
	 * @throws JSONException 
	 */
	@Override
	public JSONObject updateChoiceListItems(JSONObject propDetails) throws EDRMException{
		JSONObject resObject = new JSONObject();
		PropertyServicesUtil propertyServicesUtil = new PropertyServicesUtil();
		try {
			//requesting parameters information from JSON object
			JSONObject reqDetails = (JSONObject) propDetails.get(EDRMServiceConstants.REQUEST_DETAILS);
			JSONObject listDetObject = (JSONObject) reqDetails.get(EDRMServiceConstants.EDRM_LIST_DETAILS);
			String listName = listDetObject.getString(EDRMServiceConstants.EDRM_NAME);
			FnAuthDataProvider authDataProvider = new FnAuthDataProviderImpl();
			ObjectStore objObjectStore = authDataProvider.getObjectStore();
			//creating a choice list
			com.filenet.api.admin.ChoiceList list = propertyServicesUtil.getChoiceList(listName, objObjectStore);
			//creating choice list item
			list = propertyServicesUtil.choiceListItems(listDetObject, list, EDRMServiceConstants.EDRM_CHOICELIST_UPDATE);
			list.save(RefreshMode.REFRESH);
			resObject = new JSONObject();
			//success response from FileNet
			resObject = new CommonServicesUtil().generateSuccessResponse(EDRMServiceConstants.SUCCESS_STATUS_CODE, 
					EDRMServiceConstants.EDRM_UPDATECHOICELIST_SUCCESS_MESG, null, null, list.get_Id().toString());
			return resObject;
		}catch(EngineRuntimeException engineRuntimeException) {
			LOGGER.error(" Exception in :: updateChoiceListItems ::  EDRMPropertiesServiceImpl :: "+engineRuntimeException.getMessage());
			resObject = new JSONObject();
			resObject = new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.EDRM_UPDATECHOICELIST_FAILURE_MESG,
					engineRuntimeException.getExceptionCode().getErrorId(), engineRuntimeException.getMessage());
			return resObject;
		} catch (JSONException jsonException) {
			resObject = new JSONObject();
			resObject = new EDRMException().generateExceptionDetails(EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					EDRMServiceConstants.EDRM_UPDATECHOICELIST_FAILURE_MESG,
					EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE, jsonException.getMessage());
			return resObject;
		} catch (Exception exception) {
			resObject = new JSONObject();
			resObject = new EDRMException().generateExceptionDetails(EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					EDRMServiceConstants.EDRM_CREATEPROPERTY_FAILURE_MESG,
					EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE, exception.getMessage());
			return resObject;
		}
	}
	/**
	 * Method for deleting a choice list items in FileNet Engine
	 * @param Json propDetails choicelist details 
	 * @return Json response object
	 * @throws EDRMException 
	 * @throws ParseException 
	 * @throws JSONException 
	 */
	@Override
	public Object deleteChoiceListItems(JSONObject propDetails) throws EDRMException {
		PropertyServicesUtil propertyServicesUtil = new PropertyServicesUtil();
		EDRMResponseDTO failureResultDTO = null;
		try {
			//requesting parameters information from JSON object
			JSONObject reqDetails = (JSONObject) propDetails.get(EDRMServiceConstants.REQUEST_DETAILS);
			JSONObject listDetObject = (JSONObject) reqDetails.get(EDRMServiceConstants.EDRM_LIST_DETAILS);
			String listName = listDetObject.getString(EDRMServiceConstants.EDRM_NAME);
			FnAuthDataProvider authDataProvider = new FnAuthDataProviderImpl();
			ObjectStore objObjectStore = authDataProvider.getObjectStore();
			//getting a choice list
			com.filenet.api.admin.ChoiceList list = propertyServicesUtil.getChoiceList(listName, objObjectStore);
			com.filenet.api.collection.ChoiceList choicelist = list.get_ChoiceValues();
			JSONArray jsonArray = listDetObject.getJSONArray(EDRMServiceConstants.EDRM_LIST_VALUES);
			if(null != jsonArray && jsonArray.length()>0 ) {
				for(int value=0;value<jsonArray.length();value++) {
					JSONObject keySets = (JSONObject) jsonArray.get(value);
					for(int count=0;count<choicelist.size();count++){
						ChoiceImpl choice = (ChoiceImpl) choicelist.get(count);
						if(listDetObject.getString(EDRMServiceConstants.EDRM_LIST_TYPE).equalsIgnoreCase(EDRMServiceConstants.PROPERTY_STRING)) {
							if(choice.get_DisplayName().equalsIgnoreCase(keySets.get(EDRMServiceConstants.DISPLAY_NAME).toString()) && 
									choice.get_ChoiceStringValue().equalsIgnoreCase(keySets.get(EDRMServiceConstants.OBJECT_VALUE).toString())) {
								choicelist.remove(choice);
							}
						}
						else if(listDetObject.getString(EDRMServiceConstants.EDRM_LIST_TYPE).equalsIgnoreCase(EDRMServiceConstants.PROPERTY_INTEGER)) {
							if(choice.get_DisplayName().equalsIgnoreCase(keySets.get(EDRMServiceConstants.DISPLAY_NAME).toString()) && 
									choice.get_ChoiceIntegerValue()==Integer.valueOf(keySets.get(EDRMServiceConstants.OBJECT_VALUE).toString())) {
								choicelist.remove(choice);
							}
						}
					}
				}
				list.set_ChoiceValues(choicelist);
				list.save(RefreshMode.REFRESH);
			}
			//success response as a POJO object from FileNet
			failureResultDTO = new EDRMResponseDTO();
			failureResultDTO.setResponseCode(EDRMServiceConstants.SUCCESS_STATUS_CODE);
			failureResultDTO.setResponseMessage(EDRMServiceConstants.EDRM_DELETECHOICELIST_SUCCESS_MESG);
			return failureResultDTO;
		}catch(EngineRuntimeException engineRuntimeException) {
			LOGGER.error(" Exception in :: deleteChoiceListItems ::  EDRMPropertiesServiceImpl :: "+engineRuntimeException.getMessage());
			failureResultDTO = new EDRMResponseDTO();
			failureResultDTO.setExceptionCode(engineRuntimeException.getExceptionCode().getErrorId());
			failureResultDTO.setExceptionMessage(engineRuntimeException.getMessage());
			failureResultDTO.setResponseCode(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE);
			failureResultDTO.setResponseMessage(EDRMServiceConstants.EDRM_DELETECHOICELIST_FAILURE_MESG);
			return failureResultDTO;
		}catch(JSONException jsonException){
			failureResultDTO = new EDRMResponseDTO();
			failureResultDTO.setExceptionCode(EDRMServiceConstants.STATUS_CODE_NO_CONTENT);
			failureResultDTO.setExceptionMessage(jsonException.getMessage());
			failureResultDTO.setResponseCode(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE);
			failureResultDTO.setResponseMessage(EDRMServiceConstants.EDRM_DELETECHOICELIST_FAILURE_MESG);
			return failureResultDTO;
		}catch (Exception exception) {
			failureResultDTO = new EDRMResponseDTO();
			failureResultDTO.setExceptionCode(EDRMServiceConstants.STATUS_CODE_NO_CONTENT);
			failureResultDTO.setExceptionMessage(exception.getMessage());
			failureResultDTO.setResponseCode(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE);
			failureResultDTO.setResponseMessage(EDRMServiceConstants.EDRM_DELETECHOICELIST_FAILURE_MESG);
			return failureResultDTO;
		}
	}
	/**
	 * Method for create a marking sets in FileNet Engine
	 * @param Json propDetails details 
	 * @return Json response object
	 * @throws EDRMException 
	 */
	@Override
	public JSONObject createMarkingSet(JSONObject propDetails) throws EDRMException {
		FnAuthDataProvider authDataProvider = new FnAuthDataProviderImpl();
		Domain domain = authDataProvider.doGetDomain();
		PropertyServicesUtil propertyServicesUtil = new PropertyServicesUtil();
		JSONObject resObject = null;
		try {
			//requesting parameters information from JSON object
			JSONObject requestDetails = (JSONObject) propDetails.get(EDRMServiceConstants.REQUEST_DETAILS);
			JSONObject markingSet = (JSONObject) requestDetails.get(EDRMServiceConstants.MARKING_SET);
			String markingSetName = markingSet.getString(EDRMServiceConstants.MARKING_SET_NAME);
			JSONArray markingValues = markingSet.getJSONArray(EDRMServiceConstants.MARKING_SET_MARKING_VALUES);
			//creating a Marking Sets
			MarkingSet markingSetObject = propertyServicesUtil.createMarkingSet(domain, markingSetName, markingValues);

			resObject = new JSONObject();
			//success response from FileNet
			resObject = new CommonServicesUtil().generateSuccessResponse(EDRMServiceConstants.SUCCESS_STATUS_CODE, 
					EDRMServiceConstants.MARKING_SET_CREATION_SUCCESS, null, null, markingSetObject.get_Id().toString());
			return resObject;
		}
		catch(EngineRuntimeException engineRuntimeException) {
			LOGGER.error(" Exception in :: createMarkingSet ::  EDRMPropertiesServiceImpl :: "+engineRuntimeException.getMessage());
			resObject = new JSONObject();
			resObject = new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE, EDRMServiceConstants.MARKING_SET_CREATION_FAILED, 
					engineRuntimeException.getExceptionCode().getErrorId(), engineRuntimeException.getMessage());
			return resObject;
		}catch(JSONException jsonException){
			resObject = new JSONObject();
			resObject = new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE, EDRMServiceConstants.MARKING_SET_CREATION_FAILED, 
					EDRMServiceConstants.JSON_EXCEPTION, jsonException.getMessage());
			return resObject;
		}catch (Exception exception) {
			resObject = new JSONObject();
			resObject = new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE, EDRMServiceConstants.MARKING_SET_CREATION_FAILED, 
					EDRMServiceConstants.STATUS_CODE_NO_CONTENT, exception.getMessage());
			return resObject;
		}
	}
	/**
	 * Method for update a marking sets in FileNet Engine
	 * @param Json propDetails details 
	 * @return Json response object
	 * @throws EDRMException 
	 */
	@Override
	public JSONObject updateMarkingSet(JSONObject propDetails) throws EDRMException {
		FnAuthDataProvider authDataProvider = new FnAuthDataProviderImpl();
		Domain domain = authDataProvider.doGetDomain();
		PropertyServicesUtil propertyServicesUtil = new PropertyServicesUtil();
		JSONObject resObject = null;
		try {
			//requesting parameters information from JSON object
			JSONObject requestDetails = (JSONObject) propDetails.get(EDRMServiceConstants.REQUEST_DETAILS);
			JSONObject markingSet = (JSONObject) requestDetails.get(EDRMServiceConstants.MARKING_SET);
			String markingSetName = markingSet.getString(EDRMServiceConstants.MARKING_SET_NAME);
			JSONArray markingValues = markingSet.getJSONArray(EDRMServiceConstants.MARKING_SET_MARKING_VALUES);
			//update a Marking Sets
			MarkingSet markingSetObject = propertyServicesUtil.updateMarkingSet(domain, markingSetName, markingValues);
			resObject = new JSONObject();
			//success response from FileNet
			resObject = new CommonServicesUtil().generateSuccessResponse(EDRMServiceConstants.SUCCESS_STATUS_CODE, 
					EDRMServiceConstants.MARKING_SET_UPDATE_SUCCESS, null, null, markingSetObject.get_Id().toString());
			return resObject;
		}
		catch(EngineRuntimeException engineRuntimeException) {
			LOGGER.error(" Exception in :: updateMarkingSet ::  EDRMPropertiesServiceImpl :: "+engineRuntimeException.getMessage());
			resObject = new JSONObject();
			resObject = new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE, EDRMServiceConstants.MARKING_SET_UPDATE_FAILED, 
					engineRuntimeException.getExceptionCode().getErrorId(), engineRuntimeException.getMessage());
			return resObject;
		}catch(JSONException jsonException){
			resObject = new JSONObject();
			resObject = new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE, EDRMServiceConstants.MARKING_SET_UPDATE_FAILED, 
					EDRMServiceConstants.JSON_EXCEPTION, jsonException.getMessage());
			return resObject;
		}catch (Exception exception) {
			resObject = new JSONObject();
			resObject = new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE, EDRMServiceConstants.MARKING_SET_UPDATE_FAILED, 
					EDRMServiceConstants.STATUS_CODE_NO_CONTENT, exception.getMessage());
			return resObject;
		}
	}
}
