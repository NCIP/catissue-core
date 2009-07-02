/**
 * <p>
 * Title: SiteHDAO Class>
 * <p>
 * Description: SiteHDAO is used to add site type information into the database
 * using Hibernate.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00 Created on Jul 21, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.audit.AuditManager;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.AuditException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.privilege.PrivilegeManager;

/**
 * SiteHDAO is used to add site type information into the database using
 * Hibernate.
 * @author aniruddha_phadnis
 */
public class SiteBizLogic extends CatissueDefaultBizLogic
{

	private transient Logger logger = Logger.getCommonLogger(SiteBizLogic.class);

	/**
	 * Saves the storageType object in the database.
	 * @param dao :dao
	 * @param obj
	 *            The storageType object to be saved.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @throws BizLogicException : BizLogicException
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			Site site = (Site) obj;

			checkStatus(dao, site.getCoordinator(), "Coordinator");

			Set protectionObjects = new HashSet();

			setCordinator(dao, site);
			AuditManager auditManager = getAuditManager(sessionDataBean);
			dao.insert(site.getAddress());
			auditManager.insertAudit(dao, site.getAddress());
			dao.insert(site);
			auditManager.insertAudit(dao, site);
			protectionObjects.add(site);

			// SecurityManager.getInstance(this.getClass()).
			// insertAuthorizationData(null,
			// protectionObjects, null);

			PrivilegeManager privilegeManager = PrivilegeManager.getInstance();

			privilegeManager.insertAuthorizationData(null, protectionObjects, null, site
					.getObjectId());
		}
		catch (ApplicationException daoExp)
		{
			logger.debug(daoExp.getMessage(), daoExp);
			throw getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}

	}

	/**
	 * Updates the persistent object in the database.
	 * @param dao : dao
	 * @param oldObj : oldObj
	 * @param obj
	 *            The object to be updated.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @throws BizLogicException : BizLogicException
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			Site site = (Site) obj;
			Site siteOld = (Site) oldObj;

			if (!site.getCoordinator().getId().equals(siteOld.getCoordinator().getId()))
			{
				checkStatus(dao, site.getCoordinator(), "Coordinator");
			}

			setCordinator(dao, site);
			// Mandar : 21Aug08 ----start
			if (Status.ACTIVITY_STATUS_CLOSED.toString().equals(site.getActivityStatus()))
			{
				if (isSiteOccupied(dao, site))
				{
					throw getBizLogicException(null, "cnnot.close.site.with.spec", "");

				}
				// else
				// {
				// closeContainers(dao, site);
				// }
			}
			// Mandar : 21Aug08 ----end
			dao.update(site.getAddress());
			dao.update(site);
			AuditManager auditManager = getAuditManager(sessionDataBean);
			// Audit of update.
			Site oldSite = (Site) oldObj;
			auditManager.updateAudit(dao, site.getAddress(), oldSite.getAddress());
			auditManager.updateAudit(dao, obj, oldObj);

		}
		catch (DAOException daoExp)
		{
			logger.debug(daoExp.getMessage(), daoExp);
			throw getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (AuditException e)
		{
			logger.debug(e.getMessage(), e);
			throw getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}
	/**
	 * @param dao : dao
	 * @param site : site
	 * @return boolean
	 * @throws BizLogicException : BizLogicException
	 */
	private boolean isSiteOccupied(DAO dao, Site site) throws BizLogicException
	{
		boolean result = false;
		try
		{
			String hql = "select specimen from "
					+ Specimen.class.getName()
					+ " as specimen where specimen.activityStatus='Active' and specimen.specimenPosition in (select specimenPosition from "
					+ SpecimenPosition.class.getName()
					+ " as specimenPosition where specimenPosition.storageContainer in (select sc from "
					+ StorageContainer.class.getName() + " as sc where sc.site.id =" + site.getId()
					+ " ) )";
			List specimenList = executeQuery(hql);

			if (!specimenList.isEmpty())
			{
				result = true;
			}
		}
		catch (Exception excp)
		{
			logger.debug(excp.getMessage(), excp);
			throw getBizLogicException(null, "error.check.site.fr.spec", "");
		}

		return result;
	}

	// private void closeContainers(DAO dao, Site site) throws BizLogicException
	// {
	// try
	// {
	// ;
	// }
	// catch(Exception excp)
	// {
	// ;
	// }
	// }

