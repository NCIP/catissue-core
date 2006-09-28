/**
 * <p>Title: CollectionEventParametersForm Class</p>
 * <p>Description:  This Class handles the Collection Event Parameters.
 * <p> It extends the EventParametersForm class.    
 * Copyright:    Copyright (c) 2005
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on Aug 05, 2005
 */
package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;


/**
 * @author mandar_deshmukh
 *
 * This Class handles the Collection Event Parameters.
 */
public class CollectionEventParametersForm extends SpecimenEventParametersForm
{
	/**	
	 *	Method of specimen collection from participant (e.g. needle biopsy, central venous line, bone marrow aspiration)
	 */
	protected String collectionProcedure;
	
	/**
	 * Container type in which specimen is collected (e.g. clot tube, KEDTA, ACD, sterile specimen cup)
	 */
	protected String container;

	/**
	 * Returns the procedure of collection.
	 * @return procedure of collection.
	 */
	public String getCollectionProcedure()
	{
		return collectionProcedure;
	}

	/**
	 * Sets the procedure. 
	 * @param procedure procedure of collection.
	 */
	public void setCollectionProcedure(String collectionProcedure)
	{
		this.collectionProcedure = collectionProcedure;
	}

	/**
	 * Returns the container type used for collecting the specimen.
	 * @return container type used for collecting the specimen. 
	 */
	public String getContainer()
	{
		return container;
	}

	/**
	 * Sets the container 
	 * @param container container type used for collecting the specimen.
	 */
	public void setContainer(String container)
	{
		this.container = container;
	}
	
	
	
//	 ---- super class methods
	// ----- SUPERCLASS METHODS
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getFormId()
	 */
	public int getFormId()
	{
		return Constants.COLLECTION_EVENT_PARAMETERS_FORM_ID;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setAllValues(edu.wustl.catissuecore.domain.AbstractDomainObject)
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		super.setAllValues(abstractDomain);
		CollectionEventParameters collectionEventParameterObject = (CollectionEventParameters)abstractDomain ;
		this.collectionProcedure =  Utility.toString(collectionEventParameterObject.getCollectionProcedure()) ;
		this.container = Utility.toString(collectionEventParameterObject.getContainer());
	}
	
	
	/**
	 * Overrides the validate method of ActionForm.
     * */
     public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
     {
     	 
         ActionErrors errors = super.validate(mapping, request);
         Validator validator = new Validator();
         
         try
         {
         	
         	// checks the collectionProcedure
          	if (!validator.isValidOption( collectionProcedure ) )
            {
           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectioneventparameters.collectionprocedure")));
            }
           	
         }
         catch(Exception excp)
         {
             Logger.out.error(excp.getMessage());
         }
         return errors;
      }
	
  
     protected void reset()
     {
//     	super.reset();
//        this.collectionProcedure = null;
//        this.container = null;     	
     }

	
}
