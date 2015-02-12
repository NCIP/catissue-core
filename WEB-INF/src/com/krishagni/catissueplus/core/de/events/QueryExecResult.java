package com.krishagni.catissueplus.core.de.events;

import java.util.List;

public class QueryExecResult {
	private String[] columnLabels;
	
	private List<String[]> rows;
	
	private Integer[] columnIndices;
	
	private int dbRowsCount;

	public String[] getColumnLabels() {
		return columnLabels;
	}

	public void setColumnLabels(String[] columnLabels) {
		this.columnLabels = columnLabels;
	}

	public List<String[]> getRows() {
		return rows;
	}

	public void setRows(List<String[]> rows) {
		this.rows = rows;
	}
	
	public Integer[] getColumnIndices() {
		return columnIndices;
	}

	public void setColumnIndices(Integer[] columnIndices) {
		this.columnIndices = columnIndices;
	}

	public int getDbRowsCount() {
		return dbRowsCount;
	}

	public void setDbRowsCount(int dbRowsCount) {
		this.dbRowsCount = dbRowsCount;
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