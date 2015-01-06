
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;
import static com.krishagni.catissueplus.core.common.CommonValidator.isValidPv;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ScgErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitFactory;
import com.krishagni.catissueplus.core.biospecimen.events.ScgReportDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.util.Status;

public class VisitFactoryImpl implements VisitFactory {

	private DaoFactory daoFactory;

	private static final String SCG_REPORTS = "scg reports";

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public Visit createVisit(VisitDetail visitDetail) {
		Visit visit = new Visit();
		
		ObjectCreationException oce = new ObjectCreationException();
				
		setCpe(visitDetail, visit, oce);		
		setCpr(visitDetail, visit, oce);
		validateCprAndCpe(visit, oce);
		
		setVisitDate(visitDetail, visit, oce);
		setVisitStatus(visitDetail, visit, oce);
		setClinicalDiagnosis(visitDetail, visit, oce);
		setClinicalStatus(visitDetail, visit, oce);
		setSite(visitDetail, visit, oce);
		setActivityStatus(visitDetail, visit, oce);
		
		visit.setComments(visitDetail.getComments());
		visit.setSurgicalPathologyNumber(visitDetail.getSurgicalPathologyNumber());
		
		oce.checkErrorAndThrow();
		return visit;
	}

	private void setCpe(VisitDetail visitDetail, Visit visit, ObjectCreationException oce) {
		CollectionProtocolEvent cpe = null;
		
		Long cpeId = visitDetail.getCpeId();
		String cpTitle = visitDetail.getCpTitle(), 
			   eventLabel = visitDetail.getEventLabel();
		
		if (cpeId != null) {
			cpe = daoFactory.getCollectionProtocolDao().getCpe(cpeId);
			if (cpe == null) {
				oce.addError(ScgErrorCode.INVALID_ATTR_VALUE, "cpeId");
				return;
			}

		} else if (StringUtils.isNotBlank(cpTitle) && StringUtils.isNotBlank(eventLabel)) {			
			CollectionProtocol cp = daoFactory.getCollectionProtocolDao()
					.getCollectionProtocol(cpTitle);
			
			if (cp == null) {
				oce.addError(ScgErrorCode.INVALID_ATTR_VALUE, "collectionProtocol");
				return;
			}
			
			cpe = daoFactory.getCollectionProtocolDao()
					.getCpeByEventLabel(cp.getId(), eventLabel);			
			if (cpe == null) {
				oce.addError(ScgErrorCode.INVALID_ATTR_VALUE, "eventLabel");
				return;
			}
		}
				
		visit.setCpEvent(cpe);
	}

	private void setCpr(VisitDetail visitDetail, Visit visit, ObjectCreationException oce) {
		CollectionProtocolRegistration cpr = null;
		
		if (visitDetail.getCprId() != null) {
			cpr = daoFactory.getCprDao().getById(visitDetail.getCprId());
			if (cpr == null) {
				oce.addError(ScgErrorCode.INVALID_ATTR_VALUE, "cprId");
			}
			
		} else if (visitDetail.getPpid() != null && visitDetail.getCpTitle() != null) {			
			CollectionProtocol cp = daoFactory.getCollectionProtocolDao()
					.getCollectionProtocol(visitDetail.getCpTitle());			
			if (cp == null) {
				oce.addError(ScgErrorCode.INVALID_ATTR_VALUE, "collectionProtocol");
				return ;
			}
			
			cpr = daoFactory.getCprDao().getCprByPpId(cp.getId(), visitDetail.getPpid());
			if (cpr == null) {
				oce.addError(ScgErrorCode.INVALID_ATTR_VALUE, "ppid");
				return ;
			}
		} 
		
		
		visit.setRegistration(cpr);
	}

	private void validateCprAndCpe(Visit visit, ObjectCreationException oce) {
		CollectionProtocolRegistration cpr = visit.getRegistration();
		CollectionProtocolEvent cpe = visit.getCpEvent();
		
		if (cpr == null || cpe == null) {
			return;
		}
		
		if (!cpr.getCollectionProtocol().getId().equals(cpe.getCollectionProtocol().getId())) {
			oce.addError(ScgErrorCode.INVALID_CPR_CPE, "cpr, cpe");
		}
	}

	public void setVisitDate(VisitDetail visitDetail, Visit visit, ObjectCreationException oce) {
		CollectionProtocolRegistration cpr = visit.getRegistration();
		if (cpr == null) {
			return;
		}
		
		Date regDate = cpr.getRegistrationDate();
		Date visitDate = visitDetail.getVisitDate();
		if (visitDate != null && (visitDate.after(regDate) || visitDate.equals(regDate))) {
			visit.setVisitDate(visitDate);
		} else {
			visit.setVisitDate(Calendar.getInstance().getTime());
		}
	}
	
	private void setVisitStatus(VisitDetail visitDetail, Visit visit, ObjectCreationException oce) {
		String visitStatus = visitDetail.getVisitStatus();
		if (isValidPv(visitStatus, "visit-status")) {
			visit.setStatus(visitStatus);
			return;
		}
		
		oce.addError(ScgErrorCode.INVALID_ATTR_VALUE, "visitStatus");
	}

	private void setClinicalDiagnosis(VisitDetail visitDetail, Visit visit, ObjectCreationException oce) {
		String clinicalDiagnosis = visitDetail.getClinicalDiagnosis();
		if (isValidPv(clinicalDiagnosis, "clinical-diagnosis")) {
			visit.setClinicalDiagnosis(clinicalDiagnosis);
			return;
		}
		
		oce.addError(ScgErrorCode.INVALID_ATTR_VALUE, "clinicalDiagnosis");
	}
	
	private void setClinicalStatus(VisitDetail visitDetail, Visit visit, ObjectCreationException oce) {
		String clinicalStatus = visitDetail.getClinicalStatus();
		if (isValidPv(clinicalStatus, "clinical-status")) {
			visit.setClinicalStatus(clinicalStatus);
			return;
		}
		
		oce.addError(ScgErrorCode.INVALID_ATTR_VALUE, "clinicalStatus");
	}

	private void setSite(VisitDetail visitDetail, Visit visit, ObjectCreationException oce) {
		String visitSite = visitDetail.getVisitSite();
		if (visit.isCompleted() && isBlank(visitSite)) {
			oce.addError(ScgErrorCode.MISSING_ATTR_VALUE, "site");
			return;
		} else {
			Site site = daoFactory.getSiteDao().getSite(visitSite);
			if (site == null) {
				oce.addError(ScgErrorCode.INVALID_ATTR_VALUE, "site");
				return;
			}
			
			visit.setSite(site);
		}
	}

	private void setActivityStatus(VisitDetail visitDetail, Visit visit, ObjectCreationException oce) {
		String status = visitDetail.getActivityStatus();
		if (isBlank(status)) {
			visit.setActive();
		} else if (isValidPv(status, Status.ACTIVITY_STATUS.getStatus())) {
			visit.setActivityStatus(status);
		} else { 
			oce.addError(ScgErrorCode.INVALID_ATTR_VALUE, Status.ACTIVITY_STATUS.getStatus());
		}
	}

	//
	// TODO: Below requires further review
	//
	@Override
	public Visit updateReports(Visit oldScg, ScgReportDetail detail) {

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
