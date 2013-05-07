/**
 * <p>Title: InstitutionBizLogic Class>
 * <p>Description:	InstitutionBizLogic </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on Sep 19, 2006
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.Date;
import java.util.List;

import edu.wustl.catissuecore.domain.CpSyncAudit;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;

/**
 * @author
 *
 */
public class SynchronizeCollectionProtocolBizLogic extends CatissueDefaultBizLogic
{
	
	private transient final Logger logger = Logger
			.getCommonLogger(SynchronizeCollectionProtocolBizLogic.class);
	/**
	 * This method will Synchronize the CP that means it will find out whether CPR has any missing consent,Event or SPR and if it does
	 * then it will add that missing consent,event or SPR. 
	 * 
	 * @param title 
	 *            Title of the CP that needs to be synchronize.             
	 * @param sessionDataBean
	 *            Current session.
	 * @throws BizLogicException
	 */
	public void synchronizeCP(String title,SessionDataBean sessionDataBean) throws BizLogicException
	{
		
		try
		{
			CollectionProtocolRegistrationBizLogic collectionProtocolRegistrationBizLogicBizLogic=new CollectionProtocolRegistrationBizLogic();
			collectionProtocolRegistrationBizLogicBizLogic.processCPRs(title,sessionDataBean);
			
		} catch (DAOException e)
		{
			throw new BizLogicException(e);
		} 
	}
	public void updateSyncProcessStatus(CpSyncAudit cpSyncAudit,String status,Long processCPRCount,DAO dao) throws DAOException
	{
		if(!"In Process".equalsIgnoreCase(status))
		{
			cpSyncAudit.setEndDate(new Date());
		}
		cpSyncAudit.setStatus(status);
		cpSyncAudit.setProcessedCPRCount(processCPRCount);
		dao.update(cpSyncAudit);
		
	}
	public CpSyncAudit startSyncProcessAudit(Long cpId,DAO dao,Long userId) throws DAOException
	{
		ColumnValueBean columnValueBean = new ColumnValueBean("cpId", cpId);
		List<CpSyncAudit> cpSyncAudits = (List<CpSyncAudit>) dao.retrieve(CpSyncAudit.class.getName(), columnValueBean);
		CpSyncAudit cpSyncAudit=null;
		if(cpSyncAudits.isEmpty())
		{
			cpSyncAudit=new CpSyncAudit();
			cpSyncAudit.setCpId(cpId);
			cpSyncAudit.setProcessedCPRCount(0L);
			cpSyncAudit.setStartedDate(new Date());
			cpSyncAudit.setStatus("In Process");
			cpSyncAudit.setUserId(userId);
			dao.insert(cpSyncAudit);
		}
		else
		{
			cpSyncAudit=cpSyncAudits.get(0);
			cpSyncAudit.setProcessedCPRCount(0L);
			cpSyncAudit.setStartedDate(new Date());
			cpSyncAudit.setStatus("In Process");
			cpSyncAudit.setEndDate(null);
			cpSyncAudit.setUserId(userId);
			dao.update(cpSyncAudit);
		}
		return cpSyncAudit;
	}
	public CpSyncAudit getSyncStatus(Long cpId) throws BizLogicException 
	{
		DAO dao=null;
		CpSyncAudit cpSyncAudit=null;
		try
		{
			dao = getHibernateDao(getAppName(),null);
			ColumnValueBean columnValueBean = new ColumnValueBean("cpId", cpId);
			List<CpSyncAudit> cpSyncAudits = (List<CpSyncAudit>) dao.retrieve(CpSyncAudit.class.getName(), columnValueBean);
			if(!cpSyncAudits.isEmpty())
			{
				cpSyncAudit=cpSyncAudits.get(0);
			}
		} catch (DAOException e)
		{
			throw new BizLogicException(e);
		}
		finally
		{
			if(dao!=null)
			{
			  try	
			  {
				  dao.closeSession();
			  }
			  catch (DAOException e)
			  {
				throw new BizLogicException(e);
			  }
			}
		}
		return cpSyncAudit;
	}
}

