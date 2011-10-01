package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAddEditAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.IDomainObjectFactory;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.exception.DAOException;

public class ParticipantAddAuthorizeAction extends BaseAddEditAction
{
	private static final Logger LOGGER = Logger.getCommonLogger(ParticipantAddAuthorizeAction.class);

	public ActionForward executeXSS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws ApplicationException
	{
		String target = Constants.SUCCESS;
		DAO dao = null;
		try
		{
			AbstractActionForm abstractForm = (AbstractActionForm) form;
			AbstractDomainObject abstractDomain;
			IDomainObjectFactory abstractDomainObjectFactory = getIDomainObjectFactory();
			abstractDomain = abstractDomainObjectFactory.getDomainObject(abstractForm.getFormId(),
					abstractForm);
			SessionDataBean sessionDataBean = getSessionData(request);
			String appName = CommonServiceLocator.getInstance().getAppName();
			IDAOFactory daofactory = DAOConfigFactory.getInstance().getDAOFactory(appName);
			dao = daofactory.getDAO();
			dao.openSession(sessionDataBean);
			ParticipantBizLogic bizLogic = new ParticipantBizLogic();
			bizLogic.isAuthorized(dao, abstractDomain, sessionDataBean);
		}
		catch (ApplicationException applicationException)
		{
			ActionErrors actionErrors = new ActionErrors();
			ActionError actionError = new ActionError("access.addedit.object.denied",
					"Participant","Registration","Participant");
			actionErrors.add(ActionErrors.GLOBAL_ERROR, actionError);
			saveErrors(request, actionErrors);

			target = Constants.FAILURE;
		}
		finally
		{
			try
			{
				if(dao != null)
				{
					dao.closeSession();
				}
			}
			catch (DAOException exception)
			{
				LOGGER.error("Not able to close DAO session.", exception);
				throw new BizLogicException(exception);
			}
		}
		return mapping.findForward(target);
	}
}
