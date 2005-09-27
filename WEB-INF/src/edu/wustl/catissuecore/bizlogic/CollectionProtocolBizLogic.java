/**
 * <p>Title: CollectionProtocolBizLogic Class>
 * <p>Description:	CollectionProtocolBizLogic is used to add CollectionProtocol information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on Aug 09, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import net.sf.hibernate.HibernateException;
import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.Roles;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SecurityDataBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;


/**
 * CollectionProtocolBizLogic is used to add CollectionProtocol information into the database using Hibernate.
 * @author Mandar Deshmukh
 */
public class CollectionProtocolBizLogic extends DefaultBizLogic implements Roles
{
	/**
     * Saves the CollectionProtocol object in the database.
	 * @param obj The CollectionProtocol object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 * @throws HibernateException Exception thrown during hibernate operations.
     */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		CollectionProtocol collectionProtocol = (CollectionProtocol)obj;
		
		setPrincipalInvestigator(dao,collectionProtocol);		
		setCoordinatorCollection(dao,collectionProtocol);
		
		dao.insert(collectionProtocol,sessionDataBean, true, true);
		
		Iterator it = collectionProtocol.getCollectionProtocolEventCollection().iterator();		
		while(it.hasNext())
		{
			CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent)it.next();
			collectionProtocolEvent.setCollectionProtocol(collectionProtocol);
			dao.insert(collectionProtocolEvent,sessionDataBean, true, true);
			
