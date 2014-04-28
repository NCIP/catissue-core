
package krishagni.catissueplus.dto;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class AliquotDetailsDTO
{

	private String specimenClass;
	private String type;
	private String tissueSite;
	private String tissueSide;
	private String pathologicalStatus;
	private Double concentration;
	private Double initialAvailableQuantity;
	private Double currentAvailableQuantity;
	private List<SingleAliquotDetailsDTO> perAliquotDetailsCollection;
	private String parentLabel;
	private String parentBarcode;
	private Long parentId;
	private Long scgId;
	private Date creationDate;
	private boolean disposeParentSpecimen;
	private boolean printLabel;
	private boolean storeAllAliquotInSameContainer;

	public String getParentBarcode()
	{
		return parentBarcode;
	}

	public void setParentBarcode(String parentBarcode)
	{
		this.parentBarcode = parentBarcode;
	}

	public boolean isStoreAllAliquotInSameContainer()
	{
		return storeAllAliquotInSameContainer;
	}

	public void setStoreAllAliquotInSameContainer(boolean storeAllAliquotInSameContainer)
	{
		this.storeAllAliquotInSameContainer = storeAllAliquotInSameContainer;
	}

	public String getSpecimenClass()
	{
		return specimenClass;
	}

	public void setSpecimenClass(String specimenClass)
	{
		this.specimenClass = specimenClass;
	}

	public boolean isDisposeParentSpecimen()
	{
		return disposeParentSpecimen;
	}

	public void setDisposeParentSpecimen(boolean disposeParentSpecimen)
	{
		this.disposeParentSpecimen = disposeParentSpecimen;
	}

	public boolean isPrintLabel()
	{
		return printLabel;
	}

	public void setPrintLabel(boolean printLabel)
	{
		this.printLabel = printLabel;
	}

	public String getParentLabel()
	{
		return parentLabel;
	}

	public void setParentLabel(String parentLabel)
	{
		this.parentLabel = parentLabel;
	}

	public Long getParentId()
	{
		return parentId;
	}

	public void setParentId(Long parentId)
	{
		this.parentId = parentId;
	}
	
	public Long getScgId() 
	{
		return scgId;
	}

	public void setScgId(Long scgId) 
	{
		this.scgId = scgId;
	}

	public Date getCreationDate()
	{
		return creationDate;
	}

	public void setCreationDate(Date creationDate)
	{
		this.creationDate = creationDate;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getTissueSite()
	{
		return tissueSite;
	}

	public void setTissueSite(String tissueSite)
	{
		this.tissueSite = tissueSite;
	}

	public String getTissueSide()
	{
		return tissueSide;
	}

	public void setTissueSide(String tissueSide)
	{
		this.tissueSide = tissueSide;
	}

	public String getPathologicalStatus()
	{
		return pathologicalStatus;
	}

	public void setPathologicalStatus(String pathologicalStatus)
	{
		this.pathologicalStatus = pathologicalStatus;
	}

	public Double getConcentration()
	{
		return concentration;
	}

	public void setConcentration(Double concentration)
	{
		this.concentration = concentration;
	}

	public Double getInitialAvailableQuantity()
	{
		return initialAvailableQuantity;
	}

	public void setInitialAvailableQuantity(Double initialAvailableQuantity)
	{
		this.initialAvailableQuantity = initialAvailableQuantity;
	}

	public Double getCurrentAvailableQuantity()
	{
		return currentAvailableQuantity;
	}

	public void setCurrentAvailableQuantity(Double currentAvailableQuantity)
	{
		this.currentAvailableQuantity = currentAvailableQuantity;
	}

	public List<SingleAliquotDetailsDTO> getPerAliquotDetailsCollection()
	{
		return perAliquotDetailsCollection;
	}

	public void setPerAliquotDetailsCollection(
			List<SingleAliquotDetailsDTO> perAliquotDetailsCollection)
	{
		this.perAliquotDetailsCollection = perAliquotDetailsCollection;
	}
}
