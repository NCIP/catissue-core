/**
 * <p>
 * Title: DistributionProtocolBizLogic Class>
 * <p>
 * Description: DistributionProtocolBizLogic is used to add DistributionProtocol
 * information into the database using Hibernate.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 * 
 * @author Mandar Deshmukh
 * @version 1.00 Created on August 9 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.DistributionSpecimenRequirement;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.Roles;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.audit.AuditManager;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.exception.AuditException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;

/**
 * DistributionProtocolBizLogic is used to add DistributionProtocol information
 * into the database using Hibernate.
 * 
 * @author Mandar Deshmukh
 */
public class DistributionProtocolBizLogic extends SpecimenProtocolBizLogic implements Roles
{

	private transient final Logger logger = Logger
			.getCommonLogger(DistributionProtocolBizLogic.class);

	/**
	 * Saves the DistributionProtocol object in the database.
	 * @param dao : dao
	 * @param obj
	 *            The DistributionProtocol object to be saved.
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
			final AuditManager auditManager = this.getAuditManager(sessionDataBean);
			final DistributionProtocol distributionProtocol = (DistributionProtocol) obj;

			this.checkStatus(dao, distributionProtocol.getPrincipalInvestigator(),
					"Principal Investigator");

			this.setPrincipalInvestigator(dao, distributionProtocol);
			dao.insert(distributionProtocol);
			auditManager.insertAudit(dao, distributionProtocol);
			for (final DistributionSpecimenRequirement distributionSpecimenRequirement : distributionProtocol
					.getDistributionSpecimenRequirementCollection())
			{
				distributionSpecimenRequirement.setDistributionProtocol(distributionProtocol);
				dao.insert(distributionSpecimenRequirement);
				auditManager.insertAudit(dao, distributionSpecimenRequirement);
			}

			// Inserting authorization data
			final Set protectionObjects = new HashSet();
			protectionObjects.add(distributionProtocol);
		}
		catch (final DAOException daoExp)
		{
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final AuditException auditException)
		{
			this.logger.error(auditException.getMessage(), auditException);
			auditException.printStackTrace();
			throw this.getBizLogicException(auditException, auditException.getErrorKeyName(),
					auditException.getMsgValues());
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
			final AuditManager auditManager = this.getAuditManager(sessionDataBean);
			final DistributionProtocol distributionProtocol = (DistributionProtocol) obj;
			final DistributionProtocol distributionProtocolOld = (DistributionProtocol) oldObj;

			if (!distributionProtocol.getPrincipalInvestigator().getId().equals(
					distributionProtocolOld.getPrincipalInvestigator().getId()))
			{
				this.checkStatus(dao, distributionProtocol.getPrincipalInvestigator(),
						"Principal Investigator");
			}

			this.setPrincipalInvestigator(dao, distributionProtocol);

			this.checkForChangedStatus(distributionProtocol, distributionProtocolOld);
			dao.update(distributionProtocol);

			// Audit of Distribution Protocol.
			auditManager.updateAudit(dao, obj, oldObj);

			final Collection<DistributionSpecimenRequirement> oldDistributionSpecimenRequirementCollection = distributionProtocolOld
					.getDistributionSpecimenRequirementCollection();

			for (final DistributionSpecimenRequirement distributionSpecimenRequirement : distributionProtocol
					.getDistributionSpecimenRequirementCollection())
			{
				this.logger.debug("DistributionSpecimenRequirement Id ............... : "
						+ distributionSpecimenRequirement.getId());
				distributionSpecimenRequirement.setDistributionProtocol(distributionProtocol);
				dao.update(distributionSpecimenRequirement);

				final DistributionSpecimenRequirement oldDistributionSpecimenRequirement = (DistributionSpecimenRequirement) this
						.getCorrespondingOldObject(oldDistributionSpecimenRequirementCollection,
								distributionSpecimenRequirement.getId());

				auditManager.updateAudit(dao, distributionSpecimenRequirement,
						oldDistributionSpecimenRequirement);

			}

			this.logger.debug("distributionProtocol.getActivityStatus() "
					+ distributionProtocol.getActivityStatus());
			if (distributionProtocol.getActivityStatus().equals(
					Status.ACTIVITY_STATUS_DISABLED.toString()))
			{
				this.logger.debug("distributionProtocol.getActivityStatus() "
						+ distributionProtocol.getActivityStatus());
				final Long distributionProtocolIDArr[] = {distributionProtocol.getId()};

				final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
				final DistributionBizLogic bizLogic = (DistributionBizLogic) factory
						.getBizLogic(Constants.DISTRIBUTION_FORM_ID);
				bizLogic.disableRelatedObjects(dao, distributionProtocolIDArr);
			}

		}
		catch (final DAOException daoExp)
		{
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final AuditException e)
		{
			this.logger.error(e.getMessage(), e);
			e.printStackTrace()	;
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}

	/**
	 * This method sets the Principal Investigator
	 * @param dao : dao
	 * @param distributionProtocol : distributionProtocol
	 * @throws DAOException : DAOException
	 */
	private void setPrincipalInvestigator(DAO dao, DistributionProtocol distributionProtocol)
			throws DAOException
	{
		final Object userObj = dao.retrieveById(User.class.getName(), distributionProtocol
				.getPrincipalInvestigator().getId());
		if (userObj != null)
		{
			final User pi = (User) userObj;
			distributionProtocol.setPrincipalInvestigator(pi);
		}
	}

