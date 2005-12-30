package edu.wustl.catissuecore.query;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;




/**
 *<p>Title: Condition</p>
 *<p>Description:  Represents a condition</p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */
public class Condition {
    
    /**
     * Data element of the condition 
     */
	private DataElement dataElement = new DataElement();
	
	/**
	 * Operator between data element and value
	 */
	private Operator operator = new Operator();
	
	/**
	 * Value
	 */
	private String value;
	
	public Condition()
	{
	    
	}

	/**
	 * Constructor
	 * @param dataElement Data element of the condition
	 * @param op Operator between data element and value
	 * @param value Value
	 */
	public Condition(DataElement dataElement, Operator op, String value)
	{
	    this.dataElement = dataElement;
	    this.operator = op;
	    this.value = value;
	}
	
	/**
	 * Forms String of this condition object
	 * @param tableSufix sufix that needs to be appended to table names
	 * @return
	 */
	public String toSQLString(int tableSufix)
	{
	    String newValue = new String(value);
	    String newOperator = new String(operator.getOperator());

	    if(newOperator.equals(Operator.STARTS_WITH))
        {
	        newValue = newValue+"%";
	        newOperator = Operator.LIKE;
        }
        else if(newOperator.equals(Operator.ENDS_WITH))
        {
            newValue = "%"+newValue;
            newOperator = Operator.LIKE;
        }
        else if(newOperator.equals(Operator.CONTAINS))
        {
            newValue = "%"+newValue+"%";
            newOperator = Operator.LIKE;
        }
        else if(newOperator.equals(Operator.EQUALS_CONDITION))
        {
            newOperator = Operator.EQUAL;
        }
        else if(newOperator.equals(Operator.NOT_EQUALS_CONDITION))
        {
            newOperator = Operator.NOT_EQUALS;
        }
        
        if (dataElement.getFieldType().equalsIgnoreCase(Constants.FIELD_TYPE_TINY_INT))
        {
            if (newValue.equalsIgnoreCase(Constants.CONDITION_VALUE_YES))
            {
                newValue = Constants.TINY_INT_VALUE_ONE;
            }
            else
            {
                newValue = Constants.TINY_INT_VALUE_ZERO;
            }
        }
        
        if (dataElement.getFieldType().equalsIgnoreCase(Constants.FIELD_TYPE_VARCHAR) 
	        	|| dataElement.getFieldType().equalsIgnoreCase(Constants.FIELD_TYPE_TEXT)
	        	|| dataElement.getFieldType().equalsIgnoreCase(Constants.FIELD_TYPE_TIMESTAMP_TIME)
	        	|| dataElement.getFieldType().equalsIgnoreCase(Constants.FIELD_TYPE_TIMESTAMP_DATE))
		{
		    newValue = "'" + newValue + "'";
		}
		else if (dataElement.getFieldType().equalsIgnoreCase(Constants.FIELD_TYPE_DATE))
		{
			newValue = Variables.STR_TO_DATE_FUNCTION + "('" + newValue + "','" + Variables.DATE_PATTERN + "')";
				
		}
        
	    return new String(dataElement.toSQLString(tableSufix) + " "+ newOperator + " " + newValue.toString() + " ");
	}

    public boolean equals(Object obj)
    {
        if (obj instanceof Condition) {
            Condition condition = (Condition)obj;
            if(!dataElement.equals(condition.dataElement))
                return false;
            if(!operator.equals(condition.operator))
                return false;
            if(!value.equals(condition.value))
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
    public DataElement getDataElement()
    {
        return dataElement;
    }
    public void setDataElement(DataElement dataElement)
    {
        this.dataElement = dataElement;
    }
    public Operator getOperator()
    {
        return operator;
    }
    public void setOperator(Operator operator)
    {
        this.operator = operator;
    }
    public String getValue()
    {
        return value;
    }
    public void setValue(String value)
    {
        this.value = value;
    }
    public String toString()
    {
    	return toSQLString(1);
    }
}