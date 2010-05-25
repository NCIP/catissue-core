/**
 * <p>
 * Title: DistributionHDAO Class>
 * <p>
 * Description: DistributionHDAO is used to add distribution information into
 * the database using Hibernate.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Aniruddha Phadnis
 * @version 1.00 Created on Aug 23, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.security.locator.CSMGroupLocator;

/**
 * DistributionHDAO is used to add distribution information into the database
 * using Hibernate.
 *
 * @author aniruddha_phadnis
 */
/**
 * @author kalpana_thakur
 */
public class DistributionBizLogic extends CatissueDefaultBizLogic
{

	/**
	 *  Logger object.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(DistributionBizLogic.class);

	/**
	 * Saves the Distribution object in the database.
	 * @param dao - DAO obejct
	 * @param obj
	 *            The storageType object to be saved.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @throws BizLogicException throws BizLogicException
	 */
	@Override
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		final Distribution dist = (Distribution) obj;

		try
		{

			this.validateAndSetAssociateObject(dist, dao);
			final Object siteObj = dao.retrieveById(Site.class.getName(), dist.getToSite().getId());
			if (siteObj != null)
			{
				final Site site = (Site) siteObj;
				if (!site.getActivityStatus().equals(Status.ACTIVITY_STATUS_ACTIVE.toString()))
				{
					throw this.getBizLogicException(null,
							"errors.distribution.closedOrDisableSite", "");
				}
				dist.setToSite(site);
			}

			dao.insert(dist);
		}
		catch (final DAOException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}

	/**
	 * @param specimenArrayId - Long.
	 * @return boolean value based on Specimen array distribution
	 * @throws BizLogicException throws BizLogicException
	 */
	public boolean isSpecimenArrayDistributed(Long specimenArrayId) throws BizLogicException
	{
		boolean distributed = false;
		JDBCDAO dao = null;
		List list = null;
		// String queryStr =
		// "select array.distribution_id from catissue_specimen_array array where array.identifier ="
		// + specimenArrayId;
		final String queryStr = "select item.distribution_id from catissue_distributed_item item where item.specimen_array_id = ?";
		ColumnValueBean columnValueBean= new ColumnValueBean( specimenArrayId);
		List<ColumnValueBean> columnValueBeanList = new ArrayList<ColumnValueBean>();
		columnValueBeanList.add(columnValueBean);
		try
		{
			dao = this.openJDBCSession();
			list = dao.executeQuery(queryStr,null,columnValueBeanList);
			if (list != null && !list.isEmpty())
			{
				distributed = true;
			}

			dao.closeSession();
		}
		catch (final DAOException ex)
		{
			LOGGER.error(ex.getMessage(), ex);
			throw this.getBizLogicException(ex, ex.getErrorKeyName(), ex.getMsgValues());
		}
		return distributed;
	}

	/**
	 * @param obj - AbstractDomainObject.
	 * @return set of Distribution objects
	 */
	private Set getProtectionObjects(AbstractDomainObject obj)
	{
		final Set protectionObjects = new HashSet();

		final Distribution distribution = (Distribution) obj;
		protectionObjects.add(distribution);

		distribution.getDistributedItemCollection().iterator();

		return protectionObjects;
	}

