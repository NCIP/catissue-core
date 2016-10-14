package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.PvAttributes.CONSENT_RESPONSE;
import static com.krishagni.catissueplus.core.common.service.PvValidator.isValid;

import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

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
	public ConsentResponses createConsentResponses(CollectionProtocolRegistration cpr, ConsentDetail detail) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

		ConsentResponses consentResponses = new ConsentResponses();
		setConsentSignDate(detail, cpr, consentResponses);
		setConsentWitness(detail, cpr, consentResponses, ose);
		setConsentComments(detail, cpr, consentResponses);
		setConsentResponses(detail, cpr, consentResponses, ose);

		ose.checkAndThrow();
		return consentResponses;
	}
	
	private void setConsentSignDate(ConsentDetail detail, CollectionProtocolRegistration cpr, ConsentResponses consentResponses) {
		if (detail.isAttrModified("consentSignatureDate")) {
			consentResponses.setConsentSignDate(detail.getConsentSignatureDate());
		} else {
			consentResponses.setConsentSignDate(cpr.getConsentSignDate());
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
	
	private void setConsentWitness(
			ConsentDetail detail,
			CollectionProtocolRegistration cpr,
			ConsentResponses consentResponses,
			OpenSpecimenException ose) {

		if (detail.isAttrModified("witness")) {
			setConsentWitness(detail, consentResponses, ose);
		} else {
			consentResponses.setConsentWitness(cpr.getConsentWitness());
		}
	}
	
	private void setConsentComments(ConsentDetail detail, CollectionProtocolRegistration cpr, ConsentResponses consentResponses) {
		if (detail.isAttrModified("comments")) {
			consentResponses.setConsentComments(detail.getComments());
		} else {
			consentResponses.setConsentComments(cpr.getConsentComments());
		}
	}

	private void setConsentResponses(
			ConsentDetail detail,
			CollectionProtocolRegistration cpr,
			ConsentResponses consentResponses,
			OpenSpecimenException ose) {

		Map<String, ConsentTierResponse> responsesMap = cpr.getConsentResponses().stream()
				.collect(Collectors.toMap(ConsentTierResponse::getStatement, ConsentTierResponse::copy));

		for (ConsentTierResponseDetail responseDetail : detail.getConsentTierResponses()) {
			ConsentTierResponse response = createConsentTierResponse(
					responseDetail, cpr, responsesMap.get(responseDetail.getStatement()), ose);
			if (response != null) {
				responsesMap.put(response.getConsentTier().getStatement(), response);
			}
		}

		consentResponses.setConsentResponses(new HashSet<>(responsesMap.values()));
	}

	private ConsentTierResponse createConsentTierResponse(
			ConsentTierResponseDetail detail,
			CollectionProtocolRegistration cpr,
			ConsentTierResponse response,
			OpenSpecimenException ose) {

		if (response == null) {
			response = new ConsentTierResponse();
			response.setCpr(cpr);

			for (ConsentTier consentTier : cpr.getCollectionProtocol().getConsentTier()) {
				if (consentTier.getStatement().equals(detail.getStatement())) {
					response.setConsentTier(consentTier);
					break;
				}
			}

			if (response.getConsentTier() == null) {
				ose.addError(CprErrorCode.INVALID_CONSENT_STATEMENT, detail.getStatement());
				return null;
			}
		}

		String respStr = detail.getResponse();
		if (!isValid(CONSENT_RESPONSE, respStr)) {
			ose.addError(CprErrorCode.INVALID_CONSENT_RESPONSE, respStr);
			return null;
		}

		response.setResponse(respStr);
		return response;
	}
}
