package edu.wustl.catissuecore.flex.dag;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpSession;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.client.ui.dag.PathLink;
import edu.wustl.catissuecore.flex.dag.DAGResolveAmbiguity;
import edu.wustl.cab2b.client.ui.dag.ambiguityresolver.AmbiguityObject;
import edu.wustl.cab2b.client.ui.query.ClientQueryBuilder;
import edu.wustl.cab2b.client.ui.query.IClientQueryBuilderInterface;
import edu.wustl.cab2b.client.ui.query.IPathFinder;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.bizlogic.querysuite.CreateQueryObjectBizLogic;
import edu.wustl.catissuecore.bizlogic.querysuite.GenerateHtmlForAddLimitsBizLogic;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryOutputSpreadsheetBizLogic;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryOutputTreeBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.querysuite.exceptions.CyclicException;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.exceptions.SqlException;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.factory.SqlGeneratorFactory;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.queryengine.impl.SqlGenerator;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IQueryEntity;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.querysuite.queryobject.impl.Expression;
import edu.wustl.common.querysuite.queryobject.impl.ExpressionId;
import edu.wustl.common.querysuite.queryobject.impl.JoinGraph;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;
import edu.wustl.common.querysuite.queryobject.impl.Rule;
import edu.wustl.common.querysuite.queryobject.util.QueryObjectProcessor;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;


/**
 *The class is responsibel controlling all activities of Flex DAG
 *  
 *@author aniket_pandit
 */

public class DAGPanel {

	private IClientQueryBuilderInterface m_queryObject;
	private HttpSession m_session;
	private IPathFinder m_pathFinder;
	private IExpression expression;

	public DAGPanel(IPathFinder pathFinder)
	{
		m_pathFinder =pathFinder;
	}
	/**
	 * 
	 * @param expressionId
	 * @param isOutputView
	 * @return
	 */
	private DAGNode createNode(IExpressionId expressionId,boolean isOutputView)
	{
		IExpression expression = m_queryObject.getQuery().getConstraints().getExpression(expressionId);
        IQueryEntity constraintEntity = expression.getQueryEntity();
        DAGNode dagNode = new DAGNode();
		dagNode.setNodeName(edu.wustl.cab2b.common.util.Utility.getOnlyEntityName(constraintEntity.getDynamicExtensionsEntity()));
		dagNode.setExpressionId(expression.getExpressionId().getInt());
		if(isOutputView)
		{
			dagNode.setNodeType(DAGConstant.VIEW_ONLY_NODE);
		}
		else
		{
			dagNode.setToolTip(expression);
		}
		return dagNode;
	}

