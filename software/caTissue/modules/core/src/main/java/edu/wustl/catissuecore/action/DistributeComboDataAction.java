package edu.wustl.catissuecore.action;

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
import org.json.JSONObject;

import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.global.CommonServiceLocator;

public class DistributeComboDataAction extends SecureAction
{

	protected ActionForward executeSecureAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String limit = request.getParameter("limit");
		String query = request.getParameter("query");
		String start = request.getParameter("start");

		Integer limitFetch = Integer.parseInt(limit);
		Integer startFetch = Integer.parseInt(start);

		JSONArray jsonArray = new JSONArray();
		JSONObject mainJsonObject = new JSONObject();

		Integer total = limitFetch + startFetch;
		List<NameValueBean> distributionProtocolBean = new ArrayList<NameValueBean>();

		final String cprHQL = "select distProt.title,distProt.irbIdentifier"
			+ " from edu.wustl.catissuecore.domain.DistributionProtocol as distProt";
		final List<Object[]> dataList = AppUtility.executeQuery(cprHQL);
	    List clinicalDiagnosisValues = dataList;
	    getDistributionProtocolBean(distributionProtocolBean, clinicalDiagnosisValues);

	    mainJsonObject.put("totalCount", distributionProtocolBean.size());

		for (int i = startFetch; i < total && i < distributionProtocolBean.size(); i++)
		{
			JSONObject jsonObject = new JSONObject();
			Locale locale = CommonServiceLocator.getInstance().getDefaultLocale();

			if (query == null
					|| distributionProtocolBean.get(i).getName().toLowerCase(locale).contains(
							query.toLowerCase(locale)) || query.length() == 0)
			{
				jsonObject.put("id", distributionProtocolBean.get(i).getValue());
				jsonObject.put("field", distributionProtocolBean.get(i).getName());
				jsonArray.put(jsonObject);
			}
		}
		mainJsonObject.put("row", jsonArray);
		response.flushBuffer();
		PrintWriter out = response.getWriter();
		out.write(mainJsonObject.toString());

		return null;
	}

	private void getDistributionProtocolBean(
			List<NameValueBean> distributionProtocolBean,
			List<String[]> dataList)
	{
		final Iterator<String[]> iterator = dataList.iterator();
		if(!dataList.isEmpty())
		{
			distributionProtocolBean.add(new NameValueBean(Constants.SELECT_OPTION, ""+Constants.SELECT_OPTION_VALUE));
			/*if(isShowAllSet)
			{
				distributionProtocolBean.add(new NameValueBean(Constants.SHOW_ALL_VALUES+"Start", Constants.SHOW_ALL_VALUES));
			}*/
			while (iterator.hasNext())
			{
				final Object[] values = iterator.next();
				final String dpValue;
				if(values[1]==null || values[1].toString().length() == 0)
				{
					dpValue = values[0].toString();
				}
				else
				{
					dpValue = values[0]+"("+values[1]+")";
				}

				distributionProtocolBean.add(new NameValueBean(dpValue,
						dpValue));
			}
		}
	}

}
