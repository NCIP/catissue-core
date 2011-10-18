
package edu.wustl.catissuecore.bean;

import java.io.Serializable;
import java.util.Collection;
/**
 * DeriveSpecimenBean class.
 * @author sagar_baldwa
 *
 */
public class DeriveSpecimenBean implements Serializable
{
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Type of specimen. e.g. Tissue, Molecular, Cell, Fluid.
	 */
	private String specimenClass;
	/**
	 * Type of specimen. e.g. Tissue, Molecular, Cell, Fluid
	 */
	private Long id;

	/**
	 * Sub Type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
	 */
	private String specimenType;

	/**
	 * Virtual , manual and Auto.
	 */
	private String storageLocation;
	/**
	 * Concentration of specimen.
	 */
	private String concentration;

	/**
	 * Amount of Specimen.
	 */
	private String quantity;
	/**
	 * Display Name.
	 */
	private String displayName;

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
	 * Histopathological character of the specimen.
	 * e.g. Non-Malignant, Malignant, Non-Malignant Diseased, Pre-Malignant.
	 */
	private String pathologicalStatus;
	/**
	 * collectionEventCollectionProcedure.
	 */
	private String collectionEventCollectionProcedure;
	/**
	 * collectionEventContainer.
	 */
	private String collectionEventContainer;
	/**
	 * receivedEventReceivedQuality.
	 */
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
	 * Radio Button for Storage Container.
	 */
	private String stContSelectionforDerive;

	/**
	 * Collection of aliquot specimens derived from this specimen.
	 */
	private Collection<GenericSpecimen> aliquotSpecimenCollection;

	/**
	 * Collection of derive specimens derived from this specimen.
	 */
	private Collection<GenericSpecimen>  deriveSpecimenCollection;


	private String labelFormat;

	private String processingSPP;

	private String creationEvent;


	public String getProcessingSPP() {
		return processingSPP;
	}

	public void setProcessingSPP(String processingSPP) {
		this.processingSPP = processingSPP;
	}

	public String getCreationEvent() {
		return creationEvent;
	}

	public void setCreationEvent(
			String creationEvent) {
		this.creationEvent = creationEvent;
	}

	public String getLabelFormat()
	{
		return labelFormat;
	}

