package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.GSID.GSIDBatchUpdate;
import edu.wustl.catissuecore.GSID.GSIDServiceStatusNotifier;
import edu.wustl.catissuecore.actionForm.GSIDBatchUpdateForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.logging.api.util.StringUtils;

public class GSIDBatchUpdateAction extends SecureAction {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getCommonLogger(GSIDBatchUpdateAction.class);
	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		GSIDBatchUpdateForm gsidForm=(GSIDBatchUpdateForm)form;
		SessionDataBean sessionData = (SessionDataBean) request.getSession().getAttribute(
				Constants.SESSION_DATA);		
		request.setAttribute("GSIDBatchUpdateForm",gsidForm);
		if(sessionData.isAdmin())
		{
			//check if lock exists
			if(!GSIDBatchUpdate.isLock())
			{
				if(gsidForm.isForce())
				{
					if (!new GSIDServiceStatusNotifier().checkServiceStatus()) {
						ActionErrors actionErrors = new ActionErrors();
						ActionError actionError = new ActionError("errors.item",
								"GSID service is down. Please make another attempt once the service is back up.");
						actionErrors.add(ActionErrors.GLOBAL_ERROR, actionError);
						saveErrors(request, actionErrors);
						return mapping.findForward("failure");

					}
					new GSIDBatchUpdate(sessionData.getUserName());					
					gsidForm.setUnassignedSpecimenCount(GSIDBatchUpdate.getUnassignedSpecimenCount()-GSIDBatchUpdate.getProcessedSpecimenCount());
				}
				else
				{
					gsidForm.setUnassignedSpecimenCount(GSIDBatchUpdate.getUnassignedSpecimenCount(sessionData));
				}
			}
			else
			{
				gsidForm.setUnassignedSpecimenCount(GSIDBatchUpdate.getUnassignedSpecimenCount()-GSIDBatchUpdate.getProcessedSpecimenCount());
			}
			gsidForm.setCurrentUser(GSIDBatchUpdate.getCurrentUser());
			gsidForm.setPercentage((int)GSIDBatchUpdate.getPercentage());
			gsidForm.setLastProcessedLabel(GSIDBatchUpdate.getLastProcessedLabel());
			gsidForm.setLocked(GSIDBatchUpdate.isLock());
			gsidForm.setLastException(GSIDBatchUpdate.getLastException());			
			
			return mapping.findForward(Constants.SUCCESS);
		}
		else
		{
			return mapping.findForward(Constants.ACCESS_DENIED);
		}
		
		
	}

}
