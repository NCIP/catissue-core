/*
 * <p>Title: SpecimenArrayTypeBizLogic Class </p> <p>Description:This class
 * performs business level logic for Specimen Array Type </p> Copyright:
 * Copyright (c) year 2006 Company: Washington University, School of Medicine,
 * St. Louis.
 * @version 1.1 Created on Aug 24,2006
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;
import java.util.Iterator;

import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.common.audit.AuditManager;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.AuditException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

/**
 * <p>
 * This class initializes the fields of SpecimenArrayTypeBizLogic.java
 * </p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public class SpecimenArrayTypeBizLogic extends CatissueDefaultBizLogic
{

	private transient final Logger logger = Logger.getCommonLogger(SpecimenArrayTypeBizLogic.class);

	/**
	 * @see edu.wustl.common.bizlogic.AbstractBizLogic#insert(java.lang.Object,
	 * edu.wustl.common.dao.DAO, edu.wustl.common.beans.SessionDataBean)
	 * @param obj : obj
	 * @param dao :dao
	 * @param sessionDataBean : sessionDataBean
	 * @throws BizLogicException : BizLogicException
	 */
	@Override
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final AuditManager auditManager = this.getAuditManager(sessionDataBean);
			final SpecimenArrayType arrayType = (SpecimenArrayType) obj;

			/**
			 * Start: Change for API Search --- Jitendra 06/10/2006 In Case of
			 * Api Search, previoulsy it was failing since there was default
			 * class level initialization on domain object. For example in User
			 * object, it was initialized as protected String lastName=""; So we
			 * removed default class level initialization on domain object and
			 * are initializing in method setAllValues() of domain object. But
			 * in case of Api Search, default values will not get set since
			 * setAllValues() method of domainObject will not get called. To
			 * avoid null pointer exception, we are setting the default values
			 * same as we were setting in setAllValues() method of domainObject.
			 */
			ApiSearchUtil.setContainerTypeDefault(arrayType);
			// End:- Change for API Search

			dao.insert(arrayType.getCapacity());
			auditManager.insertAudit(dao, arrayType.getCapacity());
			dao.insert(arrayType);
			auditManager.insertAudit(dao, arrayType);
		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp, daoExp);
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
			final SpecimenArrayType arrayType = (SpecimenArrayType) obj;
			final AuditManager auditManager = this.getAuditManager(sessionDataBean);
			/**
			 * Start: Change for API Search --- Jitendra 06/10/2006 In Case of
			 * Api Search, previoulsy it was failing since there was default
			 * class level initialization on domain object. For example in User
			 * object, it was initialized as protected String lastName=""; So we
			 * removed default class level initialization on domain object and
			 * are initializing in method setAllValues() of domain object. But
			 * in case of Api Search, default values will not get set since
			 * setAllValues() method of domainObject will not get called. To
			 * avoid null pointer exception, we are setting the default values
			 * same as we were setting in setAllValues() method of domainObject.
			 */
			ApiSearchUtil.setContainerTypeDefault(arrayType);
			// End:- Change for API Search

			dao.update(arrayType.getCapacity());
			dao.update(arrayType);

			// Audit of update.
			final SpecimenArrayType oldArrayType = (SpecimenArrayType) oldObj;
			auditManager.updateAudit(dao, arrayType.getCapacity(), oldArrayType.getCapacity());
			auditManager.updateAudit(dao, obj, oldObj);
		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp, daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final AuditException e)
		{
			this.logger.debug(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}

	}

	// Added by Ashish
	/**
	 * @param obj : obj
	 * @param dao : dao
	 * @param operation : operation
	 * @throws BizLogicException : BizLogicException
	 * @return boolean
	 */
	@Override
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		final SpecimenArrayType specimenArrayType = (SpecimenArrayType) obj;
		String message = "";
		if (specimenArrayType == null)
		{
			throw this.getBizLogicException(null, "domain.object.null.err.msg", "");
			/*
			 * throw new DAOException("domain.object.null.err.msg", new
			 * String[]{"Specimen Array Type"});
			 */
		}
		final Validator validator = new Validator();

		if (specimenArrayType.getActivityStatus() == null)
		{
			specimenArrayType.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
		}
		if (Validator.isEmpty(specimenArrayType.getName()))
		{
			message = ApplicationProperties.getValue("arrayType.name");
			throw this.getBizLogicException(null, "errors.item.required", message);

		}
		// validate specimen class of array type
		if (!validator.isValidOption(specimenArrayType.getSpecimenClass()))
		{
			message = ApplicationProperties.getValue("arrayType.specimenClass");
			throw this.getBizLogicException(null, "errors.item.required", message);

		}

		// validate specimen type in array type
		final Collection specimenTypeCollection = specimenArrayType.getSpecimenTypeCollection();

		if ((specimenTypeCollection != null) && (specimenTypeCollection.size() > 0))
		{
			final Iterator arrayTypeIterator = specimenTypeCollection.iterator();
			while (arrayTypeIterator.hasNext())
			{
				final String temp = (String) arrayTypeIterator.next();
				if (temp != null)
				{
					if (!validator.isValidOption(temp))
					{
						message = ApplicationProperties.getValue("arrayType.specimenType");
						throw this.getBizLogicException(null, "errors.item.selected", message);
					}
				}
			}
		}
		else
		{
			message = ApplicationProperties.getValue("arrayType.specimenType");
			throw this.getBizLogicException(null, "errors.item.required", message);

		}

		if (!validator.isNumeric(String.valueOf(specimenArrayType.getCapacity()
				.getOneDimensionCapacity()), 1)
				|| !validator.isNumeric(String.valueOf(specimenArrayType.getCapacity()
						.getTwoDimensionCapacity()), 1))
		{
			message = ApplicationProperties.getValue("arrayType.capacity");
			throw this.getBizLogicException(null, "errors.item.format", message);

		}
		return true;
	}

	// END

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
		return edu.wustl.catissuecore.util.global.Constants.ADMIN_PROTECTION_ELEMENT;
	}

	/**
	 * To get PrivilegeName for authorization check from
	 * 'PermissionMapDetails.xml' (non-Javadoc)
	 * @param domainObject : domainObject
	 * @return String
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 */
	@Override
	protected String getPrivilegeKey(Object domainObject)
	{
		return edu.wustl.catissuecore.util.global.Constants.ADD_EDIT_SPECIMEN_ARRAY_TYPE;
	}
}
