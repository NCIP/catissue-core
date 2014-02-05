package krishagni.catissueplus.dto;

import java.util.List;

public class QueryExecResp {
	private String[] columnLabels;
	
	private List<Object[]> rows;

	public String[] getColumnLabels() {
		return columnLabels;
	}

	public void setColumnLabels(String[] columnLabels) {
		this.columnLabels = columnLabels;
	}

	public List<Object[]> getRows() {
		return rows;
	}

	public void setRows(List<Object[]> rows) {
		this.rows = rows;
	}
}
