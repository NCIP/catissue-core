package com.krishagni.catissueplus.core.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Column {
	private String expr;

	private boolean temporal;

	private String caption;

	//
	// Used in order by
	//
	private String direction;

	//
	// Mostly used in filters
	//
	private String dataType;

	private String searchType;

	//
	// Dual purpose - to show filter dropdown values and search values
	//
	private List<Object> values;

	private Map<String, String> metainfo = new HashMap<>();

	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}

	public boolean isTemporal() {
		return temporal;
	}

	public void setTemporal(boolean temporal) {
		this.temporal = temporal;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}

	public Map<String, String> getMetainfo() {
		return metainfo;
	}

	public void setMetainfo(Map<String, String> metainfo) {
		this.metainfo = metainfo;
	}
}
