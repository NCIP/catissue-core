package edu.wustl.catissuecore.bizlogic.magetab;

public class TransformerSelections {
	private String name;
	private String userFriendlyName;
	private String localName;
	private boolean selectedForSource;
	private boolean selectedForSample;
	private boolean selectedForExtract;
	
	public TransformerSelections() {
		
	}
	
	public TransformerSelections(String name, String userFriendlyName, String localName,
			boolean selectedForSource, boolean selectedForSample, boolean selectedForExtract) {
		this.name = name;
		this.userFriendlyName = userFriendlyName;
		this.localName=localName;
		this.selectedForSource = selectedForSource;
		this.selectedForSample = selectedForSample;
		this.selectedForExtract = selectedForExtract;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserFriendlyName() {
		return userFriendlyName;
	}
	public void setUserFriendlyName(String userFriendlyName) {
		this.userFriendlyName = userFriendlyName;
	}
	public boolean isSelectedForSource() {
		return selectedForSource;
	}
	public void setSelectedForSource(boolean selectedForSource) {
		this.selectedForSource = selectedForSource;
	}
	public boolean isSelectedForSample() {
		return selectedForSample;
	}
	public void setSelectedForSample(boolean selectedForSample) {
		this.selectedForSample = selectedForSample;
	}
	public boolean isSelectedForExtract() {
		return selectedForExtract;
	}
	public void setSelectedForExtract(boolean selectedForExtract) {
		this.selectedForExtract = selectedForExtract;
	}
	public String getLocalName() {
		return localName;
	}
	public void setLocalName(String localName) {
		this.localName = localName;
	}	
}
