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
import edu.wustl.cab2b.client.ui.dag.ambiguityresolver.AmbiguityObject;
import edu.wustl.cab2b.client.ui.query.ClientQueryBuilder;
import edu.wustl.cab2b.client.ui.query.IClientQueryBuilderInterface;
import edu.wustl.cab2b.client.ui.query.IPathFinder;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.bizlogic.querysuite.CreateQueryObjectBizLogic;
import edu.wustl.catissuecore.bizlogic.querysuite.GenerateHtmlForAddLimitsBizLogic;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryOutputSpreadsheetBizLogic;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryOutputTreeBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.querysuite.QueryModuleUtil;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.QuerySessionData;
import edu.wustl.common.querysuite.exceptions.CyclicException;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.exceptions.SqlException;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.factory.SqlGeneratorFactory;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.associations.IIntraModelAssociation;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.queryengine.impl.SqlGenerator;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IJoinGraph;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.IQueryEntity;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.querysuite.queryobject.impl.Expression;
import edu.wustl.common.querysuite.queryobject.impl.ExpressionId;
import edu.wustl.common.querysuite.queryobject.impl.JoinGraph;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;
import edu.wustl.common.querysuite.queryobject.impl.Rule;
import edu.wustl.common.querysuite.queryobject.impl.metadata.SelectedColumnsMetadata;
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
	private HashMap<String,IPath> m_pathMap = new HashMap<String, IPath>();
		
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
		
		IQuery query = (IQuery)m_session.getAttribute(DAGConstant.QUERY_OBJECT);// Get existing Query object from server  

		if(query != null)
		{
			m_queryObject.setQuery(query);
		}
		else
		{
			query = m_queryObject.getQuery();
		}
		System.out.println("query=======>"+query);
		m_session.setAttribute(DAGConstant.QUERY_OBJECT, query);

		try {
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
	public  List<DAGPath> linkNode(final DAGNode sourceNode, final DAGNode destNode,List<IPath> paths) {

		List<DAGPath> dagPathList = null;
		if (paths != null && !paths.isEmpty()) {
			dagPathList = new ArrayList<DAGPath>();
			IExpressionId sourceExpressionId = new ExpressionId(sourceNode.getExpressionId());
			IExpressionId destExpressionId = new ExpressionId(destNode.getExpressionId());
			if (!m_queryObject.isPathCreatesCyclicGraph(sourceExpressionId, destExpressionId,
					paths.get(0))) {
				for (int i = 0; i < paths.size(); i++) {
					IPath path = paths.get(i);
					LinkTwoNode(sourceNode, destNode, paths.get(i), new ArrayList<IExpressionId>());
					String pathStr = new Long(path.getPathId()).toString();
					DAGPath dagPath = new DAGPath();
					dagPath.setToolTip(getPathDisplayString(path));
					dagPath.setId(pathStr);
					dagPath.setSourceExpId(sourceNode.getExpressionId());
					dagPath.setDestinationExpId(destNode.getExpressionId());
					dagPathList.add(dagPath);
					String key =pathStr+"_"+sourceNode.getExpressionId()+"_"+destNode.getExpressionId();
					m_pathMap.put(key,path);
				}
			}
		}
		return dagPathList;
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
		//DAGPath dagpath  
		PathLink link = new PathLink();
		link.setAssociationExpressions(intermediateExpressions);
		link.setDestinationExpressionId(destexpressionId);
		link.setSourceExpressionId(sourceexpressionId);
		link.setPath(path);

////		if (assPosition == 0) {
////		updateQueryObject(link, sourceNode, destNode, null);
////		} else {
		updateQueryObject(link,sourceNode, destNode);

	}

	/**
	 * Updates query object
	 * @param link
	 * @param sourceNode
	 * @param destNode
	 */
	private void updateQueryObject(PathLink link, DAGNode sourceNode, DAGNode destNode) {
		//TODO required to modify code logic will not work for multiple association
		IExpressionId sourceexpressionId = new ExpressionId(sourceNode
				.getExpressionId());
		//IExpressionId destexpressionId = new ExpressionId(destNode
				//.getExpressionId());

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
		// The code is required for multiple associations
		//if (sourcePort != null) {
		//IExpressionId previousExpId = link.getLogicalConnectorExpressionId();
		//m_queryObject.addParantheses(sourceexpressionId, previousExpId, destId);
		// }
	}
	/**
	 * Gets display path string
	 * @param path
	 * @return
	 */
	public static String getPathDisplayString(IPath path) {

		String text = "";
		// text=text.concat("<HTML><B>Path</B>:");

		List<IAssociation> pathList = path.getIntermediateAssociations();
		text = text.concat(edu.wustl.cab2b.common.util.Utility.getOnlyEntityName(path.getSourceEntity()));
		for (int i = 0; i < pathList.size(); i++) {
			text = text.concat(">>");
			text = text.concat(edu.wustl.cab2b.common.util.Utility.getOnlyEntityName(pathList.get(i).getTargetEntity()));
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
			int index = text.indexOf(">>", (currentStart + offset));
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
	public int search()
	{
		int status =0;
		IQuery query = m_queryObject.getQuery();
		status=QueryModuleUtil.searchQuery(m_session, query);
		return status;
	}
	/**
	 * Repaints DAG
	 * @return
	 */
	public List<DAGNode> repaintDAG()
	{
		List<DAGNode> nodeList = new ArrayList<DAGNode>();
		IQuery query =(IQuery)m_session.getAttribute(DAGConstant.QUERY_OBJECT);
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
			
			nodeform(expressionId,dagNode,constraints,new ArrayList<IIntraModelAssociation>());
			
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
	/**
	 * 
	 * @param expressionId
	 * @param node
	 * @param constraints
	 * @param path
	 */
	private void nodeform(IExpressionId expressionId,DAGNode node,IConstraints constraints,List<IIntraModelAssociation> intraModelAssociationList)
	{
		IJoinGraph graph = constraints.getJoinGraph();
		List childList = graph.getChildrenList(expressionId);
		
		for(int i=0;i<childList.size();i++)
		{
			
			IExpressionId newExpId = (IExpressionId)childList.get(i);
			IExpression exp = constraints.getExpression(newExpId);
			IQueryEntity constraintEntity = exp.getQueryEntity();
		/*	Code to get IPath Object*/
		 	IIntraModelAssociation association  =(IIntraModelAssociation)(graph.getAssociation(expressionId,newExpId));
		 	
		 	intraModelAssociationList.add(association);
						
			if(exp.isVisible())
			{
			//	PathFinder pathFinder =(PathFinder)m_pathFinder;
				IPath pathObj = (IPath)m_pathFinder.getPathForAssociations(intraModelAssociationList);
				long pathId =pathObj.getPathId();
					
				DAGNode dagNode = new DAGNode();
				dagNode.setExpressionId(exp.getExpressionId().getInt());
				dagNode.setNodeName(edu.wustl.cab2b.common.util.Utility.getOnlyEntityName(constraintEntity.getDynamicExtensionsEntity()));
				dagNode.setToolTip(exp);
				
				
			/*	Adding Dag Path in each visible list which have childrens*/
			  	DAGPath dagPath  = new DAGPath();
				dagPath.setToolTip(getPathDisplayString(pathObj));
				dagPath.setId( new Long(pathId).toString());
				dagPath.setSourceExpId(node.getExpressionId());
				dagPath.setDestinationExpId(dagNode.getExpressionId());
				node.setDagpathList(dagPath);
				node.setAssociationList(dagNode);
				intraModelAssociationList.clear();
		
			}
			else
			{
				nodeform(newExpId,node,constraints,intraModelAssociationList);
			}
		}
	}
	/**
	 * 
	 * @param parentExpId
	 * @param parentIndex
	 * @param operator
	 */

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
	/**
	 * 
	 * @param expId
	 * @return
	 */
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
	/**
	 * 
	 * @param nodesStr
	 * @return
	 */
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
	/**
	 * 
	 *
	 */
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
	/**
	 * 
	 * @param expId
	 */
	public void deleteExpression(int expId)
	{
		IExpressionId expressionId = new ExpressionId(expId);
		m_queryObject.removeExpression(expressionId);
		//m_queryObject.setQuery(arg0)
	}
	/**
	 * 
	 * @param expId
	 */
	public void addExpressionToView(int expId) {
		IExpressionId expressionId = new ExpressionId(expId);
		Expression expression = (Expression) m_queryObject.getQuery().getConstraints().getExpression(expressionId);
		expression.setInView(true);
	}
	/**
	 * 
	 * @param expId
	 */
	public void deleteExpressionFormView(int expId)
	{
		IExpressionId expressionId = new ExpressionId(expId);
		Expression expression = (Expression) m_queryObject.getQuery().getConstraints().getExpression(expressionId);
		expression.setInView(false);
	}
	/**
	 * 
	 * @param path
	 * @param linkedNodeList
	 */
	public void deletePath(String pathName,List<DAGNode>linkedNodeList)
	{
		IPath path = m_pathMap.remove(pathName);
		IExpressionId sourceexpressionId = new ExpressionId(linkedNodeList.get(0)
					.getExpressionId());
		IExpressionId destexpressionId = new ExpressionId(linkedNodeList.get(1)
					.getExpressionId());
		List<IAssociation> associations =path.getIntermediateAssociations();
		JoinGraph graph =(JoinGraph)m_queryObject.getQuery().getConstraints().getJoinGraph();
	
		List<IExpressionId> expressionIds = graph.getIntermediateExpressions(sourceexpressionId, destexpressionId, associations);
		System.out.println(expressionIds.size());
		// If the association is direct association, remove the respective association 
		if (0 == expressionIds.size()) {
			m_queryObject.removeAssociation(sourceexpressionId, destexpressionId);
		} else {
			for (int i = 0; i < expressionIds.size(); i++) {
				m_queryObject.removeExpression(expressionIds.get(i));
			}
		}

	}

}
