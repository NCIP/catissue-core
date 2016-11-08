package com.krishagni.catissueplus.core.common.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import au.com.bytecode.opencsv.CSVReader;

public class CsvFileReader implements CsvReader {
	private Map<String, Integer> columnNameIdxMap = new HashMap<String, Integer>();

	private String[] currentRow;

	private CSVReader csvReader;

	private boolean firstRowHeaderRow;

	public CsvFileReader(CSVReader csvReader, boolean firstRowHeaderRow) {
		this.csvReader = csvReader;
		this.firstRowHeaderRow = firstRowHeaderRow;
		if (firstRowHeaderRow) {
			createColumnNameIdxMap();
		}
	}

	public static CsvFileReader createCsvFileReader(InputStream inputStream, boolean firstRowHeaderRow) {
		CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream), Utility.getFieldSeparator());
		return new CsvFileReader(csvReader, firstRowHeaderRow);
	}
	
	public static CsvFileReader createCsvFileReader(String csvFile, boolean firstRowHeaderRow) {
		try {
			BufferedInputStream bin = new BufferedInputStream(new FileInputStream(csvFile));
			InputStreamReader in = new InputStreamReader(bin, Utility.detectFileCharset(bin));
			CSVReader csvReader = new CSVReader(in, Utility.getFieldSeparator());
			return new CsvFileReader(csvReader, firstRowHeaderRow);
		} catch (IOException e) {
			throw new CsvException("Error creating CSV file reader", e);
		}
	}

	public static int getRowsCount(String csvFile, boolean firstRowHeaderRow) {
		CsvFileReader reader = null;
		try {
			reader = createCsvFileReader(csvFile, firstRowHeaderRow);

			int count = 0;
			while (reader.next()) {
				count++;
			}
			return count;
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

	public String[] getColumnNames() {
		String[] columnNames = new String[columnNameIdxMap.size()];
		for (Map.Entry<String, Integer> columnNameIdx : columnNameIdxMap.entrySet()) {
			columnNames[columnNameIdx.getValue()] = columnNameIdx.getKey();
		}

		return columnNames;
	}

	public boolean isColumnPresent(String columnName) {
		return getColumnIdx(columnName) >= 0;
	}

	public String getColumn(String columnName) {
		int columnIdx = getColumnIdx(columnName);
		if (columnIdx == -1) {
			return null;
		}

		return getColumn(columnIdx);
	}

	public String getColumn(int columnIndex) {
		if (currentRow == null) {
			throw new CsvException(
					"Programming error. Current row not initialised. Call next()");
		}

		if (columnIndex < 0 || columnIndex >= currentRow.length) {
			throw new CsvException("Invalid column index: " + columnIndex);
		}

		return currentRow[columnIndex];
	}

	public String[] getRow() {
		return currentRow;
	}

	public boolean next() {
		try {
			currentRow = csvReader.readNext();
		} catch (IOException e) {
			throw new CsvException("Error reading line from CSV file", e);
		}

		return (currentRow != null && currentRow.length > 0);
	}

	public void close() {
		try {
			csvReader.close();
		} catch (IOException e) {
			throw new CsvException("Error closing CSVReader", e);
		}
	}

	private void createColumnNameIdxMap() {
		try {
			String[] line = csvReader.readNext();
			if (line == null || line.length == 0) {
				throw new CsvException("CSV file column names line empty");
			}

			for (int i = 0; i < line.length; ++i) {
				if (line[i] == null || line[i].trim().length() == 0) {
					throw new CsvException(
							"CSV file column names line has empty/blank column names", line);
				}
				columnNameIdxMap.put(line[i].trim(), i);
			}
		} catch (IOException e) {
			throw new CsvException("Error reading CSV file column names line", e);
		}
	}

	private int getColumnIdx(String columnName) {
		if (!firstRowHeaderRow) {
			throw new CsvException(
					"CSV file reader created without first row column names");
		}

		Integer columnIdx = columnNameIdxMap.get(columnName.trim());
		return columnIdx == null ? -1 : columnIdx;
	}
}
