/*
 * $Name: 1.45 $
 * 
 * */
package edu.wustl.catissuecore.util.listener;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.sf.ehcache.CacheException;
import titli.controller.interfaces.TitliInterface;
import titli.model.Titli;
import titli.model.TitliException;
import titli.model.util.IndexUtility;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.annotations.AnnotationUtil;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolRegistrationBizLogic;
import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.ProtectionGroups;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.bizlogic.QueryBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.CVSTagReader;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.dbManager.HibernateMetaData;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;


/**
 * 
 * @author aarti_sharma
 * 
 * */
public class CatissueCoreServletContextListener implements ServletContextListener
{
    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)
    {
        /**
         * Getting Application Properties file path
         */
        String applicationResourcesPath = sce.getServletContext().getRealPath("WEB-INF")
                + System.getProperty("file.separator")
                + "classes" + System.getProperty("file.separator")
                + sce.getServletContext().getInitParameter("applicationproperties");
        
        /**
         * Configuring the Logger class so that it can be utilized by
         * the entire application
         */
        Logger.configure(applicationResourcesPath);
        
        /**
         * Initializing ApplicationProperties with the class 
         * corresponding to resource bundle of the application
         */
        ApplicationProperties.initBundle(sce.getServletContext().getInitParameter("resourcebundleclass"));
        
        /**
         * Getting and storing Home path for the application
         */
        Variables.applicationHome = sce.getServletContext().getRealPath("");
        
        /**
         * Creating Logs Folder inside catissue home
         */
        try
        {
        	File logfolder = new File(Variables.applicationHome + "/Logs");
            if (!logfolder.exists())
            {
                logfolder.mkdir();
            }
        }
        catch (Exception ex)
        {
        	Logger.out.error(ex.getMessage(), ex);
        }
        
        
        //All users should be able to view all data by default
        Map protectionGroupsForObjectTypes = new HashMap();
        protectionGroupsForObjectTypes.put(Site.class.getName(),
                new String[] {ProtectionGroups.PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(Address.class.getName(),
                new String[] {ProtectionGroups.PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(StorageContainer.class.getName(),
                new String[] {ProtectionGroups.PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(DistributionProtocol.class.getName(),
                new String[] {ProtectionGroups.PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(Distribution.class.getName(),
                new String[] {ProtectionGroups.PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(User.class.getName(),
                new String[] {ProtectionGroups.PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(Participant.class.getName(),
                new String[] {ProtectionGroups.PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(CollectionProtocol.class.getName(),
                new String[] {ProtectionGroups.PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(CollectionProtocolRegistration.class.getName(),
                new String[] {ProtectionGroups.PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(SpecimenCollectionGroup.class.getName(),
                new String[] {ProtectionGroups.PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(Specimen.class.getName(),
                new String[] {ProtectionGroups.PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(FluidSpecimen.class.getName(),
                new String[] {ProtectionGroups.PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(TissueSpecimen.class.getName(),
                new String[] {ProtectionGroups.PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(MolecularSpecimen.class.getName(),
                new String[] {ProtectionGroups.PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(CellSpecimen.class.getName(),
                new String[] {ProtectionGroups.PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(SpecimenCharacteristics.class.getName(),
                new String[] {ProtectionGroups.PUBLIC_DATA_GROUP});
        
        Constants.STATIC_PROTECTION_GROUPS_FOR_OBJECT_TYPES.putAll(protectionGroupsForObjectTypes);
        
        Variables.applicationName = ApplicationProperties.getValue("app.name");
        Variables.applicationVersion = ApplicationProperties.getValue("app.version");
		
        QueryBizLogic.initializeQueryData();
        
        // get database name and set variables used in query
        Variables.databaseName=HibernateMetaData.getDataBaseName();
        
        String fileName = Variables.applicationHome + System.getProperty("file.separator")+ ApplicationProperties.getValue("application.version.file");
        CVSTagReader cvsTagReader = new CVSTagReader();
        String cvsTag = cvsTagReader.readTag(fileName);
        Variables.applicationCvsTag = cvsTag;
        Logger.out.info("========================================================");
        Logger.out.info("Application Information");
        Logger.out.info("Name: "+Variables.applicationName);
        Logger.out.info("Version: "+Variables.applicationVersion);
        Logger.out.info("CVS TAG: "+Variables.applicationCvsTag);
        Logger.out.info("Path: "+ Variables.applicationHome);
        Logger.out.info("Database Name: "+Variables.databaseName);
        Logger.out.info("========================================================");                
        
        try
        {
            if(Variables.databaseName.equals(Constants.ORACLE_DATABASE))
            {
            	//set string/function for oracle
            	Variables.datePattern = "mm-dd-yyyy";
            	Variables.timePattern = "hh-mi-ss";
            	Variables.dateFormatFunction="TO_CHAR";
            	Variables.timeFormatFunction="TO_CHAR";
            	Variables.dateTostrFunction = "TO_CHAR";
            	Variables.strTodateFunction = "TO_DATE";
            }
            else
            {
            	Variables.datePattern = "%m-%d-%Y";
            	Variables.timePattern = "%H:%i:%s";
            	Variables.dateFormatFunction="DATE_FORMAT";
            	Variables.timeFormatFunction="TIME_FORMAT";
            	Variables.dateTostrFunction = "TO_CHAR";
            	Variables.strTodateFunction = "STR_TO_DATE";
            }
        }
        catch (Exception ex)
        {
        	Logger.out.error(ex.getMessage(), ex);
        }  
        
        //Initialize CDE Manager
        try
		{
        	CDEManager.init();
		}
        catch(Exception ex)
		{
        	Logger.out.error("Could not initialized application, Error in creating CDE manager.");
        	Logger.out.error(ex.getMessage(), ex);
		}
        
        //Initialize XML properties Manager
        try
		{
        	String path = System.getProperty("app.propertiesFile");
        	XMLPropertyHandler.init(path);
        	
        	File propetiesDirPath = new File(path);
        	Variables.propertiesDirPath = propetiesDirPath.getParent();
        	Logger.out.debug("propetiesDirPath "+Variables.propertiesDirPath);
        	
        	String propertyValue = XMLPropertyHandler.getValue("server.port");
            Logger.out.debug("property Value "+propertyValue);
		}
        catch(Exception ex)
		{
        	Logger.out.error("Could not initialized application, Error in creating XML Property handler.");
        	Logger.out.error(ex.getMessage(), ex);
		}
        
        Logger.out.debug("System property : "+System.getProperty("gov.nih.nci.security.configFile"));
        Logger.out.debug("System property : "+System.getProperty("edu.wustl.catissuecore.contactUsFile"));
        
        
        /**
         *  Following code is added for caching the Map of all storage containers having empty positions.
         *  Get the all storage containers having empty positions from ParticipantBizLogic and add it to cache
         */
        Map storageContainersMap = null;
        StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
		try
		{
			storageContainersMap = storageContainerBizLogic.getAllocatedContainerMap();
		}
		catch (Exception ex)
		{
			Logger.out.debug("Exception occured getting List of Storage Containers");
			ex.printStackTrace();
		}
        
        
        /**
         *  Following code is added for caching the Map of all participants.
         *  Get the map of participants from ParticipantBizLogic and add it to cache
         */
        
        Map participantMap = null;
        ParticipantBizLogic bizlogic = (ParticipantBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.PARTICIPANT_FORM_ID);
		try
		{
			participantMap = bizlogic.getAllParticipants();
		}
		catch (Exception ex)
		{
			Logger.out.debug("Exception occured while getting List of Participants");
			ex.printStackTrace();
		}
		
		List participantRegInfoList = null;
		CollectionProtocolRegistrationBizLogic cBizLogic = (CollectionProtocolRegistrationBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
		try
		{
			participantRegInfoList = cBizLogic.getAllParticipantRegistrationInfo();
		}
		catch(Exception e)
		{
			Logger.out.debug("Exception occured while getting List of Participant's reg info");
			e.printStackTrace();
		}
		
		
		// getting instance of catissueCoreCacheManager and adding participantMap to cache
        CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager.getInstance();
		try
		{
			catissueCoreCacheManager.addObjectToCache(Constants.MAP_OF_PARTICIPANTS,(HashMap) participantMap);
			catissueCoreCacheManager.addObjectToCache(Constants.MAP_OF_STORAGE_CONTAINERS,(TreeMap)storageContainersMap);
			catissueCoreCacheManager.addObjectToCache(Constants.LIST_OF_REGISTRATION_INFO,(Vector)participantRegInfoList);
		}
		catch (CacheException e)
		{
			Logger.out.debug("Exception occured while creating instance of CatissueCoreCacheManager");
			e.printStackTrace();
		}
//		 Initialising Entity cache
		try
		{
            EntityCache entityCache = EntityCache.getInstance();
            Logger.out.debug("Entity Cache is initialised");
            //Stores the list of system entities into the cache.-- Vishvesh.
            AnnotationUtil.getSystemEntityList();
            //Stores the ids in the cache
            Long participantId = Utility.getEntityId(AnnotationConstants.ENTITY_NAME_PARTICIPANT);
            catissueCoreCacheManager.addObjectToCache("participantEntityId",participantId);
            Long scgId = Utility.getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN_COLLN_GROUP);
            catissueCoreCacheManager.addObjectToCache("scgEntityId",scgId);
            Long specimenEntityId = Utility.getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN);
            catissueCoreCacheManager.addObjectToCache("specimenEntityId",specimenEntityId);
            
		}
		catch (Exception e)
		{
			Logger.out.debug("Exception occured while initialising entity cache");
			e.printStackTrace();
		}
		
		//initialise TiTLi index
		//create the index if it does not exist
		try
		{
			TitliInterface titli = Titli.getInstance();
			
			String dbName = titli.getDatabases().keySet().toArray(new String[0])[0];
			
			File dbIndexLocation = IndexUtility.getIndexDirectoryForDatabase(dbName);
			
			if(!dbIndexLocation.exists())
			{
				titli.index();
			}
				
		}
		catch (TitliException e)
		{
			Logger.out.debug("Exception occured while initialising TiTLi Search");
			e.printStackTrace();
		}
		
		
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)
    {
    	//  shutting down the cacheManager
		try
		{
			CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager.getInstance();
			catissueCoreCacheManager.shutdown();
		}
		catch (CacheException e)
		{
			Logger.out.debug("Exception occured while shutting instance of CatissueCoreCacheManager");
			e.printStackTrace();
		}
	 }
}