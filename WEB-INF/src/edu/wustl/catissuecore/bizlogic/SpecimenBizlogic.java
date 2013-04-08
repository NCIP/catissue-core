
package edu.wustl.catissuecore.bizlogic;

import java.text.ParseException;
import java.util.Collection;

import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.dto.BiohazardDTO;
import edu.wustl.catissuecore.dto.ExternalIdentifierDTO;
import edu.wustl.catissuecore.dto.SpecimenDTO;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

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
			getUpdatedSpecimen(oldSpecimenObj, specimenDTO);
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
	 * Populating the retrieved specimen with DTO values
	 * @param oldSpecimenObj
	 * @param specimenDTO
	 * @throws ParseException 
	 * @throws ApplicationException 
	 */
	private void getUpdatedSpecimen(Specimen oldSpecimenObj, SpecimenDTO specimenDTO)
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
		if (specimenDTO.getAvailableQuantity() != null)
			oldSpecimenObj.setAvailableQuantity(specimenDTO.getAvailableQuantity());
		if (specimenDTO.getQuantity() != null)
			oldSpecimenObj.setInitialQuantity(specimenDTO.getQuantity());
		if (specimenDTO.getComments() != null)
			oldSpecimenObj.setComment(specimenDTO.getComments());
		if (specimenDTO.getCollectionStatus() != null)
			oldSpecimenObj.setCollectionStatus(specimenDTO.getCollectionStatus());
		//		if(specimenDTO.getCreatedDate() != null)
		//			oldSpecimenObj.setCreatedOn(CommonUtilities.parseDate(specimenDTO.getCreatedDate(),
		//				CommonServiceLocator.getInstance().getDatePattern()));
		oldSpecimenObj.setIsAvailable(specimenDTO.isAvailable());
		SpecimenPosition position = new SpecimenPosition();
		if (specimenDTO.getIsVirtual())
		{
			position = null;
		}
		else
		{
			position.setPositionDimensionOneString(specimenDTO.getPos1());
			position.setPositionDimensionTwoString(specimenDTO.getPos2());
			StorageContainer container = new StorageContainer();
			container.setName(specimenDTO.getContainerName());
			position.setStorageContainer(container);
		}
		//		
		//		oldSpecimenObj.setSpecimenPosition(position);

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
			throws ApplicationException
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
			throws ApplicationException
	{
		//		DAO dao = AppUtility.openDAOSession(null);
		//
		//		Biohazard biohazard = (Biohazard) dao.retrieveById(Biohazard.class.getName(),
		//				biohazardDTO.getId());
		//
		//		AppUtility.closeDAOSession(dao);

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
