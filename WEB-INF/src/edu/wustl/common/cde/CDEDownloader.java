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
import edu.wustl.common.cde.xml.impl.XMLCDEImpl;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.cadsr.domain.DataElement;
import gov.nih.nci.cadsr.domain.EnumeratedValueDomain;
import gov.nih.nci.cadsr.domain.PermissibleValue;
import gov.nih.nci.cadsr.domain.ValueDomain;
import gov.nih.nci.cadsr.domain.ValueDomainPermissibleValue;
import gov.nih.nci.cadsr.domain.impl.DataElementImpl;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
	private boolean connect()
	{
		try
		{
			// creates the passwordauthenticaton object to be used for establishing 
			// the connection with the databse server.
			createPasswordAuthentication(CDEConConfig.proxyhostip, CDEConConfig.proxyport, CDEConConfig.username, CDEConConfig.password);
			
			//Logger.out.debug("appService");
			
			appService = ApplicationService.getRemoteInstance(CDEConConfig.dbserver);
			
		} // try
		catch (Exception conexp)
		{
			conexp.printStackTrace();
			//Logger.out.error("1Error: " + conexp);
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
	public CDE loadCDE(XMLCDE xmlCDE ,
						String vocabularyName, int limit)
						throws Exception
	{
	    Logger.out.debug("In CDEDownloader...................");
		setCDEConConfig();
		boolean bCon = connect();
		if (bCon)
		{
			// to remove the Logger configuration from the file
			System.setProperty("catissue.home", "Logs");
			//Logger.configure("ApplicationResources.properties");
			
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
		//return null;
		//Create the dataelement and set the dataEelement properties
		DataElement dataElementQuery = new DataElementImpl();
//		dataElementQuery.setPublicID(new Long(xmlCDE.getPublicId()));
		dataElementQuery.setPreferredName(xmlCDE.getName());
		//Logger.out.debug("XMLCDE : " + xmlCDE.getName());
		dataElementQuery.setLatestVersionIndicator("Yes");
		
		List resultList = appService.search(DataElement.class, dataElementQuery);
		
		if(resultList == null)
		{
			resultList = new ArrayList();
			resultList.add(dataElementQuery);
		}
		
		// check if any cde exists with the given public id.
		if (!resultList.isEmpty())
		{
			// retreive the Data Element for the given publicid
			DataElement dataElement = (DataElement) resultList.get(0);
			
//			Logger.out.debug("dataElement Details in loadCDE ");
//			Logger.out.debug("PublicID "+dataElement.getPublicID());
//			Logger.out.debug("LongName "+dataElement.getLongName());
			
			// create the cde object and set the values.
			CDEImpl cdeobj = new CDEImpl();
			
			cdeobj.setPublicId(dataElement.getPublicID().toString());
			Logger.out.debug("CDE Public Id : "+cdeobj.getPublicId());
			cdeobj.setDefination(dataElement.getPreferredDefinition());
			Logger.out.debug("CDE Def : "+cdeobj.getDefination());
			cdeobj.setLongName(dataElement.getLongName());
			Logger.out.debug("CDE Long Name : "+cdeobj.getLongName());
			cdeobj.setVersion(dataElement.getVersion().toString());
			Logger.out.debug("CDE Version : "+cdeobj.getVersion());
			cdeobj.setPreferredName(dataElement.getPreferredName());
			Logger.out.debug("CDE Perferred Name : "+cdeobj.getPreferredName());
			cdeobj.setDateLastModified(dataElement.getDateModified());
			Logger.out.debug("CDE Last Modified Date : "+cdeobj.getDateLastModified());
			
			// working on PV
			ValueDomain vd = dataElement.getValueDomain();
			Logger.out.debug("vd class : " + vd.getClass());
			String vdClass = vd.getClass().toString().substring(vd.getClass().toString().lastIndexOf(".")+1);
			Logger.out.debug("vdClass : " + vdClass);
			
			if(vdClass.charAt(0) == 'E')
			{
			    Collection valueDomainPermissibleValueCollection = ((EnumeratedValueDomain)vd).getValueDomainPermissibleValueCollection();
				Set permissibleValues = getPermissibleValues(valueDomainPermissibleValueCollection);
				cdeobj.setPermissibleValues(permissibleValues);
			}	
			
			Logger.out.debug("Permissible Value Size................."+cdeobj.getPermissibleValues().size());
			return cdeobj;
		} // list not empty
		else // no Data Element retreived
		{
			return null;
		} // list empty
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
	        PermissibleValue permissibleValue = (PermissibleValue) valueDomainPermissibleValue.getPermissibleValue();
	        edu.wustl.common.cde.PermissibleValue newPermissibleValue = new PermissibleValueImpl();
	        newPermissibleValue.setConceptid(permissibleValue.getId());
	        Logger.out.debug("Concept ID : "+newPermissibleValue.getConceptid());
	        newPermissibleValue.setValue(permissibleValue.getValue());
	        Logger.out.debug("Value : "+newPermissibleValue.getValue());
//	        newPermissibleValue.setCde(cde);
//	        Logger.out.debug("CDE : "+newPermissibleValue.getCde());
//	        newPermissibleValue.setParentPermissibleValue(parentPermissibleValue);
//	        
//	        Set subPermissibleValueSet = getPermissibleValues(permissibleValue.getValueDomainPermissibleValueCollection(), cde, newPermissibleValue);
//	        if (subPermissibleValueSet != null)
//	        {
//	            newPermissibleValue.setSubPermissibleValues(subPermissibleValueSet);
//	        }
	        permissibleValuesSet.add(newPermissibleValue);
	    }
	    
	    return permissibleValuesSet;
	}
	
	/**
	 * @param dataElement Data Element for which Value Domain is to be retrieved.
	 * @return A List of ValueDomains
	 * @throws Exception
	 * OK
	 */
//	 code commented on 13Mar06 By MD
	/*
	private List retrieveValueDomain(DataElement dataElement) throws Exception
	{
		List resValueDomain = null;
		resValueDomain = appService.search("gov.nih.nci.cadsr.domain.ValueDomain", dataElement);
		return resValueDomain;
	} // retrieveValueDomain 
*/
	/**
	 * @param lstValueDomain the value domain for which the Permissible Values are to be searched
	 * @return A List of the PermissibleValues for a Value Domain of the dataelement.
	 * @throws Exception
	 */
//	 code commented on 13Mar06 By MD
	/*
	private List retrievePermissibleValue(DataElement dataElement,XMLPermissibleValueType  xmlPermissibleValue)
			throws Exception
	{
		//get the Value Domain for the Data Element 
		List valueDomainList = retrieveValueDomain(dataElement); // valuedomain list
		Logger.out.debug("valueDomainList "+valueDomainList.size());
		// list of permissible values for the given ValueDomain
		List pvList = new ArrayList();

		Iterator iterator = valueDomainList.iterator();
		while(iterator.hasNext())
		{
			Logger.out.debug("1");
			ValueDomain valueDomain = (ValueDomain) iterator.next();

			if(valueDomain instanceof EnumeratedValueDomain)//Enumerated
			{
				Logger.out.debug("EnumeratedValueDomain ");
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
*/
/*
	private List getPermissibleValues(EnumeratedValueDomain enumValDom, XMLPermissibleValueType xmlPermissibleValue) throws Exception
	{
		List pvList = new ArrayList();
		Logger.out.debug("enumValDom.getValueDomainPermissibleValueCollection().size() "+enumValDom.getValueDomainPermissibleValueCollection().size());
		
		// get the Collection of PermissibleValues for the ValueDomain 
		Iterator pvIterator = enumValDom.getValueDomainPermissibleValueCollection().iterator();

		while (pvIterator.hasNext())
		{
			Logger.out.debug("pvIterator.hasNext()");
			// ValueDomainPermissibleValue for the enumValDom
			ValueDomainPermissibleValue valDomPerVal = (ValueDomainPermissibleValue) pvIterator.next();

			//PermissibleValues for the given VDPV
			List permissibleValueList = appService.search(PermissibleValue.class, valDomPerVal);
			Logger.out.debug("permissibleValueList "+permissibleValueList.size());
			if (!permissibleValueList.isEmpty())
			{
				//collecting all the PermissibleValues
				for (int cnt = 0; cnt < permissibleValueList.size(); cnt++)
				{
					PermissibleValue permissibleValue = (PermissibleValue) permissibleValueList
							.get(cnt);
					pvList.add(permissibleValue);
					
					Logger.out.debug("Value "+permissibleValue.getValue());
					Logger.out.debug("Id "+permissibleValue.getId());
//					 String searchTerm = permissibleValue.getValue();
//					Logger.out.debug("SearchTerm : "+ searchTerm);
//					List validValues = evsdata(searchTerm, limit);
//					if (validValues!=null )
//						pvList.addAll(validValues) ;
				} // for
			} // if
		} // while
		return pvList;
	}
*/
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
			//Logger.out.info("Invalid Proxy Port: " + proxyport);
			throw new Exception("Invalid ProxyPort");
		}

		/**
		 * Validating the Proxy IpAddress 
		 */
//		boolean validip = CommonUtilities.isvalidIP(proxyhost);
//		if (validip == false)
//		{
//			//Logger.out.info("Invalid Proxy Host: " + proxyhost);
//			throw new Exception("Invalid ProxyHost");
//		}
		// setting the system settings
		System.setProperty("proxyHost", proxyhost);
		System.setProperty("proxyPort", proxyport);
	} //createPasswordAuthentication

