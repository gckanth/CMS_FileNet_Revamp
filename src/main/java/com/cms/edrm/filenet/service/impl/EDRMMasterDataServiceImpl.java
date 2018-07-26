package com.cms.edrm.filenet.service.impl;

import java.util.Iterator;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cms.edrm.filenet.constants.EDRMServiceConstants;
import com.cms.edrm.filenet.exception.EDRMException;
import com.cms.edrm.filenet.service.EDRMMasterDataService;
import com.cms.edrm.filenet.util.CommonServicesUtil;
import com.filenet.api.admin.ChoiceList;
import com.filenet.api.collection.ChoiceListSet;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.apiimpl.core.ChoiceImpl;
/**
 * 
 * Class which implements the add and update Master Data [choicelist(lookup)] services
 *
 */
public class EDRMMasterDataServiceImpl implements EDRMMasterDataService {
	private static final Logger LOGGER = LoggerFactory.getLogger(EDRMMasterDataServiceImpl.class);
	/**
	 * Method is to get choicelist(Master Data)
	 * @param Json masterDataDetails lastsync date
	 * @return Json response object
	 * @throws EngineRuntimeException
	 * @throws JSONException
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public JSONObject getMasterData(JSONObject masterDataDetails)
			throws EDRMException {
		if (LOGGER.isDebugEnabled()) 
		{
			LOGGER.debug("Start :: getMasterData ::  EDRMMasterDataServiceImpl");
		}
		JSONObject recordResultObject = null;
		FnAuthDataProviderImpl connectionImpl = null;
		CommonServicesUtil commonServicesUtil = new CommonServicesUtil();
		try {
			connectionImpl = new FnAuthDataProviderImpl();
			ObjectStore objStore = connectionImpl.getObjectStore();
			//requesting parameters information from JSON object
			JSONObject requestDetailsJsonObject = (JSONObject) masterDataDetails.get(EDRMServiceConstants.REQUEST_DETAILS);
			String lastSyncDate = (String) requestDetailsJsonObject.get(EDRMServiceConstants.LAST_SYNC_DATE);
			String masterData = EDRMServiceConstants.MASTER_DATA;
			StringBuffer whereClause = new StringBuffer();
			//dynamically building a query
			whereClause = commonServicesUtil.buildDateTypeQuery(EDRMServiceConstants.DATE_LAST_MODIFIED, lastSyncDate, whereClause);
			SearchSQL searchSql = new SearchSQL();
			searchSql.setFromClauseInitialValue(EDRMServiceConstants.CHOICE_LIST, null, true);
			if (whereClause.toString().trim().length() > 0) {
				searchSql.setWhereClause(whereClause.toString());
			}
			SearchScope searchScope = new SearchScope(objStore);
			//fetching of choice list objects
			ChoiceListSet choiceListSet = (ChoiceListSet) searchScope.fetchObjects(searchSql, null, null, false);
			Iterator iterator = choiceListSet.iterator();
			recordResultObject = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			while (iterator.hasNext()) {
				ChoiceList choiceList = (ChoiceList) iterator.next();
				JSONObject resultJSON = new JSONObject();
				String name = choiceList.get_Name();
				StringTokenizer masterDataTokens = new StringTokenizer(masterData, ",");
				while(masterDataTokens.hasMoreTokens()){
					String masterDataTokenItem = masterDataTokens.nextToken();
					masterDataTokenItem = masterDataTokenItem.replace(" ", "");
					if(name.equalsIgnoreCase(masterDataTokenItem))
					{
						JSONArray choiceItemsArray = new JSONArray();
						com.filenet.api.collection.ChoiceList choicelistItems = null;
						choicelistItems = choiceList.get_ChoiceValues();
					    Iterator i = choicelistItems.iterator();
					    while (i.hasNext()) {
					    	JSONObject choiceItem = new JSONObject();
					        com.filenet.apiimpl.core.ChoiceImpl choice = (ChoiceImpl) i.next();
					        choiceItem.put(EDRMServiceConstants.DISPLAY_NAME, choice.get_DisplayName());
					        choiceItem.put(EDRMServiceConstants.OBJECT_VALUE, choice.get_ChoiceStringValue());
					        choiceItemsArray.put(choiceItem);
					    }
					    resultJSON.put(EDRMServiceConstants.EDRM_LIST_VALUES, choiceItemsArray);
					    resultJSON.put(EDRMServiceConstants.CREATOR, choiceList.get_Creator());
					    resultJSON.put(EDRMServiceConstants.DATE_CREATED, choiceList.get_DateCreated());
					    resultJSON.put(EDRMServiceConstants.DATE_LAST_MODIFIED, choiceList.get_DateLastModified());
					    resultJSON.put(EDRMServiceConstants.NAME, name);
					    resultJSON.put(EDRMServiceConstants.PROPERTY_ID, choiceList.get_Id().toString());
						jsonArray.put(resultJSON);
					}
				}
			}
			//success response from FileNet
			recordResultObject.put(EDRMServiceConstants.EDRM_LIST_DETAILS, jsonArray);
		}  catch (EngineRuntimeException engineRuntimeException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.ENGINE_RUNTIME_EXCEPTION,
					engineRuntimeException.getExceptionCode().getErrorId(), engineRuntimeException.getMessage());
		} catch (JSONException jsonException) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.JSON_EXCEPTION, EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					jsonException.getMessage());
		} catch (Exception exception) {
			return new EDRMException().generateExceptionDetails(EDRMServiceConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
					EDRMServiceConstants.GENERAL_EXCEPTION, EDRMServiceConstants.STATUS_CODE_NO_CONTENT,
					exception.getMessage());
		} finally {
			connectionImpl.doSignOff();
		}
		return recordResultObject;
	}
}
