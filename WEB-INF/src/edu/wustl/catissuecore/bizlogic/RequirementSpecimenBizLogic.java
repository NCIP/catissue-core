package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;
import java.util.Iterator;

import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;


public class RequirementSpecimenBizLogic extends DefaultBizLogic
{
	/**
	 * Saves the Specimen object in the database.
	 * @param obj The Specimen object to be saved.
	 * @param sessionDataBean The session in which the object is saved.
	 * @param dao DAO object
	 * @throws DAOException 
	 * @throws DAOException Database related Exception
	 * @throws UserNotAuthorizedException 
	 * @throws UserNotAuthorizedException User Not Authorized Exception
	 * @throws  
	 * @throws SMException 
	 */

	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		SpecimenRequirement reqSpecimen  = (SpecimenRequirement) obj;
		if (reqSpecimen.getSpecimenCharacteristics() != null)
		{
			dao.insert(reqSpecimen.getSpecimenCharacteristics(), sessionDataBean, false, false);
		}
		dao.insert(reqSpecimen, sessionDataBean, false, false);
		try
		{
			insertChildSpecimens(reqSpecimen,dao,sessionDataBean);
		}
		catch (SMException e)
		{
			throw handleSMException(e);
		}
	}
	/**
	 * @param reqSpecimen Specimen Object
	 * @param dao DAO object
	 * @param sessionDataBean Session data
	 * @param partOfMulipleSpecimen boolean true or false
	 * @throws DAOException Database related exception
	 * @throws SMException Security related exception
	 */
	private void insertChildSpecimens(SpecimenRequirement reqSpecimen, DAO dao, SessionDataBean sessionDataBean)
			throws SMException, DAOException
	{
		Collection childReqSpecimenCollection = reqSpecimen.getChildSpecimenCollection();
		Iterator<SpecimenRequirement> it = childReqSpecimenCollection.iterator();
		while (it.hasNext())
		{
			SpecimenRequirement childReqSpecimen = it.next();
			insert(childReqSpecimen, dao, sessionDataBean);
		}
	}
	/**
	 * Overriding the parent class's method to validate the enumerated attribute values
	 * @param obj Type of object linkedHashSet or domain object
	 * @param dao DAO object
	 * @param operation Type of Operation
	 * @return result
	 * @throws DAOException 
	 * @throws DAOException Database related exception
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	{
		return true;
	}
	
}
