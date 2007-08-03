
package edu.wustl.catissuecore.bizlogic.test;
/**
 * This is a test class for QueryOutputTreeBizLogic.java
 * @author deepti_shelar
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.mockobjects.constraint.Constraint;
import com.mockobjects.constraint.IsAnything;
import com.mockobjects.constraint.IsInstanceOf;
import com.mockobjects.dynamic.FullConstraintMatcher;
import com.mockobjects.dynamic.Mock;

import edu.common.dynamicextensions.domain.Attribute;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.client.ui.query.ClientQueryBuilder;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryOutputTreeBizLogic;
import edu.wustl.catissuecore.test.MockDAOFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;

import edu.wustl.common.querysuite.exceptions.DuplicateChildException;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.metadata.associations.IIntraModelAssociation;
import edu.wustl.common.querysuite.queryobject.IOutputTreeNode;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.test.BaseTestCase;
import edu.wustl.common.tree.QueryTreeNodeData;
import edu.wustl.common.util.dbManager.DAOException;

public class QueryOutputTreeBizLogicTest extends BaseTestCase
{
	Mock jdbcDAO;
	SessionDataBean sessionData = null;

	public QueryOutputTreeBizLogicTest(String name)
	{
		super(name);
	}

	/**
	 * Set up to set mock for jdbc.
	 */
	protected void setUp()
	{
		jdbcDAO = new Mock(JDBCDAO.class);
		MockDAOFactory factory = new MockDAOFactory();
		factory.setJDBCDAO((JDBCDAO) jdbcDAO.proxy());
		DAOFactory.setDAOFactory(factory);
	}

	/**
	 * Gets dummy tree. And returns the parent node.
	 * @return IOutputTreeNode
	 *//*
	IOutputTreeNode getDummyTreeNodes()
	{
		EntityManagerMock entityManager = new EntityManagerMock();
		IOutputTreeNode participantNode = null;
		try
		{
			EntityInterface participantEntity = entityManager.getEntityByName(EntityManagerMock.PARTICIPANT_NAME);
			participantNode = QueryObjectFactory.createOutputTreeNode(QueryGeneratorMock.createParticipantOutputEntity(participantEntity));
			AssociationInterface participanCPRegAssociation = QueryGeneratorMock.getAssociationFrom(entityManager.getAssociation(
					EntityManagerMock.PARTICIPANT_NAME, "participant"), EntityManagerMock.COLLECTION_PROTOCOL_REGISTRATION_NAME);
			IIntraModelAssociation iParticipanCPRegAssociation = QueryObjectFactory.createIntraModelAssociation(participanCPRegAssociation);
			EntityInterface cprEntity = entityManager.getEntityByName(EntityManagerMock.COLLECTION_PROTOCOL_REGISTRATION_NAME);
			participantNode.addChild(iParticipanCPRegAssociation, QueryGeneratorMock.createCollProtoRegOutputEntity(cprEntity));
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
	}*/

	/**
	 * Initialises the jdbc operations required for each test case, the jdbc operations are mocked here.
	 *
	 */
	void initJunitForJDBC()
	{
		sessionData = new SessionDataBean();
		sessionData.setUserId(new Long(1));
		Constraint[] constraints = {new IsInstanceOf(SessionDataBean.class)};
		FullConstraintMatcher fullConstraintMatcher = new FullConstraintMatcher(constraints);
		Constraint[] selectConstraints = {new IsAnything(), new IsInstanceOf(SessionDataBean.class), new IsAnything(), new IsAnything(), new IsAnything()};
		FullConstraintMatcher selectConstraintMatcher = new FullConstraintMatcher(selectConstraints);		
		Exception exception = new DAOException("");
		Constraint[] deleteConstraints = {new IsAnything()};
		jdbcDAO.expect("delete", deleteConstraints);
		jdbcDAO.expect("closeSession");
		jdbcDAO.expect("commit");
		jdbcDAO.expect("retrieve", fullConstraintMatcher);

		jdbcDAO.expect("openSession", fullConstraintMatcher);
		List<List<String>> dataList = new ArrayList<List<String>>();
		List<String> row = new ArrayList<String>();
		row.add("1");
		row.add("abc");
		row.add("1");
		row.add("ACTIVE");
		dataList.add(row);
		row.add("2");
		row.add("xyz");
		row.add("2");
		row.add("ACTIVE");
		dataList.add(row);
		
		jdbcDAO.expectAndReturn("executeQuery", selectConstraintMatcher, dataList);
		jdbcDAO.expect("executeUpdate", deleteConstraints);

	}

	/**
	 * test for createOutputTreeTable
	 *
	 *//*
	public void testCreateOutputTreeTable()
	{
		QueryOutputTreeBizLogic treeBizLogic = new QueryOutputTreeBizLogic();
		initJunitForJDBC();
		SessionDataBean sessionData = new SessionDataBean();
		sessionData.setUserId(new Long("1"));
		String sql = "select distinct Column4,Column3,Column2,Column1,Column0 from abc";
		String tableName = null;
		try
		{
			tableName = treeBizLogic.createOutputTreeTable(sql, sessionData);
		}
		catch (DAOException e1)
		{
			e1.printStackTrace();
		}
		String expectedTableName = Constants.TEMP_OUPUT_TREE_TABLE_NAME + sessionData.getUserId();
		assertNotNull(tableName);
		assertEquals(tableName, expectedTableName);
		sql = "select distinct Column4,Column3,Column2,Column1,Column0 from " + tableName;
		List dataList = null;
		try
		{
			dataList = treeBizLogic.executeQuery(sql, new SessionDataBean());
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (DAOException e)
		{
			e.printStackTrace();
		}
		assertNotNull(dataList);
	}
*/
	/**
	 * test for CreateDefaultOutputTreeData
	 */
