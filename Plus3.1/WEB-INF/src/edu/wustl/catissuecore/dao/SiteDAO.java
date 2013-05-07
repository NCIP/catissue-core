package edu.wustl.catissuecore.dao;

import java.util.ArrayList;
import java.util.List;

import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.dao.DAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;


public class SiteDAO
{
	SessionDataBean sessionDataBean=null;
	public SiteDAO(SessionDataBean sessionDataBean)
	{
		this.sessionDataBean=sessionDataBean;
	}
	public Site getSite(ParticipantMedicalIdentifier pmi) throws DAOException 
	{
	  DAO hibernateDao = null;
	  Site site=null;
	  try
	  {
		 final IDAOFactory daoFact = DAOConfigFactory.getInstance().getDAOFactory(CommonServiceLocator.getInstance().getAppName());
		 hibernateDao = daoFact.getDAO();
   		 hibernateDao.openSession(sessionDataBean);  
		 
   		 List<ColumnValueBean> columnValueBean = new ArrayList<ColumnValueBean>();
		 columnValueBean.add(new ColumnValueBean(pmi.getId()));
		 String getSiteHQL="select site from edu.wustl.catissuecore.domain.Site as site inner join  edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier pmi where site.id=pmi.id and pmi.id=?";
		 List<Site> sites=hibernateDao.executeQuery(getSiteHQL, columnValueBean);
		 site=sites.get(0);
	  }
	  catch(DAOException daoException)
	  {
		  daoException.printStackTrace();
		  throw daoException;
	  }
	  finally
	  {
		 if(hibernateDao!=null)   
		 {
			 hibernateDao.closeSession();
		 }
	  }
	  return site;
	}

}
