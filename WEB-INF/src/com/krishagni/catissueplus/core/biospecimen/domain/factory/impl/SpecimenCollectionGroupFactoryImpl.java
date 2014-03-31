
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;
import static com.krishagni.catissueplus.core.common.CommonValidator.isValidPv;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ScgErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenCollectionGroupFactory;
import com.krishagni.catissueplus.core.biospecimen.events.ScgDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.util.Status;

import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.Site;

public class SpecimenCollectionGroupFactoryImpl implements SpecimenCollectionGroupFactory {

	private DaoFactory daoFactory;

	private static final String CLINICAL_DIAGNOSIS = "clinical diagnosis";

	private static final String CLINICAL_STATUS = "clinical status";

	private static final String SCG_COLLECTION_STATUS = "Collection Status";

	private static final String CPE = "collection protocol event";

	private static final String CPR = "collection protocol registration";

	private static final String SITE = "site name";

	private static final String NAME = "name";

	private static final String COLLECTION_CONTAINER = "collection container";

	private static final String COLLECTION_PROCEDURE = "collection procedure";

	private static final String COLLECTOR = "collector name";

	private static final String RECEIVER = "receiver name";

	private static final String CPR_CPE = "registraion and event point refering to different protocols.";

	private static final String RECEIVED_QUALITY = "received quality";

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public SpecimenCollectionGroup createScg(ScgDetail scgDetail) {
		ObjectCreationException errorHandler = new ObjectCreationException();
		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		setCpe(scgDetail, scg,errorHandler);
		setCpr(scgDetail, scg,errorHandler);
		setCollectionStatus(scgDetail, scg,errorHandler);
		setClinicalStatus(scgDetail, scg,errorHandler);
		setSite(scgDetail, scg,errorHandler);
		setActivityStatus(scgDetail, scg,errorHandler);
		setBarcode(scgDetail, scg,errorHandler);
		setName(scgDetail, scg,errorHandler);
		setClinicalDiagnosis(scgDetail, scg,errorHandler);
		setCollectionEventDetails(scgDetail, scg,errorHandler);
		setReceivedEventDetails(scgDetail, scg,errorHandler);
		setComments(scgDetail, scg);
		setSprNumber(scgDetail, scg);
		validateCprAndCpe(scg,errorHandler);//this should be part of factory
		errorHandler.checkErrorAndThrow();
		return scg;
	}

