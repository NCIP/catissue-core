/**
 * <p>Title:SpunEventParametersForm Class</p>
 * <p>Description:  This Class handles the Spun event parameters..
 * <p> It extends the EventParametersForm class.    
 * Copyright:    Copyright (c) 2005
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Jyoti Singh
 * @version 1.00
 * Created on July 28th, 2005
 */

package edu.wustl.catissuecore.actionForm;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.SpunEventParameters;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * @author Jyoti_Singh
 *
 * Description:  This Class handles the Spun event parameters..
 */
public class SpunEventParametersForm extends EventParametersForm
{
	
	private Double gForce;
	private Integer durationInMinutes;

	public Double getgForce()
	{
		return gForce;
	}
	public Integer getdurationInMinutes()
	{
		return durationInMinutes;
	}
	
	
	public void setGForce(Double gForce)
	{
		this.gForce = gForce;
	}
	public void setDurationInMinutes(Integer durationInMinutes)
		{
			this.durationInMinutes = durationInMinutes;
		}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getFormId()
	 */
	public int getFormId()
	{
		return Constants.SPUN_EVENT_PARAMETERS_FORM_ID;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setAllValues(edu.wustl.catissuecore.domain.AbstractDomainObject)
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		super.setAllValues(abstractDomain);
		SpunEventParameters SpunEventParametersObject = (SpunEventParameters)abstractDomain ;
		this.gForce = SpunEventParametersObject.getGForce();
		this.durationInMinutes = SpunEventParametersObject.getDurationInMinutes();
		
		//test
		/*System.out.println("\n\n\t\tDate IN fepform: "+ .getDateOfEvent());
		form.getDateOfEvent();
		form.getTimeInHours() ;
		Integer.parseInt(form.getTimeInMinutes()) );*/

		
	}
}