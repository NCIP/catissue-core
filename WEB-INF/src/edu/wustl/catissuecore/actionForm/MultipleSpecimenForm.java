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

import edu.wustl.common.domain.AbstractDomainObject;

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
	 * @see edu.wustl.common.actionForm.AbstractActionForm#setAllVal(java.lang.Object)
	 */
	public void setAllVal(Object arg0) {
//		super.setAllVal(arg0);
	}

	/**
	 * @see edu.wustl.common.actionForm.AbstractActionForm#setAllValues(edu.wustl.common.domain.AbstractDomainObject)
	 */
	public void setAllValues(AbstractDomainObject arg0) {
		// TODO Auto-generated method stub
		//super.setAllValues(arg0);
	}

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