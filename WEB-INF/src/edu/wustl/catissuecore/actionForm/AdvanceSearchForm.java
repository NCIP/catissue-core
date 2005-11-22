/**
 * <p>Title: AdvanceSearchForm Class>
 * <p>Description:  This Class is used to encapsulate all the request parameters passed 
 * from ParticipantSearch.jsp/CollectionProtocolRegistrationSearch.jsp/
 * SpecimenCollectionGroupSearch.jsp & SpecimenSearch.jsp pages. </p>
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

import edu.wustl.catissuecore.util.global.Validator;

/**
 * This Class is used to encapsulate all the request parameters passed from Search Pages.
 * @author aniruddha_phadnis
 */
public class AdvanceSearchForm extends ActionForm
{
	/**
	 * A map that handles all the values of Advanced Search pages
	 */
    private Map values = new HashMap();
    
    /**
	 * A map that handles event parameters' data
	 */
    private Map eventMap = new HashMap();
    
    /**
	 * Objectname of the advancedConditionNode Object
	 */
    private String objectName=new String();
    
    /**
	 * Selected node from the query tree
	 */
    private String selectedNode = new String();
    
    /**
	 * A counter that holds the number of event parameter rows
	 */
    private int eventCounter = 1;
    
    /**
     * Returns the selected node from a query tree.
     * @return The selected node from a query tree.
     * @see #setSelectedNode(String)
     */
	public String getSelectedNode()
	{
		return selectedNode;
	}
	
	/**
     * Sets the selected node of a query tree.
     * @param selectedNode the selected node of a query tree.
     * @see #getSelectedNode()
     */
	public void setSelectedNode(String selectedNode)
	{
		this.selectedNode = selectedNode;
	}
	
    /**
     * No argument constructor for StorageTypeForm class 
     */
    public AdvanceSearchForm()
    {
        reset();
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
    
    //Bug 700: changed the method name for setting the map values as it was same in both AdvanceSearchForm and SimpleQueryInterfaceForm
    /**
     * Associates the specified object with the specified key in the map.
     * @param key the key to which the object is mapped.
     * @param value the object which is mapped.
     */
    public void setValue1(String key, Object value) 
    {    	
    	values.put(key, value);
    }
    
    /**
     * Returns the object to which this map maps the specified key.
     * @param key the required key.
     * @return the object to which this map maps the specified key.
     */
    public Object getValue1(String key) 
    {
        return values.get(key);
    }
	
    /**
     * Returns all the values of the map.
     * @return the values of the map.
     */
	public Collection getAllValues() 
	{
		return values.values();
	}

	/**
     * Sets the map.
     * @param values the map.
     * @see #getValues()
     */
	public void setValues(Map values)
	{
		this.values = values;
	}
	
	/**
     * Returns the map.
     * @return the map.
     * @see #setValues(Map)
     */
	public Map getValues()
	{
		return this.values;
	}
	
	/**
     * Associates the specified array object with the specified key in the map.
     * @param key the key to which the object is mapped.
     * @param value the object which is mapped.
     */
    public void setValueList(String key, Object [] value) 
    {
    	values.put(key, value);
    }
    
    /**
     * Returns the object array to which the specified key is been mapped.
     * @param key the required key.
     * @return the object to which this map maps the specified key.
     */
    public Object[] getValueList(String key)
    {
    	 return (Object [])values.get(key);
    }
        
    /**
     * Resets the values of all the fields.
     * Is called by the overridden reset method defined in ActionForm.  
     */
    protected void reset()
    {
    }

    /**
     * Overrides the validate method of ActionForm.
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
        ActionErrors errors = new ActionErrors();
        Validator validator = new Validator();
        return errors;
    }
    
    /**
     * Returns the object name.
     * @return the object name.
     * @see #setObjectName(String)
     */
	public String getObjectName()
	{
		return objectName;
	}
	
	/**
	 * Sets the object name.
	 * @param objectName The object name to be set.
	 * @see #getObjectName()
	 */
	public void setObjectName(String objectName)
	{
		this.objectName = objectName;
	}
	
	/**
     * Associates the specified object with the specified key in the map.
     * @param key the key to which the object is mapped.
     * @param value the object which is mapped.
     */
    public void setEventMap(String key, Object value) 
    {    	
    	eventMap.put(key, value);
    }

    /**
     * Returns the object to which this map maps the specified key.
     * @param key the required key.
     * @return the object to which this map maps the specified key.
     */
    public Object getEventMap(String key) 
    {
        return eventMap.get(key);
    }
    
    /**
     * Returns the map of event parameters' values.
     * @return the map of event parameters' values.
     */
    public Map getEventValues()
    {
    	return this.eventMap;
    }
    
    /**
     * Returns the no. of rows of event parameters.
     * @return The no. of rows of event parameters.
     * @see #setEventCounter(int)
     */
	public int getEventCounter()
	{
		return eventCounter;
	}
	
	/**
     * Sets the no. of rows of event parameters.
     * @param eventCounter The no. of rows of event parameters.
     * @see #getEventCounter()
     */
	public void setEventCounter(int eventCounter)
	{
		this.eventCounter = eventCounter;
	}
}