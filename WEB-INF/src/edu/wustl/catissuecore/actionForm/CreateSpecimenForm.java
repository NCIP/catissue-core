/**
 * <p>Title: CreateSpecimenForm Class>
 * <p>Description:  CreateSpecimenForm Class is used to encapsulate all the request parameters passed 
 * from Create Specimen webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 */

package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * CreateSpecimenForm Class is used to encapsulate all the request parameters passed 
 * from Create Specimen webpage.
 * @author aniruddha_phadnis
 */
public class CreateSpecimenForm extends SpecimenForm
{   
    /**
     * Identifier of the Parent Speciemen.
     * */
    private String parentSpecimenId;
       
    /**
     * Returns an identifier of the Parent Speciemen.
     * @return String an identifier of the Parent Speciemen.
     * @see #setParentSpecimenId(String)
     * */
    public String getParentSpecimenId()
    {
        return (this.parentSpecimenId);
    }

    /**
     * Sets an identifier of the Parent Speciemen.
     * @param parentSpecimenId an identifier of the Parent Speciemen.
     * @see #getParentSpecimenId()
     * */
    public void setParentSpecimenId(String parentSpecimenId)
    {
        this.parentSpecimenId = parentSpecimenId;
    }
        
    /**
     * Resets all fields.
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        reset();
    }
   
    protected void reset()
    {
    	super.reset();
    	this.parentSpecimenId=null;
    }
    
    /**
     * Returns the id assigned to form bean.
     */
    public int getFormId()
    {
        return Constants.CREATE_SPECIMEN_FORM_ID;
    }
    
    /**
     * This function Copies the data from an site object to a SiteForm object.
     * @param site An object containing the information about site.  
     */
    public void setAllValues(AbstractDomainObject abstractDomain)
    {
        try
        {
            Specimen specimen = (Specimen) abstractDomain;
            
            super.setAllValues(specimen);
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage(),excp);
        }
    }
    	
	/**
     * Overrides the validate method of ActionForm.
     * */
     public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
     {
         ActionErrors errors = super.validate(mapping,request);
         Validator validator = new Validator();
         
         try
         {
             if (operation.equals(Constants.ADD) || operation.equals(Constants.EDIT))
             {
             	if (parentSpecimenId.equals(Constants.SELECT_OPTION))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("createSpecimen.parent")));
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