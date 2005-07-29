/*
 * Created on June 9, 2005
 * Last Modified : July 6, 2005.
 */

package edu.wustl.common.cde;

/**
 * @author mandar_deshmukh
 *
 */
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.cadsr.domain.DataElement;
import gov.nih.nci.cadsr.domain.EnumeratedValueDomain;
import gov.nih.nci.cadsr.domain.NonenumeratedValueDomain;
import gov.nih.nci.cadsr.domain.PermissibleValue;
import gov.nih.nci.cadsr.domain.ValueDomainPermissibleValue;
import gov.nih.nci.cadsr.domain.impl.DataElementImpl;
import gov.nih.nci.cadsr.domain.impl.ValueDomainImpl;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.evs.query.EVSQuery;
import gov.nih.nci.evs.query.EVSQueryImpl;
import gov.nih.nci.evs.domain.DescLogicConcept;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.tree.*;
import java.util.Enumeration;

public class CDEDownloader
{

	private String vocabularyName;
	private int limit;
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

			appService = ApplicationService.getRemoteInstance(dbserver);
		} // try
		catch (Exception conexp)
		{
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
	public CDE loadCDE(CDEConConfig cdeCon, String cdePublicId,
						String vocabularyName, int limit)
						throws Exception
	{
		boolean bCon = connect(cdeCon.getProxyhostip(), cdeCon.getProxyport(),
				cdeCon.getUsername(), cdeCon.getPassword(), cdeCon.getDbserver());
		if (bCon == true)
		{
			// to remove the Logger configuration from the file
			System.setProperty(
					"catissue.home", "D:\\tomcat\\webapps\\catissuecore" + "/Logs");
			Logger
			.configure("D:\\tomcat\\webapps\\catissuecore\\WEB-INF\\classes\\ApplicationResources.properties");

			this.vocabularyName = vocabularyName;
			this.limit = limit;
			CDE resultCde = retrieveDataElement(new Long(cdePublicId));
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
	private CDE retrieveDataElement(Long CDEPublicID) throws Exception
	{
		//	Create the dataelement and set the dataelement properties
		DataElement dataElement = new DataElementImpl();
		dataElement.setPublicID(CDEPublicID);
//		dataElement.setId(CDEPublicID.toString() ); 
		dataElement.setLatestVersionIndicator("Yes");

		List resultList = appService.search("gov.nih.nci.cadsr.domain.DataElement", dataElement);

		// check if any cde exists with the given public id.
		if (!resultList.isEmpty())
		{
			// retreive the Data Element for the given publicid
			dataElement = (DataElement) resultList.get(0);

			// get the Value Domain for the Data Element 
			List lstValueDomain = retrieveValueDomain(dataElement); // valuedomain list

			// get the PermissibleValues for the ValueDoamin of the Data Element
			List lstPerValForValDomPerVal = retrievePermissibleValueForValueDomainPermissibleValue(lstValueDomain);
			// list of permissiblevalues for the DataElement

			// create the cde object and set the values.
			CDEImpl cdeobj = new CDEImpl();

			cdeobj.setPublicId(dataElement.getPublicID().toString());
			cdeobj.setDefination(dataElement.getPreferredDefinition());
			cdeobj.setLongName(dataElement.getLongName());
			cdeobj.setVersion(dataElement.getVersion().toString());
			cdeobj.setPreferredname(dataElement.getPreferredName());
			//Kapil FIX
			cdeobj.setPermissibleValues(new HashSet(lstPerValForValDomPerVal));

			return cdeobj;
		} // list not empty
		else
		// no Data Element retreived
		{
			return null;
		} // list empty
	} // retrieveDataElements

	/**
	 * @param dataElement Data Element for which Value Domain is to be retrieved.
	 * @return A List of ValueDomains
	 * @throws Exception
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
	private List retrievePermissibleValueForValueDomainPermissibleValue(List lstValueDomain)
			throws Exception
	{
		// list of permissible values for the given ValueDomain
		List pvList = new ArrayList();

		for (int i = 0; i < lstValueDomain.size(); i++)
		{
			ValueDomainImpl valDomImpl = (ValueDomainImpl) lstValueDomain.get(i);

			String valdomnm = valDomImpl.toString();
			int z = valdomnm.lastIndexOf('.') + 1;

			String valDomType = valdomnm.substring(z, z + 1);

			// check  whether ValueDomain is Enumerated or Non Enumerated
			if (valDomType.equals("E"))
			{
				// get the enumerated ValueDomain
				EnumeratedValueDomain enumValDom = (EnumeratedValueDomain) lstValueDomain.get(i);

				// get the Collection of PermissibleValues for the ValueDomain 
				Collection pvColl = enumValDom.getValueDomainPermissibleValueCollection();

				int index = 1;
				Iterator iterat = pvColl.iterator();

				List permissibleValueList = null;
				while (iterat.hasNext())
				{
					// ValueDomainPermissibleValue for the enumValDom
					ValueDomainPermissibleValue valDomPerVal = (ValueDomainPermissibleValue) iterat
							.next();

					//PermissibleValues for the given VDPV
					permissibleValueList = appService.search(
							"gov.nih.nci.cadsr.domain.PermissibleValue", valDomPerVal);

					if (!permissibleValueList.isEmpty())
					{
						//collecting all the PermissibleValues
						for (int cnt = 0; cnt < permissibleValueList.size(); cnt++)
						{
							PermissibleValue perVal = (PermissibleValue) permissibleValueList
									.get(cnt);
							pvList.add(perVal);

							String searchTerm = perVal.getValue();
							System.out.println("SearchTerm : "+ searchTerm);
							List validValues = evsdata(searchTerm, limit);
							if (validValues!=null )
								pvList.addAll(validValues) ;
						} // for
					} // if
				} // while
			} // valDomType = E
			else
			// valDomType = N   NonEnumerated
			{
				// get the nonenumerated ValueDomain
				NonenumeratedValueDomain nonEnumValDom = (NonenumeratedValueDomain) lstValueDomain
						.get(i);
				Logger.out.info(nonEnumValDom.getPreferredName());
				Logger.out.info(nonEnumValDom.getPreferredDefinition());
				Logger.out.info(nonEnumValDom.getLongName());

				pvList.add(nonEnumValDom);
			} // valdomtype = N
		} // for

		return pvList;
	} // retrievePermissibleValueForValueDomainPermissibleValue

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
		boolean validip = CommonUtilities.isvalidIP(proxyhost);
		if (validip == false)
		{
			Logger.out.info("Invalid Proxy Host: " + proxyhost);
			throw new Exception("Invalid ProxyHost");
		}
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
