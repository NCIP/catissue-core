
package edu.wustl.catissuecore.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;

import edu.wustl.catissuecore.domain.Specimen;

// TODO: Auto-generated Javadoc
/**
 * The Interface GenericSpecimen.
 *
 * @author abhijit_naik
 */
public interface GenericSpecimen extends Serializable
{

	/**
	 * Gets the aliquot specimen collection.
	 *
	 * @return the aliquot specimen collection
	 */
	public LinkedHashMap<String, GenericSpecimen> getAliquotSpecimenCollection();

	/**
	 * Gets the class name.
	 *
	 * @return the class name
	 */
	public String getClassName();

	/**
	 * Gets the concentration.
	 *
	 * @return the concentration
	 */
	public String getConcentration();

	/**
	 * Gets the derive specimen collection.
	 *
	 * @return the derive specimen collection
	 */
	public LinkedHashMap<String, GenericSpecimen> getDeriveSpecimenCollection();

	/**
	 * Gets the display name.
	 *
	 * @return the display name
	 */
	public String getDisplayName();

	/**
	 * Gets the parent name.
	 *
	 * @return the parent name
	 */
	public String getParentName();

	/**
	 * Gets the pathological status.
	 *
	 * @return the pathological status
	 */
	public String getPathologicalStatus();

	/**
	 * Gets the quantity.
	 *
	 * @return the quantity
	 */
	public String getQuantity();

	/**
	 * Gets the storage container for specimen.
	 *
	 * @return the storage container for specimen
	 */
	public String getStorageContainerForSpecimen();

	/**
	 * Gets the tissue side.
	 *
	 * @return the tissue side
	 */
	public String getTissueSide();

	/**
	 * Gets the tissue site.
	 *
	 * @return the tissue site
	 */
	public String getTissueSite();

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType();

	/**
	 * Gets the unique identifier.
	 *
	 * @return the unique identifier
	 */
	public String getUniqueIdentifier();

	/**
	 * Sets the aliquot specimen collection.
	 *
	 * @param aliquotSpecimenCollection the aliquot specimen collection
	 */
	public void setAliquotSpecimenCollection(
			LinkedHashMap<String, GenericSpecimen> aliquotSpecimenCollection);

	public Specimen getParentSpecimen();

	public void setParentSpecimen(Specimen parentSpecimen);
	/**
	 * Sets the class name.
	 *
	 * @param className the new class name
	 */
	public void setClassName(String className);

	/**
	 * Sets the concentration.
	 *
	 * @param concentration the new concentration
	 */
	public void setConcentration(String concentration);

	/**
	 * Sets the derive specimen collection.
	 *
	 * @param deriveSpecimenCollection the derive specimen collection
	 */
	public void setDeriveSpecimenCollection(
			LinkedHashMap<String, GenericSpecimen> deriveSpecimenCollection);

	/**
	 * Sets the display name.
	 *
	 * @param displayName the new display name
	 */
	public void setDisplayName(String displayName);

	/**
	 * Sets the parent name.
	 *
	 * @param parentName the new parent name
	 */
	public void setParentName(String parentName);

	/**
	 * Sets the pathological status.
	 *
	 * @param pathologicalStatus the new pathological status
	 */
	public void setPathologicalStatus(String pathologicalStatus);

	/**
	 * Sets the quantity.
	 *
	 * @param quantity the new quantity
	 */
	public void setQuantity(String quantity);

	/**
	 * Sets the storage container for specimen.
	 *
	 * @param storageContainerForSpecimen the new storage container for specimen
	 */
	public void setStorageContainerForSpecimen(String storageContainerForSpecimen);

	/**
	 * Sets the tissue side.
	 *
	 * @param tissueSide the new tissue side
	 */
	public void setTissueSide(String tissueSide);

