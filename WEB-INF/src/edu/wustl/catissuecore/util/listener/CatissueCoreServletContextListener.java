package edu.wustl.catissuecore.util.listener;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.wustl.catissuecore.util.ProtectionGroups;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

/**
 * 
 * @author aarti_sharma
 * 
 * */
public class CatissueCoreServletContextListener
        implements
            ServletContextListener, ProtectionGroups
{

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)
    {
        /**
         * Getting Application Properties file path
         */
        String applicationResourcesPath = sce.getServletContext().getRealPath(
                "WEB-INF")
                + System.getProperty("file.separator")
                + "classes"
                + System.getProperty("file.separator")
                + sce.getServletContext().getInitParameter(
                        "applicationproperties");
        
        
        
        /**
         * Initializing ApplicationProperties with the class 
         * corresponding to resource bundle of the application
         */
        ApplicationProperties.initBundle(sce.getServletContext()
                .getInitParameter("resourcebundleclass"));
        
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
        
        Map protectionGroupsForObjectTypes = new HashMap();
        protectionGroupsForObjectTypes.put("edu.wustl.catissuecore.domain.CollectionProtocol",
                new String[] {ADMINISTRATIVE_DATA_GROUP,PUBLIC_DATA_GROUP});
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

    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)
    {

    }

}