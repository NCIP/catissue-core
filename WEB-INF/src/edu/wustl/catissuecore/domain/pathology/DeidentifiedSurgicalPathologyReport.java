package edu.wustl.catissuecore.domain.pathology;
import java.util.Collection;
import java.util.Set;

import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;

/**
 * Represents the deidentified report.
 * @hibernate.joined-subclass
 * table="CATISSUE_DEIDENTIFIED_REPORT" 
 * @hibernate.joined-subclass-key
 *  column="IDENTIFIER"
 */
 
public class DeidentifiedSurgicalPathologyReport extends SurgicalPathologyReport 
{

	/**
	 * Quarantine report flag. 
	 */
	protected String isQuanrantined;
	
	/**
	 * Collection of the quarantine event parameters of the current report.
	 */
	protected Set quarantinEventParameterSetCollection;
	
	/**
	 * collection of concept referents.
	 */
	protected Collection conceptReferentCollection;
	
	/**
	 * Specimen collection group of the current report.
	 */
	protected SpecimenCollectionGroup specimenCollectionGroup;

	/**
	 * Constructor
	 */
	public DeidentifiedSurgicalPathologyReport()
	{

	}

	
	/**
	* @return collection of concept referent. 
	* @hibernate.set inverse="false"
	* table="CATISSUE_CONCEPT_REFERENT" lazy="false" cascade="all" 
	* @hibernate.collection-key  column="DEIDENTIFIED_REPORT_ID"
	* @hibernate.collection-one-to-many
	* class="edu.wustl.catissuecore.domain.pathology.ConceptReferent"
	*/
	public Collection getConceptReferentCollection()
	{
		return conceptReferentCollection;
	}

	/**
	 * @param conceptReferentCollection sets concept referent collection.
	 */
	public void setConceptReferentCollection(
			Collection conceptReferentCollection)
	{
		this.conceptReferentCollection = conceptReferentCollection;
	}

	
	/**
	 * @return quarantine status of the report.
	 * @hibernate.property  name="isQuanrantined"
	 * type="string" column="STATUS"
	 */
	public String getIsQuanrantined() 
	{
		return isQuanrantined;
	}

	/**
	 * @param isQuanrantined sets quarantine status of the report.
	 */
	public void setIsQuanrantined(String isQuanrantined)
	{
		this.isQuanrantined = isQuanrantined;
	}
	
	/**
	* @return quarantine event parameter set collection.
	* @hibernate.set inverse="false" table="CATISSUE_QUARANTINE_PARAMS"
	* lazy="false" cascade="all"
	* @hibernate.collection-key
	* column="DEID_REPORT_ID"
	* @hibernate.collection-one-to-many
	* class="edu.wustl.catissuecore.domain.pathology.QuarantineEventParameter"
	*/
	public Set getQuarantinEventParameterSetCollection()
	{
		return quarantinEventParameterSetCollection;
	}

	/**
	 * @param quarantinEventParameterSetCollection sets quarantine event parameter set.
	 */
	public void setQuarantinEventParameterSetCollection(
			Set quarantinEventParameterSetCollection)
	{
		this.quarantinEventParameterSetCollection = quarantinEventParameterSetCollection;
	}
	
	/**		
	 * @return specimen collection group of the report.
	 * @hibernate.many-to-one  name="specimenCollectionGroup"
	 * class="edu.wustl.catissuecore.domain.SpecimenCollectionGroup"
	 * column="SCG_ID" not-null="false"
	 */
	public SpecimenCollectionGroup getSpecimenCollectionGroup()
	{
		return specimenCollectionGroup;
	}

	/**
	 * @param specimenCollectionGroup sets specimen collection group of the report.
	 */
	public void setSpecimenCollectionGroup(
			SpecimenCollectionGroup specimenCollectionGroup)
	{
		this.specimenCollectionGroup = specimenCollectionGroup;
	}

}