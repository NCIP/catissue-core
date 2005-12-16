/**
 * <p>Title: ThawEventParametersForm Class</p>
 * <p>Description:  This Class handles the thaw event parameters..
 * <p> It extends the EventParametersForm class.    
 * Copyright:    Copyright (c) 2005
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Jyoti Singh
 * @version 1.00
 * Created on Aug 1, 2005
 */

package edu.wustl.catissuecore.actionForm;

import edu.wustl.catissuecore.domain.ThawEventParameters;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 *
 * Description:  This Class handles the thaw event parameters..
 */
public class ThawEventParametersForm extends SpecimenEventParametersForm
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
	}

   
	 protected void reset()
	 {
//	 	super.reset();
 	 }

   
}