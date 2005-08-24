package edu.wustl.catissuecore.query;




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
	    return new String(dataElement.toSQLString(tableSufix)+" "+ operator.toSQLString() + " " + value.toString() + " ");
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
}