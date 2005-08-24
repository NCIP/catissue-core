/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */ 
package edu.wustl.catissuecore.query;


/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

public class RelationCondition
{
    /**
     * Data Element that forms the left part
     * of the operator
     */
    private DataElement leftDataElement;
    
    /**
     * Operation between the two dataelements
     */
    private Operator operator;
    
    /**
     * Data Element that forms the right part
     * of the operator
     */
    private DataElement rightDataElement;
    
    
   
    
    /**
     * Constructor 
     * @param leftDataElement Data Element that forms the left part of the operator
     * @param operator Operation between the two dataelements
     * @param rightDataElement Data Element that forms the right part of the operator
     */
    public RelationCondition(DataElement leftDataElement, Operator operator,
            DataElement rightDataElement)
    {
        super();
        this.leftDataElement = leftDataElement;
        this.operator = operator;
        this.rightDataElement = rightDataElement;
    }
    
    /**
     * SQL string representation of this relation condition
     * @param tableSufix
     * @return
     */
    public String toSQLString(int tableSufix)
    {
        return leftDataElement.toSQLString(tableSufix) +" "+ operator.toSQLString() + " " + rightDataElement.toSQLString(tableSufix)+" ";
    }
    public DataElement getLeftDataElement()
    {
        return leftDataElement;
    }
    public void setLeftDataElement(DataElement leftDataElement)
    {
        this.leftDataElement = leftDataElement;
    }
    public Operator getOperator()
    {
        return operator;
    }
    public void setOperator(Operator operator)
    {
        this.operator = operator;
    }
    public DataElement getRightDataElement()
    {
        return rightDataElement;
    }
    public void setRightDataElement(DataElement rightDataElement)
    {
        this.rightDataElement = rightDataElement;
    }
}
