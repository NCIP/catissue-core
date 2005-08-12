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
import edu.wustl.common.util.logger.Logger;

/**
  *
 * Description:  This Class handles the Distribution..
 */
public class DistributionForm extends EventParametersForm
{
	
	private String fromSite;
	private String toSite;
	private int counter=1;
	
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
//		this.fromSite = distributionObject.getFromSite();
//		this.toSite = distributionObject.getToSite();
	}
	
	
	 public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	 {
		ActionErrors errors = super.validate(mapping, request);
		 Validator validator = new Validator();
         
		 try
		 {
			if (fromSite.equals(Constants.SELECT_OPTION))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("Distribution.fromSite")));
			}
		 }
		 catch(Exception excp)
		 {
			 Logger.out.error(excp.getMessage());
		 }
 	  
		try
			{
			   if (toSite.equals(Constants.SELECT_OPTION))
			   {
				   errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("Distribution.toSite")));
			   }
			}
			catch(Exception excp)
			{
				Logger.out.error(excp.getMessage());
			}
			return errors;
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
		 * @param key
		 *            the required key.
		 * @return the object to which this map maps the specified key.
		 */
		public Object getValue(String key)
		{
			return values.get(key);
		}

		

		/**
		 * @param values
		 *            The values to set.
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

}