			Iterator srIt = collectionProtocolEvent.getSpecimenRequirementCollection().iterator();
			while(srIt.hasNext())
			{
				SpecimenRequirement specimenRequirement = (SpecimenRequirement)srIt.next();
				specimenRequirement.getCollectionProtocolEventCollection().add(collectionProtocolEvent);
				dao.insert(specimenRequirement,sessionDataBean, true, true);
			}
		}
		HashSet protectionObjects= new HashSet();
		protectionObjects.add(collectionProtocol);
		try
        {
            SecurityManager.getInstance(this.getClass()).insertAuthorizationData(getAuthorizationData(collectionProtocol),protectionObjects,getDynamicGroups(collectionProtocol));
        }
        catch (SMException e)
        {
            Logger.out.error("Exception in Authorization: "+e.getMessage(),e);
        }
	}
	
	/**
     * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
     */
    protected void update(DAO dao,Object obj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
    {
    	CollectionProtocol collectionProtocol = (CollectionProtocol)obj;
		
    	//collectionProtocol.setPrincipalInvestigator(null);
    	//collectionProtocol.getUserCollection().clear();
		//setPrincipalInvestigator(dao,collectionProtocol);		
		//setCoordinatorCollection(dao,collectionProtocol);
		
		dao.update(collectionProtocol, sessionDataBean, true, true, false);
		
		Iterator it = collectionProtocol.getCollectionProtocolEventCollection().iterator();		
		while(it.hasNext())
		{
			CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent)it.next();
			collectionProtocolEvent.setCollectionProtocol(collectionProtocol);
			dao.update(collectionProtocolEvent, sessionDataBean, true, true, false);
			
			Iterator srIt = collectionProtocolEvent.getSpecimenRequirementCollection().iterator();
			while(srIt.hasNext())
			{
				SpecimenRequirement specimenRequirement = (SpecimenRequirement)srIt.next();
				
				Logger.out.debug("specimenRequirement "+specimenRequirement);
				
				
				specimenRequirement.getCollectionProtocolEventCollection().add(collectionProtocolEvent);
				dao.update(specimenRequirement, sessionDataBean, true, true, false);
			}
		}
		
		
		Logger.out.debug("collectionProtocol.getActivityStatus() "+collectionProtocol.getActivityStatus());
		if(collectionProtocol.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
		{
			Logger.out.debug("collectionProtocol.getActivityStatus() "+collectionProtocol.getActivityStatus());
			Long collectionProtocolIDArr[] = {collectionProtocol.getSystemIdentifier()};
			
			CollectionProtocolRegistrationBizLogic bizLogic = (CollectionProtocolRegistrationBizLogic)BizLogicFactory.getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
			bizLogic.disableRelatedObjectsForCollectionProtocol(dao,collectionProtocolIDArr);
		}
    }

    /**
     * This method returns collection of UserGroupRoleProtectionGroup objects that speciefies the 
     * user group protection group linkage through a role. It also specifies the groups the protection  
     * elements returned by this class should be added to.
     * @return
     */
    public Vector getAuthorizationData(AbstractDomainObject obj)
    {
        Logger.out.debug("--------------- In here ---------------");
        Vector authorizationData = new Vector();
        Set group = new HashSet();
        SecurityDataBean userGroupRoleProtectionGroupBean;
        String protectionGroupName;
        gov.nih.nci.security.authorization.domainobjects.User user ;
        Collection coordinators;
        User aUser;
        
        CollectionProtocol collectionProtocol = (CollectionProtocol)obj;
        String userId = new String();
        try
        {
            user = new gov.nih.nci.security.authorization.domainobjects.User();
            userId = String.valueOf(collectionProtocol.getPrincipalInvestigator().getSystemIdentifier());
            Logger.out.debug(" PI ID: "+userId);
            user = SecurityManager.getInstance(this.getClass()).getUserById(userId);
            Logger.out.debug(" PI: "+user.getLoginName());
            group.add(user);
        }
        catch (SMException e)
        {
            Logger.out.error("Exception in Authorization: "+e.getMessage(),e);
        }
        
        // Protection group of PI
        protectionGroupName = new String(Constants.getCollectionProtocolPGName(collectionProtocol.getSystemIdentifier()));
        userGroupRoleProtectionGroupBean = new SecurityDataBean();
        userGroupRoleProtectionGroupBean.setUser(userId);
        userGroupRoleProtectionGroupBean.setRoleName(PI);
        userGroupRoleProtectionGroupBean.setGroupName("PI_COLLECTION_PROTOCOL_"+collectionProtocol.getSystemIdentifier());
        userGroupRoleProtectionGroupBean.setProtectionGroupName(protectionGroupName);
        userGroupRoleProtectionGroupBean.setGroup(group);
        authorizationData.add(userGroupRoleProtectionGroupBean);
        
        
        // Protection group of coordinators
        try
        {
            coordinators = collectionProtocol.getUserCollection();
            group = new HashSet();
            for(Iterator it = coordinators.iterator();it.hasNext();)
            {
                 aUser  =(User)it.next();
                 userId = String.valueOf(aUser.getSystemIdentifier());
                 Logger.out.debug(" COORDINATOR ID: "+userId);
                 user = SecurityManager.getInstance(this.getClass()).getUserById(userId);
                 Logger.out.debug(" COORDINATOR: "+user.getLoginName());
                 group.add(user);
            }
            
        }
        catch (SMException e)
        {
            Logger.out.error("Exception in Authorization: "+e.getMessage(),e);
        }
        protectionGroupName = new String(Constants.getCollectionProtocolPGName(collectionProtocol.getSystemIdentifier()));
        userGroupRoleProtectionGroupBean = new SecurityDataBean();
        userGroupRoleProtectionGroupBean.setUser(userId);
        userGroupRoleProtectionGroupBean.setRoleName(READ_ONLY);
        userGroupRoleProtectionGroupBean.setGroupName("COORDINATORS_COLLECTION_PROTOCOL_"+collectionProtocol.getSystemIdentifier());
        userGroupRoleProtectionGroupBean.setProtectionGroupName(protectionGroupName);
        userGroupRoleProtectionGroupBean.setGroup(group);
        authorizationData.add(userGroupRoleProtectionGroupBean);
        
        Logger.out.debug(authorizationData.toString());
        return authorizationData;
    }
    
//    public Set getProtectionObjects(AbstractDomainObject obj)
//    {
//        Set protectionObjects = new HashSet();
//        CollectionProtocolEvent collectionProtocolEvent;
//        SpecimenRequirement specimenRequirement;
//        
//        CollectionProtocol collectionProtocol = (CollectionProtocol)obj;
//        protectionObjects.add(collectionProtocol);
//        Collection collectionProtocolEventCollection = collectionProtocol.getCollectionProtocolEventCollection();
//        if(collectionProtocolEventCollection != null)
//        {
//           for(Iterator it = collectionProtocolEventCollection.iterator(); it.hasNext();)
//           {
//               collectionProtocolEvent = (CollectionProtocolEvent) it.next();
//               if(collectionProtocolEvent !=null)
//               {
//                   protectionObjects.add(collectionProtocolEvent);
//                   for(Iterator it2=collectionProtocolEvent.getSpecimenRequirementCollection().iterator();it2.hasNext();)
//                   {
//                       protectionObjects.add(it2.next());
//                   }
//               }
//               
//           }
//        }
//        Logger.out.debug(protectionObjects.toString());
//        return protectionObjects;
//    }

    public String[] getDynamicGroups(AbstractDomainObject obj)
    {
        String[] dynamicGroups=null;
        return dynamicGroups;
        
    }
    
    //This method sets the Principal Investigator.
	private void setPrincipalInvestigator(DAO dao,CollectionProtocol collectionProtocol) throws DAOException
	{
		//List list = dao.retrieve(User.class.getName(), "systemIdentifier", collectionProtocol.getPrincipalInvestigator().getSystemIdentifier());
		//if (list.size() != 0)
		Object obj = dao.retrieve(User.class.getName(),  collectionProtocol.getPrincipalInvestigator().getSystemIdentifier());
		if (obj != null)
		{
			User pi = (User) obj;//list.get(0);
			collectionProtocol.setPrincipalInvestigator(pi);
		}
	}
	
	//This method sets the User Collection.
	private void setCoordinatorCollection(DAO dao,CollectionProtocol collectionProtocol) throws DAOException
	{
		Long piID = collectionProtocol.getPrincipalInvestigator().getSystemIdentifier();
		Logger.out.debug("Coordinator Size "+collectionProtocol.getUserCollection().size());
		Collection coordinatorColl = new HashSet();
		
		Iterator it = collectionProtocol.getUserCollection().iterator();
		while(it.hasNext())
		{
			User aUser  =(User)it.next();
			if(!aUser.getSystemIdentifier().equals(piID))
			{
				Logger.out.debug("Coordinator ID :"+aUser.getSystemIdentifier());
				Object obj = dao.retrieve(User.class.getName(),  aUser.getSystemIdentifier());
				if (obj != null)
				{
					User coordinator = (User) obj;//list.get(0);
					coordinatorColl.add(coordinator);
					coordinator.getCollectionProtocolCollection().add(collectionProtocol);
				}
			}
		}
		collectionProtocol.setUserCollection(coordinatorColl);
	}
    
	public void setPrivilege(DAO dao, String privilegeName, Class objectType, Long[] objectIds, Long userId, String roleId, boolean assignToUser) throws SMException, DAOException
    {
	    super.setPrivilege(dao,privilegeName,objectType,objectIds,userId, roleId, assignToUser);
	   
		CollectionProtocolRegistrationBizLogic bizLogic = (CollectionProtocolRegistrationBizLogic)BizLogicFactory.getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
		bizLogic.assignPrivilegeToRelatedObjectsForCP(dao,privilegeName,objectIds,userId);
    }
	
	
}