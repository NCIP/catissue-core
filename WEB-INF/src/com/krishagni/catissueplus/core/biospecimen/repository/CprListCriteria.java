package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.common.Pair;
import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class CprListCriteria extends AbstractListCriteria<CprListCriteria> {
	
	private Long cpId;
	
	private Date registrationDate;
	
	private String ppid;
	
	private String participantId;

	private String name;
	
	private Date dob;

	private String uid;
	
	private String specimen;

	//
	// [{[site ids], cp id}, ...]
	//
	private Set<Pair<Set<Long>, Long>> phiSiteCps;

	private Set<Pair<Set<Long>, Long>> siteCps;

	private boolean useMrnSites;

	@Override
	public CprListCriteria self() {
		return this;
	}
	
	public Long cpId() {
		return cpId;
	}
	
	public Date registrationDate() {
		return registrationDate;
	}
	
	public CprListCriteria registrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
		return self();
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
	
	public String participantId() {
		return participantId;
	}

	public CprListCriteria participantId(String participantId) {
		this.participantId = participantId;
		return self();
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

	public String uid() {
		return uid;
	}

	public CprListCriteria uid(String uid) {
		this.uid = uid;
		return self();
	}

	public CprListCriteria specimen(String specimen) {
		this.specimen = specimen;
		return self();
	}

	public String specimen() {
		return specimen;
	}

	public Set<Pair<Set<Long>, Long>> phiSiteCps() {
		return phiSiteCps;
	}

	public CprListCriteria phiSiteCps(Set<Pair<Set<Long>, Long>> phiSiteCps) {
		this.phiSiteCps = phiSiteCps;
		return self();
	}

	public Set<Pair<Set<Long>, Long>> siteCps() {
		return siteCps;
	}

	public CprListCriteria siteCps(Set<Pair<Set<Long>, Long>> siteCps) {
		this.siteCps = siteCps;
		return self();
	}

	public boolean useMrnSites() {
		return useMrnSites;
	}

	public CprListCriteria useMrnSites(boolean useMrnSites) {
		this.useMrnSites = useMrnSites;
		return self();
	}

	public boolean hasPhiFields() {
		return StringUtils.isNotBlank(name()) ||
			StringUtils.isNotBlank(participantId()) ||
			StringUtils.isNotBlank(uid()) ||
			dob() != null;
	}

	public Set<Long> phiCps() {
		if (CollectionUtils.isEmpty(phiSiteCps())) {
			return Collections.emptySet();
		}

		return phiSiteCps().stream().map(siteCp -> siteCp.second()).collect(Collectors.toSet());
	}
}