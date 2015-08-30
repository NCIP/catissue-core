package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.krishagni.catissueplus.core.de.domain.DeObject;

public class CollectionProtocolExtension extends DeObject{
	private CollectionProtocol collectionProtocol;
	
	public CollectionProtocolExtension(CollectionProtocol collectionProtocol) {
		this.collectionProtocol = collectionProtocol;
	}
	
	public CollectionProtocol getCollectionProtocol() {
		return collectionProtocol;
	}

	public void setCollectionProtocol(CollectionProtocol collectionProtocol) {
		this.collectionProtocol = collectionProtocol;
	}

	@Override
	public Long getObjectId() {
		return collectionProtocol.getId();
	}

	@Override
	public String getEntityType() {
		return "CollectionProtocolExtension";
	}

	@Override
	public String getFormName() {
		return getFormNameByEntityType();
	}

	@Override
	public Long getCpId() {
		return -1L;
	}

	@Override
	public void setAttrValues(Map<String, Object> attrValues) {
		// TODO Auto-generated method stub
		
	}

	public static CollectionProtocolExtension getFor(CollectionProtocol cp) {
		CollectionProtocolExtension extension = new CollectionProtocolExtension(cp);
		if (cp.getId() == null) {
			return extension;
		}
		
		List<Long> recIds = extension.getRecordIds();
		if (CollectionUtils.isNotEmpty(recIds)) {
			extension.setId(recIds.iterator().next());
		}
		
		return extension;
	}
}
