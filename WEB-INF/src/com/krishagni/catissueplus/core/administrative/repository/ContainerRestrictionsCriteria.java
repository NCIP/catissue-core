package com.krishagni.catissueplus.core.administrative.repository;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class ContainerRestrictionsCriteria extends AbstractListCriteria<ContainerRestrictionsCriteria> {

	private Long containerId;

	private Set<String> spmnClasses;

	private Set<String> spmnTypes;

	private Set<CollectionProtocol> cps;

	private Site site;

	@Override
	public ContainerRestrictionsCriteria self() {
		return this;
	}

	public Long containerId() {
		return containerId;
	}

	public ContainerRestrictionsCriteria containerId(Long containerId) {
		this.containerId = containerId;
		return self();
	}

	public Set<String> specimenClasses() {
		return spmnClasses;
	}

	public ContainerRestrictionsCriteria specimenClasses(Set<String> spmnClasses) {
		this.spmnClasses = spmnClasses;
		return self();
	}

	public Set<String> specimenTypes() {
		return spmnTypes;
	}

	public ContainerRestrictionsCriteria specimenTypes(Set<String> spmnTypes) {
		this.spmnTypes = spmnTypes;
		return self();
	}

	public Set<CollectionProtocol> collectionProtocols() {
		return cps;
	}

	public Set<Long> collectionProtocolIds() {
		if (CollectionUtils.isEmpty(cps)) {
			return Collections.emptySet();
		}

		return cps.stream().map(cp -> cp.getId()).collect(Collectors.toSet());
	}

	public ContainerRestrictionsCriteria collectionProtocols(Set<CollectionProtocol> cps) {
		this.cps = cps;
		return self();
	}

	public Site site() {
		return site;
	}

	public Long siteId() {
		return site != null ? site.getId() : null;
	}

	public ContainerRestrictionsCriteria site(Site site) {
		this.site = site;
		return self();
	}
}