	/*
	 * public void setPrivilege(DAO dao, String privilegeName, Class objectType,
	 * Long[] objectIds, Long userId, String roleId, boolean assignToUser,
	 * boolean assignOperation) throws SMException, DAOException {
	 * super.setPrivilege(dao,privilegeName,objectType,objectIds,userId, roleId,
	 * assignToUser, assignOperation); // DistributionBizLogic bizLogic =
	 * (DistributionBizLogic
	 * )BizLogicFactory.getBizLogic(Constants.DISTRIBUTION_FORM_ID); //
	 * bizLogic.
	 * assignPrivilegeToRelatedObjectsForDP(dao,privilegeName,objectIds,userId,
	 * roleId, assignToUser, assignOperation); }
	 */

	/**
	 * This method returns collection of UserGroupRoleProtectionGroup objects
	 * that speciefies the user group protection group linkage through a role.
	 * It also specifies the groups the protection elements returned by this
	 * class should be added to.
	 * 
	 * @return
	 */
	/*
	 * private Vector getAuthorizationData(AbstractDomainObject obj) throws
	 * SMException ,ApplicationException {
	 * Logger.out.debug("--------------- In here ---------------"); Vector
	 * authorizationData = new Vector(); Set group = new HashSet();
	 * DistributionProtocol distributionProtocol = (DistributionProtocol)obj;
	 * String userId =
	 * String.valueOf(distributionProtocol.getPrincipalInvestigator
	 * ().getCsmUserId()); Logger.out.debug(" PI ID: "+userId);
	 * gov.nih.nci.security.authorization.domainobjects.User csmUser =
	 * SecurityManagerFactory.getSecurityManager().getUserById(userId);
	 * Logger.out.debug(" PI: "+csmUser.getLoginName()); group.add(csmUser); //
	 * Protection group of PI String protectionGroupName =
	 * CSMGroupLocator.getInstance
	 * ().getPGName(distributionProtocol.getId(),DistributionProtocol.class);
	 * SecurityDataBean userGroupRoleProtectionGroupBean = new
	 * SecurityDataBean(); userGroupRoleProtectionGroupBean.setUser(userId);
	 * userGroupRoleProtectionGroupBean.setRoleName(PI);
	 * userGroupRoleProtectionGroupBean
	 * .setGroupName(CSMGroupLocator.getInstance(
	 * ).getPIGroupName(distributionProtocol
	 * .getId(),DistributionProtocol.class));
	 * userGroupRoleProtectionGroupBean.setProtGrpName(protectionGroupName);
	 * userGroupRoleProtectionGroupBean.setGroup(group);
	 * authorizationData.add(userGroupRoleProtectionGroupBean);
	 * Logger.out.debug(authorizationData.toString()); return authorizationData;
	 * }
	 */
	// Added by Ashish for validations while passing domain object from API
	/*
	 * Map values = null; int counter = 0; public void setAllValues(Object obj)
	 * { DistributionProtocol dProtocol = (DistributionProtocol)obj; Collection
	 * spcimenProtocolCollection = dProtocol.getSpecimenRequirementCollection();
	 * if(spcimenProtocolCollection != null) { values = new HashMap();
	 * counter=0; int i=1; Iterator it = spcimenProtocolCollection.iterator();
	 * while(it.hasNext()) { SpecimenRequirement specimenRequirement =
	 * (SpecimenRequirement)it.next(); String key[] = { "SpecimenRequirement:" +
	 * i +"_specimenClass", "SpecimenRequirement:" + i +"_unitspan",
	 * "SpecimenRequirement:" + i +"_specimenType", "SpecimenRequirement:" + i
	 * +"_tissueSite", "SpecimenRequirement:" + i +"_pathologyStatus",
	 * "SpecimenRequirement:" + i +"_quantity_value", "SpecimenRequirement:" + i
	 * +"_id" }; this.values = setSpecimenRequirement(key ,
	 * specimenRequirement); i++; counter++; } //At least one row should be
	 * displayed in ADD MORE therefore if(counter == 0) counter = 1; } }
	 */
	// End
	/**
	 * Overriding the parent class's method to validate the enumerated attribute
	 * values
	 * @param dao : dao
	 * @param operation : object
	 * @param obj : obj
	 * @throws BizLogicException : BizLogicException
	 * @return boolean
	 */
	@Override
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		// Added by Ashish
		// setAllValues(obj);
		// END
		final DistributionProtocol protocol = (DistributionProtocol) obj;

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
		ApiSearchUtil.setSpecimenProtocolDefault(protocol);
		// End:- Change for API Search

