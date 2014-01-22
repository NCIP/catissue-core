package krishagni.catissueplus.bizlogic.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import krishagni.catissueplus.bizlogic.FormService;
import krishagni.catissueplus.dao.FormDao;
import krishagni.catissueplus.dao.impl.FormDaoImpl;
import krishagni.catissueplus.dto.FormDetailsDTO;
import krishagni.catissueplus.dto.FormFieldSummary;
import krishagni.catissueplus.dto.FormRecordDetailsDTO;
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
	private SessionDataBean session;

	private FormDao formDao = new FormDaoImpl();
	
	public FormServiceImpl(SessionDataBean session) {
		this.session = session;
	}

	@Override
	public Container getFormDefinition(Long formId) {
		Long containerId = formDao.getContainerId(formId);
		return Container.getContainer(containerId);
	}
	
	@Override
	public List<FormFieldSummary> getFormFields(Long formId) {
		Long containerId = formDao.getContainerId(formId);
		Container container = Container.getContainer(containerId);
		return getFormFields(container, "", "");
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
			formDao.insertFormRecord(formId, objectId, recordId, session.getUserId());			
		} else {
			formDao.updateFormRecord(formId, objectId, recordId, session.getUserId());
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