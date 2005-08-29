/*
 * Created on Aug 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.actionForm;

import edu.wustl.catissuecore.util.global.Constants;


/**
 * @author mandar_deshmukh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SpecimenEventParametersForm extends EventParametersForm
{

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getFormId()
	 */
	public int getFormId()
	{
		return Constants.SPECIMEN_EVENT_PARAMETERS_FORM_ID;
	}

	private long specimenID=1;
	
	
	/**
	 * @return Returns the specimenID.
	 */
	public long getSpecimenID()
	{
		return specimenID;
	}
	/**
	 * @param specimenID The specimenID to set.
	 */
	public void setSpecimenID(long specimenID)
	{
		this.specimenID = specimenID;
	}
	
	 protected void reset()
	 {
	 	super.reset();
	 	this.specimenID = 1;
	 }
	 
	
}
