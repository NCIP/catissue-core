
package edu.wustl.catissuecore.action;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.catissuecore.actionForm.DynamicEventForm;
import edu.wustl.catissuecore.bizlogic.CatissueDefaultBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.processingprocedure.Action;
import edu.wustl.catissuecore.domain.processingprocedure.ActionApplication;
import edu.wustl.catissuecore.domain.processingprocedure.DefaultAction;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.SpecimenEventsUtility;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;

public class DynamicEventAction extends BaseAction
{

	/**
	 * This method sets all the common parameters for the Dynamic Event pages.
	 *
	 * @param request
	 *            HttpServletRequest instance in which the data will be set.
	 * @throws Exception
	 *             Throws Exception. Helps in handling exceptions at one common
	 *             point.
	 */
	private void setCommonRequestParameters(HttpServletRequest request) throws Exception
	{
		// Gets the value of the operation parameter.

		final String operation = request.getParameter(Constants.OPERATION);
		// Sets the operation attribute to be used in the Add/Edit
		// FrozenEventParameters Page.
		request.setAttribute(Constants.OPERATION, operation);

		// Sets the minutesList attribute to be used in the Add/Edit
		// FrozenEventParameters Page.
		request.setAttribute("minutesList", Constants.MINUTES_ARRAY);

		// Sets the hourList attribute to be used in the Add/Edit
		// FrozenEventParameters Page.
		request.setAttribute("hourList", Constants.HOUR_ARRAY);

		// The id of specimen of this event.
		final String specimenId = request.getParameter(Constants.SPECIMEN_ID);
		request.setAttribute(Constants.SPECIMEN_ID, specimenId);

		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final UserBizLogic userBizLogic = (UserBizLogic) factory
				.getBizLogic(Constants.USER_FORM_ID);
		final Collection userCollection = userBizLogic.getUsers(operation);

		request.setAttribute(Constants.USERLIST, userCollection);

	}

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response : HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		if (request.getParameter(Constants.REFRESH_EVENT_GRID) != null)
		{
			request.setAttribute(Constants.REFRESH_EVENT_GRID, request
					.getParameter(Constants.REFRESH_EVENT_GRID));
		}
		//this.setCommonRequestParameters(request);
		DynamicEventForm dynamicEventForm = (DynamicEventForm) form;
		resetFormParameters(request, dynamicEventForm);

