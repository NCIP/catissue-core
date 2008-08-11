package edu.wustl.catissuecore.util.querysuite;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;

/**
 * @author santhoshkumar_c
 *
 */
public class QueryDetails  {
	
	//private QueryDetails(){}	
	private List<OutputTreeDataNode> rootOutputTreeNodeList;
	private Map<String, OutputTreeDataNode> uniqueIdNodesMap;
	private Map<EntityInterface, List<EntityInterface>> mainEntityMap;
	private SessionDataBean sessionData;
	private String randomNumber;
	//private HttpSession session;
		
	public QueryDetails(HttpSession session)
	{
		//this.session = session;
		rootOutputTreeNodeList = (List<OutputTreeDataNode>)session.getAttribute(Constants.SAVE_TREE_NODE_LIST);
		uniqueIdNodesMap = (Map<String, OutputTreeDataNode>)session.getAttribute(Constants.ID_NODES_MAP);
		mainEntityMap = (Map<EntityInterface, List<EntityInterface>>)session
						.getAttribute(Constants.MAIN_ENTITY_MAP);
		sessionData = (SessionDataBean)session.getAttribute(Constants.SESSION_DATA);
		this.randomNumber = QueryModuleUtil.generateRandomNumber(session);
	}		
	
	/**
	 * @return the rootOutputTreeNodeList
	 */
	public List<OutputTreeDataNode> getRootOutputTreeNodeList() {
		return rootOutputTreeNodeList;
	}
	/**
	 * @param rootOutputTreeNodeList the rootOutputTreeNodeList to set
	 */
	public void setRootOutputTreeNodeList(
			List<OutputTreeDataNode> rootOutputTreeNodeList) {
		this.rootOutputTreeNodeList = rootOutputTreeNodeList;
	}
	/**
	 * @return the mainEntityMap
	 */
	public Map<EntityInterface, List<EntityInterface>> getMainEntityMap() {
		return mainEntityMap;
	}
	/**
	 * @param mainEntityMap the mainEntityMap to set
	 */
	public void setMainEntityMap(
			Map<EntityInterface, List<EntityInterface>> mainEntityMap) {
		this.mainEntityMap = mainEntityMap;
	}
	/**
	 * @return the randomNumber
	 */
	public String getRandomNumber() {
		return randomNumber;
	}
	/**
	 * @param randomNumber the randomNumber to set
	 */
	public void setRandomNumber(String randomNumber) {
		this.randomNumber = randomNumber;
	}
	/**
	 * @return the sessionData
	 */
	public SessionDataBean getSessionData() {
		return sessionData;
	}
	/**
	 * @param sessionData the sessionData to set
	 */
	public void setSessionData(SessionDataBean sessionData) {
		this.sessionData = sessionData;
	}
	/**
	 * @return the uniqueIdNodesMap
	 */
	public Map<String, OutputTreeDataNode> getUniqueIdNodesMap() {
		return uniqueIdNodesMap;
	}
	/**
	 * @param uniqueIdNodesMap the uniqueIdNodesMap to set
	 */
	public void setUniqueIdNodesMap(Map<String, OutputTreeDataNode> uniqueIdNodesMap) {
		this.uniqueIdNodesMap = uniqueIdNodesMap;
	}

}
