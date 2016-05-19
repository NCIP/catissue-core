package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Date;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenList;
import com.krishagni.catissueplus.core.common.AttributeModifiedSupport;
import com.krishagni.catissueplus.core.common.ListenAttributeChanges;
import com.krishagni.catissueplus.core.common.events.UserSummary;

@ListenAttributeChanges
public class SpecimenListSummary extends AttributeModifiedSupport {
	private Long id;
	
	private String name;

	private String description;

	private Date createdOn;

	private UserSummary owner;

	private boolean defaultList;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public UserSummary getOwner() {
		return owner;
	}

	public void setOwner(UserSummary owner) {
		this.owner = owner;
	}

	public boolean isDefaultList() {
		return defaultList;
	}

	public void setDefaultList(boolean defaultList) {
		this.defaultList = defaultList;
	}

	public static SpecimenListSummary fromSpecimenList(SpecimenList list){
		SpecimenListSummary listSummary = new SpecimenListSummary();
		listSummary.setId(list.getId());
		listSummary.setName(list.getName());
		listSummary.setDescription(list.getDescription());
		listSummary.setCreatedOn(list.getCreatedOn());
		listSummary.setOwner(UserSummary.from(list.getOwner()));
		listSummary.setDefaultList(list.isDefaultList());
		return listSummary;
	}
}
