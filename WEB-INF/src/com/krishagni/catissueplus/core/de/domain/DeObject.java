package com.krishagni.catissueplus.core.de.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import krishagni.catissueplus.beans.FormContextBean;
import krishagni.catissueplus.beans.FormRecordEntryBean;
import krishagni.catissueplus.beans.FormRecordEntryBean.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.de.events.FormRecordSummary;
import com.krishagni.catissueplus.core.de.repository.DaoFactory;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.FormDataManager;

@Configurable
public abstract class DeObject {	
	@Autowired
	private FormDataManager formDataMgr;
	
	@Autowired
	protected DaoFactory daoFactory;
	
	private Long id;
	
	private boolean recordLoaded = false;
	
	private static Map<String, Container> formMap = new HashMap<String, Container>();
	
	private static Map<String, FormContextBean> formCtxMap = new HashMap<String, FormContextBean>();
	
	public void saveOrUpdate() {
		try {
			Container form = getForm();
			UserContext userCtx = getUserCtx();
			FormData formData = prepareFormData(form);
			
			boolean isInsert = (this.id == null);						
			Long recordId = formDataMgr.saveOrUpdateFormData(userCtx, formData);
			
			if (isInsert) {
				Long formCtxtId = getFormContext().getIdentifier();
				FormRecordEntryBean re = prepareRecordEntry(userCtx, formCtxtId, recordId);
				daoFactory.getFormDao().saveOrUpdateRecordEntry(re);				
			}
			
			this.id = recordId;
		} catch (Exception e) {
			throw OpenSpecimenException.serverError(e);
		}		 
	}
	
	public void delete() {
		if (getId() == null) {
			return;
		}
		
		FormRecordEntryBean re = daoFactory.getFormDao().getRecordEntry(getId());
		re.setActivityStatus(Status.CLOSED);
		daoFactory.getFormDao().saveOrUpdateRecordEntry(re);
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	/** Hackish method */
	protected List<Long> getRecordIds() {
		Long formCtxtId = getFormContext().getIdentifier();
		List<FormRecordSummary> records = daoFactory.getFormDao()
				.getFormRecords(formCtxtId, getObjectId());
		
		List<Long> recIds = new ArrayList<Long>();
		for (FormRecordSummary rec : records) {
			recIds.add(rec.getRecordId());
		}
		
		return recIds;
	}
	
	protected void loadRecordIfNotLoaded() {
		if (recordLoaded) {
			return;
		}
		
		recordLoaded = true;
		
		Long recordId = getId();
		if (recordId == null) {
			return;
		}
		
		FormData formData = formDataMgr.getFormData(getForm(), recordId);		
		if (formData == null) {
			return;
		}
		
		Map<String, Object> attrValues = new HashMap<String, Object>();
		for (ControlValue cv : formData.getFieldValues()) {
			attrValues.put(cv.getControl().getUserDefinedName(), cv.getValue());
		}
		
		setAttrValues(attrValues);
	}
			
	public abstract Long getObjectId();
	
	public abstract String getEntityType();
	
	public abstract String getFormName();
	
	public abstract Long getCpId();
	
	public abstract Map<String, Object> getAttrValues();
	
	public abstract void setAttrValues(Map<String, Object> attrValues); 
	
	private UserContext getUserCtx() {
		final User user = AuthUtil.getCurrentUser();
		return new UserContext() {
			
			@Override
			public String getUserName() {				
				return user.getUsername();
			}
			
			@Override
			public Long getUserId() {
				return user.getId();
			}
			
			@Override
			public String getIpAddress() {
				return null;
			}
		};
	}
	
	private FormData prepareFormData(Container container) {
		FormData formData = new FormData(container);
		
		for (Map.Entry<String, Object> attr : getAttrValues().entrySet()) {
			Control ctrl = container.getControlByUdn(attr.getKey());
			ControlValue cv = new ControlValue(ctrl, attr.getValue());
			formData.addFieldValue(cv);
		}
					
		formData.setRecordId(this.id);
		return formData;		
	}
	
	
	private FormRecordEntryBean prepareRecordEntry(UserContext userCtx, Long formCtxId, Long recordId) {
		FormRecordEntryBean re = new FormRecordEntryBean();
		re.setFormCtxtId(formCtxId);
		re.setObjectId(getObjectId());
		re.setRecordId(recordId);
		re.setUpdatedBy(userCtx.getUserId());
		re.setUpdatedTime(Calendar.getInstance().getTime());
		re.setActivityStatus(Status.ACTIVE);
		return re;
	}	
		
	private Container getForm() {
		Container form = formMap.get(getFormName());
		if (form == null) {
			synchronized (formMap) {
				form = Container.getContainer(getFormName());
				formMap.put(getFormName(), form);
			} 
		}
		
		return form;
	}
	
	private FormContextBean getFormContext() {
		FormContextBean formCtxt = formCtxMap.get(getFormName());
		if (formCtxt == null) {
			synchronized (formCtxMap) {
				Long formId = getForm().getId();
				formCtxt = daoFactory.getFormDao().getFormContext(formId, getCpId(), getEntityType());
				formCtxMap.put(getFormName(), formCtxt);
			}
		}
		
		return formCtxt; 
	}	
}