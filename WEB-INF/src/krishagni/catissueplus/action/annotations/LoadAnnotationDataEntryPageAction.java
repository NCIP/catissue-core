package krishagni.catissueplus.action.annotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;

public class LoadAnnotationDataEntryPageAction extends BaseAction {
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
	HttpServletResponse response) throws Exception {
		
		String entityType = request.getParameter("staticEntityName");
		String entityRecId = request.getParameter("entityRecordId");
		
		request.setAttribute("entityType", entityType);
		request.setAttribute("entityRecId", entityRecId);
		return mapping.findForward(Constants.SUCCESS);
	}
}
