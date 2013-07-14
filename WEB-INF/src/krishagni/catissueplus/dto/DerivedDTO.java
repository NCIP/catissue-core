
package krishagni.catissueplus.dto;

import java.util.Collection;
import java.util.Date;


public class DerivedDTO
{

	private String parentSpecimenLabel;
	private Long parentSpecimenId;
	private String parentSpecimenBarcode;
	private String className;
	private String type;
	private Double concentration;
	private Double initialQuantity;
	private Date createdOn;
	private String comments;
	private String containerName;
	private Long containerId;
	private String pos1;
	private String pos2;
	private Collection<ExternalIdentifierDTO> externalIdentifiers;
	private boolean isToPrintLabel;
	private boolean disposeParentSpecimen;
	private String label;
	private String barcode;
	private Long specimenCollGroupId;

	
	
	public Long getSpecimenCollGroupId()
	{
		return specimenCollGroupId;
	}


	
	public void setSpecimenCollGroupId(Long specimenCollGroupId)
	{
		this.specimenCollGroupId = specimenCollGroupId;
	}


	public String getLabel()
	{
		return label;
	}

	
	public void setLabel(String label)
	{
		this.label = label;
	}

	
	public String getBarcode()
	{
		return barcode;
	}

	
	public void setBarcode(String barcode)
	{
		this.barcode = barcode;
	}

	public boolean isDisposeParentSpecimen()
	{
		return disposeParentSpecimen;
	}

	public void setDisposeParentSpecimen(boolean disposeParentSpecimen)
	{
		this.disposeParentSpecimen = disposeParentSpecimen;
	}

	public String getParentSpecimenLabel()
	{
		return parentSpecimenLabel;
	}

	public void setParentSpecimenLabel(String parentSpecimenLabel)
	{
		this.parentSpecimenLabel = parentSpecimenLabel;
	}

	public Long getParentSpecimenId()
	{
		return parentSpecimenId;
	}

	public void setParentSpecimenId(Long parentSpecimenId)
	{
		this.parentSpecimenId = parentSpecimenId;
	}

	public String getParentSpecimenBarcode()
	{
		return parentSpecimenBarcode;
	}

	public void setParentSpecimenBarcode(String parentSpecimenBarcode)
	{
		this.parentSpecimenBarcode = parentSpecimenBarcode;
	}

	public String getClassName()
	{
		return className;
	}

	public void setClassName(String className)
	{
		this.className = className;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public Double getConcentration()
	{
		return concentration;
	}

	public void setConcentration(Double concentration)
	{
		this.concentration = concentration;
	}

	public Double getInitialQuantity()
	{
		return initialQuantity;
	}

	public void setInitialQuantity(Double initialQuantity)
	{
		this.initialQuantity = initialQuantity;
	}

	public Date getCreatedOn()
	{
		return createdOn;
	}

	public void setCreatedOn(Date createdOn)
	{
		this.createdOn = createdOn;
	}

	public String getComments()
	{
		return comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}

	public String getContainerName()
	{
		return containerName;
	}

	public void setContainerName(String containerName)
	{
		this.containerName = containerName;
	}

	public Long getContainerId()
	{
		return containerId;
	}

	public void setContainerId(Long containerId)
	{
		this.containerId = containerId;
	}

	public String getPos1()
	{
		return pos1;
	}

	public void setPos1(String pos1)
	{
		this.pos1 = pos1;
	}

	public String getPos2()
	{
		return pos2;
	}

	public void setPos2(String pos2)
	{
		this.pos2 = pos2;
	}

	public Collection<ExternalIdentifierDTO> getExternalIdentifiers()
	{
		return externalIdentifiers;
	}

	public void setExternalIdentifiers(Collection<ExternalIdentifierDTO> externalIdentifiers)
	{
		this.externalIdentifiers = externalIdentifiers;
	}

	public boolean getIsToPrintLabel()
	{
		return isToPrintLabel;
	}

	public void setToPrintLabel(boolean isToPrintLabel)
	{
		this.isToPrintLabel = isToPrintLabel;
	}

}
