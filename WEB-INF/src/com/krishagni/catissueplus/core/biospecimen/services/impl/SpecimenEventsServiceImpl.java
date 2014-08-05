package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.ArrayList;
import java.util.List;

import krishagni.catissueplus.beans.FormContextBean;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenEventFormData;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenEventFormData;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenEventFormDataEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenEventFormDataSummary;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenEventService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.de.events.FormRecordSummary;
import com.krishagni.catissueplus.core.de.repository.FormDao;

import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.FormDataManager;
import edu.common.dynamicextensions.napi.impl.FormDataManagerImpl;

public class SpecimenEventsServiceImpl implements SpecimenEventService {

	private DaoFactory daoFactory;
	
	private FormDao formDao;
	
	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public FormDao getFormDao() {
		return formDao;
	}

	public void setFormDao(FormDao formDao) {
		this.formDao = formDao;
	}

	@Override
	@PlusTransactional
	public SpecimenEventFormDataEvent getSpecimenEventFormData(ReqSpecimenEventFormData req) {
		Long formId = req.getFormId();
		List<String> specimenLabels = req.getSpecimenLabels();
		FormContextBean formContext = formDao.getFormContext(formId, -1L, "SpecimenEvent");
		List<SpecimenEventFormDataSummary> specimenEventFormDataSummaries = new ArrayList<SpecimenEventFormDataSummary>();
		
		for (String specimenLabel : specimenLabels) {
			Specimen specimen = daoFactory.getSpecimenDao().getSpecimenByLabel(specimenLabel);
			SpecimenEventFormDataSummary specimenEventFormDataSummary = new SpecimenEventFormDataSummary();
			specimenEventFormDataSummary.setLabel(specimenLabel);
			specimenEventFormDataSummary.setObjectId(specimen.getId());

			List<FormRecordSummary> formRecords = formDao.getFormRecords(formContext.getIdentifier(), specimen.getId());

			if (formRecords != null && !formRecords.isEmpty()) {
				FormRecordSummary formRecord = formRecords.get(0);
				FormDataManager formDataManager = new FormDataManagerImpl(false);
				FormData formData = formDataManager.getFormData(formId, formRecord.getRecordId());
				specimenEventFormDataSummary.setFormData(formData);
			}
			specimenEventFormDataSummaries.add(specimenEventFormDataSummary);
		}

		SpecimenEventFormData specimenEventFormData = new SpecimenEventFormData();
		specimenEventFormData.setFormContextId(formContext.getIdentifier());
		specimenEventFormData.setSpecimenEventFormDataList(specimenEventFormDataSummaries);

		return SpecimenEventFormDataEvent.ok(specimenEventFormData);
	}

}
