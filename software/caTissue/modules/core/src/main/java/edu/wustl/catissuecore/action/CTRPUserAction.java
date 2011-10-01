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
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.coppa.po.Person;

public class CTRPUserAction extends BaseAction {

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger
			.getCommonLogger(CTRPUserAction.class);

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

		Enumeration<String> names;
		names = request.getAttributeNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			System.out.println("RequestAttribute-" + name + ":"
					+ request.getAttribute(name));
		}
		names = request.getHeaderNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			System.out.println("RequestHeader-" + name + ":"
					+ request.getAttribute(name));
		}
		names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			System.out.println("RequestParam-" + name + ":"
					+ request.getAttribute(name));
		}

		String emailAddress = request.getParameter("emailAddress");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		System.out.println("Form Variables:" + ctrpEntityForm.getEmailAddress()
				+ ":" + ctrpEntityForm.getFirstName() + ":"
				+ ctrpEntityForm.getLastName());
		if (AppUtility.isNotEmpty(ctrpEntityForm.getEmailAddress())) {
			//Use email address from search form
			emailAddress = ctrpEntityForm.getEmailAddress();
		}
		User searchUser = new User();
		// searchUser.setEmailAddress(ctrpEntityForm.getEntityName());
		searchUser.setEmailAddress(emailAddress);
		searchUser.setFirstName(firstName);
		searchUser.setLastName(lastName);
		Person[] userList = null;
		userList = new COPPAServiceClient().searchPerson(searchUser);
		if (userList != null && userList.length > 0) {
			request.setAttribute("COPPA_MATCH_FOUND", "true");
			request.setAttribute("COPPA_USERS", userList);
		}

		return mapping.findForward("displayMatches");
	}
}
