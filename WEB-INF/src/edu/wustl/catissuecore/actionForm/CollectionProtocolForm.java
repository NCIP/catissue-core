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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
//import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * CollectionProtocolForm Class is used to encapsulate all the request
 * parameters passed from User Add/Edit webpage.
 * 
 * @author gautam_shetty
 */
public class CollectionProtocolForm extends AbstractActionForm
{
	/**
	 * identifier is a unique id assigned to each User.
	 */
	private long systemIdentifier;

	/**
	 * Represents the operation(Add/Edit) to be performed.
	 */
	private String operation;

	private String activityStatus;

	private long principalInvestigatorId;

	private long protocolCoordinatorIds[];

	private String irbID;

	private String descriptionURL;

	private String title;

	private String shortTitle;

	private String startDate;

	private String endDate;

	private int enrollment;

	/**
	 * Map to handle values of all the CollectionProtocol Events
	 */
	protected Map values = new HashMap();

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
	 * @return Returns the values.
	 */
	public Collection getAllValues()
	{
		return values.values();
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
	 * No argument constructor for CollectionProtocolForm class.
	 */
	public CollectionProtocolForm()
	{
		reset();
	}

	/**
	 * @return Returns the descriptionurl.
	 */
	public String getDescriptionURL()
	{
		return descriptionURL;
	}

	/**
	 * @param descriptionurl
	 *            The descriptionurl to set.
	 */
	public void setDescriptionURL(String descriptionurl)
	{
		this.descriptionURL = descriptionurl;
	}

	/**
	 * @return Returns the endDate.
	 */
	public String getEndDate()
	{
		return endDate;
	}

	/**
	 * @param endDate
	 *            The endDate to set.
	 */
	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}

	/**
	 * @return Returns the irbid.
	 */
	public String getIrbID()
	{
		return irbID;
	}

	/**
	 * @param irbid
	 *            The irbid to set.
	 */
	public void setIrbID(String irbid)
	{
		this.irbID = irbid;
	}

	/**
	 * @return Returns the participants.
	 */
	public int getEnrollment()
	{
		return enrollment;
	}

	/**
	 * @param participants
	 *            The participants to set.
	 */
	public void setEnrollment(int participants)
	{
		this.enrollment = participants;
	}

	/**
	 * @return Returns the principalinvestigatorid.
	 */
	public long getPrincipalInvestigatorId()
	{
		return principalInvestigatorId;
	}

	/**
	 * @param principalinvestigator
	 *            The principalinvestigator to set.
	 */
	public void setPrincipalInvestigatorId(long principalInvestigatorId)
	{
		this.principalInvestigatorId = principalInvestigatorId;
	}

	/**
	 * @return Returns the protocolcoordinator ids.
	 */
	public long[] getProtocolCoordinatorIds()
	{
		return protocolCoordinatorIds;
	}

	/**
	 * @param protocolcoordinators
	 *            The protocolcoordinators to set.
	 */
	public void setProtocolcoordinators(long[] protocolCoordinatorIds)
	{
		this.protocolCoordinatorIds = protocolCoordinatorIds;
	}

	/**
	 * @return Returns the shorttitle.
	 */
	public String getShortTitle()
	{
		return shortTitle;
	}

	/**
	 * @param shorttitle
	 *            The shorttitle to set.
	 */
	public void setShortTitle(String shorttitle)
	{
		this.shortTitle = shorttitle;
	}

	/**
	 * @return Returns the startDate.
	 */
	public String getStartDate()
	{
		return startDate;
	}

	/**
	 * @param startDate
	 *            The startDate to set.
	 */
	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 *            The title to set.
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @param operation
	 *            The operation to set.
	 */
	public void setOperation(String operation)
	{
		this.operation = operation;
	}

	/**
	 * Returns the id assigned to form bean
	 */
	public int getFormId()
	{
		return Constants.COLLECTION_PROTOCOL_FORM_ID;
	}

	/**
	 * @return Returns the activityStatus.
	 */
	public String getActivityStatus()
	{
		return activityStatus;
	}

	/**
	 * @param activityStatus
	 *            The activityStatus to set.
	 */
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}

	/**
	 * Checks the operation to be performed is add operation.
	 * 
	 * @return Returns true if operation is equal to "add", else it returns
	 *         false
	 */
	public boolean isAddOperation()
	{
		return (getOperation().equals(Constants.ADD));
	}

	/**
	 * Returns the operation(Add/Edit) to be performed.
	 * 
	 * @return Returns the operation.
	 */
	public String getOperation()
	{
		return operation;
	}

	/**
	 * @return Returns the values.
	 */
	public Map getValues()
	{
		return values;
	}

	/**
	 * @param protocolCoordinatorIds The protocolCoordinatorIds to set.
	 */
	public void setProtocolCoordinatorIds(long[] protocolCoordinatorIds)
	{
		this.protocolCoordinatorIds = protocolCoordinatorIds;
	}

	/**
	 * @return Returns the systemIdentifier.
	 */
	public long getSystemIdentifier()
	{
		return systemIdentifier;
	}

	/**
	 * @param systemIdentifier The systemIdentifier to set.
	 */
	public void setSystemIdentifier(long systemIdentifier)
	{
		this.systemIdentifier = systemIdentifier;
	}
	/**
	 * Copies the data from an AbstractDomain object to a CollectionProtocolForm
	 * object.
	 * 
	 * @param abstractDomain
	 *            An AbstractDomain object.
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		try
		{
			CollectionProtocol collectionProtocol = (CollectionProtocol) abstractDomain;
			this.systemIdentifier = collectionProtocol.getSystemIdentifier().longValue();
			this.activityStatus = collectionProtocol.getActivityStatus();
			this.principalInvestigatorId = collectionProtocol.getPrincipalInvestigator()
					.getSystemIdentifier().longValue();
			this.title = collectionProtocol.getTitle();
			this.shortTitle = collectionProtocol.getShortTitle();
			this.startDate = collectionProtocol.getStartDate().toString();
			this.endDate = collectionProtocol.getEndDate().toString();
			this.irbID = collectionProtocol.getIrbIdentifier();
			this.enrollment = collectionProtocol.getEnrollment().intValue();
			this.descriptionURL = collectionProtocol.getDescriptionURL();
		}
		catch (Exception excp)
		{
			excp.printStackTrace();
			Logger.out.error(excp.getMessage());
		}
	}
	
	/**
	 * Resets the values of all the fields. This method defined in ActionForm is
	 * overridden in this class.
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		reset();
	}

	/**
	 * Resets the values of all the fields. Is called by the overridden reset
	 * method defined in ActionForm.
	 */
	private void reset()
	{
		this.systemIdentifier = 0;
		this.activityStatus = null;
		this.principalInvestigatorId = 0;
		this.title = null;
		this.shortTitle = null;
		this.startDate = null;
		this.endDate = null;
		this.irbID = null;
		this.enrollment = 0;
		this.descriptionURL = null;
		
		values = new HashMap();
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
    				
    				if(key.indexOf("clinicalStatus")!=-1 && value.equals(Constants.SELECT_OPTION))
    				{
    					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("collectionprotocol.clinicalstatus")));
    				}
    				
    				if(key.indexOf("studyCalendarEventPoint")!=-1 && validator.isEmpty(value))
    				{
    					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectionprotocol.studycalendartitle")));
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
            }    
		}
		catch (Exception excp)
		{
			excp.printStackTrace();
			errors = new ActionErrors();
		}
		return errors;
	}
}