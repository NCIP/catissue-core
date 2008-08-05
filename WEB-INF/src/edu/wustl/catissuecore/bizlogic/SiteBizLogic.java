/**
 * <p>Title: SiteHDAO Class>
 * <p>Description:	SiteHDAO is used to add site type information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 21, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.security.PrivilegeManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

/**
 * SiteHDAO is used to add site type information into the database using Hibernate.
 * @author aniruddha_phadnis
 */
public class SiteBizLogic extends DefaultBizLogic
{

	/**
	 * Saves the storageType object in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws DAOException, UserNotAuthorizedException
	{
		Site site = (Site) obj;

		checkStatus(dao, site.getCoordinator(), "Coordinator");

		Set protectionObjects = new HashSet();

		setCordinator(dao, site);

		dao.insert(site.getAddress(), sessionDataBean, true, true);
		dao.insert(site, sessionDataBean, true, true);
		protectionObjects.add(site);
		
//		SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null,
//				protectionObjects, null);
		
		PrivilegeManager privilegeManager = PrivilegeManager.getInstance();

		privilegeManager.insertAuthorizationData(null, 
				protectionObjects, null, site.getObjectId());
	}

	/**
	 * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws DAOException, UserNotAuthorizedException
	{
		Site site = (Site) obj;
		Site siteOld = (Site) oldObj;

		if (!site.getCoordinator().getId().equals(siteOld.getCoordinator().getId()))
			checkStatus(dao, site.getCoordinator(), "Coordinator");

		setCordinator(dao, site);

		dao.update(site.getAddress(), sessionDataBean, true, true, false);
		dao.update(site, sessionDataBean, true, true, false);

		//Audit of update.
		Site oldSite = (Site) oldObj;
		dao.audit(site.getAddress(), oldSite.getAddress(), sessionDataBean, true);
		dao.audit(obj, oldObj, sessionDataBean, true);
	}

	// This method sets the cordinator for a particular site.
	private void setCordinator(DAO dao, Site site) throws DAOException
	{
		Object object = dao.retrieve(User.class.getName(), site.getCoordinator().getId());

		if (object != null)
		{
			User user = (User)object;
			site.setCoordinator(user);
		}
	}

	protected void setPrivilege(DAO dao, String privilegeName, Class objectType, Long[] objectIds,
			Long userId, String roleId, boolean assignToUser, boolean assignOperation)
			throws SMException, DAOException
	{
		Logger.out.debug(" privilegeName:" + privilegeName + " objectType:" + objectType
				+ " objectIds:" + edu.wustl.common.util.Utility.getArrayString(objectIds)
				+ " userId:" + userId + " roleId:" + roleId + " assignToUser:" + assignToUser);
		super.setPrivilege(dao, privilegeName, objectType, objectIds, userId, roleId, assignToUser,
				assignOperation);

		StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic) BizLogicFactory
				.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
		storageContainerBizLogic.assignPrivilegeToRelatedObjectsForSite(dao, privilegeName,
				objectIds, userId, roleId, assignToUser, assignOperation);
		//	    //Giving privilege on related object ids as well
		//	    List relatedAddressObjectsIds = super.getRelatedObjects(dao,Site.class,new String[] {"address."+Constants.SYSTEM_IDENTIFIER},new String[] {Constants.SYSTEM_IDENTIFIER}, objectIds);
		//	    super.setPrivilege(dao,privilegeName,Address.class,Utility.toLongArray(relatedAddressObjectsIds),userId, roleId, assignToUser, assignOperation);
	}

	/**
	 * Overriding the parent class's method to validate the enumerated attribute values
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	{
		Site site = (Site) obj;
		
		/**
		 * Start: Change for API Search   --- Jitendra 06/10/2006
		 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
		 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
		 * So we removed default class level initialization on domain object and are initializing in method
		 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
		 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
		 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
		 */
		ApiSearchUtil.setSiteDefault(site);
		//End:- Change for API Search
		
		// added by Ashish
		String message = "";
		if (site == null)
		{
			message = ApplicationProperties.getValue("app.site");
			throw new DAOException(ApplicationProperties.getValue("domain.object.null.err.msg",message));			
		}
		Validator validator = new Validator();
		if (validator.isEmpty(site.getName()))
		{
			message = ApplicationProperties.getValue("site.name");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}

