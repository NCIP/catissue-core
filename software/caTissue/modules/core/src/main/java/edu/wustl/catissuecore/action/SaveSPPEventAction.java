/**
 *
 */

package edu.wustl.catissuecore.action;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.common.dynamicextensions.domain.integration.AbstractFormContext;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.actionForm.DisplaySPPEventForm;
import edu.wustl.catissuecore.bizlogic.ActionApplicationBizLogic;
import edu.wustl.catissuecore.bizlogic.CatissueDefaultBizLogic;
import edu.wustl.catissuecore.domain.ISPPBizlogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.processingprocedure.Action;
import edu.wustl.catissuecore.domain.processingprocedure.ActionApplication;
import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedureApplication;
import edu.wustl.catissuecore.processor.SPPEventProcessor;
import edu.wustl.catissuecore.uiobject.SpecimenCollectionGroupWrapper;
import edu.wustl.catissuecore.uiobject.SpecimenWrapper;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;

/**
 * @author suhas_khot
 *
 */
public class SaveSPPEventAction extends SecureAction
{

	/** The container parameter list. */
	private Map<AbstractFormContext, Map<String, Object>> formContextParameterMap = new HashMap<AbstractFormContext, Map<String, Object>>();

	/** The form context collection. */
	private Set<AbstractFormContext> formContextCollection = new HashSet<AbstractFormContext>();

	/** The default biz logic. */
	private final IBizLogic defaultBizLogic = new CatissueDefaultBizLogic();

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
	@SuppressWarnings("unchecked")
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException, Exception
	{
		String pageOf = "";
		final SessionDataBean sessionLoginInfo = this.getSessionData(request);

		SPPEventProcessor sppEventProcessor = new SPPEventProcessor();
		DisplaySPPEventForm displaySPPEventForm = (DisplaySPPEventForm) form;

		//parse request parameter map
		formContextParameterMap = sppEventProcessor.populateFormContextParmaterMap(request
				.getParameterMap());

		updateSPPActionForSkippedEvent();

		formContextCollection.clear();
		formContextCollection.addAll(formContextParameterMap.keySet());

		//check is SPP processing has to be done for SCG
		boolean isSCG = isSpecimenCollectionGroupObject(request);

		//validate DE data
		List<String> listOfError = sppEventProcessor.validateDEData(request, formContextCollection);
		//If errorList is empty
		if (listOfError.isEmpty())
		{
			boolean isAuthorized = true;

			//This method will returns SCG or Specimen Id depending on condition isSCG,
			//IF isSCG is true then SCGId will be returned else SpecimenId will be returned.
			String domainObjectId = getDomainObjectId(request, isSCG);

			//generate domain object Id array
			String[] domainObjectIdArr = sppEventProcessor.getSpecimenOrSCGIdArray(request,
					domainObjectId, isSCG);

			String sppName = displaySPPEventForm.getSppName();

			for (int cnt = 0; cnt < domainObjectIdArr.length; cnt++)
			{
				ISPPBizlogic sppBizlogicObject = getISPPWrapperObject(isSCG, displaySPPEventForm,
						domainObjectIdArr[cnt]);
				try
				{
					//Check authorization
					if (sppEventProcessor.isAuthorized(sppBizlogicObject.getWrapperObject(),
							sessionLoginInfo))
					{
						//Save SPP events
						performSaveAction(request, sessionLoginInfo, sppEventProcessor, isSCG,
								sppName, sppBizlogicObject);
					}
				}
				catch (BizLogicException e)
				{
					//TODO need to handle exception properly,
					//since if anything goes wrong then only authorization error will be thrown,
					//so proper error message is not conveyed to user.

					ActionErrors errors = new ActionErrors();
					ActionError error = new ActionError("access.addedit.object.denied", "events",
							"SPP data entry", "this specimen or specimen collection group");
					errors.add(ActionErrors.GLOBAL_ERROR, error);
					saveErrors(request, errors);
					isAuthorized = false;
					break;
				}
			}
			pageOf = getPageOf(request, isSCG);
			saveActionMessage(request, isAuthorized);
		}
		//If errorList is not empty, then save action errors and forward to input.
		else
		{
			saveActionErrors(request, listOfError);
			for (AbstractFormContext formContext : formContextParameterMap.keySet())
			{
				request.getSession().setAttribute("formContextParameterMap" + formContext.getId(),
						formContextParameterMap.get(formContext));
			}
			pageOf = getPageOf(request, isSCG);
		}
		if ("pageOfUtilizeSppOfScg".equals(request.getParameter(Constants.PAGE_OF))
				|| "pageOfUtilizeSppofSpecimen".equals(request.getParameter(Constants.PAGE_OF)))
		{
			request.setAttribute("sppValue", request.getParameter("sppId"));
			if (request.getAttribute(Globals.ERROR_KEY) != null)
			{
				return getActionForward(request, isSCG);
			}
			return mapping.findForward(request.getParameter(Constants.PAGE_OF));
		}
		return mapping.findForward(pageOf);
	}

