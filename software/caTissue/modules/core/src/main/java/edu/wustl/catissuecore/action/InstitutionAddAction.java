package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.actionForm.InstitutionForm;
import edu.wustl.catissuecore.ctrp.COPPAServiceClient;
import edu.wustl.catissuecore.ctrp.COPPAUtil;
import edu.wustl.catissuecore.ctrp.CTRPConstants;
import edu.wustl.catissuecore.ctrp.CTRPPropertyHandler;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAddEditAction;
import edu.wustl.common.action.CommonAddAction;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.coppa.po.Organization;

public class InstitutionAddAction extends SecureAction {

	private static final Logger logger = Logger
			.getCommonLogger(InstitutionAddAction.class);

	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		BaseAddEditAction addAction;
		ActionForward actionfwd;
		try {
			boolean coppaMatchFound = false;
			final InstitutionForm institutionForm = (InstitutionForm) form;
//			String remoteOperation = institutionForm.getRemoteOperation();
			String remoteOperation = request.getParameter("remoteOperation");

			// COPPA is enabled and user has not selected remote entity yet.
			// Check if Remote match exists
			System.out.println("Institution Add Action:" + "form name:"
					+ institutionForm.getName() + ":form remote operation:"
					+ institutionForm.getRemoteOperation()
					+ ":form remoteid:" + institutionForm.getRemoteId()
					+ ":form remote flag:" + institutionForm.isRemoteManagedFlag()
					+":request param operation:"+request.getParameter("remoteOperation"));
			if (COPPAUtil.isCOPPAEnabled()
					&& (Constants.REMOTE_OPERATION_SEARCH
							.equalsIgnoreCase(remoteOperation))) {
				try {
					Organization[] organizationList = null;
					organizationList = new COPPAServiceClient()
							.searchOrganization(institutionForm.getName());
					if (organizationList != null && organizationList.length > 0) {
						coppaMatchFound = true;
						request.setAttribute("COPPA_MATCH_FOUND", "true");
						request.getSession().setAttribute("COPPA_ORGANIZATIONS",
								organizationList);
					}
				} catch (Exception e) {
					logger.error("Failed to look for Organizations in COPPA.");
					logger.error(e,e);
				}

			}

			if (coppaMatchFound) {
				// Redirect user to pop-up for selecting remote institution
				request.setAttribute("operationAdd", Constants.ADD);
				request.setAttribute("formName",
						Constants.INSTITUTION_ADD_ACTION);
				actionfwd = mapping.findForward("pageOfInstitution");
			} else {
				// Either there is no COPPA match or user has already selected
				// COPPA match or user does not want to do remote link
				if (institutionForm.getRemoteId() != 0) {
					// Adding a new remote institution
					institutionForm.setRemoteManagedFlag(true);
					institutionForm.setDirtyEditFlag(false);
					// Get organization details by id
					Organization org = new COPPAServiceClient()
							.getOrganizationById(""
									+ institutionForm.getRemoteId());
					institutionForm.setName(org.getName().getPart().get(0)
							.getValue());
				}
				addAction = new CommonAddAction();
				logger.info("Insitution Add Action before adding to db:"
						+ "remote id:" + institutionForm.getRemoteId()
						+ "remote flag:"
						+ institutionForm.isRemoteManagedFlag()
						+ "dirty edit flag:"
						+ institutionForm.isDirtyEditFlag() + "name:"
						+ institutionForm.getName());
				actionfwd = addAction.executeXSS(mapping, institutionForm,
						request, response);
				// over write action succuess message for remote link success
				if (institutionForm.getRemoteId() != 0) {
					AppUtility.replaceActionMessage(request,
							"ctrp.instituion.link.success");
				}
			}// end -if adding user not remote pop-up

		} catch (ApplicationException applicationException) {
			logger.error("Institution Add failed.."
					+ applicationException.getCustomizedMsg());

			ActionErrors actionErrors = new ActionErrors();
			ActionError actionError = new ActionError("errors.item",
					applicationException.getCustomizedMsg());

			actionErrors.add("org.apache.struts.action.GLOBAL_ERROR",
					actionError);
			saveErrors(request, actionErrors);

			actionfwd = mapping.findForward("failure");
		}
		return actionfwd;

	}

}
