package com.cms.edrm.filenet.service.impl;

import java.sql.DriverManager;
import java.sql.SQLException;

import javax.security.auth.Subject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cms.edrm.filenet.constants.EDRMServiceConstants;
import com.cms.edrm.filenet.exception.EDRMException;
import com.cms.edrm.filenet.service.FnAuthDataProvider;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;
/**
 * 
 * Implementation Class for  AuthenticationProvider class
 *
 */
public class FnAuthDataProviderImpl implements FnAuthDataProvider {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(FnAuthDataProviderImpl.class);
	/**
	 * This method authenticates with the FileNet environment and return instance of Connection
	 * @param userName ---UserName of Authentication
	 * @param passWord ---Password of Authentication
	 * @return Connection instance
	 * @throws EDRMException 
	 */
	@Override
	public Connection doSignOn() throws EDRMException
	{
			String userName = EDRMServiceConstants.CONNECT_USER_NAME;
			String passWord = EDRMServiceConstants.CONNECT_PASSWORD;
		
			//creating the instance of the connection object
			Connection fnConnection = Factory.Connection.getConnection(EDRMServiceConstants.CONNECT_URL);
		    
		    if(userName != null && passWord != null) 
		    {       
		        // Associate the JAAS Subject with the UserContext
			    	Subject sub = UserContext.createSubject(fnConnection, userName, passWord, EDRMServiceConstants.SUBJECT_STANZANAME);
			    	UserContext fnUserContext = UserContext.get();
			        fnUserContext.pushSubject(sub);
		    }
		return fnConnection;
	}
	/**
	 * This method is to close the close Connection  of FileNet
	 * @param objConnection ---instance of Connection
	 * @param objDomain ---name of the Domain
	 * @throws EDRMException 
	 */
	@Override
	public void doSignOff() throws EDRMException 
	{
			UserContext userContext = UserContext.get();
			userContext.popSubject();
	}
	/**
	 * This method is to get the instance of the domain based on the Connection
	 * @param _fnConnection -- instance of the Connection
	 * @return instance of the domain
	 * @throws EDRMException 
	 */
	@Override
	public Domain doGetDomain() throws EDRMException
	{
		//Getting the instance of the fileNet domain
		Domain domain = Factory.Domain.fetchInstance(doSignOn(), null, null);
	    return domain;
	}
	/**
	 * This method is to get the ObjectStore based on the domain
	 * @param osName -- name of the  ObjectStore
	 * @param _fnDomain --Instance of the Domain
	 * @return --Instance of the ObjectStore
	 * @throws EDRMException 
	 */
	@Override
	public ObjectStore getObjectStore() throws EDRMException
	{
        //Getting the instance of the ObjectStore
		return Factory.ObjectStore.fetchInstance(doGetDomain(), EDRMServiceConstants.CONNECT_OBJECTSTORE, null);
	}
	/**
	 * This method is to get the SQL Connection
	 * @return --Object of the java.sql.Connection
	 * @throws EDRMException 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@Override
	public java.sql.Connection getSqlServerConnection() throws EDRMException, ClassNotFoundException, SQLException {
		String connectionUrl = EDRMServiceConstants.SQL_CONNECTION_STRING+";databaseName="+EDRMServiceConstants.SQL_DATABASE_NAME+";user="+EDRMServiceConstants.SQL_USERNAME+";password="+EDRMServiceConstants.SQL_PASSWORD+"";  
		Class.forName(EDRMServiceConstants.SQL_CONNECTION_CLASSNAME);
		java.sql.Connection con = DriverManager.getConnection(connectionUrl);
		return con;
	}
}