	/**
	 * @throws BizLogicException
	 */
	private void updateSPPActionForSkippedEvent() throws BizLogicException
	{
		Iterator<AbstractFormContext> formContextIterator = formContextParameterMap.keySet().iterator();
		while (formContextIterator.hasNext())
		{
			AbstractFormContext formContext = formContextIterator.next();
			Map<String, Object> staticParameters = formContextParameterMap.get(formContext);

			if(staticParameters.get("isSkipEvent")==null || Boolean.parseBoolean((String) staticParameters.get("isSkipEvent")))
			{
				((Action)formContext).setIsSkipped(Boolean.FALSE);
				defaultBizLogic.update(formContext);
			}
			else
			{
				((Action)formContext).setIsSkipped(Boolean.TRUE);
				defaultBizLogic.update(formContext);
				formContextIterator.remove();
			}
		}
	}

	/**
	 * Perform save action.
	 *
	 * @param request the request
	 * @param sessionLoginInfo the session login info
	 * @param actionAppBizLogic the action app biz logic
	 * @param sppEventProcessor the spp event processor
	 * @param isSCG the is scg
	 * @param sppName the spp name
	 * @param sppBizlogicObject the spp bizlogic object
	 *
	 * @throws FileNotFoundException the file not found exception
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws DynamicExtensionsApplicationException the dynamic extensions application exception
	 * @throws SQLException the SQL exception
	 * @throws BizLogicException the biz logic exception
	 * @throws ApplicationException the application exception
	 */
	private void performSaveAction(HttpServletRequest request,
			final SessionDataBean sessionLoginInfo, SPPEventProcessor sppEventProcessor,
			boolean isSCG, String sppName, ISPPBizlogic sppBizlogicObject)
			throws FileNotFoundException, DynamicExtensionsSystemException, IOException,
			DynamicExtensionsApplicationException, SQLException, BizLogicException,
			ApplicationException
	{
		//initialize resources
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic actionAppBizLogic = (ActionApplicationBizLogic) factory
				.getBizLogic(Constants.ACTION_APP_FORM_ID);

		Collection<SpecimenProcessingProcedureApplication> sppAppCollection = sppBizlogicObject
				.getSPPApplicationCollection();

		//Case 1: Insert data for SPP events.
		//In add case no processing SPPApplication will be present.
		if (sppAppCollection == null || sppAppCollection.isEmpty())
		{
			insertSPPData(request, sessionLoginInfo, actionAppBizLogic, sppEventProcessor, sppName,
					sppBizlogicObject);
		}
		else
		//Case 2: Update data for SPP events.
		//if specimen or scg has SPPApplication collection then data entry has already occurred.
		{
			boolean sppDataEntryDone = false;
			//For SCG multiple SPP are hooked, but for Specimen single SPP is hooked.
			Iterator<SpecimenProcessingProcedureApplication> sppAppIter = sppAppCollection.iterator();
			while (sppAppIter.hasNext())
			{
				SpecimenProcessingProcedureApplication sppApplication = sppAppIter.next();
				if ((isSCG && sppApplication.getSpp().getName().equals(sppName)) || (!isSCG))
				{
					sppDataEntryDone = updateSPPData(request, sppEventProcessor, actionAppBizLogic,
							sppApplication);
					break;
				}
			}
			if (!sppDataEntryDone)
			{
				insertSPPData(request, sessionLoginInfo, actionAppBizLogic, sppEventProcessor,
						sppName, sppBizlogicObject);
			}
		}
	}

