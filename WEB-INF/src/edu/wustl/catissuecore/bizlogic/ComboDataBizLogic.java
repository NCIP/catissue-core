
package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
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
import edu.wustl.dao.exception.DAOException;

/**
 * @author sagar_baldwa
 */
public class ComboDataBizLogic extends CatissueDefaultBizLogic
{

	private transient final Logger logger = Logger.getCommonLogger(ComboDataBizLogic.class);

	/**
	 * This method would return the Clinical Diagnosis List
	 * @return List which contains the Clinical Diagnosis Data
	 * @throws BizLogicException
	 * @throws BizLogicException
	 */
	public List getClinicalDiagnosisList(String query,boolean showSubset) throws BizLogicException
	{
		// populating clinical Diagnosis field
		final List<NameValueBean> clinicalDiagnosisList = new ArrayList();
		final String sourceObjectName = PermissibleValueImpl.class.getName();
		final String[] selectColumnName = {"value"};
		DAO dao = null;
		try
		{

			new DefaultBizLogic();

			dao = this.openDAOSession(null);

			String hql = "Select PermissibleValueImpl.value from edu.wustl.common.cde.PermissibleValueImpl PermissibleValueImpl WHERE PermissibleValueImpl.value like lower('%"+query+"%') AND PermissibleValueImpl.cde.publicId = 'Clinical_Diagnosis_PID'";

			List dataList = dao.executeQuery(hql);

			this.closeDAOSession(dao);

			final Iterator<String> iterator = dataList.iterator();

			clinicalDiagnosisList.add(new NameValueBean(Constants.SELECT_OPTION, ""
					+ Constants.SELECT_OPTION_VALUE));

			if(showSubset)
			{
				clinicalDiagnosisList.add(new NameValueBean(Constants.SHOW_SUBSET+"start", Constants.SHOW_SUBSET));
			}
			while (iterator.hasNext())
			{
				final String clinicaDiagnosisvalue = iterator.next();
				clinicalDiagnosisList.add(new NameValueBean(clinicaDiagnosisvalue,
						clinicaDiagnosisvalue));

			}
			if(showSubset)
			{
				clinicalDiagnosisList.add(new NameValueBean(Constants.SHOW_SUBSET+"end",Constants.SHOW_SUBSET));
			}
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
	public JSONObject getClinicalDiagnosisData(Integer limitFetch, Integer startFetch, String query,
			Collection<NameValueBean> clinicalDiagnosisBean,String showOption)
	{
		JSONObject jsonObject = null;
		JSONArray jsonArray = null;
		try
		{
			jsonArray = new JSONArray();
			jsonObject = new JSONObject();
			final List clinicalDiagnosisList;
			boolean showSubset = false;
						
			if(!clinicalDiagnosisBean.isEmpty() && Constants.SHOW_ALL_VALUES.equals(showOption))
			{
				showSubset = true;
			}

			if (clinicalDiagnosisBean == null || clinicalDiagnosisBean.isEmpty() || Constants.SHOW_ALL_VALUES.equals(showOption))
			{
				clinicalDiagnosisList = this.getClinicalDiagnosisList(query,showSubset);
			}
			else
			{
			  clinicalDiagnosisList = (List) clinicalDiagnosisBean;
			}
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
