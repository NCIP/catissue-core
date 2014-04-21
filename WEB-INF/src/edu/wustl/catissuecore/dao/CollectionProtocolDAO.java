package edu.wustl.catissuecore.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.DAOUtility;
import edu.wustl.dao.util.NamedQueryParam;


public class CollectionProtocolDAO
{

	private static final Logger LOGGER = Logger.getCommonLogger(UserDAO.class);
	public Map<String,String> getAllLabelFormatsForSpecimen(Long specimenId) throws DAOException 
	{
	  JDBCDAO jdbcDAO=null;
	  Map<String, String> lableFormates=new HashMap<String, String>();
	  try
	  {
		 final String applicationName = CommonServiceLocator.getInstance().getAppName();
		 jdbcDAO = DAOConfigFactory.getInstance().getDAOFactory(applicationName).getJDBCDAO();
		 jdbcDAO.openSession(null); 
		 
   		 List<ColumnValueBean> columnValueBean = new ArrayList<ColumnValueBean>();
		 columnValueBean.add(new ColumnValueBean(specimenId));
		 String getSiteHQL="select csp.LABEL_FORMAT,csp.DERIV_LABEL_FORMAT,csp.ALIQUOT_LABEL_FORMAT from CATISSUE_COLLECTION_PROTOCOL as csp join CATISSUE_COLL_PROT_REG as cpr on cpr.COLLECTION_PROTOCOL_ID=csp.IDENTIFIER join CATISSUE_SPECIMEN_COLL_GROUP as cscp on cscp.COLLECTION_PROTOCOL_REG_ID=cpr.IDENTIFIER join CATISSUE_SPECIMEN as cs on cs.SPECIMEN_COLLECTION_GROUP_ID=cscp.IDENTIFIER where cs.IDENTIFIER=?";
		 List groupNames=jdbcDAO.executeQuery(getSiteHQL, columnValueBean);
		 ArrayList<String> lableFormate=(ArrayList<String>)groupNames.get(0);
		 lableFormates.put("LABEL_FORMAT", lableFormate.get(0));
		 lableFormates.put("DERIV_LABEL_FORMAT", lableFormate.get(1));
		 lableFormates.put("ALIQUOT_LABEL_FORMAT", lableFormate.get(2));
	  }
	  catch(DAOException daoException)
	  {
		  daoException.printStackTrace();
		  throw daoException;
	  }
	  finally
	  {
		 if(jdbcDAO!=null)   
		 {
			 jdbcDAO.closeSession();
		 }
	  }
	  return lableFormates;
	}
	
	public List<Long> getAssociatedSiteIds(HibernateDAO hibernateDAO, Long cpId) throws DAOException
	{
		List<Long> list = new ArrayList<Long>();
		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		params.put("1", new NamedQueryParam(DBTypes.LONG, cpId));
		try {
			ResultSet resultset = hibernateDAO.executeNamedSQLQuery("getSiteIdsByCPID", params);
			List resultList = DAOUtility.getInstance().getListFromRS(resultset);
			if(resultList != null && !resultList.isEmpty())
			{
				for (Object object : resultList) 
				{
					Long val = Long.valueOf(((List)object).get(0).toString());
					list.add(val);
				}
			}
		} catch (SQLException e) {
			LOGGER.error(e);
			throw new DAOException(null, e, null);
		}
		return list;
	}
}
