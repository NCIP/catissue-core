package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.actionForm.ViewSurgicalPathologyReportForm;
import edu.wustl.catissuecore.bizlogic.AnnotationUtil;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.IdentifiedSurgicalPathologyReportBizLogic;
import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.caties.util.ViewSPRUtil;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.PathologyReportReviewParameter;
import edu.wustl.catissuecore.domain.pathology.QuarantineEventParameter;
import edu.wustl.catissuecore.domain.pathology.SurgicalPathologyReport;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.PrivilegeCache;
import edu.wustl.common.security.PrivilegeManager;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.util.Permissions;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.common.util.logger.Logger;
/**
 * @author vijay_pande
 * Action class to show Surgical Pathology  Report
 */
public class ViewSurgicalPathologyReportAction extends BaseAction
{
	/**
	 * @see edu.wustl.common.action.BaseAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ViewSurgicalPathologyReportForm viewSPR=(ViewSurgicalPathologyReportForm)form;
		String pageOf = viewSPR.getPageOf();
		String operation = viewSPR.getOperation();
		String submittedFor=viewSPR.getSubmittedFor();
		String forwardTo=viewSPR.getForwardTo();
		String strId =(String)request.getParameter(Constants.SYSTEM_IDENTIFIER);
		String reportIdStr=(String)request.getParameter("reportId");
		//If reportId is null in request then retrieved from form. For Review/Quarantine event param. Bug id: 9260
		if(reportIdStr==null)
		{
			reportIdStr = viewSPR.getIdentifiedReportId();
		}
		Long reportId=Long.valueOf(reportIdStr);
		
		Long id=null;
		if(strId!=null)
		{
			id=new Long(strId);
			viewSPR.setId(id);
		}	
		String strIdentifier = (String)request.getParameter(Constants.IDENTIFIER);
		Long identifier=null;
		if(strIdentifier!=null)
		{
			identifier=new Long(strIdentifier);
			viewSPR.setId(identifier);
			retriveFromReportId(identifier, request, viewSPR);
		}
		if(reportId!=null && reportId!=0 && operation.equalsIgnoreCase(Constants.VIEW_SURGICAL_PATHOLOGY_REPORT))
        {
            retrieveAndSetObject(pageOf, reportId, request, viewSPR);
        }
		String aliasName = "";
		request.setAttribute(Constants.PARTICIPANTIDFORREPORT, viewSPR.getParticipantIdForReport());
		viewSPR.setHasAccess(isAuthorized(getSessionBean(request), viewSPR.getCollectionProtocolId(), aliasName));
		// If request is from Query to view Deidentfied report
		if (viewSPR.getIdentifiedReportId() == null || viewSPR.getIdentifiedReportId() == "")
		{
			viewSPR.setHasAccess(false);
		}
        request.setAttribute(Constants.PAGEOF, pageOf);
        request.setAttribute(Constants.OPERATION, Constants.VIEW_SURGICAL_PATHOLOGY_REPORT);
        request.setAttribute(Constants.REQ_PATH, "");
        request.setAttribute(Constants.SUBMITTED_FOR, submittedFor);
        request.setAttribute(Constants.FORWARD_TO, forwardTo);
//      Falguni:Performance Enhancement.
		Long specimenEntityId = null;
        if (CatissueCoreCacheManager.getInstance().getObjectFromCache("specimenEntityId") != null)
    	{
    		specimenEntityId = (Long) CatissueCoreCacheManager.getInstance().getObjectFromCache("specimenEntityId");
    	}
    	else
    	{
    		specimenEntityId = AnnotationUtil.getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN);
    		CatissueCoreCacheManager.getInstance().addObjectToCache("specimenEntityId",specimenEntityId);		
    	}
        request.setAttribute("specimenEntityId",specimenEntityId );
//      Falguni:Performance Enhancement -User clicks on Report tab then annotation page on Edit participant page
		Long participantEntityId = null;
		if (CatissueCoreCacheManager.getInstance().getObjectFromCache("participantEntityId") != null)
		{
			participantEntityId = (Long) CatissueCoreCacheManager.getInstance().getObjectFromCache("participantEntityId");
		}
		else
		{
			participantEntityId = AnnotationUtil.getEntityId(AnnotationConstants.ENTITY_NAME_PARTICIPANT);
			CatissueCoreCacheManager.getInstance().addObjectToCache("participantEntityId",participantEntityId);		
		}
		request.setAttribute("participantEntityId",participantEntityId);
        if(pageOf.equalsIgnoreCase(Constants.PAGEOF_NEW_SPECIMEN)|| pageOf.equalsIgnoreCase(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
        {
        	request.setAttribute(Constants.ID,id.toString());
        }
        String flow = request.getParameter("flow");
        if (flow != null && flow.equals("viewReport"))
        {
        	pageOf = "gridViewReport";
        }  
        return mapping.findForward(pageOf);
        
	}
	
	
	/**
	 * This method retrives the appropriate SurgicalPathologyReport object and set values of ViewSurgicalPathologyReportForm object
	 * @param pageOf pageOf variable to find out domain object 
	 * @param id Identifier of the domain object
	 * @param request HttpServletRequest object
	 * @throws DAOException exception occured while DB handling
	 * @throws BizLogicException
	 */
	private void retrieveAndSetObject(String pageOf, Long reportId, HttpServletRequest request, ViewSurgicalPathologyReportForm viewSPR) throws DAOException
	{
		if(pageOf.equalsIgnoreCase(Constants.PAGEOF_PARTICIPANT) || pageOf.equalsIgnoreCase(Constants.PAGE_OF_PARTICIPANT_CP_QUERY))
		{
			
			Long participantId=getParticipantId(reportId);
			ParticipantBizLogic bizLogic=(ParticipantBizLogic)BizLogicFactory.getInstance().getBizLogic(Participant.class.getName());
			List scgList=bizLogic.getSCGList(participantId);

			viewSPR.setReportIdList(getReportIdList(scgList));
		}

		if(reportId!=null)
		{
			IdentifiedSurgicalPathologyReportBizLogic bizLogic=(IdentifiedSurgicalPathologyReportBizLogic)BizLogicFactory.getInstance().getBizLogic(IdentifiedSurgicalPathologyReport.class.getName());
			SurgicalPathologyReport report=new SurgicalPathologyReport();
			report.setId(reportId);
			try
			{
				bizLogic.populateUIBean(SurgicalPathologyReport.class.getName(), report.getId(), viewSPR);
				DeidentifiedSurgicalPathologyReport deidReport=new DeidentifiedSurgicalPathologyReport();
				deidReport.setId(viewSPR.getDeIdentifiedReportId());				
				List conceptBeanList=ViewSPRUtil.getConceptBeanList(deidReport);
				request.setAttribute(Constants.CONCEPT_BEAN_LIST, conceptBeanList);
			}
			catch(Exception ex)
			{
				Logger.out.error(ex);
			}
		}
	}
	
