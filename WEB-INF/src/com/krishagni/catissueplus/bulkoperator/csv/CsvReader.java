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
public interface CsvReader {
    public String[] getColumnNames();
    
    public String getColumn(String columnName);
    
    public String getColumn(int columnIndex);
    
    public String[] getRow();
    
    public boolean next();
    
    public void close();
}
