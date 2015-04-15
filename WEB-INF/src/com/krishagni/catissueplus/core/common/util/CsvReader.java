package com.krishagni.catissueplus.core.common.util;

public interface CsvReader {
	public String[] getColumnNames();

	public boolean isColumnPresent(String columnName);

	public String getColumn(String columnName);

	public String getColumn(int columnIndex);

	public String[] getRow();

	public boolean next();

	public void close();
}
