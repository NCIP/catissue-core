package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.krishagni.catissueplus.core.common.util.AuthUtil;

public class SpecimenReceivedEvent extends SpecimenEvent {
	private String quality;

	public SpecimenReceivedEvent(Specimen specimen) {
		super(specimen);
	}
	
	public String getQuality() {
		loadRecordIfNotLoaded();
		return quality;
	}

	public void setQuality(String quality) {
		loadRecordIfNotLoaded();
		this.quality = quality;
	}

	@Override
	public String getFormName() {
		return "SpecimenReceivedEvent";
	}
	
	@Override
	public Map<String, Object> getEventAttrs() {		
		return Collections.<String, Object>singletonMap("quality", quality);
	}

	@Override
	public void setEventAttrs(Map<String, Object> attrValues) {
		this.quality = (String)attrValues.get("quality");
	}
	
	@Override
	public void update(SpecimenEvent other) {
		update((SpecimenReceivedEvent)other);
	}
	
	public static SpecimenReceivedEvent getFor(Specimen specimen) {
		if (specimen.getId() == null) {
			return createFromSr(specimen);
		}

		List<Long> recIds = new SpecimenReceivedEvent(specimen).getRecordIds();		
		if (CollectionUtils.isEmpty(recIds)) {
			return createFromSr(specimen);
		}
		
		SpecimenReceivedEvent event = new SpecimenReceivedEvent(specimen);
		event.setId(recIds.iterator().next());
		return event;		
	}
	
	public static SpecimenReceivedEvent createFromSr(Specimen specimen) {
		SpecimenReceivedEvent event = new SpecimenReceivedEvent(specimen);
		event.setQuality(Specimen.ACCEPTABLE);
		
		SpecimenRequirement sr = specimen.getSpecimenRequirement();
		if (sr != null) {
			event.setUser(sr.getReceiver());
		}
		
		event.setTime(Calendar.getInstance().getTime());		
		if (event.getUser() == null) {
			event.setUser(AuthUtil.getCurrentUser());
		}		
		
		return event;
	}	
	
	private void update(SpecimenReceivedEvent other) {
		super.update(other);
		setQuality(other.getQuality());
	}
}
