
package krishagni.catissueplus.dto;

import edu.wustl.catissuecore.domain.Biohazard;

/**
 * DTO for Biohazard.
 * @author Ashraf
 *
 */
public class BiohazardDTO
{

	private Long id;
	private String name;
	private String type;
	private String status;

	/**
	 * @return the id
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 37;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}

		if (obj instanceof Biohazard)
		{
			Biohazard biohazard = (Biohazard) obj;
			if (this.id.compareTo(biohazard.getId()) == 0)
				return true;
			else
				return false;
		}
		else if (!(obj instanceof BiohazardDTO))
		{
			return false;
		}
		BiohazardDTO other = (BiohazardDTO) obj;
		if (id == null)
		{
			if (other.id != null)
			{
				return false;
			}
		}
		else if (!id.equals(other.id))
		{
			return false;
		}
		return true;
	}

	//	/**
	//	 * @param status the status to set
	//	 */
	//	public void setStatus(String status)
	//	{
	//		this.status = status;
	//	}
	//
	//	/* (non-Javadoc)
	//	 * @see java.lang.Object#hashCode()
	//	 */
	//	@Override
	//	public int hashCode()
	//	{
	//		int hash = 7;
	//		hash = 89 * hash + (this.id != null ? this.id.hashCode() : 0);
	//		return hash;
	//	}
	//
	//	/* (non-Javadoc)
	//	 * @see java.lang.Object#equals(java.lang.Object)
	//	 */
	//	@Override
	//	public boolean equals(Object obj)
	//	{
	//		if (obj == null)
	//			return false;
	//		else if (obj instanceof Biohazard)
	//		{
	//			Biohazard biohazard = (Biohazard) obj;
	//			if (this.id.compareTo(biohazard.getId()) == 0)
	//				return true;
	//		}
	//		else
	//		{
	//			BiohazardDTO biohazardDTO = (BiohazardDTO) obj;
	//			if (this.id == biohazardDTO.getId())
	//				return true;
	//		}
	//		return false;
	//	}

}
