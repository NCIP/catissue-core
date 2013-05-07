/**
 * <p>
 * Title: SpecimenCollectionGroupAction Class>
 * <p>
 * Description: SpecimenCollectionGroupAction initializes the fields in the New
 * Specimen Collection Group page.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Ajay Sharma
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.SynchronizeCollectionProtocolBizLogic;
import edu.wustl.catissuecore.domain.CpSyncAudit;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.util.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * SpecimenCollectionGroupAction initializes the fields in the New Specimen
 * Collection Group page.
 *
 * @author ajay_sharma
 */
public class CheckSyncCPAndForwardAction extends SecureAction
{

	/** logger. */
	private static final Logger LOGGER = Logger
			.getCommonLogger(SpecimenCollectionGroupAction.class);

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 *
	 * @return ActionForward : ActionForward
	 *
	 * @throws Exception : Exception
	 */
	public ActionForward executeSecureAction(ActionMapping mapping, final ActionForm form,
			final HttpServletRequest request, final HttpServletResponse response) throws Exception
	{
		SynchronizeCollectionProtocolBizLogic syncBizlogic=new SynchronizeCollectionProtocolBizLogic();
		if(request.getParameter("cpSearchCpId")!=null && !"".equals(request.getParameter("cpSearchCpId")))
		{
			CpSyncAudit cpSyncAudit=syncBizlogic.getSyncStatus(new Long(request.getParameter("cpSearchCpId")));
			if(cpSyncAudit!=null)
			{
			   if("In Process".equalsIgnoreCase(cpSyncAudit.getStatus()))	
				   request.setAttribute("isSyncOn", true);
			   else
				   request.setAttribute("isSyncOn", false);
			}
		}
		return mapping.findForward(Constants.SUCCESS);
	}

}