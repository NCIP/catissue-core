package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ProtocolEventDetailsForm;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.DefaultValueManager;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.dbManager.DAOException;


public class ProtocolEventDetailsAction extends BaseAction
{
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		ProtocolEventDetailsForm protocolEventDetailsForm =(ProtocolEventDetailsForm)form;
		String operation = (String)request.getParameter(Constants.OPERATION);
		HttpSession session = request.getSession();
		//Event Key when flow is form Specimen Requirement Page
		String key = (String)request.getParameter(Constants.EVENT_KEY);
		String nodeId = (String)request.getParameter(Constants.TREE_NODE_ID);
		session.setAttribute(Constants.TREE_NODE_ID,nodeId);
		String pageOf = request.getParameter(Constants.PAGE_OF);
		setUserInForm(request,operation,protocolEventDetailsForm);
		if(pageOf!=null && pageOf.equals(Constants.PAGE_OF_DEFINE_EVENTS))
		{
			initSpecimenrequirementForm(mapping, protocolEventDetailsForm, request);
		}
		else if(operation.equals(Constants.ADD)&&pageOf.equals(Constants.PAGE_OF_ADD_NEW_EVENT))
		{
			protocolEventDetailsForm.setCollectionProtocolEventkey(Constants.ADD_NEW_EVENT);
			protocolEventDetailsForm.setStudyCalendarEventPoint(1D);
			protocolEventDetailsForm.setCollectionPointLabel("");
			protocolEventDetailsForm.setClinicalStatus((String)DefaultValueManager.getDefaultValue(Constants.DEFAULT_CLINICAL_STATUS));
			protocolEventDetailsForm.setClinicalDiagnosis((String)DefaultValueManager.getDefaultValue(Constants.DEFAULT_CLINICAL_DIAGNOSIS));
			protocolEventDetailsForm.setCollectionEventCollectionProcedure((String)DefaultValueManager.getDefaultValue(Constants.DEFAULT_COLLECTION_PROCEDURE));
			protocolEventDetailsForm.setCollectionEventContainer((String)DefaultValueManager.getDefaultValue(Constants.DEFAULT_CONTAINER));
			protocolEventDetailsForm.setReceivedEventReceivedQuality((String)DefaultValueManager.getDefaultValue(Constants.DEFAULT_RECEIVED_QUALITY));
		}
		else if(pageOf!=null&&pageOf.equals(Constants.PAGE_OF_SPECIMEN_REQUIREMENT))
		{
			protocolEventDetailsForm.setCollectionProtocolEventkey(key);
		}
		List clinicalStatusList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_CLINICAL_STATUS,null);
		List clinicalDiagnosisList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_CLINICAL_DIAGNOSIS,null);
    	request.setAttribute(Constants.CLINICAL_STATUS_LIST, clinicalStatusList);
    	request.setAttribute(Constants.CLINICAL_DIAGNOSIS_LIST, clinicalDiagnosisList);

    	//setting the procedure
		List procedureList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_COLLECTION_PROCEDURE, null);
		request.setAttribute(Constants.PROCEDURE_LIST, procedureList);
		//set the container lists
		List containerList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_CONTAINER, null);
		request.setAttribute(Constants.CONTAINER_LIST, containerList);	

		//setting the quality for received events
		List qualityList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_RECEIVED_QUALITY, null);
		request.setAttribute(Constants.RECEIVED_QUALITY_LIST, qualityList);
		
    	request.setAttribute("protocolEventDetailsForm", protocolEventDetailsForm);
		return (mapping.findForward(Constants.SUCCESS));
	}
	
	/**
	 * @param request
	 * @param operation
	 * @param specimenCollectionGroupForm
	 * @throws DAOException
	 */
	private void setUserInForm(HttpServletRequest request,String operation,ProtocolEventDetailsForm protocolEventDetailsForm) 
	{
		UserBizLogic userBizLogic = (UserBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.USER_FORM_ID);
		Collection userCollection = null;
		try
		{
			userCollection = userBizLogic.getUsers(operation);
		}
		catch (DAOException e)
		{
			e.printStackTrace();
		}

		request.setAttribute(Constants.USERLIST, userCollection);

		SessionDataBean sessionData = getSessionData(request);
		if (sessionData != null)
		{
			String user = sessionData.getLastName() + ", " + sessionData.getFirstName();
			long collectionEventUserId = EventsUtil.getIdFromCollection(userCollection, user);

			if(protocolEventDetailsForm.getCollectionEventUserId() == 0)
			{
				protocolEventDetailsForm.setCollectionEventUserId(collectionEventUserId);
			}
			if(protocolEventDetailsForm.getReceivedEventUserId() == 0)
			{
				protocolEventDetailsForm.setReceivedEventUserId(collectionEventUserId);
			}
		}
	}
	
	private void initSpecimenrequirementForm(ActionMapping mapping, ProtocolEventDetailsForm protocolEventDetailsForm, HttpServletRequest request)
	{
		HttpSession session = request.getSession(); 
		Map collectionProtocolEventMap = (Map)session.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
		
		//If flow is from Specimen Requirement page save button.
		String collectionProtocolEventKey = (String)request.getAttribute(Constants.EVENT_KEY);
		
		if(collectionProtocolEventKey==null)
		{
			//If flow is from Specimen Tree View if Event Node is clicked to open Event page in Edit mode.
			collectionProtocolEventKey = (String)request.getParameter(Constants.EVENT_KEY);
		}
		if(collectionProtocolEventKey==null)
		{
			//If flow is when user Clicks Define Event button.
			collectionProtocolEventKey = (String)session.getAttribute(Constants.NEW_EVENT_KEY);
		}
		StringTokenizer st = new StringTokenizer(collectionProtocolEventKey,"_");
		if(st.hasMoreTokens())
		{
			collectionProtocolEventKey = st.nextToken();
		}
		
		CollectionProtocolEventBean collectionProtocolEventBean = (CollectionProtocolEventBean)collectionProtocolEventMap.get(collectionProtocolEventKey);	
		protocolEventDetailsForm.setClinicalDiagnosis(collectionProtocolEventBean.getClinicalDiagnosis());
		protocolEventDetailsForm.setClinicalStatus(collectionProtocolEventBean.getClinicalStatus());
		protocolEventDetailsForm.setCollectionPointLabel(collectionProtocolEventBean.getCollectionPointLabel());
		protocolEventDetailsForm.setStudyCalendarEventPoint(collectionProtocolEventBean.getStudyCalenderEventPoint());
		
		protocolEventDetailsForm.setCollectionEventComments(collectionProtocolEventBean.getCollectedEventComments());
		protocolEventDetailsForm.setCollectionEventContainer(collectionProtocolEventBean.getCollectionContainer());
		protocolEventDetailsForm.setReceivedEventComments(collectionProtocolEventBean.getReceivedEventComments());
		protocolEventDetailsForm.setReceivedEventReceivedQuality(collectionProtocolEventBean.getReceivedQuality());
		protocolEventDetailsForm.setCollectionEventCollectionProcedure(collectionProtocolEventBean.getCollectionProcedure());
		protocolEventDetailsForm.setCollectionProtocolEventkey(collectionProtocolEventKey);
	
	}
	
}
