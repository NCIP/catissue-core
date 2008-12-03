package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.CDEBizLogic;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEImpl;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.cde.PermissibleValueImpl;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAOImpl;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * 
 * @author sagar_baldwa
 *
 */
public class ComboDataBizLogic extends DefaultBizLogic
{
	/**
	 * This method would return the Clinical Diagnosis List
	 * @return List which contains the Clinical Diagnosis Data
	 * @throws DAOException 
	 * @throws DAOException 
	 */
	private List getClinicalDiagnosisList(String query) throws DAOException 
	{
		//populating clinical Diagnosis field 
//		
		String sourceObjectName =PermissibleValueImpl.class.getName();
		String[] selectColumnName ={"value"};
		String[] whereColumnName ={"value" ,"cde.publicId"}; // "storageContainer."+Constants.SYSTEM_IDENTIFIER
		String[] whereColumnCondition = {"like","=" };
		Object[] whereColumnValue ={"%"+query+"%","Clinical_Diagnosis_PID"};
		String joinCondition = null;
		
	//	String hql= "from "+PermissibleValueImpl.class.getName();
		
//		AbstractDAO hibernateDao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
//		hibernateDao.openSession(null);
//		
//		
//		List list1 = bizLogic.retrieve(PermissibleValueImpl.class.getName(),"cde.publicId","Clinical_Diagnosis_PID");
//		Logger.out.info("************************"+list1.size());
//		

		IBizLogic bizLogic = new DefaultBizLogic();
//		List list1 = bizLogic.retrieve(CDE.class.getName(),"publicId","Clinical_Diagnosis_PID");
		List clinicalDiagnosisList = new ArrayList();
		try {
			 Iterator<String> iterator = bizLogic.retrieve(sourceObjectName,selectColumnName,whereColumnName, whereColumnCondition,whereColumnValue, joinCondition).iterator();

//			CDEBizLogic cdeBizLogic = (CDEBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.CDE_FORM_ID);
			clinicalDiagnosisList.add(new NameValueBean(Constants.SELECT_OPTION, "" + Constants.SELECT_OPTION_VALUE));
			
			while(iterator.hasNext())
			{
				 String clinicaDiagnosisvalue=iterator.next();
				 clinicalDiagnosisList.add(new NameValueBean(clinicaDiagnosisvalue,clinicaDiagnosisvalue));
				 
			}
			Collections.sort(clinicalDiagnosisList);
			//cdeBizLogic.getFilteredCDE( new HashSet(list), clinicalDiagnosisList);		
		} catch (DAOException e) {
			e.printStackTrace();

		}

//		CDE cde = CDEManager.getCDEManager().getCDE(Constants.CDE_NAME_CLINICAL_DIAGNOSIS);
//		CDEBizLogic cdeBizLogic = (CDEBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.CDE_FORM_ID);
//		List clinicalDiagnosisList = new ArrayList();
//		clinicalDiagnosisList.add(new NameValueBean(Constants.SELECT_OPTION, "" + Constants.SELECT_OPTION_VALUE));
//		cdeBizLogic.getFilteredCDE(cde.getPermissibleValues(), clinicalDiagnosisList);		
		return clinicalDiagnosisList;		
	}
	
	/**
	 * This method fetches the required number of records from Clinical Diagnosis List
	 * and poplulates in the Combo Box on UI.
	 * @param limitFetch is the limit to fetch and display the Clinical Diagnosis 
	 * records in Combo Box on UI
	 * @param startFetch is the position from where you fetch the Clinical Diagnosis
	 *  data from the List
	 * @param query holds the string which the user has typed down in combo box 
	 * for autocomplete feature
	 * @return JSONObject which holds the list to eb poplulated on UI front
	 */
	public JSONObject getClinicalDiagnosisData(Integer limitFetch, Integer startFetch,
			String query)
	{
		JSONObject jsonObject = null;
		JSONArray jsonArray = null;
		try
		{		
			jsonArray = new JSONArray();
			jsonObject = new JSONObject();			
			List clinicalDiagnosisList =getClinicalDiagnosisList(query);			
			jsonObject.put("totalCount", new Integer(clinicalDiagnosisList.size()));
			ListIterator iterator = clinicalDiagnosisList.listIterator(startFetch+1);
			Integer total = limitFetch + startFetch;
			//1st record in List has value -1, so startFetch is incremented and 
			//made to fetch data from 2nd element from the List
			startFetch++;
			boolean flag = false;
			while(startFetch < total+1)
			{
				if(iterator.hasNext())
				{
					NameValueBean nameValueBean = (NameValueBean) iterator.next();
					if(nameValueBean.getName().toLowerCase().contains(query.toLowerCase()) || query == null)
					{
						JSONObject innerJsonObject = new JSONObject();
						//nameValueBean = (NameValueBean) iterator.next();
						innerJsonObject.put("id", nameValueBean.getName());
						innerJsonObject.put("field", nameValueBean.getValue());
						jsonArray.put(innerJsonObject);	
						flag = true;
					}
					else
					if(flag)
					{
						break;
					}
					
				}
				startFetch++;
			}			
			jsonObject.put("row", jsonArray);				
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e);
		}	
		return jsonObject;
	}	
}
