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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author gautam_shetty
 */
public class SimpleQueryInterfaceForm extends ActionForm
{

    Map values = new HashMap();
    
    String counter;
    
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
}