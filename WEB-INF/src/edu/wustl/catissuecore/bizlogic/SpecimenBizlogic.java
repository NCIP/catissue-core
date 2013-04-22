
package edu.wustl.catissuecore.bizlogic;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.Specimen;
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
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
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
			getUpdatedSpecimen(oldSpecimenObj, specimenDTO, dao);
			if (Variables.isSpecimenLabelGeneratorAvl)
			{
				generateLabel(oldSpecimenObj);
			}
			if (Variables.isSpecimenBarcodeGeneratorAvl)
			{
				generateBarCode(oldSpecimenObj);
			}
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
				throw this.getBizLogicException(e, "name.generator.exp", "");
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
	 * @throws ParseException 
	 * @throws ApplicationException 
	 */
	private void getUpdatedSpecimen(Specimen oldSpecimenObj, SpecimenDTO specimenDTO, DAO dao)
			throws ParseException, ApplicationException
	{
		if (specimenDTO.getActivityStatus() != null)
			oldSpecimenObj.setActivityStatus(specimenDTO.getActivityStatus());

		if (specimenDTO.getLabel() != null)
			oldSpecimenObj.setLabel(specimenDTO.getLabel());

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

		if (specimenDTO.getQuantity() != null)
			oldSpecimenObj.setInitialQuantity(specimenDTO.getQuantity());

		if (specimenDTO.getComments() != null)
			oldSpecimenObj.setComment(specimenDTO.getComments());

		if (specimenDTO.getCollectionStatus() != null
				&& specimenDTO.getCollectionStatus().equalsIgnoreCase(
						Constants.COLLECTION_STATUS_COLLECTED)
				&& oldSpecimenObj.getCollectionStatus().equalsIgnoreCase(
						Constants.COLLECTION_STATUS_PENDING))
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
				position.setPositionDimensionTwo(toPos1Int);
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

}
