package com.krishagni.catissueplus.core.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.common.service.ObjectCopier;

public abstract class AbstractObjectCopier<T> implements ObjectCopier<T> {
	private List<AttributesCopier<T>> attrCopiers = new ArrayList<AttributesCopier<T>>();
	
	public void addAttrCopier(AttributesCopier<T> attrCopier) {
		attrCopiers.add(attrCopier);
	}

	public void setAttrCopiers(List<AttributesCopier<T>> attrCopiers) {
		this.attrCopiers = attrCopiers;
	}
	
	public void copy(T source, T target) {
		attrCopiers.forEach(attrCopier -> attrCopier.copy(source, target));
	}
}
