
package edu.wustl.catissuecore.bizlogic;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.dto.BiohazardDTO;
import edu.wustl.catissuecore.dto.ExternalIdentifierDTO;
import edu.wustl.catissuecore.dto.SpecimenDTO;
import edu.wustl.catissuecore.namegenerator.BarcodeGenerator;
import edu.wustl.catissuecore.namegenerator.BarcodeGeneratorFactory;
import edu.wustl.catissuecore.namegenerator.LabelException;
import edu.wustl.catissuecore.namegenerator.LabelGenException;
import edu.wustl.catissuecore.namegenerator.LabelGenerator;
import edu.wustl.catissuecore.namegenerator.LabelGeneratorFactory;
import edu.wustl.catissuecore.namegenerator.NameGeneratorException;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;

public class SpecimenBizlogic
{

	private static final Logger LOGGER = Logger.getCommonLogger(SpecimenBizlogic.class);

	/**
	 * This will update the specimen object in the database
	 * @param specimenDTO
	 * @param sessionDataBean
	 * @throws BizLogicException
	 */
	public void updateSpecimen(DAO dao, SpecimenDTO specimenDTO, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			//object retrieval for auditing purpose
			Specimen oldSpecimenObj = (Specimen) dao.retrieveById(Specimen.class.getName(),
					specimenDTO.getId());
			//updating the object with DTO
			getUpdatedSpecimen(oldSpecimenObj, specimenDTO, sessionDataBean, dao);

			if (Variables.isSpecimenLabelGeneratorAvl)
			{
				generateLabel(oldSpecimenObj);
			}
			if (Variables.isSpecimenBarcodeGeneratorAvl)
			{
				generateBarCode(oldSpecimenObj);
			}
			validateSpecimen(oldSpecimenObj, dao);
			//updating the specimen in database
			dao.update(oldSpecimenObj);

		}
		catch (final DAOException daoExp)
		{
			LOGGER.error(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (ParseException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, null, null);
		}
		catch (ApplicationException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		catch (CloneNotSupportedException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, null, null);
		}
	}

	/**
	 * Checks if is status collected.
	 *
	 * @param specimen the specimen
	 *
	 * @return true, if checks if is status collected
	 */
	private boolean isStatusCollected(Specimen specimen)
	{
		return specimen.getCollectionStatus() != null
				&& specimen.getCollectionStatus().equals(Constants.COLLECTION_STATUS_COLLECTED);
	}

	/**
	 * Generate label.
	 *
	 * @param specimen Specimen Object
	 *
	 * @throws BizLogicException Database related Exception
	 */
	private void generateLabel(Specimen specimen) throws BizLogicException
	{
		/**
		 * Call Specimen label generator if automatic generation is specified
		 */

		if (isStatusCollected(specimen))
		{
			try
			{
				final LabelGenerator spLblGenerator;
				spLblGenerator = LabelGeneratorFactory
						.getInstance(Constants.SPECIMEN_LABEL_GENERATOR_PROPERTY_NAME);
				if (spLblGenerator != null)
				{
					spLblGenerator.setLabel(specimen);
				}
			}
			catch (LabelException e)
			{
				LOGGER.error(e.getMessage(), e);
				throw this.getBizLogicException(e, "errors.item", e.getMessage());
			}
			catch (LabelGenException e)
			{
				LOGGER.info(e);
			}
			catch (final NameGeneratorException e)
			{
				LOGGER.error(e.getMessage(), e);
				throw this.getBizLogicException(e, "name.generator.exp", "");
			}
		}
	}

	/**
	 * Generate bar code.
	 *
	 * @param specimen Specimen Object
	 *
	 * @throws BizLogicException Database related exception
	 */
	private void generateBarCode(Specimen specimen) throws BizLogicException
	{
		if (edu.wustl.catissuecore.util.global.Variables.isSpecimenBarcodeGeneratorAvl
				&& (specimen.getBarcode() == null || "".equals(specimen.getBarcode())))
		{
			try
			{
				final BarcodeGenerator spBarcodeGenerator = BarcodeGeneratorFactory
						.getInstance(Constants.SPECIMEN_BARCODE_GENERATOR_PROPERTY_NAME);
				spBarcodeGenerator.setBarcode(specimen);
			}
			catch (final NameGeneratorException e)
			{
				LOGGER.error(e.getMessage(), e);
				throw this.getBizLogicException(e, "name.generator.exp", "");
			}
		}
	}

