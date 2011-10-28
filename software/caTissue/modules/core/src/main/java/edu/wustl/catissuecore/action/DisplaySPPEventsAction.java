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
import edu.wustl.common.util.logger.Logger;

/**
 * @author suhas_khot
 *
 */
public class DisplaySPPEventsAction extends SecureAction
{

	/** Initialize logger. */
	private transient final Logger LOGGER = Logger.getCommonLogger(DisplaySPPEventsAction.class);

	/**
	 * Overrides the execute method of Action class. Initializes the various
	 * fields in SpecimenEventParameters.jsp Page.
	 *
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 *
	 * @return value for ActionForward object
	 *
	 * @throws IOException I/O exception
	 * @throws ServletException servlet exception
	 */
	@Override
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException, Exception
	{
		String pageOf = request.getParameter(Constants.PAGE_OF);
		request.setAttribute(Constants.PAGE_OF, pageOf);
		//SPPEventProcessor
		SPPEventProcessor sppEventProcessor = new SPPEventProcessor();
		//Retrieve specimen object based on specimen Id
		String specimenId = request.getParameter(Constants.SPECIMEN_ID);
		request.setAttribute(Constants.SPECIMEN_ID, specimenId);
		List<Map<String, Object>> sppEventDataCollection = sppEventProcessor
				.populateSPPEventsForASpecimen(specimenId);

		Map<String, Long> dynamicEventMap = new HashMap<String, Long>();
		new SPPBizLogic().getAllSPPEventFormNames(dynamicEventMap);
		if (request.getSession().getAttribute("dynamicEventMap") == null)
		{
			request.getSession().setAttribute("dynamicEventMap", dynamicEventMap);
		}

		//Add SPP events data collection in request scope
		request.setAttribute(Constants.SPP_EVENTS, sppEventDataCollection);

		SpecimenProcessingProcedure processingSPP = sppEventProcessor.getSPPBySpecimenId(Long.valueOf(specimenId));
		if (processingSPP != null)
		{
			request.setAttribute("nameOfSelectedSpp", processingSPP.getName());
			request.setAttribute("selectedSppId", processingSPP.getId());
		}

		return mapping.findForward(pageOf);
	}

}
