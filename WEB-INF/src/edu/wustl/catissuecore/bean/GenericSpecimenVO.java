/**
 * 
 */
package edu.wustl.catissuecore.bean;

import java.util.LinkedHashMap;

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

}
