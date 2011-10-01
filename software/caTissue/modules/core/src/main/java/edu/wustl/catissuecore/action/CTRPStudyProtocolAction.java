package edu.wustl.catissuecore.action;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CTRPEntityForm;
import edu.wustl.catissuecore.ctrp.COPPAServiceClient;
import edu.wustl.catissuecore.ctrp.COPPAUtil;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.coppa.po.Person;
import gov.nih.nci.coppa.services.pa.StudyProtocol;

public class CTRPStudyProtocolAction extends BaseAction {

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger
			.getCommonLogger(CTRPStudyProtocolAction.class);

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
		CTRPEntityForm ctrpEntityForm = (CTRPEntityForm) form;
		COPPAServiceClient client = new COPPAServiceClient();

		String title = request.getParameter("entityName");
		if (AppUtility.isNotEmpty(ctrpEntityForm.getEntityName())) {
			// Use email address from search form
			title = ctrpEntityForm.getEntityName();
		}
		COPPAServiceClient coppaClient = new COPPAServiceClient();
		CollectionProtocol searchCollectionProtocol = new CollectionProtocol();
		StudyProtocol[] protocolList = null;
		// searchUser.setEmailAddress(ctrpEntityForm.getEntityName());
		searchCollectionProtocol.setTitle(title);
		protocolList = coppaClient
				.searchSutdyProtocol(searchCollectionProtocol);
		if (AppUtility.isNotEmpty(protocolList)) {
			request.setAttribute("COPPA_MATCH_FOUND", "true");
			request.setAttribute("COPPA_STUDY_PROTOCOLS", protocolList);
		}

		return mapping.findForward("displayMatches");
	}
}
