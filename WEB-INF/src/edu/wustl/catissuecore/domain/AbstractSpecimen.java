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
import java.util.Set;

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
	
	private static final long serialVersionUID = 156565234567890L;
	
	/**
	 * System generated unique id.
	 */
	protected Long id;
	/**
	 * Parent specimen from which this specimen is derived. 
	 */
	protected AbstractSpecimen parentSpecimen;

	/**
	 * Collection of children specimens derived from this specimen. 
	 */
	protected Collection<AbstractSpecimen> childSpecimenCollection = new LinkedHashSet<AbstractSpecimen>();

	/**
	 * The combined anatomic state and pathological disease classification of a specimen.
	 */
	protected SpecimenCharacteristics specimenCharacteristics;
	
	/**
	 * Collection of Specimen Event Parameters associated with this specimen. 
	 */
	protected Collection<SpecimenEventParameters> specimenEventCollection = new HashSet<SpecimenEventParameters>();


	/**
	 * Histoathological character of specimen.
	 * e.g. Non-Malignant, Malignant, Non-Malignant Diseased, Pre-Malignant.
	 */
	protected String pathologicalStatus;

	/**
	 * A historical information about the specimen i.e. whether the specimen is a new specimen
	 * or a derived specimen or an aliquot
	 */
	protected String lineage;
	
	/**
	 * A label name of this specimen.
	 */
	protected String label;
	/**
	 * The quantity of a specimen.
	 */
	protected Double initialQuantity;
	
	/**
	 * Tissue, Molecular,Fluid and Cell
	 */
	protected String specimenClass;
	/**
	 * Type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
	 */
	protected String specimenType;
	

	@Override
	public void setAllValues(IValueObject valueObject) throws AssignDataException
	{
		// TODO Auto-generated method stub
		
	}
	public String getActivityStatus()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setActivityStatus(String activityStatus)
	{
		// TODO Auto-generated method stub
		
	}

	public Long getId()
	{
		return id;
	}

	
	public void setId(Long id)
	{
		this.id = id;
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

	
	public String getPathologicalStatus()
	{
		return pathologicalStatus;
	}

	
	public void setPathologicalStatus(String pathologicalStatus)
	{
		this.pathologicalStatus = pathologicalStatus;
	}

	
	public String getLineage()
	{
		return lineage;
	}

	
	public void setLineage(String lineage)
	{
		this.lineage = lineage;
	}
	
	public String getLabel()
	{
		return "";
	}

	public Double getInitialQuantity()
	{
		return initialQuantity;
	}
	
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
	
	public String getSpecimenClass()
	{
		return specimenClass;
	}
	
	public void setSpecimenClass(String specimenClass)
	{
		this.specimenClass = specimenClass;
	}
}