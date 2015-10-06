
package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.de.events.SavedQuerySummary;

public class DistributionProtocolDetail extends DistributionProtocolSummary {

	private String instituteName;

	private String defReceivingSiteName;
	
	private String irbId;

	private String activityStatus;

	private SavedQuerySummary report;
	
	private List<SiteDetail> distributingSites = new ArrayList<SiteDetail>();

	public String getInstituteName() {
		return instituteName;
	}

	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
	}
	
	public String getDefReceivingSiteName() {
		return defReceivingSiteName;
	}
	
	public void setDefReceivingSiteName(String defReceivingSiteName) {
		this.defReceivingSiteName = defReceivingSiteName;
	}

	public String getIrbId() {
		return irbId;
	}

	public void setIrbId(String irbId) {
		this.irbId = irbId;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public SavedQuerySummary getReport() {
		return report;
	}

	public void setReport(SavedQuerySummary report) {
		this.report = report;
	}
	
	public List<SiteDetail> getDistributingSites() {
		return distributingSites;
	}
	
	public void setDistributingSites(List<SiteDetail> distributingSites) {
		this.distributingSites = distributingSites;
	}

	public static DistributionProtocolDetail from(DistributionProtocol distributionProtocol) {
		DistributionProtocolDetail detail = new DistributionProtocolDetail();
		
		copy(distributionProtocol, detail);
		detail.setInstituteName(distributionProtocol.getInstitute().getName());
		if (distributionProtocol.getDefReceivingSite() != null) {
			detail.setDefReceivingSiteName(distributionProtocol.getDefReceivingSite().getName());
		}
		
		detail.setIrbId(distributionProtocol.getIrbId());
		detail.setActivityStatus(distributionProtocol.getActivityStatus());
		if (distributionProtocol.getReport() != null) {
			detail.setReport(SavedQuerySummary.fromSavedQuery(distributionProtocol.getReport()));
		}
		
		detail.setDistributingSites(SiteDetail.from(distributionProtocol.getDistributingSites()));
		
		return detail;
	}

	public static List<DistributionProtocolDetail> from(List<DistributionProtocol> distributionProtocols) {
		List<DistributionProtocolDetail> list = new ArrayList<DistributionProtocolDetail>();
		
		for (DistributionProtocol dp : distributionProtocols) {
			list.add(from(dp));
		}
		
		return list;
	}
}
