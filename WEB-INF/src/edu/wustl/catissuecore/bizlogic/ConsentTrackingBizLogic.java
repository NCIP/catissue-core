
package edu.wustl.catissuecore.bizlogic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.dto.ConsentTierDTO;
import edu.wustl.catissuecore.dto.ConsentResponseDto;
import edu.wustl.catissuecore.dto.ParticipantConsentFileDTO;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;

public class ConsentTrackingBizLogic
{

	public final String CONSENT_LEVEL_SPECIMEN = "specimen";
	public final String CONSENT_LEVEL_PARTICIPANT = "participant";
	public final String CONSENT_LEVEL_SCG = "scg";

	public ConsentResponseDto getConsentList(String consentLevel, Long consentLevelId, DAO dao)
			throws ApplicationException
	{

		String signedConsentURL = null;
		User witness = null;
		Date consentSignDate = null;
		Iterator ite = null;
		List<ConsentTierDTO> consentTierList = null;
		List<ColumnValueBean> parameters = new ArrayList<ColumnValueBean>();
		parameters.add(new ColumnValueBean(consentLevelId));
		Collection consentDetails;

		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		params.put("0", new NamedQueryParam(DBTypes.LONG, consentLevelId));

		if (CONSENT_LEVEL_SPECIMEN.equals(consentLevel))
		{
			consentDetails = ((HibernateDAO) dao).executeNamedQuery("specimenConsentQuery", params);
			ite = consentDetails.iterator();
			consentTierList = getConsentTierList(((HibernateDAO) dao).executeNamedQuery(
					"specimenConsetTierQuery", params));

		}
		else if (CONSENT_LEVEL_PARTICIPANT.equals(consentLevel))
		{
			consentDetails = ((HibernateDAO) dao).executeNamedQuery("cprConsentQuery", params);
			ite = consentDetails.iterator();
			List<ConsentTier> cpConsentTierList =  ((HibernateDAO) dao).executeNamedQuery("cpConsentTierList", params);
			consentTierList = getCprConsentTierList(((HibernateDAO) dao).executeNamedQuery(
					"cprConsetTierQuery", params),cpConsentTierList);

		}
		else if (CONSENT_LEVEL_SCG.equals(consentLevel))
		{
			consentDetails = ((HibernateDAO) dao).executeNamedQuery("scgConsentQuery", params);
			ite = consentDetails.iterator();
			consentTierList = getConsentTierList(((HibernateDAO) dao).executeNamedQuery(
					"scgConsetTierQuery", params));
		}

		if (ite.hasNext())
		{
			Object[] arr = (Object[]) ite.next();
			signedConsentURL = arr[0] != null ? String.valueOf(arr[0]) : null;
			witness = arr[1] != null ? (User) arr[1] : null;
			consentSignDate = arr[2] != null ? (Date) arr[2] : null;

		}
		String witnessName = "";
		Long witnessId = 0l;
		if (witness == null)
		{
			witnessName = "";

		}
		else
		{
			final String witnessFullName = witness.getLastName() + ", " + witness.getFirstName();
			witnessName = witnessFullName;
			witnessId = witness.getId();
		}

		ConsentResponseDto consentsDto = new ConsentResponseDto();
		consentsDto.setConsentTierList(consentTierList);
		consentsDto.setConsentUrl(signedConsentURL);
		consentsDto.setConsentDate(consentSignDate);
		consentsDto.setWitnessName(witnessName);
		consentsDto.setWitnessId(witnessId);
		consentsDto.setConsentLevel(consentLevel);
		consentsDto.setConsentLevelId(consentLevelId);

		return consentsDto;

	}

	private List<ConsentTierDTO> getCprConsentTierList(List consentDetailList,List<ConsentTier> cpConsentTierList)
			throws ApplicationException
	{
		Set<ConsentTierDTO> consentTierList = new HashSet<ConsentTierDTO>();
		Iterator ite = consentDetailList.iterator();
		ConsentTierDTO dto;
		while (ite.hasNext())
		{
			Object[] arr = (Object[]) ite.next();
			dto = new ConsentTierDTO();
			dto.setConsentStatment(String.valueOf(arr[0]));
			dto.setParticipantResponses(String.valueOf(arr[1]));
			dto.setId((Long) arr[2]);
			consentTierList.add(dto);
		}
		for(int i = 0;i<cpConsentTierList.size();i++){
		    
		        dto = new ConsentTierDTO();
	            dto.setConsentStatment(cpConsentTierList.get(i).getStatement());
	            dto.setParticipantResponses(Constants.NOT_SPECIFIED);
	            dto.setId(cpConsentTierList.get(i).getId());
	            consentTierList.add(dto);
		}
		return new ArrayList<ConsentTierDTO>(consentTierList);

	}

