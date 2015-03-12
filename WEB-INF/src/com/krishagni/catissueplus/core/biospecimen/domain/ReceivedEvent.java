package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

public class ReceivedEvent extends SpecimenEvent {
	private String quality;

	public ReceivedEvent(Specimen specimen) {
		super(specimen);
	}
	
	public String getQuality() {
		loadRecordIfNotLoaded();
		return quality;
	}

	public void setQuality(String quality) {
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
	
	public static ReceivedEvent getFor(Specimen specimen) {
		List<Long> recIds = new ReceivedEvent(specimen).getRecordIds();		
		if (CollectionUtils.isEmpty(recIds)) {
			return null;
		}
		
		ReceivedEvent event = new ReceivedEvent(specimen);
		event.setId(recIds.iterator().next());
		return event;		
	}
}