	/**
	 * Populating the retrieved specimen with DTO values
	 * @param oldSpecimenObj
	 * @param specimenDTO
	 * @param sessionDataBean 
	 * @throws ParseException 
	 * @throws ApplicationException 
	 * @throws CloneNotSupportedException 
	 */
	private void getUpdatedSpecimen(Specimen oldSpecimenObj, SpecimenDTO specimenDTO,
			SessionDataBean sessionDataBean, DAO dao) throws ParseException, ApplicationException,
			CloneNotSupportedException
	{
		if (specimenDTO.getActivityStatus() != null)
			oldSpecimenObj.setActivityStatus(specimenDTO.getActivityStatus());

		if (specimenDTO.getLabel() != null)
			oldSpecimenObj.setLabel(specimenDTO.getLabel());

		if (specimenDTO.getQuantity() != null)
			oldSpecimenObj.setInitialQuantity(specimenDTO.getQuantity());

		if (specimenDTO.getCollectionStatus() != null
				&& specimenDTO.getCollectionStatus().equalsIgnoreCase(
						Constants.COLLECTION_STATUS_COLLECTED)
				&& !oldSpecimenObj.getCollectionStatus().equalsIgnoreCase(
						Constants.COLLECTION_STATUS_COLLECTED))
		{
			oldSpecimenObj.setCollectionStatus(specimenDTO.getCollectionStatus());
			oldSpecimenObj.setIsAvailable(true);

			if (specimenDTO.getQuantity() != null)
			{
				oldSpecimenObj.setAvailableQuantity(specimenDTO.getQuantity());
			}
			else
			{
				oldSpecimenObj.setAvailableQuantity(oldSpecimenObj.getInitialQuantity());
			}

			this.setSpecimenEvents(oldSpecimenObj, sessionDataBean);
		}
		else
		{
			if (specimenDTO.getCollectionStatus() != null)
				oldSpecimenObj.setCollectionStatus(specimenDTO.getCollectionStatus());

			if (specimenDTO.getAvailableQuantity() != null)
				oldSpecimenObj.setAvailableQuantity(specimenDTO.getAvailableQuantity());

			if (specimenDTO.isAvailable() != null)
				oldSpecimenObj.setIsAvailable(specimenDTO.isAvailable());
		}

		if (specimenDTO.getBarcode() != null)
			oldSpecimenObj.setBarcode(specimenDTO.getBarcode());

		if (specimenDTO.getClassName() != null)
			oldSpecimenObj.setSpecimenClass(specimenDTO.getClassName());

		if (specimenDTO.getType() != null)
			oldSpecimenObj.setSpecimenType(specimenDTO.getType());

		if (specimenDTO.getPathologicalStatus() != null)
			oldSpecimenObj.setPathologicalStatus(specimenDTO.getPathologicalStatus());

		if (specimenDTO.getTissueSide() != null)
			oldSpecimenObj.getSpecimenCharacteristics().setTissueSide(specimenDTO.getTissueSide());

		if (specimenDTO.getTissueSite() != null)
			oldSpecimenObj.getSpecimenCharacteristics().setTissueSite(specimenDTO.getTissueSite());

		if (specimenDTO.getComments() != null)
			oldSpecimenObj.setComment(specimenDTO.getComments());

		SpecimenPosition position = new SpecimenPosition();
		if (specimenDTO.getIsVirtual() == null || specimenDTO.getIsVirtual())
		{
			position = null;
		}
		else if (oldSpecimenObj.getSpecimenPosition() == null
				&& Constants.COLLECTION_STATUS_COLLECTED.equals(oldSpecimenObj
						.getCollectionStatus()))
		{
			if (specimenDTO.getPos1() != null && !"".equals(specimenDTO.getPos1().trim())
					&& specimenDTO.getPos2() != null && !"".equals(specimenDTO.getPos2().trim()))
			{
				int toPos1Int = StorageContainerUtil.convertSpecimenPositionsToInteger(
						specimenDTO.getContainerName(), 1, specimenDTO.getPos1());
				int toPos2Int = StorageContainerUtil.convertSpecimenPositionsToInteger(
						specimenDTO.getContainerName(), 2, specimenDTO.getPos2());
				position.setPositionDimensionOne(toPos1Int);
				position.setPositionDimensionTwo(toPos2Int);
				position.setPositionDimensionOneString(specimenDTO.getPos1());
				position.setPositionDimensionTwoString(specimenDTO.getPos2());
				StorageContainer container = new StorageContainer();
				Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
				substParams.put("0",
						new NamedQueryParam(DBTypes.STRING, specimenDTO.getContainerName()));

				final List list = ((HibernateDAO) dao).executeNamedQuery(
						"getStorageContainerIdByContainerName", substParams);

				if (!list.isEmpty())
				{
					container.setId((Long) list.get(0));
				}
				else
				{
					throw this.getBizLogicException(null, "errors.invalid",
							ApplicationProperties.getValue("array.positionInStorageContainer"));
				}
				container.setName(specimenDTO.getContainerName());
				position.setStorageContainer(container);
				position.setSpecimen(oldSpecimenObj);
				oldSpecimenObj.setSpecimenPosition(position);
			}

		}
		oldSpecimenObj.setExternalIdentifierCollection(getExternalIdentifiers(oldSpecimenObj,
				specimenDTO));
		oldSpecimenObj.setBiohazardCollection(getBiohazards(oldSpecimenObj, specimenDTO));
	}

