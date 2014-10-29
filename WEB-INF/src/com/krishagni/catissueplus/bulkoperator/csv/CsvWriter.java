/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package com.krishagni.catissueplus.bulkoperator.csv;

/**
 * 
 * @author Vinayak Pawar (vinayak.pawar@krishagni.com)
 *
 */
public interface CsvWriter {
    public String[] getColumnNames();
    
    public void setColumnValue(String columnName, String columnValue);
    
    public void setColumnValue(int columnIdx, String columnValue);
    
    public void flush();
    
    public void nextRow();
    
    public void close();
}
