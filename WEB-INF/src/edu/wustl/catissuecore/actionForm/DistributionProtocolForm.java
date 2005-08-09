/**
 * <p>Title: DistributionProtocolForm Class>
 * <p>Description:  DistributionProtocolForm Class is used to encapsulate all the request parameters passed 
 * from User Add/Edit webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Mar 3, 2005
 */

package edu.wustl.catissuecore.actionForm;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Validator;

/**
 * DistributionProtocolForm Class is used to encapsulate all the request parameters passed 
 * from Distribution Add/Edit webpage.
 * @author Mandar Deshmukh
 * */
public class DistributionProtocolForm extends SpecimenProtocolForm
{
	
	/**
	 * Counter that contains number of rows in the 'Add More' functionality.
	 */
	private int counter=1;
	
	
	
	
	/**
	 * @return Returns the counter.
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
	
	/**
	 * No argument constructor for DistributionProtocolForm class.
	 */
	public DistributionProtocolForm()
	{
		super();
	}
	
	protected void reset()
	{
		super.reset();
	}
	
	/**
	 * Overrides the validate method of ActionForm.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		Validator validator = new Validator();
		try
		{
			if (operation.equals(Constants.ADD) || operation.equals(Constants.EDIT))
            {
                if(this.principalInvestigatorId == -1)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("collectionprotocol.principalinvestigator")));
				}
                if (validator.isEmpty(this.title))
                {
                	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectionprotocol.protocoltitle")));
                }
                
    			Iterator it = this.values.keySet().iterator();
    			while (it.hasNext())
    			{
    				String key = (String)it.next();
    				String value = (String)values.get(key);
    				System.out.println(key+ " : " + value);
    				
    				if(key.indexOf("specimenClass")!=-1 && value.equals(Constants.SELECT_OPTION))
    				{
    					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("collectionprotocol.specimenclass")));
    				}
    				
    				if(key.indexOf("specimenType")!=-1 && value.equals(Constants.SELECT_OPTION))
    				{
    					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("collectionprotocol.specimetype")));
    				}
    				
    				if(key.indexOf("tissueSite")!=-1 && value.equals(Constants.SELECT_OPTION))
    				{
    					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("collectionprotocol.specimensite")));
    				}
    				if(key.indexOf("pathologyStatus")!=-1 && value.equals(Constants.SELECT_OPTION))
    				{
    					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("collectionprotocol.specimenstatus")));
    				}
    				if(key.indexOf("quantityIn")!=-1 && validator.isEmpty(value))
    				{
    					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectionprotocol.quantity")));
    				}
    			}
            }    
		}
		catch (Exception excp)
		{
			excp.printStackTrace();
			errors = new ActionErrors();
		}
		return errors;
	}
	
	/**
	 * Returns the id assigned to form bean
	 */
	public int getFormId()
	{
		return Constants.DISTRIBUTIONPROTOCOL_FORM_ID;
	}
}