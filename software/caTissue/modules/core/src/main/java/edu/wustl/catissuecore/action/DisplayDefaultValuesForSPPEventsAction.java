
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

import edu.wustl.catissuecore.bizlogic.SOPBizLogic;
import edu.wustl.catissuecore.domain.sop.Action;
import edu.wustl.catissuecore.domain.sop.SOP;
import edu.wustl.catissuecore.sop.SOPActionComparator;
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
		Long sopIdentifier = getSOPIdentifier(request);

		//Display SOP events
		if (sopIdentifier != null)
		{
			SOPBizLogic sopBizLogic = new SOPBizLogic();
			SOP sop = sopBizLogic.getSOPById(sopIdentifier);
			Map<Action, Long> contextRecordIdMap = sopBizLogic.generateContextRecordIdMap(sop);
			request.setAttribute(Constants.CONTEXT_RECORD_MAP, contextRecordIdMap);
			TreeSet<Action> actionList = new TreeSet<Action>(new SOPActionComparator());
			actionList.addAll(sop.getActionCollection());
			request.setAttribute("actionColl", actionList);
			return mapping.findForward(Constants.PAGE_OF_DEF_VALUE_FOR_SOP);
		}
		return mapping.findForward(Constants.PAGE_OF_SOP);

	}

	/**
	 * @param request
	 * @return
	 */
	private Long getSOPIdentifier(HttpServletRequest request)
	{
		Long sopIdentifier = (Long) request.getAttribute(Constants.ID);
		if (sopIdentifier == null)
		{
			sopIdentifier = Long.valueOf(request.getParameter(Constants.ID));
		}
		request.setAttribute(Constants.ID, sopIdentifier);
		return sopIdentifier;
	}

}
