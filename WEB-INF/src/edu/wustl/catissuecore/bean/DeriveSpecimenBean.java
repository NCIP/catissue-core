
package edu.wustl.catissuecore.bean;

import java.io.Serializable;
import java.util.Collection;

public class DeriveSpecimenBean implements Serializable
{

	private static final long serialVersionUID = 1L;

	/**
	 * Type of specimen. e.g. Tissue, Molecular, Cell, Fluid
	 */
	protected String specimenClass;
	/**
	 * Type of specimen. e.g. Tissue, Molecular, Cell, Fluid
	 */
	protected Long id;

	/**
	 * Sub Type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
	 */
	protected String specimenType;

	/**
	 * Virtual , manual and Auto
	 */
	protected String storageLocation;
	/**
	 * Concentration of specimen.
	 */
	protected String concentration;

	/**
	 * Amount of Specimen.
	 */
	protected String quantity;
	/**
	 * Display Name
	 */
	protected String displayName;

	/**
	 * A historical information about the specimen i.e. whether the specimen is a new specimen
	 * or a derived specimen or an aliquot.
	 */
	private String lineage;

	/**
	 * Anatomic site from which the specimen was derived.
	 */
	private String tissueSite;

	/**
	 * For bilateral sites, left or right.
	 */
	private String tissueSide;

	/**
	 * Histopathological character of the specimen 
	 * e.g. Non-Malignant, Malignant, Non-Malignant Diseased, Pre-Malignant.
	 */
	private String pathologicalStatus;

	private String collectionEventCollectionProcedure;

	private String collectionEventContainer;

	private String receivedEventReceivedQuality;

	/**
	* A number that tells how many aliquots to be created.
	*/
	private String noOfAliquots;

	/**
	 * Initial quantity per aliquot.
	 */
	private String quantityPerAliquot;

	/**
	 * Radio Button for Storage Container
	 */
	private String stContSelectionforDerive;

	/**
	 * Collection of aliquot specimens derived from this specimen. 
	 */
	protected Collection aliquotSpecimenCollection;

	/**
	 * Collection of derive specimens derived from this specimen. 
	 */
	protected Collection deriveSpecimenCollection;

	public String getSpecimenClass()
	{
		return this.specimenClass;
	}

	public void setSpecimenClass(String specimenClass)
	{
		this.specimenClass = specimenClass;
	}

	public String getSpecimenType()
	{
		return this.specimenType;
	}

	public void setSpecimenType(String specimenType)
	{
		this.specimenType = specimenType;
	}

	public String getStorageLocation()
	{
		return this.storageLocation;
	}

	public void setStorageLocation(String storageLocation)
	{
		this.storageLocation = storageLocation;
	}

	public String getConcentration()
	{
		return this.concentration;
	}

	public void setConcentration(String concentration)
	{
		this.concentration = concentration;
	}

	public String getQuantity()
	{
		return this.quantity;
	}

	public void setQuantity(String quantity)
	{
		this.quantity = quantity;
	}

	public String getDisplayName()
	{
		return this.displayName;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	public String getLineage()
	{
		return this.lineage;
	}

	public void setLineage(String lineage)
	{
		this.lineage = lineage;
	}

	public String getTissueSite()
	{
		return this.tissueSite;
	}

	public void setTissueSite(String tissueSite)
	{
		this.tissueSite = tissueSite;
	}

	public String getTissueSide()
	{
		return this.tissueSide;
	}

	public void setTissueSide(String tissueSide)
	{
		this.tissueSide = tissueSide;
	}

	public String getPathologicalStatus()
	{
		return this.pathologicalStatus;
	}

	public void setPathologicalStatus(String pathologicalStatus)
	{
		this.pathologicalStatus = pathologicalStatus;
	}

	public String getCollectionEventCollectionProcedure()
	{
		return this.collectionEventCollectionProcedure;
	}

	public void setCollectionEventCollectionProcedure(String collectionEventCollectionProcedure)
	{
		this.collectionEventCollectionProcedure = collectionEventCollectionProcedure;
	}

	public String getCollectionEventContainer()
	{
		return this.collectionEventContainer;
	}

	public void setCollectionEventContainer(String collectionEventContainer)
	{
		this.collectionEventContainer = collectionEventContainer;
	}

	public String getReceivedEventReceivedQuality()
	{
		return this.receivedEventReceivedQuality;
	}

	public void setReceivedEventReceivedQuality(String receivedEventReceivedQuality)
	{
		this.receivedEventReceivedQuality = receivedEventReceivedQuality;
	}

	public String getNoOfAliquots()
	{
		return this.noOfAliquots;
	}

	public void setNoOfAliquots(String noOfAliquots)
	{
		this.noOfAliquots = noOfAliquots;
	}

	public String getQuantityPerAliquot()
	{
		return this.quantityPerAliquot;
	}

	public void setQuantityPerAliquot(String quantityPerAliquot)
	{
		this.quantityPerAliquot = quantityPerAliquot;
	}

	public Collection getAliquotSpecimenCollection()
	{
		return this.aliquotSpecimenCollection;
	}

	public void setAliquotSpecimenCollection(Collection aliquotSpecimenCollection)
	{
		this.aliquotSpecimenCollection = aliquotSpecimenCollection;
	}

	public Collection getDeriveSpecimenCollection()
	{
		return this.deriveSpecimenCollection;
	}

	public void setDeriveSpecimenCollection(Collection deriveSpecimenCollection)
	{
		this.deriveSpecimenCollection = deriveSpecimenCollection;
	}

	public Long getId()
	{
		return this.id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public static long getSerialVersionUID()
	{
		return serialVersionUID;
	}

	public String getStContSelectionforDerive()
	{
		return this.stContSelectionforDerive;
	}

	public void setStContSelectionforDerive(String stContSelectionforDerive)
	{
		this.stContSelectionforDerive = stContSelectionforDerive;
	}
}
