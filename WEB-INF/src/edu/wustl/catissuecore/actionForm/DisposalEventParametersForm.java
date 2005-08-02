/**
 * <p>Title: DisposalEventParametersForm Class</p>
 * <p>Description:  This Class handles the frozen event parameters..
 * <p> It extends the EventParametersForm class.    
 * Copyright:    Copyright (c) 2005
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Jyoti Singh
 * @version 1.00
 * Created on July 28th, 2005
 */

package edu.wustl.catissuecore.actionForm;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * @author Jyoti_Singh
 *
 * Description:  This Class handles the Disposal event parameters..
 */
public class DisposalEventParametersForm extends EventParametersForm
{
	/**
     * reason for disposal of specimen it.
     */
	private String reason;

	/**
	 * @return Returns the reason applied on specimen for its Disposal.
	 */
	public String getReason()
	{
		return reason;
	}
	
	/**
	 * @param reason The reason to set.
	 */
	public void setReason(String reason)
	{
		this.reason = reason;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getFormId()
	 */
	public int getFormId()
	{
		return Constants.DISPOSAL_EVENT_PARAMETERS_FORM_ID;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setAllValues(edu.wustl.catissuecore.domain.AbstractDomainObject)
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		super.setAllValues(abstractDomain);
		DisposalEventParameters DisposalEventParametersObject = (DisposalEventParameters)abstractDomain ;
		this.reason = DisposalEventParametersObject.getReason();
		
		//test
		/*System.out.println("\n\n\t\tDate IN fepform: "+ .getDateOfEvent());
		form.getDateOfEvent();
		form.getTimeInHours() ;
		Integer.parseInt(form.getTimeInMinutes()) );*/

		
	}
}