	/**
	 * Returns ExternalIdentifier Collection to be set in old specimen object.
	 * @param oldSpecimenObj
	 * @param specimenDTO
	 * @return
	 */
	private Collection<ExternalIdentifier> getExternalIdentifiers(Specimen oldSpecimenObj,
			SpecimenDTO specimenDTO)
	{
		Collection<ExternalIdentifier> externalIdentifierCollection = oldSpecimenObj
				.getExternalIdentifierCollection();

		Collection<ExternalIdentifierDTO> externalIdentifierDTOCollection = specimenDTO
				.getExternalIdentifiers();

		externalIdentifierCollection.retainAll(externalIdentifierDTOCollection);

		for (ExternalIdentifierDTO externalIdentifierDTO : externalIdentifierDTOCollection)
		{
			if (externalIdentifierDTO.getStatus().equalsIgnoreCase("ADD"))
			{
				externalIdentifierCollection.add(getExternalIdentifierDomainObjFromDTO(
						oldSpecimenObj, externalIdentifierDTO));
			}
			else if (externalIdentifierDTO.getStatus().equalsIgnoreCase("EDIT"))
			{
				for (ExternalIdentifier externalIdentifier : externalIdentifierCollection)
				{
					if (externalIdentifier.equals(externalIdentifierDTO))
					{
						externalIdentifier.setName(externalIdentifierDTO.getName());
						externalIdentifier.setValue(externalIdentifierDTO.getValue());
						break;
					}
				}
			}
		}

		if(externalIdentifierCollection.isEmpty())
		{
			ExternalIdentifier externalIdentifier = new ExternalIdentifier();
			
			externalIdentifierCollection.add(externalIdentifier);
		}
		return externalIdentifierCollection;
	}

	/**
	 * @param oldSpecimenObj
	 * @param externalIdentifierDTO
	 * @return
	 */
	private ExternalIdentifier getExternalIdentifierDomainObjFromDTO(Specimen oldSpecimenObj,
			ExternalIdentifierDTO externalIdentifierDTO)
	{
		ExternalIdentifier externalIdentifier = new ExternalIdentifier();
		externalIdentifier.setName(externalIdentifierDTO.getName());
		externalIdentifier.setValue(externalIdentifierDTO.getValue());
		externalIdentifier.setSpecimen(oldSpecimenObj);
		return externalIdentifier;
	}

	/**
	 * Returns Biohazard Collection to be set in old specimen object.
	 * @param oldSpecimenObj
	 * @param specimenDTO
	 * @return
	 * @throws ApplicationException 
	 */
	private Collection<Biohazard> getBiohazards(Specimen oldSpecimenObj, SpecimenDTO specimenDTO)
	{
		Collection<Biohazard> biohazardCollection = oldSpecimenObj.getBiohazardCollection();

		Collection<BiohazardDTO> biohazardDTOCollection = specimenDTO.getBioHazards();

		for (BiohazardDTO biohazardDTO : biohazardDTOCollection)
		{
			if (!biohazardCollection.contains(biohazardDTO))
			{
				biohazardCollection.add(getBiohazardDomainObjFromDTO(biohazardDTO));
			}
		}

		biohazardCollection.retainAll(biohazardDTOCollection);

		return biohazardCollection;
	}

