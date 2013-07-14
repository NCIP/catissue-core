
package krishagni.catissueplus.bizlogic;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import krishagni.catissueplus.Exception.CatissueException;
import krishagni.catissueplus.Exception.SpecimenErrorCodeEnum;
import krishagni.catissueplus.dto.BiohazardDTO;
import krishagni.catissueplus.dto.ExternalIdentifierDTO;
import krishagni.catissueplus.dto.SpecimenDTO;

import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.User;
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
import edu.wustl.common.bizlogic.IActivityStatus;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.condition.NotEqualClause;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;

public class SpecimenBizLogic
{

	private static final Logger LOGGER = Logger
			.getCommonLogger(SpecimenBizLogic.class);

	/**
	 * This will update the specimen object in the database
	 * @param specimenDTO
	 * @param sessionDataBean
	 * @return 
	 * @throws BizLogicException
	 */
	public SpecimenDTO updateSpecimen(HibernateDAO hibernateDao,
			SpecimenDTO specimenDTO, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		Specimen oldSpecimenObj = null;
		try
		{
			//object retrieval for auditing purpose
			oldSpecimenObj = (Specimen) hibernateDao.retrieveById(
					Specimen.class.getName(), specimenDTO.getId());
			if (!isAuthorizedForSpecimenProcessing(hibernateDao, specimenDTO,
					sessionDataBean))
			{
				throw AppUtility.getUserNotAuthorizedException(
						"SPECIMEN_PROCESSING", "", oldSpecimenObj.getClass()
								.getSimpleName());
			}
			//updating the object with DTO
			getUpdatedSpecimen(oldSpecimenObj, specimenDTO, sessionDataBean,
					hibernateDao);

			if (Variables.isSpecimenLabelGeneratorAvl
					|| Variables.isTemplateBasedLblGeneratorAvl)
			{
				generateLabel(oldSpecimenObj);
			}
			if (Variables.isSpecimenBarcodeGeneratorAvl)
			{
				generateBarCode(oldSpecimenObj);
			}
			validateSpecimen(oldSpecimenObj, hibernateDao, oldSpecimenObj);
			//updating the specimen in database
			hibernateDao.update(oldSpecimenObj);

		}
		catch (final DAOException daoExp)
		{
			LOGGER.error(daoExp.getMessage(), daoExp);
			throw this.getBizLogicException(daoExp, daoExp.getErrorKeyName(),
					daoExp.getMsgValues());
		}
		catch (ParseException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, null, null);
		}
		catch (ApplicationException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(),
					e.getMsgValues());
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
				&& specimen.getCollectionStatus().equals(
						Constants.COLLECTION_STATUS_COLLECTED);
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

