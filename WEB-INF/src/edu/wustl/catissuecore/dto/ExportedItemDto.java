
package edu.wustl.catissuecore.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import edu.emory.mathcs.backport.java.util.Arrays;

public class ExportedItemDto {

	private List<String> exportedItems = new ArrayList<String>();

	public ExportedItemDto(String exportItems){
		if(StringUtils.isEmpty(exportItems)){
			exportedItems.add("Label");
			exportedItems.add("Available Quantity");
			exportedItems.add("Storage Container");
			exportedItems.add("Specimen Type");
			exportedItems.add("Specimen Class");
		}
		else{
			String[] arr = exportItems.split(",");
			exportedItems.addAll(Arrays.asList(arr));
		}
	}
	
	public boolean isFirstName() {
		return exportedItems.contains("First Name");
	}

	public boolean isLastName() {
		return exportedItems.contains("Last Name");
	}

	public boolean isMiddleName() {
		return exportedItems.contains("Middle Name");
	}

	public boolean isBirthDate() {
		return exportedItems.contains("Birth Date");
	}

	public boolean isDeathDate() {
		return exportedItems.contains("Death Date");
	}

	public boolean isGender() {
		return exportedItems.contains("Gender");
	}

	public boolean isRace() {
		return exportedItems.contains("Race");
	}

	public boolean isVitalSatus() {
		return exportedItems.contains("Vital Status");
	}

	public boolean isMrn() {
		return exportedItems.contains("Medical Record Number");
	}

	public boolean isSite() {
		return exportedItems.contains("Site");
	}

	public boolean isEthnicity() {
		return exportedItems.contains("Ethnicity");
	}

	public boolean isRegistrationDate() {
		return exportedItems.contains("Registration Date");
	}

	public boolean isPPI() {
		return exportedItems.contains("PPI");
	}

	public boolean isShortTitle() {
		return exportedItems.contains("Protocol Short Title");
	}

	public boolean isTitle() {
		return exportedItems.contains("Protocol Title");
	}

	public boolean isScgName() {
		return exportedItems.contains("SCG Name");
	}

	public boolean isClinicalDiagnosis() {
		return exportedItems.contains("Clinical Diagnosis");
	}

	public boolean isClinicalStatus() {
		return exportedItems.contains("Clinical Status");
	}

	public boolean isCollectionSite() {
		return exportedItems.contains("Collection Status");
	}

	public boolean isCpeLabel() {
		return exportedItems.contains("CPE Label");
	}

	public boolean isEventPoint() {
		return exportedItems.contains("StudyEventPoint");
	}

	public boolean isSpr() {
		return exportedItems.contains("SPR");
	}

	public boolean isSpLabel() {
		return exportedItems.contains("Label");
	}

	public boolean isTissueSite() {
		return exportedItems.contains("Tissue Site");
	}

	public boolean isTissueSide() {
		return exportedItems.contains("Tissue Side");
	}

	public boolean isAvlQty() {
		return exportedItems.contains("Available Quantity");
	}

	public boolean isDistriQty() {
		return exportedItems.contains("Distribution Quantity");
	}

	public boolean isExternalId() {
		return exportedItems.contains("External ID");
	}

	public boolean isExternalValue() {
		return exportedItems.contains("External Value");
	}

	public boolean isSpType() {
		return exportedItems.contains("Specimen Type");
	}

	public boolean isSpClass() {
		return exportedItems.contains("Class");
	}
	
	public boolean isContainers() {
		return exportedItems.contains("Container");
	}

	
	public List<String> getExportedItems() {
		return exportedItems;
	}

	
	public void setExportedItems(List<String> exportedItems) {
		this.exportedItems = exportedItems;
	}
	
	
}
