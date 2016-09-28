package com.krishagni.catissueplus.core.de.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseExtensionEntity;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.MessageUtil;
import com.krishagni.catissueplus.core.de.events.ExtensionDetail;
import com.krishagni.catissueplus.core.de.events.ExtensionDetail.AttrDetail;
import com.krishagni.catissueplus.core.de.events.FormRecordSummary;
import com.krishagni.catissueplus.core.de.repository.DaoFactory;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.SubFormControl;
import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FileControlValue;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.FormDataManager;
import krishagni.catissueplus.beans.FormRecordEntryBean;
import krishagni.catissueplus.beans.FormRecordEntryBean.Status;

@Configurable
public abstract class DeObject {
	private static FormInfoCache formInfoCache = new FormInfoCache();

	@Autowired
	private FormDataManager formDataMgr;
	
	@Autowired
	protected DaoFactory daoFactory;
	
	private Long id;
	
	private boolean recordLoaded = false;
	
	private boolean useUdn = false;
	
	private List<Attr> attrs = new ArrayList<Attr>();

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
		FormRecordEntryBean re = prepareRecordEntry(getUserCtx(), getFormContext(), getId());
		daoFactory.getFormDao().saveOrUpdateRecordEntry(re);
	}
	
	public void delete() {
		if (getId() == null) {
			return;
		}

		FormRecordEntryBean re = daoFactory.getFormDao().getRecordEntry(getFormContext(), getObjectId(), getId());
		if (re == null) {
			return;
		}

		re.setActivityStatus(Status.CLOSED);
		daoFactory.getFormDao().saveOrUpdateRecordEntry(re);
	}
	
	/** Hackish method */
	public List<Long> getRecordIds() {
		Long formCtx = getFormContext();
		if (formCtx == null) {
			return null;
		}
		
		List<FormRecordSummary> records = daoFactory.getFormDao().getFormRecords(formCtx, getObjectId());
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
		return getFormNameByEntityType(-1L);
	}

	protected String getFormNameByEntityType(Long cpId) {
		if (cpId == null || cpId <= 0L) {
			cpId = -1L;
		}

		String formName = formInfoCache.getFormName(cpId, getEntityType());
		if (StringUtils.isBlank(formName) && cpId != -1L) {
			formName = formInfoCache.getFormName(-1L, getEntityType());
		}

		return formName;
	}
	
	public abstract Long getObjectId();
	
	public abstract String getEntityType();
	
	public abstract String getFormName();
	
	public abstract Long getCpId();

	public abstract void setAttrValues(Map<String, Object> attrValues);

	public Map<String, Object> getAttrValues() {
		loadRecordIfNotLoaded();
		return getAttrValues(attrs);
	}

	public Map<String, String> getLabelValueMap() {
		String notSpecified = MessageUtil.getInstance().getMessage("common_not_specified");
		return getAttrs().stream().collect(
			Collectors.toMap(
				attr -> attr.getCaption(),
				attr -> attr.getDisplayValue(notSpecified),
				(v1, v2) -> {throw new IllegalStateException("Duplicate key");},
				LinkedHashMap::new)
		);
	}

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
					return getFormNameByEntityType(getCpId());
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

	public static Map<String, Object> getFormInfo(Long cpId, String entity) {
		return formInfoCache.getFormInfo(cpId, entity);
	}

	public static Container getForm(String formName) {
		return formInfoCache.getForm(formName);
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
		return formInfoCache.getForm(getFormName());
	}

	private Long getFormContext() {
		String formName = getFormName();
		if (StringUtils.isBlank(formName)) {
			return null;
		}

		Long cpId = getCpId();
		Long formCtxt = formInfoCache.getFormContext(cpId, getEntityType(), formName);
		if (formCtxt == null && cpId != -1) {
			formCtxt = formInfoCache.getFormContext(-1L, getEntityType(), formName);
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

	private Map<String, Object> getAttrValues(List<Attr> attrs) {
		Map<String, Object> result = new LinkedHashMap<>();

		Object value = null;
		for (Attr attr : attrs) {
			if (attr.isSubForm()) {
				if (attr.isOneToOne()) {
					value = getAttrValues((List<Attr>)attr.getValue());
				} else {
					value = ((List<List<Attr>>)attr.getValue()).stream()
						.map(sfAttrs -> getAttrValues(sfAttrs))
						.collect(Collectors.toList());
				}
			} else {
				value = attr.getValue();
			}

			result.put(isUseUdn() ? attr.getUdn() : attr.getName(), value);
		}

		return result;
	}

	public static class Attr {
		private ControlValue ctrlValue;

		private String name;

		private String udn;

		private String caption;

		private Object value;

		private String type;

		public ControlValue getCtrlValue() {
			return ctrlValue;
		}

		public void setCtrlValue(ControlValue ctrlValue) {
			this.ctrlValue = ctrlValue;
		}

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

		public String getDisplayValue() {
			return ctrlValue.getControl().toDisplayValue(ctrlValue.getValue());
		}

		public String getDisplayValue(String defValue) {
			String result = getDisplayValue();
			if (StringUtils.isBlank(result)) {
				return  defValue;
			}

			return result;
		}

		public boolean isPhi() {
			return ctrlValue.getControl().isPhi();
		}
		
		public boolean isSubForm() {
			return "subForm".equals(type);
		}
		
		public boolean isOneToOne() {
			if (ctrlValue != null && ctrlValue.getControl() instanceof SubFormControl) {
				return ((SubFormControl)ctrlValue.getControl()).isOneToOne();
			}

			return false;
		}

		public Attr copy() {
			Attr copy = new Attr();
			BeanUtils.copyProperties(this, copy);
			return copy;
		}

		public static Attr from(ControlValue cv) {
			Attr attr = new Attr();
			attr.setCtrlValue(cv);
			attr.setName(cv.getControl().getName());
			attr.setUdn(cv.getControl().getUserDefinedName());
			attr.setCaption(cv.getControl().getCaption());
			attr.setType(cv.getControl().getCtrlType());

			Object value = cv.getValue();
			if (value != null && value.getClass().isArray()) {
				value = Arrays.asList((String[])value);
			} else if (value instanceof FileControlValue) {
				value = ((FileControlValue) value).toValueMap();
			}

			attr.setValue(value);
			return attr;
		}
	}
}