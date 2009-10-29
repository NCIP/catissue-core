
package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;
import java.util.Iterator;

import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.common.audit.AuditManager;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.AuditException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.UserNotAuthorizedException;

/**
 * @author
 *
 */
public class RequirementSpecimenBizLogic extends CatissueDefaultBizLogic
{

	private transient final Logger logger = Logger
			.getCommonLogger(RequirementSpecimenBizLogic.class);

	/**
	 * Saves the Specimen object in the database.
	 * @param obj
	 *            The Specimen object to be saved.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @param dao
	 *            DAO object
	 * @throws BizLogicException
	 *             Database related Exception
	 */

	@Override
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final SpecimenRequirement reqSpecimen = (SpecimenRequirement) obj;
			dao.insert(reqSpecimen);
		}
		catch (final DAOException daoExp)
		{
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	/**
	 * Overriding the parent class's method to validate the enumerated attribute
	 * values
	 * @param obj
	 *            Type of object linkedHashSet or domain object
	 * @param dao
	 *            DAO object
	 * @param operation
	 *            Type of Operation
	 * @return result
	 * @throws BizLogicException
	 * @throws BizLogicException
	 *             Database related exception
	 */
	@Override
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		return true;
	}

	/**
	 * This method will Update the Requirement Specimen
	 * @param dao
	 *            DAO object
	 * @param sessionDataBean
	 *            Session data
	 * @param collectionProtocolEvent
	 *            Transient CPE
	 * @param oldCollectionProtocolEvent
	 *            Persistent CPE
	 * @throws BizLogicException
	 *             Database related exception
	 * @throws UserNotAuthorizedException
	 *             User not Authorized Exception
	 */
	public void updateSpecimens(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocolEvent oldCollectionProtocolEvent,
			CollectionProtocolEvent collectionProtocolEvent) throws BizLogicException

	{
		try
		{
			// check for null added for Bug #8533
			// Patch: 8533_1
			if (collectionProtocolEvent.getSpecimenRequirementCollection() != null)
			{
				final Iterator<SpecimenRequirement> srIt = collectionProtocolEvent
						.getSpecimenRequirementCollection().iterator();
				Collection<SpecimenRequirement> oldReqspecimenCollection = null;
				if (oldCollectionProtocolEvent != null)
				{
					oldReqspecimenCollection = oldCollectionProtocolEvent
							.getSpecimenRequirementCollection();
				}
				while (srIt.hasNext())
				{
					final SpecimenRequirement specimenRequirement = srIt.next();
					if (specimenRequirement.getCollectionProtocolEvent().getId() == null)
					{
						specimenRequirement.setCollectionProtocolEvent(collectionProtocolEvent);
					}
					if (specimenRequirement.getId() == null || specimenRequirement.getId() <= 0)
					{
						specimenRequirement.setCollectionProtocolEvent(collectionProtocolEvent);
						this.insert(specimenRequirement, dao, sessionDataBean);
					}
					else
					{
						dao.update(specimenRequirement);
						if (oldReqspecimenCollection != null)
						{
							final SpecimenRequirement oldRequirementSpecimen = (SpecimenRequirement) this
									.getCorrespondingOldObject(oldReqspecimenCollection,
											specimenRequirement.getId());

							final AuditManager auditManager = this.getAuditManager(sessionDataBean);
							auditManager.updateAudit(dao, specimenRequirement,
									oldRequirementSpecimen);

						}
					}
				}
			}
			if (oldCollectionProtocolEvent != null)
			{
				// Specimen delete code
				final Collection<SpecimenRequirement> oldReqSpecimenCollection = oldCollectionProtocolEvent
						.getSpecimenRequirementCollection();
				final Collection<SpecimenRequirement> newReqSpecimenCollection = collectionProtocolEvent
						.getSpecimenRequirementCollection();
				this.checkSpecimenDelete(dao, oldReqSpecimenCollection, newReqSpecimenCollection);
			}
		}
		catch (final DAOException daoExp)
		{
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final AuditException e)
		{
			this.logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}

	}

	/**
	 * This method will check for delete specimen logic
	 * @param dao
	 *            DAO object
	 * @param oldReqSpecimenCollection
	 *            Persistent Requirement Specimen Collection
	 * @param newReqSpecimenCollection
	 *            New Requirement Specimen Collection
	 * @throws BizLogicException
	 *             Database related exception
	 */
	private void checkSpecimenDelete(DAO dao, Collection oldReqSpecimenCollection,
			Collection newReqSpecimenCollection) throws BizLogicException
	{
		SpecimenRequirement oldSpReq = null;
		SpecimenRequirement newSpReq = null;
		final Iterator<SpecimenRequirement> iterator = oldReqSpecimenCollection.iterator();
		while (iterator.hasNext())
		{
			oldSpReq = iterator.next();
			if ("New".equals(oldSpReq.getLineage()))
			{
				newSpReq = (SpecimenRequirement) this.getCorrespondingOldObject(
						newReqSpecimenCollection, oldSpReq.getId());
				if (newSpReq == null)
				{
					this.deleteRequirementSpecimen(dao, oldSpReq);
				}
				else
				{
					this.checkChildSpecimenDelete(dao, oldSpReq.getChildSpecimenCollection(),
							newSpReq.getChildSpecimenCollection());
				}
			}
		}
	}

	/**
	 * This method will check for delete specimen logic
	 * @param dao
	 *            DAO Object
	 * @param oldReqSpecimenCollection
	 *            Old Specimen Requirement Object
	 * @param newReqSpecimenCollection
	 *            New Specimen Requirement Object
	 * @throws BizLogicException
	 *             Databse related exception
	 */
	private void checkChildSpecimenDelete(DAO dao, Collection oldReqSpecimenCollection,
			Collection newReqSpecimenCollection) throws BizLogicException
	{
		SpecimenRequirement oldSpReq = null;
		SpecimenRequirement newSpReq = null;
		final Iterator<SpecimenRequirement> iterator = oldReqSpecimenCollection.iterator();
		while (iterator.hasNext())
		{
			oldSpReq = iterator.next();
			newSpReq = (SpecimenRequirement) this.getCorrespondingOldObject(
					newReqSpecimenCollection, oldSpReq.getId());
			if (newSpReq == null)
			{
				this.deleteRequirementSpecimen(dao, oldSpReq);
			}
			else
			{
				this.checkChildSpecimenDelete(dao, oldSpReq.getChildSpecimenCollection(), newSpReq
						.getChildSpecimenCollection());
			}
		}
	}

	/**
	 * This method will delete requirement Specimen from database
	 * @param dao
	 *            DAO Object
	 * @param spReq
	 *            Specimen Requirement to delete
	 * @throws BizLogicException
	 *             Database related exception
	 */
	public void deleteRequirementSpecimen(DAO dao, SpecimenRequirement spReq)
			throws BizLogicException
	{
		try
		{
			final SpecimenRequirement reqSp = (SpecimenRequirement) dao.retrieveById(
					SpecimenRequirement.class.getName(), spReq.getId());
			if (reqSp.getParentSpecimen() != null)
			{
				final Collection<AbstractSpecimen> childCollection = reqSp.getParentSpecimen()
						.getChildSpecimenCollection();
				childCollection.remove(reqSp);
				reqSp.setSpecimenCharacteristics(null);
				reqSp.setParentSpecimen(null);
			}
			dao.delete(reqSp);
		}
		catch (final DAOException daoExp)
		{
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}

	}

}
