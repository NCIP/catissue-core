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

import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * CancerResearchGroupForm Class is used to encapsulate all the request parameters passed 
 * from CancerResearchGroup Add/Edit webpage.
 * @author Mandar Deshmukh
 * */
public class CancerResearchGroupForm extends AbstractActionForm
{
    
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
        CancerResearchGroup cancerResearchGroup = (CancerResearchGroup)abstractDomain;
        this.id = cancerResearchGroup.getId().longValue();
        this.name = cancerResearchGroup.getName();
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
     * Is called by the overridden reset method defined in ActionForm.  
     * */
    protected void reset()
    {
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
            	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("cancerResearchGroup.name")));
            }    
        }
        catch(Exception excp)
        {
            Logger.out.error(excp.getMessage(),excp);
        }
        return errors;
     }
}