		// Added by Ashish

		final Validator validator = new Validator();
		String message = "";
		if (protocol == null)
		{
			throw this.getBizLogicException(null, "domain.object.null.err.msg",
					"Distribution Protocol");
		}

		if (protocol.getPrincipalInvestigator() == null)
		{
			throw this.getBizLogicException(null, "errors.item.required", "Principal Investigator");
		}

		if (Validator.isEmpty(protocol.getTitle()))
		{
			message = ApplicationProperties.getValue("distributionprotocol.protocoltitle");
			throw this.getBizLogicException(null, "errors.item.required", message);
		}

		if (Validator.isEmpty(protocol.getShortTitle()))
		{
			message = ApplicationProperties.getValue("distributionprotocol.shorttitle");
			throw this.getBizLogicException(null, "errors.item.required", message);
		}

		// if (validator.isEmpty(protocol.getIrbIdentifier()))
		// {
		// message =
		// ApplicationProperties.getValue("distributionprotocol.irbid");
		// throw new
		// DAOException(ApplicationProperties.getValue("errors.item.required"
		// ,message));
		// }

		if (protocol.getStartDate() != null)
		{
			validator.validateDate(protocol.getStartDate().toString(), false);
		}
		else
		{
			message = ApplicationProperties.getValue("distributionprotocol.startdate");
			throw this.getBizLogicException(null, "errors.item.required", message);
		}

		// END
		final Collection<DistributionSpecimenRequirement> spReqCollection = protocol
				.getDistributionSpecimenRequirementCollection();
		if (spReqCollection != null && spReqCollection.size() != 0)
		{
			final List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_SPECIMEN_CLASS, null);

