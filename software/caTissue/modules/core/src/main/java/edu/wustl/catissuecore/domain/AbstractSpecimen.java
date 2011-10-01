package edu.wustl.catissuecore.domain;

import java.util.Collection;
import edu.wustl.common.domain.AbstractDomainObject;

import java.io.Serializable;
/**
	*
	**/

public abstract class AbstractSpecimen extends AbstractDomainObject implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;


	/**
	* System generated identifier.
	**/

	protected Long id;
	/**
	* Retrieves the value of the id attribute
	* @return id
	**/

	public Long getId(){
		return id;
	}

	/**
	* Sets the value of id attribute
	**/

	public void setId(Long id){
		this.id = id;
	}

	/**
	* initialQuantity - The quantity of a specimen.
	**/

	protected Double initialQuantity;
	/**
	* Retrieves the value of the initialQuantity attribute
	* @return initialQuantity
	**/

	public Double getInitialQuantity(){
		return initialQuantity;
	}

	/**
	* Sets the value of initialQuantity attribute
	**/

	public void setInitialQuantity(Double initialQuantity){
		this.initialQuantity = initialQuantity;
	}

	/**
	* label - A label name of this specimen.
	**/

	protected String label;
	/**
	* Retrieves the value of the label attribute
	* @return label
	**/

	public String getLabel(){
		return label;
	}

	/**
	* Sets the value of label attribute
	**/

	public void setLabel(String label){
		this.label = label;
	}

	/**
	* lineage - A historical information about the specimen i.e. whether the specimen is a new specimen or a derived specimen or an aliquot
	**/

	protected String lineage;
	/**
	* Retrieves the value of the lineage attribute
	* @return lineage
	**/

	public String getLineage(){
		return lineage;
	}

	/**
	* Sets the value of lineage attribute
	**/

	public void setLineage(String lineage){
		this.lineage = lineage;
	}

	/**
	* pathologicalStatus - Histoathological character of specimen. e.g. Non-Malignant, Malignant, Non-Malignant Diseased, Pre-Malignant.
	**/

	protected String pathologicalStatus;
	/**
	* Retrieves the value of the pathologicalStatus attribute
	* @return pathologicalStatus
	**/

	public String getPathologicalStatus(){
		return pathologicalStatus;
	}

	/**
	* Sets the value of pathologicalStatus attribute
	**/

	public void setPathologicalStatus(String pathologicalStatus){
		this.pathologicalStatus = pathologicalStatus;
	}

	/**
	* specimenClass - Tissue, Molecular,Fluid and Cell.
	**/

	protected String specimenClass;
	/**
	* Retrieves the value of the specimenClass attribute
	* @return specimenClass
	**/

	public String getSpecimenClass(){
		return specimenClass;
	}

	/**
	* Sets the value of specimenClass attribute
	**/

	public void setSpecimenClass(String specimenClass){
		this.specimenClass = specimenClass;
	}

	/**
	* specimenType - Type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
	**/

	protected String specimenType;
	/**
	* Retrieves the value of the specimenType attribute
	* @return specimenType
	**/

	public String getSpecimenType(){
		return specimenType;
	}

	/**
	* Sets the value of specimenType attribute
	**/

	public void setSpecimenType(String specimenType){
		this.specimenType = specimenType;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.SpecimenCharacteristics object
	**/

	private SpecimenCharacteristics specimenCharacteristics;
	/**
	* Retrieves the value of the specimenCharacteristics attribute
	* @return specimenCharacteristics
	**/

	public SpecimenCharacteristics getSpecimenCharacteristics(){
		return specimenCharacteristics;
	}
	/**
	* Sets the value of specimenCharacteristics attribute
	**/

	public void setSpecimenCharacteristics(SpecimenCharacteristics specimenCharacteristics){
		this.specimenCharacteristics = specimenCharacteristics;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.AbstractSpecimen object's collection
	**/

	private Collection<AbstractSpecimen> childSpecimenCollection;
	/**
	* Retrieves the value of the childSpecimenCollection attribute
	* @return childSpecimenCollection
	**/

	public Collection<AbstractSpecimen> getChildSpecimenCollection(){
		return childSpecimenCollection;
	}

	/**
	* Sets the value of childSpecimenCollection attribute
	**/

	public void setChildSpecimenCollection(Collection<AbstractSpecimen> childSpecimenCollection){
		this.childSpecimenCollection = childSpecimenCollection;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.SpecimenEventParameters object's collection
	**/

	private Collection<SpecimenEventParameters> specimenEventCollection;
	/**
	* Retrieves the value of the specimenEventCollection attribute
	* @return specimenEventCollection
	**/

	public Collection<SpecimenEventParameters> getSpecimenEventCollection(){
		return specimenEventCollection;
	}

	/**
	* Sets the value of specimenEventCollection attribute
	**/

	public void setSpecimenEventCollection(Collection<SpecimenEventParameters> specimenEventCollection){
		this.specimenEventCollection = specimenEventCollection;
	}

	/**
	 * parentSpecimen from which this specimen is derived.
	 */
	protected AbstractSpecimen parentSpecimen;

	public AbstractSpecimen getParentSpecimen()
	{
		return parentSpecimen;
	}


	public void setParentSpecimen(AbstractSpecimen parentSpecimen)
	{
		this.parentSpecimen = parentSpecimen;
	}

	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof AbstractSpecimen)
		{
			AbstractSpecimen c =(AbstractSpecimen)obj;
			if(getId() != null && getId().equals(c.getId()))
				return true;
		}
		return false;
	}

	/**
	* Returns hash code for the primary key of the object
	**/
	public int hashCode()
	{
		if(getId() != null)
			return getId().hashCode();
		return 0;
	}

}