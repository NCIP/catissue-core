package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.common.util.Utility;

public class DistributionOrderStat {
	private Long id;
	
	private String name;
	
	private DistributionProtocolSummary distributionProtocol;

	private Date executionDate;

	private Map<String, Object> groupByAttrVals = new HashMap<String, Object>();
	
	private Long distributedSpecimenCount;
	
	private static final Map<String, String> attrDisplayVals = new HashMap<String, String>();
	static {
		attrDisplayVals.put("specimenType", "dist_specimen_type");
		attrDisplayVals.put("anatomicSite", "dist_anatomic_site");
		attrDisplayVals.put("pathologyStatus", "dist_pathology_status");
	};

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

	public static Map<String, String> getAttrDisplayVals() {
		return attrDisplayVals;
	}
	
	public String [] getOrderStatsReportData(DistributionOrderStatListCriteria crit) {
		List<String> data = new ArrayList<String>();
		if (crit.dpId() == null) {
			data.add(getDistributionProtocol().getShortTitle());
		}
		
		data.add(getName());
		data.add(Utility.getDateString(getExecutionDate()));
		for (String attr: crit.groupByAttrs()) {
			data.add(getGroupByAttrVals().get(attr).toString());
		}
		
		data.add(getDistributedSpecimenCount().toString());
		
		return data.toArray(new String[0]);
	}
	
}