	private Biohazard getBiohazardDomainObjFromDTO(BiohazardDTO biohazardDTO)
	{
		Biohazard biohazard = new Biohazard();
		biohazard.setId(biohazardDTO.getId());
		biohazard.setName(biohazardDTO.getName());
		biohazard.setType(biohazardDTO.getType());
		return biohazard;
	}

	protected BizLogicException getBizLogicException(Exception exception, String key,
			String logMessage)
	{
		LOGGER.debug(logMessage);
		ErrorKey errorKey = ErrorKey.getErrorKey(key);
		return new BizLogicException(errorKey, exception, logMessage);
	}

	/**
	 * Sets the specimen events.
	 *
	 * @param specimen : specimen
	 * @param sessionDataBean  : sessionDataBean
	 * @throws CloneNotSupportedException 
	 */
	private void setSpecimenEvents(Specimen specimen, SessionDataBean sessionDataBean)
			throws CloneNotSupportedException
	{
		final Specimen parentSpecimen = (Specimen) specimen.getParentSpecimen();
		if (specimen.getParentSpecimen() == null)
		{
			specimen.setPropogatingSpecimenEventCollection(specimen.getSpecimenRequirement()
					.getSpecimenEventCollection(), sessionDataBean.getUserId(), specimen);
		}
		else
		{
			specimen.setSpecimenEventCollection(this.populateDeriveSpecimenEventCollection(
					parentSpecimen, specimen));
		}
	}

	/**
	 * Sets the default events to specimen.
	 *
	 * @param specimen Set default events to specimens
	 * @param sessionDataBean Session data bean This method sets the default events to
	 * specimens if they are null
	 */
	private void setDefaultEventsToSpecimen(Specimen specimen, SessionDataBean sessionDataBean)
	{
		final Collection<SpecimenEventParameters> specimenEventColl = new HashSet<SpecimenEventParameters>();

		specimen.setSpecimenEventCollection(specimenEventColl);
	}

	/**
	 * Set event parameters from parent specimen to derived specimen.
	 *
	 * @param parentSpecimen specimen
	 * @param deriveSpecimen Derived Specimen
	 *
	 * @return set
	 * @throws CloneNotSupportedException 
	 */
	private Set<AbstractDomainObject> populateDeriveSpecimenEventCollection(
			Specimen parentSpecimen, Specimen deriveSpecimen) throws CloneNotSupportedException
	{
		final Set<AbstractDomainObject> deriveEventCollection = new HashSet<AbstractDomainObject>();
		final Set<SpecimenEventParameters> parentSpecimeneventCollection = (Set<SpecimenEventParameters>) parentSpecimen
				.getSpecimenEventCollection();
		SpecimenEventParameters deriveSpecimenEventParameters = null;
		if (parentSpecimeneventCollection != null)
		{
			for (final SpecimenEventParameters specimenEventParameters : parentSpecimeneventCollection)
			{
				deriveSpecimenEventParameters = (SpecimenEventParameters) specimenEventParameters
						.clone();
				deriveSpecimenEventParameters.setId(null);
				deriveSpecimenEventParameters.setSpecimen(deriveSpecimen);
				deriveEventCollection.add(deriveSpecimenEventParameters);
			}
		}
		return deriveEventCollection;
	}

	/**
	 * Validates the specimen DTO from UI.
	 * @param specimenDTO
	 * @throws BizLogicException
	 */
	/**
	 * @param dao 
	 * @param specimenDTO
	 * @throws BizLogicException
	 * @throws DAOException 
	 */
	private void validateSpecimen(Specimen specimen, DAO dao) throws BizLogicException,
			DAOException
	{
		final List specimenClassList = AppUtility.getSpecimenClassList();
		final String specimenClass = specimen.getClassName();
		if (!Validator.isEnumeratedValue(specimenClassList, specimenClass))
		{
			throw this.getBizLogicException(null, "protocol.class.errMsg", "");
		}

		if (!Validator.isEnumeratedValue(AppUtility.getSpecimenTypes(specimenClass),
				specimen.getSpecimenType()))
		{
			throw this.getBizLogicException(null, "protocol.type.errMsg", "");
		}

		if (specimen.getCollectionStatus().equalsIgnoreCase(Constants.SPECIMEN_COLLECTED)
				&& validateFieldValue(specimen.getLabel()))
		{
			final String message = ApplicationProperties.getValue("specimen.label");
			throw this.getBizLogicException(null, "errors.item.required", message);
		}
		if (specimen.getAvailableQuantity().compareTo(specimen.getInitialQuantity()) > 0)
		{
			final String quantityString = ApplicationProperties
					.getValue("specimen.availableQuantity");
			throw this.getBizLogicException(null, "errors.availablequantity", quantityString);
		}

		if (specimen.getSpecimenPosition() != null)
		{
			new NewSpecimenBizLogic().chkContainerValidForSpecimen(specimen.getSpecimenPosition()
					.getStorageContainer(), specimen, dao);

			this.validateStorageContainer(specimen, dao);
		}

		checkDuplicateSpecimenFields(specimen, dao);
	}

