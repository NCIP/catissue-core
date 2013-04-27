/**
 *  * <p>
 * Title: BulkEventsAddAction Class>
 * <p>
 * Description: BulkEventsAddAction handle Add SpecimenEvents on Single and Bulk Specimens
 * page.
 * </p>
 * @author Md Nazmul Hassan
 * @version 1.00
 */
 
package edu.wustl.catissuecore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.actionForm.SpecimenEventParametersForm;
import edu.wustl.catissuecore.bizlogic.SpecimenEventParametersBizLogic;
import edu.wustl.catissuecore.domain.DomainObjectFactory;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IDomainObjectFactory;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.Status;

/**
 * class definition.
 * Description: BulkEventsAddAction handle Add SpecimenEvents on Single and Bulk Specimens
 */
public class BulkEventsAddAction extends BaseAction{

	/**
	 * Overrides the execute method of Action class.
	 *
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 *
	 * @return the action forward
	 *
	 * @throws Exception generic exception
	 */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute(Constants.CP_QUERY, request.getParameter(Constants.CP_QUERY));
		// capture Specimen Identifier From UI that may be comma separated and Single
		String specimenIdFromUi="";
		AbstractActionForm aActionForm=null;
		Long createdObjId=null;
		
		AbstractDomainObject abstractDomain;
		SpecimenEventParameters specimenEventObject=null;
		
		final SessionDataBean sessionData = (SessionDataBean) request.getSession()
				.getAttribute(Constants.SESSION_DATA);
		
		SpecimenEventParametersBizLogic speEventbizLogic=new SpecimenEventParametersBizLogic();
		
		aActionForm=(AbstractActionForm) form;
		
		IDomainObjectFactory abstractDomainObjectFactory = new DomainObjectFactory();
		abstractDomain = abstractDomainObjectFactory.getDomainObject(aActionForm.getFormId(),
				aActionForm);
		
		specimenEventObject= (SpecimenEventParameters) abstractDomain;
		specimenIdFromUi=((SpecimenEventParametersForm) form).getSpecimenId();
		if(!specimenIdFromUi.contains(","))
		{
			// validate specimen is closed / Disabled
			if(this.validateSingleSpecimen(specimenIdFromUi))
			{
				createdObjId=speEventbizLogic.insertBulkSpecimenEvents(specimenIdFromUi, specimenEventObject,sessionData);
			}
			else
			{
				final ActionErrors errors = new ActionErrors();
				final ActionError error = new ActionError("single.specimen.invalid");
				errors.add(ActionErrors.GLOBAL_ERROR, error);
				this.saveErrors(request, errors);
				final String pageOf = Constants.FAILURE;
				return mapping.findForward(pageOf);
			}
		}
		else
		{
			createdObjId=speEventbizLogic.insertBulkSpecimenEvents(specimenIdFromUi, specimenEventObject,sessionData);
		}
		
		// If Single Specimen Case then Set newly created event identifier for populating event list in specimen page
		
		if(! specimenIdFromUi.contains(","))
		{
			request.setAttribute(Constants.SYSTEM_IDENTIFIER, createdObjId);
			request.setAttribute("isQuickEvent", "false");
		}	
		
		ActionMessages messages = new ActionMessages();
		messages.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("bulk.specimen.events.success"));
		saveMessages(request, messages);
		final String pageOf = Constants.SUCCESS;

		return mapping.findForward(pageOf);
		
	}
	/**
	 *
	 * @param specimenId : specimenId
	 * @return boolean : boolean
	 * @throws Exception : Exception
	 */
	private boolean validateSingleSpecimen(String specimenId) throws BizLogicException
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		final String sourceObjectName = Specimen.class.getName();
		final String[] selectColumnName = {Constants.SYSTEM_IDENTIFIER};
		final String[] whereColumnName = {Constants.SYSTEM_IDENTIFIER, Status.ACTIVITY_STATUS.toString(), Status.ACTIVITY_STATUS.toString()};
		final String[] whereColumnCondition = {"=", "!=", "!="};
		final Object[] whereColumnValue = {Long.valueOf(specimenId), Status.ACTIVITY_STATUS_DISABLED.toString(), Status.ACTIVITY_STATUS_CLOSED.toString()};
		final String joinCondition = Constants.AND_JOIN_CONDITION;

		final List list = bizLogic.retrieve(sourceObjectName, selectColumnName, whereColumnName,
				whereColumnCondition, whereColumnValue, joinCondition);
		if (!list.isEmpty() && list.size() !=0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}
