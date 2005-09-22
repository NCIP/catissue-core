package edu.wustl.catissuecore.util.listener;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.wustl.catissuecore.bizlogic.QueryBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.dao.HibernateDAO;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.Permissions;
import edu.wustl.catissuecore.util.ProtectionGroups;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.HibernateProperties;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.util.logger.Logger;

/**
 * 
 * @author aarti_sharma
 * 
 * */
public class CatissueCoreServletContextListener
        implements
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

        HibernateProperties.initBundle(Variables.catissueHome +
        			System.getProperty("file.separator") + "db.properties");

        String str = HibernateProperties.getValue("hibernate.connection.driver_class");
        
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
        protectionGroupsForObjectTypes.put("edu.wustl.catissuecore.domain.Site",
                new String[] {ADMINISTRATORS_DATA_GROUP});
        protectionGroupsForObjectTypes.put("edu.wustl.catissuecore.domain.StorageContainer",
                new String[] {ADMINISTRATORS_DATA_GROUP});
        protectionGroupsForObjectTypes.put("edu.wustl.catissuecore.domain.DistributionProtocol",
                new String[] {ADMINISTRATORS_DATA_GROUP});
        protectionGroupsForObjectTypes.put("edu.wustl.catissuecore.domain.User",
                new String[] {ADMINISTRATORS_DATA_GROUP});
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
        	Logger.out.debug("hibernate.connection.driver_class "+str);
        	
            /*Logger.out
                    .debug("Create Perm to aarti on edu.wustl.catissuecore.domain.CollectionProtocol_37 "
                            + Boolean
                                    .toString(SecurityManager
                                            .getInstance(this.getClass())
                                            .isAuthorized(
                                                    "aarti",
                                                    "edu.wustl.catissuecore.domain.CollectionProtocol_37",
                                                    UPDATE)));
            Logger.out
            .debug("CREATE Perm to admin on edu.wustl.catissuecore.domain.CollectionProtocol_37 "
                    + Boolean
                            .toString(SecurityManager
                                    .getInstance(this.getClass())
                                    .isAuthorized(
                                            "admin",
                                            "edu.wustl.catissuecore.domain.CollectionProtocol_37",
                                            UPDATE)));
            Logger.out
            .debug("CREATE Perm to aarti on edu.wustl.catissuecore.domain.CollectionProtocolEvent_28 "
                    + Boolean
                            .toString(SecurityManager
                                    .getInstance(this.getClass())
                                    .isAuthorized(
                                            "aarti",
                                            "edu.wustl.catissuecore.domain.CollectionProtocolEvent_28",
                                            UPDATE)));
            
            Logger.out
            .debug("CREATE Perm to admin on edu.wustl.catissuecore.domain.CollectionProtocolEvent_28"
                    + Boolean
                            .toString(SecurityManager
                                    .getInstance(this.getClass())
                                    .isAuthorized(
                                            "admin",
                                            "edu.wustl.catissuecore.domain.CollectionProtocolEvent_28",
                                            UPDATE)));*/
        	
        	
//        	UserBizLogic userBizLogic = new UserBizLogic();
//        	List list = userBizLogic.retrieve(User.class.getName());
//        	User user;
//        	for(int i=0; i<list.size();i++)
//        	{
//        	    user = (User) list.get(i);
//        	    if(user!=null)
//        	    {
//        	        Set protectionObjects=new HashSet();
//                    protectionObjects.add(user);
//            	    try
//                    {
//                        SecurityManager.getInstance(this.getClass()).insertAuthorizationData(userBizLogic.getAuthorizationData(user),protectionObjects,null);
//                    }
//                    catch (SMException e)
//                    {
//                        Logger.out.error("Exception in Authorization: "+e.getMessage(),e);
//                    }
//        	    }
//        	}
        	
        	
        }
        catch (Exception e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }  

    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)
    {

    }

}