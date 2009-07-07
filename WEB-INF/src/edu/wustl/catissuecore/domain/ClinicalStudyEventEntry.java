
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

	/**
	 * Serial Version Id of the class.
	 */
	private static final long serialVersionUID = 4425303326354875625L;
	/**
	 * entryNumber.
	 */
	protected Integer entryNumber;
	/**
	 * clinicalStudyEvent.
	 */
	protected ClinicalStudyEvent clinicalStudyEvent;
	/**
	 * identifier.
	 */
	protected Long id;

	/**
	 * @return the entryNumber
	 * @hibernate.property name="entryNumber" column="ENTRY_NUMBER" type="int"
	 * length="10"
	 */
	public Integer getEntryNumber()
	{
		return this.entryNumber;
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
	 * @hibernate.many-to-one column="CLINICAL_STUDY_EVENT_ID"
	 * class="edu.wustl.catissuecore.domain.ClinicalStudyEvent"
	 * constrained="true"
	 */
	public ClinicalStudyEvent getClinicalStudyEvent()
	{
		return this.clinicalStudyEvent;
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
	 * @return identifier.
	 */
	@Override
	public Long getId()
	{
		return this.id;
	}

	/**
	 * Set the identifier.
	 * @param identifier unique id.
	 */
	@Override
	public void setId(Long identifier)
	{
		this.id = identifier;

	}

	/**
	 * Set all values.
	 * @param ivalueObject of type IValueObject.
	 * @throws AssignDataException AssignDataException.
	 */
	@Override
	public void setAllValues(IValueObject ivalueObject) throws AssignDataException
	{
		//
	}
}