	/**
	 * Sets the tissue site.
	 *
	 * @param tissueSite the new tissue site
	 */
	public void setTissueSite(String tissueSite);

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type);

	/**
	 * Sets the unique identifier.
	 *
	 * @param uniqueIdentifier the new unique identifier
	 */
	public void setUniqueIdentifier(String uniqueIdentifier);

	/**
	 * Gets the checked specimen.
	 *
	 * @return the checked specimen
	 */
	public boolean getCheckedSpecimen();

	/**
	 * Sets the checked specimen.
	 *
	 * @param checkedSpecimen the new checked specimen
	 */
	public void setCheckedSpecimen(boolean checkedSpecimen);

	/**
	 * Gets the prints the specimen.
	 *
	 * @return the prints the specimen
	 */
	public boolean getPrintSpecimen();//bug 11169

	/**
	 * Sets the prints the specimen.
	 *
	 * @param printSpecimen the new prints the specimen
	 */
	public void setPrintSpecimen(boolean printSpecimen);//bug 11169

	/**
	 * Gets the read only.
	 *
	 * @return the read only
	 */
	public boolean getReadOnly();

	/**
	 * Sets the read only.
	 *
	 * @param readOnly the new read only
	 */
	public void setReadOnly(boolean readOnly);

	/**
	 * Gets the bar code.
	 *
	 * @return the bar code
	 */
	public String getBarCode();

	/**
	 * Sets the bar code.
	 *
	 * @param barCode the new bar code
	 */
	public void setBarCode(String barCode);

	/**
	 * Gets the selected container name.
	 *
	 * @return the selected container name
	 */
	public String getSelectedContainerName();

	/**
	 * Sets the selected container name.
	 *
	 * @param selectedContainerName the new selected container name
	 */
	public void setSelectedContainerName(String selectedContainerName);

	/**
	 * Gets the position dimension one.
	 *
	 * @return the position dimension one
	 */
	public String getPositionDimensionOne();

	/**
	 * Sets the position dimension one.
	 *
	 * @param positionDimensionOne the new position dimension one
	 */
	public void setPositionDimensionOne(String positionDimensionOne);

	/**
	 * Gets the position dimension two.
	 *
	 * @return the position dimension two
	 */
	public String getPositionDimensionTwo();

	/**
	 * Sets the position dimension two.
	 *
	 * @param positionDimensionTwo the new position dimension two
	 */
	public void setPositionDimensionTwo(String positionDimensionTwo);

	/**
	 * Gets the container id.
	 *
	 * @return the container id
	 */
	public String getContainerId();

	/**
	 * Sets the container id.
	 *
	 * @param containerId the new container id
	 */
	public void setContainerId(String containerId);

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public long getId();

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(long id);

	/**
	 * Gets the collection protocol id.
	 *
	 * @return the collection protocol id
	 */
	public Long getCollectionProtocolId();

	/**
	 * Sets the collection protocol id.
	 *
	 * @param collectionProtocolId the new collection protocol id
	 */
	public void setCollectionProtocolId(Long collectionProtocolId);

	/**
	 * Gets the form specimen vo.
	 *
	 * @return the form specimen vo
	 */
	public GenericSpecimen getFormSpecimenVo();

	/**
	 * Sets the form specimen vo.
	 *
	 * @param formSpecimenVo the new form specimen vo
	 */
	public void setFormSpecimenVo(GenericSpecimen formSpecimenVo);

	/**
	 * Gets the corres specimen.
	 *
	 * @return the corres specimen
	 */
	public Specimen getCorresSpecimen();

	/**
	 * Sets the corres specimen.
	 *
	 * @param corresSpecimen the new corres specimen
	 */
	public void setCorresSpecimen(Specimen corresSpecimen);

	/**
	 * Gets the show label.
	 *
	 * @return the show label
	 */
	public boolean getShowLabel();

	/**
	 * Sets the show label.
	 *
	 * @param showLabel the new show label
	 */
	public void setShowLabel(boolean showLabel);

	/**
	 * Gets the show barcode.
	 *
	 * @return the show barcode
	 */
	public boolean getShowBarcode();

	/**
	 * Sets the show barcode.
	 *
	 * @param showBarcode the new show barcode
	 */
	public void setShowBarcode(boolean showBarcode);

	/**
	 * Checks if is generate label.
	 *
	 * @return true, if is generate label
	 */
	public boolean isGenerateLabel();

	/**
	 * Sets the generate label.
	 *
	 * @param generateLabel the new generate label
	 */
	public void setGenerateLabel(boolean generateLabel);
	/*
		public String getUniqueIdentifier();
		public String getDisplayName();
		public String getClassName();
		public String getType();
		public String getTissueSite();
		public String getTissueSide();
		public String getPathologicalStatus();
		public String getStorageContainerForSpecimen();
		public String getQuantity();
		public String getConcentration();

		public String getParentName();

		public LinkedHashMap<String, GenericSpecimen> getAliquotSpecimenCollection();
		public LinkedHashMap<String, GenericSpecimen> getDeriveSpecimenCollection();
	*/
}