	/**
	 * Update spp data.
	 *
	 * @param request the request
	 * @param sppEventProcessor the spp event processor
	 * @param actionAppBizLogic the action app biz logic
	 * @param sppApplication the spp application
	 *
	 * @return true, if update spp data
	 *
	 * @throws BizLogicException the biz logic exception
	 * @throws ApplicationException the application exception
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 * @throws DynamicExtensionsApplicationException the dynamic extensions application exception
	 * @throws SQLException the SQL exception
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean updateSPPData(HttpServletRequest request, SPPEventProcessor sppEventProcessor,
			final IBizLogic actionAppBizLogic, SpecimenProcessingProcedureApplication sppApplication)
			throws BizLogicException, ApplicationException, DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException, SQLException, FileNotFoundException, IOException
	{
		//retrieves ActionApplication collection for a given SPPApplication
		Collection<ActionApplication> actionApplicationCollection = sppApplication
				.getSppActionApplicationCollection();
		//Edit case
		Map<AbstractFormContext, Long> contextRecordIdMap = sppEventProcessor
				.editActionApplicationCollection(actionAppBizLogic,
						actionApplicationCollection, formContextParameterMap,
						formContextCollection);
		//For each from Context collection update DE data
		sppEventProcessor.insertUpdateDEDataForSPPEvents(request, contextRecordIdMap,
				formContextCollection);
		return true;
	}

	/**
	 * Checks if is specimen collection group object.
	 *
	 * @param request the request
	 *
	 * @return true, if checks if is specimen collection group object
	 */
	private boolean isSpecimenCollectionGroupObject(HttpServletRequest request)
	{
		//get scgId from request
		String scgId = getSCGId(request);
		return (scgId != null && !"".equals(scgId));
	}

	/**
	 * Insert spp data.
	 *
	 * @param request the request
	 * @param sessionLoginInfo the session login info
	 * @param actionAppBizLogic the action app biz logic
	 * @param sppEventProcessor the spp event processor
	 * @param sppName the spp name
	 * @param sppBizlogicObject the spp bizlogic object
	 *
	 * @throws FileNotFoundException the file not found exception
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws DynamicExtensionsApplicationException the dynamic extensions application exception
	 * @throws SQLException the SQL exception
	 * @throws BizLogicException the biz logic exception
	 * @throws ApplicationException the application exception
	 */
	private void insertSPPData(HttpServletRequest request, final SessionDataBean sessionLoginInfo,
			final IBizLogic actionAppBizLogic, SPPEventProcessor sppEventProcessor, String sppName,
			ISPPBizlogic sppBizlogicObject) throws FileNotFoundException,
			DynamicExtensionsSystemException, IOException, DynamicExtensionsApplicationException,
			SQLException, BizLogicException, ApplicationException
	{
		//Insert data for SPP events.
		Map<AbstractFormContext, Long> contextVsRecordIdMap = sppEventProcessor
				.insertUpdateDEDataForSPPEvents(request, new HashMap<AbstractFormContext, Long>(),
						formContextCollection);
		sppEventProcessor.insertSPPApplication(actionAppBizLogic, sppBizlogicObject,
				contextVsRecordIdMap, sessionLoginInfo, formContextParameterMap,
				formContextCollection, sppName);
	}