	/**
	 * @param obj - AbstractDomainObject object.
	 * @return dynamic groups array.
	 * @throws BizLogicException throws BizLogicException.
	 */
	private String[] getDynamicGroups(AbstractDomainObject obj) throws BizLogicException
	{
		final String[] dynamicGroups = new String[1];
		try
		{
			final Distribution distribution = (Distribution) obj;
			dynamicGroups[0] = CSMGroupLocator.getInstance().getPGName(
					distribution.getDistributionProtocol().getId(),
					Class.forName("edu.wustl.catissuecore.domain.DistributionProtocol"));

			return dynamicGroups;

		}
		catch (final ApplicationException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, "errors.distribution.closedOrDisableSite", "");
		}
		catch (final ClassNotFoundException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, "errors.distribution.closedOrDisableSite", "");
		}

	}

	/**
	 * Updates the persistent object in the database.
	 * @param dao - DAO object.
	 * @param obj -
	 *            The object to be updated.
	 * @param oldObj - Object.
	 * @param sessionDataBean -
	 *            The session in which the object is saved.
	 * @throws BizLogicException throws BizLogicException
	 */
	@Override
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws BizLogicException
	{

		try
		{
			final Distribution distribution = (Distribution) obj;
			dao.update(obj,oldObj);


			final Distribution oldDistribution = (Distribution) oldObj;
			final Collection oldDistributedItemCollection = oldDistribution
					.getDistributedItemCollection();

			final Collection distributedItemCollection = distribution
					.getDistributedItemCollection();
			final Iterator it = distributedItemCollection.iterator();
			while (it.hasNext())
			{
				final DistributedItem item = (DistributedItem) it.next();

				/**
				 * Start: Change for API Search --- Jitendra 06/10/2006 In Case
				 * of Api Search, previoulsy it was failing since there was
				 * default class level initialization on domain object. For
				 * example in User object, it was initialized as protected
				 * String lastName=""; So we removed default class level
				 * initialization on domain object and are initializing in
				 * method setAllValues() of domain object. But in case of Api
				 * Search, default values will not get set since setAllValues()
				 * method of domainObject will not get called. To avoid null
				 * pointer exception, we are setting the default values same as
				 * we were setting in setAllValues() method of domainObject.
				 */
				ApiSearchUtil.setDistributedItemDefault(item);
				// End:- Change for API Search

				final DistributedItem oldItem = (DistributedItem) this.getCorrespondingOldObject(
						oldDistributedItemCollection, item.getId());
				// update the available quantity
				final Object specimenObj = dao.retrieveById(Specimen.class.getName(), item
						.getSpecimen().getId());
				final Double previousQuantity = item.getPreviousQuantity();
				double quantity = item.getQuantity().doubleValue();
				if (previousQuantity != null)
				{
					quantity = quantity - previousQuantity.doubleValue();
				}
				final boolean availability = this.checkAndSetAvailableQty((Specimen) specimenObj,
						quantity);

				if (!availability)
				{
					throw this.getBizLogicException(null,
							"errors.distribution.quantity.should.equal", "");
				}
				else
				{
					if (((Specimen) specimenObj).getAvailableQuantity().doubleValue() == 0)
					{
						((Specimen) specimenObj).setIsAvailable(new Boolean(false));
					}
					dao.update(specimenObj);
					// Audit of Specimen.
					// If a new specimen is distributed.
					if (oldItem == null)
					{
						final Object specimenObjPrev = dao.retrieveById(Specimen.class.getName(),
								item.getSpecimen().getId());

					}

				}
				item.setDistribution(distribution);

				dao.update(item);

			}
			// Mandar : 04-Apr-06 for updating the removed specimens start
			this.updateRemovedSpecimens(distributedItemCollection, oldDistributedItemCollection,
					dao, sessionDataBean);
			LOGGER.debug("Update Successful ...04-Apr-06");
			// Mandar : 04-Apr-06 end
		}
		catch (final DAOException daoExp)
		{
			LOGGER.error(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	/**
	 * @param specimen - Specimen object.
	 * @param quantity - double
	 * @return boolean value
	 */
	private boolean checkAndSetAvailableQty(Specimen specimen, double quantity)
	{
		/**
		 * Name : Virender Reviewer: Sachin lale Calling Domain object from
		 * Proxy Object
		 */
		// Specimen specimen =
		// (Specimen)HibernateMetaData.getProxyObjectImpl(specimenObj);
		if (specimen instanceof TissueSpecimen)
		{
			final TissueSpecimen tissueSpecimen = (TissueSpecimen) specimen;
			double availabeQty = Double.parseDouble(tissueSpecimen.getAvailableQuantity()
					.toString());// tissueSpecimen.getAvailableQuantityInGram().
			// doubleValue();
			LOGGER.debug("TissueAvailabeQty" + availabeQty);
			if (availabeQty <= quantity)
			{
				tissueSpecimen.setAvailableQuantity(new Double(0.0));
			}
			else
			{
				availabeQty = availabeQty - quantity;
				LOGGER.debug("TissueAvailabeQty after deduction" + availabeQty);
				tissueSpecimen.setAvailableQuantity(new Double(availabeQty));// tissueSpecimen
				// .
				// setAvailableQuantityInGram
				// (
				// new
				// Double
				// (
				// availabeQty
				// )
				// )
				// ;
			}
		}
		else if (specimen instanceof CellSpecimen)
		{
			final CellSpecimen cellSpecimen = (CellSpecimen) specimen;
			int availabeQty = (int) Double.parseDouble(cellSpecimen.getAvailableQuantity()
					.toString());//cellSpecimen.getAvailableQuantityInCellCount(
			// ).intValue();
			if (availabeQty <= quantity)
			{
				cellSpecimen.setAvailableQuantity(new Double(0.0));
			}
			else
			{
				availabeQty = availabeQty - (int) quantity;
				cellSpecimen.setAvailableQuantity(new Double(availabeQty));// cellSpecimen
				// .
				// setAvailableQuantityInCellCount
				// (
				// new
				// Integer
				// (
				// availabeQty
				// )
				// )
				// ;
			}
		}
		else if (specimen instanceof MolecularSpecimen)
		{
			final MolecularSpecimen molecularSpecimen = (MolecularSpecimen) specimen;
			double availabeQty = Double.parseDouble(molecularSpecimen.getAvailableQuantity()
					.toString());// molecularSpecimen.
			// getAvailableQuantityInMicrogram
			// ().doubleValue();
			if (availabeQty <= quantity)
			{
				molecularSpecimen.setAvailableQuantity(new Double(0.0));
			}
			else
			{
				availabeQty = availabeQty - quantity;
				molecularSpecimen.setAvailableQuantity(new Double(availabeQty));// molecularSpecimen
				// .
				// setAvailableQuantityInMicrogram
				// (
				// new
				// Double
				// (
				// availabeQty
				// )
				// )
				// ;
			}
		}
		else if (specimen instanceof FluidSpecimen)
		{
			final FluidSpecimen fluidSpecimen = (FluidSpecimen) specimen;
			double availabeQty = Double
					.parseDouble(fluidSpecimen.getAvailableQuantity().toString());// fluidSpecimen
			// .
			// getAvailableQuantityInMilliliter
			// (
			// )
			// .
			// doubleValue
			// (
			// )
			// ;
			if (availabeQty <= quantity)
			{
				fluidSpecimen.setAvailableQuantity(new Double(0.0));
			}
			else
			{
				availabeQty = availabeQty - quantity;
				fluidSpecimen.setAvailableQuantity(new Double(availabeQty));// fluidSpecimen
				// .
				// setAvailableQuantityInMilliliter
				// (
				// new
				// Double
				// (
				// availabeQty
				// )
				// )
				// ;
			}
		}
		if (specimen.getAvailableQuantity().doubleValue() == 0)
		{
			specimen.setIsAvailable(new Boolean(false));
		}
		return true;
	}

	/**
	 * @param dao - DAO object.
	 * @param distributionProtocolIDArr - array of type Long.
	 * @throws BizLogicException throws BizLogicException.
	 */
	public void disableRelatedObjects(DAO dao, Long distributionProtocolIDArr[])
			throws BizLogicException
	{
	}

	/**
	 * Overriding the parent class's method to validate the enumerated attribute
	 * values
	 */
	@Override
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		try
		{
			final Distribution distribution = (Distribution) obj;

			/**
			 * Start: Change for API Search --- Jitendra 06/10/2006 In Case of
			 * Api Search, previoulsy it was failing since there was default
			 * class level initialization on domain object. For example in User
			 * object, it was initialized as protected String lastName=""; So we
			 * removed default class level initialization on domain object and
			 * are initializing in method setAllValues() of domain object. But
			 * in case of Api Search, default values will not get set since
			 * setAllValues() method of domainObject will not get called. To
			 * avoid null pointer exception, we are setting the default values
			 * same as we were setting in setAllValues() method of domainObject.
			 */
			ApiSearchUtil.setDistributionDefault(distribution);
			// End:- Change for API Search

			// Added By Ashish
			if (distribution == null)
			{
				throw this.getBizLogicException(null, "domain.object.null.err.msg", "Distribution");
			}
			final Validator validator = new Validator();
			String message = "";
			if (distribution.getDistributionProtocol() == null
					|| distribution.getDistributionProtocol().getId() == null)
			{
				message = ApplicationProperties.getValue("distribution.protocol");
				throw this.getBizLogicException(null, "errors.item.required", message);
			}
			else
			{
				final Object distributionProtocolObj = dao.retrieveById(DistributionProtocol.class
						.getName(), distribution.getDistributionProtocol().getId());
				if (!((DistributionProtocol) distributionProtocolObj).getActivityStatus().equals(
						Status.ACTIVITY_STATUS_ACTIVE.toString()))
				{
					throw this.getBizLogicException(null, "errors.distribution.closedOrDisableDP",
							"");
				}
			}

			if (distribution.getDistributedBy() == null
					|| distribution.getDistributedBy().getId() == null)
			{
				message = ApplicationProperties.getValue("distribution.distributedBy");
				throw this.getBizLogicException(null, "errors.item.required", message);
			}

			// date validation
			final String errorKey = validator.validateDate(CommonUtilities.parseDateToString(
					distribution.getTimestamp(), CommonServiceLocator.getInstance()
							.getDatePattern()), true);
			if (errorKey.trim().length() > 0)
			{
				message = ApplicationProperties.getValue("distribution.date");
				throw this.getBizLogicException(null, "distribution.date", message);
			}

			if (distribution.getToSite() == null || distribution.getToSite().getId() == null)
			{
				message = ApplicationProperties.getValue("distribution.toSite");
				throw this.getBizLogicException(null, "errors.item.required", message);
			}

			// Collection specimenArrayCollection =
			// distribution.getSpecimenArrayCollection();
			final Collection distributedItemCollection = distribution
					.getDistributedItemCollection();

			if ((distributedItemCollection == null || distributedItemCollection.isEmpty()))
			{
				message = ApplicationProperties.getValue("distribution.distributedItem");
				throw this.getBizLogicException(null, "errors.one.item.required", message);
			}

			// END

			if (operation.equals(Constants.ADD))
			{
				if (!Status.ACTIVITY_STATUS_ACTIVE.toString().equals(
						distribution.getActivityStatus()))
				{
					throw this.getBizLogicException(null, "activityStatus.active.errMsg", "");
				}
			}
			else
			{
				if (!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES, distribution
						.getActivityStatus()))
				{
					throw this.getBizLogicException(null, "activityStatus.errMsg", "");
				}
			}

		}
		catch (final DAOException daoExp)
		{
			LOGGER.error(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		return true;
	}

	/**
	 * @param dist - Distribution object.
	 * @param dao - DAO object.
	 * @throws BizLogicException throws BizLogicException
	 */
	private void validateAndSetAssociateObject(Distribution dist, DAO dao) throws BizLogicException
	{

		try
		{
			String message = "";
			final Collection distributedItemCollection = dist.getDistributedItemCollection();
			if (distributedItemCollection != null && !distributedItemCollection.isEmpty())
			{
				final Iterator itr = distributedItemCollection.iterator();
				while (itr.hasNext())
				{
					final DistributedItem distributedItem = (DistributedItem) itr.next();
					if (distributedItem.getSpecimenArray() == null)
					{
						final Specimen specimen = distributedItem.getSpecimen();
						final Double quantity = distributedItem.getQuantity();
						if (specimen == null || specimen.getId() == null)
						{
							message = ApplicationProperties
									.getValue("errors.distribution.item.specimen");
							throw this.getBizLogicException(null, "errors.item.required", message);
						}
						if (quantity == null)
						{
							message = ApplicationProperties
									.getValue("errors.distribution.item.quantity");
							throw this.getBizLogicException(null, "errors.item.required", message);
						}
						final Specimen specimenObj = (Specimen) dao.retrieveById(Specimen.class
								.getName(), specimen.getId());

						if (specimenObj == null)
						{
							throw this.getBizLogicException(null,
									"errors.distribution.specimenNotFound", "");
						}
						else if (!(specimenObj).getActivityStatus().equals(
								Status.ACTIVITY_STATUS_ACTIVE.toString()))
						{
							throw this.getBizLogicException(null,
									"errors.distribution.closedOrDisableSpecimen", "");
						}
						if (!this.checkAndSetAvailableQty(specimenObj, quantity))
						{
							throw this.getBizLogicException(null,
									"errors.distribution.quantity.should.equal", "");
						}

						distributedItem.setSpecimen(specimenObj);
					}
					else
					{
						SpecimenArray specimenArrayObj = distributedItem.getSpecimenArray();
						// specimenArrayObj = (SpecimenArray)
						// HibernateMetaData.getProxyObjectImpl
						// (specimenArrayObj);
						// TODO : no need to retrieve the complete specimenArray
						// object ....retrieve only activity status using hql
						specimenArrayObj = (SpecimenArray) dao.retrieveById(SpecimenArray.class
								.getName(), specimenArrayObj.getId());
						if (specimenArrayObj == null)
						{
							throw this.getBizLogicException(null,
									"errors.distribution.specimenArrayNotFound", "");
						}
						else if (!(specimenArrayObj).getActivityStatus().equals(
								Status.ACTIVITY_STATUS_ACTIVE.toString()))
						{
							throw this.getBizLogicException(null,
									"errors.distribution.closedOrDisableSpecimenArray", "");
						}
						/*
						 * else if
						 * (chkForSpecimenArrayDistributed(((SpecimenArray)
						 * object).getId(), dao)) { throw new
						 * DAOException(ApplicationProperties
						 * .getValue("errors.distribution.arrayAlreadyDistributed"
						 * )); }
						 */
						distributedItem.setSpecimenArray(specimenArrayObj);
					}
					/**
					 * Start: Change for API Search --- Jitendra 06/10/2006 In
					 * Case of Api Search, previoulsy it was failing since there
					 * was default class level initialization on domain object.
					 * For example in User object, it was initialized as
					 * protected String lastName=""; So we removed default class
					 * level initialization on domain object and are
					 * initializing in method setAllValues() of domain object.
					 * But in case of Api Search, default values will not get
					 * set since setAllValues() method of domainObject will not
					 * get called. To avoid null pointer exception, we are
					 * setting the default values same as we were setting in
					 * setAllValues() method of domainObject.
					 */
					ApiSearchUtil.setDistributedItemDefault(distributedItem);
					// End:- Change for API Search

					distributedItem.setDistribution(dist);

				}
			}
		}
		catch (final DAOException daoExp)
		{
			LOGGER.error(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	// Mandar : 04-Apr-06 : bug id:1545 : - Check for removed specimens
	/**
	 * @param newDistributedItemCollection - Collection.
	 * @param oldDistributedItemCollection - Collection
	 * @param dao - DAO object
	 * @param sessionDataBean
	 * @throws BizLogicException
	 */
	private void updateRemovedSpecimens(Collection newDistributedItemCollection,
			Collection oldDistributedItemCollection, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			// iterate through the old collection and find the specimens that
			// are removed.
			final Iterator it = oldDistributedItemCollection.iterator();
			while (it.hasNext())
			{
				final DistributedItem item = (DistributedItem) it.next();
				final boolean isPresentInNew = newDistributedItemCollection.contains(item);
				LOGGER.debug("Old Object in New Collection : " + isPresentInNew);
				if (!isPresentInNew)
				{
					Object specimenObj = dao.retrieveById(Specimen.class.getName(), item.getSpecimen()
							.getId());

					final double quantity = item.getQuantity().doubleValue();
					this.updateAvailableQty((Specimen) specimenObj, quantity);
					dao.update(specimenObj);
				}

			}
			LOGGER.debug("Update Successful ...04-Apr-06");
		}
		catch (final DAOException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}

	// this method updates the specimen available qty by adding the previously
	// subtracted(during distribution) qty.
	private void updateAvailableQty(Specimen specimen, double quantity)
	{
		if (specimen instanceof TissueSpecimen)
		{
			final TissueSpecimen tissueSpecimen = (TissueSpecimen) specimen;
			double availabeQty = Double.parseDouble(tissueSpecimen.getAvailableQuantity()
					.toString());// tissueSpecimen.getAvailableQuantityInGram().
			// doubleValue();
			LOGGER.debug("TissueAvailabeQty" + availabeQty);
			availabeQty = availabeQty + quantity;
			LOGGER.debug("TissueAvailabeQty after addition" + availabeQty);
			tissueSpecimen.setAvailableQuantity(new Double(availabeQty));// tissueSpecimen
			// .
			// setAvailableQuantityInGram
			// (
			// new
			// Double
			// (
			// availabeQty
			// )
			// )
			// ;
		}
		else if (specimen instanceof CellSpecimen)
		{
			final CellSpecimen cellSpecimen = (CellSpecimen) specimen;
			int availabeQty = (int) Double.parseDouble(cellSpecimen.getAvailableQuantity()
					.toString());//cellSpecimen.getAvailableQuantityInCellCount(
			// ).intValue();
			availabeQty = availabeQty + (int) quantity;
			cellSpecimen.setAvailableQuantity(new Double(availabeQty));// cellSpecimen
			// .
			// setAvailableQuantityInCellCount
			// (new
			// Integer
			// (
			// availabeQty
			// ));
		}
		else if (specimen instanceof MolecularSpecimen)
		{
			final MolecularSpecimen molecularSpecimen = (MolecularSpecimen) specimen;
			double availabeQty = Double.parseDouble(molecularSpecimen.getAvailableQuantity()
					.toString());// molecularSpecimen.
			// getAvailableQuantityInMicrogram
			// ().doubleValue();
			availabeQty = availabeQty + quantity;
			molecularSpecimen.setAvailableQuantity(new Double(availabeQty));// molecularSpecimen
			// .
			// setAvailableQuantityInMicrogram
			// (
			// new
			// Double
			// (
			// availabeQty
			// )
			// )
			// ;
		}
		else if (specimen instanceof FluidSpecimen)
		{
			final FluidSpecimen fluidSpecimen = (FluidSpecimen) specimen;
			double availabeQty = Double
					.parseDouble(fluidSpecimen.getAvailableQuantity().toString());// fluidSpecimen
			// .
			// getAvailableQuantityInMilliliter
			// (
			// )
			// .
			// doubleValue
			// (
			// )
			// ;
			availabeQty = availabeQty + quantity;
			fluidSpecimen.setAvailableQuantity(new Double(availabeQty));// fluidSpecimen
			// .
			// setAvailableQuantityInMilliliter
			// (new
			// Double
			// (
			// availabeQty
			// ));
		}

	}

	// Mandar : 04-Apr-06 : end

	public Long getSpecimenId(String barcodeLabel, Integer distributionBasedOn)
			throws BizLogicException
	{
		final String className = Specimen.class.getName();
		final String[] selectColumnName = null;
		String[] whereColumnName = {"barcode"};
		final String[] whereColumnCondition = {"="};
		final String[] value = {barcodeLabel};
		if (distributionBasedOn.intValue() == Constants.LABEL_BASED_DISTRIBUTION)
		{
			whereColumnName = new String[]{"label"};
		}

		Specimen specimen = null;
		final List specimenList = this.retrieve(className, selectColumnName, whereColumnName,
				whereColumnCondition, value, null);

		if (specimenList == null || specimenList.isEmpty())
		{
			throw this.getBizLogicException(null, "errors.distribution.specimenNotFound", "");
		}
		specimen = (Specimen) specimenList.get(0);
		if (specimen.getActivityStatus().equals(Constants.ACTIVITY_STATUS_VALUES[2]))
		{
			throw this.getBizLogicException(null, "errors.distribution.closedSpecimen", "");
		}

		return specimen.getId();
	}

	public Long getSpecimenArrayId(String barcodeLabel, Integer distributionBasedOn)
			throws BizLogicException
	{

		final String className = SpecimenArray.class.getName();
		final String[] selectColumnName = null;
		String[] whereColumnName = {Constants.SYSTEM_BARCODE};
		final String[] whereColumnCondition = {"="};
		final String[] value = {barcodeLabel};
		if (distributionBasedOn.intValue() == Constants.LABEL_BASED_DISTRIBUTION)
		{
			whereColumnName = new String[]{Constants.SYSTEM_NAME};
		}

		SpecimenArray specimenArray = null;
		final List specimenList = this.retrieve(className, selectColumnName, whereColumnName,
				whereColumnCondition, value, null);

		if (specimenList == null || specimenList.isEmpty())
		{
			throw this.getBizLogicException(null, "errors.distribution.specimenArrayNotFound", "");
		}
		specimenArray = (SpecimenArray) specimenList.get(0);
		return specimenArray.getId();
	}

	private boolean chkForSpecimenArrayDistributed(Long spArrayId, DAO dao) throws DAOException
	{
		final String[] selectColumnName = {"id"};

		final QueryWhereClause queryWhereClause = new QueryWhereClause(DistributedItem.class
				.getName());
		queryWhereClause.addCondition(new EqualClause("specimenArray.id", "specimenArray.id"));

		final List list = dao.retrieve(DistributedItem.class.getName(), selectColumnName,
				queryWhereClause);
		if (list != null && list.size() > 0)
		{
			return true;

		}

		return false;
	}

	/**
	 * Created the list of Specimen Array details
	 *
	 * @param distribution
	 * @param arrayEntries
	 */
	protected void setSpecimenArrayDetails(Distribution distribution, List arrayEntries)
	{
		distribution.getDistributedItemCollection();
		final Iterator itr = distribution.getDistributedItemCollection().iterator();
		while (itr.hasNext())
		{
			final DistributedItem distributedItem = (DistributedItem) itr.next();
			final SpecimenArray specimenArray = distributedItem.getSpecimenArray();
			if (specimenArray != null)
			{
				final List arrayDetails = new ArrayList();
				arrayDetails.add(specimenArray.getName());
				arrayDetails.add(CommonUtilities.toString(specimenArray.getBarcode()));
				arrayDetails.add(CommonUtilities.toString(specimenArray.getSpecimenArrayType()
						.getName()));
				if (specimenArray != null && specimenArray.getLocatedAtPosition() != null)
				{
					arrayDetails.add(CommonUtilities.toString(specimenArray.getLocatedAtPosition()
							.getPositionDimensionOne()));
					arrayDetails.add(CommonUtilities.toString(specimenArray.getLocatedAtPosition()
							.getPositionDimensionTwo()));
				}
				arrayDetails.add(CommonUtilities.toString(specimenArray.getCapacity()
						.getOneDimensionCapacity()));
				arrayDetails.add(CommonUtilities.toString(specimenArray.getCapacity()
						.getTwoDimensionCapacity()));
				arrayDetails.add(CommonUtilities.toString(specimenArray.getSpecimenArrayType()
						.getSpecimenClass()));
				arrayDetails.add(CommonUtilities.toString(specimenArray.getSpecimenArrayType()
						.getSpecimenTypeCollection()));
				arrayDetails.add(CommonUtilities.toString(specimenArray.getComment()));
				arrayDetails.add(specimenArray.getId());
				arrayEntries.add(arrayDetails);
			}
		}
	}

	/**
	 * Get the data for distribution
	 *
	 * @param dist
	 * @return
	 * @throws Exception
	 */
	public List getListOfArray(Distribution dist) throws Exception
	{
		final List arrayEntries = new ArrayList();
		final long startTime = System.currentTimeMillis();
		final DAO dao = this.openDAOSession(null);

		try
		{
			final Object object = dao.retrieveById(Distribution.class.getName(), dist.getId());
			final Distribution distribution = (Distribution) object;
			this.setSpecimenArrayDetails(distribution, arrayEntries);

			final long endTime = System.currentTimeMillis();
			System.out.println("Execute time of getRequestDetailsList :" + (endTime - startTime));
			return arrayEntries;

		}
		catch (final DAOException e)
		{
			LOGGER.error(e.getMessage(), e);
			return null;
		}
		finally
		{
			this.closeDAOSession(dao);
		}

	}
}