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


public class CollectionProtocolDAO
{

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
		 String getSiteHQL="select csp.LABEL_FORMAT,csp.DERIV_LABEL_FORMAT,csp.ALIQUOT_LABEL_FORMAT from CATISSUE_SPECIMEN_PROTOCOL as csp join CATISSUE_COLL_PROT_REG as cpr on cpr.COLLECTION_PROTOCOL_ID=csp.IDENTIFIER join CATISSUE_SPECIMEN_COLL_GROUP as cscp on cscp.COLLECTION_PROTOCOL_REG_ID=cpr.IDENTIFIER join CATISSUE_SPECIMEN as cs on cs.SPECIMEN_COLLECTION_GROUP_ID=cscp.IDENTIFIER where cs.IDENTIFIER=?";
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

}
