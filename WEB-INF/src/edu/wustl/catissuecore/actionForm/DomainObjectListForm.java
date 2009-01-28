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

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;


/**
 * ApproveUserForm Class is used to encapsulate all the request parameters passed 
 * from User Approve/Reject webpage.
 * @author gautam_shetty
 */
public class DomainObjectListForm extends AbstractActionForm
{
	private static final long serialVersionUID = 1L;
	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(DomainObjectListForm.class);
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
        if (!value.equals("on"))
        {
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
        comments = null;
        values = new HashMap();
    }
    
    /**
     * Returns all values in the map.
     * @return collection of values in the map. 
     */
    public Collection getAllValues()
    {
        return values.values();
    }
    
    /**
     * @see edu.wustl.catissuecore.actionForm.AbstractForm#getFormId()
     * @return APPROVE_USER_FORM_ID
     */
    public int getFormId()
    {
        return Constants.APPROVE_USER_FORM_ID;
    }

    /**
     * @see edu.wustl.catissuecore.actionForm.AbstractForm#setAllValues(edu.wustl.catissuecore.domain.AbstractDomain)
     * @param abstractDomain An AbstractDomainObject obj
     */
    public void setAllValues(AbstractDomainObject abstractDomain)
    {}
    

    /**
	 * Overrides the validate method of ActionForm.
	 * @return error ActionErrors instance
	 * @param mapping Actionmapping instance
	 * @param request HttpServletRequest instance
	 */
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
        	 logger.error(excp.getMessage(),excp);
         }
         return errors;
      }
}
