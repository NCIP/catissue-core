package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;

public class CheckMigrationAction extends BaseAction{

	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		List idps=(ArrayList) request.getAttribute("migrationIdps");
		
		if(idps.size()==0)
		{
			return mapping.findForward(Constants.HOME);
		}
		
		return mapping.findForward(Constants.SUCCESS);
	}


}
