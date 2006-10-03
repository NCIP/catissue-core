/*
 * <p>Title: SpecimenArrayTypeBizLogic Class </p>
 * <p>Description:This class performs business level logic for Specimen Array Type </p>
 * Copyright: Copyright (c) year 2006
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.1
 * Created on Aug 24,2006
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.Iterator;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;

/**
 * <p>This class initializes the fields of SpecimenArrayTypeBizLogic.java</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public class SpecimenArrayTypeBizLogic extends DefaultBizLogic
{

	/* (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.AbstractBizLogic#insert(java.lang.Object, edu.wustl.common.dao.DAO, edu.wustl.common.beans.SessionDataBean)
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws DAOException, UserNotAuthorizedException
	{
		SpecimenArrayType arrayType = (SpecimenArrayType) obj;
		dao.insert(arrayType.getCapacity(), sessionDataBean, true, true);
		dao.insert(arrayType, sessionDataBean, true, true);
	}

	/**
	 * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void update(Object obj, Object oldObj, DAO dao, SessionDataBean sessionDataBean)
			throws DAOException, UserNotAuthorizedException
	{
		SpecimenArrayType arrayType = (SpecimenArrayType) obj;
		dao.update(arrayType.getCapacity(), sessionDataBean, true, true, false);
		dao.update(arrayType, sessionDataBean, true, true, false);

		//Audit of update.
		SpecimenArrayType oldArrayType = (SpecimenArrayType) oldObj;
		dao.audit(arrayType.getCapacity(), oldArrayType.getCapacity(), sessionDataBean, true);
		dao.audit(obj, oldObj, sessionDataBean, true);
	}

	//Added by Ashish
	/*
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	{
		SpecimenArrayType specimenArrayType = (SpecimenArrayType) obj;
		String message = "";
		if (specimenArrayType == null)
		{			
			throw new DAOException("domain.object.null.err.msg",
					new String[]{"Specimen Array Type"});
		}
		Validator validator = new Validator();
		if (validator.isEmpty(specimenArrayType.getName()))
		{
			message = ApplicationProperties.getValue("arrayType.name");
			throw new DAOException("errors.item.required", new String[]{message});

		}
		//    	 validate specimen class of array type
		if (!validator.isValidOption(specimenArrayType.getSpecimenClass()))
		{
			message = ApplicationProperties.getValue("arrayType.specimenClass");
			throw new DAOException("errors.item.required", new String[]{message});

		}
		//      validate specimen type in array type
		if ((specimenArrayType.getSpecimenTypeCollection() != null)
				&& (specimenArrayType.getSpecimenTypeCollection().size() > 0))
		{
			Iterator arrayTypeIterator = specimenArrayType.getSpecimenTypeCollection().iterator();
			while (arrayTypeIterator.hasNext())
			{
				String temp = (String) arrayTypeIterator.next();
				if (temp != null)
				{
					if (!validator.isValidOption(temp))
					{
						message = ApplicationProperties.getValue("arrayType.specimenType");
						throw new DAOException("errors.item.selected",
								new String[]{message});

					}
				}
			}
		}
		else
		{
			message = ApplicationProperties.getValue("arrayType.specimenType");
			throw new DAOException("errors.item.required", new String[]{message});

		}

		if (!validator.isNumeric(String.valueOf(specimenArrayType.getCapacity()
				.getOneDimensionCapacity()), 1)
				|| !validator.isNumeric(String.valueOf(specimenArrayType.getCapacity()
						.getTwoDimensionCapacity()), 1))
		{
			message = ApplicationProperties.getValue("arrayType.capacity");
			throw new DAOException("errors.item.format", new String[]{message});

		}
		return true;
	}
	*/
	//END
}
