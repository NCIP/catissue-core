package edu.wustl.catissuecore.bean;

import java.util.LinkedHashMap;

/**
 * @author abhijit_naik
 *
 */
public interface GenericSpecimen {

	public String getUniqueIdentifier();
	public String getSpecimenLabel();
	public void setSpecimenLabel(String label);
	public String getSpecimenClassName();
	public String getSpecimenType();
	public String getTissueSite();
	public String getTissueSide();
	public String getPathologyStatus();
	public String getStorage();
	public String getQuantity();
	public String getConcentration();
	public String getParentName();
	public LinkedHashMap<String, GenericSpecimen> getAliquotes();
	public LinkedHashMap<String, GenericSpecimen> getDerived();
}
