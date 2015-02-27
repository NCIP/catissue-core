
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isValidPv;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpeErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CprErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitFactory;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Status;

public class VisitFactoryImpl implements VisitFactory {

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public Visit createVisit(VisitDetail visitDetail) {
		Visit visit = new Visit();
		
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
				
		visit.setName(visitDetail.getName());
		setCpe(visitDetail, visit, ose);		
		setCpr(visitDetail, visit, ose);
		validateCprAndCpe(visit, ose);
		
		setVisitDate(visitDetail, visit, ose);
		setVisitStatus(visitDetail, visit, ose);
		setClinicalDiagnosis(visitDetail, visit, ose);
		setClinicalStatus(visitDetail, visit, ose);
		setSite(visitDetail, visit, ose);
		setActivityStatus(visitDetail, visit, ose);
		
		visit.setComments(visitDetail.getComments());
		visit.setSurgicalPathologyNumber(visitDetail.getSurgicalPathologyNumber());
		
		ose.checkAndThrow();
		return visit;
	}

	private void setCpe(VisitDetail visitDetail, Visit visit, OpenSpecimenException ose) {
		CollectionProtocolEvent cpe = null;
		
		Long cpeId = visitDetail.getEventId();
		String cpTitle = visitDetail.getCpTitle(), 
			   eventLabel = visitDetail.getEventLabel();
		
		if (cpeId != null) {
			cpe = daoFactory.getCollectionProtocolDao().getCpe(cpeId);
			if (cpe == null) {
				ose.addError(CpeErrorCode.NOT_FOUND);
				return;
			}
		} else if (StringUtils.isNotBlank(cpTitle) && StringUtils.isNotBlank(eventLabel)) {			
			CollectionProtocol cp = daoFactory.getCollectionProtocolDao()
					.getCollectionProtocol(cpTitle);
			
			if (cp == null) {
				ose.addError(CpErrorCode.NOT_FOUND);
				return;
			}
			
			cpe = daoFactory.getCollectionProtocolDao()
					.getCpeByEventLabel(cp.getId(), eventLabel);			
			if (cpe == null) {
				ose.addError(CpeErrorCode.LABEL_NOT_FOUND);
				return;
			}
		}
				
		visit.setCpEvent(cpe);
	}

	private void setCpr(VisitDetail visitDetail, Visit visit, OpenSpecimenException ose) {
		CollectionProtocolRegistration cpr = null;
		
		if (visitDetail.getCprId() != null) {
			cpr = daoFactory.getCprDao().getById(visitDetail.getCprId());
			if (cpr == null) {
				ose.addError(CprErrorCode.NOT_FOUND);
				return;
			}			
		} else if (visitDetail.getPpid() != null && visitDetail.getCpTitle() != null) {			
			CollectionProtocol cp = daoFactory.getCollectionProtocolDao()
					.getCollectionProtocol(visitDetail.getCpTitle());			
			if (cp == null) {
				ose.addError(CpErrorCode.NOT_FOUND);
				return ;
			}
			
			cpr = daoFactory.getCprDao().getCprByPpId(cp.getId(), visitDetail.getPpid());
			if (cpr == null) {
				ose.addError(CprErrorCode.INVALID_CP_AND_PPID);
				return ;
			}
		} 
		
		
		visit.setRegistration(cpr);
	}

	private void validateCprAndCpe(Visit visit, OpenSpecimenException ose) {
		CollectionProtocolRegistration cpr = visit.getRegistration();
		CollectionProtocolEvent cpe = visit.getCpEvent();
		
		if (cpr == null || cpe == null) {
			return;
		}
		
		if (!cpr.getCollectionProtocol().getId().equals(cpe.getCollectionProtocol().getId())) {
			ose.addError(CprErrorCode.INVALID_CPE);
		}
	}

	public void setVisitDate(VisitDetail visitDetail, Visit visit, OpenSpecimenException ose) {
		CollectionProtocolRegistration cpr = visit.getRegistration();
		if (cpr == null) {
			return;
		}
		
		Date regDate = cpr.getRegistrationDate();
		Date visitDate = visitDetail.getVisitDate();
		if (visitDate == null) {
			visitDate = Calendar.getInstance().getTime();
		}
			
		if (visitDate.before(regDate)) {
			ose.addError(VisitErrorCode.INVALID_VISIT_DATE);
			return;
		}
		
		visit.setVisitDate(visitDate);
	}
	
	private void setVisitStatus(VisitDetail visitDetail, Visit visit, OpenSpecimenException ose) {
		String visitStatus = visitDetail.getStatus();
		if (isValidPv(visitStatus, "visit-status")) {
			visit.setStatus(visitStatus);
			return;
		}
		
		ose.addError(VisitErrorCode.INVALID_STATUS);
	}

	private void setClinicalDiagnosis(VisitDetail visitDetail, Visit visit, OpenSpecimenException ose) {
		String clinicalDiagnosis = visitDetail.getClinicalDiagnosis();
		if (isValidPv(clinicalDiagnosis, "clinical-diagnosis")) {
			visit.setClinicalDiagnosis(clinicalDiagnosis);
			return;
		}
		
		ose.addError(VisitErrorCode.INVALID_CLINICAL_DIAGNOSIS);
	}
	
	private void setClinicalStatus(VisitDetail visitDetail, Visit visit, OpenSpecimenException ose) {
		String clinicalStatus = visitDetail.getClinicalStatus();
		if (isValidPv(clinicalStatus, "clinical-status")) {
			visit.setClinicalStatus(clinicalStatus);
			return;
		}
		
		ose.addError(VisitErrorCode.INVALID_CLINICAL_STATUS);
	}

	private void setSite(VisitDetail visitDetail, Visit visit, OpenSpecimenException ose) {
		String visitSite = visitDetail.getSite();
		if (visit.isCompleted() && StringUtils.isBlank(visitSite)) {
			ose.addError(VisitErrorCode.SITE_REQUIRED);
			return;
		} else {
			Site site = daoFactory.getSiteDao().getSite(visitSite);
			if (site == null) {
				ose.addError(SiteErrorCode.NOT_FOUND);
				return;
			}
			
			visit.setSite(site);
		}
	}

	private void setActivityStatus(VisitDetail visitDetail, Visit visit, OpenSpecimenException ose) {
		String status = visitDetail.getActivityStatus();
		if (StringUtils.isBlank(status)) {
			visit.setActive();
		} else if (isValidPv(status, Status.ACTIVITY_STATUS.getStatus())) {
			visit.setActivityStatus(status);
		} else { 
			ose.addError(ActivityStatusErrorCode.INVALID);
		}
	}
}