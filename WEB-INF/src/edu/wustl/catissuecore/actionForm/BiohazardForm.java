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

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * This Class is used to encapsulate all the request parameters passed from Biohazard.jsp page.
 * @author aniruddha_phadnis
 * */
public class BiohazardForm extends AbstractActionForm
{
    /**
     * identifier is a unique id assigned to each Biohazard.
     * */
    private long systemIdentifier;

    /**
     * A String containing the operation(Add/Edit) to be performed.
     * */
    private String operation;
    
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
        try
        {
            Biohazard hazard = (Biohazard) abstractDomain;
            this.systemIdentifier = hazard.getSystemIdentifier().longValue();
            this.name = hazard.getName();
            this.type = hazard.getType();
            this.comments = hazard.getComments();
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage(),excp);
        }
    }

    /**
     * Returns the identifier assigned to Storage Type.
     * @return long identifier assigned to Storage Type.
     * @see #setSystemIdentifier(long)
     * */
    public long getSystemIdentifier()
    {
        return (this.systemIdentifier);
    }

    /**
     * Sets an the identifier for a Storage Type.
     * @param identifier identifier to be assigned to Storage Type.
     * @see #getSystemIdentifier()
     * */
    public void setSystemIdentifier(long identifier)
    {
        this.systemIdentifier = identifier;
    }
    
    /**
     * Returns the operation(Add/Edit) to be performed.
     * @return Returns the operation.
     * @see #setOperation(String)
     */
    public String getOperation()
    {
        return operation;
    }
    
    /**
     * Sets the operation to be performed.
     * @param operation The operation to set.
     * @see #getOperation()
     */
    public void setOperation(String operation)
    {
        this.operation = operation;
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
	 * @param String the comments.
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
	 * @param String the name of the biohazard.
	 * @see #getName(String)
	 */
	public void setName(String name)
	{
		this.name = name;
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
        return Constants.BIOHAZARD_FORM_ID;
    }
    
    /**
     * Resets the values of all the fields.
     * Is called by the overridden reset method defined in ActionForm.  
     * */
    protected void reset()
    {
        this.name = null;
        this.type = null;
        this.comments = null;
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
                checkValidNumber(new Long(systemIdentifier).toString(),"storageType.identifier",errors,validator);
            }
            if (operation.equals(Constants.ADD) || operation.equals(Constants.EDIT))
            {
            	if(type.equals(Constants.SELECT_OPTION))
                {
                	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("biohazard.type")));
                }
            	
            	if (validator.isEmpty(name))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("biohazard.name")));
                }
            }
        }
        catch(Exception excp)
        {
            Logger.out.error(excp.getMessage());
        }
        return errors;
     }
}