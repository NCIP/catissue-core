package com.krishagni.catissueplus.core.de.domain.factory.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseExtensionEntity;
import com.krishagni.catissueplus.core.de.domain.DeObject;
import com.krishagni.catissueplus.core.de.domain.DeObject.Attr;
import com.krishagni.catissueplus.core.de.domain.factory.ExtensionFactory;
import com.krishagni.catissueplus.core.de.events.ExtensionDetail;
import com.krishagni.catissueplus.core.de.events.ExtensionDetail.AttrDetail;

public class ExtensionFactoryImpl implements ExtensionFactory {

	@Override
	public DeObject createExtension(ExtensionDetail detail, BaseExtensionEntity entity) {
		if (detail == null) {
			return null;
		}
		
		DeObject extension = entity.createExtension();
		if (extension == null) {
			return null;
		}
		
		Map<String, Attr> existingAttrs = new HashMap<String, DeObject.Attr>();
		for (Attr attr: extension.getAttrs()) {
			existingAttrs.put(attr.getName(), attr);
		}
		
		List<Attr> attrs = new ArrayList<Attr>();
		for (AttrDetail attrDetail: detail.getAttrs()) {
			Attr attr = existingAttrs.get(attrDetail.getName());
			if (attr == null) {
				attr = new Attr();
			} 
			
			BeanUtils.copyProperties(attrDetail, attr, new String[]{"caption"});
			attrs.add(attr);
		}
		
		extension.setAttrs(attrs);
		return extension;
	}
	
}
