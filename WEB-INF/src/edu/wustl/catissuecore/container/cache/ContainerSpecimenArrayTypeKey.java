package edu.wustl.catissuecore.container.cache;



 public class ContainerSpecimenArrayTypeKey implements IContainerCacheKey
{

	private static final long serialVersionUID = 1L;

	private Long specimenArrayTypeID;
	private static SpecimenArrayTypeMap specimenArrayTypeMap = (SpecimenArrayTypeMap)SpecimenArrayTypeMap.getInstance();

	public ContainerSpecimenArrayTypeKey(Long specimenArrayTypeID)
	{
		this.specimenArrayTypeID=specimenArrayTypeID;
	}
	
	public Long getSpecimenArrayTypeID() {
		return specimenArrayTypeID;
	}

	public void setSpecimenArrayTypeID(Long specimenArrayTypeID) {
		this.specimenArrayTypeID = specimenArrayTypeID;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{		
		return 1;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		boolean returnValue= false;
		if(obj instanceof ContainerSpecimenArrayTypeKey)
		{
			ContainerSpecimenArrayTypeKey key = (ContainerSpecimenArrayTypeKey)obj;
			if(key.getSpecimenArrayTypeID().intValue() == specimenArrayTypeID.intValue())
			{
				returnValue=true;
			}
		}	
		return returnValue;
	}
	
	
	public IContainerMap getCorrespondingContainerMap() 
	{
		return specimenArrayTypeMap;
	}
}