	/**
	 * @param scgCollection A collection of SpecimenCollectionGroup Id
	 * @return List of SurgicalPathologyReport Id
	 * @throws DAOException Exception occured while handling DB
	 */
	private List getReportIdList(List scgList)throws DAOException
	{
		List<NameValueBean> reportIDList=new ArrayList<NameValueBean>();
		Object[] obj=null;
		for(int i=0;i<scgList.size();i++)
		{
			obj=(Object[])scgList.get(i);
			if(obj[1]!=null || (obj[1]!=null && !((String)obj[1]).equals("")))
			{
				NameValueBean nb=new NameValueBean(obj[1],((Long)obj[2]).toString());
				reportIDList.add(nb);
			}		
		}
		return reportIDList;
	}
	
	/**
	 * Adding name,value pair in NameValueBean for Witness Name
	 * @param collProtId Get Witness List for this ID
	 * @return consentWitnessList
	 * @throws BizLogicException 
	 */ 
	public void retriveFromReportId(Long identifier,HttpServletRequest request, ViewSurgicalPathologyReportForm viewSPR) throws DAOException, BizLogicException
	{   	
		IBizLogic bizLogic=null;
		
		String witnessFullName=null;
		DefaultBizLogic defaultBizLogic=new DefaultBizLogic();
		String pageOf = request.getParameter(Constants.PAGEOF);
		
		if(pageOf.equalsIgnoreCase(Constants.PAGEOF_REVIEW_SPR))
		{
			request.setAttribute(Constants.OPERATION, Constants.REVIEW);
			viewSPR.setOperation(Constants.REVIEW);
			bizLogic =BizLogicFactory.getInstance().getBizLogic(Constants.PATHOLOGY_REPORT_REVIEW_FORM_ID);
			Object object = bizLogic.retrieve(PathologyReportReviewParameter.class.getName(), identifier);
			PathologyReportReviewParameter pathologyReportReviewParameter = (PathologyReportReviewParameter) object;
			viewSPR.setUserComments(pathologyReportReviewParameter.getComment());
			User user = (User)defaultBizLogic.retrieveAttribute(PathologyReportReviewParameter.class.getName(), pathologyReportReviewParameter.getId(), "user");
			witnessFullName = user.getFirstName()+", "+user.getLastName()+"'s";
			viewSPR.setUserName(witnessFullName);
			SurgicalPathologyReport surgicalPathologyReport = (SurgicalPathologyReport)defaultBizLogic.retrieveAttribute(PathologyReportReviewParameter.class.getName(), pathologyReportReviewParameter.getId(), "surgicalPathologyReport");
			if(surgicalPathologyReport instanceof DeidentifiedSurgicalPathologyReport)
			{
				Long identifiedSurgicalPathologyReportId =(Long)defaultBizLogic.retrieveAttribute(DeidentifiedSurgicalPathologyReport.class.getName(), surgicalPathologyReport.getId(), "specimenCollectionGroup.identifiedSurgicalPathologyReport.id");
				defaultBizLogic.populateUIBean(IdentifiedSurgicalPathologyReport.class.getName(), identifiedSurgicalPathologyReportId, viewSPR);
			}
			else
			{
				IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport =(IdentifiedSurgicalPathologyReport)surgicalPathologyReport;
				defaultBizLogic.populateUIBean(IdentifiedSurgicalPathologyReport.class.getName(), identifiedSurgicalPathologyReport.getId(), viewSPR);
			}
		}
		else
		{
			request.setAttribute(Constants.OPERATION, Constants.QUARANTINE);
			viewSPR.setOperation(Constants.QUARANTINE);
			bizLogic =BizLogicFactory.getInstance().getBizLogic(Constants.QUARANTINE_EVENT_PARAMETER_FORM_ID);
			Object object  = bizLogic.retrieve(QuarantineEventParameter.class.getName(), identifier);
			QuarantineEventParameter quarantineEventParameter =(QuarantineEventParameter) object;
			viewSPR.setUserComments(quarantineEventParameter.getComment());
			User user = (User)defaultBizLogic.retrieveAttribute(QuarantineEventParameter.class.getName(), quarantineEventParameter.getId(), "user");
			witnessFullName = user.getLastName()+", "+user.getFirstName();
			viewSPR.setUserName(witnessFullName);
			Long deIdentifiedSurgicalPathologyReportId =(Long)defaultBizLogic.retrieveAttribute(QuarantineEventParameter.class.getName(), quarantineEventParameter.getId(), "deIdentifiedSurgicalPathologyReport.specimenCollectionGroup.identifiedSurgicalPathologyReport.id");
			defaultBizLogic.populateUIBean(IdentifiedSurgicalPathologyReport.class.getName(), deIdentifiedSurgicalPathologyReportId, viewSPR);
		}
	}
	
