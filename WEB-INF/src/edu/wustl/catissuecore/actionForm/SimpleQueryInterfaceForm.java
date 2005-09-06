/*
 * Created on Aug 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.actionForm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Validator;

/**
 * @author gautam_shetty
 */
public class SimpleQueryInterfaceForm extends ActionForm
{

    Map values = new HashMap();

    String counter;

    String pageOf;

    String aliasName;

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
     * Overrides the reset method of ActionForm.
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        this.counter = "1";
        this.values = new HashMap();
    }
    
    
    /**
     * Override validate method fo ActionForm class. 
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
    {
        ActionErrors errors = new ActionErrors();
        Validator validator = new Validator();
        
        for (int i = 1;i<=Integer.parseInt(counter);i++)
        {
            String key = "SimpleConditionsNode:"+i+"_Condition_value";
            String enteredValue = (String)getValue(key);
            if (validator.isEmpty(enteredValue))
            {
                errors.add(ActionErrors.GLOBAL_ERROR, 
                        new ActionError("simpleQuery.value.required"));
            }
            else
            {
	            //---------- DataType validation
	            String dataElement = "SimpleConditionsNode:"+i+"_Condition_DataElement_field";
	            String selectedField = (String)getValue(dataElement);
	            int lastInd = selectedField.lastIndexOf(".");
	            String dataType = selectedField.substring(lastInd+1);
//	            System.out.println("\n\n\n\n*******"+ dataType + "\n\n\n\n*******\n");
	            if((dataType.trim().equals("bigint" ) || dataType.trim().equals("integer" )) && !validator.isNumeric(enteredValue,0))
	            {
	            	 errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("simpleQuery.intvalue.required"));
	            }// integer or long
	            else if((dataType.trim().equals("double" )) && !validator.isDouble(enteredValue))
	            {
	            	 errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("simpleQuery.decvalue.required"));
	            } // double
	        }
            
        }
        
        return errors;
    }
}