		if (validator.isEmpty(site.getType()))
		{
			message = ApplicationProperties.getValue("site.type");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));			
		}
		
		if (site.getCoordinator()== null || site.getCoordinator().getId() ==null || site.getCoordinator().getId() ==0 || site.getCoordinator().getId().longValue() == -1L)
		{
			message = ApplicationProperties.getValue("site.coordinator");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));
		}

		if (!validator.isEmpty(site.getEmailAddress())
				&& !validator.isValidEmailAddress(site.getEmailAddress()))
		{
			message = ApplicationProperties.getValue("site.emailAddress");
			throw new DAOException(ApplicationProperties.getValue("errors.item.format",message));
		}

		if (site.getAddress() == null || validator.isEmpty(site.getAddress().getStreet()))
		{
			message = ApplicationProperties.getValue("site.street");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}
		
		if (site.getAddress() == null || validator.isEmpty(site.getAddress().getCity()))
		{
			message = ApplicationProperties.getValue("site.city");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}

		if (site.getAddress() == null || validator.isEmpty(site.getAddress().getState()))
		{
			message = ApplicationProperties.getValue("site.state");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}

		if (site.getAddress() == null || validator.isEmpty(site.getAddress().getCountry()))
		{
			message = ApplicationProperties.getValue("site.country");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}

		if (site.getAddress() == null || validator.isEmpty(site.getAddress().getZipCode()))
		{
			message = ApplicationProperties.getValue("site.zipCode");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}
		else
		{
			if (!validator.isValidZipCode(site.getAddress().getZipCode()))
			{
				message = ApplicationProperties.getValue("site.zipCode");
				throw new DAOException(ApplicationProperties.getValue("errors.item.format",message));	
			}
		}
		
//		bug #4349
		if (!validator.isEmpty(site.getAddress().getPhoneNumber()))
		{
			if (!validator.isValidPhoneNumber(site.getAddress().getPhoneNumber()))
			{
				message = ApplicationProperties.getValue("site.phoneNumber");
				throw new DAOException(ApplicationProperties.getValue("error.phonenumber.format",message));	
			}				
		}
				
		if (!validator.isEmpty(site.getAddress().getFaxNumber()))
		{
			if (!validator.isValidPhoneNumber(site.getAddress().getFaxNumber()))
			{
				message = ApplicationProperties.getValue("site.faxNumber");
				throw new DAOException(ApplicationProperties.getValue("error.faxnumber.format",message));	
			}
		}
		//bug #4349 ends

		if (operation.equals(Constants.EDIT) && !validator.isValidOption(site.getActivityStatus()))
		{
			message = ApplicationProperties.getValue("site.activityStatus");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}

		// END

		List siteList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_SITE_TYPE, null);

		if (!Validator.isEnumeratedValue(siteList, site.getType()))
		{
			throw new DAOException(ApplicationProperties.getValue("type.errMsg"));
		}

		if (!Validator.isEnumeratedValue(CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_STATE_LIST, null), site.getAddress().getState()))
		{
			throw new DAOException(ApplicationProperties.getValue("state.errMsg"));
		}

		if (!Validator.isEnumeratedValue(CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_COUNTRY_LIST, null), site.getAddress().getCountry()))
		{
			throw new DAOException(ApplicationProperties.getValue("country.errMsg"));
		}

		if (operation.equals(Constants.ADD))
		{
			if (!Constants.ACTIVITY_STATUS_ACTIVE.equals(site.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties
						.getValue("activityStatus.active.errMsg"));
			}
		}
		else
		{
			if (!Validator.isEnumeratedValue(Constants.SITE_ACTIVITY_STATUS_VALUES, site
					.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.errMsg"));
			}
		}

		return true;
	}
	
	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.AbstractDAO, java.lang.Object)
	 */
	public String getObjectId(AbstractDAO dao, Object domainObject) 
	{
		return Constants.ADMIN_PROTECTION_ELEMENT;
	}
	
	/**
	 * To get PrivilegeName for authorization check from 'PermissionMapDetails.xml'
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 */
	protected String getPrivilegeName(Object domainObject)
    {
    	String privilegeName = Variables.privilegeDetailsMap.get(Constants.ADD_EDIT_SITE);
    	return privilegeName;
    }

	public Collection<CollectionProtocol> getRelatedCPs(Long siteId) 
	{
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		Site site = null;
		
		try 
		{
			dao.openSession(null);
			site = (Site) dao.retrieve(Site.class.getName(), siteId);		
		} 
		catch (DAOException e) 
		{
			Logger.out.debug(e.getMessage(), e);
		}
		
		if(site == null)
		{
			return null;
		}
		
		return site.getCollectionProtocolCollection();
	}
}