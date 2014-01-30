package krishagni.catissueplus.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;


public class SiteDAO
{
	private static final Logger LOGGER = Logger.getCommonLogger(SiteDAO.class);
	public Site getSite(ParticipantMedicalIdentifier pmi) throws DAOException 
	{
	  DAO hibernateDao = null;
	  Site site=null;
	  try
	  {
		 final IDAOFactory daoFact = DAOConfigFactory.getInstance().getDAOFactory(CommonServiceLocator.getInstance().getAppName());
		 hibernateDao = daoFact.getDAO();
   		 hibernateDao.openSession(null);  
		 
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
	
	public Long getIdBySiteName(HibernateDAO hibenrateDAO,String siteName) throws BizLogicException
	{
		try
		{
			Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
			params.put("0", new NamedQueryParam(DBTypes.STRING, siteName));
			List<Long> idList = hibenrateDAO.executeNamedQuery("getSiteIdByName", params);
			if(idList == null || idList.isEmpty())
			{
				throw new BizLogicException(null, null, "invalid.site.name", siteName);
			}
			return idList.get(0);
		}
		catch (DAOException e)
		{
			LOGGER.error(e);
			throw new BizLogicException(null, null,
					"errors.executeQuery.genericmessage", "");
		}
	}
	
	  public Long getSiteIdByName(String siteName) throws SQLException, ApplicationException
	    {
	      DAO dao = null;
	        try
	        {
	            dao = AppUtility.openDAOSession(null);
	            return this.getIdBySiteName((HibernateDAO)dao, siteName);
	        }
	        finally
	        {
	            AppUtility.closeDAOSession(dao);
	        }
	     
	    }

	
	public Boolean isSitePresent(HibernateDAO hibenrateDAO,String siteName, Long siteId) throws BizLogicException
	{
		try
		{
			siteName = siteName==null?"":siteName;
			siteId = siteId == null?0l:siteId;
			String hql = "select count(*) from "+Site.class.getName()+" where id="+siteId+" or name ='"+siteName+"'";
//			Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
//			params.put("0", new NamedQueryParam(DBTypes.STRING, siteName));
//			List<Long> idList = hibenrateDAO.executeNamedQuery("getSiteIdByName", params);
			List result = hibenrateDAO.executeQuery(hql);
			if(result == null || result.isEmpty())
			{
				String message = ApplicationProperties
						.getValue("specimenCollectionGroup.site");
				throw new BizLogicException(null, null, "errors.item.invalid", message);
			}
			return true;
		}
		catch (DAOException e)
		{
			LOGGER.error(e);
			throw new BizLogicException(null, null,
					"errors.executeQuery.genericmessage", "");
		}
	}

}
