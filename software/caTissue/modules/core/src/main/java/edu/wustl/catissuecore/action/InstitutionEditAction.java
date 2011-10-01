package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.InstitutionForm;
import edu.wustl.catissuecore.ctrp.COPPAServiceClient;
import edu.wustl.catissuecore.ctrp.COPPAUtil;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAddEditAction;
import edu.wustl.common.action.CommonAddEditAction;
import edu.wustl.common.action.CommonEdtAction;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.coppa.po.Organization;

public class InstitutionEditAction extends SecureAction {
	private static final Logger logger = Logger
			.getCommonLogger(InstitutionEditAction.class);

	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		BaseAddEditAction addEditAction;
		ActionForward actionfwd = null;
		try {
			boolean showEditPageAgain = false;
			final InstitutionForm institutionForm = (InstitutionForm) form;
			final IFactory factory = AbstractFactoryConfig.getInstance()
					.getBizLogicFactory();
			final DefaultBizLogic bizLogic = (DefaultBizLogic) factory
					.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
			Institution institution = (Institution) bizLogic.retrieve(
					Institution.class.getName(), institutionForm.getId());

			String remoteOperation = request.getParameter("remoteOperation");

			// COPPA is enabled and user has not selected remote entity yet.
			// Check if Remote match exists
			System.out.println("Institution Edit Action:" + "form name:"
					+ institutionForm.getName() + ":form remote operation:"
					+ institutionForm.getRemoteOperation() + ":form remoteid:"
					+ institutionForm.getRemoteId()
					+ ":request param operation:"
					+ request.getParameter("remoteOperation"));

			if (COPPAUtil.isCOPPAEnabled()) {
				if (Constants.REMOTE_OPERATION_SEARCH_LINK
						.equalsIgnoreCase(remoteOperation)) {
					Organization[] organizationList = null;
					organizationList = new COPPAServiceClient()
							.searchOrganization(institutionForm.getName());
					if (organizationList != null && organizationList.length > 0) {
						request.setAttribute("COPPA_MATCH_FOUND", "true");
						request.getSession().setAttribute(
								"COPPA_ORGANIZATIONS", organizationList);
					} else {
						// end if org not found in coppa
						AppUtility.addErrorMessage(request,
								"ctrp.edit.remotelink.noentityfound");
					}
					showEditPageAgain = true;
					request.setAttribute("operationEdit", Constants.EDIT);
					request.setAttribute("formName",
							Constants.INSTITUTION_EDIT_ACTION);
					actionfwd = mapping.findForward("pageOfInstitution");
				} else if (Constants.REMOTE_OPERATION_SYNC
						.equalsIgnoreCase(remoteOperation)) {
					Organization remoteOrg = new COPPAServiceClient()
							.getOrganizationById(institution.getRemoteId()
									.toString());
					institutionForm.setName(remoteOrg.getName().getPart()
							.get(0).getValue());
					institutionForm.setDirtyEditFlag(false);
				}// end -if syncRemoteChanges
			}// end -if coppa enabled

			if (!showEditPageAgain) {
				// User linked a remote institution or do not want any link.
				// Submit to edit page and save changes to database.
				if (institutionForm.getRemoteId() != 0) {
					// Link to remote institution
					institutionForm.setRemoteManagedFlag(true);
					institutionForm.setDirtyEditFlag(false);
					// Get organization details by id
					Organization org = new COPPAServiceClient()
							.getOrganizationById(""
									+ institutionForm.getRemoteId());
					institutionForm.setName(org.getName().getPart().get(0)
							.getValue());
				}
				addEditAction = new CommonEdtAction();
				actionfwd = addEditAction.executeXSS(mapping, institutionForm,
						request, response);
			}
		} catch (ApplicationException applicationException) {
			logger.error("Institution Edit failed.."
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
