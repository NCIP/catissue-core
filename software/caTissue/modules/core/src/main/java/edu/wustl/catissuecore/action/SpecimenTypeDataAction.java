package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.io.PrintWriter;
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

import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.global.CommonServiceLocator;


/**
 * This class is called by AJAX action from StorageType.jsp, StorageContainer.jsp and SimilarContainer.jsp
 * for fetching list of Tissue, Cell, Fluid and Molecular Specimen type.
 * @author virender_mehta
 * @created-on Dec 31, 2009
 */
public class SpecimenTypeDataAction extends BaseAction
{

	/**
	 * This method is to get SpecimenType list.
	 * @param actionMapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws JSONException JSONException
	 * @throws IOException IOException
	 */
	protected ActionForward executeAction(final ActionMapping actionMapping, final ActionForm form,
			final HttpServletRequest request, final HttpServletResponse response)
			throws JSONException, IOException
	{
		final String limit = request.getParameter("limit");
		final String start = request.getParameter("start");
		final String query = request.getParameter("query");
		final String method = request.getParameter("method");
		final Integer limitFetch = Integer.parseInt(limit);
		final Integer startFetch = Integer.parseInt(start);

		final JSONArray jsonArray = new JSONArray();
		final JSONObject mainJsonObject = new JSONObject();

		final Integer total = limitFetch + startFetch;
		final List<NameValueBean> specimenTypeList= getSpTypeList(method);
		mainJsonObject.put("totalCount", specimenTypeList.size());
		for (int iCount = startFetch; iCount < total && iCount < specimenTypeList.size(); iCount++)
		{
			Locale locale = CommonServiceLocator.getInstance().getDefaultLocale();
			if (query == null
					|| specimenTypeList.get(iCount).getName().toLowerCase(locale).contains(
							query.toLowerCase(locale)) || query.length() == 0)
			{
				final JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", specimenTypeList.get(iCount).getValue());
				jsonObject.put("field", specimenTypeList.get(iCount).getName());
				jsonArray.put(jsonObject);
			}
		}
		mainJsonObject.put("row", jsonArray);
		response.flushBuffer();
		final PrintWriter out = response.getWriter();
		out.write(mainJsonObject.toString());
		return null;
	}
	/**
	 * @param method Tissue, Specimen, Cell and Fluid
	 * @return specimenTypeList
	 */
	private List<NameValueBean> getSpTypeList(final String method)
	{
		List<NameValueBean> specimenTypeList = null;
		if(Constants.TISSUE.equals(method))
		{
			specimenTypeList = AppUtility.getSpecimenTypes(Constants.TISSUE);
		}
		else if(Constants.FLUID.equals(method))
		{
			specimenTypeList = AppUtility.getSpecimenTypes(Constants.FLUID);
		}
		else if(Constants.CELL.equals(method))
		{
			specimenTypeList = AppUtility.getSpecimenTypes(Constants.CELL);
		}
		else if(Constants.MOLECULAR.equals(method))
		{
			specimenTypeList = AppUtility.getSpecimenTypes(Constants.MOLECULAR);
		}
		return specimenTypeList;
	}

}
