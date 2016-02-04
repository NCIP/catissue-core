package com.krishagni.catissueplus.core.de.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.de.events.AddRecordEntryOp;
import com.krishagni.catissueplus.core.de.events.EntityFormRecords;
import com.krishagni.catissueplus.core.de.events.FileDetail;
import com.krishagni.catissueplus.core.de.events.FormContextDetail;
import com.krishagni.catissueplus.core.de.events.FormCtxtSummary;
import com.krishagni.catissueplus.core.de.events.FormDataDetail;
import com.krishagni.catissueplus.core.de.events.FormFieldSummary;
import com.krishagni.catissueplus.core.de.events.FormRecordsList;
import com.krishagni.catissueplus.core.de.events.FormSummary;
import com.krishagni.catissueplus.core.de.events.FormType;
import com.krishagni.catissueplus.core.de.events.GetEntityFormRecordsOp;
import com.krishagni.catissueplus.core.de.events.GetFileDetailOp;
import com.krishagni.catissueplus.core.de.events.FormRecordCriteria;
import com.krishagni.catissueplus.core.de.events.GetFormRecordsListOp;
import com.krishagni.catissueplus.core.de.events.ListEntityFormsOp;
import com.krishagni.catissueplus.core.de.events.ListFormFields;
import com.krishagni.catissueplus.core.de.events.RemoveFormContextOp;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.napi.FormData;

public interface FormService {
	public ResponseEvent<List<FormSummary>> getForms(RequestEvent<String> req);
	
	public ResponseEvent<Container> getFormDefinition(RequestEvent<Long> req);
	
	public ResponseEvent<Boolean> deleteForm(RequestEvent<Long> req);
	
	public ResponseEvent<List<FormFieldSummary>> getFormFields(RequestEvent<ListFormFields> req);
	
	public ResponseEvent<List<FormContextDetail>> getFormContexts(RequestEvent<Long> req);
	
	public ResponseEvent<List<FormContextDetail>> addFormContexts(RequestEvent<List<FormContextDetail>> req);
	
	public ResponseEvent<Boolean> removeFormContext(RequestEvent<RemoveFormContextOp> req);
	
	public ResponseEvent<List<FormCtxtSummary>> getEntityForms(RequestEvent<ListEntityFormsOp> req);
	
	public ResponseEvent<EntityFormRecords> getEntityFormRecords(RequestEvent<GetEntityFormRecordsOp> req);
	
	public ResponseEvent<FormDataDetail> getFormData(RequestEvent<FormRecordCriteria> req);
	
	public ResponseEvent<FormDataDetail> saveFormData(RequestEvent<FormDataDetail> req);
	
	public ResponseEvent<List<FormData>> saveBulkFormData(RequestEvent<List<FormData>> req);

	public ResponseEvent<FileDetail> getFileDetail(RequestEvent<GetFileDetailOp> req);

	public ResponseEvent<FileDetail> uploadFile(RequestEvent<MultipartFile> req);

	public ResponseEvent<Long> deleteRecord(RequestEvent<FormRecordCriteria> req);

	public ResponseEvent<Long> addRecordEntry(RequestEvent<AddRecordEntryOp> req);

	public ResponseEvent<List<FormRecordsList>> getFormRecords(RequestEvent<GetFormRecordsListOp> req);
	
	public ResponseEvent<List<DependentEntityDetail>> getDependentEntities(RequestEvent<Long> req);
}