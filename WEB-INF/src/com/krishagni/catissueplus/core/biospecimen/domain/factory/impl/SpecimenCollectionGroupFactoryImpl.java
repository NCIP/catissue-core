
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;
import static com.krishagni.catissueplus.core.common.CommonValidator.isValidPv;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ScgErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenCollectionGroupFactory;
import com.krishagni.catissueplus.core.biospecimen.events.ScgDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;

public class SpecimenCollectionGroupFactoryImpl implements SpecimenCollectionGroupFactory {

	private DaoFactory daoFactory;

	private static final String CLINICAL_DIAGNOSIS = "clinical diagnosis";

	private static final String CLINICAL_STATUS = "clinical status";

	private static final String SCG_COLLECTION_STATUS = "Collection Status";

	private static final String CPE = "collection protocol event";

	private static final String CPR = "collection protocol registration";

	private static final String SITE = "site";

	private static final String NAME = "name";

	private static final String ACTIVITY_STATUS = "activity status";

	private static final String COLLECTION_CONTAINER = "collection container";

	private static final String COLLECTION_PROCEDURE = "collection procedure";

	private static final String COLLECTOR = "collector name";

	private static final String RECEIVER = "receiver name";

	private static final String RECEIVED_QUALITY = "received quality";

	private List<ErroneousField> erroneousFields = new ArrayList<ErroneousField>();

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public SpecimenCollectionGroup createScg(ScgDetail scgDetail, ObjectCreationException exceptionHandler) {
		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		setCpe(scgDetail, scg);
		setCpr(scgDetail, scg);
		setSite(scgDetail, scg);
		setActivityStatus(scgDetail, scg);
		setBarcode(scgDetail, scg);
		setName(scgDetail, scg);
		setClinicalDiagnosis(scgDetail, scg);
		setClinicalStatus(scgDetail, scg);
		setCollectionEventDetails(scgDetail, scg);
		setReceivedEventDetails(scgDetail, scg);
		setCollectionStatus(scgDetail, scg);
		setComments(scgDetail, scg);
		setSprNumber(scgDetail, scg);
		exceptionHandler.addError(erroneousFields);
		return scg;
	}

	private void setCpe(ScgDetail scgDetail, SpecimenCollectionGroup scg) {
		CollectionProtocolEvent cpe = daoFactory.getCollectionProtocolDao().getCpe(scgDetail.getCpeId());
		if (cpe == null) {
			addError(ScgErrorCode.INVALID_ATTR_VALUE, CPE);
			return;
		}
		scg.setCollectionProtocolEvent(cpe);
	}

	private void setCpr(ScgDetail scgDetail, SpecimenCollectionGroup scg) {
		CollectionProtocolRegistration cpr = daoFactory.getCprDao().getCpr(scgDetail.getCprId());
		if (cpr == null) {
			addError(ScgErrorCode.INVALID_ATTR_VALUE, CPR);
			return;
		}
		scg.setCollectionProtocolRegistration(cpr);
	}

	private void setSite(ScgDetail scgDetail, SpecimenCollectionGroup scg) {
		Site site = daoFactory.getSiteDao().getSite(scgDetail.getCollectionSiteName());
		if (site == null) {
			addError(ScgErrorCode.INVALID_ATTR_VALUE, SITE);
			return;
		}
		scg.setCollectionSite(site);
	}

	private void setActivityStatus(ScgDetail scgDetail, SpecimenCollectionGroup scg) {
		if (isValidPv(scgDetail.getActivityStatus(), ACTIVITY_STATUS)) {
			scg.setActivityStatus(scgDetail.getActivityStatus());
			return;
		}
		addError(ScgErrorCode.INVALID_ATTR_VALUE, ACTIVITY_STATUS);
	}

	private void setBarcode(ScgDetail scgDetail, SpecimenCollectionGroup scg) {
		scg.setBarcode(scgDetail.getBarcode());
	}

	private void setName(ScgDetail scgDetail, SpecimenCollectionGroup scg) {
		if (isBlank(scgDetail.getName())) {//mandatory if activity status is active
			addError(ScgErrorCode.MISSING_ATTR_VALUE, NAME);
			return;
		}
		scg.setName(scgDetail.getName());
		
	}

