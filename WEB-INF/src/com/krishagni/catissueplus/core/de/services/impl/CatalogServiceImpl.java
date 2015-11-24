package com.krishagni.catissueplus.core.de.services.impl;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.de.domain.CpCatalogSetting;
import com.krishagni.catissueplus.core.de.domain.factory.CpCatalogSettingFactory;
import com.krishagni.catissueplus.core.de.events.CpCatalogSettingDetail;
import com.krishagni.catissueplus.core.de.repository.DaoFactory;
import com.krishagni.catissueplus.core.de.services.CatalogService;

public class CatalogServiceImpl implements CatalogService {
	
	private DaoFactory daoFactory;
	
	private CpCatalogSettingFactory catalogSettingFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setCatalogSettingFactory(CpCatalogSettingFactory catalogSettingFactory) {
		this.catalogSettingFactory = catalogSettingFactory;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<CpCatalogSettingDetail> getCpSetting(RequestEvent<CollectionProtocolSummary> req) {
		try {
			CpCatalogSetting setting = getSetting(req.getPayload());
			if (setting == null) {
				return ResponseEvent.response(null);
			}
			
			AccessCtrlMgr.getInstance().ensureReadCpRights(setting.getCp());
			return ResponseEvent.response(CpCatalogSettingDetail.from(setting));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<CpCatalogSettingDetail> saveCpSetting(RequestEvent<CpCatalogSettingDetail> req) {
		try {
			CpCatalogSetting setting = catalogSettingFactory.createSetting(req.getPayload());			
			CollectionProtocol cp = setting.getCp();
			AccessCtrlMgr.getInstance().ensureUpdateCpRights(cp);
			
			CpCatalogSetting existing = daoFactory.getCpCatalogSettingDao().getByCpId(cp.getId());			
			if (existing == null) {
				existing = setting;
			} else {
				existing.update(setting);
			}
			
			daoFactory.getCpCatalogSettingDao().saveOrUpdate(existing);			
			return ResponseEvent.response(CpCatalogSettingDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<CpCatalogSettingDetail> deleteCpSetting(RequestEvent<CollectionProtocolSummary> req) {
		try {
			CpCatalogSetting setting = getSetting(req.getPayload());
			if (setting == null) {
				return ResponseEvent.response(null);
			}
			
			AccessCtrlMgr.getInstance().ensureUpdateCpRights(setting.getCp());
			setting.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
			daoFactory.getCpCatalogSettingDao().saveOrUpdate(setting);
			return ResponseEvent.response(CpCatalogSettingDetail.from(setting));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private CpCatalogSetting getSetting(CollectionProtocolSummary cp) {
		CpCatalogSetting setting = null;
		
		if (cp.getId() != null) {
			setting = daoFactory.getCpCatalogSettingDao().getByCpId(cp.getId());
		} else if (StringUtils.isNotBlank(cp.getShortTitle())) {
			setting = daoFactory.getCpCatalogSettingDao().getByCpShortTitle(cp.getShortTitle());
		}
		
		return setting;		
	}
}