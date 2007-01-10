package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ViewSurgicalPathologyReportForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.IdentifiedSurgicalPathologyReportBizLogic;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.PathologyReportReviewParameterSet;
import edu.wustl.catissuecore.domain.pathology.QuarantineEventParameterSet;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.dbManager.DAOException;


public class ViewSurgicalPathologyReportAction extends BaseAction
{

	private String forward;
	private ViewSurgicalPathologyReportForm viewSPR;
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		viewSPR=(ViewSurgicalPathologyReportForm)form;
		String pageOf = request.getParameter(Constants.PAGEOF);
		String operation = (String)request.getParameter(Constants.OPERATION);
		String submittedFor="";
		Long id=new Long((String)request.getParameter(Constants.SYSTEM_IDENTIFIER));
		HttpSession session=request.getSession();
		viewSPR.setId(id);
		if(operation.equalsIgnoreCase(Constants.VIEW_SURGICAL_PATHOLOGY_REPORT))
		{
			retrieveAndSetObject(pageOf,id);
		}
		if(operation.equalsIgnoreCase("submit"))
		{
			SessionDataBean sessionBean=getSessionData(request);
			submittedFor=request.getParameter(Constants.SUBMITTED_FOR);
			addComments(viewSPR, submittedFor, pageOf, id,sessionBean, new Long(request.getParameter("currentReportId")));
			retrieveAndSetObject(pageOf,id);
			viewSPR.setComments(new String("Comments added Successfully !"));
		}
		request.setAttribute(Constants.PAGEOF, pageOf);
		request.setAttribute(Constants.OPERATION, Constants.VIEW_SURGICAL_PATHOLOGY_REPORT);
		request.setAttribute(Constants.REQ_PATH, "");
		request.setAttribute(Constants.SUBMITTED_FOR, submittedFor);
		
