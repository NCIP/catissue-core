package edu.wustl.catissuecore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;

public class RetrieveCollectionProtocolAction extends BaseAction {

	protected ActionForward executeAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String cpId = request.getParameter("CPID");
		if (cpId == null){
			return mapping.findForward(Constants.FAILURE);
		}
		Long id = new Long(cpId);
		CollectionProtocolBizLogic collectionProtocolBizLogic = 
			new CollectionProtocolBizLogic();
		CollectionProtocolUtil.updateSession(request, id);
		
		return mapping.findForward(Constants.SUCCESS);
	}


}
