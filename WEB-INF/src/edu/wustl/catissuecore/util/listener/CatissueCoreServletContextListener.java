package edu.wustl.catissuecore.util.listener;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.wustl.common.bizlogic.QueryBizLogic;
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
import edu.wustl.catissuecore.util.ProtectionGroups;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.Permissions;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.dbManager.HibernateMetaData;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;


/**
 * 
 * @author aarti_sharma
 * 
 * */
public class CatissueCoreServletContextListener implements 
						ServletContextListener, ProtectionGroups, Permissions
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
         * Initializing ApplicationProperties with the class 
         * corresponding to resource bundle of the application
         */
        ApplicationProperties.initBundle(sce.getServletContext().getInitParameter("resourcebundleclass"));
        
        /**
         * Getting and storing Home path for the application
         */
        Variables.catissueHome = sce.getServletContext().getRealPath("");
        
        /**
         * Creating Logs Folder inside catissue home
         */
        File logfolder=null;
        try
        {
            logfolder = new File(Variables.catissueHome + "/Logs");
            if (logfolder.exists() == false)
            {
                logfolder.mkdir();
            }
        }
        catch (Exception e)
        {
            ;
        }
        
        
        //All users should be able to view all data by default
        Map protectionGroupsForObjectTypes = new HashMap();
        protectionGroupsForObjectTypes.put(Site.class.getName(),
                new String[] {PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(Address.class.getName(),
                new String[] {PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(StorageContainer.class.getName(),
                new String[] {PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(DistributionProtocol.class.getName(),
                new String[] {PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(Distribution.class.getName(),
                new String[] {PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(User.class.getName(),
                new String[] {PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(Participant.class.getName(),
                new String[] {PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(CollectionProtocol.class.getName(),
                new String[] {PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(CollectionProtocolRegistration.class.getName(),
                new String[] {PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(SpecimenCollectionGroup.class.getName(),
                new String[] {PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(Specimen.class.getName(),
                new String[] {PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(FluidSpecimen.class.getName(),
                new String[] {PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(TissueSpecimen.class.getName(),
                new String[] {PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(MolecularSpecimen.class.getName(),
                new String[] {PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(CellSpecimen.class.getName(),
                new String[] {PUBLIC_DATA_GROUP});
        protectionGroupsForObjectTypes.put(SpecimenCharacteristics.class.getName(),
                new String[] {PUBLIC_DATA_GROUP});
        
        Constants.STATIC_PROTECTION_GROUPS_FOR_OBJECT_TYPES.putAll(protectionGroupsForObjectTypes);
        
        /**
         * setting system property catissue.home which can be ustilized 
         * by the Logger for creating log file
         */
        System.setProperty("catissue.home",Variables.catissueHome + "/Logs");
        
        /**
         * Configuring the Logger class so that it can be utilized by
         * the entire application
         */
        Logger.configure(applicationResourcesPath);
        
        Logger.out.info(ApplicationProperties.getValue("catissue.home")
                + Variables.catissueHome);
        Logger.out.info(ApplicationProperties.getValue("logger.conf.filename")
                + applicationResourcesPath);
        
        QueryBizLogic.initializeQueryData();
        
        try
        {
            // get database name and set variables used in query
            Variables.databaseName=HibernateMetaData.getDataBaseName();
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
        catch (Exception e1)
        {
            e1.printStackTrace();
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
        
        //Initialize CDE Manager
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
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)
    {

    }
}