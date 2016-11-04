package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.Pair;
import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class SpecimenListCriteria extends AbstractListCriteria<SpecimenListCriteria> {
	private Long cpId;

	private String[] lineages;

	private String[] collectionStatuses;

	private List<Pair<Long, Long>> siteCps;
	
	private List<String> labels;
	
	private Long specimenListId;
	
	private boolean useMrnSites;

	private String storageLocationSite;

	public SpecimenListCriteria() {
		exactMatch(true);
	}
	
	@Override
	public SpecimenListCriteria self() {		
		return this;
	} 

	public Long cpId() {
		return cpId;
	}

	public SpecimenListCriteria cpId(Long cpId) {
		this.cpId = cpId;
		return self();
	}

	public String[] lineages() {
		return lineages;
	}

	public SpecimenListCriteria lineages(String[] lineages) {
		this.lineages = lineages;
		return self();
	}

	public String[] collectionStatuses() {
		return collectionStatuses;
	}

	public SpecimenListCriteria collectionStatuses(String[] collectionStatuses) {
		this.collectionStatuses = collectionStatuses;
		return self();
	}

	public List<Pair<Long, Long>> siteCps() {
		return siteCps;
	}
	
	public SpecimenListCriteria siteCps(List<Pair<Long, Long>> siteCps) {
		this.siteCps = siteCps;
		return self();
	}
	
	public List<String> labels() {
		return labels;
	}
	
	public SpecimenListCriteria labels(List<String> labels) {
		this.labels = labels;
		return self();
	}

	public Long specimenListId() {
		return specimenListId;
	}

	public SpecimenListCriteria specimenListId(Long specimenListId) {
		this.specimenListId = specimenListId;
		return self();
	}
	
	public boolean useMrnSites() {
		return this.useMrnSites;
	}
	
	public SpecimenListCriteria useMrnSites(boolean useMrnSites) {
		this.useMrnSites = useMrnSites;
		return self();
	}

	public String storageLocationSite() {
		return storageLocationSite;
	}

	public SpecimenListCriteria storageLocationSite(String storageLocationSite) {
		this.storageLocationSite = storageLocationSite;
		return self();
	}
}