package com.krishagni.catissueplus.core.common.util;

import java.io.Closeable;

public interface CsvReader extends Closeable {
	public String[] getColumnNames();

	public boolean isColumnPresent(String columnName);

	public String getColumn(String columnName);

	public String getColumn(int columnIndex);

	public String[] getRow();

	public boolean next();
}
