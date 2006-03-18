/*
 * Created on June 9, 2005
 * Last Modified : July 6, 2005.
 */

package edu.wustl.common.cde;

/**
 * @author mandar_deshmukh
 *
 */
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.PropertyConfigurator;

import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.cde.xml.XMLCDE;
import edu.wustl.common.cde.xml.impl.XMLCDEImpl;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.cadsr.domain.DataElement;
import gov.nih.nci.cadsr.domain.EnumeratedValueDomain;
import gov.nih.nci.cadsr.domain.PermissibleValue;
import gov.nih.nci.cadsr.domain.ValueDomain;
import gov.nih.nci.cadsr.domain.ValueDomainPermissibleValue;
import gov.nih.nci.cadsr.domain.impl.DataElementImpl;
import gov.nih.nci.system.applicationservice.ApplicationService;

public class CDEDownloader
{
	public static int MAX_SERVER_CONNECT_ATTEMPTS = Integer.parseInt(ApplicationProperties.getValue("max.server.connect.attempts"));
	public static int MAX_CDE_DOWNLOAD_ATTEMPTS = Integer.parseInt(ApplicationProperties.getValue("max.cde.download.attempts"));;
	private ApplicationService appService;

	public CDEDownloader() throws Exception
	{
		init();
	}
	
	private void init() throws Exception
	{
		// creates the passwordauthenticaton object to be used for establishing 
		// the connection with the databse server.
		setCDEConConfig();
		createPasswordAuthentication(CDEConConfig.proxyhostip, CDEConConfig.proxyport, CDEConConfig.username, CDEConConfig.password);
	}
	
	/**
	 * @param proxyhost is used to specify the host to connect 
	 * @param proxyport is used to specify the port to be used for connection
	 * @param username is used to specify the local username
	 * @param password is used to specify the local password
	 * @param dbserver is used to specify the database url to connect 
	 * @return returns true if the connection with the dtabase server is successful.
	 *  sets the ApplicationService object if successful.
	 * This method will be used for establishing the connection with the server.
	 */
	public void connect() throws Exception
	{
		int connectAttempts = 0;
		while(connectAttempts < MAX_SERVER_CONNECT_ATTEMPTS)
		{
			try
			{
				appService = ApplicationService.getRemoteInstance(CDEConConfig.dbserver);
				
				if(appService!=null)
					return;
			}
			catch (Exception conexp)
			{
				Logger.out.error("Connection Error: "+conexp.getMessage(), conexp);
			}
			connectAttempts++;
		}
		throw new Exception("Connection Error: Undable to connect to "+CDEConConfig.dbserver);
	}

	/**
	 * @param cdeCon It is the Configuration object. Contains all required
	 *  information  for connection.
	 * @param cdePublicId It is the public id of the cde to download.
	 * @param vocabularyName evs database to connect
	 * @return an CDE with all the PermissibleValues. 
	 * 
	 * @throws Exception
	 */
	public CDE loadCDE(XMLCDE xmlCDE) throws Exception
	{
	    Logger.out.debug("Downloading CDE "+xmlCDE.getName());
	    
	    int downloadAttempts = 0;
		while(downloadAttempts < MAX_CDE_DOWNLOAD_ATTEMPTS)
		{
			try
			{
				CDE resultCde = retrieveDataElement(xmlCDE);
				
				if(resultCde!=null)
					return resultCde;
			}
			catch (Exception conexp)
			{
				Logger.out.error("CDE Download Error: "+conexp.getMessage(), conexp);
			}
			downloadAttempts++;
		}
		throw new Exception("CDE Download Error: Undable to download CDE "+xmlCDE.getName());
	}//loadCDE

	/**
	 * @param CDEPublicID PublicID of the CDE to download
	 * @return the CDE if available or null if cde not available
	 * @throws Exception
	 */
	private CDE retrieveDataElement(XMLCDE xmlCDE) throws Exception
	{
		//Create the dataelement and set the dataEelement properties
		DataElement dataElementQuery = new DataElementImpl();
		
		boolean loadByPublicID = Boolean.getBoolean(ApplicationProperties.getValue("cde.load.by.publicid"));
		
		if(loadByPublicID)
			dataElementQuery.setPublicID(new Long(xmlCDE.getPublicId()));
		else
			dataElementQuery.setPreferredName(xmlCDE.getName());
		
		dataElementQuery.setLatestVersionIndicator("Yes");
		
		List resultList = appService.search(DataElement.class, dataElementQuery);
		
		// check if any cde exists with the given public id.
		if (resultList!=null && !resultList.isEmpty())
		{
			//retreive the Data Element for the given search condition
			DataElement dataElement = (DataElement) resultList.get(0);
			
			// create the cde object and set the values.
			CDEImpl cdeobj = new CDEImpl();
			
			cdeobj.setPublicId(dataElement.getPublicID().toString());
			cdeobj.setDefination(dataElement.getPreferredDefinition());
			cdeobj.setLongName(dataElement.getLongName());
			cdeobj.setVersion(dataElement.getVersion().toString());
			cdeobj.setPreferredName(dataElement.getPreferredName());
			//cdeobj.setDateLastModified(dataElement.getDateModified());
			
			Logger.out.debug("CDE Public Id : "+cdeobj.getPublicId());
			Logger.out.debug("CDE Def : "+cdeobj.getDefination());
			Logger.out.debug("CDE Long Name : "+cdeobj.getLongName());
			Logger.out.debug("CDE Version : "+cdeobj.getVersion());
			Logger.out.debug("CDE Perferred Name : "+cdeobj.getPreferredName());
//			Logger.out.debug("CDE Last Modified Date : "+cdeobj.getDateLastModified());
			
			//Access the permissible value.
			ValueDomain valueDomain = dataElement.getValueDomain();
			Logger.out.debug("valueDomain class : " + valueDomain.getClass());
			
			if(valueDomain instanceof EnumeratedValueDomain)
			{
				EnumeratedValueDomain enumValueDomain = (EnumeratedValueDomain)valueDomain;
				
			    Collection permissibleValueCollection = enumValueDomain.getValueDomainPermissibleValueCollection();
				Set permissibleValues = getPermissibleValues(permissibleValueCollection);
				cdeobj.setPermissibleValues(permissibleValues);
			}
			
			Logger.out.debug("Permissible Value Size: "+cdeobj.getPermissibleValues().size());
			return cdeobj;
		}
		else //no Data Element retreived
		{
			return null;
		}
	} // retrieveDataElements
	
