
package edu.wustl.catissuecore.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import edu.wustl.catissuecore.bizlogic.ComboDataBizLogic;
import edu.wustl.common.action.BaseAction;
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
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		String limit = request.getParameter("limit");
		String query = request.getParameter("query");
		String start = request.getParameter("start");
		Integer limitFetch = Integer.parseInt(limit);
		Integer startFetch = Integer.parseInt(start);

		ComboDataBizLogic comboDataBizObj = new ComboDataBizLogic();
		JSONObject jsonObject = comboDataBizObj.getClinicalDiagnosisData(limitFetch, startFetch,
				query);

		response.setContentType("text/javascript");
		PrintWriter out = response.getWriter();
		out.write(jsonObject.toString());
		return null;
	}
}