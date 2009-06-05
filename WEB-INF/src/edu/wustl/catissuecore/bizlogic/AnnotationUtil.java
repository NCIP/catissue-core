/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author
 *@version 1.0
 */

package edu.wustl.catissuecore.bizlogic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.hibernate.HibernateException;

import edu.common.dynamicextensions.dao.impl.DynamicExtensionDAO;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.userinterface.Container;
import edu.common.dynamicextensions.domaininterface.AbstractEntityInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ConstraintPropertiesInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.global.DEConstants.AssociationDirection;
import edu.common.dynamicextensions.util.global.DEConstants.AssociationType;
import edu.common.dynamicextensions.util.global.DEConstants.Cardinality;
import edu.wustl.cab2b.server.path.PathFinder;
import edu.wustl.catissuecore.annotations.PathObject;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;

/**
 * @author vishvesh_mulay
 *
 */
public class AnnotationUtil
{

	private static Logger logger = Logger.getCommonLogger(AnnotationUtil.class);
	/**
	 * @param staticEntityId
	 * @param dynamicEntityId
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 * @throws BizLogicException
	 */
	public static synchronized Long addAssociation(Long staticEntityId, Long dynamicEntityId,
			boolean isEntityFromXmi)throws BizLogicException
			
	{
		//
		//        // Get instance of static entity from entity cache maintained by caB2B code
		//        EntityInterface staticEntity = EntityCacheFactory.getInstance()
		//                .getEntityById(staticEntityId);
		//
		//        //Get dynamic Entity from entity Manger
		//        EntityInterface dynamicEntity = (entityManager
		//                .getContainerByIdentifier(dynamicEntityId.toString()))
		//                .getEntity();

		//This change is done because when the hierarchy of objects grow in dynamic extensions then NonUniqueObjectException is thrown.
		//So static entity and dynamic entity are brought in one session and then associated.
		DAO dao = null;

		AssociationInterface association = null;
		EntityInterface staticEntity = null;
		EntityInterface dynamicEntity = null;
		
		try
		{
			String appName = DynamicExtensionDAO.getInstance().getAppName();
			dao = DAOConfigFactory.getInstance().getDAOFactory(appName).getDAO();
			dao.openSession(null);
			staticEntity = (EntityInterface) dao.retrieveById(Entity.class.getName(), staticEntityId);
			dynamicEntity = (EntityInterface) ((Container) dao.retrieveById(Container.class.getName(),
					dynamicEntityId)).getAbstractEntity();
			
			dao.closeSession();
		}
		catch (DAOException exp)
		{
			logger.debug(exp.getMessage(), exp);
			exp.printStackTrace();
			//ErrorKey errorKey = ErrorKey.getErrorKey("dao.error");
			throw new BizLogicException(exp.getErrorKey(),exp ,exp.getMsgValues());   
		}
		finally
		{
			try
			{
				dao.closeSession();
			}			
			catch (DAOException exp)
			{
				logger.debug(exp.getMessage(), exp);
				exp.printStackTrace();
				//ErrorKey errorKey = ErrorKey.getErrorKey("dao.error");
				throw new BizLogicException(exp.getErrorKey(),exp ,exp.getMsgValues());   
			}
		}
		
		try
		{
			//			Get entitygroup that is used by caB2B for path finder purpose.

			//			Commented this line since performance issue for Bug 6433
			//EntityGroupInterface entityGroupInterface = Utility.getEntityGroup(staticEntity);
			//List<EntityInterface> processedEntityList = new ArrayList<EntityInterface>();

			//addCatissueGroup(dynamicEntity, entityGroupInterface,processedEntityList);

			//Create source role and target role for the association
			String roleName = staticEntityId.toString().concat("_").concat(
					dynamicEntityId.toString());
			RoleInterface sourceRole = getRole(AssociationType.CONTAINTMENT, roleName,
					Cardinality.ZERO, Cardinality.ONE);
			RoleInterface targetRole = getRole(AssociationType.CONTAINTMENT, roleName,
					Cardinality.ZERO, Cardinality.MANY);

			//Create association with the created source and target roles.
			association = getAssociation(dynamicEntity, AssociationDirection.SRC_DESTINATION,
					roleName, sourceRole, targetRole);

			//Create constraint properties for the created association.
			ConstraintPropertiesInterface constraintProperties = getConstraintProperties(
					staticEntity, dynamicEntity);
			association.setConstraintProperties(constraintProperties);

			//Add association to the static entity and save it.
			staticEntity.addAssociation(association);
			Long start = new Long(System.currentTimeMillis());

			staticEntity = EntityManager.getInstance().persistEntityMetadataForAnnotation(
					staticEntity, true, false, association);

			Long end = new Long(System.currentTimeMillis());
			System.out.println("Time required to persist one entity is " + (end - start) / 1000
					+ "seconds");

			//Add the column related to the association to the entity table of the associated entities.
			EntityManager.getInstance().addAssociationColumn(association);

			//			Collection<AssociationInterface> staticEntityAssociation = staticEntity
			//					.getAssociationCollection();
			//			for (AssociationInterface tempAssociation : staticEntityAssociation)
			//			{
			//				if (tempAssociation.getName().equals(association.getName()))
			//				{
			//					association = tempAssociation;
			//					break;
			//				}
			//			}

			Set<PathObject> processedPathList = new HashSet<PathObject>();
			addQueryPathsForAllAssociatedEntities(dynamicEntity, staticEntity, association.getId(),staticEntity.getId(), processedPathList);

			addEntitiesToCache(isEntityFromXmi, dynamicEntity, staticEntity);
		}
		catch (DynamicExtensionsSystemException e)
		{
			logger.debug(e.getMessage(), e);
			//ErrorKey errorKey = ErrorKey.getErrorKey("de.error");
			throw new BizLogicException(null,null ,e.getMessage());   
			
		} catch (DynamicExtensionsApplicationException e)
		{
			logger.debug(e.getMessage(), e);
			//ErrorKey errorKey = ErrorKey.getErrorKey("de.error");
			throw new BizLogicException(null,null ,e.getMessage());   
			
		}
		return association.getId();
	}
	