	private void setClinicalDiagnosis(ScgDetail scgDetail, SpecimenCollectionGroup scg) {
		if (isValidPv(scg.getClinicalDiagnosis(), CLINICAL_DIAGNOSIS)) {
			scg.setClinicalDiagnosis(scg.getClinicalDiagnosis());
			return;
		}
		addError(ScgErrorCode.INVALID_ATTR_VALUE, CLINICAL_DIAGNOSIS);
	}

	private void setClinicalStatus(ScgDetail scgDetail, SpecimenCollectionGroup scg) {
		if (isValidPv(scgDetail.getClinicalStatus(), CLINICAL_STATUS)) {
			scg.setClinicalStatus(scgDetail.getClinicalStatus());
			return;
		}
		addError(ScgErrorCode.INVALID_ATTR_VALUE, CLINICAL_STATUS);
	}

	private void setCollectionEventDetails(ScgDetail scgDetail, SpecimenCollectionGroup scg) {

		setCollectionContainer(scgDetail, scg);
		setCollectionProcedure(scgDetail, scg);
		scg.setCollector(getUser(scgDetail.getCollectorName(), COLLECTOR));
		scg.setCollectionTimestamp(scgDetail.getCollectionTimestamp());
		scg.setCollectionComments(scgDetail.getCollectionComments());
	}

	private void setCollectionContainer(ScgDetail scgDetail, SpecimenCollectionGroup scg) {
		if (isValidPv(scgDetail.getCollectionContainer(), COLLECTION_CONTAINER)) {
			scg.setCollectionContainer(scgDetail.getCollectionContainer());
			return;
		}
		addError(ScgErrorCode.INVALID_ATTR_VALUE, COLLECTION_CONTAINER);
	}

	private void setCollectionProcedure(ScgDetail scgDetail, SpecimenCollectionGroup scg) {
		if (isValidPv(scgDetail.getCollectionProcedure(), COLLECTION_PROCEDURE)) {
			scg.setCollectionProcedure(scgDetail.getCollectionProcedure());
			return;
		}
		addError(ScgErrorCode.INVALID_ATTR_VALUE, COLLECTION_PROCEDURE);
	}

	private User getUser(String userLoginName, String message) {
		User user = daoFactory.getUserDao().getUser(userLoginName);
		if (user == null) {
			addError(ScgErrorCode.INVALID_ATTR_VALUE, message);
		}
		return user;
	}

	private void setReceivedEventDetails(ScgDetail scgDetail, SpecimenCollectionGroup scg) {
		setReveivedQuality(scgDetail, scg);
		scg.setReceiver(getUser(scgDetail.getReceiverName(), RECEIVER));
		scg.setReceivedComments(scgDetail.getReceivedComments());
		scg.setReceivedTimestamp(scgDetail.getReceivedTimestamp());
	}

	private void setReveivedQuality(ScgDetail scgDetail, SpecimenCollectionGroup scg) {
		if (isValidPv(scgDetail.getReceivedQuality(), RECEIVED_QUALITY)) {
			scg.setReceivedQuality(scgDetail.getReceivedQuality());
			return;
		}
		addError(ScgErrorCode.INVALID_ATTR_VALUE, RECEIVED_QUALITY);
	}

	private void setCollectionStatus(ScgDetail scgDetail, SpecimenCollectionGroup scg) {
		if (isValidPv(scgDetail.getClinicalStatus(), SCG_COLLECTION_STATUS)) {
			scg.setCollectionStatus(scgDetail.getCollectionStatus());
			return;
		}
		addError(ScgErrorCode.INVALID_ATTR_VALUE, SCG_COLLECTION_STATUS);
	}

	private void setComments(ScgDetail scgDetail, SpecimenCollectionGroup scg) {
		scg.setComment(scgDetail.getComment());
	}

	private void setSprNumber(ScgDetail scgDetail, SpecimenCollectionGroup scg) {
		scg.setSurgicalPathologyNumber(scgDetail.getSurgicalPathologyNumber());
	}

	private void addError(CatissueErrorCode event, String field) {
		erroneousFields.add(new ErroneousField(event, field));
	}

}
