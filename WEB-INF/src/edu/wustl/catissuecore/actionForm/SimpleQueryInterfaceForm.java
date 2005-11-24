/**
 * <p>Title: SimpleQueryInterfaceForm Class>
 * <p>Description:  SimpleQueryInterfaceForm Class is used to encapsulate all the request parameters passed 
 * from simple query interface webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.actionForm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Validator;

/**
 * SimpleQueryInterfaceForm Class is used to encapsulate all the request parameters passed 
 * from simple query interface webpage.
 * @author gautam_shetty
 */
public class SimpleQueryInterfaceForm extends ActionForm
{
    
    boolean mutable = true;
    
    Map values = new TreeMap();
    
    String counter;
    
    String pageOf;
    
    String aliasName;
    
    boolean andOrOperation = false;
    
    //Variables neccessary for Configuration of SImple Search 
   
    private String tableName;
    private String []selectedColumnNames;
    private String []columnNames;
  
    /**
     * @return Returns the mutable.
     */
    public boolean isMutable()
    {
        return mutable;
    }
    
    /**
     * @param mutable The mutable to set.
     */
    public void setMutable(boolean mutable)
    {
        this.mutable = mutable;
    }
    
    /**
     * Default constructor.
     */
    public SimpleQueryInterfaceForm()
    {
        this.counter = "1";
    }

    /**
     * @return Returns the counter.
     */
    public String getCounter()
    {
        return counter;
    }

    /**
     * @param counter The counter to set.
     */
    public void setCounter(String counter)
    {
        if (isMutable())
            this.counter = counter;
    }

    /**
     * @return Returns the pageOf.
     */
    public String getPageOf()
    {
        return pageOf;
    }

    /**
     * @param pageOf The pageOf to set.
     */
    public void setPageOf(String pageOf)
    {
        this.pageOf = pageOf;
    }

    public void setValue(String key, Object value)
    {
        if (isMutable())
            values.put(key, value);
    }

    public Object getValue(String key)
    {
        return values.get(key);
    }

    public Collection getAllValues()
    {
        return values.values();
    }

    /**
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

    public Map getValuesMap()
    {
        return values;
    }
    
    /**
     * @return Returns the andOrOperation.
     */
    public boolean isAndOrOperation()
    {
        return andOrOperation;
    }
    
    /**
     * @param andOrOperation The andOrOperation to set.
     */
    public void setAndOrOperation(boolean andOrOperation)
    {
        if (isMutable())
            this.andOrOperation = andOrOperation;
    }
    
