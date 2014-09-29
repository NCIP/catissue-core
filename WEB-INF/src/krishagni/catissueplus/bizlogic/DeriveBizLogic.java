package krishagni.catissueplus.bizlogic;

import krishagni.catissueplus.dto.DerivedDTO;
import krishagni.catissueplus.dto.SpecimenDTO;
import krishagni.catissueplus.util.SpecimenUtil;
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;


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
		specimenDTO.setParentSpecimenBarcode(derivedDTO.getParentSpecimenBarcode());
		specimenDTO.setPos1(derivedDTO.getPos1());
		specimenDTO.setPos2(derivedDTO.getPos2());
		specimenDTO.setQuantity(derivedDTO.getInitialQuantity());
		specimenDTO.setAvailableQuantity(derivedDTO.getInitialQuantity());
		specimenDTO.setType(derivedDTO.getType());
		specimenDTO.setSpecimenCollectionGroupId(derivedDTO.getSpecimenCollGroupId());
		return specimenDTO;
	}


	public SpecimenDTO insertDeriveSpecimen(HibernateDAO hibernateDao, DerivedDTO deriveDTO, SessionDataBean sessionDataBean) throws BizLogicException, DAOException
	{
		SpecimenDTO specimenDTO = getSpecimenDTO(deriveDTO);
		SpecimenBizLogic specimenBizlogic = new SpecimenBizLogic();
		specimenDTO = specimenBizlogic.insert(specimenDTO,hibernateDao,sessionDataBean);
		Specimen parentSpecimen = new Specimen();
		parentSpecimen.setId(specimenDTO.getParentSpecimenId());
		if(deriveDTO.isDisposeParentSpecimen())
		{
			DisposalEventParameters eventParameters = SpecimenUtil.createDisposeEvent(sessionDataBean, parentSpecimen, Constants.SPECIMEN_DISPOSAL_REASON);
			specimenBizlogic.disposeSpecimen(hibernateDao, sessionDataBean, parentSpecimen, eventParameters);
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
