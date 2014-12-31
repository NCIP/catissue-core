
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;
import static com.krishagni.catissueplus.core.common.CommonValidator.isValidPv;

import java.util.Calendar;
import java.util.Date;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ScgErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitFactory;
import com.krishagni.catissueplus.core.biospecimen.events.ScgReportDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.util.Status;

public class VisitFactoryImpl implements VisitFactory {

	private DaoFactory daoFactory;

	private static final String CLINICAL_DIAGNOSIS = "clinical diagnosis";

	private static final String CLINICAL_STATUS = "clinical status";

	private static final String VISIT_STATUS = "Collection Status";

	private static final String CPE = "No event identification";
	
	private static final String CPL = "collection point label";

	private static final String CPR = "No Collection Protocol Registration Identification Specified";
	
	private static final String CP_TITLE = "collection protocol title";
	
	private static final String PPID = "participant protocol id";
	
	private static final String SITE = "site name";

	private static final String CPR_CPE = "registraion and event point refering to different protocols.";

	private static final String SCG_REPORTS = "scg reports";

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public SpecimenCollectionGroup createVisit(VisitDetail visitDetail) {
		ObjectCreationException oce = new ObjectCreationException();
		
		SpecimenCollectionGroup visit = new SpecimenCollectionGroup();
		
		setCpe(visit, visitDetail, oce);		
		setCpr(visit, visitDetail, oce);
		validateCprAndCpe(visit, oce);
		
		setVisitDate(visit, visitDetail, oce);
		setVisitStatus(visit, visitDetail.getVisitStatus(), oce);
		setClinicalDiagnosis(visit, visitDetail.getClinicalDiagnosis(), oce);
		setClinicalStatus(visit, visitDetail.getClinicalStatus(), oce);
		setSite(visit, visitDetail.getVisitSite(), oce);
		setActivityStatus(visit, visitDetail.getActivityStatus(), oce);
		
		visit.setComment(visitDetail.getComment());
		visit.setSurgicalPathologyNumber(visitDetail.getSurgicalPathologyNumber());
		
		oce.checkErrorAndThrow();
		return visit;
	}

	private void setCpe(SpecimenCollectionGroup visit, VisitDetail visitDetail, ObjectCreationException oce) {
		CollectionProtocolEvent cpe = null;
		
		if (visitDetail.getCpeId() != null) {
			cpe = daoFactory.getCollectionProtocolDao().getCpe(visitDetail.getCpeId());
		} else if (visitDetail.getEventLabel() != null && visitDetail.getCpTitle() != null) {
			CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getCollectionProtocol(visitDetail.getCpTitle());
			if (cp == null) {
				oce.addError(ScgErrorCode.INVALID_ATTR_VALUE, CP_TITLE);
				return;
			}
			
			cpe = daoFactory.getCollectionProtocolDao().getCpeByEventLabel(cp.getId(), visitDetail.getEventLabel());			
			if (cpe == null) {
				oce.addError(ScgErrorCode.INVALID_ATTR_VALUE, CPL);
				return;
			}
		}
		
		if (cpe == null) {
			oce.addError(ScgErrorCode.INVALID_ATTR_VALUE, CPE);
			return;
		}
		
		visit.setCollectionProtocolEvent(cpe);
	}

	private void setCpr(SpecimenCollectionGroup visit, VisitDetail visitDetail, ObjectCreationException oce) {
		CollectionProtocolRegistration cpr = null;
		
		if (visitDetail.getCprId() != null) {
			cpr = daoFactory.getCprDao().getById(visitDetail.getCprId());
		} else if (visitDetail.getPpid() != null && visitDetail.getCpTitle() != null) {
			CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getCollectionProtocol(visitDetail.getCpTitle());
			if (cp == null) {
				oce.addError(ScgErrorCode.INVALID_ATTR_VALUE, CP_TITLE);
				return ;
			}
			
			cpr = daoFactory.getCprDao().getCprByPpId(cp.getId(), visitDetail.getPpid());
			if (cpr == null) {
				oce.addError(ScgErrorCode.INVALID_ATTR_VALUE, PPID);
				return ;
			}
		} 
		
		if (cpr == null) {
			oce.addError(ScgErrorCode.INVALID_ATTR_VALUE, CPR);
		}
		
		visit.setCollectionProtocolRegistration(cpr);
	}

