package edu.wustl.catissuecore.domain.pathology;

/**
 * Represents the concept referent of the pathology report.
 * @hibernate.class
 * table="CATISSUE_CONCEPT_REFERENT"
 */
public class ConceptReferent
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
	protected DeidentifiedSurgicalPathologyReport deidentifiedSurgicalPathologyReport;

	/**
	 * Constructor
	 */
	public ConceptReferent()
	{

	}

	/**
	 * @return concept associated with current concept referent.
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
	public DeidentifiedSurgicalPathologyReport getDeidentifiedSurgicalPathologyReport()
	{
		return deidentifiedSurgicalPathologyReport;
	}

	/**
	 * @param deidentifiedSurgicalPathologyReport sets the deidentified report.
	 */
	public void setDeidentifiedSurgicalPathologyReport(
			DeidentifiedSurgicalPathologyReport deidentifiedSurgicalPathologyReport)
	{
		this.deidentifiedSurgicalPathologyReport = deidentifiedSurgicalPathologyReport;
	}

	/**
	 * @return end offset
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

}