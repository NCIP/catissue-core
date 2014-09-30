
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

import org.apache.axis.utils.StringUtils;

import krishagni.catissueplus.Exception.CatissueException;
import krishagni.catissueplus.Exception.SpecimenErrorCodeEnum;
import krishagni.catissueplus.dao.SCGDAO;
import krishagni.catissueplus.dao.SpecimenDAO;
import krishagni.catissueplus.dao.StorageContainerDAO;
import krishagni.catissueplus.dto.BiohazardDTO;
import krishagni.catissueplus.dto.ExternalIdentifierDTO;
import krishagni.catissueplus.dto.SpecimenDTO;
import edu.wustl.catissuecore.bizlogic.SiteBizLogic;
import edu.wustl.catissuecore.dao.CollectionProtocolDAO;
import edu.wustl.catissuecore.dao.UserDAO;
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.User;
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
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;

public class SpecimenBizLogic
{

	private static final Logger LOGGER = Logger.getCommonLogger(SpecimenBizLogic.class);
	private HashSet<String> allocatedPositions = new HashSet<String>();
	/** The storage positions. */
	private String storagePositions = "";

	/**
	 * This will update the specimen object in the database
	 * @param hibernateDao
	 * @param specimenDTO
	 * @param sessionDataBean
	 * @return SpecimenDTO
	 * @throws BizLogicException
	 */
	public SpecimenDTO updateSpecimen(HibernateDAO hibernateDao, SpecimenDTO specimenDTO,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		Specimen oldSpecimenObj = null;
		try
		{
		    
			//object retrieval for auditing purpose
			SpecimenDAO specimenDAO = new SpecimenDAO();
			oldSpecimenObj = specimenDAO.getSpecimenById(specimenDTO.getId(), hibernateDao);
			if (!isAuthorizedForSpecimenProcessing(hibernateDao, specimenDTO, sessionDataBean))
			{
				throw AppUtility.getUserNotAuthorizedException("SPECIMEN_PROCESSING", "",
						Specimen.class.getSimpleName());
			}
			//updating the object with DTO
			getUpdatedSpecimen(oldSpecimenObj, specimenDTO, sessionDataBean, hibernateDao);
			generateLabel(oldSpecimenObj);
			generateBarCode(oldSpecimenObj);
			validateSpecimen(oldSpecimenObj, hibernateDao, oldSpecimenObj);
			//updating the specimen in database
			hibernateDao.update(oldSpecimenObj);
		}
		catch (ApplicationException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		} catch (ParseException e) {
			LOGGER.error(e.getMessage(), e);
			throw new CatissueException(SpecimenErrorCodeEnum.INTERNAL_SERVER_ERROR.getCode());
		}
//		catch (Exception e)
//		{
//			LOGGER.error(e.getMessage(), e);
//			throw new CatissueException(SpecimenErrorCodeEnum.INTERNAL_SERVER_ERROR.getCode());
//		}
		return this.getSpecimenDTOFromSpecimen(oldSpecimenObj);
	}

	/**
	 * return true if the specimen status is collected
	 * @param specimen the specimen
	 * @return true, if status is collected
	 */
	private boolean isSpecimenStatusCollected(String specimenCollectionStatus)
	{
		return !Validator.isEmpty(specimenCollectionStatus)
				&& specimenCollectionStatus.equals(Constants.COLLECTION_STATUS_COLLECTED);
	}

