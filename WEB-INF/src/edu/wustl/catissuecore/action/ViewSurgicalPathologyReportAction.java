package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ViewSurgicalPathologyReportForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.IdentifiedSurgicalPathologyReportBizLogic;
import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.caties.util.ViewSPRUtil;
import edu.wustl.catissuecore.client.CaCoreAppServicesDelegator;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.PathologyReportReviewParameter;
import edu.wustl.catissuecore.domain.pathology.QuarantineEventParameter;
import edu.wustl.catissuecore.domain.pathology.SurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.TextContent;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.authorization.domainobjects.Role;
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
		if(id!=null && id!=0 && operation.equalsIgnoreCase(Constants.VIEW_SURGICAL_PATHOLOGY_REPORT))
        {
            retrieveAndSetObject(pageOf, id, request, viewSPR);
        }
		viewSPR.setHasAccess(isAuthorized(getSessionBean(request)));
        request.setAttribute(Constants.PAGEOF, pageOf);
        request.setAttribute(Constants.OPERATION, Constants.VIEW_SURGICAL_PATHOLOGY_REPORT);
        request.setAttribute(Constants.REQ_PATH, "");
        request.setAttribute(Constants.SUBMITTED_FOR, submittedFor);
        request.setAttribute(Constants.FORWARD_TO, forwardTo);
        if(pageOf.equalsIgnoreCase(Constants.PAGEOF_NEW_SPECIMEN))
        {
        	request.setAttribute(Constants.ID,id.toString());
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
	private void retrieveAndSetObject(String pageOf, long id, HttpServletRequest request, ViewSurgicalPathologyReportForm viewSPR) throws DAOException
	{
		Long identifiedReportId=id;
		
		if(pageOf.equalsIgnoreCase(Constants.PAGEOF_PARTICIPANT) || pageOf.equalsIgnoreCase(Constants.PAGE_OF_PARTICIPANT_CP_QUERY))
		{
			
			Long participantId=getParticipantId(identifiedReportId);
			ParticipantBizLogic bizLogic=(ParticipantBizLogic)BizLogicFactory.getInstance().getBizLogic(Participant.class.getName());
			List scgList=bizLogic.getSCGList(participantId);

			viewSPR.setReportIdList(getReportIdList(scgList));
		}

		if(identifiedReportId!=null)
		{
			IdentifiedSurgicalPathologyReportBizLogic bizLogic=(IdentifiedSurgicalPathologyReportBizLogic)BizLogicFactory.getInstance().getBizLogic(IdentifiedSurgicalPathologyReport.class.getName());
			IdentifiedSurgicalPathologyReport identifiedReport=new IdentifiedSurgicalPathologyReport();
			identifiedReport.setId(identifiedReportId);
			try
			{
				bizLogic.populateUIBean(IdentifiedSurgicalPathologyReport.class.getName(), identifiedReport.getId(), viewSPR);
				filterObjects(identifiedReportId, viewSPR, request);
				DeidentifiedSurgicalPathologyReport deidReport=(DeidentifiedSurgicalPathologyReport)bizLogic.retrieveAttribute(IdentifiedSurgicalPathologyReport.class.getName(), identifiedReport.getId(), Constants.COLUMN_NAME_DEID_REPORT);
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
		List objectList=null;
		String witnessFullName=null;
		DefaultBizLogic defaultBizLogic=new DefaultBizLogic();
		String pageOf = request.getParameter(Constants.PAGEOF);
		String colName = new String(Constants.SYSTEM_IDENTIFIER);
		if(pageOf.equalsIgnoreCase(Constants.PAGEOF_REVIEW_SPR))
		{
			request.setAttribute(Constants.OPERATION, Constants.REVIEW);
			viewSPR.setOperation(Constants.REVIEW);
			bizLogic =BizLogicFactory.getInstance().getBizLogic(Constants.PATHOLOGY_REPORT_REVIEW_FORM_ID);
			objectList= bizLogic.retrieve(PathologyReportReviewParameter.class.getName(), colName, identifier);
			PathologyReportReviewParameter pathologyReportReviewParameter = (PathologyReportReviewParameter)objectList.get(0);
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
			objectList  = bizLogic.retrieve(QuarantineEventParameter.class.getName(), colName, identifier);
			QuarantineEventParameter quarantineEventParameter =(QuarantineEventParameter)objectList.get(0);
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
	 * 
	 * @param identifiedReportId
	 * @param viewSPR
	 * @param request
	 * @throws Exception
	 */
	public void filterObjects(Long identifiedReportId, ViewSurgicalPathologyReportForm viewSPR, HttpServletRequest request) throws Exception
	{
		//For PHI
		SessionDataBean sessionDataBean=(SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA);
		CaCoreAppServicesDelegator caCoreAppServicesDelegator = new CaCoreAppServicesDelegator();
		String userName = edu.wustl.common.util.Utility.toString(sessionDataBean.getUserName());
		
		DefaultBizLogic defaultBizLogic=new DefaultBizLogic();
		List reportList=(List)defaultBizLogic.retrieve(IdentifiedSurgicalPathologyReport.class.getName(), Constants.SYSTEM_IDENTIFIER, identifiedReportId);
		IdentifiedSurgicalPathologyReport identifiedReport=(IdentifiedSurgicalPathologyReport)reportList.get(0);
		TextContent textContent=null;
		if(identifiedReport.getTextContent()!=null)
		{
			textContent=(TextContent)defaultBizLogic.retrieveAttribute(IdentifiedSurgicalPathologyReport.class.getName(), identifiedReportId, Constants.COLUMN_NAME_TEXT_CONTENT);
			identifiedReport.setTextContent(textContent);
			List<IdentifiedSurgicalPathologyReport> identifiedReportList=new ArrayList<IdentifiedSurgicalPathologyReport>();
			identifiedReportList.add(identifiedReport);
			identifiedReportList=caCoreAppServicesDelegator.delegateSearchFilter(userName, identifiedReportList);
			if(identifiedReport.getTextContent().getData()==null)
			{
				viewSPR.setIdentifiedReportTextContent(Constants.HASHED_OUT);
				viewSPR.setSurgicalPathologyNumber(Constants.HASHED_OUT);
				viewSPR.setIdentifiedReportSite(Constants.HASHED_OUT);
			}
			
		}
		Long participantId=getParticipantId(identifiedReportId);
		List participantList=(List)defaultBizLogic.retrieve(Participant.class.getName(), Constants.SYSTEM_IDENTIFIER, participantId);
		participantList=caCoreAppServicesDelegator.delegateSearchFilter(userName, participantList);
		Participant participant=(Participant)participantList.get(0);
		if(participant.getFirstName()==null && participant.getLastName()==null)
		{
			viewSPR.setParticipantName(Constants.HASHED_OUT);
			if(participant.getBirthDate()==null)
			{
				viewSPR.setBirthDate(Constants.HASHED_OUT);
			}
			if(participant.getSocialSecurityNumber()==null)
			{
				viewSPR.setSocialSecurityNumber(Constants.HASHED_OUT);
			}
		}
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
	private boolean isAuthorized(SessionDataBean sessionBean) throws Exception
	{
		SecurityManager sm=SecurityManager.getInstance(this.getClass());
		try
		{
			Role role=sm.getUserRole(sessionBean.getUserId());
			if(role.getName().equalsIgnoreCase(Constants.ADMINISTRATOR))
			{
				return true;
			}		
		}
		catch(SMException ex)
		{
			Logger.out.info("Reviewer's Role not found!");
		}
		return false;
	}
}




