
package edu.wustl.catissuecore.bizlogic;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


import krishagni.catissueplus.mobile.dto.AliquotsDetailsDTO;


import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.Aliquot;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.query.util.querysuite.DAOUtil;
import edu.wustl.security.exception.UserNotAuthorizedException;

/**
 * This class is added for Bulk Operations.
 * @author sagar_baldwa
 *
 */
/**
 * @author mosin
 *
 */
/**
 * @author mosin
 *
 */
/**
 * @author mosin
 *
 */
public class AliquotBizLogic extends CatissueDefaultBizLogic
{
	/**
	 * logger instance of the class.
	 */
	private static final Logger logger = Logger.getCommonLogger(AliquotBizLogic.class);

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
			double parentSpecimenAvailQty = aliquot.getSpecimen().getAvailableQuantity().doubleValue();
			Collection<SpecimenPosition> specimenPosColl = aliquot.getSpecimenPositionCollection();
			List<SpecimenPosition> specimenPosList = null;
			if (specimenPosColl != null)
			{
				specimenPosList = new ArrayList<SpecimenPosition>(specimenPosColl);
			}
			NewSpecimenBizLogic newSpecimenBizLogic = new NewSpecimenBizLogic();
			for (int i = 0; i < aliquotCount; i++)
			{
				Specimen specimen = AppUtility.getSpecimen(aliquot.getSpecimen());
				populateAliquotObject(specimen, aliquot, i, specimenPosList, newSpecimenBizLogic, dao);
				boolean flag = newSpecimenBizLogic.validate(specimen, dao, Constants.ADD);
				if (flag)
				{
					newSpecimenBizLogic.insert(specimen, dao, sessionDataBean);
				}
			}
			Double totalAliquotQty = calculateAvailableQuantityForParent(aliquot, parentSpecimenAvailQty);
			updateParentSpecimen(aliquot.getSpecimen(), totalAliquotQty, dao);
		}
		catch (final ApplicationException exp)
		{
			logger.error(exp.getMessage(), exp);
			throw this.getBizLogicException(exp, exp.getErrorKeyName(), exp.getMsgValues());
		}
	}

	/**
	 * This function calculates the available quantity of parent after creating
	 * aliquots.
	 * @param specimenList : specimenList
	 * @param aliquotForm : aliquotForm
	 */
	private Double calculateAvailableQuantityForParent(Aliquot aliquot, Double parentSpecimenAvailQty)
	{
		Double totalAliquotQty = 0.0;
		final DecimalFormat dFormat = new DecimalFormat("#.000");
		totalAliquotQty = parentSpecimenAvailQty - (aliquot.getQuantityPerAliquot() * aliquot.getCount());
		totalAliquotQty = Double.parseDouble(dFormat.format(totalAliquotQty));
		return totalAliquotQty;
	}
	/**
	 * Update Parent Specimen.
	 * @param parentSpecimen Specimen
	 * @param totalAliquotQty Double
	 * @param dao DAO
	 * @throws ApplicationException ApplicationException
	 */
	private void updateParentSpecimen(Specimen parentSpecimen, Double totalAliquotQty,
			DAO dao) throws ApplicationException
	{
		if(totalAliquotQty < 0L)
		{
			final String quantityString = ApplicationProperties
				.getValue("specimen.availableQuantity");
			throw this.getBizLogicException(null, "errors.availablequantity",
					quantityString);
		}
		Object pSpec =  dao.retrieveById(AbstractSpecimen.class.getName(),
				parentSpecimen.getId());
		((Specimen)pSpec).setAvailableQuantity(totalAliquotQty);
		dao.update(pSpec);
		dao.commit();
	}

	/**
	 * Populate Aliquot Object.
	 * @param specimen Specimen.
	 * @param aliquot Aliquot.
	 * @param aliqoutCounter int.
	 */
	private void populateAliquotObject(Specimen specimen, Aliquot aliquot,
			int aliqoutCounter, List<SpecimenPosition> specimenPosList,
			NewSpecimenBizLogic newSpecimenBizLogic, DAO dao) throws BizLogicException
	{
		try
		{
			Specimen parentSpecimen = aliquot.getSpecimen();
			specimen.setParentSpecimen(parentSpecimen);
			specimen.setLineage(Constants.ALIQUOT);
			specimen.setActivityStatus("Active");
			SpecimenCollectionGroup specimenCollGroup = new SpecimenCollectionGroup();
			specimenCollGroup.setId(parentSpecimen.getSpecimenCollectionGroup().getId());
			specimen.setSpecimenCollectionGroup(specimenCollGroup);
			specimen.setSpecimenClass(parentSpecimen.getSpecimenClass());
			specimen.setSpecimenType(parentSpecimen.getSpecimenType());
			specimen.setInitialQuantity(aliquot.getQuantityPerAliquot());
			specimen.setAvailableQuantity(aliquot.getQuantityPerAliquot());
			specimen.setCollectionStatus(Constants.COLLECTION_STATUS_COLLECTED);
			specimen.setIsAvailable(Boolean.TRUE);
//			if (!edu.wustl.catissuecore.util.global.Variables.isSpecimenLabelGeneratorAvl)
//			{
				long totalAliquotCount = newSpecimenBizLogic.getTotalNoOfAliquotSpecimen(
						parentSpecimen.getId(), dao);
				totalAliquotCount = totalAliquotCount + aliqoutCounter + 1;
				specimen.setLabel(parentSpecimen.getLabel() + "_" + totalAliquotCount);
//			}
			processAliquotInSameContainer(specimen, aliquot, aliqoutCounter, specimenPosList);
		}
		catch (BizLogicException bizExp)
		{
			throw new BizLogicException(bizExp.getErrorKey(), bizExp, bizExp.getMsgValues());
		}
	}

	/**
	 * Process Aliquot In Same Container.
	 * @param specimen Specimen.
	 * @param aliquot Aliquot.
	 * @param aliqoutCounter int.
	 */
	private void processAliquotInSameContainer(Specimen specimen, Aliquot aliquot,
			int aliqoutCounter, List<SpecimenPosition> specimenPosList)
	{
		if (specimenPosList != null && specimenPosList.isEmpty())
		{
			specimen.setSpecimenPosition(null);
		}
		else
		{
			if(aliquot.getAliquotsInSameContainer())
			{
				SpecimenPosition newSpecimenPosition = new SpecimenPosition();
				SpecimenPosition specimenPosition = specimenPosList.get(0);

				newSpecimenPosition.setPositionDimensionOne(specimenPosition.getPositionDimensionOne());
				newSpecimenPosition.setPositionDimensionTwo(specimenPosition.getPositionDimensionTwo());

				StorageContainer container = specimenPosition.getStorageContainer();
				newSpecimenPosition.setStorageContainer(container);
				specimen.setSpecimenPosition(newSpecimenPosition);
			}
			else
			{
				if(aliqoutCounter < specimenPosList.size())
				{
					SpecimenPosition specimenPosition = specimenPosList.get(aliqoutCounter);
					specimen.setSpecimenPosition(specimenPosition);
				}
				else
				{
					specimen.setSpecimenPosition(null);
				}
			}
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
			final String message = ApplicationProperties.getValue("app.aliquots");
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
			if (aliquot.getAliquotsInSameContainer())
			{
				if (aliquot.getSpecimenPositionCollection() != null
						&& !aliquot.getSpecimenPositionCollection().isEmpty()
						&& aliquot.getSpecimenPositionCollection().size() > 1)
				{
					throw this.getBizLogicException(null,
							"error.aliquot.multiple.container.names", "");
				}
			}
			else
			{
				Collection<SpecimenPosition> specPositionColl = aliquot
						.getSpecimenPositionCollection();

				if (specPositionColl != null && !specPositionColl.isEmpty())
				{
					for (SpecimenPosition specimenPosition : specPositionColl)
					{
						if(specimenPosition != null)
						{
							validateSpecimenPosition(aliquot, dao, specimenPosition);
						}
					}
				}
			}
		}
		catch (final DAOException daoExp)
		{
			logger.error(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	/**
	 * Validate Specimen Position.
	 * @param aliquot Aliquot.
	 * @param dao DAO.
	 * @param specimenPosition SpecimenPosition.
	 o* @throws BizLogicException BizLogicException.
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
			final String message = ApplicationProperties.getValue("specimen.storageContainer");
			throw this.getBizLogicException(null, "errors.invalid", message);
		}
		if (specimenPosition != null && specimenPosition.getStorageContainer() != null
				&& specimenPosition.getStorageContainer().getName() != null)
		{
			final StorageContainer storageContainerObj = specimenPosition.getStorageContainer();
			final String sourceObjectName = StorageContainer.class.getName();
			final String[] selectColumnName = {"id"};
			final String storageContainerName = specimenPosition.getStorageContainer().getName();
			final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.addCondition(new EqualClause("name", storageContainerName));
			final List list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
			if (list.isEmpty())
			{
				final String message = ApplicationProperties.getValue("specimen.storageContainer");
				throw this.getBizLogicException(null, "errors.invalid", message);
			}
			else
			{
				storageContainerObj.setId((Long) list.get(0));
				specimenPosition.setStorageContainer(storageContainerObj);
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
			logger.error(daoExp.getMessage(), daoExp);
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
			specimen = (Specimen) dao.retrieveById(Specimen.class.getName(), specimen.getId());
			if (specimen == null)
			{
				throw this.getBizLogicException(null, "invalid.specimen." + "identifier", specimen
						.getId().toString());
			}
		}
		else if (!Validator.isEmpty(specimen.getLabel()))
		{
			List specimenList = dao
					.retrieve(Specimen.class.getName(), "label", specimen.getLabel());
			if (specimenList == null || specimenList.isEmpty())
			{
				throw this.getBizLogicException(null, "invalid.label.barcode", specimen.getLabel());
			}
			specimen = (Specimen) specimenList.get(0);
		}
		else if (!Validator.isEmpty(specimen.getBarcode()))
		{
			List specimenList = dao.retrieve(Specimen.class.getName(), "barcode", specimen
					.getBarcode());
			if (specimenList == null || specimenList.isEmpty())
			{
				throw this.getBizLogicException(null, "invalid.label.barcode", specimen
						.getBarcode());
			}
			specimen = (Specimen) specimenList.get(0);
		}
		aliquot.setSpecimen(specimen);
	}
	
	
	/**
	 * Validate availableQuantity against quantity per aliquot 
	 * @param aliquotDetailObj
	 * @param availableQuantity
	 * @throws ApplicationException
	 */
	private void validateQuantity(AliquotsDetailsDTO aliquotDetailObj,double availableQuantity) throws ApplicationException{
		double qtyPerAlq = aliquotDetailObj.getQuantityPerAliquot();
		int count = aliquotDetailObj.getNoOfAliquots();
		if((qtyPerAlq*count) > availableQuantity)
		{
			throw new ApplicationException(null,null,  Constants.INSUFFICIENT_AVAILABLE_QUANTITY);
		}
		
	}
	
	
	/**
	 * Calculate quantity per aliquot
	 * @param aliquotDetailObj
	 * @param availableQuantity
	 * @param isDouble
	 * @throws ApplicationException
	 */
	private void distributeAvailableQuantity(AliquotsDetailsDTO aliquotDetailObj,Double availableQuantity ,boolean isDouble) throws ApplicationException{
		final int aliquotCount = aliquotDetailObj.getNoOfAliquots();
		Double distributedQuantity;
		if (isDouble)
		{
			// Bug no 560
			if (availableQuantity < 0)
			{
				throw new ApplicationException(null,null,  Constants.INSUFFICIENT_AVAILABLE_QUANTITY);
			}
			double dQuantity;
			if (aliquotDetailObj.getQuantityPerAliquot() == null
					|| aliquotDetailObj.getQuantityPerAliquot() == 0)
			{
				final BigDecimal bgAvailTemp = new BigDecimal(availableQuantity);
				final BigDecimal bgCntTemp = new BigDecimal(aliquotCount);
				final BigDecimal bgAvail = bgAvailTemp.setScale(2, BigDecimal.ROUND_HALF_EVEN);
				final BigDecimal bgCnt = bgCntTemp.setScale(2, BigDecimal.ROUND_HALF_EVEN);
				final BigDecimal bgQuantity = bgAvail.divide(bgCnt, 2, BigDecimal.ROUND_FLOOR);
				dQuantity = bgQuantity.doubleValue();
				availableQuantity = availableQuantity - (dQuantity * aliquotCount);
				availableQuantity = AppUtility.roundOff(availableQuantity, Constants.QUANTITY_PRECISION);

			}else
			{
				dQuantity = aliquotDetailObj.getQuantityPerAliquot();
				availableQuantity = availableQuantity - (dQuantity * aliquotCount);
				availableQuantity = AppUtility.roundOff(availableQuantity, Constants.QUANTITY_PRECISION);
			}
			distributedQuantity = dQuantity;

			if (availableQuantity < 0)
			{
				throw new ApplicationException(null,null,  Constants.INSUFFICIENT_AVAILABLE_QUANTITY);
			}

			aliquotDetailObj.setAvailableQuantity(availableQuantity);
			
		}else
		{
			Integer availableQuantityInt= availableQuantity.intValue();
			if (availableQuantityInt < 0)
			{

				throw new ApplicationException(null,null,  Constants.INSUFFICIENT_AVAILABLE_QUANTITY);
			}
			Integer iQauntity = (int) (availableQuantityInt / aliquotCount);

			if (aliquotDetailObj.getQuantityPerAliquot() == null
					|| aliquotDetailObj.getQuantityPerAliquot() == 0)
			{
				iQauntity = (int) (availableQuantityInt / aliquotCount);
			}
			else
			{
				iQauntity = aliquotDetailObj.getQuantityPerAliquot().intValue();
			}

			distributedQuantity = iQauntity.doubleValue();
			availableQuantityInt = availableQuantityInt - (iQauntity * aliquotCount);

			if (availableQuantityInt < 0)
			{

				throw new ApplicationException(null,null,  Constants.INSUFFICIENT_AVAILABLE_QUANTITY);
			}

			aliquotDetailObj.setAvailableQuantity(availableQuantityInt.doubleValue());
		}
		aliquotDetailObj.setQuantityPerAliquot(distributedQuantity);
		
	}
	
	/**
	 * Calculate Parent availabel quantity
	 * @param intialAvailabelQuantity
	 * @param aliquotSpecimenList
	 * @return
	 */
	public double calculateAvailableQunatity(Double intialAvailabelQuantity,List aliquotSpecimenList){
		for(int i = 0; i< aliquotSpecimenList.size(); i++)
		{
			intialAvailabelQuantity = intialAvailabelQuantity + ((Specimen)aliquotSpecimenList.get(i)).getAvailableQuantity();
		}
		
		return intialAvailabelQuantity;
	}
	
	/**
	 * Get Specimen Position List as per number of aliquots
	 * @param aliquotDetailObj
	 * @param sessionDataBean
	 * @param dao
	 * @return
	 * @throws ApplicationException
	 */
	private List<SpecimenPosition> GetSpecimenPositionList(AliquotsDetailsDTO aliquotDetailObj, SessionDataBean sessionDataBean,DAO dao)
	throws ApplicationException{
		StorageContainerBizLogic scBiz = new StorageContainerBizLogic();
		StorageContainer strCont = scBiz.getStorageContainerFromName(dao,aliquotDetailObj.getContainerName());
		scBiz.validateContainerAccess(dao, strCont, sessionDataBean);
		StorageContainerForSpecimenBizLogic scfsBiz = new StorageContainerForSpecimenBizLogic();
		
		//Fetch list of available position from container.
		return scfsBiz.getAvailablePositionFromContainerForSpecimen(aliquotDetailObj.getContainerName(), aliquotDetailObj.getStartingStoragePositionX()
				, aliquotDetailObj.getStartingStoragePositionY(), aliquotDetailObj.getNoOfAliquots(), dao); // get from container biz logic (container name, pos1, pos2, count)
		
	}
	
	
	/**
	 * @param updateSpecimenAliquotCollection
	 * @param dao
	 * @throws ApplicationException
	 */
	private void updateCpBasedAliquot(Collection<AbstractDomainObject> updateSpecimenAliquotCollection,DAO dao)  throws ApplicationException{
		Iterator<AbstractDomainObject> ite = updateSpecimenAliquotCollection.iterator();
		while(ite.hasNext()){
			dao.update(ite.next());
		}
	}
	
	/**
	 * Set Aliquot Deatial Details
	 * @param aliquotSpecimen
	 * @param parentSpecimen
	 * @param aliquotDetailObj
	 * @param spePositionObj
	 */
	private void setAliquotSpecimenDetail(Specimen aliquotSpecimen,Specimen parentSpecimen,AliquotsDetailsDTO aliquotDetailObj,SpecimenPosition spePositionObj){
		aliquotSpecimen.setInitialQuantity(aliquotDetailObj.getQuantityPerAliquot());
		aliquotSpecimen.setAvailableQuantity(aliquotDetailObj.getQuantityPerAliquot());
		spePositionObj.setSpecimen(aliquotSpecimen);
		aliquotSpecimen.setSpecimenPosition(spePositionObj);
		if(aliquotSpecimen  instanceof MolecularSpecimen ){
			((MolecularSpecimen) aliquotSpecimen).setConcentrationInMicrogramPerMicroliter(((MolecularSpecimen)parentSpecimen).getConcentrationInMicrogramPerMicroliter());
		}
		aliquotSpecimen.setCreatedOn(aliquotDetailObj.getCreatedDate());
		aliquotSpecimen.setLineage(Constants.ALIQUOT);
		aliquotSpecimen.setIsAvailable(Boolean.TRUE);
		aliquotSpecimen.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
		aliquotSpecimen.setCollectionStatus(Constants.COLLECTION_STATUS_COLLECTED);
		
	}
	
	/**
	 * It create aliquot and sets inherited properties from parent specimen 
	 * @param parentSpecimen
	 * @return Specimen
	 */
	private Specimen createAliquotSpecimen(Specimen parentSpecimen){
		Specimen aliquotSpecimen = AppUtility.getSpecimen(parentSpecimen);
		aliquotSpecimen.setSpecimenClass(parentSpecimen.getClassName());
		aliquotSpecimen.setSpecimenType(parentSpecimen.getSpecimenType());
		aliquotSpecimen.setPathologicalStatus(parentSpecimen.getPathologicalStatus());
		aliquotSpecimen.setParentSpecimen(parentSpecimen);
		aliquotSpecimen.setSpecimenCollectionGroup(parentSpecimen.getSpecimenCollectionGroup());
		aliquotSpecimen.setSpecimenCharacteristics(parentSpecimen.getSpecimenCharacteristics());
		return aliquotSpecimen;
	}
	

	/**
	 * This api create aliquots
	 * @param aliquotDetailObj
	 * @param sessionDataBean
	 * @throws ApplicationException 
	 */
	public void createAliquotSpecimen(AliquotsDetailsDTO aliquotDetailObj, SessionDataBean sessionDataBean) throws ApplicationException{
		Specimen parentSpecimen = new Specimen();
		parentSpecimen.setLabel(aliquotDetailObj.getParentSpecimenLabel());
		NewSpecimenBizLogic specimenBizLogic = new NewSpecimenBizLogic();
		DAO dao = AppUtility.openDAOSession(sessionDataBean);
		
		try{
			specimenBizLogic.isAuthorized(dao, parentSpecimen, sessionDataBean);
			parentSpecimen = specimenBizLogic.getSpecimenDetailForAliquots(dao, aliquotDetailObj.getParentSpecimenLabel());
			validateQuantity(aliquotDetailObj,parentSpecimen.getAvailableQuantity());
		    distributeAvailableQuantity(aliquotDetailObj,parentSpecimen.getAvailableQuantity(),AppUtility.isQuantityDouble(parentSpecimen.getClassName(), parentSpecimen.getSpecimenType()));
			List <SpecimenPosition> specPosList = GetSpecimenPositionList(aliquotDetailObj,sessionDataBean,dao);
			
			final Collection<AbstractDomainObject> specimenCollection = new LinkedHashSet<AbstractDomainObject>();
			for (int i = 0; i < aliquotDetailObj.getNoOfAliquots(); i++)
			{
				final Specimen aliquotSpecimen = createAliquotSpecimen(parentSpecimen);
				aliquotSpecimen.setDisposeParentSpecimen(aliquotDetailObj.isDisposeParentCheck());
				setAliquotSpecimenDetail(aliquotSpecimen,parentSpecimen,aliquotDetailObj,specPosList.get(i));
				specimenCollection.add(aliquotSpecimen);
			}
			specimenBizLogic.insert(specimenCollection, sessionDataBean, 0, false);
			specimenBizLogic.disposeParentSpecimen(sessionDataBean, specimenCollection,
					Constants.SPECIMEN_DISPOSAL_REASON);
			this.updateParentSpecimen(parentSpecimen, aliquotDetailObj.getAvailableQuantity(), dao);
			
		}catch(ApplicationException exp){
			throw new BizLogicException(null, exp, exp.getMsgValues());
		}
		finally{
			dao.closeSession();
		}
	}
	
	/**
	 * This api create aliquots based on cp
	 * It first fetches all pending aliquots of specimen and marked those as collected and updates is available quantity
	 * @param aliquotDetailObj
	 * @param sessionDataBean
	 * @throws ApplicationException 
	 */
	public void createAliquotSpecimenBasedOnCp(AliquotsDetailsDTO aliquotDetailObj, SessionDataBean sessionDataBean) throws ApplicationException{
		Specimen parentSpecimen = new Specimen();
		parentSpecimen.setLabel(aliquotDetailObj.getParentSpecimenLabel());
		NewSpecimenBizLogic specimenBizLogic = new NewSpecimenBizLogic();
		DAO dao = AppUtility.openDAOSession(sessionDataBean);
		
		try{
			
			specimenBizLogic.isAuthorized(dao, parentSpecimen, sessionDataBean);
			parentSpecimen = specimenBizLogic.getSpecimenDetailForAliquots(dao, aliquotDetailObj.getParentSpecimenLabel());
			//Fetch list of pending aliquots.
			List aliquotSpecimenList = specimenBizLogic.getListOfPendingAliquotSpecimen(parentSpecimen.getId(),dao);
			
			// validate if there are enuff pending aliquots to be created
			
			//Calculate available quantity by ading aliquot available quantity
			Double availableQuantity = parentSpecimen.getAvailableQuantity();
			//Validate Available Quantity.
			validateQuantity(aliquotDetailObj,availableQuantity);
			
			//Calculate quantity per aliquot
			distributeAvailableQuantity(aliquotDetailObj,availableQuantity,AppUtility.isQuantityDouble(parentSpecimen.getClassName(), parentSpecimen.getSpecimenType()));
						
			//Fetch list of available position from container.
			List <SpecimenPosition> specPosList = GetSpecimenPositionList(aliquotDetailObj,sessionDataBean,dao);
			
			final Collection<AbstractDomainObject> newSpecimenAliquotCollection = new LinkedHashSet<AbstractDomainObject>();
			final Collection<AbstractDomainObject> updateSpecimenAliquotCollection = new LinkedHashSet<AbstractDomainObject>();
			long lastChildNo = specimenBizLogic.getTotalNoOfAliquotSpecimen(parentSpecimen.getId(), dao);
			int cnt = aliquotDetailObj.getNoOfAliquots() < aliquotSpecimenList.size() ? aliquotDetailObj.getNoOfAliquots() : aliquotSpecimenList.size();
			for (int i = 0; i < cnt; i++)
			{
				Specimen aliquotSpecimen = (Specimen) aliquotSpecimenList.get(i);
				aliquotSpecimen.setLabel(parentSpecimen.getLabel() + "_"+ (++lastChildNo));
				setAliquotSpecimenDetail(aliquotSpecimen,parentSpecimen,aliquotDetailObj,specPosList.get(i));
				updateSpecimenAliquotCollection.add(aliquotSpecimen);
				
			}
			for (int i = 0; i < aliquotDetailObj.getNoOfAliquots()-updateSpecimenAliquotCollection.size(); i++)
			{
				Specimen aliquotSpecimen = createAliquotSpecimen(parentSpecimen);
				
				aliquotSpecimen.setLabel(parentSpecimen.getLabel() + "_"+ (++lastChildNo));
				setAliquotSpecimenDetail(aliquotSpecimen,parentSpecimen,aliquotDetailObj,specPosList.get(i));
				newSpecimenAliquotCollection.add(aliquotSpecimen);
				
			}
			this.updateCpBasedAliquot(updateSpecimenAliquotCollection,dao);
			specimenBizLogic.insert(newSpecimenAliquotCollection, sessionDataBean, 0, false);
			this.updateParentSpecimen(parentSpecimen, aliquotDetailObj.getAvailableQuantity(), dao);
			if(aliquotDetailObj.isDisposeParentCheck()){
				specimenBizLogic.disposeSpecimen(sessionDataBean,
						parentSpecimen, Constants.SPECIMEN_DISPOSAL_REASON);
			}
			
			dao.commit();
			
		}catch(ApplicationException exp){
			throw new BizLogicException(null, exp, exp.getMsgValues());
		}
		finally{
			dao.closeSession();
		}
	}
}