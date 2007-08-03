
package edu.wustl.catissuecore.bizlogic.test;
/**
 * This is a test class for QueryOutputSpreadsheetBizLogic.java
 * @author deepti_shelar
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import edu.wustl.catissuecore.bizlogic.querysuite.QueryOutputSpreadsheetBizLogic;
import edu.wustl.catissuecore.test.MockDAOFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;

import edu.wustl.common.querysuite.exceptions.DuplicateChildException;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.metadata.associations.IIntraModelAssociation;
import edu.wustl.common.querysuite.queryobject.IOutputTreeNode;
import edu.wustl.common.test.BaseTestCase;
import edu.wustl.common.util.dbManager.DAOException;

public class QueryOutputSpreadsheetBizLogicTest extends BaseTestCase
{
	Mock jdbcDAO;

	public QueryOutputSpreadsheetBizLogicTest(String name)
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
	 * Initialises the jdbc operations required for each test case, the jdbc operations are mocked here.
	 */
	void initJunitForJDBC()
	{
		Constraint[] constraints = {new IsInstanceOf(SessionDataBean.class)};
		FullConstraintMatcher fullConstraintMatcher = new FullConstraintMatcher(constraints);
		Constraint[] selectConstraints = {new IsAnything(), new IsAnything(), new IsAnything(), new IsAnything(), new IsAnything()};
		FullConstraintMatcher selectConstraintMatcher = new FullConstraintMatcher(selectConstraints);

		jdbcDAO.expect("closeSession");
		jdbcDAO.expect("commit");
		jdbcDAO.expect("delete", fullConstraintMatcher);
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
		jdbcDAO.expectAndReturn("executeUpdate", fullConstraintMatcher);

	}

	/**
	 * Gets dummy tree. And returns the parent node.
	 * @return IOutputTreeNode
	 */
//	IOutputTreeNode getDummyTreeNodes()
//	{
//		EntityManagerMock entityManager = new EntityManagerMock();
//		IOutputTreeNode participantNode = null;
//		try
//		{
//			EntityInterface participantEntity = entityManager.getEntityByName(EntityManagerMock.PARTICIPANT_NAME);
//			participantNode = QueryObjectFactory.createOutputTreeNode(QueryGeneratorMock.createParticipantOutputEntity(participantEntity));
//			AssociationInterface participanCPRegAssociation = QueryGeneratorMock.getAssociationFrom(entityManager.getAssociation(
//					EntityManagerMock.PARTICIPANT_NAME, "participant"), EntityManagerMock.COLLECTION_PROTOCOL_REGISTRATION_NAME);
//			IIntraModelAssociation iParticipanCPRegAssociation = QueryObjectFactory.createIntraModelAssociation(participanCPRegAssociation);
//			EntityInterface cprEntity = entityManager.getEntityByName(EntityManagerMock.COLLECTION_PROTOCOL_REGISTRATION_NAME);
//			participantNode.addChild(iParticipanCPRegAssociation, QueryGeneratorMock.createCollProtoRegOutputEntity(cprEntity));
//		}
//		catch (DynamicExtensionsSystemException e)
//		{
//			e.printStackTrace();
//		}
//		catch (DynamicExtensionsApplicationException e)
//		{
//			e.printStackTrace();
//		}
//		catch (DuplicateChildException e)
//		{
//			e.printStackTrace();
//		}
//		return participantNode;
//	}

	/**
	 * Test for executeQuery
	 * @throws DAOException 
	 * @throws ClassNotFoundException 
	 */
	public void testExecuteQueryForSQL() throws ClassNotFoundException, DAOException
	{
		QueryOutputSpreadsheetBizLogic SpreadsheetBizLogic = new QueryOutputSpreadsheetBizLogic();
		initJunitForJDBC();
		SessionDataBean sessionData = new SessionDataBean();
		sessionData.setUserId(new Long("1"));
		String sql = "select distinct Column4,Column3,Column2,Column1,Column0 from temp_OutputTree1";
	/*	List dataList = SpreadsheetBizLogic.getRecordsForNode(sql, sessionData);
		assertNotNull(dataList);*/
	}
