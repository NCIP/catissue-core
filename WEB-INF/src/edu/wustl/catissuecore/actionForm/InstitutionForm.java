/**
 * <p>Title: InstitutionForm Class>
 * <p>Description:  This Class is used to encapsulate all the request parameters passed 
 * from Institute.jsp page. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Mar 3, 2005
 */

package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * This Class is used to encapsulate all the request parameters passed from Institute.jsp page.
 * @author kapil_kaveeshwar
 * */
public class InstitutionForm extends AbstractActionForm
{
    /**
     * systemIdentifier is a unique id assigned to each Institute.
     * */
    private long systemIdentifier = -1;

    /**
     * A String containing the operation(Add/Edit) to be performed.
     * */
    private String operation = null;
    
    /**
     * A string containing the name of the institute.
     */
    private String name = null;  

    /**
     * No argument constructor for InstitutionForm class 
     */
    public InstitutionForm()
    {
        reset();
    }

    /**
     * This function Copies the data from an institute object to a InstitutionForm object.
     * @param institute An Institute object containing the information about the institute.  
     */
    public void setAllValues(AbstractDomainObject abstractDomain)
    {
        try
        {
            Institution institute = (Institution) abstractDomain;
            this.systemIdentifier = institute.getSystemIdentifier().longValue();
            this.name = institute.getName();
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage(),excp);
        }
    }

    /**
     * Returns the systemIdentifier assigned to Institute.
     * @return int representing the id assigned to Institute.
     * @see #setSystemIdentifier(int)
     * */
    public long getSystemIdentifier()
    {
        return (this.systemIdentifier);
    }

    /**
     * Sets an id for the Institute.
     * @param systemIdentifier id to be assigned to the Institute.
     * @see #getSystemIdentifier()
     * */
    public void setSystemIdentifier(long identifier)
    {
        this.systemIdentifier = identifier;
    }
    
    /**
     * Returns the operation(Add/Edit) to be performed.
     * @return Returns the operation.
     */
    public String getOperation()
    {
        return operation;
    }
    
    /**
     * Sets the operation to be performed.
     * @param operation The operation to set.
     */
    public void setOperation(String operation)
    {
        this.operation = operation;
    }
    
    /**
     * Returns the login name of the institute.
     * @return String representing the login name of the institute
     * @see #setLoginName(String)
     */
    public String getName()
    {
        return (this.name);
    }

    /**
     * Sets the login name of this institute
     * @param loginName login name of the institute.
     * @see #getLoginName()
     */
    public void setName(String name)
    {
        this.name = name;
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
     */
    public int getFormId()
    {
        return Constants.INSTITUTION_FORM_ID;
    }
    
 
    /**
     * Resets the values of all the fields.
     * Is called by the overridden reset method defined in ActionForm.  
     * */
    protected void reset()
    {
        this.systemIdentifier = -1;
        this.name = null;
    }

    /**
    * Overrides the validate method of ActionForm.
    * */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
    	System.out.println("here in validate");
        ActionErrors errors = new ActionErrors();
        Validator validator = new Validator();
        try
        {
            if (operation.equals(Constants.SEARCH))
            {
            	if(systemIdentifier == 0)
            	{
            		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("institution.identifier")));            		
            	}
            	else
            	{
            		checkValidNumber(new Long(systemIdentifier).toString(),"institution.identifier",errors,validator);
            	}	
            }
            if (operation.equals(Constants.ADD) || operation.equals(Constants.EDIT))
            {
                if (validator.isEmpty(name))
                {
                	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("institution.name")));
                }
            }    
        }
        catch(Exception excp)
        {
            Logger.out.error(excp.getMessage(),excp);
        }
        return errors;
     }
    
    
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getActivityStatus()
     */
    public String getActivityStatus()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setActivityStatus(java.lang.String)
     */
    public void setActivityStatus(String activityStatus)
    {
        // TODO Auto-generated method stub

    }
     
}