	private void setVisitStatus(SpecimenCollectionGroup visit, String visitStatus, ObjectCreationException oce) {		
		if (isValidPv(visitStatus, VISIT_STATUS)) {
			visit.setCollectionStatus(visitStatus);
			return;
		}
		
		oce.addError(ScgErrorCode.INVALID_ATTR_VALUE, VISIT_STATUS);
	}

	private void setClinicalDiagnosis(SpecimenCollectionGroup visit, String clinicalDiagnosis, ObjectCreationException oce) {
		if (isValidPv(clinicalDiagnosis, CLINICAL_DIAGNOSIS)) {
			visit.setClinicalDiagnosis(clinicalDiagnosis);
			return;
		}
		
		oce.addError(ScgErrorCode.INVALID_ATTR_VALUE, CLINICAL_DIAGNOSIS);
	}
	
	private void setClinicalStatus(SpecimenCollectionGroup visit, String clinicalStatus, ObjectCreationException oce) {
		if (isValidPv(clinicalStatus, CLINICAL_STATUS)) {
			visit.setClinicalStatus(clinicalStatus);
			return;
		}
		
		oce.addError(ScgErrorCode.INVALID_ATTR_VALUE, CLINICAL_STATUS);
	}

	private void setSite(SpecimenCollectionGroup visit, String visitSite, ObjectCreationException oce) {
		if (visit.isCompleted() && isBlank(visitSite)) {
			oce.addError(ScgErrorCode.MISSING_ATTR_VALUE, SITE);
			return;
		} else {
			Site site = daoFactory.getSiteDao().getSite(visitSite);
			if (site == null) {
				oce.addError(ScgErrorCode.INVALID_ATTR_VALUE, SITE);
				return;
			}
			
			visit.setCollectionSite(site);
		}
	}

	private void setActivityStatus(SpecimenCollectionGroup visit, String status, ObjectCreationException oce) {
		if (isBlank(status)) {
			visit.setActive();
		} else if (isValidPv(status, Status.ACTIVITY_STATUS.getStatus())) {
			visit.setActivityStatus(status);
		} else { 
			oce.addError(ScgErrorCode.INVALID_ATTR_VALUE, Status.ACTIVITY_STATUS.getStatus());
		}
	}

	private void validateCprAndCpe(SpecimenCollectionGroup visit, ObjectCreationException oce) {
		CollectionProtocolRegistration cpr = visit.getCollectionProtocolRegistration();
		CollectionProtocolEvent cpe = visit.getCollectionProtocolEvent();
		
		if (cpr == null || cpe == null) {
			return;
		}
		
		if (!cpr.getCollectionProtocol().getId().equals(cpe.getCollectionProtocol().getId())) {
			oce.addError(ScgErrorCode.INVALID_CPR_CPE, CPR_CPE);
		}
	}
	
	public void setVisitDate(SpecimenCollectionGroup visit, VisitDetail visitDetail, ObjectCreationException oce) {
		CollectionProtocolRegistration cpr = visit.getCollectionProtocolRegistration();
		if (cpr == null) {
			return;
		}
		
		Date regDate = cpr.getRegistrationDate();
		Date visitDate = visitDetail.getVisitDate();
		if (visitDate == null) {
			visit.setCollectionTimestamp(Calendar.getInstance().getTime());
		} else if (visitDate.after(regDate) || visitDate.equals(regDate)) {
			visit.setCollectionTimestamp(visitDate);
		} else {
			oce.addError(ScgErrorCode.INVALID_VISIT_DATE, "visit date");
		}		
	}

	//
	// TODO: Below requires further review
	//
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
