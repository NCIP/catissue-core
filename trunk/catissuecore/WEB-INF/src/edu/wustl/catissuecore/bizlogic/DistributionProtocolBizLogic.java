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

import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.Roles;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.SecurityDataBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.dao.DAO;
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
public class DistributionProtocolBizLogic extends SpecimenProtocolBizLogic implements Roles
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
    	
    	if(!distributionProtocol.getPrincipalInvestigator().getId().equals(distributionProtocolOld.getPrincipalInvestigator().getId()))
			checkStatus(dao, distributionProtocol.getPrincipalInvestigator(), "Principal Investigator");
    	
		setPrincipalInvestigator(dao,distributionProtocol);
		
		checkForChangedStatus( distributionProtocol ,  distributionProtocolOld);
		dao.update(distributionProtocol, sessionDataBean, true, true, false);
		
		//Audit of Distribution Protocol.
		dao.audit(obj, oldObj, sessionDataBean, true);
		
		Collection oldSpecimenRequirementCollection = distributionProtocolOld.getSpecimenRequirementCollection();
		
		Iterator it = distributionProtocol.getSpecimenRequirementCollection().iterator();
		while(it.hasNext())
		{
			SpecimenRequirement specimenRequirement = (SpecimenRequirement)it.next();
			Logger.out.debug("SpecimenRequirement Id ............... : "+specimenRequirement.getId());
			specimenRequirement.getDistributionProtocolCollection().add(distributionProtocol);
			dao.update(specimenRequirement, sessionDataBean, true, true, false);
			
			SpecimenRequirement oldSpecimenRequirement 
				= (SpecimenRequirement)getCorrespondingOldObject(oldSpecimenRequirementCollection, 
				        specimenRequirement.getId());
			
			dao.audit(specimenRequirement, oldSpecimenRequirement, sessionDataBean, true);
		}
		
		Logger.out.debug("distributionProtocol.getActivityStatus() "+distributionProtocol.getActivityStatus());
		if(distributionProtocol.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
		{
			Logger.out.debug("distributionProtocol.getActivityStatus() "+distributionProtocol.getActivityStatus());
			Long distributionProtocolIDArr[] = {distributionProtocol.getId()};
			
			DistributionBizLogic bizLogic = (DistributionBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.DISTRIBUTION_FORM_ID);
			bizLogic.disableRelatedObjects(dao, distributionProtocolIDArr);
		}
    }
	
	//This method sets the Principal Investigator
	private void setPrincipalInvestigator(DAO dao,DistributionProtocol distributionProtocol) throws DAOException
	{
		Object userObj = dao.retrieve(User.class.getName() , distributionProtocol.getPrincipalInvestigator().getId());
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
        String protectionGroupName = new String(Constants.getDistributionProtocolPGName(distributionProtocol.getId()));
        SecurityDataBean userGroupRoleProtectionGroupBean = new SecurityDataBean();
        userGroupRoleProtectionGroupBean.setUser(userId);
        userGroupRoleProtectionGroupBean.setRoleName(PI);
        userGroupRoleProtectionGroupBean.setGroupName(Constants.getDistributionProtocolPIGroupName(distributionProtocol.getId()));
        userGroupRoleProtectionGroupBean.setProtectionGroupName(protectionGroupName);
        userGroupRoleProtectionGroupBean.setGroup(group);
        authorizationData.add(userGroupRoleProtectionGroupBean);
        
        Logger.out.debug(authorizationData.toString());
        return authorizationData;
    }
    
    // Added by Ashish for validations while passing domain object from API
    /*
    Map values = null;
    int counter = 0;
    public void setAllValues(Object obj)
	{		
		
		DistributionProtocol dProtocol = (DistributionProtocol)obj;
		
		Collection spcimenProtocolCollection = dProtocol.getSpecimenRequirementCollection();
		
		if(spcimenProtocolCollection != null)
		{
			values = new HashMap();
			counter=0;
			
			int i=1;
			
			Iterator it = spcimenProtocolCollection.iterator();
			while(it.hasNext())
			{
				SpecimenRequirement specimenRequirement = (SpecimenRequirement)it.next();

				String key[] = {
				        "SpecimenRequirement:" + i +"_specimenClass",
				        "SpecimenRequirement:" + i +"_unitspan",
				        "SpecimenRequirement:" + i +"_specimenType",
				        "SpecimenRequirement:" + i +"_tissueSite",
				        "SpecimenRequirement:" + i +"_pathologyStatus",
				        "SpecimenRequirement:" + i +"_quantity_value",
				        "SpecimenRequirement:" + i +"_id"
					};
				this.values = setSpecimenRequirement(key , specimenRequirement);
				i++;
				counter++;
			}
			
			//At least one row should be displayed in ADD MORE therefore
			if(counter == 0)
				counter = 1;
		}
	}
    */
    //End
    
    
    
    /**
     * Overriding the parent class's method to validate the enumerated attribute values
     */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
    {
		//Added by Ashish
		//setAllValues(obj);
		//END
		DistributionProtocol protocol = (DistributionProtocol)obj;	
		Collection spReqCollection = protocol.getSpecimenRequirementCollection();
		
		/**
		 * Start: Change for API Search   --- Jitendra 06/10/2006
		 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
		 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
		 * So we removed default class level initialization on domain object and are initializing in method
		 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
		 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
		 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
		 */
        ApiSearchUtil.setSpecimenProtocolDefault(protocol);
        //End:-  Change for API Search 
		
		//Added by Ashish
        
        Validator validator = new Validator();
        String message="";
		if (protocol == null)
		{			
			throw new DAOException(ApplicationProperties.getValue("domain.object.null.err.msg","Distribution Protocol"));	
		}			
		
		if(protocol.getPrincipalInvestigator() == null)
		{
			//message = ApplicationProperties.getValue("collectionprotocol.specimenstatus");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required","Principal Investigator"));	
		}
		
		if (validator.isEmpty(protocol.getTitle()))
		{
			message = ApplicationProperties.getValue("distributionprotocol.protocoltitle");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}
		
		if (validator.isEmpty(protocol.getShortTitle()))
		{
			message = ApplicationProperties.getValue("distributionprotocol.shorttitle");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}
		
//		if (validator.isEmpty(protocol.getIrbIdentifier()))
//		{
//			message = ApplicationProperties.getValue("distributionprotocol.irbid");
//			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
//		}		
		
		if(protocol.getStartDate() != null)
		{
			String errorKey = validator.validateDate(protocol.getStartDate().toString() ,false);
//			if(errorKey.trim().length() >0  )		
//			{
//				message = ApplicationProperties.getValue("distributionprotocol.startdate");
//				throw new DAOException(ApplicationProperties.getValue(errorKey,message));	
//			}
		}
		else
		{
			message = ApplicationProperties.getValue("distributionprotocol.startdate");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}	
			
		//END

		if(spReqCollection != null && spReqCollection.size() != 0)
		{
			List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_SPECIMEN_CLASS,null);			
			
	    	List tissueSiteList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_TISSUE_SITE,null);
	    	
	    	List pathologicalStatusList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_PATHOLOGICAL_STATUS,null);
	    	
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
					/**
					 * Start: Change for API Search   --- Jitendra 06/10/2006
					 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
					 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
					 * So we removed default class level initialization on domain object and are initializing in method
					 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
					 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
					 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
					 */
					ApiSearchUtil.setSpecimenRequirementDefault(requirement);
					//End:-  Change for API Search   
					
					String specimenClass = requirement.getSpecimenClass();
					
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