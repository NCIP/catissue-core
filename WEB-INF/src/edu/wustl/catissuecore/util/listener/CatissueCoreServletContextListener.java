/*
 * $Name: 1.41.2.41.2.3 $
 *
 * */

package edu.wustl.catissuecore.util.listener;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import krishagni.catissueplus.csd.CatissueUserContextProviderImpl;
import krishagni.catissueplus.util.FormProcessor;
import krishagni.catissueplus.util.QuartzSchedulerJobUtil;

import org.apache.commons.io.FilenameUtils;

import titli.model.util.TitliResultGroup;
import au.com.bytecode.opencsv.CSVReader;

import com.krishagni.catissueplus.core.de.ui.UserControlFactory;
import com.krishagni.catissueplus.core.de.ui.UserFieldMapper;
import com.krishagni.catissueplus.core.notification.schedular.ExternalAppFailNotificationSchedular;
import com.krishagni.catissueplus.core.notification.schedular.ExternalAppNotificationSchedular;

import edu.common.dynamicextensions.domain.nui.factory.ControlManager;
import edu.common.dynamicextensions.nutility.BOUtil;
import edu.common.dynamicextensions.nutility.DEApp;
import edu.common.dynamicextensions.nutility.FormProperties;
import edu.common.dynamicextensions.query.PathConfig;
import edu.wustl.bulkoperator.util.BulkEMPIOperationsUtility;
import edu.wustl.bulkoperator.util.BulkOperationUtility;
import edu.wustl.catissuecore.action.bulkOperations.BOTemplateUpdater;
import edu.wustl.catissuecore.cpSync.SyncCPThreadExecuterImpl;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.namegenerator.LabelAndBarcodeGeneratorInitializer;
import edu.wustl.catissuecore.util.EmailHandler;
import edu.wustl.catissuecore.util.HelpXMLPropertyHandler;
import edu.wustl.catissuecore.util.ParticipantAttributeDisplayInfoUtility;
import edu.wustl.catissuecore.util.ProtectionGroups;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.DefaultValueManager;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.audit.AuditManager;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.exception.ParseException;
import edu.wustl.common.participant.utility.ParticipantManagerUtility;
import edu.wustl.common.participant.utility.RaceGenderCodesProperyHandler;
import edu.wustl.common.scheduler.bizLogic.ScheduleBizLogic;
import edu.wustl.common.util.CVSTagReader;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dynamicextensions.formdesigner.mapper.ControlMapper;
import edu.wustl.dynamicextensions.formdesigner.usercontext.CSDProperties;
import edu.wustl.simplequery.bizlogic.QueryBizLogic;

/**
 *
 * @author aarti_sharma 
 *
 * */
public class CatissueCoreServletContextListener implements ServletContextListener
{

	/**	
	 * CatissueCoreServletContextListener Logger.
	 */
	private static final Logger logger = Logger
			.getCommonLogger(CatissueCoreServletContextListener.class);
	/**
	 * DATASOURCE_JNDI_NAME.
	 */
	private static final String JNDI_NAME = "java:/catissuecore";
	
	//class level instance to access methods for registering and unregistering queues
	private final ParticipantManagerUtility participantManagerUtility = new ParticipantManagerUtility();

