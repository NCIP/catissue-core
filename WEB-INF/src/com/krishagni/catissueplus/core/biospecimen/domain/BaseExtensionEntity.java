package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.de.domain.DeObject;

public abstract class BaseExtensionEntity extends BaseEntity {
	private DeObject extension;
	
	public DeObject getExtension() {
		if (extension == null) {
			extension = createExtension();
		}
		
		return extension;
	}

	public void setExtension(DeObject extension) {
		this.extension = extension;
	}
	
	public void addOrUpdateExtension() {
		if (extension == null) {
			return;			
		}
		
		extension.saveOrUpdate();
	}
	
	public boolean hasPhiFields() {
		return getExtension() == null ? false : getExtension().hasPhiFields();
	}

	public DeObject createExtension() {
		DeObject extnObj = new DeObject() {	
			@Override
			public void setAttrValues(Map<String, Object> attrValues) {
				// TODO Auto-generated method stub				
			}
			
			@Override
			public Long getObjectId() {
				return BaseExtensionEntity.this.getId();
			}
			
			@Override
			public String getFormName() {
				return getFormNameByEntityType();
			}
			
			@Override
			public String getEntityType() {
				return BaseExtensionEntity.this.getEntityType();
			}
			
			@Override
			public Long getCpId() {
				return -1L;
			} 
		};
		
		if (StringUtils.isBlank(extnObj.getFormName())) {
			return null;
		}
		
		if (getId() == null) {
			return extnObj;
		}
		
		Long recId = getRecordId(extnObj);
		extnObj.setId(recId);
		return extnObj;
	}
	
	public abstract String getEntityType();
	
	private Long getRecordId(DeObject extnObj) {
		List<Long> recIds = extnObj.getRecordIds();
		if (CollectionUtils.isEmpty(recIds)) {
			return null;
		}
		
		return recIds.iterator().next();		
	}
}