	// This method sets the cordinator for a particular site.
	/**
	 * @param dao : dao
	 * @param site : site
	 * @throws BizLogicException : BizLogicException
	 */
	private void setCordinator(DAO dao, Site site) throws BizLogicException
	{
		try
		{
			Object object = dao.retrieveById(User.class.getName(), site.getCoordinator().getId());

			if (object != null)
			{
				User user = (User) object;
				site.setCoordinator(user);
			}
		}
		catch (DAOException daoExp)
		{
			logger.debug(daoExp.getMessage(), daoExp);
			throw getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}

	}

	/*
	 * protected void setPrivilege(DAO dao, String privilegeName, Class
	 * objectType, Long[] objectIds, Long userId, String roleId, boolean
	 * assignToUser, boolean assignOperation) throws SMException, DAOException {
	 * Logger.out.debug(" privilegeName:" + privilegeName + " objectType:" +
	 * objectType + " objectIds:" +
	 * edu.wustl.common.util.Utility.getArrayString(objectIds) + " userId:" +
	 * userId + " roleId:" + roleId + " assignToUser:" + assignToUser);
	 * super.setPrivilege(dao, privilegeName, objectType, objectIds, userId,
	 * roleId, assignToUser, assignOperation); StorageContainerBizLogic
	 * storageContainerBizLogic = (StorageContainerBizLogic) BizLogicFactory
	 * .getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
	 * storageContainerBizLogic.assignPrivilegeToRelatedObjectsForSite(dao,
	 * privilegeName, objectIds, userId, roleId, assignToUser, assignOperation);
	 * // //Giving privilege on related object ids as well // List
	 * relatedAddressObjectsIds = // super.getRelatedObjects(dao,Site.class,new
	 * String[] // {"address."+Constants.SYSTEM_IDENTIFIER},new String[] //
	 * {Constants.SYSTEM_IDENTIFIER}, objectIds); //
	 * super.setPrivilege(dao,privilegeName
	 * ,Address.class,Utility.toLongArray(relatedAddressObjectsIds),userId, //
	 * roleId, assignToUser, assignOperation); }
	 */

	/**
	 * Overriding the parent class's method to validate the enumerated attribute
	 * values
	 * @param obj : obj
	 * @param dao :dao
	 * @param operation : operation
	 * @return boolean
	 * @throws BizLogicException : BizLogicException
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		Site site = (Site) obj;

		/**
		 * Start: Change for API Search --- Jitendra 06/10/2006 In Case of Api
		 * Search, previoulsy it was failing since there was default class level
		 * initialization on domain object. For example in User object, it was
		 * initialized as protected String lastName=""; So we removed default
		 * class level initialization on domain object and are initializing in
		 * method setAllValues() of domain object. But in case of Api Search,
		 * default values will not get set since setAllValues() method of
		 * domainObject will not get called. To avoid null pointer exception, we
		 * are setting the default values same as we were setting in
		 * setAllValues() method of domainObject.
		 */
		ApiSearchUtil.setSiteDefault(site);
		// End:- Change for API Search

		// added by Ashish
		String message = "";
		if (site == null)
		{
			message = ApplicationProperties.getValue("app.site");

			throw getBizLogicException(null, "domain.object.null.err.msg", message);
		}
		Validator validator = new Validator();
		if (validator.isEmpty(site.getName()))
		{
			message = ApplicationProperties.getValue("site.name");

			throw getBizLogicException(null, "errors.item.required", message);
		}

		if (validator.isEmpty(site.getType()))
		{
			message = ApplicationProperties.getValue("site.type");

			throw getBizLogicException(null, "errors.item.required", message);
		}

		if (site.getCoordinator() == null || site.getCoordinator().getId() == null
				|| site.getCoordinator().getId() == 0
				|| site.getCoordinator().getId().longValue() == -1L)
		{
			message = ApplicationProperties.getValue("site.coordinator");

			throw getBizLogicException(null, "errors.item.required", message);
		}

		if (!validator.isEmpty(site.getEmailAddress())
				&& !validator.isValidEmailAddress(site.getEmailAddress()))
		{
			message = ApplicationProperties.getValue("site.emailAddress");

			throw getBizLogicException(null, "errors.item.format", message);
		}

		if (site.getAddress() == null || validator.isEmpty(site.getAddress().getStreet()))
		{
			message = ApplicationProperties.getValue("site.street");

			throw getBizLogicException(null, "errors.item.required", message);
		}

