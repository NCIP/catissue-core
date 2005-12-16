/*
 * Created on June 9, 2005
 * Last Modified : July 6, 2005.
 */

package edu.wustl.common.cde;

/**
 * @author mandar_deshmukh
 *
 */
import edu.wustl.common.cde.xml.XMLCDE;
import edu.wustl.common.cde.xml.XMLPermissibleValueType;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.cadsr.domain.DataElement;
import gov.nih.nci.cadsr.domain.EnumeratedValueDomain;
import gov.nih.nci.cadsr.domain.NonenumeratedValueDomain;
import gov.nih.nci.cadsr.domain.PermissibleValue;
import gov.nih.nci.cadsr.domain.ValueDomain;
import gov.nih.nci.cadsr.domain.ValueDomainPermissibleValue;
import gov.nih.nci.cadsr.domain.impl.DataElementImpl;
import gov.nih.nci.cadsr.domain.impl.ValueDomainImpl;
import gov.nih.nci.evs.domain.DescLogicConcept;
import gov.nih.nci.evs.query.EVSQuery;
import gov.nih.nci.evs.query.EVSQueryImpl;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

public class CDEDownloader
{

	private String vocabularyName;
//	private int limit;
	private ApplicationService appService;

	/**
	 * @param proxyhost is used to specify the host to connect 
	 * @param proxyport is used to specify the port to be used for connection
	 * @param username is used to specify the local username
	 * @param password is used to specify the local password
	 * @param dbserver is used to specify the database url to connect 
	 * @return returns true if the connection with the dtabase server is successful.
	 *  sets the ApplicationService object if successful.
	 *    
	 * This method will be used for establishing the connection with the server.
	 */
	private boolean connect(String proxyhost, String proxyport, 
							String username, String password,
							String dbserver)
	{
		try
		{
			// creates the passwordauthenticaton object to be used for establishing 
			// the connection with the databse server.
			createPasswordAuthentication(proxyhost, proxyport, username, password);

			System.out.println("appService");
			
			appService = ApplicationService.getRemoteInstance(dbserver);
			
		} // try
		catch (Exception conexp)
		{
			conexp.printStackTrace();
			Logger.out.error("1Error: " + conexp);
			return false;
		} // catch
		return true;
	} // connect

	/**
	 * @param cdeCon It is the Configuration object. Contains all required
	 *  information  for connection.
	 * @param cdePublicId It is the public id of the cde to download.
	 * @param vocabularyName evs database to connect
	 * @return an CDE with all the PermissibleValues. 
	 * 
	 * @throws Exception
	 */
	public CDE loadCDE(CDEConConfig cdeCon, XMLCDE xmlCDE ,
						String vocabularyName, int limit)
						throws Exception
	{
		boolean bCon = connect(cdeCon.getProxyhostip(), cdeCon.getProxyport(),
				cdeCon.getUsername(), cdeCon.getPassword(), cdeCon.getDbserver());
		if (bCon)
		{
			// to remove the Logger configuration from the file
			System.setProperty("catissue.home", "Logs");
			Logger.configure("ApplicationResources.properties");

			this.vocabularyName = vocabularyName;
			CDE resultCde = retrieveDataElement(xmlCDE);
			return resultCde;
		} // connection successful
		else
		{
			return null;
		} // connection failed
	} // loadCDE

	// this method returns the dataelement list

