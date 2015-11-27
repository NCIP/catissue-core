package com.krishagni.catissueplus.core.de.domain.factory.impl;

import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.de.domain.CpCatalogSetting;
import com.krishagni.catissueplus.core.de.domain.SavedQuery;
import com.krishagni.catissueplus.core.de.domain.factory.CpCatalogSettingFactory;
import com.krishagni.catissueplus.core.de.events.CpCatalogSettingDetail;
import com.krishagni.catissueplus.core.de.services.SavedQueryErrorCode;

public class CpCatalogSettingFactoryImpl implements CpCatalogSettingFactory {
	
	private DaoFactory daoFactory;
	
	private com.krishagni.catissueplus.core.de.repository.DaoFactory deDaoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setDeDaoFactory(com.krishagni.catissueplus.core.de.repository.DaoFactory deDaoFactory) {
		this.deDaoFactory = deDaoFactory;
	}

	@Override
	public CpCatalogSetting createSetting(CpCatalogSettingDetail detail) {
		CpCatalogSetting query = new CpCatalogSetting();
		
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);		
		setCollectionProtocol(detail, query, ose);
		setQuery(detail, query, ose);
		setCreationDetail(query);
		setActivityStatus(detail, query, ose);
		
		ose.checkAndThrow();
		return query;
	}
	
	private void setCollectionProtocol(CpCatalogSettingDetail detail, CpCatalogSetting query, OpenSpecimenException ose) {
		if (detail.getCp() == null) {
			ose.addError(CpErrorCode.NOT_FOUND);
			return;
		}
		
		Long cpId = detail.getCp().getId();
		String cpShortTitle = detail.getCp().getShortTitle();
		
		CollectionProtocol cp = null;
		if (cpId != null) {
			cp = daoFactory.getCollectionProtocolDao().getById(cpId);
		} else if (StringUtils.isNotBlank(cpShortTitle)) {
			cp = daoFactory.getCollectionProtocolDao().getCpByShortTitle(cpShortTitle);
		}
		
		if (cp == null) {
			ose.addError(CpErrorCode.NOT_FOUND);
			return;
		}
		
		query.setCp(cp);
	}
	
	private void setQuery(CpCatalogSettingDetail detail, CpCatalogSetting query, OpenSpecimenException ose) {
		if (detail.getQuery() == null) {
			ose.addError(SavedQueryErrorCode.NOT_FOUND);
			return;
		}
		
		SavedQuery savedQuery = deDaoFactory.getSavedQueryDao().getQuery(detail.getQuery().getId());
		if (savedQuery == null) {
			ose.addError(SavedQueryErrorCode.NOT_FOUND);
			return;
		}
		
		query.setQuery(savedQuery);
	}
	
	private void setCreationDetail(CpCatalogSetting query) {
		query.setCreatedBy(AuthUtil.getCurrentUser());
		query.setCreationTime(Calendar.getInstance().getTime());
	}
	
	private void setActivityStatus(CpCatalogSettingDetail detail, CpCatalogSetting query, OpenSpecimenException ose) {
		if (StringUtils.isBlank(detail.getActivityStatus())) {
			query.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
			return;
		}
		
		if (!Status.isValidActivityStatus(detail.getActivityStatus())) {
			ose.addError(ActivityStatusErrorCode.INVALID);
			return;
		}
		
		query.setActivityStatus(detail.getActivityStatus());
	}
}