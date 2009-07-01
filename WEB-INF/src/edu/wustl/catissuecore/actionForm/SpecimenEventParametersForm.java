/*
 * Created on Aug 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * @author mandar_deshmukh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class SpecimenEventParametersForm extends EventParametersForm
{

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getFormId()
	 */
	//	public int getFormId()
	//	{
	//		return Constants.SPECIMEN_EVENT_PARAMETERS_FORM_ID;
	//	}
	private long specimenId;

	/**
	 * @return Returns the specimenId.
	 */
	public long getSpecimenId()
	{
		return specimenId;
	}

	/**
	 * @param specimenId The specimenId to set.
	 */
	public void setSpecimenId(long specimenId)
	{
		this.specimenId = specimenId;
	}

	/**
	 * Resets the values of all the fields.
	 */
	protected void reset()
	{
		//	 	super.reset();
		//	 	this.specimenId = -1;
	}

	/**
	  * Populates all the fields from the domain object to the form bean.
	  * @param abstractDomain An AbstractDomain Object  
	  */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		super.setAllValues(abstractDomain);

		SpecimenEventParameters specimenEventParameters = (SpecimenEventParameters) abstractDomain;

		if (specimenEventParameters.getSpecimen() != null)
		{
			specimenId = specimenEventParameters.getSpecimen().getId().longValue();
		}
	}

	/**
	* Overrides the validate method of ActionForm.
	* @return error ActionErrors instance
	* @param mapping Actionmapping instance
	* @param request HttpServletRequest instance
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = super.validate(mapping, request);

		if (specimenId == -1L)
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
					"Specimen Id"));
		}

		return errors;
	}
}