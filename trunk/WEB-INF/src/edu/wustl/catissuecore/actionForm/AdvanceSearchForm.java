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
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.QueryBizLogic;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * This Class is used to encapsulate all the request parameters passed from Search Pages.
 * @author aniruddha_phadnis
 */
public class AdvanceSearchForm extends ActionForm
{
//	Added by Ashish for showing tabs on this page.
	/**
	 * tabIndex = 1 for Biospecimen Tab
	 * tabIndex = 2 for Pathological Case Tab
	 * tabIndex = 3 for Biospecimen Array Tab
	 */
	private int tabIndex = 1;
	
	/**
	 * @return the tabIndex
	 */
	public int getTabIndex()
	{
		return tabIndex;
	}	
	/**
	 * @param tabIndex the tabIndex to set
	 */
	public void setTabIndex(int tabIndex)
	{
		this.tabIndex = tabIndex;
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
    
    
    String itemNodeId = "";
    
    //Variables neccessary for Configuration of Advance Search Results 
    
     private String tableName;
     private String []selectedColumnNames;
     private String []columnNames;
    
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

        String opConstant = "Operator:";
        Iterator it = values.keySet().iterator();

        if(objectName != null && !objectName.equals(""))
        {
        	Map resourceBundleMap = SearchUtil.getResourceBundleMap(objectName);
        	
        	Iterator iterator = resourceBundleMap.keySet().iterator();
        	
        	while(iterator.hasNext())
        	{
        		String valKey = (String)iterator.next(); //Value Key - ALIAS_NAME:COLUMN_NAME
        		String opKey  = opConstant + valKey; //Operator Key - OPERATOR:ALIAS_NAME:COLUMN_NAME
        		
        		String opValue = (String)values.get(opKey); //Operator Value
        		
        		if(validator.isOperator(opValue)) //IF the operator is a valid operator
        		{
        			String value = (String)values.get(valKey);
        			NameValueBean bean = (NameValueBean)resourceBundleMap.get(valKey);
        			String labelName = bean.getName(); //A key in ApplicationResources.properties
        			
        			if(!validator.isValue(value)) //IF the value is a valid value
        			{
        				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.missing",ApplicationProperties.getValue(labelName)));
        			}
        			else
        			{
        				if(!SearchUtil.STRING.equals(bean.getValue())) //IF the datatype is not STRING
        				{
        					if(SearchUtil.DATE.equals(bean.getValue())) //IF the datatype is DATE
        					{
        						if(!validator.checkDate(value)) //IF the start date is improper
        						{
									errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue(labelName)));
        						}
        						else
        						{
        							valKey = valKey + ":HLIMIT"; //Key for second field
        							value = (String)values.get(valKey);
        							
        							if(!validator.isValue(value)) //IF the value is a valid value
        		        			{
        		        				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.missing",ApplicationProperties.getValue(labelName)));
        		        			}
        							else
        							{
        								if(value!= null && !validator.checkDate(value)) //IF the end date is improper
                						{
        									errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue(labelName)));
                						}
        							}
        						}
        					}
        					else if(SearchUtil.NUMERIC.equals(bean.getValue())) //IF the datatype is NUMERIC
        					{
        						/* Aarti: Bug#1496 - '0' value should be allowed for search on fields that are double */
        						if(!validator.isDouble(value,true)) //IF the numeric value is improper
        						{
        							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue(labelName)));
        						}
        						else
        						{
        							valKey = valKey + ":HLIMIT"; //Key for second field
        							value = (String)values.get(valKey);
        							
        							if(!validator.isValue(value)) //IF the value is a valid value
        		        			{
        		        				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.missing",ApplicationProperties.getValue(labelName)));
        		        			}
        							else
        							{
        								if(value!= null && !validator.isDouble(value)) //IF the end value is improper
                						{
        									errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue(labelName)));
                						}
        							}
        						}
        					}
        				}
        			}
        		}
        	}
        	
        	if(objectName.equals("Specimen"))
        	{
        		validateEventParameters(validator,errors);
        	}
        }
       
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
	
	
	public String getItemNodeId() {
		return itemNodeId;
	}
	
	public void setItemNodeId(String itemId) {
		this.itemNodeId = itemId;
	}
    
    //Map added to maintain values to display the Calendar icon
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
	//This method validates the Event-Parameters Block in Specimen Search Page
	private void validateEventParameters(Validator validator,ActionErrors errors)
	{
		//Constants for EventMap keys
		String eventName = "EventName_";
		String eventColumn = "EventColumnName_";
		String eventOperator = "EventColumnOperator_";
		String eventValue = "EventColumnValue_";

		QueryBizLogic bizLogic = (QueryBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.SIMPLE_QUERY_INTERFACE_ID);
		Map eventParameterDisplayNames = null;
		
		try
		{
			eventParameterDisplayNames = SearchUtil.getEventParametersDisplayNames(bizLogic,SearchUtil.getEventParametersTables(bizLogic));
		}
		catch(Exception e)
		{
			Logger.out.debug("Exception in AdvanceSearchForm ",e);
		}
		
		for(int i=1;i<=eventCounter;i++)
		{
			String name = (String)eventMap.get(eventName + i);
			String column = (String)eventMap.get(eventColumn + i);
			
			if(validator.isValidOption(name) && validator.isValidOption(column))
			{
				String value = (String)eventMap.get(eventValue + i);
				
				String fieldName = column.substring(column.indexOf(".")+1,column.lastIndexOf("."));
				String dataType = column.substring(column.lastIndexOf(".")+1);
				String errorKey = name + "." + fieldName;
				
				if(eventParameterDisplayNames != null && eventParameterDisplayNames.get(errorKey) != null)
				{
					errorKey = (String)eventParameterDisplayNames.get(errorKey);
				}
				
				if(value.trim().equals(""))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.missing",errorKey));
				}
				else if(dataType.equals("bigint") || dataType.equals("integer"))
				{
					if(!validator.isNumeric(value,0))
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",errorKey));
					}
				}
				else if(dataType.equals("double"))
				{
					if(!validator.isDouble(value))
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",errorKey));
					}
				}
				else if(dataType.equals("timestamp"))
				{
					if(!validator.checkDate(value))
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",errorKey));
					}
				}
			}
		}
	}
}