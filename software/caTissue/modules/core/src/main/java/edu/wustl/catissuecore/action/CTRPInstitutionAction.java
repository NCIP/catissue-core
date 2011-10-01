package edu.wustl.catissuecore.action;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CTRPInstitutionForm;
import edu.wustl.catissuecore.ctrp.COPPAServiceClient;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.coppa.po.Organization;
import edu.wustl.catissuecore.util.global.Constants;

public class CTRPInstitutionAction extends BaseAction {

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger
			.getCommonLogger(CTRPInstitutionAction.class);

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * 
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            : response obj
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CTRPInstitutionForm ctrpInstitutionForm = (CTRPInstitutionForm) form;
		System.out.println("Request Param:"+request.getParameter("operation"));
		Organization[] organizationList = null;
		organizationList = new COPPAServiceClient()
				.searchOrganization(ctrpInstitutionForm.getEntityName());
		if (organizationList != null && organizationList.length > 0) {
			request.setAttribute("COPPA_MATCH_FOUND", "true");
			request.getSession().setAttribute("COPPA_ORGANIZATIONS",
					organizationList);
		}

		return mapping.findForward("displayMatches");
	}
}
