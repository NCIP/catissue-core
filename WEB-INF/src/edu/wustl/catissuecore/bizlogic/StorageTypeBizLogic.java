/**
 * <p>Title: StorageTypeHDAO Class>
 * <p>Description:	StorageTypeHDAO is used to add site type information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 21, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;
import java.util.Iterator;

import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.audit.AuditManager;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.AuditException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

/**
 * StorageTypeHDAO is used to add site type information into the database using Hibernate.
 * @author vaishali_khandelwal
 */
public class StorageTypeBizLogic extends CatissueDefaultBizLogic
{

	/**
	 *  Logger object.
	 */
	private transient static final Logger logger = Logger
			.getCommonLogger(StorageTypeBizLogic.class);

	/**
	 * Saves the storageType object in the database.
	 * @param obj The storageType object to be saved.
	 * @param dao - DAO object
	 * @param sessionDataBean The session in which the object is saved.
	 * @throws BizLogicException throws BizLogicException
	 */
	@Override
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final StorageType type = (StorageType) obj;

			/**
			 * Start: Change for API Search   --- Jitendra 06/10/2006
			 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
			 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
			 * So we removed default class level initialization on domain object and are initializing in method
			 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
			 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
			 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
			 */
			ApiSearchUtil.setContainerTypeDefault(type);
			//End:-  Change for API Search 
			final AuditManager auditManager = this.getAuditManager(sessionDataBean);
			dao.insert(type.getCapacity());
			auditManager.insertAudit(dao, type.getCapacity());

