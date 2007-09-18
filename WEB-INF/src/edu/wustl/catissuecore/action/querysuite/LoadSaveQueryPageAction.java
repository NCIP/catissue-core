
package edu.wustl.catissuecore.action.querysuite;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.bizlogic.querysuite.CreateUIFromQueryObjectBizLogic;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.querysuite.queryobject.IQuery;

public class LoadSaveQueryPageAction extends BaseAction
{
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		IQuery queryObject = (IQuery) request.getSession().getAttribute(
				AppletConstants.QUERY_OBJECT);
		CreateUIFromQueryObjectBizLogic createUIFromQueryObject = new CreateUIFromQueryObjectBizLogic();
		String htmlContents = createUIFromQueryObject.getHTMLForSavedQuery(queryObject);
		request.setAttribute("HTMLContents", htmlContents);

		return mapping.findForward("success");
	}

}
