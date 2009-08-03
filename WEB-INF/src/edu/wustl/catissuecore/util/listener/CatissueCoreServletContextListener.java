/*
 * $Name: 1.41.2.41.2.3 $
 *
 * */
package edu.wustl.catissuecore.util.listener;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import net.sf.ehcache.CacheException;
import titli.model.util.TitliResultGroup;
import edu.wustl.cab2b.server.path.PathFinder;
import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.annotations.AnnotationUtil;
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
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.CVSTagReader;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.query.util.listener.QueryCoreServletContextListenerUtil;
import edu.wustl.simplequery.bizlogic.QueryBizLogic;


/**
 *
 * @author aarti_sharma
 *
 * */
public class CatissueCoreServletContextListener implements ServletContextListener
{
	private static Logger logger = Logger.getCommonLogger(CatissueCoreServletContextListener.class);

	/**
	 * DATASOURCE_JNDI_NAME.
	 */
	String CATISSUE_DATASOURCE_JNDI_NAME = "java:/catissuecore";
	String DE_DATASOURCE_JNDI_NAME = "java:/dynamicextensions";

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent sce)
    {
		try{
    		logger.info("Initializing catissue application");
	    	ServletContext servletContext = sce.getServletContext();
	    	CommonServiceLocator.getInstance().setAppHome(sce.getServletContext().getRealPath("")); 
	    	System.out.println(":::::::::::::Application home ::::::::::::"+CommonServiceLocator.getInstance().getAppHome());
	    	//Logger.configDefaultLogger(servletContext);
	    	ErrorKey.init("~");
	    	LoggerConfig.configureLogger(CommonServiceLocator.getInstance().getPropDirPath());
	        ApplicationProperties.initBundle(servletContext.getInitParameter("resourcebundleclass"));
			setGlobalVariable();
			logger =Logger.getCommonLogger(CatissueCoreServletContextListener.class);
	        initCatissueParams();
			//logApplnInfo();
			//initCDEManager();
			//initCDEManager();
			DefaultValueManager.validateAndInitDefaultValueMap();
			logger.info("Initialization complete");
    	}
    	catch(Exception e)
    	{
    		logger.error("Application failed to initialize");
    		throw new RuntimeException( e.getLocalizedMessage(), e);

    	}
    	QueryCoreServletContextListenerUtil.contextInitialized(sce,"java:/query");
    }

	/**
	 * @throws Exception
	 * @throws ClassNotFoundException
	 * @throws DAOException
	 */
	public void initCatissueParams() throws Exception, ClassNotFoundException, DAOException
	{
		edu.wustl.query.util.global.Utility.setReadDeniedAndEntitySqlMap();
		addDefaultProtectionGroupsToMap();

		//Class.forName(DBUtil.class.getName());
		//	Variables.databaseName=HibernateMetaData.getDataBaseName();
			QueryBizLogic bLogic = new QueryBizLogic();
			bLogic.initializeQueryData();
			//setDBFunctionNamesConstants();
			createAliasAndPageOfMap();

			LabelAndBarcodeGeneratorInitializer.init();
//			Added By geeta 
			InitialiseVariablesForEdinburgh();
			
			
//			Added By geeta 
			InitialiseVariablesForDFCI();
			initialiseVariableForAppInfo();
			initCatissueCache();
			initEntityCache();
			Utility.initializePrivilegesMap();
			initTitliIndex();
			initCDEManager();
			//added to set printer related information
			String absolutePath=CommonServiceLocator.getInstance().getPropDirPath() +File.separator+"PrintServiceImplementor.properties";
			Variables.setPrinterInfo(absolutePath);
			System.setProperty("app.propertiesDir",CommonServiceLocator.getInstance().getPropDirPath());

		/*Class.forName(DBUtil.class.getName());
		Variables.databaseName=HibernateMetaData.getDataBaseName();
		QueryBizLogic.initializeQueryData();
		setDBFunctionNamesConstants();
		createAliasAndPageOfMap();
        
		LabelAndBarcodeGeneratorInitializer.init();
		
//		Added By geeta 
		InitialiseVariablesForEdinburgh();
		
		
//		Added By geeta 
		InitialiseVariablesForDFCI();
		
		initCatissueCache();
		initEntityCache();
		Utility.initializePrivilegesMap();
		initTitliIndex();
		initCDEManager();
		//added to set printer related information
		String absolutePath=Variables.propertiesDirPath +File.separator+"PrintServiceImplementor.properties";
		Variables.setPrinterInfo(absolutePath);*/

	}
	
	/**
	 * @throws Exception
	 */
	private void setGlobalVariable() throws Exception
	{
		String path = System.getProperty("app.propertiesFile");
    	XMLPropertyHandler.init(path);
    	File propetiesDirPath = new File(path);
		int maximumTreeNodeLimit = Integer.parseInt(XMLPropertyHandler.getValue(Constants.MAXIMUM_TREE_NODE_LIMIT));
		Variables.maximumTreeNodeLimit = maximumTreeNodeLimit;
	}

	/**
	 *
	 */
	private void initTitliIndex()
	{
		TitliResultGroup.isTitliConfigured = Boolean.parseBoolean(XMLPropertyHandler.getValue(Constants.TITLI_CONFIGURED));
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
	        DataSource ds = (DataSource)ctx.lookup(CATISSUE_DATASOURCE_JNDI_NAME);
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
		/*IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic)factory.getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
		
		Map storageContainersMap = storageContainerBizLogic.getAllocatedContainerMap();
		//removed participant cache
		//ParticipantBizLogic bizlogic = (ParticipantBizLogic)factory.getBizLogic(Constants.PARTICIPANT_FORM_ID);
		//Map participantMap = bizlogic.getAllParticipants();

		 //CollectionProtocolRegistrationBizLogic cBizLogic = (CollectionProtocolRegistrationBizLogic)factory.getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
		//List participantRegInfoList = cBizLogic.getAllParticipantRegistrationInfo();
		try
		{
			CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager.getInstance();
			//catissueCoreCacheManager.addObjectToCache(Constants.MAP_OF_PARTICIPANTS,(HashMap) participantMap);
			catissueCoreCacheManager.addObjectToCache(Constants.MAP_OF_STORAGE_CONTAINERS,(TreeMap)storageContainersMap);
			//catissueCoreCacheManager.addObjectToCache(Constants.LIST_OF_REGISTRATION_INFO,(Vector)participantRegInfoList);
		}
		catch (CacheException e)
		{
			System.out.println("exception  "+ e.getMessage());
			logger.debug("Exception occured while creating instance of CatissueCoreCacheManager");
			e.printStackTrace();
		}*/
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

		String appHome = CommonServiceLocator.getInstance().getAppHome();
		StringBuffer fileName = new StringBuffer();
		fileName.append(appHome).append(File.separator)
		.append(ApplicationProperties.getValue("application.version.file"));

        CVSTagReader cvsTagReader = new CVSTagReader();
        String cvsTag = cvsTagReader.readTag(fileName.toString());
        Variables.applicationCvsTag = cvsTag;
        logger.info("========================================================");
        logger.info("Application Information");
        logger.info("Name: "+CommonServiceLocator.getInstance().getAppName());
        logger.info("CVS TAG: "+Variables.applicationCvsTag);
        logger.info("Path: "+ appHome);
       // logger.info("Database Name: "+Variables.databaseName);
        logger.info("========================================================");
	}


	/**
	 *
	 */
	/*private void setDBFunctionNamesConstants()
	{
		if(Variables.databaseName.equals(Constants.ORACLE_DATABASE))
		{
			//set string/function for oracle
			Variables.datePattern = "mm-dd-yyyy";
			Variables.timePattern = "hh-mi-ss";
			CommonServiceLocator.getInstance().getDatePattern()Function="TO_CHAR";
			Variables.timeFormatFunction="TO_CHAR";
			Variables.dateTostrFunction = "TO_CHAR";
			Variables.strTodateFunction = "TO_DATE";
		}
		else
		{
			Variables.datePattern = "%m-%d-%Y";
			Variables.timePattern = "%H:%i:%s";
			CommonServiceLocator.getInstance().getDatePattern()Function="DATE_FORMAT";
			Variables.timeFormatFunction="TIME_FORMAT";
			Variables.dateTostrFunction = "TO_CHAR";
			Variables.strTodateFunction = "STR_TO_DATE";
		}
	}*/

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

        edu.wustl.security.global.Constants.STATIC_PG_FOR_OBJ_TYPES
		.putAll(protectionGroupsForObjectTypes);
	}

    /**
     * TO create map of Alias verses corresponding PAGE_OF values.
     * This is required in Simple Query Edit feature, It contains mapping of alias name for the query tables & the corresponding PAGE_OF values.
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
    	Variables.aliasAndPageOfMap.put(Constants.ALIAS_SPECIMEN, Constants.PAGE_OF_NEW_SPECIMEN);
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
public void InitialiseVariablesForEdinburgh(){
		
		//String isStateRequired=XMLPropertyHandler.getValue(Constants.IS_STATE_REQUIRED);
		if(Constants.FALSE.equals(XMLPropertyHandler.getValue(Constants.IS_STATE_REQUIRED)))
		{	
			Variables.isStateRequired = false;
		}
	
		if(Constants.TRUE.equals(XMLPropertyHandler.getValue(Constants.IS_CP_TITLE_CHANGE))){
			Variables.isCPTitleChange=true;
		}		
		if(Constants.TRUE.equals(XMLPropertyHandler.getValue(Constants.IS_REMOVE_SSN))){
			Variables.isSSNRemove=true;
		}
		if(Constants.TRUE.equals(XMLPropertyHandler.getValue(Constants.IS_REMOVE_SEX_GENOTYPE))){
			Variables.isSexGenoTypeRemove=true;
		}
		if(Constants.TRUE.equals(XMLPropertyHandler.getValue(Constants.IS_REMOVE_RACE))){
			Variables.isRaceRemove=true;
		}
		if(Constants.TRUE.equals(XMLPropertyHandler.getValue(Constants.IS_REMOVE_ETHNICITY))){
			Variables.isEthnicityRemove=true;
		}
	}

	public void InitialiseVariablesForDFCI(){
		if(Constants.FALSE.equals(XMLPropertyHandler.getValue(Constants.IS_LAST_NAME_NULL)))
		{	
			Variables.isLastNameNull = false;
		}
	}
	/**
	 * app info initialize
	 */
	public void initialiseVariableForAppInfo()
	{
		if(XMLPropertyHandler.getValue(Constants.APP_ADDITIONAL_INFO) != null)
		{
			Variables.applicationAdditionInfo = XMLPropertyHandler.getValue(Constants.APP_ADDITIONAL_INFO);
		}
	}
}
