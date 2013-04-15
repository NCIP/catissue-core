package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.dto.ConsentTierDTO;
import edu.wustl.catissuecore.dto.ConsentResponseDto;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.dao.DAO;
import edu.wustl.dao.query.generator.ColumnValueBean;

public class ConsentTrackingBizLogic {
	public final String CONSENT_LEVEL_SPECIMEN = "specimen";
	public final String CONSENT_LEVEL_PARTICIPANT = "participant";
	public final String CONSENT_LEVEL_SCG = "scg";
	
	private final String SPECIMEN_CONSENT_QUERY =  "select cpr.signedConsentDocumentURL,consentWitness,cpr.consentSignatureDate " +
			" from Specimen specimen join specimen.specimenCollectionGroup scg join scg.collectionProtocolRegistration cpr left join cpr.consentWitness consentWitness where specimen.id = ?";
	
	private final String CONSENT_QUERY =  "select cpr.signedConsentDocumentURL,consentWitness,cpr.consentSignatureDate " +
			" from CollectionProtocolRegistration cpr  left join cpr.consentWitness consentWitness  where cpr.id = ?";
	
	private final String SELECT_CONSENT_TIRE_QUERY = "select  consentTier.statement,consentTierResponse.response,consentTierStatus.status,consentTier.id,consentTierStatus.id " +
			" from Specimen specimen join specimen.specimenCollectionGroup scg join scg.collectionProtocolRegistration cpr " +
			" join specimen.consentTierStatusCollection consentTierStatus " +
			" join cpr.consentTierResponseCollection consentTierResponse" +
			" join consentTierStatus.consentTier consentTier" +
			" join consentTierResponse.consentTier consentTier_response" +
			" where specimen.id = ? and consentTierResponse.consentTier.id = consentTierStatus.consentTier.id" ;
	
	private final String CONSENT_TIRE_QUERY = "select consentTierResponse.consentTier.statement,consentTierResponse.response,consentTierResponse.consentTier.id " +
			" from Participant particiapant join particiapant.collectionProtocolRegistrationCollection cpr " +
			" join cpr.consentTierResponseCollection consentTierResponse" +
			" join consentTierResponse.consentTier consentTier " +
			" where cpr.id=?" ;
	
	private final String SCG_CONSENT_QUERY = "select cpr.signedConsentDocumentURL,consentWitness,cpr.consentSignatureDate " +
			" from SpecimenCollectionGroup scg join scg.collectionProtocolRegistration cpr left join cpr.consentWitness consentWitness where scg.id = ?";
	
	private final String SCG_CONSENT_TIER_QUERY = "select consentTier.statement,consentTierResponse.response,consentTierStatus.status,consentTier.id,consentTierStatus.id " +
			" from SpecimenCollectionGroup scg join scg.collectionProtocolRegistration cpr " +
			" join cpr.consentTierResponseCollection consentTierResponse  " +
			" join scg.consentTierStatusCollection consentTierStatus " +
			" join consentTierStatus.consentTier consentTier" +
			" join consentTierResponse.consentTier consentTier_response" +
			" where scg.id = ? and consentTierResponse.consentTier.id = consentTierStatus.consentTier.id" ;
	
	
	public ConsentResponseDto getConsentList(String consentLevel,Long consentLevelId, DAO dao) throws ApplicationException{
		
		String signedConsentURL = null;
		User witness  = null;
		Date consentSignDate =  null;
		Iterator ite = null;
		List<ConsentTierDTO> consentTierList = null;
		List<ColumnValueBean> parameters = new ArrayList<ColumnValueBean>(); 
		parameters.add(new ColumnValueBean(consentLevelId));
		Collection consentDetails;
		if(CONSENT_LEVEL_SPECIMEN.equals(consentLevel)){
			consentDetails = dao.executeQuery(SPECIMEN_CONSENT_QUERY,parameters);
			ite =  consentDetails.iterator();
			consentTierList = getConsentTierList( dao.executeQuery(SELECT_CONSENT_TIRE_QUERY,parameters));
			
		}else if(CONSENT_LEVEL_PARTICIPANT.equals(consentLevel)){
			consentDetails = dao.executeQuery(CONSENT_QUERY,parameters);
			ite =  consentDetails.iterator();
			consentTierList = getCprConsentTierList( dao.executeQuery(CONSENT_TIRE_QUERY,parameters));
		
			
		}else if(CONSENT_LEVEL_SCG.equals(consentLevel)){
			consentDetails = dao.executeQuery(SCG_CONSENT_QUERY,parameters);
			ite =  consentDetails.iterator();
			consentTierList = getConsentTierList( dao.executeQuery(SCG_CONSENT_TIER_QUERY,parameters));
		}
		
		if(ite.hasNext()){
			Object[] arr = (Object[])ite.next();
			signedConsentURL = arr[0]!=null?String.valueOf(arr[0]):null;
			witness  = arr[1]!=null?(User)arr[1]:null;
			consentSignDate =  arr[2]!=null?(Date)arr[2]:null;
					
						}
		String witnessName  = "";
		Long witnessId = 0l;
		if (witness == null)
		{
			witnessName = "";
		
		}
		else
		{
			final String witnessFullName = witness.getLastName() + ", "
					+ witness.getFirstName();
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
	
	private List<ConsentTierDTO> getCprConsentTierList(List consentDetailList) throws ApplicationException{
		List<ConsentTierDTO> consentTierList  = new ArrayList<ConsentTierDTO>();
		Iterator ite = consentDetailList.iterator();
		ConsentTierDTO dto;
		while(ite.hasNext()){
			Object[] arr = (Object[]) ite.next();
			dto = new ConsentTierDTO();
			dto.setConsentStatment(String.valueOf(arr[0]));
			dto.setParticipantResponses(String.valueOf(arr[1]));
			dto.setId((Long) arr[2]);
			consentTierList.add(dto);
		}
		return consentTierList;
		
	}
	
	
	private List<ConsentTierDTO> getConsentTierList(List consentDetailList) throws ApplicationException{
		List<ConsentTierDTO> consentTierList  = new ArrayList<ConsentTierDTO>();
		Iterator ite = consentDetailList.iterator();
		ConsentTierDTO dto;
		while(ite.hasNext()){
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
	
	public String updateConsentTier(ConsentResponseDto consentDto,DAO dao) throws ApplicationException{
		String retString = "";
		if(CONSENT_LEVEL_SPECIMEN.equals(consentDto.getConsentLevel())){
			NewSpecimenBizLogic specimenBizlogic = new NewSpecimenBizLogic();
			specimenBizlogic.updateSpecimenConsentStatus(consentDto.getConsentLevelId(), consentDto.getConsentTierList(), dao);
		}else if(CONSENT_LEVEL_PARTICIPANT.equals(consentDto.getConsentLevel())){
			ParticipantBizLogic participantBizLogic = new ParticipantBizLogic();
			participantBizLogic.updateConsentResponse(consentDto, dao);
		}else if(CONSENT_LEVEL_SCG.equals(consentDto.getConsentLevel())){
			SpecimenCollectionGroupBizLogic scgBizLogic = new SpecimenCollectionGroupBizLogic();
			scgBizLogic.updateScgConsentStatus(consentDto.getConsentLevelId(), consentDto.getConsentTierList(), dao);
		}
		
		return retString;
	}


}