	/**
     * Override validate method fo ActionForm class. 
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
    {
        ActionErrors errors = new ActionErrors();
        Validator validator = new Validator();
        
        //if the operation is AND / OR.
        if (isAndOrOperation())
        {
            String key = "SimpleConditionsNode:"+(Integer.parseInt(counter)-1)+"_Condition_DataElement_table";
            String selectedTable = (String)getValue(key);
            
            //if the table is not selected, show an error message.
            if (validator.isValidOption(selectedTable) == false)
            {
                errors.add(ActionErrors.GLOBAL_ERROR, 
                        new ActionError("simpleQuery.object.required"));
                
                this.counter = String.valueOf(Integer.parseInt(this.counter) - 1);
                
                //remove the key for the join condition of last condition object from the map.
                key = "SimpleConditionsNode:"+this.counter+"_Operator_operator";
                values.remove(key);
            }
        }
        else
        {
            boolean tableError = false, attributeError = false, conditionError = false;
            for (int i = 1;i<=Integer.parseInt(counter);i++)
            {
                String key = "SimpleConditionsNode:"+i+"_Condition_DataElement_table";
                String enteredValue = (String)getValue(key);
                if ((tableError == false) && (validator.isValidOption(enteredValue) == false))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, 
                            new ActionError("simpleQuery.object.required"));
                    tableError = true;
                }    
                
                key = "SimpleConditionsNode:"+i+"_Condition_DataElement_field";
                enteredValue = (String)getValue(key);
                if ((attributeError == false) && (validator.isValidOption(enteredValue) == false))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR,
                            new ActionError("simpleQuery.attribute.required"));
                    attributeError = true;
                }
                
                if (conditionError == false)
                {
                    key = "SimpleConditionsNode:"+i+"_Condition_value";
                    enteredValue = (String)getValue(key);
                    String nextOperator = "SimpleConditionsNode:"+i+"_Operator_operator";
                    if ((validator.isEmpty(enteredValue)))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, 
                                new ActionError("simpleQuery.value.required"));
                        conditionError = true;
                    }
                    else
                    {
        	            //---------- DataType validation
        	            String dataElement = "SimpleConditionsNode:"+i+"_Condition_DataElement_field";
        	            String selectedField = (String)getValue(dataElement);
        	            int lastInd = selectedField.lastIndexOf(".");
        	            String dataType = selectedField.substring(lastInd+1);

        	            if((dataType.trim().equals("bigint" ) || dataType.trim().equals("integer" )) && !validator.isNumeric(enteredValue,0))
        	            {
        	            	 errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("simpleQuery.intvalue.required"));
        	            	 conditionError = true;
        	            }// integer or long
        	            else if((dataType.trim().equals("double" )) && !validator.isDouble(enteredValue,0))
        	            {
        	            	 errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("simpleQuery.decvalue.required"));
        	            	 conditionError = true;
        	            } // double
        	        }
                }
            }
        }
        
        //set andOrOperation to false.
        setAndOrOperation(false);
        setMutable(false);
        
        return errors;
    }
    
	/**
	 * @return Returns the selectedColumnNames.
	 */
	public String[] getSelectedColumnNames() {
		return selectedColumnNames;
	}
	
	/**
	 * @param selectedColumnNames The selectedColumnNames to set.
	 */
	public void setSelectedColumnNames(String[] selectedColumnNames) {
		this.selectedColumnNames = selectedColumnNames;
	}
	
	/**
	 * @return Returns the columnNames.
	 */
	public String[] getColumnNames() {
		return columnNames;
	}
	/**
	 * @param columnNames The columnNames to set.
	 */
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}
	/**
	 * @return Returns the tableName.
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * @param tableName The tableName to set.
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return Returns the values.
	 */
	public Map getValues() {
		return values;
	}
	/**
	 * @param values The values to set.
	 */
	public void setValues(Map values) {
		this.values = values;
	}
	
	// -------------  variable to store the menu selected --------------------------
	private String menuSelected="";
	
	/**
	 * @return Returns the menuSelected.
	 */
	public String getMenuSelected() {
		return menuSelected;
	}
	/**
	 * @param menuSelected The menuSelected to set.
	 */
	public void setMenuSelected(String menuSelected) {
		this.menuSelected = menuSelected;
	}
	
	 /**
     * Returns the id assigned to form bean.
     * @return the id assigned to form bean
     */
    public int getFormId()
    {
        return Constants.SIMPLE_QUERY_INTERFACE_ID;
    }
    
    // MD: -- Map added to maintain values to display the Calendar icon
	/**
	 * Map to hold values for rows to display calendar icon.
	 */
	protected Map showCalendarValues = new HashMap();
	
	/**
	 * @return Returns the showCalendarValues.
	 */
	public Map getShowCalendarValues()
	{
		return showCalendarValues;
	}
	/**
	 * @param showCalendarValues The showCalendarValues to set.
	 */
	public void setShowCalendarValues(Map showCalendarValues)
	{
		this.showCalendarValues = showCalendarValues;
	}

	/**
	 * Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is mapped.
	 */
	public void setShowCalendar(String key, Object value)
	{
		showCalendarValues.put(key, value);
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * 
	 * @param key
	 *            the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public Object getShowCalendar(String key)
	{
		return showCalendarValues.get(key);
	}

}