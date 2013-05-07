package krishagni.catissueplus.mobile.dto;

import java.util.Date;

public class AliquotsDetailsDTO {
	private String containerName;
	private int noOfAliquots;
	private Double quantityPerAliquot;
	private String startingStoragePositionX;
	private String startingStoragePositionY;
	private Date createdDate;
	private String parentSpecimenLabel;
	private Double availableQuantity;
	private boolean basedOnCP;
	private boolean disposeParentCheck;
	private boolean printLabel;
	
	public boolean isPrintLabel() {
		return printLabel;
	}
	public void setPrintLabel(boolean printLabel) {
		this.printLabel = printLabel;
	}
	public boolean isDisposeParentCheck() {
		return disposeParentCheck;
	}
	public void setDisposeParentCheck(boolean disposeParentCheck) {
		this.disposeParentCheck = disposeParentCheck;
	}
	public boolean isBasedOnCP() {
		return basedOnCP;
	}
	public void setBasedOnCP(boolean basedOnCP) {
		this.basedOnCP = basedOnCP;
	}
	public Double getAvailableQuantity() {
		return availableQuantity;
	}
	public void setAvailableQuantity(Double availableQuantity) {
		this.availableQuantity = availableQuantity;
	}
	
	public String getParentSpecimenLabel() {
		return parentSpecimenLabel;
	}
	public void setParentSpecimenLabel(String parentSpecimenLabel) {
		this.parentSpecimenLabel = parentSpecimenLabel;
	}
	
	
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getContainerName() {
		return containerName;
	}
	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}
	public int getNoOfAliquots() {
		return noOfAliquots;
	}
	public void setNoOfAliquots(int noOfAliquots) {
		this.noOfAliquots = noOfAliquots;
	}
	public Double getQuantityPerAliquot() {
		return quantityPerAliquot;
	}
	public void setQuantityPerAliquot(Double quantityPerAliquot) {
		this.quantityPerAliquot = quantityPerAliquot;
	}
	public String getStartingStoragePositionX() {
		return startingStoragePositionX;
	}
	public void setStartingStoragePositionX(String startingStoragePositionX) {
		this.startingStoragePositionX = startingStoragePositionX;
	}
	public String getStartingStoragePositionY() {
		return startingStoragePositionY;
	}
	public void setStartingStoragePositionY(String startingStoragePositionY) {
		this.startingStoragePositionY = startingStoragePositionY;
	}
	

}
