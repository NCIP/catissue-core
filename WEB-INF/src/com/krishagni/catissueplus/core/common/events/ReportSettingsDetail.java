package com.krishagni.catissueplus.core.common.events;

import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.common.domain.ReportSettings;
import com.krishagni.catissueplus.core.de.events.SavedQuerySummary;

public class ReportSettingsDetail {
	private Long id;

	private boolean enabled = true;

	private SavedQuerySummary dataQuery;

	private Map<String, Object> dataCfg;

	private Map<String, Map<String, Object>> metricsCfg;

	private String emailTmpl;

	private List<UserSummary> recipients;

	private String activityStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public SavedQuerySummary getDataQuery() {
		return dataQuery;
	}

	public void setDataQuery(SavedQuerySummary dataQuery) {
		this.dataQuery = dataQuery;
	}

	public Map<String, Object> getDataCfg() {
		return dataCfg;
	}

	public void setDataCfg(Map<String, Object> dataCfg) {
		this.dataCfg = dataCfg;
	}

	public Map<String, Map<String, Object>> getMetricsCfg() {
		return metricsCfg;
	}

	public void setMetricsCfg(Map<String, Map<String, Object>> metricsCfg) {
		this.metricsCfg = metricsCfg;
	}

	public String getEmailTmpl() {
		return emailTmpl;
	}

	public void setEmailTmpl(String emailTmpl) {
		this.emailTmpl = emailTmpl;
	}

	public List<UserSummary> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<UserSummary> recipients) {
		this.recipients = recipients;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public static void fromTo(ReportSettings rptSettings, ReportSettingsDetail detail) {
		detail.setId(rptSettings.getId());
		detail.setEnabled(rptSettings.isEnabled());
		detail.setDataQuery(SavedQuerySummary.fromSavedQuery(rptSettings.getDataQuery()));
		detail.setDataCfg(rptSettings.getDataCfg());
		detail.setMetricsCfg(rptSettings.getMetricsCfg());
		detail.setEmailTmpl(rptSettings.getEmailTmpl());
		detail.setRecipients(UserSummary.from(rptSettings.getRecipients()));
		detail.setActivityStatus(rptSettings.getActivityStatus());
	}

	public static ReportSettingsDetail from(ReportSettings rptSettings) {
		ReportSettingsDetail detail = new ReportSettingsDetail();
		fromTo(rptSettings, detail);
		return detail;
	}
}