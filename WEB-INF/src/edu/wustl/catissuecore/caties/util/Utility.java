package edu.wustl.catissuecore.caties.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

public class Utility 
{
	/**
	 * Generic Initialization process for caTIES servers
	 * @throws Exception
	 */
	public static void init()throws Exception
	{
		//Initialization methods
		Variables.applicationHome = System.getProperty("user.dir");
		//Logger.out = org.apache.log4j.Logger.getLogger("");
		//Configuring common logger
		Logger.configure(CaTIESConstants.LOGGER_GENERAL);
		//Configuring logger properties
		PropertyConfigurator.configure(Variables.applicationHome + File.separator+"logger.properties");
		// Setting properties for UseImplManager
		System.setProperty("gov.nih.nci.security.configFile",
				"./catissuecore-properties"+File.separator+"ApplicationSecurityConfig.xml");
		// initializing cache manager
		CDEManager.init();
		//initializing XMLPropertyHandler to read properties from caTissueCore_Properties.xml file
		XMLPropertyHandler.init("./catissuecore-properties"+File.separator+"caTissueCore_Properties.xml");
		ApplicationProperties.initBundle("ApplicationResources");
		//initializing caties property configurator
		CaTIESProperties.initBundle("caTIES");
		// initializing SiteInfoHandler to read site names from site configuration file
		SiteInfoHandler.init(CaTIESProperties.getValue(CaTIESConstants.SITE_INFO_FILENAME));
	}

	/**
     * This method returns the list of all the SCGs associated with the given participant
     * @param participant Participant object
     * @return scgList SpecimenCollectionGroup list
     */
    public static List<SpecimenCollectionGroup> getSCGList(Participant participant)throws DAOException
    {
    	// FIRE ONLY ONE QUERY
    	
    	List<SpecimenCollectionGroup> scgList=new ArrayList<SpecimenCollectionGroup>();
    	DefaultBizLogic defaultBizLogic=new DefaultBizLogic();
    	// get all CollectionProtocolRegistration for participant
    	String sourceObjectName=CollectionProtocolRegistration.class.getName();
		String[] selectColumnName=new String[]{Constants.SYSTEM_IDENTIFIER};
		String[] whereColumnName=new String[]{Constants.COLUMN_NAME_PARTICIPANT_ID};
		String[] whereColumnValue=new String[]{participant.getId().toString()};
		String[] whereColumnCondition=new String[]{"="};
		String joinCondition="";
    	Collection cprCollection=(List)defaultBizLogic.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);	
		Long cprID;
		Iterator cprIter=cprCollection.iterator();
		// iterate on all colletionProtocolRegistration for participant
		while(cprIter.hasNext())
		{
			cprID=(Long)cprIter.next();
			Collection tempSCGCollection=(Collection)defaultBizLogic.retrieveAttribute(CollectionProtocolRegistration.class.getName(), cprID, Constants.COLUMN_NAME_SCG_COLL);
			Iterator scgIter=tempSCGCollection.iterator();
			// add all the scg associated with cpr to scgList
			while(scgIter.hasNext())
			{
				SpecimenCollectionGroup scg=(SpecimenCollectionGroup)scgIter.next();
				scgList.add(scg);
			}
		}
    	return scgList;
    } 
    
    /**
	 * Saves object tothe datastore
	 * @param obj object
	 * @throws Exception throws exception  
	 */
	public static void saveObject(Object obj)throws Exception
	{
		BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();
		IBizLogic bizLogic = bizLogicFactory.getBizLogic(obj.getClass().getName());
		SessionDataBean sessionDataBean = new SessionDataBean();
		sessionDataBean.setUserName(CaTIESProperties.getValue(CaTIESConstants.SESSION_DATA));
		bizLogic.insert(obj,sessionDataBean,Constants.HIBERNATE_DAO);
	}

	/**
	 * Delete object tothe datastore
	 * @param obj object
	 * @throws Exception throws exception  
	 */
	public static void deleteObject(Object obj)throws Exception
	{
		BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();
		IBizLogic bizLogic = bizLogicFactory.getBizLogic(obj.getClass().getName());
		SessionDataBean sessionDataBean = new SessionDataBean();
		sessionDataBean.setUserName(CaTIESProperties.getValue(CaTIESConstants.SESSION_DATA));
		bizLogic.delete(obj,Constants.HIBERNATE_DAO);
	}

	
	/**
	 * Gets object data grom datastore 
	 * @param objName object name
	 * @param property property of the object 
	 * @param val value for the property
	 * @return list of requested object from the datastore 
	 * @throws Exception throws exception
	 */
	public static List getObject(String objName,String property,String val) throws DAOException
	{
		List l=null;
		
			BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();
			IBizLogic bizLogic = bizLogicFactory.getBizLogic(objName);
			SessionDataBean sessionDataBean = new SessionDataBean();
			sessionDataBean.setUserName(CaTIESProperties.getValue(CaTIESConstants.SESSION_DATA));
			l = bizLogic.retrieve(objName,property,val);
		
		return l;
	}
	
	/**
	 * @param obj object
	 * @throws Exception
	 * updates the object in datastore
	 */
	public static void updateObject(Object obj)throws Exception
	{
		BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();
		IBizLogic bizLogic = bizLogicFactory.getBizLogic(obj.getClass().getName());
		SessionDataBean sessionDataBean = new SessionDataBean();
		sessionDataBean.setUserName(CaTIESProperties.getValue(CaTIESConstants.SESSION_DATA));
		if(obj instanceof Participant || obj instanceof SpecimenCollectionGroup)
		{
			bizLogic.update(obj,obj,Constants.HIBERNATE_DAO,sessionDataBean);
		}	
		else
		{
			bizLogic.update(obj,null,Constants.HIBERNATE_DAO,sessionDataBean);
		}	
	}
}
