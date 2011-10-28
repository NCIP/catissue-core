
package edu.wustl.catissuecore.bean;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;

import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;

// TODO: Auto-generated Javadoc
/**
 * The Class SpecimenDataBean.
 */
public class SpecimenDataBean implements GenericSpecimen
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -593612220432189911L;

	/** The unique id. */
	public String uniqueId;

	/** The id. */
	protected Long id;

	/** The class name. */
	protected String className;

	/** Type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc. */
	protected String type;

	/** Reference to dimensional position one of the specimen in Storage Container. */
	protected String positionDimensionOne = "";

	/** Reference to dimensional position two of the specimen in Storage Container. */
	protected String positionDimensionTwo = "";

	/** Barcode assigned to the specimen. */
	protected String barCode;

	/** Comment on specimen. */
	protected String comment;

	/** Parent specimen from which this specimen is derived. */
	protected Specimen parentSpecimen;

	/** Collection of attributes of a Specimen that renders it potentially harmful to a User. */
	protected Collection<Biohazard> biohazardCollection = new HashSet<Biohazard>();

	/** Collection of Specimen Event Parameters associated with this specimen. */
	protected Collection<SpecimenEventParameters> specimenEventCollection = new HashSet<SpecimenEventParameters>();

	/** Collection of a pre-existing, externally defined id associated with a specimen. */
	protected Collection<ExternalIdentifier> externalIdentifierCollection = new HashSet<ExternalIdentifier>();

	/** An event that results in the collection of one or more specimen from a participant. */
	protected SpecimenCollectionGroup specimenCollectionGroup;

	/** The pathological status. */
	protected String pathologicalStatus;

	/** The tissue site. */
	protected String tissueSite;

	/** The tissue side. */
	protected String tissueSide;

	/** A historical information about the specimen i.e. whether the specimen is a new specimen or a derived specimen or an aliquot. */
	protected String lineage;

	/** A label name of this specimen. */
	protected String label;

	//Change for API Search   --- Ashwin 04/10/2006
	/** The quantity of a specimen. */
	protected String initialQuantity;

	/** The concentration. */
	protected String concentration;

	/** The derive specimen collection. */
	public LinkedHashMap<String, GenericSpecimen> deriveSpecimenCollection = new LinkedHashMap<String, GenericSpecimen>();

	/** The parent name. */
	private String parentName;

	/** The storage type. */
	private String storageType;

	/** The selected container name. */
	private String selectedContainerName;

	/** The container id. */
	private String containerId;

	/** The checked specimen. */
	private boolean checkedSpecimen = true;

	/** The print specimen. */
	private boolean printSpecimen = false;//janhavi

	/** The corres specimen. */
	private Specimen corresSpecimen;

	/** The collection protocol id. */
	private Long collectionProtocolId = -1l;

	/** The form specimen vo. */
	private GenericSpecimen formSpecimenVo;

	/** The show barcode. */
	private boolean showBarcode = true;

	/** The show label. */
	private boolean showLabel = true;

	/** The read only. */
	private boolean readOnly = false;

	/** The generate label. */
	private boolean generateLabel = false;

	/** The container id. */
	private String creationEvent;

	/** The container id. */
	private String processingSPP;

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

	/**
	 * Gets the biohazard collection.
	 *
	 * @return Returns the biohazardCollection.
	 */
	public Collection<Biohazard> getBiohazardCollection()
	{
		return this.biohazardCollection;
	}

	/**
	 * Sets the biohazard collection.
	 *
	 * @param biohazardCollection The biohazardCollection to set.
	 */
	public void setBiohazardCollection(Collection<Biohazard> biohazardCollection)
	{
		this.biohazardCollection = biohazardCollection;
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

	/**
	 * Gets the class name.
	 *
	 * @return Returns the className.
	 */
	public String getClassName()
	{
		return this.className;
	}

	/**
	 * Sets the class name.
	 *
	 * @param className The className to set.
	 */
	public void setClassName(String className)
	{
		this.className = className;
	}

	/**
	 * Gets the comment.
	 *
	 * @return Returns the comment.
	 */
	public String getComment()
	{
		return this.comment;
	}

	/**
	 * Sets the comment.
	 *
	 * @param comment The comment to set.
	 */
	public void setComment(String comment)
	{
		this.comment = comment;
	}

	/**
	 * Gets the concentration.
	 *
	 * @return Returns the concentration.
	 */
	public String getConcentration()
	{
		return this.concentration;
	}

	/**
	 * Sets the concentration.
	 *
	 * @param concentration The concentration to set.
	 */
	public void setConcentration(String concentration)
	{
		this.concentration = concentration;
	}

	/**
	 * Gets the external identifier collection.
	 *
	 * @return Returns the externalIdentifierCollection.
	 */
	public Collection<ExternalIdentifier> getExternalIdentifierCollection()
	{
		return this.externalIdentifierCollection;
	}

	/**
	 * Sets the external identifier collection.
	 *
	 * @param externalIdentifierCollection The externalIdentifierCollection to set.
	 */
	public void setExternalIdentifierCollection(Collection<ExternalIdentifier> externalIdentifierCollection)
	{
		this.externalIdentifierCollection = externalIdentifierCollection;
	}

	/**
	 * Gets the id.
	 *
	 * @return Returns the id.
	 */
	public long getId()
	{
		long num = -1;
		if (this.id == null)
		{
			num = -1;
		}
		else
		{
			num = this.id.longValue();
		}
		return num;
	}

	/**
	 * Sets the id.
	 *
	 * @param id The id to set.
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * Gets the label.
	 *
	 * @return Returns the label.
	 */
	public String getLabel()
	{
		return this.label;
	}

	/**
	 * Sets the label.
	 *
	 * @param label The label to set.
	 */
	public void setLabel(String label)
	{
		this.label = label;
	}

	/**
	 * Gets the lineage.
	 *
	 * @return Returns the lineage.
	 */
	public String getLineage()
	{
		return this.lineage;
	}

	/**
	 * Sets the lineage.
	 *
	 * @param lineage The lineage to set.
	 */
	public void setLineage(String lineage)
	{
		this.lineage = lineage;
	}

	/**
	 * Gets the parent specimen.
	 *
	 * @return Returns the parentSpecimen.
	 */
	public Specimen getParentSpecimen()
	{
		return this.parentSpecimen;
	}

	/**
	 * Sets the parent specimen.
	 *
	 * @param parentSpecimen The parentSpecimen to set.
	 */
	public void setParentSpecimen(Specimen parentSpecimen)
	{
		this.parentSpecimen = parentSpecimen;
	}

	/**
	 * Gets the pathological status.
	 *
	 * @return Returns the pathologicalStatus.
	 */
	public String getPathologicalStatus()
	{
		return this.pathologicalStatus;
	}

	/**
	 * Sets the pathological status.
	 *
	 * @param pathologicalStatus The pathologicalStatus to set.
	 */
	public void setPathologicalStatus(String pathologicalStatus)
	{
		this.pathologicalStatus = pathologicalStatus;
	}

	/**
	 * Gets the position dimension one.
	 *
	 * @return Returns the positionDimensionOne.
	 */
	public String getPositionDimensionOne()
	{
		return String.valueOf(this.positionDimensionOne);
	}

	/**
	 * Gets the position dimension two.
	 *
	 * @return Returns the positionDimensionTwo.
	 */
	public String getPositionDimensionTwo()
	{
		return String.valueOf(this.positionDimensionTwo);
	}

	/**
	 * Gets the specimen collection group.
	 *
	 * @return Returns the specimenCollectionGroup.
	 */
	public SpecimenCollectionGroup getSpecimenCollectionGroup()
	{
		return this.specimenCollectionGroup;
	}

	/**
	 * Sets the specimen collection group.
	 *
	 * @param specimenCollectionGroup The specimenCollectionGroup to set.
	 */
	public void setSpecimenCollectionGroup(SpecimenCollectionGroup specimenCollectionGroup)
	{
		this.specimenCollectionGroup = specimenCollectionGroup;
	}

	/**
	 * Gets the specimen event collection.
	 *
	 * @return Returns the specimenEventCollection.
	 */
	public Collection<SpecimenEventParameters> getSpecimenEventCollection()
	{
		return this.specimenEventCollection;
	}

	/**
	 * Sets the specimen event collection.
	 *
	 * @param specimenEventCollection The specimenEventCollection to set.
	 */
	public void setSpecimenEventCollection(Collection<SpecimenEventParameters> specimenEventCollection)
	{
		this.specimenEventCollection = specimenEventCollection;
	}

	/**
	 * Gets the tissue side.
	 *
	 * @return Returns the tissueSide.
	 */
	public String getTissueSide()
	{
		return this.tissueSide;
	}

	/**
	 * Sets the tissue side.
	 *
	 * @param tissueSide The tissueSide to set.
	 */
	public void setTissueSide(String tissueSide)
	{
		this.tissueSide = tissueSide;
	}

	/**
	 * Gets the tissue site.
	 *
	 * @return Returns the tissueSite.
	 */
	public String getTissueSite()
	{
		return this.tissueSite;
	}

	/**
	 * Sets the tissue site.
	 *
	 * @param tissueSite The tissueSite to set.
	 */
	public void setTissueSite(String tissueSite)
	{
		this.tissueSite = tissueSite;
	}

	/**
	 * Gets the type.
	 *
	 * @return Returns the type.
	 */
	public String getType()
	{
		return this.type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type The type to set.
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * Gets the derive specimen collection.
	 *
	 * @return Returns the deriveSpecimenCollection.
	 */
	public LinkedHashMap<String, GenericSpecimen> getDeriveSpecimenCollection()
	{
		return this.deriveSpecimenCollection;
	}

	/**
	 * Sets the derive specimen collection.
	 *
	 * @param deriveSpecimenCollection The deriveSpecimenCollection to set.
	 */
	public void setDeriveSpecimenCollection(
			LinkedHashMap<String, GenericSpecimen> deriveSpecimenCollection)
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
		return this.parentName;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getQuantity()
	 */
	public String getQuantity()
	{
		// TODO Auto-generated method stub
		return this.initialQuantity;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getStorageContainerForSpecimen()
	 */
	public String getStorageContainerForSpecimen()
	{
		// TODO Auto-generated method stub
		return this.storageType;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getUniqueIdentifier()
	 */
	public String getUniqueIdentifier()
	{
		// TODO Auto-generated method stub
		return this.uniqueId;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getBarCode()
	 */
	public String getBarCode()
	{
		// TODO Auto-generated method stub
		return this.barCode;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getCheckedSpecimen()
	 */
	public boolean getCheckedSpecimen()
	{
		// TODO Auto-generated method stub
		return this.checkedSpecimen;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getContainerId()
	 */
	public String getContainerId()
	{
		// TODO Auto-generated method stub
		return this.containerId;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getReadOnly()
	 */
	public boolean getReadOnly()
	{
		// TODO Auto-generated method stub
		return this.readOnly;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getSelectedContainerName()
	 */
	public String getSelectedContainerName()
	{
		// TODO Auto-generated method stub
		return this.selectedContainerName;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setAliquotSpecimenCollection(java.util.LinkedHashMap)
	 */
	public void setAliquotSpecimenCollection(
			LinkedHashMap<String, GenericSpecimen> aliquotSpecimenCollection)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setBarCode(java.lang.String)
	 */
	public void setBarCode(String barCode)
	{
		// TODO Auto-generated method stub
		this.barCode = barCode;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setCheckedSpecimen(boolean)
	 */
	public void setCheckedSpecimen(boolean checkedSpecimen)
	{
		this.checkedSpecimen = checkedSpecimen;

	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setContainerId(java.lang.String)
	 */
	public void setContainerId(String containerId)
	{

		this.containerId = containerId;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setDisplayName(java.lang.String)
	 */
	public void setDisplayName(String displayName)
	{
		// TODO Auto-generated method stub
		this.label = displayName;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setId(long)
	 */
	public void setId(long id)
	{
		// TODO Auto-generated method stub
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setParentName(java.lang.String)
	 */
	public void setParentName(String parentName)
	{

		this.parentName = parentName;

	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setPositionDimensionOne(java.lang.String)
	 */
	public void setPositionDimensionOne(String positionDimensionOne)
	{

		this.positionDimensionOne = positionDimensionOne;

	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setPositionDimensionTwo(java.lang.String)
	 */
	public void setPositionDimensionTwo(String positionDimensionTwo)
	{

		this.positionDimensionTwo = positionDimensionTwo;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setQuantity(java.lang.String)
	 */
	public void setQuantity(String quantity)
	{

		this.initialQuantity = quantity;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setReadOnly(boolean)
	 */
	public void setReadOnly(boolean readOnly)
	{
		// TODO Auto-generated method stub
		this.readOnly = readOnly;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setSelectedContainerName(java.lang.String)
	 */
	public void setSelectedContainerName(String selectedContainerName)
	{

		this.selectedContainerName = selectedContainerName;

	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setStorageContainerForSpecimen(java.lang.String)
	 */
	public void setStorageContainerForSpecimen(String storageContainerForSpecimen)
	{

		this.storageType = storageContainerForSpecimen;

	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setUniqueIdentifier(java.lang.String)
	 */
	public void setUniqueIdentifier(String uniqueIdentifier)
	{

		this.uniqueId = uniqueIdentifier;
	}

	/**
	 * Gets the collection protocol id.
	 *
	 * @return Returns the collectionProtocolId.
	 */
	public Long getCollectionProtocolId()
	{
		return this.collectionProtocolId;
	}

	/**
	 * Sets the collection protocol id.
	 *
	 * @param collectionProtocolId The collectionProtocolId to set.
	 */
	public void setCollectionProtocolId(Long collectionProtocolId)
	{
		this.collectionProtocolId = collectionProtocolId;
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
		// TODO Auto-generated method stub
		return this.printSpecimen;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setPrintSpecimen(boolean)
	 */
	public void setPrintSpecimen(boolean printSpecimen)
	{
		this.printSpecimen = printSpecimen;
	}

	@Override
	public String getCreationEventForSpecimen() {
		return this.creationEvent;
	}

	@Override
	public String getProcessingSPPForSpecimen() {
		// TODO Auto-generated method stub
		return processingSPP;
	}

	@Override
	public void setCreationEventForSpecimen(String creationEvent) {
		this.creationEvent=creationEvent;

	}

	@Override
	public void setProcessingSPPForSpecimen(String processingSPP) {
		this.processingSPP=processingSPP;

	}

	/* override methods finish */

}
