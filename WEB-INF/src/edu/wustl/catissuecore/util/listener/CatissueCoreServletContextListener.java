/*
 * $Name: 1.41.2.35 $
 *
 * */
package edu.wustl.catissuecore.util.listener;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import net.sf.ehcache.CacheException;
import edu.wustl.cab2b.server.path.PathFinder;
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
import edu.wustl.catissuecore.namegenerator.LabelAndBarcodeGeneratorInitializer;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.ProtectionGroups;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.DefaultValueManager;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.bizlogic.QueryBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.CVSTagReader;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;
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

	private static org.apache.log4j.Logger logger =Logger.getLogger(CatissueCoreServletContextListener.class);

	/**
	 * DATASOURCE_JNDI_NAME.
	 */
	String DATASOURCE_JNDI_NAME = "java:/catissuecore";

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent sce)
    {
    	try{
    		logger.info("Initializing catissue application");
	    	ServletContext servletContext = sce.getServletContext();
	    	Variables.applicationHome = sce.getServletContext().getRealPath("");
	    	Logger.configDefaultLogger(servletContext);
	        ApplicationProperties.initBundle(servletContext.getInitParameter("resourcebundleclass"));
			setGlobalVariable();
			logger =Logger.getLogger(CatissueCoreServletContextListener.class);
	        initCatissueParams();
			logApplnInfo();
			initCDEManager();
			DefaultValueManager.validateAndInitDefaultValueMap();
			logger.info("Initialization complete");
    	}
    	catch(Exception e)
    	{
    		logger.error("Application failed to initialize");
    		throw new RuntimeException( e.getLocalizedMessage(), e);

    	}
    }

	/**
	 * @throws Exception
	 * @throws ClassNotFoundException
	 * @throws DAOException
	 */
	public void initCatissueParams() throws Exception, ClassNotFoundException, DAOException
	{
		edu.wustl.common.querysuite.security.utility.Utility.setReadDeniedAndEntitySqlMap();
		addDefaultProtectionGroupsToMap();

		Class.forName(DBUtil.class.getName());
		Variables.databaseName=HibernateMetaData.getDataBaseName();
		QueryBizLogic.initializeQueryData();
		setDBFunctionNamesConstants();
		createAliasAndPageOfMap();

		LabelAndBarcodeGeneratorInitializer.init();
		initCatissueCache();
		initEntityCache();
		Utility.initializePrivilegesMap();
//		initTitliIndex();
		initCDEManager();

	}

	/**
	 * @throws Exception
	 */
	private void setGlobalVariable() throws Exception
	{
		String path = System.getProperty("app.propertiesFile");
    	XMLPropertyHandler.init(path);
    	File propetiesDirPath = new File(path);
    	Variables.propertiesDirPath = propetiesDirPath.getParent();

        Variables.applicationName = ApplicationProperties.getValue("app.name");
        Variables.applicationVersion = ApplicationProperties.getValue("app.version");
		int maximumTreeNodeLimit = Integer.parseInt(XMLPropertyHandler.getValue(Constants.MAXIMUM_TREE_NODE_LIMIT));
		Variables.maximumTreeNodeLimit = maximumTreeNodeLimit;
	}

	/**
	 *
	 */
	private void initTitliIndex()
	{
//		try
//		{
//			TitliInterface titli = Titli.getInstance();
//
//			String dbName = titli.getDatabases().keySet().toArray(new String[0])[0];
//
//			File dbIndexLocation = IndexUtility.getIndexDirectoryForDatabase(dbName);
//
//			if(!dbIndexLocation.exists())
//			{
//				titli.index();
//			}
//
//		}
//		catch (TitliException e)
//		{
//			logger.debug("Exception occured while initialising TiTLi Search");
//			e.printStackTrace();
//		}
	}

	/**
	 *
	 */
	private void initEntityCache()
	{
		try
		{
			CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager.getInstance();
            //Stores the list of system entities into the cache.-- Vishvesh.
            AnnotationUtil.getSystemEntityList();
            //Stores the ids in the cache
            Long participantId = edu.wustl.catissuecore.bizlogic.AnnotationUtil.getEntityId(AnnotationConstants.ENTITY_NAME_PARTICIPANT);
            catissueCoreCacheManager.addObjectToCache("participantEntityId",participantId);
            Long scgId = edu.wustl.catissuecore.bizlogic.AnnotationUtil.getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN_COLLN_GROUP);
            catissueCoreCacheManager.addObjectToCache("scgEntityId",scgId);
            Long specimenEntityId = edu.wustl.catissuecore.bizlogic.AnnotationUtil.getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN);
            catissueCoreCacheManager.addObjectToCache("specimenEntityId",specimenEntityId);
            Long cpEntityId = edu.wustl.catissuecore.bizlogic.AnnotationUtil.getEntityId(AnnotationConstants.ENTITY_NAME_COLLECTION_PROTOCOL);
            catissueCoreCacheManager.addObjectToCache(AnnotationConstants.COLLECTION_PROTOCOL_ENTITY_ID,cpEntityId);

            //Added for initializing PathFinder and EntityCache
			InitialContext ctx = new InitialContext();
	        DataSource ds = (DataSource)ctx.lookup(DATASOURCE_JNDI_NAME);
	        Connection conn = ds.getConnection();
			PathFinder.getInstance(conn);

            logger.debug("Entity Cache is initialised");

		}
		catch (Exception e)
		{
			logger.debug("Exception occured while initialising entity cache");
    		throw new RuntimeException( e.getLocalizedMessage(), e);
		}
	}

	/**
	 * @throws DAOException
	 * @throws Exception
	 * @throws ClassNotFoundException
	 */
	public void initCatissueCache() throws DAOException, Exception, ClassNotFoundException
	{
		StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
		Map storageContainersMap = storageContainerBizLogic.getAllocatedContainerMap();

        ParticipantBizLogic bizlogic = (ParticipantBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.PARTICIPANT_FORM_ID);
		Map participantMap = bizlogic.getAllParticipants();

		CollectionProtocolRegistrationBizLogic cBizLogic = (CollectionProtocolRegistrationBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
		List participantRegInfoList = cBizLogic.getAllParticipantRegistrationInfo();
		try
		{
			CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager.getInstance();
			catissueCoreCacheManager.addObjectToCache(Constants.MAP_OF_PARTICIPANTS,(HashMap) participantMap);
			catissueCoreCacheManager.addObjectToCache(Constants.MAP_OF_STORAGE_CONTAINERS,(TreeMap)storageContainersMap);
			catissueCoreCacheManager.addObjectToCache(Constants.LIST_OF_REGISTRATION_INFO,(Vector)participantRegInfoList);
		}
		catch (CacheException e)
		{
			System.out.println("exception  "+ e.getMessage());
			logger.debug("Exception occured while creating instance of CatissueCoreCacheManager");
			e.printStackTrace();
		}
	}

	/**
	 *
	 */
	private void initCDEManager()
	{
		try
		{
        	CDEManager.init();
		}
        catch(Exception ex)
		{
        	logger.error("Could not initialized application, Error in creating CDE manager.");
        	logger.error(ex.getMessage(), ex);
        	throw new RuntimeException(ex);
		}
	}

	/**
	 *
	 */
	private void logApplnInfo()
	{

		StringBuffer fileName = new StringBuffer();
		fileName.append(Variables.applicationHome).append(File.separator)
		.append(ApplicationProperties.getValue("application.version.file"));

        CVSTagReader cvsTagReader = new CVSTagReader();
        String cvsTag = cvsTagReader.readTag(fileName.toString());
        Variables.applicationCvsTag = cvsTag;
        logger.info("========================================================");
        logger.info("Application Information");
        logger.info("Name: "+Variables.applicationName);
        logger.info("Version: "+Variables.applicationVersion);
        logger.info("CVS TAG: "+Variables.applicationCvsTag);
        logger.info("Path: "+ Variables.applicationHome);
        logger.info("Database Name: "+Variables.databaseName);
        logger.info("========================================================");
	}


	/**
	 *
	 */
	private void setDBFunctionNamesConstants()
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

	/**
	 * @param protectionGroupsForObjectTypes
	 */
	private void addDefaultProtectionGroupsToMap()
	{

		final Map<String, String[]>  protectionGroupsForObjectTypes = new HashMap<String, String[]>();

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

        Constants.STATIC_PROTECTION_GROUPS_FOR_OBJECT_TYPES
		.putAll(protectionGroupsForObjectTypes);

	}

    /**
     * TO create map of Alias verses corresponding pageOf values.
     * This is required in Simple Query Edit feature, It contains mapping of alias name for the query tables & the corresponding pageOf values.
	 * Patch ID: SimpleSearchEdit_9
     */
    private void createAliasAndPageOfMap()
    {
    	Variables.aliasAndPageOfMap.put(Constants.ALIAS_BIOHAZARD, Constants.PAGE_OF_BIOHAZARD);
    	Variables.aliasAndPageOfMap.put(Constants.ALIAS_CANCER_RESEARCH_GROUP, Constants.PAGE_OF_CANCER_RESEARCH_GROUP);
    	Variables.aliasAndPageOfMap.put(Constants.ALIAS_COLLECTION_PROTOCOL, Constants.PAGE_OF_COLLECTION_PROTOCOL);
    	Variables.aliasAndPageOfMap.put(Constants.ALIAS_COLLECTION_PROTOCOL_REG, Constants.PAGE_OF_COLLECTION_PROTOCOL_REG);
    	Variables.aliasAndPageOfMap.put(Constants.ALIAS_DEPARTMENT, Constants.PAGE_OF_DEPARTMENT);
    	Variables.aliasAndPageOfMap.put(Constants.ALIAS_DISTRIBUTION, Constants.PAGE_OF_DISTRIBUTION);
    	Variables.aliasAndPageOfMap.put(Constants.ALIAS_DISTRIBUTION_PROTOCOL, Constants.PAGE_OF_DISTRIBUTION_PROTOCOL);
    	Variables.aliasAndPageOfMap.put(Constants.ALIAS_DISTRIBUTION_ARRAY, Constants.PAGE_OF_DISTRIBUTION_ARRAY);
    	Variables.aliasAndPageOfMap.put(Constants.ALIAS_INSTITUTE, Constants.PAGE_OF_INSTITUTE);
    	Variables.aliasAndPageOfMap.put(Constants.ALIAS_PARTICIPANT, Constants.PAGE_OF_PARTICIPANT);
    	Variables.aliasAndPageOfMap.put(Constants.ALIAS_SITE, Constants.PAGE_OF_SITE);
    	Variables.aliasAndPageOfMap.put(Constants.ALIAS_SPECIMEN, Constants.PAGEOF_NEW_SPECIMEN);
    	Variables.aliasAndPageOfMap.put(Constants.ALIAS_SPECIMEN_ARRAY,Constants.PAGE_OF_SPECIMEN_ARRAY);
    	Variables.aliasAndPageOfMap.put(Constants.ALIAS_SPECIMEN_ARRAY_TYPE, Constants.PAGE_OF_SPECIMEN_ARRAY_TYPE);
    	Variables.aliasAndPageOfMap.put(Constants.ALIAS_SPECIMEN_COLLECTION_GROUP, Constants.PAGE_OF_SPECIMEN_COLLECTION_GROUP);
    	Variables.aliasAndPageOfMap.put(Constants.ALIAS_STORAGE_CONTAINER, Constants.PAGE_OF_STORAGE_CONTAINER);
    	Variables.aliasAndPageOfMap.put(Constants.ALIAS_STORAGE_TYPE, Constants.PAGE_OF_STORAGE_TYPE);
    	Variables.aliasAndPageOfMap.put(Constants.ALIAS_USER, Constants.PAGE_OF_USER);
    	logger.debug("Initialization of aliasAndPageOf Map completed...");
    }
    /** (non-Javadoc)
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
			logger.debug("Exception occured while shutting instance of CatissueCoreCacheManager");
			e.printStackTrace();
		}
	 }
}