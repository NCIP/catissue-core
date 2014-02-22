package com.krishagni.catissueplus.core.de.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import krishagni.catissueplus.beans.FormContextBean;
import krishagni.catissueplus.beans.FormRecordEntryBean;

import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.de.events.AddFormContextsEvent;
import com.krishagni.catissueplus.core.de.events.AllFormsSummaryEvent;
import com.krishagni.catissueplus.core.de.events.EntityFormRecordsEvent;
import com.krishagni.catissueplus.core.de.events.EntityFormsEvent;
import com.krishagni.catissueplus.core.de.events.FormContextDetail;
import com.krishagni.catissueplus.core.de.events.FormContextsAddedEvent;
import com.krishagni.catissueplus.core.de.events.FormContextsEvent;
import com.krishagni.catissueplus.core.de.events.FormCtxtSummary;
import com.krishagni.catissueplus.core.de.events.FormDataEvent;
import com.krishagni.catissueplus.core.de.events.FormDefinitionEvent;
import com.krishagni.catissueplus.core.de.events.FormFieldSummary;
import com.krishagni.catissueplus.core.de.events.FormFieldsEvent;
import com.krishagni.catissueplus.core.de.events.FormRecordSummary;
import com.krishagni.catissueplus.core.de.events.ReqAllFormsSummaryEvent;
import com.krishagni.catissueplus.core.de.events.ReqEntityFormRecordsEvent;
import com.krishagni.catissueplus.core.de.events.ReqEntityFormsEvent;
import com.krishagni.catissueplus.core.de.events.ReqFormContextsEvent;
import com.krishagni.catissueplus.core.de.events.ReqFormDataEvent;
import com.krishagni.catissueplus.core.de.events.ReqFormDefinitionEvent;
import com.krishagni.catissueplus.core.de.events.ReqFormFieldsEvent;
import com.krishagni.catissueplus.core.de.events.SaveFormDataEvent;
import com.krishagni.catissueplus.core.de.events.ReqAllFormsSummaryEvent.FormType;
import com.krishagni.catissueplus.core.de.repository.FormDao;
import com.krishagni.catissueplus.core.de.services.FormService;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DataType;
import edu.common.dynamicextensions.domain.nui.FileUploadControl;
import edu.common.dynamicextensions.domain.nui.Label;
import edu.common.dynamicextensions.domain.nui.PageBreak;
import edu.common.dynamicextensions.domain.nui.PermissibleValue;
import edu.common.dynamicextensions.domain.nui.SelectControl;
import edu.common.dynamicextensions.domain.nui.SubFormControl;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.FormDataManager;
import edu.common.dynamicextensions.napi.impl.FormDataManagerImpl;
import edu.wustl.common.beans.SessionDataBean;

public class FormServiceImpl implements FormService {
	
	private FormDao formDao;

	public FormDao getFormDao() {
		return formDao;
	}

	public void setFormDao(FormDao formDao) {
		this.formDao = formDao;
	}

	@Override
	@PlusTransactional
	public AllFormsSummaryEvent getForms(ReqAllFormsSummaryEvent req) {
		switch (req.getFormType()) {
		    case DATA_ENTRY_FORMS:
			    return AllFormsSummaryEvent.ok(formDao.getAllFormsSummary());
			    
		    case QUERY_FORMS:
		    default:
		    	return AllFormsSummaryEvent.ok(formDao.getQueryForms());		
		}		
	}
	
	@Override
	@PlusTransactional
	public FormDefinitionEvent getFormDefinition(ReqFormDefinitionEvent req) {
		Container container = Container.getContainer(req.getFormId());
		if (container == null) {
			return FormDefinitionEvent.notFound(req.getFormId());
		} else {
			return FormDefinitionEvent.ok(container);
		}
	}
	
	@Override
	@PlusTransactional
	public FormFieldsEvent getFormFields(ReqFormFieldsEvent req) {
		Long formId = req.getFormId();
		Container form = Container.getContainer(formId);
		if (form == null) {
			return FormFieldsEvent.notFound(formId);
		}
				
		return FormFieldsEvent.ok(formId, getFormFields(form, "", ""));
	}
	
	@Override
	@PlusTransactional
	public FormContextsEvent getFormContexts(ReqFormContextsEvent req) {
		return FormContextsEvent.ok(formDao.getFormContexts(req.getFormId()));		
	}
	
	
	@Override
	@PlusTransactional
	public FormContextsAddedEvent addFormContexts(AddFormContextsEvent req) {
		List<FormContextDetail> formCtxts = req.getFormContexts();
		 
		for (FormContextDetail formCtxtDetail : formCtxts) {
			Long formId = formCtxtDetail.getFormId();
			Long cpId = formCtxtDetail.getCollectionProtocol().getId();
			String entity = formCtxtDetail.getLevel();
			
			FormContextBean formCtxt = formDao.getFormContext(formId, cpId, entity);
			if (formCtxt == null) {
				formCtxt = new FormContextBean();
				formCtxt.setContainerId(formId);
				formCtxt.setCpId(cpId);
				formCtxt.setEntityType(entity);
				formDao.saveOrUpdate(formCtxt);
			}
			
			formCtxtDetail.setFormCtxtId(formCtxt.getIdentifier());
		}
		
		return FormContextsAddedEvent.ok(formCtxts);
	}

