package com.cms.edrm.filenet.service;


import java.sql.SQLException;

import com.cms.edrm.filenet.exception.EDRMException;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.ObjectStore;
/**
 * 
 * Interface for EDRMService to manage ECM Service Connection,ObjectStore and Domain
 *
 */
public interface FnAuthDataProvider
{
	/**
	 * This method authenticates with the FileNet environment and return instance of Connection
	 * @param userName ---UserName of Authentication
	 * @param passWord ---Password of Authentication
	 * @return Connection instance
	 * @throws EDRMException 
	 */
	public Connection doSignOn() throws EDRMException;
	/**
	 * This method is to close the close Connection  of FileNet
	 * @param objConnection ---instance of Connection
	 * @param objDomain ---name of the Domain
	 * @throws EDRMException 
	 */
	public void doSignOff() throws EDRMException;
	/**
	 * This method is to get the instance of the domain based on the Connection
	 * @param _fnConnection -- instance of the Connection
	 * @return instance of the domain
	 * @throws EDRMException 
	 */
	public Domain doGetDomain()throws EDRMException;
	/**
	 * This method is to get the ObjectStore based on the domain
	 * @param osName -- name of the  ObjectStore
	 * @param _fnDomain --Instance of the Domain
	 * @return --Instance of the ObjectStore
	 * @throws EDRMException 
	 */
	public ObjectStore getObjectStore() throws EDRMException;
	/**
	 * This method is to get the Sql Server Connection based on the domain
	 * @param osName -- name of the  ObjectStore
	 * @param _fnDomain --Instance of the Domain
	 * @return --Instance of the ObjectStore
	 * @throws EDRMException 
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public java.sql.Connection getSqlServerConnection() throws EDRMException, ClassNotFoundException, SQLException;
}
