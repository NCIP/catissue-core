
package edu.wustl.catissuecore.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.wustl.catissuecore.domain.Specimen;

// TODO: Auto-generated Javadoc
/**
 * The Class SpecimenRequirementBean.
 */
public class SpecimenRequirementBean implements Serializable, GenericSpecimen
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6804520255061293674L;

	/** The unique identifier. */
	private String uniqueIdentifier;

	/** Display Name. */
	protected String displayName;

	/** Type of specimen. e.g. Tissue, Molecular, Cell, Fluid */
	protected String className;

	/** Sub Type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc. */
	protected String type;

	/** Anatomic site from which the specimen was derived. */
	private String tissueSite;

	/** For bilateral sites, left or right. */
	private String tissueSide;

	/** Histopathological character of the specimen e.g. Non-Malignant, Malignant, Non-Malignant Diseased, Pre-Malignant. */
	private String pathologicalStatus;

	/** Concentration of specimen. */
	protected String concentration;

	/** Amount of Specimen. */
	protected String quantity;

	/** A historical information about the specimen i.e. whether the specimen is a new specimen or a derived specimen or an aliquot. */
	private String lineage;

	/** The storage container for specimen. */
	private String storageContainerForSpecimen;

	/** The storage container for aliquot specimem. */
	private String storageContainerForAliquotSpecimem;

	/** The collection event collection procedure. */
	private String collectionEventCollectionProcedure;

	/** The collection event container. */
	private String collectionEventContainer;

	/** The received event received quality. */
	private String receivedEventReceivedQuality;

	/** The collection event id. */
	private long collectionEventId; // Mandar : CollectionEvent 10-July-06

	/** The collection event specimen id. */
	private long collectionEventSpecimenId;

	/** The collection event user id. */
	private long collectionEventUserId;

	/** The received event id. */
	private long receivedEventId;

	/** The received event specimen id. */
	private long receivedEventSpecimenId;

	/** The received event user id. */
	private long receivedEventUserId;

	/** A number that tells how many aliquots to be created. */
	private String noOfAliquots;

	/** Initial quantity per aliquot. */
	private String quantityPerAliquot;

	/** Collection of aliquot specimens derived from this specimen. */
	protected Map<String, GenericSpecimen> aliquotSpecimenCollection = new LinkedHashMap<String, GenericSpecimen>();

	/** Collection of derive specimens derived from this specimen. */
	protected Map<String, GenericSpecimen> deriveSpecimenCollection = new LinkedHashMap<String, GenericSpecimen>();

	/** The no of derive specimen. */
	private int noOfDeriveSpecimen = 0;

	/** The derive specimen. */
	private Map<String, GenericSpecimen> deriveSpecimen = new HashMap<String, GenericSpecimen>();

	/** Type of specimen. e.g. Tissue, Molecular, Cell, Fluid */
	protected String deriveClassName;

	/** Sub Type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc. */
	protected String deriveType;

	/** Concentration of specimen. */
	protected String deriveConcentration;

	/** Amount of Specimen. */
	protected String deriveQuantity;

	/** The parent name. */
	protected String parentName;

	/** The id. */
	private long id = -1;

	/** The specimen chars id. */
	private long specimenCharsId = -1;

	/** The form specimen vo. */
	private GenericSpecimen formSpecimenVo;

	/** The corres specimen. */
	private Specimen corresSpecimen;

	/** The show barcode. */
	private boolean showBarcode = true;

	/** The show label. */
	private boolean showLabel = true;

	/** The generate label. */
	private boolean generateLabel = false;



	/** The label format for aliquot. */
	private String labelFormatForAliquot;

	/**
	 * Gets the label format for aliquot.
	 *
	 * @return the label format for aliquot
	 */
	public String getLabelFormatForAliquot()
	{
		return labelFormatForAliquot;
	}

	/**
	 * Sets the label format for aliquot.
	 *
	 * @param labelFormatForAliquot the new label format for aliquot
	 */
	public void setLabelFormatForAliquot(String labelFormatForAliquot)
	{
		this.labelFormatForAliquot = labelFormatForAliquot;
	}


	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#isGenerateLabel()
	 */
	public boolean isGenerateLabel()
	{
		return generateLabel;
	}


	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setGenerateLabel(boolean)
	 */
	public void setGenerateLabel(boolean generateLabel)
	{
		this.generateLabel = generateLabel;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getFormSpecimenVo()
	 */
	public GenericSpecimen getFormSpecimenVo()
	{
		return this.formSpecimenVo;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setFormSpecimenVo(edu.wustl.catissuecore.bean.GenericSpecimen)
	 */
	public void setFormSpecimenVo(GenericSpecimen formSpecimenVo)
	{
		this.formSpecimenVo = formSpecimenVo;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getCorresSpecimen()
	 */
	public Specimen getCorresSpecimen()
	{
		return this.corresSpecimen;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setCorresSpecimen(edu.wustl.catissuecore.domain.Specimen)
	 */
	public void setCorresSpecimen(Specimen corresSpecimen)
	{
		this.corresSpecimen = corresSpecimen;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getId()
	 */
	public long getId()
	{
		return this.id;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setId(long)
	 */
	public void setId(long id)
	{
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getClassName()
	 */
	public String getClassName()
	{
		return this.className;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setClassName(java.lang.String)
	 */
	public void setClassName(String className)
	{
		this.className = className;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getType()
	 */
	public String getType()
	{
		return this.type;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setType(java.lang.String)
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getTissueSite()
	 */
	public String getTissueSite()
	{
		return this.tissueSite;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setTissueSite(java.lang.String)
	 */
	public void setTissueSite(String tissueSite)
	{
		this.tissueSite = tissueSite;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getTissueSide()
	 */
	public String getTissueSide()
	{
		return this.tissueSide;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setTissueSide(java.lang.String)
	 */
	public void setTissueSide(String tissueSide)
	{
		this.tissueSide = tissueSide;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getPathologicalStatus()
	 */
	public String getPathologicalStatus()
	{
		return this.pathologicalStatus;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setPathologicalStatus(java.lang.String)
	 */
	public void setPathologicalStatus(String pathologicalStatus)
	{
		this.pathologicalStatus = pathologicalStatus;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getConcentration()
	 */
	public String getConcentration()
	{
		if (this.concentration == null)
		{
			this.concentration = "0";
		}
		return this.concentration;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setConcentration(java.lang.String)
	 */
	public void setConcentration(String concentration)
	{
		this.concentration = concentration;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getQuantity()
	 */
	public String getQuantity()
	{
		return this.quantity;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setQuantity(java.lang.String)
	 */
	public void setQuantity(String quantity)
	{
		this.quantity = quantity;
	}

	/**
	 * Gets the lineage.
	 *
	 * @return the lineage
	 */
	public String getLineage()
	{
		return this.lineage;
	}

	/**
	 * Sets the lineage.
	 *
	 * @param lineage the new lineage
	 */
	public void setLineage(String lineage)
	{
		this.lineage = lineage;
	}

	/**
	 * Gets the no of aliquots.
	 *
	 * @return the no of aliquots
	 */
	public String getNoOfAliquots()
	{
		return this.noOfAliquots;
	}

	/**
	 * Sets the no of aliquots.
	 *
	 * @param noOfAliquots the new no of aliquots
	 */
	public void setNoOfAliquots(String noOfAliquots)
	{
		this.noOfAliquots = noOfAliquots;
	}

	/**
	 * Gets the quantity per aliquot.
	 *
	 * @return the quantity per aliquot
	 */
	public String getQuantityPerAliquot()
	{
		return this.quantityPerAliquot;
	}

	/**
	 * Sets the quantity per aliquot.
	 *
	 * @param quantityPerAliquot the new quantity per aliquot
	 */
	public void setQuantityPerAliquot(String quantityPerAliquot)
	{
		this.quantityPerAliquot = quantityPerAliquot;
	}

	/**
	 * Gets the received event received quality.
	 *
	 * @return the received event received quality
	 */
	public String getReceivedEventReceivedQuality()
	{
		return this.receivedEventReceivedQuality;
	}

	/**
	 * Sets the received event received quality.
	 *
	 * @param receivedEventReceivedQuality the new received event received quality
	 */
	public void setReceivedEventReceivedQuality(String receivedEventReceivedQuality)
	{
		this.receivedEventReceivedQuality = receivedEventReceivedQuality;
	}

	/**
	 * Gets the no of derive specimen.
	 *
	 * @return the no of derive specimen
	 */
	public int getNoOfDeriveSpecimen()
	{
		return this.noOfDeriveSpecimen;
	}

	/**
	 * Sets the no of derive specimen.
	 *
	 * @param noOfDeriveSpecimen the new no of derive specimen
	 */
	public void setNoOfDeriveSpecimen(int noOfDeriveSpecimen)
	{
		this.noOfDeriveSpecimen = noOfDeriveSpecimen;
	}

	/**
	 * Gets the derive specimen.
	 *
	 * @return the derive specimen
	 */
	public Map<String, GenericSpecimen> getDeriveSpecimen()
	{
		return this.deriveSpecimen;
	}

	/**
	 * Sets the derive specimen.
	 *
	 * @param deriveSpecimen the derive specimen
	 */
	public void setDeriveSpecimen(Map<String, GenericSpecimen> deriveSpecimen)
	{
		this.deriveSpecimen = deriveSpecimen;
	}

	/**
	 * Gets the derive class name.
	 *
	 * @return the derive class name
	 */
	public String getDeriveClassName()
	{
		return this.deriveClassName;
	}

	/**
	 * Sets the derive class name.
	 *
	 * @param deriveClassName the new derive class name
	 */
	public void setDeriveClassName(String deriveClassName)
	{
		this.deriveClassName = deriveClassName;
	}

	/**
	 * Gets the derive type.
	 *
	 * @return the derive type
	 */
	public String getDeriveType()
	{
		return this.deriveType;
	}

	/**
	 * Sets the derive type.
	 *
	 * @param deriveType the new derive type
	 */
	public void setDeriveType(String deriveType)
	{
		this.deriveType = deriveType;
	}

	/**
	 * Gets the derive concentration.
	 *
	 * @return the derive concentration
	 */
	public String getDeriveConcentration()
	{
		return this.deriveConcentration;
	}

	/**
	 * Sets the derive concentration.
	 *
	 * @param deriveConcentration the new derive concentration
	 */
	public void setDeriveConcentration(String deriveConcentration)
	{
		this.deriveConcentration = deriveConcentration;
	}

	/**
	 * Gets the derive quantity.
	 *
	 * @return the derive quantity
	 */
	public String getDeriveQuantity()
	{
		return this.deriveQuantity;
	}

	/**
	 * Sets the derive quantity.
	 *
	 * @param deriveQuantity the new derive quantity
	 */
	public void setDeriveQuantity(String deriveQuantity)
	{
		this.deriveQuantity = deriveQuantity;
	}

	/**
	 * Gets the serial version uid.
	 *
	 * @return the serial version uid
	 */
	public static long getSerialVersionUID()
	{
		return serialVersionUID;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getAliquotSpecimenCollection()
	 */
	public LinkedHashMap<String, GenericSpecimen> getAliquotSpecimenCollection()
	{
		return (LinkedHashMap<String, GenericSpecimen>) this.aliquotSpecimenCollection;
	}

	/**
	 * Adds the aliquot specimen bean.
	 *
	 * @param specimenRequirementBean the specimen requirement bean
	 */
	public void addAliquotSpecimenBean(SpecimenRequirementBean specimenRequirementBean)
	{
		this.aliquotSpecimenCollection.put(specimenRequirementBean.getUniqueIdentifier(),
				specimenRequirementBean);
	}

	/**
	 * Gets the collection event id.
	 *
	 * @return the collection event id
	 */
	public long getCollectionEventId()
	{
		return this.collectionEventId;
	}

	/**
	 * Sets the collection event id.
	 *
	 * @param collectionEventId the new collection event id
	 */
	public void setCollectionEventId(long collectionEventId)
	{
		this.collectionEventId = collectionEventId;
	}

	/**
	 * Gets the collection event specimen id.
	 *
	 * @return the collection event specimen id
	 */
	public long getCollectionEventSpecimenId()
	{
		return this.collectionEventSpecimenId;
	}

	/**
	 * Sets the collection event specimen id.
	 *
	 * @param collectionEventSpecimenId the new collection event specimen id
	 */
	public void setCollectionEventSpecimenId(long collectionEventSpecimenId)
	{
		this.collectionEventSpecimenId = collectionEventSpecimenId;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getDeriveSpecimenCollection()
	 */
	public LinkedHashMap<String, GenericSpecimen> getDeriveSpecimenCollection()
	{
		return (LinkedHashMap<String, GenericSpecimen>) this.deriveSpecimenCollection;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getDisplayName()
	 */
	public String getDisplayName()
	{
		return this.displayName;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setDisplayName(java.lang.String)
	 */
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getStorageContainerForSpecimen()
	 */
	public String getStorageContainerForSpecimen()
	{
		return this.storageContainerForSpecimen;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setStorageContainerForSpecimen(java.lang.String)
	 */
	public void setStorageContainerForSpecimen(String storageContainerForSpecimen)
	{
		this.storageContainerForSpecimen = storageContainerForSpecimen;
	}

	/**
	 * Gets the storage container for aliquot specimem.
	 *
	 * @return the storage container for aliquot specimem
	 */
	public String getStorageContainerForAliquotSpecimem()
	{
		return this.storageContainerForAliquotSpecimem;
	}

	/**
	 * Gets the collection event user id.
	 *
	 * @return the collection event user id
	 */
	public long getCollectionEventUserId()
	{
		return this.collectionEventUserId;
	}

	/**
	 * Sets the collection event user id.
	 *
	 * @param collectionEventUserId the new collection event user id
	 */
	public void setCollectionEventUserId(long collectionEventUserId)
	{
		this.collectionEventUserId = collectionEventUserId;
	}

	/**
	 * Sets the storage container for aliquot specimem.
	 *
	 * @param storageContainerForAliquotSpecimem the new storage container for aliquot specimem
	 */
	public void setStorageContainerForAliquotSpecimem(String storageContainerForAliquotSpecimem)
	{
		this.storageContainerForAliquotSpecimem = storageContainerForAliquotSpecimem;
	}



	/**
	 * Gets the received event id.
	 *
	 * @return the received event id
	 */
	public long getReceivedEventId()
	{
		return this.receivedEventId;
	}



	/**
	 * Gets the received event specimen id.
	 *
	 * @return the received event specimen id
	 */
	public long getReceivedEventSpecimenId()
	{
		return this.receivedEventSpecimenId;
	}

	/**
	 * Sets the received event specimen id.
	 *
	 * @param receivedEventSpecimenId the new received event specimen id
	 */
	public void setReceivedEventSpecimenId(long receivedEventSpecimenId)
	{
		this.receivedEventSpecimenId = receivedEventSpecimenId;
	}

	/**
	 * Sets the received event id.
	 *
	 * @param receivedEventId the new received event id
	 */
	public void setReceivedEventId(long receivedEventId)
	{
		this.receivedEventId = receivedEventId;
	}

	/**
	 * Gets the received event user id.
	 *
	 * @return the received event user id
	 */
	public long getReceivedEventUserId()
	{
		return this.receivedEventUserId;
	}

	/**
	 * Sets the received event user id.
	 *
	 * @param receivedEventUserId the new received event user id
	 */
	public void setReceivedEventUserId(long receivedEventUserId)
	{
		this.receivedEventUserId = receivedEventUserId;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getUniqueIdentifier()
	 */
	public String getUniqueIdentifier()
	{
		return this.uniqueIdentifier;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setUniqueIdentifier(java.lang.String)
	 */
	public void setUniqueIdentifier(String uniqueIdentifier)
	{
		this.uniqueIdentifier = uniqueIdentifier;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getParentName()
	 */
	public String getParentName()
	{
		return this.parentName;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setParentName(java.lang.String)
	 */
	public void setParentName(String parentName)
	{
		this.parentName = parentName;
	}

	/**
	 * Gets the specimen chars id.
	 *
	 * @return the specimen chars id
	 */
	public long getSpecimenCharsId()
	{
		return this.specimenCharsId;
	}

	/**
	 * Sets the specimen chars id.
	 *
	 * @param specimenCharsId the new specimen chars id
	 */
	public void setSpecimenCharsId(long specimenCharsId)
	{
		this.specimenCharsId = specimenCharsId;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getBarCode()
	 */
	public String getBarCode()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getCheckedSpecimen()
	 */
	public boolean getCheckedSpecimen()
	{
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getContainerId()
	 */
	public String getContainerId()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getPositionDimensionOne()
	 */
	public String getPositionDimensionOne()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getPositionDimensionTwo()
	 */
	public String getPositionDimensionTwo()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getReadOnly()
	 */
	public boolean getReadOnly()
	{
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getSelectedContainerName()
	 */
	public String getSelectedContainerName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setAliquotSpecimenCollection(java.util.LinkedHashMap)
	 */
	public void setAliquotSpecimenCollection(
			LinkedHashMap<String, GenericSpecimen> aliquotSpecimenCollection)
	{

		this.aliquotSpecimenCollection = aliquotSpecimenCollection;

	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setBarCode(java.lang.String)
	 */
	public void setBarCode(String barCode)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setCheckedSpecimen(boolean)
	 */
	public void setCheckedSpecimen(boolean checkedSpecimen)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setContainerId(java.lang.String)
	 */
	public void setContainerId(String containerId)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setDeriveSpecimenCollection(java.util.LinkedHashMap)
	 */
	public void setDeriveSpecimenCollection(
			LinkedHashMap<String, GenericSpecimen> deriveSpecimenCollection)
	{
		this.deriveSpecimenCollection = deriveSpecimenCollection;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setPositionDimensionOne(java.lang.String)
	 */
	public void setPositionDimensionOne(String positionDimensionOne)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setPositionDimensionTwo(java.lang.String)
	 */
	public void setPositionDimensionTwo(String positionDimensionTwo)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setReadOnly(boolean)
	 */
	public void setReadOnly(boolean readOnly)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setSelectedContainerName(java.lang.String)
	 */
	public void setSelectedContainerName(String selectedContainerName)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getCollectionProtocolId()
	 */
	public Long getCollectionProtocolId()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setCollectionProtocolId(java.lang.Long)
	 */
	public void setCollectionProtocolId(Long collectionProtocolId)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getShowBarcode()
	 */
	public boolean getShowBarcode()
	{
		// TODO Auto-generated method stub
		return this.showBarcode;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getShowLabel()
	 */
	public boolean getShowLabel()
	{
		// TODO Auto-generated method stub
		return this.showLabel;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setShowBarcode(boolean)
	 */
	public void setShowBarcode(boolean showBarcode)
	{
		this.showBarcode = showBarcode;

	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setShowLabel(boolean)
	 */
	public void setShowLabel(boolean showLabel)
	{
		this.showLabel = showLabel;

	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getPrintSpecimen()
	 */
	public boolean getPrintSpecimen()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setPrintSpecimen(boolean)
	 */
	public void setPrintSpecimen(boolean printSpecimen)
	{

	}

	/**
	 * Gets the collection event collection procedure.
	 *
	 * @return the collection event collection procedure
	 */
	public String getCollectionEventCollectionProcedure()
	{
		return this.collectionEventCollectionProcedure;
	}

	/**
	 * Sets the collection event collection procedure.
	 *
	 * @param collectionEventCollectionProcedure the new collection event collection procedure
	 */
	public void setCollectionEventCollectionProcedure(String collectionEventCollectionProcedure)
	{
		this.collectionEventCollectionProcedure = collectionEventCollectionProcedure;
	}

	/**
	 * Gets the collection event container.
	 *
	 * @return the collection event container
	 */
	public String getCollectionEventContainer()
	{
		return this.collectionEventContainer;
	}

	/**
	 * Sets the collection event container.
	 *
	 * @param collectionEventContainer the new collection event container
	 */
	public void setCollectionEventContainer(String collectionEventContainer)
	{
		this.collectionEventContainer = collectionEventContainer;
	}

	/** For SCG labeling,this will be exposed through API and not in the model. */
	private String labelFormat;

	/**
	 * For SCG labeling,this will be exposed through API and not in the model.
	 *
	 * @return the label format
	 */
	public String getLabelFormat()
	{
		return this.labelFormat;
	}

	/**
	 * For SCG labeling,this will be exposed through API and not in the model.
	 *
	 * @param labelFormat the label format
	 */
	public void setLabelFormat(String labelFormat)
	{
		this.labelFormat = labelFormat;
	}

}
