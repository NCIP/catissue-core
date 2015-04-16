package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.Date;
import java.util.Set;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class CprListCriteria extends AbstractListCriteria<CprListCriteria> {
	
	private Long cpId;
	
	private String ppid;
	
	private String mrn;
	
	private String empi;
	
	private String name;
	
	private Date dob;
	
	private String specimen;
	
	private Set<Long> siteIds;

	@Override
	public CprListCriteria self() {
		return this;
	}
	
	public Long cpId() {
		return cpId;
	}
	
	public CprListCriteria cpId(Long cpId) {
		this.cpId = cpId;
		return self();
	}
	
	public String ppid() {
		return ppid;
	}
	
	public CprListCriteria ppid(String ppid) {
		this.ppid = ppid;
		return self();
	}
	
	public String mrn() {
		return mrn;
	}
	
	public CprListCriteria mrn(String mrn) {
		this.mrn = mrn;
		return self();
	}
	
	public CprListCriteria empi(String empi) {
		this.empi = empi;
		return self();
	}
	
	public String empi() {
		return empi;
	}
	
	public CprListCriteria name(String name) {
		this.name = name;
		return self();
	}
	
	public String name() {
		return name;
	}

	public CprListCriteria dob(Date dob) {
		this.dob = dob;
		return self();
	}
	
	public Date dob() {
		return dob;
	}
	
	public CprListCriteria specimen(String specimen) {
		this.specimen = specimen;
		return self();
	}
	
	public String specimen() {
		return specimen;
	}
	
	public Set<Long> siteIds() {
		return siteIds;
	}
	
	public void siteIds(Set<Long> siteIds) {
		this.siteIds = siteIds;
	}
}
