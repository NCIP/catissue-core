/**
 * 
 */
package edu.wustl.catissuecore.bean;

import java.util.LinkedHashMap;

import edu.wustl.catissuecore.domain.Specimen;

/**
 * @author abhijit_naik
 *
 */
public final class GenericSpecimenVO implements GenericSpecimen {

	private  LinkedHashMap<String, GenericSpecimen> 
			aliquotSpecimenCollection = null;
	private String className = null;
	private String concentration = null;
	private LinkedHashMap<String, GenericSpecimen> deriveSpecimenCollection = null;
	private String displayName = null;
	private String parentName = null;
	private String pathologicalStatus = null;
	private String quantity = null;
	private String storageContainerForSpecimen = null;
	private String tissueSide = null;
	private String tissueSite = null;
	private String type = null;
	private String uniqueIdentifier = null;
	private boolean checkedSpecimen = false;
	private boolean printSpecimen = false;
	private boolean readOnly =false;
	private String barCode;
	private String selectedContainerName;
	private String positionDimensionOne;
	private String positionDimensionTwo;
	private String containerId;
	private Long collectionProtocolId = -1l;
	private long id =-1;
	private Specimen corresSpecimen;
	private GenericSpecimen formSpecimenVo;

	private boolean showBarcode = true;
	private boolean showLabel = true;
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

	public LinkedHashMap<String, GenericSpecimen> getAliquotSpecimenCollection() {
		return aliquotSpecimenCollection;
	}

	public String getClassName() {
		return className;
	}

	public String getConcentration() {
		return concentration;
	}

	public LinkedHashMap<String, GenericSpecimen> getDeriveSpecimenCollection() {
		return deriveSpecimenCollection;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getParentName() {
		return parentName;
	}

	public String getPathologicalStatus() {
		return pathologicalStatus;
	}

	public String getQuantity() {
		return quantity;
	}

	public String getStorageContainerForSpecimen() {
		return storageContainerForSpecimen;
	}

	public String getTissueSide() {
		return tissueSide;
	}

	public String getTissueSite() {
		return tissueSite;
	}

	public String getType() {
		return type;
	}

	public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	public void setAliquotSpecimenCollection(
			LinkedHashMap<String, GenericSpecimen> aliquotSpecimenCollection) {
		this.aliquotSpecimenCollection = aliquotSpecimenCollection;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setConcentration(String concentration) {
		this.concentration = concentration;
	}

	public void setDeriveSpecimenCollection(
			LinkedHashMap<String, GenericSpecimen> deriveSpecimenCollection) {
		this.deriveSpecimenCollection = deriveSpecimenCollection;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public void setPathologicalStatus(String pathologicalStatus) {
		this.pathologicalStatus = pathologicalStatus;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public void setStorageContainerForSpecimen(String storageContainerForSpecimen) {
		this.storageContainerForSpecimen = storageContainerForSpecimen;
	}

	public void setTissueSide(String tissueSide) {
		this.tissueSide = tissueSide;
	}

	public void setTissueSite(String tissueSite) {
		this.tissueSite = tissueSite;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUniqueIdentifier(String uniqueIdentifier) {
		this.uniqueIdentifier = uniqueIdentifier;
	}

	public boolean getCheckedSpecimen() {
		return checkedSpecimen;
	}

	public void setCheckedSpecimen(boolean checkedSpecimen) {
		this.checkedSpecimen = checkedSpecimen;
	}

	public boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getSelectedContainerName() {
		return selectedContainerName;
	}

	public void setSelectedContainerName(String selectedContainerName) {
		this.selectedContainerName = selectedContainerName;
	}

	public String getPositionDimensionOne() {
		return positionDimensionOne;
	}

	public void setPositionDimensionOne(String positionDimensionOne) {
		this.positionDimensionOne = positionDimensionOne;
	}

	public String getPositionDimensionTwo() {
		return positionDimensionTwo;
	}

	public void setPositionDimensionTwo(String positionDimensionTwo) {
		this.positionDimensionTwo = positionDimensionTwo;
	}

	public String getContainerId() {
		return containerId;
	}

	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}

	
	

	/**
	 * @return Returns the collectionProtocolId.
	 */
	public Long getCollectionProtocolId()
	{
		return collectionProtocolId;
	}

	/**
	 * @param collectionProtocolId The collectionProtocolId to set.
	 */
	public void setCollectionProtocolId(Long collectionProtocolId)
	{
		this.collectionProtocolId = collectionProtocolId;
	}

	/** (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getId()
	 */
	public long getId()
	{
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean getShowBarcode()
	{
		// TODO Auto-generated method stub
		return showBarcode;
	}

	public boolean getShowLabel()
	{
		// TODO Auto-generated method stub
		return showLabel;
	}

	public void setShowBarcode(boolean showBarcode)
	{
		this.showBarcode = showBarcode;	
		
	}

	public void setShowLabel(boolean showLabel)
	{
		this.showLabel = showLabel;
		
	}

	public boolean getPrintSpecimen() {
		// TODO Auto-generated method stub
		return this.printSpecimen;
	}

	public void setPrintSpecimen(boolean printSpecimen) 
	{
		this.printSpecimen = printSpecimen;
	}

	
}