	@SuppressWarnings("unchecked")
	public DAGNode createQueryObject(String strToCreateQueryObject,String entityName,String mode) 
	{
		Map ruleDetailsMap = null;
		IExpressionId expressionId = null;
		DAGNode node = null;
		//IQuery query = (IQuery)session.getAttribute(AppletConstants.QUERY_OBJECT);
		IQuery query = m_queryObject.getQuery();
		System.out.println("query=======>"+query);
		m_session.setAttribute(AppletConstants.QUERY_OBJECT, query);

		try {
			//-----------Init Code  from Digramatical Applet view 
			//IQuery query = getQueryObjectFromServer(); Get exiting Query object from server
//			if(query != null)
//			{
//			queryObject.setQuery(query);
//			}
//			DAGNodeBuilder nodeBuilder  = new DAGNodeBuilder();

			Map searchedEntitiesMap = (Map)m_session.getAttribute(Constants.SEARCHED_ENTITIES_MAP);
			EntityInterface entity = (Entity) searchedEntitiesMap.get(entityName);

			CreateQueryObjectBizLogic queryBizLogic = new CreateQueryObjectBizLogic();
			if (!strToCreateQueryObject.equalsIgnoreCase("")) {
				ruleDetailsMap = queryBizLogic.getRuleDetailsMap(strToCreateQueryObject, entity);
				List<AttributeInterface> attributes = (List<AttributeInterface>) ruleDetailsMap.get(AppletConstants.ATTRIBUTES);
				List<String> attributeOperators = (List<String>) ruleDetailsMap.get(AppletConstants.ATTRIBUTE_OPERATORS);
				List<List<String>> conditionValues = (List<List<String>>) ruleDetailsMap.get(AppletConstants.ATTR_VALUES);

				if(mode.equals("Edit"))
				{
					Rule rule = ((Rule) (expression.getOperand(0)));
					rule.removeAllConditions();
					List<ICondition> conditionsList = ((ClientQueryBuilder)m_queryObject).getConditions(attributes, attributeOperators,conditionValues);
					for (ICondition condition : conditionsList)
					{
						rule.addCondition(condition);
					}
					expressionId = expression.getExpressionId();
					node = createNode(expressionId,false);
				}
				else
				{
					expressionId = m_queryObject.addRule(attributes, attributeOperators, conditionValues);
					node = createNode(expressionId,false);
				}

			}
		} catch (DynamicExtensionsSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DynamicExtensionsApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return node;
	}

	/**
	 * Sets ClientQueryBuilder object
	 * @param queryObject
	 */
	public void setQueryObject(IClientQueryBuilderInterface queryObject) {
		m_queryObject = queryObject;
	}
	/**
	 * Sets httpsession
	 * @param session
	 */
	public void setSession(HttpSession session)
	{
		m_session=session;
	}
	/**
	 * Sets Expression
	 * @param expression
	 */
	public void setExpression(IExpression expression)
	{
		this.expression = expression;
	}

	/**
	 * Links two nodes
	 * @param sourceNode
	 * @param destNode
	 * @param paths
	 */
	public void linkNode(final DAGNode sourceNode, final DAGNode destNode,List<IPath> paths) {
		try {

			//	        if (paths == null || paths.isEmpty()) {
//			JOptionPane.showMessageDialog(MainDagPanel.this,
//			"No path available/selected between source and destination categories",
//			"Connect Nodes warning", JOptionPane.WARNING_MESSAGE);
//			return;
//			}

			IExpressionId sourceExpressionId = new ExpressionId(sourceNode.getExpressionId());
			IExpressionId destExpressionId = new ExpressionId(destNode.getExpressionId());
			if (!m_queryObject.isPathCreatesCyclicGraph(sourceExpressionId, destExpressionId,
					paths.get(0))) {
				for (int i = 0; i < paths.size(); i++) {
					LinkTwoNode(sourceNode, destNode, paths.get(i), new ArrayList<IExpressionId>());
				}
			}
//			else {
//			JOptionPane.showMessageDialog(MainDagPanel.this,
//			"Cannot connect selected nodes as it creates cycle in the query graph",
//			"Connect Nodes warning", JOptionPane.WARNING_MESSAGE);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}	        
	}
	/**
	 * Gets list of paths between two nodes
	 * @param sourceNode
	 * @param destNode
	 * @return
	 */
	public List<IPath> getPaths(DAGNode sourceNode, DAGNode destNode) {
		Map<AmbiguityObject, List<IPath>> map =  null;
		AmbiguityObject ambiguityObject = null;
		try {
			IQuery query = m_queryObject.getQuery();
			IConstraints constraints = query.getConstraints();
			IExpressionId expressionId = new ExpressionId(sourceNode.getExpressionId());
			IExpression expression = constraints
			.getExpression(expressionId);
			IQueryEntity sourceEntity = expression
			.getQueryEntity();
			expressionId = new ExpressionId(destNode.getExpressionId());
			expression = constraints.getExpression(expressionId);
			IQueryEntity destinationEntity = expression
			.getQueryEntity();

			ambiguityObject = new AmbiguityObject(
					sourceEntity.getDynamicExtensionsEntity(),
					destinationEntity.getDynamicExtensionsEntity());
//			ResolveAmbiguity resolveAmbigity = new ResolveAmbiguity(
//			ambiguityObject, m_pathFinder);
			DAGResolveAmbiguity resolveAmbigity = new DAGResolveAmbiguity(ambiguityObject, m_pathFinder);
			map = resolveAmbigity.getPathsForAllAmbiguities();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return map.get(ambiguityObject);
	}
	/**
	 * Link 2 nodes
	 * @param sourceNode
	 * @param destNode
	 * @param path
	 * @param intermediateExpressions
	 */
	private void LinkTwoNode(final DAGNode sourceNode, final DAGNode destNode, final IPath path,
			List<IExpressionId> intermediateExpressions) {
		PathLink link = getPathLink(sourceNode, destNode, path, intermediateExpressions);

////		if (assPosition == 0) {
////		updateQueryObject(link, sourceNode, destNode, null);
////		} else {
		updateQueryObject(link,sourceNode, destNode);

	}

	private PathLink getPathLink(final DAGNode sourceNode, final DAGNode destNode, final IPath path, List<IExpressionId> intermediateExpressions) {
		IExpressionId sourceexpressionId = null;
		IExpressionId destexpressionId = null;

		try {
			sourceexpressionId = new ExpressionId(sourceNode
					.getExpressionId());
			destexpressionId = new ExpressionId(destNode
					.getExpressionId());
			intermediateExpressions = m_queryObject.addPath(sourceexpressionId,
					destexpressionId, path);

		} catch (CyclicException e) {
//			JOptionPane.showMessageDialog(this, "Cannot  connect nodes as it creates cycle in graph",
//			"Connect Nodes warning", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();

		}

		PathLink link = new PathLink();
		link.setAssociationExpressions(intermediateExpressions);
		link.setDestinationExpressionId(destexpressionId);
		link.setSourceExpressionId(sourceexpressionId);
		link.setPath(path);
		return link;
	}
	/**
	 * Updates query object
	 * @param link
	 * @param sourceNode
	 * @param destNode
	 */
	private void updateQueryObject(PathLink link, DAGNode sourceNode, DAGNode destNode) {
		IExpressionId sourceexpressionId = new ExpressionId(sourceNode
				.getExpressionId());
		IExpressionId destexpressionId = new ExpressionId(destNode
				.getExpressionId());

		// If the first association is added, put operator between attribute condition and association
		String operator = null;
		// if (sourcePort == null) {
		operator = sourceNode.getOperatorBetweenAttrAndAssociation();
		//} else { // Get the logical operator associated with previous association
		//   operator = sourceNode.getLogicalOperator(sourcePort);
		// }

		// Get the expressionId between which to add logical operator
		IExpressionId destId = link.getLogicalConnectorExpressionId();

		m_queryObject.setLogicalConnector(sourceexpressionId, destId,
				edu.wustl.cab2b.client.ui.query.Utility.getLogicalOperator(operator), false);

		// Put appropriate parenthesis
		//if (sourcePort != null) {
		IExpressionId previousExpId = link.getLogicalConnectorExpressionId();
		m_queryObject.addParantheses(sourceexpressionId, previousExpId, destId);
		// }
	}
	/**
	 * Gets display path string
	 * @param path
	 * @return
	 */
	public static String getPathDisplayString(IPath path) {

		String text = "Path:";
		// text=text.concat("<HTML><B>Path</B>:");

		List<IAssociation> pathList = path.getIntermediateAssociations();
		text = text.concat(Utility.getDisplayName(path.getSourceEntity()));
		for (int i = 0; i < pathList.size(); i++) {
			text = text.concat("---->");
			text = text.concat(Utility.getDisplayName(pathList.get(i).getTargetEntity()));
		}
		text = text.concat("");
		Logger.out.debug(text);
		StringBuffer sb = new StringBuffer();
		int textLength = text.length();
		Logger.out.debug(textLength);
		int currentStart = 0;
		String currentString = null;
		int offset = 100;
		int strLen = 0;
		int len = 0;
		while (currentStart < textLength && textLength > offset) {
			currentString = text.substring(currentStart, (currentStart + offset));
			strLen = strLen + currentString.length() + len;
			sb.append(currentString);
			int index = text.indexOf("--->", (currentStart + offset));
			if (index == -1) {
				index = text.indexOf(".", (currentStart + offset));
			}
			if (index == -1) {
				index = text.indexOf(",", (currentStart + offset));
			}
			if (index == -1) {
				index = text.indexOf(" ", (currentStart + offset));
			}
			if (index != -1) {
				len = index - strLen;
				currentString = text.substring((currentStart + offset), (currentStart + offset + len));
				sb.append(currentString);
				sb.append("");
			} else {
				if (currentStart == 0) {
					currentStart = offset;
				}
				sb.append(text.substring(currentStart));
				return sb.toString();
			}

			currentStart = currentStart + offset + len;
			if ((currentStart + offset + len) > textLength)
				break;
		}
		sb.append(text.substring(currentStart));
		return sb.toString();
	}

	/**
	 * Generates sql query
	 * @return
	 */
	public String search()
	{
		String message=null;
		try {
			int recordsPerPage; 
			String recordsPerPageSessionValue = (String)m_session.getAttribute(Constants.RESULTS_PER_PAGE);
			if (recordsPerPageSessionValue==null)
			{
				recordsPerPage = Integer.parseInt(XMLPropertyHandler.getValue(Constants.RECORDS_PER_PAGE_PROPERTY_NAME));
				m_session.setAttribute(Constants.RESULTS_PER_PAGE, recordsPerPage+"");
			}
			else
				recordsPerPage = new Integer(recordsPerPageSessionValue).intValue();

			boolean isRulePresentInDag = false;
			IQuery query = m_queryObject.getQuery();
			SqlGenerator sql = new SqlGenerator();
			try {
				System.out.println("Query: [ " + sql.generateSQL(query)+" ]");
			} catch (SqlException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			IConstraints constraints = query.getConstraints();
			Enumeration<IExpressionId> expressionIds = constraints.getExpressionIds();
			while(expressionIds.hasMoreElements())
			{
				IExpressionId id = expressionIds.nextElement();
				if(((Expression)constraints.getExpression(id)).containsRule())
				{
					isRulePresentInDag = true;
					break;
				}
			}
			if(isRulePresentInDag)
			{
				m_session.setAttribute(AppletConstants.QUERY_OBJECT, query);
				String selectSql = "";

				SqlGenerator sqlGenerator = (SqlGenerator)SqlGeneratorFactory.getInstance();
				QueryOutputTreeBizLogic outputTreeBizLogic = new QueryOutputTreeBizLogic();
				selectSql = sqlGenerator.generateSQL(query);
				Object obj = m_session.getAttribute(Constants.SESSION_DATA);
				if (obj != null)
				{
					SessionDataBean sessionData = (SessionDataBean) obj;

					outputTreeBizLogic.createOutputTreeTable(selectSql, sessionData);
					//Map<OutputTreeDataNode,Map<Long, Map<AttributeInterface, String>>> outputTreeMap = sqlGenerator.getOutputTreeMap();
					List<OutputTreeDataNode> rootOutputTreeNodeList = sqlGenerator.getRootOutputTreeNodeList();
					m_session.setAttribute(Constants.TREE_ROOTS,rootOutputTreeNodeList);
					//Set<OutputTreeDataNode> keys = outputTreeMap.keySet();
					Long noOfTrees = new Long(rootOutputTreeNodeList.size());
					m_session.setAttribute(Constants.NO_OF_TREES, noOfTrees);
					Map<String, OutputTreeDataNode> uniqueIdNodesMap = QueryObjectProcessor.getAllChildrenNodes(rootOutputTreeNodeList);
					m_session.setAttribute(Constants.ID_NODES_MAP, uniqueIdNodesMap);
					int i =0;
					for(OutputTreeDataNode node :rootOutputTreeNodeList)
					{
						Vector treeData = outputTreeBizLogic.createDefaultOutputTreeData(i,node, sessionData);
						m_session.setAttribute(Constants.TREE_DATA+"_"+i, treeData);
						i += 1;
					}
					OutputTreeDataNode node = rootOutputTreeNodeList.get(0);
					QueryOutputSpreadsheetBizLogic outputSpreadsheetBizLogic = new QueryOutputSpreadsheetBizLogic();
					String parentNodeId = null;
					String treeNo = "0";
					Map spreadSheetDatamap = outputSpreadsheetBizLogic.createSpreadsheetData(treeNo,node, sessionData,parentNodeId,recordsPerPage);
					m_session.setAttribute(Constants.SPREADSHEET_DATA_LIST, spreadSheetDatamap.get(Constants.SPREADSHEET_DATA_LIST));
					m_session.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, spreadSheetDatamap.get(Constants.SPREADSHEET_COLUMN_LIST));
					message ="SUCCESS";
				}
				else
				{
					message = AppletConstants.EMPTY_DAG_ERROR_MESSAGE;
				}

			}
		} catch (MultipleRootsException e)
		{
			e.printStackTrace();
		}
		catch (SqlException e)
		{
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (DAOException e)
		{
			e.printStackTrace();
		}				
		return message;
	}
	/**
	 * Repaints DAG
	 * @return
	 */
	public List<DAGNode> repaintDAG()
	{
		List<DAGNode> nodeList = new ArrayList<DAGNode>();
		IQuery query =(IQuery)m_session.getAttribute("queryObject");
		IConstraints constraints = query.getConstraints();

		HashSet<IExpressionId> visibleExpression = new HashSet<IExpressionId>();

		Enumeration<IExpressionId> expressionIds = constraints.getExpressionIds();
		while(expressionIds.hasMoreElements())
		{
			IExpressionId id = expressionIds.nextElement();
			IExpression expression  = constraints.getExpression(id);
			if(expression.isVisible())
			{
				visibleExpression.add(id);
			}
		}
		for(IExpressionId expressionId:visibleExpression){

			IExpression exp = constraints.getExpression(expressionId);
			IQueryEntity constraintEntity = exp.getQueryEntity();
			String nodeDisplayName = edu.wustl.cab2b.common.util.Utility.getOnlyEntityName(constraintEntity.getDynamicExtensionsEntity()); 
			DAGNode dagNode = new DAGNode();
			dagNode.setExpressionId(exp.getExpressionId().getInt());
			dagNode.setNodeName(nodeDisplayName);
			dagNode.setToolTip(exp);
			if(!exp.containsRule())
			{
				dagNode.setNodeType(DAGConstant.VIEW_ONLY_NODE);
			}
			if(!exp.isInView())
			{
				dagNode.setNodeType(DAGConstant.CONSTRAINT_ONLY_NODE);
			}
			nodeform(expressionId,dagNode,constraints,nodeDisplayName);

			int numOperands = exp.numberOfOperands();
			int numOperator = numOperands-1;
			for(int i=0;i<numOperator;i++)
			{
				String operator  = exp.getLogicalConnector(i, i+1).getLogicalOperator().toString();
				dagNode.setOperatorList(operator.toUpperCase());
			}

			nodeList.add(dagNode);

		}
		return nodeList;

	}

	private void nodeform(IExpressionId expressionId,DAGNode node,IConstraints constraints,String path)
	{

		List childList = constraints.getJoinGraph().getChildrenList(expressionId);
		for(int i=0;i<childList.size();i++)
		{
			String nodeDisplayName = "";
			IExpressionId newId = (IExpressionId)childList.get(i);
			IExpression exp = constraints.getExpression(newId);
			IQueryEntity constraintEntity = exp.getQueryEntity();
			if(exp.isVisible())
			{
				nodeDisplayName =edu.wustl.cab2b.common.util.Utility.getOnlyEntityName(constraintEntity.getDynamicExtensionsEntity());
				path = path + "--->"+nodeDisplayName;

				DAGNode dagNode = new DAGNode();
				dagNode.setExpressionId(exp.getExpressionId().getInt());
				dagNode.setNodeName(edu.wustl.cab2b.common.util.Utility.getOnlyEntityName(constraintEntity.getDynamicExtensionsEntity()));
				dagNode.setToolTip(exp);

				node.setAssociationList(dagNode);
				node.setPathList(path);

			}
			else
			{
				nodeDisplayName =edu.wustl.cab2b.common.util.Utility.getOnlyEntityName(constraintEntity.getDynamicExtensionsEntity());
				path = path + "--->"+nodeDisplayName;	
				nodeform(newId,node,constraints,path);
			}
		}
	}


	public void updateLogicalOperator(int parentExpId,int parentIndex,String operator )
	{
		IExpressionId parentExpressionId = new ExpressionId(parentExpId);
		IQuery query = m_queryObject.getQuery();
		IExpression parentExpression = query.getConstraints().getExpression(parentExpressionId);
		LogicalOperator logicOperator = edu.wustl.cab2b.client.ui.query.Utility.getLogicalOperator(operator);
		int childIndex = parentIndex +1;
		parentExpression.setLogicalConnector(parentIndex, childIndex,QueryObjectFactory.createLogicalConnector(logicOperator));
		m_queryObject.setQuery(query);

	}
	public Map editAddLimitUI(int expId)
	{
		Map<String, Object> map = new HashMap<String,Object>();
		IExpressionId expressionId = new ExpressionId(expId);
		IExpression expression = m_queryObject.getQuery().getConstraints().getExpression(expressionId);
		EntityInterface entity = expression.getQueryEntity().getDynamicExtensionsEntity();
		GenerateHtmlForAddLimitsBizLogic generateHTMLBizLogic = new GenerateHtmlForAddLimitsBizLogic();
		Rule rule = ((Rule) (expression.getOperand(0)));
		List<ICondition> conditions = rule.getConditions();
		String html = generateHTMLBizLogic.generateHTML(entity, conditions);
		map.put(DAGConstant.HTML_STR, html);
		map.put(DAGConstant.EXPRESSION, expression);
		return map;
	}

	public DAGNode addNodeToOutPutView(String nodesStr)
	{
		DAGNode node = null;
		if (!nodesStr.equalsIgnoreCase(""))
		{
			if(nodesStr.indexOf("~")!= -1)
			{
				String[] entityArr =  nodesStr.split("~");
				Map entityMap = (Map)m_session.getAttribute(Constants.SEARCHED_ENTITIES_MAP);

				for(int i=0;i<entityArr.length; i++)
				{
					String entityName = entityArr[i];
					EntityInterface entity = (EntityInterface)entityMap.get(entityName);
					IExpressionId expressionId = ((ClientQueryBuilder)m_queryObject).addExpression(entity);
//					DAGNodeBuilder nodeBuilder  = new DAGNodeBuilder();
					node = createNode(expressionId,true);

				}
			}
		}
		return node;
	}
	public void restoreQueryObject()
	{
		IQuery query =m_queryObject.getQuery();
		int roots = ((JoinGraph)(query.getConstraints().getJoinGraph())).getAllRoots().size();
		if(roots > 1)
		{
			//errorMessage = AppletConstants.MULTIPLE_ROOTS_EXCEPTION;
			//	showValidationMessagesToUser(errorMessage);
		}
		else
		{
			m_session.setAttribute(AppletConstants.QUERY_OBJECT, query);
		}
	}
	public void deleteExpression(int expId)
	{
		IExpressionId expressionId = new ExpressionId(expId);
		m_queryObject.removeExpression(expressionId);
		//m_queryObject.setQuery(arg0)
	}

	public void addExpressionToView(int expId) {
		IExpressionId expressionId = new ExpressionId(expId);
		Expression expression = (Expression) m_queryObject.getQuery().getConstraints().getExpression(expressionId);
		expression.setInView(true);
	}
	public void deleteExpressionFormView(int expId)
	{
		IExpressionId expressionId = new ExpressionId(expId);
		Expression expression = (Expression) m_queryObject.getQuery().getConstraints().getExpression(expressionId);
		expression.setInView(false);
	}
	public void deletePath(IPath path,List<DAGNode>linkedNodeList)
	{
		PathLink link = getPathLink(linkedNodeList.get(0),linkedNodeList.get(1) , path, new ArrayList<IExpressionId>());
		List<IExpressionId> expressionIds = link.getAssociationExpressions();
		// If the association is direct association, remove the respective association 
		if (0 == expressionIds.size()) {
			m_queryObject.removeAssociation(link.getSourceExpressionId(), link.getDestinationExpressionId());
		} else {
			for (int i = 0; i < expressionIds.size(); i++) {
				m_queryObject.removeExpression(expressionIds.get(i));
			}
		}

	}

}