	public void setLabelFormat(String labelFormat)
	{
		this.labelFormat = labelFormat;
	}
	/**
	 * Get SpecimenClass.
	 * @return String
	 */
	public String getSpecimenClass()
	{
		return this.specimenClass;
	}
	/**
	 * Set SpecimenClass.
	 * @param specimenClass String
	 */
	public void setSpecimenClass(String specimenClass)
	{
		this.specimenClass = specimenClass;
	}
	/**
	 * Get SpecimenType.
	 * @return String
	 */
	public String getSpecimenType()
	{
		return this.specimenType;
	}
	/**
	 * Set SpecimenType.
	 * @param specimenType String
	 */
	public void setSpecimenType(String specimenType)
	{
		this.specimenType = specimenType;
	}
	/**
	 * Get StorageLocation.
	 * @return String
	 */
	public String getStorageLocation()
	{
		return this.storageLocation;
	}
	/**
	 * Set StorageLocation.
	 * @param storageLocation String
	 */
	public void setStorageLocation(String storageLocation)
	{
		this.storageLocation = storageLocation;
	}
	/**
	 * Get Concentration.
	 * @return String
	 */
	public String getConcentration()
	{
		return this.concentration;
	}
	/**
	 * Set Concentration.
	 * @param concentration String
	 */
	public void setConcentration(String concentration)
	{
		this.concentration = concentration;
	}
	/**
	 * Get Quantity.
	 * @return String
	 */
	public String getQuantity()
	{
		return this.quantity;
	}
	/**
	 * Set Quantity.
	 * @param quantity String
	 */
	public void setQuantity(String quantity)
	{
		this.quantity = quantity;
	}
	/**
	 * Get DisplayName.
	 * @return String
	 */
	public String getDisplayName()
	{
		return this.displayName;
	}
	/**
	 * Set DisplayName.
	 * @param displayName String
	 */
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
	/**
	 * Get Lineage.
	 * @return String
	 */
	public String getLineage()
	{
		return this.lineage;
	}
	/**
	 * Set Lineage.
	 * @param lineage String
	 */
	public void setLineage(String lineage)
	{
		this.lineage = lineage;
	}
	/**
	 * Get TissueSite.
	 * @return String
	 */
	public String getTissueSite()
	{
		return this.tissueSite;
	}
	/**
	 * Set TissueSite.
	 * @param tissueSite String
	 */
	public void setTissueSite(String tissueSite)
	{
		this.tissueSite = tissueSite;
	}
	/**
	 * Get TissueSide.
	 * @return String
	 */
	public String getTissueSide()
	{
		return this.tissueSide;
	}
	/**
	 * Set TissueSide.
	 * @param tissueSide String
	 */
	public void setTissueSide(String tissueSide)
	{
		this.tissueSide = tissueSide;
	}
	/**
	 * Get PathologicalStatus.
	 * @return String
	 */
	public String getPathologicalStatus()
	{
		return this.pathologicalStatus;
	}
	/**
	 * Set PathologicalStatus.
	 * @param pathologicalStatus String
	 */
	public void setPathologicalStatus(String pathologicalStatus)
	{
		this.pathologicalStatus = pathologicalStatus;
	}
	/**
	 * Get CollectionEventCollectionProcedure.
	 * @return String
	 */
	public String getCollectionEventCollectionProcedure()
	{
		return this.collectionEventCollectionProcedure;
	}
	/**
	 * Set CollectionEventCollectionProcedure.
	 * @param collectionEventCollectionProcedure String
	 */
	public void setCollectionEventCollectionProcedure(String
			collectionEventCollectionProcedure)
	{
		this.collectionEventCollectionProcedure = collectionEventCollectionProcedure;
	}
	/**
	 * Get CollectionEventContainer.
	 * @return String
	 */
	public String getCollectionEventContainer()
	{
		return this.collectionEventContainer;
	}
	/**
	 * Set CollectionEventContainer.
	 * @param collectionEventContainer String
	 */
	public void setCollectionEventContainer(String collectionEventContainer)
	{
		this.collectionEventContainer = collectionEventContainer;
	}
	/**
	 * Get ReceivedEventReceivedQuality.
	 * @return String
	 */
	public String getReceivedEventReceivedQuality()
	{
		return this.receivedEventReceivedQuality;
	}
	/**
	 * Set ReceivedEventReceivedQuality.
	 * @param receivedEventReceivedQuality String
	 */
	public void setReceivedEventReceivedQuality(String receivedEventReceivedQuality)
	{
		this.receivedEventReceivedQuality = receivedEventReceivedQuality;
	}
	/**
	 * Get NoOfAliquots.
	 * @return String.
	 */
	public String getNoOfAliquots()
	{
		return this.noOfAliquots;
	}
	/**
	 * Set NoOfAliquots.
	 * @param noOfAliquots String
	 */
	public void setNoOfAliquots(String noOfAliquots)
	{
		this.noOfAliquots = noOfAliquots;
	}
	/**
	 * Get QuantityPerAliquot.
	 * @return String
	 */
	public String getQuantityPerAliquot()
	{
		return this.quantityPerAliquot;
	}
	/**
	 * Set QuantityPerAliquot.
	 * @param quantityPerAliquot String
	 */
	public void setQuantityPerAliquot(String quantityPerAliquot)
	{
		this.quantityPerAliquot = quantityPerAliquot;
	}
	/**
	 * Get AliquotSpecimenCollection.
	 * @return Collection
	 */
	public Collection<GenericSpecimen>  getAliquotSpecimenCollection()
	{
		return this.aliquotSpecimenCollection;
	}
	/**
	 * Set AliquotSpecimenCollection.
	 * @param aliquotSpecimenCollection aliquotSpecimenCollection.
	 */
	public void setAliquotSpecimenCollection(Collection<GenericSpecimen>  aliquotSpecimenCollection)
	{
		this.aliquotSpecimenCollection = aliquotSpecimenCollection;
	}
	/**
	 * Get DeriveSpecimenCollection.
	 * @return Collection
	 */
	public Collection<GenericSpecimen>  getDeriveSpecimenCollection()
	{
		return this.deriveSpecimenCollection;
	}
	/**
	 * Set DeriveSpecimenCollection.
	 * @param deriveSpecimenCollection Collection
	 */
	public void setDeriveSpecimenCollection(Collection<GenericSpecimen>  deriveSpecimenCollection)
	{
		this.deriveSpecimenCollection = deriveSpecimenCollection;
	}
	/**
	 * Get Id.
	 * @return Long
	 */
	public Long getId()
	{
		return this.id;
	}
	/**
	 * Set Id.
	 * @param id Long
	 */
	public void setId(Long id)
	{
		this.id = id;
	}
	/**
	 * Get SerialVersionUID.
	 * @return long
	 */
	public static long getSerialVersionUID()
	{
		return serialVersionUID;
	}
	/**
	 * Get StContSelectionforDerive.
	 * @return String
	 */
	public String getStContSelectionforDerive()
	{
		return this.stContSelectionforDerive;
	}
	/**
	 * Set StContSelectionforDerive.
	 * @param stContSelectionforDerive String
	 */
	public void setStContSelectionforDerive(String stContSelectionforDerive)
	{
		this.stContSelectionforDerive = stContSelectionforDerive;
	}
}