	private List<ConsentTierDTO> getConsentTierList(List consentDetailList)
			throws ApplicationException
	{
		List<ConsentTierDTO> consentTierList = new ArrayList<ConsentTierDTO>();
		Iterator ite = consentDetailList.iterator();
		ConsentTierDTO dto;
		while (ite.hasNext())
		{
			Object[] arr = (Object[]) ite.next();
			dto = new ConsentTierDTO();
			dto.setConsentStatment(String.valueOf(arr[0]));
			dto.setParticipantResponses(String.valueOf(arr[1]));
			dto.setStatus(String.valueOf(arr[2]));
			dto.setId((Long) arr[3]);
			//	dto.setConsentStatusId((Long) arr[4]);
			consentTierList.add(dto);
		}
		return consentTierList;

	}

	public String updateConsentTier(ConsentResponseDto consentDto, DAO dao,
			SessionDataBean sessionDataBean) throws ApplicationException
	{
		String retString = "";
		if (CONSENT_LEVEL_SPECIMEN.equals(consentDto.getConsentLevel()))
		{
			NewSpecimenBizLogic specimenBizlogic = new NewSpecimenBizLogic();
			specimenBizlogic.updateSpecimenConsentStatus(consentDto.getConsentLevelId(),
					consentDto.getConsentTierList(), consentDto.isDisposeSpecimen(), dao,
					sessionDataBean);
		}
		else if (CONSENT_LEVEL_PARTICIPANT.equals(consentDto.getConsentLevel()))
		{
			ParticipantBizLogic participantBizLogic = new ParticipantBizLogic();
			participantBizLogic.updateConsentResponse(consentDto, consentDto.isDisposeSpecimen(),
					dao, sessionDataBean);
		}
		else if (CONSENT_LEVEL_SCG.equals(consentDto.getConsentLevel()))
		{
			SpecimenCollectionGroupBizLogic scgBizLogic = new SpecimenCollectionGroupBizLogic();
			scgBizLogic.updateScgConsentStatus(consentDto.getConsentLevelId(),
					consentDto.getConsentTierList(), consentDto.isDisposeSpecimen(), dao,
					sessionDataBean);
		}

		return retString;
	}
	public ParticipantConsentFileDTO getParticipantConsentFileDetails(Long cprId, HibernateDAO dao)
			throws ApplicationException {
		String fileName = null;
		byte[] byteArr= {};
		ParticipantConsentFileDTO participantConsentFileDTO=new ParticipantConsentFileDTO();
		try {
			
			final ColumnValueBean columnValueBean = new ColumnValueBean("id",cprId);
			final List<ColumnValueBean> columnValueBeanList = new ArrayList<ColumnValueBean>();
			columnValueBeanList.add(columnValueBean);
			
			final List resultList = dao.executeNameQueryParamHQL("getConsentFileName", columnValueBeanList);
			
			final Iterator iterator = resultList.iterator();
			if (iterator.hasNext()) {
				fileName = (String) iterator.next();
			}
			String consentDir = XMLPropertyHandler.getValue(Constants.PARTICIPANT_CONSENT_DOC_DIR_LOCATION);
			File file = new File(consentDir + "/" + fileName);
			FileInputStream fin = new FileInputStream(file);
			byteArr = IOUtils.toByteArray(fin);
			participantConsentFileDTO.setFileName(fileName);
			participantConsentFileDTO.setByteArr(byteArr);
		
		} catch (ApplicationException ex) {
			throw new BizLogicException(ex.getErrorKey(), ex, ex.getMessage());
			
		}catch (IOException  ex) {
			throw new BizLogicException(null, null, ex.getMessage());
			
		} 
		return participantConsentFileDTO;

	}

}
