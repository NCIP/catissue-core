/*
 * Created on Aug 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.exception.AssignDataException;

/**
 * @hibernate.class table="CATISSUE_QUERY_INTERFACE_TABLE_DATA"
 * @author gautam_shetty
 */
public class QueryTableData extends AbstractDomainObject
        implements Serializable
{

    /**
     * System Identifier
     */
	private Long systemIdentifier;

	/**
     * Name of the table
     */
	private String tableName;

	/**
     * Display name of the table
     */
	private String displayName;

	/**
     * Alias name of the table
     */
	private String aliasName;
	
	/**
     * A collection of all the columns of a table
     */
	private Collection columnDataCollection = new HashSet();

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

    /**
     * @hibernate.property name="aliasName" type="string" column="ALIAS_NAME" length="50"
     * @return Returns the aliasName.
     */
    public String getAliasName()
    {
        return aliasName;
    }

    /**
     * @param aliasName The aliasName to set.
     */
    public void setAliasName(String aliasName)
    {
        this.aliasName = aliasName;
    }
    
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm abstractForm)
            throws AssignDataException
    {
        // TODO Auto-generated method stub

    }

    /**
     * Returns the system identifier.
     * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @return Returns the system identifier.
     */
    public Long getSystemIdentifier()
    {
        return this.systemIdentifier;
    }

    /**
     * @param systemIdentifier The System Identifier.
     */
    public void setSystemIdentifier(Long systemIdentifier)
    {
    	this.systemIdentifier = systemIdentifier;
    }
    
    /**
	 * Returns collection of all the columns of this table.
	 * @return collection of all the columns of this table.
	 * @hibernate.set name="columnDataCollection" table="CATISSUE_QUERY_INTERFACE_COLUMN_DATA"
	 * cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="TABLE_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.QueryColumnData"
	 * @see setColumnDataCollection(Collection)
	 */
    public Collection getColumnDataCollection()
	{
		return columnDataCollection;
	}
    
    /**
     * @param columnDataCollection The collection of all the columns of this table.
     */
	public void setColumnDataCollection(Collection columnDataCollection)
	{
		this.columnDataCollection = columnDataCollection;
	}
}