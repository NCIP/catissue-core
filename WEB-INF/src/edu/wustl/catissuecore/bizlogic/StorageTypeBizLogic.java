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

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;

/**
 * StorageTypeHDAO is used to add site type information into the database using Hibernate.
 * @author aniruddha_phadnis
 */
public class StorageTypeBizLogic extends DefaultBizLogic
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
		StorageType type = (StorageType) obj;
		dao.insert(type.getCapacity(), sessionDataBean, true, true);
		dao.insert(type, sessionDataBean, true, true);
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
		StorageType type = (StorageType) obj;

		dao.update(type.getCapacity(), sessionDataBean, true, true, false);
		dao.update(type, sessionDataBean, true, true, false);

		//Audit of update.
		StorageType oldStorageType = (StorageType) oldObj;
		dao.audit(type.getCapacity(), oldStorageType.getCapacity(), sessionDataBean, true);
		dao.audit(obj, oldObj, sessionDataBean, true);
	}

	//Added by Ashish
	/*
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	{
		StorageType storageType = (StorageType) obj;
		String message = "";
		if (storageType == null)
			throw new DAOException("domain.object.null.err.msg", new String[]{"Storage Type"});
		Validator validator = new Validator();
		//		if (validator.isEmpty(storageType.get))
		//		{
		//			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
		//					ApplicationProperties.getValue("storageType.type")));
		//		}
		//		else
		//		{
		//			String s = new String("- _");
		//			String delimitedString = validator.delimiterExcludingGiven(s);
		//			if (validator.containsSpecialCharacters(type, delimitedString))
		//			{
		//				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.valid.data",
		//						ApplicationProperties.getValue("storageType.type")));
		//			}
		//
		//		}
		if (validator.isEmpty(String.valueOf(storageType.getCapacity().getOneDimensionCapacity())))
		{
			message = ApplicationProperties.getValue("storageType.oneDimensionCapacity");
			throw new DAOException("errors.item.required",
					new String[]{message});

		}
		else
		{
			if (!validator.isNumeric(String.valueOf(storageType.getCapacity()
					.getOneDimensionCapacity())))
			{
				message = ApplicationProperties.getValue("storageType.oneDimensionCapacity");
				throw new DAOException("errors.item.format",
						new String[]{message});

			}
		}

		if (validator.isEmpty(storageType.getOneDimensionLabel()))
		{
			message = ApplicationProperties.getValue("storageType.oneDimensionLabel");
			throw new DAOException("errors.item.required",
					new String[]{message});

		}

		//--------		checkValidSelectionForAny(holdsStorageTypeIds, "storageType.holdsStorageType", errors);

		if (validator.isEmpty(String.valueOf(storageType.getCapacity().getTwoDimensionCapacity())))
		{
			message = ApplicationProperties.getValue("storageType.twoDimensionCapacity");
			throw new DAOException("errors.item.required",
					new String[]{message});

		}
		else
		{
			if (!validator.isNumeric(String.valueOf(storageType.getCapacity()
					.getTwoDimensionCapacity())))
			{
				message = ApplicationProperties.getValue("storageType.twoDimensionCapacity");
				throw new DAOException("errors.item.format",
						new String[]{message});

			}
		}

		if (validator.isEmpty(storageType.getTwoDimensionLabel())
				&& (storageType.getCapacity().getTwoDimensionCapacity() > 1))
		{
			message = ApplicationProperties.getValue("storageType.twoDimensionLabel");
			throw new DAOException("errors.labelRequired",
					new String[]{message});

		}

		if (!validator.isEmpty(storageType.getDefaultTempratureInCentigrade().toString())
				&& !validator.isDouble(storageType.getDefaultTempratureInCentigrade().toString(),
						false))
		{
			message = ApplicationProperties.getValue("storageType.defaultTemperature");
			throw new DAOException("errors.item.format",
					new String[]{message});

		}
		return true;
	}
	*/
	//END

}