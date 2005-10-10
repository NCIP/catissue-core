/**
 * <p>Title: DistributionProtocolBizLogic Class>
 * <p>Description:	DistributionProtocolBizLogic is used to add DistributionProtocol information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on August 9 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * DistributionProtocolBizLogic is used to add DistributionProtocol information into the database using Hibernate.
 * @author Mandar Deshmukh
 */
public class DistributionProtocolBizLogic extends DefaultBizLogic
{
	/**
     * Saves the DistributionProtocol object in the database.
	 * @param obj The DistributionProtocol object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
     */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException 
	{
		DistributionProtocol distributionProtocol = (DistributionProtocol)obj;
		setPrincipalInvestigator(dao,distributionProtocol);
		dao.insert(distributionProtocol,sessionDataBean, true, true);

		Iterator it = distributionProtocol.getSpecimenRequirementCollection().iterator();
		while(it.hasNext())
		{
			SpecimenRequirement specimenRequirement = (SpecimenRequirement)it.next();
			specimenRequirement.getDistributionProtocolCollection().add(distributionProtocol);
			dao.insert(specimenRequirement,sessionDataBean, true, true);
		}
		
//		Inserting authorization data
        Set protectionObjects=new HashSet();
        protectionObjects.add(distributionProtocol);
	    try
        {
            SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null,protectionObjects,null);
        }
        catch (SMException e)
        {
            Logger.out.error("Exception in Authorization: "+e.getMessage(),e);
        }
	}
	
	/**
     * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
     */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
    {
		DistributionProtocol distributionProtocol = (DistributionProtocol)obj;
		setPrincipalInvestigator(dao,distributionProtocol);
		dao.update(distributionProtocol, sessionDataBean, true, true, false);

		Iterator it = distributionProtocol.getSpecimenRequirementCollection().iterator();
		while(it.hasNext())
		{
			SpecimenRequirement specimenRequirement = (SpecimenRequirement)it.next();
			specimenRequirement.getDistributionProtocolCollection().add(distributionProtocol);
			dao.update(specimenRequirement, sessionDataBean, true, true, false);
		}
		
		Logger.out.debug("distributionProtocol.getActivityStatus() "+distributionProtocol.getActivityStatus());
		if(distributionProtocol.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
		{
			Logger.out.debug("distributionProtocol.getActivityStatus() "+distributionProtocol.getActivityStatus());
			Long distributionProtocolIDArr[] = {distributionProtocol.getSystemIdentifier()};
			
			DistributionBizLogic bizLogic = (DistributionBizLogic)BizLogicFactory.getBizLogic(Constants.DISTRIBUTION_FORM_ID);
			bizLogic.disableRelatedObjects(dao, distributionProtocolIDArr);
		}
    }
	
	//This method sets the Principal Investigator
	private void setPrincipalInvestigator(DAO dao,DistributionProtocol distributionProtocol) throws DAOException
	{
		Object userObj = dao.retrieve(User.class.getName() , distributionProtocol.getPrincipalInvestigator().getSystemIdentifier());
		if (userObj != null)
		{
			User pi = (User) userObj;
			distributionProtocol.setPrincipalInvestigator(pi);
		}
	}
}