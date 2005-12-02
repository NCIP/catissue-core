/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */ 
package edu.wustl.catissuecore.query;

import java.util.HashMap;
import java.util.Map;


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
    
    
	public static final String EQUALS_CONDITION = "Equals" ;
	
	public static final String NOT_EQUALS_CONDITION = "Not Equals" ;

    /**
     * LIKE constant
     */
    
    public static final String LIKE = "like";
    /**
     * NOT EQUALS constant
     */
    public static final String NOT_EQUALS = "!=";
    
    /**
     * LESS THAN OR EQUALS constant
     */
    public static final String LESS_THAN_OR_EQUALS = "<=";
    
    /**
     * GREATER THAN OR EQUALS constant
     */
    public static final String GREATER_THAN_OR_EQUALS = ">=";
    
    /**
     * BETWEEN constant
     */
    public static final String BETWEEN = "Between";
    
    /**
     * NOT BETWEEN constant
     */
    public static final String NOT_BETWEEN = "Not Between";
    
    /**
     * STARTS WITH constant
     */
    public static final String STARTS_WITH = "Starts With";
    
    /**
     * ENDS WITH constant
     */
    public static final String ENDS_WITH = "Ends With";
    
    /**
     * CONTAINS WITH constant
     */
    public static final String CONTAINS = "Contains";
    
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
        return " "+operator+" ";
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
    public static String getOperator(String operator)
    {
    	Map operators = new HashMap();
//    	operators.put(Operator.STARTS_WITH,Operator.LIKE);
//    	operators.put(Operator.ENDS_WITH,Operator.LIKE) ;
//		operators.put(Operator.CONTAINS,Operator.LIKE);
		operators.put(Operator.NOT_EQUALS_CONDITION,Operator.NOT_EQUALS);
		operators.put(Operator.EQUALS_CONDITION,Operator.EQUAL);
		
		/*operators.put(Operator.LESS_THAN_CONDITION,Operator.LESS_THAN);
		operators.put(Operator.GREATER_THAN_CONDITION,Operator.GREATER_THAN) ;
		operators.put(Operator.LIKE_CONDITION,Operator.LIKE);*/
		String value=(String)operators.get(operator);
		return value;
    }
}