		return mapping.findForward(forward);
	}
	
	private void retrieveAndSetObject(String pageOf,long id) throws DAOException
	{
		String className;
		String colName=new String(Constants.SYSTEM_IDENTIFIER);
		long colValue=id;
		
		DefaultBizLogic defaultBizLogic=new DefaultBizLogic();
		
		if(pageOf.equalsIgnoreCase(Constants.PAGEOF_SPECIMEN_COLLECTION_GROUP))
		{
			forward=new String(Constants.SPECIMEN_COLLECTION_GROUP);
			className=SpecimenCollectionGroup.class.getName();
			List scgList=defaultBizLogic.retrieve(className, colName, colValue);
			SpecimenCollectionGroup scg=(SpecimenCollectionGroup)scgList.get(0);
			viewSPR.setAllValues(scg.getIdentifiedSurgicalPathologyReport());
			viewSPR.setParticipant(scg.getParticipant());
		}
		if(pageOf.equalsIgnoreCase(Constants.PAGEOF_SPECIMEN))
		{
			forward=new String(Constants.SPECIMEN);
			className=Specimen.class.getName();
			List specimenList=defaultBizLogic.retrieve(className, colName, colValue);
			Specimen specimen=(Specimen)specimenList.get(0);
			SpecimenCollectionGroup scg=specimen.getSpecimenCollectionGroup();
			viewSPR.setAllValues(scg.getIdentifiedSurgicalPathologyReport());
			viewSPR.setParticipant(scg.getParticipant());
		}
		if(pageOf.equalsIgnoreCase(Constants.PAGEOF_PARTICIPANT))
		{
			forward=new String(Constants.SPECIMEN);
			className=Participant.class.getName();
			List participantList=defaultBizLogic.retrieve(className, colName, colValue);
			Participant participant=(Participant)participantList.get(0);
			viewSPR.setParticipant(participant);
			Collection scgCollection=participant.getSpecimenCollectionGroupCollection();
			
			Iterator iter=scgCollection.iterator();
			if(iter.hasNext())
			{
				colValue=(Long)iter.next();
				className=SpecimenCollectionGroup.class.getName();
				List scgList=defaultBizLogic.retrieve(className, colName, colValue);
				SpecimenCollectionGroup scg=(SpecimenCollectionGroup)scgList.get(0);
				viewSPR.setAllValues(scg.getIdentifiedSurgicalPathologyReport());
			}
			viewSPR.setReportId(getReportIdList(scgCollection));
		}
	}
	
	private List getReportIdList(Collection scgCollection)throws DAOException
	{
		Long[] scg=null;
		List reportIDList=null;
		
		String sourceObjectName=IdentifiedSurgicalPathologyReport.class.getName();
		String[] displayNameFields=new String[] {Constants.SYSTEM_IDENTIFIER};
		String valueField=new String(Constants.SYSTEM_IDENTIFIER);
		String[] whereColumnName = new String[]{"specimenCollectionGroup"};
		String[] whereColumnCondition = new String[]{"="};
		Object[] whereColumnValue = null;
		String joinCondition = null;
		String separatorBetweenFields = Constants.SEPARATOR;
		
		if(scgCollection!=null)
		{
			Iterator iter=scgCollection.iterator();
			scg=new Long[scgCollection.size()];
			int i=0;
			while(iter.hasNext())
			{
				scg[i++]=(Long)iter.next();
			}
		
		
			whereColumnValue=scg;
			BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();
			IdentifiedSurgicalPathologyReportBizLogic bizLogic =(IdentifiedSurgicalPathologyReportBizLogic) bizLogicFactory.getBizLogic(IdentifiedSurgicalPathologyReport.class.getName());
			reportIDList = bizLogic.getList(sourceObjectName, displayNameFields, valueField, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition, separatorBetweenFields, false);
		}
		return reportIDList;
	}
	
	private void addComments(ViewSurgicalPathologyReportForm form, String submittedFor, String pageOf, long id, SessionDataBean sessionBean, long currentReportId) throws DAOException
	{
		if(submittedFor.equalsIgnoreCase("review"))
		{
			saveReviewComment(getISPR(pageOf, id, currentReportId), sessionBean, form.getComments());
		}
		else if(submittedFor.equalsIgnoreCase("quarantine"))
		{
			saveQuarantineComment(getISPR(pageOf, id, currentReportId).getDeidentifiedSurgicalPathologyReport(), sessionBean, form.getComments());
		}	
	}
	
	
	private IdentifiedSurgicalPathologyReport getISPR(String pageOf, long id, long currentReportId)throws DAOException
	{
		IdentifiedSurgicalPathologyReport ispr=null;
		
		String className;
		String colName=new String(Constants.SYSTEM_IDENTIFIER);
		long colValue=id;
		PathologyReportReviewParameterSet reviewParam=new PathologyReportReviewParameterSet();
	
		DefaultBizLogic defaultBizLogic=new DefaultBizLogic();
		
		forward=new String(Constants.SPECIMEN_COLLECTION_GROUP);
		className=IdentifiedSurgicalPathologyReport.class.getName();
		List isprList=defaultBizLogic.retrieve(className, colName, currentReportId);
		ispr=(IdentifiedSurgicalPathologyReport)isprList.get(0);
		
		return ispr;
	}
	
	protected SessionDataBean getSessionData(HttpServletRequest request) 
	{
		Object obj = request.getSession().getAttribute(Constants.SESSION_DATA);
		if(obj!=null)
		{
			SessionDataBean sessionData = (SessionDataBean) obj;
			return  sessionData;
		}
		return null;
	}
	
	private void saveReviewComment(IdentifiedSurgicalPathologyReport ispr,SessionDataBean sessionBean, String comments) throws DAOException
	{
		
		String className;
		String colName=new String(Constants.SYSTEM_IDENTIFIER);
		
		PathologyReportReviewParameterSet reviewParam=new PathologyReportReviewParameterSet();
		DefaultBizLogic defaultBizLogic=new DefaultBizLogic();
		className=User.class.getName();
		List userList=defaultBizLogic.retrieve(className, colName, sessionBean.getUserId());
		reviewParam.setUser((User)userList.get(0));
		reviewParam.setComments(comments);
		reviewParam.setTimestamp(new Date());
		Set pathologyReportReviewParameterSetCollection=ispr.getPathologyReportReviewParameterSetCollection();
		pathologyReportReviewParameterSetCollection.add(reviewParam);
		ispr.setPathologyReportReviewParameterSetCollection(pathologyReportReviewParameterSetCollection);
		
	}
	
	private void saveQuarantineComment(DeidentifiedSurgicalPathologyReport dispr,SessionDataBean sessionBean, String comments) throws DAOException
	{
		String className;
		String colName=new String(Constants.SYSTEM_IDENTIFIER);
		
		QuarantineEventParameterSet quarantineParam=new QuarantineEventParameterSet();
		DefaultBizLogic defaultBizLogic=new DefaultBizLogic();
		className=User.class.getName();
		List userList=defaultBizLogic.retrieve(className, colName, sessionBean.getUserId());
		quarantineParam.setUser((User)userList.get(0));
		quarantineParam.setComments(comments);
		quarantineParam.setTimestamp(new Date());
		Set quarantinEventParameterSetCollection=dispr.getQuarantinEventParameterSetCollection();
		quarantinEventParameterSetCollection.add(quarantineParam);
		dispr.setQuarantinEventParameterSetCollection(quarantinEventParameterSetCollection);
	}
}


