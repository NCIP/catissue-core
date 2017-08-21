package com.krishagni.catissueplus.core.administrative.events;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DistributionOrderStat {
	private Long id;
	
	private String name;
	
	private DistributionProtocolSummary distributionProtocol;

	private Date executionDate;

	private Map<String, Object> groupByAttrVals = new HashMap<String, Object>();
	
	private Long distributedSpecimenCount;
	
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

	public DistributionProtocolSummary getDistributionProtocol() {
		return distributionProtocol;
	}

	public void setDistributionProtocol(DistributionProtocolSummary distributionProtocol) {
		this.distributionProtocol = distributionProtocol;
	}

	public Date getExecutionDate() {
		return executionDate;
	}

	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
	}

	public Map<String, Object> getGroupByAttrVals() {
		return groupByAttrVals;
	}
	
	public void setGroupByAttrVals(Map<String, Object> groupByAttrVals) {
		this.groupByAttrVals = groupByAttrVals;
	}
	
	public Long getDistributedSpecimenCount() {
		return distributedSpecimenCount;
	}

	public void setDistributedSpecimenCount(Long distributedSpecimenCount) {
		this.distributedSpecimenCount = distributedSpecimenCount;
	}

}
