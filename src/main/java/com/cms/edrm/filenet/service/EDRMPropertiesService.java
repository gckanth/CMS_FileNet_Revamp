package com.cms.edrm.filenet.service;

import org.json.JSONObject;

import com.cms.edrm.filenet.exception.EDRMException;
/**
 * 
 * Interface to implement add and update document services
 *
 */
public interface EDRMPropertiesService {
	/**
	 * Method for creating a  property definition in class definition in FileNet Engine
	 * @param Json Document propertyDefDetails
	 * @return Json response object
	 * @throws EDRMException 
	 */
	JSONObject createPropertyDefinition(JSONObject propertyDefDetails) throws EDRMException;
	/**
	 * Method for deleting a  property definition from class definition in FileNet Engine
	 * @param Json Document propertyDefDetails
	 * @return Json response object
	 * @throws EDRMException 
	 */
	JSONObject deletePropertyDefinition(JSONObject propertyDefDetails) throws EDRMException;
	/**
	 * Method for creating a choice list  in FileNet Engine
	  * @param Json propDetails choicelist details 
	 * @return Json response object
	 * @throws EDRMException 
	 */
	JSONObject createChoiceList(JSONObject propDetails) throws EDRMException;
	/**
	 * Method for updating a choice list items in FileNet Engine
	  * @param Json propDetails choicelist details 
	 * @return Json response object
	 * @throws EDRMException 
	 */
	JSONObject updateChoiceListItems(JSONObject propDetails) throws EDRMException;
	/**
	 * Method for deleting a choice list items in FileNet Engine
	  * @param Json propDetails choicelist details 
	 * @return Json response object
	 * @throws EDRMException 
	 */
	Object deleteChoiceListItems(JSONObject propDetails) throws EDRMException;
	/**
	 * Method for create a marking set items in FileNet Engine
	  * @param Json propDetails marking set details 
	 * @return Json response object
	 * @throws EDRMException 
	 */
	JSONObject createMarkingSet(JSONObject propDetails) throws EDRMException;
	/**
	 * Method for update a marking set items in FileNet Engine
	  * @param Json propDetails marking set details 
	 * @return Json response object
	 * @throws EDRMException 
	 */
	JSONObject updateMarkingSet(JSONObject propDetails) throws EDRMException;
}
