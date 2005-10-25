package edu.wustl.catissuecore.util.listener;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.wustl.catissuecore.bizlogic.QueryBizLogic;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.Permissions;
import edu.wustl.catissuecore.util.ProtectionGroups;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.HibernateProperties;
import edu.wustl.catissuecore.util.global.Variables;
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
        protectionGroupsForObjectTypes.put(Site.class.getName(),
                new String[] {ADMINISTRATORS_DATA_GROUP});
        protectionGroupsForObjectTypes.put(Address.class.getName(),
                new String[] {ADMINISTRATORS_DATA_GROUP});
        protectionGroupsForObjectTypes.put(StorageContainer.class.getName(),
                new String[] {ADMINISTRATORS_DATA_GROUP});
        protectionGroupsForObjectTypes.put(DistributionProtocol.class.getName(),
                new String[] {TECHNICIANS_DATA_GROUP});
        protectionGroupsForObjectTypes.put(User.class.getName(),
                new String[] {ADMINISTRATORS_DATA_GROUP});
        protectionGroupsForObjectTypes.put(Participant.class.getName(),
                new String[] {SUPERVISORS_DATA_GROUP});
        protectionGroupsForObjectTypes.put(CollectionProtocol.class.getName(),
                new String[] {ADMINISTRATORS_DATA_GROUP});
        protectionGroupsForObjectTypes.put(SpecimenCollectionGroup.class.getName(),
                new String[] {ADMINISTRATORS_DATA_GROUP});
        protectionGroupsForObjectTypes.put(FluidSpecimen.class.getName(),
                new String[] {ADMINISTRATORS_DATA_GROUP});
        protectionGroupsForObjectTypes.put(TissueSpecimen.class.getName(),
                new String[] {ADMINISTRATORS_DATA_GROUP});
        protectionGroupsForObjectTypes.put(MolecularSpecimen.class.getName(),
                new String[] {ADMINISTRATORS_DATA_GROUP});
        protectionGroupsForObjectTypes.put(CellSpecimen.class.getName(),
                new String[] {ADMINISTRATORS_DATA_GROUP});
        protectionGroupsForObjectTypes.put(SpecimenCharacteristics.class.getName(),
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
        	
//        	Logger.out
//            .debug("edu.wustl.catissuecore.domain.Participant_5"
//                    + Boolean
//                            .toString(SecurityManager
//                                    .getInstance(this.getClass())
//                                    .isAuthorized(
//                                            "sharma.aarti@gmail.com",
//                                            "edu.wustl.catissuecore.domain.Participant_5",
//                                            READ)));
//        	
//        	AbstractBizLogic bizLogic = BizLogicFactory.getBizLogic(Constants.PARTICIPANT_FORM_ID);
////        	bizLogic.setPrivilege(Constants.HIBERNATE_DAO,READ,Participant.class,new Long[] {new Long(5)},new Long(29),null, null, true);
//        	SecurityManager.getInstance(this.getClass()).deAssignPrivilegeToUser("READ",Participant.class,new Long[] {new Long(5)},new Long(29));
//        	Logger.out
//            .debug("After edu.wustl.catissuecore.domain.Participant_5 "
//                    + Boolean
//                            .toString(SecurityManager
//                                    .getInstance(this.getClass())
//                                    .isAuthorized(
//                                            "sharma.aarti@gmail.com",
//                                            "edu.wustl.catissuecore.domain.Participant_5",
//                                            READ)));
        	
//        	ProtectionElement protectionElement = new ProtectionElement();
//        	protectionElement.setObjectId("edu.wustl.catissuecore.domain.Participant_*");
//        	ProtectionElementSearchCriteria protectionElementSearchCriteria = new ProtectionElementSearchCriteria(protectionElement);
//        	List list = SecurityManager.getInstance(this.getClass()).getObjects(protectionElementSearchCriteria);
//        	for(int i = 0; i<list.size(); i++)
//        	{
//        		protectionElement = (ProtectionElement) list.get(i);
//        		Logger.out.debug(protectionElement.getObjectId());
//        	}
        }
        catch (Exception e1)
        {
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