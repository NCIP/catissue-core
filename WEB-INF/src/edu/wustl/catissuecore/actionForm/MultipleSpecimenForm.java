/*
 * Created on Sep 4, 2006
 * @author mandar_deshmukh
 * 
 * MultipleSpecimenForm Class is used to encapsulate all the request parameters passed from Multiple Specimen page.
 * 
 */
package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/**
 * @author mandar_deshmukh
 * @author Rahul Ner.
 * 
 * MultipleSpecimenForm Class is used to encapsulate all the request parameters
 * passed from Multiple Specimen page.
 */
public class MultipleSpecimenForm extends NewSpecimenForm {
	
	/**
	 * Initial No of specimens that user wants to add.
	 */
	private int numberOfSpecimen = 1;
	
	/**
	 * Used to specify which field use has selected in multiple specimen popup.
	 */
	private String specimenAttributeKey = "";


	/**
	 * Overrides the validate method of ActionForm.
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {

		ActionErrors errors = new ActionErrors();
	
 		if(numberOfSpecimen < 1)
        {
        	errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.multiplespecimen.minimumspecimen"));
        }

		return errors;
	}

	
	/**
	 * @return Returns the numberOfSpecimen.
	 */
	public int getNumberOfSpecimen()
	{
		return numberOfSpecimen;
	}

	
	/**
	 * @param numberOfSpecimen The numberOfSpecimen to set.
	 */
	public void setNumberOfSpecimen(int numberOfSpecimen)
	{
		this.numberOfSpecimen = numberOfSpecimen;
	}

	
	/**
	 * @return Returns the specimenAttributeKey.
	 */
	public String getSpecimenAttributeKey()
	{
		return specimenAttributeKey;
	}

	
	/**
	 * @param specimenAttributeKey The specimenAttributeKey to set.
	 */
	public void setSpecimenAttributeKey(String specimenAttributeKey)
	{
		this.specimenAttributeKey = specimenAttributeKey;
	}

}