	/**
	 * Generate label for the given specimen
	 * @param specimen Specimen Object
	 * @throws BizLogicException 
	 */
	private void generateLabel(Specimen specimen) throws BizLogicException
	{
		if ((Variables.isSpecimenLabelGeneratorAvl || Variables.isTemplateBasedLblGeneratorAvl)
				&& isSpecimenStatusCollected(specimen.getCollectionStatus()))
		{
			try
			{
				final LabelGenerator spLblGenerator;
				spLblGenerator = LabelGeneratorFactory.getInstance(Constants.SPECIMEN_LABEL_GENERATOR_PROPERTY_NAME);
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
	 * Generate barcode for the given specimen
	 * @param specimen Specimen Object
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
	 */
	private void getUpdatedSpecimen(Specimen oldSpecimenObj, SpecimenDTO specimenDTO, SessionDataBean sessionDataBean,
			HibernateDAO hibernateDao) throws ParseException, ApplicationException
	{
		if (!Validator.isEmpty(specimenDTO.getActivityStatus()))
		{
			oldSpecimenObj.setActivityStatus(specimenDTO.getActivityStatus());
		}
		if (specimenDTO.getCreatedDate() != null)
		{
			oldSpecimenObj.setCreatedOn(specimenDTO.getCreatedDate());
		}
		if (!Validator.isEmpty(specimenDTO.getLabel()))
		{
			oldSpecimenObj.setLabel(specimenDTO.getLabel());
		}
		Double quantityDiff = 0.0;
		if (specimenDTO.getQuantity() != null)
		{
			if(specimenDTO.getQuantity() > oldSpecimenObj.getInitialQuantity())
			{
				quantityDiff = specimenDTO.getQuantity() - oldSpecimenObj.getInitialQuantity();
			}
			oldSpecimenObj.setInitialQuantity(specimenDTO.getQuantity());
		}
		if (isSpecimenStatusCollected(specimenDTO.getCollectionStatus())
				&& !oldSpecimenObj.getCollectionStatus().equalsIgnoreCase(Constants.COLLECTION_STATUS_COLLECTED))
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
			{
				oldSpecimenObj.setCollectionStatus(specimenDTO.getCollectionStatus());
			}
			if(quantityDiff > 0.0)
			{
				oldSpecimenObj.setAvailableQuantity(oldSpecimenObj.getAvailableQuantity() + quantityDiff);
				oldSpecimenObj.setIsAvailable(Boolean.TRUE);
			}
			if (specimenDTO.getAvailableQuantity() != null)
			{
				oldSpecimenObj.setAvailableQuantity(specimenDTO.getAvailableQuantity());
			}
			if (specimenDTO.isAvailable() != null)
			{
				oldSpecimenObj.setIsAvailable(specimenDTO.isAvailable());
			}
		}
		if(oldSpecimenObj.getAvailableQuantity() == 0)
		{
			oldSpecimenObj.setIsAvailable(Boolean.FALSE);
		}
		if (!Validator.isEmpty(specimenDTO.getBarcode()))
		{
			oldSpecimenObj.setBarcode(specimenDTO.getBarcode());
		}

		if (!Validator.isEmpty(specimenDTO.getClassName()))
		{
			oldSpecimenObj.setSpecimenClass(specimenDTO.getClassName());
		}
		if (!Validator.isEmpty(specimenDTO.getType()))
		{
			oldSpecimenObj.setSpecimenType(specimenDTO.getType());
		}
		if (!Validator.isEmpty(specimenDTO.getPathologicalStatus()))
		{
			oldSpecimenObj.setPathologicalStatus(specimenDTO.getPathologicalStatus());
		}
		if (!Validator.isEmpty(specimenDTO.getTissueSide()))
		{
			oldSpecimenObj.setTissueSide(specimenDTO.getTissueSide());
		}
		if (!Validator.isEmpty(specimenDTO.getTissueSite()))
		{
			oldSpecimenObj.setTissueSite(specimenDTO.getTissueSite());
		}
		if (!Validator.isEmpty(specimenDTO.getComments()))
		{
			oldSpecimenObj.setComment(specimenDTO.getComments());
		}
		SpecimenPosition position = new SpecimenPosition();
		if (specimenDTO.getIsVirtual() == null || specimenDTO.getIsVirtual())
		{
			position = null;
		}
		else if (oldSpecimenObj.getSpecimenPosition() == null
				&& isSpecimenStatusCollected(oldSpecimenObj.getCollectionStatus()))
		{
			if (specimenDTO.getPos1() != null && !"".equals(specimenDTO.getPos1().trim())
					&& specimenDTO.getPos2() != null && !"".equals(specimenDTO.getPos2().trim()))
			{
				int toPos1Int = StorageContainerUtil.convertSpecimenPositionsToInteger(specimenDTO.getContainerName(),
						1, specimenDTO.getPos1());
				int toPos2Int = StorageContainerUtil.convertSpecimenPositionsToInteger(specimenDTO.getContainerName(),
						2, specimenDTO.getPos2());
				position.setPositionDimensionOne(toPos1Int);
				position.setPositionDimensionTwo(toPos2Int);
				position.setPositionDimensionOneString(specimenDTO.getPos1());
				position.setPositionDimensionTwoString(specimenDTO.getPos2());
				StorageContainer container = new StorageContainer();
				Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
				substParams.put("0", new NamedQueryParam(DBTypes.STRING, specimenDTO.getContainerName()));
				final List containerIds = ((HibernateDAO) hibernateDao).executeNamedQuery("getStorageContainerIdByContainerName",
						substParams);
				if (!containerIds.isEmpty())
				{
					container.setId((Long) containerIds.get(0));
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
		if (Constants.MOLECULAR.equals(oldSpecimenObj.getClassName()) && specimenDTO.getConcentration() != null)
		{
			oldSpecimenObj.setConcentrationInMicrogramPerMicroliter(specimenDTO.getConcentration());
		}
		oldSpecimenObj.setExternalIdentifierCollection(getExternalIdentifiers(oldSpecimenObj, specimenDTO));
		oldSpecimenObj.setBiohazardCollection(getBiohazards(oldSpecimenObj, specimenDTO));
	}

	/**
	 * Returns ExternalIdentifier Collection to be set in old specimen object.
	 * @param oldSpecimenObj
	 * @param specimenDTO
	 * @returns ExternalIdentifier Collection 
	 */
	private Collection<ExternalIdentifier> getExternalIdentifiers(Specimen oldSpecimenObj, SpecimenDTO specimenDTO)
	{
		Collection<ExternalIdentifier> externalIdentifierCollection = oldSpecimenObj.getExternalIdentifierCollection();
		Collection<ExternalIdentifierDTO> externalIdentifierDTOCollection = specimenDTO.getExternalIdentifiers();
		externalIdentifierCollection.retainAll(externalIdentifierDTOCollection);
		for (ExternalIdentifierDTO externalIdentifierDTO : externalIdentifierDTOCollection)
		{
			if (externalIdentifierDTO.getStatus().equalsIgnoreCase("ADD"))
			{
				externalIdentifierCollection.add(getExternalIdentifierDomainObjFromDTO(oldSpecimenObj,
						externalIdentifierDTO));
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
	 * @returns ExternalIdentifier
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
	 * @returns Collection<Biohazard>
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

	/**
	 * @param biohazardDTO
	 * @returns Biohazard 
	 */
	private Biohazard getBiohazardDomainObjFromDTO(BiohazardDTO biohazardDTO)
	{
		Biohazard biohazard = new Biohazard();
		biohazard.setId(biohazardDTO.getId());
		biohazard.setName(biohazardDTO.getName());
		biohazard.setType(biohazardDTO.getType());
		return biohazard;
	}

	/**
	 * @param exception
	 * @param key
	 * @param logMessage
	 * @returns BizLogicException 
	 */
	private BizLogicException getBizLogicException(Exception exception, String key, String logMessage)
	{
		LOGGER.debug(logMessage);
		ErrorKey errorKey = ErrorKey.getErrorKey(key);
		return new BizLogicException(errorKey, exception, logMessage);
	}

	/**
	 * Sets the specimen events.
	 * @param specimen : specimen
	 * @param sessionDataBean  : sessionDataBean
	 */
	private void setSpecimenEvents(Specimen specimen, SessionDataBean sessionDataBean)
	{
		final Specimen parentSpecimen = (Specimen) specimen.getParentSpecimen();
		if (specimen.getParentSpecimen() == null)
		{
			if (specimen.getId() == null)
			{
				specimen.setPropogatingSpecimenEventCollection(specimen.getSpecimenCollectionGroup()
						.getSpecimenEventParametersCollection(), sessionDataBean.getUserId(), specimen);
			}
			else
			{
				specimen.setPropogatingSpecimenEventCollection(specimen.getSpecimenRequirement()
						.getSpecimenEventCollection(), sessionDataBean.getUserId(), specimen);
			}
		}
//		else
//		{
//			specimen.setSpecimenEventCollection(this.populateDeriveSpecimenEventCollection(parentSpecimen, specimen));
//		}
	}

	/**
	 * Set event parameters from parent specimen to derived specimen.
	 * @param parentSpecimen specimen
	 * @param deriveSpecimen Derived Specimen
	 * @return Set<SpecimenEventParameters>
	 */
//	private Set<SpecimenEventParameters> populateDeriveSpecimenEventCollection(Specimen parentSpecimen,
//			Specimen deriveSpecimen)
//	{
//		final Set<SpecimenEventParameters> deriveEventCollection = new HashSet<SpecimenEventParameters>();
//		final Set<SpecimenEventParameters> parentSpecimeneventCollection = (Set<SpecimenEventParameters>) parentSpecimen
//				.getSpecimenEventCollection();
//		SpecimenEventParameters deriveSpecimenEventParameters = null;
//		if (parentSpecimeneventCollection != null)
//		{
//			for (final SpecimenEventParameters specimenEventParameters : parentSpecimeneventCollection)
//			{
//				try
//				{
//					deriveSpecimenEventParameters = (SpecimenEventParameters) specimenEventParameters.clone();
//				}
//				catch (CloneNotSupportedException e)
//				{
//					LOGGER.error(e);
//					throw new CatissueException(SpecimenErrorCodeEnum.INTERNAL_SERVER_ERROR.getCode());
//				}
//				deriveSpecimenEventParameters.setId(null);
//				deriveSpecimenEventParameters.setSpecimen(deriveSpecimen);
//				deriveEventCollection.add(deriveSpecimenEventParameters);
//			}
//		}
//		return deriveEventCollection;
//	}

	/**
	 * Validates the specimen DTO from UI.
	 * @param oldSpecimenObj 
	 * @param dao 
	 * @param specimenDTO
	 * @throws ApplicationException 
	 */
	private void validateSpecimen(Specimen specimen, HibernateDAO hibernateDao, Specimen oldSpecimenObj)
			throws ApplicationException
	{
		final List specimenClassList = AppUtility.getSpecimenClassList();
		final String specimenClass = specimen.getClassName();
		if (!Validator.isEnumeratedValue(specimenClassList, specimenClass))
		{
			LOGGER.error(ApplicationProperties.getValue("protocol.class.errMsg"));
			throw new CatissueException(ApplicationProperties.getValue("protocol.class.errMsg"),
					SpecimenErrorCodeEnum.INVALID_CLASS.getCode());
		}
		if (!Validator.isEnumeratedValue(AppUtility.getSpecimenTypes(specimenClass), specimen.getSpecimenType()))
		{
			LOGGER.error(ApplicationProperties.getValue("protocol.type.errMsg"));
			throw new CatissueException(ApplicationProperties.getValue("protocol.type.errMsg"),
					SpecimenErrorCodeEnum.INVALID_TYPE.getCode());
		}
		if (specimen.getCollectionStatus().equalsIgnoreCase(Constants.SPECIMEN_COLLECTED)
				&& Validator.isEmpty(specimen.getLabel()))
		{
			final String message = ApplicationProperties.getValue("specimen.label");
			throw this.getBizLogicException(null, "errors.item.required", message);
		}
		if (specimen.getAvailableQuantity().compareTo(specimen.getInitialQuantity()) > 0)
		{
			final String quantityString = ApplicationProperties.getValue("specimen.availableQuantity");
			throw this.getBizLogicException(null, "errors.availablequantity", quantityString);
		}
		if (specimen.getSpecimenPosition() != null && validateContainerRestrictions(hibernateDao,specimen))
		{
			StorageContainerBizlogic containerForSpecimenBizLogic = new StorageContainerBizlogic();
			SpecimenPosition specimenPosition = specimen.getSpecimenPosition();
			if (specimen.getId() == null
					|| (oldSpecimenObj != null && Constants.COLLECTION_STATUS_PENDING.equals(oldSpecimenObj
							.getCollectionStatus())))
				if (!containerForSpecimenBizLogic.isPositionAvailable(specimenPosition.getStorageContainer().getName(),
						specimenPosition.getPositionDimensionOne(), specimenPosition.getPositionDimensionTwo(),
						hibernateDao))
				{
					LOGGER.error(ApplicationProperties.getValue("shipment.samePositionForSpecimens"));
					throw new CatissueException(ApplicationProperties.getValue("shipment.samePositionForSpecimens"),
							SpecimenErrorCodeEnum.POSITION_ALREADY_OCCUPIED.getCode());
				}
		}
		if(specimen.getParentSpecimen()!=null && ((Specimen)(specimen.getParentSpecimen())).getCollectionStatus().equals(Constants.COLLECTION_STATUS_PENDING)){
		   
		    throw this.getBizLogicException(null, "specimen.parent.label.required","");
		}
		if(Constants.COLLECTION_STATUS_COLLECTED.equals(specimen.getCollectionStatus()) && Validator.isEmpty(specimen.getLabel()))
		{
			LOGGER.error("Specimen label cannot be empty.");
			throw new CatissueException(SpecimenErrorCodeEnum.LABEL_REQUIRED.getCode());
		}
//		checkDuplicateSpecimenFields(specimen, hibernateDao);
	}
	
	private boolean validateContainerRestrictions(HibernateDAO hibernateDao,Specimen specimen) throws BizLogicException 
	{
		StorageContainerDAO containerDAO = new StorageContainerDAO();
		if(!containerDAO.isContainerCanHoldSpecimen(hibernateDao, specimen.getClassName(), specimen.getSpecimenType(), specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getId(), specimen.getSpecimenPosition().getStorageContainer().getId()))
		{
			throw new CatissueException(SpecimenErrorCodeEnum.INVALID_CONTAINER.getCode());
		}
		return true;
	}

	/**
	 * Checks for duplicate specimenLabel and specimenBarcode fields.
	 * @param specimen Specimen
	 * @param hibernateDao hibernateDao
	 * @throws DAOException 
	 */
	private void checkDuplicateSpecimenFields(Specimen specimen, HibernateDAO hibernateDao) throws DAOException
	{
		List specimenIds = null;
		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		if (specimen.getLabel() != null)
		{
			params.put("0",	new NamedQueryParam(DBTypes.STRING, specimen.getLabel()==null?"":specimen.getLabel()));
			params.put("1",	new NamedQueryParam(DBTypes.STRING, specimen.getBarcode()==null?"":specimen.getBarcode()));
			specimenIds = hibernateDao.executeNamedQuery(Specimen.class.getName()+".getSpecimenIdByLabelorBarcode", params);
			if(specimen.getId() == null && specimenIds != null && specimenIds.size() >= 1)
			{
				throw new CatissueException(SpecimenErrorCodeEnum.DUPLICATE_LABEL_BARCODE.getCode());
//					throw DAOUtility.getInstance().getDAOException(null, "errors.specimen.label.barcode",specimen.getLabel());
			}
			else if(specimen.getId() != null && specimenIds != null && (specimenIds.size() > 1 || !specimen.getId().toString().equals(specimenIds.get(0).toString())))
			{
				throw new CatissueException(SpecimenErrorCodeEnum.DUPLICATE_LABEL_BARCODE.getCode());
//				throw DAOUtility.getInstance().getDAOException(null, "errors.specimen.label.barcode",specimen.getLabel());
			}
		}
	}

	/**
	 * populates the specimenDTO object from the given specimen
	 * @param specimen
	 * @returns SpecimenDTO
	 * @throws BizLogicException
	 */
	public SpecimenDTO getSpecimenDTOFromSpecimen(Specimen specimen) throws BizLogicException
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
		specimenDTO.setConcentration(specimen.getConcentrationInMicrogramPerMicroliter());
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
			specimenDTO.setSpecimenCollectionGroupName(specimen.getSpecimenCollectionGroup().getName());
		}
		specimenDTO.setTissueSide(specimen.getTissueSide());
		specimenDTO.setTissueSite(specimen.getTissueSite());
		specimenDTO.setType(specimen.getSpecimenType());
		if (specimen.getSpecimenPosition() != null)
		{
			specimenDTO.setPos1(specimen.getSpecimenPosition().getPositionDimensionOneString());
			specimenDTO.setPos2(specimen.getSpecimenPosition().getPositionDimensionTwoString());
			specimenDTO.setContainerId(specimen.getSpecimenPosition().getStorageContainer().getId());
			specimenDTO.setContainerName(specimen.getSpecimenPosition().getStorageContainer().getName());
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
	 * @returns Collection<ExternalIdentifierDTO>
	 */
	private Collection<ExternalIdentifierDTO> getExternalIdentifierDTOCollection(Specimen specimen)
	{
		Collection<ExternalIdentifier> externalIdentifiers = specimen.getExternalIdentifierCollection();
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
	 * @returns Collection<BiohazardDTO>
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

	/**
	 * Accepts the list of Specimen DTO's and insert them 
	 * @param specimenDTOList
	 * @param hibernateDao
	 * @param sessionDataBean
	 * @return
	 * @throws BizLogicException
	 */
	public List<SpecimenDTO> insert(List<SpecimenDTO> specimenDTOList, HibernateDAO hibernateDao,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		List<SpecimenDTO> specimenDTOs = new ArrayList<SpecimenDTO>();
		try
		{
			for (SpecimenDTO specimenDTO : specimenDTOList)
			{
				specimenDTO = insert(specimenDTO, hibernateDao, sessionDataBean);
				specimenDTOs.add(specimenDTO);
			}
		}
		catch (ApplicationException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		return specimenDTOs;
	}

	/**
	 * Accepts the SpecimenDTO and inserts
	 * @param specimenDTO
	 * @param hibernateDao
	 * @param sessionDataBean
	 * @returns SpecimenDTO
	 * @throws BizLogicException
	 */
	public SpecimenDTO insert(SpecimenDTO specimenDTO, HibernateDAO hibernateDao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		Specimen specimen = new Specimen();
		try
		{ 
			if (!isAuthorizedForSpecimenProcessing(hibernateDao, specimenDTO, sessionDataBean))
			{
				throw AppUtility.getUserNotAuthorizedException("SPECIMEN_PROCESSING", "", specimen.getClass()
						.getSimpleName());
			}
			specimen = populateSpecimenFromDTO(specimenDTO, hibernateDao, sessionDataBean);
			generateLabel(specimen);
			generateBarCode(specimen);
			validateSpecimen(specimen, hibernateDao, null);
			hibernateDao.insert(specimen);
			
		}
		catch (ApplicationException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		return getSpecimenDTOFromSpecimen(specimen);
	}

//	private void setConsentTierStatus(Specimen specimen,
//			Collection<ConsentTierStatus> consentTierStatusCollection)
//	{
//		Collection<ConsentTierStatus> consentTierStatusCollectionForSpecimen = null;
//		if (consentTierStatusCollection != null)
//		{
//			consentTierStatusCollectionForSpecimen = new HashSet<ConsentTierStatus>();
//			final Iterator<ConsentTierStatus> itr = consentTierStatusCollection.iterator();
//			while (itr.hasNext())
//			{
//				final ConsentTierStatus conentTierStatus = (ConsentTierStatus) itr.next();
//				final ConsentTierStatus consentTierStatusForSpecimen = new ConsentTierStatus();
//				consentTierStatusForSpecimen.setStatus(conentTierStatus.getStatus());
//				consentTierStatusForSpecimen.setConsentTier(conentTierStatus.getConsentTier());
//				consentTierStatusCollectionForSpecimen.add(consentTierStatusForSpecimen);
//			}
//			specimen.setConsentTierStatusCollection(consentTierStatusCollectionForSpecimen);
//		}
//
//	}

	private boolean isAuthorizedForSpecimenProcessing(HibernateDAO hibernateDao, SpecimenDTO specimenDTO,
			SessionDataBean sessionDataBean) throws DAOException, SMException
	{
		boolean isAuthorize = sessionDataBean.isAdmin();
		if (!isAuthorize)
		{ 
			String specimenName;
			Long specimenId;
			if ((Constants.ALIQUOT.equals(specimenDTO.getLineage())
					|| Constants.DERIVED_SPECIMEN.equals(specimenDTO.getLineage())) && specimenDTO.getId() == null)
			{
				specimenName = specimenDTO.getParentSpecimenName();
				specimenId = specimenDTO.getParentSpecimenId();
			}
			else
			{
				specimenName = specimenDTO.getLabel();
				specimenId = specimenDTO.getId();
			}
			SpecimenDAO specimenDAO = new SpecimenDAO();
			Long siteId = specimenDAO.getSiteIdBySpecimenLabelOrId(hibernateDao, specimenName, specimenId);
			Long cpId = 0l;
			if(Constants.NEW_SPECIMEN.equals(specimenDTO.getLineage()))
			{
				SCGDAO scgdao = new SCGDAO();
				if(!Validator.isEmpty(specimenDTO.getSpecimenCollectionGroupName()))
				{
					cpId = scgdao.getCPID(specimenDTO.getSpecimenCollectionGroupName(),hibernateDao);
				}
				else if(specimenDTO.getSpecimenCollectionGroupId() != null)
				{
					cpId = scgdao.getCPID(specimenDTO.getSpecimenCollectionGroupId(),hibernateDao);
				}
			}
			else
			{
				if(specimenId != null)
				{
					cpId = specimenDAO.getCpId(specimenId, hibernateDao);
				}
				else if(!Validator.isEmpty(specimenName))
				{
					cpId = specimenDAO.getCpId(specimenName, hibernateDao);
				}
			}
			isAuthorize = chkAuthorizationForCPnSite(siteId, cpId, sessionDataBean.getUserName());
			
			if(isAuthorize && siteId != null)
			{   
				UserDAO userDAO = new UserDAO();
				List userSiteIds = userDAO.getAssociatedSiteIds(hibernateDao, sessionDataBean.getUserId());
				
				return userSiteIds != null?userSiteIds.contains(siteId):false;
			}
			if(!isAuthorize && siteId == null)
			{
				List<Long> siteIds = getSiteIds(hibernateDao,cpId,sessionDataBean.getUserId());
				SiteBizLogic siteBizLogic = new SiteBizLogic();
				isAuthorize = siteBizLogic.checkSpecimenProcessingPrivileges(hibernateDao,siteIds,sessionDataBean.getUserName());
			}
		}
		return isAuthorize;
	}

	private List<Long> getSiteIds(HibernateDAO hibernateDao, Long cpId,Long userId) throws DAOException 
	{
		UserDAO userDAO = new UserDAO();
		CollectionProtocolDAO cpDAO = new CollectionProtocolDAO();
		List userSiteIds = userDAO.getAssociatedSiteIds(hibernateDao, userId);
		List<Long> cpSiteIds = cpDAO.getAssociatedSiteIds(hibernateDao, cpId);
		Set<Long> siteIdset = new HashSet<Long>();
		siteIdset.addAll(cpSiteIds);
		siteIdset.retainAll(userSiteIds);
		return new ArrayList<Long>(siteIdset);
	}

	private Boolean chkAuthorizationForCPnSite(Long siteId, Long cpId, String userName) throws SMException
	{
		final PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(userName);
//		privilegeCache.hasPrivilege(Site.class.getName()+"_31","SPECIMEN_PROCESSING");
//		privilegeCache.hasPrivilege(CollectionProtocol.class.getName()+"_110","SPECIMEN_PROCESSING");
//		privilegeCache.getPrivilegesforObjectId(CollectionProtocol.class.getName()+"_110");
//		privilegeCache.getPrivilegesforObjectId(Site.class.getName()+"_31");
		boolean isAuthorized = Boolean.FALSE;
		if (siteId != null)
		{
			String siteProtectionEleName = "SITE_" + siteId + "_All_CP";
			isAuthorized = privilegeCache.hasPrivilege(siteProtectionEleName, "SPECIMEN_PROCESSING");
		}
		if (!isAuthorized)
		{
			String cpProtectionEleName = CollectionProtocol.class.getName() + "_" + cpId;
			// Checking whether the logged in user has the required
			// privilege on the given protection element
			isAuthorized = privilegeCache.hasPrivilege(cpProtectionEleName, "SPECIMEN_PROCESSING");
		}
		
		return isAuthorized;
	}

	public Specimen populateSpecimenFromDTO(SpecimenDTO specimenDTO, HibernateDAO hibernateDao,
			SessionDataBean sessionDataBean) throws BizLogicException, DAOException
	{
		Specimen specimen = new Specimen();
		specimen.setActivityStatus(specimenDTO.getActivityStatus());
		specimen.setAvailableQuantity(specimenDTO.getAvailableQuantity()==null?0.0:specimenDTO.getAvailableQuantity());
		specimen.setBarcode(specimenDTO.getBarcode());
		specimen.setExternalIdentifierCollection(getExterIdentifierColl(specimenDTO.getExternalIdentifiers(), specimen));
		specimen.setCollectionStatus(specimenDTO.getCollectionStatus());
		specimen.setComment(specimenDTO.getComments());
		specimen.setConcentrationInMicrogramPerMicroliter(specimenDTO.getConcentration());
		specimen.setCreatedOn(specimenDTO.getCreatedDate());
		specimen.setInitialQuantity(specimenDTO.getQuantity()==null?0.0:specimenDTO.getQuantity());
		specimen.setIsAvailable(Boolean.TRUE);
		specimen.setLabel(specimenDTO.getLabel());
		specimen.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		specimen.setAvailableQuantity(specimen.getInitialQuantity());
		if(specimenDTO.getRequirementId()!=null)
		{
			SpecimenRequirement sr = (SpecimenRequirement)hibernateDao.retrieveById(SpecimenRequirement.class.getName(), specimenDTO.getRequirementId());
			specimen.setSpecimenRequirement(sr);
		}
		if (Constants.ALIQUOT.equals(specimenDTO.getLineage())
				|| Constants.DERIVED_SPECIMEN.equals(specimenDTO.getLineage()))
		{
			specimen.setLineage(specimenDTO.getLineage());
			SpecimenDAO specimenDAO = new SpecimenDAO();
			Specimen parentSpecimen = specimenDAO.getSpecimenById(specimenDTO.getParentSpecimenId(), hibernateDao);
			
			if (parentSpecimen == null)
			{
				throw this.getBizLogicException(null, "errors.specimen.parentspecimen", "");
			}
			try
			{
				if(parentSpecimen.getChildSpecimenCollection() == null || parentSpecimen.getChildSpecimenCollection().isEmpty())
				{
					Collection<AbstractSpecimen> childColl = new HashSet<AbstractSpecimen>();
					childColl.add(specimen);
					parentSpecimen.setChildSpecimenCollection(childColl);
				}
				else
				{
					parentSpecimen.getChildSpecimenCollection().add(specimen);
				}
			}
			catch(Exception e)
			{
				Collection<AbstractSpecimen> childColl = new HashSet<AbstractSpecimen>();
				childColl.add(specimen);
				parentSpecimen.setChildSpecimenCollection(childColl);
			}
			specimen.setTissueSide(parentSpecimen.getTissueSide());
			specimen.setTissueSite(parentSpecimen.getTissueSite());
			specimen.setPathologicalStatus(parentSpecimen.getPathologicalStatus());
			Collection<Biohazard> biohazards = new HashSet<Biohazard>();
			for (Biohazard biohazard : parentSpecimen.getBiohazardCollection())
			{
				biohazards.add(biohazard);
			}
			specimen.setBiohazardCollection(biohazards);
		//	setConsentTierStatus(specimen, parentSpecimen.getConsentTierStatusCollection());
			//specimen.setChildSpecimenCollection(parentSpecimen.getChildSpecimenCollection());
			specimen.setParentSpecimen(parentSpecimen);
			specimen.setSpecimenCollectionGroup(parentSpecimen.getSpecimenCollectionGroup());
			setSpecimenEvents(specimen, sessionDataBean);
//			SpecimenCollectionGroup collectionGroup = new SpecimenCollectionGroup();
//			collectionGroup.setId(parentSpecimen.getSpecimenCollectionGroup().getId());
//			specimen.setSpecimenCollectionGroup(collectionGroup);
		}
		else
		{
			specimen.setLineage(Constants.NEW_SPECIMEN);
			SCGDAO scgdao = new SCGDAO();
			SpecimenCollectionGroup collectionGroup = scgdao.getSCG(specimenDTO.getSpecimenCollectionGroupId(),
					hibernateDao);
			specimen.setSpecimenCollectionGroup(collectionGroup);
			specimen.setPathologicalStatus(specimenDTO.getPathologicalStatus());
			specimen.setTissueSide(specimenDTO.getTissueSide());
			specimen.setTissueSite(specimenDTO.getTissueSite());
			
			specimen.setBiohazardCollection(getBiohazardCollection(specimenDTO.getBioHazards()));
		//	specimen.setConsentTierStatusCollectionFromSCG(collectionGroup);
		}
		specimen.setSpecimenClass(specimenDTO.getClassName());
		specimen.setSpecimenType(specimenDTO.getType());
		SpecimenPosition specimenPosition = new SpecimenPosition();
		if (Validator.isEmpty(specimenDTO.getContainerName()) && specimenDTO.getContainerId() == null)
		{
			specimen.setSpecimenPosition(null);
		}
		else
		{
			try
			{
				String pos1 = null;
				String pos2 = null;
				if (!Validator.isEmpty(storagePositions))
				{
					final String positionStr = storagePositions;
					final String positions[] = positionStr.split(",");
					final String strContainerName = positions[0];
					if ((!Validator.isEmpty(strContainerName.trim()))
							&& (!Validator.isEmpty(specimenDTO.getContainerName()))
							&& (strContainerName.equals(specimenDTO.getContainerName())))
					{
						pos1 = positions[1];
						pos2 = positions[2];
					}
				}
			StorageContainerBizlogic storageContainerBizlogic = new StorageContainerBizlogic();
			 specimenPosition = storageContainerBizlogic.getPositionIfAvailableFromContainer(
					 specimenDTO.getContainerName(), specimenDTO.getPos1(),
					 specimenDTO.getPos2(), pos1,pos2,hibernateDao,allocatedPositions);
			 String storageValue = "";
			 if (specimenDTO.getContainerName() != null)
				{
					storageValue = storageContainerBizlogic.getStorageValueKey(specimenDTO.getContainerName(), null,
							specimenPosition.getPositionDimensionOneString(), specimenPosition.getPositionDimensionTwoString(),hibernateDao);
				}
				else
				{
					storageValue = storageContainerBizlogic.getStorageValueKey(null,specimenDTO.getContainerId().toString(),
							specimenPosition.getPositionDimensionOneString(), specimenPosition.getPositionDimensionTwoString(),hibernateDao);
				}
				if (!this.allocatedPositions.contains(storageValue))
				{
					this.allocatedPositions.add(storageValue);
					final StringBuffer posBuffer = new StringBuffer();
					posBuffer.append(specimenPosition.getStorageContainer().getName());
					posBuffer.append(',');
					posBuffer.append(specimenPosition.getPositionDimensionOne());
					posBuffer.append(',');
					posBuffer.append(specimenPosition.getPositionDimensionTwo());
					storagePositions = posBuffer.toString();
					
				}
				else
				{
					throw AppUtility.getApplicationException(null,
							"errors.storageContainer.inUse", "StorageContainerUtil.java");
				}
//           singleAliquotDetailsDTO.setPos1(specimenPosition.getPositionDimensionOneString());
//           singleAliquotDetailsDTO.setPos2(specimenPosition.getPositionDimensionTwoString());
//			specimenPosition.setPositionDimensionOneString((specimenDTO.getPos1()));
//			specimenPosition.setPositionDimensionTwoString((specimenDTO.getPos2()));
//			try
//			{
//				specimenPosition.setPositionDimensionOne((StorageContainerUtil.convertPositionsToIntegerUsingContId(specimenDTO
//						.getContainerId().toString(), 1, specimenDTO.getPos1())));
//				specimenPosition.setPositionDimensionTwo((StorageContainerUtil.convertPositionsToIntegerUsingContId(specimenDTO
//						.getContainerId().toString(), 2, specimenDTO.getPos2())));
			}
			catch (ApplicationException e)
			{
				LOGGER.error(e.getMessage(), e);
				throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
			}
			StorageContainer container = new StorageContainer();
			container.setId(specimenPosition.getStorageContainer().getId());
			container.setName(specimenPosition.getStorageContainer().getName());
			specimenPosition.setStorageContainer(container);
			specimenPosition.setSpecimen(specimen);
			specimen.setSpecimenPosition(specimenPosition);
		}
		setSpecimenEvents(specimen, sessionDataBean);
		return specimen;
	}

	private Collection<ExternalIdentifier> getExterIdentifierColl(Collection<ExternalIdentifierDTO> externalIdentifiers, Specimen specimen)
	{
		Collection<ExternalIdentifier> externalIdentifierColl = new HashSet<ExternalIdentifier>();
		if (externalIdentifiers != null)
		{
			for (ExternalIdentifierDTO externalIdentifierDTO : externalIdentifiers)
			{
				externalIdentifierColl.add(getExternalIdentifierDomainObjFromDTO(specimen, externalIdentifierDTO));
			}
		}
		if (externalIdentifierColl.isEmpty())
		{
			externalIdentifierColl.add(new ExternalIdentifier());
		}
		return externalIdentifierColl;
	}

	private Collection<Biohazard> getBiohazardCollection(Collection<BiohazardDTO> bioHazards)
	{
		Collection<Biohazard> biohazardColl = new HashSet<Biohazard>();
		for (BiohazardDTO biohazardDTO : bioHazards)
		{
			biohazardColl.add(getBiohazardDomainObjFromDTO(biohazardDTO));
		}
		return biohazardColl;
	}

	public void disposeSpecimen(HibernateDAO hibernateDao, SessionDataBean sessionDataBean, Specimen specimen,
			DisposalEventParameters disposalEvent) throws DAOException, BizLogicException
	{
		if(Status.ACTIVITY_STATUS_DISABLED.toString().equals(specimen.getActivityStatus())){
			chkActiveChilds(hibernateDao,specimen.getId());
		}
		else{
			specimen.setActivityStatus(Status.ACTIVITY_STATUS_CLOSED.toString());
		}
		
		SpecimenDAO specimenDAO = new SpecimenDAO();
		specimenDAO.populateEventWithUserId(disposalEvent, hibernateDao);
		disposeEvent((DisposalEventParameters) disposalEvent, specimen, hibernateDao);
		specimen.getSpecimenEventCollection().add(disposalEvent);
		hibernateDao.insert(disposalEvent);
	}

	private void chkActiveChilds(HibernateDAO hibernateDao, Long id) throws DAOException, BizLogicException {
		String hql = "select sp.id from "+Specimen.class.getName()+" sp where sp.parentSpecimen.id = "+id+" and sp.activityStatus != 'Disabled'"
				+ " and sp.collectionStatus='Collected'";
		List result = hibernateDao.executeQuery(hql, null);
		if(result != null && result.size() >=1 ){
			final ErrorKey errorKey = ErrorKey.getErrorKey("errors.specimen.contains.subspecimen");
			throw new BizLogicException(errorKey, null, "");
		}
	
	}

	private void disposeEvent(DisposalEventParameters disposalEventParameters, Specimen specimen,
			HibernateDAO hibernateDao) throws BizLogicException, DAOException
	{
		if (disposalEventParameters.getActivityStatus().equals(Status.ACTIVITY_STATUS_DISABLED.getStatus()))
		{
			SpecimenDAO specimenDAO = new SpecimenDAO();
			specimenDAO.disableChildSpecimens(hibernateDao, specimen.getId());
		}
		specimen = (Specimen) hibernateDao.retrieveById(Specimen.class.getName(), specimen.getId());
		final SpecimenPosition prevPosition = specimen.getSpecimenPosition();
		specimen.setSpecimenPosition(null);
		specimen.setIsAvailable(Boolean.FALSE);
//		specimen.setActivityStatus(disposalEventParameters.getActivityStatus());
		hibernateDao.update(specimen);
		if(prevPosition != null)
		{
			hibernateDao.delete(prevPosition);
		}
	}

	private DisposalEventParameters createDisposeEvent(SessionDataBean sessionDataBean, AbstractSpecimen specimen,
			String disposalReason)
	{
		final DisposalEventParameters disposalEvent = new DisposalEventParameters();
		disposalEvent.setSpecimen(specimen);
		disposalEvent.setReason(disposalReason);
		disposalEvent.setTimestamp(new Date(System.currentTimeMillis()));
		final User user = new User();
		user.setId(sessionDataBean.getUserId());
		disposalEvent.setUser(user);
		disposalEvent.setActivityStatus(specimen.getActivityStatus());
		return disposalEvent;
	}
	
	   public boolean hasConsents(Long cpId,HibernateDAO dao) throws DAOException
	    {
	        boolean hasConsents=true;
	        Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
	        substParams.put("0", new NamedQueryParam(DBTypes.STRING, String.valueOf(cpId)));
	        List<ConsentTierStatus> consents = dao.executeNamedQuery(
	                "getconsentTierCollection", substParams);
	        if(consents.isEmpty())
	        {
	            hasConsents=false;
	        }
	        return hasConsents;
	    }
	   
	   public Long getAssociatedIdentifiedReportId(Long specimenId, HibernateDAO hibernateDao)
	            throws ApplicationException
	    {
	        Long valueToReturn = null;
	        Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
            substParams.put("0", new NamedQueryParam(DBTypes.LONG,specimenId));
	        final List<Long> reportIDList =  hibernateDao.executeNamedQuery(
                    Specimen.class.getName()+".getAssociatedIdentifiedReportId", substParams);
	      
	        if (reportIDList != null && !reportIDList.isEmpty())
	        {
	            valueToReturn = ((Long) reportIDList.get(0));
	        }
	        return valueToReturn;
	    }
	   
	   /**
	     * @param sessionData
	     * @param specimenid
	     * @return
	     * @throws ApplicationException
	     * @throws DAOException
	     */
	    public List<Object> getcpIdandPartId(Long specimenId, HibernateDAO hibernateDao)
	            throws ApplicationException, DAOException
	    {

	        final String hql1 = 
	       null;
	        Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
            substParams.put("0", new NamedQueryParam(DBTypes.LONG,specimenId));
            List<Object> list =   hibernateDao.executeNamedQuery(
                    Specimen.class.getName()+".getcpIdandPartId", substParams);
          
	        return list;
	    }
	    


}