package com.cms.edrm.filenet.service;

import org.json.JSONObject;

import com.cms.edrm.filenet.exception.EDRMException;
/**
 * 
 * Interface to implement add and update document services
 *
 */
public interface EDRMTaskService {
	/**
	 * Method for adding a new custom object into FileNet Engine
	 * @param Json custom object properties details
	 * @return Json response object
	 * @throws EDRMException 
	 */
	JSONObject createTasks(JSONObject taskDetails) throws EDRMException;
	/**
	 * Method is to update the meta data of an existing custom object
	 * @param Json custom object properties details
	 * @return Json response object
	 * @throws EDRMException 
	 */
	JSONObject updateTasks(JSONObject taskDetails) throws EDRMException;
	
	/**
	 * Method is to delete the existing custom object
	 * @param Json custom object properties details
	 * @return Json response object
	 * @throws EDRMException 
	 */
	JSONObject deleteTask(JSONObject propertiesList) throws EDRMException;
}
