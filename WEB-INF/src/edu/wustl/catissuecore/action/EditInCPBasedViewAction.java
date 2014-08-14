package edu.wustl.catissuecore.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.krishagni.catissueplus.core.privileges.PrivilegeType;

import edu.wustl.catissuecore.actionForm.CPSearchForm;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolRegistrationBizLogic;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.util.CDMSCaTissueIntegrationUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.CDMSIntegrationConstants;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.Utility;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;

public class EditInCPBasedViewAction extends BaseAction {
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
	            HttpServletRequest request, HttpServletResponse response) throws Exception
	    {
	        String objectToEdit= request.getParameter("pageOf"); 
	        final SessionDataBean sessionData=(SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA);
	        StringBuffer path= new StringBuffer();
	        String cpId = "";
	        if(Constants.PAGE_OF_SPECIMEN_COLLECTION_GROUP.equalsIgnoreCase(objectToEdit))
	    {
	       	String scgId = request.getParameter(CDMSIntegrationConstants.SCGID);
            if(scgId==null || "".equals(scgId))
            {
            	scgId = request.getParameter(Constants.SYSTEM_IDENTIFIER);
            }
	        String scgOperation =CDMSIntegrationConstants.ADD;
	        if(scgId!=null && !scgId.equals("null") && !scgId.equals("0"))
	        {
	            scgOperation =CDMSIntegrationConstants.EDIT;
	        }
	        if(scgOperation!=null && scgOperation.equals(CDMSIntegrationConstants.ADD))
	        {/*Get ALREADY CREATED ANTICIAPTORY SCGS - THE RECENT ONE*/
	            cpId=request.getParameter(CDMSIntegrationConstants.COLLECTION_PROTOCOL_ID);
	            final String visitNum=request.getParameter(CDMSIntegrationConstants.VISIT_NUMBER);
	            final String participantId=request.getParameter(CDMSIntegrationConstants.PARTICIPANT_ID);
	            final String collectionEventId=request.getParameter(CDMSIntegrationConstants.COLL_PROTOCOL_EVENT_ID);
	            String visitId=request.getParameter(CDMSIntegrationConstants.EVENTENTRYID);
	            String previousScgDateString = request.getParameter(CDMSIntegrationConstants.PREV_SCG_DATE);
	            String recentScgDateString = request.getParameter(CDMSIntegrationConstants.RECENT_SCG_DATE);
	            DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd-hh.mm.ss");
	            Date recentScgDate = null, previousScgDate = null; 
	            if(previousScgDateString != null)
	            {
	            	previousScgDate = dateformat.parse(previousScgDateString);
	            }
	            if(recentScgDateString != null)
	            {
	            	recentScgDate = dateformat.parse(recentScgDateString);
	            }
	            if(visitId == null)
	            {
	                visitId=(String)request.getSession().getAttribute(CDMSIntegrationConstants.EVENTENTRYID);
	            }
	            //scgId= new CDMSCaTissueIntegrationUtil().getRecentSCG(sessionData.getUserName(), Utility.toLong(cpId), Utility.toLong(participantId), Utility.toLong(collectionEventId),visitNum, Utility.toLong(visitId));
	            scgId= new CDMSCaTissueIntegrationUtil().getRecentSCG(Utility.toLong(cpId), 
	            		Utility.toLong(participantId), Utility.toLong(collectionEventId),
	            		previousScgDate, recentScgDate);
	            if(scgId!=null)
	            {
	                scgOperation=CDMSIntegrationConstants.EDIT;
	            }
	            path.append("&URLCollectionProtocolId=").append(cpId).append("&URLParticipantId=").append(participantId);
	            path.append("&URLCollectionEventId=").append(collectionEventId).append("&URLId=").append(Constants.SPECIMEN_COLLECTION_GROUP).append("_").append(scgId).append('&');
	            path.append(CDMSIntegrationConstants.OPERATION).append('=').append(scgOperation);
//	            path="&URLCollectionProtocolId="+cpId+"&URLParticipantId="+participantId+"&URLCollectionEventId="+collectionEventId+"&URLId="+scgId+"&"+ClinPortalIntegrationConstants.OPERATION+"="+scgOperation;
	        }
	        else
	        {
	            List ids=CDMSCaTissueIntegrationUtil.getCPBasedViewInfo(scgId);
	            if(!ids.isEmpty())
	            {
	                final Object[] id= (Object[]) ids.get(0);
	                cpId = id[0].toString();
	                path.append("&cpId=").append(id[0].toString()).append("&participantId=").append(id[2].toString()).append(",").append(id[3].toString());
                  path.append("&scgId=").append(scgId);
//	                path.append("&URLCollectionProtocolId=").append(id[0].toString()).append("&URLParticipantId=").append(id[2].toString());
//	                path.append("&URLCollectionEventId=").append(id[1].toString()).append("&URLId=").append(Constants.SPECIMEN_COLLECTION_GROUP).append("_").append(scgId).append('&');
//	                path.append(CDMSIntegrationConstants.OPERATION).append('=').append(scgOperation);
	         //       path=path+"&URLCollectionProtocolId="+id[0].toString()+"&URLParticipantId="+id[2].toString()+"&URLCollectionEventId="+id[1].toString()+"&URLId="+scgId+"&"+ClinPortalIntegrationConstants.OPERATION+"="+scgOperation;
	            }
	        }
	        populateCPSearchForm(request);
	    }
	    else
	     if("pageOfNewSpecimen".equalsIgnoreCase(objectToEdit))
	     {
	    	 String specimenid = request.getParameter(Constants.SYSTEM_IDENTIFIER);
	    	 NewSpecimenBizLogic specimenBizlogic = new NewSpecimenBizLogic();
	         List<Object> list = specimenBizlogic.getcpIdandPartId(sessionData, specimenid);
	     			if(!list.isEmpty()) 
	                {
	                    final Object[] id= (Object[]) list.get(0);
	                    cpId = id[0].toString();
	                    path.append("&cpId=").append(id[0].toString()).append("&participantId=").append(id[1].toString()).append(",").append(id[3].toString());
	                    path.append("&scgId=").append(id[2].toString());
	                    path.append("&specimenId=").append(specimenid);
//	                    path.append("&URLCollectionProtocolId=").append(id[0].toString()).append("&URLParticipantId=").append(id[1].toString());
//	                    path.append("&URLId=").append(Constants.SPECIMEN+"_"+specimenid);
	                }   
	     }
	     else if("pageOfCollectionProtocolRegistration".equalsIgnoreCase(objectToEdit))
	     { 
	    	 String cprId = request
	 				.getParameter(Constants.SYSTEM_IDENTIFIER);
	 		CollectionProtocolRegistrationBizLogic cprBizLogic = new CollectionProtocolRegistrationBizLogic();
	 		List<Object> list = cprBizLogic.getCPIdandPartId(sessionData, cprId);
	 		if (!list.isEmpty()) {
	 			final Object[] id = (Object[]) list.get(0);
	 			cpId = id[0].toString();
	 		 path.append("&cpId=").append(id[0].toString()).append("&participantId=").append(id[1].toString()).append(",").append(id[2].toString());
	 		 
//	 			path.append("&URLCollectionProtocolId=").append(id[0].toString())
//	 					.append("&URLParticipantId=").append(id[1].toString());
	 		}
	    	 
	     }
	        
	     ActionForward actionForward=mapping.findForward(CDMSIntegrationConstants.CP_BASED_VIEW);
	     String path1 = actionForward.getPath();
	     final ActionForward newActionForward = new ActionForward();
	     newActionForward.setName(actionForward.getName());
	     newActionForward.setRedirect(false);
	     newActionForward.setContextRelative(false);
	     if(hasSpecimenProcessingPriv(sessionData, cpId)){
	     newActionForward.setPath(path1+path.toString());
	     }
	     else{
	    	 newActionForward.setPath(path1);
	     }
	     
	    actionForward = newActionForward;
	        return actionForward;
	    }

	private boolean hasSpecimenProcessingPriv(final SessionDataBean sessionData, String cpId) throws SMException {
		boolean isAuthorize = sessionData.isAdmin();
		if (!isAuthorize)
		{ 

			final PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(sessionData.getUserName());
			String cpProtectionEleName = CollectionProtocol.class.getName() + "_" + cpId;
			// Checking whether the logged in user has the required
			// privilege on the given protection element
			isAuthorize = privilegeCache.hasPrivilege(cpProtectionEleName, PrivilegeType.SPECIMEN_PROCESSING.toString());
			
			if(!isAuthorize)
			{   
				isAuthorize = AppUtility.checkForAllCurrentAndFutureCPs(PrivilegeType.SPECIMEN_PROCESSING.toString(), sessionData, cpId);
			}
		}
		return isAuthorize;
	}

	    /**
	     *
	     * @param request
	     */
	    private void populateCPSearchForm(final HttpServletRequest request)
	    {
	       final String cpId=request.getParameter(CDMSIntegrationConstants.COLLECTION_PROTOCOL_ID);
	       final String participantId=request.getParameter(CDMSIntegrationConstants.PARTICIPANT_ID);
	       final CPSearchForm cpSearchForm = new CPSearchForm();
	      if(cpId!=null && !cpId.isEmpty() && participantId!=null && !participantId.isEmpty())
	      {
	       cpSearchForm.setCpId(Long.valueOf(cpId));
	       cpSearchForm.setParticipantId(Long.valueOf(participantId));
	      }
	    }
	
}