/*
	*//**
	 * test for CreateSpreadsheetData For FirstLevel tree node
	 *
	 *//*
	public void testCreateSpreadsheetDataForFirstLevelTreeNode()
	{
		IOutputTreeNode rootNode = getDummyTreeNodes();
		QueryOutputSpreadsheetBizLogic SpreadsheetBizLogic = new QueryOutputSpreadsheetBizLogic();
		initJunitForJDBC();
		SessionDataBean sessionData = new SessionDataBean();
		sessionData.setUserId(new Long("1"));
		Map<Long, Map<AttributeInterface, String>> nodeAttributeColumnNameMap = getNodeAttributeColumnNameMap();
		String parentNodeId = "1_1";
		Map<String, List<String>> spreadSheetDataMap = null;
		try
		{
			spreadSheetDataMap = SpreadsheetBizLogic.createSpreadsheetData(rootNode, nodeAttributeColumnNameMap,
					 parentNodeId, sessionData);
		}
		catch (DAOException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		assertNotNull(spreadSheetDataMap);
	}*/

	/**
	 * test for CreateSpreadsheetData On NodeClick
	 *
	 *//*
	public void testCreateSpreadsheetDataOnNodeClick()
	{
		IOutputTreeNode rootNode = getDummyTreeNodes();
		QueryOutputSpreadsheetBizLogic SpreadsheetBizLogic = new QueryOutputSpreadsheetBizLogic();
		initJunitForJDBC();
		SessionDataBean sessionData = new SessionDataBean();
		sessionData.setUserId(new Long("1"));
		Map<Long, Map<AttributeInterface, String>> nodeAttributeColumnNameMap = getNodeAttributeColumnNameMap();
		String parentNodeId = "1_1";
		Map<String, List<String>> spreadSheetDataMap = null;
		try
		{
			spreadSheetDataMap = SpreadsheetBizLogic.createSpreadsheetData(rootNode, nodeAttributeColumnNameMap,
					 parentNodeId, sessionData);
		}
		catch (DAOException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		assertNotNull(spreadSheetDataMap);
	}*/

	/**
	 * Returns the map for each node with its attribute and column names mapped.
	 * @return Map<Long, Map<AttributeInterface, String>> NodeAttributeColumnNameMap
	 *//*
	Map<Long, Map<AttributeInterface, String>> getNodeAttributeColumnNameMap()
	{
		EntityManagerMock entityManager = new EntityManagerMock();
		Map<AttributeInterface, String> attrColumnNameMap = new HashMap<AttributeInterface, String>();
		EntityInterface participantEntity = null;
		EntityInterface cprEntity = null;
		AttributeInterface attr = new Attribute();
		try
		{
			participantEntity = entityManager.getEntityByName(EntityManagerMock.PARTICIPANT_NAME);
			cprEntity = entityManager.getEntityByName(EntityManagerMock.COLLECTION_PROTOCOL_REGISTRATION_NAME);
		}
		catch (DynamicExtensionsSystemException e)
		{
			e.printStackTrace();
		}
		attr.setId(new Long(1));
		attr.setName(Constants.ID);
		attr.setEntity(participantEntity);
		attrColumnNameMap.put(attr, "Column0");
		Map<Long, Map<AttributeInterface, String>> nodeAttributeColumnNameMap = new HashMap<Long, Map<AttributeInterface, String>>();
		nodeAttributeColumnNameMap.put(new Long(1), attrColumnNameMap);
		attr = new Attribute();
		attr.setId(new Long(10));
		attr.setEntity(cprEntity);
		attr.setName(Constants.ID);
		attrColumnNameMap.put(attr, "Column10");
		nodeAttributeColumnNameMap.put(new Long(10), attrColumnNameMap);
		return nodeAttributeColumnNameMap;
	}*/
}
