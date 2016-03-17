package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.PvAttributes.CONSENT_RESPONSE;
import static com.krishagni.catissueplus.core.common.service.PvValidator.isValid;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentResponses;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentTier;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentTierResponse;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ConsentResponsesFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CprErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierResponseDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class ConsentResponsesFactoryImpl implements ConsentResponsesFactory {
	
	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public ConsentResponses createConsentResponses(ConsentDetail detail) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

		ConsentResponses consentResponses = new ConsentResponses();
		setConsentSignDate(detail, consentResponses);
		setConsentWitness(detail, consentResponses, ose);
		setConsentComments(detail, consentResponses);
		setConsentResponses(detail, consentResponses, ose);

		ose.checkAndThrow();
		return consentResponses;
	}
	
	private void setConsentSignDate(ConsentDetail detail, ConsentResponses consentResponses) {
		if (detail.getConsentSignatureDate() != null) {
			consentResponses.setConsentSignDate(detail.getConsentSignatureDate());
		}				
	}
	
	private void setConsentWitness(ConsentDetail detail, ConsentResponses consentResponses, OpenSpecimenException ose) {
		UserSummary user = detail.getWitness();
		if (user == null) {
			return;
		}
		
		User witness = null;
		if (user.getId() != null) {
			witness = daoFactory.getUserDao().getById(user.getId());
		} else if (user.getEmailAddress() != null) {
			witness = daoFactory.getUserDao().getUserByEmailAddress(user.getEmailAddress());
		} else if (user.getLoginName() != null && user.getDomain() != null) {
			witness = daoFactory.getUserDao().getUser(user.getLoginName(), user.getDomain());
		}
		
		if (witness == null) {
			ose.addError(CprErrorCode.CONSENT_WITNESS_NOT_FOUND);
		}
		
		consentResponses.setConsentWitness(witness);
	}
	
	private void setConsentComments(ConsentDetail detail, ConsentResponses consentResponses) {
		consentResponses.setConsentComments(detail.getComments());
	}
	
	private void setConsentResponses(ConsentDetail detail, ConsentResponses consentResponses, OpenSpecimenException ose) {
		Set<ConsentTierResponse> consentTierResponses = new HashSet<ConsentTierResponse>();

		CollectionProtocolRegistration cpr = getCpr(detail.getCprId(), detail.getCpId(), detail.getPpid());
		if (cpr == null) {
			throw OpenSpecimenException.userError(CprErrorCode.NOT_FOUND);
		}

		for (ConsentTierResponseDetail consentResponse : detail.getConsentTierResponses()) {
			if (StringUtils.isBlank(consentResponse.getResponse())) {
				continue;
			}
			
			ConsentTierResponse response = new ConsentTierResponse();
			response.setCpr(cpr);
			
			for (ConsentTier consentTier : cpr.getCollectionProtocol().getConsentTier()) { 
				if (consentTier.getStatement().equals(consentResponse.getStatement())) {
					response.setConsentTier(consentTier);
					break;
				}
			}
			
			if (response.getConsentTier() == null) {
				ose.addError(CprErrorCode.INVALID_CONSENT_STATEMENT, consentResponse.getStatement());
			}
			
			String resp = consentResponse.getResponse();
			if (!isValid(CONSENT_RESPONSE, resp)) {
				ose.addError(CprErrorCode.INVALID_CONSENT_RESPONSE, resp);
				return;
			}
			response.setResponse(resp);
			
			consentTierResponses.add(response);
		}

		consentResponses.setConsentResponses(consentTierResponses);		
	}
	
	private CollectionProtocolRegistration getCpr(Long cprId, Long cpId, String ppid) {
		CollectionProtocolRegistration cpr = null;
		if (cprId != null) {
			cpr = daoFactory.getCprDao().getById(cprId);
		} else if (cpId != null && StringUtils.isNotBlank(ppid)) {
			cpr = daoFactory.getCprDao().getCprByPpid(cpId, ppid);
		}
		
		if (cpr == null) {
			throw OpenSpecimenException.userError(CprErrorCode.NOT_FOUND);
		}
		
		return cpr;
	}
}
