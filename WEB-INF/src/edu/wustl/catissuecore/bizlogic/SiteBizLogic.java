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

	private transient final Logger logger = Logger.getCommonLogger(SiteBizLogic.class);

	/**
	 * Saves the storageType object in the database.
	 * @param dao :dao
	 * @param obj
	 *            The storageType object to be saved.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @throws BizLogicException : BizLogicException
	 */
	@Override
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final Site site = (Site) obj;

			this.checkStatus(dao, site.getCoordinator(), "Coordinator");

			final Set protectionObjects = new HashSet();

			this.setCordinator(dao, site);
			final AuditManager auditManager = this.getAuditManager(sessionDataBean);
			dao.insert(site.getAddress());
			auditManager.insertAudit(dao, site.getAddress());
			dao.insert(site);
			auditManager.insertAudit(dao, site);
			protectionObjects.add(site);

			// SecurityManager.getInstance(this.getClass()).
			// insertAuthorizationData(null,
			// protectionObjects, null);

			final PrivilegeManager privilegeManager = PrivilegeManager.getInstance();

			privilegeManager.insertAuthorizationData(null, protectionObjects, null, site
					.getObjectId());
		}
		catch (final ApplicationException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
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
	@Override
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final Site site = (Site) obj;
			final Site siteOld = (Site) oldObj;

			if (!site.getCoordinator().getId().equals(siteOld.getCoordinator().getId()))
			{
				this.checkStatus(dao, site.getCoordinator(), "Coordinator");
			}

			this.setCordinator(dao, site);
			// Mandar : 21Aug08 ----start
			if (Status.ACTIVITY_STATUS_CLOSED.toString().equals(site.getActivityStatus()))
			{
				if (this.isSiteOccupied(dao, site))
				{
					throw this.getBizLogicException(null, "cnnot.close.site.with.spec", "");

				}
				// else
				// {
				// closeContainers(dao, site);
				// }
			}
			// Mandar : 21Aug08 ----end
			dao.update(site.getAddress());
			dao.update(site);
			final AuditManager auditManager = this.getAuditManager(sessionDataBean);
			// Audit of update.
			final Site oldSite = (Site) oldObj;
			auditManager.updateAudit(dao, site.getAddress(), oldSite.getAddress());
			auditManager.updateAudit(dao, obj, oldObj);

		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final AuditException e)
		{
			this.logger.debug(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
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
			final String hql = "select specimen from "
					+ Specimen.class.getName()
					+ " as specimen where specimen.activityStatus='Active' and specimen.specimenPosition in (select specimenPosition from "
					+ SpecimenPosition.class.getName()
					+ " as specimenPosition where specimenPosition.storageContainer in (select sc from "
					+ StorageContainer.class.getName() + " as sc where sc.site.id =" + site.getId()
					+ " ) )";
			final List specimenList = this.executeQuery(hql);

			if (!specimenList.isEmpty())
			{
				result = true;
			}
		}
		catch (final Exception excp)
		{
			this.logger.debug(excp.getMessage(), excp);
			throw this.getBizLogicException(null, "error.check.site.fr.spec", "");
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
			final Object object = dao.retrieveById(User.class.getName(), site.getCoordinator()
					.getId());

			if (object != null)
			{
				final User user = (User) object;
				site.setCoordinator(user);
			}
		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
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
	@Override
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		final Site site = (Site) obj;

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

			throw this.getBizLogicException(null, "domain.object.null.err.msg", message);
		}
		final Validator validator = new Validator();
		if (Validator.isEmpty(site.getName()))
		{
			message = ApplicationProperties.getValue("site.name");

			throw this.getBizLogicException(null, "errors.item.required", message);
		}

		if (Validator.isEmpty(site.getType()))
		{
			message = ApplicationProperties.getValue("site.type");

			throw this.getBizLogicException(null, "errors.item.required", message);
		}

		if (site.getCoordinator() == null || site.getCoordinator().getId() == null
				|| site.getCoordinator().getId() == 0
				|| site.getCoordinator().getId().longValue() == -1L)
		{
			message = ApplicationProperties.getValue("site.coordinator");

			throw this.getBizLogicException(null, "errors.item.required", message);
		}

		if (!Validator.isEmpty(site.getEmailAddress())
				&& !validator.isValidEmailAddress(site.getEmailAddress()))
		{
			message = ApplicationProperties.getValue("site.emailAddress");

			throw this.getBizLogicException(null, "errors.item.format", message);
		}

		if (site.getAddress() == null || Validator.isEmpty(site.getAddress().getStreet()))
		{
			message = ApplicationProperties.getValue("site.street");

			throw this.getBizLogicException(null, "errors.item.required", message);
		}

		if (site.getAddress() == null || Validator.isEmpty(site.getAddress().getCity()))
		{
			message = ApplicationProperties.getValue("site.city");

			throw this.getBizLogicException(null, "errors.item.required", message);
		}
		if (edu.wustl.catissuecore.util.global.Variables.isStateRequired)
		{
			if (site.getAddress() == null || Validator.isEmpty(site.getAddress().getState()))
			{
				message = ApplicationProperties.getValue("site.state");

				throw this.getBizLogicException(null, "errors.item.required", message);
			}
		}

		if (site.getAddress() == null || Validator.isEmpty(site.getAddress().getCountry()))
		{
			message = ApplicationProperties.getValue("site.country");

			throw this.getBizLogicException(null, "errors.item.required", message);
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

			throw this.getBizLogicException(null, "errors.item.required", message);
		}

		// END

		final List siteList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_SITE_TYPE, null);

		if (!Validator.isEnumeratedValue(siteList, site.getType()))
		{

			throw this.getBizLogicException(null, "type.errMsg", message);
		}
		if ((site.getAddress().getState() != null && site.getAddress().getState() != null)
				&& edu.wustl.catissuecore.util.global.Variables.isStateRequired)
		{
			if (!Validator.isEnumeratedValue(CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_STATE_LIST, null), site.getAddress().getState()))
			{

				throw this.getBizLogicException(null, "state.errMsg", "");
			}
		}

		if (!Validator.isEnumeratedValue(CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_COUNTRY_LIST, null), site.getAddress().getCountry()))
		{

			throw this.getBizLogicException(null, "country.errMsg", "");
		}

		if (operation.equals(Constants.ADD))
		{
			if (!Status.ACTIVITY_STATUS_ACTIVE.toString().equals(site.getActivityStatus()))
			{

				throw this.getBizLogicException(null, "activityStatus.active.errMsg", "");
			}
		}
		else
		{
			if (!Validator.isEnumeratedValue(Constants.SITE_ACTIVITY_STATUS_VALUES, site
					.getActivityStatus()))
			{

				throw this.getBizLogicException(null, "activityStatus.errMsg", "");
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
	@Override
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
	@Override
	protected String getPrivilegeName(Object domainObject)
	{
		final String privilegeName = Variables.privilegeDetailsMap.get(Constants.ADD_EDIT_SITE);
		return privilegeName;
	}

	/**
	 * @param siteId : siteId
	 * @return Collection
	 * @throws BizLogicException : BizLogicException
	 */
	public Collection<CollectionProtocol> getRelatedCPs(Long siteId, DAO dao)
			throws BizLogicException
	{

		Site site = null;
		try
		{

			site = (Site) dao.retrieveById(Site.class.getName(), siteId);
		}
		catch (final DAOException e)
		{
			this.logger.debug(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}

		if (site == null)
		{
			return null;
		}

		return site.getCollectionProtocolCollection();
	}
}