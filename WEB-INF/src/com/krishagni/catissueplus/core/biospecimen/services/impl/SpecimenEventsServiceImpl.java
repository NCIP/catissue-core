package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenEventsService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.de.repository.FormDao;
import com.krishagni.catissueplus.core.de.services.FormService;

import edu.common.dynamicextensions.napi.FormData;

public class SpecimenEventsServiceImpl implements SpecimenEventsService {

	private DaoFactory daoFactory;

	private FormDao formDao;

	private FormService formSvc;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setFormDao(FormDao formDao) {
		this.formDao = formDao;
	}

	public void setFormSvc(FormService formSvc) {
		this.formSvc = formSvc;
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<FormData>> saveSpecimenEvents(RequestEvent<List<FormData>> req) {
		List<FormData> formDataList = req.getPayload();
		if (CollectionUtils.isEmpty(formDataList)) {
			return ResponseEvent.response(Collections.<FormData>emptyList());
		}
		
		Long formId = formDataList.get(0).getContainer().getId();
		Long formCtxtId = formDao.getFormCtxtId(formId, "SpecimenEvent", -1L);
		for (FormData formData : formDataList) {
			String specimenLabel = formData.getAppData().get("id").toString();
			Specimen specimen = daoFactory.getSpecimenDao().getByLabel(specimenLabel);
			if (specimen == null) {
				return ResponseEvent.userError(SpecimenErrorCode.NOT_FOUND);				
			}
			
			Long cpId = specimen.getVisit().getRegistration().getCollectionProtocol().getId();
//			if (!privilegeSvc.hasPrivilege(req.getSessionDataBean().getUserId(), cpId, Permissions.SPECIMEN_PROCESSING)) {
//				return ResponseEvent.userError(SpecimenErrorCode.OP_NOT_ALLOWED);
//			}
			
			Map<String, Object> appData = formData.getAppData();
			appData.put("formCtxtId", formCtxtId.doubleValue());
			appData.put("objectId", specimen.getId().doubleValue());
		}

		return formSvc.saveBulkFormData(req);
	}
}