/**
 * <p>Title: BiohazardFormForm Class>
 * <p>Description:  This Class is used to encapsulate all the request parameters passed 
 * from Biohazard.jsp page. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 15, 2005
 */

package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * This Class is used to encapsulate all the request parameters passed from Biohazard.jsp page.
 * @author aniruddha_phadnis
 * */
public class BiohazardForm extends AbstractActionForm
{

    /**
     * A string containing the type of Biohazard.
     */
    private String type;

    /**
     * A string containing the name of the Biohazard.
     */
    private String name;
    
    /**
     * A string containing the comments about the Biohazard.
     */
    private String comments;
    
    /**
     * Default Constructor
     */
    public BiohazardForm()
    {
        reset();
    }

    /**
     * This function Copies the data from an biohazard object to a BiohazardForm object.
     * @param abstractDomain A Biohazard object containing the information about Biohazard.  
     */
    public void setAllValues(AbstractDomainObject abstractDomain)
    {
        Biohazard hazard=(Biohazard)abstractDomain;
        this.id = hazard.getId().longValue();
        this.name = hazard.getName();
        this.type = hazard.getType();
        this.comments = hazard.getComment();
    }
    
    /**
     * Returns the type of the storage.
     * @return String the type of the storage.
     * @see #setType(String)
     */
    public String getType()
    {
        return this.type;
    }

    /**
     * Sets the type of the storage.
     * @param type String type of the storage to be set.
     * @see #getType()
     */
    public void setType(String type)
    {
        this.type = type;
    }
         
    /**
     * Returns the comments.
	 * @return String the comments.
	 * @see #setComments(String)
	 */
	public String getComments()
	{
		return comments;
	}
	
	/**
	 * Sets the type of the comments.
	 * @param comments the comments.
	 * @see #getComments(String)
	 */
	public void setComments(String comments)
	{
		this.comments = comments;
	}
	
	/**
     * Returns the name of the biohazard.
	 * @return String name of the biohazard.
	 * @see #setName(String)
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Sets the name of the biohazard.
	 * @param name the name of the biohazard.
	 * @see #getName(String)
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
    /** 
     * Getting Activity Status
     * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getActivityStatus()
     * @return null
     */
    public String getActivityStatus()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    /**
     * Setting Activity Status 
     * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setActivityStatus(java.lang.String)
     * @param activityStatus Setting Activity Status
     */
    public void setActivityStatus(String activityStatus)
    {
        // TODO Auto-generated method stub

    }
    
    /**
     * Checks the operation to be performed is add or not.
     * @return Returns true if operation is equal to "add", else it returns false
     * */
    public boolean isAddOperation()
    {
        return(getOperation().equals(Constants.ADD));
    }
    
    /**
     * Returns the id assigned to form bean
     * @return BIOHAZARD_FORM_ID
     */
    public int getFormId()
    {
        return Constants.BIOHAZARD_FORM_ID;
    }
    
    /**
     * Resets the values of all the fields.
     * Is called by the overridden reset method defined in ActionForm.  
     * */
    protected void reset()
    {
       
        /**
	     * Name : Virender Mehta
	     * Reviewer:Sachin Lale
	     * Bug ID: defaultValueConfiguration_BugID
	     * Patch ID:defaultValueConfiguration_BugID_8
	     * Description: Configuration for default value Biohazard type
	     */
//        this.type = (String)DefaultValueManager.getDefaultValue(Constants.DEFAULT_BIOHAZARD);
        
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
         	// Mandar 10-apr-06 : bugid :353 
        	// Error messages should be in the same sequence as the sequence of fields on the page.

            	if (validator.isEmpty(name))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("biohazard.name")));
                }
            	if(!validator.isValidOption(type))
                {
                	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("biohazard.type")));
                }
        }
        catch(Exception excp)
        {
            Logger.out.error(excp.getMessage());
        }
        return errors;
     }
}