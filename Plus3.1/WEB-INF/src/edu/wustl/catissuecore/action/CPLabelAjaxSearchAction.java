
package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.labelSQLApp.bizlogic.LabelSQLBizlogic;
import edu.wustl.common.labelSQLApp.domain.LabelSQL;

/**
 * The Class ClinportalStudyFormAjaxSearchAction.
 */
public class CPLabelAjaxSearchAction extends SecureAction
{

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.wustl.common.action.SecureAction#executeSecureAction(org.apache.struts
	 * .action.ActionMapping, org.apache.struts.action.ActionForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws ApplicationException,
			JSONException, IOException, BizLogicException, Exception
	{
		
		String varlimit = request.getParameter("limit");
		String varquery = request.getParameter("query");
		String varstart = request.getParameter("start");
		Integer varstartFetch = 0;
		Integer varlimitFetch = 999;
		if (varquery == null)
		{
			varquery = "";
		}
		if (varlimit != null)
		{
			varlimitFetch = Integer.parseInt(varlimit);
		}
		if (varstart != null)
		{
			varstartFetch = Integer.parseInt(varstart);
		}

		JSONArray varjsonArray = new JSONArray();
		JSONObject varmainJsonObject = new JSONObject();

		Integer total = varlimitFetch + varstartFetch;
		List vardataList = new ArrayList();
		String cpId = request.getParameter("id");

		if (request.getParameter(Constants.OPERATION) != null
				&& request.getParameter(Constants.OPERATION).equalsIgnoreCase("fetchLabels"))
		{
			List<LabelSQL> labelSQLs = new LabelSQLBizlogic().getAllLabelSQL();
			for (Iterator iterator = labelSQLs.iterator(); iterator.hasNext();)
			{
				LabelSQL labelSQL = (LabelSQL) iterator.next();
				vardataList.add(new NameValueBean(labelSQL.getLabel(), labelSQL.getId()));
			}
			//			vardataList = labelSQLs;
		}

		List<NameValueBean> varquerySpecificNVBeans = new ArrayList<NameValueBean>();
		populateQuerySpecificNameValueBeansList(varquerySpecificNVBeans, vardataList, varquery);
		varmainJsonObject.put("totalCount", varquerySpecificNVBeans.size());

		for (int i = varstartFetch; i < total && i < varquerySpecificNVBeans.size(); i++)
		{
			JSONObject jsonObject = new JSONObject();
			Locale locale = CommonServiceLocator.getInstance().getDefaultLocale();

			if (varquery == null
					|| varquerySpecificNVBeans.get(i).getName().toLowerCase(locale).contains(
							varquery.toLowerCase(locale)) || varquery.length() == 0)
			{
				jsonObject.put("id", varquerySpecificNVBeans.get(i).getValue());
				jsonObject.put("field", varquerySpecificNVBeans.get(i).getName());
				varjsonArray.put(jsonObject);
			}
		}

		varmainJsonObject.put("row", varjsonArray);
		PrintWriter out = response.getWriter();
		out.write(varmainJsonObject.toString());

		return null;
	}

	/**
	 * This method populates name value beans list as per query,
	 * i.e. word typed into the auto-complete drop-down text field.
	 *
	 * @param querySpecificNVBeans the query specific nv beans
	 * @param cplist the cplist
	 * @param query the query
	 */
	public static synchronized void populateQuerySpecificNameValueBeansList(
			List<NameValueBean> querySpecificNVBeans, List cplist, String query)
	{
		for (Object obj : cplist)
		{
			NameValueBean nvb = (NameValueBean) obj;

			Locale locale = CommonServiceLocator.getInstance().getDefaultLocale();
			if (nvb.getName().toLowerCase(locale).contains(query.toLowerCase(locale)))
			{
				querySpecificNVBeans.add(nvb);
			}
		}
	}

}
