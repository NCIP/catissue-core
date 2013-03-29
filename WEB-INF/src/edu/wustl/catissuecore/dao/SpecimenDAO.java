package edu.wustl.catissuecore.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.dao.DAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;

/**
 * @author rinku
 *
 */
public class SpecimenDAO {

	/**Get the specimen label and id for given scgId.
	 * @param scgId
	 * @return
	 * @throws DAOException
	 */
	public List<NameValueBean> getSpecimenLableAndId(DAO dao,Long scgId) throws DAOException {
	
		
		List specimens = null;
		List<NameValueBean> nvBeanList = new ArrayList<NameValueBean>();
		try{
			final String applicationName = CommonServiceLocator.getInstance().getAppName();
			String hql = "select specimen.id,specimen.label from edu.wustl.catissuecore.domain.Specimen as specimen where specimen.specimenCollectionGroup.id = ? and specimen.collectionStatus='Collected'";
		    ColumnValueBean columnValueBean=new ColumnValueBean(scgId);
	        List<ColumnValueBean>  columnValueBeans=new ArrayList();
	        columnValueBeans.add(columnValueBean);
	        specimens=dao.executeQuery(hql, columnValueBeans);
	        
	        for(Object specimen:specimens)
			{		
			        Object[] sp = (Object[]) specimen;
	        	    NameValueBean nvb = new NameValueBean();
			        nvb.setName((String)sp[1]);
			        nvb.setValue((Long)sp[0]);
			        nvBeanList.add(nvb);
			}
		}
		catch(DAOException daoException)
		  {
			  daoException.printStackTrace();
			  throw daoException;
		  }
	   return nvBeanList;
	}	
}
