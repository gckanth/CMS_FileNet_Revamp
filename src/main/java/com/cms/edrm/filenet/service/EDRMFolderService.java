package com.cms.edrm.filenet.service;

import org.json.JSONObject;

import com.cms.edrm.filenet.exception.EDRMException;

public interface EDRMFolderService {

	/**
	 * Method for adding a new folder into FileNet Engine
	 * @param Json Folder propertiesList
	 * @return Json response object
	 * @throws EDRMException 
	 */
	JSONObject createFolder(JSONObject propertiesList) throws EDRMException;
	
	/**
	 * Method for update a folder into FileNet Engine
	 * @param Json Folder propertiesList
	 * @return Json response object
	 * @throws EDRMException 
	 */
	JSONObject updateFolder(JSONObject propertiesList) throws EDRMException;
	
	/**
	 * Method for deleting a folder into FileNet Engine
	 * @param Json Folder propertiesList
	 * @return Json response object
	 * @throws EDRMException 
	 */
	JSONObject deleteFolder(JSONObject propertiesList) throws EDRMException;
	
	/**
	 * Method for moving/relocation a folder in FileNet Engine
	 * @param Json Folder propertiesList
	 * @return Json response object
	 * @throws EDRMException 
	 */
	JSONObject moveOrRelocationFolder(JSONObject propertiesList) throws EDRMException;
}
