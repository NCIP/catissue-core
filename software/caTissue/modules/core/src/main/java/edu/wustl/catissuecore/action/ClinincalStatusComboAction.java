package edu.wustl.catissuecore.action;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.wustl.catissuecore.bizlogic.SPPBizLogic;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.logger.Logger;

public class ClinincalStatusComboAction extends BaseAction
{

	private transient final Logger logger = Logger.getCommonLogger(ClinincalStatusComboAction.class);

	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
			{

		final String limit = request.getParameter("limit");
		final String query = request.getParameter("query");
		final String start = request.getParameter("start");
		final String  showOption = request.getParameter("showOption");
		final Integer limitFetch = Integer.parseInt(limit);
		final Integer startFetch = Integer.parseInt(start);
		final String requestFor = request.getParameter("requestFor");
		List clinicalStatusList = null;
		SPPBizLogic sopBizLogic = new SPPBizLogic();
		if("specimenClass".equals(requestFor))
		{
			clinicalStatusList = AppUtility.getSpecimenClassList();
		}
		else if("specimenType".equals(requestFor))
		{
			String specimenClass = request.getParameter("selSpecClass");
			clinicalStatusList = AppUtility.getSpecimenTypes(specimenClass);
		}
		else if("storageLocation".equals(requestFor))
		{
			
			clinicalStatusList = new LinkedList();
			clinicalStatusList.add(new NameValueBean("Virtual", "Virtual"));
			clinicalStatusList.add(new NameValueBean("Auto", "Auto"));
			clinicalStatusList.add(new NameValueBean("Manual", "Manual"));
		}
		else if("specimenEvent".equals(requestFor))
		{
			String processingSPP = request.getParameter("processingSPPName");
			clinicalStatusList = sopBizLogic.getAllEventsForSPP(processingSPP);
		}
		else if("processingSPP".equals(requestFor))
		{
			clinicalStatusList = sopBizLogic.getAllSPPNames();
		}
		else
		{
			clinicalStatusList = CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_CLINICAL_STATUS, null);
		}
		final JSONObject jsonObject = getClinicalStatusData(limitFetch,
				startFetch, clinicalStatusList, query,showOption);

		response.setContentType("text/javascript");
		final PrintWriter out = response.getWriter();
		out.write(jsonObject.toString());
		return null;
	}

	private JSONObject getClinicalStatusData(Integer limitFetch,
			Integer startFetch, List clinicalStatusList, String query,
			String showOption) {
		JSONObject jsonObject = null;
		JSONArray jsonArray = null;
		try
		{
			jsonArray = new JSONArray();
			jsonObject = new JSONObject();
			jsonObject.put("totalCount", new Integer(clinicalStatusList.size()));
			final ListIterator iterator = clinicalStatusList.listIterator(startFetch);
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
						innerJsonObject.put("field", nameValueBean.getName());
						innerJsonObject.put("id", nameValueBean.getValue());
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