	/**
	 * This method is called during server startup, It is used when want some initliazation before
	 * server start.
	 * @param sce ServletContextEvent
	 */
	public void contextInitialized(final ServletContextEvent sce)
	{
		try
		{
			logger.info("Initializing catissue application");
			final ServletContext servletContext = sce.getServletContext();
			ApplicationProperties
			.initBundle(servletContext.getInitParameter("resourcebundleclass"));
	
			CommonServiceLocator.getInstance().setAppHome(sce.getServletContext().getRealPath(""));
			logger.info(":::::::::::::Application home ::::::::::::"
					+ CommonServiceLocator.getInstance().getAppHome());
			ErrorKey.init("~");
			AuditManager.init();
			LoggerConfig.configureLogger(CommonServiceLocator.getInstance().getPropDirPath());
			this.setGlobalVariable();
			this.initCatissueParams();
			logApplnInfo();
			DefaultValueManager.validateAndInitDefaultValueMap();
			BulkOperationUtility.changeBulkOperationStatusToFailed();
			//QueryCoreServletContextListenerUtil.contextInitialized(sce, "java:/query");
			if (XMLPropertyHandler.getValue(Constants.EMPI_ENABLED).equalsIgnoreCase("true"))
			{
				BulkEMPIOperationsUtility.changeBulkOperationStatusToFailed();
				// eMPI integration initialization
				initeMPI();
			}
			if (Constants.TRUE.equals(XMLPropertyHandler.getValue("Imaging.enabled")))
			{
				Variables.isImagingConfigurred = true;
			}
			SyncCPThreadExecuterImpl executerImpl = SyncCPThreadExecuterImpl.getInstance();
			executerImpl.init();
			initializeParticipantConfig();
            /** 
             * 	Details: Quartz Scheduler for executing nightly cron jobs
             *  Added By: Ashraf
             */
			
            QuartzSchedulerJobUtil.scheduleQuartzSchedulerJob();
            //QueryDataExportService.initialize();
            
            ExternalAppNotificationSchedular.scheduleExtAppNotifSchedulerJob();
            ExternalAppFailNotificationSchedular.scheduleExtAppFailNotifSchedulerJob();

			CSDProperties.getInstance().setUserContextProvider(new CatissueUserContextProviderImpl());
			
			FormProperties.getInstance().setPostProcessor(new FormProcessor());

            BOUtil.getInstance().setGenerator(new BOTemplateUpdater());

            InitialContext ic = new InitialContext();
			DataSource ds = (DataSource)ic.lookup(JNDI_NAME);
			String dateFomat = CommonServiceLocator.getInstance().getDatePattern();
			String timeFormat = CommonServiceLocator.getInstance().getTimePattern(); 
			
			String dir = new StringBuilder(XMLPropertyHandler.getValue("appserver.home.dir")).append(File.separator)
					.append("os-data").append(File.separator)
					.append("de-file-data").append(File.separator)
					.toString();
			File dirFile = new File(dir);
			if (!dirFile.exists()) {
				if (!dirFile.mkdirs()) {
					throw new RuntimeException("Error couldn't create directory for storing de file data");
				}
			}
						
			DEApp.init(ds, dir, dateFomat,timeFormat);
			initQueryPathsConfig();            
			ControlManager.getInstance().registerFactory(UserControlFactory.getInstance());
			ControlMapper.getInstance().registerControlMapper("userField", new UserFieldMapper());
			
			logger.info("Initialization complete");									
		}
		catch (final Exception e)
		{
			CatissueCoreServletContextListener.logger.error(
					"Application failed to initialize" + e.getMessage(), e);
			throw new RuntimeException(e.getLocalizedMessage(), e);
		}
	}

 	private void initQueryPathsConfig() {
 		String path = System.getProperty("app.propertiesDir") + File.separatorChar + "paths.xml";
 		PathConfig.intialize(path);
	}

	/**
	 * Inite mpi.
	 */
	private void initeMPI()
	{
		try
		{
			checkEMPIAdminUser();
			RaceGenderCodesProperyHandler.init("HL7MesRaceGenderCodes.xml");
			participantManagerUtility.registerWMQListener();
			try
			{
				ParticipantManagerUtility.initialiseParticiapntMatchScheduler();
			}
			catch (Exception excep)
			{
				logger.error(" ####### ERROR WHILE INITIALISING THE SHECUDER FOR PROCESSING THE PARTICIPANTS ######### ");
				logger.error(excep.getMessage(), excep);
			}

		}
		catch (Exception excep)
		{
			logger.error("Could not initialized application, Error in loading the HL7 race gender code property handler.");
			logger.error(excep.getMessage(), excep);
		}
		catch (Error excep)
		{
			logger.error("EMPI : ERROR WHILE REGISTERING WMQ LISTENER ");
			logger.error(excep.getMessage(), excep);
		}

	}

