/*
 * $Name: 1.41.2.41.2.3 $
 *
 * */

package edu.wustl.catissuecore.util.listener;

import java.io.File;
import java.io.IOException;

import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import krishagni.catissueplus.util.FormProcessor;

import com.krishagni.catissueplus.core.de.ui.StorageContainerControlFactory;
import com.krishagni.catissueplus.core.de.ui.StorageContainerMapper;
import com.krishagni.catissueplus.core.de.ui.UserControlFactory;
import com.krishagni.catissueplus.core.de.ui.UserFieldMapper;

import edu.common.dynamicextensions.domain.nui.factory.ControlManager;
import edu.common.dynamicextensions.nutility.DEApp;
import edu.common.dynamicextensions.nutility.FormProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.exception.ParseException;
import edu.wustl.common.util.CVSTagReader;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dynamicextensions.formdesigner.mapper.ControlMapper;
import gov.nih.nci.system.dao.DAOException;

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
			LoggerConfig.configureLogger(CommonServiceLocator.getInstance().getPropDirPath());
			this.setGlobalVariable();
			this.initCatissueParams();
			logApplnInfo();
//			BulkOperationUtility.changeBulkOperationStatusToFailed();
			if (Constants.TRUE.equals(XMLPropertyHandler.getValue("Imaging.enabled")))
			{
				Variables.isImagingConfigurred = true;
			}
            
//			CSDProperties.getInstance().setUserContextProvider(new CatissueUserContextProviderImpl());
			
			FormProperties.getInstance().setPostProcessor(new FormProcessor());

//      BOUtil.getInstance().setGenerator(new BOTemplateUpdater());
            
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
			ControlManager.getInstance().registerFactory(UserControlFactory.getInstance());			
			ControlMapper.getInstance().registerControlMapper("userField", new UserFieldMapper());
			
			ControlManager.getInstance().registerFactory(StorageContainerControlFactory.getInstance());
			ControlMapper.getInstance().registerControlMapper("storageContainer", new StorageContainerMapper());
			
			
			logger.info("Initialization complete");									
		}
		catch (final Exception e)
		{
			CatissueCoreServletContextListener.logger.error(
					"Application failed to initialize" + e.getMessage(), e);
			throw new RuntimeException(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Initialize caTissue default properties.
	 * @throws ClassNotFoundException ClassNotFoundException
	 * @throws DAOException DAOException
	 * @throws ParseException ParseException
	 * @throws IOException 
	 */
	public void initCatissueParams() throws ClassNotFoundException, ParseException,
			IOException
	{
		initThrottlingModule();
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
//		final String cvsTag = cvsTagReader.readTag(fileName.toString());
		Variables.applicationCvsTag = "";
		logger.info("========================================================");
		logger.info("Application Information");
		logger.info("Name: " + CommonServiceLocator.getInstance().getAppName());
		logger.info("CVS TAG: " + Variables.applicationCvsTag);
		logger.info("Path: " + appHome);
		logger.info("========================================================");
	}



	/**
	 * Context destroyed.
	 * Shutting down catch manager
	 * @param sce ServletContextEvent
	 */
	public void contextDestroyed(final ServletContextEvent sce)
	{
//		try
//		{
////			BulkOperationUtility.changeBulkOperationStatusToFailed();
//		}
//		catch (final DAOException e)
//		{
//			CatissueCoreServletContextListener.logger.error("Exception occured while updating "
//					+ "the Bulk Operation job status." + e.getMessage(), e);
//		}
	}

}
