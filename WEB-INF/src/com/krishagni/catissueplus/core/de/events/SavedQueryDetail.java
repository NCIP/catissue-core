package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.de.domain.Filter;
import com.krishagni.catissueplus.core.de.domain.QueryExpressionNode;
import com.krishagni.catissueplus.core.de.domain.ReportSpec;
import com.krishagni.catissueplus.core.de.domain.SavedQuery;

public class SavedQueryDetail extends SavedQuerySummary {
	private Long cpId;
	
	private String drivingForm;

	private Filter[] filters;

	private QueryExpressionNode[] queryExpression;

	private Object[] selectList;
	
	private ReportSpec reporting;
	
	private String wideRowMode;

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public String getDrivingForm() {
		return drivingForm;
	}

	public void setDrivingForm(String drivingForm) {
		this.drivingForm = drivingForm;
	}

	public Filter[] getFilters() {
		return filters;
	}

	public void setFilters(Filter[] filters) {
		this.filters = filters;
	}

	public QueryExpressionNode[] getQueryExpression() {
		return queryExpression;
	}

	public void setQueryExpression(QueryExpressionNode[] queryExpression) {
		this.queryExpression = queryExpression;
	}

	public Object[] getSelectList() {
		return selectList;
	}

	public void setSelectList(Object[] selectList) {
		this.selectList = selectList;
	}
	
	public ReportSpec getReporting() {
		return reporting;
	}

	public void setReporting(ReportSpec reporting) {
		this.reporting = reporting;
	}

	public String getWideRowMode() {
		return wideRowMode;
	}

	public void setWideRowMode(String wideRowMode) {
		this.wideRowMode = wideRowMode;
	}

	public static SavedQueryDetail fromSavedQuery(SavedQuery savedQuery){
		SavedQueryDetail detail = new SavedQueryDetail();
		
		detail.setId(savedQuery.getId());
		detail.setTitle(savedQuery.getTitle());
		detail.setCpId(savedQuery.getCpId());
		detail.setDrivingForm(savedQuery.getDrivingForm());		
		detail.setCreatedBy(UserSummary.from(savedQuery.getCreatedBy()));
		detail.setLastModifiedBy(UserSummary.from(savedQuery.getLastUpdatedBy()));
		detail.setLastModifiedOn(savedQuery.getLastUpdated());
		detail.setFilters(savedQuery.getFilters());
		detail.setQueryExpression(savedQuery.getQueryExpression());
		detail.setSelectList(savedQuery.getSelectList());
		detail.setReporting(savedQuery.getReporting());
		detail.setWideRowMode(savedQuery.getWideRowMode());
		return detail;
	}	
}
