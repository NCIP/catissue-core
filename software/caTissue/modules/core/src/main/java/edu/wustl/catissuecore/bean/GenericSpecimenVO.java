/**
 *
 */

package edu.wustl.catissuecore.bean;

import java.util.LinkedHashMap;

import edu.wustl.catissuecore.domain.Specimen;

// TODO: Auto-generated Javadoc
/**
 * The Class GenericSpecimenVO.
 *
 * @author abhijit_naik
 */
public final class GenericSpecimenVO implements GenericSpecimen
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4892794446362429521L;

	/** The aliquot specimen collection. */
	private LinkedHashMap<String, GenericSpecimen> aliquotSpecimenCollection = null;

	/** The class name. */
	private String className = null;

	/** The concentration. */
	private String concentration = null;

	/** The derive specimen collection. */
	private LinkedHashMap<String, GenericSpecimen> deriveSpecimenCollection = null;

	/** The display name. */
	private String displayName = null;

	/** The parent name. */
	private String parentName = null;

	/** The pathological status. */
	private String pathologicalStatus = null;

	/** The quantity. */
	private String quantity = null;

	/** The storage container for specimen. */
	private String storageContainerForSpecimen = null;

	/** The tissue side. */
	private String tissueSide = null;

	/** The tissue site. */
	private String tissueSite = null;

	/** The type. */
	private String type = null;

	/** The unique identifier. */
	private String uniqueIdentifier = null;

	/** The checked specimen. */
	private boolean checkedSpecimen = false;

	/** The print specimen. */
	private boolean printSpecimen = false;

	/** The read only. */
	private boolean readOnly = false;

	/** The bar code. */
	private String barCode;

	/** The selected container name. */
	private String selectedContainerName;

	/** The position dimension one. */
	private String positionDimensionOne;

	/** The position dimension two. */
	private String positionDimensionTwo;

	/** The container id. */
	private String containerId;

	/** The collection protocol id. */
	private Long collectionProtocolId = -1l;

	/** The id. */
	private long id = -1;

	/** The corres specimen. */
	private Specimen corresSpecimen;

	/** The form specimen vo. */
	private GenericSpecimen formSpecimenVo;

	/** The show barcode. */
	private boolean showBarcode = true;

	/** The show label. */
	private boolean showLabel = true;

	/** The generate label. */
	private boolean generateLabel = false;

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
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getAliquotSpecimenCollection()
	 */
	public LinkedHashMap<String, GenericSpecimen> getAliquotSpecimenCollection()
	{
		return this.aliquotSpecimenCollection;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getClassName()
	 */
	public String getClassName()
	{
		return this.className;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getConcentration()
	 */
	public String getConcentration()
	{
		return this.concentration;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getDeriveSpecimenCollection()
	 */
	public LinkedHashMap<String, GenericSpecimen> getDeriveSpecimenCollection()
	{
		return this.deriveSpecimenCollection;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getDisplayName()
	 */
	public String getDisplayName()
	{
		return this.displayName;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getParentName()
	 */
	public String getParentName()
	{
		return this.parentName;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getPathologicalStatus()
	 */
	public String getPathologicalStatus()
	{
		return this.pathologicalStatus;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getQuantity()
	 */
	public String getQuantity()
	{
		return this.quantity;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getStorageContainerForSpecimen()
	 */
	public String getStorageContainerForSpecimen()
	{
		return this.storageContainerForSpecimen;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getTissueSide()
	 */
	public String getTissueSide()
	{
		return this.tissueSide;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getTissueSite()
	 */
	public String getTissueSite()
	{
		return this.tissueSite;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getType()
	 */
	public String getType()
	{
		return this.type;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getUniqueIdentifier()
	 */
	public String getUniqueIdentifier()
	{
		return this.uniqueIdentifier;
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
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setClassName(java.lang.String)
	 */
	public void setClassName(String className)
	{
		this.className = className;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setConcentration(java.lang.String)
	 */
	public void setConcentration(String concentration)
	{
		this.concentration = concentration;
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
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setDisplayName(java.lang.String)
	 */
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setParentName(java.lang.String)
	 */
	public void setParentName(String parentName)
	{
		this.parentName = parentName;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setPathologicalStatus(java.lang.String)
	 */
	public void setPathologicalStatus(String pathologicalStatus)
	{
		this.pathologicalStatus = pathologicalStatus;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setQuantity(java.lang.String)
	 */
	public void setQuantity(String quantity)
	{
		this.quantity = quantity;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setStorageContainerForSpecimen(java.lang.String)
	 */
	public void setStorageContainerForSpecimen(String storageContainerForSpecimen)
	{
		this.storageContainerForSpecimen = storageContainerForSpecimen;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setTissueSide(java.lang.String)
	 */
	public void setTissueSide(String tissueSide)
	{
		this.tissueSide = tissueSide;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setTissueSite(java.lang.String)
	 */
	public void setTissueSite(String tissueSite)
	{
		this.tissueSite = tissueSite;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setType(java.lang.String)
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setUniqueIdentifier(java.lang.String)
	 */
	public void setUniqueIdentifier(String uniqueIdentifier)
	{
		this.uniqueIdentifier = uniqueIdentifier;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getCheckedSpecimen()
	 */
	public boolean getCheckedSpecimen()
	{
		return this.checkedSpecimen;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setCheckedSpecimen(boolean)
	 */
	public void setCheckedSpecimen(boolean checkedSpecimen)
	{
		this.checkedSpecimen = checkedSpecimen;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getReadOnly()
	 */
	public boolean getReadOnly()
	{
		return this.readOnly;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setReadOnly(boolean)
	 */
	public void setReadOnly(boolean readOnly)
	{
		this.readOnly = readOnly;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getBarCode()
	 */
	public String getBarCode()
	{
		return this.barCode;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setBarCode(java.lang.String)
	 */
	public void setBarCode(String barCode)
	{
		this.barCode = barCode;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getSelectedContainerName()
	 */
	public String getSelectedContainerName()
	{
		return this.selectedContainerName;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setSelectedContainerName(java.lang.String)
	 */
	public void setSelectedContainerName(String selectedContainerName)
	{
		this.selectedContainerName = selectedContainerName;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getPositionDimensionOne()
	 */
	public String getPositionDimensionOne()
	{
		return this.positionDimensionOne;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setPositionDimensionOne(java.lang.String)
	 */
	public void setPositionDimensionOne(String positionDimensionOne)
	{
		this.positionDimensionOne = positionDimensionOne;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getPositionDimensionTwo()
	 */
	public String getPositionDimensionTwo()
	{
		return this.positionDimensionTwo;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setPositionDimensionTwo(java.lang.String)
	 */
	public void setPositionDimensionTwo(String positionDimensionTwo)
	{
		this.positionDimensionTwo = positionDimensionTwo;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getContainerId()
	 */
	public String getContainerId()
	{
		return this.containerId;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#setContainerId(java.lang.String)
	 */
	public void setContainerId(String containerId)
	{
		this.containerId = containerId;
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

	/**
	 * (non-Javadoc).
	 *
	 * @return the id
	 *
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


	public Specimen getParentSpecimen() {
		// TODO Auto-generated method stub
		return null;
	}


	public void setParentSpecimen(Specimen parentSpecimen) {
		// TODO Auto-generated method stub

	}


	@Override
	public String getCreationEventForSpecimen() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getProcessingSOPForSpecimen() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setCreationEventForSpecimen(String creationEvent) {
		// TODO Auto-generated method stub

	}


	@Override
	public void setProcessingSOPForSpecimen(String processing) {
		// TODO Auto-generated method stub

	}

}
