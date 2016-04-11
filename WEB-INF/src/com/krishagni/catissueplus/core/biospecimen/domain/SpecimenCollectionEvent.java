package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.common.util.AuthUtil;

@Configurable
public class SpecimenCollectionEvent extends SpecimenEvent {
	private String procedure;
	
	private String container;

	public SpecimenCollectionEvent(Specimen specimen) {
		super(specimen);
	}
	
	public String getProcedure() {
		loadRecordIfNotLoaded();
		return procedure;
	}

	public void setProcedure(String procedure) {
		loadRecordIfNotLoaded();
		this.procedure = procedure;
	}

	public String getContainer() {
		loadRecordIfNotLoaded();
		return container;
	}

	public void setContainer(String container) {
		loadRecordIfNotLoaded();
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
	
	@Override
	public void update(SpecimenEvent other) {
		update((SpecimenCollectionEvent)other);
	}
	
	public static SpecimenCollectionEvent getFor(Specimen specimen) {
		if (specimen.getId() == null) {
			return createFromSr(specimen);
		}
		
		List<Long> recIds = new SpecimenCollectionEvent(specimen).getRecordIds();		
		if (CollectionUtils.isEmpty(recIds)) {
			return createFromSr(specimen);
		}
		
		SpecimenCollectionEvent event = new SpecimenCollectionEvent(specimen);
		event.setId(recIds.iterator().next());
		return event;		
	}
	
	public static SpecimenCollectionEvent createFromSr(Specimen specimen) {
		SpecimenCollectionEvent event = new SpecimenCollectionEvent(specimen);
		event.setContainer(Specimen.NOT_SPECIFIED);
		event.setProcedure(Specimen.NOT_SPECIFIED);
		
		SpecimenRequirement sr = specimen.getSpecimenRequirement();
		if (sr != null) {
			event.setContainer(sr.getCollectionContainer());
			event.setProcedure(sr.getCollectionProcedure());
			event.setUser(sr.getCollector());
		}
		
		event.setTime(Calendar.getInstance().getTime());		
		if (event.getUser() == null) {
			event.setUser(AuthUtil.getCurrentUser());
		}		
		
		return event;
	}
	
	private void update(SpecimenCollectionEvent other) {
		super.update(other);
		setContainer(other.getContainer());
		setProcedure(other.getProcedure());
	}
}
