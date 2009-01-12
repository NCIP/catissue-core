/**
 * <p>Title: AbstractSpecimen Class>
 * <p>Description:  A single unit of tissue, body fluid, or derivative
 *                  biological macromolecule that is collected or created from a Participant </p>
 * Company: Washington University, School of Medicine, St. Louis.
 * @version caTissueSuite V1.1
 */
package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.bizlogic.IActivityStatus;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @hibernate.class table="CATISSUE_ABSTRACT_SPECIMEN"
 * @author virender_mehta
 */

public abstract class AbstractSpecimen extends AbstractDomainObject implements Serializable, IActivityStatus
{
	/**
	 * This is the serial version ID generated for the class.
	 */
	private static final long serialVersionUID = 156565234567890L;

	/**
	 * System generated identifier.
	 */
	protected Long id;
	/**
	 * parentSpecimen from which this specimen is derived.
	 */
	protected AbstractSpecimen parentSpecimen;
	/**
	 * Collection of childSpecimenCollection derived from this specimen.
	 */
	protected Collection<AbstractSpecimen> childSpecimenCollection = new LinkedHashSet<AbstractSpecimen>();
	/**
	 * The combined specimenCharacteristics of anatomic state and pathological
	 * disease classification of a specimen.
	 */
	protected SpecimenCharacteristics specimenCharacteristics;
	/**
	 * Collection of Specimen Event Parameters associated with this specimen.
	 */
	protected Collection<SpecimenEventParameters> specimenEventCollection =
		new HashSet<SpecimenEventParameters>();
	/**
	 * pathologicalStatus - Histoathological character of specimen.
	 * e.g. Non-Malignant, Malignant, Non-Malignant Diseased, Pre-Malignant.
	 */
	protected String pathologicalStatus;
	/**
	 * lineage - A historical information about the specimen i.e. whether the specimen is a new specimen
	 * or a derived specimen or an aliquot
	 */
	protected String lineage;
	/**
	 * label - A label name of this specimen.
	 */
	protected String label;
	/**
	 * initialQuantity - The quantity of a specimen.
	 */
	protected Double initialQuantity;
	/**
	 * specimenClass - Tissue, Molecular,Fluid and Cell.
	 */
	protected String specimenClass;
	/**
	 * specimenType - Type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
	 */
	protected String specimenType;
	/**
	 * Overidden from AbstractDomainObject class.
	 * @param valueObject IValueObject.
	 * @throws AssignDataException assignDataException.
	 */
	public void setAllValues(IValueObject valueObject) throws AssignDataException
	{}
	/**
	 * It would return the Activity Status.
	 * @return activity status of String type.
	 */
	public String getActivityStatus()
	{
		return null;
	}
	/**
	 * Abstract method which would set the Activity Status.
	 * @param activityStatus of String type
	 */
	public void setActivityStatus(String activityStatus)
	{}
	/**
	 * It returns the identifier.
	 * @return identifier of type Long.
	 */
	public Long getId()
	{
		return id;
	}
	/**
	 * Set the identifier.
	 * @param identifier which is of Long type.
	 */
	public void setId(Long identifier)
	{
		this.id = identifier;
	}

	/**
	 * Returns the combined anatomic state and pathological disease classification of a specimen.
	 * @hibernate.many-to-one column="SPECIMEN_CHARACTERISTICS_ID"
	 * class="edu.wustl.catissuecore.domain.SpecimenCharacteristics" constrained="true"
	 * @return the combined anatomic state and pathological disease classification of a specimen.
	 * @see #setSpecimenCharacteristics(SpecimenCharacteristics)
	 */
	public SpecimenCharacteristics getSpecimenCharacteristics()
	{
		return specimenCharacteristics;
	}

	/**
	 * Sets the combined anatomic state and pathological disease classification of a specimen.
	 * @param specimenCharacteristics the combined anatomic state and pathological disease 
	 * classification of a specimen.
	 * @see #getSpecimenCharacteristics()
	 */
	public void setSpecimenCharacteristics(SpecimenCharacteristics specimenCharacteristics)
	{
		this.specimenCharacteristics = specimenCharacteristics;
	}

	/**
	 * Get the pathological status.
	 * @return pathological status in String type.
	 */
	public String getPathologicalStatus()
	{
		return pathologicalStatus;
	}

	/**
	 * Set the pathological status.
	 * @param pathologicalStatus of type String.
	 */
	public void setPathologicalStatus(String pathologicalStatus)
	{
		this.pathologicalStatus = pathologicalStatus;
	}

	/**
	 * Get the lineage.
	 * @return String.
	 */
	public String getLineage()
	{
		return lineage;
	}

