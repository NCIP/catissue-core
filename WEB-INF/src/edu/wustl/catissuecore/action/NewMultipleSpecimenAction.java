
package edu.wustl.catissuecore.action;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CreateSpecimenForm;
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

public class NewMultipleSpecimenAction extends SecureAction
{

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showCommentsDialog(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		request.setAttribute("output", "init");
		request.setAttribute("type", Constants.COMMENTS_TYPE);

		Map multipleSpecimenMap = chkForMultipleSpecimenMap(request);
		String keyInSpecimenMap = request.getParameter(Constants.SPECIMEN_ATTRIBUTE_KEY);
		String comments = (String) multipleSpecimenMap.get(keyInSpecimenMap);

		Logger.out.debug("setting comments to " + comments);

		((NewSpecimenForm) form).setComments(comments);
		return mapping.findForward("comments");
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showMultipleSpecimen(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		request.getSession().setAttribute(Constants.MULTIPLE_SPECIMEN_FORM_BEAN_MAP_KEY, new HashMap());
		request.getSession().setAttribute(Constants.MULTIPLE_SPECIMEN_MAP_KEY, new HashMap());
		request.getSession().setAttribute(Constants.MULTIPLE_SPECIMEN_EVENT_MAP_KEY, new HashMap());

		return mapping.findForward("specimen");
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward submitComments(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{

		String comments = ((NewSpecimenForm) form).getComments();
		request.setAttribute("output", "success");

		Map multipleSpecimenMap = chkForMultipleSpecimenMap(request);
		String keyInSpecimenMap = request.getParameter(Constants.SPECIMEN_ATTRIBUTE_KEY);
		multipleSpecimenMap.put(keyInSpecimenMap, comments);

		Logger.out.debug("User entered " + comments);
		return mapping.findForward("comments");
	}

	/**
	 * This method returns  multipleSpecimenMap from session 
	 * 
	 * @param request
	 * @return
	 */
	private Map chkForMultipleSpecimenMap(HttpServletRequest request)
	{
		Map multipleSpecimenMap = (Map) request.getSession().getAttribute(Constants.MULTIPLE_SPECIMEN_MAP_KEY);

		return multipleSpecimenMap;
	}

	private Map chkForEventsMap(HttpServletRequest request)
	{
		Map eventsMap = (Map) request.getSession().getAttribute(Constants.MULTIPLE_SPECIMEN_EVENT_MAP_KEY);
		return eventsMap;
	}

	/**
	 * This method returns  multipleSpecimenFormBeanMap from session 
	 * @param request
	 * @return
	 */

	private Map chkForMultipleSpecimenFormBeanMap(HttpServletRequest request)
	{
		Map multipleSpecimenMap = (Map) request.getSession().getAttribute(Constants.MULTIPLE_SPECIMEN_FORM_BEAN_MAP_KEY);
		if (multipleSpecimenMap == null)
		{
			Logger.out.debug("adding new multipleSpecimenMap to session");
			multipleSpecimenMap = new HashMap();
			request.getSession().setAttribute(Constants.MULTIPLE_SPECIMEN_MAP_KEY, multipleSpecimenMap);
		}
		return multipleSpecimenMap;
	}

	/**
	 * displays external identifer page populated with previously added information.
	 * 
	 */
	public ActionForward showExtenalIdentifierDialog(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		request.setAttribute("type", Constants.EXTERNALIDENTIFIER_TYPE);
		request.setAttribute("output", "init");

		Map multipleSpecimenMap = chkForMultipleSpecimenMap(request);
		String keyInSpecimenMap = request.getParameter(Constants.SPECIMEN_ATTRIBUTE_KEY);
		String keyForCount = keyInSpecimenMap + Constants.APPEND_COUNT;
		Map externalIdentifiers = (Map) multipleSpecimenMap.get(keyInSpecimenMap);
		Integer externalIdCnt = (Integer) multipleSpecimenMap.get(keyForCount);
		if (externalIdentifiers != null)
		{
			((NewSpecimenForm) form).setExternalIdentifier(externalIdentifiers);
			if (externalIdCnt != null)
				((NewSpecimenForm) form).setExIdCounter(externalIdCnt.intValue());
		}
		return mapping.findForward("externalIdentifier");
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward submitExternalIdentifiers(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{

		Map externalIdentifiers = ((NewSpecimenForm) form).getExternalIdentifier();
		int externalIdCnt = ((NewSpecimenForm) form).getExIdCounter();
		//		request.getSession().setAttribute("Comments",comments);

		request.setAttribute("output", "success");

		Map multipleSpecimenMap = chkForMultipleSpecimenMap(request);
		String keyInSpecimenMap = request.getParameter(Constants.SPECIMEN_ATTRIBUTE_KEY);
		String keyForCount = keyInSpecimenMap + Constants.APPEND_COUNT;
		multipleSpecimenMap.put(keyInSpecimenMap, externalIdentifiers);
		multipleSpecimenMap.put(keyForCount, new Integer(externalIdCnt));

		return mapping.findForward("externalIdentifier");
	}

	public ActionForward deleteExternalIdentifiers(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{

		request.setAttribute("output", "init");
		request.setAttribute("type", Constants.EXTERNALIDENTIFIER_TYPE);
		String button = request.getParameter("button");
		Map externalIdentifiers = ((NewSpecimenForm) form).getExternalIdentifier();

		if (button != null)
		{
			if (button.equals("deleteExId"))
			{
				List key = new ArrayList();
				key.add("ExternalIdentifier:i_name");
				key.add("ExternalIdentifier:i_value");
				MapDataParser.deleteRow(key, externalIdentifiers, request.getParameter("status"));
			}
		}

		return mapping.findForward("externalIdentifier");

	}

	/**
	 * displays external identifer page populated with previously added information.
	 * 
	 */
	public ActionForward showBioHazardDialog(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		request.setAttribute("type", Constants.BIOHAZARD_TYPE);
		request.setAttribute("output", "init");

		setBioHazardRequestAttributes(request);

		Map multipleSpecimenMap = chkForMultipleSpecimenMap(request);
		String keyInSpecimenMap = request.getParameter(Constants.SPECIMEN_ATTRIBUTE_KEY);
		String keyForCount = keyInSpecimenMap + Constants.APPEND_COUNT;
		Map bioHazards = (Map) multipleSpecimenMap.get(keyInSpecimenMap);
		Integer bioHazardCnt = (Integer) multipleSpecimenMap.get(keyForCount);
		if (bioHazards != null)
		{
			((NewSpecimenForm) form).setBiohazard(bioHazards);
			if (bioHazardCnt != null)
				((NewSpecimenForm) form).setBhCounter(bioHazardCnt.intValue());
		}
		return mapping.findForward("bioHazard");
	}

	/**
	 * displays external identifer page populated with previously added information.
	 * 
	 */

	public ActionForward showDerivedSpecimenDialog(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		int rowSelected = -1;
		//mandar: 06Nov06: multiplespecimen call  ----------- start
		if(request.getParameter("isMultipleSpecimenCall")!= null)
		{
			String isMultipleSpecimenCall = request.getParameter("isMultipleSpecimenCall");
			request.setAttribute("isMultipleSpecimenCall",isMultipleSpecimenCall ); 
		}
		//mandar: 06Nov06: multiplespecimen call  ----------- end
		
		
		if (request.getParameter("rowSelected") != null && !request.getParameter("rowSelected").equals("null"))
		{
			rowSelected = Integer.parseInt(request.getParameter("rowSelected"));
		}
		Map multipleSpecimenMap = chkForMultipleSpecimenFormBeanMap(request);
		String keyInSpecimenMap = request.getParameter(Constants.SPECIMEN_ATTRIBUTE_KEY);

		List formBeanList = (List) multipleSpecimenMap.get(keyInSpecimenMap);
		String forward = "derivedSpecimen";
		boolean flag = false;
		if (formBeanList != null && formBeanList.size() != 0)
		{
			flag = true;
			if (request.getParameter("deriveButtonClicked") != null)
			{
				request.setAttribute("showDerivedPage", "false");
			}

		}
		if (rowSelected != -1 || flag)
		{
			if (rowSelected != -1)
			{
				AbstractActionForm actionForm = (AbstractActionForm) formBeanList.get(rowSelected);
				request.setAttribute(Constants.DERIVED_FORM, actionForm);
				forward = "derivedSpecimenEdit";
			}

			if (formBeanList != null && formBeanList.size() > 0)
			{
				int count = 0;

				List gridData = new ArrayList();
				Iterator it = formBeanList.iterator();

				while (it.hasNext())
				{
					List rowData = new ArrayList();
					CreateSpecimenForm tempForm = (CreateSpecimenForm) it.next();

					rowData.add(tempForm.getLabel());
					rowData.add(tempForm.getClassName());
					rowData.add(tempForm.getType());
					rowData.add(tempForm.getQuantity());
					rowData.add("" + count);
					gridData.add(rowData);
					count++;
				}

				request.setAttribute(Constants.SPREADSHEET_DATA_LIST, gridData);
			}

		}
		return mapping.findForward(forward);
	}

	/**
	 * displays external identifer page populated with previously added information.
	 * 
	 */

	public ActionForward addDerivedSpecimen(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{

		int rowSelected = -1;
		if (request.getParameter("rowSelected") != null && !request.getParameter("rowSelected").equals("null"))
		{
			rowSelected = Integer.parseInt(request.getParameter("rowSelected"));
		}
		AbstractActionForm actionForm = (AbstractActionForm) request.getAttribute(Constants.DERIVED_FORM);

		Map multipleSpecimenMap = chkForMultipleSpecimenFormBeanMap(request);
		String keyInSpecimenMap = request.getParameter(Constants.SPECIMEN_ATTRIBUTE_KEY);

		List formBeanList = (List) multipleSpecimenMap.get(keyInSpecimenMap);
		if (formBeanList == null)
		{
			formBeanList = new ArrayList();
		}

		if (rowSelected != -1)
		{
			formBeanList.remove(rowSelected);
			formBeanList.add(rowSelected, actionForm);
		}
		else
		{
			formBeanList.add(actionForm);
		}

		multipleSpecimenMap.put(keyInSpecimenMap, formBeanList);
		request.setAttribute("showDerivedPage", "false");
		return mapping.findForward("showDerivedSpecimenDialog");
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward submitBioHazards(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{

		Map bioHazards = ((NewSpecimenForm) form).getBiohazard();
		
		int bioHazardCnt = ((NewSpecimenForm) form).getBhCounter();

		request.setAttribute("output", "success");
		request.setAttribute("type", Constants.BIOHAZARD_TYPE);
		setBioHazardRequestAttributes(request);

		Map multipleSpecimenMap = chkForMultipleSpecimenMap(request);
		String keyInSpecimenMap = request.getParameter(Constants.SPECIMEN_ATTRIBUTE_KEY);
		String keyForCount = keyInSpecimenMap + Constants.APPEND_COUNT;
		multipleSpecimenMap.put(keyInSpecimenMap, bioHazards);
		multipleSpecimenMap.put(keyForCount, new Integer(bioHazardCnt));

		return mapping.findForward("bioHazard");
	}

	public ActionForward deleteBioHazards(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{

		request.setAttribute("output", "init");
		request.setAttribute("type", Constants.BIOHAZARD_TYPE);
		String button = request.getParameter("button");
		Map bioHazards = ((NewSpecimenForm) form).getBiohazard();

		if (button != null)
		{
			if (button.equals("deleteBiohazard"))
			{
				List key = new ArrayList();
				key.add("Biohazard:i_type");
				key.add("Biohazard:i_id");
				MapDataParser.deleteRow(key, bioHazards, request.getParameter("status"));

			}
		}
		setBioHazardRequestAttributes(request);
		return mapping.findForward("bioHazard");

	}

	private void setBioHazardRequestAttributes(HttpServletRequest request) throws DAOException
	{

		//Setting biohazard list

		NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);

		String[] bhIdArray = {"-1"};
		String[] bhTypeArray = {Constants.SELECT_OPTION};
		String[] bhNameArray = {Constants.SELECT_OPTION};

		String selectColNames[] = {Constants.SYSTEM_IDENTIFIER, "name", "type"};
		List biohazardList = bizLogic.retrieve(Biohazard.class.getName(), selectColNames);
		Iterator iterator = biohazardList.iterator();

		//Creating & setting the biohazard name, id & type list
		if (biohazardList != null && !biohazardList.isEmpty())
		{
			bhIdArray = new String[biohazardList.size() + 1];
			bhTypeArray = new String[biohazardList.size() + 1];
			bhNameArray = new String[biohazardList.size() + 1];

			bhIdArray[0] = "-1";
			bhTypeArray[0] = "";
			bhNameArray[0] = Constants.SELECT_OPTION;

			int i = 1;

			while (iterator.hasNext())
			{
				Object hazard[] = (Object[]) iterator.next();
				bhIdArray[i] = String.valueOf(hazard[0]);
				bhNameArray[i] = (String) hazard[1];
				bhTypeArray[i] = (String) hazard[2];
				i++;
			}
		}

		request.setAttribute(Constants.BIOHAZARD_NAME_LIST, bhNameArray);
		request.setAttribute(Constants.BIOHAZARD_ID_LIST, bhIdArray);
		request.setAttribute(Constants.BIOHAZARD_TYPES_LIST, bhTypeArray);

		biohazardList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_BIOHAZARD, null);
		request.setAttribute(Constants.BIOHAZARD_TYPE_LIST, biohazardList);

	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showEventsDialog(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		request.setAttribute("output", "init");
		request.setAttribute("type", Constants.EVENTS_TYPE);

		setEventsRequestAttributes(request);

		UserBizLogic userBizLogic = (UserBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.USER_FORM_ID);
		Collection userCollection = userBizLogic.getUsers(Constants.ADD);

		SessionDataBean sessionData = getSessionData(request);
		if (sessionData != null)
		{
			String user = sessionData.getLastName() + ", " + sessionData.getFirstName();
			long collectionEventUserId = getIdFromCollection(userCollection, user);
			((NewSpecimenForm) form).setCollectionEventUserId(collectionEventUserId);
			((NewSpecimenForm) form).setReceivedEventUserId(collectionEventUserId);
		}
		setDateParameters((NewSpecimenForm) form);

		Map multipleSpecimenEventMap = chkForEventsMap(request);
		String keyInEventSpecimenMap = request.getParameter(Constants.SPECIMEN_ATTRIBUTE_KEY);
		NewSpecimenForm specimenForm = (NewSpecimenForm) multipleSpecimenEventMap.get(keyInEventSpecimenMap);

		if (specimenForm != null)
		{

			//NewSpecimenForm specimenForm = (NewSpecimenForm)collAndReceivedParameters.get("CollAndRecEvent");
			setCollectionEventParameters(form, specimenForm);
			setRecievedEventParameters(form, specimenForm);
		}
		//to display default selection as NotSpecified
		setDefaultListSelection(form);
		return mapping.findForward("events");
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward submitEvents(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{

		//request.setAttribute("output", "success");
		ActionErrors errors = validateEvents(request, form);
		if (errors.isEmpty())
		{
			request.setAttribute("output", "success");
			Map multipleSpecimenEventMap = chkForEventsMap(request);
			String keyInSpecimenEventMap = request.getParameter(Constants.SPECIMEN_ATTRIBUTE_KEY);

			multipleSpecimenEventMap.put(keyInSpecimenEventMap, form);

			return mapping.findForward("events");
		}
		else
		{
			saveErrors(request, errors);
			return showEventsDialog(mapping, form, request, response);
		}
		//return mapping.findForward("events");
	}

	private void setEventsRequestAttributes(HttpServletRequest request) throws DAOException
	{
		UserBizLogic userBizLogic = (UserBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.USER_FORM_ID);
		Collection userCollection = userBizLogic.getUsers(Constants.ADD);

		request.setAttribute(Constants.USERLIST, userCollection);
		//Sets the hourList attribute to be used in the Add/Edit FrozenEventParameters Page.
		request.setAttribute(Constants.HOUR_LIST, Constants.HOUR_ARRAY);
		//Sets the minutesList attribute to be used in the Add/Edit FrozenEventParameters Page.
		request.setAttribute(Constants.MINUTES_LIST, Constants.MINUTES_ARRAY);
		// set the procedure lists
		List procedureList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_COLLECTION_PROCEDURE, null);
		request.setAttribute(Constants.PROCEDURE_LIST, procedureList);

		List qualityList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_RECEIVED_QUALITY, null);
		request.setAttribute(Constants.RECEIVED_QUALITY_LIST, qualityList);

		//		 set the container lists
		List containerList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_CONTAINER, null);
		request.setAttribute(Constants.CONTAINER_LIST, containerList);

	}

	private void setCollectionEventParameters(ActionForm form, NewSpecimenForm specimenForm)
	{
		((NewSpecimenForm) form).setCollectionEventId(specimenForm.getCollectionEventId());
		((NewSpecimenForm) form).setCollectionEventSpecimenId(specimenForm.getCollectionEventSpecimenId());
		((NewSpecimenForm) form).setCollectionEventUserId(specimenForm.getCollectionEventUserId());
		((NewSpecimenForm) form).setCollectionEventdateOfEvent(specimenForm.getCollectionEventdateOfEvent());
		((NewSpecimenForm) form).setCollectionEventTimeInHours(specimenForm.getCollectionEventTimeInHours());
		((NewSpecimenForm) form).setCollectionEventTimeInMinutes(specimenForm.getCollectionEventTimeInMinutes());
		((NewSpecimenForm) form).setCollectionEventCollectionProcedure(specimenForm.getCollectionEventCollectionProcedure());
		((NewSpecimenForm) form).setCollectionEventContainer(specimenForm.getCollectionEventContainer());
		((NewSpecimenForm) form).setCollectionEventComments(specimenForm.getCollectionEventComments());

	}

	private void setRecievedEventParameters(ActionForm form, NewSpecimenForm specimenForm)
	{
		((NewSpecimenForm) form).setReceivedEventId(specimenForm.getReceivedEventId());
		((NewSpecimenForm) form).setReceivedEventSpecimenId(specimenForm.getReceivedEventSpecimenId());
		((NewSpecimenForm) form).setReceivedEventUserId(specimenForm.getReceivedEventUserId());
		((NewSpecimenForm) form).setReceivedEventDateOfEvent(specimenForm.getReceivedEventDateOfEvent());
		((NewSpecimenForm) form).setReceivedEventTimeInHours(specimenForm.getReceivedEventTimeInHours());
		((NewSpecimenForm) form).setReceivedEventTimeInMinutes(specimenForm.getReceivedEventTimeInMinutes());
		((NewSpecimenForm) form).setReceivedEventReceivedQuality(specimenForm.getReceivedEventReceivedQuality());
		((NewSpecimenForm) form).setReceivedEventComments(specimenForm.getReceivedEventComments());
	}

//	private SessionDataBean getSessionData(HttpServletRequest request)
//	{
//		Object obj = request.getSession().getAttribute(Constants.SESSION_DATA);
//		if (obj != null)
//		{
//			SessionDataBean sessionData = (SessionDataBean) obj;
//			return sessionData;
//		}
//		return null;
//	}

	/**
	 * 
	 * @param userList Collection
	 * @param userName userName
	 * @return long
	 */
	private long getIdFromCollection(Collection userList, String userName)
	{
		Iterator itr = userList.iterator();
		for (int i = 0; itr.hasNext(); i++)
		{
			NameValueBean nameValueBean = (NameValueBean) itr.next();
			if (nameValueBean.getName() != null && nameValueBean.getName().trim().equals(userName))
			{
				String id = nameValueBean.getValue();
				return Long.valueOf(id).longValue();
			}
		}
		return -1;
	}

	private void setDateParameters(NewSpecimenForm specimenForm)
	{
		// set the current Date and Time for the event.
		Calendar cal = Calendar.getInstance();
		//Collection Event fields
		if (specimenForm.getCollectionEventdateOfEvent() == null)
		{
			specimenForm.setCollectionEventdateOfEvent(Utility.parseDateToString(cal.getTime(), Constants.DATE_PATTERN_MM_DD_YYYY));
		}
		if (specimenForm.getCollectionEventTimeInHours() == null)
		{
			specimenForm.setCollectionEventTimeInHours(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)));
		}
		if (specimenForm.getCollectionEventTimeInMinutes() == null)
		{
			specimenForm.setCollectionEventTimeInMinutes(Integer.toString(cal.get(Calendar.MINUTE)));
		}

		//ReceivedEvent Fields
		if (specimenForm.getReceivedEventDateOfEvent() == null)
		{
			specimenForm.setReceivedEventDateOfEvent(Utility.parseDateToString(cal.getTime(), Constants.DATE_PATTERN_MM_DD_YYYY));
		}
		if (specimenForm.getReceivedEventTimeInHours() == null)
		{
			specimenForm.setReceivedEventTimeInHours(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)));
		}
		if (specimenForm.getReceivedEventTimeInMinutes() == null)
		{
			specimenForm.setReceivedEventTimeInMinutes(Integer.toString(cal.get(Calendar.MINUTE)));
		}

	}

	private ActionErrors validateEvents(HttpServletRequest request, ActionForm actionForm)
	{
		NewSpecimenForm form = (NewSpecimenForm) actionForm;
		Validator validator = new Validator();
		ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
		if (errors == null || errors.size() == 0)
		{
			errors = new ActionErrors();
		}
		if ((form.getCollectionEventUserId()) == -1L)
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required", "Collection Event's user"));
		}
		if (!validator.checkDate(form.getCollectionEventdateOfEvent()))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required", "Collection Event's date"));
		}

		// checks the collectionProcedure
		if (!validator.isValidOption(form.getCollectionEventCollectionProcedure()))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required", ApplicationProperties
					.getValue("collectioneventparameters.collectionprocedure")));
		}
		//		 checks the collection container
		if (!validator.isValidOption(form.getCollectionEventContainer()))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required", ApplicationProperties
					.getValue("collectioneventparameters.container")));
		}

		if ((form.getReceivedEventUserId()) == -1L)
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required", "Received Event's user"));
		}
		if (!validator.checkDate(form.getReceivedEventDateOfEvent()))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required", "Received Event's date"));
		}

		// checks the collectionProcedure
		if (!validator.isValidOption(form.getReceivedEventReceivedQuality()))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required", ApplicationProperties
					.getValue("receivedeventparameters.receivedquality")));
		}

		return errors;

	}

	/*
	 * This method sets the default selection of list boxes to Not Specified.
	 * @author mandar_deshmukh
	 *
	 */
	private void setDefaultListSelection(ActionForm form)
	{
		if(form!=null)
		{
			if(((NewSpecimenForm) form).getCollectionEventCollectionProcedure() == null)
				((NewSpecimenForm) form).setCollectionEventCollectionProcedure(Constants.NOT_SPECIFIED);
			
			if(((NewSpecimenForm) form).getCollectionEventContainer() == null)
				((NewSpecimenForm) form).setCollectionEventContainer(Constants.NOT_SPECIFIED);
			
			if(((NewSpecimenForm) form).getReceivedEventReceivedQuality() == null)
				((NewSpecimenForm) form).setReceivedEventReceivedQuality(Constants.NOT_SPECIFIED);
		}
	}
	
	// -----------------
	// --------- Changes By  Mandar : 05Dec06 for Bug 2866 
	// --------- Extending SecureAction.
	
	/**
     * Overrides the executeSecureAction method of SecureAction class.
     * */
    protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
		//-- code for handling method calls
		String methodName = request.getParameter(Constants.METHOD_NAME);
		if(methodName!= null)
		{
			return invokeMethod(methodName, mapping, form, request, response);
		}
		return null;
    }

}