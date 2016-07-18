package com.krishagni.catissueplus.core.query;

import java.util.ArrayList;
import java.util.List;

public class ListDetail {
	private List<Column> columns = new ArrayList<>();

	private List<Row> rows = new ArrayList<>();

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public List<Row> getRows() {
		return rows;
	}

	public void setRows(List<Row> rows) {
		this.rows = rows;
	}
}
