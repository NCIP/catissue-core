/**
 *<p>Title: DataElement</p>
 *<p>Description:  Represents data element of a query</p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */ 
package edu.wustl.catissuecore.query;


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
    private String table;
    
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
    }
    
    /**
     * Constructor
     * @param table Table/object name
     * @param field Field name
     */
    public DataElement(String table, String field)
    {
        this.table = table;
        this.field = field;
    }
    
    public DataElement(String table, String field, String fieldType)
    {
        this.table = table;
        this.field = field;
        this.fieldType = fieldType;
    }
    
    /**
     * SQL string representation
     * @param tableSufix sufix for table name
     * @return SQL string representation
     */
    public String toSQLString(int tableSufix)
    {
        return table + tableSufix + "." + field+" ";
    }
    
    public String getColumnNameString(int tableSufix)
    {
        return table + tableSufix + "_" + field;
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
    
    public String getTable()
    {
        return table;
    }
    
    public void setTable(String table)
    {
        this.table = table;
    }
}
