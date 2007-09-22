package edu.wustl.catissuecore.action.querysuite;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.bizlogic.querysuite.CreateUIFromQueryObjectBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.querysuite.queryobject.IQuery;

public class LoadSaveQueryPageAction extends BaseAction {
	@Override
	protected ActionForward executeAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		IQuery queryObject = (IQuery) request.getSession().getAttribute(
				AppletConstants.QUERY_OBJECT);
		boolean isShowAll = request.getParameter("showall") == null ? false
				: true;
		CreateUIFromQueryObjectBizLogic createUIFromQueryObject = new CreateUIFromQueryObjectBizLogic();
		String htmlContents = createUIFromQueryObject.getHTMLForSavedQuery(
				queryObject, isShowAll,Constants.SAVE_QUERY_PAGE);
		request.setAttribute("HTMLContents", htmlContents);
		String showAllLink = isShowAll ? Constants.SHOW_SELECTED_ATTRIBUTE
				: Constants.SHOW_ALL_ATTRIBUTE;
		request.setAttribute("showAllLink", showAllLink);
		if (!isShowAll)
			request.setAttribute("showall", "showall=true");
		return mapping.findForward("success");

	}

}
