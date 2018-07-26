package com.cms.edrm.filenet.service;

import org.json.JSONObject;

import com.cms.edrm.filenet.exception.EDRMException;
/**
 * 
 * Interface to implement add and update document services
 *
 */
public interface EDRMMasterDataService {
	/**
	 * Method for adding a new document into FileNet Engine
	 * @param Json Document propertiesList
	 * @return Json response object
	 * @throws EDRMException 
	 */
	JSONObject getMasterData(JSONObject masterDataDetails) throws EDRMException;
	
}
