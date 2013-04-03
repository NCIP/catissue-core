/**
 */

package edu.wustl.catissuecore.dto;



public class OrderErrorDTO {
	private String specimenLabel;
	private String error;
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getSpecimenLabel() {
		return specimenLabel;
	}
	public void setSpecimenLabel(String specimenLabel) {
		this.specimenLabel = specimenLabel;
	}
	
	
}