	private void checkEMPIAdminUser() throws ApplicationException, MessagingException
	{
		String eMPIAdminUName = XMLPropertyHandler.getValue(Constants.HL7_LISTENER_ADMIN_USER);
		User validUser = AppUtility.getUser(eMPIAdminUName);
		EmailHandler emailHandlrObj = new EmailHandler();
		if (validUser == null)
		{
			emailHandlrObj.sendEMPIAdminUserNotExitsEmail();
		}
		if (validUser != null
				&& Constants.ACTIVITY_STATUS_CLOSED.equals(validUser.getActivityStatus()))
		{
			emailHandlrObj.sendEMPIAdminUserClosedEmail(validUser);
		}
	}

	/**
	 * Initialize caTissue default properties.
	 * @throws ClassNotFoundException ClassNotFoundException
	 * @throws DAOException DAOException
	 * @throws ParseException ParseException
	 * @throws IOException 
	 */
	public void initCatissueParams() throws ClassNotFoundException, DAOException, ParseException,
			IOException
	{
		//edu.wustl.query.util.global.Utility.setReadDeniedAndEntitySqlMap();
		this.addDefaultProtectionGroupsToMap();
		final QueryBizLogic bLogic = new QueryBizLogic();
		bLogic.initializeQueryData();
		this.createAliasAndPageOfMap();
		LabelAndBarcodeGeneratorInitializer.init();
		this.initialiseVariablesForEdinburgh();
		this.initialiseVariablesForDFCI();
		this.initialiseVariableForAppInfo();
		Utility.initializePrivilegesMap();
		this.initTitliIndex();
		this.initCDEManager();
		this.initDashboardCache();
		this.initReportScheduler();
		initThrottlingModule();
		final String absolutePath = CommonServiceLocator.getInstance().getPropDirPath()
				+ File.separator + "PrintServiceImplementor.properties";
		Variables.setPrinterInfo(absolutePath);
		System.setProperty("app.propertiesDir", CommonServiceLocator.getInstance().getPropDirPath());
	}
	

	private void initThrottlingModule() 
	{
		String timeIntervalInMinutes = XMLPropertyHandler.getValue(Constants.MAXIMUM_TREE_NODE_LIMIT);
		String maxLimits = XMLPropertyHandler.getValue(Constants.MAXIMUM_TREE_NODE_LIMIT);
		final int maximumTreeNodeLimit = Integer.parseInt(maxLimits);
		Variables.throttlingMaxLimit = maximumTreeNodeLimit;
		final long timeInterval = Long.parseLong(timeIntervalInMinutes);
		Variables.throttlingTimeInterval = timeInterval*60*1000;
		
	}

