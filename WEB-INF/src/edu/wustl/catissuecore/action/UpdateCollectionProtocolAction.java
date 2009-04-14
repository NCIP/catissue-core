package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.dto.CollectionProtocolDTO;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.security.exception.UserNotAuthorizedException;
;

public class UpdateCollectionProtocolAction extends BaseAction {


	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String target = Constants.SUCCESS;

		try{ 
			CollectionProtocol collectionProtocol = CollectionProtocolUtil
			.populateCollectionProtocolObjects(request);

			CollectionProtocolBean collectionProtocolBean = 
				(CollectionProtocolBean)(request.getSession())
				.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
			IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			IBizLogic bizLogic =factory.getBizLogic(Constants.COLLECTION_PROTOCOL_FORM_ID);
			HttpSession session = request.getSession();
			SessionDataBean sessionDataBean = (SessionDataBean)
							session.getAttribute(Constants.SESSION_DATA);
			CollectionProtocolDTO collectionProtocolDTO = AppUtility.getCoolectionProtocolDTO(collectionProtocol,session);
			bizLogic.update(collectionProtocolDTO, null, 
					0, sessionDataBean);
			CollectionProtocolUtil.updateSession(request, collectionProtocol.getId());
			if(Constants.DISABLED.equals(collectionProtocolBean.getActivityStatus()))
			{
				target = "disabled";
			}
			else
			{	
				target = Constants.SUCCESS;
			}
			ActionMessages actionMessages = new ActionMessages();
			actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"object.edit.successOnly","Collection Protocol"));
			saveMessages(request, actionMessages);
			
		}
		catch (Exception exception)
		{
			ActionErrors actionErrors = new ActionErrors();
			
			if(exception instanceof UserNotAuthorizedException)
			{
	            UserNotAuthorizedException ex = (UserNotAuthorizedException) exception;
				SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute(Constants.SESSION_DATA);
	            String userName = "";
	        	
	            if(sessionDataBean != null)
	        	{
	        	    userName = sessionDataBean.getUserName();
	        	}
	            String className = Constants.COLLECTION_PROTOCOL;
	            String decoratedPrivilegeName = AppUtility.getDisplayLabelForUnderscore(ex.getPrivilegeName());
	            String baseObject = "";
	            if (ex.getBaseObject() != null && ex.getBaseObject().trim().length() != 0)
	            {
	                baseObject = ex.getBaseObject();
	            } else 
	            {
	                baseObject = className;
	            }
	                
	            ActionError error = new ActionError("access.addedit.object.denied", userName, className,decoratedPrivilegeName,baseObject);
	            actionErrors.add(ActionErrors.GLOBAL_ERROR, error);
	        	saveErrors(request, actionErrors);
	        	target = Constants.FAILURE;
	        	return (mapping.findForward(target));
			}
			
			actionErrors.add(actionErrors.GLOBAL_MESSAGE, new ActionError(
					"errors.item",exception.getMessage()));
			saveErrors(request, actionErrors);						
			target = Constants.FAILURE;
		}
		return mapping.findForward(target);
	}
}
