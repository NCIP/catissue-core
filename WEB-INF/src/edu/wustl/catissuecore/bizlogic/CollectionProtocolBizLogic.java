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
import java.util.List;
import java.util.Set;
import java.util.Vector;

import net.sf.hibernate.HibernateException;
import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.Roles;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.SecurityDataBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
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
		
		checkStatus(dao, collectionProtocol.getPrincipalInvestigator(), "Principal Investigator");
		
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
            SecurityManager.getInstance(this.getClass()).insertAuthorizationData(
            		getAuthorizationData(collectionProtocol),
					protectionObjects,
					getDynamicGroups(collectionProtocol));
        }
        catch (SMException e)
        {
        	throw handleSMException(e);
        }
	}
	
	/**
     * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
     */
    protected void update(DAO dao,Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
    {
    	CollectionProtocol collectionProtocol = (CollectionProtocol)obj;
    	CollectionProtocol collectionProtocolOld = (CollectionProtocol)oldObj;
    	Logger.out.debug("PI OB*****************8"+collectionProtocol.getPrincipalInvestigator());
    	Logger.out.debug("PI Identifier................."+collectionProtocol.getPrincipalInvestigator().getSystemIdentifier());
    	Logger.out.debug("Email Address*****************8"+collectionProtocol.getPrincipalInvestigator().getEmailAddress());
		Logger.out.debug("Principal Investigator*****************8"+collectionProtocol.getPrincipalInvestigator().getCsmUserId());
    	if(!collectionProtocol.getPrincipalInvestigator().getSystemIdentifier().equals(collectionProtocolOld.getPrincipalInvestigator().getSystemIdentifier()))
			checkStatus(dao, collectionProtocol.getPrincipalInvestigator(), "Principal Investigator");
    	
    	Iterator it = collectionProtocol.getUserCollection().iterator();
		while(it.hasNext())
		{
			User coordinator  = (User)it.next();
			if(!coordinator.getSystemIdentifier().equals(collectionProtocol.getPrincipalInvestigator().getSystemIdentifier()))
			{
				if(!hasCoordinator(coordinator, collectionProtocolOld))
					checkStatus(dao, coordinator, "Coordinator");
			}
			else
				it.remove();
		}
		checkForChangedStatus( collectionProtocol,  collectionProtocolOld  );
		dao.update(collectionProtocol, sessionDataBean, true, true, false);
		
		//Audit of Collection Protocol.
		dao.audit(obj, oldObj, sessionDataBean, true);
		
		Collection oldCollectionProtocolEventCollection 
				= collectionProtocolOld.getCollectionProtocolEventCollection();
		Logger.out.debug("collectionProtocol.getCollectionProtocolEventCollection Size................ : "
		        +collectionProtocol.getCollectionProtocolEventCollection().size());
		
		it = collectionProtocol.getCollectionProtocolEventCollection().iterator();		
		while(it.hasNext())
		{
			CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent)it.next();
			Logger.out.debug("CollectionProtocolEvent Id ............... : "+collectionProtocolEvent.getSystemIdentifier());
			collectionProtocolEvent.setCollectionProtocol(collectionProtocol);
			dao.update(collectionProtocolEvent, sessionDataBean, true, true, false);
			
			//Audit of collectionProtocolEvent
			CollectionProtocolEvent oldCollectionProtocolEvent 
						= (CollectionProtocolEvent)getCorrespondingOldObject(oldCollectionProtocolEventCollection, 
						        collectionProtocolEvent.getSystemIdentifier());
			dao.audit(collectionProtocolEvent, oldCollectionProtocolEvent, sessionDataBean, true);
			
			Iterator srIt = collectionProtocolEvent.getSpecimenRequirementCollection().iterator();
			while(srIt.hasNext())
			{
				SpecimenRequirement specimenRequirement = (SpecimenRequirement)srIt.next();
				
				Logger.out.debug("specimenRequirement "+specimenRequirement);
				
				specimenRequirement.getCollectionProtocolEventCollection().add(collectionProtocolEvent);
				dao.update(specimenRequirement, sessionDataBean, true, true, false);
			}
		}
		
		//Disable related Objects
		Logger.out.debug("collectionProtocol.getActivityStatus() "+collectionProtocol.getActivityStatus());
		if(collectionProtocol.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
		{
			Logger.out.debug("collectionProtocol.getActivityStatus() "+collectionProtocol.getActivityStatus());
			Long collectionProtocolIDArr[] = {collectionProtocol.getSystemIdentifier()};
			
			CollectionProtocolRegistrationBizLogic bizLogic = (CollectionProtocolRegistrationBizLogic)BizLogicFactory.getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
			bizLogic.disableRelatedObjectsForCollectionProtocol(dao,collectionProtocolIDArr);
		}
		
		try
		{
		    updatePIAndCoordinatorGroup(dao, collectionProtocolOld, true);
			
		    Long csmUserId = getCSMUserId(dao, collectionProtocol.getPrincipalInvestigator());
		    if (csmUserId != null)
		    {
		        collectionProtocol.getPrincipalInvestigator().setCsmUserId(csmUserId);
		        Logger.out.debug("PI ....."+collectionProtocol.getPrincipalInvestigator().getCsmUserId());
		        updatePIAndCoordinatorGroup(dao, collectionProtocol, false);
		    }
		}
		catch (SMException smExp)
		{
        	throw handleSMException(smExp);
		}
    }
    
    private void updatePIAndCoordinatorGroup(DAO dao, CollectionProtocol collectionProtocol, boolean operation)throws SMException, DAOException
    {
        Long principalInvestigatorId = collectionProtocol.getPrincipalInvestigator().getCsmUserId();
        Logger.out.debug("principalInvestigatorId.........................."+principalInvestigatorId);
        String userGroupName = Constants.getCollectionProtocolPIGroupName(collectionProtocol.getSystemIdentifier());
        Logger.out.debug("userGroupName.........................."+userGroupName);
        if (operation)
        {
            SecurityManager.getInstance(CollectionProtocolBizLogic.class).removeUserFromGroup(userGroupName,principalInvestigatorId.toString());
        }
        else
        {
            SecurityManager.getInstance(CollectionProtocolBizLogic.class).assignUserToGroup(userGroupName,principalInvestigatorId.toString());
        }
        
        userGroupName = Constants.getCollectionProtocolCoordinatorGroupName(collectionProtocol.getSystemIdentifier());
            
        UserBizLogic userBizLogic = (UserBizLogic)BizLogicFactory.getBizLogic(Constants.USER_FORM_ID);
        Iterator iterator = collectionProtocol.getUserCollection().iterator();
        while (iterator.hasNext())
        {
            User user = (User)iterator.next();
            if (operation)
            {
                SecurityManager.getInstance(CollectionProtocolBizLogic.class).removeUserFromGroup(userGroupName,user.getCsmUserId().toString());
            }
            else
            {
                Long csmUserId = getCSMUserId(dao, user);
    		    if (csmUserId != null)
    		    {
    		        Logger.out.debug("Co-ord ....."+csmUserId);
    		        SecurityManager.getInstance(CollectionProtocolBizLogic.class).assignUserToGroup(userGroupName, csmUserId.toString());
    		    }
            }
        }
    }
    
    /**
     * @param collectionProtocol
     * @return
     * @throws DAOException
     */
    private Long getCSMUserId(DAO dao, User user) throws DAOException
    {
        String [] selectColumnNames = {Constants.CSM_USER_ID};
        String [] whereColumnNames = {Constants.SYSTEM_IDENTIFIER};
        String [] whereColumnCondition = {"="};
        String [] whereColumnValues = {user.getSystemIdentifier().toString()};
        List csmUserIdList = dao.retrieve(User.class.getName(),selectColumnNames,whereColumnNames,
                				whereColumnCondition,whereColumnValues,Constants.AND_JOIN_CONDITION);
        Logger.out.debug("csmUserIdList##########################Size........."+csmUserIdList.size());
        
        if (csmUserIdList.isEmpty() == false)
        {
            Long csmUserId = (Long) csmUserIdList.get(0);
            
            return csmUserId;
        }
        
        return null;
    }
    
    /**
     * This method returns collection of UserGroupRoleProtectionGroup objects that speciefies the 
     * user group protection group linkage through a role. It also specifies the groups the protection  
     * elements returned by this class should be added to.
     * @return
     */
    private Vector getAuthorizationData(AbstractDomainObject obj) throws SMException
    {
        Logger.out.debug("--------------- In here ---------------");

        Vector authorizationData = new Vector();
        Set group = new HashSet();
        
        CollectionProtocol collectionProtocol = (CollectionProtocol)obj;
        
        String userId = String.valueOf(collectionProtocol.getPrincipalInvestigator().getCsmUserId());
        Logger.out.debug(" PI ID: "+userId);
        gov.nih.nci.security.authorization.domainobjects.User user = SecurityManager.getInstance(this.getClass()).getUserById(userId);
        Logger.out.debug(" PI: "+user.getLoginName());
        group.add(user);
        
        // Protection group of PI
        String protectionGroupName = new String(Constants.getCollectionProtocolPGName(collectionProtocol.getSystemIdentifier()));
        SecurityDataBean userGroupRoleProtectionGroupBean = new SecurityDataBean();
        userGroupRoleProtectionGroupBean.setUser(userId);
        userGroupRoleProtectionGroupBean.setRoleName(PI);
        userGroupRoleProtectionGroupBean.setGroupName(Constants.getCollectionProtocolPIGroupName(collectionProtocol.getSystemIdentifier()));
        userGroupRoleProtectionGroupBean.setProtectionGroupName(protectionGroupName);
        userGroupRoleProtectionGroupBean.setGroup(group);
        authorizationData.add(userGroupRoleProtectionGroupBean);
        
        // Protection group of coordinators
        Collection coordinators = collectionProtocol.getUserCollection();
        group = new HashSet();
        for(Iterator it = coordinators.iterator();it.hasNext();)
        {
        	User  aUser  =(User)it.next();
            userId = String.valueOf(aUser.getCsmUserId());
            Logger.out.debug(" COORDINATOR ID: "+userId);
            user = SecurityManager.getInstance(this.getClass()).getUserById(userId);
            Logger.out.debug(" COORDINATOR: "+user.getLoginName());
            group.add(user);
        }
            
        protectionGroupName = new String(Constants.getCollectionProtocolPGName(collectionProtocol.getSystemIdentifier()));
        userGroupRoleProtectionGroupBean = new SecurityDataBean();
        userGroupRoleProtectionGroupBean.setUser(userId);
        userGroupRoleProtectionGroupBean.setRoleName(READ_ONLY);
        userGroupRoleProtectionGroupBean.setGroupName(Constants.getCollectionProtocolCoordinatorGroupName(collectionProtocol.getSystemIdentifier()));
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

    private String[] getDynamicGroups(AbstractDomainObject obj)
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
					
					checkStatus(dao, coordinator, "coordinator");
					
					coordinatorColl.add(coordinator);
					coordinator.getCollectionProtocolCollection().add(collectionProtocol);
				}
			}
		}
		collectionProtocol.setUserCollection(coordinatorColl);
	}
    
	public void setPrivilege(DAO dao, String privilegeName, Class objectType, Long[] objectIds, Long userId, String roleId, boolean assignToUser, boolean assignOperation) throws SMException, DAOException
    {
	    super.setPrivilege(dao,privilegeName,objectType,objectIds,userId, roleId, assignToUser, assignOperation);
	   
//		CollectionProtocolRegistrationBizLogic bizLogic = (CollectionProtocolRegistrationBizLogic)BizLogicFactory.getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
//		bizLogic.assignPrivilegeToRelatedObjectsForCP(dao,privilegeName,objectIds,userId, roleId, assignToUser);
    }
	
	private boolean hasCoordinator(User coordinator, CollectionProtocol collectionProtocol)
	{
		Iterator it = collectionProtocol.getUserCollection().iterator();
		while(it.hasNext())
		{
			User coordinatorOld  = (User)it.next();
			if(coordinator.getSystemIdentifier().equals(coordinatorOld.getSystemIdentifier()))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
     * Overriding the parent class's method to validate the enumerated attribute values
     */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
    {
		CollectionProtocol protocol = (CollectionProtocol)obj;
		Collection eventCollection = protocol.getCollectionProtocolEventCollection();
		
		if(eventCollection != null && eventCollection.size() != 0)
		{
			List specimenClassList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_SPECIMEN_CLASS,null);

//	    	NameValueBean undefinedVal = new NameValueBean(Constants.UNDEFINED,Constants.UNDEFINED);
	    	List tissueSiteList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_TISSUE_SITE,null);

	    	List pathologicalStatusList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_PATHOLOGICAL_STATUS,null);
	    	List clinicalStatusList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_CLINICAL_STATUS,null);

			Iterator eventIterator = eventCollection.iterator();
			
			while(eventIterator.hasNext())
			{
				CollectionProtocolEvent event = (CollectionProtocolEvent)eventIterator.next();
				
				if(event == null)
				{
					throw new DAOException(ApplicationProperties.getValue("collectionProtocol.eventsEmpty.errMsg"));
				}
				else
				{
					if(!Validator.isEnumeratedValue(clinicalStatusList,event.getClinicalStatus()))
					{
						throw new DAOException(ApplicationProperties.getValue("collectionProtocol.clinicalStatus.errMsg"));
					}
					
					Collection reqCollection = event.getSpecimenRequirementCollection();
					
					if(reqCollection != null && reqCollection.size() != 0)
					{
						Iterator reqIterator = reqCollection.iterator();
						
						while(reqIterator.hasNext())
						{
							SpecimenRequirement requirement = (SpecimenRequirement)reqIterator.next();
							
							if(requirement == null)
							{
								throw new DAOException(ApplicationProperties.getValue("protocol.spReqEmpty.errMsg"));
							}
							else
							{
								String specimenClass = Utility.getSpecimenClassName(requirement);
								
								if(!Validator.isEnumeratedValue(specimenClassList,specimenClass))
								{
									throw new DAOException(ApplicationProperties.getValue("protocol.class.errMsg"));
								}

								if(!Validator.isEnumeratedValue(Utility.getSpecimenTypes(specimenClass),requirement.getSpecimenType()))
								{
									throw new DAOException(ApplicationProperties.getValue("protocol.type.errMsg"));
								}
								
								if(!Validator.isEnumeratedValue(tissueSiteList,requirement.getTissueSite()))
								{
									throw new DAOException(ApplicationProperties.getValue("protocol.tissueSite.errMsg"));
								}
								
								if(!Validator.isEnumeratedValue(pathologicalStatusList,requirement.getPathologyStatus()))
								{
									throw new DAOException(ApplicationProperties.getValue("protocol.pathologyStatus.errMsg"));
								}
							}
						}
					}
					else
					{
						throw new DAOException(ApplicationProperties.getValue("protocol.spReqEmpty.errMsg"));
					}
				}
			}
		}
		else
		{
			throw new DAOException(ApplicationProperties.getValue("collectionProtocol.eventsEmpty.errMsg"));
		}
		
		if(operation.equals(Constants.ADD))
		{
			if(!Constants.ACTIVITY_STATUS_ACTIVE.equals(protocol.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.active.errMsg"));
			}
		}
		else
		{
			if(!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES,protocol.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.errMsg"));
			}
		}
		
		return true;
    }
}