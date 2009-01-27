
package edu.wustl.catissuecore.util.querysuite;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.querysuite.queryobject.IOutputTerm;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;

/**
 * @author santhoshkumar_c
 *
 */
public class QueryDetails
{
	/**
	 * List of Output Tree Data Nodes.
	 */
	private List<OutputTreeDataNode> rootOutputTreeNodeList;

	/**
	 * Map with key->Id and value->OutputTreeDataNode object.
	 */
	private Map<String, OutputTreeDataNode> uniqueIdNodesMap;

	/**
	 * Map containing the entity and list of corresponding main entities.
	 */
	private Map<EntityInterface, List<EntityInterface>> mainEntityMap;

	/**
	 * SessionDataBean object.
	 */
	private SessionDataBean sessionData;

	/**
	 * Random number to be generated.
	 */
	private String randomNumber;

	/**
	 * Map with key->attribute name and id and value->column name.
	 */
	private Map<AttributeInterface, String> attrColNameMap;

	/**
	 * The columns related to temporal query.
	 */
	private Map<String, IOutputTerm> outputTermCols;

	/**
	 * IQuery object.
	 */
	private IQuery query;

	/**
	 *@param session HttpSession object
	 */
	public QueryDetails(HttpSession session)
	{
		//this.session = session;
		rootOutputTreeNodeList = (List<OutputTreeDataNode>) session.getAttribute
			(Constants.SAVE_TREE_NODE_LIST);
		uniqueIdNodesMap = (Map<String, OutputTreeDataNode>) session.getAttribute
			(Constants.ID_NODES_MAP);
		mainEntityMap = (Map<EntityInterface, List<EntityInterface>>) session.getAttribute
			(Constants.MAIN_ENTITY_MAP);
		sessionData = (SessionDataBean) session.getAttribute(Constants.SESSION_DATA);
		this.randomNumber = QueryModuleUtil.generateRandomNumber(session);
		attrColNameMap = (Map<AttributeInterface, String>) session.getAttribute
			(Constants.ATTRIBUTE_COLUMN_NAME_MAP);
		outputTermCols = (Map<String, IOutputTerm>) session.getAttribute(Constants.OUTPUT_TERMS_COLUMNS);
		query = (IQuery) session.getAttribute(AppletConstants.QUERY_OBJECT);
	}

	/**
	 *
	 * @return attributeColumnNameMap Map with key->attribute value->column names
	 */
	public Map<AttributeInterface, String> getAttributeColumnNameMap()
	{
		return attrColNameMap;
	}

	/**
	 * @param attrColNameMap Map with key->attribute value->column names
	 */
	public void setAttributeColumnNameMap(Map<AttributeInterface, String> attrColNameMap)
	{
		this.attrColNameMap = attrColNameMap;
	}

	/**
	 * @return rootOutputTreeNodeList The list of nodes to be shown on UI in tree
	 */
	public List<OutputTreeDataNode> getRootOutputTreeNodeList()
	{
		return rootOutputTreeNodeList;
	}

	/**
	 * @param rootOutputTreeNodeList the rootOutputTreeNodeList to set
	 */
	public void setRootOutputTreeNodeList(List<OutputTreeDataNode> rootOutputTreeNodeList)
	{
		this.rootOutputTreeNodeList = rootOutputTreeNodeList;
	}

	/**
	 * @return mainEntityMap Map containing the main entities of specifiec entity
	 */
	public Map<EntityInterface, List<EntityInterface>> getMainEntityMap()
	{
		return mainEntityMap;
	}

	/**
	 * @param mainEntityMap the mainEntityMap to set
	 */
	public void setMainEntityMap(Map<EntityInterface, List<EntityInterface>> mainEntityMap)
	{
		this.mainEntityMap = mainEntityMap;
	}

	/**
	 * @return randomNumber Random number
	 */
	public String getRandomNumber()
	{
		return randomNumber;
	}

	/**
	 * @param randomNumber the randomNumber to set
	 */
	public void setRandomNumber(String randomNumber)
	{
		this.randomNumber = randomNumber;
	}

	/**
	 * @return the sessionData
	 */
	public SessionDataBean getSessionData()
	{
		return sessionData;
	}

	/**
	 * @param sessionData the sessionData to set
	 */
	public void setSessionData(SessionDataBean sessionData)
	{
		this.sessionData = sessionData;
	}

	/**
	 * @return the uniqueIdNodesMap
	 */
	public Map<String, OutputTreeDataNode> getUniqueIdNodesMap()
	{
		return uniqueIdNodesMap;
	}

	/**
	 * @param uniqueIdNodesMap the uniqueIdNodesMap to set
	 */
	public void setUniqueIdNodesMap(Map<String, OutputTreeDataNode> uniqueIdNodesMap)
	{
		this.uniqueIdNodesMap = uniqueIdNodesMap;
	}

	/**
	 * @return the outputTermsColumns
	 */
	public Map<String, IOutputTerm> getOutputTermsColumns()
	{
		return outputTermCols;
	}

	/**
	 * @param outputTermCols the outputTermsColumns to set
	 */
	public void setOutputTermsColumns(Map<String, IOutputTerm> outputTermCols)
	{
		this.outputTermCols = outputTermCols;
	}

	/**
	 * @return the query
	 */
	public IQuery getQuery()
	{
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(IQuery query)
	{
		this.query = query;
	}
}
