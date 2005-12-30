/**
 *<p>Title: DataElement</p>
 *<p>Description:  Represents data element of a query</p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */ 
package edu.wustl.catissuecore.query;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;


/**
 * @author aarti_sharma
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class DataElement
{
    /**
     * Table/object name
     */
    private Table table;
    
    /**
     * Field name
     */
    private String field;
    
    /*
     * 
     */
    private String fieldType;
            
    
    public DataElement()
    {
    	this.table = new Table();
    }
    
    /**
     * Constructor
     * @param table Table/object name
     * @param field Field name
     */
    public DataElement(String table, String field)
    {
        this.table = new Table(table,table);
        this.field = field;
        this.fieldType = Constants.FIELD_TYPE_TEXT;
    }
    
    public DataElement(String table, String field, String fieldType)
    {
        this.table = new Table(table,table);
        this.field = field;
        this.fieldType = fieldType;
    }
    
    public DataElement(Table table, String field)
    {
        this.table = table;
        this.field = field;
        this.fieldType = Constants.FIELD_TYPE_TEXT;
    }
    
    public DataElement(Table table, String field, String fieldType)
    {
        this.table = table;
        this.field = field;
        this.fieldType = fieldType;
    }
    
    /**
	 * @param leftDataElement
	 */
	public DataElement(DataElement leftDataElement) {
		this.table = new Table(leftDataElement.getTable());
        this.field = leftDataElement.field;
        this.fieldType = leftDataElement.fieldType;
	}

	/**
     * SQL string representation
     * @param tableSufix sufix for table name
     * @return SQL string representation
     */
    public String toSQLString(int tableSufix)
    {
       String fieldName = table.toSQLString() + tableSufix + "." + field+" ";
       if ((fieldType != null) && (Constants.FIELD_TYPE_TIMESTAMP_TIME.equalsIgnoreCase(fieldType)))
		{
            fieldName = Variables.TIME_FORMAT_FUNCTION + "(" + fieldName + ",'" + Variables.TIME_PATTERN + "') ";
		}
		else if ((fieldType != null) && (Constants.FIELD_TYPE_TIMESTAMP_DATE.equalsIgnoreCase(fieldType)))
		{
		    fieldName = Variables.DATE_FORMAT_FUNCTION + "(" + fieldName + ",'" + Variables.DATE_PATTERN + "') ";
		}
       
        return fieldName;
    }
    
    public String getColumnNameString(int tableSufix)
    {
        return table.getTableName() + tableSufix + "_" + field;
    }
    
    public boolean equals(Object obj)
    {
        if (obj instanceof DataElement) {
            DataElement dataElement = (DataElement)obj;
            if(!table.equals(dataElement.table))
                return false;
            if(!field.equals(dataElement.field))
                return false;
            
           
            return true;
	    }
        
       else
           return false;
    }
    public int hashCode()
    {
       return 1;
    }

    public String getField()
    {
        return field;
    }
    public void setField(String field)
    {
        this.field = field;
    }
    
    /**
     * @return Returns the fieldType.
     */
    public String getFieldType()
    {
        return fieldType;
    }
    
    /**
     * @param fieldType The fieldType to set.
     */
    public void setFieldType(String fieldType)
    {
        this.fieldType = fieldType;
    }
    
    public String getTableAliasName()
    {
        return table.getTableName();
    }
    
    public Table getTable()
    {
        return table;
    }
    
    public void setTableName(String table)
    {
        this.table.setTableName(table);
    }
    
    public void setTable(String tablename)
    {
        this.table = new Table(tablename);
    }
    
    public void setTable(Table table)
    {
        this.table = table;
    }
    
    
    
}
