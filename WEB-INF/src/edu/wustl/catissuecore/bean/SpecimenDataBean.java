package edu.wustl.catissuecore.bean;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;

import edu.wustl.catissuecore.domain.Quantity;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;

public class SpecimenDataBean implements GenericSpecimen
{
	
	public String uniqueId ;
	protected Long id;

	protected String className;
	/**
	 * Type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
	 */
	protected String type;

	/**
	 * Reference to dimensional position one of the specimen in Storage Container.
	 */
	protected Integer positionDimensionOne;

	/**
	 * Reference to dimensional position two of the specimen in Storage Container.
	 */
	protected Integer positionDimensionTwo;

	/**
	 * Barcode assigned to the specimen.
	 */
	protected String barcode;

	/**
	 * Comment on specimen.
	 */
	protected String comment;

	/**
	 * Parent specimen from which this specimen is derived. 
	 */
	protected Specimen parentSpecimen;

	/**
	 * Collection of attributes of a Specimen that renders it potentially harmful to a User.
	 */
	protected Collection biohazardCollection = new HashSet();

	
	/**
	 * Collection of Specimen Event Parameters associated with this specimen. 
	 */
	protected Collection specimenEventCollection = new HashSet();

	/**
	 * Collection of a pre-existing, externally defined id associated with a specimen.
	 */
	protected Collection externalIdentifierCollection = new HashSet();

	/**
	 * An event that results in the collection of one or more specimen from a participant.
	 */
	protected SpecimenCollectionGroup specimenCollectionGroup;

	protected String pathologicalStatus;
	protected String tissueSite ;
	protected String tissueSide;
	/**
	 * A historical information about the specimen i.e. whether the specimen is a new specimen
	 * or a derived specimen or an aliquot.
	 */
	protected String lineage;

	/**
	 * A label name of this specimen.
	 */
	protected String label;

	//Change for API Search   --- Ashwin 04/10/2006
	/**
	 * The quantity of a specimen.
	 */
	protected Quantity initialQuantity;
	
	protected String concentration;

	
	public LinkedHashMap <String,GenericSpecimen> deriveSpecimenCollection = new LinkedHashMap<String,GenericSpecimen>();
	
	/* getters and setter for attributes */
	/**
	 * @return Returns the barcode.
	 */
	public String getBarcode()
	{
		return barcode;
	}

	/**
	 * @param barcode The barcode to set.
	 */
	public void setBarcode(String barcode)
	{
		this.barcode = barcode;
	}

	/**
	 * @return Returns the biohazardCollection.
	 */
	public Collection getBiohazardCollection()
	{
		return biohazardCollection;
	}

	/**
	 * @param biohazardCollection The biohazardCollection to set.
	 */
	public void setBiohazardCollection(Collection biohazardCollection)
	{
		this.biohazardCollection = biohazardCollection;
	}

	/**
	 * @return Returns the className.
	 */
	public String getClassName()
	{
		return className;
	}

	/**
	 * @param className The className to set.
	 */
	public void setClassName(String className)
	{
		this.className = className;
	}

	/**
	 * @return Returns the comment.
	 */
	public String getComment()
	{
		return comment;
	}

	/**
	 * @param comment The comment to set.
	 */
	public void setComment(String comment)
	{
		this.comment = comment;
	}

	/**
	 * @return Returns the concentration.
	 */
	public String getConcentration()
	{
		return concentration;
	}

	/**
	 * @param concentration The concentration to set.
	 */
	public void setConcentration(String concentration)
	{
		this.concentration = concentration;
	}

	/**
	 * @return Returns the externalIdentifierCollection.
	 */
	public Collection getExternalIdentifierCollection()
	{
		return externalIdentifierCollection;
	}

	/**
	 * @param externalIdentifierCollection The externalIdentifierCollection to set.
	 */
	public void setExternalIdentifierCollection(Collection externalIdentifierCollection)
	{
		this.externalIdentifierCollection = externalIdentifierCollection;
	}

	/**
	 * @return Returns the id.
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * @return Returns the initialQuantity.
	 */
	public Quantity getInitialQuantity()
	{
		return initialQuantity;
	}

	/**
	 * @param initialQuantity The initialQuantity to set.
	 */
	public void setInitialQuantity(Quantity initialQuantity)
	{
		this.initialQuantity = initialQuantity;
	}

	/**
	 * @return Returns the label.
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * @param label The label to set.
	 */
	public void setLabel(String label)
	{
		this.label = label;
	}

	/**
	 * @return Returns the lineage.
	 */
	public String getLineage()
	{
		return lineage;
	}

