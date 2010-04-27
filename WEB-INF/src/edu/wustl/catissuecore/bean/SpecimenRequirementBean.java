
package edu.wustl.catissuecore.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.wustl.catissuecore.domain.Specimen;

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

	private long collectionEventId; // Mandar : CollectionEvent 10-July-06
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
	protected Map<String, GenericSpecimen> aliquotSpecimenCollection = new LinkedHashMap<String, GenericSpecimen>();

	/**
	 * Collection of derive specimens derived from this specimen.
	 */
	protected Map<String, GenericSpecimen> deriveSpecimenCollection = new LinkedHashMap<String, GenericSpecimen>();

	private int noOfDeriveSpecimen = 0;

	private Map<String, GenericSpecimen> deriveSpecimen = new HashMap<String, GenericSpecimen>();

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
	private GenericSpecimen formSpecimenVo;
	private Specimen corresSpecimen;

	private boolean showBarcode = true;
	private boolean showLabel = true;

	private boolean generateLabel = false;

	private String labelGenType;

	private String labelGenTypeForAliquot;

	private String labelFormatForAliquot;

	private boolean genLabelForAliquot;



	public String getLabelGenTypeForAliquot()
	{
		return labelGenTypeForAliquot;
	}




	public void setLabelGenTypeForAliquot(String labelGenTypeForAliquot)
	{
		this.labelGenTypeForAliquot = labelGenTypeForAliquot;
	}




	public String getLabelFormatForAliquot()
	{
		return labelFormatForAliquot;
	}




	public void setLabelFormatForAliquot(String labelFormatForAliquot)
	{
		this.labelFormatForAliquot = labelFormatForAliquot;
	}




	public boolean isGenLabelForAliquot()
	{
		return genLabelForAliquot;
	}




	public void setGenLabelForAliquot(boolean genLabelForAliquot)
	{
		this.genLabelForAliquot = genLabelForAliquot;
	}



	public String getLabelGenType()
	{
		return labelGenType;
	}



	public void setLabelGenType(String labelGenType)
	{
		this.labelGenType = labelGenType;
	}


	public boolean isGenerateLabel()
	{
		return generateLabel;
	}


	public void setGenerateLabel(boolean generateLabel)
	{
		this.generateLabel = generateLabel;
	}

	public GenericSpecimen getFormSpecimenVo()
	{
		return this.formSpecimenVo;
	}

	public void setFormSpecimenVo(GenericSpecimen formSpecimenVo)
	{
		this.formSpecimenVo = formSpecimenVo;
	}

	public Specimen getCorresSpecimen()
	{
		return this.corresSpecimen;
	}

	public void setCorresSpecimen(Specimen corresSpecimen)
	{
		this.corresSpecimen = corresSpecimen;
	}

	public long getId()
	{
		return this.id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getClassName()
	{
		return this.className;
	}

	public void setClassName(String className)
	{
		this.className = className;
	}

	public String getType()
	{
		return this.type;
	}

	public void setType(String type)
	{
		this.type = type;
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

	public String getConcentration()
	{
		if (this.concentration == null)
		{
			this.concentration = "0";
		}
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

	public String getLineage()
	{
		return this.lineage;
	}

	public void setLineage(String lineage)
	{
		this.lineage = lineage;
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

	public String getReceivedEventReceivedQuality()
	{
		return this.receivedEventReceivedQuality;
	}

	public void setReceivedEventReceivedQuality(String receivedEventReceivedQuality)
	{
		this.receivedEventReceivedQuality = receivedEventReceivedQuality;
	}

	public int getNoOfDeriveSpecimen()
	{
		return this.noOfDeriveSpecimen;
	}

	public void setNoOfDeriveSpecimen(int noOfDeriveSpecimen)
	{
		this.noOfDeriveSpecimen = noOfDeriveSpecimen;
	}

	public Map<String, GenericSpecimen> getDeriveSpecimen()
	{
		return this.deriveSpecimen;
	}

	public void setDeriveSpecimen(Map<String, GenericSpecimen> deriveSpecimen)
	{
		this.deriveSpecimen = deriveSpecimen;
	}

	public String getDeriveClassName()
	{
		return this.deriveClassName;
	}

	public void setDeriveClassName(String deriveClassName)
	{
		this.deriveClassName = deriveClassName;
	}

	public String getDeriveType()
	{
		return this.deriveType;
	}

	public void setDeriveType(String deriveType)
	{
		this.deriveType = deriveType;
	}

	public String getDeriveConcentration()
	{
		return this.deriveConcentration;
	}

	public void setDeriveConcentration(String deriveConcentration)
	{
		this.deriveConcentration = deriveConcentration;
	}

	public String getDeriveQuantity()
	{
		return this.deriveQuantity;
	}

	public void setDeriveQuantity(String deriveQuantity)
	{
		this.deriveQuantity = deriveQuantity;
	}

	public static long getSerialVersionUID()
	{
		return serialVersionUID;
	}

	public LinkedHashMap<String, GenericSpecimen> getAliquotSpecimenCollection()
	{
		return (LinkedHashMap<String, GenericSpecimen>) this.aliquotSpecimenCollection;
	}

	public void addAliquotSpecimenBean(SpecimenRequirementBean specimenRequirementBean)
	{
		this.aliquotSpecimenCollection.put(specimenRequirementBean.getUniqueIdentifier(),
				specimenRequirementBean);
	}

	public long getCollectionEventId()
	{
		return this.collectionEventId;
	}

	public void setCollectionEventId(long collectionEventId)
	{
		this.collectionEventId = collectionEventId;
	}

	public long getCollectionEventSpecimenId()
	{
		return this.collectionEventSpecimenId;
	}

	public void setCollectionEventSpecimenId(long collectionEventSpecimenId)
	{
		this.collectionEventSpecimenId = collectionEventSpecimenId;
	}

	public LinkedHashMap<String, GenericSpecimen> getDeriveSpecimenCollection()
	{
		return (LinkedHashMap<String, GenericSpecimen>) this.deriveSpecimenCollection;
	}

	public String getDisplayName()
	{
		return this.displayName;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	public String getStorageContainerForSpecimen()
	{
		return this.storageContainerForSpecimen;
	}

	public void setStorageContainerForSpecimen(String storageContainerForSpecimen)
	{
		this.storageContainerForSpecimen = storageContainerForSpecimen;
	}

	public String getStorageContainerForAliquotSpecimem()
	{
		return this.storageContainerForAliquotSpecimem;
	}

	public void setStorageContainerForAliquotSpecimem(String storageContainerForAliquotSpecimem)
	{
		this.storageContainerForAliquotSpecimem = storageContainerForAliquotSpecimem;
	}

	public long getCollectionEventUserId()
	{
		return this.collectionEventUserId;
	}

	public void setCollectionEventUserId(long collectionEventUserId)
	{
		this.collectionEventUserId = collectionEventUserId;
	}

	public long getReceivedEventId()
	{
		return this.receivedEventId;
	}

	public void setReceivedEventId(long receivedEventId)
	{
		this.receivedEventId = receivedEventId;
	}

	public long getReceivedEventSpecimenId()
	{
		return this.receivedEventSpecimenId;
	}

	public void setReceivedEventSpecimenId(long receivedEventSpecimenId)
	{
		this.receivedEventSpecimenId = receivedEventSpecimenId;
	}

	public long getReceivedEventUserId()
	{
		return this.receivedEventUserId;
	}

	public void setReceivedEventUserId(long receivedEventUserId)
	{
		this.receivedEventUserId = receivedEventUserId;
	}

	public String getUniqueIdentifier()
	{
		return this.uniqueIdentifier;
	}

	public void setUniqueIdentifier(String uniqueIdentifier)
	{
		this.uniqueIdentifier = uniqueIdentifier;
	}

	public String getParentName()
	{
		return this.parentName;
	}

	public void setParentName(String parentName)
	{
		this.parentName = parentName;
	}

	public long getSpecimenCharsId()
	{
		return this.specimenCharsId;
	}

	public void setSpecimenCharsId(long specimenCharsId)
	{
		this.specimenCharsId = specimenCharsId;
	}

	public String getBarCode()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getCheckedSpecimen()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public String getContainerId()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getPositionDimensionOne()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getPositionDimensionTwo()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getReadOnly()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public String getSelectedContainerName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setAliquotSpecimenCollection(
			LinkedHashMap<String, GenericSpecimen> aliquotSpecimenCollection)
	{

		this.aliquotSpecimenCollection = aliquotSpecimenCollection;

	}

	public void setBarCode(String barCode)
	{
		// TODO Auto-generated method stub

	}

	public void setCheckedSpecimen(boolean checkedSpecimen)
	{
		// TODO Auto-generated method stub

	}

	public void setContainerId(String containerId)
	{
		// TODO Auto-generated method stub

	}

	public void setDeriveSpecimenCollection(
			LinkedHashMap<String, GenericSpecimen> deriveSpecimenCollection)
	{
		this.deriveSpecimenCollection = deriveSpecimenCollection;
	}

	public void setPositionDimensionOne(String positionDimensionOne)
	{
		// TODO Auto-generated method stub

	}

	public void setPositionDimensionTwo(String positionDimensionTwo)
	{
		// TODO Auto-generated method stub

	}

	public void setReadOnly(boolean readOnly)
	{
		// TODO Auto-generated method stub

	}

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

	public boolean getShowBarcode()
	{
		// TODO Auto-generated method stub
		return this.showBarcode;
	}

	public boolean getShowLabel()
	{
		// TODO Auto-generated method stub
		return this.showLabel;
	}

	public void setShowBarcode(boolean showBarcode)
	{
		this.showBarcode = showBarcode;

	}

	public void setShowLabel(boolean showLabel)
	{
		this.showLabel = showLabel;

	}

	public boolean getPrintSpecimen()
	{
		return false;
	}

	public void setPrintSpecimen(boolean printSpecimen)
	{

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

	/**
	 * For SCG labeling,this will be exposed through API and not in the model.
	 */
	private String labelFormat;

	/**
	 * For SCG labeling,this will be exposed through API and not in the model.
	 */
	public String getLabelFormat()
	{
		return this.labelFormat;
	}

	/**
	 * For SCG labeling,this will be exposed through API and not in the model.
	 */
	public void setLabelFormat(String labelFormat)
	{
		this.labelFormat = labelFormat;
	}

}
