
package edu.wustl.catissuecore.bizlogic.querysuite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.databaseproperties.ColumnProperties;
import edu.common.dynamicextensions.domain.databaseproperties.TableProperties;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ColumnPropertiesInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.TablePropertiesInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.querysuite.EntityManagerMock;
import edu.wustl.common.querysuite.QueryGeneratorMock;
import edu.wustl.common.querysuite.exceptions.DuplicateChildException;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.metadata.associations.IIntraModelAssociation;
import edu.wustl.common.querysuite.queryobject.IOutputTreeNode;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.util.QueryObjectProcessor;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * Creates QueryOutputTree Object as per the data filled by the user on AddLimits section.
 * Creates QueryOutputTree Table.
 * Creates Dyn Ext entity which represents the newly created table for out put tree.
 * @author deepti_shelar
 *
 */
public class QueryOutputTreeBizLogic
{
	/**
	 * This method is called from ViewSearchResultsAction. It first creates a temp table for output tree.
	 * Then it creates a dynamic extensions entity which represents the newly created table.
	 * @param query IQuery , this object has tree associated with it
	 */
	public void createOutputTree(IQuery query)
	{
		IOutputTreeNode rootNode = getDummyTreeNodes();
		query.setRootOutputClass(rootNode);
		createOutputTreeTable(query);
		EntityInterface dynExtEntity = createDynExtEntity(query);
		System.out.println(dynExtEntity);
	}
	/**
	 * Creates new table which has the same structure and also same data , as the output tree structurs has.  
	 * @param query IQuery , this object has tree associated with it
	 */
	void createOutputTreeTable(IQuery query)
	{
		//		sql = SqlGeneratorFactory.getInstance().generateSQL(query);
		String selectSql = "Select Participant_1.IDENTIFIER, Participant_1.MIDDLE_NAME, Participant_1.FIRST_NAME,  Participant_1.GENDER, Participant_1.LAST_NAME ,  cpr_1.REGISTRATION_DATE From CATISSUE_PARTICIPANT Participant_1 ,catissue_coll_prot_reg cpr_1";
		String tableName = "temp_OutputTree";
		String createTableSql = "Create table " + tableName + " As " + selectSql;
		Logger.out.debug("sql for create table " + createTableSql);
		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		try
		{
			jdbcDao.openSession(null);
			jdbcDao.delete(tableName);
			jdbcDao.executeUpdate(createTableSql);
			jdbcDao.commit();
			jdbcDao.closeSession();
		}
		catch (DAOException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Gets dummy tree. And returns the parent node.
	 * @return IOutputTreeNode
	 */
	IOutputTreeNode getDummyTreeNodes()
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		IOutputTreeNode participantNode = null;
		try
		{
			EntityInterface participantEntity = entityManager.getEntityByName(EntityManagerMock.PARTICIPANT_NAME);
			participantNode = QueryObjectFactory.createOutputTreeNode(QueryGeneratorMock.createParticipantOutputEntity(participantEntity));
			AssociationInterface participanCPRegAssociation = QueryGeneratorMock.getAssociationFrom(entityManager.getAssociation(
					EntityManagerMock.PARTICIPANT_NAME, "participant"), EntityManagerMock.COLLECTION_PROTOCOL_REGISTRATION_NAME);
			IIntraModelAssociation iParticipanCPRegAssociation = QueryObjectFactory.createIntraModelAssociation(participanCPRegAssociation);
			EntityInterface cprEntity = entityManager.getEntityByName(EntityManagerMock.COLLECTION_PROTOCOL_REGISTRATION_NAME);
			IOutputTreeNode cprNode = participantNode.addChild(iParticipanCPRegAssociation, QueryGeneratorMock
					.createCollProtoRegOutputEntity(cprEntity));
			AssociationInterface cprAndSpgAssociation = QueryGeneratorMock.getAssociationFrom(entityManager.getAssociation(
					EntityManagerMock.COLLECTION_PROTOCOL_REGISTRATION_NAME, "collectionProtocolRegistration"),
					EntityManagerMock.SPECIMEN_COLLECTION_GROUP_NAME);
			IIntraModelAssociation iCprAndSpgAssociation = QueryObjectFactory.createIntraModelAssociation(cprAndSpgAssociation);
			EntityInterface specimenEntity = entityManager.getEntityByName(EntityManagerMock.SPECIMEN_NAME);
			EntityInterface scgEntity = entityManager.getEntityByName(EntityManagerMock.SPECIMEN_COLLECTION_GROUP_NAME);

			IOutputTreeNode scgNode = cprNode.addChild(iCprAndSpgAssociation, QueryGeneratorMock.createScgOutputEntity(scgEntity));
			AssociationInterface spgAndSpecimeAssociation = QueryGeneratorMock.getAssociationFrom(entityManager.getAssociation(
					EntityManagerMock.SPECIMEN_COLLECTION_GROUP_NAME, "specimenCollectionGroup"), EntityManagerMock.SPECIMEN_NAME);
			IIntraModelAssociation iSpgAndSpecimeAssociation = QueryObjectFactory.createIntraModelAssociation(spgAndSpecimeAssociation);
			scgNode.addChild(iSpgAndSpecimeAssociation, QueryGeneratorMock.createSpecimenOutputEntity(specimenEntity));
		}
		catch (DynamicExtensionsSystemException e)
		{
			e.printStackTrace();
		}
		catch (DynamicExtensionsApplicationException e)
		{
			e.printStackTrace();
		}
		catch (DuplicateChildException e)
		{
			e.printStackTrace();
		}
		return participantNode;
	}

	/**
	 * Creates a new DynExt entity, the query obj passed to it has a node associated with it.
	 * All the attributes of root entity and all of its child nodes' attributes are added to this newly created entity.
	 * 
	 * @param query IQuery , this object has tree associated with it
	 * @return EntityInterface DynExt entity
	 */
	private EntityInterface createDynExtEntity(IQuery query)
	{
		EntityInterface entity = DomainObjectFactory.getInstance().createEntity();
		IOutputTreeNode root = query.getRootOutputClass();
		List<IOutputTreeNode> allChildNodes = new ArrayList<IOutputTreeNode>();
		List<IOutputTreeNode> childNodes = getAllChildNodes(root, allChildNodes);

		Collection<AttributeInterface> attrs = new ArrayList<AttributeInterface>();
		Map<Long, AttributeInterface> idAndAttributeMap = new HashMap<Long, AttributeInterface>();
		for (IOutputTreeNode node : childNodes)
		{
			Collection<AttributeInterface> attrColl = node.getOutputEntity().getDynamicExtensionsEntity().getAttributeCollection();
			Collection<AttributeInterface> attrCollClone = (Collection<AttributeInterface>) QueryObjectProcessor.getObjectCopy(attrColl);
			attrs.addAll(attrCollClone);
		}
		LinkedHashMap attributeIndexMap = getAttributeIndexMappings(attrs);
		for (AttributeInterface attr : attrs)
		{
			idAndAttributeMap.put(attr.getId(), attr);
		}
		entity = addAttributesToEntity(entity, attributeIndexMap, idAndAttributeMap);
		return entity;
	}

	/**
	 * All the attributes of all the nodes present in a tree are added to an entity maintaining the same index.
	 * @param entity entity to which attributes to be added
	 * @param attributeIndexMap map of attribute and its index 
	 * @param idAndAttributeMap id and attribute map
	 * @return
	 */
	EntityInterface addAttributesToEntity(EntityInterface entity, LinkedHashMap attributeIndexMap, Map idAndAttributeMap)
	{
		for (int index = 0; index < attributeIndexMap.size(); index++)
		{
			AttributeInterface attr = (AttributeInterface) idAndAttributeMap.get(attributeIndexMap.get(index));
			entity.addAttribute(attr);
		}
		return entity;
	}

	/**
	 * creates a dummy map for attribute and its index, this will later replaced by a map generates from sqlgenerator.
	 * @param attrs collection of attribute obj
	 * @return Map of index and the attribute's id.
	 */
	LinkedHashMap getAttributeIndexMappings(Collection<AttributeInterface> attrs)
	{
		LinkedHashMap dummyMap = new LinkedHashMap();
		int index = 0;
		for (AttributeInterface attr : attrs)
		{
			dummyMap.put(index++, attr.getId());
		}
		return dummyMap;
	}

	/**
	 * Returns all child nodes for the root node passed to it.This is a recursive method.
	 * @param node IOutputTreeNode ,
	 * @param allChildNodes list of nodes
	 * @return List of IOutputTreeNode all child nodes
	 */
	List<IOutputTreeNode> getAllChildNodes(IOutputTreeNode node, List<IOutputTreeNode> all)
	{
		List<IOutputTreeNode> childNodes = new ArrayList<IOutputTreeNode>();
		all.add(node);
		if (!node.isLeaf())
		{
			childNodes = (node.getChildren());
			for (IOutputTreeNode childNode : childNodes)
			{
				getAllChildNodes(childNode, all);
			}
		}
		return all;
	}
	/**
	 * This method takes a map as an input , key : Attribute AND value : columnName.
	 * One by one these attributes are cloned and added to entity along with their tableproperties and column properties are also set. 
	 * @param entity EntityInterface obj to which the attributes are added
	 * @param tableName this name is then set as tableproperties for this entity
	 * @param attributeColumnNameMap Map of attribute and its columnname.
	 * @return EntityInterface the entity with all the attributes and their properties added with it. 
	 */
	EntityInterface addAttributesToEntity1(EntityInterface entity, String tableName ,HashMap<AttributeInterface, String> attributeColumnNameMap)
	{
		TablePropertiesInterface tableProperty = new TableProperties();
		tableProperty.setName(tableName);
		entity.setTableProperties(tableProperty);
		Iterator iter = attributeColumnNameMap.entrySet().iterator();
		while(iter.hasNext())
		{
			Map.Entry<AttributeInterface,String> entry = (Map.Entry)iter.next();
			AttributeInterface attribute = entry.getKey();
			String columnName = entry.getValue();
			AttributeInterface attrCopy = (AttributeInterface)QueryObjectProcessor.getObjectCopy(attribute);
			ColumnPropertiesInterface columnProperty = new ColumnProperties();
			columnProperty.setName(columnName);
			attrCopy.setColumnProperties(columnProperty);
			entity.addAttribute(attrCopy);
		}
		return entity;
	}

}