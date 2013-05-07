
package edu.wustl.catissuecore.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CollectionProtocolForm;
import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.bizlogic.SynchronizeCollectionProtocolBizLogic;
import edu.wustl.catissuecore.cpSync.SyncCPThreadExecuterImpl;
import edu.wustl.catissuecore.domain.CpSyncAudit;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.global.CommonServiceLocator;

/**
 * Forward to collection protocol main page.
 * @author pathik_sheth
 *
 */
public class OpenCollectionProtocolAction extends BaseAction
{

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final CollectionProtocolForm formName = (CollectionProtocolForm) form;
		final String operation = request.getParameter(Constants.OPERATION);
		final String pageOf = request.getParameter(Constants.PAGE_OF);
		final HttpSession session = request.getSession();
		if ("pageOfmainCP".equalsIgnoreCase(pageOf))
		{
			session.removeAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
			session.removeAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP);
		}
		final CollectionProtocolBean cpBean = (CollectionProtocolBean) session
				.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
		if (cpBean != null)
		{
			request.setAttribute("isParticipantReg", cpBean.isParticiapantReg());
			request.setAttribute(Constants.OPERATION, operation);
			final String treeNode = "cpName_" + cpBean.getTitle();
			session.setAttribute(Constants.TREE_NODE_ID, treeNode);
			SyncCPThreadExecuterImpl executerImpl = SyncCPThreadExecuterImpl.getInstance();
			boolean isSyncOn = executerImpl.isSyncOn(cpBean.getTitle());
			request.setAttribute("isSyncOn", isSyncOn);
			if(Constants.EDIT.equalsIgnoreCase(operation))
			{
				SynchronizeCollectionProtocolBizLogic syncBizlogic=new  SynchronizeCollectionProtocolBizLogic();
				CpSyncAudit cpSyncAudit=syncBizlogic.getSyncStatus(cpBean.getIdentifier());
				StringBuffer synchMessage=new StringBuffer();
				if(cpSyncAudit!=null)
				{		
					request.setAttribute("displaySynchMessage", Constants.TRUE);
					if("In Process".equalsIgnoreCase(cpSyncAudit.getStatus()))
					{
						synchMessage.append("Edit is disabled  since syncronization is in process.").append("(").append(cpSyncAudit.getProcessedCPRCount()).append("participants synchronized so far.)");
					}
					else
					{
						Date endDate=cpSyncAudit.getEndDate();
						String DATE_FORMAT_NOW = CommonServiceLocator.getInstance()
								.getDatePattern();
						SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);						
						if("Done".equalsIgnoreCase(cpSyncAudit.getStatus()))
						{
							synchMessage.append("Protocol synchronization last done on ").append(sdf.format(endDate)).append(".");
						}
						else
						{
							synchMessage.append("Protocol synchronization was aborted at ").append(sdf.format(endDate)).append(".Please contact your Administrator.");
						}
					}
					
					request.setAttribute("synchMessage", synchMessage.toString());
				}	
			}
		}
		
		
		request.setAttribute("formName", formName);
		//bug 18481
		request.setAttribute(Constants.ERROR_PAGE_FOR_CP, request.getParameter(Constants.ERROR_PAGE_FOR_CP));
		//		request.setAttribute("labelGen", labelGen);
		//		System.out.println("formName   &&****#$#$#$#$$##$#$   :  "+ cpBean.isGenerateLabel());
		return mapping.findForward(Constants.SUCCESS);
	}

}
