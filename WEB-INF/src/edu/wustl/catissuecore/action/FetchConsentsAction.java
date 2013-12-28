package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.ConsentTrackingBizLogic;
import edu.wustl.catissuecore.bizlogic.IdentifiedSurgicalPathologyReportBizLogic;
import edu.wustl.catissuecore.dto.ConsentResponseDto;
import edu.wustl.catissuecore.util.ConsentUtil;
import edu.wustl.catissuecore.util.SprPrintPdfUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;

public class FetchConsentsAction  extends SecureAction {

	private transient final Logger logger = Logger
			.getCommonLogger(ExportCollectionProtocolAction.class);

	@Override
	/**
	 * Execute Secure Action.
	 */
	protected ActionForward executeSecureAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String mappingForward = null;
		SessionDataBean sessionDataBean = (SessionDataBean) request
					.getSession().getAttribute(Constants.SESSION_DATA);
		DAO dao=null;
		String consentLevel = request.getParameter("consentLevel");
		String consentLevelId = request.getParameter("consentLevelId");
		String cprId = request.getParameter("cprId")==null?"0":request.getParameter("cprId");
		try{
			dao = AppUtility.openDAOSession(sessionDataBean);
			List witnessList = new ArrayList();
			if(consentLevel.equals("participant")){
				Long cpId = ConsentUtil.getCpIdFromCprId(Long.parseLong(consentLevelId), dao);
				witnessList = ConsentUtil.witnessNameList(cpId.toString());
			}
			ConsentTrackingBizLogic bizLogic = new ConsentTrackingBizLogic();
			ConsentResponseDto consentsDto = bizLogic.getConsentList(consentLevel, Long.parseLong(consentLevelId),dao);
			request.setAttribute("consentsDto", consentsDto);
			request.setAttribute("consentLevelId",consentLevelId);
			request.setAttribute("consentLevel",consentLevel);
			request.setAttribute("witnessList", witnessList);
			request.setAttribute("datePattern",ApplicationProperties.getValue("date.pattern"));
			List specimenResponseList = new ArrayList();
			specimenResponseList = AppUtility.responceList(Constants.EDIT);
			request.setAttribute(Constants.SPECIMEN_RESPONSELIST, specimenResponseList);
			
			if(consentLevel.equals("scg")){
				request.setAttribute("entityId", request.getParameter("entityId"));
				request.setAttribute("staticEntityName", request.getParameter("staticEntityName"));
			}else if(consentLevel.equals("participant")){
				request.setAttribute("reportId", request.getParameter("reportId"));
				request.setAttribute("pageof", request.getParameter("pageof"));
				request.setAttribute("participantEntityId", request.getParameter("participantEntityId"));
				request.setAttribute("participantId",request.getParameter("participantId"));
				request.setAttribute("cpId",request.getParameter("cpId"));
				
				request.setAttribute("staticEntityName","edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry");
			}else if(consentLevel.equals("specimen")){
				request.setAttribute("reportId", request.getParameter("reportId"));
				request.setAttribute("pageof", request.getParameter("pageof"));
				request.setAttribute("staticEntityName", request.getParameter("staticEntityName"));
				request.setAttribute("entityId", request.getParameter("entityId"));
			
			}
			
			
		}/*catch(Exception ex){
			System.out.println("");
		}*/
		finally{
			AppUtility.closeDAOSession(dao);
		}
			return mapping.findForward("success");
	}

}
