package edu.wustl.catissuecore.gridgrouper;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.action.BaseAddEditAction;
import edu.wustl.common.exception.ApplicationException;

public class GridGroupSyncAction extends BaseAddEditAction {

	@Override
	public ActionForward executeXSS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ApplicationException {
		
		System.out.println("********Sync ... START");
		
		GridGroupSync sync = new GridGroupSync();
		try {
			sync.syncAllMigratedUsers();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			response.flushBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("********Sync ... END");
		return null;
	}

}
