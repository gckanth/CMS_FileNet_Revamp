package com.cms.edrm.filenet.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadJSONFile {

	static Logger LOGGER = LoggerFactory.getLogger(ReadJSONFile.class);
	
	public static JSONObject addDocument(){
		JSONObject jsonObject = null;
		try{
			String fileName = "src/main/resources/addDocument.json";
			// Reading JSON file
			File file = new File(fileName);
		    String content = FileUtils.readFileToString(file, "utf-8");
		    // Convert JSON string to JSONObject
		    jsonObject = new JSONObject(content);
		}
		catch(JSONException e){
			LOGGER.error("JSONException ::: "+e);
		} catch (IOException e) {
			LOGGER.error("IOException ::: "+e);
		}
		return jsonObject;
	}
	
	public static JSONObject updateDocument(){
		JSONObject jsonObject = null;
		try{
			String fileName = "src/main/resources/documentUpdate.json";
			// Reading JSON file
			File file = new File(fileName);
		    String content = FileUtils.readFileToString(file, "utf-8");
		    // Convert JSON string to JSONObject
		    jsonObject = new JSONObject(content);
		}
		catch(JSONException e){
			LOGGER.error("JSONException ::: "+e);
		} catch (IOException e) {
			LOGGER.error("IOException ::: "+e);
		}
		return jsonObject;
	}
	
	public static JSONObject documentLinking(){
		JSONObject jsonObject = null;
		try{
			String fileName = "src/main/resources/documentLink.json";
			// Reading JSON file
			File file = new File(fileName);
		    String content = FileUtils.readFileToString(file, "utf-8");
		    // Convert JSON string to JSONObject
		    jsonObject = new JSONObject(content);
		}
		catch(JSONException e){
			LOGGER.error("JSONException ::: "+e);
		} catch (IOException e) {
			LOGGER.error("IOException ::: "+e);
		}
		return jsonObject;
	}
	
	public static JSONObject createDocumentClass() {
		JSONObject jsonObject = null;
		try{
			String fileName = "src/main/resources/addDocumentClass.json";
			// Reading JSON file
			File file = new File(fileName);
		    String content = FileUtils.readFileToString(file, "utf-8");
		    // Convert JSON string to JSONObject
		    jsonObject = new JSONObject(content);
		}
		catch(JSONException e){
			LOGGER.error("JSONException ::: "+e);
		} catch (IOException e) {
			LOGGER.error("IOException ::: "+e);
		}
		return jsonObject;
	}
	public static JSONObject createPropertyDefinition() {
		JSONObject jsonObject = null;
		try{
			String fileName = "src/main/resources/createPropertyDefinition.json";
			// Reading JSON file
			File file = new File(fileName);
		    String content = FileUtils.readFileToString(file, "utf-8");
		    // Convert JSON string to JSONObject
		    jsonObject = new JSONObject(content);
		}
		catch(JSONException e){
			LOGGER.error("JSONException ::: "+e);
		} catch (IOException e) {
			LOGGER.error("IOException ::: "+e);
		}
		return jsonObject;
	}
	public static JSONObject deletePropertyDefinition() {
		JSONObject jsonObject = null;
		try{
			String fileName = "src/main/resources/deletePropertyDefinition.json";
			// Reading JSON file
			File file = new File(fileName);
		    String content = FileUtils.readFileToString(file, "utf-8");
		    // Convert JSON string to JSONObject
		    jsonObject = new JSONObject(content);
		}
		catch(JSONException e){
			LOGGER.error("JSONException ::: "+e);
		} catch (IOException e) {
			LOGGER.error("IOException ::: "+e);
		}
		return jsonObject;
	}
	public static JSONObject createChoiceList() {
		JSONObject jsonObject = null;
		try{
			String fileName = "src/main/resources/createChoiceList.json";
			// Reading JSON file
			File file = new File(fileName);
		    String content = FileUtils.readFileToString(file, "utf-8");
		    // Convert JSON string to JSONObject
		    jsonObject = new JSONObject(content);
		}
		catch(JSONException e){
			LOGGER.error("JSONException ::: "+e);
		} catch (IOException e) {
			LOGGER.error("IOException ::: "+e);
		}
		return jsonObject;
	}
	public static JSONObject updateChoiceListItems() {
		JSONObject jsonObject = null;
		try{
			String fileName = "src/main/resources/updateChoiceListItems.json";
			// Reading JSON file
			File file = new File(fileName);
		    String content = FileUtils.readFileToString(file, "utf-8");
		    // Convert JSON string to JSONObject
		    jsonObject = new JSONObject(content);
		}
		catch(JSONException e){
			LOGGER.error("JSONException ::: "+e);
		} catch (IOException e) {
			LOGGER.error("IOException ::: "+e);
		}
		return jsonObject;
	}
	public static JSONObject deleteChoiceListItems() {
		JSONObject jsonObject = null;
		try{
			String fileName = "src/main/resources/deleteChoiceListItems.json";
			// Reading JSON file
			File file = new File(fileName);
		    String content = FileUtils.readFileToString(file, "utf-8");
		    // Convert JSON string to JSONObject
		    jsonObject = new JSONObject(content);
		}
		catch(JSONException e){
			LOGGER.error("JSONException ::: "+e);
		} catch (IOException e) {
			LOGGER.error("IOException ::: "+e);
		}
		return jsonObject;
	}
	public static JSONObject createTask() {
		JSONObject jsonObject = null;
		try{
			String fileName = "src/main/resources/createTask.json";
			// Reading JSON file
			File file = new File(fileName);
		    String content = FileUtils.readFileToString(file, "utf-8");
		    // Convert JSON string to JSONObject
		    jsonObject = new JSONObject(content);
		}
		catch(JSONException e){
			LOGGER.error("JSONException ::: "+e);
		} catch (IOException e) {
			LOGGER.error("IOException ::: "+e);
		}
		return jsonObject;
	}
	public static JSONObject updateTask() {
		JSONObject jsonObject = null;
		try{
			String fileName = "src/main/resources/updateTask.json";
			// Reading JSON file
			File file = new File(fileName);
		    String content = FileUtils.readFileToString(file, "utf-8");
		    // Convert JSON string to JSONObject
		    jsonObject = new JSONObject(content);
		}
		catch(JSONException e){
			LOGGER.error("JSONException ::: "+e);
		} catch (IOException e) {
			LOGGER.error("IOException ::: "+e);
		}
		return jsonObject;
	}
	public static JSONObject deleteDocument() {
		JSONObject jsonObject = null;
		try{
			String fileName = "src/main/resources/deleteDocument.json";
			// Reading JSON file
			File file = new File(fileName);
		    String content = FileUtils.readFileToString(file, "utf-8");
		    // Convert JSON string to JSONObject
		    jsonObject = new JSONObject(content);
		}
		catch(JSONException e){
			LOGGER.error("JSONException ::: "+e);
		} catch (IOException e) {
			LOGGER.error("IOException ::: "+e);
		}
		return jsonObject;
	}
	public static JSONObject createMarkingSet() {
		JSONObject jsonObject = null;
		try{
			String fileName = "src/main/resources/createMarkingSet.json";
			// Reading JSON file
			File file = new File(fileName);
		    String content = FileUtils.readFileToString(file, "utf-8");
		    // Convert JSON string to JSONObject
		    jsonObject = new JSONObject(content);
		}
		catch(JSONException e){
			LOGGER.error("JSONException ::: "+e);
		} catch (IOException e) {
			LOGGER.error("IOException ::: "+e);
		}
		return jsonObject;
	}
	public static JSONObject updateMarkingSet() {
		JSONObject jsonObject = null;
		try{
			String fileName = "src/main/resources/updateMarkingSet.json";
			// Reading JSON file
			File file = new File(fileName);
		    String content = FileUtils.readFileToString(file, "utf-8");
		    // Convert JSON string to JSONObject
		    jsonObject = new JSONObject(content);
		}
		catch(JSONException e){
			LOGGER.error("JSONException ::: "+e);
		} catch (IOException e) {
			LOGGER.error("IOException ::: "+e);
		}
		return jsonObject;
	}
	public static JSONObject createFolder() {
		JSONObject jsonObject = null;
		try{
			String fileName = "src/main/resources/folderCreation.json";
			// Reading JSON file
			File file = new File(fileName);
		    String content = FileUtils.readFileToString(file, "utf-8");
		    // Convert JSON string to JSONObject
		    jsonObject = new JSONObject(content);
		}
		catch(JSONException e){
			LOGGER.error("JSONException ::: "+e);
		} catch (IOException e) {
			LOGGER.error("IOException ::: "+e);
		}
		return jsonObject;
	}
	public static JSONObject readMasterDataSyncJSON() {
		JSONObject jsonObject = null;
		try{
			String fileName = "src/main/resources/masterDataSync.json";
			// Reading JSON file
			File file = new File(fileName);
		    String content = FileUtils.readFileToString(file, "utf-8");
		    // Convert JSON string to JSONObject
		    jsonObject = new JSONObject(content);
		}
		catch(JSONException e){
			LOGGER.error("JSONException ::: "+e);
		} catch (IOException e) {
			LOGGER.error("IOException ::: "+e);
		}
		return jsonObject;
	}
	public static JSONObject readDocumentClassSyncJSON() {
		JSONObject jsonObject = null;
		try{
			String fileName = "src/main/resources/documentClassSync.json";
			// Reading JSON file
			File file = new File(fileName);
		    String content = FileUtils.readFileToString(file, "utf-8");
		    // Convert JSON string to JSONObject
		    jsonObject = new JSONObject(content);
		}
		catch(JSONException e){
			LOGGER.error("JSONException ::: "+e);
		} catch (IOException e) {
			LOGGER.error("IOException ::: "+e);
		}
		return jsonObject;
	}
	public static JSONObject readDocumentObjectsSyncJSON() {
		JSONObject jsonObject = null;
		try{
			String fileName = "src/main/resources/documentObjectsSync.json";
			// Reading JSON file
			File file = new File(fileName);
		    String content = FileUtils.readFileToString(file, "utf-8");
		    // Convert JSON string to JSONObject
		    jsonObject = new JSONObject(content);
		}
		catch(JSONException e){
			LOGGER.error("JSONException ::: "+e);
		} catch (IOException e) {
			LOGGER.error("IOException ::: "+e);
		}
		return jsonObject;
	}
	public static JSONObject readFolderObjectsSyncJSON() {
		JSONObject jsonObject = null;
		try{
			String fileName = "src/main/resources/folderObjectsSync.json";
			// Reading JSON file
			File file = new File(fileName);
		    String content = FileUtils.readFileToString(file, "utf-8");
		    // Convert JSON string to JSONObject
		    jsonObject = new JSONObject(content);
		}
		catch(JSONException e){
			LOGGER.error("JSONException ::: "+e);
		} catch (IOException e) {
			LOGGER.error("IOException ::: "+e);
		}
		return jsonObject;
	}
	public static JSONObject readMarkingSetSyncJSON() {
		JSONObject jsonObject = null;
		try{
			String fileName = "src/main/resources/markingSetSync.json";
			// Reading JSON file
			File file = new File(fileName);
		    String content = FileUtils.readFileToString(file, "utf-8");
		    // Convert JSON string to JSONObject
		    jsonObject = new JSONObject(content);
		}
		catch(JSONException e){
			LOGGER.error("JSONException ::: "+e);
		} catch (IOException e) {
			LOGGER.error("IOException ::: "+e);
		}
		return jsonObject;
	}
	public static JSONObject readAnnotationsSyncJSON() {
		JSONObject jsonObject = null;
		try{
			String fileName = "src/main/resources/annotationsSync.json";
			// Reading JSON file
			File file = new File(fileName);
		    String content = FileUtils.readFileToString(file, "utf-8");
		    // Convert JSON string to JSONObject
		    jsonObject = new JSONObject(content);
		}
		catch(JSONException e){
			LOGGER.error("JSONException ::: "+e);
		} catch (IOException e) {
			LOGGER.error("IOException ::: "+e);
		}
		return jsonObject;
	}
	public static JSONObject readDataAuditSyncJSON() {
		JSONObject jsonObject = null;
		try{
			String fileName = "src/main/resources/dataAuditSync.json";
			// Reading JSON file
			File file = new File(fileName);
		    String content = FileUtils.readFileToString(file, "utf-8");
		    // Convert JSON string to JSONObject
		    jsonObject = new JSONObject(content);
		}
		catch(JSONException e){
			LOGGER.error("JSONException ::: "+e);
		} catch (IOException e) {
			LOGGER.error("IOException ::: "+e);
		}
		return jsonObject;
	}

	public static JSONObject updateFolderJSON() {
		JSONObject jsonObject = null;
		try{
			String fileName = "src/main/resources/folderUpdate.json";
			// Reading JSON file
			File file = new File(fileName);
		    String content = FileUtils.readFileToString(file, "utf-8");
		    // Convert JSON string to JSONObject
		    jsonObject = new JSONObject(content);
		}
		catch(JSONException e){
			LOGGER.error("JSONException ::: "+e);
		} catch (IOException e) {
			LOGGER.error("IOException ::: "+e);
		}
		return jsonObject;
	}
	public static JSONObject deleteFolderJSON() {
		JSONObject jsonObject = null;
		try{
			String fileName = "src/main/resources/deleteFolder.json";
			// Reading JSON file
			File file = new File(fileName);
		    String content = FileUtils.readFileToString(file, "utf-8");
		    // Convert JSON string to JSONObject
		    jsonObject = new JSONObject(content);
		}
		catch(JSONException e){
			LOGGER.error("JSONException ::: "+e);
		} catch (IOException e) {
			LOGGER.error("IOException ::: "+e);
		}
		return jsonObject;
	}
	public static JSONObject deleteTaskJSON() {
		JSONObject jsonObject = null;
		try{
			String fileName = "src/main/resources/deleteTask.json";
			// Reading JSON file
			File file = new File(fileName);
		    String content = FileUtils.readFileToString(file, "utf-8");
		    // Convert JSON string to JSONObject
		    jsonObject = new JSONObject(content);
		}
		catch(JSONException e){
			LOGGER.error("JSONException ::: "+e);
		} catch (IOException e) {
			LOGGER.error("IOException ::: "+e);
		}
		return jsonObject;
	}
	public static JSONObject moveFolderJSON() {
		JSONObject jsonObject = null;
		try{
			String fileName = "src/main/resources/moveFolder.json";
			// Reading JSON file
			File file = new File(fileName);
		    String content = FileUtils.readFileToString(file, "utf-8");
		    // Convert JSON string to JSONObject
		    jsonObject = new JSONObject(content);
		}
		catch(JSONException e){
			LOGGER.error("JSONException ::: "+e);
		} catch (IOException e) {
			LOGGER.error("IOException ::: "+e);
		}
		return jsonObject;
	}
	public static JSONObject deLinkDocumentJSON() {
		JSONObject jsonObject = null;
		try{
			String fileName = "src/main/resources/deLinkDocument.json";
			// Reading JSON file
			File file = new File(fileName);
		    String content = FileUtils.readFileToString(file, "utf-8");
		    // Convert JSON string to JSONObject
		    jsonObject = new JSONObject(content);
		}
		catch(JSONException e){
			LOGGER.error("JSONException ::: "+e);
		} catch (IOException e) {
			LOGGER.error("IOException ::: "+e);
		}
		return jsonObject;
	}
}
