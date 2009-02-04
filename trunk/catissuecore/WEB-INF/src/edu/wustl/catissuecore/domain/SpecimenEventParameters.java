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

import edu.wustl.catissuecore.actionForm.SpecimenEventParametersForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.logger.Logger;


/**
 * Attributes associated with a specific specimen event.
 * @hibernate.class table="CATISSUE_SPECIMEN_EVENT_PARAM"
 * @author aniruddha_phadnis
 */
public abstract class SpecimenEventParameters extends EventParameters implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;
	
	protected Specimen specimen;
	
	/**
     * Returns System generated unique id.
     * @return System generated unique id.
     * @see #setId(Integer)
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="CATISSUE_SPEC_EVENT_PARAM_SEQ" 
     */
	public Long getId()
	{
		return id;
	}
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
	
	
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.EventParameters#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
	 */
	public void setAllValues(AbstractActionForm abstractForm) throws AssignDataException
	{
		super.setAllValues(abstractForm);
			
		// TODO need to discuss why CellSpecimen is created -- Santosh
		//Temporary fix.
		if(abstractForm.isAddOperation())
		{
			specimen = new Specimen();
		}
		SpecimenEventParametersForm specimenEventParametersForm = (SpecimenEventParametersForm) abstractForm;
		Logger.out.debug("specimenEventParametersForm.getSpecimenId()............................."+specimenEventParametersForm.getSpecimenId());
		if(specimen!=null)
			specimen.setId(new Long(specimenEventParametersForm.getSpecimenId()));
	}
	
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
}