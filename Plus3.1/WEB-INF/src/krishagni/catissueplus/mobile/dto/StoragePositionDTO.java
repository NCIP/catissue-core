package krishagni.catissueplus.mobile.dto;

public class StoragePositionDTO {
	private Long specimenId;
	private String specimenLabel;
	private Long childContainerId;
	private String childContainerLabel;
	private String containerName;
	private String containerId;
	private int posx;
	private int posy;
	private SpecimenDTO specimenDTO;
	private int childContainerPosxCap;
	private int childContainerPosyCap;
	
	
	
	
	
	
	public int getChildContainerPosxCap() {
		return childContainerPosxCap;
	}
	public void setChildContainerPosxCap(int childContainerPosxCap) {
		this.childContainerPosxCap = childContainerPosxCap;
	}
	public int getChildContainerPosyCap() {
		return childContainerPosyCap;
	}
	public void setChildContainerPosyCap(int childContainerPosyCap) {
		this.childContainerPosyCap = childContainerPosyCap;
	}
	public SpecimenDTO getSpecimenDTO() {
		return specimenDTO;
	}
	public void setSpecimenDTO(SpecimenDTO specimenDTO) {
		this.specimenDTO = specimenDTO;
	}
	public Long getSpecimenId() {
		return specimenId;
	}
	public void setSpecimenId(Long specimenId) {
		this.specimenId = specimenId;
	}
	public String getSpecimenLabel() {
		return specimenLabel;
	}
	public void setSpecimenLabel(String specimenLabel) {
		this.specimenLabel = specimenLabel;
	}
	public Long getChildContainerId() {
		return childContainerId;
	}
	public void setChildContainerId(Long childContainerId) {
		this.childContainerId = childContainerId;
	}
	public String getChildContainerLabel() {
		return childContainerLabel;
	}
	public void setChildContainerLabel(String childContainerLabel) {
		this.childContainerLabel = childContainerLabel;
	}
	public String getContainerName() {
		return containerName;
	}
	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}
	public String getContainerId() {
		return containerId;
	}
	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}
	public int getPosx() {
		return posx;
	}
	public void setPosx(int posx) {
		this.posx = posx;
	}
	public int getPosy() {
		return posy;
	}
	public void setPosy(int posy) {
		this.posy = posy;
	}
}
