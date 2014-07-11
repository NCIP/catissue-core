
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;
import static com.krishagni.catissueplus.core.common.CommonValidator.isValidPv;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.auth.domain.factory.AuthenticationType;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ScgErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenCollectionGroupFactory;
import com.krishagni.catissueplus.core.biospecimen.events.ScgDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ScgReportDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.util.Status;

import edu.wustl.catissuecore.domain.CollectionProtocolEvent;

public class SpecimenCollectionGroupFactoryImpl implements SpecimenCollectionGroupFactory {

	private DaoFactory daoFactory;

	private static final String CLINICAL_DIAGNOSIS = "clinical diagnosis";

	private static final String CLINICAL_STATUS = "clinical status";

	private static final String SCG_COLLECTION_STATUS = "Collection Status";

	private static final String CPE = "collection protocol event";

	private static final String CPR = "collection protocol registration";

	private static final String SITE = "site name";

	private static final String BARCODE = "barcode";

	private static final String COLLECTION_CONTAINER = "collection container";

	private static final String COLLECTION_PROCEDURE = "collection procedure";

	private static final String COLLECTOR = "collector name";

	private static final String RECEIVER = "receiver name";

	private static final String CPR_CPE = "registraion and event point refering to different protocols.";

	private static final String RECEIVED_QUALITY = "received quality";

	private static final String SCG_REPORTS = "scg reports";

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public SpecimenCollectionGroup createScg(ScgDetail scgDetail) {
		ObjectCreationException errorHandler = new ObjectCreationException();
		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		setCpe(scgDetail, scg, errorHandler);
		setCpr(scgDetail, scg, errorHandler);
		setCollectionStatus(scgDetail.getCollectionStatus(), scg, errorHandler);
		setClinicalStatus(scgDetail.getClinicalStatus(), scg, errorHandler);
		setSite(scgDetail.getCollectionSiteName(), scg, errorHandler);
		setActivityStatus(scgDetail.getActivityStatus(), scg, errorHandler);
		setClinicalDiagnosis(scgDetail.getClinicalDiagnosis(), scg, errorHandler);
		setCollectionEventDetails(scgDetail, scg, errorHandler);
		setReceivedEventDetails(scgDetail, scg, errorHandler);
		setComments(scgDetail.getComment(), scg, errorHandler);
		setSprNumber(scgDetail.getSurgicalPathologyNumber(), scg);
		validateCprAndCpe(scg, errorHandler);//this should be part of factory
		errorHandler.checkErrorAndThrow();
		return scg;
	}

	@Override
	public SpecimenCollectionGroup patch(SpecimenCollectionGroup scg, Map<String, Object> scgProps) {
		ObjectCreationException errorHandler = new ObjectCreationException();
		Iterator<Entry<String, Object>> entries = scgProps.entrySet().iterator();
		while (entries.hasNext()) {
			Entry<String, Object> entry = entries.next();
			if ("comment".equals(entry.getKey())) {
				setComments(String.valueOf(entry.getValue()), scg, errorHandler);
			}
			if ("clinicalStatus".equals(entry.getKey())) {
				setClinicalStatus(String.valueOf(entry.getValue()), scg, errorHandler);
			}
			if ("clinicalDiagnosis".equals(entry.getKey())) {
				setClinicalDiagnosis(String.valueOf(entry.getValue()), scg, errorHandler);
			}
			if ("activityStatus".equals(entry.getKey())) {
				setActivityStatus(String.valueOf(entry.getValue()), scg, errorHandler);
			}
			if ("collectionStatus".equals(entry.getKey())) {
				setCollectionStatus(String.valueOf(entry.getValue()), scg, errorHandler);
			}
			if ("collectionSiteName".equals(entry.getKey())) {
				setSite(String.valueOf(entry.getValue()), scg, errorHandler);
			}
			if ("collectionContainer".equals(entry.getKey())) {
				setCollectionContainer(String.valueOf(entry.getValue()), scg, errorHandler);
			}
			if ("collectionProcedure".equals(entry.getKey())) {
				setCollectionProcedure(String.valueOf(entry.getValue()), scg, errorHandler);
			}
			if ("collectionComment".equals(entry.getKey())) {
				setCollectionComments(String.valueOf(entry.getValue()), scg, errorHandler);
			}
			if ("collector".equals(entry.getKey())) {
				setCollectionComments(String.valueOf(entry.getValue()), scg, errorHandler);
			}
			if ("collectionSiteName".equals(entry.getKey())) {
				setSite(String.valueOf(entry.getValue()), scg, errorHandler);
			}
		}
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

	private void setClinicalStatus(String clinicalStatus, SpecimenCollectionGroup scg,
			ObjectCreationException errorHandler) {
		if (isValidPv(clinicalStatus, CLINICAL_STATUS)) {
			scg.setClinicalStatus(clinicalStatus);
			return;
		}
		errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, CLINICAL_STATUS);
	}