		if ((Variables.isSpecimenLabelGeneratorAvl || Variables.isTemplateBasedLblGeneratorAvl)
				&& isStatusCollected(specimen))
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
				throw this.getBizLogicException(e, "errors.item",
						e.getMessage());
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
				&& (specimen.getBarcode() == null || "".equals(specimen
						.getBarcode())))
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
	private void getUpdatedSpecimen(Specimen oldSpecimenObj,
			SpecimenDTO specimenDTO, SessionDataBean sessionDataBean, DAO dao)
			throws ParseException, ApplicationException,
			CloneNotSupportedException
	{
		if (!Validator.isEmpty(specimenDTO.getActivityStatus()))
			oldSpecimenObj.setActivityStatus(specimenDTO.getActivityStatus());

		if (specimenDTO.getCreatedDate() != null)
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
			oldSpecimenObj.setCollectionStatus(specimenDTO
					.getCollectionStatus());
			oldSpecimenObj.setIsAvailable(true);

			if (specimenDTO.getQuantity() != null)
			{
				oldSpecimenObj.setAvailableQuantity(specimenDTO.getQuantity());
			}
			else
			{
				oldSpecimenObj.setAvailableQuantity(oldSpecimenObj
						.getInitialQuantity());
			}

			this.setSpecimenEvents(oldSpecimenObj, sessionDataBean);
		}
		else
		{
			if (!Validator.isEmpty(specimenDTO.getCollectionStatus()))
				oldSpecimenObj.setCollectionStatus(specimenDTO
						.getCollectionStatus());

			if (specimenDTO.getAvailableQuantity() != null)
				oldSpecimenObj.setAvailableQuantity(specimenDTO
						.getAvailableQuantity());

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
			oldSpecimenObj.setPathologicalStatus(specimenDTO
					.getPathologicalStatus());

		if (!Validator.isEmpty(specimenDTO.getTissueSide()))
			oldSpecimenObj.getSpecimenCharacteristics().setTissueSide(
					specimenDTO.getTissueSide());

		if (!Validator.isEmpty(specimenDTO.getTissueSite()))
			oldSpecimenObj.getSpecimenCharacteristics().setTissueSite(
					specimenDTO.getTissueSite());

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
			if (specimenDTO.getPos1() != null
					&& !"".equals(specimenDTO.getPos1().trim())
					&& specimenDTO.getPos2() != null
					&& !"".equals(specimenDTO.getPos2().trim()))
			{
				int toPos1Int = StorageContainerUtil
						.convertSpecimenPositionsToInteger(
								specimenDTO.getContainerName(), 1,
								specimenDTO.getPos1());
				int toPos2Int = StorageContainerUtil
						.convertSpecimenPositionsToInteger(
								specimenDTO.getContainerName(), 2,
								specimenDTO.getPos2());
				position.setPositionDimensionOne(toPos1Int);
				position.setPositionDimensionTwo(toPos2Int);
				position.setPositionDimensionOneString(specimenDTO.getPos1());
				position.setPositionDimensionTwoString(specimenDTO.getPos2());
				StorageContainer container = new StorageContainer();
				Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
				substParams.put("0", new NamedQueryParam(DBTypes.STRING,
						specimenDTO.getContainerName()));

				final List list = ((HibernateDAO) dao).executeNamedQuery(
						"getStorageContainerIdByContainerName", substParams);

				if (!list.isEmpty())
				{
					container.setId((Long) list.get(0));
				}
				else
				{
					throw this
							.getBizLogicException(
									null,
									"errors.invalid",
									ApplicationProperties
											.getValue("array.positionInStorageContainer"));
				}
				container.setName(specimenDTO.getContainerName());
				position.setStorageContainer(container);
				position.setSpecimen(oldSpecimenObj);
				oldSpecimenObj.setSpecimenPosition(position);
			}

		}
		if (Constants.MOLECULAR.equals(oldSpecimenObj.getClassName())
				&& specimenDTO.getConcentration() != null)
		{
			oldSpecimenObj.setConcentrationInMicrogramPerMicroliter(specimenDTO
					.getConcentration());
		}
		oldSpecimenObj.setExternalIdentifierCollection(getExternalIdentifiers(
				oldSpecimenObj, specimenDTO));
		oldSpecimenObj.setBiohazardCollection(getBiohazards(oldSpecimenObj,
				specimenDTO));
	}

	/**
	 * Returns ExternalIdentifier Collection to be set in old specimen object.
	 * @param oldSpecimenObj
	 * @param specimenDTO
	 * @return
	 */
	private Collection<ExternalIdentifier> getExternalIdentifiers(
			Specimen oldSpecimenObj, SpecimenDTO specimenDTO)
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
				externalIdentifierCollection
						.add(getExternalIdentifierDomainObjFromDTO(
								oldSpecimenObj, externalIdentifierDTO));
			}
			else if (externalIdentifierDTO.getStatus().equalsIgnoreCase("EDIT"))
			{
				for (ExternalIdentifier externalIdentifier : externalIdentifierCollection)
				{
					if (externalIdentifier.equals(externalIdentifierDTO))
					{
						externalIdentifier.setName(externalIdentifierDTO
								.getName());
						externalIdentifier.setValue(externalIdentifierDTO
								.getValue());
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
	private ExternalIdentifier getExternalIdentifierDomainObjFromDTO(
			Specimen oldSpecimenObj, ExternalIdentifierDTO externalIdentifierDTO)
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
	private Collection<Biohazard> getBiohazards(Specimen oldSpecimenObj,
			SpecimenDTO specimenDTO)
	{
		Collection<Biohazard> biohazardCollection = oldSpecimenObj
				.getBiohazardCollection();

		Collection<BiohazardDTO> biohazardDTOCollection = specimenDTO
				.getBioHazards();

		for (BiohazardDTO biohazardDTO : biohazardDTOCollection)
		{
			if (!biohazardCollection.contains(biohazardDTO))
			{
				biohazardCollection
						.add(getBiohazardDomainObjFromDTO(biohazardDTO));
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

	private BizLogicException getBizLogicException(Exception exception,
			String key, String logMessage)
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
	private void setSpecimenEvents(Specimen specimen,
			SessionDataBean sessionDataBean) throws CloneNotSupportedException
	{
		final Specimen parentSpecimen = (Specimen) specimen.getParentSpecimen();
		if (specimen.getParentSpecimen() == null)
		{
			if(specimen.getId() == null)
			{
				specimen.setPropogatingSpecimenEventCollection(specimen
						.getSpecimenCollectionGroup().getSpecimenEventParametersCollection(),
						sessionDataBean.getUserId(), specimen);
			}
			else
			{
				specimen.setPropogatingSpecimenEventCollection(specimen
					.getSpecimenRequirement().getSpecimenEventCollection(),
					sessionDataBean.getUserId(), specimen);
			}
		}
		else
		{
			specimen.setSpecimenEventCollection(this
					.populateDeriveSpecimenEventCollection(parentSpecimen,
							specimen));
		}
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
			Specimen parentSpecimen, Specimen deriveSpecimen)
			throws CloneNotSupportedException
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
	 * @param oldSpecimenObj 
	 * @param dao 
	 * @param specimenDTO
	 * @throws ApplicationException 
	 */
	private void validateSpecimen(Specimen specimen, HibernateDAO hibernateDao,
			Specimen oldSpecimenObj) throws ApplicationException
	{
		final List specimenClassList = AppUtility.getSpecimenClassList();
		final String specimenClass = specimen.getClassName();
		if (!Validator.isEnumeratedValue(specimenClassList, specimenClass))
		{
			throw this.getBizLogicException(null, "protocol.class.errMsg", "");
		}

		if (!Validator.isEnumeratedValue(
				AppUtility.getSpecimenTypes(specimenClass),
				specimen.getSpecimenType()))
		{
			throw this.getBizLogicException(null, "protocol.type.errMsg", "");
		}
		if (specimen.getCollectionStatus().equalsIgnoreCase(
				Constants.SPECIMEN_COLLECTED)
				&& validateFieldValue(specimen.getLabel()))
		{
			final String message = ApplicationProperties
					.getValue("specimen.label");
			throw this.getBizLogicException(null, "errors.item.required",
					message);
		}
		if (specimen.getAvailableQuantity().compareTo(
				specimen.getInitialQuantity()) > 0)
		{
			final String quantityString = ApplicationProperties
					.getValue("specimen.availableQuantity");
			throw this.getBizLogicException(null, "errors.availablequantity",
					quantityString);
		}

		if (specimen.getSpecimenPosition() != null)
		{
			StorageContainerBizlogic containerForSpecimenBizLogic = new StorageContainerBizlogic();
			SpecimenPosition specimenPosition = specimen.getSpecimenPosition();
			if (specimen.getId() == null
					|| (oldSpecimenObj != null && Constants.COLLECTION_STATUS_PENDING
							.equals(oldSpecimenObj.getCollectionStatus())))
				if (!containerForSpecimenBizLogic.isPositionAvailable(
						specimenPosition.getStorageContainer().getName(),
						specimenPosition.getPositionDimensionOne(),
						specimenPosition.getPositionDimensionTwo(),
						hibernateDao))
				{
					throw this.getBizLogicException(null,
							"shipment.samePositionForSpecimens", "");
				}
		}

		checkDuplicateSpecimenFields(specimen, hibernateDao);
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
	 * Checks duplicate specimen fields.
	 *
	 * @param specimen Specimen
	 * @param hibernateDao DAO object
	 * @throws DAOException 
	 *
	 * @throws BizLogicException Database exception
	 */
	private void checkDuplicateSpecimenFields(Specimen specimen,
			HibernateDAO hibernateDao) throws DAOException, BizLogicException
	{
		List list = null;
		if (specimen.getLabel() != null)
		{
			list = hibernateDao.retrieve(Specimen.class.getName(), "label",
					specimen.getLabel());
			for (Object object : list)
			{
				final Specimen specimenObject = (Specimen) object;
				if (!specimenObject.getId().equals(specimen.getId()))
				{
					throw this.getBizLogicException(null,
							"label.already.exits", specimen.getLabel());
				}
			}
		}
		if (specimen.getBarcode() != null)
		{
			list = hibernateDao.retrieve(Specimen.class.getName(), "barcode",
					specimen.getBarcode());
			for (Object object : list)
			{
				final Specimen specimenObject = (Specimen) object;
				if (!specimenObject.getId().equals(specimen.getId()))
				{
					throw this.getBizLogicException(null,
							"barcode.already.exits", specimen.getBarcode());
				}
			}
		}
	}

	public SpecimenDTO getDTO(Specimen specimen) throws BizLogicException
	{
		SpecimenDTO specimenDTO = new SpecimenDTO();
		specimenDTO.setId(specimen.getId());
		specimenDTO.setLabel(specimen.getLabel());
		specimenDTO.setActivityStatus(specimen.getActivityStatus());
		specimenDTO.setAvailable(specimen.getIsAvailable());
		specimenDTO.setAvailableQuantity(specimen.getAvailableQuantity());
		specimenDTO.setBarcode(specimen.getBarcode());
		specimenDTO.setClassName(specimen.getClassName());
		specimenDTO.setCollectionStatus(specimen.getCollectionStatus());
		specimenDTO.setComments(specimen.getComment());
		specimenDTO.setConcentration(specimen
				.getConcentrationInMicrogramPerMicroliter());
		specimenDTO.setCreatedDate(specimen.getCreatedOn());
		specimenDTO.setLineage(specimen.getLineage());
		if (specimen.getParentSpecimen() != null)
		{
			specimenDTO.setParentSpecimenId(specimen.getParentSpecimen()
					.getId());
			specimenDTO.setParentSpecimenName(specimen.getParentSpecimen()
					.getLabel());
		}
		specimenDTO.setPathologicalStatus(specimen.getPathologicalStatus());
		specimenDTO.setQuantity(specimen.getInitialQuantity());
		if (specimen.getSpecimenCollectionGroup() != null)
		{
			specimenDTO.setSpecimenCollectionGroupId(specimen
					.getSpecimenCollectionGroup().getId());
			specimenDTO.setSpecimenCollectionGroupName(specimen
					.getSpecimenCollectionGroup().getName());
		}
		specimenDTO.setTissueSide(specimen.getSpecimenCharacteristics()
				.getTissueSide());
		specimenDTO.setTissueSite(specimen.getSpecimenCharacteristics()
				.getTissueSite());
		specimenDTO.setType(specimen.getSpecimenType());
		if (specimen.getSpecimenPosition() != null)
		{
			specimenDTO.setPos1(specimen.getSpecimenPosition()
					.getPositionDimensionOneString());
			specimenDTO.setPos2(specimen.getSpecimenPosition()
					.getPositionDimensionTwoString());
			specimenDTO.setContainerId(specimen.getSpecimenPosition()
					.getStorageContainer().getId());
			specimenDTO.setContainerName(specimen.getSpecimenPosition()
					.getStorageContainer().getName());
			specimenDTO.setIsVirtual(Boolean.FALSE);
		}
		else
		{
			specimenDTO.setIsVirtual(Boolean.TRUE);
		}
		specimenDTO.setConcentration(specimen
				.getConcentrationInMicrogramPerMicroliter());
		specimenDTO
				.setExternalIdentifiers(getExternalIdentifierDTOCollection(specimen));
		specimenDTO.setBioHazards(getBiohazardDTOCollection(specimen));

		return specimenDTO;
	}

	/**
	 * Populate ExternalIdentifierDTOCollection from Specimen.
	 * @param specimen
	 * @return
	 */
	private Collection<ExternalIdentifierDTO> getExternalIdentifierDTOCollection(
			Specimen specimen)
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

	public boolean isSpecimenLabelGeneratorAvl(Long identifier,
			HibernateDAO hibernateDao) throws BizLogicException
	{
		boolean generateLabel = Variables.isSpecimenLabelGeneratorAvl;

		if (Variables.isTemplateBasedLblGeneratorAvl)
		{
			try
			{
				Specimen specimen = (Specimen) hibernateDao.retrieveById(
						Specimen.class.getName(), identifier);

				final CollectionProtocolRegistration collectionProtocolRegistration = specimen
						.getSpecimenCollectionGroup()
						.getCollectionProtocolRegistration();

				String parentLabelFormat = collectionProtocolRegistration
						.getCollectionProtocol().getSpecimenLabelFormat();

				String derivativeLabelFormat = collectionProtocolRegistration
						.getCollectionProtocol().getDerivativeLabelFormat();

				String aliquotLabelFormat = collectionProtocolRegistration
						.getCollectionProtocol().getAliquotLabelFormat();

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
				throw this.getBizLogicException(e, e.getErrorKeyName(),
						e.getMsgValues());
			}
		}
		return generateLabel;
	}

	public List<SpecimenDTO> insert(List<SpecimenDTO> specimenDTOList,
			HibernateDAO hibernateDao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		List<SpecimenDTO> specimenDTOs = new ArrayList<SpecimenDTO>();
		try
		{
			Specimen parentSpecimen = null;
			if (Constants.ALIQUOT.equals(specimenDTOList.get(0).getLineage())
					|| Constants.DERIVED_SPECIMEN.equals(specimenDTOList.get(0)
							.getLineage()))
			{
				parentSpecimen = getParent(specimenDTOList.get(0)
						.getParentSpecimenName(), hibernateDao);
			}
			for (SpecimenDTO specimenDTO : specimenDTOList)
			{
				if (!isAuthorizedForSpecimenProcessing(hibernateDao,
						specimenDTO, sessionDataBean))
				{
					throw AppUtility.getUserNotAuthorizedException(
							"SPECIMEN_PROCESSING", "",
							Specimen.class.getSimpleName());
				}
				Specimen specimen = getSpecimen(specimenDTO, hibernateDao,
						sessionDataBean, parentSpecimen);
				insertSpecimen(hibernateDao, specimen);
				if (Constants.ALIQUOT.equals(specimenDTO.getLineage())
						|| Constants.DERIVED_SPECIMEN.equals(specimenDTO
								.getLineage()))
				{
					parentSpecimen.getChildSpecimenCollection().add(specimen);
				}
				specimenDTOs.add(getDTO(specimen));
			}
		}
		catch (ApplicationException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(),
					e.getMsgValues());
		}
		catch (CloneNotSupportedException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, "", "");
		}
		return specimenDTOs;
	}

	public SpecimenDTO insert(SpecimenDTO specimenDTO,
			HibernateDAO hibernateDao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		Specimen specimen = new Specimen();
		try
		{
			if (!isAuthorizedForSpecimenProcessing(hibernateDao, specimenDTO,
					sessionDataBean))
			{
				throw AppUtility.getUserNotAuthorizedException(
						"SPECIMEN_PROCESSING", "", specimen.getClass()
								.getSimpleName());
			}
			//			Specimen parentSpecimen = getParent(specimenDTO.getParentSpecimenName(), hibernateDao);
			specimen = getSpecimen(specimenDTO, hibernateDao, sessionDataBean,
					null);
			insertSpecimen(hibernateDao, specimen);
		}
		catch (ApplicationException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(),
					e.getMsgValues());
		}
		catch (CloneNotSupportedException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, null, null);
		}
		return getDTO(specimen);
	}

	public Specimen getParent(String parentSpecimenLabel,
			HibernateDAO hibernateDao) throws DAOException, BizLogicException
	{
		Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
		substParams.put("0", new NamedQueryParam(DBTypes.STRING,
				parentSpecimenLabel));

		List specimenIdList = hibernateDao.executeNamedQuery("getSpecimenId",
				substParams);
		if (specimenIdList != null && specimenIdList.size() > 0)
		{
			return (Specimen) hibernateDao.retrieveById(
					Specimen.class.getName(),
					Long.valueOf(specimenIdList.get(0).toString()));
		}
		else
		{
			LOGGER.error("");
			throw new CatissueException(
					SpecimenErrorCodeEnum.NOT_FOUND.getDescription(),
					SpecimenErrorCodeEnum.NOT_FOUND.getCode());
		}

	}

	private Collection<ConsentTierStatus> setConsentTierStatus(
			Specimen specimen,
			Collection<ConsentTierStatus> consentTierStatusCollection)
	{
		Collection<ConsentTierStatus> consentTierStatusCollectionForSpecimen = null;
		if (consentTierStatusCollection != null)
		{
			consentTierStatusCollectionForSpecimen = new HashSet<ConsentTierStatus>();
			final Iterator<ConsentTierStatus> itr = consentTierStatusCollection
					.iterator();
			while (itr.hasNext())
			{
				final ConsentTierStatus conentTierStatus = (ConsentTierStatus) itr
						.next();
				final ConsentTierStatus consentTierStatusForSpecimen = new ConsentTierStatus();
				consentTierStatusForSpecimen.setStatus(conentTierStatus
						.getStatus());
				consentTierStatusForSpecimen.setConsentTier(conentTierStatus
						.getConsentTier());
				consentTierStatusCollectionForSpecimen
						.add(consentTierStatusForSpecimen);
			}
		}
		return consentTierStatusCollectionForSpecimen;
	}

	private void insertSpecimen(HibernateDAO hibernateDao, Specimen specimen)
			throws BizLogicException, ApplicationException, DAOException
	{
		generateLabel(specimen);
		generateBarCode(specimen);
		validateSpecimen(specimen, hibernateDao, null);
		hibernateDao.insert(specimen);
	}

	private boolean isAuthorizedForSpecimenProcessing(
			HibernateDAO hibernateDao, SpecimenDTO specimenDTO,
			SessionDataBean sessionDataBean) throws DAOException, SMException
	{
		boolean isAuthorize = sessionDataBean.isAdmin();
		if (!isAuthorize)
		{
			String specimenName;
			Long specimenId;
			if (Constants.ALIQUOT.equals(specimenDTO.getLineage())
					|| Constants.DERIVED_SPECIMEN.equals(specimenDTO
							.getLineage()))
			{
				specimenName = specimenDTO.getParentSpecimenName();
				specimenId = specimenDTO.getParentSpecimenId();
			}
			else
			{
				specimenName = specimenDTO.getLabel();
				specimenId = specimenDTO.getId();
			}
			Long siteId = getAssociatedSiteId(hibernateDao, specimenName,
					specimenId);
			Long cpId = getCPId(hibernateDao, specimenName, specimenId);
			isAuthorize = chkAuthorizationForCPnSite(siteId, cpId,
					sessionDataBean.getUserName());
		}
		return isAuthorize;
	}

	private Long getCPId(HibernateDAO hibernateDao, String parentSpecimenName,
			Long parentSpecimenId) throws DAOException
	{
		Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
		substParams.put("0",
				new NamedQueryParam(DBTypes.LONG, parentSpecimenId));

		List siteList = hibernateDao.executeNamedQuery("getCPID", substParams);
		if (siteList != null && siteList.size() > 0)
		{
			return Long.valueOf(siteList.get(0).toString());
		}
		return null;
	}

	private Boolean chkAuthorizationForCPnSite(Long siteId, Long cpId,
			String userName) throws SMException
	{
		final PrivilegeCache privilegeCache = PrivilegeManager.getInstance()
				.getPrivilegeCache(userName);
		boolean isAuthorized = Boolean.FALSE;
		if (siteId != null)
		{
			String siteProtectionEleName = "SITE_" + siteId + "_All_CP";
			isAuthorized = privilegeCache.hasPrivilege(siteProtectionEleName,
					"SPECIMEN_PROCESSING");
		}
		if (!isAuthorized)
		{
			String cpProtectionEleName = CollectionProtocol.class.getName()
					+ "_" + cpId;

			// Checking whether the logged in user has the required
			// privilege on the given protection element
			isAuthorized = privilegeCache.hasPrivilege(cpProtectionEleName,
					"SPECIMEN_PROCESSING");
		}
		return isAuthorized;
	}

	private Specimen getSpecimen(SpecimenDTO specimenDTO,
			HibernateDAO hibernateDao, SessionDataBean sessionDataBean,
			Specimen parentSpecimen) throws BizLogicException, DAOException,
			CloneNotSupportedException
	{
		Specimen specimen = new Specimen();
		specimen.setActivityStatus(specimenDTO.getActivityStatus());
		specimen.setAvailableQuantity(specimenDTO.getAvailableQuantity());
		specimen.setBarcode(specimenDTO.getBarcode());
		specimen.setExternalIdentifierCollection(getExterIdentifierColl(
				specimenDTO.getExternalIdentifiers(), specimen));
		specimen.setCollectionStatus(specimenDTO.getCollectionStatus());
		specimen.setComment(specimenDTO.getComments());
		specimen.setConcentrationInMicrogramPerMicroliter(specimenDTO
				.getConcentration());
		specimen.setCreatedOn(specimenDTO.getCreatedDate());
		specimen.setInitialQuantity(specimenDTO.getQuantity());
		specimen.setIsAvailable(Boolean.TRUE);
		specimen.setLabel(specimenDTO.getLabel());
		specimen.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		if (Constants.ALIQUOT.equals(specimenDTO.getLineage())
				|| Constants.DERIVED_SPECIMEN.equals(specimenDTO.getLineage()))
		{
			specimen.setLineage(specimenDTO.getLineage());
			parentSpecimen = parentSpecimen == null
					? getParent(specimenDTO.getParentSpecimenName(),
							hibernateDao) : parentSpecimen;
			if (parentSpecimen == null)
			{
				throw this.getBizLogicException(null,
						"errors.specimen.parentspecimen", "");
			}
			specimen.setSpecimenCharacteristics(parentSpecimen
					.getSpecimenCharacteristics());
			specimen.setPathologicalStatus(parentSpecimen
					.getPathologicalStatus());
			Collection<Biohazard> biohazards = new HashSet<Biohazard>();
			for (Biohazard biohazard : parentSpecimen.getBiohazardCollection())
			{
				biohazards.add(biohazard);
			}
			specimen.setBiohazardCollection(biohazards);
			setConsentTierStatus(specimen,
					parentSpecimen.getConsentTierStatusCollection());
			//specimen.setChildSpecimenCollection(parentSpecimen.getChildSpecimenCollection());
			specimen.setParentSpecimen(parentSpecimen);
			specimen.setSpecimenCollectionGroup(parentSpecimen
					.getSpecimenCollectionGroup());
			setSpecimenEvents(specimen, sessionDataBean);
			SpecimenCollectionGroup collectionGroup = new SpecimenCollectionGroup();
			collectionGroup.setId(parentSpecimen.getSpecimenCollectionGroup()
					.getId());
			specimen.setSpecimenCollectionGroup(collectionGroup);

		}
		else
		{

			specimen.setLineage(Constants.NEW_SPECIMEN);
			SpecimenCollectionGroup collectionGroup = getSCG(
					specimenDTO.getSpecimenCollectionGroupId(), hibernateDao);
			specimen.setSpecimenCollectionGroup(collectionGroup);
			specimen.setPathologicalStatus(specimenDTO.getPathologicalStatus());
			SpecimenCharacteristics specimenCharacteristics = new SpecimenCharacteristics();
			specimenCharacteristics.setTissueSide(specimenDTO.getTissueSide());
			specimenCharacteristics.setTissueSite(specimenDTO.getTissueSite());
			specimen.setSpecimenCharacteristics(specimenCharacteristics);
			specimen.setBiohazardCollection(getBiohazardCollection(specimenDTO
					.getBioHazards()));
		}
		specimen.setSpecimenClass(specimenDTO.getClassName());
		specimen.setSpecimenType(specimenDTO.getType());
		SpecimenPosition position = new SpecimenPosition();
		if (Validator.isEmpty(specimenDTO.getPos1()))
		{
			specimen.setSpecimenPosition(null);
		}
		else
		{
			position.setPositionDimensionOneString((specimenDTO.getPos1()));
			position.setPositionDimensionTwoString((specimenDTO.getPos2()));
			try
			{
				position.setPositionDimensionOne((StorageContainerUtil
						.convertPositionsToIntegerUsingContId(specimenDTO
								.getContainerId().toString(), 1, specimenDTO
								.getPos1())));
				position.setPositionDimensionTwo((StorageContainerUtil
						.convertPositionsToIntegerUsingContId(specimenDTO
								.getContainerId().toString(), 2, specimenDTO
								.getPos2())));
			}
			catch (ApplicationException e)
			{
				LOGGER.error(e.getMessage(), e);
				throw this.getBizLogicException(e, e.getErrorKeyName(),
						e.getMsgValues());
			}
			StorageContainer container = new StorageContainer();
			container.setId(specimenDTO.getContainerId());
			container.setName(specimenDTO.getContainerName());
			position.setStorageContainer(container);
			position.setSpecimen(specimen);
			specimen.setSpecimenPosition(position);
		}
		//		SpecimenCollectionGroup collectionGroup = new SpecimenCollectionGroup();
		//		collectionGroup.setId(specimenDTO.getSpecimenCollectionGroupId());
		//		specimen.setSpecimenCollectionGroup(collectionGroup);
		setSpecimenEvents(specimen, sessionDataBean);
		return specimen;
	}

	private SpecimenCollectionGroup getSCG(Long specimenCollectionGroupId,
			HibernateDAO hibernateDao) throws DAOException
	{
		SpecimenCollectionGroup collectionGroup = new SpecimenCollectionGroup();
		Object obj = hibernateDao.retrieveById(
				SpecimenCollectionGroup.class.getName(),
				specimenCollectionGroupId);
		if (obj == null)
		{
			throw new CatissueException(SpecimenErrorCodeEnum.NOT_FOUND.getDescription(),SpecimenErrorCodeEnum.NOT_FOUND.getCode());
		}
		return (SpecimenCollectionGroup) obj;
	}

	private Collection getExterIdentifierColl(
			Collection<ExternalIdentifierDTO> externalIdentifiers,
			Specimen specimen)
	{
		Collection<ExternalIdentifier> collection = new HashSet<ExternalIdentifier>();
		if (externalIdentifiers != null)
		{
			for (ExternalIdentifierDTO externalIdentifierDTO : externalIdentifiers)
			{
				collection.add(getExternalIdentifierDomainObjFromDTO(specimen,
						externalIdentifierDTO));
			}
		}
		if (collection.isEmpty())
		{
			collection.add(new ExternalIdentifier());
		}
		return collection;
	}

	private Collection<Biohazard> getBiohazardCollection(
			Collection<BiohazardDTO> bioHazards)
	{
		Collection<Biohazard> collection = new HashSet<Biohazard>();
		for (BiohazardDTO biohazardDTO : bioHazards)
		{
			collection.add(getBiohazardDomainObjFromDTO(biohazardDTO));
		}

		return collection;
	}

	private Long getAssociatedSiteId(HibernateDAO hibernateDAO,
			String specimenLabel, Long specimenId) throws DAOException
	{
		Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
		substParams.put("0", new NamedQueryParam(DBTypes.LONG, specimenId));
		substParams
				.put("1", new NamedQueryParam(DBTypes.STRING, specimenLabel));
		substParams.put("2", new NamedQueryParam(DBTypes.STRING,
				Constants.ACTIVITY_STATUS_ACTIVE));

		List siteList = hibernateDAO.executeNamedQuery(
				"getSiteIdFromContainer", substParams);
		if (siteList != null && siteList.size() > 0)
		{
			return Long.valueOf(siteList.get(0).toString());
		}
		return null;
	}

	public HashMap<String, String> getParentDetails(String parentLabel,
			String barcode, HibernateDAO hibernateDao) throws DAOException
	{
		Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
		substParams.put("0", new NamedQueryParam(DBTypes.STRING, parentLabel));
		substParams.put("1", new NamedQueryParam(DBTypes.STRING, barcode));
		substParams.put("2", new NamedQueryParam(DBTypes.STRING,
				Constants.ACTIVITY_STATUS_ACTIVE));
		final List result = hibernateDao.executeNamedQuery("getSCGnCPIDByLorB",
				substParams);
		SpecimenDTO specimenDTO = new SpecimenDTO();
		if (result != null && !result.isEmpty())
		{
			Object[] obj = (Object[]) result.get(0);
			specimenDTO.setLabel(parentLabel);
			specimenDTO.setId(Long.valueOf(obj[0].toString()));
			specimenDTO.setSpecimenCollectionGroupId(Long.valueOf(obj[1]
					.toString()));
		}
		return null;
	}

	public String reduceQuantity(Double quantityReducedBy, Long specimenId,
			HibernateDAO dao) throws BizLogicException, DAOException
	{

		ColumnValueBean columnValueBean = new ColumnValueBean(specimenId);
		columnValueBean.setColumnName("id");
		List specimens = dao
				.retrieve(Specimen.class.getName(), columnValueBean);

		if (specimens.isEmpty())
		{
			return ApplicationProperties
					.getValue("specimen.closed.unavailable");
		}

		Specimen specimen = (Specimen) specimens.get(0);

		if (!Constants.ACTIVITY_STATUS_ACTIVE.equalsIgnoreCase(specimen
				.getActivityStatus()))
		{
			return ApplicationProperties
					.getValue("specimen.closed.unavailable");
		}
		Double previousQuantity = specimen.getAvailableQuantity();
		Double remainingQuantity = previousQuantity.doubleValue()
				- quantityReducedBy;
		specimen.setAvailableQuantity(remainingQuantity);

		int remainingQuantityMoreThenZero = remainingQuantity.compareTo(0D);
		if (remainingQuantityMoreThenZero == -1)
		{
			List<String> parameters = new ArrayList<String>();
			parameters.add(previousQuantity.toString());
			return ApplicationProperties.getValue("requested.quantity.exceeds",
					parameters);
		}
		else if (remainingQuantity.compareTo(0D) == 0)
		{
			specimen.setIsAvailable(false);
		}
		else
		{
			specimen.setIsAvailable(true);
		}

		dao.update(specimen);
		return Constants.SUCCESS;
	}

	public void disposeSpecimen(HibernateDAO hibernateDao,
			SessionDataBean sessionDataBean, Specimen specimen,
			String specimenDisposalReason) throws DAOException,
			BizLogicException
	{
		final DisposalEventParameters disposalEvent = this.createDisposeEvent(
				sessionDataBean, specimen, specimenDisposalReason);
		checkStatusAndGetUserId(disposalEvent, hibernateDao);
		disposeEvent((DisposalEventParameters) disposalEvent, specimen,
				hibernateDao);
		specimen.getSpecimenEventCollection().add(disposalEvent);
		hibernateDao.insert(disposalEvent);

	}

	private void checkStatusAndGetUserId(
			SpecimenEventParameters specimenEventParameter,
			HibernateDAO hibernateDao) throws DAOException, BizLogicException
	{
		List list = null;
		String message = "";
		if (specimenEventParameter.getUser().getId() != null)
		{

			Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
			params.put("0", new NamedQueryParam(DBTypes.LONG,
					specimenEventParameter.getUser().getId()));

			list = hibernateDao.executeNamedQuery("eventUserListById", params);
			message = ApplicationProperties.getValue("app.UserID");
		}
		else if (specimenEventParameter.getUser().getLoginName() != null
				|| specimenEventParameter.getUser().getLoginName().length() > 0)
		{
			Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
			params.put("0", new NamedQueryParam(DBTypes.LONG,
					specimenEventParameter.getUser().getLoginName()));

			list = hibernateDao.executeNamedQuery("eventUserListById", params);

			message = ApplicationProperties.getValue("user.loginName");
		}
		if (list != null && !list.isEmpty())
		{
			Object[] object = (Object[]) list.get(0);
			final User user = new User();
			user.setId((Long) object[0]);
			user.setActivityStatus((String) object[1]);
			// check for closed User
			this.checkStatus(hibernateDao, user, "User");
			specimenEventParameter.setUser(user);
		}
		else
		{
			throw this.getBizLogicException(null, "errors.item.forboformat",
					message);
		}

	}

	private void disposeEvent(DisposalEventParameters disposalEventParameters,
			Specimen specimen, HibernateDAO hibernateDao)
			throws BizLogicException, DAOException
	{
		if (disposalEventParameters.getActivityStatus().equals(
				Status.ACTIVITY_STATUS_DISABLED.getStatus()))
		{
			this.disableSubSpecimens(hibernateDao, specimen.getId().toString());
		}

		specimen = (Specimen) hibernateDao.retrieveById(
				Specimen.class.getName(), specimen.getId());
		final SpecimenPosition prevPosition = specimen.getSpecimenPosition();
		specimen.setSpecimenPosition(null);
		specimen.setIsAvailable(Boolean.FALSE);
		specimen.setActivityStatus(disposalEventParameters.getActivityStatus());
		hibernateDao.update(specimen);
		hibernateDao.delete(prevPosition);

	}

	private void disableSubSpecimens(HibernateDAO hibernateDao, String speID)
			throws BizLogicException, DAOException
	{
		final String sourceObjectName = Specimen.class.getName();
		final String[] selectColumnName = {edu.wustl.common.util.global.Constants.SYSTEM_IDENTIFIER};
		final QueryWhereClause queryWhereClause = new QueryWhereClause(
				sourceObjectName);
		queryWhereClause
				.addCondition(
						new EqualClause("parentSpecimen.id", Long
								.valueOf(speID)))
				.andOpr()
				.addCondition(
						new NotEqualClause(Status.ACTIVITY_STATUS.toString(),
								Status.ACTIVITY_STATUS_DISABLED.toString()));

		List listOfSpecimenIDs = hibernateDao.retrieve(sourceObjectName,
				selectColumnName, queryWhereClause);
		listOfSpecimenIDs = CommonUtilities.removeNull(listOfSpecimenIDs);

	}

	protected void checkStatus(HibernateDAO hibernateDao, IActivityStatus ado,
			String errorName) throws BizLogicException
	{
		if (ado != null)
		{
			Long identifier = ((AbstractDomainObject) ado).getId();
			if (identifier != null)
			{
				String className = ado.getClass().getName();
				String activityStatus = ado.getActivityStatus();
				if (activityStatus == null)
				{
					activityStatus = getActivityStatus(hibernateDao, className,
							identifier);
				}
				if (activityStatus.equals(Status.ACTIVITY_STATUS_CLOSED
						.toString()))
				{
					throw getBizLogicException(null, "error.object.closed",
							errorName);
				}
			}
		}
	}

	public String getActivityStatus(HibernateDAO hibernateDao,
			String sourceObjectName, Long indetifier) throws BizLogicException
	{
		String[] selectColumnName = {Status.ACTIVITY_STATUS.getStatus()};
		QueryWhereClause queryWhereClause = new QueryWhereClause(
				sourceObjectName);
		List<Object> list;
		try
		{
			queryWhereClause.addCondition(new EqualClause(
					Constants.SYSTEM_IDENTIFIER, indetifier.toString(),
					sourceObjectName));
			list = hibernateDao.retrieve(sourceObjectName, selectColumnName,
					queryWhereClause);
		}
		catch (DAOException daoEx)
		{
			LOGGER.debug(daoEx.getMessage(), daoEx);
			throw getBizLogicException(daoEx, daoEx.getErrorKeyName(),
					daoEx.getMsgValues());
		}
		String activityStatus = "";
		if (!list.isEmpty())
		{
			Object obj = list.get(0);
			LOGGER.debug("obj Class " + obj.getClass());
			//Object[] objArr = (String)obj
			activityStatus = (String) obj;
		}
		return activityStatus;
	}

	private DisposalEventParameters createDisposeEvent(
			SessionDataBean sessionDataBean, AbstractSpecimen specimen,
			String disposalReason)
	{
		final DisposalEventParameters disposalEvent = new DisposalEventParameters();
		disposalEvent.setSpecimen(specimen);
		disposalEvent.setReason(disposalReason);
		disposalEvent.setTimestamp(new Date(System.currentTimeMillis()));
		final User user = new User();
		user.setId(sessionDataBean.getUserId());
		disposalEvent.setUser(user);
		disposalEvent.setActivityStatus(Status.ACTIVITY_STATUS_CLOSED
				.toString());
		return disposalEvent;
	}

}