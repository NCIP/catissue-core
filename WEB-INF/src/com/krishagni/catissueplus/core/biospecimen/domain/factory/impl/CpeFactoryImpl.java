package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpeFactory;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolEventDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
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
		ObjectCreationException oce = new ObjectCreationException();
		
		CollectionProtocolEvent cpe = new CollectionProtocolEvent();
				
		cpe.setId(detail.getId());
		setEventLabel(detail, cpe, oce);
		setEventPoint(detail, cpe, oce);
		setCp(detail, cpe, oce);
		setDefaultSite(detail, cpe, oce);
		setActivityStatus(detail, cpe, oce);
		cpe.setClinicalDiagnosis(detail.getClinicalDiagnosis());
		cpe.setClinicalStatus(detail.getClinicalStatus());
		
		oce.checkErrorAndThrow();
		return cpe;
	}
	
	public void setEventLabel(CollectionProtocolEventDetail detail, CollectionProtocolEvent cpe, ObjectCreationException oce) {
		if (StringUtils.isBlank(detail.getEventLabel())) {
			oce.addError(CpErrorCode.MISSING_EVENT_LABEL, "eventLabel"); // event label
			return;
		}
		
		cpe.setEventLabel(detail.getEventLabel());
	}
	
	public void setEventPoint(CollectionProtocolEventDetail detail, CollectionProtocolEvent cpe, ObjectCreationException oce) {
		Double eventPoint = detail.getEventPoint();
		if (eventPoint == null) {
			eventPoint = 0d;
		}
		
		if (eventPoint < 0) {
			oce.addError(CpErrorCode.INVALID_EVENT_POINT, "eventPoint");
			return;
		}
		
		cpe.setEventPoint(eventPoint);
	}
	
	public void setCp(CollectionProtocolEventDetail detail, CollectionProtocolEvent cpe, ObjectCreationException oce) {
		CollectionProtocol cp = daoFactory.getCollectionProtocolDao()
				.getCollectionProtocol(detail.getCollectionProtocol());
		if (cp == null) {
			oce.addError(CpErrorCode.INVALID_CP_TITLE, "collectionProtocol");
			return;
		}
		
		cpe.setCollectionProtocol(cp);		
	}
	
	public void setDefaultSite(CollectionProtocolEventDetail detail, CollectionProtocolEvent cpe, ObjectCreationException oce) {
		if (StringUtils.isBlank(detail.getDefaultSite())) {
			return;
		}
		
		Site site = daoFactory.getSiteDao().getSite(detail.getDefaultSite());
		if (site == null) {
			oce.addError(CpErrorCode.INVALID_SITE, "defaultSite");
			return;
		}
		
		cpe.setDefaultSite(site);
	}
	
	public void setActivityStatus(CollectionProtocolEventDetail detail, CollectionProtocolEvent cpe, ObjectCreationException oce) {
		String activityStatus = detail.getActivityStatus();
		if (StringUtils.isBlank(activityStatus)) {
			activityStatus = Status.ACTIVITY_STATUS_ACTIVE.getStatus();
		}
		
		cpe.setActivityStatus(activityStatus);
	}

}
