
package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.cde.PermissibleValueImpl;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.condition.LikeClause;
import edu.wustl.dao.exception.DAOException;

/**
 * @author sagar_baldwa
 */
public class ComboDataBizLogic extends CatissueDefaultBizLogic
{

	private transient final Logger logger = Logger.getCommonLogger(ComboDataBizLogic.class);

	/**
	 * This method would return the Clinical Diagnosis List
	 * 
	 * @return List which contains the Clinical Diagnosis Data
	 * @throws BizLogicException
	 * @throws BizLogicException
	 */
	private List getClinicalDiagnosisList(String query) throws BizLogicException
	{
		// populating clinical Diagnosis field
		//		
		final List clinicalDiagnosisList = new ArrayList();
		final String sourceObjectName = PermissibleValueImpl.class.getName();
		final String[] selectColumnName = {"value"};
		// DAO hibernateDao = DAOFactory.getInstance().getDAO(0);
		// hibernateDao.openSession(null);
		//		
		//		
		// List list1 =
		// bizLogic.retrieve(PermissibleValueImpl.class.getName(),"cde.publicId"
		// ,"Clinical_Diagnosis_PID");
		// Logger.out.info("************************"+list1.size());
		//		
		DAO dao = null;
		try
		{

			new DefaultBizLogic();

			dao = this.openDAOSession(null);

			final QueryWhereClause whereClause = new QueryWhereClause(sourceObjectName);
			whereClause.addCondition(new LikeClause("value", "%" + query + "%")).andOpr()
					.addCondition(new EqualClause("cde.publicId", "Clinical_Diagnosis_PID"));

			final List dataList = dao.retrieve(sourceObjectName, selectColumnName, whereClause);

			this.closeDAOSession(dao);

			// Iterator<String> iterator =
			// bizLogic.retrieve(sourceObjectName,selectColumnName
			// ,whereColumnName, whereColumnCondition,whereColumnValue,
			// joinCondition).iterator();
			final Iterator<String> iterator = dataList.iterator();

			// CDEBizLogic cdeBizLogic = (CDEBizLogic)
			// BizLogicFactory.getInstance().getBizLogic(Constants.CDE_FORM_ID);
			clinicalDiagnosisList.add(new NameValueBean(Constants.SELECT_OPTION, ""
					+ Constants.SELECT_OPTION_VALUE));

			while (iterator.hasNext())
			{
				final String clinicaDiagnosisvalue = iterator.next();
				clinicalDiagnosisList.add(new NameValueBean(clinicaDiagnosisvalue,
						clinicaDiagnosisvalue));

			}
			Collections.sort(clinicalDiagnosisList);
			// cdeBizLogic.getFilteredCDE( new HashSet(list),
			// clinicalDiagnosisList);

			// CDE cde = CDEManager.getCDEManager().getCDE(Constants.
			// CDE_NAME_CLINICAL_DIAGNOSIS);
			// CDEBizLogic cdeBizLogic = (CDEBizLogic)
			// BizLogicFactory.getInstance().getBizLogic(Constants.CDE_FORM_ID);
			// List clinicalDiagnosisList = new ArrayList();
			// clinicalDiagnosisList.add(new
			// NameValueBean(Constants.SELECT_OPTION, "" +
			// Constants.SELECT_OPTION_VALUE));
			// cdeBizLogic.getFilteredCDE(cde.getPermissibleValues(),
			// clinicalDiagnosisList);

		}
		catch (final DAOException exp)
		{
			this.logger.error(exp.getMessage(),exp);
			exp.printStackTrace();
			throw this.getBizLogicException(exp, exp.getErrorKeyName(), exp.getMsgValues());
		}
		finally
		{
			this.closeDAOSession(dao);
		}
		return clinicalDiagnosisList;
	}

	/**
	 * This method fetches the required number of records from Clinical
	 * Diagnosis List and poplulates in the Combo Box on UI.
	 * 
	 * @param limitFetch
	 *            is the limit to fetch and display the Clinical Diagnosis
	 *            records in Combo Box on UI
	 * @param startFetch
	 *            is the position from where you fetch the Clinical Diagnosis
	 *            data from the List
	 * @param query
	 *            holds the string which the user has typed down in combo box
	 *            for autocomplete feature
	 * @return JSONObject which holds the list to eb poplulated on UI front
	 */
	public JSONObject getClinicalDiagnosisData(Integer limitFetch, Integer startFetch, String query)
	{
		JSONObject jsonObject = null;
		JSONArray jsonArray = null;
		try
		{
			jsonArray = new JSONArray();
			jsonObject = new JSONObject();
			final List clinicalDiagnosisList = this.getClinicalDiagnosisList(query);
			jsonObject.put("totalCount", new Integer(clinicalDiagnosisList.size()));
			final ListIterator iterator = clinicalDiagnosisList.listIterator(startFetch + 1);
			final Integer total = limitFetch + startFetch;
			// 1st record in List has value -1, so startFetch is incremented and
			// made to fetch data from 2nd element from the List
			startFetch++;
			boolean flag = false;
			while (startFetch < total + 1)
			{
				if (iterator.hasNext())
				{
					final NameValueBean nameValueBean = (NameValueBean) iterator.next();
					if (nameValueBean.getName().toLowerCase().contains(query.toLowerCase())
							|| query == null)
					{
						final JSONObject innerJsonObject = new JSONObject();
						// nameValueBean = (NameValueBean) iterator.next();
						innerJsonObject.put("id", nameValueBean.getName());
						innerJsonObject.put("field", nameValueBean.getValue());
						jsonArray.put(innerJsonObject);
						flag = true;
					}
					else if (flag)
					{
						break;
					}

				}
				startFetch++;
			}
			jsonObject.put("row", jsonArray);
		}
		catch (final Exception e)
		{
			this.logger.error(e.getMessage(), e);
			e.printStackTrace();
			//System.out.println(e);
		}
		return jsonObject;
	}
}
