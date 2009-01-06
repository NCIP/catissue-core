/*
 * Created on Jul 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.actionForm;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.DistributionSpecimenRequirement;
import edu.wustl.catissuecore.domain.SpecimenProtocol;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;


/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class SpecimenProtocolForm extends AbstractActionForm
{

	protected long principalInvestigatorId;

	protected String irbID;

	protected String descriptionURL;

	protected String title;

	protected String shortTitle;

	protected String startDate;

	protected String endDate;

	protected String enrollment;

	/**
	 * Patch Id : Collection_Event_Protocol_Order_8 (Changed from HashMap to LinkedHashMap)
	 * Description : To get CollectionProtocol Events in order
	 */
	/**
	 * Map to handle values of all the CollectionProtocol Events
	 */
	protected Map values = new LinkedHashMap();
	
	/**
	 * Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is mapped.
	 */
	public void setValue(String key, Object value)
	{
	    if (isMutable())
	    {
	        values.put(key, value);
	    }
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
	public SpecimenProtocolForm()
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
	public String getEnrollment()
	{
		return enrollment;
	}

	/**
	 * @param participants
	 *            The participants to set.
	 */
	public void setEnrollment(String participants)
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
	 * @param principalInvestigatorId
	 *            The principalinvestigator to set.
	 */
	public void setPrincipalInvestigatorId(long principalInvestigatorId)
	{
		this.principalInvestigatorId = principalInvestigatorId;
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
	 * @return Returns the values.
	 */
	public Map getValues()
	{
		return values;
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
	    SpecimenProtocol protocol = (SpecimenProtocol)abstractDomain;
		
		this.id = protocol.getId().longValue();
		
		if(protocol.getPrincipalInvestigator() != null && protocol.getPrincipalInvestigator().getId() != null)
		{
			this.principalInvestigatorId = protocol.getPrincipalInvestigator()
				.getId().longValue();
		}
		else
		{
			this.principalInvestigatorId = -1;
		}
		
		this.title = Utility.toString(protocol.getTitle());
		this.shortTitle = Utility.toString(protocol.getShortTitle());
		this.startDate = Utility.parseDateToString(protocol.getStartDate(),Variables.dateFormat);
		this.endDate = Utility.parseDateToString(protocol.getEndDate(),Variables.dateFormat);
		this.irbID = Utility.toString(protocol.getIrbIdentifier());
		this.enrollment = Utility.toString(protocol.getEnrollment());
		this.descriptionURL = Utility.toString(protocol.getDescriptionURL());
		
		this.activityStatus = Utility.toString(protocol.getActivityStatus());
	}
	
	/**
	 * Resets the values of all the fields. Is called by the overridden reset
	 * method defined in ActionForm.
	 */
	protected void reset()
	{
		this.principalInvestigatorId = 0;
		this.title = null;
		this.shortTitle = null;
		this.startDate = null;
		this.endDate = null;
		this.irbID = null;
		this.enrollment = null;
		this.descriptionURL = null;
		
		values = new LinkedHashMap();
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
		Validator validator = new Validator();
		try
		{
			if (operation!=null && (operation.equals(Constants.ADD) || operation.equals(Constants.EDIT) || operation.equals("AssignPrivilegePage")))
            {
                if(this.principalInvestigatorId == -1)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("collectionprotocol.principalinvestigator")));
				}

                if (validator.isEmpty(this.title))
                {
                	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectionprotocol.protocoltitle")));
                }
                
                if (validator.isEmpty(this.shortTitle))
                {
                	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectionprotocol.shorttitle")));
                }
                
//              if (validator.isEmpty(this.irbID))
//              {
//                	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectionprotocol.irbid")));
//              }

// --- startdate
                //  date validation according to bug id  722 and 730 and 939
        		String errorKey = validator.validateDate(startDate ,false);
        		if(errorKey.trim().length() >0)
        		{
        			Logger.out.debug("startdate errorKey : " +errorKey);
        			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorKey,ApplicationProperties.getValue("collectionprotocol.startdate")));
        		}

//  --- end date        		
             	if (!validator.isEmpty(endDate))
    			{
    	         	//  date validation according to bug id  722 and 730 and 939
    	    		errorKey = validator.validateDate(endDate ,false);
    	    		if(errorKey.trim().length() >0 && !errorKey.equals(""))
    	    		{
            			Logger.out.debug("enddate errorKey: " + errorKey );
    	    			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorKey,ApplicationProperties.getValue("collectionprotocol.enddate")));
    	    		}
    			}
                
      			// code added as per bug id 235 
    			// code to validate startdate less than end date
    			// check the start date less than end date
    			if (validator.checkDate(startDate) && validator.checkDate(endDate))
    			{
    				if(!validator.compareDates(startDate,endDate))
    				{
    					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("specimenprotocol.invaliddate",ApplicationProperties.getValue("specimenprotocol.invaliddate")));
    				}
    			}
    			if (!validator.isEmpty(enrollment))
                {

    				try
					{
    					Integer intEnrollment = new Integer(enrollment);
					}
    				catch(NumberFormatException e)
					{
    					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.enrollment",ApplicationProperties.getValue("collectionprotocol.participants")));
					}                 	
                }    				
            }
		}
		catch (Exception excp)
		{
	    	// use of logger as per bug 79
	    	Logger.out.error("error in SPForm : " + excp.getMessage(),excp); 
			errors = new ActionErrors();
		}
		return errors;
	}
	
	/**
	 * 
	 * @param key Atring Array of Key
	 * @param requirement Specimen Requirement
	 */
	protected void setSpecimenRequirement(String [] key, DistributionSpecimenRequirement requirement)
	{
	    values.put(key[0] , requirement.getSpecimenClass());
		values.put(key[1] , Utility.getUnit(requirement.getSpecimenClass() , requirement.getSpecimenType()));
		values.put(key[2] , requirement.getSpecimenType());
		values.put(key[3] , requirement.getTissueSite());
		values.put(key[4] , requirement.getPathologyStatus());
		//values.put(key[5] , Utility.toString(requirement.getQuantity().getValue()));
		
		if(!Utility.isQuantityDouble(requirement.getSpecimenClass(),requirement.getSpecimenType()))
		{
			Double doubleQuantity = requirement.getQuantity();
			if(doubleQuantity == null)
			{
				values.put(key[5] , "0"); 
			}
			else if (doubleQuantity.toString().contains("E"))
	    	{    		
				values.put(key[5] , doubleQuantity.toString()); 
	    	}
	    	else
	    	{
	    		long longQuantity = doubleQuantity.longValue();
	    		values.put(key[5] , new Long(longQuantity).toString()); 
	    		
	    	}		
	
		}
		else
		{
			if(requirement.getQuantity() == null)
			{
				values.put(key[5] , "0"); 
			}
			else
			{
				values.put(key[5] , requirement.getQuantity().toString());
			}
		}
		
		values.put(key[6] , Utility.toString(requirement.getId()));
		
		if(requirement.getSpecimenClass().equals(Constants.TISSUE) && requirement.getQuantity() != null)
		{
			String tissueType = requirement.getSpecimenType();
			if(tissueType.equalsIgnoreCase(Constants.FROZEN_TISSUE_SLIDE)
					|| tissueType.equalsIgnoreCase(Constants.FIXED_TISSUE_BLOCK) 
					||tissueType.equalsIgnoreCase(Constants.FROZEN_TISSUE_BLOCK)  
			        ||tissueType.equalsIgnoreCase(Constants.FIXED_TISSUE_SLIDE))
			{
				values.put(key[5] , Utility.toString(new Integer(requirement.getQuantity().intValue())));
			}
		}
	}
}