	/**
	 * Method to retrieve participant Id associated with the identified report
	 * @param identifiedReportId Id of identified report
	 * @return participant Id
	 * @throws DAOException DAO exception occured while running query
	 */
	private Long getParticipantId(Long identifiedReportId) throws DAOException
	{
		IdentifiedSurgicalPathologyReportBizLogic bizLogic=(IdentifiedSurgicalPathologyReportBizLogic)BizLogicFactory.getInstance().getBizLogic(IdentifiedSurgicalPathologyReport.class.getName());
	
		String sourceObjectName=IdentifiedSurgicalPathologyReport.class.getName();
		String[] selectColumnName={Constants.COLUMN_NAME_SCG_CPR_PARTICIPANT_ID};
		String[] whereColumnName={Constants.SYSTEM_IDENTIFIER};
		String[] whereColumnCondition={"="}; 
		Object[] whereColumnValue={identifiedReportId};
		String joinCondition="";
		
		List participantIdList=bizLogic.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue,joinCondition);
		if(participantIdList!=null && participantIdList.size()>0)
		{
			return (Long)participantIdList.get(0); 
		}
		return null;
	}
	
	/**
	 * This method is to retrieve sessionDataBean from request object
	 * @param request HttpServletRequest object
	 * @return sessionBean SessionDataBean object
	 */
	private SessionDataBean getSessionBean(HttpServletRequest request)
	{
		try
		{
			SessionDataBean sessionBean = (SessionDataBean) request.getSession().getAttribute(Constants.SESSION_DATA);
			return sessionBean;
		}
		catch(Exception ex)
		{
			return null;
		}
	}
	
	/**
	 * This method verifies wthere the user is 
	 * @param sessionBean
	 * @return
	 * @throws Exception
	 */
	private boolean isAuthorized(SessionDataBean sessionBean, long identifier, String aliasName) throws Exception
	{ 
		String userName = sessionBean.getUserName();
		
		// To get privilegeCache through 
		// Singleton instance of PrivilegeManager, requires User LoginName		
		PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
		PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(userName);
		boolean isAuthorized = true;
		if(sessionBean.isSecurityRequired())
		{
			SecurityManager sm=SecurityManager.getInstance(this.getClass());
			aliasName =CollectionProtocol.class.getName(); 
			
//			String userName = sessionBean.getUserName();
			
			// Call to SecurityManager.checkPermission bypassed &
			// instead, call redirected to privilegeCache.hasPrivilege			
			isAuthorized  = privilegeCache.hasPrivilege(aliasName+"_"+String.valueOf(identifier), Permissions.READ_DENIED);
//			boolean isAuthorized  = SecurityManager.getInstance(ViewSurgicalPathologyReportAction.class).
//			checkPermission(userName, aliasName, identifier, Permissions.READ_DENIED, PrivilegeType.ObjectLevel);
			if(!isAuthorized)
			{
				//Check the permission of the user on the identified data of the object.
				// Call to SecurityManager.checkPermission bypassed &
				// instead, call redirected to privilegeCache.hasPrivilege	
				boolean hasPrivilegeOnIdentifiedData  = privilegeCache.hasPrivilege(aliasName+"_"+identifier, Permissions.REGISTRATION);
				if(!hasPrivilegeOnIdentifiedData)
				{
					hasPrivilegeOnIdentifiedData = Utility.checkForAllCurrentAndFutureCPs(null,  Permissions.REGISTRATION, sessionBean, String.valueOf(identifier));
				}
//				boolean hasPrivilegeOnIdentifiedData  = SecurityManager.getInstance(ViewSurgicalPathologyReportAction.class).
//				checkPermission(userName, aliasName, identifier, Permissions.IDENTIFIED_DATA_ACCESS, PrivilegeType.ObjectLevel); 

				if(!hasPrivilegeOnIdentifiedData)
				{
					isAuthorized = false;
				}
				else
				{
					isAuthorized = true;
				}
			}
		}
		return isAuthorized;
	}
}




