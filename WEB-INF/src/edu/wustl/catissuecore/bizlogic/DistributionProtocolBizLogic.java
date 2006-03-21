/**
 * <p>Title: DistributionProtocolBizLogic Class>
 * <p>Description:	DistributionProtocolBizLogic is used to add DistributionProtocol information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on August 9 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.Roles;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.NameValueBean;
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
 * DistributionProtocolBizLogic is used to add DistributionProtocol information into the database using Hibernate.
 * @author Mandar Deshmukh
 */
public class DistributionProtocolBizLogic extends DefaultBizLogic implements Roles
{
	/**
     * Saves the DistributionProtocol object in the database.
	 * @param obj The DistributionProtocol object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
     */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException 
	{
		DistributionProtocol distributionProtocol = (DistributionProtocol)obj;
		
		checkStatus(dao, distributionProtocol.getPrincipalInvestigator(), "Principal Investigator");
		
		setPrincipalInvestigator(dao,distributionProtocol);
		dao.insert(distributionProtocol,sessionDataBean, true, true);

		Iterator it = distributionProtocol.getSpecimenRequirementCollection().iterator();
		while(it.hasNext())
		{
			SpecimenRequirement specimenRequirement = (SpecimenRequirement)it.next();
			specimenRequirement.getDistributionProtocolCollection().add(distributionProtocol);
			dao.insert(specimenRequirement,sessionDataBean, true, true);
		}
		
		//Inserting authorization data
        Set protectionObjects=new HashSet();
        protectionObjects.add(distributionProtocol);
        
	    try
        {
            SecurityManager.getInstance(this.getClass()).insertAuthorizationData(
            		getAuthorizationData(distributionProtocol), protectionObjects, null);
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
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
    {
		DistributionProtocol distributionProtocol = (DistributionProtocol)obj;
		DistributionProtocol distributionProtocolOld = (DistributionProtocol)oldObj;
    	
    	if(!distributionProtocol.getPrincipalInvestigator().getSystemIdentifier().equals(distributionProtocolOld.getPrincipalInvestigator().getSystemIdentifier()))
			checkStatus(dao, distributionProtocol.getPrincipalInvestigator(), "Principal Investigator");
    	
		setPrincipalInvestigator(dao,distributionProtocol);
		checkForChangedStatus( distributionProtocol ,  distributionProtocolOld  );
		dao.update(distributionProtocol, sessionDataBean, true, true, false);
		
		//Audit of Distribution Protocol.
		dao.audit(obj, oldObj, sessionDataBean, true);
		
		Collection oldSpecimenRequirementCollection = distributionProtocolOld.getSpecimenRequirementCollection();

		Iterator it = distributionProtocol.getSpecimenRequirementCollection().iterator();
		while(it.hasNext())
		{
			SpecimenRequirement specimenRequirement = (SpecimenRequirement)it.next();
			Logger.out.debug("SpecimenRequirement Id ............... : "+specimenRequirement.getSystemIdentifier());
			specimenRequirement.getDistributionProtocolCollection().add(distributionProtocol);
			dao.update(specimenRequirement, sessionDataBean, true, true, false);
			
			SpecimenRequirement oldSpecimenRequirement 
				= (SpecimenRequirement)getCorrespondingOldObject(oldSpecimenRequirementCollection, 
				        specimenRequirement.getSystemIdentifier());
			
			dao.audit(specimenRequirement, oldSpecimenRequirement, sessionDataBean, true);
		}
		
		Logger.out.debug("distributionProtocol.getActivityStatus() "+distributionProtocol.getActivityStatus());
		if(distributionProtocol.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
		{
			Logger.out.debug("distributionProtocol.getActivityStatus() "+distributionProtocol.getActivityStatus());
			Long distributionProtocolIDArr[] = {distributionProtocol.getSystemIdentifier()};
			
			DistributionBizLogic bizLogic = (DistributionBizLogic)BizLogicFactory.getBizLogic(Constants.DISTRIBUTION_FORM_ID);
			bizLogic.disableRelatedObjects(dao, distributionProtocolIDArr);
		}
    }
	
	//This method sets the Principal Investigator
	private void setPrincipalInvestigator(DAO dao,DistributionProtocol distributionProtocol) throws DAOException
	{
		Object userObj = dao.retrieve(User.class.getName() , distributionProtocol.getPrincipalInvestigator().getSystemIdentifier());
		if (userObj != null)
		{
			User pi = (User) userObj;
			distributionProtocol.setPrincipalInvestigator(pi);
		}
	}
	
	public void setPrivilege(DAO dao, String privilegeName, Class objectType, Long[] objectIds, Long userId, String roleId, boolean assignToUser, boolean assignOperation) throws SMException, DAOException
    {
	    super.setPrivilege(dao,privilegeName,objectType,objectIds,userId, roleId, assignToUser, assignOperation);
	    
//		DistributionBizLogic bizLogic = (DistributionBizLogic)BizLogicFactory.getBizLogic(Constants.DISTRIBUTION_FORM_ID);
//		bizLogic.assignPrivilegeToRelatedObjectsForDP(dao,privilegeName,objectIds,userId, roleId, assignToUser, assignOperation);
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
        
        DistributionProtocol distributionProtocol = (DistributionProtocol)obj;
        
    	String userId = String.valueOf(distributionProtocol.getPrincipalInvestigator().getCsmUserId());
        Logger.out.debug(" PI ID: "+userId);
        gov.nih.nci.security.authorization.domainobjects.User csmUser = SecurityManager.getInstance(this.getClass()).getUserById(userId);
        Logger.out.debug(" PI: "+csmUser.getLoginName());
        group.add(csmUser);
        
        // Protection group of PI
        String protectionGroupName = new String(Constants.getDistributionProtocolPGName(distributionProtocol.getSystemIdentifier()));
        SecurityDataBean userGroupRoleProtectionGroupBean = new SecurityDataBean();
        userGroupRoleProtectionGroupBean.setUser(userId);
        userGroupRoleProtectionGroupBean.setRoleName(PI);
        userGroupRoleProtectionGroupBean.setGroupName(Constants.getDistributionProtocolPIGroupName(distributionProtocol.getSystemIdentifier()));
        userGroupRoleProtectionGroupBean.setProtectionGroupName(protectionGroupName);
        userGroupRoleProtectionGroupBean.setGroup(group);
        authorizationData.add(userGroupRoleProtectionGroupBean);
        
        Logger.out.debug(authorizationData.toString());
        return authorizationData;
    }
    
    /**
     * Overriding the parent class's method to validate the enumerated attribute values
     */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
    {
		DistributionProtocol protocol = (DistributionProtocol)obj;
		Collection spReqCollection = protocol.getSpecimenRequirementCollection();

		if(spReqCollection != null && spReqCollection.size() != 0)
		{
			List specimenClassList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_SPECIMEN_CLASS,null);

//	    	NameValueBean undefinedVal = new NameValueBean(Constants.UNDEFINED,Constants.UNDEFINED);
	    	List tissueSiteList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_TISSUE_SITE,null);

	    	List pathologicalStatusList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_PATHOLOGICAL_STATUS,null);
 
			Iterator it = spReqCollection.iterator();

			while(it.hasNext())
			{
				SpecimenRequirement requirement = (SpecimenRequirement)it.next();
				
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

					if(specimenClass.equals(Constants.CELL))
			    	{
			    		if(requirement.getSpecimenType() != null)
			    		{
			    			throw new DAOException(ApplicationProperties.getValue("protocol.type.errMsg"));
			    		}
			    	}
					else if(!Validator.isEnumeratedValue(Utility.getSpecimenTypes(specimenClass),requirement.getSpecimenType()))
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