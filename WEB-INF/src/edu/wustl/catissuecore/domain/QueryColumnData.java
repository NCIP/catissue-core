/*
 * Created on Aug 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;

/**
 * @author gautam_shetty
 * @hibernate.class table="CATISSUE_QUERY_INTERFACE_COLUMN_DATA"
 */
public class QueryColumnData implements Serializable
{

    private long identifier;

    private QueryTableData tableData = new QueryTableData();

    private String columnName;

    private String displayName;
    
    /**
     * Returns the systemIdentifier.
     * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @return Returns the identifier.
     */
    public long getIdentifier()
    {
        return identifier;
    }

    /**
     * @param identifier The identifier to set.
     */
    public void setIdentifier(long identifier)
    {
        this.identifier = identifier;
    }

    /**
     * @hibernate.many-to-one column="TABLE_ID" class="edu.wustl.catissuecore.domain.QueryTableData"
     * constrained="true"
     * @return Returns the tableData.
     */
    public QueryTableData getTableData()
    {
        return tableData;
    }
    
    /**
     * @param tableData The tableData to set.
     */
    public void setTableData(QueryTableData tableData)
    {
        this.tableData = tableData;
    }
    
    /**
     * @hibernate.property name="columnName" type="string" column="COLUMN_NAME" length="50"
     * @return Returns the tableName.
     */
    public String getColumnName()
    {
        return columnName;
    }

    /**
     * @param tableName The tableName to set.
     */
    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }
    
    /**
     * @hibernate.property name="displayName" type="string" column="DISPLAY_NAME" length="50"
     * @return Returns the displayName.
     */
    public String getDisplayName()
    {
        return displayName;
    }

    /**
     * @param displayName The displayName to set.
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }
}