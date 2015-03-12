package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class CollectionEvent extends SpecimenEvent {
	private String procedure;
	
	private String container;

	public CollectionEvent(Specimen specimen) {
		super(specimen);
	}
	
	public String getProcedure() {
		loadRecordIfNotLoaded();
		return procedure;
	}

	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}

	public String getContainer() {
		loadRecordIfNotLoaded();
		return container;
	}

	public void setContainer(String container) {
		this.container = container;
	}

	@Override
	public String getFormName() {
		return "SpecimenCollectionEvent"; 
	}
	
	@Override
	public Map<String, Object> getEventAttrs() {
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("procedure", procedure);
		attrs.put("container", container);
		return attrs;
	}
	
	@Override
	public void setEventAttrs(Map<String, Object> attrValues) {
		this.procedure = (String)attrValues.get("procedure");
		this.container = (String)attrValues.get("container");		
	}
		
	public static CollectionEvent getFor(Specimen specimen) {
		List<Long> recIds = new CollectionEvent(specimen).getRecordIds();		
		if (CollectionUtils.isEmpty(recIds)) {
			return null;
		}
		
		CollectionEvent event = new CollectionEvent(specimen);
		event.setId(recIds.iterator().next());
		return event;		
	}
}
