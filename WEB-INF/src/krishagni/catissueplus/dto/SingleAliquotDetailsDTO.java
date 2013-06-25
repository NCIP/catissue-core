
package krishagni.catissueplus.dto;

public class SingleAliquotDetailsDTO
{

	Double quantity;
	String aliqoutLabel;
	String storagecontainer;
	String pos1;
	String pos2;
	String barCode;
	Long aliquotId;

	public Long getAliquotId()
	{
		return aliquotId;
	}

	public void setAliquotId(Long aliquotId)
	{
		this.aliquotId = aliquotId;
	}

	public String getBarCode()
	{
		return barCode;
	}

	public void setBarCode(String barCode)
	{
		this.barCode = barCode;
	}

	public Double getQuantity()
	{
		return quantity;
	}

	public void setQuantity(Double quantity)
	{
		this.quantity = quantity;
	}

	public String getAliqoutLabel()
	{
		return aliqoutLabel;
	}

	public void setAliqoutLabel(String aliqoutLabel)
	{
		this.aliqoutLabel = aliqoutLabel;
	}

	public String getStoragecontainer()
	{
		return storagecontainer;
	}

	public void setStoragecontainer(String storagecontainer)
	{
		this.storagecontainer = storagecontainer;
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

}
