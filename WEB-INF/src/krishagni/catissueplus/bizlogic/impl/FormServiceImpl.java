package krishagni.catissueplus.bizlogic.impl;

import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.FormDataManager;
import edu.common.dynamicextensions.napi.impl.FormDataManagerImpl;

import krishagni.catissueplus.bizlogic.FormService;
import krishagni.catissueplus.dao.FormDao;
import krishagni.catissueplus.dao.impl.FormDaoImpl;
import krishagni.catissueplus.dto.FormDetailsDTO;
import krishagni.catissueplus.dto.FormRecordDetailsDTO;

public class FormServiceImpl implements FormService {
	
	private FormDao formDao = new FormDaoImpl();

	@Override
	public Container getFormDefinition(Long formId) {
		Long containerId = formDao.getContainerId(formId);
		return Container.getContainer(containerId);
	}	
	
	@Override
	public FormData getFormData(Long formId, Long recordId) {
		Long containerId = formDao.getContainerId(formId);
		FormDataManager formDataMgr = new FormDataManagerImpl(false);
		return formDataMgr.getFormData(containerId, recordId);
	}
	
	@Override
	public List<FormDetailsDTO> getForms(Long cpId, String entity, Long objectId) {
		if (cpId == null) {
			return getForms(entity, objectId);
		}
		return formDao.getForms(cpId, entity, objectId);
	}

	@Override
	public List<FormDetailsDTO> getForms(String entity, Long objectId) {
		Long cpId = null;
		if (entity.equals("SpecimenCollectionGroup")) {
			cpId = formDao.getCpIdByScgId(objectId);
		} else if (entity.equals("Specimen")) {
			cpId = formDao.getCpIdBySpecimenId(objectId);
		} else if (entity.equals("Participant")) {
			cpId = formDao.getCpIdByRegistrationId(objectId);
		}
						
		return formDao.getForms(cpId, entity, objectId);
	}

	@Override
	public List<FormRecordDetailsDTO> getFormRecords(Long formId, Long objectId) {
		return formDao.getFormRecords(formId, objectId);
	}

	@Override
	public Long saveFormData(Long formId, Long recordId, String formDataJson) {
		boolean isInsert = (recordId == null);
		Long containerId = formDao.getContainerId(formId);		
		FormData formData = FormData.fromJson(formDataJson, containerId);
		Map<String, Object> appData = formData.getAppData();
		Long objectId = ((Double)appData.get("entityObjId")).longValue();
						
		FormDataManager formDataMgr = new FormDataManagerImpl(false);
		recordId = formDataMgr.saveOrUpdateFormData(null, formData);
		
		if (isInsert) {
			formDao.insertFormRecord(formId, objectId, recordId, null);			
		} else {
			formDao.updateFormRecord(formId, objectId, recordId, null);
		}
		
		return recordId;		
	}

	@Override
	public Long saveForm(FormDetailsDTO form) {
		if (form.getContainerId() == null) {
			throw new IllegalArgumentException("Container ID cannot be null");
		}
		
		if (form.getEntityType() == null) {
			throw new IllegalArgumentException("Entity type cannot be null");
		}
		
		FormDetailsDTO existing = formDao.getFormByContainerId(form.getCpId(), form.getEntityType(), form.getContainerId());
		if (existing != null) {
			form.setId(existing.getId());			
		} else {
			formDao.insertForm(form);
		}
				
		return form.getId();
	}
}