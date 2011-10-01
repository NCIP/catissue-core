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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
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
			dao.insert(type.getCapacity());
			dao.insert(type);
		}
		catch (final DAOException daoExp)
		{
			StorageTypeBizLogic.logger.error(daoExp.getMessage(), daoExp);
			throw this.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
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

			dao.update(type.getCapacity(), ((StorageType) oldObj).getCapacity());
			dao.update(type,oldObj);
		}
		catch (final DAOException daoExp)
		{
			StorageTypeBizLogic.logger.error(daoExp.getMessage(), daoExp);
			throw this.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
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
		if (obj == null)
		{
			throw this.getBizLogicException(null, "domain.object.null.err.msg", "");
		}

		final StorageType storageType = (StorageType) obj;
		String message = "";


		//throw new DAOException("domain.object.null.err.msg", new String[]{"Storage Type"});
		final Validator validator = new Validator();
		if (Validator.isEmpty(storageType.getName()))
		{

			throw this.getBizLogicException(null, "errors.item.required", ApplicationProperties
					.getValue("storageType.type"));
		}
		else
		{
			final String delimiter = new String("- _");
			final String delimitedString = validator.delimiterExcludingGiven(delimiter);
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

		if (storageType.getDefaultTemperatureInCentigrade() != null
				&& !Validator.isEmpty(storageType.getDefaultTemperatureInCentigrade().toString())
				&& !validator.isDouble(storageType.getDefaultTemperatureInCentigrade().toString(),
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
		final Collection specArrTypColl = (Collection) this.retrieveAttribute(
				StorageType.class.getName(), type.getId(), "elements(holdsStorageTypeCollection)");
		return AppUtility.getobjectIds(specArrTypColl);
	}

	/**
	 * To get the Specimen Class types that the given StorageType can hold.
	 * @param type The reference to StorageType object.
	 * @return The array of String representing Specimen Class types that the given StorageType can hold.
	 * @throws BizLogicException throws BizLogicException
	 */
	public String[] getDefaultHoldsSpecimenClasstypeList(StorageType type)
	{
		String[] holdsSpecClass = null;
		final Collection<String> specClassTypeColl = type.getHoldsSpecimenClassCollection();

		if (specClassTypeColl != null)
		{
			if (specClassTypeColl.size() == AppUtility.getSpecimenClassTypes().size())
			{
				holdsSpecClass = new String[]{"-1"};
			}
			else
			{
				holdsSpecClass = new String[specClassTypeColl.size()];

				final Iterator<String> iterator = specClassTypeColl.iterator();
				int index = 0;
				while (iterator.hasNext())
				{
					final String specimenClassType = (String) iterator.next();
					holdsSpecClass[index] = specimenClassType;
					index++;
				}
			}
		}
		return holdsSpecClass;
	}
	/**
	 * To get the Specimen Class types that the given StorageType can hold.
	 * @param type The reference to StorageType object.
	 * @return The array of String representing Specimen Class types that the given StorageType can hold.
	 * @throws ApplicationException  ApplicationException
	 * @throws BizLogicException throws BizLogicException
	 */
	public String[] getDefaultHoldsSpecimenTypeList(StorageType type) throws ApplicationException
	{
		String[] holdsSpType = null;
		final Collection<String> spTypeColl = type.getHoldsSpecimenTypeCollection();
		final Collection<String> holdSpTypeColl = new HashSet<String>();
		holdSpTypeColl.addAll(AppUtility.getAllSpType());
		if (spTypeColl != null)
		{
			if (spTypeColl.size() == holdSpTypeColl.size())
			{
				holdsSpType = new String[]{"-1"};
			}
			else
			{
				holdsSpType = new String[spTypeColl.size()];

				final Iterator<String> iterator = spTypeColl.iterator();
				int index = 0;
				while (iterator.hasNext())
				{
					final String spType = (String) iterator.next();
					holdsSpType[index] = spType;
					index++;
				}
			}
		}
		return holdsSpType;
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
	/**
	 * @param dao - DAO object.
	 * @param container - StorageContainer object
	 * @throws BizLogicException throws BizLogicException
	 */
	protected void loadStorageType(DAO dao, StorageContainer container) throws BizLogicException
	{
		try
		{
			final StorageType storageType = container.getStorageType();
			if (storageType != null)
			{
				final String[] selectColumnName ={};
				final String sourceObjectName = StorageType.class.getName();
				final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
				String errMsg = Constants.DOUBLE_QUOTES;
				if(container.getStorageType().getName() != null && container.getStorageType().getName() != "")
				{
					errMsg = "Storage Type Name";
					queryWhereClause.addCondition(new EqualClause("name", storageType.getName()));
				}
				else
				{
					errMsg = "Storage Type Identifier";
					queryWhereClause.addCondition(new EqualClause("id", storageType.getId()));
				}
				final List list = dao.retrieve(sourceObjectName, selectColumnName,	queryWhereClause);

				if (list.isEmpty())
				{
					this.logger.debug("Storage Type :"+storageType.getId()+ " or Storage Type name : "
							  +storageType.getName()+" is invalid");
					throw this.getBizLogicException(null,"errors.item.format",errMsg);
				}
				else
				{
					populateContainerAttributes(container, list);
				}
			}
		}
		catch (final DAOException daoExp)
		{
			this.logger.error(daoExp.getMessage(), daoExp);
			throw this.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	/**
	 * Populates the attributes of container, that depend on its storage type.
	 * @param container container
	 * @param list list
	 */
	private void populateContainerAttributes(StorageContainer container,
			final List list)
	{
		final StorageType type = (StorageType) list.get(0);

		if((container.getHoldsSpecimenArrayTypeCollection() == null || container.getHoldsSpecimenArrayTypeCollection().isEmpty()) && type.getHoldsSpecimenArrayTypeCollection() != null)
		{
				container.setHoldsSpecimenArrayTypeCollection(new HashSet<SpecimenArrayType>(type.getHoldsSpecimenArrayTypeCollection()));
		}
		if((container.getHoldsSpecimenClassCollection() == null || container.getHoldsSpecimenClassCollection().isEmpty()) && type.getHoldsSpecimenClassCollection() != null)
		{
				container.setHoldsSpecimenClassCollection(new HashSet<String>(type.getHoldsSpecimenClassCollection()));
		}
		if((container.getHoldsSpecimenTypeCollection() == null || container.getHoldsSpecimenTypeCollection().isEmpty()) && type.getHoldsSpecimenTypeCollection() != null)
		{
				container.setHoldsSpecimenTypeCollection(new HashSet<String>(type.getHoldsSpecimenTypeCollection()));
		}
		if((container.getHoldsStorageTypeCollection() == null || container.getHoldsStorageTypeCollection().isEmpty()) && type.getHoldsStorageTypeCollection() != null)
		{
				container.setHoldsStorageTypeCollection(new HashSet<StorageType>(type.getHoldsStorageTypeCollection()));
		}
		container.setStorageType(type);
	}
	/**
	 * To check weather the container to display can holds the given type of
	 * container.
	 * @param typeId
	 *            ContinerType id of container
	 * @param storageContainer
	 *            The StorageContainer reference to be displayed on the page.
	 * @return true if the given container can hold the type.
	 * @throws BizLogicException throws BizLogicException
	 */
	public boolean canHoldContainerType(int typeId, StorageContainer storageContainer)
			throws BizLogicException
	{
		if (!this.isValidContaierType(typeId))
		{
			return false;
		}

		final boolean canHold = false;
		final Collection containerTypeCollection = (Collection) this.retrieveAttribute(
				StorageContainer.class.getName(), storageContainer.getId(),
				"elements(holdsStorageTypeCollection)");
		if (!containerTypeCollection.isEmpty())
		{
			final Iterator itr = containerTypeCollection.iterator();
			while (itr.hasNext())
			{
				final StorageType type = (StorageType) itr.next();
				final long storagetypeId = type.getId().longValue();
				if (storagetypeId == Constants.ALL_STORAGE_TYPE_ID || storagetypeId == typeId)
				{
					return true;
				}
			}
		}
		return canHold;
	}
	/**
	 * Patch ID: 4598_2 Is container type one from the container types present
	 * in the system.
	 *
	 * @param typeID
	 *            Container type ID
	 * @return true/ false
	 * @throws BizLogicException throws BizLogicException
	 */
	private boolean isValidContaierType(int typeID) throws BizLogicException
	{
		final Long longId = (Long) this.retrieveAttribute(StorageType.class.getName(), new Long(
				typeID), "id");
		return !(longId == null);
	}
}