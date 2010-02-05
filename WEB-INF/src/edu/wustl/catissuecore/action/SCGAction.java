package edu.wustl.catissuecore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CPSearchForm;
import edu.wustl.catissuecore.util.ClinPortalCaTissueIntegrationUtil;
import edu.wustl.catissuecore.util.global.ClinPortalIntegrationConstants;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.Utility;


public class SCGAction extends BaseAction
{
    protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        String scgId = request.getParameter(ClinPortalIntegrationConstants.SCGID);
        final SessionDataBean sessionData=(SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA);

        String scgOperation =ClinPortalIntegrationConstants.ADD;
        if(scgId!=null && !scgId.equals("null") && !scgId.equals("0"))
        {
            scgOperation =ClinPortalIntegrationConstants.EDIT;
        }
        final StringBuffer path= new StringBuffer();
        if(scgOperation!=null && scgOperation.equals(ClinPortalIntegrationConstants.ADD))
        {/*Get ALREADY CREATED ANTICIAPTORY SCGS - THE RECENT ONE*/
            final String cpId=request.getParameter(ClinPortalIntegrationConstants.COLLECTION_PROTOCOL_ID);
            final String visitNum=request.getParameter(ClinPortalIntegrationConstants.VISIT_NUMBER);
            final String participantId=request.getParameter(ClinPortalIntegrationConstants.PARTICIPANT_ID);
            final String collectionEventId=request.getParameter(ClinPortalIntegrationConstants.COLL_PROTOCOL_EVENT_ID);
            String visitId=request.getParameter(ClinPortalIntegrationConstants.EVENTENTRYID);
            if(visitId==null)
            {
                visitId=(String)request.getSession().getAttribute(ClinPortalIntegrationConstants.EVENTENTRYID);
            }
            scgId= new ClinPortalCaTissueIntegrationUtil().getRecentSCG(sessionData.getUserName(), Utility.toLong(cpId), Utility.toLong(participantId), Utility.toLong(collectionEventId),visitNum, Utility.toLong(visitId));
            if(scgId!=null)
            {
                scgOperation=ClinPortalIntegrationConstants.EDIT;
            }
            path.append("&URLCollectionProtocolId=").append(cpId).append("&URLParticipantId=").append(participantId);
            path.append("&URLCollectionEventId=").append(collectionEventId).append("&URLId=").append(scgId).append('&');
            path.append(ClinPortalIntegrationConstants.OPERATION).append('=').append(scgOperation);
//            path="&URLCollectionProtocolId="+cpId+"&URLParticipantId="+participantId+"&URLCollectionEventId="+collectionEventId+"&URLId="+scgId+"&"+ClinPortalIntegrationConstants.OPERATION+"="+scgOperation;
        }
        else
        {
            List ids=ClinPortalCaTissueIntegrationUtil.getCPBasedViewInfo(scgId);
            if(!ids.isEmpty())
            {
                final Object[] id= (Object[]) ids.get(0);
                path.append("&URLCollectionProtocolId=").append(id[0].toString()).append("&URLParticipantId=").append(id[2].toString());
                path.append("&URLCollectionEventId=").append(id[1].toString()).append("&URLId=").append(scgId).append('&');
                path.append(ClinPortalIntegrationConstants.OPERATION).append('=').append(scgOperation);
         //       path=path+"&URLCollectionProtocolId="+id[0].toString()+"&URLParticipantId="+id[2].toString()+"&URLCollectionEventId="+id[1].toString()+"&URLId="+scgId+"&"+ClinPortalIntegrationConstants.OPERATION+"="+scgOperation;
            }
        }
        populateCPSearchForm(request);
        ActionForward actionForward=mapping.findForward(ClinPortalIntegrationConstants.CP_BASED_VIEW);
        final ActionForward newActionForward = new ActionForward();
        newActionForward.setName(actionForward.getName());
        newActionForward.setRedirect(false);
        newActionForward.setContextRelative(false);
        newActionForward.setPath(actionForward.getPath()+path.toString());
        actionForward = newActionForward;
        return actionForward;
    }

    /**
     *
     * @param request
     */
    private void populateCPSearchForm(final HttpServletRequest request)
    {
       final String cpId=request.getParameter(ClinPortalIntegrationConstants.COLLECTION_PROTOCOL_ID);
       final String participantId=request.getParameter(ClinPortalIntegrationConstants.PARTICIPANT_ID);
       final CPSearchForm cpSearchForm = new CPSearchForm();
       cpSearchForm.setCpId(Long.valueOf(cpId));
       cpSearchForm.setParticipantId(Long.valueOf(participantId));
    }



}
