package com.cms.edrm.filenet;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.cms.edrm.filenet.exception.EDRMException;
import com.cms.edrm.filenet.pojo.EDRMResponseDTO;
import com.cms.edrm.filenet.service.EDRMDocumentService;
import com.cms.edrm.filenet.service.impl.EDRMDocumentServiceImpl;
import com.cms.edrm.filenet.service.impl.EDRMFolderServiceImpl;
import com.cms.edrm.filenet.service.impl.EDRMMasterDataServiceImpl;
import com.cms.edrm.filenet.service.impl.EDRMPropertiesServiceImpl;
import com.cms.edrm.filenet.service.impl.EDRMSearchServiceImpl;
import com.cms.edrm.filenet.service.impl.EDRMTaskServiceImpl;
import com.cms.edrm.filenet.util.ReadJSONFile;

/**
 * 
 * Class which performs all the fileNet utility services
 *
 */
public class EDRMFilenetAPI 
{

	private final static Logger LOGGER = Logger.getLogger(EDRMFilenetAPI.class);
	JSONObject json = new JSONObject();
	JSONObject resultObject = null;
	JSONObject resultExceptionObject = null;
	JSONObject responseObject = new JSONObject();
	/**
	 * Method is to add document into fileNet
	 * @param Json Document propertiesList
	 * @return Json response object
	 * @throws EDRMException 
	 */
	public JSONObject addDocument(JSONObject propertiesList){
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Start :: addDocument :: CMS EDRMFilenetServices ");
		}
		EDRMDocumentService docService = new EDRMDocumentServiceImpl();
		JSONObject responseJson = new JSONObject();
		try {
			responseJson = docService.addDocument(propertiesList);
		} 
		catch (EDRMException runtimeException) {
			LOGGER.error("Exception :: addDocument :: EDRMFilenetServices "+runtimeException);
		} 
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("End :: addDocument :: EDRMFilenetServices ");
		}

		return responseJson; 
	}
	/**
	 * Method is to update the meta data for an exsiting document
	 * @param Json Document propertiesList
	 * @return Json response object
	 * @throws EDRMException 
	 */
	public JSONObject  updateDocument(JSONObject propertiesList) throws EDRMException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Start :: updateDocument :: EDRMFilenetServices ");
		}
		EDRMDocumentService updateDocumentService = new EDRMDocumentServiceImpl();
		JSONObject responseJson = new JSONObject();
		try {
			responseJson = updateDocumentService.updateDocument(propertiesList);
		} 
		catch (EDRMException runtimeException) {
			LOGGER.error("Exception :: updateDocument :: EDRMFilenetServices "+runtimeException);
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("End :: updateDocument :: EDRMFilenetServices ");
		}
		return responseJson;
	}
	/**
	 * Method is to update the meta data for an exsiting document
	 * @param Json Document propertiesList
	 * @return Json response object
	 * @throws EDRMException 
	 */
	public JSONObject  deleteDocument(JSONObject propertiesList) throws EDRMException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Start :: deleteDocument :: EDRMFilenetServices ");
		}
		EDRMDocumentService updateDocumentService = new EDRMDocumentServiceImpl();
		JSONObject responseJson = new JSONObject();
		try {
			responseJson = updateDocumentService.deleteDocument(propertiesList);
		} 
		catch (EDRMException runtimeException) {
			LOGGER.error("Exception :: deleteDocument :: EDRMFilenetServices "+runtimeException);
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("End :: deleteDocument :: EDRMFilenetServices ");
		}
		return responseJson;
	}
	/**
	 * Method is to link documents
	 * @param Json Document propertiesList
	 * @return Json response object
	 * @throws EDRMException 
	 */
	public JSONObject  documentLinking(JSONObject propertiesList) throws EDRMException	{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Start :: documentLinking :: EDRMFilenetServices ");
		}
		EDRMDocumentService impl = new EDRMDocumentServiceImpl();
		JSONObject responseJson = new JSONObject();
		try {
			responseJson = impl.documentLinking(propertiesList);
		} 
		catch (EDRMException runtimeException) {
			LOGGER.error("Exception :: documentLinking :: EDRMFilenetServices "+runtimeException);
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("End :: documentLinking :: EDRMFilenetServices ");
		}
		return responseJson;
	}
	/**
	 * Method is to link documents
	 * @param Json Document propertiesList
	 * @return Json response object
	 * @throws EDRMException 
	 */
	public JSONObject  deLinkDocument(JSONObject propertiesList) throws EDRMException	{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Start :: deLinkDocument :: EDRMFilenetServices ");
		}
		EDRMDocumentService impl = new EDRMDocumentServiceImpl();
		JSONObject responseJson = new JSONObject();
		try {
			responseJson = impl.deLinkDocument(propertiesList);
		} 
		catch (EDRMException runtimeException) {
			LOGGER.error("Exception :: deLinkDocument :: EDRMFilenetServices "+runtimeException);
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("End :: deLinkDocument :: EDRMFilenetServices ");
		}
		return responseJson;
	}
	/**
	 * Method is to create Document Class
	 * @param Json Document Class propertiesList
	 * @return Json response object
	 * @throws EDRMException 
	 */
	public JSONObject createDocumentClass(JSONObject propertiesList) throws EDRMException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Start :: createDocumentClass :: EDRMFilenetServices ");
		}
		EDRMDocumentService impl = new EDRMDocumentServiceImpl();
		JSONObject responseJson = new JSONObject();
		try {
			responseJson = impl.createDocumentClass(propertiesList);
		} 
		catch (EDRMException runtimeException) {
			LOGGER.error("Exception :: createDocumentClass :: EDRMFilenetServices "+runtimeException);
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("End :: createDocumentClass :: EDRMFilenetServices ");
		}
		return responseJson;
	}
	/**
	 * Method for creating a  property definition in class definition in FileNet Engine
	 * @param Json Document propertyDefDetails
	 * @return Json response object
	 * @throws EDRMException 
	 */
	public JSONObject createPropertyDefinition(JSONObject propertiesList) throws EDRMException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Start :: createPropertyDefinition :: EDRMFilenetServices ");
		}
		EDRMPropertiesServiceImpl impl = new EDRMPropertiesServiceImpl();
		JSONObject responseJson = new JSONObject();
		try {
			responseJson = impl.createPropertyDefinition(propertiesList);
		} 
		catch (EDRMException runtimeException) {
			LOGGER.error("Exception :: createPropertyDefinition :: EDRMFilenetServices "+runtimeException);
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("End :: createPropertyDefinition :: EDRMFilenetServices ");
		}
		return responseJson;
	}
	/**
	 * Method for deleting a  property definition from class definition in FileNet Engine
	 * @param Json Document propertyDefDetails
	 * @return Json response object
	 * @throws EDRMException 
	 */
	public JSONObject deletePropertyDefinition(JSONObject propertiesList) throws EDRMException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Start :: deletePropertyDefinition :: EDRMFilenetServices ");
		}
		EDRMPropertiesServiceImpl impl = new EDRMPropertiesServiceImpl();
		JSONObject responseJson = new JSONObject();
		try {
			responseJson = impl.deletePropertyDefinition(propertiesList);
		} 
		catch (EDRMException runtimeException) {
			LOGGER.error("Exception :: deletePropertyDefinition :: EDRMFilenetServices "+runtimeException);
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("End :: deletePropertyDefinition :: EDRMFilenetServices ");
		}
		return responseJson;
	}
	/**
	 * Method for creating a choice list  in FileNet Engine
	 * @param Json propDetails choicelist details 
	 * @return Json response object
	 * @throws EDRMException 
	 */
	public JSONObject createChoiceList(JSONObject propertiesList) throws EDRMException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Start :: createChoiceList :: EDRMFilenetServices ");
		}
		EDRMPropertiesServiceImpl impl = new EDRMPropertiesServiceImpl();
		JSONObject responseJson = new JSONObject();
		try {
			responseJson = impl.createChoiceList(propertiesList);
		} 
		catch (EDRMException runtimeException) {
			LOGGER.error("Exception :: createChoiceList :: EDRMFilenetServices "+runtimeException);
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("End :: createChoiceList :: EDRMFilenetServices ");
		}
		return responseJson;
	}
	/**
	 * Method for updating a choice list items in FileNet Engine
	 * @param Json propDetails choicelist details 
	 * @return Json response object
	 * @throws EDRMException 
	 */
	public JSONObject updateChoiceListItems(JSONObject propertiesList) throws EDRMException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Start :: updateChoiceListItems :: EDRMFilenetServices ");
		}
		EDRMPropertiesServiceImpl impl = new EDRMPropertiesServiceImpl();
		JSONObject responseJson = new JSONObject();
		try {
			responseJson = impl.updateChoiceListItems(propertiesList);
		} 
		catch (EDRMException runtimeException) {
			LOGGER.error("Exception :: updateChoiceListItems :: EDRMFilenetServices "+runtimeException);
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("End :: updateChoiceListItems :: EDRMFilenetServices ");
		}
		return responseJson;
	}
	/**
	 * Method for deleting a choice list items in FileNet Engine
	 * @param Json propDetails choicelist details 
	 * @return dto
	 * @throws EDRMException 
	 */
	public Object deleteChoiceListItems(JSONObject propertiesList) throws EDRMException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Start :: deleteChoiceListItems :: EDRMFilenetServices ");
		}
		EDRMPropertiesServiceImpl impl = new EDRMPropertiesServiceImpl();
		Object dto = new EDRMResponseDTO();
		try {
			dto = impl.deleteChoiceListItems(propertiesList);
		} 
		catch (EDRMException runtimeException) {
			LOGGER.error("Exception :: deleteChoiceListItems :: EDRMFilenetServices "+runtimeException);
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("End :: deleteChoiceListItems :: EDRMFilenetServices ");
		}
		return dto;
	}
	/**
	 * Method for create Custom Object in FileNet Engine
	 * @param Json propDetails choicelist details 
	 * @return Json response object
	 * @throws EDRMException 
	 */
	public JSONObject createTask(JSONObject propertiesList) throws EDRMException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Start :: createTask :: EDRMFilenetServices ");
		}
		EDRMTaskServiceImpl impl = new EDRMTaskServiceImpl();
		JSONObject responseJson = new JSONObject();
		try {
			responseJson = impl.createTasks(propertiesList);
		} 
		catch (EDRMException runtimeException) {
			LOGGER.error("Exception :: createTask :: EDRMFilenetServices "+runtimeException);
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("End :: createTask :: EDRMFilenetServices ");
		}
		return responseJson;
	}
	/**
	 * Method for update Custom Object in FileNet Engine
	 * @param Json propDetails choicelist details 
	 * @return Json response object
	 * @throws EDRMException 
	 */
	public JSONObject updateTask(JSONObject propertiesList) throws EDRMException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Start :: updateTask :: EDRMFilenetServices ");
		}
		EDRMTaskServiceImpl impl = new EDRMTaskServiceImpl();
		JSONObject responseJson = new JSONObject();
		try {
			responseJson = impl.updateTasks(propertiesList);
		} 
		catch (EDRMException runtimeException) {
			LOGGER.error("Exception :: updateTask :: EDRMFilenetServices "+runtimeException);
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("End :: updateTask :: EDRMFilenetServices ");
		}
		return responseJson;
	}
	/**
	 * Method for create marking set in FileNet Engine
	 * @param Json propDetails marking set details 
	 * @return Json response object
	 * @throws EDRMException 
	 */
	public JSONObject createMarkingSet(JSONObject propertiesList) throws EDRMException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Start :: createMarkingSet :: EDRMFilenetServices ");
		}
		EDRMPropertiesServiceImpl impl = new EDRMPropertiesServiceImpl();
		JSONObject responseJson = new JSONObject();
		try {
			responseJson = impl.createMarkingSet(propertiesList);
		} 
		catch (EDRMException runtimeException) {
			LOGGER.error("Exception :: createMarkingSet :: EDRMFilenetServices "+runtimeException);
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("End :: createMarkingSet :: EDRMFilenetServices ");
		}
		return responseJson;
	}
	/**
	 * Method for update marking set in FileNet Engine
	 * @param Json propDetails marking set details 
	 * @return Json response object
	 * @throws EDRMException 
	 */
	public JSONObject updateMarkingSet(JSONObject propertiesList) throws EDRMException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Start :: updateMarkingSet :: EDRMFilenetServices ");
		}
		EDRMPropertiesServiceImpl impl = new EDRMPropertiesServiceImpl();
		JSONObject responseJson = new JSONObject();
		try {
			responseJson = impl.updateMarkingSet(propertiesList);
		} 
		catch (EDRMException runtimeException) {
			LOGGER.error("Exception :: updateMarkingSet :: EDRMFilenetServices "+runtimeException);
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("End :: updateMarkingSet :: EDRMFilenetServices ");
		}
		return responseJson;
	}
	/**
	 * Method for create folder in FileNet Engine
	 * @param Json propDetails folder details 
	 * @return Json response object
	 * @throws EDRMException 
	 */
	public JSONObject createFolder(JSONObject propertiesList) throws EDRMException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Start :: createFolder :: EDRMFilenetServices ");
		}
		EDRMFolderServiceImpl impl = new EDRMFolderServiceImpl();
		JSONObject responseJson = new JSONObject();
		try {
			responseJson = impl.createFolder(propertiesList);
		} 
		catch (EDRMException runtimeException) {
			LOGGER.error("Exception :: createFolder :: EDRMFilenetServices "+runtimeException);
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("End :: createFolder :: EDRMFilenetServices ");
		}
		return responseJson;
	}
	/**
	 * Method for update folder in FileNet Engine
	 * @param Json propDetails folder details 
	 * @return Json response object
	 * @throws EDRMException 
	 */
	public JSONObject updateFolder(JSONObject propertiesList) throws EDRMException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Start :: updateFolder :: EDRMFilenetServices ");
		}
		EDRMFolderServiceImpl impl = new EDRMFolderServiceImpl();
		JSONObject responseJson = new JSONObject();
		try {
			responseJson = impl.updateFolder(propertiesList);
		} 
		catch (EDRMException runtimeException) {
			LOGGER.error("Exception :: updateFolder :: EDRMFilenetServices "+runtimeException);
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("End :: updateFolder :: EDRMFilenetServices ");
		}
		return responseJson;
	}
	/**
	 * Method for delete folder in FileNet Engine
	 * @param Json propDetails folder details 
	 * @return Json response object
	 * @throws EDRMException 
	 */
	public JSONObject deleteFolder(JSONObject propertiesList) throws EDRMException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Start :: deleteFolder :: EDRMFilenetServices ");
		}
		EDRMFolderServiceImpl impl = new EDRMFolderServiceImpl();
		JSONObject responseJson = new JSONObject();
		try {
			responseJson = impl.deleteFolder(propertiesList);
		} 
		catch (EDRMException runtimeException) {
			LOGGER.error("Exception :: deleteFolder :: EDRMFilenetServices "+runtimeException);
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("End :: deleteFolder :: EDRMFilenetServices ");
		}
		return responseJson;
	}
	/**
	 * Method for delete custom object in FileNet Engine
	 * @param Json propDetails custom object details 
	 * @return Json response object
	 * @throws EDRMException 
	 */
	public JSONObject deleteTask(JSONObject propertiesList) throws EDRMException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Start :: deleteTask :: EDRMFilenetServices ");
		}
		EDRMTaskServiceImpl impl = new EDRMTaskServiceImpl();
		JSONObject responseJson = new JSONObject();
		try {
			responseJson = impl.deleteTask(propertiesList);
		} 
		catch (EDRMException runtimeException) {
			LOGGER.error("Exception :: deleteTask :: EDRMFilenetServices "+runtimeException);
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("End :: deleteTask :: EDRMFilenetServices ");
		}
		return responseJson;
	}
	/**
	 * Method for move folder in FileNet Engine
	 * @param Json propDetails folder details 
	 * @return Json response object
	 * @throws EDRMException 
	 */
	public JSONObject moveFolder(JSONObject propertiesList) throws EDRMException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Start :: moveFolder :: EDRMFilenetServices ");
		}
		EDRMFolderServiceImpl impl = new EDRMFolderServiceImpl();
		JSONObject responseJson = new JSONObject();
		try {
			responseJson = impl.moveOrRelocationFolder(propertiesList);
		} 
		catch (EDRMException runtimeException) {
			LOGGER.error("Exception :: moveFolder :: EDRMFilenetServices "+runtimeException);
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("End :: moveFolder :: EDRMFilenetServices ");
		}
		return responseJson;
	}
	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception{
		EDRMFilenetAPI eDRMFilenetAPI = new EDRMFilenetAPI();
		EDRMSearchServiceImpl impl = new EDRMSearchServiceImpl();
		EDRMMasterDataServiceImpl impl2 = new EDRMMasterDataServiceImpl();
		try {
			//JSONObject jsonObject = ReadJSONFile.addDocument();
			/*LOGGER.info(jsonObject);*/
			//JSONObject jsonObject = ReadJSONFile.createDocumentClass();
			//JSONObject jsonObject = ReadJSONFile.updateDocument();
			//JSONObject jsonObject = ReadJSONFile.documentLinking();
			JSONObject jsonObject = ReadJSONFile.createPropertyDefinition();
			//JSONObject jsonObject = ReadJSONFile.deletePropertyDefinition();
			//JSONObject jsonObject = ReadJSONFile.createChoiceList();
			//JSONObject jsonObject = ReadJSONFile.updateChoiceListItems();
			//JSONObject jsonObject = ReadJSONFile.deleteChoiceListItems();
			//JSONObject jsonObject = ReadJSONFile.createTask();
			//JSONObject jsonObject = ReadJSONFile.updateTask();
			//JSONObject jsonObject = ReadJSONFile.deleteDocument();
			//JSONObject jsonObject = ReadJSONFile.createMarkingSet();
			//JSONObject jsonObject = ReadJSONFile.createFolder();

			//JSONObject jsonObject = ReadJSONFile.updateFolderJSON();
			//JSONObject jsonObject = ReadJSONFile.deleteFolderJSON();
			//JSONObject jsonObject = ReadJSONFile.deleteTaskJSON();
			//JSONObject jsonObject = ReadJSONFile.moveFolderJSON();
			//JSONObject jsonObject = ReadJSONFile.updateMarkingSet();
			/*JSONObject jsonObject = ReadJSONFile.deLinkDocumentJSON();*/

			LOGGER.info("Final Output : "+eDRMFilenetAPI.createPropertyDefinition(jsonObject));

			/*JSONObject jsonObject = ReadJSONFile.readMasterDataSyncJSON();
			LOGGER.info("Final Output : "+impl2.getMasterData(jsonObject));*/

			/*JSONObject jsonObject = ReadJSONFile.readDocumentClassSyncJSON();
			LOGGER.info("Final Output : "+impl.documentClassSearchByDate(jsonObject));*/

			/*JSONObject jsonObject = ReadJSONFile.readDocumentObjectsSyncJSON();
			LOGGER.info("Final Output : "+impl.documentInstanceSearchByDate(jsonObject));*/

			/*JSONObject jsonObject = ReadJSONFile.readMarkingSetSyncJSON();
			LOGGER.info("Final Output : "+impl.markingSetSearchByDate(jsonObject));*/

			/*JSONObject jsonObject = ReadJSONFile.readAnnotationsSyncJSON();
			LOGGER.info("Final Output : "+impl.annotationsSearchByDate(jsonObject));*/

			/*JSONObject jsonObject = ReadJSONFile.readDataAuditSyncJSON();
			LOGGER.info("Final Output : "+impl.dataAuditedByDate(jsonObject));*/

			/*JSONObject jsonObject = ReadJSONFile.readFolderObjectsSyncJSON();
			LOGGER.info("Final Output : "+impl.folderSearchByDate(jsonObject));*/

		} catch (Exception exception) {
			throw new EDRMException(exception.getMessage(), exception);
		}
	}
}
