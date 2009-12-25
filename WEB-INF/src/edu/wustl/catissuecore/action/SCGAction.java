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
import edu.wustl.common.action.BaseAction;


public class SCGAction extends BaseAction
{


    protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        String scgId = request.getParameter(ClinPortalIntegrationConstants.SCGID);
        String scgOperation =ClinPortalIntegrationConstants.ADD;
        if(scgId!=null && !scgId.equals("null") && !scgId.equals("0"))
        {
            scgOperation =ClinPortalIntegrationConstants.EDIT;
        }
        String path="";
        String pageOf=ClinPortalIntegrationConstants.ADD;
        if(scgOperation!=null && scgOperation.equals(ClinPortalIntegrationConstants.ADD))
        {/*Get ALREADY CREATED ANTICIAPTORY SCGS - THE RECENT ONE*/
            //collectionProtocolId=250&participantId=719398 and collectionEventId
            final String cpId=request.getParameter(ClinPortalIntegrationConstants.COLLECTION_PROTOCOL_ID);
            final String participantId=request.getParameter(ClinPortalIntegrationConstants.PARTICIPANT_ID);
            final String collectionEventId=request.getParameter(ClinPortalIntegrationConstants.COLL_PROTOCOL_EVENT_ID);
          //  scgId=new SpecimenCollectionGroupBizLogic().getRecentSCG(cpId, participantId,collectionEventId);
            path=path+"&collectionProtocolId="+cpId+"&participantId="+participantId+"&collectionEventId="+collectionEventId;


        }
        else
        {
            pageOf=ClinPortalIntegrationConstants.EDIT;
            List ids=ClinPortalCaTissueIntegrationUtil.getCPBasedViewInfo(scgId);
            if(!ids.isEmpty())
            {
            path=path+"&collectionProtocolId="+ids.get(0)+"&participantId="+ids.get(2)+"&collectionEventId="+ids.get(1);
            }
        }
      //  if(scgId!=null && !scgId.equals("null") && !scgId.equals("0"))
     //   {
           // path=path+"&id="+scgId;
     //   }

        populateCPSearchForm(request);
        ActionForward actionForward=mapping.findForward(pageOf);
        ActionForward newActionForward = new ActionForward();
        newActionForward.setName(actionForward.getName());
        newActionForward.setRedirect(false);
        newActionForward.setContextRelative(false);
        newActionForward.setPath(actionForward.getPath()+path);
        actionForward = newActionForward;
        return actionForward;
    }

    /**
     *
     * @param request
     */
    private void populateCPSearchForm(HttpServletRequest request)
    {
        final String cpId=request.getParameter(ClinPortalIntegrationConstants.COLLECTION_PROTOCOL_ID);
        final String participantId=request.getParameter(ClinPortalIntegrationConstants.PARTICIPANT_ID);
       CPSearchForm cpSearchForm = new CPSearchForm();
       cpSearchForm.setCpId(Long.valueOf(cpId));
       cpSearchForm.setParticipantId(Long.valueOf(participantId));
    }



}
