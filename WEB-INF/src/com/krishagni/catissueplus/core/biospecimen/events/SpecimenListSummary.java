package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenList;
import com.krishagni.catissueplus.core.common.AttributeModifiedSupport;
import com.krishagni.catissueplus.core.common.ListenAttributeChanges;
import com.krishagni.catissueplus.core.common.events.UserSummary;

@ListenAttributeChanges
public class SpecimenListSummary extends AttributeModifiedSupport {
	private Long id;
	
	private String name;

	private String description;

	private UserSummary owner;

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

	public UserSummary getOwner() {
		return owner;
	}

	public void setOwner(UserSummary owner) {
		this.owner = owner;
	}
	
	public static SpecimenListSummary fromSpecimenList(SpecimenList list){
		SpecimenListSummary listSummary = new SpecimenListSummary();
		listSummary.setId(list.getId());
		listSummary.setName(list.getName());
		listSummary.setDescription(list.getDescription());
		listSummary.setOwner(UserSummary.from(list.getOwner()));
		return listSummary;
	}
}
