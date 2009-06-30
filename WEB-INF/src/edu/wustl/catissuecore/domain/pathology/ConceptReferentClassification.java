/**
 * <p>Title: ConceptReferentClassification Class>
 * <p>Description:  ConceptReferentClassification domain object.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on March 07,2007
 */

package edu.wustl.catissuecore.domain.pathology;

import java.util.Collection;
import java.util.HashSet;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * Represents concept referent classification
 * @hibernate.class table="CATISSUE_CONCEPT_CLASSIFICATN"
 */
public class ConceptReferentClassification extends AbstractDomainObject
{

	/**
	 * System generated unique id
	 */
	protected Long id;

	/**
	 * name od the classification
	 */
	protected String name;

	/**
	 * collection of concept referent
	 */
	protected Collection conceptReferentCollection = new HashSet();

	/**
	 * Constructor
	 */
	public ConceptReferentClassification()
	{

	}

	/**
	 * @return collection of concept referent
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.pathology.ConceptReferent"
	 * @hibernate.set table="CATISSUE_CONCEPT_REFERENT" cascade="save-update"
	 * @hibernate.collection-key column="CONCEPT_CLASSIFICATION_ID"
	 */
	public Collection getConceptReferentCollection()
	{
		return conceptReferentCollection;
	}

	/**
	 * @param conceptReferentCollection sets concept referent collection
	 */
	public void setConceptReferentCollection(Collection conceptReferentCollection)
	{
		this.conceptReferentCollection = conceptReferentCollection;
	}

	/**
	 * @return system generated id
	 * @hibernate.id type="long" length="30" column="IDENTIFIER" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_CONCEPT_CLASSFCTN_SEQ"
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
	 * @return name of referent classification 
	 * @hibernate.property type="string" length="500" column="NAME"
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name sets name of the referent classification
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		// TODO Auto-generated method stub

	}

}