package edu.wustl.catissuecore.caties.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.sf.ehcache.CacheException;

import org.apache.log4j.PropertyConfigurator;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolRegistrationBizLogic;
import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.reportloader.CSVLogger;
import edu.wustl.catissuecore.reportloader.CaTIESConstants;
import edu.wustl.catissuecore.reportloader.SiteInfoHandler;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

public class InitUtility 
{
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
	 * initializes the parsser manager
	 */
	public static void initializeParticipantCache()
	{
		// participant map for cache manager
		Map participantMap = null;
		ParticipantBizLogic bizlogic = (ParticipantBizLogic) BizLogicFactory.getInstance()
				.getBizLogic(Participant.class.getName());
		List cprList=null;
		
		/// MOVE THIS TO UTILTY ALSO CODE FORM SERVLTCONTEXTLISTENER
		try
		{
			BizLogicFactory bizFactory=BizLogicFactory.getInstance();
			CollectionProtocolRegistrationBizLogic cpbizlogic= (CollectionProtocolRegistrationBizLogic) bizFactory.getBizLogic(CollectionProtocolRegistration.class.getName());
			cprList=cpbizlogic.getAllParticipantRegistrationInfo();
			// get all participant list to set to chache manager
			participantMap = bizlogic.getAllParticipants();
		}
		catch (Exception ex)
		{
			Logger.out.error("Exception occured getting List of Participants " ,ex);
		}
		// getting instance of catissueCoreCacheManager and adding participantMap to cache
		try
		{
			// get instance of catissueCoreCacheManager
			CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager
			.getInstance();
			// add objects to cacheManager
			catissueCoreCacheManager.addObjectToCache(Constants.MAP_OF_PARTICIPANTS, (HashMap) participantMap);
			catissueCoreCacheManager.addObjectToCache(edu.wustl.catissuecore.util.global.Constants.LIST_OF_REGISTRATION_INFO, (Vector)cprList);
		}
		catch (CacheException e)
		{
			Logger.out
					.error("Exception occured while creating instance of CatissueCoreCacheManager" , e);
		}	
	}
	
}