	/**
	 * @param lineage The lineage to set.
	 */
	public void setLineage(String lineage)
	{
		this.lineage = lineage;
	}

	/**
	 * @return Returns the parentSpecimen.
	 */
	public Specimen getParentSpecimen()
	{
		return parentSpecimen;
	}

	/**
	 * @param parentSpecimen The parentSpecimen to set.
	 */
	public void setParentSpecimen(Specimen parentSpecimen)
	{
		this.parentSpecimen = parentSpecimen;
	}

	/**
	 * @return Returns the pathologicalStatus.
	 */
	public String getPathologicalStatus()
	{
		return pathologicalStatus;
	}

	/**
	 * @param pathologicalStatus The pathologicalStatus to set.
	 */
	public void setPathologicalStatus(String pathologicalStatus)
	{
		this.pathologicalStatus = pathologicalStatus;
	}

	/**
	 * @return Returns the positionDimensionOne.
	 */
	public Integer getPositionDimensionOne()
	{
		return positionDimensionOne;
	}

	/**
	 * @param positionDimensionOne The positionDimensionOne to set.
	 */
	public void setPositionDimensionOne(Integer positionDimensionOne)
	{
		this.positionDimensionOne = positionDimensionOne;
	}

	/**
	 * @return Returns the positionDimensionTwo.
	 */
	public Integer getPositionDimensionTwo()
	{
		return positionDimensionTwo;
	}

	/**
	 * @param positionDimensionTwo The positionDimensionTwo to set.
	 */
	public void setPositionDimensionTwo(Integer positionDimensionTwo)
	{
		this.positionDimensionTwo = positionDimensionTwo;
	}

	/**
	 * @return Returns the specimenCollectionGroup.
	 */
	public SpecimenCollectionGroup getSpecimenCollectionGroup()
	{
		return specimenCollectionGroup;
	}

	/**
	 * @param specimenCollectionGroup The specimenCollectionGroup to set.
	 */
	public void setSpecimenCollectionGroup(SpecimenCollectionGroup specimenCollectionGroup)
	{
		this.specimenCollectionGroup = specimenCollectionGroup;
	}

	/**
	 * @return Returns the specimenEventCollection.
	 */
	public Collection getSpecimenEventCollection()
	{
		return specimenEventCollection;
	}

	/**
	 * @param specimenEventCollection The specimenEventCollection to set.
	 */
	public void setSpecimenEventCollection(Collection specimenEventCollection)
	{
		this.specimenEventCollection = specimenEventCollection;
	}

	/**
	 * @return Returns the tissueSide.
	 */
	public String getTissueSide()
	{
		return tissueSide;
	}

	/**
	 * @param tissueSide The tissueSide to set.
	 */
	public void setTissueSide(String tissueSide)
	{
		this.tissueSide = tissueSide;
	}

	/**
	 * @return Returns the tissueSite.
	 */
	public String getTissueSite()
	{
		return tissueSite;
	}

	/**
	 * @param tissueSite The tissueSite to set.
	 */
	public void setTissueSite(String tissueSite)
	{
		this.tissueSite = tissueSite;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(String type)
	{
		this.type = type;
	}
	
	/**
	 * @return Returns the deriveSpecimenCollection.
	 */
	public LinkedHashMap<String, GenericSpecimen> getDeriveSpecimenCollection()
	{
		return deriveSpecimenCollection;
	}

	/**
	 * @param deriveSpecimenCollection The deriveSpecimenCollection to set.
	 */
	public void setDeriveSpecimenCollection(LinkedHashMap<String, GenericSpecimen> deriveSpecimenCollection)
	{
		this.deriveSpecimenCollection = deriveSpecimenCollection;
	}
	
	/* getters and setter for attributes finish */

	
	

	/* overide methods */
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getAliquotSpecimenCollection()
	 */
	public LinkedHashMap<String, GenericSpecimen> getAliquotSpecimenCollection()
	{
		// TODO Auto-generated method stub
		return null;
	}

	
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getDisplayName()
	 */
	public String getDisplayName()
	{
		// TODO Auto-generated method stub
		return this.label;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getParentName()
	 */
	public String getParentName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getQuantity()
	 */
	public String getQuantity()
	{
		// TODO Auto-generated method stub
		return this.initialQuantity.toString();
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getStorageContainerForSpecimen()
	 */
	public String getStorageContainerForSpecimen()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getUniqueIdentifier()
	 */
	public String getUniqueIdentifier()
	{
		// TODO Auto-generated method stub
		return this.uniqueId;
	}
	
	
	/* override methods finish */



		
}
