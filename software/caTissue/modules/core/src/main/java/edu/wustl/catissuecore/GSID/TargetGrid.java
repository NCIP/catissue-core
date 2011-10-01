package edu.wustl.catissuecore.GSID;


public enum TargetGrid {
	TRAINING("training"), NCI_PROD("nci_prod"), NCI_QA("nci_qa"), NCI_STAGE("nci_stage");
	
	String target ; 
	private TargetGrid (String target) {
		this.target=target;
	}
	
	public String toString() {
		return target;
	}
	
	public static TargetGrid byName(String name) {
		for (TargetGrid grid : values()) {
			if (grid.target.equals(name)) {
				return grid;
			}
		}
		return null;
	}
}