//	 code commented on 13Mar06 By MD
/*	
	private List evsdata(String searchTerm, int limit)
	{
		List evsDataList = new ArrayList();
		try
		{
			evsDataList = getEvsConceptTree(searchTerm, limit);
		} // try
		catch (Exception ex)
		{
			//Logger.out.error("2"+ex);
		} // catch
		return evsDataList;
	} // evsdata
*/
	/**
	 * @param searchTerm The PermissibleValue to search for the concept
	 * @param limit The depth of the PermissibleValue tree 
	 * @return A List of all possible Concepts for the given Permissible Value
	 * upto the limit specified
	 */
/*
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
				//            Logger.out.debug(j + "]\t " + conceptCode + "  :: | "
				//                    + resultList.get(j));
				//Logger.out.info(j + "]\t " + conceptCode + "  :: | " + resultList.get(j));

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
			//Logger.out.error("3Error: " + e2);
			return null;
		}
	} //getEvsConceptTree
*/
	// to be deleted later.
	/**
	 * @param root The Root node of the tree to print
	 * This method prints the entire tree from the given node up to its leaves.
	 */
//	 code commented on 13Mar06 By MD
	/*
	private void printTree(DefaultMutableTreeNode root)
	{
		try
		{
			if (!root.isLeaf())
			{
				int childCount = root.getChildCount();

				Object uo = root.getUserObject();
				DescLogicConcept dlc = (DescLogicConcept) uo;
				Logger.out.debug("Code : " + dlc.getCode() + ",\t ConceptCode : "
						+ dlc.getId() + ",\t Name : " + dlc.getName());
				Logger.out.debug("Children : " + childCount);
				Logger.out.debug("\n________________________________");

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
				Logger.out.debug("Code : " + dlc.getCode() + ",\t ConceptCode : "
						+ dlc.getId() + ",\t Name : " + dlc.getName());
				Logger.out.debug("\n================================\n");
			}
		} // try
		catch (Exception e)
		{
			//Logger.out.error("4Error : " + e );
		} // catch
	} // printTree
*/	
	public static void main(String []args) throws Exception
	{
		CDEDownloader cdeDownloader = new CDEDownloader();
		XMLCDE xmlCDE = new XMLCDEImpl();
		xmlCDE.setName("PT_RACE_CAT" );
		xmlCDE.setPublicId("106" );
		Logger.out.debug("\nIn Main : \n");
		Logger.out.debug("cdeDownloader................."+cdeDownloader);
		CDE cde = cdeDownloader.loadCDE(xmlCDE,"NCI_Thesaurus",10);
		printCDE(cde);
	} 
	
	/**
	 * This method prints the given cde's information.
	 * @param cde The cde to print.
	 */
	public static void printCDE(CDE cde)
	{
		Logger.out.debug("\nPrinting CDE\n---------------\n");
		Logger.out.debug("\nLong Name : "+ cde.getLongName());
		Logger.out.debug("\nPublicID : "+cde.getPublicId());
		Logger.out.debug("\nDef : " + cde.getDefination());
		Logger.out.debug("\nDate Last Modified : " + cde.getDateLastModified());
		Logger.out.debug("\n------PV ---------\n");
		HashSet hs =(HashSet) cde.getPermissibleValues(); 
		Iterator itr = hs.iterator();
		while(itr.hasNext() )
		{
		    edu.wustl.common.cde.PermissibleValue pv = (edu.wustl.common.cde.PermissibleValue) itr.next(); 
			Logger.out.debug(" Id : " + pv.getConceptid());
			Logger.out.debug(" Value : " + pv.getValue());
		}
		
		Logger.out.debug("\nDone");
	}
	
	private void setCDEConConfig()
	{
		CDEConConfig.proxyhostip ="scproxy.persistent.co.in";
		CDEConConfig.proxyport = "80";
		CDEConConfig.password ="";
		CDEConConfig.username = "gautam_shetty";
		CDEConConfig.dbserver ="http://cabio.nci.nih.gov/cacore30/server/HTTPServer";
	}
} // class CDEDownloader
