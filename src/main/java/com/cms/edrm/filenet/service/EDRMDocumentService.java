package com.cms.edrm.filenet.service;

import org.json.JSONObject;

import com.cms.edrm.filenet.exception.EDRMException;
/**
 * 
 * Interface to implement add and update document services
 *
 */
public interface EDRMDocumentService {
	/**
	 * Method for adding a new document into FileNet Engine
	 * @param Json Document propertiesList
	 * @return Json response object
	 * @throws EDRMException 
	 */
	JSONObject addDocument(JSONObject propertiesList) throws EDRMException;
	/**
	 * Method is to update the meta data of an existing document
	 * @param Json Document propertiesList
	 * @return Json response object
	 * @throws EDRMException 
	 */
	JSONObject updateDocument(JSONObject propertiesList) throws EDRMException;
	/**
	 * Method is to delete the existing document
	 * @param Json Document propertiesList
	 * @return Json response object
	 * @throws EDRMException 
	 */
	JSONObject deleteDocument(JSONObject propertiesList) throws EDRMException;
	/**
	 * Method is to document linking
	 * @param Json Document Link propertiesList
	 * @return Json response object
	 * @throws EDRMException 
	 */
	JSONObject documentLinking(JSONObject propertiesList) throws EDRMException;
	/**
	 * Method is to de linking of document
	 * @param Json Document Link propertiesList
	 * @return Json response object
	 * @throws EDRMException 
	 */
	JSONObject deLinkDocument(JSONObject propertiesList) throws EDRMException;
	/**
	 * Method is to create Document Class
	 * @param Json Document propertiesList
	 * @return Json response object
	 * @throws EDRMException 
	 */
	JSONObject createDocumentClass(JSONObject jsonObject) throws EDRMException;
}
