/**
 * <p>Title: CollectionProtocolForm Class>
 * <p>Description:  CollectionProtocolForm Class is used to encapsulate all the request parameters passed 
 * from User Add/Edit webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Mar 3, 2005
 */

package edu.wustl.catissuecore.actionForm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Validator;

/**
 * CollectionProtocolForm Class is used to encapsulate all the request
 * parameters passed from User Add/Edit webpage.
 * 
 * @author gautam_shetty
 */
public class CollectionProtocolForm extends SpecimenProtocolForm
{
	protected long protocolCoordinatorIds[];

	/**
	 * Counter that contains number of rows in the 'Add More' functionality. outer block
	 */
	private int outerCounter=1;

	/**
	 * Counter that contains number of rows in the 'Add More' functionality. inner block
	 */
	protected Map innerLoopValues = new HashMap();
	
	
	
	
	
	
	
	/**
	 * @return Returns the innerLoopValues.
	 */
	public Map getInnerLoopValues()
	{
		return innerLoopValues;
	}
	/**
	 * @param innerLoopValues The innerLoopValues to set.
	 */
	public void setInnerLoopValues(Map innerLoopValues)
	{
		this.innerLoopValues = innerLoopValues;
	}

	/**
	 * Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is mapped.
	 */
	public void setIvl(String key, Object value)
	{
		innerLoopValues.put(key, value);
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * 
	 * @param key
	 *            the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public Object getIvl(String key)
	{
		return innerLoopValues.get(key);
	}

	/**
	 * @return Returns the outerCounter.
	 */
	public int getOuterCounter()
	{
		return outerCounter;
	}
	/**
	 * @param outerCounter The outerCounter to set.
	 */
	public void setOuterCounter(int outerCounter)
	{
		this.outerCounter = outerCounter;
	}
	
	
	
	
	/**
	 * No argument constructor for CollectionProtocolForm class.
	 */
	public CollectionProtocolForm()
	{
		super();
	}
	
	protected void reset()
	{
		super.reset();
		protocolCoordinatorIds = null;
	}
	
	/**
	 * @return Returns the protocolcoordinator ids.
	 */
	public long[] getProtocolCoordinatorIds()
	{
		return protocolCoordinatorIds;
	}

	/**
	 * @param protocolCoordinatorIds The protocolCoordinatorIds to set.
	 */
	public void setProtocolCoordinatorIds(long[] protocolCoordinatorIds)
	{
		this.protocolCoordinatorIds = protocolCoordinatorIds;
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
                
                if (!validator.isNumeric(enrollment) || validator.isEmpty(enrollment ))
                {
                	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.enrollment",ApplicationProperties.getValue("collectionprotocol.participants")));
                }
                
    			Iterator it = this.values.keySet().iterator();
    			while (it.hasNext())
    			{
    				String key = (String)it.next();
    				String value = (String)values.get(key);
//    				System.out.println(key+ " : " + value);
    				
    				if(key.indexOf("clinicalStatus")!=-1 && value.equals(Constants.SELECT_OPTION))
    				{
    					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("collectionprotocol.clinicalstatus")));
    				}
    				
    				if(key.indexOf("studyCalendarEventPoint")!=-1 && (validator.isEmpty(value) || !validator.isNumeric(value)))
    				{
    					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.studycalendarpoint",ApplicationProperties.getValue("collectionprotocol.studycalendartitle")));
    				}

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
    			// code added as per bug id 235 
    			// code to validate startdate less than end date
    			// check the start date less than end date
    			if (!validator.isEmpty(startDate) && !validator.isEmpty(endDate )  )
    			{
    				try
					{
    					String pattern="MM-dd-yyyy";
    					SimpleDateFormat dF = new SimpleDateFormat(pattern);
    					Date sDate = dF.parse(this.startDate );
    					Date eDate = dF.parse(this.endDate );
    						
    					int check = sDate.compareTo(eDate );
    					
    					if(check>0)
    					{
    						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("specimenprotocol.invaliddate",ApplicationProperties.getValue("specimenprotocol.invaliddate")));
    					}
    					
					} // try
    				catch (Exception excp1)
					{
    					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("specimenprotocol.invaliddate",ApplicationProperties.getValue("specimenprotocol.invaliddate")));
						errors = new ActionErrors();
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
		return Constants.COLLECTION_PROTOCOL_FORM_ID;
	}
	
	public static void main(String[] args)
	{
		int maxCount=1;
		int maxIntCount=1;
		
		CollectionProtocolForm collectionProtocolForm = null;
		
		Object obj = new Object();//request.getAttribute("collectionProtocolForm");
		
		if(obj != null && obj instanceof CollectionProtocolForm)
		{
			collectionProtocolForm = (CollectionProtocolForm)obj;
			maxCount = collectionProtocolForm.getOuterCounter();
		}
	
		for(int counter=1;counter<=maxCount;counter++)
		{
			String commonLabel = "value(CollectionProtocolEvent:" + counter;
			
			String cid = "ivl(" + counter + ")";
			String functionName = "insRow('" + commonLabel + "','" + cid +"')";
			
			if(collectionProtocolForm!=null)
			{
				Object o = collectionProtocolForm.getIvl(cid);
				maxIntCount = Integer.parseInt(o.toString());
			}
		}

	}
}