	/**
	 * Save action message.
	 *
	 * @param request the request
	 * @param isAuthorized the is authorized
	 */
	private void saveActionMessage(HttpServletRequest request, boolean isAuthorized)
	{
		if (isAuthorized)
		{
			ActionMessages messages = new ActionMessages();
			messages
					.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("sppEvent.add.successOnly"));
			saveMessages(request, messages);
		}
	}

	/**
	 * Gets the action forward.
	 *
	 * @param request the request
	 * @param isSCG the is scg
	 *
	 * @return the action forward
	 */
	private ActionForward getActionForward(HttpServletRequest request, boolean isSCG)
	{
		String url = "/DisplaySPPEventsFromDashboardAction.do?pageOf=pageOfDynamicEvent&selectedAll="
				+ request.getParameter("selectedAll") + "&sppId=" + request.getParameter("sppId");
		if (isSCG)
		{
			url = url + "&scgId=" + getSCGId(request);
		}
		else
		{
			url = url + "&specimenId=" + request.getParameter(Constants.SPECIMEN_ID);
		}
		ActionForward actionForward = new ActionForward();
		actionForward.setName("redirectToDashBoard");
		actionForward.setRedirect(false);
		actionForward.setPath(url);
		return actionForward;
	}

	/**
	 * Gets the scg id.
	 *
	 * @param request the request
	 *
	 * @return the SCG id
	 */
	private String getSCGId(HttpServletRequest request)
	{
		String scgId = request.getParameter("id");
		if (scgId == null)
		{
			scgId = request.getParameter(Constants.SCGID);
		}
		return scgId;
	}

	/**
	 * Save action errors.
	 *
	 * @param request the request
	 * @param listOfError the list of error
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
	 * Gets the ispp wrapper object.
	 *
	 * @param isSCG the is scg
	 * @param displaySPPEventForm the display spp event form
	 * @param domainObjectId the domain object id
	 *
	 * @return the ISPP wrapper object
	 *
	 * @throws BizLogicException the biz logic exception
	 */
	private ISPPBizlogic getISPPWrapperObject(boolean isSCG,
			DisplaySPPEventForm displaySPPEventForm, String domainObjectId)
			throws BizLogicException
	{
		ISPPBizlogic sppBizlogicObject;
		if (isSCG)
		{
			displaySPPEventForm.setScgId(domainObjectId);
			//Fetch Specimen Collection Group
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) defaultBizLogic.retrieve(
					SpecimenCollectionGroup.class.getName(), Long.valueOf(domainObjectId));
			//create specimen collection group wrapper object
			sppBizlogicObject = new SpecimenCollectionGroupWrapper();
			sppBizlogicObject.setWrapperObject(scg);
		}
		else
		{
			//Fetch Specimen object
			Specimen specimen = (Specimen) defaultBizLogic.retrieve(Specimen.class.getName(), Long
					.valueOf(domainObjectId));
			//create specimen collection group wrapper object
			sppBizlogicObject = new SpecimenWrapper();
			sppBizlogicObject.setWrapperObject(specimen);
		}
		return sppBizlogicObject;
	}

	/**
	 * Gets the domain object id.
	 *
	 * @param request the request
	 * @param isSCG the is scg
	 *
	 * @return the domain object id
	 */
	private String getDomainObjectId(HttpServletRequest request, boolean isSCG)
	{
		String scgId = getSCGId(request);
		String domainObjectId;
		if (isSCG)
		{
			request.setAttribute("id", scgId);
			request.setAttribute(Constants.SCGID, scgId);
			domainObjectId = scgId;
		}
		else
		{
			String specimenId = request.getParameter(Constants.SPECIMEN_ID);
			request.setAttribute(Constants.SPECIMEN_ID, specimenId);
			domainObjectId = specimenId;
		}
		return domainObjectId;
	}

	/**
	 * Gets the page of.
	 *
	 * @param request the request
	 * @param isSCG the is scg
	 *
	 * @return the page of
	 */
	private String getPageOf(HttpServletRequest request, boolean isSCG)
	{
		String pageOf;
		if (isSCG)
		{
			pageOf = "pageOfSCG";
			request.setAttribute(Constants.PAGE_OF, pageOf);
		}
		else
		{
			pageOf = "pageOfDisplaySPPEvents";
			request.setAttribute(Constants.PAGE_OF, request.getParameter(Constants.PAGE_OF));
		}
		return pageOf;
	}

}