	private void setCpe(ScgDetail scgDetail, SpecimenCollectionGroup scg, ObjectCreationException errorHandler) {
		CollectionProtocolEvent cpe = daoFactory.getCollectionProtocolDao().getCpe(scgDetail.getCpeId());
		if (cpe == null) {
			errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, CPE);
			return;
		}
		scg.setCollectionProtocolEvent(cpe);
	}

	private void setCpr(ScgDetail scgDetail, SpecimenCollectionGroup scg, ObjectCreationException errorHandler) {
		CollectionProtocolRegistration cpr = daoFactory.getCprDao().getCpr(scgDetail.getCprId());
		if (cpr == null) {
			errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, CPR);
			return;
		}
		scg.setCollectionProtocolRegistration(cpr);
	}

	private void setClinicalStatus(ScgDetail scgDetail, SpecimenCollectionGroup scg, ObjectCreationException errorHandler) {
		if (isValidPv(scgDetail.getClinicalStatus(), CLINICAL_STATUS)) {
			scg.setClinicalStatus(scgDetail.getClinicalStatus());
			return;
		}
		errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, CLINICAL_STATUS);
	}

	private void setSite(ScgDetail scgDetail, SpecimenCollectionGroup scg, ObjectCreationException errorHandler) {
		if (scg.isCompleted() && isBlank(scgDetail.getCollectionSiteName())) {
			errorHandler.addError(ScgErrorCode.MISSING_ATTR_VALUE, SITE);
			return;
		}
		else {
			Site site = daoFactory.getSiteDao().getSite(scgDetail.getCollectionSiteName());
			if (site == null) {
				errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, SITE);
				return;
			}
			scg.setCollectionSite(site);
		}

	}

	private void setActivityStatus(ScgDetail scgDetail, SpecimenCollectionGroup scg, ObjectCreationException errorHandler) {
		if (isValidPv(scgDetail.getActivityStatus(), Status.ACTIVITY_STATUS.getStatus())) {
			scg.setActivityStatus(scgDetail.getActivityStatus());
			return;
		}
		errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, Status.ACTIVITY_STATUS.getStatus());
	}

	private void setBarcode(ScgDetail scgDetail, SpecimenCollectionGroup scg, ObjectCreationException errorHandler) {
		scg.setBarcode(scgDetail.getBarcode());
	}

	private void setName(ScgDetail scgDetail, SpecimenCollectionGroup scg, ObjectCreationException errorHandler) {
		if (!scg.isCompleted() && isBlank(scgDetail.getName())) {
			errorHandler.addError(ScgErrorCode.MISSING_ATTR_VALUE, NAME);
			return;
		}
		scg.setName(scgDetail.getName());

	}

	private void setClinicalDiagnosis(ScgDetail scgDetail, SpecimenCollectionGroup scg, ObjectCreationException errorHandler) {
		if (isValidPv(scg.getClinicalDiagnosis(), CLINICAL_DIAGNOSIS)) {
			scg.setClinicalDiagnosis(scg.getClinicalDiagnosis());
			return;
		}
		errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, CLINICAL_DIAGNOSIS);
	}

	private void setCollectionEventDetails(ScgDetail scgDetail, SpecimenCollectionGroup scg, ObjectCreationException errorHandler) {

		setCollectionContainer(scgDetail, scg,errorHandler);
		setCollectionProcedure(scgDetail, scg,errorHandler);
		scg.setCollector(getUser(scgDetail.getCollectorName(), COLLECTOR,errorHandler));
		scg.setCollectionTimestamp(scgDetail.getCollectionTimestamp());
		scg.setCollectionComments(scgDetail.getCollectionComments());
	}

	private void setCollectionContainer(ScgDetail scgDetail, SpecimenCollectionGroup scg, ObjectCreationException errorHandler) {
		if (isValidPv(scgDetail.getCollectionContainer(), COLLECTION_CONTAINER)) {
			scg.setCollectionContainer(scgDetail.getCollectionContainer());
			return;
		}
		errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, COLLECTION_CONTAINER);
	}

	private void setCollectionProcedure(ScgDetail scgDetail, SpecimenCollectionGroup scg, ObjectCreationException errorHandler) {
		if (isValidPv(scgDetail.getCollectionProcedure(), COLLECTION_PROCEDURE)) {
			scg.setCollectionProcedure(scgDetail.getCollectionProcedure());
			return;
		}
		errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, COLLECTION_PROCEDURE);
	}

	private User getUser(String userLoginName, String message, ObjectCreationException errorHandler) {
		User user = daoFactory.getUserDao().getUser(userLoginName);
		if (user == null) {
			errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, message);
		}
		return user;
	}

	private void setReceivedEventDetails(ScgDetail scgDetail, SpecimenCollectionGroup scg, ObjectCreationException errorHandler) {
		setReveivedQuality(scgDetail, scg,errorHandler);
		scg.setReceiver(getUser(scgDetail.getReceiverName(), RECEIVER,errorHandler));
		scg.setReceivedComments(scgDetail.getReceivedComments());
		scg.setReceivedTimestamp(scgDetail.getReceivedTimestamp());
	}

	private void setReveivedQuality(ScgDetail scgDetail, SpecimenCollectionGroup scg, ObjectCreationException errorHandler) {
		if (isValidPv(scgDetail.getReceivedQuality(), RECEIVED_QUALITY)) {
			scg.setReceivedQuality(scgDetail.getReceivedQuality());
			return;
		}
		errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, RECEIVED_QUALITY);
	}

	private void setCollectionStatus(ScgDetail scgDetail, SpecimenCollectionGroup scg, ObjectCreationException errorHandler) {
		if (isValidPv(scgDetail.getClinicalStatus(), SCG_COLLECTION_STATUS)) {
			scg.setCollectionStatus(scgDetail.getCollectionStatus());
			return;
		}
		errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, SCG_COLLECTION_STATUS);
	}

	private void setComments(ScgDetail scgDetail, SpecimenCollectionGroup scg) {
		scg.setComment(scgDetail.getComment());
	}

	private void setSprNumber(ScgDetail scgDetail, SpecimenCollectionGroup scg) {
		scg.setSurgicalPathologyNumber(scgDetail.getSurgicalPathologyNumber());
	}

	private void validateCprAndCpe(SpecimenCollectionGroup scg, ObjectCreationException errorHandler) {
		if (scg.getCollectionProtocolRegistration() != null && scg.getCollectionProtocolEvent() != null) {
			if (!scg.getCollectionProtocolRegistration().getCollectionProtocol().getId()
					.equals(scg.getCollectionProtocolEvent().getCollectionProtocol().getId())) {
				errorHandler.addError(ScgErrorCode.INVALID_CPR_CPE, CPR_CPE);
			}
		}
	}

//	private void addError(CatissueErrorCode event, String field) {
//		objectCreationException.addError(event, field);
//	}

}
