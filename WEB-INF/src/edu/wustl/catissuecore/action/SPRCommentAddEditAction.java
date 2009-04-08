package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ViewSurgicalPathologyReportForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.DomainObjectFactory;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.QuarantineEventParameter;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;

public class SPRCommentAddEditAction extends BaseAction
{
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		DefaultBizLogic defaultBizLogic=new DefaultBizLogic();
        ViewSurgicalPathologyReportForm viewSurgicalPathologyReportForm=(ViewSurgicalPathologyReportForm)form;
        try 
        {
			AbstractDomainObject abstractDomain=(AbstractDomainObject)(new DomainObjectFactory().getDomainObject(viewSurgicalPathologyReportForm.getFormId(), viewSurgicalPathologyReportForm));
	    	abstractDomain = defaultBizLogic.populateDomainObject(abstractDomain.getClass().getName(), new Long(viewSurgicalPathologyReportForm.getId()), viewSurgicalPathologyReportForm);
	    	IBizLogic bizLogic= BizLogicFactory.getInstance().getBizLogic(viewSurgicalPathologyReportForm.getFormId());
			if(abstractDomain!=null)
			{
				Object object = bizLogic.retrieve(abstractDomain.getClass().getName(), new Long(viewSurgicalPathologyReportForm.getId()));
				AbstractDomainObject abstractDomainOld = (AbstractDomainObject) object;
				if(abstractDomainOld instanceof QuarantineEventParameter)
				{
					QuarantineEventParameter quarantineEventParamanter=(QuarantineEventParameter)abstractDomainOld;
					DeidentifiedSurgicalPathologyReport deidReport=(DeidentifiedSurgicalPathologyReport)bizLogic.retrieveAttribute(QuarantineEventParameter.class.getName(), quarantineEventParamanter.getId(), Constants.COLUMN_NAME_DEID_REPORT);
					quarantineEventParamanter.setDeIdentifiedSurgicalPathologyReport(deidReport);
				}
	            bizLogic.update(abstractDomain, abstractDomainOld, 0, getSessionData(request));
			}
        }
        catch (Exception ex) 
        {
			Logger.out.error("Error occured in SPRCommentAddEditAction "+ex);
			return (mapping.findForward(Constants.FAILURE));
		}
        // OnSubmit
        if(viewSurgicalPathologyReportForm.getOnSubmit()!= null && viewSurgicalPathologyReportForm.getOnSubmit().trim().length()>0  )
        {
        	String forwardTo = viewSurgicalPathologyReportForm.getOnSubmit();
            return (mapping.findForward(forwardTo));
        }
		return (mapping.findForward(Constants.SUCCESS));
	}
}
