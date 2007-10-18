/**
 * 
 */

package edu.wustl.catissuecore.annotations;

import java.io.Serializable;

import edu.common.dynamicextensions.domaininterface.EntityInterface;

/**
 * @author ashish_gupta
 *
 */
public class PathObject implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	EntityInterface sourceEntity;
	EntityInterface targetEntity;

	/**
	 * This method overrides the equals method of the Object Class.
	 * This method checks whether path has been added between the source entity and target entity.
	 * @return boolean true if path has been added else false. 
	 */
	public boolean equals(Object obj)
	{

		boolean equals = false;
		if (obj instanceof PathObject)
		{

			PathObject pathObject = (PathObject) obj;
			if (pathObject.getSourceEntity() != null && pathObject.getTargetEntity() != null
					&& pathObject.getSourceEntity().getId() != null
					&& pathObject.getTargetEntity().getId() != null && sourceEntity != null
					&& targetEntity != null && sourceEntity.getId() != null
					&& targetEntity.getId() != null)
			{

				if ((sourceEntity.getId().compareTo(pathObject.getSourceEntity().getId()) == 0)
						&& (targetEntity.getId().compareTo(pathObject.getTargetEntity().getId()) == 0))
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
		return sourceEntity;
	}

	/**
	 * @param sourceEntity the sourceEntity to set
	 */
	public void setSourceEntity(EntityInterface sourceEntity)
	{
		this.sourceEntity = sourceEntity;
	}

	/**
	 * @return the targetEntity
	 */
	public EntityInterface getTargetEntity()
	{
		return targetEntity;
	}

	/**
	 * @param targetEntity the targetEntity to set
	 */
	public void setTargetEntity(EntityInterface targetEntity)
	{
		this.targetEntity = targetEntity;
	}

	public int hashCode()
	{

		return 1;
	}
}
