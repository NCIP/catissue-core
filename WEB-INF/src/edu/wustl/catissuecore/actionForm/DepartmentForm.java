/**
 * <p>Title: DepartmentForm Class</p>
 * <p>Description:  DepartmentForm Class is used to handle transactions related to Department   
 * from Department Add/Edit webpage. </p>
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
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * DepartmentForm Class is used to encapsulate all the request parameters passed 
 * from Department Add/Edit webpage.
 * @author Mandar Deshmukh
 * */
public class DepartmentForm extends AbstractActionForm
{
    /**
     * identifier is a unique id assigned to each Department.
     * */
    private long identifier;

    /**
     * Represents the operation(Add/Edit) to be performed.
     * */
    private String operation;
    
    /**
     * Name of the Department.
     */
    private String name;
    
    /**
     * No argument constructor for UserForm class 
     */
    public DepartmentForm()
    {
        reset();
    }

    /**
     * Copies the data from an AbstractDomain object to a DepartmentForm object.
     * @param Department An AbstractDomain object.  
     */
    public void setAllValues(AbstractDomainObject abstractDomain)
    {
        try
        {
            Department department = (Department)abstractDomain;
            this.identifier = department.getSystemIdentifier().longValue();
            this.name = department.getName();
        }
        catch (Exception excp)
        {
	    	// use of logger as per bug 79
	    	Logger.out.error(excp.getMessage(),excp); 
            
        }
    }

    /**
     * Returns the identifier assigned to Department.
     * @return int representing the id assigned to Department.
     * @see #setIdentifier(int)
     * */
    public long getSystemIdentifier()
    {
        return (this.identifier);
    }

    /**
     * Sets an id for the User.
     * @param identifier id to be assigned to the Department.
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
     * Returns the name of the Department.
     * @return String representing the name of the Department
     */
    public String getName()
    {
        return (this.name);
    }

    /**
     * Sets the name of this Department
     * @param Name name of the Department.
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
        return Constants.DEPARTMENT_FORM_ID;
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
            if (validator.isEmpty(name))
            {
            	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("department.name")));
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