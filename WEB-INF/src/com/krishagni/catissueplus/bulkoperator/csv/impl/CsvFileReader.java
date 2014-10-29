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

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

import com.krishagni.catissueplus.bulkoperator.csv.CsvException;
import com.krishagni.catissueplus.bulkoperator.csv.CsvReader;

/**
 * 
 * @author Vinayak Pawar (vinayak.pawar@krishagni.com)
 *
 */
public class CsvFileReader implements CsvReader {
    private Map<String, Integer> columnNameIdxMap = new HashMap<String, Integer>();
    
    private String[] currentRow;
    
    private CSVReader csvReader;
    
    private boolean isFirstRowColumnNames;
    
    public CsvFileReader(CSVReader csvReader, boolean isFirstRowColumnNames) {
        this.csvReader = csvReader;
        this.isFirstRowColumnNames = isFirstRowColumnNames;
        if (isFirstRowColumnNames) {
            createColumnNameIdxMap();
        }
    }
    
    public static CsvFileReader createCsvFileReader(InputStream inputStream,boolean isFirstRowColumnNames) {
		CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream));
		return new CsvFileReader(csvReader, isFirstRowColumnNames);
    }
    
    public static CsvFileReader createCsvFileReader(String csvFile, boolean isFirstRowColumnNames) {
        try {
            CSVReader csvReader = new CSVReader(new FileReader(csvFile));
            return new CsvFileReader(csvReader, isFirstRowColumnNames);
        } catch (IOException e) {
            throw new CsvException("Error creating CSV file reader", e);
        }
    }
    
    public String[] getColumnNames() {
        String[] columnNames = new String[columnNameIdxMap.size()];
        for (Map.Entry<String, Integer> columnNameIdx : columnNameIdxMap.entrySet()) {
            columnNames[columnNameIdx.getValue()] = columnNameIdx.getKey();
        }

        return columnNames;
    }

	public String getColumn(String columnName) {
        if (!isFirstRowColumnNames) {
            throw new CsvException("CSV file reader created without first row column names");
        }
                
        Integer columnIdx = columnNameIdxMap.get(columnName.trim());
        if (columnIdx == null) {
        	return null;
        }

        return    getColumn(columnIdx);
    }

    public String getColumn(int columnIndex) {
        if (currentRow == null) {
            throw new CsvException("Programming error. Current row not initialised. Call next()");
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
                    throw new CsvException("CSV file column names line has empty/blank column names");
                }
                columnNameIdxMap.put(line[i].trim(), i);
            }
        } catch (IOException e) {
            throw new CsvException("Error reading CSV file column names line", e);    
        }
    }
}