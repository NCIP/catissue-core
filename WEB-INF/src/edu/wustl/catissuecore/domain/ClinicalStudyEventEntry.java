/**
 * 
 */
package edu.wustl.catissuecore.domain;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;


/**
 * @author srinivasarao_vassadi
 * @hibernate.class table="CATISSUE_CLIN_STUDY_EVNT_NTRY"
 *
 */
public class ClinicalStudyEventEntry extends AbstractDomainObject
{

	protected Integer entryNumber;
	
	protected ClinicalStudyEvent clinicalStudyEvent;
	
	protected Long id;
	
	
	/**
     * @return the entryNumber
     * @hibernate.property name="entryNumber" column="ENTRY_NUMBER" type="int"
     * length="10"
     */
	public Integer getEntryNumber()
	{
		return entryNumber;
	}

	
	/**
	 * @param entryNumber the entryNumber to set
	 */
	public void setEntryNumber(Integer entryNumber)
	{
		this.entryNumber = entryNumber;
	}

	
	
	/**
     * @return the clinicalStudyEvent
     * @hibernate.many-to-one column="CLINICAL_STUDY_EVENT_ID" class="edu.wustl.catissuecore.domain.ClinicalStudyEvent"
     * constrained="true" 
     */
	public ClinicalStudyEvent getClinicalStudyEvent()
	{
		return clinicalStudyEvent;
	}


	
	/**
	 * @param clinicalStudyEvent the clinicalStudyEvent to set
	 */
	public void setClinicalStudyEvent(ClinicalStudyEvent clinicalStudyEvent)
	{
		this.clinicalStudyEvent = clinicalStudyEvent;
	}
	
	
	/**
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="CATISSUE_STUDY_EVENT_ENTRY_SEQ"
     */
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;

	}   
    public void setAllValues(IValueObject arg0) throws AssignDataException
    {
        // TODO Auto-generated method stub
        
    }

	


	
	

}
