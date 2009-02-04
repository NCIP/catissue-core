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

import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Constants;
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
		
		/**
		 * Start: Change for API Search   --- Jitendra 06/10/2006
		 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
		 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
		 * So we removed default class level initialization on domain object and are initializing in method
		 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
		 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
		 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
		 */
        ApiSearchUtil.setContainerTypeDefault(arrayType);
        //End:-  Change for API Search 
        
		dao.insert(arrayType.getCapacity(), sessionDataBean, true, true);
		dao.insert(arrayType, sessionDataBean, true, true);
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
		SpecimenArrayType arrayType = (SpecimenArrayType) obj;
		
		/**
		 * Start: Change for API Search   --- Jitendra 06/10/2006
		 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
		 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
		 * So we removed default class level initialization on domain object and are initializing in method
		 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
		 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
		 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
		 */
        ApiSearchUtil.setContainerTypeDefault(arrayType);
        //End:-  Change for API Search 
        
		dao.update(arrayType.getCapacity(), sessionDataBean, true, true, false);
		dao.update(arrayType, sessionDataBean, true, true, false);

		//Audit of update.
		SpecimenArrayType oldArrayType = (SpecimenArrayType) oldObj;
		dao.audit(arrayType.getCapacity(), oldArrayType.getCapacity(), sessionDataBean, true);
		dao.audit(obj, oldObj, sessionDataBean, true);
	}	
	
	//Added by Ashish
	
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	{
		SpecimenArrayType specimenArrayType = (SpecimenArrayType) obj;
		String message = "";
		if (specimenArrayType == null)
		{	
			throw new DAOException("domain.object.null.err.msg");
			/*throw new DAOException("domain.object.null.err.msg",
					new String[]{"Specimen Array Type"});*/
		}
		Validator validator = new Validator();
		
		if(specimenArrayType.getActivityStatus() == null)
		{
			specimenArrayType.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		}
		if (validator.isEmpty(specimenArrayType.getName()))
		{
			message = ApplicationProperties.getValue("arrayType.name");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));

		}
		//    	 validate specimen class of array type
		if (!validator.isValidOption(specimenArrayType.getSpecimenClass()))
		{
			message = ApplicationProperties.getValue("arrayType.specimenClass");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));

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
						throw new DAOException(ApplicationProperties.getValue("errors.item.selected",
								message));

					}
				}
			}
		}
		else
		{
			message = ApplicationProperties.getValue("arrayType.specimenType");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));

		}

		if (!validator.isNumeric(String.valueOf(specimenArrayType.getCapacity()
				.getOneDimensionCapacity()), 1)
				|| !validator.isNumeric(String.valueOf(specimenArrayType.getCapacity()
						.getTwoDimensionCapacity()), 1))
		{
			message = ApplicationProperties.getValue("arrayType.capacity");
			throw new DAOException(ApplicationProperties.getValue("errors.item.format", message));

		}
		return true;
	}
	
	//END
}
