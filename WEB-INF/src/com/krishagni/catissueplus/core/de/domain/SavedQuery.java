package com.krishagni.catissueplus.core.de.domain;

import java.util.Date;

import com.google.gson.Gson;
import com.krishagni.catissueplus.core.administrative.domain.User;

public class SavedQuery {

	private Long id;

	private String title;

	private User createdBy;

	private User lastUpdatedBy;

	private Date lastRunOn;

	private Date lastUpdated;

	private Long lastRunCount;
	
	private String drivingForm;

	private Filter[] filters;

	private QueryExpressionNode[] queryExpression;

	private String[] selectList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getLastRunOn() {
		return lastRunOn;
	}

	public void setLastRunOn(Date lastRunOn) {
		this.lastRunOn = lastRunOn;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(User lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Long getLastRunCount() {
		return lastRunCount;
	}

	public void setLastRunCount(Long lastRunCount) {
		this.lastRunCount = lastRunCount;
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

	public String getQueryDefJson() {
		SavedQuery query = new SavedQuery();
		query.selectList = selectList;
		query.filters = filters;
		query.queryExpression = queryExpression;
		query.drivingForm = drivingForm;
		return new Gson().toJson(query);
	}

	public void setQueryDefJson(String queryDefJson) {
		Gson gson = new Gson();
		SavedQuery query = gson.fromJson(queryDefJson, SavedQuery.class);		
		this.selectList = query.selectList;
		this.filters = query.filters;		
		this.queryExpression = query.queryExpression;
		this.drivingForm = query.drivingForm;
	}
	
	public String getAql() {
		return AqlBuilder.getInstance().getQuery(selectList, filters, queryExpression);
	}
	
	public void update(SavedQuery query) {
		setTitle(query.getTitle());
		setDrivingForm(query.getDrivingForm());
		setLastUpdatedBy(query.getLastUpdatedBy());
		setLastUpdated(query.getLastUpdated());
		setSelectList(query.getSelectList());
		setFilters(query.getFilters());
		setQueryExpression(query.getQueryExpression());
	}
}