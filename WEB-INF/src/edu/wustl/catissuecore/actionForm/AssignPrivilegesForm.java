/**
 * <p>Title: AssignPrivilegesForm Class>
 * <p>Description:  This Class is used to encapsulate all the request parameters passed 
 * from AssignPrivileges.jsp page. </p>
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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * This Class is used to encapsulate all the request parameters passed from AssignPrivileges.jsp page.
 * @author aniruddha_phadnis
 * */
public class AssignPrivilegesForm extends ActionForm
{
	/**
     * A String containing the assignment operation(Allow/Disallow) to be performed.
     * */
    private String assignOperation;
    
    /**
     * A String array containing the list of privileges.
     * */
    private String [] privileges;
    
    /**
     * A String array containing the list of object-types.
     * */
    private String [] objectTypes;
    
    /**
     * A String array containing the list of record identifiers.
     * */
    private String [] recordIds;
    
    /**
     * A String array containing the list of attributes.
     * */
    private String [] attributes;
    
    /**
     * A String array containing the list of groups/users.
     * */
    private String [] groups;
    
    /**
     * Returns the assignment operation.
     * @return String the assignment operation.
     * @see #setAssignOperation(String)
     */
	public String getAssignOperation()
	{
		return assignOperation;
	}
	
	/**
     * Sets the assignment operation.
     * @param assignOperation String the assignment operation.
     * @see #getAssignOperation()
     */
	public void setAssignOperation(String assignOperation)
	{
		this.assignOperation = assignOperation;
	}
	
	/**
     * Returns the list of attributes.
     * @return String[] the list of attributes.
     * @see #setAttributes(String[])
     */
	public String[] getAttributes()
	{
		return attributes;
	}
	
	/**
     * Sets the list of attributes.
     * @param attributes String[] the list of attributes.
     * @see #getAttributes()
     */
	public void setAttributes(String[] attributes)
	{
		this.attributes = attributes;
	}
	
	/**
     * Returns the list of groups/users.
     * @return String[] the list of groups/users.
     * @see #setGroups(String[])
     */
	public String[] getGroups()
	{
		return groups;
	}
	
	/**
     * Sets the list of groups/users.
     * @param attributes String[] the list of groups/users.
     * @see #getGroups()
     */
	public void setGroups(String[] groups)
	{
		this.groups = groups;
	}
	
	/**
     * Returns the list of object types.
     * @return String[] the list of object types.
     * @see #setObjectTypes(String[])
     */
	public String[] getObjectTypes()
	{
		return objectTypes;
	}
	
	/**
     * Sets the list of object types.
     * @param attributes String[] the list of object types.
     * @see #getObjectTypes()
     */
	public void setObjectTypes(String[] objectTypes)
	{
		this.objectTypes = objectTypes;
	}
	
	/**
     * Returns the list of privileges.
     * @return String[] the list of privileges.
     * @see #setPrivileges(String[])
     */
	public String[] getPrivileges()
	{
		return privileges;
	}
	
	/**
     * Sets the list of privileges.
     * @param attributes String[] the list of privileges.
     * @see #getPrivileges()
     */
	public void setPrivileges(String[] privileges)
	{
		this.privileges = privileges;
	}
	
	/**
     * Returns the list of record identifiers.
     * @return String[] the list of record identifiers.
     * @see #setRecordIds(String[])
     */
	public String[] getRecordIds()
	{
		return recordIds;
	}
	
	/**
     * Sets the list of record identifiers.
     * @param attributes String[] the list of record identifiers.
     * @see #getRecordIds()
     */
	public void setRecordIds(String[] recordIds)
	{
		this.recordIds = recordIds;
	}
	
	/**
	 * Overrides the validate method of ActionForm.
	 */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
        ActionErrors errors = new ActionErrors();
        Validator validator = new Validator();
        
        if(assignOperation == null)
        {
        	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("assignPrivileges.assign")));
        }
        
        if(privileges == null)
        {
        	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("assignPrivileges.privileges")));
        }
        
        if(objectTypes == null)
        {
        	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("assignPrivileges.objectTypes")));
        }
        
        if(recordIds == null)
        {
        	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("assignPrivileges.recordIds")));
        }
        
        if(attributes == null)
        {
        	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("assignPrivileges.attributes")));
        }
        
        if(groups == null)
        {
        	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("assignPrivileges.groups")));
        }
        
        return errors;
    }
}