
package edu.wustl.catissuecore.bizlogic;

import java.util.List;
import java.util.Vector;

import edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.tree.QueryTreeNodeData;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

/**
 * This class is used add, update and retrieve report loader queue information
 * using Hibernate.
 */

public class ReportLoaderQueueBizLogic extends CatissueDefaultBizLogic
{

	private transient final Logger logger = Logger.getCommonLogger(ReportLoaderQueueBizLogic.class);

	/**
	 * Saves the ReportLoaderQueue object in the database.
	 * @param dao : dao
	 * @param obj
	 *            The storageType object to be saved.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @throws BizLogicException : BizLogicException
	 */
	@Override
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final ReportLoaderQueue reportLoaderQueue = (ReportLoaderQueue) obj;
			dao.insert(reportLoaderQueue);
		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	/**
	 * Deletes the ReportLoaderQueue object in the database.
	 * @param obj
	 *            The object to be updated.
	 * @param dao : dao
	 * @throws BizLogicException : BizLogicException
	 */
	@Override
	protected void delete(Object obj, DAO dao) throws BizLogicException
	{
		try
		{
			dao.delete(obj);
		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	/**
	 * Updates the ReportLoaderQueue object in the database.
	 * @param dao :dao
	 * @param oldObj : oldObj
	 * @param obj
	 *            The object to be updated.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @throws BizLogicException : BizLogicException
	 */
	@Override
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final ReportLoaderQueue reportLoaderQueue = (ReportLoaderQueue) obj;
			dao.update(reportLoaderQueue);
		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	/**
	 * This function takes identifier as parameter and returns corresponding
	 * TextContent
	 * @param identifier : identifier
	 * @return - TextContent object
	 * @throws Exception : Exception
	 */
	public ReportLoaderQueue getReportLoaderQueueById(Long identifier) throws Exception
	{
		final Object object = this.retrieve(ReportLoaderQueue.class.getName(), identifier);
		final ReportLoaderQueue reportLoaderQueue = (ReportLoaderQueue) object;
		return reportLoaderQueue;
	}

	/**
	 * To retrieve the tree nodes
	 * @param reportQueueId : reportQueueId
	 * @param siteName : siteName
	 * @param treeDataVector : treeDataVector
	 * @return Vector
	 * @throws BizLogicException : BizLogicException
	 */
	public Vector getTreeViewData(Long reportQueueId, String siteName, Vector treeDataVector)
			throws BizLogicException
	{

		int partcipantListSize = 0, cprListSize = 0, scgListSize = 0;
		String displayName = "";

		/*
		 * Query to fetch the list of participants:
		 */
		final String partHql = "select p.id , p.lastName ,p.firstName "
				+ " from edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue as rlq, "
				+ " edu.wustl.catissuecore.domain.Participant as p" + " where rlq.id= "
				+ reportQueueId + " and p.id in elements(rlq.participantCollection) ";

		// Query query0 = session.createQuery(hql1);
		final List participantList = this.executeQuery(partHql);

		partcipantListSize = participantList.size();
		for (int partCount = 0; partCount < partcipantListSize; partCount++)
		{
			final Object[] participantObj = (Object[]) participantList.get(partCount);
			final Long pid = (Long) participantObj[0];
			final String lastName = (String) participantObj[1];
			final String firtName = (String) participantObj[2];

			displayName = lastName + "," + firtName;
			this.setConflictTreeNode(pid.toString(), Constants.PARTICIPANT, displayName, "0", null,
					null, null, treeDataVector);

			/*
			 * Query to fetch the list of collection protocol:
			 */

			final String cprHql = "select cpr.id , cpr.collectionProtocol.shortTitle"
					+ " from edu.wustl.catissuecore.domain.CollectionProtocolRegistration cpr"
					+ " where cpr.participant.id =" + pid + "";

			// Query query1 = session.createQuery(hql2);
			final List cprList = this.executeQuery(cprHql);

			cprListSize = cprList.size();
			for (int cprCount = 0; cprCount < cprListSize; cprCount++)
			{

				final Object[] cprObj = (Object[]) cprList.get(cprCount);
				final Long cprid = (Long) cprObj[0];
				final String shortTitle = (String) cprObj[1];

				displayName = shortTitle;
				// Create tree nodes for Collection Protocol
				this.setConflictTreeNode(cprid.toString(), Constants.COLLECTION_PROTOCOL,
						displayName, pid.toString(), Constants.PARTICIPANT, null, null,
						treeDataVector);

				final String scgHql = "select scg.id , scg.name , s.name"
						+ " from edu.wustl.catissuecore.domain.CollectionProtocolRegistration as cpr,"
						+ " edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg ,"
						+ " edu.wustl.catissuecore.domain.Site as s" +

						" where cpr.id = " + cprid
						+ "  and scg.id in elements(cpr.specimenCollectionGroupCollection)"
						+ " and (scg.surgicalPathologyNumber=" + null
						+ " or scg.surgicalPathologyNumber='')";

				// Query query2 = session.createQuery(hql3);
				final List scgList = this.executeQuery(scgHql);

				scgListSize = scgList.size();
				for (int scgCount = 0; scgCount < scgListSize; scgCount++)
				{

					final Object[] scgObj = (Object[]) scgList.get(scgCount);
					final String siteNamefromQuery = (String) scgObj[2];

					if (siteNamefromQuery.equals(siteName))
					{
						final Long scgid = (Long) scgObj[0];
						final String scgName = (String) scgObj[1];

						// Create tree nodes for Specimen Collection Group
						this.setConflictTreeNode(scgid.toString(),
								Constants.SPECIMEN_COLLECTION_GROUP, scgName, cprid.toString(),
								Constants.COLLECTION_PROTOCOL, pid.toString(),
								Constants.PARTICIPANT, treeDataVector);
					}
				}

			}
		}
		return treeDataVector;

	}

	/**
	 * To set the tree nodes
	 * @param identifier : identifier
	 * @param objectName : objectName
	 * @param displayName : displayName
	 * @param parentIdentifier : parentIdentifier
	 * @param parentObjectName : parentObjectName
	 * @param combinedParentIdentifier : combinedParentIdentifier
	 * @param combinedParentObjectName : combinedParentObjectName
	 * @param vector : vector
	 */
	private void setConflictTreeNode(String identifier, String objectName, String displayName,
			String parentIdentifier, String parentObjectName, String combinedParentIdentifier,
			String combinedParentObjectName, Vector vector)
	{
		final QueryTreeNodeData treeNode = new QueryTreeNodeData();
		treeNode.setIdentifier(identifier);
		treeNode.setObjectName(objectName);
		treeNode.setDisplayName(displayName);
		treeNode.setParentIdentifier(parentIdentifier);
		treeNode.setParentObjectName(parentObjectName);
		treeNode.setCombinedParentIdentifier(combinedParentIdentifier);
		treeNode.setCombinedParentObjectName(combinedParentObjectName);
		vector.add(treeNode);
	}

	/**
	 * Executes hql Query and returns the results.
	 * @param hql
	 *            String hql
	 * @throws BizLogicException
	 *             DAOException
	 * @return List
	 */
	@Override
	public List executeQuery(String hql) throws BizLogicException
	{
		DAO dao = null;

		try
		{
			dao = this.openDAOSession(null);
			final List list = dao.executeQuery(hql);
			dao.closeSession();
			return list;
		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		finally
		{
			this.closeDAOSession(dao);
		}
	}

}
