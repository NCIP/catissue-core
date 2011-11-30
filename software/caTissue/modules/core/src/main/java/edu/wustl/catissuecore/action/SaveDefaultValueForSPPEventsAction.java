/**
 *
 */

package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.owasp.stinger.http.MutableHttpRequest;

import edu.common.dynamicextensions.domain.integration.AbstractFormContext;
import edu.wustl.catissuecore.bizlogic.SPPBizLogic;
import edu.wustl.catissuecore.deintegration.DEIntegration;
import edu.wustl.catissuecore.domain.processingprocedure.Action;
import edu.wustl.catissuecore.processor.SPPEventProcessor;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;

/**
 * @author suhas_khot
 *
 */
public class SaveDefaultValueForSPPEventsAction extends SecureAction
{

	/**
	 * Overrides the execute method of Action class. Initializes the various
	 * fields in SpecimenEventParameters.jsp Page.
	 *
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 *
	 * @return value for ActionForward object
	 *
	 * @throws IOException I/O exception
	 * @throws ServletException servlet exception
	 */
	@Override
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException, Exception
	{
		getOperation(request);

		Set<AbstractFormContext> formContextCollection = new HashSet<AbstractFormContext>();
		Map<AbstractFormContext, Map<String, Object>> formContextParameterMap = new HashMap<AbstractFormContext, Map<String, Object>>();

		ActionForward actionForward = mapping.findForward(Constants.PAGE_OF_SPP);
		//Save SPP events
		if (Boolean.parseBoolean(request.getParameter(Constants.SAVE_SPP_EVENTS)))
		{
			SPPEventProcessor sppEventProcessor = new SPPEventProcessor();
			formContextParameterMap = sppEventProcessor.populateFormContextParmaterMap(request
					.getParameterMap());
			formContextCollection.addAll(formContextParameterMap.keySet());
			boolean isEditOperation = false;

			List<String> listOfError = validateDEData(request, formContextCollection,
					formContextParameterMap, sppEventProcessor);

			if (listOfError.isEmpty())
			{
				for (AbstractFormContext formContext : formContextCollection)
				{
					Action action = (Action) formContext;

					//Generate mock request object required for DE data entry
					MutableHttpRequest httprequest = getMockRequestObject(request,
							formContextParameterMap, formContext);

					Map<AbstractFormContext, Long> contextRecordIdMap = new SPPBizLogic()
							.generateContextRecordIdMap(formContext, action);

					Set<AbstractFormContext> contextCollection = new HashSet<AbstractFormContext>();
					contextCollection.add(formContext);

					Map<AbstractFormContext, Long> contextVsRecordIdMap = sppEventProcessor
							.insertUpdateDEDataForSPPEvents(httprequest, contextRecordIdMap,
									contextCollection);

					if (action.getApplicationDefaultValue() == null)
					{
						DEIntegration deIntegration = new DEIntegration();
						deIntegration.insertAndHookRecordEntry(formContext, action,
								contextVsRecordIdMap);
					}
					else
					{
						isEditOperation = true;
					}
				}
				String[] displayNameParams = {Constants.DEFAULT_VALUES, Constants.SPP_EVENTS_MSG};
				setSuccessMsg(request, displayNameParams, isEditOperation);
			}
			else
			{
				saveActionErrors(request, listOfError);
				actionForward = mapping.findForward("pageOfDisplaySPPDefaultValue");
			}
		}
		return actionForward;
	}

	/**
	 * @param request
	 * @param listOfError
	 */
	private void saveActionErrors(HttpServletRequest request, List<String> listOfError)
	{
		ActionErrors errors = new ActionErrors();
		for (String errorString : listOfError)
		{
			ActionError error = new ActionError("errors.item", errorString);
			errors.add(ActionErrors.GLOBAL_ERROR, error);
		}
		saveErrors(request, errors);
	}

	/**
	 * @param request
	 * @param formContextCollection
	 * @param formContextParameterMap
	 * @param sppEventProcessor
	 * @return
	 * @throws Exception
	 */
	private List<String> validateDEData(HttpServletRequest request,
			Set<AbstractFormContext> formContextCollection,
			Map<AbstractFormContext, Map<String, Object>> formContextParameterMap,
			SPPEventProcessor sppEventProcessor) throws Exception
	{
		MutableHttpRequest httpMockrequest = new MutableHttpRequest(request);
		httpMockrequest.getParameterMap().clear();
		for (AbstractFormContext formContext : formContextCollection)
		{
			//Generate mock request object required for DE data entry
			httpMockrequest.getParameterMap().putAll(formContextParameterMap.get(formContext));
		}
		//validate DE data
		List<String> listOfError = sppEventProcessor.validateDEData(httpMockrequest,
				formContextParameterMap);
		return listOfError;
	}

	/**
	 * @param request
	 * @param formContextParameterMap
	 * @param formContext
	 * @return
	 */
	private MutableHttpRequest getMockRequestObject(HttpServletRequest request,
			Map<AbstractFormContext, Map<String, Object>> formContextParameterMap,
			AbstractFormContext formContext)
	{
		Map<String, Object> parameterValueMap = formContextParameterMap.get(formContext);
		MutableHttpRequest httprequest = new MutableHttpRequest(request);
		httprequest.getParameterMap().clear();
		httprequest.getParameterMap().putAll(parameterValueMap);
		return httprequest;
	}

	/**
	 * Sets the success msg.
	 *
	 * @param request the request
	 * @param displayNameParams the display name params
	 * @param isEditOperation the is edit operation
	 */
	private void setSuccessMsg(HttpServletRequest request, String[] displayNameParams,
			boolean isEditOperation)
	{
		String key = "object.add.success";
		if (isEditOperation)
		{
			key = "object.edit.success";
		}
		ActionMessages messages = new ActionMessages();
		messages.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage(key, displayNameParams));
		saveMessages(request, messages);
	}

	/**
	 * @param request
	 */
	private void getOperation(HttpServletRequest request)
	{
		String operation = (String) request.getAttribute(Constants.OPERATION);
		if (operation == null)
		{
			operation = request.getParameter(Constants.OPERATION);
		}
		request.setAttribute(Constants.OPERATION, operation);
	}
}