	/**
	 * @param CDEPublicID PublicID of the CDE to download
	 * @return the CDE if available or null if cde not available
	 * @throws Exception
	 */
	private CDE retrieveDataElement(XMLCDE xmlCDE) throws Exception
	{
		//Create the dataelement and set the dataEelement properties
		DataElement dataElementQuery = new DataElementImpl();
		dataElementQuery.setPublicID(new Long(xmlCDE.getPublicId()));
		dataElementQuery.setLatestVersionIndicator("Yes");

		//List resultList = appService.search(DataElement.class, dataElementQuery);
		List resultList = new ArrayList();
		resultList.add(dataElementQuery);
		
		// check if any cde exists with the given public id.
		if (!resultList.isEmpty())
		{
			// retreive the Data Element for the given publicid
			DataElement dataElement = (DataElement) resultList.get(0);
			
			System.out.println("dataElement ");
			System.out.println("PublicID "+dataElement.getPublicID());
			System.out.println("LongName "+dataElement.getLongName());
			
			
			// create the cde object and set the values.
			CDEImpl cdeobj = new CDEImpl();

			cdeobj.setPublicId(dataElement.getPublicID().toString());
			cdeobj.setDefination(dataElement.getPreferredDefinition());
			cdeobj.setLongName(dataElement.getLongName());
			//cdeobj.setVersion(dataElement.getVersion().toString());
			cdeobj.setPreferredName(dataElement.getPreferredName());

			Iterator iterator = xmlCDE.getXMLPermissibleValues().iterator();
			while(iterator.hasNext())
			{
				XMLPermissibleValueType  aXMLPermissibleValueType = (XMLPermissibleValueType)iterator.next();
				System.out.println("aXMLPermissibleValueType.getConceptCode() "+aXMLPermissibleValueType.getConceptCode());
				if(aXMLPermissibleValueType.getConceptCode().equals("PV"))
				{
					System.out.println("here");
					// get the PermissibleValues for the ValueDoamin of the Data Element
					List permissibleValuesList = retrievePermissibleValue(dataElementQuery,aXMLPermissibleValueType);
					
					Iterator pvIterator = permissibleValuesList.iterator();
					while(pvIterator.hasNext())
					{
						 pvIterator.next();
						//cdeobj.setPermissibleValues(new HashSet(permissibleValuesList));
					}

				}
			}
			return cdeobj;
		} // list not empty
		else // no Data Element retreived
		{
			return null;
		} // list empty
	} // retrieveDataElements

	/**
	 * @param dataElement Data Element for which Value Domain is to be retrieved.
	 * @return A List of ValueDomains
	 * @throws Exception
	 * OK
	 */
	private List retrieveValueDomain(DataElement dataElement) throws Exception
	{
		List resValueDomain = null;
		resValueDomain = appService.search("gov.nih.nci.cadsr.domain.ValueDomain", dataElement);
		return resValueDomain;
	} // retrieveValueDomain 

	/**
	 * @param lstValueDomain the value domain for which the Permissible Values are to be searched
	 * @return A List of the PermissibleValues for a Value Domain of the dataelement.
	 * @throws Exception
	 */
	private List retrievePermissibleValue(DataElement dataElement,XMLPermissibleValueType  xmlPermissibleValue)
			throws Exception
	{
		//get the Value Domain for the Data Element 
		List valueDomainList = retrieveValueDomain(dataElement); // valuedomain list
		System.out.println("valueDomainList "+valueDomainList.size());
		// list of permissible values for the given ValueDomain
		List pvList = new ArrayList();

		Iterator iterator = valueDomainList.iterator();
		while(iterator.hasNext())
		{
			System.out.println("1");
			ValueDomain valueDomain = (ValueDomainImpl) iterator.next();

			if(valueDomain instanceof EnumeratedValueDomain)//Enumerated
			{
				System.out.println("EnumeratedValueDomain ");
				//get the enumerated ValueDomain
				EnumeratedValueDomain enumValDom = (EnumeratedValueDomain) valueDomain;
				pvList = getPermissibleValues(enumValDom, xmlPermissibleValue);
			} 
			else //NonEnumerated
			{
				NonenumeratedValueDomain nonEnumValDom = (NonenumeratedValueDomain)valueDomain;
				pvList.add(nonEnumValDom);
			} 
		}
		return pvList;
	} // retrievePermissibleValueForValueDomainPermissibleValue


	private List getPermissibleValues(EnumeratedValueDomain enumValDom, XMLPermissibleValueType xmlPermissibleValue) throws Exception
	{
		List pvList = new ArrayList();
		System.out.println("enumValDom.getValueDomainPermissibleValueCollection().size() "+enumValDom.getValueDomainPermissibleValueCollection().size());
		
		// get the Collection of PermissibleValues for the ValueDomain 
		Iterator pvIterator = enumValDom.getValueDomainPermissibleValueCollection().iterator();

		while (pvIterator.hasNext())
		{
			System.out.println("pvIterator.hasNext()");
			// ValueDomainPermissibleValue for the enumValDom
			ValueDomainPermissibleValue valDomPerVal = (ValueDomainPermissibleValue) pvIterator.next();

			//PermissibleValues for the given VDPV
			List permissibleValueList = appService.search(PermissibleValue.class, valDomPerVal);
			System.out.println("permissibleValueList "+permissibleValueList.size());
			if (!permissibleValueList.isEmpty())
			{
				//collecting all the PermissibleValues
				for (int cnt = 0; cnt < permissibleValueList.size(); cnt++)
				{
					PermissibleValue permissibleValue = (PermissibleValue) permissibleValueList
							.get(cnt);
					pvList.add(permissibleValue);
					
					System.out.println("Value "+permissibleValue.getValue());
					System.out.println("Id "+permissibleValue.getId());
					/* String searchTerm = permissibleValue.getValue();
					System.out.println("SearchTerm : "+ searchTerm);
					List validValues = evsdata(searchTerm, limit);
					if (validValues!=null )
						pvList.addAll(validValues) ;*/
				} // for
			} // if
		} // while
		return pvList;
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
	private void createPasswordAuthentication(String proxyhost,String proxyport,
												String username,String password)
												throws Exception
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
			Logger.out.info("Invalid Proxy Port: " + proxyport);
			throw new Exception("Invalid ProxyPort");
		}

