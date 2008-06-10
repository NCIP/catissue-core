/**
 * 
 */
package edu.wustl.catissuecore.domain;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.exception.AssignDataException;


/**
 * @author ashish_gupta
 * @hibernate.class table="CATISSUE_CONTAINER_POSITION"
 *
 */
public class ContainerPosition extends AbstractPosition
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4331441815323965860L;
	protected Container occupiedContainer;
	protected Container parentContainer;
	
	
	
	
	/**
	 * @return the parentContainer
	 * @hibernate.many-to-one column="PARENT_CONTAINER_ID" class="edu.wustl.catissuecore.domain.Container" constrained="true"
	 */
	public Container getParentContainer()
	{
		return parentContainer;
	}

	
	/**
	 * @param parentContainer the parentContainer to set
	 */
	public void setParentContainer(Container parentContainer)
	{
		this.parentContainer = parentContainer;
	}

	/**
	 * @return the occupiedContainer
	 * @hibernate.many-to-one column="OCCUPIED_CONTAINER_ID" class="edu.wustl.catissuecore.domain.Container" constrained="true"
	 */
	public Container getOccupiedContainer()
	{
		return occupiedContainer;
	}
	
	/**
	 * @param occupiedContainer the occupiedContainer to set
	 */
	public void setOccupiedContainer(Container occupiedContainer)
	{
		this.occupiedContainer = occupiedContainer;
	}


	@Override
	public void setAllValues(IValueObject arg0) throws AssignDataException
	{
		// TODO Auto-generated method stub
		
	}
	
	
}
