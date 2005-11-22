/*
 * Created on Aug 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.exception.AssignDataException;

/**
 * @author gautam_shetty
 * @hibernate.class table="CATISSUE_QUERY_INTERFACE_COLUMN_DATA"
 */
public class QueryColumnData extends AbstractDomainObject
        implements Serializable
{

	/**
     * System Identifier
     */
	private Long systemIdentifier;

	/**
     * Table of this column
     */
	private QueryTableData tableData = new QueryTableData();

    /**
     * Name of this column
     */
    private String columnName;

    /**
     * Datatype of this column
     */
    private String columnType;

    /**
     * Returns the systemIdentifier.
     * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @return Returns the identifier.
     */
    public Long getSystemIdentifier()
    {
        return systemIdentifier;
    }

    /**
     * @param systemIdentifier The identifier to set.
     */
    public void setSystemIdentifier(Long systemIdentifier)
    {
        this.systemIdentifier = systemIdentifier;
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
     * @param columnName The columnName to set.
     */
    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }
    
    /**
     * @hibernate.property name="columnType" type="string" column="ATTRIBUTE_TYPE" length="30"
     * @return Returns the columnType.
     */
    public String getColumnType()
    {
        return columnType;
    }

    /**
     * @param columnType The columnType to set.
     */
    public void setColumnType(String columnType)
    {
        this.columnType = columnType;
    }

    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm abstractForm)
            throws AssignDataException
    {
        // TODO Auto-generated method stub

    }
}