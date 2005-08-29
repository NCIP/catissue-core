/**
 * <p>Title: ApproveUserForm Class>
 * <p>Description:  ApproveUserForm Class is used to encapsulate all the request parameters passed 
 * from User Approve/Reject webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 26, 2005
 */
package edu.wustl.catissuecore.actionForm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;


/**
 * ApproveUserForm Class is used to encapsulate all the request parameters passed 
 * from User Approve/Reject webpage.
 * @author gautam_shetty
 */
public class DomainObjectListForm extends AbstractActionForm
{
    
    protected long systemIdentifier;
    
    /**
     * Represents the operation(Approve/Reject) to be performed.
     * */
    protected String operation;
    
    /**
     * Map of users whose registration is to be approved/rejected.
     */
    protected Map values = new HashMap();
    
    /**
     * Comments given by the approver.
     */
    protected String comments;
    
    /**
     * Associates the specified object with the specified key in the map.
     * @param key the key to which the object is mapped.
     * @param value the object which is mapped.
     */
    public void setValue(String key, Object value) 
    {
        if (!value.equals("on")){
            values.put(key, value);
        }
            
    }

    /**
     * Returns the object to which this map maps the specified key.
     * @param key the required key.
     * @return the object to which this map maps the specified key.
     */
    public Object getValue(String key) 
    {
        return values.get(key);
    }
    
    /**
     * Returns the comments given by the approver.
     * @return the comments given by the approver.
     * @see #setComments(String)
     */
    public String getComments()
    {
        return comments;
    }
    
    /**
     * Sets the comments given by the approver.
     * @param comments the comments given by the approver.
     * @see #getComments() 
     */
    public void setComments(String comments)
    {
        this.comments = comments;
    }
    
    /**
     * Initializes an empty ApproveUserForm instance.
     */
    public DomainObjectListForm()
    {
        reset();
    }
    
    /**
     * Resets all the values.
     */
    protected void reset()
    {
        systemIdentifier = -1;
        operation = null;
        comments = null;
        values = new HashMap();
    }
    
    /**
     * Returns the systemIdentifier assigned to User.
     * @return int representing the id assigned to User.
     * @see #setSystemIdentifier(int)
     * */
    public long getSystemIdentifier()
    {
        return (this.systemIdentifier);
    }

    /**
     * Sets an id for the User.
     * @param systemIdentifier id to be assigned to the User.
     * @see #getSystemIdentifier()
     * */
    public void setSystemIdentifier(long identifier)
    {
        this.systemIdentifier = identifier;
    }
    
    /**
     * Returns the operation(Approve/Reject) which is to be performed.
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
     * Returns all values in the map.
     * @return collection of values in the map. 
     */
    public Collection getAllValues()
    {
        return values.values();
    }
    
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.actionForm.AbstractForm#isAddOperation()
     */
    public boolean isAddOperation()
    {
        return false;
    }
    
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.actionForm.AbstractForm#getFormId()
     */
    public int getFormId()
    {
        return Constants.APPROVE_USER_FORM_ID;
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
    
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.actionForm.AbstractForm#setAllValues(edu.wustl.catissuecore.domain.AbstractDomain)
     */
    public void setAllValues(AbstractDomainObject abstractDomain)
    {}
    

    /**
     * Overrides the validate method of ActionForm.
     * */
     public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
     {
         ActionErrors errors = new ActionErrors();
                  
         try
         {
             if (values.size() == 0 && (!operation.equals(Constants.USER_DETAILS)))
             {
                 errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.approveUser.required",operation));
             }
         }
         catch(Exception excp)
         {
             Logger.out.error(excp.getMessage(),excp);
         }
         return errors;
      }
}
