package krishagni.catissueplus.bizlogic;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import krishagni.catissueplus.dto.BiohazardDTO;
import krishagni.catissueplus.dto.DerivedDTO;
import krishagni.catissueplus.dto.SpecimenDTO;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;


public class DeriveBizLogic
{
	private static final Logger LOGGER = Logger.getCommonLogger(DeriveBizLogic.class);
	public SpecimenDTO getSpecimenDTO(DerivedDTO derivedDTO) throws BizLogicException
	{
		SpecimenDTO specimenDTO = new SpecimenDTO();
		specimenDTO.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		specimenDTO.setAvailable(Boolean.TRUE);
		specimenDTO.setAvailableQuantity(derivedDTO.getInitialQuantity());
		specimenDTO.setBarcode(derivedDTO.getBarcode());
		specimenDTO.setLabel(derivedDTO.getLabel());
		specimenDTO.setClassName(derivedDTO.getClassName());
		specimenDTO.setCollectionStatus(Constants.COLLECTION_STATUS_COLLECTED);
		specimenDTO.setComments(derivedDTO.getComments());
		specimenDTO.setConcentration(derivedDTO.getConcentration());
		specimenDTO.setContainerId(derivedDTO.getContainerId());
		specimenDTO.setContainerName(derivedDTO.getContainerName());
		specimenDTO.setCreatedDate(derivedDTO.getCreatedOn());
		specimenDTO.setExternalIdentifiers(derivedDTO.getExternalIdentifiers());
//		specimenDTO.setBioHazards(new HashSet<BiohazardDTO>());
		specimenDTO.setLineage(Constants.DERIVED_SPECIMEN);
		specimenDTO.setParentSpecimenId(derivedDTO.getParentSpecimenId());
		specimenDTO.setParentSpecimenName(derivedDTO.getParentSpecimenLabel());
		specimenDTO.setPos1(derivedDTO.getPos1());
		specimenDTO.setPos2(derivedDTO.getPos2());
		specimenDTO.setQuantity(derivedDTO.getInitialQuantity());
		specimenDTO.setAvailableQuantity(derivedDTO.getInitialQuantity());
		specimenDTO.setType(derivedDTO.getType());
		specimenDTO.setSpecimenCollectionGroupId(derivedDTO.getSpecimenCollGroupId());
		return specimenDTO;
	}

	private Collection<BiohazardDTO> getBiohazardColl(Long parentSpecimenId,
			HibernateDAO hibernateDao) throws BizLogicException
	{
		String sql = "select specimen.biohazardCollection from "+Specimen.class.getName()+" specimen where specimen.id="+parentSpecimenId;
		Collection<BiohazardDTO> biohazardDTOs = new HashSet<BiohazardDTO>();
		try{
			
			List bioList = hibernateDao.executeQuery(sql);
			if(bioList != null && !bioList.isEmpty())
			{
				for (Object object : bioList)
				{
					Biohazard biohazard = (Biohazard)object;
					BiohazardDTO biohazardDTO = new BiohazardDTO();
					biohazardDTO.setId(biohazard.getId());
					biohazardDTO.setName(biohazard.getName());
					biohazardDTO.setType(biohazard.getType());
					biohazardDTO.setStatus("ADD");
					biohazardDTOs.add(biohazardDTO);
				}
			}
		}
		catch (final DAOException daoExp)
		{
			LOGGER.error(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		return biohazardDTOs;
	}

	private SpecimenCharacteristics getSpecimenCharacteristics(Long parentSpecimenId,
			HibernateDAO hibernateDao) throws BizLogicException 
	{
		
		SpecimenCharacteristics characteristics = null;
		Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
		substParams.put("0",new NamedQueryParam(DBTypes.LONG, parentSpecimenId));
		try
		{
			List list = hibernateDao.executeNamedQuery("getSpChars", substParams);
			if(list != null || !list.isEmpty())
			{
				characteristics = (SpecimenCharacteristics)list.get(0);
			}
		}
		catch (final DAOException daoExp)
		{
			LOGGER.error(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		return characteristics;
	}

	public SpecimenDTO insertDeriveSpecimen(HibernateDAO hibernateDao, DerivedDTO deriveDTO, SessionDataBean sessionDataBean) throws BizLogicException
	{
		SpecimenDTO specimenDTO = getSpecimenDTO(deriveDTO);
		SpecimenBizLogic specimenBizlogic = new SpecimenBizLogic();
		specimenDTO = specimenBizlogic.insert(specimenDTO,hibernateDao,sessionDataBean);
		if(deriveDTO.isDisposeParentSpecimen())
		{
			//dispose parent specimen
		}
		return specimenDTO;
	}

	protected BizLogicException getBizLogicException(Exception exception, String key,
			String logMessage)
	{
		LOGGER.debug(logMessage);
		ErrorKey errorKey = ErrorKey.getErrorKey(key);
		return new BizLogicException(errorKey, exception, logMessage);
	}
}
