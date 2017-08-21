package com.krishagni.catissueplus.core.de.events;

import java.util.List;

public class QueryExecResult {
	private String[] columnLabels;

	private String[] columnUrls;
	
	private List<String[]> rows;
	
	private Integer[] columnIndices;
	
	private int dbRowsCount;

	public String[] getColumnLabels() {
		return columnLabels;
	}

	public QueryExecResult setColumnLabels(String[] columnLabels) {
		this.columnLabels = columnLabels;
		return this;
	}

	public String[] getColumnUrls() {
		return columnUrls;
	}

	public QueryExecResult setColumnUrls(String[] columnUrls) {
		this.columnUrls = columnUrls;
		return this;
	}

	public List<String[]> getRows() {
		return rows;
	}

	public QueryExecResult setRows(List<String[]> rows) {
		this.rows = rows;
		return this;
	}
	
	public Integer[] getColumnIndices() {
		return columnIndices;
	}

	public QueryExecResult setColumnIndices(Integer[] columnIndices) {
		this.columnIndices = columnIndices;
		return this;
	}

	public int getDbRowsCount() {
		return dbRowsCount;
	}

	public QueryExecResult setDbRowsCount(int dbRowsCount) {
		this.dbRowsCount = dbRowsCount;
		return this;
	}

	public static QueryExecResult create(String[] labels, List<String[]> rows, int dbRowsCount, Integer[] indices) {
		QueryExecResult resp = new QueryExecResult();
		resp.setColumnLabels(labels);
		resp.setRows(rows);
		resp.setDbRowsCount(dbRowsCount);
		resp.setColumnIndices(indices);	
		return resp;
	}
	
}