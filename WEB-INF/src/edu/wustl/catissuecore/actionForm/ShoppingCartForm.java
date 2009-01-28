/**
 * <p>Title: ShoppingCartForm Class>
 * <p>Description:  This Class is used to encapsulate all the request parameters passed 
 * from ShoppingCart.jsp page. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 15, 2005
 */

package edu.wustl.catissuecore.actionForm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.global.Validator;

/**
 * This Class is used to encapsulate all the request parameters passed from ShoppingCart.jsp page.
 * @author aniruddha_phadnis
 * */
public class ShoppingCartForm extends ActionForm
{

	private static final long serialVersionUID = 1L;

	/**
     * The operation(Add/Delete/Export) to be performed.
     */
    private String operation;
    
    private boolean checkAll;
    
	private Map values = new HashMap();
	
	/**
	 * Default Constructor
	 */
	public ShoppingCartForm()
	{
	}
	
	/**
     * Returns the operation(Add/Delete/Export) to be performed.
     * @return Returns the operation.
     */
    public String getOperation()
    {
        return operation;
    }
    
    /**
     * Sets the operation to be performed.
     * @param operation The operation to set.
     */
    public void setOperation(String operation)
    {
        this.operation = operation;
    }
    
    /**
	 * @return Returns the checkAll.
	 */
	public boolean isCheckAll()
	{
		return checkAll;
	}
	/**
	 * @param checkAll The checkAll to set.
	 */
	public void setCheckAll(boolean checkAll)
	{
		this.checkAll = checkAll;
	}
	
	 /**
     * Associates the specified object with the specified key in the map.
     * @param key the key to which the object is mapped.
     * @param value the object which is mapped.
     */
    public void setValue(String key, Object value) 
    {
            values.put(key, value);
    }

    /**
     * Returns the object to which this map maps the specified key.
     * @param key the required key.
     * @return the object to which this map maps the specified key.
     */
    public Object getValue(String key) 
    {
        return values.get(key);
    }
    
	
	/**
	 * @return Returns the values.
	 */
	public Collection getAllValues() 
	{
		return values.values();
	}

	/**
	 * @param values
	 * The values to set.
	 */
	public void setValues(Map values)
	{
		this.values = values;
	}
	
	/**
	 * @param values The values to set.
	 */
	public Map getValues()
	{
		return this.values;
	}
	
	/**
	 * Overrides the validate method of ActionForm.
	 * @return error ActionErrors instance
	 * @param mapping Actionmapping instance
	 * @param request HttpServletRequest instance
	 */
     public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
     {
         ActionErrors errors = new ActionErrors();
//         Validator validator = new Validator();

         return errors;
     }
     
     /**
      * @return SHOPPING_CART_FORM_ID Returns the id assigned to form bean
      */
     public int getFormId()
     {
         return Constants.SHOPPING_CART_FORM_ID;
     }
}