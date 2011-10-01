
package edu.wustl.catissuecore.action;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.common.dynamicextensions.domain.integration.AbstractFormContext;
import edu.wustl.catissuecore.bizlogic.ActionApplicationBizLogic;
import edu.wustl.catissuecore.bizlogic.CatissueDefaultBizLogic;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.processor.SPPEventProcessor;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.dao.DAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;

public class DynamicEventAddAction extends BaseAction
{

	/** The container parameter list. */
	private Map<AbstractFormContext, Map<String, Object>> formContextParameterMap = new HashMap<AbstractFormContext, Map<String, Object>>();

	/** The form context collection. */
	private Set<AbstractFormContext> formContextCollection = new HashSet<AbstractFormContext>();

	/** The default biz logic. */
	private final IBizLogic defaultBizLogic = new CatissueDefaultBizLogic();

	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ActionForward actionForward = null;
		DAO dao = null;
		//Fetch Specimen
		String specimenId = request.getParameter(Constants.SPECIMEN_ID);
		String[] specimenIdArr = {specimenId};
		if (specimenId.contains(","))
		{
			specimenIdArr = specimenId.split(",");
		}
		try
		{
			NewSpecimenBizLogic newSpecimenBizLogic = new NewSpecimenBizLogic();
			final String displayNameFields = Constants.ID;
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final IBizLogic specBizLogic = factory.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
			List specimenList;
			for (int cnt = 0; cnt < specimenIdArr.length; cnt++)
			{
				specimenList = specBizLogic.retrieve(Specimen.class.getName(), displayNameFields,
						Long.valueOf(specimenIdArr[cnt]));
				Specimen toAuthspecimen = (Specimen) specimenList.get(0);

				IDAOFactory daofactory = DAOConfigFactory.getInstance().getDAOFactory(
						newSpecimenBizLogic.getAppName());
				dao = daofactory.getDAO();
				dao.openSession(null);
				newSpecimenBizLogic.isAuthorized(dao, toAuthspecimen, (SessionDataBean) request
						.getSession().getAttribute(Constants.SESSION_DATA));
			}
		}
		catch (Exception e)
		{
			final ActionErrors errors = new ActionErrors();
			ActionError error = new ActionError("access.addedit.object.denied",
					"specimen event parameters", "Specimen Processing", "Collection Protocol");
			errors.add(ActionErrors.GLOBAL_ERROR, error);
			saveErrors(request, errors);
			actionForward = mapping.findForward(Constants.PAGE_OF_DYNAMIC_EVENT);
			return actionForward;
		}
		finally
		{
			if (dao != null)
			{
				dao.closeSession();
			}
		}
		try
		{
			final SessionDataBean sessionLoginInfo = this.getSessionData(request);

			//initialize biz logic
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final IBizLogic actionAppBizLogic = (ActionApplicationBizLogic) factory
					.getBizLogic(Constants.ACTION_APP_FORM_ID);

			SPPEventProcessor sppEventProcessor = new SPPEventProcessor();
			//parse request parameter map
			formContextParameterMap = sppEventProcessor
					.populateFormContextParmaterMapForAdhocEvent(request.getParameterMap());
			formContextCollection.clear();
			formContextCollection.addAll(formContextParameterMap.keySet());

			request.setAttribute(Constants.SPECIMEN_ID, specimenId);

			//validate DE data
			List<String> listOfError = sppEventProcessor.validateDEData(request, formContextCollection);
			if (listOfError.isEmpty())
			{
				for (int cnt = 0; cnt < specimenIdArr.length; cnt++)
				{
					final Specimen specimen = (Specimen) defaultBizLogic.retrieve(Specimen.class
							.getName(), new Long(specimenIdArr[cnt]));
					String recordIdentifier = (String) request.getParameter("recordIdentifier");

					if (recordIdentifier == null || "0".equalsIgnoreCase(recordIdentifier))
					{
						Map<AbstractFormContext, Long> contextVsRecordIdMap = sppEventProcessor
								.insertUpdateDEDataForSOPEvents(request,
										new HashMap<AbstractFormContext, Long>(),
										formContextCollection);

						sppEventProcessor.insertAction(actionAppBizLogic, specimen,
								contextVsRecordIdMap, sessionLoginInfo, formContextParameterMap,
								formContextCollection);
					}
					else
					{
						//Edit case
						Map<AbstractFormContext, Long> contextRecordIdMap = sppEventProcessor
								.editActionApplicationForAdhocEvents(actionAppBizLogic, Long
										.valueOf(recordIdentifier), formContextParameterMap,
										formContextCollection);
						//For each from Context collection update DE data
						sppEventProcessor.insertUpdateDEDataForSOPEvents(request,
								contextRecordIdMap, formContextCollection);
					}
				}

				//Add success message
				String containerCaption = sppEventProcessor
						.getContainerCaption(formContextCollection.iterator().next()
								.getContainerId());
				addSuccessMessage(request, containerCaption);
			}
			else
			{
				saveActionErrors(request, listOfError);
				for(AbstractFormContext formContext : formContextParameterMap.keySet())
				{
					request.getSession().setAttribute("formContextParameterMap"+formContext.getId(), formContextParameterMap.get(formContext));
				}
				if("edit".equals(request.getParameter(Constants.OPERATION)))
				{
					request.getSession().setAttribute(Constants.DYN_EVENT_FORM, form);
				}
			}

			//refresh Grid on specimen events parameter page
			if (Boolean.parseBoolean(request.getParameter(Constants.REFRESH_EVENT_GRID)))
			{
				actionForward = mapping.findForward(Constants.PAGE_OF_SPECIMEN_EVENTS);
			}
			else
			{
				actionForward = mapping.findForward(Constants.SUCCESS);
			}
			return actionForward;
		}
		catch (Exception e)
		{
			request.getSession().removeAttribute("deurl");
			request.getSession().removeAttribute("specimenIdList");
			request.getSession().removeAttribute("dynamicEventFormData");
			throw new Exception(e);
		}
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

		request.getSession().setAttribute(Constants.SPECIMEN_ID,
				request.getParameter(Constants.SPECIMEN_ID));
		request.getSession().setAttribute(Constants.SPECIMEN_EVENT_PARAMETER,
				request.getParameter(Constants.SPECIMEN_EVENT_PARAMETER));
		request.getSession().setAttribute(Constants.EVENT_NAME,
				request.getParameter(Constants.SPECIMEN_EVENT_PARAMETER));
		request.getSession().setAttribute(Constants.SPECIMEN_LABLE,
				request.getParameter(Constants.SPECIMEN_LABLE));
	}

	/**
	 * @param request
	 * @param containerCaption
	 */
	private void addSuccessMessage(HttpServletRequest request, String containerCaption)
	{
		ActionMessages messages = new ActionMessages();
		String[] displayNameParams = {edu.wustl.cab2b.common.util.Utility
				.getFormattedString(containerCaption)};
		messages.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("event.add.successOnly",
				displayNameParams));
		saveMessages(request, messages);
	}

}
