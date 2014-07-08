
package edu.wustl.catissuecore.bizlogic;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
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
import edu.wustl.catissuecore.util.SpecimenUtil;
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
	 * @return 
	 * @throws BizLogicException
	 */
	public SpecimenDTO updateSpecimen(HibernateDAO hibernateDao, SpecimenDTO specimenDTO,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		Specimen oldSpecimenObj = null;
		try
		{
			//object retrieval for auditing purpose
			oldSpecimenObj = (Specimen) hibernateDao.retrieveById(Specimen.class.getName(),
					specimenDTO.getId());
			//updating the object with DTO
			getUpdatedSpecimen(oldSpecimenObj, specimenDTO, sessionDataBean, hibernateDao);

			if (Variables.isSpecimenLabelGeneratorAvl || Variables.isTemplateBasedLblGeneratorAvl)
			{
				generateLabel(oldSpecimenObj);
			}
			if (Variables.isSpecimenBarcodeGeneratorAvl)
			{
				generateBarCode(oldSpecimenObj);
			}
			validateSpecimen(oldSpecimenObj, hibernateDao);
			//updating the specimen in database
			hibernateDao.update(oldSpecimenObj);

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
		return this.getDTO(oldSpecimenObj);
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
			SessionDataBean sessionDataBean, HibernateDAO hibernateDao) throws ParseException, ApplicationException,
			CloneNotSupportedException
	{
		if (!Validator.isEmpty(specimenDTO.getActivityStatus()))
			oldSpecimenObj.setActivityStatus(specimenDTO.getActivityStatus());

		if(specimenDTO.getCreatedDate() != null)
			oldSpecimenObj.setCreatedOn(specimenDTO.getCreatedDate());
		if (!Validator.isEmpty(specimenDTO.getLabel()))
			oldSpecimenObj.setLabel(specimenDTO.getLabel());

		if (specimenDTO.getQuantity() != null)
			oldSpecimenObj.setInitialQuantity(specimenDTO.getQuantity());

		if (!Validator.isEmpty(specimenDTO.getCollectionStatus())
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
			if (!Validator.isEmpty(specimenDTO.getCollectionStatus()))
				oldSpecimenObj.setCollectionStatus(specimenDTO.getCollectionStatus());

			if (specimenDTO.getAvailableQuantity() != null)
				oldSpecimenObj.setAvailableQuantity(specimenDTO.getAvailableQuantity());

			if (specimenDTO.isAvailable() != null)
				oldSpecimenObj.setIsAvailable(specimenDTO.isAvailable());
		}

		if (!Validator.isEmpty(specimenDTO.getBarcode()))
			oldSpecimenObj.setBarcode(specimenDTO.getBarcode());

		if (!Validator.isEmpty(specimenDTO.getClassName()))
			oldSpecimenObj.setSpecimenClass(specimenDTO.getClassName());

		if (!Validator.isEmpty(specimenDTO.getType()))
			oldSpecimenObj.setSpecimenType(specimenDTO.getType());

		if (!Validator.isEmpty(specimenDTO.getPathologicalStatus()))
			oldSpecimenObj.setPathologicalStatus(specimenDTO.getPathologicalStatus());

		if (!Validator.isEmpty(specimenDTO.getTissueSide()))
			oldSpecimenObj.setTissueSide(specimenDTO.getTissueSide());

		if (!Validator.isEmpty(specimenDTO.getTissueSite()))
			oldSpecimenObj.setTissueSite(specimenDTO.getTissueSite());

		if (!Validator.isEmpty(specimenDTO.getComments()))
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

				final List list = ((HibernateDAO) hibernateDao).executeNamedQuery(
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
		if(Constants.MOLECULAR.equals(oldSpecimenObj.getClassName()) && specimenDTO.getConcentration() != null)
		{
			oldSpecimenObj.setConcentrationInMicrogramPerMicroliter(specimenDTO.getConcentration());
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

		if (externalIdentifierCollection.isEmpty())
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
	 * @param hibernateDao 
	 * @param specimenDTO
	 * @throws BizLogicException
	 * @throws DAOException 
	 */
	private void validateSpecimen(Specimen specimen, HibernateDAO hibernateDao) throws BizLogicException,
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
					.getStorageContainer(), specimen, hibernateDao);

			this.validateStorageContainer(specimen, hibernateDao);
		}

		checkDuplicateSpecimenFields(specimen, hibernateDao);
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

	public edu.wustl.catissuecore.dto.SpecimenDTO getDTO(Specimen specimen)
			throws BizLogicException
	{
		edu.wustl.catissuecore.dto.SpecimenDTO specimenDTO = new edu.wustl.catissuecore.dto.SpecimenDTO();
		//Specimen specimen = (Specimen) dao.retrieveById(Specimen.class.getName(), identifier);
		specimenDTO.setId(specimen.getId());
		specimenDTO.setLabel(specimen.getLabel());
		specimenDTO.setActivityStatus(specimen.getActivityStatus());
		specimenDTO.setAvailable(specimen.getIsAvailable());
		specimenDTO.setAvailableQuantity(specimen.getAvailableQuantity());
		specimenDTO.setBarcode(specimen.getBarcode());
		specimenDTO.setClassName(specimen.getClassName());
		specimenDTO.setCollectionStatus(specimen.getCollectionStatus());
		specimenDTO.setComments(specimen.getComment());
		//				specimenDTO.setConcentration(specimen.getc)
		specimenDTO.setCreatedDate(specimen.getCreatedOn());
		specimenDTO.setLineage(specimen.getLineage());
		if (specimen.getParentSpecimen() != null)
		{
			specimenDTO.setParentSpecimenId(specimen.getParentSpecimen().getId());
			specimenDTO.setParentSpecimenName(specimen.getParentSpecimen().getLabel());
		}
		specimenDTO.setPathologicalStatus(specimen.getPathologicalStatus());
		specimenDTO.setQuantity(specimen.getInitialQuantity());
		if (specimen.getSpecimenCollectionGroup() != null)
		{
			specimenDTO.setSpecimenCollectionGroupId(specimen.getSpecimenCollectionGroup().getId());
			specimenDTO.setSpecimenCollectionGroupName(specimen.getSpecimenCollectionGroup()
					.getName());
		}
		specimenDTO.setTissueSide(specimen.getTissueSide());
		specimenDTO.setTissueSite(specimen.getTissueSite());
		specimenDTO.setType(specimen.getSpecimenType());
		if (specimen.getSpecimenPosition() != null)
		{
			specimenDTO.setPos1(specimen.getSpecimenPosition().getPositionDimensionOneString());
			specimenDTO.setPos2(specimen.getSpecimenPosition().getPositionDimensionTwoString());
			specimenDTO
					.setContainerId(specimen.getSpecimenPosition().getStorageContainer().getId());
			specimenDTO.setContainerName(specimen.getSpecimenPosition().getStorageContainer()
					.getName());
			specimenDTO.setIsVirtual(Boolean.FALSE);
		}
		else
		{
			specimenDTO.setIsVirtual(Boolean.TRUE);
		}
		specimenDTO.setConcentration(specimen.getConcentrationInMicrogramPerMicroliter());
		specimenDTO.setExternalIdentifiers(getExternalIdentifierDTOCollection(specimen));
		specimenDTO.setBioHazards(getBiohazardDTOCollection(specimen));

		return specimenDTO;
	}

	/**
	 * Populate ExternalIdentifierDTOCollection from Specimen.
	 * @param specimen
	 * @return
	 */
	private Collection<ExternalIdentifierDTO> getExternalIdentifierDTOCollection(Specimen specimen)
	{
		Collection<ExternalIdentifier> externalIdentifiers = specimen
				.getExternalIdentifierCollection();

		Collection<ExternalIdentifierDTO> externalIdentifierDTOs = new HashSet<ExternalIdentifierDTO>();

		for (ExternalIdentifier externalIdentifier : externalIdentifiers)
		{
			ExternalIdentifierDTO externalIdentifierDTO = new ExternalIdentifierDTO();

			externalIdentifierDTO.setId(externalIdentifier.getId());
			externalIdentifierDTO.setName(externalIdentifier.getName());
			externalIdentifierDTO.setValue(externalIdentifier.getValue());

			externalIdentifierDTOs.add(externalIdentifierDTO);
		}
		return externalIdentifierDTOs;
	}

	/**
	 * Populate BiohazardDTOCollection from Specimen.
	 * @param specimen
	 * @return
	 */
	private Collection<BiohazardDTO> getBiohazardDTOCollection(Specimen specimen)
	{
		Collection<Biohazard> biohazards = specimen.getBiohazardCollection();

		Collection<BiohazardDTO> biohazardDTOs = new HashSet<BiohazardDTO>();

		for (Biohazard biohazard : biohazards)
		{
			BiohazardDTO biohazardDTO = new BiohazardDTO();

			biohazardDTO.setId(biohazard.getId());
			biohazardDTO.setName(biohazard.getName());
			biohazardDTO.setType(biohazard.getType());

			biohazardDTOs.add(biohazardDTO);
		}

		return biohazardDTOs;
	}

	public boolean isSpecimenLabelGeneratorAvl(Long identifier, HibernateDAO hibernateDao) throws BizLogicException
	{
		boolean generateLabel = Variables.isSpecimenLabelGeneratorAvl;

		if (Variables.isTemplateBasedLblGeneratorAvl)
		{
			try
			{
				Specimen specimen = (Specimen) hibernateDao.retrieveById(Specimen.class.getName(),
						identifier);

				final CollectionProtocolRegistration collectionProtocolRegistration = specimen
						.getSpecimenCollectionGroup().getCollectionProtocolRegistration();

				String parentLabelFormat = collectionProtocolRegistration.getCollectionProtocol()
						.getSpecimenLabelFormat();

				String derivativeLabelFormat = collectionProtocolRegistration
						.getCollectionProtocol().getDerivativeLabelFormat();

				String aliquotLabelFormat = collectionProtocolRegistration.getCollectionProtocol()
						.getAliquotLabelFormat();

				String lineage = specimen.getLineage();
				if (lineage == null || "".equals(lineage))
				{
					lineage = Constants.NEW_SPECIMEN;
				}

				generateLabel = SpecimenUtil.isLblGenOnForCP(parentLabelFormat,
						derivativeLabelFormat, aliquotLabelFormat, lineage);
			}
			catch (DAOException e)
			{
				LOGGER.error(e.getMessage(), e);
				throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
			}
		}
		return generateLabel;
	}

	public SpecimenDTO insert(SpecimenDTO specimenDTO, HibernateDAO dao) throws BizLogicException
	{
		Specimen specimen = getSpecimen(specimenDTO);
		try
		{
			validateSpecimen(specimen, dao);
			dao.insert(specimen);
		}
		catch (DAOException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		return getDTO(specimen);
	}

	private Specimen getSpecimen(SpecimenDTO specimenDTO)
	{
		Specimen specimen = new Specimen();
		specimen.setActivityStatus(specimenDTO.getActivityStatus());
		specimen.setAvailableQuantity(specimenDTO.getAvailableQuantity());
		specimen.setBarcode(specimenDTO.getBarcode());
		specimen.setBiohazardCollection(getBiohazardCollection(specimenDTO.getBioHazards()));
		specimen.setExternalIdentifierCollection(getExterIdentifierColl(specimenDTO.getExternalIdentifiers(),specimen));
		specimen.setCollectionStatus(specimenDTO.getCollectionStatus());
		specimen.setComment(specimenDTO.getComments());
		specimen.setConcentrationInMicrogramPerMicroliter(specimenDTO.getConcentration());
		specimen.setCreatedOn(specimenDTO.getCreatedDate());
		specimen.setInitialQuantity(specimen.getInitialQuantity());
		specimen.setIsAvailable(specimenDTO.isAvailable());
		specimen.setLabel(specimenDTO.getLabel());
		specimen.setLineage(specimenDTO.getLineage());
//		specimen.setParentSpecimen(new Specimen().setId(specimenDTO.getParentSpecimenId()));
		specimen.setPathologicalStatus(specimenDTO.getPathologicalStatus());
		specimen.setSpecimenClass(specimenDTO.getClassName());
		specimen.setSpecimenType(specimenDTO.getType());
		specimen.setTissueSide(specimenDTO.getTissueSide());
		specimen.setTissueSite(specimenDTO.getTissueSite());
		SpecimenPosition position = new SpecimenPosition();
		position.setPositionDimensionOneString((specimenDTO.getPos1()));
		position.setPositionDimensionTwoString((specimenDTO.getPos2()));
		StorageContainer container = new StorageContainer();
		container.setId(specimenDTO.getContainerId());
		container.setName(specimenDTO.getContainerName());
		position.setStorageContainer(container);
		position.setSpecimen(specimen);
		specimen.setSpecimenPosition(position);
		return specimen;
	}

	private Collection getExterIdentifierColl(Collection<ExternalIdentifierDTO> externalIdentifiers, Specimen specimen)
	{
		Collection<ExternalIdentifier> collection = new ArrayList<ExternalIdentifier>();
		for (ExternalIdentifierDTO externalIdentifierDTO : externalIdentifiers)
		{
			collection.add(getExternalIdentifierDomainObjFromDTO(specimen, externalIdentifierDTO));
		}
		return collection;
	}

	private Collection<Biohazard> getBiohazardCollection(Collection<BiohazardDTO> bioHazards)
	{
		Collection<Biohazard> collection = new ArrayList<Biohazard>();
		for (BiohazardDTO biohazardDTO : bioHazards)
		{
			collection.add(getBiohazardDomainObjFromDTO(biohazardDTO));
		}
		
		return collection;
	}

	public int getCollectedChildSpecimenCountByLabel(Long id, HibernateDAO hibernateDao) throws ParseException, ApplicationException,
            CloneNotSupportedException
    {

                Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
                substParams.put("0",
                        new NamedQueryParam(DBTypes.LONG, id));
                substParams.put("1",
                        new NamedQueryParam(DBTypes.STRING, Constants.COLLECTION_STATUS_COLLECTED));
                
                final List list = hibernateDao.executeNamedQuery(
                        "getChildSpecimenCountByLabel", substParams);
                int count = 0;
                if (!list.isEmpty())
                {
                    count = (Integer)list.get(0);
                }
                return count;
    }


}
