package com.cms.edrm.filenet.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ResourceLoader 
{
	/** static instance of the class */
	public static ResourceLoader _instance = null;
	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceLoader.class);
	/** hold the values read from the file */
	public Properties msgPropObj = new Properties();
	/**
	 * Constructor read and loads the file
	 * @throws EDRMException 
	 */
	public ResourceLoader(String propertyFileName){
		try {
			loadProperties(propertyFileName);
		} catch (Exception Exception) 
		{
			LOGGER.error("ExceptionMessage :: "+Exception.getMessage(),Exception);
		}
	}
	/**
	 * Restrict instantiation of a class to one object.
	 * @throws Exception 
	 */
	public static ResourceLoader getInstance(String propertyFileName)
	{
		if(_instance == null){
			_instance=new ResourceLoader(propertyFileName);
		}
		return _instance;		
	}
	/**
	 * Read the property file via InputStream and load the
	 * data into Properties object.
	 * @throws Exception 
	 */
	private void loadProperties(String propertyFileName) throws IOException{
		InputStream  inputStream  = null;
		try{
			inputStream = ResourceLoader.class.getClassLoader().getResourceAsStream(propertyFileName);  
			if(inputStream != null){
				msgPropObj.load(inputStream);
			}	
		}
		catch (IOException ioException) 
		{
			throw new IOException(ioException.getMessage(),ioException);
		}
		finally
		{
			if(inputStream != null)
			{
				try 
				{
					inputStream.close();
				} 
				catch (IOException e) 
				{
					LOGGER.error("Error while loading the propery file: "+e.getMessage(),e);
				}
			}
		}
	}
	/**
	 * Return the value of a property
	 * @param id	Property ID
	 * @return	value	value of the ID as String
	 * @throws Exception 
	 */
	public String getProperty(String propertyName){
		String value = "";
		if(msgPropObj != null)
		{
			value = msgPropObj.getProperty(propertyName);
			if(value != null)
			{
				value = value.trim();
			}else{
				LOGGER.error("The property not available : "+ propertyName);
			}
		}
		return value;
	}
	public String getProperty(String strId, String className)
	{
		String statement = "";
		if(msgPropObj!=null){
			statement = msgPropObj.getProperty(strId);
			if(statement!=null && !statement.equals("")){
				statement = statement.replace("{xxxxx}", className);
			}
		}
		return statement;
	}
}
