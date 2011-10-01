package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.tree.StorageContainerTreeNode;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;


public class TreeDataBizLogic extends DefaultBizLogic
{
	/**
	 * Logger object.
	 */
	private final transient Logger logger = Logger.getCommonLogger(TreeDataBizLogic.class);
	/**
	 * This method will add all the node into the List that contains any
	 * container node and add a dummy container node to show [+] sign on the UI,
	 * so that on clicking expand sign ajax call will retrieve child container
	 * node under the site node.
	 * @param userId - user id
	 * @return List of sites with dummy container
	 * @throws ApplicationException
	 */
	public List<StorageContainerTreeNode> getSiteWithDummyContainer(Long userId) throws ApplicationException
	{
		final String sql = "SELECT site.IDENTIFIER, site.NAME,COUNT(site.NAME) FROM CATISSUE_SITE "
				+ " site join CATISSUE_STORAGE_CONTAINER sc ON sc.site_id = site.identifier join "
				+ "CATISSUE_CONTAINER con ON con.identifier = sc.identifier WHERE con.ACTIVITY_STATUS!='Disabled' "
				+ "GROUP BY site.IDENTIFIER, site.NAME" + " order by upper(site.NAME)";
		final List<StorageContainerTreeNode> nodeList =
			new LinkedList<StorageContainerTreeNode>();
		try
		{
			final List resultList = AppUtility.executeSQLQuery(sql);
			Long nodeIdentifier;
			String nodeName = null;
			String dummyNodeName = null;
			final Iterator iterator = resultList.iterator();
			final Set<Long> siteIdSet = new UserBizLogic().getRelatedSiteIds(userId);
			while (iterator.hasNext())
			{
				final List rowList = (List) iterator.next();
				nodeIdentifier = Long.valueOf((String) rowList.get(0));

				if (AppUtility.hasPrivilegeonSite(siteIdSet, nodeIdentifier))
				{
					nodeName = (String) rowList.get(1);
					dummyNodeName = Constants.DUMMY_NODE_NAME;

					final StorageContainerTreeNode siteNode = new StorageContainerTreeNode(
							nodeIdentifier, nodeName, nodeName);
					final StorageContainerTreeNode dummyContainerNode = new StorageContainerTreeNode(
							nodeIdentifier, dummyNodeName, dummyNodeName);
					dummyContainerNode.setParentNode(siteNode);
					siteNode.getChildNodes().add(dummyContainerNode);
					nodeList.add(siteNode);
				}
			}
		}
		catch (final DAOException daoExp)
		{
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		return nodeList;
	}

	/**
	 * @param identifier
	 *            Identifier of the container or site node.
	 * @param nodeName
	 *            Name of the site or container
	 * @param parentId
	 *            parent identifier of the selected node
	 * @return conNodeList This List contains all the containers
	 * @throws ApplicationException
	 * @Description This method will retrieve all the containers under the
	 *              selected node
	 */
	public List<StorageContainerTreeNode> getStorageContainers(Long identifier, String nodeName,
			String parentId) throws ApplicationException
	{
		JDBCDAO dao = null;
		List<StorageContainerTreeNode> conNodeList = new LinkedList<StorageContainerTreeNode>();
		try
		{
			dao = AppUtility.openJDBCSession();
			List resultList = new ArrayList();
			if (Constants.ZERO_ID.equals(parentId))
			{
				resultList = this.getContainersForSite(identifier, dao);//Bug 11694
			}
			else
			{
				final String sql = this.createSql();
				ColumnValueBean columnValueBean = new ColumnValueBean(identifier);
				List<ColumnValueBean> columnValueBeansList = new ArrayList<ColumnValueBean>();
				columnValueBeansList.add(columnValueBean);
				resultList = dao.executeQuery(sql,null,columnValueBeansList);
			}
			String containerName = null;
			Long nodeIdentifier;
			Long parentContainerId;
			Long childCount;
			String activityStatus = null;

			final Iterator iterator = resultList.iterator();
			while (iterator.hasNext())
			{
				final List rowList = (List) iterator.next();
				nodeIdentifier = Long.valueOf((String) rowList.get(0));
				containerName = (String) rowList.get(1);
				activityStatus = (String) rowList.get(2);
				parentContainerId = Long.valueOf((String) rowList.get(3));
				childCount = Long.valueOf((String) rowList.get(4));

				conNodeList = AppUtility.getTreeNodeDataVector(conNodeList,
						nodeIdentifier, containerName, activityStatus, childCount,
						parentContainerId, nodeName);
			}
			if (conNodeList.isEmpty())
			{
				final StorageContainerTreeNode containerNode = new StorageContainerTreeNode(
						identifier, nodeName, nodeName, activityStatus);
				conNodeList.add(containerNode);
			}
		}
		catch (final DAOException daoExp)
		{
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (final DAOException e)
			{
				this.logger.error(e.getMessage(), e);
				e.printStackTrace();
			}
		}
		return conNodeList;
	}

	/**
	 * @param identifier
	 *            Identifier of the container or site node
	 * @param parentId
	 *            Parent identifier of the selected node
	 * @return String SQL This method with return the sql depending on the node
	 *         clicked (i.e parent Node or child node)
	 */
	private String createSql()
	{
		final String sql = "SELECT cn.IDENTIFIER, cn.name, cn.activity_status, pos.PARENT_CONTAINER_ID,COUNT(sc3.IDENTIFIER) "
				+ "FROM CATISSUE_CONTAINER cn join CATISSUE_STORAGE_CONTAINER sc ON sc.IDENTIFIER=cn.IDENTIFIER "
				+ "left outer join catissue_container_position pos on pos.CONTAINER_ID=cn.IDENTIFIER left outer join "
				+ "catissue_container_position con_pos on con_pos.PARENT_CONTAINER_ID=cn.IDENTIFIER left outer join "
				+ "CATISSUE_STORAGE_CONTAINER sc3 on con_pos.CONTAINER_ID=sc3.IDENTIFIER "
				+ "WHERE pos.PARENT_CONTAINER_ID= ?"
				+ " AND cn.ACTIVITY_STATUS!='Disabled' GROUP BY cn.IDENTIFIER, cn.NAME, cn.activity_status, pos.PARENT_CONTAINER_ID"
				+ " ORDER BY cn.IDENTIFIER ";

		return sql;
	}
	/**
	 * This method will return list of parent containers.
	 * @param identifier - site id
	 * @param dao - DAO object
	 * @return list of container for sites
	 * @throws BizLogicException throws BizLogicException
	 */
	private List getContainersForSite(Long identifier, JDBCDAO dao) throws BizLogicException
	{
		final List resultList = new ArrayList();
		final Map<Long, List> resultSCMap = new LinkedHashMap<Long, List>();
		List storageContainerList = new ArrayList();
		List childContainerList = new ArrayList();
		final Set childContainerIds = new LinkedHashSet();
		/**
		 *  This query will return list of all storage containers within the specified site.
		 */
		final String query = "SELECT cn.IDENTIFIER,cn.NAME,cn.ACTIVITY_STATUS FROM CATISSUE_CONTAINER cn join CATISSUE_STORAGE_CONTAINER sc "
				+ "ON sc.IDENTIFIER=cn.IDENTIFIER join CATISSUE_SITE site "
				+ "ON sc.site_id = site.identifier WHERE "
				+ "cn.ACTIVITY_STATUS!='Disabled' AND site_id= ?";
		ColumnValueBean columnValueBean = new ColumnValueBean(identifier);
		List<ColumnValueBean> columnValueBeansList = new ArrayList<ColumnValueBean>();
		columnValueBeansList.add(columnValueBean);
		try
		{
			storageContainerList = dao.executeQuery(query,null,columnValueBeansList);
			final Iterator iterator = storageContainerList.iterator();
			while (iterator.hasNext())
			{
				final List rowList = (List) iterator.next();
				final Long id = Long.valueOf((String) rowList.get(0));
				resultSCMap.put(id, rowList);
			}
			/**
			 * This query will return list of all child storage containers within the specified site.
			 */
			final String childQuery = "SELECT pos.CONTAINER_ID FROM CATISSUE_CONTAINER_POSITION pos "
					+ "join CATISSUE_STORAGE_CONTAINER sc ON pos.CONTAINER_ID=sc.IDENTIFIER "
					+ "WHERE sc.site_id=?";

			ColumnValueBean valueBean = new ColumnValueBean(identifier);
			List<ColumnValueBean> valueBeansList = new ArrayList<ColumnValueBean>();
			valueBeansList.add(valueBean);

			childContainerList = dao.executeQuery(childQuery,null,valueBeansList);
			final Iterator iterator1 = childContainerList.iterator();
			while (iterator1.hasNext())
			{
				final List rowListPos = (List) iterator1.next();
				childContainerIds.add(Long.valueOf((String) rowListPos.get(0)));
			}
			final Set parentIds = resultSCMap.keySet();
			//removed all child containers
			//this will return set of all parent(1st level) containers
			parentIds.removeAll(childContainerIds);

			final StringBuffer parentContainerIdsBuffer = new StringBuffer();
			final Iterator it = parentIds.iterator();
			while (it.hasNext())
			{
				parentContainerIdsBuffer.append(it.next());
				parentContainerIdsBuffer.append(",");

			}
			parentContainerIdsBuffer.deleteCharAt(parentContainerIdsBuffer.length() - 1);
			/*
			 * This query will return child count of parent containers.
			 */
			final String imediateChildCountQuery = "SELECT PARENT_CONTAINER_ID,COUNT(*) FROM CATISSUE_CONTAINER_POSITION GROUP BY "
					+ "PARENT_CONTAINER_ID HAVING PARENT_CONTAINER_ID IN ("
					+ parentContainerIdsBuffer.toString() + ")";
			final List result = dao.executeQuery(imediateChildCountQuery);
			final Map<Long, Long> childCountMap = new LinkedHashMap<Long, Long>();
			final Iterator itr = result.iterator();
			while (itr.hasNext())
			{
				final List rowList = (List) itr.next();
				final Long id = Long.valueOf((String) rowList.get(0));
				final Long childCount = Long.valueOf((String) rowList.get(1));
				childCountMap.put(id, childCount);
			}
			final Iterator parentContainerIterator = parentIds.iterator();
			/**
			 * Formed resultList having data as follows:
			 * 1. parent container id
			 * 2. container name
			 * 3. site id
			 * 4. child count
			 *
			 */
			while (parentContainerIterator.hasNext())
			{
				final Long id = (Long) parentContainerIterator.next();//parent container id
				final List strorageContainerList = resultSCMap.get(id);
				strorageContainerList.add(identifier.toString());//site id
				if (childCountMap.containsKey(id))
				{
					strorageContainerList.add(childCountMap.get(id).toString()); //child count
				}
				else
				{
					strorageContainerList.add("0");
				}
				resultList.add(strorageContainerList);
			}
		}
		catch (final DAOException daoExp)
		{
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		finally
		{
			resultSCMap.clear();
			storageContainerList.clear();
			childContainerList.clear();
			childContainerIds.clear();
		}
		return resultList;
	}
}