package com.krishagni.catissueplus.core.de.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hibernate.envers.NotAudited;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krishagni.catissueplus.core.administrative.domain.User;

public class SavedQuery {
	private Long id;

	private String title;

	private User createdBy;

	private User lastUpdatedBy;

	private Date lastRunOn;

	private Date lastUpdated;

	private Long lastRunCount;
	
	private Long cpId;
	
	private String drivingForm;

	private Filter[] filters;

	private QueryExpressionNode[] queryExpression;

	private Object[] selectList;
	
	private ReportSpec reporting;
	
	private Set<QueryFolder> folders = new HashSet<QueryFolder>();
	
	private String wideRowMode = "DEEP";

	private Date deletedOn;

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

	@NotAudited
	public Filter[] getFilters() {
		return filters;
	}

	public void setFilters(Filter[] filters) {
		this.filters = filters;
	}

	@NotAudited
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
	
	@NotAudited
	public ReportSpec getReporting() {
		return reporting;
	}

	public void setReporting(ReportSpec reporting) {
		this.reporting = reporting;
	}

	@NotAudited
	public Set<QueryFolder> getFolders() {
		return folders;
	}

	public void setFolders(Set<QueryFolder> folders) {
		this.folders = folders;
	}

	public String getWideRowMode() {
		return wideRowMode;
	}

	public void setWideRowMode(String wideRowMode) {
		this.wideRowMode = wideRowMode;
	}

	public Date getDeletedOn() {
		return deletedOn;
	}

	public void setDeletedOn(Date deletedOn) {
		this.deletedOn = deletedOn;
	}

	public String getQueryDefJson() {
		return getQueryDefJson(false);
	}
	
	public String getQueryDefJson(boolean includeTitle) {
		SavedQuery query = new SavedQuery();
		
		if (includeTitle) {
			query.title = title;
		}
		
		query.cpId = cpId;
		query.selectList = selectList;
		query.filters = filters;
		query.queryExpression = queryExpression;
		query.drivingForm = drivingForm;
		query.folders = null;
		query.reporting = reporting;
		query.wideRowMode = wideRowMode;
		
		try {
			return getWriteMapper().writeValueAsString(query);
		} catch (Exception e) {
			throw new RuntimeException("Error marshalling saved query to JSON", e);
		}				
	}

	public void setQueryDefJson(String queryDefJson) {
		setQueryDefJson(queryDefJson, false);
	}
	
	public void setQueryDefJson(String queryDefJson, boolean includeTitle) {
		SavedQuery query = null;
		try {
			query = getReadMapper().readValue(queryDefJson, SavedQuery.class);
		} catch (Exception e) {
			throw new RuntimeException("Error marshalling JSON to saved query", e);
		}
		if(includeTitle){
			this.title = query.title;
		}
		this.cpId = query.cpId;
		this.selectList = query.selectList;
		this.filters = query.filters;
		this.queryExpression = query.queryExpression;
		this.drivingForm = query.drivingForm;
		this.reporting = query.reporting;
		this.wideRowMode = query.wideRowMode;
	}
	
	public String getAql() {
		return AqlBuilder.getInstance().getQuery(selectList, filters, queryExpression);
	}
	
	public String getAql(Filter[] conjunctionFilters) {
		return AqlBuilder.getInstance().getQuery(selectList, filters, conjunctionFilters, queryExpression);
	}
	
	public void update(SavedQuery query) {
		setTitle(query.getTitle());
		setCpId(query.getCpId());
		setDrivingForm(query.getDrivingForm());
		setLastUpdatedBy(query.getLastUpdatedBy());
		setLastUpdated(query.getLastUpdated());
		setSelectList(query.getSelectList());
		setFilters(query.getFilters());		
		setQueryExpression(query.getQueryExpression());
		setReporting(query.getReporting());
		setWideRowMode(query.getWideRowMode());
	}
	
	@Override
	public int hashCode() {
		return 31 * 1 + ((id == null) ? 0 : id.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		SavedQuery other = (SavedQuery) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	public SavedQuery copy() {
		SavedQuery copy = new SavedQuery();
		copy.setQueryDefJson(getQueryDefJson(true), true);
		copy.selectList = curateSelectList(copy.selectList);
		return copy;
	}
		
	private ObjectMapper getReadMapper() {
		return new ObjectMapper();
	}
	
	private ObjectMapper getWriteMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibilityChecker(
			mapper.getSerializationConfig().getDefaultVisibilityChecker()
				.withFieldVisibility(Visibility.ANY)
				.withGetterVisibility(Visibility.NONE)
				.withSetterVisibility(Visibility.NONE)
				.withCreatorVisibility(Visibility.NONE));
		return mapper;		
	}

	private Object[] curateSelectList(Object[] selectList) {
		Object[] result = new Object[selectList.length];
		int idx = 0;
		for (Object field : selectList) {
			if (field instanceof Map) {
				field = new ObjectMapper().convertValue(field, SelectField.class);
			}

			result[idx++] = field;
		}

		return result;
	}
}