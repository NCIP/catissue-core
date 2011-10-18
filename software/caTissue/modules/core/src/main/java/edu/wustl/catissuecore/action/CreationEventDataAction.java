package edu.wustl.catissuecore.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.emory.mathcs.backport.java.util.Collections;
import edu.wustl.catissuecore.bizlogic.SPPBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.global.CommonServiceLocator;


public class CreationEventDataAction extends BaseAction{

	@Override
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String sppName = request.getParameter("processingSOPForSpecimen");
		String query = request.getParameter("query");
		List creationEventList=new ArrayList();
		creationEventList.add(new NameValueBean(Constants.NOT_SPECIFIED,Constants.NOT_SPECIFIED));
		creationEventList.addAll(new SPPBizLogic().getAllEventsForSPP(sppName));

		Collections.sort(creationEventList);
		JSONArray jsonArray = new JSONArray();
		JSONObject mainJsonObject = new JSONObject();

		List<NameValueBean> sopBean = new ArrayList<NameValueBean>();
		populateQuerySpecificNameValueBeansList(sopBean, creationEventList, "");
		mainJsonObject.put("totalCount", sopBean.size());

		for (int i = 0; i < sopBean.size(); i++)
		{
			JSONObject jsonObject = new JSONObject();
			Locale locale = CommonServiceLocator.getInstance().getDefaultLocale();

			if (query == null
					|| sopBean.get(i).getName().toLowerCase(locale).contains(
							query.toLowerCase(locale)) || query.length() == 0)
			{
				jsonObject.put("id", sopBean.get(i).getValue());
				jsonObject.put("field", sopBean.get(i).getName());
				jsonArray.put(jsonObject);
			}
		}
		mainJsonObject.put("row", jsonArray);
		response.flushBuffer();
		PrintWriter out = response.getWriter();
		out.write(mainJsonObject.toString());

		return null;
	}

	/**
	 * This method populates name value beans list as per query,
	 * i.e. word typed into the auto-complete drop-down text field.
	 * @param querySpecificNVBeans
	 * @param users
	 * @param query
	 */
	private void populateQuerySpecificNameValueBeansList(List<NameValueBean> querySpecificNVBeans,
			List users, String query)
	{
		Locale locale = CommonServiceLocator.getInstance().getDefaultLocale();

		for (Object obj : users)
		{
			NameValueBean nvb = (NameValueBean) obj;

			if (nvb.getName().toLowerCase(locale).contains(query.toLowerCase(locale)))
			{
				querySpecificNVBeans.add(nvb);
			}
		}
	}


}
