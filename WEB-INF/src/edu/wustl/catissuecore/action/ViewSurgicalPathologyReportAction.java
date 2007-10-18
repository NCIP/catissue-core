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
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.PathologyReportReviewParameter;
import edu.wustl.catissuecore.domain.pathology.QuarantineEventParameter;
import edu.wustl.catissuecore.domain.pathology.SurgicalPathologyReport;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;
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
		if(id!=null && id!=0 && operation.equalsIgnoreCase(Constants.VIEW_SURGICAL_PATHOLOGY_REPORT))
        {
            retrieveAndSetObject(pageOf, id, request);
        }
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
	private void retrieveAndSetObject(String pageOf, long id, HttpServletRequest request) throws DAOException
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
			DefaultBizLogic defaultBizLogic=new DefaultBizLogic();
			IdentifiedSurgicalPathologyReport identifiedReport=new IdentifiedSurgicalPathologyReport();
			identifiedReport.setId(identifiedReportId);
			try
			{
				defaultBizLogic.populateUIBean(IdentifiedSurgicalPathologyReport.class.getName(), identifiedReport.getId(), viewSPR);
				DeidentifiedSurgicalPathologyReport deidReport=(DeidentifiedSurgicalPathologyReport)defaultBizLogic.retrieveAttribute(IdentifiedSurgicalPathologyReport.class.getName(), identifiedReport.getId(), Constants.COLUMN_NAME_DEID_REPORT);
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
			if(obj[1]!=null || !((String)obj[1]).equals(""))
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
}




