/*
 * <p>Title: SpecimenArrayTypeBizLogic Class </p>
 * <p>Description:This class performs business level logic for Specimen Array Type </p>
 * Copyright: Copyright (c) year 2006
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.1
 * Created on Aug 24,2006
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
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;

/**
 * <p>This class initializes the fields of SpecimenArrayTypeBizLogic.java</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public class SpecimenArrayTypeBizLogic extends CatissueDefaultBizLogic
{
	private transient Logger logger = Logger.getCommonLogger(SpecimenArrayTypeBizLogic.class);

	/* (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.AbstractBizLogic#insert(java.lang.Object, edu.wustl.common.dao.DAO, edu.wustl.common.beans.SessionDataBean)
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			AuditManager auditManager = getAuditManager(sessionDataBean);
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

			dao.insert(arrayType.getCapacity());
			auditManager.insertAudit(dao,arrayType.getCapacity());
			dao.insert(arrayType);
			auditManager.insertAudit(dao,arrayType);
		}
		catch(DAOException daoExp)
		{
			logger.debug(daoExp, daoExp);
			throw getBizLogicException(daoExp, daoExp.getErrorKeyName(),daoExp.getMsgValues());
		} catch (AuditException e) {
			logger.debug(e.getMessage(), e);
			throw getBizLogicException(e, e.getErrorKeyName(),e.getMsgValues());
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
			SpecimenArrayType arrayType = (SpecimenArrayType) obj;
			AuditManager auditManager = getAuditManager(sessionDataBean);
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

			dao.update(arrayType.getCapacity());
			dao.update(arrayType);

			//Audit of update.
			SpecimenArrayType oldArrayType = (SpecimenArrayType) oldObj;
			auditManager.updateAudit(dao,arrayType.getCapacity(), oldArrayType.getCapacity());
			auditManager.updateAudit(dao,obj, oldObj);
		}
		catch(DAOException daoExp)
		{
			logger.debug(daoExp, daoExp);
			throw getBizLogicException(daoExp, daoExp.getErrorKeyName(),daoExp.getMsgValues());
		} catch (AuditException e)
		{
			logger.debug(e.getMessage(), e);
			throw getBizLogicException(e, e.getErrorKeyName(),e.getMsgValues());
		}

	}	
	
	//Added by Ashish
	
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		SpecimenArrayType specimenArrayType = (SpecimenArrayType) obj;
		String message = "";
		if (specimenArrayType == null)
		{	
			throw getBizLogicException(null, "domain.object.null.err.msg", "");
			/*throw new DAOException("domain.object.null.err.msg",
					new String[]{"Specimen Array Type"});*/
		}
		Validator validator = new Validator();
		
		if(specimenArrayType.getActivityStatus() == null)
		{
			specimenArrayType.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
		}
		if (validator.isEmpty(specimenArrayType.getName()))
		{
			message = ApplicationProperties.getValue("arrayType.name");
			throw getBizLogicException(null, "errors.item.required", message);

		}
		//    	 validate specimen class of array type
		if (!validator.isValidOption(specimenArrayType.getSpecimenClass()))
		{
			message = ApplicationProperties.getValue("arrayType.specimenClass");
			throw getBizLogicException(null,"errors.item.required", message);

		}
		 
		//      validate specimen type in array type
		Collection specimenTypeCollection = specimenArrayType.getSpecimenTypeCollection();
		
		if ((specimenTypeCollection != null)
				&& (specimenTypeCollection.size() > 0))
		{
			Iterator arrayTypeIterator = specimenTypeCollection.iterator();
			while (arrayTypeIterator.hasNext())
			{
				String temp = (String) arrayTypeIterator.next();
				if (temp != null)
				{
					if (!validator.isValidOption(temp))
					{
						message = ApplicationProperties.getValue("arrayType.specimenType");
						throw getBizLogicException(null,"errors.item.selected",
								message);
					}
				}
			}
		}
		else
		{
			message = ApplicationProperties.getValue("arrayType.specimenType");
			throw getBizLogicException(null,"errors.item.required",message);

		}

		if (!validator.isNumeric(String.valueOf(specimenArrayType.getCapacity()
				.getOneDimensionCapacity()), 1)
				|| !validator.isNumeric(String.valueOf(specimenArrayType.getCapacity()
						.getTwoDimensionCapacity()), 1))
		{
			message = ApplicationProperties.getValue("arrayType.capacity");
			throw getBizLogicException(null,"errors.item.format", message);

		}
		return true;
	}
	
	//END
	
	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.DAO, java.lang.Object)
	 */
	public String getObjectId(DAO dao, Object domainObject) 
	{
		return edu.wustl.catissuecore.util.global.Constants.ADMIN_PROTECTION_ELEMENT;
	}
	
	/**
	 * To get PrivilegeName for authorization check from 'PermissionMapDetails.xml'
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 */
	protected String getPrivilegeKey(Object domainObject)
    {
    	return edu.wustl.catissuecore.util.global.Constants.ADD_EDIT_SPECIMEN_ARRAY_TYPE;
    }
}
