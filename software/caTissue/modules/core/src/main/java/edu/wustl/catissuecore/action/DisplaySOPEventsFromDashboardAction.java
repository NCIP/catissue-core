/**
 *
 */

package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.SOPBizLogic;
import edu.wustl.catissuecore.domain.sop.Action;
import edu.wustl.catissuecore.domain.sop.ActionApplication;
import edu.wustl.catissuecore.domain.sop.SOP;
import edu.wustl.catissuecore.processor.SPPEventProcessor;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.logger.Logger;

/**
 * @author suhas_khot
 *
 */
public class DisplaySOPEventsFromDashboardAction extends SecureAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger
			.getCommonLogger(ListSpecimenEventParametersAction.class);

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
		Long sopId = Long.parseLong(request.getParameter("sopId"));
		edu.wustl.dao.DAO dao = null;
		List<Map<String, Object>> gridData = new ArrayList<Map<String, Object>>();
		dao = edu.wustl.catissuecore.util.global.AppUtility.openDAOSession(null);
		SOP processingSOP = (SOP) dao.retrieveById(SOP.class.getName(), sopId);; // TODO - Retrieve SOP object based on
		// sop_id from request.
		Collection<Action> sopActionCollection = processingSOP.getActionCollection();
		request.setAttribute("selectedAll", request.getParameter("selectedAll"));
		request.setAttribute("sppId", sopId);
		SPPEventProcessor sppEventProcessor = new SPPEventProcessor();
		if (request.getParameter("specimenId") != null)
		{
			request.setAttribute("specimenId", request.getParameter("specimenId"));
		}
		else
		{
			request.setAttribute("scgId", request.getParameter("scgId"));
		}
		request.setAttribute("nameOfSelectedSop", processingSOP.getName());
		request.setAttribute("selectedSopId", processingSOP.getId());
		List<Map<String, Object>> sppEventDataCollection = sppEventProcessor
				.populateSPPEventsData(processingSOP);

		Map<String, Long> dynamicEventMap = new HashMap<String, Long>();
		new SOPBizLogic().getAllSOPEventFormNames(dynamicEventMap);
		if (request.getSession().getAttribute("dynamicEventMap") == null)
		{
			request.getSession().setAttribute("dynamicEventMap", dynamicEventMap);
		}
		request.setAttribute(Constants.SPP_EVENTS, sppEventDataCollection);
		return mapping.findForward("pageOfSopData");
	}

	/**
	 * Process sop application.
	 *
	 * @param bizLogic the biz logic
	 * @param sopActionApplicationCollection the sop action application collection
	 *
	 * @return the list< map< string, object>>
	 *
	 * @throws ApplicationException the application exception
	 */
	private List<Map<String, Object>> processSOPApplication(final IBizLogic bizLogic,
			final Collection<ActionApplication> sopActionApplicationCollection)
			throws ApplicationException
	{
		final List<Map<String, Object>> gridData = new ArrayList<Map<String, Object>>();

		for (final ActionApplication actionApp : sopActionApplicationCollection)
		{
			Map<String, Object> rowDataMap = new HashMap<String, Object>();
			if (actionApp != null)
			{
				long containerId = actionApp.getApplicationRecordEntry().getFormContext()
						.getContainerId();
				List contList = AppUtility
						.executeSQLQuery("select caption from dyextn_container where identifier="
								+ containerId);
				String container = (String) ((List) contList.get(0)).get(0);
				//	rowDataMap.put(Constants.ID, String.valueOf(actionApp.getId()));
				rowDataMap.put(Constants.FORM_CONTEXT_ID, String.valueOf(actionApp
						.getApplicationRecordEntry().getFormContext().getId()));
				rowDataMap.put(Constants.CONTAINER_IDENTIFIER, containerId);
				//rowDataMap.put(Constants.EVENT_DATE, actionApp.getTimestamp());
				rowDataMap.put(Constants.PAGE_OF, "pageOfDynamicEvent");
				rowDataMap.put("Caption", edu.wustl.cab2b.common.util.Utility
						.getFormattedString(container));
				gridData.add(rowDataMap);
			}
		}
		return gridData;
	}

}
