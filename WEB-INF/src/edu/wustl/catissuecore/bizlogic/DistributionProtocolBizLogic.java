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

import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * DistributionProtocolBizLogic is used to add DistributionProtocol information into the database using Hibernate.
 * @author Mandar Deshmukh
 */
public class DistributionProtocolBizLogic extends DefaultBizLogic
{
	/**
     * Saves the DistributionProtocol object in the database.
     * @param session The session in which the object is saved.
     * @param obj The DistributionProtocol object to be saved.
     * @throws DAOException 
     */
	protected void insert(DAO dao, Object obj) throws DAOException 
	{
		DistributionProtocol distributionProtocol = (DistributionProtocol)obj;
		setPrincipalInvestigator(dao,distributionProtocol);
		dao.insert(distributionProtocol,true);

		Iterator it = distributionProtocol.getSpecimenRequirementCollection().iterator();
		while(it.hasNext())
		{
			SpecimenRequirement specimenRequirement = (SpecimenRequirement)it.next();
			specimenRequirement.getDistributionProtocolCollection().add(distributionProtocol);
			dao.insert(specimenRequirement,true);
		}
	}
	
	/**
     * Updates the persistent object in the database.
     * @param session The session in which the object is saved.
     * @param obj The object to be updated.
     * @throws DAOException 
     */
	protected void update(DAO dao, Object obj) throws DAOException
    {
		DistributionProtocol distributionProtocol = (DistributionProtocol)obj;
		setPrincipalInvestigator(dao,distributionProtocol);
		dao.update(distributionProtocol);

		Iterator it = distributionProtocol.getSpecimenRequirementCollection().iterator();
		while(it.hasNext())
		{
			SpecimenRequirement specimenRequirement = (SpecimenRequirement)it.next();
			specimenRequirement.getDistributionProtocolCollection().add(distributionProtocol);
			dao.update(specimenRequirement);
		}
    }
	
	//This method sets the Principal Investigator
	private void setPrincipalInvestigator(DAO dao,DistributionProtocol distributionProtocol) throws DAOException
	{
		List list = dao.retrieve(User.class.getName(), "systemIdentifier", distributionProtocol.getPrincipalInvestigator().getSystemIdentifier());
		if (list.size() != 0)
		{
			User pi = (User) list.get(0);
			distributionProtocol.setPrincipalInvestigator(pi);
		}
	}
}