	/**
	 * Returns the Set of Permissible values from the collection of value domains. 
	 * @param valueDomainCollection The value domain collection. 
	 * @param cde The CDE to which this permissible values belong. 
	 * @return the Set of Permissibel values from the collection of value domains.
	 */
	private Set getPermissibleValues(Collection valueDomainCollection)
	{
	    Logger.out.debug("Value Domain Size : "+valueDomainCollection.size());
	    
	    Set permissibleValuesSet = new HashSet();
	    
	    Iterator iterator = valueDomainCollection.iterator();
	    while(iterator.hasNext())
	    {
	        ValueDomainPermissibleValue valueDomainPermissibleValue = (ValueDomainPermissibleValue) iterator.next();
	        
	        //caDSR permissible value object
	        PermissibleValue permissibleValue = (PermissibleValue) valueDomainPermissibleValue.getPermissibleValue();
	        
	        //Create the instance of catissue permissible value
	        edu.wustl.common.cde.PermissibleValueImpl cachedPermissibleValue = new PermissibleValueImpl();
	        
	        cachedPermissibleValue.setConceptid(permissibleValue.getId());
	        cachedPermissibleValue.setValue(permissibleValue.getValue());

	        Logger.out.debug("Concept ID : "+cachedPermissibleValue.getConceptid());
	        Logger.out.debug("Value : "+cachedPermissibleValue.getValue());
	        permissibleValuesSet.add(cachedPermissibleValue);
	    }
	    return permissibleValuesSet;
	}
	
	/**
	 * @param proxyhost  url of the database to connect
	 * @param proxyport port to be used for connection
	 * @param username Username of the local system  
	 * @param password Password of the local system
	 * @throws Exception
	 * This method which accepts the local username,password, 
	 * proxy host and proxy port to create a PasswordAuthentication 
	 * that will be used for establishing the connection.
	 */
	private void createPasswordAuthentication(String proxyhost,
				String proxyport,String username,String password) throws Exception
	{
		/**
		 * username is a final variable for the username.  
		 */
		final String localusername = username;

		/**
		 * password is a final variable for the password.  
		 */
		final String localpassword = password;

		/**
		 * authenticator is an object of Authenticator used for 
		 * authentication of the username and password.   
		 */
		Authenticator authenticator = new Authenticator()
		{

			protected PasswordAuthentication getPasswordAuthentication()
			{
				// sets http authentication 
				return new PasswordAuthentication(localusername, localpassword.toCharArray());
			}
		};
		// setting the authenticator
		Authenticator.setDefault(authenticator);

		/**
		 * Checking the proxy port for validity
		 */
		boolean validnum = CommonUtilities.checknum(proxyport, 0, 0);

		if (validnum == false)
		{
			//Logger.out.info("Invalid Proxy Port: " + proxyport);
			throw new Exception("Invalid ProxyPort");
		}

		// setting the system settings
		System.setProperty("proxyHost", proxyhost);
		System.setProperty("proxyPort", proxyport);
	} //createPasswordAuthentication


	private void setCDEConConfig()
	{
		CDEConConfig.proxyhostip ="scproxy.persistent.co.in";
		CDEConConfig.proxyport = "80";
		CDEConConfig.password ="";
		CDEConConfig.username = "gautam_shetty";
		CDEConConfig.dbserver = ApplicationProperties.getValue("casdr.server");
	}

	public static void main(String []args) throws Exception
	{
		ApplicationProperties.initBundle("ApplicationResources");
		
		// to remove the Logger configuration from the file
		Variables.catissueHome = System.getProperty("user.dir");
		Logger.out = org.apache.log4j.Logger.getLogger("");
		PropertyConfigurator.configure(Variables.catissueHome+"/WEB-INF/src/"+"ApplicationResources.properties");
		
		XMLCDE xmlCDE = new XMLCDEImpl();
		xmlCDE.setName("PT_RACE_CAT" );
		xmlCDE.setPublicId("106" );
		
		CDEDownloader cdeDownloader = new CDEDownloader();
		cdeDownloader.connect();
		CDE cde = cdeDownloader.loadCDE(xmlCDE);
	} 	
} // class CDEDownloader