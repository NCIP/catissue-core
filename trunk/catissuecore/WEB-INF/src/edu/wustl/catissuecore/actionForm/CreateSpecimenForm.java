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

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * CreateSpecimenForm Class is used to encapsulate all the request parameters passed 
 * from Create Specimen webpage.
 * @author aniruddha_phadnis
 */
public class CreateSpecimenForm extends SpecimenForm implements Cloneable
{   
    /**
     * Identifier of the Parent Speciemen.
     * */
    private String parentSpecimenId;
    
    /**
	 * label of Parent Specimen
	 */
	private String parentSpecimenLabel = "";
	
	 /**
	 * barcode of Parent Specimen
	 */
	private String parentSpecimenBarcode = "";
	
	 /**
	 * Radio button to choose barcode/specimen identifier
	 */
	private String checkedButton = "1";
	
	/**
	 *  Decides the calling of reset
	 */
	private boolean reset = true;
	
	
       
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
        
 
    protected void reset()
    {
    	if (reset == true) 
	    {
        super.reset();
     	this.parentSpecimenId=null;
	    }
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
        Specimen specimen = (Specimen) abstractDomain;
        super.setAllValues(specimen);
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
             	if(request.getParameter("retainForm")==null)
             	{
             	if(this.getCheckedButton().equals("1")) 
             	{
             	 	if (validator.isEmpty(parentSpecimenLabel))
                 {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("createSpecimen.parentLabel")));
                 }
             	} else
             	{
             		if (validator.isEmpty(parentSpecimenBarcode))
                    {
                       errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("createSpecimen.parentBarcode")));
                    }
             	}
             	}
             	
             }
         }
         catch(Exception excp)
         {
             Logger.out.error(excp.getMessage());
         }
         return errors;
      }
	/**
	 * @return Returns the checkedButton.
	 */
	public String getCheckedButton()
	{
		return checkedButton;
	}
	/**
	 * @param checkedButton The checkedButton to set.
	 */
	public void setCheckedButton(String checkedButton)
	{
		this.checkedButton = checkedButton;
	}
	/**
	 * @return Returns the parentSpecimenBarcode.
	 */
	public String getParentSpecimenBarcode()
	{
		return parentSpecimenBarcode;
	}
	/**
	 * @param parentSpecimenBarcode The parentSpecimenBarcode to set.
	 */
	public void setParentSpecimenBarcode(String parentSpecimenBarcode)
	{
		this.parentSpecimenBarcode = parentSpecimenBarcode;
	}
	/**
	 * @return Returns the parentSpecimenLabel.
	 */
	public String getParentSpecimenLabel()
	{
		return parentSpecimenLabel;
	}
	/**
	 * @param parentSpecimenLabel The parentSpecimenLabel to set.
	 */
	public void setParentSpecimenLabel(String parentSpecimenLabel)
	{
		this.parentSpecimenLabel = parentSpecimenLabel;
	}
	/**
	 * @return Returns the reset.
	 */
	public boolean getReset()
	{
		return reset;
	}
	/**
	 * @param reset The reset to set.
	 */
	public void setReset(boolean reset)
	{
		this.reset = reset;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch (CloneNotSupportedException e)
		{
				e.printStackTrace();
		}
		return null;
	}
}