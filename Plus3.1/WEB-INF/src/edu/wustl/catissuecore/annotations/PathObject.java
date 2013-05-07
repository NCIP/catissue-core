
package edu.wustl.catissuecore.annotations;

import java.io.Serializable;

import edu.common.dynamicextensions.domaininterface.EntityInterface;

/**
 * @author ashish_gupta
 *
 */
public class PathObject implements Serializable
{

	private static final long serialVersionUID = 1L;
	EntityInterface sourceEntity;
	EntityInterface targetEntity;

	/**
	 * This method overrides the equals method of the Object Class.
	 * This method checks whether path has been added between the source entity and target entity.
	 * @param obj - Object
	 * @return boolean true if path has been added else false.
	 */
	@Override
	public boolean equals(Object obj)
	{

		boolean equals = false;
		if (obj instanceof PathObject)
		{

			final PathObject pathObject = (PathObject) obj;
			if (pathObject.getSourceEntity() != null && pathObject.getTargetEntity() != null
					&& pathObject.getSourceEntity().getId() != null
					&& pathObject.getTargetEntity().getId() != null && this.sourceEntity != null
					&& this.targetEntity != null && this.sourceEntity.getId() != null
					&& this.targetEntity.getId() != null)
			{

				if ((this.sourceEntity.getId().compareTo(pathObject.getSourceEntity().getId()) == 0)
						&& (this.targetEntity.getId().compareTo(
								pathObject.getTargetEntity().getId()) == 0))
				{

					equals = true;
				}
			}
		}
		return equals;
	}

	/**
	 * @return the sourceEntity
	 */
	public EntityInterface getSourceEntity()
	{
		return this.sourceEntity;
	}

	/**
	 * @param sourceEntityParam the sourceEntity to set
	 */
	public void setSourceEntity(EntityInterface sourceEntityParam)
	{
		this.sourceEntity = sourceEntityParam;
	}

	/**
	 * @return the targetEntity
	 */
	public EntityInterface getTargetEntity()
	{
		return this.targetEntity;
	}

	/**
	 * @param targetEntityParam the targetEntity to set
	 */
	public void setTargetEntity(EntityInterface targetEntityParam)
	{
		this.targetEntity = targetEntityParam;
	}

	/**
	 * @return - int
	 */
	@Override
	public int hashCode()
	{

		return 1;
	}
}