	/**
	 * Validates combobox value.
	 * @param value
	 * @return
	 */
	private boolean validateComboValue(String value)
	{
		return Validator.isEmpty(value) || value.equalsIgnoreCase(Constants.SELECT_OPTION)
				|| value.equalsIgnoreCase(Constants.DEFAULT_CONDITION);
	}

	/**
	 * Validates String value.
	 * @param value
	 * @return
	 */
	private boolean validateFieldValue(String value)
	{
		return Validator.isEmpty(value);
	}

	/**
	 * Validate storage container.
	 *
	 * @param specimen Specimen to validate
	 * @param dao DAO object
	 *
	 * @throws BizLogicException Database related exception
	 * @throws DAOException 
	 */
	private void validateStorageContainer(Specimen specimen, DAO dao) throws BizLogicException,
			DAOException
	{
		if (specimen.getSpecimenPosition() != null
				&& specimen.getSpecimenPosition().getStorageContainer() != null
				&& (specimen.getSpecimenPosition().getStorageContainer().getId() == null && specimen
						.getSpecimenPosition().getStorageContainer().getName() == null))
		{
			final String message = ApplicationProperties.getValue("specimen.storageContainer");
			throw this.getBizLogicException(null, "errors.invalid", message);
		}
		if (specimen.getSpecimenPosition() != null
				&& specimen.getSpecimenPosition().getStorageContainer() != null
				&& specimen.getSpecimenPosition().getStorageContainer().getName() != null
				&& specimen.getSpecimenPosition().getStorageContainer().getId() != null)
		{
			final StorageContainer storageContainerObj = specimen.getSpecimenPosition()
					.getStorageContainer();
			final String sourceObjectName = StorageContainer.class.getName();
			final String[] selectColumnName = {"id"};
			final String storageContainerName = specimen.getSpecimenPosition()
					.getStorageContainer().getName();

			final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.addCondition(new EqualClause("name", storageContainerName));
			final List list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);

			if (!list.isEmpty())
			{
				storageContainerObj.setId((Long) list.get(0));
				specimen.getSpecimenPosition().setStorageContainer(storageContainerObj);
			}
			else
			{
				final String message = ApplicationProperties.getValue("specimen.storageContainer");
				throw this.getBizLogicException(null, "errors.invalid", message);
			}
		}

	}

	/**
	 * Checks duplicate specimen fields.
	 *
	 * @param specimen Specimen
	 * @param dao DAO object
	 * @throws DAOException 
	 *
	 * @throws BizLogicException Database exception
	 */
	private void checkDuplicateSpecimenFields(Specimen specimen, DAO dao) throws DAOException,
			BizLogicException
	{
		List list = null;
		if (specimen.getLabel() != null)
		{
			list = dao.retrieve(Specimen.class.getName(), "label", specimen.getLabel());
			for (Object object : list)
			{
				final Specimen specimenObject = (Specimen) object;
				if (!specimenObject.getId().equals(specimen.getId()))
				{
					throw this.getBizLogicException(null, "label.already.exits",
							specimen.getLabel());
				}
			}
		}
		if (specimen.getBarcode() != null)
		{
			list = dao.retrieve(Specimen.class.getName(), "barcode", specimen.getBarcode());
			for (Object object : list)
			{
				final Specimen specimenObject = (Specimen) object;
				if (!specimenObject.getId().equals(specimen.getId()))
				{
					throw this.getBizLogicException(null, "barcode.already.exits",
							specimen.getBarcode());
				}
			}
		}
	}

}