			dao.insert(type);
			auditManager.insertAudit(dao, type);
		}
		catch (final DAOException daoExp)
		{
			StorageTypeBizLogic.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final AuditException e)
		{
			StorageTypeBizLogic.logger.error(e.getMessage(), e);
			e.printStackTrace() ;
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}

	/**
	 * Updates the persistent object in the database.
	 * @param dao - DAO object
	 * @param obj The object to be updated.
	 * @param sessionDataBean - The session in which the object is saved.
	 * @throws BizLogicException throws  BizLogicException
	 */
	@Override
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final StorageType type = (StorageType) obj;

			/**
			 * Start: Change for API Search   --- Jitendra 06/10/2006
			 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
			 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
			 * So we removed default class level initialization on domain object and are initializing in method
			 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
			 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
			 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
			 */
			ApiSearchUtil.setContainerTypeDefault(type);
			//End:-  Change for API Search 

			dao.update(type.getCapacity());
			dao.update(type);

			//Audit of update.
			final AuditManager auditManager = this.getAuditManager(sessionDataBean);
			final StorageType oldStorageType = (StorageType) oldObj;

			auditManager.updateAudit(dao, type.getCapacity(), oldStorageType.getCapacity());
			auditManager.updateAudit(dao, obj, oldObj);
		}
		catch (final DAOException daoExp)
		{
			StorageTypeBizLogic.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final AuditException e)
		{
			StorageTypeBizLogic.logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}

	//Added by Ashish

	/**
	 * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param dao - DAO object
	 * @param operation - operation
	 * @returns boolean value
	 * @throws BizLogicException throws  BizLogicException
	 */
	@Override
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		final StorageType storageType = (StorageType) obj;
		String message = "";
		if (storageType == null)
		{
			throw this.getBizLogicException(null, "domain.object.null.err.msg", "");
		}

		//throw new DAOException("domain.object.null.err.msg", new String[]{"Storage Type"});
		final Validator validator = new Validator();
		if (Validator.isEmpty(storageType.getName()))
		{

			throw this.getBizLogicException(null, "errors.item.required", ApplicationProperties
					.getValue("storageType.type"));
		}
		else
		{
			final String s = new String("- _");
			final String delimitedString = validator.delimiterExcludingGiven(s);
			if (validator.containsSpecialCharacters(storageType.getName(), delimitedString))
			{

				throw this.getBizLogicException(null, "errors.valid.data", ApplicationProperties
						.getValue("storageType.type"));
			}

		}
		if (Validator.isEmpty(String.valueOf(storageType.getCapacity().getOneDimensionCapacity())))
		{
			message = ApplicationProperties.getValue("storageType.oneDimensionCapacity");
			throw this.getBizLogicException(null, "errors.item.required", message);

		}
		else
		{
			if (!validator.isNumeric(String.valueOf(storageType.getCapacity()
					.getOneDimensionCapacity())))
			{
				message = ApplicationProperties.getValue("storageType.oneDimensionCapacity");

				throw this.getBizLogicException(null, "errors.item.format", message);

			}
		}

		if (Validator.isEmpty(storageType.getOneDimensionLabel()))
		{
			message = ApplicationProperties.getValue("storageType.oneDimensionLabel");
			throw this.getBizLogicException(null, "errors.item.required", message);

		}
		if (Validator.isEmpty(String.valueOf(storageType.getCapacity().getTwoDimensionCapacity())))
		{
			message = ApplicationProperties.getValue("storageType.twoDimensionCapacity");
			throw this.getBizLogicException(null, "errors.item.required", message);

		}
		else
		{
			if (!validator.isNumeric(String.valueOf(storageType.getCapacity()
					.getTwoDimensionCapacity())))
			{
				message = ApplicationProperties.getValue("storageType.twoDimensionCapacity");

				throw this.getBizLogicException(null, "errors.item.format", message);

			}
		}

		if (Validator.isEmpty(storageType.getTwoDimensionLabel())
				&& (storageType.getCapacity().getTwoDimensionCapacity().intValue() > 1))
		{
			message = ApplicationProperties.getValue("storageType.twoDimensionLabel");
			throw this.getBizLogicException(null, "errors.labelRequired", message);

		}

		if (storageType.getDefaultTempratureInCentigrade() != null
				&& !Validator.isEmpty(storageType.getDefaultTempratureInCentigrade().toString())
				&& !validator.isDouble(storageType.getDefaultTempratureInCentigrade().toString(),
						false))
		{
			message = ApplicationProperties.getValue("storageType.defaultTemperature");
			throw this.getBizLogicException(null, "errors.item.format", message);

		}
		return true;
	}

	//END

	/**
	 * To get the ids of the StorageType that the given StorageType can hold.
	 * @param type The reference to StorageType object.
	 * @return The array of ids of StorageType that the given StorageType can hold.
	 * @throws BizLogicException throws BizLogicException
	 */
	public long[] getDefaultHoldStorageTypeList(StorageType type) throws BizLogicException
	{
		final Collection spcimenArrayTypeCollection = (Collection) this.retrieveAttribute(
				StorageType.class.getName(), type.getId(), "elements(holdsStorageTypeCollection)");
		return AppUtility.getobjectIds(spcimenArrayTypeCollection);
	}

	/**
	 * To get the Specimen Class types that the given StorageType can hold.
	 * @param type The reference to StorageType object.
	 * @return The array of String representing Specimen Class types that the given StorageType can hold.
	 * @throws BizLogicException throws BizLogicException
	 */
	public String[] getDefaultHoldsSpecimenClasstypeList(StorageType type)
	{
		String[] holdsSpecimenClasses = null;
		final Collection specimenClassTypeCollection = type.getHoldsSpecimenClassCollection();

		if (specimenClassTypeCollection != null)
		{
			if (specimenClassTypeCollection.size() == AppUtility.getSpecimenClassTypes().size())
			{
				holdsSpecimenClasses = new String[]{"-1"};
			}
			else
			{
				holdsSpecimenClasses = new String[specimenClassTypeCollection.size()];

				final Iterator it = specimenClassTypeCollection.iterator();
				int i = 0;
				while (it.hasNext())
				{
					final String specimenClassType = (String) it.next();
					holdsSpecimenClasses[i] = specimenClassType;
					i++;
				}
			}
		}
		return holdsSpecimenClasses;
	}

	/**
	 * To get the ids of the SpecimenArrayType that the given StorageType can hold.
	 * @param type The reference to StorageType object.
	 * @return The array of ids of SpecimenArrayType that the given StorageType can hold.
	 * @throws BizLogicException throws BizLogicException
	 */
	public long[] getDefaultHoldSpecimenArrayTypeList(StorageType type) throws BizLogicException
	{
		//Collection spcimenArrayTypeCollection = (Collection) retrieveAttribute(StorageType.class.getName(), type.getId(), "elements(holdsSpecimenArrayTypeCollection)");
		return AppUtility.getobjectIds(type.getHoldsSpecimenArrayTypeCollection());
	}

	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.DAO, java.lang.Object)
	 */
	@Override
	public String getObjectId(DAO dao, Object domainObject)
	{
		return Constants.ADMIN_PROTECTION_ELEMENT;
	}

	/**
	 * To get PrivilegeName for authorization check from 'PermissionMapDetails.xml'
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 */
	@Override
	protected String getPrivilegeKey(Object domainObject)
	{
		return Constants.ADD_EDIT_STORAGE_TYPE;
	}
}