	/**
	 * @param staticEntityId
	 * @param dynamicEntityId
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 * @throws BizLogicException
	 */
	public static synchronized Long addNewPathsForExistingMainContainers(Long staticEntityId, Long dynamicEntityId,
			boolean isEntityFromXmi)
			throws //DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException, DynamicExtensionsSystemException,
			BizLogicException
	{
		String appName = CommonServiceLocator.getInstance().getAppName();
		DAO dao = null;
		AssociationInterface association = null;
		
		try
		{
			dao = DAOConfigFactory.getInstance().getDAOFactory(appName).getDAO();
			dao.openSession(null);
		
		
		
		EntityInterface staticEntity = null;
		EntityInterface dynamicEntity = null;
		
		staticEntity = (EntityInterface) dao.retrieveById(Entity.class.getName(), staticEntityId);
		dynamicEntity = (EntityInterface) ((Container) dao.retrieveById(Container.class.getName(),
					dynamicEntityId)).getAbstractEntity();
			
		association = getAssociationForEntity(staticEntity, dynamicEntity);

		Set<PathObject> processedPathList = new HashSet<PathObject>();
			
		addQueryPathsForEntityHierarchy(dynamicEntity, staticEntity, association.getId(), staticEntity.getId(), processedPathList);
		addEntitiesToCache(isEntityFromXmi, dynamicEntity, staticEntity);

		}
		
		catch (DAOException exp)
		{
			logger.debug(exp.getMessage(), exp);
			exp.printStackTrace();
			//ErrorKey errorKey = ErrorKey.getErrorKey("dao.error");
			throw new BizLogicException(exp.getErrorKey(),exp ,exp.getMsgValues());   

		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			
			catch (DAOException exp)
			{
				logger.debug(exp.getMessage(), exp);
				exp.printStackTrace();
				//ErrorKey errorKey = ErrorKey.getErrorKey("dao.error");
				throw new BizLogicException(exp.getErrorKey(),exp ,exp.getMsgValues());   

			}
		}
		return association.getId();
	}
	