	private void setSite(String siteName, SpecimenCollectionGroup scg, ObjectCreationException errorHandler) {
		if (scg.isCompleted() && isBlank(siteName)) {
			errorHandler.addError(ScgErrorCode.MISSING_ATTR_VALUE, SITE);
			return;
		}
		else {
			Site site = daoFactory.getSiteDao().getSite(siteName);
			if (site == null) {
				errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, SITE);
				return;
			}
			scg.setCollectionSite(site);
		}

	}

	private void setActivityStatus(String activityStatus, SpecimenCollectionGroup scg,
			ObjectCreationException errorHandler) {
		if (isValidPv(activityStatus, Status.ACTIVITY_STATUS.getStatus())) {
			scg.setActivityStatus(activityStatus);
			return;
		}
		errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, Status.ACTIVITY_STATUS.getStatus());
	}

	private void setClinicalDiagnosis(String clinicalDiagnosis, SpecimenCollectionGroup scg,
			ObjectCreationException errorHandler) {
		if (isValidPv(clinicalDiagnosis, CLINICAL_DIAGNOSIS)) {
			scg.setClinicalDiagnosis(clinicalDiagnosis);
			return;
		}
		errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, CLINICAL_DIAGNOSIS);
	}

	private void setCollectionEventDetails(ScgDetail scgDetail, SpecimenCollectionGroup scg,
			ObjectCreationException errorHandler) {

		setCollectionContainer(scgDetail.getCollectionContainer(), scg, errorHandler);
		setCollectionProcedure(scgDetail.getCollectionProcedure(), scg, errorHandler);
		scg.setCollector(getUser(scgDetail.getCollectorName(), COLLECTOR, errorHandler));
		scg.setCollectionTimestamp(scgDetail.getCollectionTimestamp());
		setCollectionComments(scgDetail.getCollectionComments(), scg, errorHandler);
	}

	private void setCollectionComments(String collectionComments, SpecimenCollectionGroup scg,
			ObjectCreationException errorHandler) {
		scg.setCollectionComments(collectionComments);
	}

	private void setCollectionContainer(String collectionContainer, SpecimenCollectionGroup scg,
			ObjectCreationException errorHandler) {
		if (isValidPv(collectionContainer, COLLECTION_CONTAINER)) {
			scg.setCollectionContainer(collectionContainer);
			return;
		}
		errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, COLLECTION_CONTAINER);
	}

	private void setCollectionProcedure(String collectionProcedure, SpecimenCollectionGroup scg,
			ObjectCreationException errorHandler) {
		if (isValidPv(collectionProcedure, COLLECTION_PROCEDURE)) {
			scg.setCollectionProcedure(collectionProcedure);
			return;
		}
		errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, COLLECTION_PROCEDURE);
	}

	private User getUser(String userLoginName, String message, ObjectCreationException errorHandler) {
		User user = daoFactory.getUserDao().getUserByLoginNameAndDomainName(userLoginName,
				AuthenticationType.CATISSUE.value());
		if (user == null) {
			errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, message);
		}
		return user;
	}

	private void setReceivedEventDetails(ScgDetail scgDetail, SpecimenCollectionGroup scg,
			ObjectCreationException errorHandler) {
		setReveivedQuality(scgDetail, scg, errorHandler);
		scg.setReceiver(getUser(scgDetail.getReceiverName(), RECEIVER, errorHandler));
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

	private void setCollectionStatus(String collectionStatus, SpecimenCollectionGroup scg,
			ObjectCreationException errorHandler) {
		if (isValidPv(collectionStatus, SCG_COLLECTION_STATUS)) {
			scg.setCollectionStatus(collectionStatus);
			return;
		}
		errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, SCG_COLLECTION_STATUS);
	}

	private void setComments(String comment, SpecimenCollectionGroup scg, ObjectCreationException errorHandler) {
		scg.setComment(comment);
	}

	private void setSprNumber(String sprNumber, SpecimenCollectionGroup scg) {
		scg.setSurgicalPathologyNumber(sprNumber);
	}

	private void validateCprAndCpe(SpecimenCollectionGroup scg, ObjectCreationException errorHandler) {
		if (scg.getCollectionProtocolRegistration() != null && scg.getCollectionProtocolEvent() != null) {
			if (!scg.getCollectionProtocolRegistration().getCollectionProtocol().getId()
					.equals(scg.getCollectionProtocolEvent().getCollectionProtocol().getId())) {
				errorHandler.addError(ScgErrorCode.INVALID_CPR_CPE, CPR_CPE);
			}
		}
	}

	@Override
	public SpecimenCollectionGroup updateReports(SpecimenCollectionGroup oldScg, ScgReportDetail detail) {

		ObjectCreationException errorHandler = new ObjectCreationException();
		if (detail.getDeIdentifiedReport() == null && detail.getIdentifiedReport() == null) {
			errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, SCG_REPORTS);
		}

		if (detail.getDeIdentifiedReport() != null) {
			oldScg.setDeIdentifiedReport(detail.getDeIdentifiedReport());
		}

		if (detail.getIdentifiedReport() != null) {
			oldScg.setIdentifiedReport(detail.getIdentifiedReport());
		}
		errorHandler.checkErrorAndThrow();
		return oldScg;
	}

}
