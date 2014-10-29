/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package com.krishagni.catissueplus.bulkoperator.csv.impl;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVWriter;

import com.krishagni.catissueplus.bulkoperator.csv.CsvException;
import com.krishagni.catissueplus.bulkoperator.csv.CsvWriter;

/**
 * 
 * @author Vinayak Pawar (vinayak.pawar@krishagni.com)
 *
 */
public class CsvFileWriter implements CsvWriter {
    private List<String[]> rows;
    
    private int batchSize = 1;
    
    private Map<String, Integer> columnNameIdxMap = new HashMap<String, Integer>();
    
    private CSVWriter csvWriter;
    
    private int numColumns;
    
    private String[] currentRow;
    
    private boolean isFirstRowColumnNames;
    
    public CsvFileWriter(CSVWriter csvWriter, String[] columnNames, int batchSize) {
        this.csvWriter = csvWriter;
        this.batchSize = batchSize;
        this.rows = new ArrayList<String[]>(batchSize);                
        createColumnNameIdxMap(columnNames);
        this.numColumns = columnNameIdxMap.size();
        rows.add(getColumnNames());
        this.isFirstRowColumnNames = true;
    }
    
    public CsvFileWriter(CSVWriter csvWriter, int numColumns, int batchSize) {
        this.csvWriter = csvWriter;
        this.batchSize = batchSize;
        this.rows = new ArrayList<String[]>(batchSize);
        this.numColumns = numColumns;
        this.isFirstRowColumnNames = false;
    }
    
    public static CsvFileWriter createCsvFileWriter(String outFile, String[] columnNames, int batchSize) {
        try {
            CSVWriter csvWriter = new CSVWriter(new FileWriter(outFile));
            return new CsvFileWriter(csvWriter, columnNames, batchSize);
        } catch (IOException e) {
            throw new CsvException("Error creating CSVWriter", e);
        }         
    }
    
    public static CsvFileWriter createCsvFileWriter(String outFile, int numColumns, int batchSize) {
        try {
            CSVWriter csvWriter = new CSVWriter(new FileWriter(outFile));
            return new CsvFileWriter(csvWriter, numColumns, batchSize);
        } catch (IOException e) {
            throw new CsvException("Error creating CSVWriter", e);
        }
    }

    public String[] getColumnNames() {
        String[] columnNames = new String[columnNameIdxMap.size()];
        for (Map.Entry<String, Integer> columnNameIdx : columnNameIdxMap.entrySet()) {
            columnNames[columnNameIdx.getValue()] = columnNameIdx.getKey();
        }

        return columnNames;
    }
    
    public void setColumnValue(String columnName, String columnValue) {
        if (!isFirstRowColumnNames) {
            throw new CsvException("CSV file writer created without first row column names");
        }
                
        Integer columnIdx = columnNameIdxMap.get(columnName.trim());        
        if (columnIdx == null) {
            throw new CsvException("Invalid column name: " + columnName);
        }
        
        setColumnValue(columnIdx, columnValue);
    }

    public void setColumnValue(int columnIdx, String columnValue) {
        if (currentRow == null) {
            throw new CsvException("Programming error. Current row not initialized");
        }
        
        if (columnIdx < 0 || columnIdx >= currentRow.length) {
            throw new CsvException("Invalid column index: " + columnIdx);
        }
        
        currentRow[columnIdx] = columnValue;        
    }

    public void flush() {
        try {
            csvWriter.writeAll(rows);
            csvWriter.flush();
            rows.clear();
        } catch (IOException e) {
            throw new CsvException("Error writing to CSV file", e);
        }
    }
    
     public void nextRow() {
        if (rows.size() >= batchSize) {
            flush();
        }
        currentRow = createRow();
        rows.add(currentRow);
    }
    public int getNumberOfRows() {
    	return rows.size();
    }
    public void close() {
        try {
            flush();
            csvWriter.close();
        } catch (IOException e) {
            throw new CsvException("Error closing CSVWriter", e);
        }

    }
    
    private void createColumnNameIdxMap(String[] columnNames) {
        if (columnNames == null || columnNames.length == 0) {
            throw new CsvException("Empty column names");
        }
        
        for (int i = 0; i < columnNames.length; ++i) {
            if (columnNames[i] == null) {
                throw new CsvException("Empty column name");
            }
            
            String columnName = columnNames[i].trim();
            if (columnName.length() == 0) {
                throw new CsvException("Empty column name");
            }
            
            if (columnNameIdxMap.containsKey(columnName)) {
                throw new CsvException("Duplicate column name: " + columnName);
            }
            
            columnNameIdxMap.put(columnName, i);
        }        
    }
    
    private String[] createRow() {
        String[] row = new String[numColumns];
        for (int i = 0; i < row.length; ++i) {
            row[i] = "";
        }
        
        return row;
    }
}