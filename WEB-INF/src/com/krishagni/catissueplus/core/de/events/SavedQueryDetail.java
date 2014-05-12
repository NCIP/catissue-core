package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.de.domain.Filter;
import com.krishagni.catissueplus.core.de.domain.QueryExpressionNode;
import com.krishagni.catissueplus.core.de.domain.SavedQuery;

public class SavedQueryDetail extends SavedQuerySummary {
	private Long cpId;
	
	private String drivingForm;

	private Filter[] filters;

	private QueryExpressionNode[] queryExpression;

	private String[] selectList;

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

	public String[] getSelectList() {
		return selectList;
	}

	public void setSelectList(String[] selectList) {
		this.selectList = selectList;
	}
	
	public static SavedQueryDetail fromSavedQuery(SavedQuery savedQuery){
		SavedQueryDetail detail = new SavedQueryDetail();
		
		detail.setId(savedQuery.getId());
		detail.setTitle(savedQuery.getTitle());
		detail.setCpId(savedQuery.getCpId());
		detail.setDrivingForm(savedQuery.getDrivingForm());		
		detail.setCreatedBy( UserSummary.fromUser(savedQuery.getCreatedBy()));
		detail.setLastModifiedBy(UserSummary.fromUser(savedQuery.getLastUpdatedBy()));
		detail.setLastModifiedOn(savedQuery.getLastUpdated());
		detail.setLastRunCount(savedQuery.getLastRunCount());
		detail.setFilters(savedQuery.getFilters());
		detail.setQueryExpression(savedQuery.getQueryExpression());
		detail.setSelectList(savedQuery.getSelectList());
		
		return detail;
	}
}
