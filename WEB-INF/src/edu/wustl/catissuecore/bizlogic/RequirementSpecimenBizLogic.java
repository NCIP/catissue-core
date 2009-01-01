package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;
import java.util.Iterator;

import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAO;
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
		dao.insert(reqSpecimen, sessionDataBean, false, false);
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
	
	/**
	 * This method will Update the Requirement Specimen
	 * @param dao DAO object
	 * @param sessionDataBean Session data
	 * @param collectionProtocolEvent Transient CPE 
	 * @param oldCollectionProtocolEvent Persistent CPE
	 * @throws DAOException Database related exception
	 * @throws UserNotAuthorizedException User not Authorized Exception
	 */
	public void updateSpecimens(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocolEvent oldCollectionProtocolEvent,
			CollectionProtocolEvent collectionProtocolEvent) throws DAOException,
			UserNotAuthorizedException
	{
		//check for null added for Bug #8533
		//Patch: 8533_1
		if(collectionProtocolEvent.getSpecimenRequirementCollection() != null)
		{	
			Iterator<SpecimenRequirement> srIt = collectionProtocolEvent
					.getSpecimenRequirementCollection().iterator();
			Collection<SpecimenRequirement> oldReqspecimenCollection = null;
			if (oldCollectionProtocolEvent != null)
			{
				oldReqspecimenCollection = oldCollectionProtocolEvent
						.getSpecimenRequirementCollection();
			}
			while (srIt.hasNext())
			{
				SpecimenRequirement specimenRequirement = srIt.next();
				if (specimenRequirement.getCollectionProtocolEvent().getId()==null) 
				{
					specimenRequirement.setCollectionProtocolEvent(collectionProtocolEvent);
				}
				if (specimenRequirement.getId() == null || specimenRequirement.getId() <= 0)
				{
					specimenRequirement.setCollectionProtocolEvent(collectionProtocolEvent);
					insert(specimenRequirement, dao, sessionDataBean);
				}
				else
				{
					if(specimenRequirement.getSpecimenCharacteristics().getId() ==null ||
							specimenRequirement.getSpecimenCharacteristics().getId() <= 0)
					{
						dao.insert(specimenRequirement.getSpecimenCharacteristics(), sessionDataBean, false, false);
					}
					dao.update(specimenRequirement, sessionDataBean, true, false, false);
					if(oldReqspecimenCollection!=null)
					{
						SpecimenRequirement oldRequirementSpecimen = (SpecimenRequirement) getCorrespondingOldObject(
								oldReqspecimenCollection, specimenRequirement.getId());
						dao.audit(specimenRequirement, oldRequirementSpecimen, sessionDataBean, true);
					}
				}
			}
		}
		if(oldCollectionProtocolEvent!=null)
		{
			// Specimen delete code
			Collection<SpecimenRequirement> oldReqSpecimenCollection = oldCollectionProtocolEvent.getSpecimenRequirementCollection();
			Collection<SpecimenRequirement> newReqSpecimenCollection = collectionProtocolEvent.getSpecimenRequirementCollection();
			checkSpecimenDelete(dao, oldReqSpecimenCollection,newReqSpecimenCollection);
		}
	}
	/**
	 * 
	 * This method will check for delete specimen logic
	 * @param dao DAO object
	 * @param oldReqSpecimenCollection Persistent Requirement Specimen Collection
	 * @param newReqSpecimenCollection New Requirement Specimen Collection
	 * @throws DAOException Database related exception
	 */
	private void checkSpecimenDelete(DAO dao,Collection oldReqSpecimenCollection, 
			Collection newReqSpecimenCollection) throws DAOException
	{
		SpecimenRequirement oldSpReq = null;
		SpecimenRequirement newSpReq = null;
		Iterator<SpecimenRequirement> iterator = oldReqSpecimenCollection.iterator();
		while(iterator.hasNext())
		{
			oldSpReq = (SpecimenRequirement)iterator.next();
			if("New".equals(oldSpReq.getLineage()))
			{
				newSpReq  = (SpecimenRequirement)getCorrespondingOldObject(newReqSpecimenCollection, oldSpReq.getId());
				if(newSpReq ==  null)
				{
					deleteRequirementSpecimen(dao,oldSpReq);
				}
				else
				{
					checkChildSpecimenDelete(dao, oldSpReq.getChildSpecimenCollection(),newSpReq.getChildSpecimenCollection());
				}
			}
		}
	}
	/**
	 * This method will check for delete specimen logic 
	 * @param dao DAO Object
	 * @param oldReqSpecimenCollection Old Specimen Requirement Object
	 * @param newReqSpecimenCollection New Specimen Requirement Object
	 * @throws DAOException Databse related exception
	 */
	private void checkChildSpecimenDelete(DAO dao,Collection oldReqSpecimenCollection, 
			Collection newReqSpecimenCollection) throws DAOException
	{
		SpecimenRequirement oldSpReq = null;
		SpecimenRequirement newSpReq = null;
		Iterator<SpecimenRequirement> iterator = oldReqSpecimenCollection.iterator();
		while(iterator.hasNext())
		{
			oldSpReq = (SpecimenRequirement)iterator.next();
			newSpReq  = (SpecimenRequirement)getCorrespondingOldObject(newReqSpecimenCollection, oldSpReq.getId());
			if(newSpReq ==  null)
			{
				deleteRequirementSpecimen(dao,oldSpReq);
			}
			else
			{
				checkChildSpecimenDelete(dao, oldSpReq.getChildSpecimenCollection(),newSpReq.getChildSpecimenCollection());
			}
		}
	}
	
	/**
	 * This method will delete requirement Specimen from database
	 * @param dao DAO Object
	 * @param spReq Specimen Requirement to delete
	 * @throws DAOException Database related exception
	 */
	public void deleteRequirementSpecimen(DAO dao, SpecimenRequirement spReq) throws DAOException
	{
		SpecimenRequirement reqSp = (SpecimenRequirement)dao.retrieve(SpecimenRequirement.class.getName(), spReq.getId());
		if(reqSp.getParentSpecimen()!= null)
		{
			Collection<AbstractSpecimen> childCollection = reqSp.getParentSpecimen().getChildSpecimenCollection();
			childCollection.remove(reqSp);
			reqSp.setSpecimenCharacteristics(null);
			reqSp.setParentSpecimen(null);
		}
		dao.delete(reqSp);
	}
	
}
