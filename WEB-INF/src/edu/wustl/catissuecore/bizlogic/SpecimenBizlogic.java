package edu.wustl.catissuecore.bizlogic;

import java.text.ParseException;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.dto.SpecimenDTO;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
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
	public void updateSpecimen(DAO dao,SpecimenDTO specimenDTO,SessionDataBean sessionDataBean)
	throws BizLogicException
	{
		try
		{
			//object retrieval for auditing purpose
			Specimen oldSpecimenObj = (Specimen)dao.retrieveById(Specimen.class.getName(), specimenDTO.getId());
			//updating the object with DTO
			getUpdatedSpecimen(oldSpecimenObj,specimenDTO);
			//updating the specimen in database
			dao.update(oldSpecimenObj);
			
		}
		catch (final DAOException daoExp)
		{
			LOGGER.error(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}  catch (ParseException e) 
		{
			LOGGER.error(e.getMessage(), e);
			throw this
			.getBizLogicException(e, null,null);
		}
	}

	
	/**
	 * Populating the retrieved specimen with DTO values
	 * @param oldSpecimenObj
	 * @param specimenDTO
	 * @throws ParseException
	 */
	private void getUpdatedSpecimen(Specimen oldSpecimenObj,SpecimenDTO specimenDTO) throws ParseException 
	{
		if(specimenDTO.getActivityStatus() != null) 
		oldSpecimenObj.setActivityStatus(specimenDTO.getActivityStatus());
		if(specimenDTO.getLabel() != null) 
			oldSpecimenObj.setLabel(specimenDTO.getLabel());
		if(specimenDTO.getBarcode() != null) 
			oldSpecimenObj.setBarcode(specimenDTO.getBarcode());
		if(specimenDTO.getClassName() != null) 
			oldSpecimenObj.setSpecimenClass(specimenDTO.getClassName());
		if(specimenDTO.getType() != null) 
			oldSpecimenObj.setSpecimenType(specimenDTO.getType());
		if(specimenDTO.getPathologicalStatus() != null) 
			oldSpecimenObj.setPathologicalStatus(specimenDTO.getPathologicalStatus());
		if(specimenDTO.getTissueSide() != null) 
			oldSpecimenObj.getSpecimenCharacteristics().setTissueSide(specimenDTO.getTissueSide());
		if(specimenDTO.getTissueSite() != null) 
			oldSpecimenObj.getSpecimenCharacteristics().setTissueSite(specimenDTO.getTissueSite());
		if(specimenDTO.getAvailableQuantity() != null)
			oldSpecimenObj.setAvailableQuantity(specimenDTO.getAvailableQuantity());
		if(specimenDTO.getQuantity() != null)
			oldSpecimenObj.setInitialQuantity(specimenDTO.getQuantity());
		if(specimenDTO.getComments() != null)
			oldSpecimenObj.setComment(specimenDTO.getComments());
//		if(specimenDTO.getCreatedDate() != null)
//			oldSpecimenObj.setCreatedOn(CommonUtilities.parseDate(specimenDTO.getCreatedDate(),
//				CommonServiceLocator.getInstance().getDatePattern()));
		oldSpecimenObj.setIsAvailable(specimenDTO.isAvailable());
		SpecimenPosition position = new SpecimenPosition();
		if(specimenDTO.getIsVirtual())
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
//		oldSpecimenObj.setCollectionStatus(specimenDTO.getCollectionStatus());
	}


	protected BizLogicException getBizLogicException(Exception exception, String key,
			String logMessage)
	{
		LOGGER.debug(logMessage);
		ErrorKey errorKey = ErrorKey.getErrorKey(key);
		return new BizLogicException(errorKey, exception, logMessage);
	}


}

