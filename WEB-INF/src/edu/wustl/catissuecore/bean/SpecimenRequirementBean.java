package edu.wustl.catissuecore.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class SpecimenRequirementBean implements Serializable, GenericSpecimen
{
	
	private static final long serialVersionUID = -6804520255061293674L;

	private String uniqueIdentifier;

	/**
	 * Display Name
	 */
	protected String displayName;

	/**
	 * Type of specimen. e.g. Tissue, Molecular, Cell, Fluid
	 */
	protected String className;

	/**
	 * Sub Type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
	 */
	protected String type;

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
    
    
	/**
	 * Concentration of specimen.
	 */
	protected String concentration;

	/**
	 * Amount of Specimen.
	 */
	protected String quantity;
	
	/**
     * A historical information about the specimen i.e. whether the specimen is a new specimen
     * or a derived specimen or an aliquot.
     */
    private String lineage;
    
    private String storageContainerForSpecimen;
    
    private String storageContainerForAliquotSpecimem;
	
	
	private String collectionEventCollectionProcedure;
	
	private String collectionEventContainer;
	
	private String receivedEventReceivedQuality;
	
	
	private long collectionEventId;																											// Mandar : CollectionEvent 10-July-06
	private long collectionEventSpecimenId;
	private long collectionEventUserId;
		
	
	private long receivedEventId;
	private long receivedEventSpecimenId;
	private long receivedEventUserId;
	
	 /**
     * A number that tells how many aliquots to be created.
     */
    private String noOfAliquots;
    
    /**
     * Initial quantity per aliquot.
     */
    private String quantityPerAliquot;
	
	/**
	 * Collection of aliquot specimens derived from this specimen. 
	 */
	protected Map aliquotSpecimenCollection =new LinkedHashMap();
	
	/**
	 * Collection of derive specimens derived from this specimen. 
	 */
	protected Map deriveSpecimenCollection = new LinkedHashMap();
	
	/**
     * Number of biohazard rows.
     */
    private int noOfDeriveSpecimen=1;
    
    private Map deriveSpecimen = new HashMap();
    
	/**
	 * Type of specimen. e.g. Tissue, Molecular, Cell, Fluid
	 */
	protected String deriveClassName;

	/**
	 * Sub Type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
	 */
	protected String deriveType;

	/**
	 * Concentration of specimen.
	 */
	protected String deriveConcentration;

	/**
	 * Amount of Specimen.
	 */
	protected String deriveQuantity;
	
	protected String parentName;

	private long id = -1;
	
	private long specimenCharsId = -1;
	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getClassName()
	{
		return className;
	}

	
	public void setClassName(String className)
	{
		this.className = className;
	}

	
	public String getType()
	{
		return type;
	}

	
	public void setType(String type)
	{
		this.type = type;
	}

	
	public String getTissueSite()
	{
		return tissueSite;
	}

	
	public void setTissueSite(String tissueSite)
	{
		this.tissueSite = tissueSite;
	}

	
	public String getTissueSide()
	{
		return tissueSide;
	}

	
	public void setTissueSide(String tissueSide)
	{
		this.tissueSide = tissueSide;
	}

	
	public String getPathologicalStatus()
	{
		return pathologicalStatus;
	}

	
	public void setPathologicalStatus(String pathologicalStatus)
	{
		this.pathologicalStatus = pathologicalStatus;
	}

	
	public String getConcentration()
	{
		return concentration;
	}

	
	public void setConcentration(String concentration)
	{
		this.concentration = concentration;
	}

	
	public String getQuantity()
	{
		return quantity;
	}

	
	public void setQuantity(String quantity)
	{
		this.quantity = quantity;
	}

	
	public String getLineage()
	{
		return lineage;
	}

	
	public void setLineage(String lineage)
	{
		this.lineage = lineage;
	}

	public String getNoOfAliquots()
	{
		return noOfAliquots;
	}

	
	public void setNoOfAliquots(String noOfAliquots)
	{
		this.noOfAliquots = noOfAliquots;
	}

	
	public String getQuantityPerAliquot()
	{
		return quantityPerAliquot;
	}

	
	public void setQuantityPerAliquot(String quantityPerAliquot)
	{
		this.quantityPerAliquot = quantityPerAliquot;
	}

	
	public String getCollectionEventCollectionProcedure()
	{
		return collectionEventCollectionProcedure;
	}

	
	public void setCollectionEventCollectionProcedure(String collectionEventCollectionProcedure)
	{
		this.collectionEventCollectionProcedure = collectionEventCollectionProcedure;
	}

	
	public String getCollectionEventContainer()
	{
		return collectionEventContainer;
	}

	
	public void setCollectionEventContainer(String collectionEventContainer)
	{
		this.collectionEventContainer = collectionEventContainer;
	}

	
	public String getReceivedEventReceivedQuality()
	{
		return receivedEventReceivedQuality;
	}

	
	public void setReceivedEventReceivedQuality(String receivedEventReceivedQuality)
	{
		this.receivedEventReceivedQuality = receivedEventReceivedQuality;
	}

	
	public int getNoOfDeriveSpecimen()
	{
		return noOfDeriveSpecimen;
	}

	
	public void setNoOfDeriveSpecimen(int noOfDeriveSpecimen)
	{
		this.noOfDeriveSpecimen = noOfDeriveSpecimen;
	}

	
	public Map getDeriveSpecimen()
	{
		return deriveSpecimen;
	}

	
	public void setDeriveSpecimen(Map deriveSpecimen)
	{
		this.deriveSpecimen = deriveSpecimen;
	}

	
	public String getDeriveClassName()
	{
		return deriveClassName;
	}

	
	public void setDeriveClassName(String deriveClassName)
	{
		this.deriveClassName = deriveClassName;
	}

	
	public String getDeriveType()
	{
		return deriveType;
	}

	
	public void setDeriveType(String deriveType)
	{
		this.deriveType = deriveType;
	}

	
	public String getDeriveConcentration()
	{
		return deriveConcentration;
	}

	
	public void setDeriveConcentration(String deriveConcentration)
	{
		this.deriveConcentration = deriveConcentration;
	}

	
	public String getDeriveQuantity()
	{
		return deriveQuantity;
	}

	
	public void setDeriveQuantity(String deriveQuantity)
	{
		this.deriveQuantity = deriveQuantity;
	}

	
	public static long getSerialVersionUID()
	{
		return serialVersionUID;
	}


	
	public LinkedHashMap getAliquotSpecimenCollection()
	{
		return (LinkedHashMap)aliquotSpecimenCollection;
	}


	
	public void setAliquotSpecimenCollection(Map aliquotSpecimenCollection)
	{
		this.aliquotSpecimenCollection = aliquotSpecimenCollection;
	}

	public void addAliquotSpecimenBean(SpecimenRequirementBean specimenRequirementBean)
	{
		aliquotSpecimenCollection.put(specimenRequirementBean.getUniqueIdentifier(), specimenRequirementBean);
	}

	
	
	public LinkedHashMap getDeriveSpecimenCollection()
	{
		return (LinkedHashMap)deriveSpecimenCollection;
	}


	
	public void setDeriveSpecimenCollection(Map deriveSpecimenCollection)
	{
		this.deriveSpecimenCollection = deriveSpecimenCollection;
	}


	
	public String getDisplayName()
	{
		return displayName;
	}


	
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}


	
	public String getStorageContainerForSpecimen()
	{
		return storageContainerForSpecimen;
	}


	
	public void setStorageContainerForSpecimen(String storageContainerForSpecimen)
	{
		this.storageContainerForSpecimen = storageContainerForSpecimen;
	}


	
	public String getStorageContainerForAliquotSpecimem()
	{
		return storageContainerForAliquotSpecimem;
	}


	
	public void setStorageContainerForAliquotSpecimem(String storageContainerForAliquotSpecimem)
	{
		this.storageContainerForAliquotSpecimem = storageContainerForAliquotSpecimem;
	}


	
	public long getCollectionEventId()
	{
		return collectionEventId;
	}


	
	public void setCollectionEventId(long collectionEventId)
	{
		this.collectionEventId = collectionEventId;
	}


	
	public long getCollectionEventSpecimenId()
	{
		return collectionEventSpecimenId;
	}


	
	public void setCollectionEventSpecimenId(long collectionEventSpecimenId)
	{
		this.collectionEventSpecimenId = collectionEventSpecimenId;
	}


	
	public long getCollectionEventUserId()
	{
		return collectionEventUserId;
	}


	
	public void setCollectionEventUserId(long collectionEventUserId)
	{
		this.collectionEventUserId = collectionEventUserId;
	}


	
	public long getReceivedEventId()
	{
		return receivedEventId;
	}


	
	public void setReceivedEventId(long receivedEventId)
	{
		this.receivedEventId = receivedEventId;
	}


	
	public long getReceivedEventSpecimenId()
	{
		return receivedEventSpecimenId;
	}


	
	public void setReceivedEventSpecimenId(long receivedEventSpecimenId)
	{
		this.receivedEventSpecimenId = receivedEventSpecimenId;
	}


	
	public long getReceivedEventUserId()
	{
		return receivedEventUserId;
	}

	public void setReceivedEventUserId(long receivedEventUserId)
	{
		this.receivedEventUserId = receivedEventUserId;
	}
	
	public String getUniqueIdentifier()
	{
		return uniqueIdentifier;
	}

	
	public void setUniqueIdentifier(String uniqueIdentifier)
	{
		this.uniqueIdentifier = uniqueIdentifier;
	}

	public String getParentName()
	{
		return parentName;
	}

	public void setParentName(String parentName)
	{
		this.parentName = parentName;
	}


	public long getSpecimenCharsId() {
		return specimenCharsId;
	}


	public void setSpecimenCharsId(long specimenCharsId) {
		this.specimenCharsId = specimenCharsId;
	}	
}
