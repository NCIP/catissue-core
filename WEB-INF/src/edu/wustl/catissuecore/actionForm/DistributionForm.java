/**
 * <p>Title: DistributionForm Class</p>
 * <p>Description:  This Class handles the Distribution..
 * <p> It extends the EventParametersForm class.    
 * Copyright:    Copyright (c) 2005
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Jyoti Singh
 * @version 1.00
 * Created on Aug 10, 2005
 */

package edu.wustl.catissuecore.actionForm;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Validator;

/**
  *
 * Description:  This Class handles the Distribution..
 */
public class DistributionForm extends EventParametersForm
{
	
	private String fromSite;
	private String toSite;
	private int counter=1;
	private String distributionProtocolId;
	
	/**
	 * Map to handle values of all the CollectionProtocol Events
	 */
	protected Map values = new HashMap();
	
	public int getFormId()
	{
		return Constants.DISTRIBUTION_FORM_ID;
	}

	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		super.setAllValues(abstractDomain);
		Distribution distributionObject = (Distribution)abstractDomain ;
		this.fromSite = String.valueOf(distributionObject.getFromSite().getSystemIdentifier());
		this.toSite = String.valueOf(distributionObject.getToSite().getSystemIdentifier());
		this.userId = distributionObject.getUser().getSystemIdentifier().longValue();
		this.distributionProtocolId = String.valueOf(distributionObject.getDistributionProtocol().getSystemIdentifier());
	}
	
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errors = super.validate(mapping, request);
		Validator validator = new Validator();

		if(distributionProtocolId.equals("-1"))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("distribution.protocol")));
		}
		
		if(fromSite.equals("-1"))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("distribution.fromSite")));
		}
		
		if(toSite.equals("-1"))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("distribution.toSite")));
		}
		
		//Validations for Add-More Block
        String className = "DistributedItem:";
        String key1 = "_Specimen_systemIdentifier";
        String key2 = "_quantity";
        int index = 1;
        boolean isError = false;

        while(true)
        {
        	String keyOne = className + index + key1;
			String keyTwo = className + index + key2;
        	String value1 = (String)values.get(keyOne);
        	String value2 = (String)values.get(keyTwo);
        	
        	if(value1 == null || value2 == null)
        	{
        		break;
        	}
        	else if(value1.equals(Constants.SELECT_OPTION) && value2.equals(""))
        	{
        		values.remove(keyOne);
        		values.remove(keyTwo);
        	}
        	else if(!value1.equals(Constants.SELECT_OPTION))
        	{
        		if(value2.equals(""))
        		{
        			isError = true;
        			break;
        		}
        		else
        		{
        			if(!validator.isDouble(value2))
        			{
        				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("itemrecord.quantity")));
        				break;
        			}
        		}
        	}
        	else if(value1.equals(Constants.SELECT_OPTION) && !value2.equals(""))
        	{
        		isError = true;
        		break;
        	}
        	index++;
        }
        
        if(isError)
        {
        	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.distribution.missing",ApplicationProperties.getValue("distribution.msg")));
        }

		return errors;
	}
	
	/**
	 * @return Returns the distributionProtocolId.
	 */
	public String getDistributionProtocolId()
	{
		return distributionProtocolId;
	}
	/**
	 * @param distributionProtocolId The distributionProtocolId to set.
	 */
	public void setDistributionProtocolId(String distributionProtocolId)
	{
		this.distributionProtocolId = distributionProtocolId;
	}
	
	/**
	 * @return
	 */ 
	public String getFromSite() {
		return fromSite;
	}

	/**
	 * @param fromSite
	 */
	public void setFromSite(String fromSite) {
		this.fromSite = fromSite;
	}

	/**
	 * @return
	 */
	public int getCounter()
	{
		return counter;
	}
	
	/**
	 * @param counter The counter to set.
	 */
	public void setCounter(int counter)
	{
		this.counter = counter;
	}
	public String getToSite() {
		return toSite;
	}

	/**
	 * @param toSite
	 */
	public void setToSite(String toSite) {
		this.toSite = toSite;
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
		 * 
		 * @param key the required key.
		 * @return the object to which this map maps the specified key.
		 */
		public Object getValue(String key)
		{
			return values.get(key);
		}		

		/**
		 * @param values The values to set.
		 */
		public void setValues(Map values)
		{
			this.values = values;
		}

	/**
	 * @return
	 */
	public Map getValues() {
		return values;
	}

	protected void reset()
    {
        super.reset();
        this.distributionProtocolId = null;
        this.fromSite = null;
        this.toSite = null;
        this.counter =1;
       
    }

}