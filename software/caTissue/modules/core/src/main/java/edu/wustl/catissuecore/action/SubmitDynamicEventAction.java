
package edu.wustl.catissuecore.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.exception.DynamicExtensionsCacheException;
import edu.wustl.catissuecore.actionForm.DynamicEventForm;
import edu.wustl.catissuecore.bizlogic.CatissueDefaultBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.deintegration.ActionApplicationRecordEntry;
import edu.wustl.catissuecore.domain.processingprocedure.ActionApplication;
import edu.wustl.catissuecore.domain.processingprocedure.DefaultAction;
import edu.wustl.catissuecore.upgrade.IntegrateDEData;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.security.exception.SMException;

public class SubmitDynamicEventAction extends BaseAction
{

	/** logger. */
	private static final Logger LOGGER = Logger.getCommonLogger(SubmitDynamicEventAction.class);

	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		try
		{
			String bulkSpecimenDataEntry = (String) request.getSession().getAttribute(
					"bulkSpecimenDataEntry");
			if (request.getSession().getAttribute("specimenIdList") != null
					&& !((List) request.getSession().getAttribute("specimenIdList")).isEmpty())
			{
				String url = "/catissuecore/DynamicEventAdd.do";
				performOperation(request);
				ArrayList<String> specIdList = (ArrayList) request.getSession().getAttribute(
						"specimenIdList");
				specIdList.remove(0);
				if (!specIdList.isEmpty())
				{
					response.sendRedirect(url);
				}
				else
				{
					request.getSession().removeAttribute("deurl");
					request.getSession().removeAttribute("specimenIdList");
					request.getSession().removeAttribute("dynamicEventFormData");
					request.getSession().removeAttribute("bulkSpecimenDataEntry");
				}

				return null;
			}
			else if (bulkSpecimenDataEntry == null || !"yes".equals(bulkSpecimenDataEntry))
			{
				Specimen specimen = performOperation(request);
				request.setAttribute(Constants.SPECIMEN_ID, specimen.getId());
				request.setAttribute(Constants.PAGE_OF,
						Constants.PAGE_OF_LIST_SPECIMEN_EVENT_PARAMETERS_CP_QUERY);
				return mapping.findForward("loaddynamicevents");
			}
			return null;

		}
		catch (Exception e)
		{
			request.getSession().removeAttribute("deurl");
			request.getSession().removeAttribute("specimenIdList");
			request.getSession().removeAttribute("dynamicEventFormData");
			request.getSession().removeAttribute("bulkSpecimenDataEntry");
			throw new Exception(e);
		}

	}

	@SuppressWarnings("unchecked")
	private Specimen performOperation(HttpServletRequest request) throws BizLogicException,
			SMException, ApplicationException, DynamicExtensionsCacheException, NumberFormatException
	{
		final DynamicEventForm dynamicEventForm = (DynamicEventForm) request.getSession()
				.getAttribute("dynamicEventFormData");
		dynamicEventForm.setFormId(Integer.valueOf(request.getParameter("recordIdentifier")));

		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.USER_FORM_ID);

		String sourceObjectName = User.class.getName();
		final String displayNameFields = Constants.ID;
		final long valueField = dynamicEventForm.getUserId();
		final List<User> userList = bizLogic.retrieve(sourceObjectName, displayNameFields,
				valueField);

		User user = userList.get(0);
		final IBizLogic actionAppBizLogic = factory.getBizLogic(Constants.ACTION_APP_FORM_ID);

		ActionApplication actionApplication = null;
		ActionApplicationRecordEntry actionAppRecordEntry = null;
		Date dateOfEvent = null;
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm");
		try
		{
			dateOfEvent = formatter
					.parse(dynamicEventForm.getDateOfEvent() + " "
							+ dynamicEventForm.getTimeInHours() + ":"
							+ dynamicEventForm.getTimeInMinutes());
		}
		catch (ParseException e)
		{
			LOGGER.error("Invalid Date Parser Exception: " + e.getMessage(), e);
		}

		if (dynamicEventForm.getOperation().equalsIgnoreCase(Constants.EDIT))
		{
			final String whereColumnName = Constants.SYSTEM_IDENTIFIER;
			final String whereColumnValue = String.valueOf(dynamicEventForm.getId());
			final String srcObjectName = ActionApplication.class.getName();
			final List<ActionApplication> ActionApplicationRecordEntryCollection = actionAppBizLogic
					.retrieve(srcObjectName, whereColumnName, whereColumnValue);
			actionApplication = ActionApplicationRecordEntryCollection.get(0);
			actionAppRecordEntry = actionApplication.getApplicationRecordEntry();
			actionApplication.setReasonDeviation(dynamicEventForm.getReasonDeviation());
			actionApplication.setTimestamp(dateOfEvent);
		}
		else
		{
			DefaultAction defaultAction = null;
			actionAppRecordEntry = new ActionApplicationRecordEntry();

			final IBizLogic defaultBizLogic = new CatissueDefaultBizLogic();
			List<DefaultAction> defaultActionList = defaultBizLogic.retrieve(DefaultAction.class
					.getName(), "containerId", String.valueOf(dynamicEventForm.getContId()));
			if (defaultActionList != null && !defaultActionList.isEmpty())
			{
				defaultAction = defaultActionList.get(0);
				defaultAction.setContainerId(dynamicEventForm.getContId());
			}
			if (defaultAction == null)
			{
				defaultAction = new DefaultAction();
				defaultAction.setContainerId(dynamicEventForm.getContId());
				defaultBizLogic.insert(defaultAction);
			}
			actionAppRecordEntry.setFormContext(defaultAction);
			actionAppRecordEntry.setActivityStatus("Active");

			actionApplication = new ActionApplication();
			actionApplication.setReasonDeviation(dynamicEventForm.getReasonDeviation());
			actionApplication.setTimestamp(dateOfEvent);
			actionAppBizLogic.insert(actionApplication);
			actionAppBizLogic.insert(actionAppRecordEntry);
		}
		final IBizLogic specBizLogic = factory.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
		final List specimenList = specBizLogic.retrieve(Specimen.class.getName(),
				displayNameFields, Long.valueOf(request.getParameter("specimenId")));
		Specimen specimen = (Specimen) specimenList.get(0);
		specimen.setId(Long.valueOf(request.getParameter("specimenId")));

		actionApplication.setSpecimen(specimen);
		actionApplication.setPerformedBy(user);
		actionApplication.setApplicationRecordEntry(actionAppRecordEntry);

		actionApplication.setApplicationRecordEntry(actionAppRecordEntry);
		actionAppBizLogic.update(actionApplication);

		IntegrateDEData integrateDEData = new IntegrateDEData();
		integrateDEData.associateRecords(dynamicEventForm.getContId(),
				actionAppRecordEntry.getId(), Long
						.valueOf(request.getParameter("recordIdentifier")));

		return specimen;
	}

}
