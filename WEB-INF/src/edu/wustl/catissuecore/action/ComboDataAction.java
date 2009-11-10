
package edu.wustl.catissuecore.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.bizlogic.ComboDataBizLogic;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.dao.DAO;

/**
 *
 * @author renuka_bajpai
 *
 */
public class ComboDataAction extends BaseAction
{

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
        final String limit = request.getParameter("limit");
		final String query = request.getParameter("query");
		final String start = request.getParameter("start");
		final String  showAllValue = request.getParameter("showAll");
		final Integer limitFetch = Integer.parseInt(limit);
		final Integer startFetch = Integer.parseInt(start);
		Long collectionProtocolId = null;
		boolean isShowAll = false;
		if(!"undefined".equals(request.getParameter("collectionProtocolId")) && !"".equals(request.getParameter("collectionProtocolId")))
		{
		  collectionProtocolId = Long.parseLong(request.getParameter("collectionProtocolId"));
		}
		final HttpSession newSession = request.getSession();
		final CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean) newSession
				.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);

		Collection<NameValueBean> clinicalDiagnosisBean = new ArrayList<NameValueBean>();

		if(collectionProtocolId != null)
		{
			final String cprHQL = "select clinicalDiag.clinicalDiagnosis"
				+ " from edu.wustl.catissuecore.domain.ClinicalDiagnosis as clinicalDiag" + " where " +
						"clinicalDiag.collectionProtocol.id="+collectionProtocolId;
			final List<Object[]> dataList = AppUtility.executeQuery(cprHQL);
		    List clinicalDiagnosisValues = dataList;
		    if(showAllValue != null && Constants.SHOW_ALL_VALUES.equals(showAllValue))
		    {
		    	isShowAll = true;
		    }
		  	getClinicalDiagnosisBean(clinicalDiagnosisBean, clinicalDiagnosisValues,true);
			
		}else if(collectionProtocolBean != null && collectionProtocolBean.getClinicalDiagnosis() != null && (collectionProtocolBean.getClinicalDiagnosis()).length != 0)
		{
			List<String> dataList = Arrays.asList(collectionProtocolBean.getClinicalDiagnosis());
			getClinicalDiagnosisBean(clinicalDiagnosisBean, dataList,false);
		}	
		final ComboDataBizLogic comboDataBizObj = new ComboDataBizLogic();
		final JSONObject jsonObject = comboDataBizObj.getClinicalDiagnosisData(limitFetch,
				startFetch, query,clinicalDiagnosisBean,isShowAll);

		response.setContentType("text/javascript");
		final PrintWriter out = response.getWriter();
		out.write(jsonObject.toString());
		return null;
	}

	/**
	 * This is to get the Clinical Diagnosis bean.
	 * @param clinicalDiagnosisBean clinicalDiagnosisBean.
	 * @param dataList dataList.
	 */
	private void getClinicalDiagnosisBean(
			Collection<NameValueBean> clinicalDiagnosisBean,
			final List<String> dataList,boolean isShowAllSet)
	
	{
		final Iterator<String> iterator = dataList.iterator();
		
		if(!dataList.isEmpty())
		{
			clinicalDiagnosisBean.add(new NameValueBean(Constants.SELECT_OPTION, ""+Constants.SELECT_OPTION_VALUE));
			if(isShowAllSet)
			{
				clinicalDiagnosisBean.add(new NameValueBean(Constants.SHOW_ALL_VALUES+"Start", Constants.SHOW_ALL_VALUES));
			}
			while (iterator.hasNext())
			{
				final String clinicaDiagnosisvalue = (String)iterator.next();
				clinicalDiagnosisBean.add(new NameValueBean(clinicaDiagnosisvalue,
						clinicaDiagnosisvalue));

			}

			if(isShowAllSet)
			{
				clinicalDiagnosisBean.add(new NameValueBean(Constants.SHOW_ALL_VALUES+"End", Constants.SHOW_ALL_VALUES));
			}
		}
	}
}