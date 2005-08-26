/*
 * Created on Aug 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;

/**
 * @hibernate.class table="CATISSUE_QUERY_INTERFACE_TABLE_DATA"
 * @author gautam_shetty
 */
public class QueryTableData implements Serializable
{

    private long identifier;

    private String tableName;

    private String displayName;

    /**
     * Display name of Table.
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

    /**
     * Returns the systemIdentifier.
     * @hibernate.id name="identifier" column="TABLE_ID" type="long" length="30"
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
     * Returns the name of this table.
     * @hibernate.property name="tableName" type="string" column="TABLE_NAME" length="50"
     * @return Returns the tableName.
     */
    public String getTableName()
    {
        return tableName;
    }

    /**
     * @param tableName The tableName to set.
     */
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }
}