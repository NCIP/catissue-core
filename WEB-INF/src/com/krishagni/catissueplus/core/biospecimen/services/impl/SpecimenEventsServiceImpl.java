package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.events.SaveSpecimenEventsDataEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenEventsSavedEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenEventService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.de.events.SaveBulkFormDataEvent;
import com.krishagni.catissueplus.core.de.repository.FormDao;
import com.krishagni.catissueplus.core.de.services.FormService;
import com.krishagni.catissueplus.core.privileges.services.PrivilegeService;

import edu.common.dynamicextensions.napi.FormData;
import edu.wustl.security.global.Permissions;

public class SpecimenEventsServiceImpl implements SpecimenEventService {

	private DaoFactory daoFactory;

	private FormDao formDao;

	private FormService formSvc;
	
	private PrivilegeService privilegeSvc;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setFormDao(FormDao formDao) {
		this.formDao = formDao;
	}

	public void setFormSvc(FormService formSvc) {
		this.formSvc = formSvc;
	}
	
	public void setPrivilegeSvc(PrivilegeService privilegeSvc) {
		this.privilegeSvc = privilegeSvc;
	}

	@Override
	@PlusTransactional
	public SpecimenEventsSavedEvent saveSpecimenEvents(SaveSpecimenEventsDataEvent req) {
		Long formCtxtId = formDao.getFormCtxtId(req.getFormId(), "SpecimenEvent", -1L);
		List<FormData> formDataList = req.getFormDataList();
		for (FormData formData : formDataList) {
			String specimenLabel = formData.getAppData().get("id").toString();
			Specimen specimen = daoFactory.getSpecimenDao().getSpecimenByLabel(specimenLabel);
			if(specimen == null) {
				return SpecimenEventsSavedEvent.badRequest(new IllegalArgumentException("Specimen with label"+ specimenLabel+ "does not exist"));
			}
			Long cpId = specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getId();
			if(!privilegeSvc.hasPrivilege(req.getSessionDataBean().getUserId(), cpId, Permissions.SPECIMEN_PROCESSING)) {
				return SpecimenEventsSavedEvent.notAuthorized(new IllegalAccessException("Does not have access for data entry on specimen" + specimenLabel));
			}
			
			Map<String, Object> appData = formData.getAppData();
			appData.put("formCtxtId", formCtxtId.doubleValue());
			appData.put("objectId", specimen.getId().doubleValue());
		}

		SaveBulkFormDataEvent event = new SaveBulkFormDataEvent();
		event.setFormDataList(formDataList);
		event.setFormId(req.getFormId());
		event.setSessionDataBean(req.getSessionDataBean());

		return SpecimenEventsSavedEvent.setEventStatus(formSvc.saveBulkFormData(event));
	}
	
	/*	@Override
	@PlusTransactional
	public SpecimenEventFormDataEvent getSpecimenEventFormData(
			ReqSpecimenEventFormData req) {
		Long formId = req.getFormId();
		List<String> specimenLabels = req.getSpecimenLabels();
		FormContextBean formContext = formDao.getFormContext(formId, -1L,
				"SpecimenEvent");
		List<SpecimenEventFormDataSummary> specimenEventFormDataSummaries = new ArrayList<SpecimenEventFormDataSummary>();

		for (String specimenLabel : specimenLabels) {
			Specimen specimen = daoFactory.getSpecimenDao().getSpecimenByLabel(
					specimenLabel);
			SpecimenEventFormDataSummary specimenEventFormDataSummary = new SpecimenEventFormDataSummary();
			specimenEventFormDataSummary.setLabel(specimenLabel);
			specimenEventFormDataSummary.setObjectId(specimen.getId());

			List<FormRecordSummary> formRecords = formDao.getFormRecords(
					formContext.getIdentifier(), specimen.getId());
			List<String> formRecordList = new ArrayList<String>();

			for (FormRecordSummary formRecordSummary : formRecords) {
				FormDataManager formDataManager = new FormDataManagerImpl(false);
				FormData formData = formDataManager.getFormData(formId,
						formRecordSummary.getRecordId());
				formRecordList.add(formData.toJson());
			}
			specimenEventFormDataSummary.setFormRecords(formRecordList);
			specimenEventFormDataSummaries.add(specimenEventFormDataSummary);
		}

		SpecimenEventFormData specimenEventFormData = new SpecimenEventFormData();
		specimenEventFormData.setFormContextId(formContext.getIdentifier());
		specimenEventFormData
				.setSpecimenEventFormDataList(specimenEventFormDataSummaries);

		return SpecimenEventFormDataEvent.ok(specimenEventFormData);
	}*/

}