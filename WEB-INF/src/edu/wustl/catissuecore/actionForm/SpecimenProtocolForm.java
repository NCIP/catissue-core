/*
 * Created on Jul 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.actionForm;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.SpecimenProtocol;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.util.global.Validator;
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
	 * @param principalinvestigator
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
		try
		{
			SpecimenProtocol protocol = (SpecimenProtocol)abstractDomain;
			
			this.systemIdentifier = protocol.getSystemIdentifier().longValue();
			this.principalInvestigatorId = protocol.getPrincipalInvestigator()
					.getSystemIdentifier().longValue();
			this.title = Utility.toString(protocol.getTitle());
			this.shortTitle = Utility.toString(protocol.getShortTitle());
			this.startDate = Utility.parseDateToString(protocol.getStartDate(),Constants.DATE_PATTERN_MM_DD_YYYY);
			this.endDate = Utility.parseDateToString(protocol.getEndDate(),Constants.DATE_PATTERN_MM_DD_YYYY);
			this.irbID = Utility.toString(protocol.getIrbIdentifier());
			this.enrollment = Utility.toString(protocol.getEnrollment());
			this.descriptionURL = Utility.toString(protocol.getDescriptionURL());
			
			this.activityStatus = Utility.toString(protocol.getActivityStatus());
		}
		catch (Exception excp)
		{
	    	Logger.out.error(excp.getMessage(),excp); 
		}
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
                
                if (validator.isEmpty(this.shortTitle))
                {
                	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectionprotocol.shorttitle")));
                }
                
                if (validator.isEmpty(this.irbID))
                {
                	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectionprotocol.irbid")));
                }
                
                if (validator.isEmpty(startDate))
                {
                	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectionprotocol.startdate")));
                }
				else
				{
				    if (!validator.checkDate(startDate))
				    {
				    	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid",ApplicationProperties.getValue("collectionprotocol.startdate")));
				    }
				}
                if (!validator.isEmpty(endDate) &&  !validator.checkDate(endDate))
                {
                	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid",ApplicationProperties.getValue("collectionprotocol.enddate")));
                }
             
                
      			// code added as per bug id 235 
    			// code to validate startdate less than end date
    			// check the start date less than end date
    			if (validator.checkDate(startDate) && validator.checkDate(endDate )  )
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
    			if (!validator.isNumeric(enrollment) && !validator.isEmpty(enrollment ))
                {
                 	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.enrollment",ApplicationProperties.getValue("collectionprotocol.participants")));
                }
               

            }    
		}
		catch (Exception excp)
		{
	    	// use of logger as per bug 79
	    	Logger.out.error(excp.getMessage(),excp); 
			errors = new ActionErrors();
		}
		return errors;
	}
	
}