			final List tissueSiteList = CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_TISSUE_SITE, null);

			final List pathologicalStatusList = CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_PATHOLOGICAL_STATUS, null);

			spReqCollection.iterator();

			for (final DistributionSpecimenRequirement requirement : spReqCollection)
			{
				if (requirement == null)
				{
					throw this.getBizLogicException(null, "protocol.spReqEmpty.errMsg", "");
				}
				else
				{
					/**
					 * Start: Change for API Search --- Jitendra 06/10/2006 In
					 * Case of Api Search, previoulsy it was failing since there
					 * was default class level initialization on domain object.
					 * For example in User object, it was initialized as
					 * protected String lastName=""; So we removed default class
					 * level initialization on domain object and are
					 * initializing in method setAllValues() of domain object.
					 * But in case of Api Search, default values will not get
					 * set since setAllValues() method of domainObject will not
					 * get called. To avoid null pointer exception, we are
					 * setting the default values same as we were setting in
					 * setAllValues() method of domainObject.
					 */
					ApiSearchUtil.setSpecimenRequirementDefault(requirement);
					// End:- Change for API Search

					final String specimenClass = requirement.getSpecimenClass();

					if (!Validator.isEnumeratedValue(specimenClassList, specimenClass))
					{
						throw this.getBizLogicException(null, "protocol.class.errMsg", "");
					}

					if (!Validator.isEnumeratedValue(AppUtility.getSpecimenTypes(specimenClass),
							requirement.getSpecimenType()))
					{
						throw this.getBizLogicException(null, "protocol.type.errMsg", "");
					}

					if (!Validator.isEnumeratedValue(tissueSiteList, requirement.getTissueSite()))
					{
						throw this.getBizLogicException(null, "protocol.tissueSite.errMsg", "");
					}

					if (!Validator.isEnumeratedValue(pathologicalStatusList, requirement
							.getPathologyStatus()))
					{
						throw this
								.getBizLogicException(null, "protocol.pathologyStatus.errMsg", "");
					}
				}
			}
		}

		if (operation.equals(Constants.ADD))
		{
			if (!Status.ACTIVITY_STATUS_ACTIVE.toString().equals(protocol.getActivityStatus()))
			{
				throw this.getBizLogicException(null, "activityStatus.active.errMsg", "");
			}
		}
		else
		{
			if (!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES, protocol
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
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.DAO,
	 *      java.lang.Object)
	 * @return String
	 */
	@Override
	public String getObjectId(DAO dao, Object domainObject)
	{
		return edu.wustl.catissuecore.util.global.Constants.ADMIN_PROTECTION_ELEMENT;
	}

	/**
	 * To get PrivilegeName for authorization check from
	 * 'PermissionMapDetails.xml' (non-Javadoc)
	 * @param domainObject : domainObject
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 * @return String
	 */
	@Override
	protected String getPrivilegeKey(Object domainObject)
	{
		return Constants.ADD_EDIT_DP;
	}

	/**
	 * @param dao : dao
	 * @param domainObject : domainObject
	 * @param sessionDataBean : sessionDataBean
	 * @throws BizLogicException : BizLogicException
	 * @return boolean
	 */
	@Override
	public boolean isAuthorized(DAO dao, Object domainObject, SessionDataBean sessionDataBean)
			throws BizLogicException
	{

		if (sessionDataBean != null && sessionDataBean.isAdmin())
		{
			return true;
		}
		final boolean isAuthorized = false;

		try
		{
			String protectionElementName = null;
			protectionElementName = this.getObjectId(dao, domainObject);
			// Get the required privilege name which we would like to check for
			// the logged in user.
			final String privilegeName = this.getPrivilegeName(domainObject);
			final PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(
					sessionDataBean.getUserName());
			final Set<Long> siteIdSet = new UserBizLogic().getRelatedSiteIds(sessionDataBean
					.getUserId());
			for (final Long id : siteIdSet)
			{
				final String objectId = Site.class.getName() + "_" + id;
				if (privilegeCache.hasPrivilege(objectId, privilegeName))
				{
					return true;
				}
			}
			// control is here, that means, User is not Auth.
			if (!isAuthorized)
			{
				throw AppUtility.getUserNotAuthorizedException(privilegeName,
						protectionElementName, domainObject.getClass().getSimpleName());
			}

		}
		catch (final SMException e)
		{
			this.logger.error(e.getMessage(), e);
			e.printStackTrace() ;
			throw AppUtility.handleSMException(e);
		}
		return isAuthorized;
	}
}