	/**
	 * Set the lineage.
	 * @param lineage of type String.
	 */
	public void setLineage(String lineage)
	{
		this.lineage = lineage;
	}

	/**
	 * Get the label.
	 * @return label of String type.
	 */
	public String getLabel()
	{
		return Constants.DOUBLE_QUOTES;
	}

	/**
	 * Get the initial quantity.
	 * @return initial quantity in double.
	 */
	public Double getInitialQuantity()
	{
		return initialQuantity;
	}

	/**
	 * Set the initial quantity.
	 * @param initialQuantity which is of Double type.
	 */
	public void setInitialQuantity(Double initialQuantity)
	{
		this.initialQuantity = initialQuantity;
	}

	/**
	 * Returns the type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
	 * @return The type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
	 * @see #getType(String)
	 */
	public String getSpecimenType()
	{
		return specimenType;
	}

	/**
	 * Set the specimen type.
	 * @param specimenType SpecimenType)
	 */
	public void setSpecimenType(String specimenType)
	{
		this.specimenType = specimenType;
	}

	/**
	 * Returns the parent specimen from which this specimen is derived.
	 * @hibernate.many-to-one column="PARENT_SPECIMEN_ID"
	 * class="edu.wustl.catissuecore.domain.Specimen" constrained="true"
	 * @return the parent specimen from which this specimen is derived.
	 * @see #setParentSpecimen(SpecimenNew)
	 */
	public AbstractSpecimen getParentSpecimen()
	{
		return parentSpecimen;
	}

	/**
	 * Sets the parent specimen from which this specimen is derived.
	 * @param parentSpecimen the parent specimen from which this specimen is derived.
	 * @see #getParentSpecimen()
	 */
	public void setParentSpecimen(AbstractSpecimen parentSpecimen)
	{
		this.parentSpecimen = parentSpecimen;
	}

	/**
	 * Returns the collection of children specimens derived from this specimen.
	 * @hibernate.set name="childrenSpecimen" table="CATISSUE_SPECIMEN"
	 * cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="PARENT_SPECIMEN_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.Specimen"
	 * @return the collection of children specimens derived from this specimen.
	 * @see #setChildrenSpecimen(Set)
	 */
	public Collection<AbstractSpecimen> getChildSpecimenCollection()
	{
		return childSpecimenCollection;
	}

	/**
	 * Sets the collection of children specimens derived from this specimen.
	 * @param childrenSpecimen the collection of children specimens
	 * derived from this specimen.
	 * @see #getChildrenSpecimen()
	 */
	public void setChildSpecimenCollection(Collection<AbstractSpecimen> childrenSpecimen)
	{
		this.childSpecimenCollection = childrenSpecimen;
	}
	/**
	 * Returns the collection of Specimen Event Parameters associated with this specimen.
	 * @hibernate.set name="specimenEventCollection" table="CATISSUE_SPECIMEN_EVENT"
	 * cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="SPECIMEN_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.SpecimenEventParameters"
	 * @return the collection of Specimen Event Parameters associated with this specimen.
	 * @see #setSpecimenEventCollection(Set)
	 */
	public Collection<SpecimenEventParameters> getSpecimenEventCollection()
	{
		return specimenEventCollection;
	}

	/**
	 * Sets the collection of Specimen Event Parameters associated with this specimen.
	 * @param specimenEventCollection the collection of Specimen Event Parameters
	 * associated with this specimen.
	 * @see #getSpecimenEventCollection()
	 */
	public void setSpecimenEventCollection(Collection specimenEventCollection)
	{
		this.specimenEventCollection = specimenEventCollection;
	}

	/**
	 * This function returns the actual type of the specimen i.e Cell / Fluid / Molecular / Tissue.
	 * @return String className.
	 */
	public final String getClassName()
	{
		String className = null;

		if (this instanceof CellSpecimen || this instanceof CellSpecimenRequirement)
		{
			className = Constants.CELL;
		}
		else if (this instanceof MolecularSpecimen || this instanceof MolecularSpecimenRequirement)
		{
			className = Constants.MOLECULAR;
		}
		else if (this instanceof FluidSpecimen|| this instanceof FluidSpecimenRequirement)
		{
			className = Constants.FLUID;
		}
		else if (this instanceof TissueSpecimen || this instanceof TissueSpecimenRequirement)
		{
			className = Constants.TISSUE;
		}
		return className;
	}

	/**
	 * Get the specimen class.
	 * @return String type "Specimen Class".
	 */
	public String getSpecimenClass()
	{
		return specimenClass;
	}

	/**
	 * Set the specimen class.
	 * @param specimenClass SpecimenClass.
	 */
	public void setSpecimenClass(String specimenClass)
	{
		this.specimenClass = specimenClass;
	}
}