/*
 * Created on Aug 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.actionForm;

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
	
	 protected void reset()
	 {
//	 	super.reset();
//	 	this.specimenId = -1;
	 }
	 
	 public void setAllValues(AbstractDomainObject abstractDomain)
 	 {
	     super.setAllVal(abstractDomain);
		 
	     SpecimenEventParameters specimenEventParameters = (SpecimenEventParameters)abstractDomain;
		 
		 if(specimenEventParameters.getSpecimen()!=null)
		     specimenId = specimenEventParameters.getSpecimen().getId().longValue();
 	 }
	 
	 public void setAllVal(Object obj)
	 {
	     edu.wustl.catissuecore.domainobject.SpecimenEventParameters specimenEventParameters=(edu.wustl.catissuecore.domainobject.SpecimenEventParameters) obj;
	     super.setAllVal(specimenEventParameters);
		 
		 if(specimenEventParameters.getSpecimen()!=null)
		     specimenId = specimenEventParameters.getSpecimen().getId().longValue();
	 }
}