
package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.SPPBizLogic;
import edu.wustl.catissuecore.domain.processingprocedure.Action;
import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedure;
import edu.wustl.catissuecore.processingprocedure.SPPActionComparator;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;

public class DisplayDefaultValuesForSPPEventsAction extends SecureAction
{

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
			HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException, Exception
	{
		Long sppIdentifier = getSPPIdentifier(request);

		//Display SPP events
		if (sppIdentifier != null)
		{
			SPPBizLogic sppBizLogic = new SPPBizLogic();
			SpecimenProcessingProcedure spp = sppBizLogic.getSPPById(sppIdentifier);
			Map<Action, Long> contextRecordIdMap = sppBizLogic.generateContextRecordIdMap(spp);
			request.setAttribute(Constants.CONTEXT_RECORD_MAP, contextRecordIdMap);
			TreeSet<Action> actionList = new TreeSet<Action>(new SPPActionComparator());
			actionList.addAll(spp.getActionCollection());
			request.setAttribute("actionColl", actionList);
			return mapping.findForward(Constants.PAGE_OF_DEF_VALUE_FOR_SPP);
		}
		return mapping.findForward(Constants.PAGE_OF_SPP);

	}

	/**
	 * @param request
	 * @return
	 */
	private Long getSPPIdentifier(HttpServletRequest request)
	{
		Long sppIdentifier = (Long) request.getAttribute(Constants.ID);
		if (sppIdentifier == null)
		{
			sppIdentifier = Long.valueOf(request.getParameter(Constants.ID));
		}
		request.setAttribute(Constants.ID, sppIdentifier);
		return sppIdentifier;
	}

}
