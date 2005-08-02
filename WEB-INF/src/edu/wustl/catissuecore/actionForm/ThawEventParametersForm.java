/**
 * <p>Title: FrozenEventParametersForm Class</p>
 * <p>Description:  This Class handles the frozen event parameters..
 * <p> It extends the EventParametersForm class.    
 * Copyright:    Copyright (c) 2005
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 28th, 2005
 */

package edu.wustl.catissuecore.actionForm;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.ThawEventParameters;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * @author mandar_deshmukh
 *
 * Description:  This Class handles the frozen event parameters..
 */
public class ThawEventParametersForm extends EventParametersForm
{
	
	public int getFormId()
	{
		return Constants.THAW_EVENT_PARAMETERS_FORM_ID;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setAllValues(edu.wustl.catissuecore.domain.AbstractDomainObject)
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		super.setAllValues(abstractDomain);
		ThawEventParameters ThawEventParametersObject = (ThawEventParameters)abstractDomain ;
				//test
		/*System.out.println("\n\n\t\tDate IN fepform: "+ .getDateOfEvent());
		form.getDateOfEvent();
		form.getTimeInHours() ;
		Integer.parseInt(form.getTimeInMinutes()) );*/

		
	}
}