package com.krishagni.catissueplus.core.de.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.common.dynamicextensions.domain.nui.Container;

public class FormRecordsList {
	private Long id;
	
	private String name;
	
	private String caption;
	
	private List<FormRecordSummary> records;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public List<FormRecordSummary> getRecords() {
		return records;
	}

	public void setRecords(List<FormRecordSummary> records) {
		this.records = records;
	}
	
	public static FormRecordsList from(Container container, Collection<FormRecordSummary> recs) {
		FormRecordsList list = new FormRecordsList();
		list.setId(container.getId());
		list.setCaption(container.getCaption());
		list.setName(container.getName());
		list.setRecords(new ArrayList<FormRecordSummary>(recs));
		return list;
	}
}
