/**
 *<p>Title: Relation</p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */ 
package edu.wustl.catissuecore.query;



public class Relation
{
    /**
     * source table of the relation
     */
    private String sourceTable;
    
    /**
     * Target table of the relation
     */
    private String targetTable;
    
    
    /**
     * Constructor
     * @param sourceTable source table of the relation
     * @param targetTable Target table of the relation
     */
    public Relation(String sourceTable, String targetTable)
    {
        super();
        this.sourceTable = sourceTable;
        this.targetTable = targetTable;
    }
    
    public String getSourceTable()
    {
        return sourceTable;
    }
    
    public void setSourceTable(String sourceTable)
    {
        this.sourceTable = sourceTable;
    }
    
    public String getTargetTable()
    {
        return targetTable;
    }
    
    public void setTargetTable(String targetTable)
    {
        this.targetTable = targetTable;
    }
    
    public boolean equals(Object obj)
    {
        if (obj instanceof Relation) 
        {
            Relation relation = (Relation)obj;
            if( !this.sourceTable.endsWith(relation.sourceTable))
                return false;
            if(!this.targetTable.equals(relation.targetTable))
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
}
