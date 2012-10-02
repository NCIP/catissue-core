package edu.wustl.catissuecore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.CDMSIntegrationConstants;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;


/**
 * @author rinku
 *
 */
public class URLSpecimenViewAction extends BaseAction
{
    protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        
     final StringBuffer path= new StringBuffer();
     final SessionDataBean sessionData=(SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA);
     String specimenid = request.getParameter("identifier");
     List<Object> list = getSpecimenInfo(sessionData, specimenid);
 			if(!list.isEmpty())
            {
                final Object[] id= (Object[]) list.get(0);
                path.append("&URLCollectionProtocolId=").append(id[0].toString()).append("&URLParticipantId=").append(id[1].toString());
                path.append("&URLId=").append(Constants.SPECIMEN+"_"+specimenid);
            }
        ActionForward actionForward=mapping.findForward(CDMSIntegrationConstants.CP_BASED_VIEW);
        final ActionForward newActionForward = new ActionForward();
        newActionForward.setName(actionForward.getName());
        newActionForward.setRedirect(false);
        newActionForward.setContextRelative(false);
        newActionForward.setPath(actionForward.getPath()+path.toString());
        actionForward = newActionForward;
        return actionForward;
    }

	/**
	 * @param sessionData
	 * @param specimenid
	 * @return
	 * @throws ApplicationException
	 * @throws DAOException
	 */
	private List<Object> getSpecimenInfo(final SessionDataBean sessionData,
			String specimenid) throws ApplicationException, DAOException {
		
		final String hql1 = "select specimen.specimenCollectionGroup.collectionProtocolRegistration."
							+ "collectionProtocol.id,specimen.specimenCollectionGroup.collectionProtocolRegistration.participant.id"
							+ " from edu.wustl.catissuecore.domain.Specimen as specimen where "
							+ "specimen.id=" +specimenid;
		 DAO dao = null;
		 List<Object> list = null;
		 
		try
		{
		 dao = AppUtility.openDAOSession(sessionData);
		 list =dao.executeQuery(hql1);
		}
		finally
		{
		 dao.closeSession();
		}
		return list;
	}



}

