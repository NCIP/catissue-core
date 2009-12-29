
package edu.wustl.catissuecore.bizlogic;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import edu.wustl.catissuecore.domain.Aliquot;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.exception.DAOException;

/**
 * This class is added for Bulk Operations.
 * @author sagar_baldwa
 *
 */
public class AliquotBizLogic extends CatissueDefaultBizLogic
{

	/**
	 * logger instance of the class.
	 */
	private transient final Logger logger = Logger.getCommonLogger(AliquotBizLogic.class);
	/**
	 * Insert.
	 * @param obj Object.
	 * @param dao DAO.
	 * @param sessionDataBean SessionDataBean.
	 * @throws BizLogicException BizLogicException.
	 */
	@Override
	protected void insert(final Object obj, final DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final Aliquot aliquot = (Aliquot) obj;
			int aliquotCount = aliquot.getCount();
			NewSpecimenBizLogic newSpecimenBizLogic = new NewSpecimenBizLogic();
			for (int i = 0; i < aliquotCount; i++)
			{
				final String specimenClass = AppUtility.getSpecimenClassName(
						aliquot.getSpecimen());
				Specimen specimen = AppUtility.getSpecimenObjectOnClassName(specimenClass);
				populateAliquotObject(specimen, aliquot);
				boolean flag = newSpecimenBizLogic.validate(specimen, dao, Constants.ADD);
				if (flag)
				{
					newSpecimenBizLogic.insert(specimen, dao, sessionDataBean);
				}
			}
			dao.commit();
		}
		catch (final ApplicationException exp)
		{
			this.logger.error(exp.getMessage(), exp);
			throw this.getBizLogicException(exp, exp.getErrorKeyName(), exp.getMsgValues());
		}
	}

	/**
	 * Populate Aliquot Object.
	 * @param specimen Specimen.
	 * @param aliquot Aliquot.
	 */
	private void populateAliquotObject(Specimen specimen, Aliquot aliquot)
	{
		Specimen parentSpecimen = aliquot.getSpecimen();
		specimen.setLineage(Constants.ALIQUOT);
		specimen.setActivityStatus("Active");
		specimen.setCreatedOn(parentSpecimen.getCreatedOn());
		specimen.setParentSpecimen(parentSpecimen);
		specimen.setSpecimenClass(parentSpecimen.getSpecimenClass());
		specimen.setSpecimenType(parentSpecimen.getSpecimenType());
		specimen.setBiohazardCollection(parentSpecimen.getBiohazardCollection());
		specimen.setInitialQuantity(aliquot.getQuantityPerAliquot());
		specimen.setAvailableQuantity(aliquot.getQuantityPerAliquot());
		specimen.setCollectionStatus(Constants.COLLECTION_STATUS_COLLECTED);
		specimen.setIsAvailable(Boolean.TRUE);
		specimen.setSpecimenCollectionGroup(new SpecimenCollectionGroup());
		specimen.setSpecimenCharacteristics(parentSpecimen.getSpecimenCharacteristics());

		Collection<SpecimenEventParameters> specimenEventParam = parentSpecimen
				.getSpecimenEventCollection();
		Collection<SpecimenEventParameters> newSpecimenEventColl = new HashSet<SpecimenEventParameters>();

		for (SpecimenEventParameters eventParameters : specimenEventParam)
		{
			eventParameters.setSpecimen(specimen);
			newSpecimenEventColl.add(eventParameters);
		}
		specimen.setSpecimenEventCollection(newSpecimenEventColl);
		if (aliquot.getAliquotsInSameContainer()
				&& aliquot.getSpecimen().getSpecimenPosition() != null)
		{
			StorageContainer storageContainer = aliquot.getSpecimen().getSpecimenPosition()
					.getStorageContainer();
			SpecimenPosition specimenPosition = new SpecimenPosition();
			specimenPosition.setStorageContainer(storageContainer);
			specimen.setSpecimenPosition(specimenPosition);
		}
		else
		{
			specimen.setSpecimenPosition(aliquot.getSpecimenPosition());
		}
	}
	/**
	 * Validate.
	 * @param obj Object.
	 * @param dao DAO.
	 * @param operation String.
	 * @throws BizLogicException BizLogicException.
	 * @return boolean boolean.
	 */
	@Override
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		final Aliquot aliquot = (Aliquot) obj;
		if (aliquot == null)
		{
			final String message = ApplicationProperties.getValue("app.aliquot");
			throw this.getBizLogicException(null, "domain.object.null.err.msg", message);
		}
		if (aliquot.getCount() <= 0)
		{
			final String message = ApplicationProperties.getValue("aliquots.noOfAliquots");
			throw this.getBizLogicException(null, "errors.item.format", message);
		}
		if (aliquot.getQuantityPerAliquot() < 0)
		{
			final String message = ApplicationProperties.getValue("aliquots.qtyPerAliquot");
			throw this.getBizLogicException(null, "errors.item.format", message);
		}
		validateSpecimenObject(dao, aliquot);
		validateAliquotSpecimenPosition(aliquot, dao);
		return true;
	}

	/**
	 * Validate Aliquot Specimen Position.
	 * @param aliquot Aliquot.
	 * @param dao DAO.
	 * @throws BizLogicException BizLogicException.
	 */
	private void validateAliquotSpecimenPosition(Aliquot aliquot, DAO dao) throws BizLogicException
	{
		try
		{
			if (!aliquot.getAliquotsInSameContainer())
			{
				SpecimenPosition specimenPosition = aliquot.getSpecimenPosition();
				if (specimenPosition != null)
				{
					validateSpecimenPosition(aliquot, dao, specimenPosition);
				}
				else
				{
					final String message = ApplicationProperties
							.getValue("specimen.storageContainer");
					throw this.getBizLogicException(null, "errors.invalid", message);
				}
			}
		}
		catch (final DAOException daoExp)
		{
			this.logger.error(daoExp.getMessage(), daoExp);
			throw this
				.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	/**
	 * Validate Specimen Position.
	 * @param aliquot Aliquot.
	 * @param dao DAO.
	 * @param specimenPosition SpecimenPosition.
	 * @throws BizLogicException BizLogicException.
	 * @throws DAOException DAOException.
	 */
	private void validateSpecimenPosition(Aliquot aliquot, DAO dao,
			SpecimenPosition specimenPosition) throws BizLogicException, DAOException
	{
		if (specimenPosition != null
				&& specimenPosition.getStorageContainer() != null
				&& (specimenPosition.getStorageContainer().getId() == null && specimenPosition
						.getStorageContainer().getName() == null))
		{
			final String message = ApplicationProperties
					.getValue("specimen.storageContainer");
			throw this.getBizLogicException(null, "errors.invalid", message);
		}
		if (specimenPosition != null && specimenPosition.getStorageContainer() != null
				&& specimenPosition.getStorageContainer().getName() != null)
		{
			final StorageContainer storageContainerObj = specimenPosition
					.getStorageContainer();
			final String sourceObjectName = StorageContainer.class.getName();
			final String[] selectColumnName = {"id"};
			final String storageContainerName = specimenPosition.getStorageContainer()
					.getName();
			final QueryWhereClause queryWhereClause = new QueryWhereClause(
					sourceObjectName);
			queryWhereClause
					.addCondition(new EqualClause("name", storageContainerName));
			final List list = dao.retrieve(sourceObjectName, selectColumnName,
					queryWhereClause);

			if (!list.isEmpty())
			{
				storageContainerObj.setId((Long) list.get(0));
				specimenPosition.setStorageContainer(storageContainerObj);
				aliquot.setSpecimenPosition(specimenPosition);
			}
			else
			{
				final String message = ApplicationProperties
						.getValue("specimen.storageContainer");
				throw this.getBizLogicException(null, "errors.invalid", message);
			}
		}
	}

	/**
	 * Validate Specimen Object.
	 * @param dao DAO.
	 * @param aliquot Aliquot.
	 * @throws BizLogicException BizLogicException.
	 */
	private void validateSpecimenObject(DAO dao, final Aliquot aliquot) throws BizLogicException
	{
		retrieveSpecimenObject(dao, aliquot);
		validateSpecimenActivityAndCollectionStatus(aliquot);
		validateSpecimenQuantity(aliquot);
	}

	/**
	 * Validate Specimen Activity And Collection Status.
	 * @param aliquot Aliquot.
	 * @throws BizLogicException BizLogicException.
	 */
	private void validateSpecimenActivityAndCollectionStatus(Aliquot aliquot)
			throws BizLogicException
	{
		if ((aliquot.getSpecimen().getIsAvailable() == null)
				|| (!aliquot.getSpecimen().getIsAvailable().booleanValue() && !"Collected"
						.equals(aliquot.getSpecimen().getCollectionStatus())))
		{
			throw this.getBizLogicException(null, "specimen.available.errMsg", "");
		}
		if (!Status.ACTIVITY_STATUS_ACTIVE.toString().equals(
				aliquot.getSpecimen().getActivityStatus()))
		{
			throw this.getBizLogicException(null, "activityStatus.active.errMsg", "");
		}
	}

	/**
	 * Validate Specimen Quantity.
	 * @param aliquot Aliquot.
	 * @throws BizLogicException BizLogicException.
	 */
	private void validateSpecimenQuantity(Aliquot aliquot) throws BizLogicException
	{
		double calculatedSpecimenAvailableQty;
		final DecimalFormat dFormat = new DecimalFormat("#.00");

		double specimenAvailableQty = aliquot.getSpecimen().getAvailableQuantity();
		int aliquotCount = aliquot.getCount();
		Double availQty = aliquot.getQuantityPerAliquot();

		if (aliquot.getQuantityPerAliquot() == 0)
		{
			final BigDecimal bgAvailTemp = new BigDecimal(specimenAvailableQty);
			final BigDecimal bgCntTemp = new BigDecimal(aliquotCount);
			final BigDecimal bgAvail = bgAvailTemp.setScale(2, BigDecimal.ROUND_HALF_EVEN);
			final BigDecimal bgCnt = bgCntTemp.setScale(2, BigDecimal.ROUND_HALF_EVEN);
			final BigDecimal bgQuantity = bgAvail.divide(bgCnt, 2, BigDecimal.ROUND_FLOOR);
			calculatedSpecimenAvailableQty = bgQuantity.doubleValue();
			specimenAvailableQty = specimenAvailableQty
					- Double.parseDouble(dFormat
							.format((calculatedSpecimenAvailableQty * aliquotCount)));
			specimenAvailableQty = Double.parseDouble(dFormat.format(specimenAvailableQty));
		}
		else
		{
			calculatedSpecimenAvailableQty = aliquot.getQuantityPerAliquot();
			specimenAvailableQty = specimenAvailableQty
					- Double.parseDouble(dFormat.format((availQty * aliquotCount)));
		}
		if (specimenAvailableQty < 0)
		{
			throw this.getBizLogicException(null, "errors.item.qtyInsufficient", "");
		}
	}

	/**
	 * Retrieve Specimen Object.
	 * @param dao DAO.
	 * @param aliquot Aliquot.
	 * @throws BizLogicException BizLogicException.
	 */
	private void retrieveSpecimenObject(DAO dao, final Aliquot aliquot) throws BizLogicException
	{
		try
		{
			if (aliquot.getSpecimen() == null)
			{
				final String message = ApplicationProperties.getValue("app.newSpecimen");
				throw this.getBizLogicException(null, "domain.object.null.err.msg", message);
			}
			else
			{
				getSpecimen(dao, aliquot);
			}
		}
		catch (DAOException daoExp)
		{
			this.logger.error(daoExp.getMessage(), daoExp);
			final String message = ApplicationProperties.getValue("aliquots.noOfAliquots");
			throw this.getBizLogicException(daoExp, "errors.item.format", message);
		}
	}

	/**
	 * Get Specimen.
	 * @param dao DAO.
	 * @param aliquot Aliquot.
	 * @throws DAOException DAOException.
	 * @throws BizLogicException BizLogicException.
	 */
	private void getSpecimen(DAO dao, final Aliquot aliquot) throws DAOException, BizLogicException
	{
		Specimen specimen = aliquot.getSpecimen();
		if (specimen.getId() != null)
		{
			specimen = (Specimen) dao.retrieveById(Specimen.class.getName(), specimen
					.getId());
			if (specimen == null)
			{
				throw this.getBizLogicException(null, "invalid.specimen." +
						"identifier", specimen.getId().toString());
			}
		}
		else if (Validator.isEmpty(specimen.getLabel()))
		{
			List specimenList = dao.retrieve(Specimen.class.getName(), "label",
					specimen.getLabel());
			if (specimenList == null || specimenList.isEmpty())
			{
				throw this.getBizLogicException(null, "invalid.label.barcode",
					specimen.getLabel());
			}
			specimen = (Specimen) specimenList.get(0);
		}
		else if (Validator.isEmpty(specimen.getBarcode()))
		{
			List specimenList = dao.retrieve(Specimen.class.getName(),
					"barcode", specimen.getBarcode());
			if (specimenList == null || specimenList.isEmpty())
			{
				throw this.getBizLogicException(null, "invalid.label.barcode",
						specimen.getBarcode());
			}
			specimen = (Specimen) specimenList.get(0);
		}
		aliquot.setSpecimen(specimen);
	}
}