	private void initReportScheduler()
	{
		ScheduleBizLogic scheduleBizLogic = new ScheduleBizLogic();
		try
		{
			scheduleBizLogic.scheduleOnStartUp();
		}
		catch (Exception e)
		{
			CatissueCoreServletContextListener.logger.error("Exception occured while initialising "
					+ "Report Scheduler" + e.getMessage(), e);
			throw new RuntimeException(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Set Global variable.
	 * @throws Exception Exception
	 */
	private void setGlobalVariable() throws Exception
	{
		final String path = System.getProperty("app.propertiesFile");
		XMLPropertyHandler.init(path);
		new File(path);
		final int maximumTreeNodeLimit = Integer.parseInt(XMLPropertyHandler
				.getValue(Constants.MAXIMUM_TREE_NODE_LIMIT));
		Variables.maximumTreeNodeLimit = maximumTreeNodeLimit;
		Variables.isToDisplayAdminEmail = Boolean.parseBoolean(XMLPropertyHandler.getValue("display.admin.emails.onSummaryPage"));
		
		HelpXMLPropertyHandler.init(CommonServiceLocator.getInstance().getPropDirPath()
				+ File.separator + "help_links.xml");
	}

	/**
	 * Initialize Titli.
	 */
	private void initTitliIndex()
	{
		TitliResultGroup.isTitliConfigured = Boolean.parseBoolean(XMLPropertyHandler
				.getValue(Constants.KEYWORD_CONFIGURED));
	}

	/**
	 * Initialize CDE Manager.
	 */
	private void initCDEManager()
	{
		try
		{
			CDEManager.init();
		}
		catch (final Exception ex)
		{
			CatissueCoreServletContextListener.logger.error("Could not initialized application, "
					+ "Error in creating CDE manager.");
			CatissueCoreServletContextListener.logger.error(ex.getMessage(), ex);
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Application Information.
	 */
	private void logApplnInfo()
	{

		final String appHome = CommonServiceLocator.getInstance().getAppHome();
		final StringBuffer fileName = new StringBuffer();
		fileName.append(appHome).append(File.separator)
				.append(ApplicationProperties.getValue("application.version.file"));
		final CVSTagReader cvsTagReader = new CVSTagReader();
		final String cvsTag = cvsTagReader.readTag(fileName.toString());
		Variables.applicationCvsTag = cvsTag;
		logger.info("========================================================");
		logger.info("Application Information");
		logger.info("Name: " + CommonServiceLocator.getInstance().getAppName());
		logger.info("CVS TAG: " + Variables.applicationCvsTag);
		logger.info("Path: " + appHome);
		logger.info("========================================================");
	}

	/**
	 * Add Default Protection Groups.
	 */
	private void addDefaultProtectionGroupsToMap()
	{

		final Map<String, String[]> protectionGroupsForObjectTypes = new HashMap<String, String[]>();

		protectionGroupsForObjectTypes.put(Site.class.getName(),
				new String[]{ProtectionGroups.PUBLIC_DATA_GROUP});
		protectionGroupsForObjectTypes.put(Address.class.getName(),
				new String[]{ProtectionGroups.PUBLIC_DATA_GROUP});
		protectionGroupsForObjectTypes.put(StorageContainer.class.getName(),
				new String[]{ProtectionGroups.PUBLIC_DATA_GROUP});
		protectionGroupsForObjectTypes.put(DistributionProtocol.class.getName(),
				new String[]{ProtectionGroups.PUBLIC_DATA_GROUP});
		protectionGroupsForObjectTypes.put(Distribution.class.getName(),
				new String[]{ProtectionGroups.PUBLIC_DATA_GROUP});
		protectionGroupsForObjectTypes.put(User.class.getName(),
				new String[]{ProtectionGroups.PUBLIC_DATA_GROUP});
		protectionGroupsForObjectTypes.put(Participant.class.getName(),
				new String[]{ProtectionGroups.PUBLIC_DATA_GROUP});
		protectionGroupsForObjectTypes.put(CollectionProtocol.class.getName(),
				new String[]{ProtectionGroups.PUBLIC_DATA_GROUP});
		protectionGroupsForObjectTypes.put(CollectionProtocolRegistration.class.getName(),
				new String[]{ProtectionGroups.PUBLIC_DATA_GROUP});
		protectionGroupsForObjectTypes.put(SpecimenCollectionGroup.class.getName(),
				new String[]{ProtectionGroups.PUBLIC_DATA_GROUP});
		protectionGroupsForObjectTypes.put(Specimen.class.getName(),
				new String[]{ProtectionGroups.PUBLIC_DATA_GROUP});
//		protectionGroupsForObjectTypes.put(SpecimenCharacteristics.class.getName(),
//				new String[]{ProtectionGroups.PUBLIC_DATA_GROUP});

		edu.wustl.security.global.Constants.STATIC_PG_FOR_OBJ_TYPES
				.putAll(protectionGroupsForObjectTypes);
	}

	/**
	 * TO create map of Alias verses corresponding PAGE_OF values.
	 * This is required in Simple Query Edit feature,
	 * It contains mapping of alias name for the query tables &
	 * the corresponding PAGE_OF values.
	 * Patch ID: SimpleSearchEdit_9
	 */
	private void createAliasAndPageOfMap()
	{
		Variables.aliasAndPageOfMap.put(Constants.ALIAS_BIOHAZARD, Constants.PAGE_OF_BIOHAZARD);
		Variables.aliasAndPageOfMap.put(Constants.ALIAS_CANCER_RESEARCH_GROUP,
				Constants.PAGE_OF_CANCER_RESEARCH_GROUP);
		Variables.aliasAndPageOfMap.put(Constants.ALIAS_COLLECTION_PROTOCOL,
				Constants.PAGE_OF_COLLECTION_PROTOCOL);
		Variables.aliasAndPageOfMap.put(Constants.ALIAS_COLLECTION_PROTOCOL_REG,
				Constants.PAGE_OF_COLLECTION_PROTOCOL_REG);
		Variables.aliasAndPageOfMap.put(Constants.ALIAS_DEPARTMENT, Constants.PAGE_OF_DEPARTMENT);
		Variables.aliasAndPageOfMap.put(Constants.ALIAS_DISTRIBUTION,
				Constants.PAGE_OF_DISTRIBUTION);
		Variables.aliasAndPageOfMap.put(Constants.ALIAS_DISTRIBUTION_PROTOCOL,
				Constants.PAGE_OF_DISTRIBUTION_PROTOCOL);
		Variables.aliasAndPageOfMap.put(Constants.ALIAS_DISTRIBUTION_ARRAY,
				Constants.PAGE_OF_DISTRIBUTION_ARRAY);
		Variables.aliasAndPageOfMap.put(Constants.ALIAS_INSTITUTE, Constants.PAGE_OF_INSTITUTE);
		Variables.aliasAndPageOfMap.put(Constants.ALIAS_PARTICIPANT, Constants.PAGE_OF_PARTICIPANT);
		Variables.aliasAndPageOfMap.put(Constants.ALIAS_SITE, Constants.PAGE_OF_SITE);
		Variables.aliasAndPageOfMap.put(Constants.ALIAS_SPECIMEN, Constants.PAGE_OF_NEW_SPECIMEN);
		Variables.aliasAndPageOfMap.put(Constants.ALIAS_SPECIMEN_ARRAY,
				Constants.PAGE_OF_SPECIMEN_ARRAY);
		Variables.aliasAndPageOfMap.put(Constants.ALIAS_SPECIMEN_ARRAY_TYPE,
				Constants.PAGE_OF_SPECIMEN_ARRAY_TYPE);
		Variables.aliasAndPageOfMap.put(Constants.ALIAS_SPECIMEN_COLLECTION_GROUP,
				Constants.PAGE_OF_SPECIMEN_COLLECTION_GROUP);
		Variables.aliasAndPageOfMap.put(Constants.ALIAS_STORAGE_CONTAINER,
				Constants.PAGE_OF_STORAGE_CONTAINER);
		Variables.aliasAndPageOfMap.put(Constants.ALIAS_STORAGE_TYPE,
				Constants.PAGE_OF_STORAGE_TYPE);
		Variables.aliasAndPageOfMap.put(Constants.ALIAS_USER, Constants.PAGE_OF_USER);
		logger.debug("Initialization of aliasAndPageOf Map completed...");
	}

	/**
	 * Context destroyed.
	 * Shutting down catch manager
	 * @param sce ServletContextEvent
	 */
	public void contextDestroyed(final ServletContextEvent sce)
	{
		try
		{
			BulkOperationUtility.changeBulkOperationStatusToFailed();
			SyncCPThreadExecuterImpl executerImpl = SyncCPThreadExecuterImpl.getInstance();
			executerImpl.shutdown();

		}
		catch (final DAOException e)
		{
			CatissueCoreServletContextListener.logger.error("Exception occured while updating "
					+ "the Bulk Operation job status." + e.getMessage(), e);
		}
	}

	/**
	 * Initialize variables required for Edinburg requirement.
	 */
	private void initialiseVariablesForEdinburgh()
	{
		if (Constants.FALSE.equals(XMLPropertyHandler.getValue(Constants.IS_STATE_REQUIRED)))
		{
			Variables.isStateRequired = false;
		}
		if (Constants.TRUE.equals(XMLPropertyHandler.getValue(Constants.IS_CP_TITLE_CHANGE)))
		{
			Variables.isCPTitleChange = true;
		}
		if (Constants.TRUE.equals(XMLPropertyHandler.getValue(Constants.IS_REMOVE_SSN)))
		{
			Variables.isSSNRemove = true;
		}
		if (Constants.TRUE.equals(XMLPropertyHandler.getValue(Constants.IS_REMOVE_SEX_GENOTYPE)))
		{
			Variables.isSexGenoTypeRemove = true;
		}
		if (Constants.TRUE.equals(XMLPropertyHandler.getValue(Constants.IS_REMOVE_RACE)))
		{
			Variables.isRaceRemove = true;
		}
		if (Constants.TRUE.equals(XMLPropertyHandler.getValue(Constants.IS_REMOVE_ETHNICITY)))
		{
			Variables.isEthnicityRemove = true;
		}
	}

	/**
	 * Initialize variables required for DFCI requirement.
	 */
	private void initialiseVariablesForDFCI()
	{
		if (Constants.FALSE.equals(XMLPropertyHandler.getValue(Constants.IS_LAST_NAME_NULL)))
		{
			Variables.isLastNameNull = false;
		}
	}

	/**
	 * Application info initialize.
	 */
	private void initialiseVariableForAppInfo()
	{
		if (XMLPropertyHandler.getValue(Constants.APP_ADDITIONAL_INFO) != null)
		{
			Variables.applicationAdditionInfo = XMLPropertyHandler
					.getValue(Constants.APP_ADDITIONAL_INFO);
		}
	}
	private void initializeParticipantConfig() throws Exception
	{
		ParticipantAttributeDisplayInfoUtility.initializeParticipantConfigObject();
	}

	/** This method reads the file containing default dashboard items and system level dashboard items.
	 *  And stores the items in two different Arraylist defined as Constants. These constants are further used
	 *  to display the respective dashboard.
	 */

	private void initDashboardCache() throws IOException
	{

		String filepath = XMLPropertyHandler.getValue(Constants.DASHBOARD_ITEMS_FILE_PATH);
		String filename = FilenameUtils.getName(filepath);
		
		//get the file from server's properties folder
		String server_file_path =  CommonServiceLocator.getInstance().getPropDirPath()
				+ File.separator + filename;
		
		if (filepath != null && !filepath.isEmpty())
		{
			CSVReader reader = new CSVReader(new FileReader(server_file_path));

			List<String[]> dashboardItems = reader.readAll();
			List<String[]> systemDashboardItems = new ArrayList<String[]>();
			List<String[]> defaultDashboardItems = new ArrayList<String[]>();

			if (!dashboardItems.isEmpty())
				dashboardItems.remove(0); //remove the header from items

			for (String[] item : dashboardItems)
			{
				if (item.length == 3)
				{
					String type = item[2];
					if (edu.wustl.common.util.global.Constants.SYSTEM_DASHBOARD
							.equalsIgnoreCase(type))
					{
						systemDashboardItems.add(item);
					}
					else if (edu.wustl.common.util.global.Constants.DEFAULT_DASHBOARD
							.equalsIgnoreCase(type))
					{
						defaultDashboardItems.add(item);
					}
				}
			}
			edu.wustl.common.util.global.Constants.DEFAULT_DASHBOARD_ITEMS = defaultDashboardItems;
			edu.wustl.common.util.global.Constants.SYSTEM_DASHBOARD_ITEMS = systemDashboardItems;

			reader.close();
		}
	}

}