		/**
		 * Validating the Proxy IpAddress 
		 */
//		boolean validip = CommonUtilities.isvalidIP(proxyhost);
//		if (validip == false)
//		{
//			Logger.out.info("Invalid Proxy Host: " + proxyhost);
//			throw new Exception("Invalid ProxyHost");
//		}
		// setting the system settings
		System.setProperty("proxyHost", proxyhost);
		System.setProperty("proxyPort", proxyport);
	} //createPasswordAuthentication

	private List evsdata(String searchTerm, int limit)
	{
		List evsDataList = new ArrayList();
		try
		{
			evsDataList = getEvsConceptTree(searchTerm, limit);
		} // try
		catch (Exception ex)
		{
			Logger.out.error("2"+ex);
		} // catch
		return evsDataList;
	} // evsdata

	/**
	 * @param searchTerm The PermissibleValue to search for the concept
	 * @param limit The depth of the PermissibleValue tree 
	 * @return A List of all possible Concepts for the given Permissible Value
	 * upto the limit specified
	 */
	private List getEvsConceptTree(String searchTerm, int limit)
	{
		try
		{
			EVSQuery aEVSQuery = new EVSQueryImpl();
			List resultList = null;

			Vector roles = new Vector();
			String lev = "" + limit;
			int j = 0;
			aEVSQuery.getTree(vocabularyName, searchTerm, true, false, 0, lev, roles);

			resultList = appService.evsSearch(aEVSQuery);
			if (!resultList.isEmpty())
			{
				Object conceptCode = resultList.get(j);
				//            System.out.println(j + "]\t " + conceptCode + "  :: | "
				//                    + resultList.get(j));
				Logger.out.info(j + "]\t " + conceptCode + "  :: | " + resultList.get(j));

				DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) conceptCode;
				Object uo = dmtn.getUserObject();
				DescLogicConcept dlc = (DescLogicConcept) uo;

				//  to check the tree object
				printTree(dmtn);
			}
			return resultList;
		} // try
		catch (Exception e2)
		{
			Logger.out.error("3Error: " + e2);
			return null;
		}
	} //getEvsConceptTree

	// to be deleted later.
	/**
	 * @param root The Root node of the tree to print
	 * This method prints the entire tree from the given node up to its leaves.
	 */
	private void printTree(DefaultMutableTreeNode root)
	{
		try
		{
			if (!root.isLeaf())
			{
				int childCount = root.getChildCount();

				Object uo = root.getUserObject();
				DescLogicConcept dlc = (DescLogicConcept) uo;
				System.out.println("Code : " + dlc.getCode() + ",\t ConceptCode : "
						+ dlc.getId() + ",\t Name : " + dlc.getName());
				System.out.println("Children : " + childCount);
				System.out.println("\n________________________________");

				Enumeration enum = root.children();
				while (enum.hasMoreElements())
				{
					DefaultMutableTreeNode child = (DefaultMutableTreeNode) enum.nextElement();
					printTree(child);
				} // while
			} // not a leaf
			else
			{
				Object uo = root.getUserObject();
				DescLogicConcept dlc = (DescLogicConcept) uo;
				System.out.println("Code : " + dlc.getCode() + ",\t ConceptCode : "
						+ dlc.getId() + ",\t Name : " + dlc.getName());
				System.out.println("\n================================\n");
			}
		} // try
		catch (Exception e)
		{
			Logger.out.error("4Error : " + e );
		} // catch
	} // printTree
} // class CDEDownloader
