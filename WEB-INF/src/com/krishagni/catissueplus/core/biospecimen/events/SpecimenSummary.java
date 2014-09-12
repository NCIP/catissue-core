package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public class SpecimenSummary {
	private Long id;

	private String label;
	
	private String lineage;

	private String specimenClass;

	private String specimenType;

	private String tissueSite;

	private String tissueSide;
	
	private String cpShortTitle;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLineage() {
		return lineage;
	}

	public void setLineage(String lineage) {
		this.lineage = lineage;
	}

	public String getSpecimenClass() {
		return specimenClass;
	}

	public void setSpecimenClass(String specimenClass) {
		this.specimenClass = specimenClass;
	}

	public String getSpecimenType() {
		return specimenType;
	}

	public void setSpecimenType(String specimenType) {
		this.specimenType = specimenType;
	}

	public String getTissueSite() {
		return tissueSite;
	}

	public void setTissueSite(String tissueSite) {
		this.tissueSite = tissueSite;
	}

	public String getTissueSide() {
		return tissueSide;
	}

	public void setTissueSide(String tissueSide) {
		this.tissueSide = tissueSide;
	}

	public String getCpShortTitle() {
		return cpShortTitle;
	}

	public void setCpShortTitle(String cpShortTitle) {
		this.cpShortTitle = cpShortTitle;
	}

	public static SpecimenSummary from(Specimen specimen) {
		SpecimenSummary summary = new SpecimenSummary();
		BeanUtils.copyProperties(specimen, summary);
		summary.setCpShortTitle(specimen.getSpecimenCollectionGroup()
				.getCollectionProtocolRegistration().getCollectionProtocol().getShortTitle());
		return summary;
	}
	
	public static List<SpecimenSummary> from(Collection<Specimen> specimens) {
		List<SpecimenSummary> summaries = new ArrayList<SpecimenSummary>();
		for (Specimen specimen : specimens) {
			summaries.add(SpecimenSummary.from(specimen));
		}
		
		return summaries;
	}
}
