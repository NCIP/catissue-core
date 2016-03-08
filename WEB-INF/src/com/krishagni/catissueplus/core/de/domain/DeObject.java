package com.krishagni.catissueplus.core.de.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import krishagni.catissueplus.beans.FormContextBean;
import krishagni.catissueplus.beans.FormRecordEntryBean;
import krishagni.catissueplus.beans.FormRecordEntryBean.Status;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseExtensionEntity;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.de.events.ExtensionDetail;
import com.krishagni.catissueplus.core.de.events.FormRecordSummary;
import com.krishagni.catissueplus.core.de.events.FormSummary;
import com.krishagni.catissueplus.core.de.events.ExtensionDetail.AttrDetail;
import com.krishagni.catissueplus.core.de.repository.DaoFactory;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.SubFormControl;
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
	
	private boolean useUdn = false;
	
	private List<Attr> attrs = new ArrayList<Attr>();
	
	private static Map<String, String> entityTypeFormNameMap = new HashMap<String, String>();
 	
	private static Map<String, Container> formMap = new HashMap<String, Container>();
	
	private static Map<String, FormContextBean> formCtxMap = new HashMap<String, FormContextBean>();
	
	public DeObject() { }
	
	public DeObject(boolean useUdn) {
		this.useUdn = useUdn;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public boolean isRecordLoaded() {
		return recordLoaded;
	}

	public void setRecordLoaded(boolean recordLoaded) {
		this.recordLoaded = recordLoaded;
	}

	public boolean isUseUdn() {
		return useUdn;
	}

	public void setUseUdn(boolean useUdn) {
		this.useUdn = useUdn;
	}

	public List<Attr> getAttrs() {
		loadRecordIfNotLoaded();
		return attrs;
	}

	public void setAttrs(List<Attr> attrs) {
		this.attrs = attrs;
	}
	
	public Long getFormId() {
		return getForm().getId(); 
	}

	public void saveOrUpdate() {
		try {
			Container form = getForm();
			UserContext userCtx = getUserCtx();
			FormData formData = prepareFormData(form);
			
			boolean isInsert = (this.id == null);						
			this.id = formDataMgr.saveOrUpdateFormData(userCtx, formData);
			
			if (isInsert) {
				saveRecordEntry();			
			}
			
			attrs.clear();
			attrs.addAll(getAttrs(formData));
		} catch (Exception e) {
			throw OpenSpecimenException.serverError(e);
		}
	}
	
	public void saveRecordEntry() {
		UserContext userCtx = getUserCtx();
		Long formCtxtId = getFormContext().getIdentifier();
		FormRecordEntryBean re = prepareRecordEntry(userCtx, formCtxtId, getId());
		daoFactory.getFormDao().saveOrUpdateRecordEntry(re);
	}
	
	public void delete() {
		if (getId() == null) {
			return;
		}

		Long formCtxId = getFormContext().getIdentifier();
		Long objectId = getObjectId();
		FormRecordEntryBean re = daoFactory.getFormDao().getRecordEntry(formCtxId, objectId, getId());
		if (re == null) {
			return;
		}

		re.setActivityStatus(Status.CLOSED);
		daoFactory.getFormDao().saveOrUpdateRecordEntry(re);
	}
	
	/** Hackish method */
	public List<Long> getRecordIds() {
		FormContextBean formCtx = getFormContext();
		if (formCtx == null) {
			return null;
		}
		
		List<FormRecordSummary> records = daoFactory.getFormDao()
				.getFormRecords(formCtx.getIdentifier(), getObjectId());
		
		List<Long> recIds = new ArrayList<Long>();
		for (FormRecordSummary rec : records) {
			recIds.add(rec.getRecordId());
		}
		
		return recIds;
	}
	
	public boolean hasPhiFields() {
		return getForm().hasPhiFields();
	}
	
	public void copyAttrsTo(DeObject other) {
		for (Attr attr : getAttrs()) {
			other.getAttrs().add(attr.copy());
		}
	}
	
	protected void loadRecordIfNotLoaded() {
		Long recordId = getId();
		if (recordLoaded || recordId == null) {
			return;
		}
		
		recordLoaded = true;
		attrs.clear();

		FormData formData = formDataMgr.getFormData(getForm(), recordId);
		if (formData == null) {
			return;
		}
		
		attrs.addAll(getAttrs(formData));
		
		Map<String, Object> attrValues = new HashMap<String, Object>();
		for (ControlValue cv : formData.getOrderedFieldValues()) {
			attrValues.put(cv.getControl().getUserDefinedName(), cv.getValue());
		}
		
		setAttrValues(attrValues);
	}
	
	protected String getFormNameByEntityType() {
		if (!entityTypeFormNameMap.containsKey(getEntityType())) {
			synchronized(entityTypeFormNameMap) {
				List<FormSummary> forms = daoFactory.getFormDao().getFormsByEntityType(getEntityType());
				String formName = forms.isEmpty() ? null: forms.get(0).getName();
				entityTypeFormNameMap.put(getEntityType(), formName);
			}
		}
		
		return entityTypeFormNameMap.get(getEntityType());
	}
	
	public abstract Long getObjectId();
	
	public abstract String getEntityType();
	
	public abstract String getFormName();
	
	public abstract Long getCpId();
	
	public Map<String, Object> getAttrValues() {
		Map<String, Object> vals = new HashMap<String, Object>();
		for (Attr attr: attrs) {
			if (isUseUdn()) {
				vals.put(attr.getUdn(), attr.getValue());
			} else {
				vals.put(attr.getName(), attr.getValue());
			}
		}
		
		return vals;
	}
	
	public abstract void setAttrValues(Map<String, Object> attrValues);
	
	public static Long saveFormData(
			final String formName, 
			final String entityType, 
			final Long cpId,
			final Long objectId, 
			final Map<String, Object> values) {
		
		DeObject object = new DeObject() {
			@Override
			public void setAttrValues(Map<String, Object> attrValues) {				
			}	
			
			@Override
			public Long getObjectId() {
				return objectId;
			}
			
			@Override
			public String getFormName() {
				if (StringUtils.isBlank(formName)) {
					return getFormNameByEntityType();
				}
				return formName;
			}
			
			@Override
			public String getEntityType() {
				return entityType;
			}
			
			@Override
			public Long getCpId() {
				return cpId;
			}
			
			@Override
			public Map<String, Object> getAttrValues() {
				return values;
			}
		};

		object.saveOrUpdate();
		return object.getId();
	}
	
	public static DeObject createExtension(ExtensionDetail detail, BaseExtensionEntity entity) {
		if (detail == null) {
			return null;
		}
		
		DeObject extension = entity.createExtension();
		if (extension == null) {
			return null;
		}
		
		Map<String, Attr> existingAttrs = new HashMap<String, Attr>();
		for (Attr attr : extension.getAttrs()) {
			existingAttrs.put(attr.getName(), attr);
		}
		
		List<Attr> attrs = new ArrayList<Attr>();
		for (AttrDetail attrDetail : detail.getAttrs()) {
			Attr attr = existingAttrs.remove(attrDetail.getName());
			if (attr == null) {
				attr = new Attr();
			} 
			
			BeanUtils.copyProperties(attrDetail, attr, new String[]{"caption"});
			attrs.add(attr);
		}
		
		attrs.addAll(existingAttrs.values());
		extension.setAttrs(attrs);
		return extension;
	}
	
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
		FormData formData = FormData.getFormData(container, getAttrValues(), useUdn, null);
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
		String formName = getFormName();
		if (StringUtils.isBlank(formName)) {
			return null;
		}
		
		FormContextBean formCtxt = formCtxMap.get(formName);
		if (formCtxt == null) {
			synchronized (formCtxMap) {
				Long formId = getForm().getId();
				formCtxt = daoFactory.getFormDao().getFormContext(formId, getCpId(), getEntityType());
				formCtxMap.put(formName, formCtxt);
			}
		}
		
		return formCtxt;
	}
	
	private List<Attr> getAttrs(FormData formData) {
		List<Attr> attrs = new ArrayList<Attr>();
		for (ControlValue cv : formData.getOrderedFieldValues()) {
			if (cv == null) {
				continue;
			}
                
			if (cv.getControl() instanceof SubFormControl) {
				SubFormControl sfCtrl = (SubFormControl)cv.getControl();
				if (sfCtrl.isOneToOne()) {
					cv.setValue(getAttrs((FormData)cv.getValue()));
				} else {
					List<List<Attr>> values = new ArrayList<List<Attr>>();
					for (Object fd : (List)cv.getValue()) {
						values.add(getAttrs((FormData)fd));
					}
					cv.setValue(values);
				}
			}
			attrs.add(Attr.from(cv));
		}

		return attrs;
	}

	public static class Attr {
		private String name;
		
		private String udn;
		
		private String caption;
		
		private Object value;
		
		private String type;
		
		private boolean phi;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUdn() {
			return udn;
		}

		public void setUdn(String udn) {
			this.udn = udn;
		}

		public String getCaption() {
			return caption;
		}

		public void setCaption(String caption) {
			this.caption = caption;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}
		
		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public boolean isPhi() {
			return phi;
		}

		public void setPhi(boolean phi) {
			this.phi = phi;
		}

		public static Attr from(ControlValue cv) {
			Attr attr = new Attr();
			attr.setName(cv.getControl().getName()); 
			attr.setUdn(cv.getControl().getUserDefinedName());
			attr.setCaption(cv.getControl().getCaption());
			attr.setType(cv.getControl().getCtrlType());
			attr.setPhi(cv.getControl().isPhi());
			
			Object value = cv.getValue();
			if (value != null && value.getClass().isArray()) {
				value = Arrays.asList((String[])value);
			}
			attr.setValue(value);
			
			return attr;
		}
		
		public Attr copy() {
			Attr attr = new Attr();
			BeanUtils.copyProperties(this, attr);
			return attr;
		}
	}
}
