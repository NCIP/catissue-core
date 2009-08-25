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

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 *
 * Description:  This Class handles the thaw event parameters..
 */
public class ThawEventParametersForm extends SpecimenEventParametersForm
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5926826650182436670L;

	/**
	 * @return THAW_EVENT_PARAMETERS_FORM_ID
	 */
	@Override
	public int getFormId()
	{
		return Constants.THAW_EVENT_PARAMETERS_FORM_ID;
	}

	/**
	 * Populates all the fields from the domain object to the form bean.
	 * @param abstractDomain An AbstractDomain Object  
	 */
	@Override
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		super.setAllValues(abstractDomain);
		//ThawEventParameters ThawEventParametersObject = (ThawEventParameters) abstractDomain;
	}

	/**
	 * Resets the values of all the fields.
	 */
	@Override
	protected void reset()
	{
		//	 	super.reset();
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}

}