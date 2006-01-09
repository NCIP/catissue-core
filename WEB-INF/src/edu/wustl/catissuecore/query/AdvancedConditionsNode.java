/**
 *<p>Title: AdvancedConditionsNode</p>
 *<p>Description:  Represents Conditions node of a tree formed in advanced query interface </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

package edu.wustl.catissuecore.query;

import java.util.Vector;





public class AdvancedConditionsNode {
    
    /**
     * Object of the conditions in this node
     */
    private String objectName =  new String();
	
    /**
     * Vector of Condition objects that are on object identified by objectName
     * and are AND ed in the SQL representation of the query.
     */
    private Vector objectConditions = new Vector();
	
    /**
     * Operation with child nodes
     */
    private Operator operationBtwChildNodes = new Operator(Operator.OR);
    
    private boolean defaultAndOr = false;
	
	/**
	 * Constructor
	 * @param objectName - Name of the object to which these conditions belong
	 */
	public AdvancedConditionsNode(String objectName)
	{
	    this.objectName = objectName;
	}

	/**
	 * Adds condition to objectConditions vector
	 * @param condition
	 * @return
	 */
	public boolean addConditionToNode(Condition condition)
	{
	    return objectConditions.add(condition);
	}

    public Operator getOperationWithChildCondition()
    {
        return operationBtwChildNodes;
    }
    
    public void setOperationWithChildCondition(
            Operator operationBtwChildNodes)
    {
        this.operationBtwChildNodes = operationBtwChildNodes;
    }
    
     
    /**
     * Forms SQL String for this node
     * @param tableSufix sufix to be appended to table names in the SQL string formation
     * @return SQL string
     */
    public String toSQLString(int tableSufix)
    {
        StringBuffer conditionsString = new StringBuffer();
        for(int i=0;i<objectConditions.size();i++)
        {
            conditionsString.append(((Condition) objectConditions.get(i)).toSQLString(tableSufix)+ " " );
            if(i != objectConditions.size()-1)
                conditionsString.append(Operator.AND+" ");
        }
        return conditionsString.toString();
    }
    
    public boolean equals(Object obj)
    {
        if ( obj instanceof AdvancedConditionsNode) 
        {
            AdvancedConditionsNode advancedConditionsNode = (AdvancedConditionsNode)obj;
            if(objectConditions.equals(advancedConditionsNode.objectConditions))
                return false;
            if(operationBtwChildNodes.equals(advancedConditionsNode.operationBtwChildNodes))
                return false;
            return true;
        }
        else
        {
            return false;
        }
    }
        
    public int hashCode()
    {
               return 1;
    }
    public Vector getObjectConditions()
    {
        return objectConditions;
    }
    public void setObjectConditions(Vector objectConditions)
    {
        this.objectConditions = objectConditions;
    }
    public String getObjectName()
    {
        return objectName;
    }
    public void setObjectName(String objectName)
    {
        this.objectName = objectName;
    }

	public boolean isDefaultAndOr() {
		return defaultAndOr;
	}

	public void setDefaultAndOr(boolean defaultAndOr) {
		this.defaultAndOr = defaultAndOr;
	}
}