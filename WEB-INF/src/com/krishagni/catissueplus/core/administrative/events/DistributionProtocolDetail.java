
package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.DpDistributionSite;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.de.events.ExtensionDetail;
import com.krishagni.catissueplus.core.de.events.SavedQuerySummary;

public class DistributionProtocolDetail extends DistributionProtocolSummary {

	private String instituteName;

	private String defReceivingSiteName;
	
	private String irbId;

	private String activityStatus;

	private SavedQuerySummary report;
	
	private Map<String, List<String>> distributingSites = new HashMap<String, List<String>>();

	private ExtensionDetail extensionDetail;

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
	
	public Map<String, List<String>> getDistributingSites() {
		return distributingSites;
	}
	
	public void setDistributingSites(Map<String, List<String>> distributingSites) {
		this.distributingSites = distributingSites;
	}

	public ExtensionDetail getExtensionDetail() {
		return extensionDetail;
	}

	public void setExtensionDetail(ExtensionDetail extensionDetail) {
		this.extensionDetail = extensionDetail;
	}

	public static DistributionProtocolDetail from(DistributionProtocol distributionProtocol) {
		DistributionProtocolDetail detail = new DistributionProtocolDetail();
		
		copy(distributionProtocol, detail);
		detail.setInstituteName(distributionProtocol.getInstitute().getName());
		if (distributionProtocol.getDefReceivingSite() != null) {
			detail.setDefReceivingSiteName(distributionProtocol.getDefReceivingSite().getName());
		}
		
		detail.setIrbId(distributionProtocol.getIrbId());
		detail.setPrincipalInvestigator(UserSummary.from(distributionProtocol.getPrincipalInvestigator()));
		detail.setActivityStatus(distributionProtocol.getActivityStatus());
		if (distributionProtocol.getReport() != null) {
			detail.setReport(SavedQuerySummary.fromSavedQuery(distributionProtocol.getReport()));
		}
		
		Set<DpDistributionSite> distSites = distributionProtocol.getDistributingSites();
		detail.setDistributingSites(DpDistributionSite.getInstituteSitesMap(distSites));
		detail.setExtensionDetail(ExtensionDetail.from(distributionProtocol.getExtension()));

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
