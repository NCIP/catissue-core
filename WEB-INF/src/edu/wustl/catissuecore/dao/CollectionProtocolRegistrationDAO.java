package edu.wustl.catissuecore.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;


public class CollectionProtocolRegistrationDAO
{

	public String getCPRIdForSpecimen(Long specimenId) throws DAOException 
	{
	  JDBCDAO jdbcDAO=null;
	  String cprId=null;
	  try
	  {
		 final String applicationName = CommonServiceLocator.getInstance().getAppName();
		 jdbcDAO = DAOConfigFactory.getInstance().getDAOFactory(applicationName).getJDBCDAO();
		 jdbcDAO.openSession(null); 
		 
   		 List<ColumnValueBean> columnValueBean = new ArrayList<ColumnValueBean>();
		 columnValueBean.add(new ColumnValueBean(specimenId));
		 String getSiteHQL="select cscp.COLLECTION_PROTOCOL_REG_ID from CATISSUE_SPECIMEN_COLL_GROUP as cscp join CATISSUE_SPECIMEN as cs on cs.SPECIMEN_COLLECTION_GROUP_ID=cscp.IDENTIFIER where cs.IDENTIFIER=?";
		 List cprIds=jdbcDAO.executeQuery(getSiteHQL, columnValueBean);
		 cprId=((ArrayList<String>) cprIds.get(0)).get(0);
		
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
	  return cprId;
	}

}
