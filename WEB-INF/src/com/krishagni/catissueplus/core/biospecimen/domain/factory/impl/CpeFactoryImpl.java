package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpeErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpeFactory;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolEventDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Status;

public class CpeFactoryImpl implements CpeFactory {
	private DaoFactory daoFactory;

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public CollectionProtocolEvent createCpe(CollectionProtocolEventDetail detail) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		CollectionProtocolEvent cpe = new CollectionProtocolEvent();
				
		cpe.setId(detail.getId());
		setEventLabel(detail, cpe, ose);
		setEventPoint(detail, cpe, ose);
		setCp(detail, cpe, ose);
		setDefaultSite(detail, cpe, ose);
		setActivityStatus(detail, cpe, ose);
		cpe.setClinicalDiagnosis(detail.getClinicalDiagnosis());
		cpe.setClinicalStatus(detail.getClinicalStatus());
		
		ose.checkAndThrow();
		return cpe;
	}
	
	@Override
	public CollectionProtocolEvent createCpeCopy(CollectionProtocolEventDetail detail, CollectionProtocolEvent existing) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

		CollectionProtocolEvent cpe = new CollectionProtocolEvent();

		setEventLabel(detail, cpe, ose);
		
		if (detail.getEventPoint() != null) {
			setEventPoint(detail, cpe, ose);
		} else {
			cpe.setEventPoint(existing.getEventPoint());
		}
		
		cpe.setCollectionProtocol(existing.getCollectionProtocol());
		
		if (!StringUtils.isBlank(detail.getDefaultSite())) {
			setDefaultSite(detail, cpe, ose);
		} else {
			cpe.setDefaultSite(existing.getDefaultSite());
		}
		
		if (!StringUtils.isBlank(detail.getClinicalDiagnosis())) {
			cpe.setClinicalDiagnosis(detail.getClinicalDiagnosis());
		} else {
			cpe.setClinicalDiagnosis(existing.getClinicalDiagnosis());
		}
		
		if (!StringUtils.isBlank(detail.getClinicalStatus())) {
			cpe.setClinicalStatus(detail.getClinicalStatus());
		} else {
			cpe.setClinicalStatus(existing.getClinicalStatus());
		}
		
		if (!StringUtils.isBlank(detail.getActivityStatus())) {
			setActivityStatus(detail, cpe, ose);
                } else {
                        cpe.setActivityStatus(existing.getActivityStatus());
                }
		
		ose.checkAndThrow();		
		return cpe;
	}
	
	public void setEventLabel(CollectionProtocolEventDetail detail, CollectionProtocolEvent cpe, OpenSpecimenException ose) {
		if (StringUtils.isBlank(detail.getEventLabel())) {
			ose.addError(CpeErrorCode.LABEL_REQUIRED);
			return;
		}
		
		cpe.setEventLabel(detail.getEventLabel());
	}
	
	public void setEventPoint(CollectionProtocolEventDetail detail, CollectionProtocolEvent cpe, OpenSpecimenException ose) {
		Double eventPoint = detail.getEventPoint();
		if (eventPoint == null) {
			eventPoint = 0d;
		}
		
		if (eventPoint < 0) {
			ose.addError(CpeErrorCode.INVALID_POINT);
			return;
		}
		
		cpe.setEventPoint(eventPoint);
	}
	
	public void setCp(CollectionProtocolEventDetail detail, CollectionProtocolEvent cpe, OpenSpecimenException ose) {
		CollectionProtocol cp = daoFactory.getCollectionProtocolDao()
				.getCollectionProtocol(detail.getCollectionProtocol());
		
		if (cp == null) {
			ose.addError(CpErrorCode.NOT_FOUND);
			return;
		}
		
		cpe.setCollectionProtocol(cp);		
	}
	
	public void setDefaultSite(CollectionProtocolEventDetail detail, CollectionProtocolEvent cpe, OpenSpecimenException ose) {
		if (StringUtils.isBlank(detail.getDefaultSite())) {
			return;
		}
		
		Site site = daoFactory.getSiteDao().getSite(detail.getDefaultSite());
		if (site == null) {
			ose.addError(SiteErrorCode.NOT_FOUND);
			return;
		}
		
		cpe.setDefaultSite(site);
	}
	
	public void setActivityStatus(CollectionProtocolEventDetail detail, CollectionProtocolEvent cpe, OpenSpecimenException ose) {
		String activityStatus = detail.getActivityStatus();
		if (StringUtils.isBlank(activityStatus)) {
			activityStatus = Status.ACTIVITY_STATUS_ACTIVE.getStatus();
		}
		
		cpe.setActivityStatus(activityStatus);
	}

}
