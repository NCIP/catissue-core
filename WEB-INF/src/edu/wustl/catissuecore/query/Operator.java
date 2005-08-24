/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */ 
package edu.wustl.catissuecore.query;


public class Operator
{
    /**
     * OR constant
     */
    public static final String OR = "OR";
    
    /**
     * EXIST constant
     */
    public static final String EXIST = "EXIST";
    
    /**
     * EQUAL constant
     */
    public static final String EQUAL = "=";
    
    /**
     * LESS THAN constant
     */
    public static final String LESS_THAN = "<";
    
    /**
     * AND constant
     */
    public static final String AND = "AND";
    
    /**
     * GREATER THAN constant
     */
    public static final String GREATER_THAN = ">";
    
    /**
     * Operator String
     */
    private String operator;
    
    public Operator()
    {
    }
    
    public Operator(String operator)
    {
        this.operator = operator;
    }
    
    public String toSQLString()
    {
        return operator;
    }
    
    public boolean equals(Object obj)
    {
        if (obj instanceof DataElement) {
            Operator operator = (Operator)obj;
            if(!this.operator.equals(operator.operator))
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

    public String getOperator()
    {
        return operator;
    }
    
    public void setOperator(String operator)
    {
        this.operator = operator;
    }
}
