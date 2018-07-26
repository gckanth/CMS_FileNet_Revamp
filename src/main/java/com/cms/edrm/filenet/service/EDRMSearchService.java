package com.cms.edrm.filenet.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import com.cms.edrm.filenet.exception.EDRMException;
import com.filenet.api.exception.EngineRuntimeException;

/**
 * 
 * Interface to implement FileNetP8 search services
 *
 */
public interface EDRMSearchService {

	/**
	 * Method for search a document classes by last modified date in FileNet Engine
	 * @param Date
	 * @return JSONObject
	 * @throws EDRMException 
	 * @throws ParseException 
	 * @throws JSONException 
	 * @throws EngineRuntimeException 
	 * @throws IOException 
	 */
	public JSONObject documentClassSearchByDate(JSONObject jsonObject)throws EDRMException, EngineRuntimeException, JSONException, ParseException, IOException;
	/**
	 * Method for search a document instance by last modified date in FileNet Engine
	 * @param Date
	 * @return JSONObject
	 * @throws EDRMException 
	 * @throws ParseException 
	 * @throws JSONException 
	 * @throws EngineRuntimeException 
	 * @throws IOException 
	 */
	public JSONObject documentInstanceSearchByDate(JSONObject jsonObject)throws EDRMException, EngineRuntimeException, JSONException, ParseException, IOException;
	/**
	 * Method for search a marking set by last modified date in FileNet Engine
	 * @param Date
	 * @return JSONObject
	 * @throws EDRMException 
	 * @throws ParseException 
	 * @throws JSONException 
	 * @throws EngineRuntimeException 
	 * @throws IOException 
	 */
	public JSONObject markingSetSearchByDate(JSONObject jsonObject)throws EDRMException, EngineRuntimeException, JSONException, ParseException, IOException;
	/**
	 * Method for search a data audited by last modified date in FileNet Engine
	 * @param Date
	 * @return JSONObject
	 * @throws EDRMException 
	 * @throws ParseException 
	 * @throws JSONException 
	 * @throws EngineRuntimeException 
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public JSONObject dataAuditedByDate(JSONObject jsonObject)throws EDRMException, EngineRuntimeException, JSONException, ParseException, IOException, ClassNotFoundException, SQLException;
	/**
	 * Method for search a folder by last modified date in FileNet Engine
	 * @param Date
	 * @return JSONObject
	 * @throws EDRMException 
	 * @throws ParseException 
	 * @throws JSONException 
	 * @throws EngineRuntimeException 
	 * @throws IOException 
	 */
	public JSONObject folderSearchByDate(JSONObject jsonObject)throws EDRMException, EngineRuntimeException, JSONException, ParseException, IOException;
	/**
	 * Method for search a annotations by last modified date in FileNet Engine
	 * @param Date
	 * @return JSONObject
	 * @throws EDRMException 
	 * @throws ParseException 
	 * @throws JSONException 
	 * @throws EngineRuntimeException 
	 * @throws IOException 
	 */
	public JSONObject annotationsSearchByDate(JSONObject jsonObject)throws EDRMException, EngineRuntimeException, JSONException, ParseException, IOException;
	
}
