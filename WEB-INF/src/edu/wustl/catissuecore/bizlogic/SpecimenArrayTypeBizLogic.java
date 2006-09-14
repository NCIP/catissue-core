/*
 * <p>Title: SpecimenArrayTypeBizLogic Class </p>
 * <p>Description:This class performs business level logic for Specimen Array Type </p>
 * Copyright: Copyright (c) year 2006
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.1
 * Created on Aug 24,2006
 */
package edu.wustl.catissuecore.bizlogic;

import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;


/**
 * <p>This class initializes the fields of SpecimenArrayTypeBizLogic.java</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public class SpecimenArrayTypeBizLogic extends DefaultBizLogic {
	
	/* (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.AbstractBizLogic#insert(java.lang.Object, edu.wustl.common.dao.DAO, edu.wustl.common.beans.SessionDataBean)
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException {
		SpecimenArrayType arrayType = (SpecimenArrayType) obj;
		dao.insert(arrayType.getCapacity(),sessionDataBean, true, true);
		dao.insert(arrayType,sessionDataBean, true, true);
	}
	
	/**
     * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
     */
	protected void update(Object obj,Object oldObj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException 
    {
		SpecimenArrayType arrayType = (SpecimenArrayType) obj;
		dao.update(arrayType.getCapacity(),sessionDataBean, true, true,false);
		dao.update(arrayType,sessionDataBean, true, true,false);
		
	    //Audit of update.
		SpecimenArrayType oldArrayType = (SpecimenArrayType) oldObj;
	    dao.audit(arrayType.getCapacity(), oldArrayType.getCapacity(), sessionDataBean, true);
	    dao.audit(obj, oldObj, sessionDataBean, true);
    }
	
}
