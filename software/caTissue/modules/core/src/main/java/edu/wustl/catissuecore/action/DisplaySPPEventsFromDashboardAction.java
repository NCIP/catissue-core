/**
 *
 */

package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.SPPBizLogic;
import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedure;
import edu.wustl.catissuecore.processor.SPPEventProcessor;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;

/**
 * @author suhas_khot
 *
 */
public class DisplaySPPEventsFromDashboardAction extends SecureAction
{
	/**
	 * Overrides the execute method of Action class. Initializes the various
	 * fields in SpecimenEventParameters.jsp Page.
	 *
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws IOException
	 *             I/O exception
	 * @throws ServletException
	 *             servlet exception
	 * @return value for ActionForward object
	 */
	@Override
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long sppId = Long.parseLong(request.getParameter("sppId"));
		edu.wustl.dao.DAO dao = null;
		dao = edu.wustl.catissuecore.util.global.AppUtility.openDAOSession(null);
		SpecimenProcessingProcedure processingSPP = (SpecimenProcessingProcedure) dao
				.retrieveById(SpecimenProcessingProcedure.class.getName(), sppId);; // TODO - Retrieve SPP object based on
		// spp_id from request.

		request.setAttribute("selectedAll", request.getParameter("selectedAll"));
		request.setAttribute("sppId", sppId);
		if (request.getParameter("specimenId") != null)
		{
			request.setAttribute("specimenId", request.getParameter("specimenId"));
		}
		else
		{
			request.setAttribute("scgId", request.getParameter("scgId"));
		}
		request.setAttribute("typeObject", request.getParameter("typeObject"));
		request.setAttribute("nameOfSelectedSpp", processingSPP.getName());
		request.setAttribute("selectedSppId", processingSPP.getId());
		List<Map<String, Object>> sppEventDataCollection = new SPPEventProcessor()
				.populateSPPEventsWithDefaultValue(processingSPP.getActionCollection(), true);
		request.setAttribute(Constants.SPP_EVENTS, sppEventDataCollection);
		request.setAttribute(Constants.DISPLAY_EVENTS_WITH_DEFAULT_VALUES, true);

		Map<String, Long> dynamicEventMap = new HashMap<String, Long>();
		new SPPBizLogic().getAllSPPEventFormNames(dynamicEventMap);
		if (request.getSession().getAttribute("dynamicEventMap") == null)
		{
			request.getSession().setAttribute("dynamicEventMap", dynamicEventMap);
		}
		return mapping.findForward("pageOfSppData");
	}

}
