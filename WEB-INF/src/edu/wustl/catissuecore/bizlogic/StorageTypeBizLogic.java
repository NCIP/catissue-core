
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
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;

/**
 * StorageTypeHDAO is used to add site type information into the database using Hibernate.
 * @author vaishali_khandelwal
 */
public class StorageTypeBizLogic extends DefaultBizLogic
{

	/**
	 * Saves the storageType object in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws BizLogicException 
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			StorageType type = (StorageType) obj;

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

			dao.insert(type.getCapacity(), true);
			dao.insert(type, true);
		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "bizlogic.error", "");
		}
	}

	/**
	 * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws BizLogicException 
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			StorageType type = (StorageType) obj;

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
			StorageType oldStorageType = (StorageType) oldObj;
			((HibernateDAO)dao).audit(type.getCapacity(), oldStorageType.getCapacity());
			((HibernateDAO)dao).audit(obj, oldObj);
		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "bizlogic.error", "");
		}
	}

	//Added by Ashish

	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		StorageType storageType = (StorageType) obj;
		String message = "";
		if (storageType == null)
			throw getBizLogicException(null, "domain.object.null.err.msg", "");
			
		//throw new DAOException("domain.object.null.err.msg", new String[]{"Storage Type"});
		Validator validator = new Validator();
		if (validator.isEmpty(storageType.getName()))
		{
			
			throw getBizLogicException(null, "errors.item.required",
					ApplicationProperties.getValue("storageType.type"));
		}
		else
		{
			String s = new String("- _");
			String delimitedString = validator.delimiterExcludingGiven(s);
			if (validator.containsSpecialCharacters(storageType.getName(), delimitedString))
			{
				
				throw getBizLogicException(null, "errors.valid.data",
						ApplicationProperties.getValue("storageType.type"));
			}

		}
		if (validator.isEmpty(String.valueOf(storageType.getCapacity().getOneDimensionCapacity())))
		{
			message = ApplicationProperties.getValue("storageType.oneDimensionCapacity");
			throw getBizLogicException(null, "errors.item.required", message);

		}
		else
		{
			if (!validator.isNumeric(String.valueOf(storageType.getCapacity()
					.getOneDimensionCapacity())))
			{
				message = ApplicationProperties.getValue("storageType.oneDimensionCapacity");
				
				throw getBizLogicException(null,"errors.item.format", message);

			}
		}

		if (validator.isEmpty(storageType.getOneDimensionLabel()))
		{
			message = ApplicationProperties.getValue("storageType.oneDimensionLabel");
			throw getBizLogicException(null,"errors.item.required", message);

		}
		if (validator.isEmpty(String.valueOf(storageType.getCapacity().getTwoDimensionCapacity())))
		{
			message = ApplicationProperties.getValue("storageType.twoDimensionCapacity");
			throw getBizLogicException(null,"errors.item.required", message);

		}
		else
		{
			if (!validator.isNumeric(String.valueOf(storageType.getCapacity()
					.getTwoDimensionCapacity())))
			{
				message = ApplicationProperties.getValue("storageType.twoDimensionCapacity");
				
				throw getBizLogicException(null,"errors.item.format", message);

			}
		}

		if (validator.isEmpty(storageType.getTwoDimensionLabel())
				&& (storageType.getCapacity().getTwoDimensionCapacity().intValue() > 1))
		{
			message = ApplicationProperties.getValue("storageType.twoDimensionLabel");
			throw getBizLogicException(null,"errors.labelRequired", message);

		}

		if (storageType.getDefaultTempratureInCentigrade() != null && !validator.isEmpty(storageType.getDefaultTempratureInCentigrade().toString())
				&& !validator.isDouble(storageType.getDefaultTempratureInCentigrade().toString(),
						false))
		{
			message = ApplicationProperties.getValue("storageType.defaultTemperature");
			throw getBizLogicException(null,"errors.item.format", message);

		}
		return true;
	}
	//END
	
	/**
	 * To get the ids of the StorageType that the given StorageType can hold. 
	 * @param type The reference to StorageType object.
	 * @return The array of ids of StorageType that the given StorageType can hold.
	 * @throws BizLogicException
	 */
	public long[] getDefaultHoldStorageTypeList(StorageType type) throws BizLogicException
	{
		Collection spcimenArrayTypeCollection = (Collection) retrieveAttribute(StorageType.class.getName(), type.getId(), "elements(holdsStorageTypeCollection)");
		return Utility.getobjectIds(spcimenArrayTypeCollection);
	}
	/**
	 * To get the Specimen Class types that the given StorageType can hold. 
	 * @param type The reference to StorageType object.
	 * @return The array of String representing Specimen Class types that the given StorageType can hold.
	 * @throws BizLogicException
	 */
	public String[] getDefaultHoldsSpecimenClasstypeList(StorageType type)
	{
		String[] holdsSpecimenClasses = null;
		Collection specimenClassTypeCollection = type.getHoldsSpecimenClassCollection();

		if (specimenClassTypeCollection != null)
		{
			if (specimenClassTypeCollection.size() == Utility.getSpecimenClassTypes().size())
			{
				holdsSpecimenClasses =  new String[] {"-1"};
			}
			else
			{
				holdsSpecimenClasses = new String[specimenClassTypeCollection.size()];
				
				Iterator it = specimenClassTypeCollection.iterator();
				int i = 0;
				while (it.hasNext())
				{
					String specimenClassType = (String) it.next();
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
	 * @throws BizLogicException
	 */
	public long[] getDefaultHoldSpecimenArrayTypeList(StorageType type) throws BizLogicException
	{
		//Collection spcimenArrayTypeCollection = (Collection) retrieveAttribute(StorageType.class.getName(), type.getId(), "elements(holdsSpecimenArrayTypeCollection)");
		return Utility.getobjectIds(type.getHoldsSpecimenArrayTypeCollection());
	}
	
	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.DAO, java.lang.Object)
	 */
	public String getObjectId(DAO dao, Object domainObject) 
	{
		return Constants.ADMIN_PROTECTION_ELEMENT;
	}
	
	/**
	 * To get PrivilegeName for authorization check from 'PermissionMapDetails.xml'
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 */
	protected String getPrivilegeKey(Object domainObject)
    {
    	return Constants.ADD_EDIT_STORAGE_TYPE;
    }
}