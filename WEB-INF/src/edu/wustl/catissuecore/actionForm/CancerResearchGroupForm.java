/**
 * <p>Title: CancerResearchGroupForm Class</p>
 * <p>Description:  CancerResearchGroupForm Class is used to handle transactions related to CancerResearchGroup   
 * from CancerResearchGroup Add/Edit webpage. </p>
 * Copyright:    Copyright (c) 2005
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on May 23rd, 2005
 */
package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * CancerResearchGroupForm Class is used to encapsulate all the request parameters passed 
 * from CancerResearchGroup Add/Edit webpage.
 * @author Mandar Deshmukh
 * */
public class CancerResearchGroupForm extends AbstractActionForm
{
    /**
     * identifier is a unique id assigned to each CancerResearchGroup.
     * */
    private long identifier;

    /**
     * Represents the operation(Add/Edit) to be performed.
     * */
    private String operation;
    
    /**
     * Name of the CancerResearchGroup.
     */
    private String name;
    
    /**
     * No argument constructor for UserForm class 
     */
    public CancerResearchGroupForm()
    {
        reset();
    }

    /**
     * Copies the data from an AbstractDomain object to a CancerResearchGroupForm object.
     * @param CancerResearchGroup An AbstractDomain object.  
     */
    public void setAllValues(AbstractDomainObject abstractDomain)
    {
        try
        {
        	CancerResearchGroup cancerResearchGroup = (CancerResearchGroup)abstractDomain;
            this.identifier = cancerResearchGroup.getSystemIdentifier().longValue();
            this.name = cancerResearchGroup.getName();
        }
        catch (Exception excp)
        {
            excp.printStackTrace();	
            Logger.out.error(excp.getMessage());
        }
    }

    /**
     * Returns the identifier assigned to CancerResearchGroup.
     * @return int representing the id assigned to CancerResearchGroup.
     * @see #setIdentifier(int)
     * */
    public long getSystemIdentifier()
    {
        return (this.identifier);
    }

    /**
     * Sets an id for the User.
     * @param identifier id to be assigned to the CancerResearchGroup.
     * @see #getIdentifier()
     * */
    public void setSystemIdentifier(long identifier)
    {
        this.identifier = identifier;
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
     * Returns the name of the CancerResearchGroup.
     * @return String representing the name of the CancerResearchGroup
     */
    public String getName()
    {
        return (this.name);
    }

    /**
     * Sets the name of this CancerResearchGroup
     * @param Name name of the CancerResearchGroup.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Checks the operation to be performed is add operation.
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
        return Constants.CANCER_RESEARCH_GROUP_FORM_ID;
    }
    
    /**
     * Resets the values of all the fields.
     * This method defined in ActionForm is overridden in this class.
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        reset();
    }
    
    /**
     * Resets the values of all the fields.
     * Is called by the overridden reset method defined in ActionForm.  
     * */
    protected void reset()
    {
        this.identifier = -1;
        this.operation = null;
        this.name = null;
    }

    /**
    * Overrides the validate method of ActionForm.
    * */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
        ActionErrors errors = new ActionErrors();
        Validator validator = new Validator();
        try
        {
            if (operation.equals(Constants.SEARCH))
            {
            	if(identifier == 0)
            	{
            		System.out.println("Identifier: "+identifier);
            		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("cancerResearchGroup.identifier")));            		
            	}
            	else
            	{
            		checkValidNumber(new Long(identifier).toString(),"cancerResearchGroup.identifier",errors,validator);
            	}	
            }
            if (operation.equals(Constants.ADD) || operation.equals(Constants.EDIT))
            {
                if (validator.isEmpty(name))
                {
                	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("cancerResearchGroup.name")));
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