		if (site.getAddress() == null || validator.isEmpty(site.getAddress().getCity()))
		{
			message = ApplicationProperties.getValue("site.city");

			throw getBizLogicException(null, "errors.item.required", message);
		}
		if (edu.wustl.catissuecore.util.global.Variables.isStateRequired)
		{
			if (site.getAddress() == null || validator.isEmpty(site.getAddress().getState()))
			{
				message = ApplicationProperties.getValue("site.state");

				throw getBizLogicException(null, "errors.item.required", message);
			}
		}

		if (site.getAddress() == null || validator.isEmpty(site.getAddress().getCountry()))
		{
			message = ApplicationProperties.getValue("site.country");

			throw getBizLogicException(null, "errors.item.required", message);
		}
		/*
		 * Commented by Geeta to remove the validation on zip code. if
		 * (site.getAddress() == null ||
		 * validator.isEmpty(site.getAddress().getZipCode())) { message =
		 * ApplicationProperties.getValue("site.zipCode"); throw new
		 * DAOException(ApplicationProperties.getValue( "errors.item.required",
		 * message)); } else { if
		 * (!validator.isValidZipCode(site.getAddress().getZipCode())) { message
		 * = ApplicationProperties.getValue("site.zipCode"); throw new
		 * DAOException(ApplicationProperties.getValue( "errors.item.format",
		 * message)); } }
		 */
		if (operation.equals(Constants.EDIT) && !validator.isValidOption(site.getActivityStatus()))
		{
			message = ApplicationProperties.getValue("site.activityStatus");

			throw getBizLogicException(null, "errors.item.required", message);
		}

		// END

		List siteList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_SITE_TYPE, null);

		if (!Validator.isEnumeratedValue(siteList, site.getType()))
		{

			throw getBizLogicException(null, "type.errMsg", message);
		}
		if ((site.getAddress().getState() != null && site.getAddress().getState() != null)
				&& edu.wustl.catissuecore.util.global.Variables.isStateRequired)
		{
			if (!Validator.isEnumeratedValue(CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_STATE_LIST, null), site.getAddress().getState()))
			{

				throw getBizLogicException(null, "state.errMsg", "");
			}
		}

		if (!Validator.isEnumeratedValue(CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_COUNTRY_LIST, null), site.getAddress().getCountry()))
		{

			throw getBizLogicException(null, "country.errMsg", "");
		}

		if (operation.equals(Constants.ADD))
		{
			if (!Status.ACTIVITY_STATUS_ACTIVE.toString().equals(site.getActivityStatus()))
			{

				throw getBizLogicException(null, "activityStatus.active.errMsg", "");
			}
		}
		else
		{
			if (!Validator.isEnumeratedValue(Constants.SITE_ACTIVITY_STATUS_VALUES, site
					.getActivityStatus()))
			{

				throw getBizLogicException(null, "activityStatus.errMsg", "");
			}
		}

		return true;
	}

	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check
	 * (non-Javadoc)
	 * @param dao : dao
	 * @param domainObject : domainObject
	 * @return String
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.DAO,
	 *      java.lang.Object)
	 */
	public String getObjectId(DAO dao, Object domainObject)
	{
		return Constants.ADMIN_PROTECTION_ELEMENT;
	}

	/**
	 * To get PrivilegeName for authorization check from
	 * 'PermissionMapDetails.xml' (non-Javadoc)
	 * @param domainObject : domainObject
	 * @return String
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 */
	protected String getPrivilegeName(Object domainObject)
	{
		String privilegeName = Variables.privilegeDetailsMap.get(Constants.ADD_EDIT_SITE);
		return privilegeName;
	}
	/**
	 * @param siteId : siteId
	 * @return Collection
	 * @throws BizLogicException : BizLogicException
	 */
	public Collection<CollectionProtocol> getRelatedCPs(Long siteId) throws BizLogicException
	{

		DAO dao = null;
		Site site = null;
		try
		{
			dao = openDAOSession(null);
			site = (Site) dao.retrieveById(Site.class.getName(), siteId);
		}
		catch (DAOException e)
		{
			logger.debug(e.getMessage(), e);
			throw getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		finally
		{
			closeDAOSession(dao);
		}

		if (site == null)
		{
			return null;
		}

		return site.getCollectionProtocolCollection();
	}
}