	/**
	 * getAssociationForEntity.
	 * @param staticEntity
	 * @param dynamicEntity
	 * @return
	 */
	public static AssociationInterface getAssociationForEntity(EntityInterface staticEntity,AbstractEntityInterface dynamicEntity)
	{
		Collection<AssociationInterface> associationCollection = staticEntity
				.getAssociationCollection();
		for (AssociationInterface associationInteface : associationCollection)
		{
			if (associationInteface.getTargetEntity() != null
					&& associationInteface.getTargetEntity().equals(dynamicEntity))
			{
				return associationInteface;
			}
		}
		return null;
	}
	
	/**
	 * @param dynamicEntity
	 * @param entityGroupInterface
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 * @throws BizLogicException
	 */
	public static void addQueryPathsForEntityHierarchy(EntityInterface dynamicEntity,
			EntityInterface staticEntity, Long associationId, Long staticEntityId,
			Set<PathObject> processedPathList) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException, BizLogicException
	{
		PathObject pathObject = new PathObject();
		pathObject.setSourceEntity(staticEntity);
		pathObject.setTargetEntity(dynamicEntity);

		if (processedPathList.contains(pathObject))
		{
			return;
		}
		else
		{
			processedPathList.add(pathObject);
		}

		Long start = new Long(System.currentTimeMillis());
		JDBCDAO jdbcDAO = null;
		boolean ispathAdded = false;
		try
		{
			jdbcDAO = openSession();
			ispathAdded = isPathAdded(staticEntity.getId(),dynamicEntity.getId(), jdbcDAO);
		}
		catch (DAOException daoExp)
		{
			logger.debug(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
		}
		finally
		{
			try
			{
				closeSession(jdbcDAO);
			}
			catch (DAOException e)
			{
				logger.debug(e.getMessage(), e);
				e.printStackTrace();
			}
		}
		if(!ispathAdded)
		{
			if(dynamicEntity.getId() != null)
			{
				addPathsForQuery(staticEntity.getId(), dynamicEntity, staticEntityId, associationId);
			}
		}
		Collection<AssociationInterface> associationCollection = dynamicEntity
		.getAssociationCollection();
		for(AssociationInterface association : associationCollection)
		{
			System.out.println("PERSISTING PATH");
			addQueryPathsForEntityHierarchy(association.getTargetEntity(), dynamicEntity,
					association.getId(), staticEntityId, processedPathList);
		}
		Long end = new Long(System.currentTimeMillis());
		System.out.println("Time required to add complete paths is" + (end - start) / 1000
				+ "seconds");
	}
	
	/**
	 * @param staticEntityId
	 * @param dynamicEntityId
	 * @return
	 */
	private static boolean isPathAdded(Long staticEntityId, Long dynamicEntityId, JDBCDAO jdbcDAO)
	{
		boolean ispathAdded = false;
		try
		{
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
		
			String checkForPathQuery = "select path_id from path where FIRST_ENTITY_ID = ? and LAST_ENTITY_ID = ?";
			preparedStatement = jdbcDAO.getPreparedStatement(checkForPathQuery);
			preparedStatement.setLong(1, staticEntityId);

			preparedStatement.setLong(2, dynamicEntityId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet != null)
			{
				while (resultSet.next())
				{
					ispathAdded = true;
					break;
				}
			}

			System.out.println("ispathAdded  ----- " + ispathAdded);

		}
		catch (DAOException e)
		{
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}
		
		return ispathAdded;
	}

	/**
	 * @param dynamicEntity
	 * @param staticEntity
	 */
	public static void addCatissueGroup(EntityInterface dynamicEntity, EntityGroupInterface entityGroupInterface,List<EntityInterface> processedEntityList)
	{
		if (processedEntityList.contains(dynamicEntity))
		{
			return;
		}
		else
		{
			processedEntityList.add(dynamicEntity);
		}

		//Add the entity group to the dynamic entity and all it's associated entities.
		if (!checkBaseEntityGroup(dynamicEntity.getEntityGroup()))
		{
			dynamicEntity.setEntityGroup(entityGroupInterface);
		}
		Collection<AssociationInterface> associationCollection = dynamicEntity
				.getAssociationCollection();

		for (AssociationInterface associationInteface : associationCollection)
		{
			addCatissueGroup(associationInteface.getTargetEntity(), entityGroupInterface, processedEntityList);
			//associationInteface.getTargetEntity().addEntityGroupInterface(entityGroupInterface);
		}
	}

	/**
	 * @param entityGroupColl
	 * @return
	 */
	public static boolean checkBaseEntityGroup(EntityGroupInterface entityGroup)
	{
		if (entityGroup.getId().intValue() == Constants.CATISSUE_ENTITY_GROUP)
		{
			return true;
		}

		return false;
	}

	/**
	 * @param dynamicEntity
	 * @param staticEntity
	 * @param associationId
	 * @param isEntityFromXmi
	 * @throws BizLogicException
	 */
	public static void addQueryPathsForAllAssociatedEntities(EntityInterface dynamicEntity,
			EntityInterface staticEntity, Long associationId, Long staticEntityId, Set<PathObject> processedPathList) throws BizLogicException
	{
		Long start = new Long(System.currentTimeMillis());
		if(staticEntity != null)
		{
			PathObject pathObject = new PathObject();
			pathObject.setSourceEntity(staticEntity);
			pathObject.setTargetEntity(dynamicEntity);

			if (processedPathList.contains(pathObject))
			{
				return;
			}
			else
			{
				processedPathList.add(pathObject);
			}

			AnnotationUtil.addPathsForQuery(staticEntity.getId(), dynamicEntity,staticEntityId, associationId);
		}

		Collection<AssociationInterface> associationCollection = dynamicEntity
				.getAssociationCollection();
		for (AssociationInterface associationInteface : associationCollection)
		{
			System.out.println("PERSISTING PATH");
			addQueryPathsForAllAssociatedEntities( associationInteface
					.getTargetEntity(),dynamicEntity, associationInteface.getId(),staticEntityId,processedPathList);

			//			AnnotationUtil.addPathsForQuery(dynamicEntity.getId(), associationInteface
			//					.getTargetEntity().getId(), associationInteface.getId());
		}
		Long end = new Long(System.currentTimeMillis());
		System.out.println("Time required to add complete paths is" + (end - start) / 1000
				+ "seconds");

	}

	/**
	 * @param isEntityFromXmi
	 * @param dynamicEntity
	 * @param staticEntity
	 * @throws BizLogicException
	 */
	public static void addEntitiesToCache(boolean isEntityFromXmi, EntityInterface dynamicEntity,
			EntityInterface staticEntity) throws BizLogicException
	{
		Long start = new Long(System.currentTimeMillis());

//      Commented the code as this is not required since cache is refreshed using refreshCache.

//		Set<EntityInterface> entitySet = new HashSet<EntityInterface>();
//		entitySet.add(dynamicEntity);
//		entitySet.add(staticEntity);
//		DynamicExtensionsUtility.getAssociatedEntities(dynamicEntity, entitySet);
		if (!isEntityFromXmi)
		{
//			Commented the code as this is not required since cache is refreshed using refreshCache.

//			for (EntityInterface entity : entitySet)
//			{
//				EntityCache.getInstance().addEntityToCache(entity);
//			}
			//EntityInterface cachedStaticEntityInterfaceEntityCache.getInstance().getEntityById(staticEntityId);
			Connection conn = null;
			try
			{
				InitialContext ctx = new InitialContext();
				String DATASOURCE_JNDI_NAME = "java:/catissuecore";
		        DataSource ds = (DataSource)ctx.lookup(DATASOURCE_JNDI_NAME);
		        conn = ds.getConnection();
				PathFinder.getInstance().refreshCache(conn,true);
			}
			catch (Exception e)
			{
				logger.debug(e.getMessage(), e);
				e.printStackTrace();
			}
			finally
			{
				try
				{
					if(conn!=null)
					{
						conn.close();
					}
				}
				catch (HibernateException e)
				{
					logger.debug(e.getMessage(), e);
					e.printStackTrace();
				}
				catch (SQLException e)
				{
					logger.debug(e.getMessage(), e);
					e.printStackTrace();
				}
			}

		}

		Long end = new Long(System.currentTimeMillis());
		logger.info("Time required to refresh cache is " + (end - start) / 1000 + "seconds");
		System.out.println("Time required to refresh cache is " + (end - start) / 1000 + "seconds");
	}

	/**
	 * @param staticEntity
	 * @param dynamicEntity
	 * @return
	 */
	 private static ConstraintPropertiesInterface getConstraintProperties(
				EntityInterface staticEntity, EntityInterface dynamicEntity)
		{
			ConstraintPropertiesInterface constprop = DomainObjectFactory.getInstance()
					.createConstraintProperties();
			constprop.setName(dynamicEntity.getTableProperties().getName());
			for (AttributeInterface attribute : staticEntity.getPrimaryKeyAttributeCollection())
			{
				constprop.getTgtEntityConstraintKeyProperties().getTgtForiegnKeyColumnProperties()
						.setName(
								"DYEXTN_AS_" + staticEntity.getId().toString() + "_"
										+ dynamicEntity.getId().toString());
				constprop.getTgtEntityConstraintKeyProperties().setSrcPrimaryKeyAttribute(attribute);

			}
			constprop.getSrcEntityConstraintKeyPropertiesCollection().clear();
			return constprop;
		}


	/**
	 * @param targetEntity
	 * @param associationDirection
	 * @param assoName
	 * @param sourceRole
	 * @param targetRole
	 * @return
	 */
	private static AssociationInterface getAssociation(EntityInterface targetEntity,
			AssociationDirection associationDirection, String assoName, RoleInterface sourceRole,
			RoleInterface targetRole) throws BizLogicException 
	{
		AssociationInterface association = null;
		try 
		{
			association = DomainObjectFactory.getInstance().createAssociation();
			association.setTargetEntity(targetEntity);
			association.setAssociationDirection(associationDirection);
			association.setName(assoName);
			association.setSourceRole(sourceRole);
			association.setTargetRole(targetRole);
		} 
		catch (DynamicExtensionsSystemException exp) {
			logger.debug(exp.getMessage(), exp);
			exp.printStackTrace();
			//ErrorKey errorKey = ErrorKey.getErrorKey("de.error");
			throw new BizLogicException(null,null ,exp.getMessage());    
		}
		return association;
	}

	/**
	 * @param associationType associationType
	 * @param name name
	 * @param minCard  minCard
	 * @param maxCard maxCard
	 * @return  RoleInterface
	 */
	private static RoleInterface getRole(AssociationType associationType, String name,
			Cardinality minCard, Cardinality maxCard)
	{
		RoleInterface role = DomainObjectFactory.getInstance().createRole();
		role.setAssociationsType(associationType);
		role.setName(name);
		role.setMinimumCardinality(minCard);
		role.setMaximumCardinality(maxCard);
		return role;
	}

	/**
	 * @param staticEntityId
	 * @param dynamicEntityId
	 * @param deAssociationID
	 */
	public static void addPathsForQuery(Long staticEntityId, EntityInterface dynamicEntity,
			Long hookEntityId,Long deAssociationID)
	{
		Long maxPathId = getMaxId("path_id", "path");
		maxPathId += 1;
		insertNewPaths(maxPathId, staticEntityId, dynamicEntity, deAssociationID);
		if (hookEntityId != null && staticEntityId != hookEntityId)
		{
			maxPathId += 1;
			addPathFromStaticEntity(maxPathId, hookEntityId, staticEntityId, dynamicEntity.getId(),
					deAssociationID);
		}
	}
	/**
	 *
	 * @param hookEntityId
	 */
	private static void addPathFromStaticEntity(Long maxPathId, Long hookEntityId, Long previousDynamicEntity,Long dynamicEntityId,
			Long deAssociationId)
	{
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		String query = "";
		JDBCDAO jdbcDAO = null;
		try
		{
			jdbcDAO = openSession();
			
			query = "select ASSOCIATION_ID from INTRA_MODEL_ASSOCIATION where DE_ASSOCIATION_ID="
				+ deAssociationId;
			statement = jdbcDAO.getPreparedStatement(query);
			resultSet = statement.executeQuery();
			resultSet.next();
			Long intraModelAssociationId = resultSet.getLong(1);
			jdbcDAO.closeStatement(resultSet);			

			query = "select INTERMEDIATE_PATH from path where FIRST_ENTITY_ID="
					+ hookEntityId + " and LAST_ENTITY_ID=" + previousDynamicEntity;
			statement = jdbcDAO.getPreparedStatement(query);
			resultSet = statement.executeQuery();
			resultSet.next();
			String path = resultSet.getString(1);
			path = path.concat("_").concat(intraModelAssociationId.toString());
			jdbcDAO.closeStatement(resultSet);	


			query = "insert into path (PATH_ID, FIRST_ENTITY_ID,INTERMEDIATE_PATH, LAST_ENTITY_ID) values (?,?,?,?)";
			statement = jdbcDAO.getPreparedStatement(query);

			statement.setLong(1, maxPathId);
			statement.setLong(2, hookEntityId);
			statement.setString(3, path);
			statement.setLong(4, dynamicEntityId);
			statement.execute();
			statement.close();
			jdbcDAO.commit();
		}
		catch (SQLException e)
		{
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}
		catch (DAOException daoExp)
		{
			logger.debug(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
		}
		finally
		{
			try
			{
				resultSet.close();
				closeSession(jdbcDAO);
			}
			catch (SQLException e)
			{
				logger.debug(e.getMessage(), e);
				e.printStackTrace();
			}
			catch (DAOException daoExp)
			{
				logger.debug(daoExp.getMessage(), daoExp);
				daoExp.printStackTrace();
			}

		}
	}
	/**
	 * @param maxPathId
	 * @param staticEntityId
	 * @param dynamicEntityId
	 * @param deAssociationID
	 */
	private static void insertNewPaths(Long maxPathId, Long staticEntityId,  EntityInterface dynamicEntity,
			Long deAssociationID)
	{
		// StringBuffer query = new StringBuffer();
		Long intraModelAssociationId = getMaxId("ASSOCIATION_ID", "ASSOCIATION");
		intraModelAssociationId += 1;
		JDBCDAO jdbcDAO = null;
		try
		{
			jdbcDAO  = openSession();

			String associationQuery = "insert into ASSOCIATION (ASSOCIATION_ID, ASSOCIATION_TYPE) values ("
					+ intraModelAssociationId
					+ ","
					+ edu.wustl.cab2b.server.path.AssociationType.INTRA_MODEL_ASSOCIATION.getValue() + ")";
			String intraModelQuery = "insert into INTRA_MODEL_ASSOCIATION (ASSOCIATION_ID, DE_ASSOCIATION_ID) values ("
					+ intraModelAssociationId + "," + deAssociationID + ")";
			String directPathQuery = "insert into PATH (PATH_ID, FIRST_ENTITY_ID,INTERMEDIATE_PATH, LAST_ENTITY_ID) values ("
					+ maxPathId
					+ ","
					+ staticEntityId
					+ ","
					+ intraModelAssociationId
					+ ","
					+ dynamicEntity.getId() + ")";

			List<String> list = new ArrayList<String>();
			list.add(associationQuery);
			list.add(intraModelQuery);
			list.add(directPathQuery);

			executeQuery(jdbcDAO, list);
			
			maxPathId += 1;
			addInheritancePaths(maxPathId, dynamicEntity, staticEntityId, jdbcDAO);
			//addIndirectPaths(maxPathId, staticEntityId, dynamicEntityId, intraModelAssociationId,
					//conn);
			jdbcDAO.commit();
		}
		catch (Exception e)
		{
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				closeSession(jdbcDAO);
			}
			catch (DAOException e)
			{
				logger.debug(e.getMessage(), e);
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * This method replicates paths of parent entity for derived entity
	 * @param maxPathId
	 * @param connection 
	 * @param dynamicEntityId
	 * @param conn
	 * @throws DynamicExtensionsSystemException 
	 * @throws DynamicExtensionsApplicationException 
	 */
	private static void addInheritancePaths(Long maxPathId, EntityInterface entity, Long staticEntityId, JDBCDAO jdbcDAO)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		ResultSet resultSet = null; // NOPMD - DD anomaly

		try
		{
			//This map is added because the following algo creates multiple paths between same entities
			//The map will contains only single unique path between entities
			Map<String, Object> mapQuery = new HashMap<String, Object>();
			List<String> query = new ArrayList<String>();
			String sql = "";
			String intermediatePath = "";
			Long last_entity_id = null;
			Long first_entity_id = null;
			boolean ispathAdded = false;

			while (entity.getParentEntity() != null)
			{
				//replicate outgoing paths of parent entity (outgoing associations)
				Collection<AssociationInterface> allAssociations = entity.getParentEntity()
						.getAllAssociations();
				for (AssociationInterface association : allAssociations)
				{
					intermediatePath = "";
					sql = "select INTERMEDIATE_PATH,LAST_ENTITY_ID from path where FIRST_ENTITY_ID="
							+ association.getEntity().getId();
					resultSet = jdbcDAO.getQueryResultSet(sql);
					List<ArrayList<String>> idlist = new ArrayList<ArrayList<String>>();
					while (resultSet.next())
					{
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(0, resultSet.getString(1));
						temp.add(1, Long.toString(resultSet.getLong(2)));
						idlist.add(temp);
					}
					jdbcDAO.closeStatement(resultSet);
					for (int cnt = 0; cnt < idlist.size(); cnt++)
					{
						ArrayList<String> temp = idlist.get(cnt);
						intermediatePath = temp.get(0);
						last_entity_id = Long.valueOf(temp.get(1));
						ispathAdded = isPathAdded(entity.getId(), last_entity_id, jdbcDAO);
						if (!ispathAdded)
						{
							sql = "INSERT INTO path values(" + maxPathId + "," + entity.getId()
									+ ",'" + intermediatePath + "'," + last_entity_id + ")";
							String uniquepathStr = entity.getId() + "_" + intermediatePath
									+ "_" + last_entity_id;
							if (!mapQuery.containsKey(uniquepathStr))
							{
								mapQuery.put(uniquepathStr, null);
								query.add(sql);
								maxPathId++;
							}
						}
					}

				}

				// replicate incoming paths of parent entity (incoming associations)
				intermediatePath = "";
				sql = "select FIRST_ENTITY_ID,INTERMEDIATE_PATH from path where LAST_ENTITY_ID="
						+ entity.getParentEntity().getId();
				resultSet = jdbcDAO.getQueryResultSet(sql);
				List<ArrayList<String>> idlist = new ArrayList<ArrayList<String>>();
				while (resultSet.next())
				{
					ArrayList<String> temp = new ArrayList<String>();
					temp.add(0, Long.toString(resultSet.getLong(1)));
					temp.add(1, resultSet.getString(2));
				}
				jdbcDAO.closeStatement(resultSet);
				for (int cnt = 0; cnt < idlist.size(); cnt++)
				{
					ArrayList<String> temp = idlist.get(cnt);
					first_entity_id = Long.valueOf(temp.get(0));
					intermediatePath = temp.get(1);
					if (first_entity_id.compareTo(staticEntityId) != 0)
					{

						ispathAdded = isPathAdded(first_entity_id, entity.getId(), jdbcDAO);
						if (!ispathAdded)
						{
							sql = "INSERT INTO path values(" + maxPathId + ","
									+ first_entity_id + ",'" + intermediatePath + "',"
									+ entity.getId() + ")";
							String uniquepathStr = first_entity_id + "_" + intermediatePath
									+ "_" + entity.getId();

							if (!mapQuery.containsKey(uniquepathStr))
							{
								mapQuery.put(uniquepathStr, null);
								query.add(sql);
								maxPathId++;
							}
						}
					}
				}
				
				entity = entity.getParentEntity();
			}
			
			executeQuery(jdbcDAO, query);

		}//while
		catch (SQLException e)
		{
			throw new DynamicExtensionsSystemException("SQL Exception while adding paths for derived entity.", e);
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException("SQL Exception while adding paths for derived entity.", e);
		}
	}

	private static JDBCDAO openSession() throws DAOException
	{
		String applicationName = CommonServiceLocator.getInstance().getAppName();
		JDBCDAO jdbcDAO = DAOConfigFactory.getInstance().getDAOFactory(applicationName).getJDBCDAO();
		jdbcDAO.openSession(null);
		
		return jdbcDAO;
	}
	
	private static JDBCDAO closeSession(JDBCDAO jdbcDAO) throws DAOException
	{
		jdbcDAO.closeSession();
		return jdbcDAO;
	}

	/**
	 * @param maxPathId
	 * @param staticEntityId
	 * @param dynamicEntityId
	 * @param intraModelAssociationId
	 * @param conn
	 * @throws SQLException
	 */
	private static void addIndirectPaths(Long maxPathId, Long staticEntityId, Long dynamicEntityId,
			Long intraModelAssociationId, Connection conn)

	{
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		String query = "";
		try
		{
			//resultSet = getIndirectPaths(conn, staticEntityId);
			query = "select FIRST_ENTITY_ID,INTERMEDIATE_PATH from path where LAST_ENTITY_ID="
					+ staticEntityId;
			statement = conn.prepareStatement(query);
			resultSet = statement.executeQuery();


			query = "insert into path (PATH_ID, FIRST_ENTITY_ID,INTERMEDIATE_PATH, LAST_ENTITY_ID) values (?,?,?,?)";
			statement = conn.prepareStatement(query);
			while (resultSet.next())
			{

				Long firstEntityId = resultSet.getLong(1);
				String path = resultSet.getString(2);
				path = path.concat("_").concat(intraModelAssociationId.toString());

				statement.setLong(1, maxPathId);
				maxPathId++;
				statement.setLong(2, firstEntityId);
				statement.setString(3, path);
				statement.setLong(4, dynamicEntityId);
				statement.execute();
				statement.clearParameters();
			}
		}
		catch (SQLException e)
		{
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				resultSet.close();
				statement.close();
			}
			catch (SQLException e)
			{
				logger.debug(e.getMessage(), e);
				e.printStackTrace();
			}

		}
	}

//	/**
//	 * @param conn
//	 * @param staticEntityId
//	 * @return
//	 * @throws SQLException
//	 */
//	private static ResultSet getIndirectPaths(Connection conn, Long staticEntityId)
//	{
//		String query = "select FIRST_ENTITY_ID,INTERMEDIATE_PATH from path where LAST_ENTITY_ID="
//				+ staticEntityId;
//		java.sql.PreparedStatement statement = null;
//		ResultSet resultSet = null;
//		try
//		{
//			statement = conn.prepareStatement(query);
//			resultSet = statement.executeQuery();
//		}
//		catch (SQLException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		finally
//		{
//			try
//			{
//
//				statement.close();
//			}
//			catch (SQLException e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
//		return resultSet;
//	}

	/**
	 * @param conn
	 * @param queryList
	 * @throws SQLException
	 */
	private static void executeQuery(JDBCDAO jdbcDAO, List<String> queryList)
	{
	
		try
		{
			
			for (String query : queryList)
			{
				jdbcDAO.executeUpdate(query);
			}
		}
		catch (DAOException e)
		{
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}
		
	}

	/**
	 * @param columnName
	 * @param tableName
	 * @return
	 */
	private static Long getMaxId(String columnName, String tableName)
	{
		String query = "select max(" + columnName + ") from " + tableName;
		//		HibernateDAO hibernateDAO = (HibernateDAO) DAOFactory.getInstance().getDAO(
		//				0);
		ResultSet resultSet = null;
		JDBCDAO jdbcDAO = null;
		try
		{
			jdbcDAO = openSession();
			resultSet = jdbcDAO.getQueryResultSet(query);
			resultSet.next();
			Long maxId = resultSet.getLong(1);

			return maxId;
		}
		catch (Exception e)
		{
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				resultSet.close();
				closeSession(jdbcDAO);

			}
			catch (DAOException e)
			{
				logger.debug(e.getMessage(), e);
				e.printStackTrace();
			}
			catch (SQLException e)
			{
				logger.debug(e.getMessage(), e);
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * @param entity_name_participant
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	public static Long getEntityId(String entityName) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		if (entityName != null)
		{
			EntityManagerInterface entityManager = EntityManager.getInstance();
			return entityManager.getEntityId(entityName);
		}
		return new Long(0);
	}

}