package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ViewSurgicalPathologyReportForm;
import edu.wustl.catissuecore.bean.ConceptHighLightingBean;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.caties.util.ViewSPRUtil;
import edu.wustl.catissuecore.client.CaCoreAppServicesDelegator;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.pathology.Concept;
import edu.wustl.catissuecore.domain.pathology.ConceptReferent;
import edu.wustl.catissuecore.domain.pathology.ConceptReferentClassification;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.PathologyReportReviewParameter;
import edu.wustl.catissuecore.domain.pathology.QuarantineEventParameter;
import edu.wustl.catissuecore.domain.pathology.SurgicalPathologyReport;
import edu.wustl.catissuecore.reportloader.ReportLoaderUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
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

	private ViewSurgicalPathologyReportForm viewSPR;
	/**
	 * @see edu.wustl.common.action.BaseAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		viewSPR=(ViewSurgicalPathologyReportForm)form;
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
			retriveFromReportId(identifier, request);
		}
        boolean isAuthorized;
		if(id!=null&&id!=0 && operation.equalsIgnoreCase(Constants.VIEW_SURGICAL_PATHOLOGY_REPORT))
        {
            isAuthorized=isAuthorized(getSessionBean(request));
            retrieveAndSetObject(pageOf,id,isAuthorized, request);
            //retrieveAndSetObject(pageOf,id,request);
        }
        request.setAttribute(Constants.PAGEOF, pageOf);
        request.setAttribute(Constants.OPERATION, Constants.VIEW_SURGICAL_PATHOLOGY_REPORT);
        request.setAttribute(Constants.REQ_PATH, "");
        request.setAttribute(Constants.SUBMITTED_FOR, submittedFor);
        request.setAttribute(Constants.FORWARD_TO, forwardTo);
        if(pageOf.equalsIgnoreCase(Constants.PAGEOF_SPECIMEN))
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
	private void retrieveAndSetObject(String pageOf,long id,boolean isAuthorized, HttpServletRequest request) throws DAOException, BizLogicException
	{
		String className;
		String colName=new String(Constants.SYSTEM_IDENTIFIER);
		long colValue=id;	
		DefaultBizLogic defaultBizLogic=new DefaultBizLogic();
		SpecimenCollectionGroup specimenCollectionGroup=null;
		
		//For PHI
		//SessionDataBean sessionDataBean=(SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA);
		//CaCoreAppServicesDelegator caCoreAppServicesDelegator = new CaCoreAppServicesDelegator();
		//String userName = Utility.toString(sessionDataBean.getUserName());		
		
		//if page is of Specimen Collection group then the domain object is SpecimenCollectionGroup
		if(pageOf.equalsIgnoreCase(Constants.PAGEOF_SPECIMEN_COLLECTION_GROUP)|| pageOf.equalsIgnoreCase(Constants.PAGE_OF_SCG_CP_QUERY))
		{
			className=SpecimenCollectionGroup.class.getName();
			List scgList=defaultBizLogic.retrieve(className, colName, colValue);
			
			//Passing scg list to the filter
			//List scgObjList = new ArrayList();
			//try
			//{
			//	scgObjList = caCoreAppServicesDelegator.delegateSearchFilter(userName,scgList);
			//}
			//catch (Exception e)
			//{
			//	Logger.out.debug(""+e);
			//}
			//SpecimenCollectionGroup scg=(SpecimenCollectionGroup)scgObjList.get(0);
			specimenCollectionGroup=(SpecimenCollectionGroup)scgList.get(0);
			
			//IdentifiedSurgicalPathologyReport identifiedReport = scg.getIdentifiedSurgicalPathologyReport();			
			//if(identifiedReport.getId() == null)
			//{
			//	Participant participant = identifiedReport.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getParticipant();
			//	participant.setId(null);
			//	identifiedReport.getSpecimenCollectionGroup().getCollectionProtocolRegistration().setParticipant(participant);												
			//}
			
			//viewSPR.setDeIdentifiedReport(scg.getDeIdentifiedSurgicalPathologyReport());
			//viewSPR.setAllValues(identifiedReport);
						
			// For Category HighLighter
	//		  prepareCategoryPage(request,scg.getDeIdentifiedSurgicalPathologyReport());
		}
		//if page is of Specimen then the domain object is Specimen
		else if(pageOf.equalsIgnoreCase(Constants.PAGEOF_SPECIMEN) || pageOf.equalsIgnoreCase(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
		{
			className=Specimen.class.getName();
			List specimenList=defaultBizLogic.retrieve(className, colName, colValue);
			//For PHI
			//List specimenObjList = new ArrayList();
			//try
			//{
			//	specimenObjList = caCoreAppServicesDelegator.delegateSearchFilter(userName,specimenList);
			//}
			//catch (Exception e)
			//{
			//	Logger.out.debug(""+e);
			//}
			
			//Specimen specimen=(Specimen)specimenObjList.get(0);
			Specimen specimen=(Specimen)specimenList.get(0);
			specimenCollectionGroup=specimen.getSpecimenCollectionGroup();
			
			//IdentifiedSurgicalPathologyReport identifiedReport = scg.getIdentifiedSurgicalPathologyReport();			
			//if(identifiedReport.getId() == null)
			//{
			//	Participant participant = identifiedReport.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getParticipant();
			//	participant.setId(null);
			//	identifiedReport.getSpecimenCollectionGroup().getCollectionProtocolRegistration().setParticipant(participant);												
			//}			
			
			//viewSPR.setAllValues(identifiedReport);
			//viewSPR.setDeIdentifiedReport(scg.getDeIdentifiedSurgicalPathologyReport());
			
//			 For Category HighLighter
//			  prepareCategoryPage(request,scg.getDeIdentifiedSurgicalPathologyReport());			
		}
		// if page is of Participant then the domain object is Participant
		// Also needs to retrieve a list of SurgicalPathologyReport objects (One-to-Many relationship)
		else if(pageOf.equalsIgnoreCase(Constants.PAGEOF_PARTICIPANT) || pageOf.equalsIgnoreCase(Constants.PAGE_OF_PARTICIPANT_CP_QUERY))
		{
			className=Participant.class.getName();
			List participantList=defaultBizLogic.retrieve(className, colName, colValue);
			Participant participant=(Participant)participantList.get(0);
//			viewSPR.setParticipant(participant);
			List scgList=ReportLoaderUtil.getSCGList(participant);
			//For PHI
			//List scgObjList = new ArrayList();
			//try
			//{
			//	scgObjList = caCoreAppServicesDelegator.delegateSearchFilter(userName,scgList);
			//}
			//catch (Exception e)
			//{
			//	Logger.out.debug(""+e);
			//}
						
//			if(scgObjList.size()>0)
//			{
//				SpecimenCollectionGroup scg=(SpecimenCollectionGroup)scgObjList.get(0);
			if(scgList!=null && scgList.size()>0)
			{
				specimenCollectionGroup=(SpecimenCollectionGroup)scgList.get(0);
			}
			
				//IdentifiedSurgicalPathologyReport identifiedReport = scg.getIdentifiedSurgicalPathologyReport();				
				//if(identifiedReport.getId() == null)
				//{
				//	Participant participantFrmScg = identifiedReport.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getParticipant();
				//	participantFrmScg.setId(null);
				//	identifiedReport.getSpecimenCollectionGroup().getCollectionProtocolRegistration().setParticipant(participantFrmScg);												
				//}			
				
				//viewSPR.setAllValues(identifiedReport);
				//viewSPR.setDeIdentifiedReport(scg.getDeIdentifiedSurgicalPathologyReport());
				
//				 For Category HighLighter
//			prepareCategoryPage(request,scg.getDeIdentifiedSurgicalPathologyReport());
			viewSPR.setReportIdList(getReportIdList(scgList));
		}
//		else
//		{
//			String requestFor=(String)request.getParameter(Constants.REQUEST_FOR);
//			IBizLogic bizLogic=null;
//			List objectList=null;
//			String witnessFullName=null;
//			colName = new String(Constants.SYSTEM_IDENTIFIER);
//			if(requestFor!=null &&requestFor.equals(Constants.REVIEW))
//			{
//				bizLogic =BizLogicFactory.getInstance().getBizLogic(Constants.PATHOLOGY_REPORT_REVIEW_FORM_ID);
//				objectList= bizLogic.retrieve(PathologyReportReviewParameter.class.getName(), colName,id);
//				PathologyReportReviewParameter pathologyReportReviewParameter = (PathologyReportReviewParameter)objectList.get(0);
//				viewSPR.setUserComments(pathologyReportReviewParameter.getComments());
//				witnessFullName = pathologyReportReviewParameter.getUser().getFirstName()+", "+pathologyReportReviewParameter.getUser().getLastName()+"'s";
//				viewSPR.setUserName(witnessFullName);
//				SurgicalPathologyReport surgicalPathologyReport = pathologyReportReviewParameter.getSurgicalPathologyReport();
//				try
//				{
//					DeidentifiedSurgicalPathologyReport deidentifiedSurgicalPathologyReport =(DeidentifiedSurgicalPathologyReport)surgicalPathologyReport;
//					viewSPR.setAllValues(deidentifiedSurgicalPathologyReport.getSpecimenCollectionGroup().getIdentifiedSurgicalPathologyReport());
//				}
//				catch(ClassCastException e) 
//				{
//					IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport =(IdentifiedSurgicalPathologyReport)surgicalPathologyReport;
//					viewSPR.setAllValues(identifiedSurgicalPathologyReport);
//				}
//			}
//			else
//			{
//				bizLogic =BizLogicFactory.getInstance().getBizLogic(Constants.QUARANTINE_EVENT_PARAMETER_FORM_ID);
//				objectList  = bizLogic.retrieve(QuarantineEventParameter.class.getName(), colName, id);
//				QuarantineEventParameter quarantineEventParameter =(QuarantineEventParameter)objectList.get(0);
//				viewSPR.setUserComments(quarantineEventParameter.getComments());
//				witnessFullName = quarantineEventParameter.getUser().getLastName()+", "+quarantineEventParameter.getUser().getFirstName();
//				viewSPR.setUserName(witnessFullName);
//				DeidentifiedSurgicalPathologyReport deidentifiedSurgicalPathologyReport =quarantineEventParameter.getDeIdentifiedSurgicalPathologyReport();
//				viewSPR.setAllValues(deidentifiedSurgicalPathologyReport.getSpecimenCollectionGroup().getIdentifiedSurgicalPathologyReport());
//			}
		if(specimenCollectionGroup!=null)
		{
			try
			{
				defaultBizLogic.populateUIBean(SpecimenCollectionGroup.class.getName(), specimenCollectionGroup.getId(), viewSPR);
				DeidentifiedSurgicalPathologyReport deidReport=(DeidentifiedSurgicalPathologyReport)defaultBizLogic.retrieveAttribute(SpecimenCollectionGroup.class.getName(), specimenCollectionGroup.getId(), Constants.COLUMN_NAME_DEID_REPORT);
				List conceptBeanList=ViewSPRUtil.getConceptBeanList(request,deidReport);
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
		List reportIDList=new ArrayList();
		SpecimenCollectionGroup scg;
		for(int i=0;i<scgList.size();i++)
		{
			scg=(SpecimenCollectionGroup)scgList.get(i);
			if(scg.getIdentifiedSurgicalPathologyReport()!=null)
			{
				NameValueBean nb=new NameValueBean(scg.getSurgicalPathologyNumber(),scg.getIdentifiedSurgicalPathologyReport().getId().toString());
				reportIDList.add(nb);
			}		
		}
		return reportIDList;
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
			if(role.getName().equalsIgnoreCase(Constants.ROLE_ADMINISTRATOR))
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
	
	/**
	 * Adding name,value pair in NameValueBean for Witness Name
	 * @param collProtId Get Witness List for this ID
	 * @return consentWitnessList
	 */ 
	public void retriveFromReportId(Long identifier,HttpServletRequest request) throws DAOException
	{   	
		IBizLogic bizLogic=null;
		List objectList=null;
		String witnessFullName=null;
		String pageOf = request.getParameter(Constants.PAGEOF);
		String colName = new String(Constants.SYSTEM_IDENTIFIER);
		if(pageOf.equalsIgnoreCase(Constants.REVIEW_SPR))
		{
			
			bizLogic =BizLogicFactory.getInstance().getBizLogic(Constants.PATHOLOGY_REPORT_REVIEW_FORM_ID);
			objectList= bizLogic.retrieve(PathologyReportReviewParameter.class.getName(), colName, identifier);
			PathologyReportReviewParameter pathologyReportReviewParameter = (PathologyReportReviewParameter)objectList.get(0);
			viewSPR.setUserComments(pathologyReportReviewParameter.getComment());
			witnessFullName = pathologyReportReviewParameter.getUser().getFirstName()+", "+pathologyReportReviewParameter.getUser().getLastName()+"'s";
			viewSPR.setUserName(witnessFullName);
			SurgicalPathologyReport surgicalPathologyReport = pathologyReportReviewParameter.getSurgicalPathologyReport();
			try
			{
				DeidentifiedSurgicalPathologyReport deidentifiedSurgicalPathologyReport =(DeidentifiedSurgicalPathologyReport)surgicalPathologyReport;
				viewSPR.setAllValues(deidentifiedSurgicalPathologyReport.getSpecimenCollectionGroup().getIdentifiedSurgicalPathologyReport());
			}
			catch(ClassCastException e) 
			{
				IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport =(IdentifiedSurgicalPathologyReport)surgicalPathologyReport;
				viewSPR.setAllValues(identifiedSurgicalPathologyReport);
			}
		}
		else
		{
			bizLogic =BizLogicFactory.getInstance().getBizLogic(Constants.QUARANTINE_EVENT_PARAMETER_FORM_ID);
			objectList  = bizLogic.retrieve(QuarantineEventParameter.class.getName(), colName, identifier);
			QuarantineEventParameter quarantineEventParameter =(QuarantineEventParameter)objectList.get(0);
			viewSPR.setUserComments(quarantineEventParameter.getComment());
			witnessFullName = quarantineEventParameter.getUser().getLastName()+", "+quarantineEventParameter.getUser().getFirstName();
			viewSPR.setUserName(witnessFullName);
			DeidentifiedSurgicalPathologyReport deidentifiedSurgicalPathologyReport =quarantineEventParameter.getDeIdentifiedSurgicalPathologyReport();
			viewSPR.setAllValues(deidentifiedSurgicalPathologyReport.getSpecimenCollectionGroup().getIdentifiedSurgicalPathologyReport());
		}
	}
}




