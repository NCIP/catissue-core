
package edu.wustl.catissuecore.action.bulkOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.BulkEventOperationsForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.bulkOperations.BulkOperationsBizlogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.action.CommonAddEditAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.logger.Logger;

public class BulkOperationSubmitAction extends BaseAction
{

	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		
		
		// Get Specimen Ids for which bulk events should be added
		BulkEventOperationsForm bulkEventOperationsForm = (BulkEventOperationsForm) form;
		String target = bulkEventOperationsForm.getOperation();
		if(target == null)
		{
			target = Constants.SUCCESS;
		}
		
		List<String> specimenIds = new ArrayList<String>(bulkEventOperationsForm.getSpecimenIds().keySet());
		request.setAttribute(Constants.SPECIMEN_ID, specimenIds);
		
		try
		{
			if (specimenIds != null && specimenIds.size() > 0)
			{
				//Insert bulk events
				BulkOperationsBizlogic bizlogic = (BulkOperationsBizlogic) BizLogicFactory.getInstance().getBizLogic(Constants.BULK_OPERATIONS_FORM_ID);
				bizlogic.insertEvents(bulkEventOperationsForm.getOperation(), getSessionData(request), specimenIds, bulkEventOperationsForm.getUserId(), bulkEventOperationsForm
								.getDateOfEvent(), bulkEventOperationsForm.getTimeInHours(),
						bulkEventOperationsForm.getTimeInMinutes(), bulkEventOperationsForm.getComments(), bulkEventOperationsForm.getEventSpecificData());
				
				ActionErrors errors = new ActionErrors();
				ActionError error = null;
				if (specimenIds != null && specimenIds.size() > 0)
				{
					if(bulkEventOperationsForm.getOperation().equals(Constants.BULK_TRANSFERS))
					{
						error = new ActionError("bulk.operations.success", "transfer ");
					}
					else
					{
						error = new ActionError("bulk.operations.success", "disposal ");
					}
				}
				else
				{
					error = new ActionError("specimen.cart.size.zero");
				}
				errors.add(ActionErrors.GLOBAL_ERROR, error);
				saveErrors(request, errors);
			}
		}
		
		catch (UserNotAuthorizedException ex)
		{
			SessionDataBean sessionDataBean = getSessionData(request);
	        String userName = "";
            if(sessionDataBean != null)
        	{
        	    userName = sessionDataBean.getUserName();
        	}
			UserNotAuthorizedException excp = (UserNotAuthorizedException) ex;
			String className = new CommonAddEditAction().getActualClassName(SpecimenEventParameters.class.getName());
			String decoratedPrivilegeName = Utility.getDisplayLabelForUnderscore(excp.getPrivilegeName());
			String baseObject = "";
            if (excp.getBaseObject() != null && excp.getBaseObject().trim().length() != 0)
            {
                baseObject = excp.getBaseObject();
            } 
            else 
            {
                baseObject = className;
            }
              
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("access.addedit.object.denied", userName, className,decoratedPrivilegeName,baseObject);
			errors.add(ActionErrors.GLOBAL_ERROR, error);
			saveErrors(request, errors);
		}
		catch (BizLogicException excp)
		{
			ActionErrors errors = new ActionErrors();
			ActionError error = new ActionError("errors.item", excp.getMessage());
			errors.add(ActionErrors.GLOBAL_ERROR, error);
			saveErrors(request, errors);
			
			Logger.out.error(excp.getMessage(), excp);
		}

		return mapping.findForward(target);
	}

}
