package edu.wustl.catissuecore.bean;

import java.util.LinkedHashMap;

/**
 * @author abhijit_naik
 *
 */
public interface GenericSpecimen 
{

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
}
