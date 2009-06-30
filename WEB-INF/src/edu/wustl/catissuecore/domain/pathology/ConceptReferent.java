/**
 * <p>Title: ConceptReferent Class>
 * <p>Description:  ConceptReferent domain object.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on March 07,2007
 */

package edu.wustl.catissuecore.domain.pathology;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * Represents the concept referent of the pathology report.
 * @hibernate.class
 * table="CATISSUE_CONCEPT_REFERENT"
 */
public class ConceptReferent extends AbstractDomainObject
{

	/**
	 * End offset of the concept in the report.
	 */
	protected Long endOffset;

	/**
	 * system generated unique id.
	 */
	protected Long id;

	/**
	 * modifier flag. 
	 */
	protected Boolean isModifier;

	/**
	 * The concept is negated one or not. 
	 */
	protected Boolean isNegated;

	/**
	 * start offset of the concept in the report.
	 */
	protected Long startOffset;

	/**
	 * concept associated with the report.
	 */
	protected Concept concept;

	/**
	 * Concept referent classification associated with the current concept referent.
	 */
	protected ConceptReferentClassification conceptReferentClassification;

	/**
	 * Deidentified report of the current concept referent.
	 */
	protected DeidentifiedSurgicalPathologyReport deIdentifiedSurgicalPathologyReport;

	/**
	 * Constructor
	 */
	public ConceptReferent()
	{

	}

	/**
	 * @return concept associated with current concept referent.
	 * @hibernate.many-to-one column="CONCEPT_ID" class="edu.wustl.catissuecore.domain.pathology.Concept" cascade="save-update"
	 */
	public Concept getConcept()
	{
		return concept;
	}

	/**
	 * @param concept sets concept 
	 */
	public void setConcept(Concept concept)
	{
		this.concept = concept;
	}

	/**
	 * @return concept referent classification
	 * @hibernate.many-to-one class="edu.wustl.catissuecore.domain.pathology.ConceptReferentClassification" column="CONCEPT_CLASSIFICATION_ID" cascade="none"
	 */
	public ConceptReferentClassification getConceptReferentClassification()
	{
		return conceptReferentClassification;
	}

	/**
	 * @param conceptReferentClassification concept referent classification
	 */
	public void setConceptReferentClassification(
			ConceptReferentClassification conceptReferentClassification)
	{
		this.conceptReferentClassification = conceptReferentClassification;
	}

	/**	
	 * @return deidentified report associated with the concept referent. 
	 * @hibernate.many-to-one  name="deidentifiedSurgicalPathologyReport"
	 * class="edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport"
	 * column="DEIDENTIFIED_REPORT_ID" not-null="false"
	 */
	public DeidentifiedSurgicalPathologyReport getDeIdentifiedSurgicalPathologyReport()
	{
		return deIdentifiedSurgicalPathologyReport;
	}

	/**
	 * @param deidentifiedSurgicalPathologyReport sets the deidentified report.
	 */
	public void setDeIdentifiedSurgicalPathologyReport(
			DeidentifiedSurgicalPathologyReport deIdentifiedSurgicalPathologyReport)
	{
		this.deIdentifiedSurgicalPathologyReport = deIdentifiedSurgicalPathologyReport;
	}

	/**
	 * @return end offset
	 * @hibernate.property type="long" column="END_OFFSET" length="30" 
	 */
	public Long getEndOffset()
	{
		return endOffset;
	}

	/**
	 * @param endOffset sets end offset
	 */
	public void setEndOffset(Long endOffset)
	{
		this.endOffset = endOffset;
	}

	/**
	* @return system generated id for concept referent
	* @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	* unsaved-value="null" generator-class="native" 
	* @hibernate.generator-param name="sequence" value="CATISSUE_CONCEPT_REFERENT_SEQ"
	*/
	public Long getId()
	{
		return id;
	}

	/**
	 * @param id sets system generated id
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * @return modifier flag
	 * @hibernate.property type="boolean" length="30" column="IS_MODIFIER"
	 */
	public Boolean getIsModifier()
	{
		return isModifier;
	}

	/**
	 * @param isModifier sets modifier flag
	 */
	public void setIsModifier(Boolean isModifier)
	{
		this.isModifier = isModifier;
	}

	/**
	 * @return negated flag
	 * @hibernate.property type="boolean" length="30" column="IS_NEGATED"
	 */
	public Boolean getIsNegated()
	{
		return isNegated;
	}

	/**
	 * @param isNegated sets negated flag
	 */
	public void setIsNegated(Boolean isNegated)
	{
		this.isNegated = isNegated;
	}

	/**
	 * @return start offset
	 * @hibernate.property type="long" length="30" column="START_OFFSET"
	 */
	public Long getStartOffset()
	{
		return startOffset;
	}

	/**
	 * @param startOffset sets start offset
	 */
	public void setStartOffset(Long startOffset)
	{
		this.startOffset = startOffset;
	}

	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		// TODO Auto-generated method stub

	}

}