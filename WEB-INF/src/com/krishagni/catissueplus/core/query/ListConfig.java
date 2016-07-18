package com.krishagni.catissueplus.core.query;

import java.util.ArrayList;
import java.util.List;

public class ListConfig {
	private Long cpId;

	private String drivingForm;

	private List<Column> columns = new ArrayList<>();

	private String criteria;

	private String restriction;

	private List<Column> orderBy = new ArrayList<>();

	private List<Column> filters = new ArrayList<>();

	private List<Column> hiddenColumns = new ArrayList<>();

	private int startAt = 0;

	private int maxResults = 100;

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

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getRestriction() {
		return restriction;
	}

	public void setRestriction(String restriction) {
		this.restriction = restriction;
	}

	public List<Column> getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(List<Column> orderBy) {
		this.orderBy = orderBy;
	}

	public List<Column> getFilters() {
		return filters;
	}

	public void setFilters(List<Column> filters) {
		this.filters = filters;
	}

	public List<Column> getHiddenColumns() {
		return hiddenColumns;
	}

	public void setHiddenColumns(List<Column> hiddenColumns) {
		this.hiddenColumns = hiddenColumns;
	}

	public int getStartAt() {
		return startAt;
	}

	public void setStartAt(int startAt) {
		this.startAt = startAt;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}
}
