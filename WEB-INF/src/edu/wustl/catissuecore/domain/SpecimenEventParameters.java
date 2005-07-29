/**
 * <p>Title: SpecimenEventParameters Class>
 * <p>Description:  Attributes associated with a specific specimen event. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Apr 7, 2005
 */
package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;

/**
 * Attributes associated with a specific specimen event.
 * @hibernate.class table="CATISSUE_SPECIMEN_EVENT_PARAMETERS"
 * @author aniruddha_phadnis
 */
public abstract class SpecimenEventParameters extends EventParameters
		implements
			java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;
	
	protected Specimen specimen;
	
	/**
     * @hibernate.many-to-one column="SPECIMEN_ID"  class="edu.wustl.catissuecore.domain.Specimen" constrained="true"
	 * @see #setParticipant(Site)
     */
	public Specimen getSpecimen() 
	{
		return specimen;
	}
	
	/**
	 * @param specimen The specimen to set.
	 */
	public void setSpecimen(Specimen specimen) 
	{
		this.specimen = specimen;
	}
	
	
//	/* (non-Javadoc)
//	 * @see edu.wustl.catissuecore.domain.EventParameters#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
//	 */
//	public void setAllValues(AbstractActionForm abstractForm)
//	{
//		super.setAllValues(abstractForm);
//	}
}