	@Override
	@PlusTransactional
	public EntityFormsEvent getEntityForms(ReqEntityFormsEvent req) {
		List<FormCtxtSummary> forms = null;
		
		switch (req.getEntityType()) {
		    case COLLECTION_PROTOCOL_REGISTRATION:
		    	forms = formDao.getCprForms(req.getEntityId());
		    	break;
		    	
		    case SPECIMEN:
		    	forms = formDao.getSpecimenForms(req.getEntityId());
		    	break;
		    	
		    case SPECIMEN_COLLECTION_GROUP:
		    	forms = formDao.getScgForms(req.getEntityId());
		    	break;
		}
		
		return EntityFormsEvent.ok(forms);
	}

	@Override
	@PlusTransactional
	public EntityFormRecordsEvent getEntityFormRecords(ReqEntityFormRecordsEvent req) {
		List<FormRecordSummary> formRecs = formDao.getFormRecords(req.getFormCtxtId(), req.getEntityId());
		return EntityFormRecordsEvent.ok(formRecs);
	}

	@Override
	@PlusTransactional
	public FormDataEvent getFormData(ReqFormDataEvent req) {
		Long formId = req.getFormId(), recordId = req.getRecordId();
		FormDataManager formDataMgr = new FormDataManagerImpl(false);
		
		FormData formData = formDataMgr.getFormData(formId, recordId);		
		if (formData == null) {
			return FormDataEvent.notFound(formId, recordId);
		} else {
			return FormDataEvent.ok(formId, recordId, formData);
		}
	}

	@Override
	@PlusTransactional
	public FormDataEvent saveFormData(SaveFormDataEvent req) {
		FormData formData = req.getFormData();
		Map<String, Object> appData = formData.getAppData();
		if (appData.get("formCtxtId") == null || appData.get("objectId") == null) {
			return FormDataEvent.badRequest();
		}
		
		Long formCtxtId = ((Double)appData.get("formCtxtId")).longValue();
		Long objectId = ((Double)appData.get("objectId")).longValue();

		Long recordId = req.getRecordId();		
		formData.setRecordId(recordId);
		boolean isInsert = (recordId == null);
						
		FormDataManager formDataMgr = new FormDataManagerImpl(false);
		recordId = formDataMgr.saveOrUpdateFormData(null, req.getFormData());
				
		SessionDataBean session = req.getSessionDataBean();
		FormRecordEntryBean recordEntry = null;
		if (isInsert) {
			recordEntry = new FormRecordEntryBean();						
		} else {
			recordEntry = formDao.getRecordEntry(formCtxtId, objectId, recordId);
		}
		
		recordEntry.setFormCtxtId(formCtxtId);
		recordEntry.setObjectId(objectId);
		recordEntry.setRecordId(recordId);
		recordEntry.setUpdatedBy(session.getUserId());
		recordEntry.setUpdatedTime(Calendar.getInstance().getTime());
		formDao.saveOrUpdateRecordEntry(recordEntry);
		
		formData.setRecordId(recordId);
		return FormDataEvent.ok(formData.getContainer().getId(), recordId, formData);		
	}
	
	private List<FormFieldSummary> getFormFields(Container container, String captionPrefix, String namePrefix) {
        List<FormFieldSummary> fields = new ArrayList<FormFieldSummary>();

        for (Control control : container.getControls()) {
                if (control instanceof SubFormControl) {
                        SubFormControl sfCtrl = (SubFormControl)control;
                        Container sf = sfCtrl.getSubContainer();
                        String sfCaptionPrefix = captionPrefix + sf.getCaption() + ": ";
                        String sfNamePrefix = namePrefix + sfCtrl.getUserDefinedName() + ".";
                        fields.addAll(getFormFields(sf, sfCaptionPrefix, sfNamePrefix));
                        
                } else if (!(control instanceof Label || control instanceof PageBreak)) {
                        FormFieldSummary field = new FormFieldSummary();
                        field.setName(namePrefix + control.getUserDefinedName());
                        field.setCaption(captionPrefix + control.getCaption());

                        DataType dataType = (control instanceof FileUploadControl) ? DataType.STRING : control.getDataType();
                        field.setDataType(dataType.toString());

                        if (control instanceof SelectControl) {
                                SelectControl selectCtrl = (SelectControl)control;                                
                                List<String> pvs = new ArrayList<String>();
                                for (PermissibleValue pv : selectCtrl.getPvs()) {
                                    pvs.add(pv.getValue());
                                }

                                field.setPvs(pvs);
                        }

                        fields.add(field);
                }
        }

        return fields;		
	}
	
}