		// if operation is add
		if (dynamicEventForm.isAddOperation())
		{
			if (dynamicEventForm.getUserId() == 0)
			{
				final SessionDataBean sessionData = this.getSessionData(request);
				if (sessionData != null && sessionData.getUserId() != null)
				{
					final long userId = sessionData.getUserId().longValue();
					dynamicEventForm.setUserId(userId);
				}
			}

			// set the current Date and Time for the event.
			final Calendar cal = Calendar.getInstance();
			if (dynamicEventForm.getDateOfEvent() == null)
			{
				dynamicEventForm.setDateOfEvent(CommonUtilities.parseDateToString(cal.getTime(),
						CommonServiceLocator.getInstance().getDatePattern()));
			}
			if (dynamicEventForm.getTimeInHours() == null)
			{
				dynamicEventForm.setTimeInHours(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)));
			}
			if (dynamicEventForm.getTimeInMinutes() == null)
			{
				dynamicEventForm.setTimeInMinutes(Integer.toString(cal.get(Calendar.MINUTE)));
			}
		}
		else
		{
			String specimenId = (String) request.getAttribute(Constants.SPECIMEN_ID);
			if (specimenId == null)
			{
				final DynamicEventForm sopF = ((DynamicEventForm) dynamicEventForm);
				request.setAttribute(Constants.SPECIMEN_ID, specimenId);
			}
		}

		String reasonDeviation = "";
		reasonDeviation = dynamicEventForm.getReasonDeviation();
		if (reasonDeviation == null)
		{
			reasonDeviation = "";
		}

		String currentEventParametersDate = "";
		currentEventParametersDate = dynamicEventForm.getDateOfEvent();
		if (currentEventParametersDate == null)
		{
			currentEventParametersDate = "";
		}

		final Integer dynamicEventParametersYear = new Integer(AppUtility
				.getYear(currentEventParametersDate));
		final Integer dynamicEventParametersMonth = new Integer(AppUtility
				.getMonth(currentEventParametersDate));
		final Integer dynamicEventParametersDay = new Integer(AppUtility
				.getDay(currentEventParametersDate));
		request.setAttribute("minutesList", Constants.MINUTES_ARRAY);

		// Sets the hourList attribute to be used in the Add/Edit
		// FrozenEventParameters Page.
		request.setAttribute("hourList", Constants.HOUR_ARRAY);
		request.setAttribute("dynamicEventParametersYear", dynamicEventParametersYear);
		request.setAttribute("dynamicEventParametersDay", dynamicEventParametersDay);
		request.setAttribute("dynamicEventParametersMonth", dynamicEventParametersMonth);
		request.setAttribute("formName", Constants.DYNAMIC_EVENT_ACTION);
		request.setAttribute("addForJSP", Constants.ADD);
		request.setAttribute("editForJSP", Constants.EDIT);
		request.setAttribute("reasonDeviation", reasonDeviation);
		request.setAttribute("userListforJSP", Constants.USERLIST);
		request.setAttribute("currentEventParametersDate", currentEventParametersDate);

		request.setAttribute(Constants.PAGE_OF, "pageOfDynamicEvent");
		request.setAttribute(Constants.SPECIMEN_ID, request.getSession().getAttribute(
				Constants.SPECIMEN_ID));
		//request.setAttribute(Constants.SPECIMEN_ID, request.getAttribute(Constants.SPECIMEN_ID));

		//request.setAttribute(Constants.PAGE_OF, request.getParameter(Constants.PAGE_OF));
		request.setAttribute("changeAction", Constants.CELL_SPECIMEN_REVIEW_PARAMETERS_ADD_ACTION);
		final String operation = request.getParameter(Constants.OPERATION);
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final UserBizLogic userBizLogic = (UserBizLogic) factory
				.getBizLogic(Constants.USER_FORM_ID);
		final Collection userCollection = userBizLogic.getUsers(operation);
		request.setAttribute(Constants.USERLIST, userCollection);
		HashMap dynamicEventMap = (HashMap) request.getSession().getAttribute("dynamicEventMap");

		String iframeURL="";
		long recordIdentifier=0;
		Long eventId=null;
		if(dynamicEventForm.getOperation().equals(Constants.ADD))
		{
			String eventName = request.getParameter("eventName");
			if(eventName == null)
			{
				eventName = (String)request.getSession().getAttribute("eventName");
			}
			request.getSession().setAttribute("eventName", eventName);
			dynamicEventForm.setEventName(eventName);

			eventId=(Long) dynamicEventMap.get(eventName);
			if (Boolean.parseBoolean(request.getParameter("showDefaultValues")))
			{
				IBizLogic defaultBizLogic = new CatissueDefaultBizLogic();
				List<Action> actionList = defaultBizLogic.retrieve(Action.class.getName(),
						Constants.ID, request.getParameter("formContextId"));
				if (actionList != null && !actionList.isEmpty())
				{
					Action action = (Action) actionList.get(0);
					if (action.getApplicationDefaultValue() != null)
					{
						recordIdentifier = SpecimenEventsUtility.getRecordIdentifier(action
								.getApplicationDefaultValue().getId(), action.getContainerId());
					}
				}
			}
			else
			{
				IBizLogic defaultBizLogic = new CatissueDefaultBizLogic();
				List<DefaultAction> actionList = defaultBizLogic.retrieve(DefaultAction.class
						.getName(), Constants.CONTAINER_ID, eventId);
				DefaultAction action =null;
				if (actionList != null && !actionList.isEmpty())
				{
					action = (DefaultAction) actionList.get(0);

				}
				else
				{
					action=new DefaultAction();
					action.setContainerId(eventId);
					defaultBizLogic.insert(action);
				}
				request.setAttribute("formContextId",action.getId());
			}
			if(eventName != null)
			{
				request.setAttribute("formDisplayName", Utility.getFormattedString(eventName));
			}
			if(eventId != null)
			{
				request.getSession().setAttribute("OverrideCaption", "_" + eventId.toString());
				iframeURL = "/catissuecore/LoadDataEntryFormAction.do?dataEntryOperation=insertParentData&useApplicationStylesheet=true&showInDiv=false&overrideCSS=true&overrideScroll=true"+(Boolean.parseBoolean(request.getParameter(Constants.CONTAINS_ERROR))?"&containerId=":"&containerIdentifier=")
						+ eventId.toString() + "&OverrideCaption=" + "_" + eventId.toString();
				if (recordIdentifier != 0)
				{
					iframeURL = iframeURL + "&recordIdentifier=" + recordIdentifier;
				}
			}
		}
		else
		{
			String actionApplicationId = request.getParameter("id");
			if(actionApplicationId ==null)
			{
				actionApplicationId = (String) request.getAttribute("id");
			}
			if(Boolean.parseBoolean(request.getParameter(Constants.CONTAINS_ERROR))
					&& request.getSession().getAttribute("dynamicEventsForm") != null)
			{
				dynamicEventForm = (DynamicEventForm) request.getSession().getAttribute("dynamicEventsForm");
				actionApplicationId = String.valueOf(dynamicEventForm.getId());
			}
			recordIdentifier = SpecimenEventsUtility.getRecordIdentifier(dynamicEventForm
					.getRecordEntry(), dynamicEventForm.getContId());

			Long containerId = dynamicEventForm.getContId();
			IBizLogic defaultBizLogic = new CatissueDefaultBizLogic();

			String specimenId = (String) request.getAttribute(Constants.SPECIMEN_ID);
			boolean isDefaultAction = true;
			if(specimenId!=null && !"".equals(specimenId))
			{
				List<Specimen> specimentList = defaultBizLogic.retrieve(
						Specimen.class.getName(), Constants.ID, specimenId);
				if (specimentList != null && !specimentList.isEmpty())
				{
					Specimen specimen = (Specimen) specimentList.get(0);
					if(specimen.getProcessingSPPApplication() != null)
					{
						for(ActionApplication actionApplication : specimen.getProcessingSPPApplication().getSppActionApplicationCollection())
						{
							if(actionApplication.getId().toString().equals(actionApplicationId))
							{
								for(Action action : specimen.getSpecimenRequirement().getProcessingSPP().getActionCollection())
								{
									if(action.getContainerId().equals(containerId))
									{
										request.setAttribute("formContextId", action.getId());
										break;
									}
								}
								isDefaultAction = false;
								break;
							}
						}
					}
				}
			}
			if(isDefaultAction)
			{
				List<DefaultAction> actionList = defaultBizLogic.retrieve(
						DefaultAction.class.getName(), Constants.CONTAINER_ID, dynamicEventForm
								.getContId());
				if (actionList != null && !actionList.isEmpty())
				{
					DefaultAction action = (DefaultAction) actionList.get(0);
					request.setAttribute("formContextId", action.getId());
				}
			}

			dynamicEventForm.setRecordIdentifier(recordIdentifier);
			iframeURL = "/catissuecore/LoadDataEntryFormAction.do?dataEntryOperation=edit&useApplicationStylesheet=true&showInDiv=false"+(Boolean.parseBoolean(request.getParameter(Constants.CONTAINS_ERROR))?"&containerId=":"&containerIdentifier=")
					+ dynamicEventForm.getContId()
					+ "&recordIdentifier="
					+ recordIdentifier
					+ "&OverrideCaption=" + "_" + dynamicEventForm.getContId();
			List contList = AppUtility
					.executeSQLQuery("select caption from dyextn_container where identifier="
							+ dynamicEventForm.getContId());
			String eventName = (String) ((List) contList.get(0)).get(0);
			request.setAttribute("formDisplayName", Utility.getFormattedString(eventName));
		}
		if (request.getSession().getAttribute("specimenId") == null)
		{
			request.getSession().setAttribute("specimenId", request.getParameter("specimenId"));
		}

		request.setAttribute("recordIdentifier", recordIdentifier);
		request.getSession().setAttribute("recordIdentifier", recordIdentifier);
		request.getSession().setAttribute("containerId", dynamicEventForm.getContId());

		request.getSession().setAttribute("mandatory_Message", "false");
		String formContxtId = getFormContextFromRequest(request);
		if(!"".equals(iframeURL))
		{
			iframeURL = iframeURL + "&FormContextIdentifier=" + formContxtId;
		}
		populateStaticAttributes(request, dynamicEventForm);
		request.setAttribute("iframeURL", iframeURL);
		return mapping.findForward((String) request.getAttribute(Constants.PAGE_OF));
	}

	/**
	 * @param request
	 * @param dynamicEventForm
	 */
	private void resetFormParameters(HttpServletRequest request, DynamicEventForm dynamicEventForm)
	{
		if("edit".equals(request.getParameter(Constants.OPERATION))  && request.getSession().getAttribute(Constants.DYN_EVENT_FORM)!= null)
		{
			DynamicEventForm originalForm =  (DynamicEventForm) request.getSession().getAttribute(Constants.DYN_EVENT_FORM);
			dynamicEventForm.setContId(originalForm.getContId());
			dynamicEventForm.setReasonDeviation(originalForm.getReasonDeviation());
			dynamicEventForm.setRecordEntry(originalForm.getRecordEntry());
			dynamicEventForm.setRecordIdentifier(originalForm.getRecordIdentifier());
			dynamicEventForm.setUserId(originalForm.getUserId());
			dynamicEventForm.setTimeInHours(originalForm.getTimeInHours());
			dynamicEventForm.setTimeInMinutes(originalForm.getTimeInMinutes());
			dynamicEventForm.setId(originalForm.getId());
		}
	}

	/**
	 * @param request
	 * @param dynamicEventForm
	 */
	private void populateStaticAttributes(HttpServletRequest request,
			final DynamicEventForm dynamicEventForm)
	{
		String sessionMapName = "formContextParameterMap"+ getFormContextFromRequest(request);
		Map<String, Object> formContextParameterMap = (Map<String, Object>) request.getSession().getAttribute(sessionMapName);
		if(formContextParameterMap !=null && Boolean.parseBoolean(request.getParameter(Constants.CONTAINS_ERROR)))
		{
			setDateParameters(request, formContextParameterMap);
			dynamicEventForm.setTimeInMinutes((String) formContextParameterMap.get("timeInMinutes"));
			dynamicEventForm.setTimeInHours((String) formContextParameterMap.get("timeInHours"));
			dynamicEventForm.setUserId(Long.valueOf((String)formContextParameterMap.get(Constants.USER_ID)));
			dynamicEventForm.setDateOfEvent((String) formContextParameterMap.get(Constants.DATE_OF_EVENT));
			dynamicEventForm.setReasonDeviation((String) formContextParameterMap.get(Constants.REASON_DEVIATION));
			request.getSession().removeAttribute(sessionMapName);
			request.getSession().removeAttribute(Constants.DYN_EVENT_FORM);
		}
	}

	/**
	 * @param request
	 * @param formContextParameterMap
	 */
	private void setDateParameters(HttpServletRequest request,
			Map<String, Object> formContextParameterMap)
	{
		if(formContextParameterMap.get(Constants.DATE_OF_EVENT)!= null)
		{
			String currentEventParametersDate = (String) formContextParameterMap.get(Constants.DATE_OF_EVENT);
			request.setAttribute("currentEventParametersDate", currentEventParametersDate);
			request.setAttribute("dynamicEventParametersYear", AppUtility
					.getYear(currentEventParametersDate));
			request.setAttribute("dynamicEventParametersDay", AppUtility
					.getDay(currentEventParametersDate));
			request.setAttribute("dynamicEventParametersMonth", AppUtility
					.getMonth(currentEventParametersDate));
		}
	}

	/**
	 * @param request
	 * @return
	 */
	private String getFormContextFromRequest(HttpServletRequest request)
	{
		String formContxtId = request.getParameter("formContextId");
		if(formContxtId == null)
		{
			formContxtId = String.valueOf(request.getAttribute("formContextId"));
		}
		return formContxtId;
	}
}