//	public void testCreateDefaultOutputTreeData()
//	{
//		initJunitForJDBC();
//		Map<Long, Map<AttributeInterface, String>> nodeAttributeColumnNameMap = getNodeAttributeColumnNameMap();
//		IQuery query = new ClientQueryBuilder().getQuery();
//		IOutputTreeNode root = getDummyTreeNodes();
//		query.setRootOutputClass(root);
//		Vector<QueryTreeNodeData> treeDataVector = new Vector<QueryTreeNodeData>();
//		QueryTreeNodeData treeNode = new QueryTreeNodeData();
//		treeNode.setIdentifier(Constants.ALL);
//		treeNode.setObjectName(Constants.ALL);
//		treeNode.setDisplayName(Constants.ALL);
//		treeNode.setParentIdentifier(Constants.ZERO_ID);
//		treeNode.setParentObjectName("");
//		treeDataVector.add(treeNode);
//		treeNode = new QueryTreeNodeData();
//		treeNode.setIdentifier("1_1");
//		treeNode.setObjectName("edu.wustl.catissuecore.domain.Participant");
//		treeNode.setDisplayName("Participant");
//		treeNode.setParentIdentifier(Constants.ALL);
//		treeNode.setParentObjectName(Constants.ALL);
//		treeDataVector.add(treeNode);
//		QueryOutputTreeBizLogic treeBizLogic = new QueryOutputTreeBizLogic();
//
//		
//		Long userId = sessionData.getUserId();
//		String expectedTableName = Constants.TEMP_OUPUT_TREE_TABLE_NAME + userId;
//		Vector treeVector = null;
//		/*try
//		{
//			treeVector = treeBizLogic.createDefaultOutputTreeData(query, sessionData, nodeAttributeColumnNameMap);
//		}
//		catch (DAOException e)
//		{
//			e.printStackTrace();
//		}
//		catch (ClassNotFoundException e)
//		{
//			e.printStackTrace();
//		}
//		assertEquals(treeDataVector.size(), treeVector.size());*/
//		assertNotNull(treeVector);
//	}

	/**
	 * Test for BuildTreeForNode
	 *
	 */
//	public void testBuildTreeForNode()
//	{
//		initJunitForJDBC();
//		Map<Long, Map<AttributeInterface, String>> nodeAttributeColumnNameMap = getNodeAttributeColumnNameMap();
//		Map<Long, IOutputTreeNode> idNodeMap = new HashMap<Long, IOutputTreeNode>();
//		IOutputTreeNode root = getDummyTreeNodes();
//		idNodeMap.put(new Long(1), root);
//		QueryOutputTreeBizLogic treeBizLogic = new QueryOutputTreeBizLogic();
//		String outputTreeStr = null;
//	/*	try
//		{
//			outputTreeStr = treeBizLogic.updateTreeForLabelNode("1_1::1_1", idNodeMap, nodeAttributeColumnNameMap, sessionData);
//		}
//		catch (ClassNotFoundException e)
//		{
//			e.printStackTrace();
//		}
//		catch (DAOException e)
//		{
//			e.printStackTrace();
//		}*/
//		String expectedTreeStr = "1_1::1_1::10_1,CollectionProtocolRegistration_1,edu.wustl.catissuecore.domain.CollectionProtocolRegistration,1_1::1_1,edu.wustl.catissuecore.domain.Participant|";
//		assertEquals(outputTreeStr, expectedTreeStr);
//		assertNotNull(outputTreeStr);
//	}

	/**
	 * Returns the map for each node with its attribute and column names mapped.
	 * @return Map<Long, Map<AttributeInterface, String>> NodeAttributeColumnNameMap
	 */
	Map<Long, Map<AttributeInterface, String>> getNodeAttributeColumnNameMap()
	{
		Map<AttributeInterface, String> attrColumnNameMap = new HashMap<AttributeInterface, String>();
		AttributeInterface attr = new Attribute();
		attr.setId(new Long(1));
		attr.setName(Constants.ID);
		attrColumnNameMap.put(attr, "Column0");
		Map<Long, Map<AttributeInterface, String>> nodeAttributeColumnNameMap = new HashMap<Long, Map<AttributeInterface, String>>();
		nodeAttributeColumnNameMap.put(new Long(1), attrColumnNameMap);
		attr = new Attribute();
		attr.setId(new Long(10));
		attr.setName(Constants.ID);
		attrColumnNameMap.put(attr, "Column10");
		nodeAttributeColumnNameMap.put(new Long(10), attrColumnNameMap);
		return nodeAttributeColumnNameMap;
	}
}
