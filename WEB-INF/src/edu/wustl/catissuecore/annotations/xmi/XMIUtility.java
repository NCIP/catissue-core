/*
 * Created on Sep 27, 2007
 * @author
 *
 */

package edu.wustl.catissuecore.annotations.xmi;

import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domaininterface.AbstractEntityInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerConstantsInterface;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * @author preeti_lodha
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class XMIUtility
{

	private static EntityManagerInterface entityManager = EntityManager.getInstance();

	/**
	 * @param groupName - groupName
	 * @return - EntityGroupInterface
	 * @throws DynamicExtensionsSystemException - DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException - DynamicExtensionsApplicationException
	 */
	public static EntityGroupInterface getEntityGroup(String groupName)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		if (groupName != null)
		{
			//System.out.println( "Searching " + groupName );
			return entityManager.getEntityGroupByName( groupName );
		}
		return null;
	}

	/**
	 * @param entityGroup - entityGroup
	 * @throws DynamicExtensionsSystemException - DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException - DynamicExtensionsApplicationException
	 * @throws BizLogicException - BizLogicException
	 */
	/*public static void addHookEntitiesToGroup(EntityGroupInterface entityGroup)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException,
			BizLogicException
	{
		//ContainerInterface mainContainer = getMainContainer(entityGroup);
		final Collection < ContainerInterface > mainContainers = entityGroup
				.getMainContainerCollection();
		if (mainContainers != null)
		{
			final AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();
			for (final ContainerInterface mainContainer : mainContainers)
			{
				//System.out.println( mainContainer.getId() );
				final Collection entityMapsForContainer = annotationBizLogic
						.getEntityMapsForContainer( mainContainer.getId() );
				//System.out.println( "entityMapsForContainer " + entityMapsForContainer );
				if (entityMapsForContainer != null)
				{
					final Iterator entityMapsForContainerIter = entityMapsForContainer.iterator();
					//Add all associated static entities to uml model
					while (entityMapsForContainerIter.hasNext())
					{
						final Object entityMap = entityMapsForContainerIter.next();
						if (entityMap != null)
						{
							final Long staticEntityId = ( (EntityMap) entityMap )
									.getStaticEntityId();
							final EntityInterface staticEntity = entityManager
									.getEntityByIdentifier( staticEntityId );
							if (staticEntity != null)
							{
								EntityInterface xmiStaticEntity = null;
								final AssociationInterface association = getHookEntityAssociation(
										staticEntity, mainContainer.getAbstractEntity() );
								final Collection < EntityInterface > entityColl = entityGroup
										.getEntityCollection();
								for (final EntityInterface entity : entityColl)
								{
									if (entity.getId().compareTo( staticEntity.getId() ) == 0)
									{
										xmiStaticEntity = entity;
										break;
									}
								}

								if (xmiStaticEntity == null)
								{
									xmiStaticEntity = getHookEntityDetailsForXMI( staticEntity,
											mainContainer.getAbstractEntity() );
									entityGroup.addEntity( xmiStaticEntity );
								}
								else
								{
									xmiStaticEntity.addAssociation( association );
								}
							}
						}
					}
				}
			}
		}
	}*/

	/**
	 * @param srcEntity - srcEntity
	 * @param targetEntity - targetEntity
	 * @return - EntityInterface
	 * @throws DynamicExtensionsSystemException - DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException - DynamicExtensionsApplicationException
	 */
	public static EntityInterface getHookEntityDetailsForXMI(EntityInterface srcEntity,
			AbstractEntityInterface targetEntity) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		//For XMI : add only id , name and table properties
		final EntityInterface xmiEntity = new Entity();
		xmiEntity.setName( getHookEntityName( srcEntity.getName() ) );
		xmiEntity.setDescription( srcEntity.getDescription() );
		xmiEntity.setTableProperties( srcEntity.getTableProperties() );
		xmiEntity.setId( srcEntity.getId() );
		xmiEntity.addAttribute( getIdAttribute( srcEntity ) );
		xmiEntity.addAssociation( getHookEntityAssociation( srcEntity, targetEntity ) );

		return xmiEntity;
	}

	/**
	 * @param name - name
	 * @return - String
	 */
	public static String getHookEntityName(String name)
	{
		//Return last token from name
		String hookEntityname = null;
		final StringTokenizer strTokenizer = new StringTokenizer( name , "." );
		while (strTokenizer.hasMoreElements())
		{
			hookEntityname = strTokenizer.nextToken();
		}
		return hookEntityname;
	}

	/**
	 * @param srcEntity - srcEntity
	 * @param targetEntity - targetEntity
	 * @return -AssociationInterface
	 * @throws DynamicExtensionsApplicationException - DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException - DynamicExtensionsSystemException
	 */
	private static AssociationInterface getHookEntityAssociation(EntityInterface srcEntity,
			AbstractEntityInterface targetEntity) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		if (( srcEntity != null ) && ( targetEntity != null ))
		{
			final Collection < AssociationInterface > associations = srcEntity.getAllAssociations();
			if (associations != null)
			{
				final Iterator assocIter = associations.iterator();
				while (assocIter.hasNext())
				{
					final AssociationInterface association = (AssociationInterface) assocIter
							.next();
					if (association.getTargetEntity().equals( targetEntity ))
					{
						final String srcEntityName = getHookEntityName( srcEntity.getName() );
						//Change name of association
						association.setName( "Assoc_" + srcEntityName + "_"
								+ targetEntity.getName() );
						association.getSourceRole().setName( srcEntityName );
						association.getTargetRole().setName( targetEntity.getName() + "Collection" );
						return association;
					}
				}
				/*EntityManagerInterface entityManager = EntityManager.getInstance();
				Collection<AssociationInterface> associations = return entityManager.getAssociations(srcEntity.getId(), targetEntity.getId());*/

				/*if(associations!=null)
				{
					Iterator assocIter = associations.iterator();
					while(assocIter.hasNext())
					{
						AssociationInterface association = (AssociationInterface)assocIter.next();
						ConstraintPropertiesInterface constraintProperties = association.getConstraintProperties();
						if(constraintProperties!=null)
						{
							constraintProperties.setSourceEntityKey()
						}
					}
				}*/
			}
		}
		return null;
	}

	/**
	 * @param entity - entity
	 * @return - AttributeInterface
	 */
	public static AttributeInterface getIdAttribute(EntityInterface entity)
	{
		if (entity != null)
		{
			final Collection < AttributeInterface > attributes = entity.getAllAttributes();
			if (attributes != null)
			{
				final Iterator attributesIter = attributes.iterator();
				while (attributesIter.hasNext())
				{
					final AttributeInterface attribute = (AttributeInterface) attributesIter.next();
					if (( attribute != null )
							&& ( EntityManagerConstantsInterface.ID_ATTRIBUTE_NAME
									.equals( attribute.getName() ) ))
					{
						return attribute;
					}
				}
			}
		}
		return null;
	}

	/**
	 * @param entityGroup - entityGroup
	 * @return - ContainerInterface
	 */
	public static ContainerInterface getMainContainer(EntityGroupInterface entityGroup)
	{
		if (entityGroup != null)
		{
			final Collection < ContainerInterface > mainContainers = entityGroup
					.getMainContainerCollection();
			if (mainContainers != null)
			{
				final Iterator mainContainerIter = mainContainers.iterator();
				if (mainContainerIter.hasNext())
				{
					//Return just the forst main container
					final ContainerInterface mainContainer = (ContainerInterface) mainContainerIter
							.next();
					//System.out.println( "mainContainer " + mainContainer );
					return mainContainer;
				}
			}
		}
		return null;
	}

}
