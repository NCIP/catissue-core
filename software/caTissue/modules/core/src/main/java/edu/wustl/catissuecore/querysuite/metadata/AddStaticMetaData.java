
package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.metadata.AssociationType;
import edu.wustl.catissuecore.metadata.AssociationsType;
import edu.wustl.catissuecore.metadata.AttributeType;
import edu.wustl.catissuecore.metadata.EntitiesType;
import edu.wustl.catissuecore.metadata.EntityType;
import edu.wustl.catissuecore.metadata.StaticMetaDataType;
import edu.wustl.catissuecore.util.metadata.EntitiesProcessor;

public class AddStaticMetaData
{

	/**
	 * Connection Instance.
	 */
	private static Connection connection = null;
	/**
	 * Statement Instance.
	 */
	private static Statement stmt = null;

	/**
	 * This method adds Container Meta data.
	 * @throws SQLException SQL Exception
	 * @throws IOException IO Exception
	 * @throws SAXException
	 * @throws DynamicExtensionsSystemException
	 * @throws IOException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */

	public static void main(String[] args) throws DynamicExtensionsSystemException, SAXException,
			SQLException, IOException, ClassNotFoundException
	{
		connection = DBConnectionUtil.getDBConnection(args);
		connection.setAutoCommit(true);
		stmt = connection.createStatement();
		UpdateMetadataUtil.isExecuteStatement = true;
		UpdateMetadata.DATABASE_TYPE = args[2];
		addStaticMetadata(args[7]);
	}

	public static void addStaticMetadata(String filePath) throws SQLException, IOException,
			DynamicExtensionsSystemException, SAXException
	{
		EntitiesProcessor entitiesProcessor = new EntitiesProcessor();
		//	StaticMetaDataType staticMetaDataType= entitiesProcessor.importMetaDataValues("StaticModelMetaData.xml", "E:/workspace/caTISSUE_SUITE1May/software/caTissue/src/resources/xml");
		StaticMetaDataType staticMetaDataType = entitiesProcessor.importMetaDataValues(
				"StaticModelMetaData.xml", filePath);

		AddEntity addEntity = new AddEntity(connection);

		EntitiesType entitiesType = staticMetaDataType.getEntities();
		List<EntityType> entityTypes = entitiesType.getEntity();

		/**
		 * specify entity List.
		 */
		List<String> entityList = new ArrayList<String>();

		for (EntityType entityType : entityTypes)
		{
			entityList.add(entityType.getEntityName());

			addEntity(addEntity, entityType);

			List<AttributeType> attributeTypes = entityType.getAttribute();
			Map<String, String> attributeColumnNameMap = new HashMap<String, String>();
			if (attributeTypes != null)
			{
				addAttribute(entityList, entityType, attributeTypes, attributeColumnNameMap);

			}
		}
		AssociationsType associationsType = staticMetaDataType.getAssociations();
		List<AssociationType> associationTypes = associationsType.getAssociation();
		addAssociation(associationTypes);


		/* This is for adding path from ActionApplication -> User.
		 * There is already an association from AbstractApplication -> User, but this association cannot be used
		 * to query from Action Application to User. Hence this piece of Code will add a path from Action Application
		 * to User using association Abstract Application -> User.
		 * */

		// The fully qualified name of parent class.
		String parentClassName = "edu.wustl.catissuecore.domain.processingprocedure.AbstractApplication";

		// List of all children belonging to the parent class, for which paths have to be added
		List<String> childClassNames = new ArrayList<String>();
		childClassNames.add("edu.wustl.catissuecore.domain.processingprocedure.ActionApplication");
		childClassNames.add("edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedureApplication");

		// List of all associating entities for which paths have to be added from child classes.
		List<String> associatingEntityname = new ArrayList<String>();
		associatingEntityname.add("edu.wustl.catissuecore.domain.User");

		AddPath addPath = new AddPath(stmt, connection);
		addPath.getSQLForAddingChildPath(parentClassName, childClassNames, associatingEntityname,
				Boolean.TRUE);

	}

	private static void addAssociation(List<AssociationType> associationTypes) throws SQLException,
			IOException
	{
		for (AssociationType associationType : associationTypes)
		{

			if (associationType.getTargetAssociationId() != null
					&& associationType.getTargetAssociationId().equals(""))
			{
				associationType.setTargetAssociationId(null);
			}
			if (associationType.getSrcAssociationId() != null
					&& associationType.getSrcAssociationId().equals(""))
			{
				associationType.setSrcAssociationId(null);
			}
			final AddAssociations addAssociations = new AddAssociations(connection);
			addAssociations.addAssociation(associationType.getSourceEntityName(), associationType
					.getTargetEntityName(), associationType.getAssociationName(), associationType
					.getAssociationTye(), associationType.getRoleName(),
					associationType.isIsSwap(), associationType.getRoleNameTable(), associationType
							.getSrcAssociationId(), associationType.getTargetAssociationId(),
					associationType.getMaxCardinality(), 1, associationType.getDirection(),
					associationType.isManytomany());

		}
	}

	private static void addAttribute(List<String> entityList, EntityType entityType,
			List<AttributeType> attributeTypes, Map<String, String> attributeColumnNameMap)
			throws SQLException, IOException
	{
		Map<String, List<String>> entityNameAttributeNameMap = new HashMap<String, List<String>>();
		Map<String, String> attributeDatatypeMap = new HashMap<String, String>();

		List<String> attributeNames = new ArrayList<String>();
		/**
		 * specify attribute Primary key Map.
		 */
		Map<String, String> attributePrimarkeyMap = new HashMap<String, String>();

		for (AttributeType attributeType : attributeTypes)
		{
			attributeNames.add(attributeType.getAttributeNames());
			attributeColumnNameMap.put(attributeType.getAttributeNames(), attributeType
					.getTableColumn());
			attributeDatatypeMap.put(attributeType.getAttributeNames(), attributeType
					.getAttributeDataType());
			String isPrimaryKey = "0";
			if (attributeType.isPrimaryKey())
			{
				isPrimaryKey = "1";
			}
			attributePrimarkeyMap.put(attributeType.getAttributeNames(), isPrimaryKey);
		}
		entityNameAttributeNameMap.put(entityType.getEntityName(), attributeNames);

		final AddAttribute addAttribute = new AddAttribute(connection, entityNameAttributeNameMap,
				attributeColumnNameMap, attributeDatatypeMap, attributePrimarkeyMap, entityList);
		addAttribute.addAttribute();
	}

	private static void addEntity(AddEntity addEntity, EntityType entityType) throws IOException,
			SQLException
	{
		int entityId = UpdateMetadataUtil.getEntityIdByName(entityType.getEntityName(), connection
				.createStatement());
		if (entityId == 0)
		{
			int isAbstract = 0;
			if (entityType.isIsAbstract())
			{
				isAbstract = 1;
			}
			if (entityType.getParentEntity().equals(""))
			{
				entityType.setParentEntity("NULL");
			}
			List<String> entityName = new ArrayList<String>();
			entityName.add(entityType.getEntityName());
			addEntity.addEntity(entityName, entityType.getTableName(),
					entityType.getParentEntity(), 3, isAbstract);
		}
	}

}
