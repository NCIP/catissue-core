
package edu.wustl.catissuecore.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;

import edu.wustl.catissuecore.domain.Specimen;

/**
 * @author abhijit_naik
 *
 */
public interface GenericSpecimen extends Serializable
{

	public LinkedHashMap<String, GenericSpecimen> getAliquotSpecimenCollection();

	public String getClassName();

	public String getConcentration();

	public LinkedHashMap<String, GenericSpecimen> getDeriveSpecimenCollection();

	public String getDisplayName();

	public String getParentName();

	public String getPathologicalStatus();

	public String getQuantity();

	public String getStorageContainerForSpecimen();

	public String getTissueSide();

	public String getTissueSite();

	public String getType();

	public String getUniqueIdentifier();

	public void setAliquotSpecimenCollection(
			LinkedHashMap<String, GenericSpecimen> aliquotSpecimenCollection);

	public void setClassName(String className);

	public void setConcentration(String concentration);

	public void setDeriveSpecimenCollection(
			LinkedHashMap<String, GenericSpecimen> deriveSpecimenCollection);

	public void setDisplayName(String displayName);

	public void setParentName(String parentName);

	public void setPathologicalStatus(String pathologicalStatus);

	public void setQuantity(String quantity);

	public void setStorageContainerForSpecimen(String storageContainerForSpecimen);

	public void setTissueSide(String tissueSide);

	public void setTissueSite(String tissueSite);

	public void setType(String type);

	public void setUniqueIdentifier(String uniqueIdentifier);

	public boolean getCheckedSpecimen();

	public void setCheckedSpecimen(boolean checkedSpecimen);

	public boolean getPrintSpecimen();//bug 11169

	public void setPrintSpecimen(boolean printSpecimen);//bug 11169

	public boolean getReadOnly();

	public void setReadOnly(boolean readOnly);

	public String getBarCode();

	public void setBarCode(String barCode);

	public String getSelectedContainerName();

	public void setSelectedContainerName(String selectedContainerName);

	public String getPositionDimensionOne();

	public void setPositionDimensionOne(String positionDimensionOne);

	public String getPositionDimensionTwo();

	public void setPositionDimensionTwo(String positionDimensionTwo);

	public String getContainerId();

	public void setContainerId(String containerId);

	public long getId();

	public void setId(long id);

	public Long getCollectionProtocolId();

	public void setCollectionProtocolId(Long collectionProtocolId);

	public GenericSpecimen getFormSpecimenVo();

	public void setFormSpecimenVo(GenericSpecimen formSpecimenVo);

	public Specimen getCorresSpecimen();

	public void setCorresSpecimen(Specimen corresSpecimen);

	public boolean getShowLabel();

	public void setShowLabel(boolean showLabel);

	public boolean getShowBarcode();

	public void setShowBarcode(boolean showBarcode);

	public boolean isGenerateLabel();

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
