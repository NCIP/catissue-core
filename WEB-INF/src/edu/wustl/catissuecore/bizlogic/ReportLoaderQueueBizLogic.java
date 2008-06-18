package edu.wustl.catissuecore.bizlogic;

import java.util.List;
import java.util.Vector;

import edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.tree.QueryTreeNodeData;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * This class is used add, update and retrieve report loader queue  
 * information using Hibernate.
 */

public class ReportLoaderQueueBizLogic extends DefaultBizLogic
{

	/**
	 * Saves the ReportLoaderQueue object in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		ReportLoaderQueue reportLoaderQueue = (ReportLoaderQueue) obj;
		dao.insert(reportLoaderQueue, sessionDataBean, false, false);
 	}

	/**
	 * Deletes the ReportLoaderQueue object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void delete(Object obj, DAO dao) throws DAOException, UserNotAuthorizedException
	{
		dao.delete(obj);
	}


	/**
	 * Updates the ReportLoaderQueue object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		ReportLoaderQueue reportLoaderQueue = (ReportLoaderQueue)obj;
		dao.update(reportLoaderQueue, sessionDataBean, false, false, false);
	}
	
	/**
	 * This function takes identifier as parameter and returns corresponding TextContent
	 * @return - TextContent object
	 */
	public ReportLoaderQueue getReportLoaderQueueById(Long identifier) throws Exception
	{
		Object object = retrieve(ReportLoaderQueue.class.getName(), identifier);
		ReportLoaderQueue reportLoaderQueue = (ReportLoaderQueue) object;
		return reportLoaderQueue;
	}
	
	/** To retrieve the tree nodes 
	 * @param reportQueueId
	 * @param siteName
	 * @param treeDataVector
	 * @return
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	public Vector getTreeViewData(Long reportQueueId,String siteName,Vector treeDataVector) throws DAOException, ClassNotFoundException
	{
			
		int partcipantListSize=0,cprListSize=0,scgListSize=0;
		String displayName="";
		
		/*
		 * Query to fetch the list of participants:
		 */
		String partHql = "select p.id , p.lastName ,p.firstName "+
		" from edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue as rlq, " +
		" edu.wustl.catissuecore.domain.Participant as p"+
		" where rlq.id= " +reportQueueId+ " and p.id in elements(rlq.participantCollection) " ;


		//Query query0 = session.createQuery(hql1);
		List participantList=(List)executeQuery(partHql);
		
		
		partcipantListSize = participantList.size();
		for (int partCount = 0; partCount < partcipantListSize; partCount++)
		{
			Object[] participantObj = (Object[]) participantList.get(partCount);
			Long pid = (Long) participantObj[0];
			String lastName = (String) participantObj[1];
			String firtName = (String) participantObj[2];
			
			displayName = lastName+"," +firtName;
			setConflictTreeNode(pid.toString(),
					Constants.PARTICIPANT,displayName, "0", null, null, null,treeDataVector);
			
			
			/*
			 * Query to fetch the list of collection protocol:
			 */
			
			String cprHql = "select cpr.id , cpr.collectionProtocol.shortTitle"+
			" from edu.wustl.catissuecore.domain.CollectionProtocolRegistration cpr" +
			" where cpr.participant.id =" + pid + "" ;
			
		//	Query query1 = session.createQuery(hql2);
			List cprList=(List)executeQuery(cprHql);
			
			cprListSize = cprList.size();
			for (int cprCount = 0; cprCount < cprListSize; cprCount++)
			{
				
				Object[] cprObj = (Object[]) cprList.get(cprCount);
				Long cprid = (Long) cprObj[0];
				String shortTitle = (String) cprObj[1];
				
				displayName = shortTitle;
				//Create tree nodes for Collection Protocol
				setConflictTreeNode(cprid.toString(), 
							Constants.COLLECTION_PROTOCOL,displayName,pid.toString() ,Constants.PARTICIPANT,
							null, null, treeDataVector);
			
				
				
				String scgHql = "select scg.id , scg.name , s.name"+
				" from edu.wustl.catissuecore.domain.CollectionProtocolRegistration as cpr,"+
				" edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg ,"+
				" edu.wustl.catissuecore.domain.Site as s"+
				
				" where cpr.id = "+cprid + "  and scg.id in elements(cpr.specimenCollectionGroupCollection)" +
				" and (scg.surgicalPathologyNumber="+null+
				" or scg.surgicalPathologyNumber='')";
				
				
				
			//	Query query2 = session.createQuery(hql3);
				List scgList=(List)executeQuery(scgHql);
				
				scgListSize = scgList.size();
				for (int scgCount = 0; scgCount < scgListSize; scgCount++)
				{
					
					
					Object[] scgObj = (Object[]) scgList.get(scgCount);
					String siteNamefromQuery = (String) scgObj[2];
					
					if(siteNamefromQuery.equals(siteName))
					{
						Long scgid = (Long) scgObj[0];
						String scgName = (String) scgObj[1];
		
						//	Create tree nodes for Specimen Collection Group
							setConflictTreeNode(scgid.toString() , Constants.SPECIMEN_COLLECTION_GROUP, scgName ,
							cprid.toString(), Constants.COLLECTION_PROTOCOL,
							pid.toString(),Constants.PARTICIPANT,treeDataVector);
					}			
				}

			}
		}
		return treeDataVector;
		
	}
	
	
	/** 
	 * To set the tree nodes 
	 * @param identifier
	 * @param objectName
	 * @param displayName
	 * @param parentIdentifier
	 * @param parentObjectName
	 * @param combinedParentIdentifier
	 * @param combinedParentObjectName
	 * @param vector
	 */
	private void setConflictTreeNode(String identifier,String objectName, String displayName,String parentIdentifier,String parentObjectName,String combinedParentIdentifier,String combinedParentObjectName,Vector vector)
	{
		QueryTreeNodeData treeNode = new QueryTreeNodeData();
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
	 * @param hql String hql
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	public List executeQuery(String hql) throws DAOException, ClassNotFoundException
	{
		HibernateDAO dao = (HibernateDAO) DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		dao.openSession(null);
		List list = dao.executeQuery(hql, null, false, null);
		dao.closeSession();
		return list;
	}
			
	
}
